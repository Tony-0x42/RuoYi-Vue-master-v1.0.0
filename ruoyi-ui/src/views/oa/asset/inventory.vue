<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item :label="$t('oa.asset.inventoryName')" prop="name">
        <el-input v-model="queryParams.name" :placeholder="$t('oa.asset.inventoryName')" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item :label="$t('common.status')" prop="status">
        <el-select v-model="queryParams.status" :placeholder="$t('common.status')" clearable>
          <el-option :label="$t('oa.asset.inventoryStatusPending')" :value="0" />
          <el-option :label="$t('oa.asset.inventoryStatusProgress')" :value="1" />
          <el-option :label="$t('oa.asset.inventoryStatusDone')" :value="2" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">{{ $t('common.search') }}</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{ $t('common.reset') }}</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5"><el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['oa:assetInventory:add']">{{ $t('common.add') }}</el-button></el-col>
      <el-col :span="1.5"><el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['oa:assetInventory:edit']">{{ $t('common.edit') }}</el-button></el-col>
      <el-col :span="1.5"><el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['oa:assetInventory:remove']">{{ $t('common.delete') }}</el-button></el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="inventoryList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <index-column :page="queryParams.pageNum" :size="queryParams.pageSize" />
      <el-table-column :label="$t('oa.asset.inventoryName')" align="center" prop="name" />
      <el-table-column :label="$t('oa.asset.scopeType')" align="center" prop="scopeType" width="120">
        <template slot-scope="scope">
          <span v-if="scope.row.scopeType === 0">{{ $t('oa.asset.scopeAll') }}</span>
          <span v-else-if="scope.row.scopeType === 1">{{ $t('oa.asset.scopeCategory') }}</span>
          <span v-else>{{ $t('oa.asset.scopeLocation') }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('common.status')" align="center" prop="status" width="100">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === 0">{{ $t('oa.asset.inventoryStatusPending') }}</el-tag>
          <el-tag v-else-if="scope.row.status === 1" type="warning">{{ $t('oa.asset.inventoryStatusProgress') }}</el-tag>
          <el-tag v-else type="success">{{ $t('oa.asset.inventoryStatusDone') }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="$t('oa.asset.startTime')" align="center" prop="startTime" width="160">
        <template slot-scope="scope"><span>{{ parseTime(scope.row.startTime) }}</span></template>
      </el-table-column>
      <el-table-column :label="$t('common.operation')" align="center" class-name="small-padding fixed-width" width="240">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-s-claim" @click="handleGenerate(scope.row)" v-hasPermi="['oa:assetInventory:edit']">{{ $t('oa.asset.generate') }}</el-button>
          <el-button size="mini" type="text" icon="el-icon-tickets" @click="handleItems(scope.row)" v-hasPermi="['oa:assetInventory:query']">{{ $t('oa.asset.items') }}</el-button>
          <el-button size="mini" type="text" icon="el-icon-document" @click="handleDiff(scope.row)" v-hasPermi="['oa:assetInventory:query']">{{ $t('oa.asset.diffReport') }}</el-button>
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['oa:assetInventory:edit']">{{ $t('common.edit') }}</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['oa:assetInventory:remove']">{{ $t('common.delete') }}</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="$t('oa.asset.items')" :visible.sync="itemsOpen" width="900px" append-to-body>
      <el-table :data="itemList" max-height="400">
        <el-table-column :label="$t('oa.asset.code')" align="center" prop="assetCode" />
        <el-table-column :label="$t('oa.asset.name')" align="center" prop="assetName" />
        <el-table-column :label="$t('oa.asset.bookStatus')" align="center" prop="bookStatus" width="100">
          <template slot-scope="scope"><el-tag>{{ statusText(scope.row.bookStatus) }}</el-tag></template>
        </el-table-column>
        <el-table-column :label="$t('oa.asset.actualStatus')" align="center" prop="actualStatus" width="140">
          <template slot-scope="scope">
            <el-select v-model="scope.row.actualStatus" size="mini" @change="val => handleItemChange(scope.row, val)">
              <el-option :label="$t('oa.asset.statusIdle')" :value="0" />
              <el-option :label="$t('oa.asset.statusInUse')" :value="1" />
              <el-option :label="$t('oa.asset.statusRepairing')" :value="2" />
              <el-option :label="$t('oa.asset.statusScrapped')" :value="3" />
            </el-select>
          </template>
        </el-table-column>
        <el-table-column :label="$t('oa.asset.result')" align="center" prop="result" width="100">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.result === 0" type="success">{{ $t('oa.asset.resultNormal') }}</el-tag>
            <el-tag v-else-if="scope.row.result === 1" type="warning">{{ $t('oa.asset.resultProfit') }}</el-tag>
            <el-tag v-else-if="scope.row.result === 2" type="danger">{{ $t('oa.asset.resultLoss') }}</el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>

    <el-dialog :title="$t('oa.asset.diffReport')" :visible.sync="diffOpen" width="500px" append-to-body>
      <el-descriptions :column="1" border>
        <el-descriptions-item :label="$t('oa.asset.diffTotal')">{{ diffData.total }}</el-descriptions-item>
        <el-descriptions-item :label="$t('oa.asset.diffNormal')">{{ diffData.normal }}</el-descriptions-item>
        <el-descriptions-item :label="$t('oa.asset.diffProfit')">{{ diffData.profit }}</el-descriptions-item>
        <el-descriptions-item :label="$t('oa.asset.diffLoss')">{{ diffData.loss }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
import { listInventory, delInventory, generateInventory, diffInventory, listInventoryItems, updateInventoryItem } from "@/api/oa/asset"

export default {
  name: "OaAssetInventory",
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      inventoryList: [],
      itemList: [],
      currentInventoryId: undefined,
      itemsOpen: false,
      diffOpen: false,
      diffData: {},
      queryParams: { pageNum: 1, pageSize: 10, name: undefined, status: undefined }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      listInventory(this.queryParams).then(response => {
        this.inventoryList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    handleQuery() { this.queryParams.pageNum = 1; this.getList() },
    resetQuery() { this.resetForm("queryForm"); this.handleQuery() },
    handleSelectionChange(selection) { this.ids = selection.map(item => item.id); this.single = selection.length != 1; this.multiple = !selection.length },
    handleAdd() { this.$router.push({ path: '/oa/assetDir/assetInventory/form', query: { mode: 'add' } }) },
    handleUpdate(row) {
      const id = row.id || this.ids
      this.$router.push({ path: '/oa/assetDir/assetInventory/form', query: { mode: 'edit', id: id } })
    },
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm(this.$t('oa.asset.confirm.deleteInventory', { ids })).then(function() { return delInventory(ids) }).then(() => { this.getList(); this.$modal.msgSuccess(this.$t('common.deleteSuccess')) }).catch(() => {})
    },
    handleGenerate(row) {
      this.$modal.confirm(this.$t('oa.asset.confirm.generate', { name: row.name })).then(() => generateInventory(row.id)).then(() => { this.$modal.msgSuccess(this.$t('common.success')); this.getList() }).catch(() => {})
    },
    handleItems(row) {
      this.currentInventoryId = row.id
      listInventoryItems(row.id).then(response => { this.itemList = response.rows || []; this.itemsOpen = true })
    },
    handleDiff(row) {
      diffInventory(row.id).then(response => { this.diffData = response.data || {}; this.diffOpen = true })
    },
    handleItemChange(row, val) {
      row.result = (val == null || val !== row.bookStatus) ? 2 : 0
      updateInventoryItem(this.currentInventoryId, row.id, row).then(() => { this.$modal.msgSuccess(this.$t('common.editSuccess')) })
    },
    statusText(status) {
      if (status === 0) return this.$t('oa.asset.statusIdle')
      if (status === 1) return this.$t('oa.asset.statusInUse')
      if (status === 2) return this.$t('oa.asset.statusRepairing')
      if (status === 3) return this.$t('oa.asset.statusScrapped')
      return '-'
    }
  }
}
</script>
