<template>
  <oa-form-page :title="$t('oa.attendance.overtime')" @submit="handleSubmit" @save="handleSave" @close="handleClose">
    <template #form>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item :label="$t('oa.attendance.userId')" prop="userId">
          <el-select v-model="form.userId" :placeholder="$t('oa.attendance.placeholder.userId')" filterable style="width:100%" :disabled="isDetail">
            <el-option v-for="user in userOptions" :key="user.userId" :label="user.nickName || user.userName" :value="user.userId" />
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('oa.attendance.startTime')" prop="startTime">
          <el-date-picker v-model="form.startTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" :placeholder="$t('oa.attendance.placeholder.startTime')" style="width:100%" :disabled="isDetail" />
        </el-form-item>
        <el-form-item :label="$t('oa.attendance.endTime')" prop="endTime">
          <el-date-picker v-model="form.endTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" :placeholder="$t('oa.attendance.placeholder.endTime')" style="width:100%" :disabled="isDetail" />
        </el-form-item>
        <el-form-item :label="$t('oa.attendance.hours')" prop="hours">
          <el-input-number v-model="form.hours" :min="0" :precision="2" style="width:100%" :disabled="isDetail" />
        </el-form-item>
        <el-form-item :label="$t('oa.attendance.reason')" prop="reason">
          <el-input v-model="form.reason" type="textarea" :placeholder="$t('oa.attendance.placeholder.reason')" :disabled="isDetail" />
        </el-form-item>
      </el-form>
    </template>

    <flow-submit-dialog
      :visible.sync="flowVisible"
      process-key="oa_attendance_overtime"
      :business-key="businessKey"
      :submit-api="submitApi"
      @success="handleSuccess"
    />
  </oa-form-page>
</template>

<script>
import OaFormPage from "@/views/oa/components/OaFormPage"
import FlowSubmitDialog from "@/components/FlowSubmitDialog"
import { getOvertime, addOvertime, updateOvertime, submitOvertime } from "@/api/oa/attendance"
import { listUser } from "@/api/system/user"

export default {
  name: "OaAttendanceOvertimeForm",
  components: {
    OaFormPage,
    FlowSubmitDialog
  },
  data() {
    return {
      flowVisible: false,
      userOptions: [],
      form: {
        id: undefined,
        userId: undefined,
        startTime: undefined,
        endTime: undefined,
        hours: 0,
        reason: undefined,
        status: 'draft'
      },
      rules: {
        userId: [{ required: true, message: this.$t('oa.attendance.required.userId'), trigger: "change" }],
        startTime: [{ required: true, message: this.$t('oa.attendance.required.startTime'), trigger: "change" }],
        endTime: [{ required: true, message: this.$t('oa.attendance.required.endTime'), trigger: "change" }],
        reason: [{ required: true, message: this.$t('oa.attendance.required.reason'), trigger: "blur" }]
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
    businessKey() {
      return this.form.id ? 'attendance_overtime:' + this.form.id : ''
    }
  },
  mounted() {
    this.loadUsers()
    const id = this.$route.query.id
    if (id) {
      this.loadOvertime(id)
    }
  },
  methods: {
    loadUsers() {
      listUser({ pageSize: 1000 }).then(response => {
        this.userOptions = response.rows || []
      })
    },
    loadOvertime(id) {
      getOvertime(id).then(response => {
        this.form = response.data
      }).catch(() => {})
    },
    saveForm() {
      return new Promise((resolve, reject) => {
        this.$refs["form"].validate(valid => {
          if (!valid) {
            reject(new Error('表单校验失败'))
            return
          }
          const data = { ...this.form }
          const request = data.id != undefined ? updateOvertime(data) : addOvertime(data)
          request.then(response => {
            if (!data.id && response.data) {
              this.form.id = response.data
            }
            resolve(response)
          }).catch(reject)
        })
      })
    },
    handleSave() {
      this.saveForm().then(() => {
        this.$modal.msgSuccess(this.$t('common.saveSuccess'))
      }).catch(() => {})
    },
    handleSubmit() {
      this.saveForm().then(() => {
        this.flowVisible = true
      }).catch(() => {})
    },
    submitApi(data) {
      return submitOvertime(this.form.id, data)
    },
    handleSuccess() {
      this.$modal.msgSuccess(this.$t('oa.attendance.submitApproval'))
      this.handleClose()
    },
    handleClose() {
      this.$router.back()
    }
  }
}
</script>
