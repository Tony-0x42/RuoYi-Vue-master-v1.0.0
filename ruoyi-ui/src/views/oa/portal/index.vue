<template>
  <div class="app-container portal-container">
    <el-row :gutter="20" class="portal-header">
      <el-col :span="16">
        <h3 class="portal-title">{{ $t('oa.portal.title') }}</h3>
        <p class="portal-subtitle">{{ $t('oa.portal.subtitle') }}</p>
      </el-col>
      <el-col :span="8" class="text-right">
        <el-input
          v-model="appKeyword"
          :placeholder="$t('oa.portal.placeholder.searchApp')"
          prefix-icon="el-icon-search"
          size="small"
          style="width: 200px"
          clearable
          @input="handleAppSearch"
        />
      </el-col>
    </el-row>

    <el-row :gutter="20" class="portal-row">
      <el-col :span="16">
        <el-card class="portal-card" v-loading="todoLoading">
          <div slot="header" class="card-header">
            <span>{{ $t('oa.portal.todo') }}</span>
            <el-link type="primary" @click="handleMoreTodo">{{ $t('oa.portal.more') }}</el-link>
          </div>
          <el-form :model="todoQuery" inline size="small" class="mini-form">
            <el-form-item>
              <el-input v-model="todoQuery.processName" :placeholder="$t('oa.portal.placeholder.processName')" clearable />
            </el-form-item>
            <el-form-item>
              <el-input v-model="todoQuery.initiator" :placeholder="$t('oa.portal.placeholder.initiator')" clearable />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" @click="loadTodos">{{ $t('common.search') }}</el-button>
            </el-form-item>
          </el-form>
          <el-table :data="todoList" size="small" :show-header="true">
            <el-table-column :label="$t('oa.portal.processName')" prop="processName" />
            <el-table-column :label="$t('oa.portal.initiator')" prop="initiator" width="120" />
            <el-table-column :label="$t('oa.portal.arriveTime')" prop="arriveTime" width="160" />
            <el-table-column :label="$t('common.operation')" width="80">
              <template slot-scope="scope">
                <el-button type="text" size="mini" @click="handleTodo(scope.row)">{{ $t('oa.portal.handle') }}</el-button>
              </template>
            </el-table-column>
          </el-table>
          <pagination
            v-show="todoTotal > 0"
            :total="todoTotal"
            :page.sync="todoQuery.pageNum"
            :limit.sync="todoQuery.pageSize"
            @pagination="loadTodos"
            small
          />
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="portal-card" v-loading="msgLoading">
          <div slot="header" class="card-header">
            <span>{{ $t('oa.portal.message') }}</span>
            <el-badge :value="unreadCount" v-if="unreadCount > 0" />
          </div>
          <div v-for="msg in messageList" :key="msg.id" class="message-item">
            <div class="message-dot" :class="{ read: msg.read }"></div>
            <div class="message-content">
              <div class="message-type">{{ msg.type }}</div>
              <div class="message-text">{{ msg.content }}</div>
            </div>
          </div>
          <div v-if="messageList.length === 0" class="empty-text">{{ $t('oa.portal.noMessage') }}</div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="portal-row">
      <el-col :span="8">
        <el-card class="portal-card">
          <div slot="header" class="card-header">
            <span>{{ $t('oa.portal.schedule') }}</span>
          </div>
          <div v-for="(item, index) in scheduleList" :key="index" class="schedule-item">
            <div class="schedule-time">{{ item.time }}</div>
            <div class="schedule-title">{{ item.title }}</div>
          </div>
          <div v-if="scheduleList.length === 0" class="empty-text">{{ $t('oa.portal.noSchedule') }}</div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="portal-card" v-loading="appLoading">
          <div slot="header" class="card-header">
            <span>{{ $t('oa.portal.appCenter') }}</span>
          </div>
          <div class="app-grid">
            <div v-for="app in appList" :key="app.code" class="app-item">
              <el-button type="text" @click="toggleAppFavorite(app)">
                <i :class="app.favorite ? 'el-icon-star-on' : 'el-icon-star-off'"></i>
              </el-button>
              <div class="app-icon"><i :class="'el-icon-' + app.icon"></i></div>
              <div class="app-name">{{ app.name }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="portal-card" v-loading="dashboardLoading">
          <div slot="header" class="card-header">
            <span>{{ $t('oa.portal.dashboard') }}</span>
            <el-radio-group v-model="dashboardRange" size="mini" @change="loadDashboard">
              <el-radio-button label="7">7{{ $t('oa.portal.day') }}</el-radio-button>
              <el-radio-button label="30">30{{ $t('oa.portal.day') }}</el-radio-button>
              <el-radio-button label="90">90{{ $t('oa.portal.day') }}</el-radio-button>
            </el-radio-group>
          </div>
          <div class="dashboard-list">
            <div v-for="(value, key) in dashboardData" :key="key" class="dashboard-item" v-if="key !== 'range'">
              <div class="dashboard-label">{{ $t('oa.portal.' + key) }}</div>
              <div class="dashboard-value">{{ value }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getHome, listTodos, listMessages, listApps, toggleFavorite, getDashboard } from "@/api/oa/portal"

export default {
  name: "OaPortal",
  data() {
    return {
      todoLoading: false,
      msgLoading: false,
      appLoading: false,
      dashboardLoading: false,
      todoList: [],
      todoTotal: 0,
      todoQuery: {
        pageNum: 1,
        pageSize: 10,
        processName: undefined,
        initiator: undefined
      },
      messageList: [],
      unreadCount: 0,
      appKeyword: undefined,
      appList: [],
      scheduleList: [
        { time: "09:30", title: this.$t('oa.portal.scheduleMeeting') },
        { time: "14:00", title: this.$t('oa.portal.scheduleReview') }
      ],
      dashboardRange: "7",
      dashboardData: {}
    }
  },
  created() {
    this.loadHome()
    this.loadTodos()
    this.loadMessages()
    this.loadApps()
    this.loadDashboard()
  },
  methods: {
    loadHome() {
      getHome().then(response => {
        const data = response.data
        if (data.favoriteApps && data.favoriteApps.length > 0) {
          this.favoriteApps = data.favoriteApps.map(item => item.appCode)
        }
      })
    },
    loadTodos() {
      this.todoLoading = true
      listTodos(this.todoQuery).then(response => {
        this.todoList = response.rows
        this.todoTotal = response.total
        this.todoLoading = false
      })
    },
    loadMessages() {
      this.msgLoading = true
      listMessages({}).then(response => {
        this.messageList = response.rows
        this.unreadCount = this.messageList.filter(item => !item.read).length
        this.msgLoading = false
      })
    },
    loadApps() {
      this.appLoading = true
      listApps({ keyword: this.appKeyword }).then(response => {
        this.appList = response.data
        this.appLoading = false
      })
    },
    loadDashboard() {
      this.dashboardLoading = true
      getDashboard({ range: this.dashboardRange }).then(response => {
        this.dashboardData = response.data
        this.dashboardLoading = false
      })
    },
    handleAppSearch() {
      this.loadApps()
    },
    toggleAppFavorite(app) {
      toggleFavorite({ appCode: app.code }).then(response => {
        app.favorite = response.data
        this.$modal.msgSuccess(app.favorite ? this.$t('oa.portal.favoriteSuccess') : this.$t('oa.portal.cancelFavoriteSuccess'))
      })
    },
    handleTodo(row) {
      this.$modal.msgInfo(this.$t('oa.portal.todoHandle', { name: row.processName }))
    },
    handleMoreTodo() {
      this.$modal.msgInfo(this.$t('oa.portal.moreTodo'))
    }
  }
}
</script>

<style scoped>
.portal-container {
  padding: 10px;
}
.portal-header {
  margin-bottom: 20px;
}
.portal-title {
  margin: 0;
  font-size: 20px;
}
.portal-subtitle {
  margin: 5px 0 0;
  color: #909399;
  font-size: 13px;
}
.text-right {
  text-align: right;
}
.portal-row {
  margin-bottom: 20px;
}
.portal-card {
  min-height: 320px;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: bold;
}
.mini-form {
  margin-bottom: 10px;
}
.mini-form .el-form-item {
  margin-bottom: 5px;
}
.message-item {
  display: flex;
  align-items: flex-start;
  padding: 10px 0;
  border-bottom: 1px solid #f0f0f0;
}
.message-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: #f56c6c;
  margin-top: 6px;
  margin-right: 10px;
}
.message-dot.read {
  background: #c0c4cc;
}
.message-type {
  font-size: 12px;
  color: #909399;
}
.message-text {
  font-size: 13px;
  color: #303133;
}
.empty-text {
  text-align: center;
  color: #909399;
  padding: 30px 0;
}
.schedule-item {
  display: flex;
  padding: 10px 0;
  border-bottom: 1px solid #f0f0f0;
}
.schedule-time {
  width: 60px;
  color: #409eff;
  font-weight: bold;
}
.schedule-title {
  flex: 1;
  color: #303133;
}
.app-grid {
  display: flex;
  flex-wrap: wrap;
}
.app-item {
  width: 33.33%;
  text-align: center;
  padding: 15px 0;
  position: relative;
}
.app-item .el-button {
  position: absolute;
  top: 0;
  right: 5px;
  padding: 0;
}
.app-icon {
  font-size: 28px;
  color: #409eff;
}
.app-name {
  font-size: 12px;
  margin-top: 5px;
}
.dashboard-list {
  display: flex;
  flex-wrap: wrap;
}
.dashboard-item {
  width: 50%;
  text-align: center;
  padding: 20px 0;
}
.dashboard-label {
  font-size: 12px;
  color: #909399;
}
.dashboard-value {
  font-size: 24px;
  color: #303133;
  font-weight: bold;
  margin-top: 5px;
}
</style>
