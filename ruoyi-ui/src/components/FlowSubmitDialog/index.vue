<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogVisible"
    width="700px"
    append-to-body
    @closed="handleClosed"
  >
    <el-form ref="form" :model="form" label-width="100px">
      <el-form-item label="审批人" prop="approvalAssignee">
        <user-multi-select v-model="form.approvalAssignee" title="选择审批人" />
      </el-form-item>
      <el-form-item label="审批意见" prop="opinion">
        <el-input v-model="form.opinion" type="textarea" :rows="3" placeholder="请输入审批意见" />
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" :loading="loading" @click="handleSubmit">确 定</el-button>
      <el-button @click="handleCancel">取 消</el-button>
    </div>
  </el-dialog>
</template>

<script>
import UserMultiSelect from '@/components/UserMultiSelect'
import { listDefinition } from '@/api/bpm/definition'
import { startProcess, submitTask } from '@/api/bpm/runtime'

export default {
  name: 'FlowSubmitDialog',
  components: { UserMultiSelect },
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    title: {
      type: String,
      default: '提交审批'
    },
    processKey: {
      type: String,
      default: ''
    },
    businessKey: {
      type: String,
      default: ''
    },
    taskId: {
      type: String,
      default: ''
    },
    submitApi: {
      type: Function,
      default: null
    }
  },
  data() {
    return {
      dialogVisible: this.visible,
      loading: false,
      form: {
        approvalAssignee: '',
        opinion: ''
      }
    }
  },
  watch: {
    visible(val) {
      this.dialogVisible = val
    },
    dialogVisible(val) {
      this.$emit('update:visible', val)
    }
  },
  methods: {
    parseCandidates(value) {
      if (!value) return []
      return [...new Set(String(value).split(',').map(s => s.trim()).filter(Boolean))]
    },
    handleSubmit() {
      this.loading = true
      const selectedCandidates = this.parseCandidates(this.form.approvalAssignee)
      const payload = {
        approvalAssignee: selectedCandidates,
        opinion: this.form.opinion,
        businessKey: this.businessKey,
        processKey: this.processKey
      }

      let promise
      if (this.taskId) {
        promise = submitTask(this.taskId, {
          opinion: payload.opinion,
          variables: { approvalAssignee: selectedCandidates }
        })
      } else if (this.submitApi) {
        promise = this.submitApi(payload)
      } else {
        promise = this.startProcessFallback(payload)
      }

      promise.then(response => {
        this.$emit('success', response)
        this.dialogVisible = false
      }).catch(() => {}).finally(() => {
        this.loading = false
      })
    },
    startProcessFallback(payload) {
      if (!this.processKey) {
        return Promise.reject(new Error('缺少流程标识'))
      }
      return listDefinition({
        processKey: this.processKey,
        status: 1,
        pageNum: 1,
        pageSize: 1
      }).then(response => {
        const rows = response.rows || []
        if (!rows.length) {
          throw new Error('未找到已发布的流程定义')
        }
        return startProcess(rows[0].id, {
          businessKey: payload.businessKey,
          variables: { approvalAssignee: payload.approvalAssignee }
        })
      })
    },
    handleCancel() {
      this.dialogVisible = false
    },
    handleClosed() {
      this.form.approvalAssignee = ''
      this.form.opinion = ''
    }
  }
}
</script>
