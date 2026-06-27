<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="模板名称" prop="templateName">
        <el-input v-model="queryParams.templateName" placeholder="请输入模板名称" clearable @keyup.enter.native="handleQuery" />
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
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['bpm:workbench:template:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['bpm:workbench:template:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['bpm:workbench:template:remove']">删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="templateList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="模板ID" align="center" prop="templateId" width="80" />
      <el-table-column label="模板名称" align="center" prop="templateName" />
      <el-table-column label="编码" align="center" prop="templateCode" />
      <el-table-column label="默认模板" align="center" prop="isDefault" width="90">
        <template slot-scope="scope">
          <el-tag size="mini" :type="scope.row.isDefault === '1' ? 'success' : 'info'">{{ scope.row.isDefault === '1' ? '是' : '否' }}</el-tag>
        </template>
      </el-table-column>
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
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['bpm:workbench:template:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['bpm:workbench:template:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="1100px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="模板名称" prop="templateName">
              <el-input v-model="form.templateName" placeholder="请输入模板名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="模板编码" prop="templateCode">
              <el-input v-model="form.templateCode" placeholder="请输入模板编码" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="默认模板" prop="isDefault">
              <el-radio-group v-model="form.isDefault">
                <el-radio label="1">是</el-radio>
                <el-radio label="0">否</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-radio-group v-model="form.status">
                <el-radio label="0">正常</el-radio>
                <el-radio label="1">停用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="适用范围" prop="scopeList">
          <div class="scope-tip">不配置则全员可用；多个模板都适用时，取默认模板</div>
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

        <el-form-item label="模板布局" prop="configJson">
          <div class="layout-canvas">
            <div class="component-palette">
              <div class="palette-title">可用组件</div>
              <draggable
                :list="componentList"
                :group="{ name: 'layout', pull: 'clone', put: false }"
                :clone="cloneComponent"
                :sort="false"
                class="palette-list"
                draggable=".palette-item"
              >
                <div v-for="comp in componentList" :key="comp.componentId" class="palette-item">
                  <i :class="comp.componentIcon || 'el-icon-s-order'"></i>
                  <span>{{ comp.componentLabel }}</span>
                </div>
              </draggable>
            </div>
            <div class="canvas-area">
              <div class="canvas-title">
                画布区域
                <span v-if="layoutList.length === 0" class="empty-tip">拖拽左侧组件到此处</span>
              </div>
              <draggable
                v-model="layoutList"
                group="layout"
                class="canvas-grid"
                ghost-class="layout-ghost"
                drag-class="layout-drag"
                animation="200"
              >
                <div v-for="(item, index) in layoutList" :key="index" class="layout-card" :class="'span-' + item.span">
                  <div class="card-toolbar">
                    <el-input v-model="item.title" size="mini" placeholder="标题" class="title-input" />
                    <el-select v-model="item.span" size="mini" style="width: 80px;">
                      <el-option label="1/4" :value="6" />
                      <el-option label="1/3" :value="8" />
                      <el-option label="1/2" :value="12" />
                      <el-option label="2/3" :value="16" />
                      <el-option label="3/4" :value="18" />
                      <el-option label="1/1" :value="24" />
                    </el-select>
                    <el-input v-model="item.icon" size="mini" placeholder="图标" style="width: 90px;" />
                    <i class="el-icon-delete delete-btn" @click="removeLayoutItem(index)"></i>
                  </div>
                  <div class="card-preview">
                    <i :class="item.icon || 'el-icon-s-order'"></i>
                    <span>{{ item.component }}</span>
                  </div>
                </div>
              </draggable>
            </div>
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
import { listTemplate, getTemplate, addTemplate, updateTemplate, delTemplate } from "@/api/bpm/workbench"
import { listComponent } from "@/api/bpm/workbench"
import { listRole } from "@/api/system/role"
import { listDept } from "@/api/system/dept"
import { listUser } from "@/api/system/user"
import Treeselect from "@riophae/vue-treeselect"
import "@riophae/vue-treeselect/dist/vue-treeselect.css"
import draggable from "vuedraggable"

export default {
  name: "BpmWorkbenchTemplate",
  components: { Treeselect, draggable },
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      templateList: [],
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
      layoutList: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        templateName: undefined,
        status: undefined
      },
      form: {
        templateName: undefined,
        templateCode: undefined,
        isDefault: "0",
        status: "0",
        scopeList: [],
        configJson: undefined
      },
      rules: {
        templateName: [{ required: true, message: "模板名称不能为空", trigger: "blur" }]
      }
    }
  },
  created() {
    this.getList()
    this.loadComponentList()
    this.loadRoleList()
    this.loadDeptList()
  },
  methods: {
    getList() {
      this.loading = true
      listTemplate(this.queryParams).then(response => {
        this.templateList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    loadComponentList() {
      listComponent({ pageSize: 1000, status: '0' }).then(response => {
        this.componentList = response.rows || []
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
    cloneComponent(comp) {
      return {
        component: comp.componentName,
        title: comp.componentLabel,
        icon: comp.componentIcon || 'el-icon-s-order',
        span: 12
      }
    },
    removeLayoutItem(index) {
      this.layoutList.splice(index, 1)
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.templateId)
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
        templateId: undefined,
        templateName: undefined,
        templateCode: undefined,
        isDefault: "0",
        status: "0",
        scopeList: [],
        configJson: undefined
      }
      this.layoutList = []
      this.roleScope = []
      this.deptScope = []
      this.userScope = []
      this.resetForm("form")
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "新增模板"
    },
    handleUpdate(row) {
      this.reset()
      const templateId = row.templateId || this.ids
      getTemplate(templateId).then(response => {
        this.form = response.data
        if (!this.form.scopeList) this.form.scopeList = []
        this.roleScope = this.form.scopeList.filter(s => s.scopeType === '1').map(s => s.scopeTargetId)
        this.deptScope = this.form.scopeList.filter(s => s.scopeType === '2').map(s => s.scopeTargetId)
        this.userScope = this.form.scopeList.filter(s => s.scopeType === '3').map(s => s.scopeTargetId)
        try {
          this.layoutList = this.form.configJson ? JSON.parse(this.form.configJson) : []
        } catch (e) {
          this.layoutList = []
        }
        this.open = true
        this.title = "修改模板"
      })
    },
    handleStatusChange(row) {
      const text = row.status === "0" ? "启用" : "停用"
      this.$modal.confirm('确认要"' + text + '""' + row.templateName + '"模板吗？').then(() => {
        return updateTemplate(row)
      }).then(() => {
        this.$modal.msgSuccess(text + "成功")
      }).catch(() => {
        row.status = row.status === "0" ? "1" : "0"
      })
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.configJson = JSON.stringify(this.layoutList)
          if (this.form.templateId != undefined) {
            updateTemplate(this.form).then(() => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addTemplate(this.form).then(() => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    handleDelete(row) {
      const templateIds = row.templateId || this.ids
      this.$modal.confirm('是否确认删除模板编号为"' + templateIds + '"的数据项？').then(() => {
        return delTemplate(templateIds)
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
.layout-canvas {
  display: flex;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  min-height: 360px;
  .component-palette {
    width: 180px;
    border-right: 1px solid #e4e7ed;
    background: #f5f7fa;
    .palette-title {
      padding: 12px;
      font-weight: 600;
      color: #303133;
      border-bottom: 1px solid #e4e7ed;
    }
    .palette-list {
      padding: 8px;
      min-height: 300px;
    }
    .palette-item {
      padding: 10px 12px;
      margin-bottom: 8px;
      background: #fff;
      border: 1px solid #dcdfe6;
      border-radius: 4px;
      cursor: move;
      display: flex;
      align-items: center;
      i {
        margin-right: 8px;
        color: #409eff;
      }
      &:hover {
        border-color: #409eff;
        color: #409eff;
      }
    }
  }
  .canvas-area {
    flex: 1;
    padding: 12px;
    .canvas-title {
      font-weight: 600;
      color: #303133;
      margin-bottom: 12px;
      .empty-tip {
        font-weight: normal;
        color: #c0c4cc;
        font-size: 12px;
        margin-left: 8px;
      }
    }
    .canvas-grid {
      display: flex;
      flex-wrap: wrap;
      min-height: 280px;
      background: #fafafa;
      border: 1px dashed #dcdfe6;
      border-radius: 4px;
      padding: 8px;
      &.sortable-ghost {
        background: #e6f7ff;
      }
      .layout-ghost {
        opacity: 0.5;
        background: #e6f7ff;
        border: 1px dashed #409eff;
      }
      .layout-drag {
        opacity: 0.9;
      }
      .layout-card {
        padding: 8px;
        box-sizing: border-box;
        &.span-6 { width: 25%; }
        &.span-8 { width: 33.333%; }
        &.span-12 { width: 50%; }
        &.span-16 { width: 66.666%; }
        &.span-18 { width: 75%; }
        &.span-24 { width: 100%; }
        .card-toolbar {
          background: #fff;
          border: 1px solid #e4e7ed;
          border-bottom: none;
          border-radius: 4px 4px 0 0;
          padding: 8px;
          display: flex;
          align-items: center;
          gap: 6px;
          .title-input {
            flex: 1;
          }
          .delete-btn {
            color: #f56c6c;
            cursor: pointer;
            font-size: 14px;
            padding: 4px;
            &:hover {
              color: #ff4d4f;
            }
          }
        }
        .card-preview {
          background: #fff;
          border: 1px solid #e4e7ed;
          border-radius: 0 0 4px 4px;
          height: 80px;
          display: flex;
          flex-direction: column;
          align-items: center;
          justify-content: center;
          color: #909399;
          i {
            font-size: 28px;
            margin-bottom: 6px;
            color: #c0c4cc;
          }
        }
      }
    }
  }
}
</style>
