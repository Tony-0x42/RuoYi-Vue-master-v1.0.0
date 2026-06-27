<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="流程标题" prop="instanceTitle">
        <el-input
          v-model="queryParams.instanceTitle"
          placeholder="请输入流程标题"
          clearable
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="节点名称" prop="nodeName">
        <el-input
          v-model="queryParams.nodeName"
          placeholder="请输入节点名称"
          clearable
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="taskList">
      <index-column :page="queryParams.pageNum" :size="queryParams.pageSize" />
      <el-table-column label="流程标题" align="center" prop="instanceTitle" :show-overflow-tooltip="true" />
      <el-table-column label="节点名称" align="center" prop="nodeName" />
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <span>{{ formatStatus(scope.row.status) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="审批意见" align="center" prop="comment" :show-overflow-tooltip="true" />
      <el-table-column label="完成时间" align="center" prop="completeTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.completeTime) }}</span>
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
import { doneList } from "@/api/bpm/task"

export default {
  name: "BpmTaskDone",

  data() {
    return {
      loading: true,
      showSearch: true,
      total: 0,
      taskList: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        instanceTitle: undefined,
        nodeName: undefined
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      doneList(this.queryParams).then(response => {
        this.taskList = response.rows
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
    formatStatus(status) {
      const map = { '0': '待处理', '1': '已提交', '2': '已退回', '3': '已拒绝' }
      return map[status] || status
    }
  }
}
</script>
