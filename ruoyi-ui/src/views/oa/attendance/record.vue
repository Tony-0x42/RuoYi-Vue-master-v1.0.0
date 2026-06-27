<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item :label="$t('oa.attendance.userId')" prop="userId">
        <el-select v-model="queryParams.userId" :placeholder="$t('oa.attendance.placeholder.userId')" clearable filterable>
          <el-option v-for="user in userOptions" :key="user.userId" :label="user.nickName || user.userName" :value="user.userId" />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('oa.attendance.status')" prop="status">
        <el-select v-model="queryParams.status" :placeholder="$t('oa.attendance.placeholder.status')" clearable>
          <el-option v-for="item in statusOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('common.createTime')">
        <el-date-picker
          v-model="dateRange"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          :start-placeholder="$t('common.startDate')"
          :end-placeholder="$t('common.endDate')"
        ></el-date-picker>
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
          v-hasPermi="['oa:attendanceRecord:add']"
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
          v-hasPermi="['oa:attendanceRecord:edit']"
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
          v-hasPermi="['oa:attendanceRecord:remove']"
        >{{ $t('common.delete') }}</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="recordList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <index-column :page="queryParams.pageNum" :size="queryParams.pageSize" />
      <el-table-column :label="$t('oa.attendance.userId')" align="center" prop="userName" width="120" />
      <el-table-column :label="$t('oa.attendance.checkInDate')" align="center" prop="checkInDate" width="120" />
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
      <el-table-column :label="$t('common.operation')" align="center" class-name="small-padding fixed-width" width="200">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['oa:attendanceRecord:edit']">{{ $t('common.edit') }}</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['oa:attendanceRecord:remove']">{{ $t('common.delete') }}</el-button>
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

    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item :label="$t('oa.attendance.userId')" prop="userId">
          <el-select v-model="form.userId" :placeholder="$t('oa.attendance.placeholder.userId')" filterable style="width:100%">
            <el-option v-for="user in userOptions" :key="user.userId" :label="user.nickName || user.userName" :value="user.userId" />
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('oa.attendance.checkInTime')" prop="checkInTime">
          <el-date-picker
            v-model="form.checkInTime"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            :placeholder="$t('oa.attendance.checkInTime')"
            style="width:100%"
          />
        </el-form-item>
        <el-form-item :label="$t('oa.attendance.checkInType')" prop="type">
          <el-select v-model="form.type" :placeholder="$t('oa.attendance.placeholder.checkInType')" style="width:100%">
            <el-option v-for="item in typeOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('oa.attendance.location')" prop="location">
          <el-input v-model="form.location" :placeholder="$t('oa.attendance.placeholder.location')" />
        </el-form-item>
        <el-form-item :label="$t('oa.attendance.status')" prop="status">
          <el-select v-model="form.status" :placeholder="$t('oa.attendance.placeholder.status')" style="width:100%">
            <el-option v-for="item in statusOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('common.remark')">
          <el-input v-model="form.remark" type="textarea" :rows="2" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">{{ $t('common.submit') }}</el-button>
        <el-button @click="cancel">{{ $t('common.cancel') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listRecord, getRecord, addRecord, updateRecord, delRecord } from "@/api/oa/attendance"
import { listUser } from "@/api/system/user"

export default {
  name: "OaAttendanceRecord",
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      recordList: [],
      userOptions: [],
      dateRange: [],
      title: "",
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userId: undefined,
        status: undefined
      },
      form: {},
      rules: {
        userId: [{ required: true, message: this.$t('oa.attendance.required.userId'), trigger: "change" }],
        checkInTime: [{ required: true, message: this.$t('oa.attendance.checkInTime'), trigger: "change" }]
      }
    }
  },
  computed: {
    statusOptions() {
      return [
        { label: this.$t('oa.attendance.normal'), value: 'normal' },
        { label: this.$t('oa.attendance.late'), value: 'late' },
        { label: this.$t('oa.attendance.early'), value: 'early' },
        { label: this.$t('oa.attendance.absent'), value: 'absent' }
      ]
    },
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
    this.getList()
    this.loadUsers()
  },
  methods: {
    getList() {
      this.loading = true
      const query = { ...this.queryParams }
      if (this.dateRange && this.dateRange.length === 2) {
        query.params = { beginTime: this.dateRange[0], endTime: this.dateRange[1] }
      }
      listRecord(query).then(response => {
        this.recordList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    loadUsers() {
      listUser({ pageSize: 1000 }).then(response => {
        this.userOptions = response.rows || []
      })
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = {
        id: undefined,
        userId: undefined,
        checkInTime: undefined,
        type: "gps",
        location: undefined,
        status: "normal",
        remark: undefined
      }
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
      this.title = this.$t('oa.attendance.addRecord')
    },
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getRecord(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = this.$t('oa.attendance.editRecord')
      })
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != undefined) {
            updateRecord(this.form).then(() => {
              this.$modal.msgSuccess(this.$t('common.editSuccess'))
              this.open = false
              this.getList()
            })
          } else {
            addRecord(this.form).then(() => {
              this.$modal.msgSuccess(this.$t('common.addSuccess'))
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm(this.$t('oa.attendance.confirm.deleteRecord', { ids })).then(function() {
        return delRecord(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess(this.$t('common.deleteSuccess'))
      }).catch(() => {})
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
    }
  }
}
</script>
