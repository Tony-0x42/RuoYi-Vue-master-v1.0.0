<template>
  <oa-form-page title="资产报废" @submit="handleSubmit" @save="handleSave" @close="goBack">
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
        <el-col :span="24">
          <el-form-item label="报废原因" prop="reason">
            <el-input v-model="form.reason" type="textarea" :rows="2" placeholder="请输入报废原因" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="处置方式" prop="disposalMethod">
            <el-input v-model="form.disposalMethod" placeholder="请输入处置方式" />
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
      process-key="oa_asset_scrap"
      :business-key="businessKey"
      :submit-api="submitApi"
      @success="handleSuccess"
    />
  </oa-form-page>
</template>

<script>
import { getAsset, scrapAsset } from "@/api/oa/asset"
import OaFormPage from "@/views/oa/components/OaFormPage"
import FlowSubmitDialog from "@/components/FlowSubmitDialog"

export default {
  name: "OaAssetScrapForm",
  components: { OaFormPage, FlowSubmitDialog },
  data() {
    return {
      flowVisible: false,
      asset: {},
      form: {
        id: undefined,
        assetId: undefined,
        reason: undefined,
        disposalMethod: undefined,
        remark: undefined
      },
      rules: {
        reason: [{ required: true, message: "请输入报废原因", trigger: "blur" }],
        disposalMethod: [{ required: true, message: "请输入处置方式", trigger: "blur" }]
      }
    }
  },
  computed: {
    businessKey() {
      return this.form.id ? 'asset_scrap:' + this.form.id : ''
    }
  },
  created() {
    const assetId = this.$route.query.assetId
    if (assetId) {
      this.form.assetId = parseInt(assetId)
      this.loadAsset(assetId)
    }
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
            reason: this.form.reason,
            disposalMethod: this.form.disposalMethod,
            remark: this.form.remark
          }
          scrapAsset(this.form.assetId, data).then(response => {
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
      return scrapAsset(this.form.assetId, {
        id: this.form.id,
        reason: this.form.reason,
        disposalMethod: this.form.disposalMethod,
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
