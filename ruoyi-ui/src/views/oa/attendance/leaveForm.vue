<template>
  <oa-form-page @submit="handleSubmit" @save="handleSave" @close="handleClose">
    <template #form>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item :label="$t('oa.attendance.userId')" prop="userId">
              <el-select v-model="form.userId" :placeholder="$t('oa.attendance.placeholder.userId')" filterable style="width:100%" :disabled="isDetail">
                <el-option v-for="user in userOptions" :key="user.userId" :label="user.nickName || user.userName" :value="user.userId" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('oa.attendance.leaveType')" prop="leaveType">
              <el-select v-model="form.leaveType" :placeholder="$t('oa.attendance.placeholder.leaveType')" style="width:100%" :disabled="isDetail">
                <el-option v-for="item in leaveTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('oa.attendance.startTime')" prop="startTime">
              <el-date-picker v-model="form.startTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" :placeholder="$t('oa.attendance.placeholder.startTime')" style="width:100%" :disabled="isDetail" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('oa.attendance.endTime')" prop="endTime">
              <el-date-picker v-model="form.endTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" :placeholder="$t('oa.attendance.placeholder.endTime')" style="width:100%" :disabled="isDetail" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('oa.attendance.days')" prop="days">
              <el-input-number v-model="form.days" :min="0" :precision="2" style="width:100%" :disabled="isDetail" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="$t('oa.attendance.reason')" prop="reason">
              <el-input v-model="form.reason" type="textarea" :placeholder="$t('oa.attendance.placeholder.reason')" :disabled="isDetail" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </template>

    <flow-submit-dialog
      :visible="flowVisible"
      process-key="oa_attendance_leave"
      :business-key="'attendance_leave:' + form.id"
      :submit-api="submitApi"
      @close="flowVisible = false"
      @success="handleClose"
    />
  </oa-form-page>
</template>

<script>
import OaFormPage from "@/views/oa/components/OaFormPage"
import FlowSubmitDialog from "@/components/FlowSubmitDialog"
import { getLeave, addLeave, updateLeave, submitLeave } from "@/api/oa/attendance"
import { listUser } from "@/api/system/user"

export default {
  name: "OaAttendanceLeaveForm",
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
        leaveType: undefined,
        startTime: undefined,
        endTime: undefined,
        days: 0,
        reason: undefined,
        status: 'draft'
      },
      rules: {
        userId: [{ required: true, message: this.$t('oa.attendance.required.userId'), trigger: "change" }],
        leaveType: [{ required: true, message: this.$t('oa.attendance.required.leaveType'), trigger: "change" }],
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
    leaveTypeOptions() {
      return [
        { label: this.$t('oa.attendance.annual'), value: 'annual' },
        { label: this.$t('oa.attendance.sick'), value: 'sick' },
        { label: this.$t('oa.attendance.personal'), value: 'personal' },
        { label: this.$t('oa.attendance.other'), value: 'other' }
      ]
    }
  },
  mounted() {
    this.loadUsers()
    const id = this.$route.query.id
    if (id) {
      this.getLeave(id)
    }
  },
  methods: {
    loadUsers() {
      listUser({ pageSize: 1000 }).then(response => {
        this.userOptions = response.rows || []
      })
    },
    getLeave(id) {
      getLeave(id).then(response => {
        this.form = response.data
      }).catch(() => {})
    },
    handleSave() {
      this.$refs["form"].validate(valid => {
        if (!valid) {
          return
        }
        const data = { ...this.form }
        if (data.id != undefined) {
          updateLeave(data).then(() => {
            this.$modal.msgSuccess(this.$t('common.editSuccess'))
          }).catch(() => {})
        } else {
          addLeave(data).then(response => {
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
        if (!this.form.id) {
          this.$modal.msgWarning("请先保存请假申请")
          return
        }
        this.flowVisible = true
      })
    },
    submitApi(data) {
      return submitLeave(this.form.id, data)
    },
    handleClose() {
      this.$router.back()
    }
  }
}
</script>
