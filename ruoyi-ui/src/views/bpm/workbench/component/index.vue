<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="组件名称" prop="componentName">
        <el-input v-model="queryParams.componentName" placeholder="请输入组件英文名" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="显示名称" prop="componentLabel">
        <el-input v-model="queryParams.componentLabel" placeholder="请输入显示名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="状态" clearable>
          <el-option label="正常" value="0" />
          <el-option label="停用" value="1" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['bpm:workbench:component:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['bpm:workbench:component:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['bpm:workbench:component:remove']">删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="componentList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <index-column :page="queryParams.pageNum" :size="queryParams.pageSize" />
      <el-table-column label="英文名" align="center" prop="componentName" :show-overflow-tooltip="true" />
      <el-table-column label="显示名称" align="center" prop="componentLabel" />
      <el-table-column label="组件路径" align="center" prop="componentPath" :show-overflow-tooltip="true" />
      <el-table-column label="图标" align="center" prop="icon" width="80" />
      <el-table-column label="状态" align="center" prop="status" width="80">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.status" active-value="0" inactive-value="1" @change="handleStatusChange(scope.row)"></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="160">
        <template slot-scope="scope"><span>{{ parseTime(scope.row.createTime) }}</span></template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="180">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['bpm:workbench:component:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['bpm:workbench:component:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="组件英文名" prop="componentName">
          <el-input v-model="form.componentName" placeholder="如：TaskTodo" :disabled="form.componentId != undefined" />
        </el-form-item>
        <el-form-item label="显示名称" prop="componentLabel">
          <el-input v-model="form.componentLabel" placeholder="如：待办任务" />
        </el-form-item>
        <el-form-item label="组件路径" prop="componentPath">
          <el-input v-model="form.componentPath" placeholder="如：@/views/bpm/workbench/components/TaskTodo" />
        </el-form-item>
        <el-form-item label="图标" prop="icon">
          <el-input v-model="form.icon" placeholder="如：el-icon-s-check" />
        </el-form-item>
        <el-form-item label="描述" prop="componentDesc">
          <el-input v-model="form.componentDesc" type="textarea" placeholder="组件描述" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio label="0">正常</el-radio>
            <el-radio label="1">停用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="可用范围" prop="scopeList">
          <div class="scope-tip">不配置则全员可用</div>
          <el-tabs v-model="scopeTab" type="border-card">
            <el-tab-pane label="角色" name="role">
              <el-select v-model="roleScope" multiple filterable placeholder="选择角色" style="width: 100%" @change="handleScopeChange('1', $event)">
                <el-option v-for="role in roleList" :key="role.roleId" :label="role.roleName" :value="role.roleId" />
              </el-select>
            </el-tab-pane>
            <el-tab-pane label="部门" name="dept">
              <treeselect v-model="deptScope" :multiple="true" :options="deptOptions" :show-count="true" placeholder="选择部门" @input="handleScopeChange('2', $event)" />
            </el-tab-pane>
            <el-tab-pane label="人员" name="user">
              <el-select v-model="userScope" multiple filterable remote :remote-method="searchUser" placeholder="选择人员" style="width: 100%" @change="handleScopeChange('3', $event)">
                <el-option v-for="user in userList" :key="user.userId" :label="user.nickName || user.userName" :value="user.userId" />
              </el-select>
            </el-tab-pane>
          </el-tabs>
          <div class="scope-tags">
            <el-tag v-for="(item, index) in form.scopeList" :key="index" closable size="mini" @close="removeScope(index)" style="margin-right: 6px; margin-top: 6px;">
              {{ scopeTypeLabel(item.scopeType) }}: {{ item.scopeTargetName || item.scopeTargetId }}
            </el-tag>
          </div>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listComponent, getComponent, addComponent, updateComponent, delComponent } from "@/api/bpm/workbench"
import { listRole } from "@/api/system/role"
import { listDept } from "@/api/system/dept"
import { listUser } from "@/api/system/user"
import Treeselect from "@riophae/vue-treeselect"
import "@riophae/vue-treeselect/dist/vue-treeselect.css"

export default {
  name: "BpmWorkbenchComponent",
  components: { Treeselect },
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      componentList: [],
      title: "",
      open: false,
      scopeTab: "role",
      roleScope: [],
      deptScope: [],
      userScope: [],
      roleList: [],
      deptOptions: [],
      userList: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        componentName: undefined,
        componentLabel: undefined,
        status: undefined
      },
      form: {
        componentName: undefined,
        componentLabel: undefined,
        componentPath: undefined,
        componentDesc: undefined,
        icon: undefined,
        status: "0",
        scopeList: []
      },
      rules: {
        componentName: [{ required: true, message: "组件英文名不能为空", trigger: "blur" }],
        componentLabel: [{ required: true, message: "显示名称不能为空", trigger: "blur" }],
        componentPath: [{ required: true, message: "组件路径不能为空", trigger: "blur" }]
      }
    }
  },
  created() {
    this.getList()
    this.loadRoleList()
    this.loadDeptList()
  },
  methods: {
    getList() {
      this.loading = true
      listComponent(this.queryParams).then(response => {
        this.componentList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    loadRoleList() {
      listRole({ pageSize: 1000 }).then(response => {
        this.roleList = response.rows || []
      })
    },
    loadDeptList() {
      listDept().then(response => {
        this.deptOptions = this.handleTree(response.data, "deptId")
      })
    },
    searchUser(query) {
      if (query.length < 1) return
      listUser({ userName: query, pageSize: 50 }).then(response => {
        this.userList = response.rows || []
      })
    },
    handleScopeChange(scopeType, values) {
      // 移除当前类型的旧范围
      this.form.scopeList = this.form.scopeList.filter(s => s.scopeType !== scopeType)
      if (values && values.length > 0) {
        values.forEach(id => {
          let name = id
          if (scopeType === '1') {
            const role = this.roleList.find(r => r.roleId === id)
            name = role ? role.roleName : id
          } else if (scopeType === '2') {
            const dept = this.findDept(this.deptOptions, id)
            name = dept ? dept.label : id
          } else if (scopeType === '3') {
            const user = this.userList.find(u => u.userId === id)
            name = user ? (user.nickName || user.userName) : id
          }
          this.form.scopeList.push({ scopeType, scopeTargetId: id, scopeTargetName: name })
        })
      }
    },
    findDept(options, id) {
      for (const opt of options || []) {
        if (opt.id === id) return opt
        if (opt.children) {
          const found = this.findDept(opt.children, id)
          if (found) return found
        }
      }
      return null
    },
    removeScope(index) {
      this.form.scopeList.splice(index, 1)
    },
    scopeTypeLabel(type) {
      return { '1': '角色', '2': '部门', '3': '人员' }[type] || type
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.componentId)
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.resetForm("queryForm")
      this.handleQuery()
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = {
        componentId: undefined,
        componentName: undefined,
        componentLabel: undefined,
        componentPath: undefined,
        componentDesc: undefined,
        icon: undefined,
        status: "0",
        scopeList: []
      }
      this.roleScope = []
      this.deptScope = []
      this.userScope = []
      this.resetForm("form")
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "新增组件"
    },
    handleUpdate(row) {
      this.reset()
      const componentId = row.componentId || this.ids
      getComponent(componentId).then(response => {
        this.form = response.data
        if (!this.form.scopeList) this.form.scopeList = []
        this.roleScope = this.form.scopeList.filter(s => s.scopeType === '1').map(s => s.scopeTargetId)
        this.deptScope = this.form.scopeList.filter(s => s.scopeType === '2').map(s => s.scopeTargetId)
        this.userScope = this.form.scopeList.filter(s => s.scopeType === '3').map(s => s.scopeTargetId)
        this.open = true
        this.title = "修改组件"
      })
    },
    handleStatusChange(row) {
      const text = row.status === "0" ? "启用" : "停用"
      this.$modal.confirm('确认要"' + text + '""' + row.componentLabel + '"组件吗？').then(() => {
        return updateComponent(row)
      }).then(() => {
        this.$modal.msgSuccess(text + "成功")
      }).catch(() => {
        row.status = row.status === "0" ? "1" : "0"
      })
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.componentId != undefined) {
            updateComponent(this.form).then(() => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addComponent(this.form).then(() => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    handleDelete(row) {
      const componentIds = row.componentId || this.ids
      this.$modal.confirm('是否确认删除组件编号为"' + componentIds + '"的数据项？').then(() => {
        return delComponent(componentIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    }
  }
}
</script>

<style scoped lang="scss">
.scope-tip {
  font-size: 12px;
  color: #909399;
  margin-bottom: 8px;
}
.scope-tags {
  margin-top: 8px;
}
</style>
