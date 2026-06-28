<template>
  <oa-form-page title="盘点任务" @submit="handleSubmit" @save="handleSave" @close="handleClose">
    <template #form>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item :label="$t('oa.asset.inventoryName')" prop="name">
          <el-input v-model="form.name" :placeholder="$t('oa.asset.inventoryName')" :disabled="isDetail" />
        </el-form-item>
        <el-form-item :label="$t('oa.asset.scopeType')" prop="scopeType">
          <el-select v-model="form.scopeType" :placeholder="$t('oa.asset.scopeType')" style="width:100%" :disabled="isDetail">
            <el-option :label="$t('oa.asset.scopeAll')" :value="0" />
            <el-option :label="$t('oa.asset.scopeCategory')" :value="1" />
            <el-option :label="$t('oa.asset.scopeLocation')" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('oa.asset.scopeIds')" prop="scopeIds">
          <el-input v-model="form.scopeIds" :placeholder="$t('oa.asset.placeholder.scopeIds')" :disabled="isDetail" />
        </el-form-item>
        <el-form-item :label="$t('common.status')" prop="status">
          <el-radio-group v-model="form.status" :disabled="isDetail">
            <el-radio :label="0">{{ $t('oa.asset.inventoryStatusPending') }}</el-radio>
            <el-radio :label="1">{{ $t('oa.asset.inventoryStatusProgress') }}</el-radio>
            <el-radio :label="2">{{ $t('oa.asset.inventoryStatusDone') }}</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
    </template>
  </oa-form-page>
</template>

<script>
import OaFormPage from "@/views/oa/components/OaFormPage"
import { getInventory, addInventory, updateInventory } from "@/api/oa/asset"

export default {
  name: "OaAssetInventoryForm",
  components: {
    OaFormPage
  },
  data() {
    return {
      form: {
        id: undefined,
        name: undefined,
        scopeType: 0,
        scopeIds: undefined,
        status: 0
      },
      rules: {
        name: [
          { required: true, message: this.$t('oa.asset.required.inventoryName'), trigger: "blur" }
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
      this.loadInventory(id)
    }
  },
  methods: {
    loadInventory(id) {
      getInventory(id).then(response => {
        this.form = response.data
      }).catch(() => {})
    },
    saveInventory() {
      return new Promise((resolve, reject) => {
        this.$refs["form"].validate(valid => {
          if (!valid) {
            reject(new Error('表单校验失败'))
            return
          }
          const data = { ...this.form }
          const request = data.id != undefined ? updateInventory(data) : addInventory(data)
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
      this.saveInventory().then(() => {
        this.$modal.msgSuccess(this.form.id ? this.$t('common.editSuccess') : this.$t('common.addSuccess'))
      }).catch(() => {})
    },
    handleSubmit() {
      this.saveInventory().then(() => {
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
