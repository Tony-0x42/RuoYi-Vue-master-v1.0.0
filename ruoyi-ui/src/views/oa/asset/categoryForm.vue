<template>
  <oa-form-page title="资产分类" @submit="handleSubmit" @save="handleSave" @close="handleClose">
    <template #form>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item :label="$t('oa.asset.categoryName')" prop="name">
          <el-input v-model="form.name" :placeholder="$t('oa.asset.categoryName')" :disabled="isDetail" />
        </el-form-item>
        <el-form-item :label="$t('oa.asset.categoryCode')" prop="code">
          <el-input v-model="form.code" :placeholder="$t('oa.asset.categoryCode')" :disabled="isDetail" />
        </el-form-item>
        <el-form-item :label="$t('oa.asset.depreciationYears')" prop="depreciationYears">
          <el-input-number v-model="form.depreciationYears" :min="0" :max="100" controls-position="right" :disabled="isDetail" />
        </el-form-item>
        <el-form-item :label="$t('common.status')" prop="status">
          <el-radio-group v-model="form.status" :disabled="isDetail">
            <el-radio :label="1">{{ $t('oa.asset.enable') }}</el-radio>
            <el-radio :label="0">{{ $t('oa.asset.disable') }}</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
    </template>
  </oa-form-page>
</template>

<script>
import OaFormPage from "@/views/oa/components/OaFormPage"
import { getCategory, addCategory, updateCategory } from "@/api/oa/asset"

export default {
  name: "OaAssetCategoryForm",
  components: {
    OaFormPage
  },
  data() {
    return {
      form: {
        id: undefined,
        name: undefined,
        code: undefined,
        depreciationYears: 5,
        status: 1,
        sort: 0,
        parentId: 0
      },
      rules: {
        name: [
          { required: true, message: this.$t('oa.asset.required.categoryName'), trigger: "blur" }
        ],
        code: [
          { required: true, message: this.$t('oa.asset.required.categoryCode'), trigger: "blur" }
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
      this.loadCategory(id)
    }
  },
  methods: {
    loadCategory(id) {
      getCategory(id).then(response => {
        this.form = response.data
      }).catch(() => {})
    },
    saveCategory() {
      return new Promise((resolve, reject) => {
        this.$refs["form"].validate(valid => {
          if (!valid) {
            reject(new Error('表单校验失败'))
            return
          }
          const data = { ...this.form }
          const request = data.id != undefined ? updateCategory(data) : addCategory(data)
          request.then(response => {
            if (!data.id && response.data) {
              this.form.id = response.data
            }
            resolve(response)
          }).catch(reject)
        })
      })
    },
    handleSave() {
      this.saveCategory().then(() => {
        this.$modal.msgSuccess(this.form.id ? this.$t('common.editSuccess') : this.$t('common.addSuccess'))
      }).catch(() => {})
    },
    handleSubmit() {
      this.saveCategory().then(() => {
        this.$modal.msgSuccess(this.form.id ? this.$t('common.editSuccess') : this.$t('common.addSuccess'))
        this.handleClose()
      }).catch(() => {})
    },
    handleClose() {
      this.$router.back()
    }
  }
}
</script>
