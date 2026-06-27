<template>
  <div class="app-container workbench-my">
    <el-card class="workbench-card" shadow="never">
      <div slot="header" class="workbench-header">
        <div class="header-left">
          <span class="title"><i class="el-icon-s-home"></i> {{ currentName || '我的工作台' }}</span>
          <el-dropdown v-if="configList.length > 0" @command="switchConfig" style="margin-left: 12px;">
            <el-button size="mini" type="primary" plain>切换工作台<i class="el-icon-arrow-down el-icon--right"></i></el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item v-for="item in configList" :key="item.configId" :command="item">
                <i v-if="item.isDefault === '1'" class="el-icon-star-on" style="color: #f7ba2a;"></i>
                {{ item.configName }}
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
        <div class="header-right">
          <el-button size="mini" icon="el-icon-plus" @click="handleAdd">新建</el-button>
          <el-button size="mini" icon="el-icon-edit" @click="handleEdit">编辑</el-button>
          <el-button size="mini" icon="el-icon-star-off" @click="handleSetDefault">设为默认</el-button>
          <el-button size="mini" icon="el-icon-delete" @click="handleDelete">删除</el-button>
        </div>
      </div>

      <workbench-renderer v-if="currentLayout && currentLayout.length > 0" :layout="currentLayout" />
      <div v-else class="empty-workbench">
        <i class="el-icon-s-platform"></i>
        <div>暂无工作台布局</div>
        <el-button type="primary" size="small" icon="el-icon-plus" style="margin-top: 12px;" @click="handleAdd">创建我的工作台</el-button>
      </div>
    </el-card>

    <el-dialog :title="dialogTitle" :visible.sync="dialogOpen" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="工作台名称" prop="configName">
          <el-input v-model="form.configName" placeholder="请输入工作台名称" />
        </el-form-item>
        <el-form-item label="基于模板" prop="templateId">
          <el-select v-model="form.templateId" placeholder="选择模板（可选）" clearable style="width: 100%" @change="onTemplateChange">
            <el-option v-for="t in availableTemplates" :key="t.templateId" :label="t.templateName" :value="t.templateId" />
          </el-select>
        </el-form-item>
        <el-form-item label="设为默认" prop="isDefault">
          <el-radio-group v-model="form.isDefault">
            <el-radio label="1">是</el-radio>
            <el-radio label="0">否</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="布局配置" prop="configJson">
          <div class="layout-toolbar">
            <el-select v-model="selectedComponent" placeholder="选择组件" size="small" style="width: 200px; margin-right: 8px;">
              <el-option v-for="comp in availableComponents" :key="comp.componentId" :label="comp.componentLabel" :value="comp.componentName" />
            </el-select>
            <el-input v-model="componentTitle" placeholder="卡片标题" size="small" style="width: 150px; margin-right: 8px;" />
            <el-input v-model="componentIcon" placeholder="图标" size="small" style="width: 120px; margin-right: 8px;" />
            <el-select v-model="componentSpan" placeholder="宽度" size="small" style="width: 100px; margin-right: 8px;">
              <el-option label="1/3" :value="8" />
              <el-option label="1/2" :value="12" />
              <el-option label="2/3" :value="16" />
              <el-option label="1/1" :value="24" />
            </el-select>
            <el-button type="primary" size="small" icon="el-icon-plus" @click="addLayoutItem">添加</el-button>
          </div>
          <el-table :data="layoutList" size="small" border style="margin-top: 10px;">
            <el-table-column label="排序" width="60" align="center">
              <template slot-scope="scope">
                <el-button size="mini" type="text" icon="el-icon-top" @click="moveUp(scope.$index)"></el-button>
                <el-button size="mini" type="text" icon="el-icon-bottom" @click="moveDown(scope.$index)"></el-button>
              </template>
            </el-table-column>
            <el-table-column label="组件" prop="component" align="center" />
            <el-table-column label="标题" prop="title" align="center" />
            <el-table-column label="图标" prop="icon" align="center" />
            <el-table-column label="宽度" prop="span" align="center" />
            <el-table-column label="操作" width="80" align="center">
              <template slot-scope="scope">
                <el-button size="mini" type="text" icon="el-icon-delete" @click="removeLayoutItem(scope.$index)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
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
import WorkbenchRenderer from '../components/WorkbenchRenderer'
import { listUserConfig, getUserConfig, addUserConfig, updateUserConfig, delUserConfig, setDefaultUserConfig } from "@/api/bpm/workbench"
import { listAvailableComponents, listAvailableTemplates, getTemplate } from "@/api/bpm/workbench"

