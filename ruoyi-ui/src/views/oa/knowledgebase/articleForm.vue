<template>
  <oa-form-page @submit="handleSubmit" @save="handleSave" @close="handleClose">
    <template #form>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item :label="$t('oa.knowledgebase.articleTitle')" prop="title">
              <el-input v-model="form.title" :placeholder="$t('oa.knowledgebase.placeholder.title')" :disabled="isDetail" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('oa.knowledgebase.category')" prop="categoryId">
              <el-select v-model="form.categoryId" :placeholder="$t('oa.knowledgebase.placeholder.category')" style="width:100%" :disabled="isDetail">
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
            <el-form-item :label="$t('oa.knowledgebase.summary')" prop="summary">
              <el-input v-model="form.summary" type="textarea" :rows="2" :placeholder="$t('oa.knowledgebase.placeholder.summary')" :disabled="isDetail" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('oa.knowledgebase.tags')" prop="tags">
              <el-input v-model="form.tags" :placeholder="$t('oa.knowledgebase.placeholder.tags')" :disabled="isDetail" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('oa.knowledgebase.status')" prop="status">
              <el-radio-group v-model="form.status" :disabled="isDetail">
                <el-radio :label="0">{{ $t('oa.knowledgebase.statusDraft') }}</el-radio>
                <el-radio :label="1">{{ $t('oa.knowledgebase.statusPublished') }}</el-radio>
                <el-radio :label="2">{{ $t('oa.knowledgebase.statusOffline') }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item>
              <el-checkbox v-model="form.top" :disabled="isDetail">{{ $t('oa.knowledgebase.top') }}</el-checkbox>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="$t('oa.knowledgebase.content')" prop="content">
              <editor v-model="form.content" :min-height="192" :read-only="isDetail" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </template>
  </oa-form-page>
</template>

<script>
import OaFormPage from "@/views/oa/components/OaFormPage"
import { listCategory, getArticle, addArticle, updateArticle } from "@/api/oa/knowledgebase"

export default {
  name: "OaKnowledgebaseArticleForm",
  components: {
    OaFormPage
  },
  data() {
    return {
      categoryList: [],
      form: {
        id: undefined,
        title: undefined,
        categoryId: undefined,
        summary: undefined,
        content: undefined,
        tags: undefined,
        status: 0,
        top: false,
        version: 1
      },
      rules: {
        title: [
          { required: true, message: this.$t('oa.knowledgebase.required.title'), trigger: "blur" }
        ],
        categoryId: [
          { required: true, message: this.$t('oa.knowledgebase.required.category'), trigger: "change" }
        ],
        content: [
          { required: true, message: this.$t('oa.knowledgebase.required.content'), trigger: "blur" }
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
    const id = this.$route.query.id
    if (id) {
      this.getArticle(id)
    }
  },
  methods: {
    getCategoryList() {
      listCategory({ pageNum: 1, pageSize: 1000, status: 1 }).then(response => {
        this.categoryList = response.rows
      })
    },
    getArticle(id) {
      getArticle(id).then(response => {
        this.form = response.data
        this.form.top = this.form.top === 1
      }).catch(() => {})
    },
    saveData(back) {
      this.$refs["form"].validate(valid => {
        if (!valid) {
          return
        }
        const data = { ...this.form }
        data.top = data.top ? 1 : 0
        if (data.id != undefined) {
          updateArticle(data).then(() => {
            this.$modal.msgSuccess(this.$t('common.editSuccess'))
            if (back) {
              this.$router.back()
            }
          }).catch(() => {})
        } else {
          addArticle(data).then(response => {
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
