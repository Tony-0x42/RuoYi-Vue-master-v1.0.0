<template>
  <div class="app-container">
    <el-page-header :content="asset.name || $t('oa.asset.detail')" @back="goBack" />
    <el-card class="box-card" style="margin-top:15px">
      <div slot="header" class="clearfix">
        <span>{{ $t('oa.asset.basicInfo') }}</span>
      </div>
      <el-descriptions :column="3" border>
        <el-descriptions-item :label="$t('oa.asset.code')">{{ asset.code }}</el-descriptions-item>
        <el-descriptions-item :label="$t('oa.asset.name')">{{ asset.name }}</el-descriptions-item>
        <el-descriptions-item :label="$t('oa.asset.category')">{{ asset.categoryName || '-' }}</el-descriptions-item>
        <el-descriptions-item :label="$t('oa.asset.model')">{{ asset.model || '-' }}</el-descriptions-item>
        <el-descriptions-item :label="$t('oa.asset.spec')">{{ asset.spec || '-' }}</el-descriptions-item>
        <el-descriptions-item :label="$t('oa.asset.purchaseDate')">{{ asset.purchaseDate }}</el-descriptions-item>
        <el-descriptions-item :label="$t('oa.asset.value')">{{ asset.value }}</el-descriptions-item>
        <el-descriptions-item :label="$t('oa.asset.userName')">{{ asset.userName || '-' }}</el-descriptions-item>
        <el-descriptions-item :label="$t('oa.asset.location')">{{ asset.location || '-' }}</el-descriptions-item>
        <el-descriptions-item :label="$t('common.status')">
          <el-tag v-if="asset.status === 0" type="info">{{ $t('oa.asset.statusIdle') }}</el-tag>
          <el-tag v-else-if="asset.status === 1" type="success">{{ $t('oa.asset.statusInUse') }}</el-tag>
          <el-tag v-else-if="asset.status === 2" type="warning">{{ $t('oa.asset.statusRepairing') }}</el-tag>
          <el-tag v-else type="danger">{{ $t('oa.asset.statusScrapped') }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item :label="$t('common.createTime')">{{ parseTime(asset.createTime) }}</el-descriptions-item>
      </el-descriptions>
    </el-card>

    <el-card class="box-card" style="margin-top:15px">
      <div slot="header" class="clearfix">
        <span>{{ $t('oa.asset.qrcode') }}</span>
      </div>
      <div style="text-align:center">
        <img v-if="qrcode" :src="qrcode" style="width:200px;height:200px" />
        <p>{{ $t('oa.asset.qrcodeTip') }}</p>
      </div>
    </el-card>
  </div>
</template>

<script>
import { getAsset, qrcodeAsset } from "@/api/oa/asset"

export default {
  name: "OaAssetDetail",
  data() {
    return {
      asset: {},
      qrcode: ""
    }
  },
  created() {
    this.loadData()
  },
  methods: {
    loadData() {
      const id = this.$route.params.id
      if (!id) { this.goBack(); return }
      getAsset(id).then(response => {
        this.asset = response.data || {}
        if (this.asset.code) {
          qrcodeAsset(this.asset.code).then(res => { this.qrcode = (res.data && res.data.qrcode) || "" })
        }
      })
    },
    goBack() {
      this.$router.push('/oa/asset')
    }
  }
}
</script>
