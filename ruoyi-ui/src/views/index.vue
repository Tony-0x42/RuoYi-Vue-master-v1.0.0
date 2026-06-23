<template>
  <div class="app-container workbench">
    <!-- 顶部欢迎 -->
    <el-row :gutter="20" class="welcome-row">
      <el-col :span="24">
        <div class="welcome-card">
          <div class="welcome-info">
            <div class="welcome-title">
              您好，{{ nickName || name }}，欢迎进入工作台
            </div>
            <div class="welcome-desc">
              {{ welcomeText }} · 今天是 {{ currentDate }}
            </div>
          </div>
          <div class="welcome-extra">
            <el-button type="primary" size="small" icon="el-icon-s-check" @click="goTodo">
              办理待办
            </el-button>
            <el-button size="small" icon="el-icon-message" @click="goNotice">
              查看公告
            </el-button>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stat-row">
      <el-col :xs="24" :sm="12" :lg="8">
        <div class="stat-card stat-todo" @click="goTodo">
          <div class="stat-icon"><i class="el-icon-s-check"></i></div>
          <div class="stat-body">
            <div class="stat-num">{{ todoTotal }}</div>
            <div class="stat-label">待办任务</div>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="12" :lg="8">
        <div class="stat-card stat-done" @click="goDone">
          <div class="stat-icon"><i class="el-icon-finished"></i></div>
          <div class="stat-body">
            <div class="stat-num">{{ doneTotal }}</div>
            <div class="stat-label">已办任务</div>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="12" :lg="8">
        <div class="stat-card stat-notice" @click="goNotice">
          <div class="stat-icon"><i class="el-icon-message"></i></div>
          <div class="stat-body">
            <div class="stat-num">{{ noticeTotal }}</div>
            <div class="stat-label">通知公告</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 任务 + 公告 -->
    <el-row :gutter="20" class="main-row">
      <!-- 待办/已办 -->
      <el-col :xs="24" :lg="16" class="main-col">
        <el-card class="panel-card" shadow="never">
          <div slot="header" class="panel-header">
            <span><i class="el-icon-s-check panel-icon"></i> 任务中心</span>
            <el-radio-group v-model="taskTab" size="mini">
              <el-radio-button label="todo">待办</el-radio-button>
              <el-radio-button label="done">已办</el-radio-button>
            </el-radio-group>
          </div>

          <div v-loading="taskLoading" class="panel-body">
            <el-table
              :data="taskList"
              size="small"
              :show-header="true"
              stripe
              class="task-table"
            >
              <el-table-column label="流程标题" prop="instanceTitle" :show-overflow-tooltip="true" min-width="160">
                <template slot-scope="scope">
                  <span class="task-title">{{ scope.row.instanceTitle || '-' }}</span>
                </template>
              </el-table-column>
              <el-table-column label="节点" prop="nodeName" width="120" align="center">
                <template slot-scope="scope">
                  <el-tag size="mini" type="info" effect="plain">{{ scope.row.nodeName || '-' }}</el-tag>
                </template>
              </el-table-column>
              <el-table-column label="时间" width="160" align="center">
                <template slot-scope="scope">
                  <span>{{ parseTime(taskTab === 'todo' ? scope.row.createTime : scope.row.completeTime) }}</span>
                </template>
              </el-table-column>
              <el-table-column label="状态" width="90" align="center" v-if="taskTab === 'done'">
                <template slot-scope="scope">
                  <el-tag size="mini" :type="statusType(scope.row.status)">{{ formatStatus(scope.row.status) }}</el-tag>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="120" align="center" class-name="small-padding fixed-width">
                <template slot-scope="scope">
                  <el-button
                    v-if="taskTab === 'todo'"
                    size="mini"
                    type="text"
                    icon="el-icon-check"
                    @click="handleTodo(scope.row)"
                  >办理</el-button>
                  <el-button
                    size="mini"
                    type="text"
                    icon="el-icon-view"
                    @click="handleTrace(scope.row)"
                  >详情</el-button>
                </template>
              </el-table-column>
            </el-table>

            <div v-if="taskTotal === 0 && !taskLoading" class="empty-block">
              <i class="el-icon-document"></i>
              <div>暂无{{ taskTab === 'todo' ? '待办' : '已办' }}任务</div>
            </div>

            <pagination
              v-show="taskTotal > 0"
              small
              :total="taskTotal"
              :page.sync="taskQuery.pageNum"
              :limit.sync="taskQuery.pageSize"
              @pagination="loadTask"
            />
          </div>
        </el-card>
      </el-col>

      <!-- 公告 -->
      <el-col :xs="24" :lg="8" class="main-col">
        <el-card class="panel-card" shadow="never">
          <div slot="header" class="panel-header">
            <span><i class="el-icon-message panel-icon"></i> 通知公告</span>
            <el-button type="text" size="mini" @click="goNotice">更多</el-button>
          </div>

          <div v-loading="noticeLoading" class="panel-body notice-list">
            <div
              v-for="item in noticeList"
              :key="item.noticeId"
              class="notice-item"
              :class="{ 'is-read': item.isRead }"
              @click="previewNotice(item)"
            >
              <div class="notice-main">
                <el-tag size="mini" :type="item.noticeType === '1' ? 'warning' : 'success'" class="notice-tag">
                  {{ item.noticeType === '1' ? '通知' : '公告' }}
                </el-tag>
                <span class="notice-title-text">{{ item.noticeTitle }}</span>
                <span v-if="!item.isRead" class="notice-unread-dot"></span>
              </div>
              <div class="notice-meta-line">
                <span>{{ item.createBy || '系统' }}</span>
                <span>{{ item.createTime }}</span>
              </div>
            </div>

            <div v-if="noticeList.length === 0 && !noticeLoading" class="empty-block">
              <i class="el-icon-document"></i>
              <div>暂无公告</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <notice-detail-view ref="noticeViewRef" />
  </div>
