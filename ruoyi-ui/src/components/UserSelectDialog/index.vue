<template>
  <div class="user-select-dialog-wrap">
    <slot />
    <el-dialog
      :title="title"
      :visible.sync="dialogVisible"
      width="800px"
      append-to-body
      @opened="handleOpen"
      @closed="handleClosed"
    >
      <el-form ref="queryForm" :model="queryParams" size="small" :inline="true">
        <el-form-item label="用户名称" prop="userName">
          <el-input
            v-model="queryParams.userName"
            placeholder="请输入用户名称"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table
        v-loading="loading"
        :data="userList"
        highlight-current-row
        @row-click="handleRowClick"
        @current-change="handleCurrentChange"
      >
        <index-column :page="queryParams.pageNum" :size="queryParams.pageSize" />
        <el-table-column label="" width="55" align="center">
          <template slot-scope="scope">
            <el-radio v-model="selectedId" :label="stringId(scope.row.userId)">&nbsp;</el-radio>
          </template>
        </el-table-column>
        <el-table-column label="用户名称" align="center" prop="userName" :show-overflow-tooltip="true" />
        <el-table-column label="用户昵称" align="center" prop="nickName" :show-overflow-tooltip="true" />
        <el-table-column label="部门" align="center" prop="deptName" :show-overflow-tooltip="true" />
      </el-table>

      <pagination
        v-show="total > 0"
        :total="total"
        :page.sync="queryParams.pageNum"
        :limit.sync="queryParams.pageSize"
        @pagination="getList"
      />

      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="handleConfirm">确 定</el-button>
        <el-button @click="handleCancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listUser } from '@/api/system/user'
import Pagination from '@/components/Pagination'

export default {
  name: 'UserSelectDialog',
  components: {
    Pagination
  },
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    value: {
      type: String,
      default: ''
    },
    title: {
      type: String,
      default: '选择用户'
    }
  },
  data() {
    return {
      dialogVisible: this.visible,
      selectedId: '',
      userList: [],
      total: 0,
      loading: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userName: undefined
      }
    }
  },
  watch: {
    visible(val) {
      this.dialogVisible = val
    },
    dialogVisible(val) {
      this.$emit('update:visible', val)
    },
    value(val) {
      const id = val || ''
      if (this.selectedId !== id) {
        this.selectedId = id
      }
    }
  },
  methods: {
    stringId(id) {
      return id == null ? '' : String(id)
    },
    handleOpen() {
      this.selectedId = this.value || ''
      this.queryParams.userName = undefined
      this.queryParams.pageNum = 1
      this.queryParams.pageSize = 10
      this.getList()
    },
    handleClosed() {
      this.queryParams.userName = undefined
      this.queryParams.pageNum = 1
      this.queryParams.pageSize = 10
      this.selectedId = this.value || ''
    },
    getList() {
      this.loading = true
      listUser(this.queryParams).then(response => {
        this.userList = response.rows || []
        this.total = response.total || 0
      }).finally(() => {
        this.loading = false
      })
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.queryParams.userName = undefined
      this.handleQuery()
    },
    handleRowClick(row) {
      if (row) {
        this.selectedId = this.stringId(row.userId)
      }
    },
    handleCurrentChange(row) {
      if (row) {
        this.selectedId = this.stringId(row.userId)
      }
    },
    handleConfirm() {
      if (!this.selectedId) {
        this.$message.warning('请选择用户')
        return
      }
      this.$emit('input', this.selectedId)
      this.$emit('change', this.selectedId)
      this.dialogVisible = false
    },
    handleCancel() {
      this.dialogVisible = false
    }
  }
}
</script>
