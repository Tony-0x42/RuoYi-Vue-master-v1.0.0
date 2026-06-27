<template>
  <div class="app-container">
    <el-alert
      :title="'当前流程定义：' + (definitionName || '-')"
      type="info"
      :closable="false"
      style="margin-bottom: 10px;"
    />
    <el-row class="designer-toolbar">
      <el-button type="primary" icon="el-icon-check" size="mini" @click="handleSave">保存模型</el-button>
      <el-button type="success" icon="el-icon-upload2" size="mini" @click="handleDeploy">发布流程</el-button>
      <el-button type="warning" icon="el-icon-refresh" size="mini" @click="handleReset">重置</el-button>
      <el-button icon="el-icon-zoom-in" size="mini" @click="handleZoomIn">放大</el-button>
      <el-button icon="el-icon-zoom-out" size="mini" @click="handleZoomOut">缩小</el-button>
      <el-button icon="el-icon-full-screen" size="mini" @click="handleFit">适应屏幕</el-button>
    </el-row>
    <div class="designer-container">
      <div ref="canvas" class="designer-canvas" />
      <div class="designer-properties">
        <h4>属性面板</h4>
        <div v-if="selectedElement">
          <el-form label-width="80px" size="mini">
            <el-form-item label="节点类型">
              <span>{{ selectedElement.type }}</span>
            </el-form-item>
            <el-form-item label="名称">
              <el-input v-model="elementName" @change="updateName" />
            </el-form-item>
            <template v-if="isUserTask">
              <el-form-item label="办理人">
                <UserSelect v-model="elementAssignee" @change="updateProperties" />
              </el-form-item>
              <el-form-item label="候选用户">
                <UserMultiSelect v-model="elementCandidateUsers" @change="updateProperties" />
              </el-form-item>
              <el-form-item label="候选角色">
                <RoleMultiSelect v-model="elementCandidateGroups" @change="updateProperties" />
              </el-form-item>
              <el-form-item label="候选部门">
                <DeptTreeSelect v-model="elementCandidateDepts" @change="updateProperties" />
              </el-form-item>
              <el-form-item label="会审">
                <el-checkbox v-model="elementMultiInstance" @change="updateMultiInstance">启用多实例会审</el-checkbox>
              </el-form-item>
              <el-form-item v-if="elementMultiInstance" label="会审变量">
                <el-input v-model="elementCollection" placeholder="集合变量名，如：assigneeList" @change="updateMultiInstance" />
              </el-form-item>
            </template>
            <template v-if="isSequenceFlow">
              <el-form-item label="条件表达式">
                <el-input v-model="elementCondition" placeholder="如：${leaveDays > 3}" @change="updateCondition" />
              </el-form-item>
            </template>
          </el-form>
        </div>
        <div v-else class="no-selection">
          请在画布中选择一个元素
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import BpmnModeler from 'bpmn-js/lib/Modeler'
import 'bpmn-js/dist/assets/diagram-js.css'
import 'bpmn-js/dist/assets/bpmn-font/css/bpmn.css'
import flowableModdle from 'flowable-bpmn-moddle/resources/camunda.json'
import { getDefinition, getModelXml, saveModel, deployDefinition } from '@/api/bpm/definition'
import UserSelect from '@/components/UserSelect'
import UserMultiSelect from '@/components/UserMultiSelect'
import RoleMultiSelect from '@/components/RoleMultiSelect'
import DeptTreeSelect from '@/components/DeptTreeSelect'

