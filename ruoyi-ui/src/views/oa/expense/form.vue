<template>
  <oa-form-page :title="$t('oa.expense.report')" @submit="handleSubmit" @save="handleSave" @close="handleClose">
    <template #form>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item :label="$t('oa.expense.userName')" prop="userName">
              <el-input v-model="form.userName" :placeholder="$t('oa.expense.placeholder.userName')" :disabled="isDetail" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('oa.expense.category')" prop="categoryId">
              <el-select v-model="form.categoryId" :placeholder="$t('oa.expense.placeholder.category')" style="width:100%" :disabled="isDetail">
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
              <el-input v-model="form.reason" type="textarea" :placeholder="$t('oa.expense.placeholder.reason')" :disabled="isDetail" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="$t('oa.expense.loan')" prop="loanId">
              <el-select v-model="form.loanId" :placeholder="$t('oa.expense.placeholder.loan')" clearable style="width:50%" :disabled="isDetail">
                <el-option
                  v-for="item in availableLoanList"
                  :key="item.id"
                  :label="item.reason + ' (' + item.amount + ')'"
                  :value="item.id"
                />
              </el-select>
              <el-input-number v-model="form.offsetAmount" :min="0" :precision="2" style="margin-left:10px" :disabled="isDetail" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider content-position="left">{{ $t('oa.expense.items') }}</el-divider>
        <el-table :data="form.items" border>
          <el-table-column :label="$t('oa.expense.category')" align="center" width="180">
            <template slot-scope="scope">
              <el-select v-model="scope.row.categoryId" size="mini" style="width:100%" :disabled="isDetail">
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
              <el-input-number v-model="scope.row.amount" :min="0" :precision="2" size="mini" style="width:100%" :disabled="isDetail" />
            </template>
          </el-table-column>
          <el-table-column :label="$t('oa.expense.invoice')" align="center" width="140">
            <template slot-scope="scope">
              <el-select v-model="scope.row.invoiceId" size="mini" clearable style="width:100%" :disabled="isDetail">
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
              <el-date-picker v-model="scope.row.expenseDate" type="date" size="mini" value-format="yyyy-MM-dd" style="width:100%" :disabled="isDetail" />
            </template>
          </el-table-column>
          <el-table-column :label="$t('oa.expense.description')" align="center">
            <template slot-scope="scope">
              <el-input v-model="scope.row.description" size="mini" :disabled="isDetail" />
            </template>
          </el-table-column>
          <el-table-column :label="$t('common.operation')" align="center" width="80">
            <template slot-scope="scope">
              <el-button size="mini" type="text" icon="el-icon-delete" @click="removeItem(scope.$index)" :disabled="isDetail">{{ $t('common.delete') }}</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-row v-if="!isDetail" style="margin-top:10px">
          <el-col :span="24" style="text-align:center">
            <el-button type="primary" size="mini" icon="el-icon-plus" @click="addItem">{{ $t('oa.expense.addItem') }}</el-button>
          </el-col>
        </el-row>
      </el-form>
    </template>

    <flow-submit-dialog
      :visible="flowVisible"
      process-key="oa_expense_report"
      :business-key="'expense_report:' + form.id"
      :form-data="form"
      @close="flowVisible = false"
      @success="handleClose"
    />
  </oa-form-page>
</template>

<script>
import OaFormPage from "@/views/oa/components/OaFormPage"
import FlowSubmitDialog from "@/components/FlowSubmitDialog"
import { getReport, addReport, updateReport } from "@/api/oa/expense"
import { listCategory, listInvoice, listAvailableLoan } from "@/api/oa/expense"

export default {
  name: "OaExpenseForm",
  components: {
    OaFormPage,
    FlowSubmitDialog
  },
  data() {
    return {
      flowVisible: false,
      categoryList: [],
      invoiceList: [],
      availableLoanList: [],
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
    mode() {
      const m = this.$route.query.mode
      return ["add", "edit", "detail"].includes(m) ? m : "add"
    },
    isDetail() {
      return this.mode === "detail"
    }
  },
  mounted() {
    this.getCategoryList()
    this.getInvoiceList()
    this.getAvailableLoanList()
    const id = this.$route.query.id
    if (id) {
      this.getReport(id)
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
    getReport(id) {
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
    handleSave() {
      this.$refs["form"].validate(valid => {
        if (!valid) {
          return
        }
        const data = { ...this.form }
        if (data.id != undefined) {
          updateReport(data).then(() => {
            this.$modal.msgSuccess(this.$t('common.editSuccess'))
          })
        } else {
          addReport(data).then(response => {
            this.form.id = response.data
            this.$modal.msgSuccess(this.$t('common.addSuccess'))
          })
        }
      })
    },
    handleSubmit() {
      if (!this.form.id) {
        this.$modal.msgWarning("请先保存报销单")
        return
      }
      this.flowVisible = true
    },
    handleClose() {
      this.$router.back()
    }
  }
}
</script>
