<template>
  <oa-form-page :title="title" :mode="mode" @save="handleSave" @submit="handleSubmit" @close="handleClose">
    <el-form ref="form" :model="form" :rules="rules" label-width="100px">
      <el-form-item :label="$t('oa.expense.category')" prop="categoryId">
        <el-select v-model="form.categoryId" :placeholder="$t('oa.expense.placeholder.category')" :disabled="isDetail" style="width:100%">
          <el-option
            v-for="item in categoryList"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('oa.expense.level')" prop="level">
        <el-input v-model="form.level" :placeholder="$t('oa.expense.placeholder.level')" :disabled="isDetail" />
      </el-form-item>
      <el-form-item :label="$t('oa.expense.city')" prop="city">
        <el-input v-model="form.city" :placeholder="$t('oa.expense.placeholder.city')" :disabled="isDetail" />
      </el-form-item>
      <el-form-item :label="$t('oa.expense.limitAmount')" prop="limitAmount">
        <el-input-number v-model="form.limitAmount" :min="0" :precision="2" :disabled="isDetail" style="width:100%" />
      </el-form-item>
      <el-form-item :label="$t('oa.expense.periodType')" prop="periodType">
        <el-select v-model="form.periodType" :placeholder="$t('oa.expense.placeholder.periodType')" :disabled="isDetail" style="width:100%">
          <el-option :label="$t('oa.expense.periodDay')" value="day" />
          <el-option :label="$t('oa.expense.periodTime')" value="time" />
          <el-option :label="$t('oa.expense.periodMonth')" value="month" />
          <el-option :label="$t('oa.expense.periodYear')" value="year" />
        </el-select>
      </el-form-item>
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
import { listCategory, getStandard, addStandard, updateStandard } from "@/api/oa/expense"
import OaFormPage from "@/views/oa/components/OaFormPage"

export default {
  name: "OaExpenseStandardForm",
  components: { OaFormPage },
  data() {
    return {
      mode: 'add',
      categoryList: [],
      form: {
        id: undefined,
        categoryId: undefined,
        level: undefined,
        city: undefined,
        deptId: undefined,
        limitAmount: 0,
        periodType: "time",
        status: 1
      },
      rules: {
        categoryId: [
          { required: true, message: this.$t('oa.expense.required.category'), trigger: "change" }
        ],
        limitAmount: [
          { required: true, message: this.$t('oa.expense.required.limitAmount'), trigger: "blur" }
        ]
      }
    }
  },
  computed: {
    isDetail() {
      return this.mode === 'detail'
    },
    title() {
      if (this.mode === 'detail') return '费用标准详情'
      return this.form.id != undefined ? '编辑费用标准' : '新增费用标准'
    }
  },
  created() {
    this.getCategoryList()
    const { mode, id } = this.$route.query
    this.mode = mode || 'add'
    if (id) {
      this.loadStandard(id)
    }
  },
  methods: {
    getCategoryList() {
      listCategory({ pageNum: 1, pageSize: 1000, status: 1 }).then(response => {
        this.categoryList = response.rows
      })
    },
    loadStandard(id) {
      getStandard(id).then(response => {
        this.form = response.data
      })
    },
    handleSave() {
      this.$refs["form"].validate(valid => {
        if (!valid) return
        const data = { ...this.form }
        const request = data.id != undefined ? updateStandard(data) : addStandard(data)
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
        const request = data.id != undefined ? updateStandard(data) : addStandard(data)
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
