<template>
  <oa-form-page :title="title" :mode="mode" @save="handleSave" @submit="handleSubmit" @close="handleClose">
    <el-form ref="form" :model="form" :rules="rules" label-width="100px">
      <el-row>
        <el-col :span="12">
          <el-form-item :label="$t('oa.expense.deptId')" prop="deptId">
            <el-input-number v-model="form.deptId" :disabled="isDetail" style="width:100%" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('oa.expense.projectId')" prop="projectId">
            <el-input-number v-model="form.projectId" :disabled="isDetail" style="width:100%" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('oa.expense.itemId')" prop="itemId">
            <el-input-number v-model="form.itemId" :disabled="isDetail" style="width:100%" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('oa.expense.year')" prop="year">
            <el-input-number v-model="form.year" :min="2000" :max="2100" :disabled="isDetail" style="width:100%" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('oa.expense.totalAmount')" prop="totalAmount">
            <el-input-number v-model="form.totalAmount" :min="0" :precision="2" :disabled="isDetail" style="width:100%" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('oa.expense.usedAmount')" prop="usedAmount">
            <el-input-number v-model="form.usedAmount" :min="0" :precision="2" :disabled="isDetail" style="width:100%" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item :label="$t('common.status')" prop="status">
        <el-radio-group v-model="form.status" :disabled="isDetail">
          <el-radio :label="1">{{ $t('common.enable') }}</el-radio>
          <el-radio :label="0">{{ $t('common.disable') }}</el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>
  </oa-form-page>
</template>

<script>
import { getBudget, addBudget, updateBudget } from "@/api/oa/expense"
import OaFormPage from "@/views/oa/components/OaFormPage"

export default {
  name: "OaExpenseBudgetForm",
  components: { OaFormPage },
  data() {
    return {
      mode: 'add',
      form: {
        id: undefined,
        deptId: undefined,
        projectId: undefined,
        itemId: undefined,
        year: new Date().getFullYear(),
        totalAmount: 0,
        usedAmount: 0,
        status: 1
      },
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
  computed: {
    isDetail() {
      return this.mode === 'detail'
    },
    title() {
      if (this.mode === 'detail') return '预算详情'
      return this.form.id != undefined ? '编辑预算' : '新增预算'
    }
  },
  created() {
    const { mode, id } = this.$route.query
    this.mode = mode || 'add'
    if (id) {
      this.loadBudget(id)
    }
  },
  methods: {
    loadBudget(id) {
      getBudget(id).then(response => {
        this.form = response.data
      })
    },
    handleSave() {
      this.$refs["form"].validate(valid => {
        if (!valid) return
        const data = { ...this.form }
        const request = data.id != undefined ? updateBudget(data) : addBudget(data)
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
        const request = data.id != undefined ? updateBudget(data) : addBudget(data)
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
