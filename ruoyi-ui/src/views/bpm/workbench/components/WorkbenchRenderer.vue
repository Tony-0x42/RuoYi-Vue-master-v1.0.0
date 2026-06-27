<template>
  <div class="workbench-renderer">
    <el-row :gutter="16">
      <el-col
        v-for="(item, index) in layout"
        :key="index"
        :xs="24"
        :sm="item.span >= 12 ? 24 : 12"
        :lg="item.span || 8"
        class="workbench-col"
      >
        <el-card class="workbench-card" shadow="never">
          <div slot="header" class="card-header">
            <span><i :class="item.icon || 'el-icon-s-order'"></i> {{ item.title }}</span>
          </div>
          <component :is="resolveComponent(item.component)" v-if="item.component" />
          <div v-else class="component-missing">
            <i class="el-icon-warning-outline"></i>
            <div>未配置组件</div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import WelcomeCard from './WelcomeCard'
import TaskTodo from './TaskTodo'
import TaskDone from './TaskDone'
import NoticeList from './NoticeList'

export default {
  name: 'WorkbenchRenderer',
  props: {
    layout: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      // 内置组件映射：新增组件需在此注册并 import
      componentMap: {
        WelcomeCard,
        TaskTodo,
        TaskDone,
        NoticeList
      }
    }
  },
  methods: {
    resolveComponent(componentName) {
      if (!componentName) return null
      const comp = this.componentMap[componentName]
      if (!comp) {
        return {
          render: h => h('div', { style: { padding: '20px', textAlign: 'center', color: '#c0c4cc' } }, [
            h('i', { class: 'el-icon-warning-outline' }),
            `未找到组件 "${componentName}"`
          ])
        }
      }
      return comp
    }
  }
}
</script>

<style scoped lang="scss">
.workbench-renderer {
  .workbench-col {
    margin-bottom: 16px;
  }
  .workbench-card {
    border-radius: 8px;
    height: 100%;
    ::v-deep .el-card__header {
      padding: 14px 18px;
      border-bottom: 1px solid #f0f0f0;
      font-weight: 600;
      color: #303133;
    }
    ::v-deep .el-card__body {
      padding: 16px;
    }
  }
  .card-header i {
    color: #409eff;
    margin-right: 6px;
  }
  .component-missing {
    text-align: center;
    padding: 40px 0;
    color: #c0c4cc;
    i {
      font-size: 32px;
      display: block;
      margin-bottom: 8px;
    }
  }
}
</style>
