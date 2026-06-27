<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item :label="$t('calendar.search.title')" prop="title">
        <el-input
          v-model="queryParams.title"
          :placeholder="$t('calendar.search.titlePlaceholder')"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="$t('calendar.search.type')" prop="type">
        <el-select v-model="queryParams.type" :placeholder="$t('calendar.search.type')" clearable>
          <el-option :label="$t('calendar.type.personal')" value="personal" />
          <el-option :label="$t('calendar.type.meeting')" value="meeting" />
          <el-option :label="$t('calendar.type.leave')" value="leave" />
          <el-option :label="$t('calendar.type.trip')" value="trip" />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('calendar.search.dateRange')" prop="dateRange">
        <el-date-picker
          v-model="dateRange"
          type="datetimerange"
          value-format="yyyy-MM-dd HH:mm:ss"
          :default-time="['00:00:00','23:59:59']"
          range-separator="-"
          start-placeholder="开始时间"
          end-placeholder="结束时间"
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
          v-hasPermi="['oa:calendar:add']"
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
          v-hasPermi="['oa:calendar:edit']"
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
          v-hasPermi="['oa:calendar:remove']"
        >{{ $t('common.delete') }}</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="eventList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <index-column :page="queryParams.pageNum" :size="queryParams.pageSize" />
      <el-table-column :label="$t('calendar.search.title')" align="center" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <a class="link-type" style="cursor:pointer" @click="handleView(scope.row)">{{ scope.row.title }}</a>
        </template>
      </el-table-column>
      <el-table-column :label="$t('calendar.search.type')" align="center" prop="type" width="100">
        <template slot-scope="scope">
          <span>{{ typeLabel(scope.row.type) }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('calendar.startTime')" align="center" prop="startTime" width="150">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.startTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('calendar.endTime')" align="center" prop="endTime" width="150">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.endTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('calendar.location')" align="center" prop="location" :show-overflow-tooltip="true" />
      <el-table-column :label="$t('calendar.source')" align="center" prop="source" width="100" />
      <el-table-column :label="$t('common.operation')" align="center" class-name="small-padding fixed-width">
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
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['oa:calendar:edit']"
          >{{ $t('common.edit') }}</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['oa:calendar:remove']"
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

    <!-- 添加/修改/详情对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="720px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px" :disabled="isDetail">
        <el-row>
          <el-col :span="24">
            <el-form-item :label="$t('calendar.form.title')" prop="title">
              <el-input v-model="form.title" :placeholder="$t('calendar.search.titlePlaceholder')" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('calendar.search.type')" prop="type">
              <el-select v-model="form.type" :placeholder="$t('calendar.search.type')" style="width: 100%">
                <el-option :label="$t('calendar.type.personal')" value="personal" />
                <el-option :label="$t('calendar.type.meeting')" value="meeting" />
                <el-option :label="$t('calendar.type.leave')" value="leave" />
                <el-option :label="$t('calendar.type.trip')" value="trip" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('calendar.allDay')">
              <el-radio-group v-model="form.allDay">
                <el-radio :label="0">{{ $t('common.no') }}</el-radio>
                <el-radio :label="1">{{ $t('common.yes') }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="$t('calendar.search.dateRange')" prop="timeRange">
              <el-date-picker
                v-model="form.timeRange"
                type="datetimerange"
                value-format="yyyy-MM-dd HH:mm:ss"
                :default-time="['00:00:00','23:59:59']"
                range-separator="-"
                :start-placeholder="$t('calendar.startTime')"
                :end-placeholder="$t('calendar.endTime')"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('calendar.location')">
              <el-input v-model="form.location" :placeholder="$t('calendar.form.locationPlaceholder')" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('calendar.attendees')">
              <el-select v-model="form.attendeeIds" multiple collapse-tags :placeholder="$t('calendar.attendees')" style="width: 100%">
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
            <el-form-item :label="$t('calendar.form.remark')">
              <el-input v-model="form.remark" type="textarea" :rows="3" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button v-if="!isDetail" type="primary" @click="submitForm">{{ $t('common.submit') }}</el-button>
        <el-button @click="cancel">{{ $t('common.cancel') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import './i18n'
import { listCalendarEvent, getCalendarEvent, delCalendarEvent, addCalendarEvent, updateCalendarEvent } from "@/api/oa/calendar"
import { listUser } from "@/api/system/user"

export default {
  name: "OaCalendar",
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      eventList: [],
      title: "",
      open: false,
      isDetail: false,
      dateRange: [],
      userOptions: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        title: undefined,
        type: undefined
      },
      form: {},
      rules: {
        title: [
          { required: true, message: this.$t('calendar.form.titleRequired'), trigger: "blur" }
        ],
        type: [
          { required: true, message: this.$t('calendar.form.typeRequired'), trigger: "change" }
        ],
        timeRange: [
          { required: true, message: this.$t('calendar.form.timeRequired'), trigger: "change" }
        ]
      }
    }
  },
  created() {
    this.getList()
    this.loadUsers()
  },
  methods: {
    getList() {
      this.loading = true
      this.queryParams.params = {
        beginTime: this.dateRange && this.dateRange.length > 0 ? this.dateRange[0] : undefined,
        endTime: this.dateRange && this.dateRange.length > 0 ? this.dateRange[1] : undefined
      }
      listCalendarEvent(this.queryParams).then(response => {
        this.eventList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    loadUsers() {
      listUser({ pageSize: 1000 }).then(response => {
        this.userOptions = response.rows || []
      })
    },
    typeLabel(type) {
      return this.$te('calendar.type.' + type) ? this.$t('calendar.type.' + type) : type
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = {
        eventId: undefined,
        title: undefined,
        type: "personal",
        allDay: 0,
        timeRange: [],
        location: undefined,
        attendeeIds: [],
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
      this.ids = selection.map(item => item.eventId)
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = this.$t('calendar.add')
    },
    handleUpdate(row) {
      this.reset()
      const eventId = row.eventId || this.ids
      getCalendarEvent(eventId).then(response => {
        const data = response.data
        data.timeRange = [data.startTime, data.endTime]
        data.attendeeIds = data.attendeeIds || []
        this.form = data
        this.open = true
        this.title = this.$t('calendar.edit')
      })
    },
    handleView(row) {
      this.reset()
      getCalendarEvent(row.eventId).then(response => {
        const data = response.data
        data.timeRange = [data.startTime, data.endTime]
        data.attendeeIds = data.attendeeIds || []
        this.form = data
        this.open = true
        this.isDetail = true
        this.title = this.$t('calendar.detail')
      })
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          const data = { ...this.form }
          data.startTime = data.timeRange[0]
          data.endTime = data.timeRange[1]
          delete data.timeRange
          if (data.eventId != undefined) {
            updateCalendarEvent(data).then(() => {
              this.$modal.msgSuccess(this.$t('common.editSuccess'))
              this.open = false
              this.getList()
            })
          } else {
            addCalendarEvent(data).then(() => {
              this.$modal.msgSuccess(this.$t('common.addSuccess'))
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    handleDelete(row) {
      const eventIds = row.eventId || this.ids
      this.$modal.confirm(this.$t('calendar.deleteConfirm', { eventIds: eventIds })).then(function() {
        return delCalendarEvent(eventIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess(this.$t('common.deleteSuccess'))
      }).catch(() => {})
    }
  }
}
</script>
