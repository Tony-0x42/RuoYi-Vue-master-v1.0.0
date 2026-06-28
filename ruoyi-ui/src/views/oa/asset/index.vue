<template>
  <div class="app-container">
    <el-row :gutter="20" class="mb8">
      <el-col :span="4"><el-card shadow="hover"><div class="stat-title">{{ $t('oa.asset.total') }}</div><div class="stat-num">{{ stats.total }}</div></el-card></el-col>
      <el-col :span="4"><el-card shadow="hover"><div class="stat-title">{{ $t('oa.asset.idle') }}</div><div class="stat-num">{{ stats.idle }}</div></el-card></el-col>
      <el-col :span="4"><el-card shadow="hover"><div class="stat-title">{{ $t('oa.asset.inUse') }}</div><div class="stat-num">{{ stats.inUse }}</div></el-card></el-col>
      <el-col :span="4"><el-card shadow="hover"><div class="stat-title">{{ $t('oa.asset.repairing') }}</div><div class="stat-num">{{ stats.repairing }}</div></el-card></el-col>
      <el-col :span="4"><el-card shadow="hover"><div class="stat-title">{{ $t('oa.asset.scrapped') }}</div><div class="stat-num">{{ stats.scrapped }}</div></el-card></el-col>
      <el-col :span="4"><el-card shadow="hover"><div class="stat-title">{{ $t('oa.asset.totalValue') }}</div><div class="stat-num">{{ stats.totalValue }}</div></el-card></el-col>
    </el-row>

    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item :label="$t('oa.asset.code')" prop="code">
        <el-input v-model="queryParams.code" :placeholder="$t('oa.asset.code')" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item :label="$t('oa.asset.name')" prop="name">
        <el-input v-model="queryParams.name" :placeholder="$t('oa.asset.name')" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item :label="$t('oa.asset.category')" prop="categoryId">
        <el-select v-model="queryParams.categoryId" :placeholder="$t('oa.asset.category')" clearable style="width:160px">
          <el-option v-for="item in categoryList" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('common.status')" prop="status">
        <el-select v-model="queryParams.status" :placeholder="$t('common.status')" clearable style="width:120px">
          <el-option :label="$t('oa.asset.statusIdle')" :value="0" />
          <el-option :label="$t('oa.asset.statusInUse')" :value="1" />
          <el-option :label="$t('oa.asset.statusRepairing')" :value="2" />
          <el-option :label="$t('oa.asset.statusScrapped')" :value="3" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">{{ $t('common.search') }}</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{ $t('common.reset') }}</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5"><el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['oa:asset:add']">{{ $t('common.add') }}</el-button></el-col>
      <el-col :span="1.5"><el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['oa:asset:edit']">{{ $t('common.edit') }}</el-button></el-col>
      <el-col :span="1.5"><el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['oa:asset:remove']">{{ $t('common.delete') }}</el-button></el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="assetList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <index-column :page="queryParams.pageNum" :size="queryParams.pageSize" />
      <el-table-column :label="$t('oa.asset.code')" align="center" prop="code" width="140" />
      <el-table-column :label="$t('oa.asset.name')" align="center" prop="name" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <a class="link-type" style="cursor:pointer" @click="handleView(scope.row)">{{ scope.row.name }}</a>
        </template>
      </el-table-column>
      <el-table-column :label="$t('oa.asset.category')" align="center" prop="categoryName" width="120" />
      <el-table-column :label="$t('oa.asset.model')" align="center" prop="model" width="120" />
      <el-table-column :label="$t('oa.asset.userName')" align="center" prop="userName" width="120" />
      <el-table-column :label="$t('oa.asset.location')" align="center" prop="location" width="120" />
      <el-table-column :label="$t('common.status')" align="center" prop="status" width="100">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === 0" type="info">{{ $t('oa.asset.statusIdle') }}</el-tag>
          <el-tag v-else-if="scope.row.status === 1" type="success">{{ $t('oa.asset.statusInUse') }}</el-tag>
          <el-tag v-else-if="scope.row.status === 2" type="warning">{{ $t('oa.asset.statusRepairing') }}</el-tag>
          <el-tag v-else type="danger">{{ $t('oa.asset.statusScrapped') }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="$t('oa.asset.value')" align="center" prop="value" width="100" />
      <el-table-column :label="$t('common.createTime')" align="center" prop="createTime" width="160">
        <template slot-scope="scope"><span>{{ parseTime(scope.row.createTime) }}</span></template>
      </el-table-column>
      <el-table-column :label="$t('common.operation')" align="center" class-name="small-padding fixed-width" width="280">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleView(scope.row)">{{ $t('oa.asset.view') }}</el-button>
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['oa:asset:edit']">{{ $t('common.edit') }}</el-button>
          <el-button size="mini" type="text" icon="el-icon-s-custom" @click="handleReceive(scope.row)" v-hasPermi="['oa:asset:edit']" v-if="scope.row.status === 0">{{ $t('oa.asset.receive') }}</el-button>
          <el-button size="mini" type="text" icon="el-icon-refresh-left" @click="handleReturn(scope.row)" v-hasPermi="['oa:asset:edit']" v-if="scope.row.status === 1">{{ $t('oa.asset.return') }}</el-button>
          <el-button size="mini" type="text" icon="el-icon-s-promotion" @click="handleTransfer(scope.row)" v-hasPermi="['oa:asset:edit']" v-if="scope.row.status === 1">{{ $t('oa.asset.transfer') }}</el-button>
          <el-button size="mini" type="text" icon="el-icon-s-tools" @click="handleRepair(scope.row)" v-hasPermi="['oa:asset:edit']" v-if="scope.row.status !== 3">{{ $t('oa.asset.repair') }}</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete-solid" @click="handleScrap(scope.row)" v-hasPermi="['oa:asset:edit']" v-if="scope.row.status !== 3">{{ $t('oa.asset.scrap') }}</el-button>
          <el-button size="mini" type="text" icon="el-icon-full-screen" @click="handleQrcode(scope.row)" v-hasPermi="['oa:asset:query']">{{ $t('oa.asset.qrcode') }}</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item :label="$t('oa.asset.code')" prop="code">
              <el-input v-model="form.code" :placeholder="$t('oa.asset.placeholder.code')" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('oa.asset.name')" prop="name">
              <el-input v-model="form.name" :placeholder="$t('oa.asset.placeholder.name')" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('oa.asset.category')" prop="categoryId">
              <el-select v-model="form.categoryId" :placeholder="$t('oa.asset.category')" style="width:100%">
                <el-option v-for="item in categoryList" :key="item.id" :label="item.name" :value="item.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('common.status')" prop="status">
              <el-select v-model="form.status" :placeholder="$t('common.status')" style="width:100%">
                <el-option :label="$t('oa.asset.statusIdle')" :value="0" />
                <el-option :label="$t('oa.asset.statusInUse')" :value="1" />
                <el-option :label="$t('oa.asset.statusRepairing')" :value="2" />
                <el-option :label="$t('oa.asset.statusScrapped')" :value="3" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('oa.asset.model')" prop="model">
              <el-input v-model="form.model" :placeholder="$t('oa.asset.placeholder.model')" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('oa.asset.purchaseDate')" prop="purchaseDate">
              <el-date-picker v-model="form.purchaseDate" type="date" :placeholder="$t('oa.asset.purchaseDate')" value-format="yyyy-MM-dd" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('oa.asset.value')" prop="value">
              <el-input-number v-model="form.value" :min="0" :precision="2" controls-position="right" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('oa.asset.location')" prop="location">
              <el-input v-model="form.location" :placeholder="$t('oa.asset.placeholder.location')" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="$t('oa.asset.spec')" prop="spec">
              <el-input v-model="form.spec" type="textarea" :rows="2" :placeholder="$t('oa.asset.placeholder.spec')" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">{{ $t('common.submit') }}</el-button>
        <el-button @click="cancel">{{ $t('common.cancel') }}</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="$t('oa.asset.qrcode')" :visible.sync="qrcodeOpen" width="400px" append-to-body>
      <div style="text-align:center">
        <img v-if="qrcodeData.qrcode" :src="qrcodeData.qrcode" style="width:200px;height:200px" />
        <p>{{ qrcodeData.asset && qrcodeData.asset.code }}</p>
        <p>{{ qrcodeData.asset && qrcodeData.asset.name }}</p>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listAsset, getAsset, addAsset, updateAsset, delAsset, returnAsset, qrcodeAsset, statisticsAsset, listCategory } from "@/api/oa/asset"

