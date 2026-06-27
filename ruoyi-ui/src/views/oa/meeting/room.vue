<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item :label="$t('oa.meeting.room.code')" prop="code">
        <el-input
          v-model="queryParams.code"
          :placeholder="$t('oa.meeting.room.codePlaceholder')"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="$t('oa.meeting.room.name')" prop="name">
        <el-input
          v-model="queryParams.name"
          :placeholder="$t('oa.meeting.room.namePlaceholder')"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item :label="$t('common.status')" prop="status">
        <el-select v-model="queryParams.status" :placeholder="$t('common.status')" clearable>
          <el-option :label="$t('oa.meeting.room.enable')" :value="1" />
          <el-option :label="$t('oa.meeting.room.disable')" :value="0" />
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
          v-hasPermi="['oa:meetingRoom:add']"
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
          v-hasPermi="['oa:meetingRoom:edit']"
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
          v-hasPermi="['oa:meetingRoom:remove']"
        >{{ $t('common.delete') }}</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="roomList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <index-column :page="queryParams.pageNum" :size="queryParams.pageSize" />
      <el-table-column :label="$t('oa.meeting.room.code')" align="center" prop="code" />
      <el-table-column :label="$t('oa.meeting.room.name')" align="center" prop="name" />
      <el-table-column :label="$t('oa.meeting.room.location')" align="center" prop="location" :show-overflow-tooltip="true" />
      <el-table-column :label="$t('oa.meeting.room.capacity')" align="center" prop="capacity" width="80" />
      <el-table-column :label="$t('oa.meeting.room.devices')" align="center" prop="devices" :show-overflow-tooltip="true" />
      <el-table-column :label="$t('common.status')" align="center" prop="status" width="100">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === 1" type="success">{{ $t('oa.meeting.room.enable') }}</el-tag>
          <el-tag v-else type="danger">{{ $t('oa.meeting.room.disable') }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="$t('common.createTime')" align="center" prop="createTime" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('common.operation')" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['oa:meetingRoom:edit']"
          >{{ $t('common.edit') }}</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['oa:meetingRoom:remove']"
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

    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item :label="$t('oa.meeting.room.code')" prop="code">
          <el-input v-model="form.code" :placeholder="$t('oa.meeting.room.codePlaceholder')" />
        </el-form-item>
        <el-form-item :label="$t('oa.meeting.room.name')" prop="name">
          <el-input v-model="form.name" :placeholder="$t('oa.meeting.room.namePlaceholder')" />
        </el-form-item>
        <el-form-item :label="$t('oa.meeting.room.location')" prop="location">
          <el-input v-model="form.location" :placeholder="$t('oa.meeting.room.locationPlaceholder')" />
        </el-form-item>
        <el-form-item :label="$t('oa.meeting.room.capacity')" prop="capacity">
          <el-input-number v-model="form.capacity" :min="1" controls-position="right" style="width: 100%" />
        </el-form-item>
        <el-form-item :label="$t('oa.meeting.room.devices')" prop="devices">
          <el-input v-model="form.devices" :placeholder="$t('oa.meeting.room.devicesPlaceholder')" />
        </el-form-item>
        <el-form-item :label="$t('common.status')" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">{{ $t('oa.meeting.room.enable') }}</el-radio>
            <el-radio :label="0">{{ $t('oa.meeting.room.disable') }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item :label="$t('oa.meeting.room.picUrl')" prop="picUrl">
          <el-input v-model="form.picUrl" placeholder="https://" />
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
import { listRoom, getRoom, addRoom, updateRoom, delRoom } from "@/api/oa/meeting"

export default {
  name: "OaMeetingRoom",
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      roomList: [],
      title: "",
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        code: undefined,
        name: undefined,
        status: undefined
      },
      form: {},
      rules: {
        code: [
          { required: true, message: this.$t('oa.meeting.required.code'), trigger: "blur" }
        ],
        name: [
          { required: true, message: this.$t('oa.meeting.required.name'), trigger: "blur" }
        ],
        capacity: [
          { required: true, message: this.$t('oa.meeting.required.capacity'), trigger: "blur" }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      listRoom(this.queryParams).then(response => {
        this.roomList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = {
        id: undefined,
        code: undefined,
        name: undefined,
        location: undefined,
        capacity: 10,
        devices: undefined,
        status: 1,
        picUrl: undefined
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
      this.title = this.$t('common.add') + this.$t('oa.meeting.roomTitle')
    },
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getRoom(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = this.$t('common.edit') + this.$t('oa.meeting.roomTitle')
      })
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != undefined) {
            updateRoom(this.form).then(() => {
              this.$modal.msgSuccess(this.$t('common.editSuccess'))
              this.open = false
              this.getList()
            })
          } else {
            addRoom(this.form).then(() => {
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
      this.$modal.confirm(this.$t('oa.meeting.deleteRoomConfirm', { ids })).then(function() {
        return delRoom(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess(this.$t('common.deleteSuccess'))
      }).catch(() => {})
    }
  }
}
</script>