export default {
  name: "BpmWorkbenchMy",
  components: { WorkbenchRenderer },
  data() {
    return {
      loading: true,
      configList: [],
      currentConfig: null,
      currentLayout: [],
      currentName: '',
      availableComponents: [],
      availableTemplates: [],
      dialogOpen: false,
      dialogTitle: '',
      selectedComponent: undefined,
      componentTitle: '',
      componentIcon: 'el-icon-s-order',
      componentSpan: 12,
      layoutList: [],
      form: {
        configName: undefined,
        templateId: undefined,
        isDefault: '0',
        configJson: undefined
      },
      rules: {
        configName: [{ required: true, message: "工作台名称不能为空", trigger: "blur" }]
      }
    }
  },
  created() {
    this.loadAvailable()
    this.loadList()
  },
  methods: {
    loadList() {
      this.loading = true
      listUserConfig({ pageSize: 100 }).then(response => {
        this.configList = response.rows || []
        const defaultConfig = this.configList.find(c => c.isDefault === '1')
        const firstConfig = this.configList[0]
        this.switchConfig(defaultConfig || firstConfig)
      }).finally(() => {
        this.loading = false
      })
    },
    loadAvailable() {
      listAvailableComponents().then(response => {
        this.availableComponents = response.data || []
      })
      listAvailableTemplates().then(response => {
        this.availableTemplates = response.data || []
      })
    },
    switchConfig(item) {
      if (!item) {
        this.currentConfig = null
        this.currentLayout = []
        this.currentName = ''
        return
      }
      this.currentConfig = item
      this.currentName = item.configName
      try {
        this.currentLayout = item.configJson ? JSON.parse(item.configJson) : []
      } catch (e) {
        this.currentLayout = []
      }
    },
    onTemplateChange(templateId) {
      if (!templateId) return
      getTemplate(templateId).then(response => {
        try {
          this.layoutList = response.data.configJson ? JSON.parse(response.data.configJson) : []
        } catch (e) {
          this.layoutList = []
        }
      })
    },
    addLayoutItem() {
      if (!this.selectedComponent) {
        this.$modal.msgWarning("请选择组件")
        return
      }
      const comp = this.availableComponents.find(c => c.componentName === this.selectedComponent)
      this.layoutList.push({
        component: this.selectedComponent,
        title: this.componentTitle || comp.componentLabel,
        icon: this.componentIcon,
        span: this.componentSpan
      })
    },
    removeLayoutItem(index) {
      this.layoutList.splice(index, 1)
    },
    moveUp(index) {
      if (index === 0) return
      const temp = this.layoutList[index]
      this.$set(this.layoutList, index, this.layoutList[index - 1])
      this.$set(this.layoutList, index - 1, temp)
    },
    moveDown(index) {
      if (index === this.layoutList.length - 1) return
      const temp = this.layoutList[index]
      this.$set(this.layoutList, index, this.layoutList[index + 1])
      this.$set(this.layoutList, index + 1, temp)
    },
    handleAdd() {
      this.reset()
      this.dialogTitle = '新建工作台'
      this.dialogOpen = true
    },
    handleEdit() {
      if (!this.currentConfig) {
        this.$modal.msgWarning("请先选择一个工作台")
        return
      }
      this.reset()
      getUserConfig(this.currentConfig.configId).then(response => {
        this.form = response.data
        try {
          this.layoutList = this.form.configJson ? JSON.parse(this.form.configJson) : []
        } catch (e) {
          this.layoutList = []
        }
        this.dialogTitle = '编辑工作台'
        this.dialogOpen = true
      })
    },
    handleSetDefault() {
      if (!this.currentConfig) {
        this.$modal.msgWarning("请先选择一个工作台")
        return
      }
      setDefaultUserConfig(this.currentConfig.configId).then(() => {
        this.$modal.msgSuccess("设置成功")
        this.loadList()
      })
    },
    handleDelete() {
      if (!this.currentConfig) {
        this.$modal.msgWarning("请先选择一个工作台")
        return
      }
      this.$modal.confirm('是否确认删除工作台"' + this.currentConfig.configName + '"？').then(() => {
        return delUserConfig(this.currentConfig.configId)
      }).then(() => {
        this.$modal.msgSuccess("删除成功")
        this.loadList()
      }).catch(() => {})
    },
    cancel() {
      this.dialogOpen = false
      this.reset()
    },
    reset() {
      this.form = {
        configName: undefined,
        templateId: undefined,
        isDefault: '0',
        configJson: undefined
      }
      this.layoutList = []
      this.selectedComponent = undefined
      this.componentTitle = ''
      this.componentIcon = 'el-icon-s-order'
      this.componentSpan = 12
      this.resetForm("form")
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.configJson = JSON.stringify(this.layoutList)
          if (this.form.configId != undefined) {
            updateUserConfig(this.form).then(() => {
              this.$modal.msgSuccess("修改成功")
              this.dialogOpen = false
              this.loadList()
            })
          } else {
            addUserConfig(this.form).then(() => {
              this.$modal.msgSuccess("新增成功")
              this.dialogOpen = false
              this.loadList()
            })
          }
        }
      })
    }
  }
}
</script>

<style scoped lang="scss">
.workbench-my {
  .workbench-card {
    border-radius: 8px;
    ::v-deep .el-card__header {
      padding: 14px 18px;
      border-bottom: 1px solid #f0f0f0;
    }
  }
  .workbench-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    .title {
      font-size: 16px;
      font-weight: 600;
      color: #303133;
      i {
        color: #409eff;
        margin-right: 6px;
      }
    }
  }
  .empty-workbench {
    text-align: center;
    padding: 80px 0;
    color: #c0c4cc;
    i {
      font-size: 48px;
      display: block;
      margin-bottom: 12px;
    }
  }
  .layout-toolbar {
    display: flex;
    align-items: center;
    flex-wrap: wrap;
  }
}
</style>