export default {
  name: "OaAsset",
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      assetList: [],
      categoryList: [],
      stats: { total: 0, idle: 0, inUse: 0, repairing: 0, scrapped: 0, totalValue: 0 },
      title: "",
      open: false,
      qrcodeOpen: false,
      qrcodeData: {},
      queryParams: { pageNum: 1, pageSize: 10, code: undefined, name: undefined, categoryId: undefined, status: undefined },
      form: {},
      rules: {
        name: [{ required: true, message: this.$t('oa.asset.required.name'), trigger: "blur" }],
        categoryId: [{ required: true, message: this.$t('oa.asset.required.category'), trigger: "change" }]
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
    }
  },
  created() {
    this.getCategoryList()
    this.getStatistics()
    this.getList()
  },
  methods: {
    getCategoryList() {
      listCategory({ pageNum: 1, pageSize: 1000, status: 1 }).then(response => { this.categoryList = response.rows })
    },
    getStatistics() {
      statisticsAsset().then(response => { this.stats = response.data || this.stats })
    },
    getList() {
      this.loading = true
      listAsset(this.queryParams).then(response => {
        this.assetList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    cancel() { this.open = false; this.reset() },
    reset() {
      this.form = { id: undefined, code: undefined, name: undefined, categoryId: undefined, model: undefined, spec: undefined, purchaseDate: undefined, value: 0, location: undefined, status: 0 }
      this.resetForm("form")
    },
    handleQuery() { this.queryParams.pageNum = 1; this.getList() },
    resetQuery() { this.resetForm("queryForm"); this.handleQuery() },
    handleSelectionChange(selection) { this.ids = selection.map(item => item.id); this.single = selection.length != 1; this.multiple = !selection.length },
    handleAdd() { this.reset(); this.open = true; this.title = this.$t('oa.asset.addAsset') },
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getAsset(id).then(response => { this.form = response.data; this.open = true; this.title = this.$t('oa.asset.editAsset') })
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          const data = { ...this.form }
          if (data.id != undefined) {
            updateAsset(data).then(() => { this.$modal.msgSuccess(this.$t('common.editSuccess')); this.open = false; this.getList(); this.getStatistics() })
          } else {
            addAsset(data).then(() => { this.$modal.msgSuccess(this.$t('common.addSuccess')); this.open = false; this.getList(); this.getStatistics() })
          }
        }
      })
    },
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm(this.$t('oa.asset.confirm.deleteAsset', { ids })).then(function() { return delAsset(ids) }).then(() => { this.getList(); this.getStatistics(); this.$modal.msgSuccess(this.$t('common.deleteSuccess')) }).catch(() => {})
    },
    handleView(row) { this.$router.push('/oa/asset/detail/' + row.id) },
    handleQrcode(row) {
      qrcodeAsset(row.code).then(response => { this.qrcodeData = response.data || {}; this.qrcodeOpen = true })
    },
    handleReceive(row) {
      this.$router.push({ path: '/oa/asset/receive', query: { assetId: row.id } })
    },
    handleReturn(row) {
      this.$modal.confirm(this.$t('oa.asset.confirm.return', { name: row.name })).then(() => {
        return returnAsset(row.id, this.currentUserId, this.currentUserName)
      }).then(() => { this.$modal.msgSuccess(this.$t('common.success')); this.getList(); this.getStatistics() }).catch(() => {})
    },
    handleTransfer(row) {
      this.$router.push({ path: '/oa/asset/transfer', query: { assetId: row.id } })
    },
    handleRepair(row) {
      this.$router.push({ path: '/oa/asset/repair', query: { assetId: row.id } })
    },
    handleScrap(row) {
      this.$router.push({ path: '/oa/asset/scrap', query: { assetId: row.id } })
    }
  }
}
</script>

<style scoped>
.stat-title { color: #909399; font-size: 13px; text-align: center; }
.stat-num { font-size: 22px; font-weight: bold; text-align: center; margin-top: 8px; color: #303133; }
</style>
