<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
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
      <el-form-item :label="$t('oa.expense.level')" prop="level">
        <el-input
          v-model="queryParams.level"
          :placeholder="$t('oa.expense.placeholder.level')"
          clearable
          @keyup.enter.native="handleQuery"
        />
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
          v-hasPermi="['oa:expenseStandard:add']"
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
          v-hasPermi="['oa:expenseStandard:edit']"
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
          v-hasPermi="['oa:expenseStandard:remove']"
        >{{ $t('common.delete') }}</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="standardList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <index-column :page="queryParams.pageNum" :size="queryParams.pageSize" />
      <el-table-column :label="$t('oa.expense.category')" align="center" prop="categoryName" />
      <el-table-column :label="$t('oa.expense.level')" align="center" prop="level" width="120" />
      <el-table-column :label="$t('oa.expense.city')" align="center" prop="city" width="120" />
      <el-table-column :label="$t('oa.expense.limitAmount')" align="center" prop="limitAmount" width="120" />
      <el-table-column :label="$t('oa.expense.periodType')" align="center" prop="periodType" width="120">
        <template slot-scope="scope">
          <span>{{ scope.row.periodType ? $t('oa.expense.period' + scope.row.periodType.charAt(0).toUpperCase() + scope.row.periodType.slice(1)) : '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('common.status')" align="center" prop="status" width="100">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === 1" type="success">{{ $t('common.enable') }}</el-tag>
          <el-tag v-else type="info">{{ $t('common.disable') }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="$t('common.operation')" align="center" class-name="small-padding fixed-width" width="180">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['oa:expenseStandard:edit']"
          >{{ $t('common.edit') }}</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['oa:expenseStandard:remove']"
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
import { listStandard, delStandard } from "@/api/oa/expense"
import { listCategory } from "@/api/oa/expense"

export default {
  name: "OaExpenseStandard",
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      standardList: [],
      categoryList: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        categoryId: undefined,
        level: undefined
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
      listStandard(this.queryParams).then(response => {
        this.standardList = response.rows
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
      this.$router.push({ path: '/oa/expenseDir/expenseStandard/form' })
    },
    handleUpdate(row) {
      const id = row.id || this.ids
      this.$router.push({ path: '/oa/expenseDir/expenseStandard/form', query: { id } })
    },
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm(this.$t('oa.expense.confirm.deleteStandard', { ids })).then(function() {
        return delStandard(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess(this.$t('common.deleteSuccess'))
      }).catch(() => {})
    }
  }
}
</script>
