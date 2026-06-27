<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="流程标题" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="请输入流程标题"
          clearable
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="流程定义" prop="definitionName">
        <el-input
          v-model="queryParams.definitionName"
          placeholder="请输入流程定义名称"
          clearable
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="状态" clearable style="width: 240px">
          <el-option label="运行中" value="0" />
          <el-option label="已完成" value="1" />
          <el-option label="已拒绝" value="2" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleStart"
        >发起流程</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="instanceList">
      <index-column :page="queryParams.pageNum" :size="queryParams.pageSize" />
      <el-table-column label="流程标题" align="center" prop="title" :show-overflow-tooltip="true" />
      <el-table-column label="流程定义" align="center" prop="definitionName" />
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <span>{{ formatStatus(scope.row.status) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="当前节点" align="center" prop="currentNodeName" />
      <el-table-column label="开始时间" align="center" prop="startTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.startTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="结束时间" align="center" prop="endTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.endTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-picture-outline"
            @click="handleTrace(scope.row)"
          >流程图</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['bpm:instance:remove']"
          >删除</el-button>
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

    <!-- 发起流程对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="选择流程" prop="definitionId">
          <el-select v-model="form.definitionId" placeholder="请选择已发布的流程" style="width: 100%" @change="handleDefinitionChange">
            <el-option
              v-for="item in definitionOptions"
              :key="item.definitionId"
              :label="item.definitionName + '（' + item.definitionCode + '）'"
              :value="item.definitionId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="流程标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入流程标题" />
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
  </div>
</template>

<script>
import { listInstance, delInstance } from "@/api/bpm/instance"
import { listDefinition } from "@/api/bpm/definition"
import { listVariableByDefinition } from "@/api/bpm/variable"
import { startProcess } from "@/api/bpm/runtime"

export default {
  name: "BpmInstance",

  data() {
    return {
      loading: true,
      showSearch: true,
      total: 0,
      instanceList: [],
      definitionOptions: [],
      title: "",
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        title: undefined,
        definitionName: undefined,
        status: undefined
      },
      form: {
        definitionId: undefined,
        title: undefined,
        variableDefinitions: [],
        variables: []
      },
      rules: {
        definitionId: [
          { required: true, message: "请选择流程", trigger: "change" }
        ],
        title: [
          { required: true, message: "流程标题不能为空", trigger: "blur" }
        ]
      }
    }
  },
  created() {
    this.loadDefinitions()
    this.getList()
  },
  methods: {
    loadDefinitions() {
      listDefinition({ pageSize: 9999, status: "1" }).then(response => {
        this.definitionOptions = response.rows
      })
    },
    getList() {
      this.loading = true
      listInstance(this.queryParams).then(response => {
        this.instanceList = response.rows
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
        definitionId: undefined,
        title: undefined,
        variableDefinitions: [],
        variables: []
      }
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
    handleStart() {
      this.reset()
      this.open = true
      this.title = "发起流程"
    },
    handleDefinitionChange(definitionId) {
      this.form.variableDefinitions = []
      this.form.variables = []
      if (!definitionId) {
        return
      }
      listVariableByDefinition(definitionId).then(response => {
        this.form.variableDefinitions = (response.data || []).map(item => ({
          variableId: item.variableId,
          variableCode: item.variableCode,
          variableName: item.variableName,
          variableType: item.variableType,
          defaultValue: item.defaultValue,
          variableValue: item.defaultValue
        }))
      })
    },
    addVariable() {
      this.form.variables.push({ variableCode: "", variableValue: "" })
    },
    removeVariable(index) {
      this.form.variables.splice(index, 1)
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          const variables = {}
          // 1. 放入有效变量定义的值
          this.form.variableDefinitions.forEach(item => {
            if (item.variableCode) {
              variables[item.variableCode] = item.variableValue
            }
          })
          // 2. 扩展变量可覆盖或补充
          this.form.variables.forEach(item => {
            if (item.variableCode) {
              variables[item.variableCode] = item.variableValue
            }
          })
          startProcess(this.form.definitionId, { title: this.form.title, variables: variables }).then(() => {
            this.$modal.msgSuccess("发起成功")
            this.open = false
            this.getList()
          })
        }
      })
    },
    handleDelete(row) {
      this.$modal.confirm('是否确认删除流程实例编号为"' + row.instanceId + '"的数据项？').then(function() {
        return delInstance(row.instanceId)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    formatStatus(status) {
      const map = { '0': '运行中', '1': '已完成', '2': '已拒绝' }
      return map[status] || status
    },
    handleTrace(row) {
      if (!row.processInstanceId) {
        this.$modal.msgError('该实例未绑定 Flowable 流程实例')
        return
      }
      this.$router.push('/bpm/trace?processInstanceId=' + row.processInstanceId)
    }
  }
}
</script>
