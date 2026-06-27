<template>
  <div class="app-container addressbook-container">
    <el-row :gutter="10">
      <!-- 左侧部门树 -->
      <el-col :span="5" class="tree-panel">
        <el-card shadow="never" class="box-card">
          <div slot="header" class="clearfix">
            <span>{{ $t('addressBook.orgStructure') }}</span>
          </div>
          <el-input
            v-model="deptName"
            :placeholder="$t('user.form.deptIdPlaceholder')"
            clearable
            size="small"
            prefix-icon="el-icon-search"
            style="margin-bottom:10px;"
          />
          <el-tree
            ref="tree"
            :data="deptOptions"
            :props="defaultProps"
            :expand-on-click-node="false"
            :filter-node-method="filterNode"
            node-key="id"
            default-expand-all
            highlight-current
            @node-click="handleNodeClick"
          />
        </el-card>
      </el-col>

      <!-- 中间人员列表 -->
      <el-col :span="10" class="list-panel">
        <el-card shadow="never" class="box-card">
          <div slot="header" class="clearfix">
            <span>{{ $t('addressBook.title') }}</span>
          </div>
          <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" class="mb8">
            <el-form-item>
              <el-input
                v-model="queryParams.keyword"
                :placeholder="$t('addressBook.searchPlaceholder')"
                clearable
                @keyup.enter.native="handleQuery"
              />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">{{ $t('addressBook.search') }}</el-button>
              <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">{{ $t('addressBook.reset') }}</el-button>
            </el-form-item>
          </el-form>

          <el-table v-loading="loading" :data="userList" highlight-current-row @row-click="handleRowClick">
            <el-table-column :label="$t('addressBook.name')" align="left" prop="nickName" show-overflow-tooltip>
              <template slot-scope="scope">
                <div class="user-cell">
                  <el-avatar :size="32" :src="scope.row.avatar" icon="el-icon-user-solid" />
                  <div class="user-info">
                    <div>{{ scope.row.nickName || scope.row.userName }}</div>
                    <div class="user-dept">{{ scope.row.dept ? scope.row.dept.deptName : '-' }}</div>
                  </div>
                </div>
              </template>
            </el-table-column>
            <el-table-column :label="$t('addressBook.dept')" align="center" prop="dept.deptName" show-overflow-tooltip />
            <el-table-column :label="$t('addressBook.phone')" align="center" prop="phonenumber" width="120" />
          </el-table>

          <pagination
            v-show="total > 0"
            :total="total"
            :page.sync="queryParams.pageNum"
            :limit.sync="queryParams.pageSize"
            @pagination="getList"
          />
        </el-card>
      </el-col>

      <!-- 右侧人员详情 -->
      <el-col :span="9" class="detail-panel">
        <el-card v-if="selectedUser" shadow="never" class="box-card">
          <div class="detail-header">
            <el-avatar :size="64" :src="selectedUser.avatar" icon="el-icon-user-solid" />
            <div class="detail-title">
              <h3>{{ selectedUser.nickName || selectedUser.userName }}</h3>
              <p>{{ selectedUser.dept ? selectedUser.dept.deptName : '-' }} / {{ selectedUser.postName || '-' }}</p>
            </div>
          </div>

          <el-divider />

          <el-descriptions :column="1" border>
            <el-descriptions-item :label="$t('addressBook.account')">{{ selectedUser.userName }}</el-descriptions-item>
            <el-descriptions-item :label="$t('addressBook.name')">{{ selectedUser.nickName || '-' }}</el-descriptions-item>
            <el-descriptions-item :label="$t('addressBook.dept')">{{ selectedUser.dept ? selectedUser.dept.deptName : '-' }}</el-descriptions-item>
            <el-descriptions-item :label="$t('addressBook.phone')">{{ selectedUser.phonenumber || '-' }}</el-descriptions-item>
            <el-descriptions-item :label="$t('addressBook.email')">{{ selectedUser.email || '-' }}</el-descriptions-item>
            <el-descriptions-item :label="$t('addressBook.status')">
              <el-tag v-if="selectedUser.status === '0'" type="success" size="small">{{ $t('addressBook.normal') }}</el-tag>
              <el-tag v-else type="danger" size="small">{{ $t('addressBook.disabled') }}</el-tag>
            </el-descriptions-item>
          </el-descriptions>

          <div class="detail-actions">
            <el-button type="primary" size="small" icon="el-icon-star-off" @click="handleAddContact">
              {{ $t('addressBook.addContact') }}
            </el-button>
          </div>

          <el-divider />

          <h4>{{ $t('addressBook.reporting') }}</h4>
          <el-table :data="subordinates" size="small" :show-header="false">
            <el-table-column prop="nickName" />
            <el-table-column prop="dept.deptName" />
          </el-table>
        </el-card>

        <el-card v-else shadow="never" class="box-card empty-card">
          <p>{{ $t('addressBook.noData') }}</p>
        </el-card>
      </el-col>
    </el-row>

    <!-- 添加常用联系人对话框 -->
    <el-dialog :title="$t('addressBook.addContact')" :visible.sync="contactOpen" width="400px" append-to-body>
      <el-form ref="contactForm" :model="contactForm" label-width="80px">
        <el-form-item :label="$t('addressBook.contactGroups')">
          <el-select v-model="contactForm.groupId" clearable :placeholder="$t('addressBook.all')">
            <el-option
              v-for="group in contactGroups"
              :key="group.groupId"
              :label="group.groupName"
              :value="group.groupId"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitContact">{{ $t('common.confirm') }}</el-button>
        <el-button @click="contactOpen = false">{{ $t('common.cancel') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import './i18n'
import { getDeptTree, getDeptUsers, searchUsers, getUserDetail, addContact, listContactGroups } from '@/api/oa/addressbook'

export default {
  name: 'OaAddressBook',
  data() {
    return {
      // 遮罩层
      loading: false,
      // 部门树
      deptOptions: [],
      // 部门名称筛选
      deptName: undefined,
      // 部门树默认属性
      defaultProps: {
        children: 'children',
        label: 'label'
      },
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        keyword: undefined,
        deptId: undefined
      },
      // 人员列表
      userList: [],
      // 总条数
      total: 0,
      // 当前选中人员
      selectedUser: null,
      // 同部门同事
      subordinates: [],
      // 联系人分组
      contactGroups: [],
      // 添加联系人弹窗
      contactOpen: false,
      // 联系人表单
      contactForm: {
        contactUserId: undefined,
        groupId: undefined
      }
    }
  },
  watch: {
    deptName(val) {
      this.$refs.tree.filter(val)
    }
  },
  created() {
    this.getDeptTree()
    this.getList()
    this.getContactGroups()
  },
  methods: {
    /** 查询部门树 */
    getDeptTree() {
      getDeptTree().then(response => {
        this.deptOptions = response.data || []
      })
    },

    /** 节点筛选 */
    filterNode(value, data) {
      if (!value) return true
      return data.label.indexOf(value) !== -1
    },
    /** 节点点击 */
    handleNodeClick(data) {
      this.queryParams.deptId = data.id
      this.queryParams.keyword = undefined
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 查询人员列表 */
    getList() {
      this.loading = true
      if (this.queryParams.keyword) {
        searchUsers(this.queryParams).then(response => {
          this.userList = response.rows
          this.total = response.total
          this.loading = false
        }).catch(() => {
          this.loading = false
        })
      } else if (this.queryParams.deptId) {
        getDeptUsers(this.queryParams.deptId).then(response => {
          this.userList = response.data || []
          this.total = this.userList.length
          this.loading = false
        }).catch(() => {
          this.loading = false
        })
      } else {
        searchUsers(this.queryParams).then(response => {
          this.userList = response.rows
          this.total = response.total
          this.loading = false
        }).catch(() => {
          this.loading = false
        })
      }
    },
    /** 搜索按钮 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮 */
    resetQuery() {
      this.queryParams = {
        pageNum: 1,
        pageSize: 10,
        keyword: undefined,
        deptId: undefined
      }
      this.getList()
    },
    /** 行点击 */
    handleRowClick(row) {
      getUserDetail(row.userId).then(response => {
        this.selectedUser = response.data
        this.subordinates = response.subordinates || []
      })
    },
    /** 获取联系人分组 */
    getContactGroups() {
      listContactGroups().then(response => {
        this.contactGroups = response.data || []
      })
    },
    /** 添加常用联系人 */
    handleAddContact() {
      if (!this.selectedUser) {
        this.$modal.msgWarning(this.$t('addressBook.noData'))
        return
      }
      this.contactForm = {
        contactUserId: this.selectedUser.userId,
        groupId: undefined
      }
      this.contactOpen = true
    },
    /** 提交联系人 */
    submitContact() {
      addContact(this.contactForm).then(() => {
        this.$modal.msgSuccess(this.$t('common.success'))
        this.contactOpen = false
      })
    }
  }
}
</script>

<style scoped>
.addressbook-container {
  padding: 10px;
}
.tree-panel, .list-panel, .detail-panel {
  height: calc(100vh - 140px);
}
.box-card {
  height: 100%;
}
.user-cell {
  display: flex;
  align-items: center;
}
.user-info {
  margin-left: 10px;
}
.user-dept {
  font-size: 12px;
  color: #909399;
}
.detail-header {
  display: flex;
  align-items: center;
}
.detail-title {
  margin-left: 15px;
}
.detail-title h3 {
  margin: 0;
}
.detail-title p {
  margin: 5px 0 0;
  color: #606266;
  font-size: 14px;
}
.detail-actions {
  margin-top: 15px;
  text-align: center;
}
.empty-card {
  display: flex;
  align-items: center;
  justify-content: center;
  color: #909399;
}
</style>