const EMPTY_BPMN = `<?xml version="1.0" encoding="UTF-8"?>
<bpmn2:definitions xmlns:bpmn2="http://www.omg.org/spec/BPMN/20100524/MODEL" xmlns:bpmndi="http://www.omg.org/spec/BPMN/20100524/DI" xmlns:omgdi="http://www.omg.org/spec/DD/20100524/DI" xmlns:omgdc="http://www.omg.org/spec/DD/20100524/DC" id="Definitions_1" targetNamespace="http://bpmn.io/schema/bpmn">
  <bpmn2:process id="Process_1" isExecutable="true">
    <bpmn2:startEvent id="StartEvent_1" />
  </bpmn2:process>
  <bpmndi:BPMNDiagram id="BPMNDiagram_1">
    <bpmndi:BPMNPlane id="BPMNPlane_1" bpmnElement="Process_1">
      <bpmndi:BPMNShape id="_BPMNShape_StartEvent_1" bpmnElement="StartEvent_1">
        <omgdc:Bounds x="152" y="152" width="36" height="36" />
      </bpmndi:BPMNShape>
    </bpmndi:BPMNPlane>
  </bpmndi:BPMNDiagram>
</bpmn2:definitions>`

export default {
  name: 'BpmDesigner',
  components: { UserSelect, UserMultiSelect, RoleMultiSelect, DeptTreeSelect },
  data() {
    return {
      definitionId: undefined,
      definitionName: '',
      modeler: null,
      selectedElement: null,
      elementName: '',
      elementAssignee: '',
      elementCandidateUsers: '',
      elementCandidateGroups: '',
      elementCandidateDepts: '',
      elementMultiInstance: false,
      elementCollection: '',
      elementCondition: ''
    }
  },
  computed: {
    isUserTask() {
      return this.selectedElement && this.selectedElement.type === 'bpmn:UserTask'
    },
    isSequenceFlow() {
      return this.selectedElement && this.selectedElement.type === 'bpmn:SequenceFlow'
    }
  },
  created() {
    this.definitionId = this.$route.query.definitionId
  },
  mounted() {
    this.initModeler()
    this.loadData()
  },
  beforeDestroy() {
    if (this.modeler) {
      this.modeler.destroy()
    }
  },
  methods: {
    initModeler() {
      this.modeler = new BpmnModeler({
        container: this.$refs.canvas,
        moddleExtensions: {
          flowable: flowableModdle
        }
      })

      this.modeler.on('selection.changed', (e) => {
        const selection = e.newSelection
        this.selectedElement = selection.length ? selection[0] : null
        this.loadElementProperties()
      })

      this.modeler.on('element.changed', (e) => {
        if (this.selectedElement && e.element.id === this.selectedElement.id) {
          this.loadElementProperties()
        }
      })
    },
    loadData() {
      if (!this.definitionId) {
        this.$modal.msgError('缺少流程定义ID')
        return
      }
      getDefinition(this.definitionId).then(response => {
        this.definitionName = response.data.definitionName
      })
      getModelXml(this.definitionId).then(response => {
        const xml = response.data
        if (xml && xml.trim()) {
          this.importXml(xml)
        } else {
          this.createNewDiagram()
        }
      }).catch(() => {
        this.createNewDiagram()
      })
    },
    importXml(xml) {
      this.modeler.importXML(xml).then(() => {
        this.ensureExecutable()
        this.modeler.get('canvas').zoom('fit-viewport')
      }).catch(err => {
        this.$modal.msgError('导入模型失败，已创建新画布：' + err.message)
        this.createNewDiagram()
      })
    },
    createNewDiagram() {
      this.modeler.createDiagram().then(() => {
        this.ensureExecutable()
        this.modeler.get('canvas').zoom('fit-viewport')
      }).catch(err => {
        this.$modal.msgError('创建画布失败：' + err.message)
      })
    },
    ensureExecutable() {
      const canvas = this.modeler.get('canvas')
      const root = canvas.getRootElement()
      if (root && root.businessObject && root.businessObject.isExecutable !== true) {
        const modeling = this.modeler.get('modeling')
        modeling.updateProperties(root, { isExecutable: true })
      }
    },
    loadElementProperties() {
      if (!this.selectedElement) {
        this.elementName = ''
        this.elementAssignee = ''
        this.elementCandidateUsers = ''
        this.elementCandidateGroups = ''
        this.elementCandidateDepts = ''
        this.elementMultiInstance = false
        this.elementCollection = ''
        this.elementCondition = ''
        return
      }
      const businessObject = this.selectedElement.businessObject
      this.elementName = businessObject.name || ''

      if (this.isUserTask) {
        this.elementAssignee = businessObject.get('flowable:assignee') || ''
        this.elementCandidateUsers = businessObject.get('flowable:candidateUsers') || ''
        this.elementCandidateGroups = businessObject.get('flowable:candidateGroups') || ''
        const extensionElements = businessObject.get('extensionElements')
        this.elementCandidateDepts = this.getExtensionValue(extensionElements, 'candidateDepts') || ''
        const loop = businessObject.get('loopCharacteristics')
        this.elementMultiInstance = !!loop
        this.elementCollection = loop ? (loop.get('collection') || '') : ''
      }

      if (this.isSequenceFlow) {
        const condition = businessObject.get('conditionExpression')
        this.elementCondition = condition ? (condition.get('body') || '') : ''
      }
    },
    updateName() {
      if (!this.selectedElement) return
      const modeling = this.modeler.get('modeling')
      modeling.updateProperties(this.selectedElement, { name: this.elementName })
    },
    updateProperties() {
      if (!this.isUserTask) return
      const props = {}
      props['flowable:assignee'] = this.elementAssignee || undefined
      props['flowable:candidateUsers'] = this.elementCandidateUsers || undefined
      props['flowable:candidateGroups'] = this.elementCandidateGroups || undefined

      // Candidate departments are stored in extensionElements because Flowable has no standard attribute
      this.setExtensionValue(this.selectedElement.businessObject, 'candidateDepts', this.elementCandidateDepts)

      const modeling = this.modeler.get('modeling')
      modeling.updateProperties(this.selectedElement, props)
    },
    updateMultiInstance() {
      if (!this.isUserTask) return
      const bpmnFactory = this.modeler.get('bpmnFactory')
      const modeling = this.modeler.get('modeling')
      if (this.elementMultiInstance) {
        const loopCharacteristics = bpmnFactory.create('bpmn:MultiInstanceLoopCharacteristics')
        if (this.elementCollection) {
          loopCharacteristics.set('collection', this.elementCollection)
          loopCharacteristics.set('elementVariable', 'assignee')
        }
        modeling.updateProperties(this.selectedElement, { loopCharacteristics })
      } else {
        modeling.updateProperties(this.selectedElement, { loopCharacteristics: undefined })
      }
    },
    updateCondition() {
      if (!this.isSequenceFlow) return
      const bpmnFactory = this.modeler.get('bpmnFactory')
      const modeling = this.modeler.get('modeling')
      if (this.elementCondition) {
        const condition = bpmnFactory.create('bpmn:FormalExpression', { body: this.elementCondition })
        modeling.updateProperties(this.selectedElement, { conditionExpression: condition })
      } else {
        modeling.updateProperties(this.selectedElement, { conditionExpression: undefined })
      }
    },
    getExtensionValue(extensionElements, name) {
      if (!extensionElements || !extensionElements.values) return ''
      const elem = extensionElements.values.find(v => v.$type === 'flowable:' + name)
      return elem ? (elem.value || '') : ''
    },
    setExtensionValue(businessObject, name, value) {
      const bpmnFactory = this.modeler.get('bpmnFactory')
      const moddle = this.modeler.get('moddle')
      let extensionElements = businessObject.get('extensionElements')
      if (!extensionElements) {
        extensionElements = bpmnFactory.create('bpmn:ExtensionElements')
        businessObject.set('extensionElements', extensionElements)
      }
      let values = extensionElements.get('values') || []
      values = values.filter(v => v.$type !== 'flowable:' + name)
      if (value) {
        const newElem = moddle.create('flowable:' + name, { value })
        values.push(newElem)
      }
      extensionElements.set('values', values)
    },
    handleSave() {
      this.modeler.saveXML({ format: true }).then(result => {
        saveModel(this.definitionId, result.xml).then(() => {
          this.$modal.msgSuccess('保存成功')
        })
      }).catch(err => {
        this.$modal.msgError('保存失败：' + err.message)
      })
    },
    handleDeploy() {
      this.$modal.confirm('是否确认发布该流程？').then(() => {
        return deployDefinition(this.definitionId)
      }).then(() => {
        this.$modal.msgSuccess('发布成功')
      }).catch(() => {})
    },
    handleReset() {
      this.loadData()
    },
    handleZoomIn() {
      this.modeler.get('zoomScroll').stepZoom(1)
    },
    handleZoomOut() {
      this.modeler.get('zoomScroll').stepZoom(-1)
    },
    handleFit() {
      this.modeler.get('canvas').zoom('fit-viewport')
    }
  }
}
</script>

