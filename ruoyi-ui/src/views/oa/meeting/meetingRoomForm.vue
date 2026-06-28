<template>
  <oa-form-page @submit="handleSubmit" @save="handleSave" @close="handleClose">
    <template #form>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item :label="$t('oa.meeting.room.code')" prop="code">
          <el-input v-model="form.code" :placeholder="$t('oa.meeting.room.codePlaceholder')" :disabled="isDetail" />
        </el-form-item>
        <el-form-item :label="$t('oa.meeting.room.name')" prop="name">
          <el-input v-model="form.name" :placeholder="$t('oa.meeting.room.namePlaceholder')" :disabled="isDetail" />
        </el-form-item>
        <el-form-item :label="$t('oa.meeting.room.location')" prop="location">
          <el-input v-model="form.location" :placeholder="$t('oa.meeting.room.locationPlaceholder')" :disabled="isDetail" />
        </el-form-item>
        <el-form-item :label="$t('oa.meeting.room.capacity')" prop="capacity">
          <el-input-number v-model="form.capacity" :min="1" controls-position="right" style="width: 100%" :disabled="isDetail" />
        </el-form-item>
        <el-form-item :label="$t('oa.meeting.room.devices')" prop="devices">
          <el-input v-model="form.devices" :placeholder="$t('oa.meeting.room.devicesPlaceholder')" :disabled="isDetail" />
        </el-form-item>
        <el-form-item :label="$t('common.status')" prop="status">
          <el-radio-group v-model="form.status" :disabled="isDetail">
            <el-radio :label="1">{{ $t('oa.meeting.room.enable') }}</el-radio>
            <el-radio :label="0">{{ $t('oa.meeting.room.disable') }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item :label="$t('oa.meeting.room.picUrl')" prop="picUrl">
          <el-input v-model="form.picUrl" placeholder="https://" :disabled="isDetail" />
        </el-form-item>
      </el-form>
    </template>
  </oa-form-page>
</template>

<script>
import OaFormPage from "@/views/oa/components/OaFormPage"
import { getRoom, addRoom, updateRoom } from "@/api/oa/meeting"

export default {
  name: "OaMeetingRoomForm",
  components: {
    OaFormPage
  },
  data() {
    return {
      form: {
        id: undefined,
        code: undefined,
        name: undefined,
        location: undefined,
        capacity: 10,
        devices: undefined,
        status: 1,
        picUrl: undefined
      },
      rules: {
        code: [
          { required: true, message: this.$t('oa.meeting.required.code'), trigger: "blur" }
        ],
        name: [
          { required: true, message: this.$t('oa.meeting.required.name'), trigger: "blur" }
        ],
        capacity: [
          { required: true, message: this.$t('oa.meeting.required.capacity'), trigger: "blur" }
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
    const id = this.$route.query.id
    if (id) {
      this.getRoom(id)
    }
  },
  methods: {
    getRoom(id) {
      getRoom(id).then(response => {
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
          updateRoom(data).then(() => {
            this.$modal.msgSuccess(this.$t('common.editSuccess'))
          }).catch(() => {})
        } else {
          addRoom(data).then(response => {
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
        const data = { ...this.form }
        if (data.id != undefined) {
          updateRoom(data).then(() => {
            this.$modal.msgSuccess(this.$t('common.editSuccess'))
            this.$router.back()
          }).catch(() => {})
        } else {
          addRoom(data).then(() => {
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
