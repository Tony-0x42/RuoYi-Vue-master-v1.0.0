<template>
  <oa-form-page :title="pageTitle" @submit="handleSubmit" @save="handleSave" @close="goBack">
    <el-form ref="form" :model="form" :rules="rules" label-width="100px" slot="form">
      <el-row>
        <el-col :span="12">
          <el-form-item :label="$t('oa.message.type')" prop="type">
            <el-select v-model="form.type" :placeholder="$t('oa.message.placeholder.type')" :disabled="isDetail" style="width:100%">
              <el-option :label="$t('oa.message.todo')" value="todo" />
              <el-option :label="$t('oa.message.result')" value="result" />
              <el-option :label="$t('oa.message.notice')" value="notice" />
              <el-option :label="$t('oa.message.system')" value="system" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('oa.message.priority')" prop="priority">
            <el-select v-model="form.priority" :placeholder="$t('oa.message.placeholder.priority')" :disabled="isDetail" style="width:100%">
              <el-option :label="$t('oa.message.low')" value="low" />
              <el-option :label="$t('oa.message.normal')" value="normal" />
              <el-option :label="$t('oa.message.high')" value="high" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item :label="$t('oa.message.title')" prop="title">
            <el-input v-model="form.title" :placeholder="$t('oa.message.placeholder.title')" :disabled="isDetail" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item :label="$t('oa.message.content')" prop="content">
            <el-input v-model="form.content" type="textarea" :rows="4" :placeholder="$t('oa.message.placeholder.content')" :disabled="isDetail" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('oa.message.channel')" prop="channel">
            <el-select v-model="form.channel" :placeholder="$t('oa.message.placeholder.channel')" :disabled="isDetail" style="width:100%">
              <el-option :label="$t('oa.message.site')" value="site" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item :label="$t('oa.message.recipient')" prop="recipientUserIds">
            <UserMultiSelect v-model="form.recipientUserIds" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
  </oa-form-page>
</template>

<script>
import { sendMessage } from "@/api/oa/message"
import OaFormPage from "@/views/oa/components/OaFormPage"
import UserMultiSelect from "@/components/UserMultiSelect"

export default {
  name: "OaMessageForm",
  components: { OaFormPage, UserMultiSelect },
  data() {
    return {
      form: {
        type: "system",
        title: undefined,
        content: undefined,
        priority: "normal",
        channel: "site",
        recipientUserIds: ""
      },
      rules: {
        title: [
          { required: true, message: this.$t('oa.message.required.title'), trigger: "blur" }
        ],
        content: [
          { required: true, message: this.$t('oa.message.required.content'), trigger: "blur" }
        ],
        recipientUserIds: [
          { required: true, message: this.$t('oa.message.required.recipient'), trigger: "change" }
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
      return this.$t('oa.message.sendMessage')
    }
  },
  created() {
    this.reset()
  },
  methods: {
    reset() {
      this.form = {
        type: "system",
        title: undefined,
        content: undefined,
        priority: "normal",
        channel: "site",
        recipientUserIds: ""
      }
      this.resetForm("form")
    },
    buildData() {
      const data = { ...this.form }
      data.recipientUserIds = String(data.recipientUserIds || "")
        .split(",")
        .map(s => Number(s.trim()))
        .filter(id => !isNaN(id) && id > 0)
      return data
    },
    saveMessage() {
      return new Promise((resolve, reject) => {
        this.$refs["form"].validate(valid => {
          if (!valid) {
            reject(new Error("表单校验失败"))
            return
          }
          sendMessage(this.buildData()).then(response => {
            resolve(response)
          }).catch(reject)
        })
      })
    },
    handleSave() {
      this.saveMessage().then(() => {
        this.$modal.msgSuccess(this.$t('oa.message.sendSuccess'))
        this.reset()
      }).catch(() => {})
    },
    handleSubmit() {
      this.saveMessage().then(() => {
        this.$modal.msgSuccess(this.$t('oa.message.sendSuccess'))
        this.goBack()
      }).catch(() => {})
    },
    goBack() {
      this.$router.back()
    }
  }
}
</script>
