<template>
  <oa-form-page title="资产领用" @submit="handleSubmit" @save="handleSave" @close="goBack">
    <el-form ref="form" :model="form" :rules="rules" label-width="100px" slot="form">
      <el-row>
        <el-col :span="12">
          <el-form-item label="资产编码">
            <el-input v-model="asset.code" disabled />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="资产名称">
            <el-input v-model="asset.name" disabled />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="领用人" prop="userName">
            <el-input v-model="form.userName" placeholder="请输入领用人姓名" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="领用人ID" prop="userId">
            <el-input-number v-model="form.userId" :min="1" controls-position="right" style="width:100%" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="备注" prop="remark">
            <el-input v-model="form.remark" type="textarea" :rows="2" placeholder="请输入备注" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <flow-submit-dialog
      :visible.sync="flowVisible"
      process-key="oa_asset_receive"
      :business-key="businessKey"
      :submit-api="submitApi"
      @success="handleSuccess"
    />
  </oa-form-page>
</template>

<script>
import { getAsset, receiveAsset } from "@/api/oa/asset"
import OaFormPage from "@/views/oa/components/OaFormPage"
import FlowSubmitDialog from "@/components/FlowSubmitDialog"

export default {
  name: "OaAssetReceiveForm",
  components: { OaFormPage, FlowSubmitDialog },
  data() {
    return {
      flowVisible: false,
      asset: {},
      form: {
        id: undefined,
        assetId: undefined,
        userId: undefined,
        userName: undefined,
        remark: undefined
      },
      rules: {
        userName: [{ required: true, message: "请输入领用人姓名", trigger: "blur" }],
        userId: [{ required: true, message: "请输入领用人ID", trigger: "blur" }]
      }
    }
  },
  computed: {
    currentUserId() {
      const id = this.$store.state.user.id
      return id ? parseInt(id) : 1
    },
    currentUserName() {
      return this.$store.state.user.name || 'admin'
    },
    businessKey() {
      return this.form.id ? 'asset_receive:' + this.form.id : ''
    }
  },
  created() {
    const assetId = this.$route.query.assetId
    if (assetId) {
      this.form.assetId = parseInt(assetId)
      this.loadAsset(assetId)
    }
    this.form.userId = this.currentUserId
    this.form.userName = this.currentUserName
  },
  methods: {
    loadAsset(id) {
      getAsset(id).then(response => {
        this.asset = response.data || {}
      })
    },
    saveRecord() {
      return new Promise((resolve, reject) => {
        this.$refs["form"].validate(valid => {
          if (!valid) {
            reject(new Error("表单校验失败"))
            return
          }
          const data = {
            userId: this.form.userId,
            userName: this.form.userName,
            remark: this.form.remark
          }
          receiveAsset(this.form.assetId, data).then(response => {
            if (response.data) {
              this.form.id = response.data
            }
            resolve(response)
          }).catch(reject)
        })
      })
    },
    handleSave() {
      this.saveRecord().then(() => {
        this.$modal.msgSuccess("保存成功")
      }).catch(() => {})
    },
    handleSubmit() {
      this.saveRecord().then(() => {
        this.flowVisible = true
      }).catch(() => {})
    },
    submitApi(data) {
      return receiveAsset(this.form.assetId, {
        id: this.form.id,
        userId: this.form.userId,
        userName: this.form.userName,
        remark: this.form.remark,
        approvalAssignee: data.approvalAssignee
      })
    },
    handleSuccess() {
      this.$modal.msgSuccess("提交成功")
      this.goBack()
    },
    goBack() {
      this.$router.back()
    }
  }
}
</script>
