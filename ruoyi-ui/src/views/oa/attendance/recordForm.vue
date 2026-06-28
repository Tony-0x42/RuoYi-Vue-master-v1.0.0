<template>
  <oa-form-page @save="handleSave" @submit="handleSubmit" @close="handleClose">
    <template #form>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item :label="$t('oa.attendance.userId')" prop="userId">
          <el-select v-model="form.userId" :placeholder="$t('oa.attendance.placeholder.userId')" filterable style="width:100%" :disabled="isDetail">
            <el-option v-for="user in userOptions" :key="user.userId" :label="user.nickName || user.userName" :value="user.userId" />
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('oa.attendance.checkInTime')" prop="checkInTime">
          <el-date-picker
            v-model="form.checkInTime"
            type="datetime"
            value-format="yyyy-MM-dd HH:mm:ss"
            :placeholder="$t('oa.attendance.checkInTime')"
            style="width:100%"
            :disabled="isDetail"
          />
        </el-form-item>
        <el-form-item :label="$t('oa.attendance.checkInType')" prop="type">
          <el-select v-model="form.type" :placeholder="$t('oa.attendance.placeholder.checkInType')" style="width:100%" :disabled="isDetail">
            <el-option v-for="item in typeOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('oa.attendance.location')" prop="location">
          <el-input v-model="form.location" :placeholder="$t('oa.attendance.placeholder.location')" :disabled="isDetail" />
        </el-form-item>
        <el-form-item :label="$t('oa.attendance.status')" prop="status">
          <el-select v-model="form.status" :placeholder="$t('oa.attendance.placeholder.status')" style="width:100%" :disabled="isDetail">
            <el-option v-for="item in statusOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('common.remark')">
          <el-input v-model="form.remark" type="textarea" :rows="2" :disabled="isDetail" />
        </el-form-item>
      </el-form>
    </template>
  </oa-form-page>
</template>

<script>
import OaFormPage from "@/views/oa/components/OaFormPage"
import { getRecord, addRecord, updateRecord } from "@/api/oa/attendance"
import { listUser } from "@/api/system/user"

export default {
  name: "OaAttendanceRecordForm",
  components: {
    OaFormPage
  },
  data() {
    return {
      userOptions: [],
      form: {
        id: undefined,
        userId: undefined,
        checkInTime: undefined,
        type: "gps",
        location: undefined,
        status: "normal",
        remark: undefined
      },
      rules: {
        userId: [{ required: true, message: this.$t('oa.attendance.required.userId'), trigger: "change" }],
        checkInTime: [{ required: true, message: this.$t('oa.attendance.checkInTime'), trigger: "change" }]
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
        { label: this.$t('oa.attendance.normal'), value: 'normal' },
        { label: this.$t('oa.attendance.late'), value: 'late' },
        { label: this.$t('oa.attendance.early'), value: 'early' },
        { label: this.$t('oa.attendance.absent'), value: 'absent' }
      ]
    },
    typeOptions() {
      return [
        { label: this.$t('oa.attendance.gps'), value: 'gps' },
        { label: this.$t('oa.attendance.wifiType'), value: 'wifi' },
        { label: this.$t('oa.attendance.device'), value: 'device' },
        { label: this.$t('oa.attendance.field'), value: 'field' }
      ]
    }
  },
  mounted() {
    this.loadUsers()
    const id = this.$route.query.id
    if (id) {
      this.getRecord(id)
    }
  },
  methods: {
    loadUsers() {
      listUser({ pageSize: 1000 }).then(response => {
        this.userOptions = response.rows || []
      })
    },
    getRecord(id) {
      getRecord(id).then(response => {
        this.form = response.data
      }).catch(() => {})
    },
    saveForm(back) {
      this.$refs["form"].validate(valid => {
        if (!valid) {
          return
        }
        const data = { ...this.form }
        if (data.id != undefined) {
          updateRecord(data).then(() => {
            this.$modal.msgSuccess(this.$t('common.editSuccess'))
            if (back) {
              this.$router.back()
            }
          }).catch(() => {})
        } else {
          addRecord(data).then(response => {
            this.form.id = response.data
            this.$modal.msgSuccess(this.$t('common.addSuccess'))
            if (back) {
              this.$router.back()
            }
          }).catch(() => {})
        }
      })
    },
    handleSave() {
      this.saveForm(false)
    },
    handleSubmit() {
      this.saveForm(true)
    },
    handleClose() {
      this.$router.back()
    }
  }
}
</script>