</template>

<script>
import { mapState } from 'vuex'
import { todoList, doneList } from '@/api/bpm/task'
import { listNoticeTop, markNoticeRead } from '@/api/system/notice'
import NoticeDetailView from '@/layout/components/HeaderNotice/DetailView'

export default {
  name: 'Index',
  components: { NoticeDetailView },
  data() {
    return {
      currentDate: '',
      welcomeText: '祝您工作愉快',
      // 任务中心
      taskTab: 'todo',
      taskLoading: false,
      taskList: [],
      taskTotal: 0,
      taskQuery: {
        pageNum: 1,
        pageSize: 10
      },
      // 公告
      noticeLoading: false,
      noticeList: [],
      noticeTotal: 0
    }
  },
  computed: {
    ...mapState('user', ['name', 'nickName'])
  },
  watch: {
    taskTab() {
      this.taskQuery.pageNum = 1
      this.loadTask()
    }
  },
  created() {
    this.setDate()
    this.loadTask()
    this.loadNotice()
  },
  methods: {
    setDate() {
      const now = new Date()
      const year = now.getFullYear()
      const month = String(now.getMonth() + 1).padStart(2, '0')
      const day = String(now.getDate()).padStart(2, '0')
      const weekDays = ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六']
      this.currentDate = `${year}年${month}月${day}日 ${weekDays[now.getDay()]}`

      const hour = now.getHours()
      if (hour < 12) {
        this.welcomeText = '上午好'
      } else if (hour < 18) {
        this.welcomeText = '下午好'
      } else {
        this.welcomeText = '晚上好'
      }
    },
    loadTask() {
      this.taskLoading = true
      const request = this.taskTab === 'todo' ? todoList : doneList
      request(this.taskQuery).then(response => {
        this.taskList = response.rows || []
        this.taskTotal = response.total || 0
      }).finally(() => {
        this.taskLoading = false
      })
    },
    loadNotice() {
      this.noticeLoading = true
      listNoticeTop().then(response => {
        this.noticeList = response.data || []
        this.noticeTotal = response.unreadCount !== undefined
          ? response.unreadCount
          : this.noticeList.filter(n => !n.isRead).length
      }).finally(() => {
        this.noticeLoading = false
      })
    },
    previewNotice(item) {
      if (!item.isRead) {
        markNoticeRead(item.noticeId).catch(() => {})
        item.isRead = true
        this.$set(this.noticeList, this.noticeList.indexOf(item), { ...item, isRead: true })
        this.noticeTotal = Math.max(0, this.noticeTotal - 1)
      }
      this.$refs.noticeViewRef.open(item.noticeId)
    },
    formatStatus(status) {
      const map = { '0': '待处理', '1': '已提交', '2': '已退回', '3': '已拒绝' }
      return map[status] || status
    },
    statusType(status) {
      const map = { '0': 'info', '1': 'success', '2': 'warning', '3': 'danger' }
      return map[status] || 'info'
    },
    handleTodo(row) {
      this.$router.push({ path: '/bpm/task/todo' })
    },
    handleTrace(row) {
      if (row.processInstanceId) {
        this.$router.push({ path: '/bpm/trace', query: { processInstanceId: row.processInstanceId } })
      } else {
        this.$modal.msgInfo('暂无流程详情')
      }
    },
    goTodo() {
      this.$router.push({ path: '/bpm/task/todo' })
    },
    goDone() {
      this.$router.push({ path: '/bpm/task/done' })
    },
    goNotice() {
      this.$router.push({ path: '/system/notice' })
    }
  }
}
</script>

