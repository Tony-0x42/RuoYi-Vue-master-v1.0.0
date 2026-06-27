<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item :label="$t('oa.expense.year')" prop="year">
        <el-input-number v-model="queryParams.year" :min="2000" :max="2100" clearable />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">{{ $t('common.search') }}</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{ $t('common.reset') }}</el-button>
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
          v-hasPermi="['oa:expenseBudget:add']"
        >{{ $t('common.add') }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['oa:expenseBudget:edit']"
        >{{ $t('common.edit') }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['oa:expenseBudget:remove']"
        >{{ $t('common.delete') }}</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="budgetList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <index-column :page="queryParams.pageNum" :size="queryParams.pageSize" />
      <el-table-column :label="$t('oa.expense.deptId')" align="center" prop="deptId" width="120" />
      <el-table-column :label="$t('oa.expense.projectId')" align="center" prop="projectId" width="120" />
      <el-table-column :label="$t('oa.expense.itemId')" align="center" prop="itemId" width="120" />
      <el-table-column :label="$t('oa.expense.year')" align="center" prop="year" width="100" />
      <el-table-column :label="$t('oa.expense.totalAmount')" align="center" prop="totalAmount" width="120" />
      <el-table-column :label="$t('oa.expense.usedAmount')" align="center" prop="usedAmount" width="120" />
      <el-table-column :label="$t('oa.expense.executionRate')" align="center" width="120">
        <template slot-scope="scope">
          <span>{{ calculateRate(scope.row) }}%</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('common.status')" align="center" prop="status" width="100">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === 1" type="success">{{ $t('common.enable') }}</el-tag>
          <el-tag v-else type="info">{{ $t('common.disable') }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="$t('common.operation')" align="center" class-name="small-padding fixed-width" width="180">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['oa:expenseBudget:edit']"
          >{{ $t('common.edit') }}</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['oa:expenseBudget:remove']"
          >{{ $t('common.delete') }}</el-button>
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

    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item :label="$t('oa.expense.deptId')" prop="deptId">
              <el-input-number v-model="form.deptId" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('oa.expense.projectId')" prop="projectId">
              <el-input-number v-model="form.projectId" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('oa.expense.itemId')" prop="itemId">
              <el-input-number v-model="form.itemId" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('oa.expense.year')" prop="year">
              <el-input-number v-model="form.year" :min="2000" :max="2100" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('oa.expense.totalAmount')" prop="totalAmount">
              <el-input-number v-model="form.totalAmount" :min="0" :precision="2" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('oa.expense.usedAmount')" prop="usedAmount">
              <el-input-number v-model="form.usedAmount" :min="0" :precision="2" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item :label="$t('common.status')" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">{{ $t('common.enable') }}</el-radio>
            <el-radio :label="0">{{ $t('common.disable') }}</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">{{ $t('common.submit') }}</el-button>
        <el-button @click="cancel">{{ $t('common.cancel') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listBudget, getBudget, addBudget, updateBudget, delBudget } from "@/api/oa/expense"

export default {
  name: "OaExpenseBudget",
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      budgetList: [],
      title: "",
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        year: new Date().getFullYear()
      },
      form: {},
      rules: {
        year: [
          { required: true, message: this.$t('oa.expense.required.year'), trigger: "blur" }
        ],
        totalAmount: [
          { required: true, message: this.$t('oa.expense.required.totalAmount'), trigger: "blur" }
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
      listBudget(this.queryParams).then(response => {
        this.budgetList = response.rows
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
        id: undefined,
        deptId: undefined,
        projectId: undefined,
        itemId: undefined,
        year: new Date().getFullYear(),
        totalAmount: 0,
        usedAmount: 0,
        status: 1
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
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = this.$t('oa.expense.addBudget')
    },
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getBudget(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = this.$t('oa.expense.editBudget')
      })
    },
    calculateRate(row) {
      if (!row.totalAmount || row.totalAmount === 0) return 0
      const used = row.usedAmount || 0
      return Math.round(used * 100 / row.totalAmount)
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          const data = { ...this.form }
          if (data.id != undefined) {
            updateBudget(data).then(() => {
              this.$modal.msgSuccess(this.$t('common.editSuccess'))
              this.open = false
              this.getList()
            })
          } else {
            addBudget(data).then(() => {
              this.$modal.msgSuccess(this.$t('common.addSuccess'))
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm(this.$t('oa.expense.confirm.deleteBudget', { ids })).then(function() {
        return delBudget(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess(this.$t('common.deleteSuccess'))
      }).catch(() => {})
    }
  }
}
</script>
