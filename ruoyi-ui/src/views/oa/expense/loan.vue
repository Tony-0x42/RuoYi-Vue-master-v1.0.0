<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item :label="$t('oa.expense.userName')" prop="userName">
        <el-input
          v-model="queryParams.userName"
          :placeholder="$t('oa.expense.placeholder.userName')"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="$t('common.status')" prop="status">
        <el-select v-model="queryParams.status" :placeholder="$t('common.status')" clearable>
          <el-option :label="$t('oa.expense.loanDraft')" :value="0" />
          <el-option :label="$t('oa.expense.loanApproving')" :value="1" />
          <el-option :label="$t('oa.expense.loanPassed')" :value="2" />
          <el-option :label="$t('oa.expense.loanRejected')" :value="3" />
          <el-option :label="$t('oa.expense.loanCleared')" :value="9" />
        </el-select>
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
          v-hasPermi="['oa:expenseLoan:add']"
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
          v-hasPermi="['oa:expenseLoan:edit']"
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
          v-hasPermi="['oa:expenseLoan:remove']"
        >{{ $t('common.delete') }}</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="loanList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <index-column :page="queryParams.pageNum" :size="queryParams.pageSize" />
      <el-table-column :label="$t('oa.expense.userName')" align="center" prop="userName" width="120" />
      <el-table-column :label="$t('oa.expense.amount')" align="center" prop="amount" width="120" />
      <el-table-column :label="$t('oa.expense.repaidAmount')" align="center" prop="repaidAmount" width="120" />
      <el-table-column :label="$t('oa.expense.reason')" align="center" prop="reason" :show-overflow-tooltip="true" />
      <el-table-column :label="$t('oa.expense.dueDate')" align="center" prop="dueDate" width="120">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.dueDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('common.status')" align="center" prop="status" width="120">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === 0" type="warning">{{ $t('oa.expense.loanDraft') }}</el-tag>
          <el-tag v-else-if="scope.row.status === 1" type="primary">{{ $t('oa.expense.loanApproving') }}</el-tag>
          <el-tag v-else-if="scope.row.status === 2" type="success">{{ $t('oa.expense.loanPassed') }}</el-tag>
          <el-tag v-else-if="scope.row.status === 3" type="info">{{ $t('oa.expense.loanRejected') }}</el-tag>
          <el-tag v-else type="info">{{ $t('oa.expense.loanCleared') }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="$t('common.operation')" align="center" class-name="small-padding fixed-width" width="220">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-s-promotion"
            @click="handleSubmit(scope.row)"
            v-hasPermi="['oa:expenseLoan:edit']"
            v-if="scope.row.status === 0"
          >{{ $t('oa.expense.loanSubmit') }}</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-money"
            @click="handleRepayment(scope.row)"
            v-hasPermi="['oa:expenseLoan:edit']"
            v-if="scope.row.status === 2"
          >{{ $t('oa.expense.repayment') }}</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['oa:expenseLoan:edit']"
          >{{ $t('common.edit') }}</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['oa:expenseLoan:remove']"
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

    <el-dialog :title="$t('oa.expense.repayment')" :visible.sync="repaymentOpen" width="500px" append-to-body>
      <el-form ref="repaymentForm" :model="repaymentForm" :rules="repaymentRules" label-width="100px">
        <el-form-item :label="$t('oa.expense.repaymentAmount')" prop="amount">
          <el-input-number v-model="repaymentForm.amount" :min="0" :precision="2" style="width:100%" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitRepayment">{{ $t('common.submit') }}</el-button>
        <el-button @click="repaymentOpen = false">{{ $t('common.cancel') }}</el-button>
      </div>
    </el-dialog>

    <flow-submit-dialog
      :visible="flowVisible"
      process-key="oa_expense_loan"
      :business-key="'expense_loan:' + currentLoanId"
      :submit-api="submitApi"
      @close="flowVisible = false"
      @success="handleSubmitSuccess"
    />
  </div>
</template>

<script>
import FlowSubmitDialog from "@/components/FlowSubmitDialog"
import { listLoan, delLoan, repaymentLoan, submitLoan } from "@/api/oa/expense"

export default {
  name: "OaExpenseLoan",
  components: {
    FlowSubmitDialog
  },
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      loanList: [],
      flowVisible: false,
      currentLoanId: undefined,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userName: undefined,
        status: undefined
      },
      repaymentForm: {
        amount: 0
      },
      repaymentRules: {
        amount: [
          { required: true, message: this.$t('oa.expense.required.repaymentAmount'), trigger: "blur" }
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
      listLoan(this.queryParams).then(response => {
        this.loanList = response.rows
        this.total = response.total
        this.loading = false
      })
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
      this.$router.push('/oa/expenseLoan/form?mode=add')
    },
    handleUpdate(row) {
      const id = row ? row.id : this.ids[0]
      this.$router.push('/oa/expenseLoan/form?mode=edit&id=' + id)
    },
    handleRepayment(row) {
      this.currentLoanId = row.id
      this.repaymentForm = { amount: 0 }
      this.repaymentOpen = true
    },
    handleSubmit(row) {
      this.currentLoanId = row.id
      this.flowVisible = true
    },
    submitApi(data) {
      return submitLoan(this.currentLoanId, data)
    },
    handleSubmitSuccess() {
      this.flowVisible = false
      this.getList()
    },
    submitRepayment() {
      this.$refs["repaymentForm"].validate(valid => {
        if (valid) {
          repaymentLoan(this.currentLoanId, this.repaymentForm).then(() => {
            this.$modal.msgSuccess(this.$t('oa.expense.repaymentSuccess'))
            this.repaymentOpen = false
            this.getList()
          })
        }
      })
    },
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm(this.$t('oa.expense.confirm.deleteLoan', { ids })).then(function() {
        return delLoan(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess(this.$t('common.deleteSuccess'))
      }).catch(() => {})
    }
  }
}
</script>
