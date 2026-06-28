<template>
  <oa-form-page @submit="handleSubmit" @save="handleSave" @close="handleClose">
    <template #form>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item :label="$t('oa.notice.categoryName')" prop="name">
          <el-input v-model="form.name" :placeholder="$t('oa.notice.placeholder.categoryName')" :disabled="isDetail" />
        </el-form-item>
        <el-form-item :label="$t('oa.notice.categoryCode')" prop="code">
          <el-input v-model="form.code" :placeholder="$t('oa.notice.placeholder.categoryCode')" :disabled="isDetail" />
        </el-form-item>
        <el-form-item :label="$t('common.status')" prop="status">
          <el-radio-group v-model="form.status" :disabled="isDetail">
            <el-radio :label="1">{{ $t('oa.notice.enable') }}</el-radio>
            <el-radio :label="0">{{ $t('oa.notice.disable') }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item :label="$t('oa.notice.sort')" prop="sort">
          <el-input-number v-model="form.sort" controls-position="right" :min="0" :disabled="isDetail" />
        </el-form-item>
      </el-form>
    </template>
  </oa-form-page>
</template>

<script>
import OaFormPage from "@/views/oa/components/OaFormPage"
import { getCategory, addCategory, updateCategory } from "@/api/oa/notice"

export default {
  name: "OaNoticeCategoryForm",
  components: {
    OaFormPage
  },
  data() {
    return {
      form: {
        id: undefined,
        name: undefined,
        code: undefined,
        status: 1,
        sort: 0
      },
      rules: {
        name: [
          { required: true, message: this.$t('oa.notice.required.categoryName'), trigger: "blur" }
        ],
        code: [
          { required: true, message: this.$t('oa.notice.required.categoryCode'), trigger: "blur" }
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
      this.getCategory(id)
    }
  },
  methods: {
    getCategory(id) {
      getCategory(id).then(response => {
        this.form = response.data
      }).catch(() => {})
    },
    submitData() {
      if (this.form.id != undefined) {
        return updateCategory(this.form)
      }
      return addCategory(this.form).then(response => {
        if (response.data) {
          this.form.id = response.data
        }
      })
    },
    handleSave() {
      this.$refs["form"].validate(valid => {
        if (!valid) {
          return
        }
        this.submitData().then(() => {
          this.$modal.msgSuccess(this.form.id ? this.$t('common.editSuccess') : this.$t('common.addSuccess'))
        }).catch(() => {})
      })
    },
    handleSubmit() {
      this.$refs["form"].validate(valid => {
        if (!valid) {
          return
        }
        this.submitData().then(() => {
          this.$modal.msgSuccess(this.form.id ? this.$t('common.editSuccess') : this.$t('common.addSuccess'))
          this.$router.back()
        }).catch(() => {})
      })
    },
    handleClose() {
      this.$router.back()
    }
  }
}
</script>
