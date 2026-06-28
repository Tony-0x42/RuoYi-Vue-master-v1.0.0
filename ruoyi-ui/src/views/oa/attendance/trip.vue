<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item :label="$t('oa.attendance.userId')" prop="userName">
        <el-input v-model="queryParams.userName" :placeholder="$t('oa.attendance.placeholder.userId')" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item :label="$t('oa.attendance.destination')" prop="destination">
        <el-input v-model="queryParams.destination" :placeholder="$t('oa.attendance.placeholder.destination')" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item :label="$t('common.status')" prop="status">
        <el-select v-model="queryParams.status" :placeholder="$t('common.status')" clearable style="width:160px">
          <el-option v-for="item in statusOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">{{ $t('common.search') }}</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{ $t('common.reset') }}</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['oa:attendanceTrip:add']">{{ $t('common.add') }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['oa:attendanceTrip:edit']">{{ $t('common.edit') }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['oa:attendanceTrip:remove']">{{ $t('common.delete') }}</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="tripList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <index-column :page="queryParams.pageNum" :size="queryParams.pageSize" />
      <el-table-column :label="$t('oa.attendance.userId')" align="center" prop="userName" width="120" />
      <el-table-column :label="$t('oa.attendance.destination')" align="center" prop="destination" :show-overflow-tooltip="true" />
      <el-table-column :label="$t('oa.attendance.startTime')" align="center" prop="startTime" width="160">
        <template slot-scope="scope"><span>{{ parseTime(scope.row.startTime) }}</span></template>
      </el-table-column>
      <el-table-column :label="$t('oa.attendance.endTime')" align="center" prop="endTime" width="160">
        <template slot-scope="scope"><span>{{ parseTime(scope.row.endTime) }}</span></template>
      </el-table-column>
      <el-table-column :label="$t('oa.attendance.days')" align="center" prop="days" width="100" />
      <el-table-column :label="$t('oa.attendance.reason')" align="center" prop="reason" :show-overflow-tooltip="true" />
      <el-table-column :label="$t('common.status')" align="center" prop="status" width="100">
        <template slot-scope="scope">
          <el-tag :type="statusType(scope.row.status)" size="mini">{{ statusLabel(scope.row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="$t('common.operation')" align="center" class-name="small-padding fixed-width" width="220">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-s-promotion"
            @click="handleSubmit(scope.row)"
            v-hasPermi="['oa:attendanceTrip:edit']"
            v-if="scope.row.status === 'draft'"
          >{{ $t('oa.attendance.submitTrip') }}</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['oa:attendanceTrip:edit']"
          >{{ $t('common.edit') }}</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['oa:attendanceTrip:remove']"
          >{{ $t('common.delete') }}</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <flow-submit-dialog
      :visible="flowVisible"
      process-key="oa_attendance_trip"
      :business-key="currentRow ? 'attendance_trip:' + currentRow.id : ''"
      :submit-api="submitApi"
      @close="flowVisible = false"
      @success="handleSubmitSuccess"
    />
  </div>
</template>

<script>
import { listTrip, delTrip, submitTrip } from "@/api/oa/attendance"
import FlowSubmitDialog from "@/components/FlowSubmitDialog"

export default {
  name: "OaAttendanceTrip",
  components: { FlowSubmitDialog },
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      tripList: [],
      flowVisible: false,
      currentRow: null,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userName: undefined,
        destination: undefined,
        status: undefined
      }
    }
  },
  computed: {
    statusOptions() {
      return [
        { label: this.$t('oa.attendance.draft'), value: 'draft' },
        { label: this.$t('oa.attendance.approving'), value: 'approving' },
        { label: this.$t('oa.attendance.agreed'), value: 'agreed' },
        { label: this.$t('oa.attendance.rejected'), value: 'rejected' }
      ]
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      listTrip(this.queryParams).then(response => {
        this.tripList = response.rows
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
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    handleAdd() {
      this.$router.push({ path: '/oa/hr/trip/form' })
    },
    handleUpdate(row) {
      const id = row ? row.id : this.ids[0]
      this.$router.push({ path: '/oa/hr/trip/form', query: { id } })
    },
    handleSubmit(row) {
      this.currentRow = row
      this.flowVisible = true
    },
    submitApi(data) {
      return submitTrip(this.currentRow.id, data)
    },
    handleSubmitSuccess() {
      this.getList()
      this.$modal.msgSuccess(this.$t('oa.attendance.submitApproval'))
    },
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm(this.$t('oa.attendance.confirm.deleteTrip', { ids })).then(function() {
        return delTrip(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess(this.$t('common.deleteSuccess'))
      }).catch(() => {})
    },
    statusType(status) {
      const map = { draft: 'info', approving: 'warning', agreed: 'success', rejected: 'danger' }
      return map[status] || 'info'
    },
    statusLabel(status) {
      return this.$te('oa.attendance.' + status) ? this.$t('oa.attendance.' + status) : status
    }
  }
}
</script>
