<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item :label="$t('oa.expense.userName')" prop="userName">
        <el-input
          v-model="queryParams.userName"
          :placeholder="$t('oa.expense.placeholder.userName')"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="$t('oa.expense.category')" prop="categoryId">
        <el-select v-model="queryParams.categoryId" :placeholder="$t('oa.expense.placeholder.category')" clearable>
          <el-option
            v-for="item in categoryList"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('common.status')" prop="status">
        <el-select v-model="queryParams.status" :placeholder="$t('common.status')" clearable>
          <el-option :label="$t('oa.expense.statusDraft')" :value="0" />
          <el-option :label="$t('oa.expense.statusApproving')" :value="1" />
          <el-option :label="$t('oa.expense.statusFinance')" :value="2" />
          <el-option :label="$t('oa.expense.statusPending')" :value="3" />
          <el-option :label="$t('oa.expense.statusFinished')" :value="4" />
          <el-option :label="$t('oa.expense.statusRejected')" :value="9" />
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
          v-hasPermi="['oa:expenseReport:add']"
        >{{ $t('common.add') }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['oa:expenseReport:edit']"
        >{{ $t('common.edit') }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['oa:expenseReport:remove']"
        >{{ $t('common.delete') }}</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="reportList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <index-column :page="queryParams.pageNum" :size="queryParams.pageSize" />
      <el-table-column :label="$t('oa.expense.userName')" align="center" prop="userName" width="120" />
      <el-table-column :label="$t('oa.expense.category')" align="center" prop="categoryName" width="120" />
      <el-table-column :label="$t('oa.expense.totalAmount')" align="center" prop="totalAmount" width="120" />
      <el-table-column :label="$t('oa.expense.reason')" align="center" prop="reason" :show-overflow-tooltip="true" />
      <el-table-column :label="$t('common.status')" align="center" prop="status" width="120">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === 0" type="warning">{{ $t('oa.expense.statusDraft') }}</el-tag>
          <el-tag v-else-if="scope.row.status === 1" type="primary">{{ $t('oa.expense.statusApproving') }}</el-tag>
          <el-tag v-else-if="scope.row.status === 2" type="primary">{{ $t('oa.expense.statusFinance') }}</el-tag>
          <el-tag v-else-if="scope.row.status === 3" type="success">{{ $t('oa.expense.statusPending') }}</el-tag>
          <el-tag v-else-if="scope.row.status === 4" type="success">{{ $t('oa.expense.statusFinished') }}</el-tag>
          <el-tag v-else type="info">{{ $t('oa.expense.statusRejected') }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="$t('common.createTime')" align="center" prop="createTime" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('common.operation')" align="center" class-name="small-padding fixed-width" width="220">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-s-promotion"
            @click="handleSubmit(scope.row)"
            v-hasPermi="['oa:expenseReport:edit']"
            v-if="scope.row.status === 0"
          >{{ $t('oa.expense.submit') }}</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['oa:expenseReport:edit']"
          >{{ $t('common.edit') }}</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['oa:expenseReport:remove']"
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
import { listReport, delReport, submitReport } from "@/api/oa/expense"
import { listCategory } from "@/api/oa/expense"

export default {
  name: "OaExpenseReport",
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      reportList: [],
      categoryList: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userName: undefined,
        categoryId: undefined,
        status: undefined
      }
    }
  },
  created() {
    this.getCategoryList()
    this.getList()
  },
  methods: {
    getCategoryList() {
      listCategory({ pageNum: 1, pageSize: 1000, status: 1 }).then(response => {
        this.categoryList = response.rows
      })
    },
    getList() {
      this.loading = true
      listReport(this.queryParams).then(response => {
        this.reportList = response.rows
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
      this.$router.push('/oa/expense/form?mode=add')
    },
    handleUpdate(row) {
      const id = row ? row.id : this.ids[0]
      this.$router.push('/oa/expense/form?mode=edit&id=' + id)
    },
    handleSubmit(row) {
      this.$modal.confirm(this.$t('oa.expense.confirm.submit', { id: row.id })).then(function() {
        return submitReport(row.id)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess(this.$t('oa.expense.submitSuccess'))
      }).catch(() => {})
    },
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm(this.$t('oa.expense.confirm.deleteReport', { ids })).then(function() {
        return delReport(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess(this.$t('common.deleteSuccess'))
      }).catch(() => {})
    }
  }
}
</script>
