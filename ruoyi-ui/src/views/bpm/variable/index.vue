<template>
  <div class="app-container">
    <el-alert
      :title="ownerTitle"
      type="info"
      :closable="false"
      style="margin-bottom: 15px;"
    />
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="变量名称" prop="variableName">
        <el-input
          v-model="queryParams.variableName"
          placeholder="请输入变量名称"
          clearable
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="变量编码" prop="variableCode">
        <el-input
          v-model="queryParams.variableCode"
          placeholder="请输入变量编码"
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
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['bpm:variable:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['bpm:variable:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['bpm:variable:remove']"
        >删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="variableList" @selection-change="handleSelectionChange">
      <index-column :page="queryParams.pageNum" :size="queryParams.pageSize" />
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="变量名称" align="center" prop="variableName" :show-overflow-tooltip="true" />
      <el-table-column label="变量编码" align="center" prop="variableCode" :show-overflow-tooltip="true" />
      <el-table-column label="变量类型" align="center" prop="variableType">
        <template slot-scope="scope">
          <span>{{ variableTypeFormat(scope.row.variableType) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="默认值" align="center" prop="defaultValue" :show-overflow-tooltip="true" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['bpm:variable:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['bpm:variable:remove']"
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

    <!-- 添加或修改变量定义对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="变量名称" prop="variableName">
          <el-input v-model="form.variableName" placeholder="请输入变量名称" />
        </el-form-item>
        <el-form-item label="变量编码" prop="variableCode">
          <el-input v-model="form.variableCode" placeholder="请输入变量编码" />
        </el-form-item>
        <el-form-item label="变量类型" prop="variableType">
          <el-select v-model="form.variableType" placeholder="请选择变量类型" style="width: 100%">
            <el-option
              v-for="item in variableTypeOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="默认值" prop="defaultValue">
          <el-input v-model="form.defaultValue" placeholder="请输入默认值" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" />
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
import { listVariable, listVariableByCategory, listVariableByDefinition, getVariable, delVariable, addVariable, updateVariable } from "@/api/bpm/variable"
import { getDefinition } from "@/api/bpm/definition"
import { getCategory } from "@/api/bpm/category"

export default {
  name: "BpmVariable",
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      variableList: [],
      categoryId: undefined,
      categoryName: "",
      definitionId: undefined,
      definitionName: "",
      title: "",
      open: false,
      variableTypeOptions: [
        { value: 0, label: "字符串" },
        { value: 1, label: "数字" },
        { value: 2, label: "布尔" },
        { value: 3, label: "日期" }
      ],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        categoryId: undefined,
        definitionId: undefined,
        variableName: undefined,
        variableCode: undefined
      },
      form: {},
      rules: {
        variableName: [
          { required: true, message: "变量名称不能为空", trigger: "blur" }
        ],
        variableCode: [
          { required: true, message: "变量编码不能为空", trigger: "blur" }
        ],
        variableType: [
          { required: true, message: "变量类型不能为空", trigger: "change" }
        ]
      }
    }
  },
  computed: {
    ownerTitle() {
      if (this.categoryId) {
        return '当前流程分类：' + (this.categoryName || '-')
      }
      if (this.definitionId) {
        return '当前流程定义：' + (this.definitionName || '-')
      }
      return '未指定变量所属对象'
    }
  },
  created() {
    this.categoryId = this.$route.query.categoryId ? Number(this.$route.query.categoryId) : undefined
    this.definitionId = this.$route.query.definitionId ? Number(this.$route.query.definitionId) : undefined
    this.queryParams.categoryId = this.categoryId
    this.queryParams.definitionId = this.definitionId
    if (this.categoryId) {
      getCategory(this.categoryId).then(response => {
        this.categoryName = response.data.categoryName
      })
    } else if (this.definitionId) {
      getDefinition(this.definitionId).then(response => {
        this.definitionName = response.data.definitionName
      })
    }
    this.getList()
  },
  methods: {
    variableTypeFormat(value) {
      const item = this.variableTypeOptions.find(o => o.value == value)
      return item ? item.label : value
    },
    getList() {
      this.loading = true
      if (this.categoryId) {
        listVariableByCategory(this.categoryId).then(response => {
          this.variableList = response.data
          this.total = response.data.length
          this.loading = false
        })
      } else if (this.definitionId) {
        listVariable(this.queryParams).then(response => {
          this.variableList = response.rows
          this.total = response.total
          this.loading = false
        })
      } else {
        listVariable(this.queryParams).then(response => {
          this.variableList = response.rows
          this.total = response.total
          this.loading = false
        })
      }
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = {
        variableId: undefined,
        categoryId: this.categoryId,
        definitionId: this.definitionId,
        variableName: undefined,
        variableCode: undefined,
        variableType: 0,
        defaultValue: undefined,
        remark: undefined
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
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加变量定义"
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.variableId)
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    handleUpdate(row) {
      this.reset()
      const variableId = row.variableId || this.ids
      getVariable(variableId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改变量定义"
      })
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.variableId != undefined) {
            updateVariable(this.form).then(() => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addVariable(this.form).then(() => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    handleDelete(row) {
      const variableIds = row.variableId || this.ids
      this.$modal.confirm('是否确认删除变量定义编号为"' + variableIds + '"的数据项？').then(function() {
        return delVariable(variableIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    }
  }
}
</script>
