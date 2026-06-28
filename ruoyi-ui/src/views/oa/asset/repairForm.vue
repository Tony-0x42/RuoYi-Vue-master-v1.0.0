<template>
  <oa-form-page title="资产维修" @submit="handleSubmit" @save="handleSave" @close="goBack">
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
          <el-form-item label="维修原因" prop="reason">
            <el-input v-model="form.reason" type="textarea" :rows="2" placeholder="请输入维修原因" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="维修费用" prop="cost">
            <el-input-number v-model="form.cost" :min="0" :precision="2" controls-position="right" style="width:100%" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="维修商" prop="vendor">
            <el-input v-model="form.vendor" placeholder="请输入维修商" />
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
      process-key="oa_asset_repair"
      :business-key="businessKey"
      :submit-api="submitApi"
      @success="handleSuccess"
    />
  </oa-form-page>
</template>

<script>
import { getAsset, repairAsset } from "@/api/oa/asset"
import OaFormPage from "@/views/oa/components/OaFormPage"
import FlowSubmitDialog from "@/components/FlowSubmitDialog"

export default {
  name: "OaAssetRepairForm",
  components: { OaFormPage, FlowSubmitDialog },
  data() {
    return {
      flowVisible: false,
      asset: {},
      form: {
        id: undefined,
        assetId: undefined,
        reason: undefined,
        cost: 0,
        vendor: undefined,
        remark: undefined
      },
      rules: {
        reason: [{ required: true, message: "请输入维修原因", trigger: "blur" }]
      }
    }
  },
  computed: {
    businessKey() {
      return this.form.id ? 'asset_repair:' + this.form.id : ''
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
            cost: this.form.cost,
            vendor: this.form.vendor,
            remark: this.form.remark
          }
          repairAsset(this.form.assetId, data).then(response => {
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
      return repairAsset(this.form.assetId, {
        id: this.form.id,
        reason: this.form.reason,
        cost: this.form.cost,
        vendor: this.form.vendor,
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
