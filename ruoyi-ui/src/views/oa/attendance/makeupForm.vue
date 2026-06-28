<template>
  <oa-form-page @submit="handleSubmit" @save="handleSave" @close="handleClose">
    <template #form>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item :label="$t('oa.attendance.userId')" prop="userId">
          <el-select v-model="form.userId" :placeholder="$t('oa.attendance.placeholder.userId')" filterable style="width:100%">
            <el-option v-for="user in userOptions" :key="user.userId" :label="user.nickName || user.userName" :value="user.userId" />
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('oa.attendance.makeupDate')" prop="makeupDate">
          <el-date-picker v-model="form.makeupDate" type="date" value-format="yyyy-MM-dd" :placeholder="$t('oa.attendance.placeholder.makeupDate')" style="width:100%" />
        </el-form-item>
        <el-form-item :label="$t('oa.attendance.checkType')" prop="checkType">
          <el-select v-model="form.checkType" :placeholder="$t('oa.attendance.placeholder.checkType')" style="width:100%">
            <el-option v-for="item in checkTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('oa.attendance.checkInTime')" prop="checkTime">
          <el-date-picker v-model="form.checkTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" :placeholder="$t('oa.attendance.placeholder.checkTime')" style="width:100%" />
        </el-form-item>
        <el-form-item :label="$t('oa.attendance.reason')" prop="reason">
          <el-input v-model="form.reason" type="textarea" :placeholder="$t('oa.attendance.placeholder.reason')" />
        </el-form-item>
      </el-form>
    </template>

    <flow-submit-dialog
      :visible="flowVisible"
      process-key="oa_attendance_makeup"
      :business-key="'attendance_makeup:' + form.id"
      :submit-api="submitApi"
      @close="flowVisible = false"
      @success="handleSuccess"
    />
  </oa-form-page>
</template>

<script>
import OaFormPage from "@/views/oa/components/OaFormPage"
import FlowSubmitDialog from "@/components/FlowSubmitDialog"
import { getMakeup, addMakeup, updateMakeup, submitMakeup } from "@/api/oa/attendance"
import { listUser } from "@/api/system/user"

export default {
  name: "OaAttendanceMakeupForm",
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
        makeupDate: undefined,
        checkType: 'check_in',
        checkTime: undefined,
        reason: undefined,
        status: 'draft'
      },
      rules: {
        userId: [{ required: true, message: this.$t('oa.attendance.required.userId'), trigger: "change" }],
        makeupDate: [{ required: true, message: this.$t('oa.attendance.required.makeupDate'), trigger: "change" }],
        checkType: [{ required: true, message: this.$t('oa.attendance.required.checkType'), trigger: "change" }],
        reason: [{ required: true, message: this.$t('oa.attendance.required.reason'), trigger: "blur" }]
      }
    }
  },
  computed: {
    checkTypeOptions() {
      return [
        { label: this.$t('oa.attendance.checkIn'), value: 'check_in' },
        { label: this.$t('oa.attendance.checkOut'), value: 'check_out' }
      ]
    }
  },
  mounted() {
    this.loadUsers()
    const id = this.$route.query.id
    if (id) {
      this.getMakeup(id)
    }
  },
  methods: {
    loadUsers() {
      listUser({ pageSize: 1000 }).then(response => {
        this.userOptions = response.rows || []
      }).catch(() => {})
    },
    getMakeup(id) {
      getMakeup(id).then(response => {
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
          updateMakeup(data).then(() => {
            this.$modal.msgSuccess(this.$t('common.editSuccess'))
          }).catch(() => {})
        } else {
          addMakeup(data).then(response => {
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
          this.$modal.msgWarning("请先保存补卡申请")
          return
        }
        this.flowVisible = true
      })
    },
    submitApi(data) {
      return submitMakeup(this.form.id, { approvalAssignee: data.approvalAssignee })
    },
    handleSuccess() {
      this.$router.back()
    },
    handleClose() {
      this.$router.back()
    }
  }
}
</script>
