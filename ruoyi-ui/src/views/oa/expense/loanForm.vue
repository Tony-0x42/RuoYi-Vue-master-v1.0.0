<template>
  <oa-form-page @submit="handleSubmit" @save="handleSave" @close="handleClose">
    <template #form>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item :label="$t('oa.expense.userName')" prop="userName">
              <el-input v-model="form.userName" :placeholder="$t('oa.expense.placeholder.userName')" :disabled="isDetail" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('oa.expense.amount')" prop="amount">
              <el-input-number v-model="form.amount" :min="0" :precision="2" style="width:100%" :disabled="isDetail" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('oa.expense.dueDate')" prop="dueDate">
              <el-date-picker v-model="form.dueDate" type="date" value-format="yyyy-MM-dd" style="width:100%" :disabled="isDetail" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="$t('oa.expense.reason')" prop="reason">
              <el-input v-model="form.reason" type="textarea" :placeholder="$t('oa.expense.placeholder.reason')" :disabled="isDetail" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </template>

    <flow-submit-dialog
      :visible="flowVisible"
      process-key="oa_expense_loan"
      :business-key="'expense_loan:' + form.id"
      :submit-api="submitApi"
      @close="flowVisible = false"
      @success="handleClose"
    />
  </oa-form-page>
</template>

<script>
import OaFormPage from "@/views/oa/components/OaFormPage"
import FlowSubmitDialog from "@/components/FlowSubmitDialog"
import { getLoan, addLoan, updateLoan, submitLoan } from "@/api/oa/expense"

export default {
  name: "OaExpenseLoanForm",
  components: {
    OaFormPage,
    FlowSubmitDialog
  },
  data() {
    return {
      flowVisible: false,
      form: {
        id: undefined,
        userId: undefined,
        userName: undefined,
        amount: 0,
        repaidAmount: 0,
        reason: undefined,
        status: 0,
        dueDate: undefined
      },
      rules: {
        userName: [
          { required: true, message: this.$t('oa.expense.required.userName'), trigger: "blur" }
        ],
        amount: [
          { required: true, message: this.$t('oa.expense.required.amount'), trigger: "blur" }
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
    const id = this.$route.query.id
    if (id) {
      this.getLoan(id)
    }
  },
  methods: {
    getLoan(id) {
      getLoan(id).then(response => {
        this.form = response.data
      }).catch(() => {})
    },
    handleSave() {
      this.$refs["form"].validate(valid => {
        if (!valid) {
          return
        }
        const data = { ...this.form }
        if (data.id != undefined) {
          updateLoan(data).then(() => {
            this.$modal.msgSuccess(this.$t('common.editSuccess'))
          }).catch(() => {})
        } else {
          addLoan(data).then(response => {
            this.form.id = response.data
            this.$modal.msgSuccess(this.$t('common.addSuccess'))
          }).catch(() => {})
        }
      })
    },
    handleSubmit() {
      this.$refs["form"].validate(valid => {
        if (!valid) {
          return
        }
        if (!this.form.id) {
          this.$modal.msgWarning("请先保存借款单")
          return
        }
        this.flowVisible = true
      })
    },
    submitApi(data) {
      return submitLoan(this.form.id, data)
    },
    handleClose() {
      this.$router.back()
    }
  }
}
</script>