<style scoped lang="scss">
.workbench {
  padding-bottom: 20px;
}

.welcome-row {
  margin-bottom: 20px;
}
.welcome-card {
  background: linear-gradient(90deg, #4a90e2 0%, #63b3ed 100%);
  border-radius: 8px;
  padding: 24px 28px;
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-shadow: 0 4px 12px rgba(74, 144, 226, 0.25);
}
.welcome-title {
  font-size: 22px;
  font-weight: 600;
  margin-bottom: 8px;
}
.welcome-desc {
  font-size: 14px;
  opacity: 0.9;
}
.welcome-extra {
  .el-button {
    margin-left: 10px;
  }
  .el-button--primary {
    background: #fff;
    border-color: #fff;
    color: #4a90e2;
    &:hover {
      background: #f0f7ff;
      border-color: #f0f7ff;
      color: #4a90e2;
    }
  }
  .el-button--default {
    background: rgba(255, 255, 255, 0.15);
    border-color: rgba(255, 255, 255, 0.3);
    color: #fff;
    &:hover {
      background: rgba(255, 255, 255, 0.25);
      border-color: rgba(255, 255, 255, 0.4);
      color: #fff;
    }
  }
}

.stat-row {
  margin-bottom: 20px;
}
.stat-card {
  border-radius: 8px;
  padding: 20px 24px;
  display: flex;
  align-items: center;
  color: #fff;
  cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s;
  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 6px 16px rgba(0, 0, 0, 0.12);
  }
}
.stat-todo { background: linear-gradient(135deg, #ff9a56 0%, #ff6b6b 100%); }
.stat-done { background: linear-gradient(135deg, #48bb78 0%, #38a169 100%); }
.stat-notice { background: linear-gradient(135deg, #4299e1 0%, #3182ce 100%); }
.stat-icon {
  width: 56px;
  height: 56px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.2);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 26px;
  margin-right: 18px;
}
.stat-num {
  font-size: 28px;
  font-weight: 700;
  line-height: 1.2;
}
.stat-label {
  font-size: 14px;
  opacity: 0.9;
}

.main-row {
  .main-col {
    margin-bottom: 20px;
  }
}
.panel-card {
  border-radius: 8px;
  height: 100%;
  ::v-deep .el-card__header {
    padding: 16px 20px;
    border-bottom: 1px solid #f0f0f0;
  }
  ::v-deep .el-card__body {
    padding: 0;
  }
}
.panel-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}
.panel-icon {
  color: #409eff;
  margin-right: 6px;
}
.panel-body {
  padding: 16px 20px;
}

.task-table {
  ::v-deep th {
    background: #f7f9fb;
    color: #606266;
    font-weight: 600;
  }
}
.task-title {
  color: #303133;
  font-weight: 500;
}

.notice-list {
  .notice-item {
    padding: 14px 0;
    border-bottom: 1px solid #f5f5f5;
    cursor: pointer;
    transition: background 0.15s;
    &:last-child {
      border-bottom: none;
    }
    &:hover {
      background: #f7f9fb;
      border-radius: 4px;
    }
    &.is-read {
      .notice-title-text { color: #999; }
      .notice-tag { opacity: 0.55; }
    }
  }
  .notice-main {
    display: flex;
    align-items: center;
    gap: 8px;
    margin-bottom: 8px;
  }
  .notice-tag {
    flex-shrink: 0;
  }
  .notice-title-text {
    flex: 1;
    font-size: 14px;
    color: #303133;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
  }
  .notice-unread-dot {
    width: 6px;
    height: 6px;
    border-radius: 50%;
    background: #f56c6c;
    flex-shrink: 0;
  }
  .notice-meta-line {
    display: flex;
    justify-content: space-between;
    font-size: 12px;
    color: #909399;
    padding-left: 46px;
  }
}

.empty-block {
  text-align: center;
  padding: 40px 0;
  color: #c0c4cc;
  font-size: 13px;
  i {
    font-size: 36px;
    display: block;
    margin-bottom: 10px;
  }
}

@media screen and (max-width: 768px) {
  .welcome-card {
    flex-direction: column;
    align-items: flex-start;
  }
  .welcome-extra {
    margin-top: 16px;
  }
}
</style>
