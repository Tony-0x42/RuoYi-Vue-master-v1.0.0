<template>
  <div class="app-container">
    <el-form ref="form" :model="form" :rules="rules" label-width="100px">
      <el-row>
        <el-col :span="12">
          <el-form-item :label="$t('oa.expense.userName')" prop="userName">
            <el-input v-model="form.userName" :placeholder="$t('oa.expense.placeholder.userName')" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('oa.expense.category')" prop="categoryId">
            <el-select v-model="form.categoryId" :placeholder="$t('oa.expense.placeholder.category')" style="width:100%">
              <el-option
                v-for="item in categoryList"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item :label="$t('oa.expense.reason')" prop="reason">
            <el-input v-model="form.reason" type="textarea" :placeholder="$t('oa.expense.placeholder.reason')" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item :label="$t('oa.expense.loan')" prop="loanId">
            <el-select v-model="form.loanId" :placeholder="$t('oa.expense.placeholder.loan')" clearable style="width:50%">
              <el-option
                v-for="item in availableLoanList"
                :key="item.id"
                :label="item.reason + ' (' + item.amount + ')'"
                :value="item.id"
              />
            </el-select>
            <el-input-number v-model="form.offsetAmount" :min="0" :precision="2" style="margin-left:10px" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-divider content-position="left">{{ $t('oa.expense.items') }}</el-divider>
      <el-table :data="form.items" border>
        <el-table-column :label="$t('oa.expense.category')" align="center" width="180">
          <template slot-scope="scope">
            <el-select v-model="scope.row.categoryId" size="mini" style="width:100%">
              <el-option
                v-for="item in categoryList"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              />
            </el-select>
          </template>
        </el-table-column>
        <el-table-column :label="$t('oa.expense.amount')" align="center" width="140">
          <template slot-scope="scope">
            <el-input-number v-model="scope.row.amount" :min="0" :precision="2" size="mini" style="width:100%" />
          </template>
        </el-table-column>
        <el-table-column :label="$t('oa.expense.invoice')" align="center" width="140">
          <template slot-scope="scope">
            <el-select v-model="scope.row.invoiceId" size="mini" clearable style="width:100%">
              <el-option
                v-for="item in invoiceList"
                :key="item.id"
                :label="item.invoiceNo"
                :value="item.id"
              />
            </el-select>
          </template>
        </el-table-column>
        <el-table-column :label="$t('oa.expense.expenseDate')" align="center" width="150">
          <template slot-scope="scope">
            <el-date-picker v-model="scope.row.expenseDate" type="date" size="mini" value-format="yyyy-MM-dd" style="width:100%" />
          </template>
        </el-table-column>
        <el-table-column :label="$t('oa.expense.description')" align="center">
          <template slot-scope="scope">
            <el-input v-model="scope.row.description" size="mini" />
          </template>
        </el-table-column>
        <el-table-column :label="$t('common.operation')" align="center" width="80">
          <template slot-scope="scope">
            <el-button size="mini" type="text" icon="el-icon-delete" @click="removeItem(scope.$index)">{{ $t('common.delete') }}</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-row style="margin-top:10px">
        <el-col :span="24" style="text-align:center">
          <el-button type="primary" size="mini" icon="el-icon-plus" @click="addItem">{{ $t('oa.expense.addItem') }}</el-button>
        </el-col>
      </el-row>
    </el-form>

    <div style="margin-top:20px; text-align:center">
      <el-button type="success" icon="el-icon-s-promotion" @click="handleOpenSubmit">{{ $t('oa.expense.submit') }}</el-button>
      <el-button @click="handleCancel">{{ $t('common.cancel') }}</el-button>
    </div>

    <flow-submit-dialog
      :visible.sync="submitOpen"
      process-key="oa_expense_report"
      :business-key="businessKey"
      :submit-api="submitApi"
      @success="handleSubmitSuccess"
    />
  </div>
</template>

<script>
import { getReport, addReport, updateReport, submitReport } from "@/api/oa/expense"
import { listCategory, listInvoice, listAvailableLoan } from "@/api/oa/expense"
import FlowSubmitDialog from "@/components/FlowSubmitDialog"

export default {
  name: "OaExpenseReportForm",
  components: { FlowSubmitDialog },
  data() {
    return {
      categoryList: [],
      invoiceList: [],
      availableLoanList: [],
      submitOpen: false,
      form: {
        id: undefined,
        userName: undefined,
        userId: undefined,
        deptId: undefined,
        categoryId: undefined,
        reason: undefined,
        loanId: undefined,
        offsetAmount: 0,
        status: 0,
        items: []
      },
      rules: {
        userName: [
          { required: true, message: this.$t('oa.expense.required.userName'), trigger: "blur" }
        ],
        categoryId: [
          { required: true, message: this.$t('oa.expense.required.category'), trigger: "change" }
        ]
      }
    }
  },
  computed: {
    businessKey() {
      return this.form.id ? 'expense_report:' + this.form.id : ''
    }
  },
  created() {
    this.getCategoryList()
    this.getInvoiceList()
    this.getAvailableLoanList()
    const id = this.$route.query.id
    if (id) {
      this.loadReport(id)
    }
  },
  methods: {
    getCategoryList() {
      listCategory({ pageNum: 1, pageSize: 1000, status: 1 }).then(response => {
        this.categoryList = response.rows
      })
    },
    getInvoiceList() {
      listInvoice({ pageNum: 1, pageSize: 1000, status: 0 }).then(response => {
        this.invoiceList = response.rows
      })
    },
    getAvailableLoanList() {
      listAvailableLoan().then(response => {
        this.availableLoanList = response.rows
      })
    },
    loadReport(id) {
      getReport(id).then(response => {
        this.form = response.data
        if (!this.form.items) {
          this.form.items = []
        }
      })
    },
    addItem() {
      this.form.items.push({
        categoryId: undefined,
        amount: 0,
        invoiceId: undefined,
        description: undefined,
        expenseDate: undefined
      })
    },
    removeItem(index) {
      this.form.items.splice(index, 1)
    },
    saveReport() {
      return new Promise((resolve, reject) => {
        this.$refs["form"].validate(valid => {
          if (!valid) {
            reject(new Error('表单校验失败'))
            return
          }
          const data = { ...this.form }
          const request = data.id != undefined ? updateReport(data) : addReport(data)
          request.then(response => {
            if (!data.id && response.data) {
              this.form.id = response.data
            }
            resolve(response)
          }).catch(reject)
        })
      })
    },
    handleOpenSubmit() {
      this.saveReport().then(() => {
        this.submitOpen = true
      }).catch(() => {})
    },
    submitApi(data) {
      return submitReport(this.form.id, data)
    },
    handleSubmitSuccess() {
      this.$modal.msgSuccess(this.$t('oa.expense.submitSuccess'))
      this.goBack()
    },
    handleCancel() {
      this.goBack()
    },
    goBack() {
      this.$router.push({ path: '/oa/expenseDir/expense' })
    }
  }
}
</script>
