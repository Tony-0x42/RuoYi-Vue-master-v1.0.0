<template>
  <div class="app-container">
    <el-tabs v-model="activeTab" type="border-card" @tab-click="handleTabClick">
      <el-tab-pane :label="$t('oa.task.list')" name="list">
        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="80px">
          <el-form-item :label="$t('oa.task.title')" prop="title">
            <el-input
              v-model="queryParams.title"
              :placeholder="$t('oa.task.placeholder.title')"
              clearable
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item :label="$t('oa.task.statusLabel')" prop="status">
            <el-select v-model="queryParams.status" :placeholder="$t('oa.task.placeholder.status')" clearable>
              <el-option v-for="item in statusOptions" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
          <el-form-item :label="$t('oa.task.priorityLabel')" prop="priority">
            <el-select v-model="queryParams.priority" :placeholder="$t('oa.task.placeholder.priority')" clearable>
              <el-option v-for="item in priorityOptions" :key="item.value" :label="item.label" :value="item.value" />
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
              v-hasPermi="['oa:task:add']"
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
              v-hasPermi="['oa:task:edit']"
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
              v-hasPermi="['oa:task:remove']"
            >{{ $t('common.delete') }}</el-button>
          </el-col>
          <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="taskList" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="55" align="center" />
          <index-column :page="queryParams.pageNum" :size="queryParams.pageSize" />
          <el-table-column :label="$t('oa.task.title')" align="center" :show-overflow-tooltip="true">
            <template slot-scope="scope">
              <a class="link-type" style="cursor:pointer" @click="handleView(scope.row)">{{ scope.row.title }}</a>
            </template>
          </el-table-column>
          <el-table-column :label="$t('oa.task.owner')" align="center" prop="ownerName" width="120" />
          <el-table-column :label="$t('oa.task.priorityLabel')" align="center" prop="priority" width="100">
            <template slot-scope="scope">
              <el-tag :type="priorityType(scope.row.priority)" size="mini">{{ priorityLabel(scope.row.priority) }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column :label="$t('oa.task.statusLabel')" align="center" prop="status" width="100">
            <template slot-scope="scope">
              <el-tag :type="statusType(scope.row.status)" size="mini">{{ statusLabel(scope.row.status) }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column :label="$t('oa.task.progress')" align="center" prop="progress" width="120">
            <template slot-scope="scope">
              <el-progress :percentage="scope.row.progress || 0" :stroke-width="10" />
            </template>
          </el-table-column>
          <el-table-column :label="$t('oa.task.endTime')" align="center" prop="endTime" width="160">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.endTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column :label="$t('common.operation')" align="center" class-name="small-padding fixed-width" width="200">
            <template slot-scope="scope">
              <el-button size="mini" type="text" icon="el-icon-view" @click="handleView(scope.row)">{{ $t('oa.task.view') }}</el-button>
              <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['oa:task:edit']">{{ $t('common.edit') }}</el-button>
              <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['oa:task:remove']">{{ $t('common.delete') }}</el-button>
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
      </el-tab-pane>

      <el-tab-pane :label="$t('oa.task.board')" name="board">
        <div class="board-container">
          <el-row :gutter="16">
            <el-col :span="6" v-for="column in boardColumns" :key="column.status">
              <div class="board-column">
                <div class="board-header">
                  <el-tag :type="statusType(column.status)">{{ column.title }}</el-tag>
                  <span class="board-count">{{ column.tasks.length }}</span>
                </div>
                <div class="board-body">
                  <div
                    class="board-card"
                    v-for="task in column.tasks"
                    :key="task.id"
                    @click="handleUpdate(task)"
                  >
                    <div class="card-title">{{ task.title }}</div>
                    <div class="card-meta">
                      <span>{{ task.ownerName || '-' }}</span>
                      <el-tag :type="priorityType(task.priority)" size="mini">{{ priorityLabel(task.priority) }}</el-tag>
                    </div>
                    <div class="card-time">{{ parseTime(task.endTime) }}</div>
                  </div>
                </div>
              </div>
            </el-col>
          </el-row>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import { listTask, delTask, boardTask } from "@/api/oa/task"

export default {
  name: "OaTask",
  data() {
    return {
      activeTab: "list",
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      taskList: [],
      boardTasks: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        title: undefined,
        status: undefined,
        priority: undefined
      }
    }
  },
  computed: {
    statusOptions() {
      return [
        { label: this.$t('oa.task.status.pending'), value: 0 },
        { label: this.$t('oa.task.status.inProgress'), value: 1 },
        { label: this.$t('oa.task.status.completed'), value: 2 },
        { label: this.$t('oa.task.status.cancelled'), value: 3 }
      ]
    },
    priorityOptions() {
      return [
        { label: this.$t('oa.task.priority.urgent'), value: 'urgent' },
        { label: this.$t('oa.task.priority.high'), value: 'high' },
        { label: this.$t('oa.task.priority.medium'), value: 'medium' },
        { label: this.$t('oa.task.priority.low'), value: 'low' }
      ]
    },
    boardColumns() {
      return [
        { status: 0, title: this.$t('oa.task.status.pending'), tasks: this.boardTasks.filter(t => t.status === 0) },
        { status: 1, title: this.$t('oa.task.status.inProgress'), tasks: this.boardTasks.filter(t => t.status === 1) },
        { status: 2, title: this.$t('oa.task.status.completed'), tasks: this.boardTasks.filter(t => t.status === 2) },
        { status: 3, title: this.$t('oa.task.status.cancelled'), tasks: this.boardTasks.filter(t => t.status === 3) }
      ]
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      listTask(this.queryParams).then(response => {
        this.taskList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    getBoard() {
      boardTask({}).then(response => {
        this.boardTasks = response.data || []
      })
    },
    handleTabClick(tab) {
      if (tab.name === "board") {
        this.getBoard()
      }
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
      this.$router.push('/oa/task/form?mode=add')
    },
    handleUpdate(row) {
      const id = row ? row.id : this.ids[0]
      this.$router.push('/oa/task/form?mode=edit&id=' + id)
    },
    handleView(row) {
      this.$router.push('/oa/task/form?mode=detail&id=' + row.id)
    },
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm(this.$t('oa.task.confirm.deleteTask', { ids })).then(function() {
        return delTask(ids)
      }).then(() => {
        this.refreshCurrentTab()
        this.$modal.msgSuccess(this.$t('common.deleteSuccess'))
      }).catch(() => {})
    },
    refreshCurrentTab() {
      if (this.activeTab === "board") {
        this.getBoard()
      } else {
        this.getList()
      }
    },
    priorityType(priority) {
      const map = { urgent: 'danger', high: 'warning', medium: 'primary', low: 'info' }
      return map[priority] || 'info'
    },
    statusType(status) {
      const map = { 0: 'info', 1: 'primary', 2: 'success', 3: 'danger' }
      return map[status] || 'info'
    },
    priorityLabel(priority) {
      return this.$te('oa.task.priority.' + priority) ? this.$t('oa.task.priority.' + priority) : priority
    },
    statusLabel(status) {
      const keyMap = { 0: 'pending', 1: 'inProgress', 2: 'completed', 3: 'cancelled' }
      const key = keyMap[status]
      return key ? this.$t('oa.task.status.' + key) : status
    }
  }
}
</script>

<style scoped>
.board-container {
  padding: 10px;
}
.board-column {
  background: #f5f7fa;
  border-radius: 4px;
  min-height: 400px;
  padding: 10px;
}
.board-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
  font-weight: bold;
}
.board-count {
  color: #909399;
  font-size: 12px;
}
.board-card {
  background: #fff;
  border-radius: 4px;
  padding: 10px;
  margin-bottom: 10px;
  cursor: pointer;
  box-shadow: 0 1px 3px rgba(0,0,0,0.1);
}
.board-card:hover {
  box-shadow: 0 2px 8px rgba(0,0,0,0.15);
}
.card-title {
  font-weight: 500;
  margin-bottom: 8px;
}
.card-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 12px;
  color: #606266;
  margin-bottom: 4px;
}
.card-time {
  font-size: 12px;
  color: #909399;
}
</style>
