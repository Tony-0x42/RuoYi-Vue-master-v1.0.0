<template>
  <oa-form-page @save="handleSave" @submit="handleSubmit" @close="handleClose">
    <template #form>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item :label="$t('oa.attendance.group')" prop="name">
          <el-input v-model="form.name" :placeholder="$t('oa.attendance.placeholder.name')" :disabled="isDetail" />
        </el-form-item>
        <el-form-item :label="$t('oa.attendance.memberType')" prop="memberType">
          <el-select v-model="form.memberType" :placeholder="$t('oa.attendance.placeholder.memberType')" style="width:100%" :disabled="isDetail">
            <el-option v-for="item in memberTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('oa.attendance.memberIds')" prop="memberIds">
          <el-input v-model="form.memberIds" :placeholder="$t('oa.attendance.placeholder.memberIds')" :disabled="isDetail" />
        </el-form-item>
        <el-form-item :label="$t('oa.attendance.shiftId')" prop="shiftId">
          <el-select v-model="form.shiftId" :placeholder="$t('oa.attendance.placeholder.shiftId')" style="width:100%" :disabled="isDetail">
            <el-option v-for="item in shiftOptions" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('oa.attendance.checkInType')" prop="checkInType">
          <el-select v-model="form.checkInType" :placeholder="$t('oa.attendance.placeholder.checkInType')" style="width:100%" :disabled="isDetail">
            <el-option v-for="item in checkInTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('oa.attendance.workDays')" prop="workDays">
          <el-select v-model="form.workDays" :placeholder="$t('oa.attendance.placeholder.workDays')" style="width:100%" :disabled="isDetail">
            <el-option label="周一到周五" value="1,2,3,4,5" />
            <el-option label="周一到周六" value="1,2,3,4,5,6" />
            <el-option label="每天" value="1,2,3,4,5,6,7" />
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
import { getGroup, addGroup, updateGroup, listShift } from "@/api/oa/attendance"

export default {
  name: "OaAttendanceGroupForm",
  components: {
    OaFormPage
  },
  data() {
    return {
      shiftOptions: [],
      form: {
        id: undefined,
        name: undefined,
        memberType: "user",
        memberIds: undefined,
        shiftId: undefined,
        checkInType: "gps",
        workDays: "1,2,3,4,5",
        remark: undefined
      },
      rules: {
        name: [{ required: true, message: this.$t('oa.attendance.required.name'), trigger: "blur" }],
        shiftId: [{ required: true, message: this.$t('oa.attendance.required.shiftId'), trigger: "change" }]
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
    memberTypeOptions() {
      return [
        { label: this.$t('oa.attendance.dept'), value: 'dept' },
        { label: this.$t('oa.attendance.role'), value: 'role' },
        { label: this.$t('oa.attendance.user'), value: 'user' }
      ]
    },
    checkInTypeOptions() {
      return [
        { label: this.$t('oa.attendance.gps'), value: 'gps' },
        { label: this.$t('oa.attendance.wifiType'), value: 'wifi' },
        { label: this.$t('oa.attendance.device'), value: 'device' },
        { label: this.$t('oa.attendance.field'), value: 'field' },
        { label: '多种', value: 'multiple' }
      ]
    }
  },
  mounted() {
    this.loadShifts()
    const id = this.$route.query.id
    if (id) {
      this.getGroup(id)
    }
  },
  methods: {
    loadShifts() {
      listShift({ pageSize: 1000 }).then(response => {
        this.shiftOptions = response.rows || []
      })
    },
    getGroup(id) {
      getGroup(id).then(response => {
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
          updateGroup(data).then(() => {
            this.$modal.msgSuccess(this.$t('common.editSuccess'))
            if (back) {
              this.$router.back()
            }
          }).catch(() => {})
        } else {
          addGroup(data).then(response => {
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
