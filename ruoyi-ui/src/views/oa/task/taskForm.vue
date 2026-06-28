<template>
  <oa-form-page :title="$t('oa.task.title')" @submit="handleSubmit" @save="handleSave" @close="handleClose">
    <template #form>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="24">
            <el-form-item :label="$t('oa.task.title')" prop="title">
              <el-input v-model="form.title" :placeholder="$t('oa.task.placeholder.title')" :disabled="isDetail" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('oa.task.owner')" prop="ownerId">
              <el-select v-model="form.ownerId" :placeholder="$t('oa.task.placeholder.owner')" filterable style="width:100%" :disabled="isDetail">
                <el-option v-for="user in userOptions" :key="user.userId" :label="user.nickName || user.userName" :value="user.userId" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('oa.task.participant')" prop="participantIds">
              <el-select v-model="form.participantIds" multiple collapse-tags :placeholder="$t('oa.task.placeholder.participant')" style="width:100%" :disabled="isDetail">
                <el-option v-for="user in userOptions" :key="user.userId" :label="user.nickName || user.userName" :value="user.userId" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('oa.task.priorityLabel')" prop="priority">
              <el-select v-model="form.priority" :placeholder="$t('oa.task.placeholder.priority')" style="width:100%" :disabled="isDetail">
                <el-option v-for="item in priorityOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('oa.task.statusLabel')" prop="status">
              <el-select v-model="form.status" :placeholder="$t('oa.task.placeholder.status')" style="width:100%" :disabled="isDetail">
                <el-option v-for="item in statusOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="$t('oa.task.progress')" prop="progress">
              <el-slider v-model="form.progress" :max="100" show-input :disabled="isDetail" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="$t('oa.task.startTime') + ' / ' + $t('oa.task.endTime')" prop="timeRange">
              <el-date-picker
                v-model="form.timeRange"
                type="datetimerange"
                value-format="yyyy-MM-dd HH:mm:ss"
                :default-time="['00:00:00','23:59:59']"
                range-separator="-"
                :start-placeholder="$t('oa.task.startTime')"
                :end-placeholder="$t('oa.task.endTime')"
                style="width:100%"
                :disabled="isDetail"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="$t('oa.task.description')" prop="description">
              <el-input v-model="form.description" type="textarea" :rows="3" :placeholder="$t('oa.task.placeholder.description')" :disabled="isDetail" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="$t('oa.task.subtask')">
              <div v-for="(sub, index) in form.subtasks" :key="index" class="subtask-row">
                <el-input v-model="sub.title" :placeholder="$t('oa.task.placeholder.subtaskTitle')" style="width: 60%" :disabled="isDetail" />
                <el-select v-model="sub.status" style="width: 100px; margin-left: 8px" :disabled="isDetail">
                  <el-option v-for="item in statusOptions" :key="item.value" :label="item.label" :value="item.value" />
                </el-select>
                <el-button v-if="!isDetail" type="text" icon="el-icon-delete" style="margin-left: 8px" @click="removeSubtask(index)">{{ $t('common.delete') }}</el-button>
              </div>
              <el-button v-if="!isDetail" type="text" icon="el-icon-plus" @click="addSubtask">{{ $t('oa.task.addSubtask') }}</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </template>
  </oa-form-page>
</template>

<script>
import OaFormPage from "@/views/oa/components/OaFormPage"
import { getTask, addTask, updateTask } from "@/api/oa/task"
import { listUser } from "@/api/system/user"

export default {
  name: "OaTaskForm",
  components: {
    OaFormPage
  },
  data() {
    return {
      userOptions: [],
      form: {
        id: undefined,
        title: undefined,
        description: undefined,
        ownerId: undefined,
        participantIds: [],
        priority: "medium",
        status: 0,
        progress: 0,
        timeRange: [],
        subtasks: []
      },
      rules: {
        title: [
          { required: true, message: this.$t('oa.task.required.title'), trigger: "blur" }
        ]
      }
    }
  },
  computed: {
    mode() {
      const m = this.$route.query.mode
      return ["add", "edit", "detail"].includes(m) ? m : "add"
    },
    isDetail() {
      return this.mode === "detail"
    },
    statusOptions() {
      return [
        { label: this.$t('oa.task.status.pending'), value: 0 },
        { label: this.$t('oa.task.status.inProgress'), value: 1 },
        { label: this.$t('oa.task.status.completed'), value: 2 },
        { label: this.$t('oa.task.status.cancelled'), value: 3 }
      ]
    },
    priorityOptions() {
      return [
        { label: this.$t('oa.task.priority.urgent'), value: 'urgent' },
        { label: this.$t('oa.task.priority.high'), value: 'high' },
        { label: this.$t('oa.task.priority.medium'), value: 'medium' },
        { label: this.$t('oa.task.priority.low'), value: 'low' }
      ]
    }
  },
  mounted() {
    this.loadUsers()
    const id = this.$route.query.id
    if (id) {
      this.getTask(id)
    }
  },
  methods: {
    loadUsers() {
      listUser({ pageSize: 1000 }).then(response => {
        this.userOptions = response.rows || []
      })
    },
    getTask(id) {
      getTask(id).then(response => {
        const data = response.data
        data.timeRange = [data.startTime, data.endTime]
        data.participantIds = this.extractParticipantIds(data.members)
        data.subtasks = data.subtasks || []
        this.form = data
      }).catch(() => {})
    },
    extractParticipantIds(members) {
      if (!members) return []
      return members.filter(m => m.role === 'participant').map(m => m.userId)
    },
    addSubtask() {
      this.form.subtasks.push({ title: "", status: 0 })
    },
    removeSubtask(index) {
      this.form.subtasks.splice(index, 1)
    },
    buildData() {
      const data = { ...this.form }
      data.startTime = data.timeRange && data.timeRange.length > 0 ? data.timeRange[0] : undefined
      data.endTime = data.timeRange && data.timeRange.length > 0 ? data.timeRange[1] : undefined
      delete data.timeRange
      data.subtasks = (data.subtasks || []).filter(s => s.title && s.title.trim() !== "")
      return data
    },
    handleSave() {
      this.$refs["form"].validate(valid => {
        if (!valid) {
          return
        }
        const data = this.buildData()
        if (data.id != undefined) {
          updateTask(data).then(() => {
            this.$modal.msgSuccess(this.$t('common.editSuccess'))
          }).catch(() => {})
        } else {
          addTask(data).then(response => {
            this.form.id = response.data
            this.$modal.msgSuccess(this.$t('common.addSuccess'))
          }).catch(() => {})
        }
      })
    },
    handleSubmit() {
      this.$refs["form"].validate(valid => {
        if (!valid) {
          return
        }
        const data = this.buildData()
        if (data.id != undefined) {
          updateTask(data).then(() => {
            this.$modal.msgSuccess(this.$t('common.editSuccess'))
            this.$router.back()
          }).catch(() => {})
        } else {
          addTask(data).then(() => {
            this.$modal.msgSuccess(this.$t('common.addSuccess'))
            this.$router.back()
          }).catch(() => {})
        }
      })
    },
    handleClose() {
      this.$router.back()
    }
  }
}
</script>

<style scoped>
.subtask-row {
  display: flex;
  align-items: center;
  margin-bottom: 8px;
}
</style>
