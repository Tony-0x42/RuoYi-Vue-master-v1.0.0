<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item :label="$t('oa.meeting.search.title')" prop="title">
        <el-input
          v-model="queryParams.title"
          :placeholder="$t('oa.meeting.search.titlePlaceholder')"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="$t('oa.meeting.search.room')" prop="roomId">
        <el-select v-model="queryParams.roomId" :placeholder="$t('oa.meeting.search.room')" clearable style="width: 180px">
          <el-option
            v-for="item in roomOptions"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('oa.meeting.search.status')" prop="status">
        <el-select v-model="queryParams.status" :placeholder="$t('oa.meeting.search.status')" clearable>
          <el-option :label="$t('oa.meeting.status.pending')" :value="0" />
          <el-option :label="$t('oa.meeting.status.ongoing')" :value="1" />
          <el-option :label="$t('oa.meeting.status.finished')" :value="2" />
          <el-option :label="$t('oa.meeting.status.cancelled')" :value="3" />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('oa.meeting.search.dateRange')" prop="dateRange">
        <el-date-picker
          v-model="dateRange"
          type="datetimerange"
          value-format="yyyy-MM-dd HH:mm:ss"
          :default-time="['00:00:00','23:59:59']"
          range-separator="-"
          :start-placeholder="$t('oa.meeting.form.timeRange')"
          :end-placeholder="$t('oa.meeting.form.timeRange')"
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
          v-hasPermi="['oa:meeting:add']"
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
          v-hasPermi="['oa:meeting:edit']"
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
          v-hasPermi="['oa:meeting:remove']"
        >{{ $t('common.delete') }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-s-data"
          size="mini"
          @click="handleOccupancy"
          v-hasPermi="['oa:meeting:query']"
        >{{ $t('oa.meeting.occupancy') }}</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="meetingList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <index-column :page="queryParams.pageNum" :size="queryParams.pageSize" />
      <el-table-column :label="$t('oa.meeting.search.title')" align="center" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <a class="link-type" style="cursor:pointer" @click="handleView(scope.row)">{{ scope.row.title }}</a>
        </template>
      </el-table-column>
      <el-table-column :label="$t('oa.meeting.search.room')" align="center" prop="roomName" width="140" />
      <el-table-column :label="$t('oa.meeting.form.timeRange')" align="center" width="300">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.startTime) }} ~ {{ parseTime(scope.row.endTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('oa.meeting.form.organizer')" align="center" prop="organizerName" width="120" />
      <el-table-column :label="$t('oa.meeting.search.status')" align="center" prop="status" width="100">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === 0" type="warning">{{ $t('oa.meeting.status.pending') }}</el-tag>
          <el-tag v-else-if="scope.row.status === 1" type="success">{{ $t('oa.meeting.status.ongoing') }}</el-tag>
          <el-tag v-else-if="scope.row.status === 2" type="info">{{ $t('oa.meeting.status.finished') }}</el-tag>
          <el-tag v-else type="danger">{{ $t('oa.meeting.status.cancelled') }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="$t('common.createTime')" align="center" prop="createTime" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('common.operation')" align="center" class-name="small-padding fixed-width" width="260">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleView(scope.row)"
          >{{ $t('common.search') }}</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-close"
            @click="handleCancel(scope.row)"
            v-hasPermi="['oa:meeting:edit']"
            v-if="scope.row.status === 0"
          >{{ $t('oa.meeting.cancel') }}</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['oa:meeting:edit']"
          >{{ $t('common.edit') }}</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['oa:meeting:remove']"
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

    <el-dialog :title="title" :visible.sync="open" width="720px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px" :disabled="isDetail">
        <el-row>
          <el-col :span="24">
            <el-form-item :label="$t('oa.meeting.form.title')" prop="title">
              <el-input v-model="form.title" :placeholder="$t('oa.meeting.search.titlePlaceholder')" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="$t('oa.meeting.form.timeRange')" prop="timeRange">
              <el-date-picker
                v-model="form.timeRange"
                type="datetimerange"
                value-format="yyyy-MM-dd HH:mm:ss"
                :default-time="['09:00:00','18:00:00']"
                range-separator="~"
                :start-placeholder="$t('oa.meeting.form.timeRange')"
                :end-placeholder="$t('oa.meeting.form.timeRange')"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('oa.meeting.search.room')" prop="roomId">
              <el-select v-model="form.roomId" :placeholder="$t('oa.meeting.form.roomIdRequired')" style="width: 100%">
                <el-option
                  v-for="item in roomOptions"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('oa.meeting.form.attendees')">
              <el-select v-model="form.attendeeIds" multiple collapse-tags :placeholder="$t('oa.meeting.form.attendees')" style="width: 100%">
                <el-option
                  v-for="user in userOptions"
                  :key="user.userId"
                  :label="user.userName"
                  :value="user.userId"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="$t('oa.meeting.form.content')" prop="content">
              <el-input v-model="form.content" type="textarea" :rows="4" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="$t('oa.meeting.form.remark')" prop="remark">
              <el-input v-model="form.remark" type="textarea" :rows="2" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button v-if="!isDetail" type="primary" @click="submitForm">{{ $t('common.submit') }}</el-button>
        <el-button @click="cancel">{{ $t('common.cancel') }}</el-button>
      </div>
    </el-dialog>

    <!-- 占用视图抽屉 -->
    <el-drawer :title="$t('oa.meeting.occupancy')" :visible.sync="occupancyOpen" direction="rtl" size="600px">
      <div style="padding: 0 20px">
        <el-form :inline="true" size="small">
          <el-form-item :label="$t('oa.meeting.search.room')">
            <el-select v-model="occupancyQuery.roomId" :placeholder="$t('oa.meeting.search.room')" clearable>
              <el-option
                v-for="item in roomOptions"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
          <el-form-item :label="$t('oa.meeting.search.dateRange')">
            <el-date-picker
              v-model="occupancyQuery.dateRange"
              type="datetimerange"
              value-format="yyyy-MM-dd HH:mm:ss"
              :default-time="['00:00:00','23:59:59']"
              range-separator="-"
              style="width: 260px"
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" size="mini" @click="loadOccupancy">{{ $t('common.search') }}</el-button>
          </el-form-item>
        </el-form>
        <el-timeline>
          <el-timeline-item
            v-for="item in occupancyList"
            :key="item.id"
            :timestamp="parseTime(item.startTime) + ' ~ ' + parseTime(item.endTime)"
          >
            {{ item.title }}（{{ item.roomName }}）
          </el-timeline-item>
        </el-timeline>
      </div>
    </el-drawer>
  </div>
</template>

<script>
import { listMeeting, getMeeting, addMeeting, updateMeeting, delMeeting, cancelMeeting, listAvailableRoom, listOccupancy } from "@/api/oa/meeting"
import { listUser } from "@/api/system/user"

export default {
  name: "OaMeeting",
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      meetingList: [],
      roomOptions: [],
      userOptions: [],
      title: "",
      open: false,
      isDetail: false,
      occupancyOpen: false,
      dateRange: [],
      occupancyQuery: {
        roomId: undefined,
        dateRange: []
      },
      occupancyList: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        title: undefined,
        roomId: undefined,
        status: undefined
      },
      form: {},
      rules: {
        title: [
          { required: true, message: this.$t('oa.meeting.form.titleRequired'), trigger: "blur" }
        ],
        timeRange: [
          { required: true, message: this.$t('oa.meeting.form.timeRequired'), trigger: "change" }
        ],
        roomId: [
          { required: true, message: this.$t('oa.meeting.form.roomIdRequired'), trigger: "change" }
        ]
      }
    }
  },
  created() {
    this.getList()
    this.loadRooms()
    this.loadUsers()
  },
  methods: {
    getList() {
      this.loading = true
      this.queryParams.params = {
        beginTime: this.dateRange && this.dateRange.length > 0 ? this.dateRange[0] : undefined,
        endTime: this.dateRange && this.dateRange.length > 0 ? this.dateRange[1] : undefined
      }
      listMeeting(this.queryParams).then(response => {
        this.meetingList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    loadRooms() {
      listAvailableRoom({ pageSize: 1000 }).then(response => {
        this.roomOptions = response.data || []
      })
    },
    loadUsers() {
      listUser({ pageSize: 1000 }).then(response => {
        this.userOptions = response.rows || []
      })
    },
    loadOccupancy() {
      const params = {
        roomId: this.occupancyQuery.roomId,
        startTime: this.occupancyQuery.dateRange && this.occupancyQuery.dateRange.length > 0 ? this.occupancyQuery.dateRange[0] : undefined,
        endTime: this.occupancyQuery.dateRange && this.occupancyQuery.dateRange.length > 0 ? this.occupancyQuery.dateRange[1] : undefined
      }
      listOccupancy(params).then(response => {
        this.occupancyList = response.data || []
      })
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = {
        id: undefined,
        title: undefined,
        timeRange: [],
        roomId: undefined,
        attendeeIds: [],
        content: undefined,
        remark: undefined
      }
      this.isDetail = false
      this.resetForm("form")
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.dateRange = []
      this.resetForm("queryForm")
      this.handleQuery()
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = this.$t('oa.meeting.add')
    },
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getMeeting(id).then(response => {
        const data = response.data
        data.timeRange = [data.startTime, data.endTime]
        data.attendeeIds = (data.attendees || []).map(a => a.userId)
        this.form = data
        this.open = true
        this.title = this.$t('oa.meeting.edit')
      })
    },
    handleView(row) {
      this.reset()
      getMeeting(row.id).then(response => {
        const data = response.data
        data.timeRange = [data.startTime, data.endTime]
        data.attendeeIds = (data.attendees || []).map(a => a.userId)
        this.form = data
        this.open = true
        this.isDetail = true
        this.title = this.$t('oa.meeting.detail')
      })
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          const data = { ...this.form }
          data.startTime = data.timeRange[0]
          data.endTime = data.timeRange[1]
          delete data.timeRange
          delete data.attendees
          if (data.id != undefined) {
            updateMeeting(data).then(() => {
              this.$modal.msgSuccess(this.$t('common.editSuccess'))
              this.open = false
              this.getList()
            })
          } else {
            addMeeting(data).then(() => {
              this.$modal.msgSuccess(this.$t('common.addSuccess'))
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    handleOccupancy() {
      this.occupancyOpen = true
      const now = new Date()
      const start = new Date(now.getFullYear(), now.getMonth(), now.getDate(), 0, 0, 0)
      const end = new Date(now.getFullYear(), now.getMonth(), now.getDate(), 23, 59, 59)
      const fmt = d => {
        const pad = n => n < 10 ? '0' + n : n
        return d.getFullYear() + '-' + pad(d.getMonth() + 1) + '-' + pad(d.getDate()) + ' ' + pad(d.getHours()) + ':' + pad(d.getMinutes()) + ':' + pad(d.getSeconds())
      }
      this.occupancyQuery.dateRange = [fmt(start), fmt(end)]
      this.$nextTick(() => {
        this.loadOccupancy()
      })
    },
    handleCancel(row) {
      this.$modal.confirm(this.$t('oa.meeting.cancel') + '「' + row.title + '」？').then(function() {
        return cancelMeeting(row.id)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess(this.$t('oa.meeting.cancelSuccess'))
      }).catch(() => {})
    },
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm(this.$t('oa.meeting.deleteConfirm', { ids })).then(function() {
        return delMeeting(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess(this.$t('common.deleteSuccess'))
      }).catch(() => {})
    }
  }
}
</script>
