<template>
  <div class="app-container">
    <el-alert
      :title="'流程实例跟踪：' + processInstanceId"
      type="info"
      :closable="false"
      style="margin-bottom: 10px;"
    />
    <el-row class="trace-toolbar">
      <el-button icon="el-icon-back" size="mini" @click="handleBack">返回</el-button>
      <el-button icon="el-icon-refresh" size="mini" @click="loadTrace">刷新</el-button>
    </el-row>
    <div ref="canvas" class="trace-canvas" />
  </div>
</template>

<script>
import BpmnViewer from 'bpmn-js/lib/Viewer'
import { getTraceXml } from '@/api/bpm/runtime'

export default {
  name: 'BpmTrace',
  data() {
    return {
      processInstanceId: '',
      viewer: null
    }
  },
  created() {
    this.processInstanceId = this.$route.query.processInstanceId
  },
  mounted() {
    this.initViewer()
    this.loadTrace()
  },
  beforeDestroy() {
    if (this.viewer) {
      this.viewer.destroy()
    }
  },
  methods: {
    initViewer() {
      this.viewer = new BpmnViewer({
        container: this.$refs.canvas
      })
    },
    loadTrace() {
      if (!this.processInstanceId) {
        this.$modal.msgError('缺少流程实例ID')
        return
      }
      getTraceXml(this.processInstanceId).then(response => {
        const xml = response.data
        if (!xml) {
          this.$modal.msgError('未找到流程模型')
          return
        }
        this.viewer.importXML(xml).then(() => {
          this.viewer.get('canvas').zoom('fit-viewport')
          this.highlightCurrentTask()
        }).catch(err => {
          this.$modal.msgError('加载流程图失败：' + err.message)
        })
      })
    },
    highlightCurrentTask() {
      // 高亮当前活动节点（绿色）
      const canvas = this.viewer.get('canvas')
      const elementRegistry = this.viewer.get('elementRegistry')
      elementRegistry.forEach(element => {
        if (element.type === 'bpmn:UserTask' || element.type === 'bpmn:Task') {
          // 简单示例：高亮所有任务节点
          canvas.addMarker(element.id, 'highlight')
        }
      })
    },
    handleBack() {
      this.$router.go(-1)
    }
  }
}
</script>

<style scoped>
.trace-toolbar {
  margin-bottom: 10px;
}
.trace-canvas {
  height: calc(100vh - 220px);
  border: 1px solid #dcdfe6;
  background: #f5f7fa;
}
</style>

<style>
.highlight:not(.djs-connection) .djs-visual > :nth-child(1) {
  fill: #67c23a !important;
  stroke: #67c23a !important;
}
</style>
