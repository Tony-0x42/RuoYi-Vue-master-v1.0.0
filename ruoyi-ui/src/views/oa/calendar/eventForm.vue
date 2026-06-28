<template>
  <oa-form-page :title="$t('calendar.title')" @submit="handleSubmit" @save="handleSave" @close="handleClose">
    <template #form>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="24">
            <el-form-item :label="$t('calendar.form.title')" prop="title">
              <el-input v-model="form.title" :placeholder="$t('calendar.search.titlePlaceholder')" :disabled="isDetail" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('calendar.search.type')" prop="type">
              <el-select v-model="form.type" :placeholder="$t('calendar.search.type')" style="width: 100%" :disabled="isDetail">
                <el-option :label="$t('calendar.type.personal')" value="personal" />
                <el-option :label="$t('calendar.type.meeting')" value="meeting" />
                <el-option :label="$t('calendar.type.leave')" value="leave" />
                <el-option :label="$t('calendar.type.trip')" value="trip" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('calendar.allDay')">
              <el-radio-group v-model="form.allDay" :disabled="isDetail">
                <el-radio :label="0">{{ $t('common.no') }}</el-radio>
                <el-radio :label="1">{{ $t('common.yes') }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="$t('calendar.search.dateRange')" prop="timeRange">
              <el-date-picker
                v-model="form.timeRange"
                type="datetimerange"
                value-format="yyyy-MM-dd HH:mm:ss"
                :default-time="['00:00:00','23:59:59']"
                range-separator="-"
                :start-placeholder="$t('calendar.startTime')"
                :end-placeholder="$t('calendar.endTime')"
                style="width: 100%"
                :disabled="isDetail"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('calendar.location')">
              <el-input v-model="form.location" :placeholder="$t('calendar.form.locationPlaceholder')" :disabled="isDetail" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('calendar.attendees')">
              <el-select v-model="form.attendeeIds" multiple collapse-tags :placeholder="$t('calendar.attendees')" style="width: 100%" :disabled="isDetail">
                <el-option
                  v-for="user in userOptions"
                  :key="user.userId"
                  :label="user.userName"
                  :value="user.userId"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="$t('calendar.form.remark')">
              <el-input v-model="form.remark" type="textarea" :rows="3" :disabled="isDetail" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </template>
  </oa-form-page>
</template>

<script>
import './i18n'
import OaFormPage from "@/views/oa/components/OaFormPage"
import { getCalendarEvent, addCalendarEvent, updateCalendarEvent } from "@/api/oa/calendar"
import { listUser } from "@/api/system/user"

export default {
  name: "OaCalendarEventForm",
  components: {
    OaFormPage
  },
  data() {
    return {
      userOptions: [],
      form: {
        eventId: undefined,
        title: undefined,
        type: "personal",
        allDay: 0,
        timeRange: [],
        location: undefined,
        attendeeIds: [],
        remark: undefined
      },
      rules: {
        title: [
          { required: true, message: this.$t('calendar.form.titleRequired'), trigger: "blur" }
        ],
        type: [
          { required: true, message: this.$t('calendar.form.typeRequired'), trigger: "change" }
        ],
        timeRange: [
          { required: true, message: this.$t('calendar.form.timeRequired'), trigger: "change" }
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
    }
  },
  mounted() {
    this.loadUsers()
    const id = this.$route.query.id
    if (id) {
      this.getCalendarEvent(id)
    }
  },
  methods: {
    loadUsers() {
      listUser({ pageSize: 1000 }).then(response => {
        this.userOptions = response.rows || []
      })
    },
    getCalendarEvent(id) {
      getCalendarEvent(id).then(response => {
        const data = response.data
        data.timeRange = [data.startTime, data.endTime]
        data.attendeeIds = data.attendeeIds || []
        this.form = data
      }).catch(() => {})
    },
    buildSubmitData() {
      const data = { ...this.form }
      if (data.timeRange && data.timeRange.length >= 2) {
        data.startTime = data.timeRange[0]
        data.endTime = data.timeRange[1]
      }
      delete data.timeRange
      return data
    },
    saveCalendarEvent() {
      return new Promise((resolve, reject) => {
        this.$refs["form"].validate(valid => {
          if (!valid) {
            reject(new Error("表单校验失败"))
            return
          }
          const data = this.buildSubmitData()
          const isEdit = data.eventId != undefined
          if (isEdit) {
            updateCalendarEvent(data).then(response => {
              resolve({ response, isEdit })
            }).catch(reject)
          } else {
            addCalendarEvent(data).then(response => {
              if (response.data) {
                this.form.eventId = response.data
              }
              resolve({ response, isEdit })
            }).catch(reject)
          }
        })
      })
    },
    handleSave() {
      this.saveCalendarEvent().then(({ isEdit }) => {
        this.$modal.msgSuccess(isEdit ? this.$t('common.editSuccess') : this.$t('common.addSuccess'))
      }).catch(() => {})
    },
    handleSubmit() {
      this.saveCalendarEvent().then(({ isEdit }) => {
        this.$modal.msgSuccess(isEdit ? this.$t('common.editSuccess') : this.$t('common.addSuccess'))
        this.handleClose()
      }).catch(() => {})
    },
    handleClose() {
      this.$router.back()
    }
  }
}
</script>
