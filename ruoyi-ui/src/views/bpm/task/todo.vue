<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="流程标题" prop="instanceTitle">
        <el-input
          v-model="queryParams.instanceTitle"
          placeholder="请输入流程标题"
          clearable
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="节点名称" prop="nodeName">
        <el-input
          v-model="queryParams.nodeName"
          placeholder="请输入节点名称"
          clearable
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="taskList">
      <index-column :page="queryParams.pageNum" :size="queryParams.pageSize" />
      <el-table-column label="流程标题" align="center" prop="instanceTitle" :show-overflow-tooltip="true" />
      <el-table-column label="节点名称" align="center" prop="nodeName" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="280">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-check"
            @click="handleSubmit(scope.row)"
          >办理</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-back"
            @click="handleReturn(scope.row)"
          >退回</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-close"
            @click="handleReject(scope.row)"
          >拒绝</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-s-check"
            @click="handleClaim(scope.row)"
          >签收</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-user"
            @click="handleAssign(scope.row)"
            v-hasPermi="['bpm:task:assign']"
          >指派</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-s-promotion"
            @click="handleDelegate(scope.row)"
            v-hasPermi="['bpm:task:delegate']"
          >特送</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 办理任务对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="审批意见" prop="comment">
          <el-input v-model="form.comment" type="textarea" placeholder="请输入审批意见" />
        </el-form-item>
        <el-form-item label="流程变量" v-if="form.variableDefinitions && form.variableDefinitions.length > 0">
          <div v-for="(item, index) in form.variableDefinitions" :key="index" style="margin-bottom: 10px;">
            <el-row :gutter="10">
              <el-col :span="8">
                <el-input v-model="item.variableCode" placeholder="变量编码" disabled />
              </el-col>
              <el-col :span="16">
                <el-input v-if="item.variableType == 0" v-model="item.variableValue" :placeholder="'默认值: ' + (item.defaultValue || '-')" />
                <el-input v-else-if="item.variableType == 1" v-model="item.variableValue" type="number" :placeholder="'默认值: ' + (item.defaultValue || '-')" />
                <el-select v-else-if="item.variableType == 2" v-model="item.variableValue" placeholder="请选择" style="width: 100%">
                  <el-option label="是" value="true" />
                  <el-option label="否" value="false" />
                </el-select>
                <el-date-picker v-else-if="item.variableType == 3" v-model="item.variableValue" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择日期时间" style="width: 100%" />
              </el-col>
            </el-row>
          </div>
        </el-form-item>
        <el-form-item label="扩展变量">
          <div v-for="(item, index) in form.variables" :key="'ext-' + index" style="margin-bottom: 10px;">
            <el-row :gutter="10">
              <el-col :span="10">
                <el-input v-model="item.variableCode" placeholder="变量编码" />
              </el-col>
              <el-col :span="10">
                <el-input v-model="item.variableValue" placeholder="变量值" />
              </el-col>
              <el-col :span="4">
                <el-button type="danger" icon="el-icon-delete" circle size="mini" @click="removeVariable(index)"></el-button>
              </el-col>
            </el-row>
          </div>
          <el-button type="primary" plain size="mini" icon="el-icon-plus" @click="addVariable">添加变量</el-button>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 退回/拒绝对话框 -->
    <el-dialog :title="actionTitle" :visible.sync="actionOpen" width="500px" append-to-body>
      <el-form ref="actionForm" :model="actionForm" :rules="actionRules" label-width="80px">
        <el-form-item label="审批意见" prop="comment">
          <el-input v-model="actionForm.comment" type="textarea" placeholder="请输入审批意见" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitAction">确 定</el-button>
        <el-button @click="actionOpen = false">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 任务指派/特送用户选择对话框 -->
    <UserSelectDialog
      :visible.sync="assignDialogVisible"
      :value="assignUserId"
      :title="assignTitle"
      @input="assignUserId = $event"
      @change="handleAssignConfirm"
    />
  </div>
</template>

<script>
import { todoList } from "@/api/bpm/task"
import { listVariableByDefinition } from "@/api/bpm/variable"
import { submitTask, returnTask, rejectTask, claimTask, assignTask, delegateTask } from "@/api/bpm/runtime"
import UserSelectDialog from "@/components/UserSelectDialog"

