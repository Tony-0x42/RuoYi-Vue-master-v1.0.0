<template>
  <el-dialog
    :title="dialogTitle"
    :visible="visible"
    width="600px"
    append-to-body
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <el-form v-loading="loading" label-width="100px">
      <el-form-item label="下一节点">
        <span>{{ nextNodeName || '-' }}</span>
      </el-form-item>
      <el-form-item label="候选人">
        <el-select
          v-model="selectedCandidates"
          multiple
          placeholder="请选择候选人"
          style="width: 100%"
        >
          <el-option
            v-for="user in candidateList"
            :key="user.userId"
            :label="user.nickName || user.userName"
            :value="user.userId"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="审批意见">
        <el-input
          v-model="opinion"
          type="textarea"
          :rows="3"
          placeholder="请输入审批意见"
        />
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button
        v-if="taskId"
        type="danger"
        :loading="returnLoading"
        @click="handleReturn"
      >退回上一环节</el-button>
      <el-button type="primary" :loading="submitLoading" @click="handleSubmit">提交</el-button>
      <el-button @click="handleClose">取消</el-button>
    </div>
  </el-dialog>
</template>

<script>
import {
  previewStart,
  previewNext,
  getReturnTarget,
  startProcess,
  completeTask,
  returnToPrevious
} from '@/api/bpm/preview'

export default {
  name: 'FlowSubmitDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    processKey: {
      type: String,
      default: ''
    },
    taskId: {
      type: String,
      default: ''
    },
    businessKey: {
      type: String,
      default: ''
    },
    variables: {
      type: Object,
      default: () => ({})
    },
    formData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      loading: false,
      submitLoading: false,
      returnLoading: false,
      nextNodeName: '',
      candidateList: [],
      selectedCandidates: [],
      opinion: ''
    }
  },
  computed: {
    dialogTitle() {
      return this.taskId ? '审批提交' : '流程发起'
    },
    userId() {
      const getters = this.$store.getters
      const stateUser = this.$store.state.user
      return getters.userId ||
        stateUser.userId ||
        getters.id ||
        stateUser.id ||
        (stateUser.userInfo && stateUser.userInfo.userId)
    }
  },
  watch: {
    visible(val) {
      if (val) {
        this.resetData()
        this.loadPreview()
      }
    }
  },
  methods: {
    resetData() {
      this.nextNodeName = ''
      this.candidateList = []
      this.selectedCandidates = []
      this.opinion = ''
    },
    loadPreview() {
      this.loading = true
      const params = { variables: this.variables }
      const promise = this.taskId
        ? previewNext({ taskId: this.taskId, operator: this.userId, ...params })
        : previewStart({ processKey: this.processKey, operator: this.userId, ...params })
      promise.then(response => {
        const nodes = (response.data && response.data.nodes) || []
        if (nodes.length > 0) {
          const node = nodes[0]
          this.nextNodeName = node.nodeName || ''
          this.candidateList = node.candidates || []
        }
      }).catch(error => {
        console.error('流程预览失败', error)
      }).finally(() => {
        this.loading = false
      })
    },
    handleSubmit() {
      this.submitLoading = true
      const variables = {
        ...this.variables,
        approvalAssignee: this.selectedCandidates
      }
      const promise = this.taskId
        ? completeTask(this.taskId, {
            operator: this.userId,
            action: 'AGREE',
            opinion: this.opinion,
            formData: this.formData,
            variables,
            nextAssignees: this.selectedCandidates
          })
        : startProcess({
            processDefinitionKey: this.processKey,
            businessKey: this.businessKey,
            starter: this.userId,
            formData: this.formData,
            variables
          })
      promise.then(response => {
        this.$message.success('提交成功')
        this.$emit('success', response.data)
        this.handleClose()
      }).catch(error => {
        console.error('提交失败', error)
      }).finally(() => {
        this.submitLoading = false
      })
    },
    handleReturn() {
      if (!this.taskId) {
        return
      }
      this.returnLoading = true
      getReturnTarget(this.taskId).then(response => {
        const target = response.data || {}
        return returnToPrevious(this.taskId, {
          operator: this.userId,
          targetNodeId: target.targetNodeId,
          returnAssignee: target.targetUserId,
          opinion: this.opinion
        })
      }).then(response => {
        this.$message.success('退回成功')
        this.$emit('success', response.data)
        this.handleClose()
      }).catch(error => {
        console.error('退回失败', error)
      }).finally(() => {
        this.returnLoading = false
      })
    },
    handleClose() {
      this.$emit('close')
    }
  }
}
</script>
