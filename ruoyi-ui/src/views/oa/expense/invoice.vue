<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item :label="$t('oa.expense.invoiceNo')" prop="invoiceNo">
        <el-input
          v-model="queryParams.invoiceNo"
          :placeholder="$t('oa.expense.placeholder.invoiceNo')"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="$t('common.status')" prop="status">
        <el-select v-model="queryParams.status" :placeholder="$t('common.status')" clearable>
          <el-option :label="$t('oa.expense.invoiceWaitRecognize')" :value="0" />
          <el-option :label="$t('oa.expense.invoiceRecognized')" :value="1" />
          <el-option :label="$t('oa.expense.invoiceVerified')" :value="2" />
          <el-option :label="$t('oa.expense.invoiceUsed')" :value="3" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">{{ $t('common.search') }}</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{ $t('common.reset') }}</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['oa:expenseInvoice:add']"
        >{{ $t('common.add') }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-view"
          size="mini"
          :disabled="single"
          @click="handleRecognize"
          v-hasPermi="['oa:expenseInvoice:edit']"
        >{{ $t('oa.expense.recognize') }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-check"
          size="mini"
          :disabled="single"
          @click="handleVerify"
          v-hasPermi="['oa:expenseInvoice:edit']"
        >{{ $t('oa.expense.verify') }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['oa:expenseInvoice:remove']"
        >{{ $t('common.delete') }}</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="invoiceList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <index-column :page="queryParams.pageNum" :size="queryParams.pageSize" />
      <el-table-column :label="$t('oa.expense.invoiceCode')" align="center" prop="invoiceCode" width="150" />
      <el-table-column :label="$t('oa.expense.invoiceNo')" align="center" prop="invoiceNo" width="120" />
      <el-table-column :label="$t('oa.expense.invoiceType')" align="center" prop="invoiceType" width="150" />
      <el-table-column :label="$t('oa.expense.amount')" align="center" prop="amount" width="120" />
      <el-table-column :label="$t('oa.expense.invoiceDate')" align="center" prop="invoiceDate" width="120">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.invoiceDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('oa.expense.seller')" align="center" prop="seller" :show-overflow-tooltip="true" />
      <el-table-column :label="$t('common.status')" align="center" prop="status" width="120">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === 0" type="info">{{ $t('oa.expense.invoiceWaitRecognize') }}</el-tag>
          <el-tag v-else-if="scope.row.status === 1" type="warning">{{ $t('oa.expense.invoiceRecognized') }}</el-tag>
          <el-tag v-else-if="scope.row.status === 2" type="success">{{ $t('oa.expense.invoiceVerified') }}</el-tag>
          <el-tag v-else type="info">{{ $t('oa.expense.invoiceUsed') }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="$t('common.operation')" align="center" class-name="small-padding fixed-width" width="180">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['oa:expenseInvoice:edit']"
          >{{ $t('common.edit') }}</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['oa:expenseInvoice:remove']"
          >{{ $t('common.delete') }}</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
  </div>
</template>

<script>
import { listInvoice, delInvoice, recognizeInvoice, verifyInvoice, getInvoice } from "@/api/oa/expense"

export default {
  name: "OaExpenseInvoice",
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      invoiceList: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        invoiceNo: undefined,
        status: undefined
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      listInvoice(this.queryParams).then(response => {
        this.invoiceList = response.rows
        this.total = response.total
        this.loading = false
      })
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
      this.$router.push({ path: '/oa/expenseDir/expenseInvoice/form' })
    },
    handleUpdate(row) {
      const id = row.id || this.ids
      this.$router.push({ path: '/oa/expenseDir/expenseInvoice/form', query: { id } })
    },
    handleRecognize() {
      const id = this.ids[0]
      getInvoice(id).then(response => {
        recognizeInvoice(response.data).then(res => {
          this.$modal.msgSuccess(this.$t('oa.expense.recognizeSuccess'))
          this.getList()
        })
      })
    },
    handleVerify() {
      const id = this.ids[0]
      getInvoice(id).then(response => {
        verifyInvoice(response.data).then(res => {
          this.$modal.msgSuccess(res.data.duplicate ? this.$t('oa.expense.verifyDuplicate') : this.$t('oa.expense.verifySuccess'))
          this.getList()
        })
      })
    },
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm(this.$t('oa.expense.confirm.deleteInvoice', { ids })).then(function() {
        return delInvoice(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess(this.$t('common.deleteSuccess'))
      }).catch(() => {})
    }
  }
}
</script>
