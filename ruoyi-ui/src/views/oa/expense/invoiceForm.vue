<template>
  <oa-form-page :title="title" :mode="mode" @save="handleSave" @submit="handleSubmit" @close="handleClose">
    <el-form ref="form" :model="form" :rules="rules" label-width="100px">
      <el-row>
        <el-col :span="12">
          <el-form-item :label="$t('oa.expense.invoiceCode')" prop="invoiceCode">
            <el-input v-model="form.invoiceCode" :placeholder="$t('oa.expense.placeholder.invoiceCode')" :disabled="isDetail" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('oa.expense.invoiceNo')" prop="invoiceNo">
            <el-input v-model="form.invoiceNo" :placeholder="$t('oa.expense.placeholder.invoiceNo')" :disabled="isDetail" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('oa.expense.invoiceType')" prop="invoiceType">
            <el-select v-model="form.invoiceType" :placeholder="$t('oa.expense.placeholder.invoiceType')" :disabled="isDetail" style="width:100%">
              <el-option :label="$t('oa.expense.typeVat')" value="增值税普通发票" />
              <el-option :label="$t('oa.expense.typeElectronic')" value="增值税电子普通发票" />
              <el-option :label="$t('oa.expense.typeTrain')" value="火车票" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('oa.expense.invoiceDate')" prop="invoiceDate">
            <el-date-picker v-model="form.invoiceDate" type="date" value-format="yyyy-MM-dd" :disabled="isDetail" style="width:100%" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('oa.expense.amount')" prop="amount">
            <el-input-number v-model="form.amount" :min="0" :precision="2" :disabled="isDetail" style="width:100%" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('oa.expense.tax')" prop="tax">
            <el-input-number v-model="form.tax" :min="0" :precision="2" :disabled="isDetail" style="width:100%" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('oa.expense.buyer')" prop="buyer">
            <el-input v-model="form.buyer" :placeholder="$t('oa.expense.placeholder.buyer')" :disabled="isDetail" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('oa.expense.seller')" prop="seller">
            <el-input v-model="form.seller" :placeholder="$t('oa.expense.placeholder.seller')" :disabled="isDetail" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
  </oa-form-page>
</template>

<script>
import { getInvoice, addInvoice, updateInvoice } from "@/api/oa/expense"
import OaFormPage from "@/views/oa/components/OaFormPage"

export default {
  name: "OaExpenseInvoiceForm",
  components: { OaFormPage },
  data() {
    return {
      mode: 'add',
      form: {
        id: undefined,
        invoiceCode: undefined,
        invoiceNo: undefined,
        invoiceType: undefined,
        amount: 0,
        tax: 0,
        invoiceDate: undefined,
        buyer: undefined,
        seller: undefined,
        status: 0
      },
      rules: {
        invoiceNo: [
          { required: true, message: this.$t('oa.expense.required.invoiceNo'), trigger: "blur" }
        ],
        amount: [
          { required: true, message: this.$t('oa.expense.required.amount'), trigger: "blur" }
        ]
      }
    }
  },
  computed: {
    isDetail() {
      return this.mode === 'detail'
    },
    title() {
      if (this.mode === 'detail') return '发票详情'
      return this.form.id != undefined ? '编辑发票' : '新增发票'
    }
  },
  created() {
    const { mode, id } = this.$route.query
    this.mode = mode || 'add'
    if (id) {
      this.loadInvoice(id)
    }
  },
  methods: {
    loadInvoice(id) {
      getInvoice(id).then(response => {
        this.form = response.data
      })
    },
    handleSave() {
      this.$refs["form"].validate(valid => {
        if (!valid) return
        const data = { ...this.form }
        const request = data.id != undefined ? updateInvoice(data) : addInvoice(data)
        request.then(response => {
          if (!data.id && response.data) {
            this.form.id = response.data
          }
          this.$modal.msgSuccess(data.id != undefined ? this.$t('common.editSuccess') : this.$t('common.addSuccess'))
        })
      })
    },
    handleSubmit() {
      this.$refs["form"].validate(valid => {
        if (!valid) return
        const data = { ...this.form }
        const request = data.id != undefined ? updateInvoice(data) : addInvoice(data)
        request.then(response => {
          this.$modal.msgSuccess(data.id != undefined ? this.$t('common.editSuccess') : this.$t('common.addSuccess'))
          this.$router.back()
        })
      })
    },
    handleClose() {
      this.$router.back()
    }
  }
}
</script>