export default {
  name: "BpmTaskTodo",
  components: { UserSelectDialog },
  data() {
    return {
      loading: true,
      showSearch: true,
      total: 0,
      taskList: [],
      title: "",
      open: false,
      actionTitle: "",
      actionOpen: false,
      currentTaskId: undefined,
      currentDefinitionId: undefined,
      actionType: undefined,
      assignDialogVisible: false,
      assignTitle: "",
      assignAction: "",
      assignRow: null,
      assignUserId: "",
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        instanceTitle: undefined,
        nodeName: undefined
      },
      form: {
        comment: undefined,
        variableDefinitions: [],
        variables: []
      },
      rules: {
        comment: [
          { required: true, message: "审批意见不能为空", trigger: "blur" }
        ]
      },
      actionForm: {
        comment: undefined
      },
      actionRules: {
        comment: [
          { required: true, message: "审批意见不能为空", trigger: "blur" }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      todoList(this.queryParams).then(response => {
        this.taskList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = {
        comment: undefined,
        variableDefinitions: [],
        variables: []
      }
      this.currentDefinitionId = undefined
      this.resetForm("form")
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.resetForm("queryForm")
      this.handleQuery()
    },
    addVariable() {
      this.form.variables.push({ variableCode: "", variableValue: "" })
    },
    removeVariable(index) {
      this.form.variables.splice(index, 1)
    },
    handleSubmit(row) {
      const businessKey = row.businessKey
      if (businessKey) {
        const separatorIndex = businessKey.indexOf(':')
        if (separatorIndex > 0) {
          const prefix = businessKey.substring(0, separatorIndex)
          const id = businessKey.substring(separatorIndex + 1)
          if (prefix === 'expense_report') {
            this.$router.push({ path: '/oa/expense', query: { id } })
            return
          } else if (prefix === 'expense_loan') {
            this.$router.push({ path: '/oa/expenseLoan', query: { id } })
            return
          } else if (prefix === 'asset_receive') {
            this.$router.push({ path: '/oa/asset', query: { id } })
            return
          } else if (prefix === 'attendance_leave') {
            this.$router.push({ path: '/oa/hr/leave', query: { id } })
            return
          } else if (prefix === 'attendance_overtime') {
            this.$router.push({ path: '/oa/hr/overtime', query: { id } })
            return
          } else if (prefix === 'attendance_trip') {
            this.$router.push({ path: '/oa/hr/trip', query: { id } })
            return
          } else if (prefix === 'attendance_makeup') {
            this.$router.push({ path: '/oa/hr/makeup', query: { id } })
            return
          }
        }
        this.$modal.msgInfo('请在业务页面处理')
        return
      }
      this.reset()
      this.currentTaskId = row.flowableTaskId
      this.currentDefinitionId = row.definitionId
      this.open = true
      this.title = "办理任务"
      if (row.definitionId) {
        listVariableByDefinition(row.definitionId).then(response => {
          this.form.variableDefinitions = (response.data || []).map(item => ({
            variableId: item.variableId,
            variableCode: item.variableCode,
            variableName: item.variableName,
            variableType: item.variableType,
            defaultValue: item.defaultValue,
            variableValue: item.defaultValue
          }))
        })
      }
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          const variables = {}
          this.form.variableDefinitions.forEach(item => {
            if (item.variableCode) {
              variables[item.variableCode] = item.variableValue
            }
          })
          this.form.variables.forEach(item => {
            if (item.variableCode) {
              variables[item.variableCode] = item.variableValue
            }
          })
          submitTask(this.currentTaskId, { comment: this.form.comment, variables: variables }).then(() => {
            this.$modal.msgSuccess("提交成功")
            this.open = false
            this.getList()
          })
        }
      })
    },
    handleReturn(row) {
      this.actionType = "return"
      this.currentTaskId = row.flowableTaskId
      this.actionTitle = "退回任务"
      this.actionForm = { comment: undefined }
      this.actionOpen = true
    },
    handleReject(row) {
      this.actionType = "reject"
      this.currentTaskId = row.flowableTaskId
      this.actionTitle = "拒绝任务"
      this.actionForm = { comment: undefined }
      this.actionOpen = true
    },
    submitAction() {
      this.$refs["actionForm"].validate(valid => {
        if (valid) {
          const request = this.actionType === "return" ? returnTask : rejectTask
          request(this.currentTaskId, this.actionForm.comment).then(() => {
            this.$modal.msgSuccess(this.actionType === "return" ? "退回成功" : "拒绝成功")
            this.actionOpen = false
            this.getList()
          })
        }
      })
    },
    handleClaim(row) {
      this.$modal.confirm('是否确认签收任务"' + row.flowableTaskId + '"？').then(function() {
        return claimTask(row.flowableTaskId)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("签收成功")
      }).catch(() => {})
    },
    handleAssign(row) {
      this.assignRow = row
      this.assignTitle = "任务指派"
      this.assignAction = "assign"
      this.assignUserId = ""
      this.assignDialogVisible = true
    },
    handleDelegate(row) {
      this.assignRow = row
      this.assignTitle = "任务特送"
      this.assignAction = "delegate"
      this.assignUserId = ""
      this.assignDialogVisible = true
    },
    handleAssignConfirm() {
      if (!this.assignUserId) {
        this.$modal.msgError("请选择用户")
        return
      }
      const taskId = this.assignRow.flowableTaskId
      const action = this.assignAction === "assign"
        ? assignTask(taskId, this.assignUserId)
        : delegateTask(taskId, this.assignUserId)
      action.then(() => {
        this.$modal.msgSuccess(this.assignAction === "assign" ? "指派成功" : "特送成功")
        this.assignDialogVisible = false
        this.getList()
      }).catch(() => {})
    }
  }
}
</script>
