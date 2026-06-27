<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item :label="$t('oa.asset.categoryName')" prop="name">
        <el-input v-model="queryParams.name" :placeholder="$t('oa.asset.categoryName')" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item :label="$t('common.status')" prop="status">
        <el-select v-model="queryParams.status" :placeholder="$t('common.status')" clearable>
          <el-option :label="$t('oa.asset.enable')" :value="1" />
          <el-option :label="$t('oa.asset.disable')" :value="0" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">{{ $t('common.search') }}</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{ $t('common.reset') }}</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['oa:assetCategory:add']">{{ $t('common.add') }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['oa:assetCategory:edit']">{{ $t('common.edit') }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['oa:assetCategory:remove']">{{ $t('common.delete') }}</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="categoryList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <index-column :page="queryParams.pageNum" :size="queryParams.pageSize" />
      <el-table-column :label="$t('oa.asset.categoryName')" align="center" prop="name" />
      <el-table-column :label="$t('oa.asset.categoryCode')" align="center" prop="code" />
      <el-table-column :label="$t('oa.asset.depreciationYears')" align="center" prop="depreciationYears" width="100" />
      <el-table-column :label="$t('common.status')" align="center" prop="status" width="100">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === 1" type="success">{{ $t('oa.asset.enable') }}</el-tag>
          <el-tag v-else type="danger">{{ $t('oa.asset.disable') }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="$t('common.createTime')" align="center" prop="createTime" width="160">
        <template slot-scope="scope"><span>{{ parseTime(scope.row.createTime) }}</span></template>
      </el-table-column>
      <el-table-column :label="$t('common.operation')" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['oa:assetCategory:edit']">{{ $t('common.edit') }}</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['oa:assetCategory:remove']">{{ $t('common.delete') }}</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item :label="$t('oa.asset.categoryName')" prop="name">
          <el-input v-model="form.name" :placeholder="$t('oa.asset.categoryName')" />
        </el-form-item>
        <el-form-item :label="$t('oa.asset.categoryCode')" prop="code">
          <el-input v-model="form.code" :placeholder="$t('oa.asset.categoryCode')" />
        </el-form-item>
        <el-form-item :label="$t('oa.asset.depreciationYears')" prop="depreciationYears">
          <el-input-number v-model="form.depreciationYears" :min="0" :max="100" controls-position="right" />
        </el-form-item>
        <el-form-item :label="$t('common.status')" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">{{ $t('oa.asset.enable') }}</el-radio>
            <el-radio :label="0">{{ $t('oa.asset.disable') }}</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">{{ $t('common.submit') }}</el-button>
        <el-button @click="cancel">{{ $t('common.cancel') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listCategory, getCategory, addCategory, updateCategory, delCategory } from "@/api/oa/asset"

export default {
  name: "OaAssetCategory",
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      categoryList: [],
      title: "",
      open: false,
      queryParams: { pageNum: 1, pageSize: 10, name: undefined, status: undefined },
      form: {},
      rules: {
        name: [{ required: true, message: this.$t('oa.asset.required.categoryName'), trigger: "blur" }],
        code: [{ required: true, message: this.$t('oa.asset.required.categoryCode'), trigger: "blur" }]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      listCategory(this.queryParams).then(response => {
        this.categoryList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = { id: undefined, name: undefined, code: undefined, depreciationYears: 5, status: 1, sort: 0, parentId: 0 }
      this.resetForm("form")
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.resetForm("queryForm")
      this.handleQuery()
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = this.$t('oa.asset.addCategory')
    },
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getCategory(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = this.$t('oa.asset.editCategory')
      })
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != undefined) {
            updateCategory(this.form).then(() => { this.$modal.msgSuccess(this.$t('common.editSuccess')); this.open = false; this.getList() })
          } else {
            addCategory(this.form).then(() => { this.$modal.msgSuccess(this.$t('common.addSuccess')); this.open = false; this.getList() })
          }
        }
      })
    },
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm(this.$t('oa.asset.confirm.deleteCategory', { ids })).then(function() { return delCategory(ids) }).then(() => { this.getList(); this.$modal.msgSuccess(this.$t('common.deleteSuccess')) }).catch(() => {})
    }
  }
}
</script>
