<template>
  <oa-form-page @submit="handleSubmit" @save="handleSave" @close="handleClose">
    <template #form>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="24">
            <el-form-item :label="$t('addressBook.name')" prop="contactUserId">
              <el-input v-model="contactUserName" :disabled="true" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="$t('addressBook.contactGroups')" prop="groupId">
              <el-select v-model="form.groupId" clearable :placeholder="$t('addressBook.all')" style="width:100%" :disabled="isDetail">
                <el-option
                  v-for="group in contactGroups"
                  :key="group.groupId"
                  :label="group.groupName"
                  :value="group.groupId"
                />
              </el-select>
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
import { addContact, listContactGroups, getUserDetail } from "@/api/oa/addressbook"

export default {
  name: "OaAddressBookContactForm",
  components: {
    OaFormPage
  },
  data() {
    return {
      contactGroups: [],
      contactUserName: undefined,
      form: {
        contactUserId: undefined,
        groupId: undefined
      },
      rules: {
        contactUserId: [
          { required: true, message: this.$t('addressBook.noData'), trigger: "change" }
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
    this.getContactGroups()
    const contactUserId = this.$route.query.contactUserId
    if (contactUserId) {
      this.form.contactUserId = Number(contactUserId)
      this.getUserDetail(this.form.contactUserId)
    }
  },
  methods: {
    getContactGroups() {
      listContactGroups().then(response => {
        this.contactGroups = response.data || []
      })
    },
    getUserDetail(userId) {
      getUserDetail(userId).then(response => {
        const user = response.data
        this.contactUserName = user.nickName || user.userName
      }).catch(() => {})
    },
    handleSave() {
      this.$refs["form"].validate(valid => {
        if (!valid) {
          return
        }
        addContact(this.form).then(() => {
          this.$modal.msgSuccess(this.$t('common.addSuccess'))
        }).catch(() => {})
      })
    },
    handleSubmit() {
      this.$refs["form"].validate(valid => {
        if (!valid) {
          return
        }
        addContact(this.form).then(() => {
          this.$modal.msgSuccess(this.$t('common.addSuccess'))
          this.$router.back()
        }).catch(() => {})
      })
    },
    handleClose() {
      this.$router.back()
    }
  }
}
</script>
