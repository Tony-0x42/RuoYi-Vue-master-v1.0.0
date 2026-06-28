<template>
  <oa-form-page title="资产调拨" @submit="handleSubmit" @save="handleSave" @close="goBack">
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
          <el-form-item label="原使用人">
            <el-input v-model="form.fromUserName" disabled />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="目标使用人ID" prop="toUserId">
            <el-input-number v-model="form.toUserId" :min="1" controls-position="right" style="width:100%" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="目标使用人" prop="toUserName">
            <el-input v-model="form.toUserName" placeholder="请输入目标使用人姓名" />
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
      process-key="oa_asset_transfer"
      :business-key="businessKey"
      :submit-api="submitApi"
      @success="handleSuccess"
    />
  </oa-form-page>
</template>

<script>
import { getAsset, transferAsset } from "@/api/oa/asset"
import OaFormPage from "@/views/oa/components/OaFormPage"
import FlowSubmitDialog from "@/components/FlowSubmitDialog"

export default {
  name: "OaAssetTransferForm",
  components: { OaFormPage, FlowSubmitDialog },
  data() {
    return {
      flowVisible: false,
      asset: {},
      form: {
        id: undefined,
        assetId: undefined,
        fromUserId: undefined,
        fromUserName: undefined,
        toUserId: undefined,
        toUserName: undefined,
        remark: undefined
      },
      rules: {
        toUserId: [{ required: true, message: "请输入目标使用人ID", trigger: "blur" }],
        toUserName: [{ required: true, message: "请输入目标使用人姓名", trigger: "blur" }]
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
      return this.form.id ? 'asset_transfer:' + this.form.id : ''
    }
  },
  created() {
    const assetId = this.$route.query.assetId
    if (assetId) {
      this.form.assetId = parseInt(assetId)
      this.loadAsset(assetId)
    }
    this.form.fromUserId = this.currentUserId
    this.form.fromUserName = this.currentUserName
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
            fromUserId: this.form.fromUserId,
            fromUserName: this.form.fromUserName,
            toUserId: this.form.toUserId,
            toUserName: this.form.toUserName,
            remark: this.form.remark
          }
          transferAsset(this.form.assetId, data).then(response => {
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
      return transferAsset(this.form.assetId, {
        id: this.form.id,
        fromUserId: this.form.fromUserId,
        fromUserName: this.form.fromUserName,
        toUserId: this.form.toUserId,
        toUserName: this.form.toUserName,
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
