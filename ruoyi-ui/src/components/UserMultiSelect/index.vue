<template>
  <div class="user-multi-select">
    <div class="selected-summary">
      <el-button icon="el-icon-user" size="mini" @click="openDialog">选择用户</el-button>
      <template v-if="selectedCount > 0">
        <el-tag
          v-for="user in selectedUsers"
          :key="stringId(user.userId)"
          size="mini"
          closable
          class="user-tag"
          @close="removeUser(user)"
        >
          {{ formatTag(user) }}
        </el-tag>
        <span class="count-text">已选择 {{ selectedCount }} 人</span>
      </template>
      <span v-else class="no-selection">未选择用户</span>
    </div>

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

      <div v-if="selectedCount > 0" class="dialog-selection">
        <span class="dialog-selection-label">已选择：</span>
        <el-tag
          v-for="user in selectedUsers"
          :key="stringId(user.userId)"
          size="mini"
          class="user-tag"
        >
          {{ formatTag(user) }}
        </el-tag>
      </div>

      <el-table
        ref="userTable"
        v-loading="loading"
        :data="userList"
        row-key="userId"
        :reserve-selection="true"
        @selection-change="handleSelectionChange"
      >
        <index-column :page="queryParams.pageNum" :size="queryParams.pageSize" />
        <el-table-column type="selection" width="55" align="center" />
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
        <span class="selected-count">已选择 {{ selectedCount }} 人</span>
        <el-button type="primary" @click="handleConfirm">确 定</el-button>
        <el-button @click="handleCancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listUser, getUser } from '@/api/system/user'
import Pagination from '@/components/Pagination'

export default {
  name: 'UserMultiSelect',
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
      selectedUsers: [],
      userList: [],
      total: 0,
      loading: false,
      isSyncingSelection: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userName: undefined
      }
    }
  },
  computed: {
    selectedCount() {
      return this.selectedUsers.length
    }
  },
  watch: {
    visible(val) {
      this.dialogVisible = val
    },
    dialogVisible(val) {
      this.$emit('update:visible', val)
    },
    value: {
      immediate: true,
      handler(val) {
        this.syncValueToUsers(val)
      }
    }
  },
  methods: {
    stringId(id) {
      return id == null ? '' : String(id)
    },
    parseValue(value) {
      if (!value) return []
      return [...new Set(String(value).split(',').map(s => s.trim()).filter(Boolean))]
    },
    formatTag(user) {
      if (!user) return ''
      return user.nickName || user.userName || this.stringId(user.userId)
    },
    syncValueToUsers(value) {
      const ids = this.parseValue(value)
      const existingMap = new Map(this.selectedUsers.map(u => [this.stringId(u.userId), u]))
      const keptUsers = []
      const unknownIds = []

      for (const id of ids) {
        if (existingMap.has(id)) {
          keptUsers.push(existingMap.get(id))
        } else {
          unknownIds.push(id)
        }
      }

      if (unknownIds.length === 0) {
        this.selectedUsers = keptUsers
        this.applySelectionToTable()
        return
      }

      const fetchTasks = unknownIds.map(id =>
        getUser(id)
          .then(response => response.data || null)
          .catch(() => ({ userId: id, userName: id, nickName: id }))
      )

      Promise.all(fetchTasks).then(fetchedUsers => {
        const resolved = fetchedUsers.filter(Boolean)
        this.selectedUsers = [...keptUsers, ...resolved]
        this.applySelectionToTable()
      })
    },
    openDialog() {
      this.dialogVisible = true
    },
    handleOpen() {
      this.queryParams.userName = undefined
      this.queryParams.pageNum = 1
      this.queryParams.pageSize = 10
      this.syncValueToUsers(this.value)
      this.getList()
    },
    handleClosed() {
      this.queryParams.userName = undefined
      this.queryParams.pageNum = 1
      this.queryParams.pageSize = 10
      this.syncValueToUsers(this.value)
    },
    getList() {
      this.loading = true
      listUser(this.queryParams).then(response => {
        this.userList = response.rows || []
        this.total = response.total || 0
        this.$nextTick(() => {
          this.applySelectionToTable()
        })
      }).finally(() => {
        this.loading = false
      })
    },
    applySelectionToTable() {
      const table = this.$refs.userTable
      if (!table || !this.userList.length) return
      this.isSyncingSelection = true
      const selectedIds = new Set(this.selectedUsers.map(u => this.stringId(u.userId)))
      for (const row of this.userList) {
        const id = this.stringId(row.userId)
        const shouldBeSelected = selectedIds.has(id)
        table.toggleRowSelection(row, shouldBeSelected)
      }
      this.isSyncingSelection = false
    },
    handleSelectionChange(selection) {
      if (this.isSyncingSelection) return
      const currentPageIds = new Set(this.userList.map(row => this.stringId(row.userId)))
      const selectedMap = new Map(this.selectedUsers.map(u => [this.stringId(u.userId), u]))

      for (const row of selection) {
        selectedMap.set(this.stringId(row.userId), row)
      }
      for (const id of currentPageIds) {
        if (!selection.some(item => this.stringId(item.userId) === id)) {
          selectedMap.delete(id)
        }
      }

      this.selectedUsers = Array.from(selectedMap.values())
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.queryParams.userName = undefined
      this.handleQuery()
    },
    removeUser(user) {
      const id = this.stringId(user.userId)
      this.selectedUsers = this.selectedUsers.filter(u => this.stringId(u.userId) !== id)
      this.applySelectionToTable()
      this.emitChange()
    },
    emitChange() {
      const value = this.selectedUsers.map(u => this.stringId(u.userId)).join(',')
      this.$emit('input', value)
      this.$emit('change', value)
    },
    handleConfirm() {
      this.emitChange()
      this.dialogVisible = false
    },
    handleCancel() {
      this.dialogVisible = false
    }
  }
}
</script>

<style scoped>
.user-multi-select {
  display: inline-block;
}
.selected-summary {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 8px;
}
.user-tag {
  margin-left: 4px;
}
.count-text {
  color: #606266;
  font-size: 12px;
}
.no-selection {
  color: #909399;
  font-size: 12px;
}
.dialog-selection {
  margin-bottom: 12px;
  padding: 8px 12px;
  background-color: #f5f7fa;
  border-radius: 4px;
}
.dialog-selection-label {
  color: #606266;
  font-size: 13px;
}
.selected-count {
  float: left;
  line-height: 32px;
  color: #606266;
  font-size: 13px;
}
</style>
