<template>
  <oa-form-page :title="pageTitle" @submit="handleSubmit" @save="handleSave" @close="goBack">
    <el-form ref="form" :model="form" :rules="rules" label-width="100px" slot="form">
      <el-row>
        <el-col :span="12">
          <el-form-item :label="$t('oa.message.template.code')" prop="code">
            <el-input v-model="form.code" :placeholder="$t('oa.message.template.code')" :disabled="isDetail" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('oa.message.template.name')" prop="name">
            <el-input v-model="form.name" :placeholder="$t('oa.message.template.name')" :disabled="isDetail" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('oa.message.template.type')" prop="type">
            <el-select v-model="form.type" :placeholder="$t('oa.message.template.type')" :disabled="isDetail" style="width:100%">
              <el-option :label="$t('oa.message.todo')" value="todo" />
              <el-option :label="$t('oa.message.result')" value="result" />
              <el-option :label="$t('oa.message.notice')" value="notice" />
              <el-option :label="$t('oa.message.system')" value="system" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item :label="$t('oa.message.template.channelsJson')" prop="channelsJson">
            <el-input v-model="form.channelsJson" type="textarea" :rows="2" :placeholder="'[\"site\"]'" :disabled="isDetail" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item :label="$t('oa.message.template.contentJson')" prop="contentJson">
            <el-input v-model="form.contentJson" type="textarea" :rows="4" :placeholder='"{\\\"site\\\":\\\"内容 {{name}}\\\"}"' :disabled="isDetail" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item :label="$t('oa.message.template.variables')" prop="variables">
            <el-input v-model="form.variables" :placeholder="'name,time'" :disabled="isDetail" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item :label="$t('common.status')" prop="status">
            <el-radio-group v-model="form.status" :disabled="isDetail">
              <el-radio :label="1">{{ $t('oa.message.template.enabled') }}</el-radio>
              <el-radio :label="0">{{ $t('oa.message.template.disabled') }}</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
  </oa-form-page>
</template>

<script>
import { getTemplate, addTemplate, updateTemplate } from "@/api/oa/message"
import OaFormPage from "@/views/oa/components/OaFormPage"

export default {
  name: "OaMessageTemplateForm",
  components: { OaFormPage },
  data() {
    return {
      form: {
        id: undefined,
        code: undefined,
        name: undefined,
        type: "system",
        channelsJson: '["site"]',
        contentJson: '',
        variables: '',
        status: 1
      },
      rules: {
        code: [
          { required: true, message: this.$t('oa.message.template.required.code'), trigger: "blur" }
        ],
        name: [
          { required: true, message: this.$t('oa.message.template.required.name'), trigger: "blur" }
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
    },
    pageTitle() {
      return this.$t('oa.message.template.title')
    }
  },
  created() {
    this.reset()
    const id = this.$route.query.id
    if ((this.mode === "edit" || this.mode === "detail") && id) {
      this.loadTemplate(id)
    }
  },
  methods: {
    reset() {
      this.form = {
        id: undefined,
        code: undefined,
        name: undefined,
        type: "system",
        channelsJson: '["site"]',
        contentJson: '',
        variables: '',
        status: 1
      }
      this.resetForm("form")
    },
    loadTemplate(id) {
      getTemplate(id).then(response => {
        this.form = response.data
      })
    },
    saveTemplate() {
      return new Promise((resolve, reject) => {
        this.$refs["form"].validate(valid => {
          if (!valid) {
            reject(new Error("表单校验失败"))
            return
          }
          const data = { ...this.form }
          const request = data.id != undefined ? updateTemplate(data) : addTemplate(data)
          request.then(response => {
            resolve(response)
          }).catch(reject)
        })
      })
    },
    handleSave() {
      this.saveTemplate().then(() => {
        this.$modal.msgSuccess(this.form.id ? this.$t('common.editSuccess') : this.$t('common.addSuccess'))
      }).catch(() => {})
    },
    handleSubmit() {
      this.saveTemplate().then(() => {
        this.$modal.msgSuccess(this.form.id ? this.$t('common.editSuccess') : this.$t('common.addSuccess'))
        this.goBack()
      }).catch(() => {})
    },
    goBack() {
      this.$router.back()
    }
  }
}
</script>
