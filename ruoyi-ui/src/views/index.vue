<template>
  <div class="app-container workbench-my">
    <el-card class="workbench-card" shadow="never" v-loading="loading">
      <div slot="header" class="workbench-header">
        <div class="header-left">
          <span class="title"><i class="el-icon-s-home"></i> {{ currentName || '工作台' }}</span>
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
          <el-button size="mini" icon="el-icon-s-tools" @click="goMyWorkbench">管理工作台</el-button>
        </div>
      </div>

      <workbench-renderer v-if="currentLayout && currentLayout.length > 0" :layout="currentLayout" />
      <div v-else class="empty-workbench">
        <i class="el-icon-s-platform"></i>
        <div>暂无工作台布局</div>
        <el-button type="primary" size="small" icon="el-icon-plus" style="margin-top: 12px;" @click="goMyWorkbench">去配置我的工作台</el-button>
      </div>
    </el-card>
  </div>
</template>

<script>
import WorkbenchRenderer from './bpm/workbench/components/WorkbenchRenderer'
import { listUserConfig, getDefaultUserConfig } from '@/api/bpm/workbench'
import { getDefaultTemplate } from '@/api/bpm/workbench'

export default {
  name: "Index",
  components: { WorkbenchRenderer },
  data() {
    return {
      loading: true,
      configList: [],
      currentConfig: null,
      currentLayout: [],
      currentName: ''
    }
  },
  created() {
    this.loadWorkbench()
  },
  methods: {
    loadWorkbench() {
      this.loading = true
      listUserConfig({ pageSize: 100 }).then(response => {
        this.configList = response.rows || []
        const defaultConfig = this.configList.find(c => c.isDefault === '1')
        if (defaultConfig) {
          this.switchConfig(defaultConfig)
          this.loading = false
        } else if (this.configList.length > 0) {
          this.switchConfig(this.configList[0])
          this.loading = false
        } else {
          this.loadDefaultTemplate()
        }
      }).catch(() => {
        this.loadDefaultTemplate()
      })
    },
    loadDefaultTemplate() {
      getDefaultTemplate().then(response => {
        if (response.data) {
          this.currentName = response.data.templateName
          try {
            this.currentLayout = response.data.configJson ? JSON.parse(response.data.configJson) : []
          } catch (e) {
            this.currentLayout = []
          }
        }
      }).finally(() => {
        this.loading = false
      })
    },
    switchConfig(item) {
      if (!item) return
      this.currentConfig = item
      this.currentName = item.configName
      try {
        this.currentLayout = item.configJson ? JSON.parse(item.configJson) : []
      } catch (e) {
        this.currentLayout = []
      }
    },
    goMyWorkbench() {
      this.$router.push('/workbench/myWorkbench')
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
      font-size: 14px;
      font-weight: normal;
      color: #909399;
      i {
        color: #c0c4cc;
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
}
</style>