<style scoped>
.designer-toolbar {
  margin-bottom: 10px;
}
.designer-container {
  display: flex;
  height: calc(100vh - 220px);
  border: 1px solid #dcdfe6;
}
.designer-canvas {
  flex: 1;
  background: #f5f7fa;
}
.designer-canvas ::v-deep .djs-palette {
  width: 92px !important;
  background: #fafafa;
  border-right: 1px solid #dcdfe6;
}
.designer-canvas ::v-deep .djs-palette .djs-palette-entries {
  display: flex;
  flex-direction: column;
}
.designer-canvas ::v-deep .djs-palette .entry {
  width: auto !important;
  min-width: 80px;
  height: 32px;
  display: flex;
  align-items: center;
  padding: 0 6px;
  margin: 2px 4px;
  border-radius: 3px;
}
.designer-canvas ::v-deep .djs-palette .entry:hover {
  background: #e4e7ed;
}
.designer-canvas ::v-deep .djs-palette .entry::after {
  margin-left: 6px;
  font-size: 12px;
  color: #303133;
  white-space: nowrap;
}
.designer-canvas ::v-deep .djs-palette .entry[data-action="hand-tool"]::after { content: '抓手'; }
.designer-canvas ::v-deep .djs-palette .entry[data-action="lasso-tool"]::after { content: '套索'; }
.designer-canvas ::v-deep .djs-palette .entry[data-action="space-tool"]::after { content: '空间'; }
.designer-canvas ::v-deep .djs-palette .entry[data-action="global-connect-tool"]::after { content: '连线'; }
.designer-canvas ::v-deep .djs-palette .entry[data-action="create.start-event"]::after { content: '开始'; }
.designer-canvas ::v-deep .djs-palette .entry[data-action="create.intermediate-event"]::after { content: '中间'; }
.designer-canvas ::v-deep .djs-palette .entry[data-action="create.end-event"]::after { content: '结束'; }
.designer-canvas ::v-deep .djs-palette .entry[data-action="create.exclusive-gateway"]::after { content: '网关'; }
.designer-canvas ::v-deep .djs-palette .entry[data-action="create.task"]::after { content: '任务'; }
.designer-canvas ::v-deep .djs-palette .entry[data-action="create.subprocess-expanded"]::after { content: '子流程'; }
.designer-canvas ::v-deep .djs-palette .entry[data-action="create.data-object"]::after { content: '数据对象'; }
.designer-canvas ::v-deep .djs-palette .entry[data-action="create.data-store"]::after { content: '数据存储'; }
.designer-canvas ::v-deep .djs-palette .entry[data-action="create.participant-expanded"]::after { content: '泳道'; }
.designer-canvas ::v-deep .djs-palette .entry[data-action="create.group"]::after { content: '分组'; }
.designer-canvas ::v-deep .djs-palette .separator {
  width: 80%;
  margin: 6px auto;
}
.designer-properties {
  width: 300px;
  padding: 15px;
  border-left: 1px solid #dcdfe6;
  background: #fff;
  overflow-y: auto;
}
.designer-properties h4 {
  margin-top: 0;
  margin-bottom: 15px;
}
.no-selection {
  color: #909399;
  text-align: center;
  padding: 30px 0;
}
</style>
