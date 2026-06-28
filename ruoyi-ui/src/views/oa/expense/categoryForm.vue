<template>
  <oa-form-page :title="title" :mode="mode" @save="handleSave" @submit="handleSubmit" @close="handleClose">
    <el-form ref="form" :model="form" :rules="rules" label-width="100px">
      <el-form-item :label="$t('oa.expense.categoryName')" prop="name">
        <el-input v-model="form.name" :placeholder="$t('oa.expense.placeholder.categoryName')" :disabled="isDetail" />
      </el-form-item>
      <el-form-item :label="$t('oa.expense.categoryCode')" prop="code">
        <el-input v-model="form.code" :placeholder="$t('oa.expense.placeholder.categoryCode')" :disabled="isDetail" />
      </el-form-item>
      <el-form-item :label="$t('oa.expense.parentCategory')" prop="parentId">
        <el-select v-model="form.parentId" :placeholder="$t('oa.expense.placeholder.parentCategory')" :disabled="isDetail" clearable style="width:100%">
          <el-option :label="$t('oa.expense.topCategory')" :value="0" />
          <el-option
            v-for="item in parentCategoryList"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('oa.expense.processKey')" prop="processKey">
        <el-input v-model="form.processKey" :placeholder="$t('oa.expense.placeholder.processKey')" :disabled="isDetail" />
      </el-form-item>
      <el-form-item :label="$t('common.status')" prop="status">
        <el-radio-group v-model="form.status" :disabled="isDetail">
          <el-radio :label="1">{{ $t('common.enable') }}</el-radio>
          <el-radio :label="0">{{ $t('common.disable') }}</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item :label="$t('oa.expense.sort')" prop="sort">
        <el-input-number v-model="form.sort" :min="0" :disabled="isDetail" />
      </el-form-item>
    </el-form>
  </oa-form-page>
</template>

<script>
import { listCategory, getCategory, addCategory, updateCategory } from "@/api/oa/expense"
import OaFormPage from "@/views/oa/components/OaFormPage"

export default {
  name: "OaExpenseCategoryForm",
  components: { OaFormPage },
  data() {
    return {
      mode: 'add',
      parentCategoryList: [],
      form: {
        id: undefined,
        parentId: 0,
        name: undefined,
        code: undefined,
        processKey: undefined,
        budgetItemId: undefined,
        status: 1,
        sort: 0
      },
      rules: {
        name: [
          { required: true, message: this.$t('oa.expense.required.categoryName'), trigger: "blur" }
        ],
        code: [
          { required: true, message: this.$t('oa.expense.required.categoryCode'), trigger: "blur" }
        ]
      }
    }
  },
  computed: {
    isDetail() {
      return this.mode === 'detail'
    },
    title() {
      if (this.mode === 'detail') return '费用类型详情'
      return this.form.id != undefined ? '编辑费用类型' : '新增费用类型'
    }
  },
  created() {
    this.getParentCategoryList()
    const { mode, id } = this.$route.query
    this.mode = mode || 'add'
    if (id) {
      this.loadCategory(id)
    }
  },
  methods: {
    getParentCategoryList() {
      listCategory({ pageNum: 1, pageSize: 1000, status: 1 }).then(response => {
        this.parentCategoryList = response.rows.filter(item => !item.parentId || item.parentId === 0)
      })
    },
    loadCategory(id) {
      getCategory(id).then(response => {
        this.form = response.data
      })
    },
    handleSave() {
      this.$refs["form"].validate(valid => {
        if (!valid) return
        const data = { ...this.form }
        const request = data.id != undefined ? updateCategory(data) : addCategory(data)
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
        const request = data.id != undefined ? updateCategory(data) : addCategory(data)
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
