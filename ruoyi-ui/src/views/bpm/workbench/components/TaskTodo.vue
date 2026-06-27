<template>
  <div class="app-container" style="padding: 0; background: transparent;">
    <el-table v-loading="loading" :data="taskList" size="small" stripe>
      <el-table-column label="序号" width="50" align="center">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column label="流程标题" prop="instanceTitle" :show-overflow-tooltip="true" min-width="140" />
      <el-table-column label="节点" prop="nodeName" width="100" align="center" />
      <el-table-column label="创建时间" width="150" align="center">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="100" align="center">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-check" @click="handleTodo(scope.row)">办理</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div v-if="!loading && taskList.length === 0" class="empty-block">
      <i class="el-icon-document"></i>
      <div>暂无待办任务</div>
    </div>
  </div>
</template>

<script>
import { todoList } from '@/api/bpm/task'

export default {
  name: 'TaskTodo',
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
      todoList({ pageNum: 1, pageSize: 10 }).then(response => {
        this.taskList = response.rows || []
      }).finally(() => {
        this.loading = false
      })
    },
    handleTodo(row) {
      this.$router.push({ path: '/bpm/task/todo' })
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
