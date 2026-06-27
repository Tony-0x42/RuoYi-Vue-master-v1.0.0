<template>
  <div class="app-container" style="padding: 0; background: transparent;">
    <el-table v-loading="loading" :data="taskList" size="small" stripe>
      <el-table-column label="流程标题" prop="instanceTitle" :show-overflow-tooltip="true" min-width="140" />
      <el-table-column label="节点" prop="nodeName" width="100" align="center" />
      <el-table-column label="完成时间" width="150" align="center">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.completeTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" width="90" align="center">
        <template slot-scope="scope">
          <el-tag size="mini" :type="statusType(scope.row.status)">{{ formatStatus(scope.row.status) }}</el-tag>
        </template>
      </el-table-column>
    </el-table>
    <div v-if="!loading && taskList.length === 0" class="empty-block">
      <i class="el-icon-document"></i>
      <div>暂无已办任务</div>
    </div>
  </div>
</template>

<script>
import { doneList } from '@/api/bpm/task'

export default {
  name: 'TaskDone',
  data() {
    return {
      loading: true,
      taskList: []
    }
  },
  created() {
    this.loadList()
  },
  methods: {
    loadList() {
      this.loading = true
      doneList({ pageNum: 1, pageSize: 10 }).then(response => {
        this.taskList = response.rows || []
      }).finally(() => {
        this.loading = false
      })
    },
    formatStatus(status) {
      const map = { '0': '待处理', '1': '已提交', '2': '已退回', '3': '已拒绝' }
      return map[status] || status
    },
    statusType(status) {
      const map = { '0': 'info', '1': 'success', '2': 'warning', '3': 'danger' }
      return map[status] || 'info'
    }
  }
}
</script>

<style scoped lang="scss">
.empty-block {
  text-align: center;
  padding: 30px 0;
  color: #c0c4cc;
  font-size: 13px;
  i {
    font-size: 32px;
    display: block;
    margin-bottom: 8px;
  }
}
</style>
