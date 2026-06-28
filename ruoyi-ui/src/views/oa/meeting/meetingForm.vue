<template>
  <oa-form-page @submit="handleSubmit" @save="handleSave" @close="handleClose">
    <template #form>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="24">
            <el-form-item :label="$t('oa.meeting.form.title')" prop="title">
              <el-input v-model="form.title" :placeholder="$t('oa.meeting.search.titlePlaceholder')" :disabled="isDetail" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="$t('oa.meeting.form.timeRange')" prop="timeRange">
              <el-date-picker
                v-model="form.timeRange"
                type="datetimerange"
                value-format="yyyy-MM-dd HH:mm:ss"
                :default-time="['09:00:00','18:00:00']"
                range-separator="~"
                :start-placeholder="$t('oa.meeting.form.timeRange')"
                :end-placeholder="$t('oa.meeting.form.timeRange')"
                style="width: 100%"
                :disabled="isDetail"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('oa.meeting.search.room')" prop="roomId">
              <el-select v-model="form.roomId" :placeholder="$t('oa.meeting.form.roomIdRequired')" style="width: 100%" :disabled="isDetail">
                <el-option
                  v-for="item in roomOptions"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('oa.meeting.form.attendees')">
              <el-select v-model="form.attendeeIds" multiple collapse-tags :placeholder="$t('oa.meeting.form.attendees')" style="width: 100%" :disabled="isDetail">
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
            <el-form-item :label="$t('oa.meeting.form.content')" prop="content">
              <el-input v-model="form.content" type="textarea" :rows="4" :disabled="isDetail" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="$t('oa.meeting.form.remark')" prop="remark">
              <el-input v-model="form.remark" type="textarea" :rows="2" :disabled="isDetail" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </template>
  </oa-form-page>
</template>

<script>
import OaFormPage from "@/views/oa/components/OaFormPage"
import { getMeeting, addMeeting, updateMeeting, listAvailableRoom } from "@/api/oa/meeting"
import { listUser } from "@/api/system/user"

export default {
  name: "OaMeetingForm",
  components: {
    OaFormPage
  },
  data() {
    return {
      roomOptions: [],
      userOptions: [],
      form: {
        id: undefined,
        title: undefined,
        timeRange: [],
        roomId: undefined,
        attendeeIds: [],
        content: undefined,
        remark: undefined
      },
      rules: {
        title: [
          { required: true, message: this.$t('oa.meeting.form.titleRequired'), trigger: "blur" }
        ],
        timeRange: [
          { required: true, message: this.$t('oa.meeting.form.timeRequired'), trigger: "change" }
        ],
        roomId: [
          { required: true, message: this.$t('oa.meeting.form.roomIdRequired'), trigger: "change" }
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
    this.loadRooms()
    this.loadUsers()
    const id = this.$route.query.id
    if (id) {
      this.getMeeting(id)
    }
  },
  methods: {
    loadRooms() {
      listAvailableRoom({ pageSize: 1000 }).then(response => {
        this.roomOptions = response.data || []
      })
    },
    loadUsers() {
      listUser({ pageSize: 1000 }).then(response => {
        this.userOptions = response.rows || []
      })
    },
    getMeeting(id) {
      getMeeting(id).then(response => {
        const data = response.data
        data.timeRange = [data.startTime, data.endTime]
        data.attendeeIds = (data.attendees || []).map(a => a.userId)
        this.form = data
      }).catch(() => {})
    },
    buildSubmitData() {
      const data = { ...this.form }
      if (data.timeRange && data.timeRange.length === 2) {
        data.startTime = data.timeRange[0]
        data.endTime = data.timeRange[1]
      }
      delete data.timeRange
      delete data.attendees
      return data
    },
    handleSave() {
      this.$refs["form"].validate(valid => {
        if (!valid) {
          return
        }
        const data = this.buildSubmitData()
        if (data.id != undefined) {
          updateMeeting(data).then(() => {
            this.$modal.msgSuccess(this.$t('common.editSuccess'))
          }).catch(() => {})
        } else {
          addMeeting(data).then(response => {
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
        const data = this.buildSubmitData()
        if (data.id != undefined) {
          updateMeeting(data).then(() => {
            this.$modal.msgSuccess(this.$t('common.editSuccess'))
            this.$router.back()
          }).catch(() => {})
        } else {
          addMeeting(data).then(() => {
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
