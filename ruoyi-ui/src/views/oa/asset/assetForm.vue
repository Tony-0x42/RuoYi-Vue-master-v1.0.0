<template>
  <oa-form-page :title="title" @submit="handleSubmit" @save="handleSave" @close="handleClose">
    <template #form>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item :label="$t('oa.asset.code')" prop="code">
              <el-input v-model="form.code" :placeholder="$t('oa.asset.placeholder.code')" :disabled="isDetail" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('oa.asset.name')" prop="name">
              <el-input v-model="form.name" :placeholder="$t('oa.asset.placeholder.name')" :disabled="isDetail" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('oa.asset.category')" prop="categoryId">
              <el-select v-model="form.categoryId" :placeholder="$t('oa.asset.category')" style="width:100%" :disabled="isDetail">
                <el-option v-for="item in categoryList" :key="item.id" :label="item.name" :value="item.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('common.status')" prop="status">
              <el-select v-model="form.status" :placeholder="$t('common.status')" style="width:100%" :disabled="isDetail">
                <el-option :label="$t('oa.asset.statusIdle')" :value="0" />
                <el-option :label="$t('oa.asset.statusInUse')" :value="1" />
                <el-option :label="$t('oa.asset.statusRepairing')" :value="2" />
                <el-option :label="$t('oa.asset.statusScrapped')" :value="3" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('oa.asset.model')" prop="model">
              <el-input v-model="form.model" :placeholder="$t('oa.asset.placeholder.model')" :disabled="isDetail" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('oa.asset.purchaseDate')" prop="purchaseDate">
              <el-date-picker v-model="form.purchaseDate" type="date" :placeholder="$t('oa.asset.purchaseDate')" value-format="yyyy-MM-dd" style="width:100%" :disabled="isDetail" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('oa.asset.value')" prop="value">
              <el-input-number v-model="form.value" :min="0" :precision="2" controls-position="right" style="width:100%" :disabled="isDetail" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('oa.asset.location')" prop="location">
              <el-input v-model="form.location" :placeholder="$t('oa.asset.placeholder.location')" :disabled="isDetail" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="$t('oa.asset.spec')" prop="spec">
              <el-input v-model="form.spec" type="textarea" :rows="2" :placeholder="$t('oa.asset.placeholder.spec')" :disabled="isDetail" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </template>
  </oa-form-page>
</template>

<script>
import OaFormPage from "@/views/oa/components/OaFormPage"
import { getAsset, addAsset, updateAsset, listCategory } from "@/api/oa/asset"

export default {
  name: "OaAssetForm",
  components: {
    OaFormPage
  },
  data() {
    return {
      categoryList: [],
      form: {
        id: undefined,
        code: undefined,
        name: undefined,
        categoryId: undefined,
        model: undefined,
        spec: undefined,
        purchaseDate: undefined,
        value: 0,
        location: undefined,
        status: 0
      },
      rules: {
        name: [{ required: true, message: this.$t('oa.asset.required.name'), trigger: "blur" }],
        categoryId: [{ required: true, message: this.$t('oa.asset.required.category'), trigger: "change" }]
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
    title() {
      if (this.mode === 'detail') return this.$t('oa.asset.viewAsset')
      return this.form.id != undefined ? this.$t('oa.asset.editAsset') : this.$t('oa.asset.addAsset')
    }
  },
  mounted() {
    this.getCategoryList()
    const id = this.$route.query.id
    if (id) {
      this.getAsset(id)
    }
  },
  methods: {
    getCategoryList() {
      listCategory({ pageNum: 1, pageSize: 1000, status: 1 }).then(response => {
        this.categoryList = response.rows
      })
    },
    getAsset(id) {
      getAsset(id).then(response => {
        this.form = response.data
      }).catch(() => {})
    },
    saveAsset(back) {
      this.$refs["form"].validate(valid => {
        if (!valid) {
          return
        }
        const data = { ...this.form }
        if (data.id != undefined) {
          updateAsset(data).then(() => {
            this.$modal.msgSuccess(this.$t('common.editSuccess'))
            if (back) {
              this.handleClose()
            }
          }).catch(() => {})
        } else {
          addAsset(data).then(response => {
            this.form.id = response.data
            this.$modal.msgSuccess(this.$t('common.addSuccess'))
            if (back) {
              this.handleClose()
            }
          }).catch(() => {})
        }
      })
    },
    handleSave() {
      this.saveAsset(false)
    },
    handleSubmit() {
      this.saveAsset(true)
    },
    handleClose() {
      this.$router.push('/oa/assetDir/asset')
    }
  }
}
</script>
