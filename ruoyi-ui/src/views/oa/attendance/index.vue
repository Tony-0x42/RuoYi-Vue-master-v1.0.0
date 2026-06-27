<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="16">
        <el-card>
          <div slot="header" class="clearfix">
            <span>{{ $t('oa.attendance.title') }}</span>
            <el-button
              style="float: right; padding: 3px 0"
              type="text"
              @click="handleCheckIn"
              v-hasPermi="['oa:attendance:list']"
            >{{ $t('oa.attendance.checkIn') }}</el-button>
          </div>
          <div class="check-in-panel">
            <div class="current-time">{{ currentTime }}</div>
            <div class="current-date">{{ currentDate }}</div>
            <el-button type="primary" size="large" icon="el-icon-location-outline" @click="handleCheckIn" v-hasPermi="['oa:attendance:list']">
              {{ $t('oa.attendance.checkIn') }}
            </el-button>
          </div>
        </el-card>

        <el-card style="margin-top: 15px">
          <div slot="header" class="clearfix">
            <span>{{ $t('oa.attendance.recentRecord') }}</span>
          </div>
          <el-table v-loading="loading" :data="recordList">
            <el-table-column :label="$t('oa.attendance.checkInTime')" align="center" prop="checkInTime" width="160">
              <template slot-scope="scope">
                <span>{{ parseTime(scope.row.checkInTime) }}</span>
              </template>
            </el-table-column>
            <el-table-column :label="$t('oa.attendance.checkInType')" align="center" prop="type" width="120">
              <template slot-scope="scope">
                {{ typeLabel(scope.row.type) }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('oa.attendance.location')" align="center" prop="location" />
            <el-table-column :label="$t('oa.attendance.status')" align="center" prop="status" width="100">
              <template slot-scope="scope">
                <el-tag :type="statusType(scope.row.status)" size="mini">{{ statusLabel(scope.row.status) }}</el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>

      <el-col :span="8">
        <el-card>
          <div slot="header" class="clearfix">
            <span>{{ $t('oa.attendance.statistics') }}</span>
          </div>
          <div class="stat-list">
            <div class="stat-item">
              <div class="stat-label">{{ $t('oa.attendance.normal') }}</div>
              <div class="stat-value">{{ statistics.normal || 0 }}</div>
            </div>
            <div class="stat-item">
              <div class="stat-label">{{ $t('oa.attendance.late') }}</div>
              <div class="stat-value">{{ statistics.late || 0 }}</div>
            </div>
            <div class="stat-item">
              <div class="stat-label">{{ $t('oa.attendance.early') }}</div>
              <div class="stat-value">{{ statistics.early || 0 }}</div>
            </div>
            <div class="stat-item">
              <div class="stat-label">{{ $t('oa.attendance.absent') }}</div>
              <div class="stat-value">{{ statistics.absent || 0 }}</div>
            </div>
          </div>
        </el-card>

        <el-card style="margin-top: 15px">
          <div slot="header" class="clearfix">
            <span>{{ $t('oa.attendance.leaveBalance') }}</span>
          </div>
          <el-table :data="leaveBalanceList" size="small">
            <el-table-column :label="$t('oa.attendance.leaveType')" align="center" prop="leaveType">
              <template slot-scope="scope">
                {{ leaveTypeLabel(scope.row.leaveType) }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('oa.attendance.totalDays')" align="center" prop="totalDays" />
            <el-table-column :label="$t('oa.attendance.usedDays')" align="center" prop="usedDays" />
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <el-dialog :title="$t('oa.attendance.checkIn')" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item :label="$t('oa.attendance.checkInType')" prop="type">
          <el-select v-model="form.type" :placeholder="$t('oa.attendance.placeholder.checkInType')" style="width:100%">
            <el-option v-for="item in typeOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('oa.attendance.location')" prop="location">
          <el-input v-model="form.location" :placeholder="$t('oa.attendance.placeholder.location')" />
        </el-form-item>
        <el-form-item :label="$t('oa.attendance.wifi')" prop="wifi">
          <el-input v-model="form.wifi" placeholder="WiFi" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitCheckIn">{{ $t('common.submit') }}</el-button>
        <el-button @click="cancel">{{ $t('common.cancel') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { checkIn, listMyRecord, attendanceStatistics, listLeaveBalance } from "@/api/oa/attendance"

export default {
  name: "OaAttendance",
  data() {
    return {
      loading: true,
      currentTime: "",
      currentDate: "",
      recordList: [],
      statistics: {},
      leaveBalanceList: [],
      open: false,
      form: {},
      rules: {
        type: [{ required: true, message: this.$t('oa.attendance.placeholder.checkInType'), trigger: "change" }]
      }
    }
  },
  computed: {
    typeOptions() {
      return [
        { label: this.$t('oa.attendance.gps'), value: 'gps' },
        { label: this.$t('oa.attendance.wifiType'), value: 'wifi' },
        { label: this.$t('oa.attendance.device'), value: 'device' },
        { label: this.$t('oa.attendance.field'), value: 'field' }
      ]
    }
  },
  created() {
    this.updateTime()
    this.getRecords()
    this.getStatistics()
    this.getLeaveBalance()
    this.timer = setInterval(this.updateTime, 1000)
  },
  beforeDestroy() {
    if (this.timer) {
      clearInterval(this.timer)
    }
  },
  methods: {
    updateTime() {
      const now = new Date()
      this.currentTime = this.parseTime(now, "{h}:{i}:{s}")
      this.currentDate = this.parseTime(now, "{y}-{m}-{d} {a}")
    },
    getRecords() {
      this.loading = true
      const userId = this.$store.state.user.id
      listMyRecord({ pageSize: 10, userId: userId }).then(response => {
        this.recordList = response.rows || []
        this.loading = false
      })
    },
    getStatistics() {
      attendanceStatistics().then(response => {
        this.statistics = response.data || {}
      })
    },
    getLeaveBalance() {
      listLeaveBalance({ pageSize: 10 }).then(response => {
        this.leaveBalanceList = response.rows || []
      })
    },
    handleCheckIn() {
      this.reset()
      this.open = true
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = {
        type: "gps",
        location: undefined,
        wifi: undefined,
        status: "normal"
      }
      this.resetForm("form")
    },
    submitCheckIn() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          checkIn(this.form).then(() => {
            this.$modal.msgSuccess(this.$t('common.success'))
            this.open = false
            this.getRecords()
            this.getStatistics()
          })
        }
      })
    },
    statusType(status) {
      const map = { normal: 'success', late: 'warning', early: 'warning', absent: 'danger' }
      return map[status] || 'info'
    },
    statusLabel(status) {
      return this.$te('oa.attendance.' + status) ? this.$t('oa.attendance.' + status) : status
    },
    typeLabel(type) {
      const key = type === 'wifi' ? 'wifiType' : type
      return this.$te('oa.attendance.' + key) ? this.$t('oa.attendance.' + key) : type
    },
    leaveTypeLabel(type) {
      return this.$te('oa.attendance.' + type) ? this.$t('oa.attendance.' + type) : type
    }
  }
}
</script>

<style scoped>
.check-in-panel {
  text-align: center;
  padding: 30px 0;
}
.current-time {
  font-size: 36px;
  font-weight: bold;
  color: #303133;
}
.current-date {
  font-size: 16px;
  color: #909399;
  margin: 10px 0 20px;
}
.stat-list {
  display: flex;
  flex-wrap: wrap;
}
.stat-item {
  width: 50%;
  text-align: center;
  padding: 15px 0;
}
.stat-label {
  font-size: 14px;
  color: #606266;
}
.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #409EFF;
  margin-top: 5px;
}
</style>
