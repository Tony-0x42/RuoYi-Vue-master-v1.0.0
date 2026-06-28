<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item :label="$t('oa.attendance.group')" prop="name">
        <el-input
          v-model="queryParams.name"
          :placeholder="$t('oa.attendance.placeholder.name')"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="$t('oa.attendance.memberType')" prop="memberType">
        <el-select v-model="queryParams.memberType" :placeholder="$t('oa.attendance.placeholder.memberType')" clearable>
          <el-option v-for="item in memberTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
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
          v-hasPermi="['oa:attendanceGroup:add']"
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
          v-hasPermi="['oa:attendanceGroup:edit']"
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
          v-hasPermi="['oa:attendanceGroup:remove']"
        >{{ $t('common.delete') }}</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="groupList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <index-column :page="queryParams.pageNum" :size="queryParams.pageSize" />
      <el-table-column :label="$t('oa.attendance.group')" align="center" prop="name" />
      <el-table-column :label="$t('oa.attendance.memberType')" align="center" prop="memberType" width="120">
        <template slot-scope="scope">
          {{ memberTypeLabel(scope.row.memberType) }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('oa.attendance.shiftId')" align="center" prop="shiftName" width="160" />
      <el-table-column :label="$t('oa.attendance.checkInType')" align="center" prop="checkInType" width="120">
        <template slot-scope="scope">
          {{ typeLabel(scope.row.checkInType) }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('oa.attendance.workDays')" align="center" prop="workDays" width="140" />
      <el-table-column :label="$t('common.operation')" align="center" class-name="small-padding fixed-width" width="200">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['oa:attendanceGroup:edit']">{{ $t('common.edit') }}</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['oa:attendanceGroup:remove']">{{ $t('common.delete') }}</el-button>
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
  </div>
</template>

<script>
import { listGroup, delGroup, listShift } from "@/api/oa/attendance"

export default {
  name: "OaAttendanceGroup",
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      groupList: [],
      shiftOptions: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: undefined,
        memberType: undefined
      }
    }
  },
  computed: {
    memberTypeOptions() {
      return [
        { label: this.$t('oa.attendance.dept'), value: 'dept' },
        { label: this.$t('oa.attendance.role'), value: 'role' },
        { label: this.$t('oa.attendance.user'), value: 'user' }
      ]
    },
    checkInTypeOptions() {
      return [
        { label: this.$t('oa.attendance.gps'), value: 'gps' },
        { label: this.$t('oa.attendance.wifiType'), value: 'wifi' },
        { label: this.$t('oa.attendance.device'), value: 'device' },
        { label: this.$t('oa.attendance.field'), value: 'field' },
        { label: '多种', value: 'multiple' }
      ]
    }
  },
  created() {
    this.getList()
    this.loadShifts()
  },
  methods: {
    getList() {
      this.loading = true
      listGroup(this.queryParams).then(response => {
        this.groupList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    loadShifts() {
      listShift({ pageSize: 1000 }).then(response => {
        this.shiftOptions = response.rows || []
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
      this.$router.push('/oa/attendance/group/form?mode=add')
    },
    handleUpdate(row) {
      const id = row ? row.id : this.ids[0]
      this.$router.push('/oa/attendance/group/form?mode=edit&id=' + id)
    },
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm(this.$t('oa.attendance.confirm.deleteGroup', { ids })).then(function() {
        return delGroup(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess(this.$t('common.deleteSuccess'))
      }).catch(() => {})
    },
    memberTypeLabel(type) {
      return this.$te('oa.attendance.' + type) ? this.$t('oa.attendance.' + type) : type
    },
    typeLabel(type) {
      const key = type === 'wifi' ? 'wifiType' : type
      return this.$te('oa.attendance.' + key) ? this.$t('oa.attendance.' + key) : type
    }
  }
}
</script>
