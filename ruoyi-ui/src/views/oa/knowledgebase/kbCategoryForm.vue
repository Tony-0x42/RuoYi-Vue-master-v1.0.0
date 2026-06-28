<template>
  <oa-form-page @submit="handleSubmit" @save="handleSave" @close="handleClose">
    <template #form>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item :label="$t('oa.knowledgebase.categoryName')" prop="name">
          <el-input v-model="form.name" :placeholder="$t('oa.knowledgebase.categoryName')" :disabled="isDetail" />
        </el-form-item>
        <el-form-item :label="$t('oa.knowledgebase.categoryCode')" prop="code">
          <el-input v-model="form.code" :placeholder="$t('oa.knowledgebase.categoryCode')" :disabled="isDetail" />
        </el-form-item>
        <el-form-item :label="$t('common.status')" prop="status">
          <el-radio-group v-model="form.status" :disabled="isDetail">
            <el-radio :label="1">{{ $t('oa.knowledgebase.enable') }}</el-radio>
            <el-radio :label="0">{{ $t('oa.knowledgebase.disable') }}</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
    </template>
  </oa-form-page>
</template>

<script>
import OaFormPage from "@/views/oa/components/OaFormPage"
import { getCategory, addCategory, updateCategory } from "@/api/oa/knowledgebase"

export default {
  name: "OaKnowledgebaseCategoryForm",
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
        sort: 0,
        parentId: 0
      },
      rules: {
        name: [
          { required: true, message: this.$t('oa.knowledgebase.required.title'), trigger: "blur" }
        ],
        code: [
          { required: true, message: this.$t('oa.knowledgebase.required.categoryCode'), trigger: "blur" }
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
    saveData(back) {
      this.$refs["form"].validate(valid => {
        if (!valid) {
          return
        }
        const data = { ...this.form }
        if (data.id != undefined) {
          updateCategory(data).then(() => {
            this.$modal.msgSuccess(this.$t('common.editSuccess'))
            if (back) {
              this.$router.back()
            }
          }).catch(() => {})
        } else {
          addCategory(data).then(response => {
            this.form.id = response.data
            this.$modal.msgSuccess(this.$t('common.addSuccess'))
            if (back) {
              this.$router.back()
            }
          }).catch(() => {})
        }
      })
    },
    handleSave() {
      this.saveData(false)
    },
    handleSubmit() {
      this.saveData(true)
    },
    handleClose() {
      this.$router.back()
    }
  }
}
</script>
