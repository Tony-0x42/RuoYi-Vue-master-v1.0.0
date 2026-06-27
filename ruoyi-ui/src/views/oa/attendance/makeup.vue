<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item :label="$t('oa.attendance.userId')" prop="userName">
        <el-input v-model="queryParams.userName" :placeholder="$t('oa.attendance.placeholder.userId')" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item :label="$t('oa.attendance.checkType')" prop="checkType">
        <el-select v-model="queryParams.checkType" :placeholder="$t('oa.attendance.placeholder.checkType')" clearable style="width:160px">
          <el-option v-for="item in checkTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
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
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['oa:attendanceMakeup:add']">{{ $t('common.add') }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['oa:attendanceMakeup:edit']">{{ $t('common.edit') }}</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['oa:attendanceMakeup:remove']">{{ $t('common.delete') }}</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="makeupList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <index-column :page="queryParams.pageNum" :size="queryParams.pageSize" />
      <el-table-column :label="$t('oa.attendance.userId')" align="center" prop="userName" width="120" />
      <el-table-column :label="$t('oa.attendance.makeupDate')" align="center" prop="makeupDate" width="120">
        <template slot-scope="scope"><span>{{ parseTime(scope.row.makeupDate, '{y}-{m}-{d}') }}</span></template>
      </el-table-column>
      <el-table-column :label="$t('oa.attendance.checkType')" align="center" prop="checkType" width="120">
        <template slot-scope="scope">{{ checkTypeLabel(scope.row.checkType) }}</template>
      </el-table-column>
      <el-table-column :label="$t('oa.attendance.checkInTime')" align="center" prop="checkTime" width="160">
        <template slot-scope="scope"><span>{{ parseTime(scope.row.checkTime) }}</span></template>
      </el-table-column>
      <el-table-column :label="$t('oa.attendance.reason')" align="center" prop="reason" :show-overflow-tooltip="true" />
      <el-table-column :label="$t('common.status')" align="center" prop="status" width="100">
        <template slot-scope="scope">
          <el-tag :type="statusType(scope.row.status)" size="mini">{{ statusLabel(scope.row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="$t('common.operation')" align="center" class-name="small-padding fixed-width" width="220">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-s-promotion" @click="handleSubmit(scope.row)" v-hasPermi="['oa:attendanceMakeup:edit']" v-if="scope.row.status === 'draft'">{{ $t('oa.attendance.submitMakeup') }}</el-button>
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['oa:attendanceMakeup:edit']">{{ $t('common.edit') }}</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['oa:attendanceMakeup:remove']">{{ $t('common.delete') }}</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item :label="$t('oa.attendance.userId')" prop="userId">
          <el-select v-model="form.userId" :placeholder="$t('oa.attendance.placeholder.userId')" filterable style="width:100%">
            <el-option v-for="user in userOptions" :key="user.userId" :label="user.nickName || user.userName" :value="user.userId" />
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('oa.attendance.makeupDate')" prop="makeupDate">
          <el-date-picker v-model="form.makeupDate" type="date" value-format="yyyy-MM-dd" :placeholder="$t('oa.attendance.placeholder.makeupDate')" style="width:100%" />
        </el-form-item>
        <el-form-item :label="$t('oa.attendance.checkType')" prop="checkType">
          <el-select v-model="form.checkType" :placeholder="$t('oa.attendance.placeholder.checkType')" style="width:100%">
            <el-option v-for="item in checkTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('oa.attendance.checkInTime')" prop="checkTime">
          <el-date-picker v-model="form.checkTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" :placeholder="$t('oa.attendance.placeholder.checkTime')" style="width:100%" />
        </el-form-item>
        <el-form-item :label="$t('oa.attendance.reason')" prop="reason">
          <el-input v-model="form.reason" type="textarea" :placeholder="$t('oa.attendance.placeholder.reason')" />
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
import { listMakeup, getMakeup, addMakeup, updateMakeup, delMakeup, submitMakeup } from "@/api/oa/attendance"
import { listUser } from "@/api/system/user"

export default {
  name: "OaAttendanceMakeup",
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      makeupList: [],
      userOptions: [],
      title: "",
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userName: undefined,
        checkType: undefined,
        status: undefined
      },
      form: {},
      rules: {
        userId: [{ required: true, message: this.$t('oa.attendance.required.userId'), trigger: "change" }],
        makeupDate: [{ required: true, message: this.$t('oa.attendance.required.makeupDate'), trigger: "change" }],
        checkType: [{ required: true, message: this.$t('oa.attendance.required.checkType'), trigger: "change" }],
        reason: [{ required: true, message: this.$t('oa.attendance.required.reason'), trigger: "blur" }]
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
    },
    checkTypeOptions() {
      return [
        { label: this.$t('oa.attendance.checkIn'), value: 'check_in' },
        { label: this.$t('oa.attendance.checkOut'), value: 'check_out' }
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
      listMakeup(this.queryParams).then(response => {
        this.makeupList = response.rows
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
        makeupDate: undefined,
        checkType: 'check_in',
        checkTime: undefined,
        reason: undefined,
        status: 'draft'
      }
      this.resetForm("form")
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
      this.reset()
      this.open = true
      this.title = this.$t('oa.attendance.addMakeup')
    },
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getMakeup(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = this.$t('oa.attendance.editMakeup')
      })
    },
    handleSubmit(row) {
      this.$modal.confirm(this.$t('oa.attendance.confirm.submitMakeup', { id: row.id })).then(function() {
        return submitMakeup(row.id)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess(this.$t('oa.attendance.submitApproval'))
      }).catch(() => {})
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          const data = { ...this.form }
          if (data.id != undefined) {
            updateMakeup(data).then(() => {
              this.$modal.msgSuccess(this.$t('common.editSuccess'))
              this.open = false
              this.getList()
            })
          } else {
            addMakeup(data).then(() => {
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
      this.$modal.confirm(this.$t('oa.attendance.confirm.deleteMakeup', { ids })).then(function() {
        return delMakeup(ids)
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
    },
    checkTypeLabel(type) {
      return type === 'check_out' ? this.$t('oa.attendance.checkOut') : this.$t('oa.attendance.checkIn')
    }
  }
}
</script>
