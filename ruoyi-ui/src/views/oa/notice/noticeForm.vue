<template>
  <oa-form-page @submit="handleSubmit" @save="handleSave" @close="handleClose">
    <template #form>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item :label="$t('oa.notice.title')" prop="title">
              <el-input v-model="form.title" :placeholder="$t('oa.notice.placeholder.title')" :disabled="isDetail" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('oa.notice.category')" prop="categoryId">
              <el-select v-model="form.categoryId" :placeholder="$t('oa.notice.placeholder.category')" style="width:100%" :disabled="isDetail">
                <el-option
                  v-for="item in categoryList"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('oa.notice.scopeType')" prop="scopeType">
              <el-select v-model="form.scopeType" :placeholder="$t('oa.notice.placeholder.scopeType')" style="width:100%" :disabled="isDetail">
                <el-option :label="$t('oa.notice.scopeAll')" value="all" />
                <el-option :label="$t('oa.notice.scopeDept')" value="dept" />
                <el-option :label="$t('oa.notice.scopeRole')" value="role" />
                <el-option :label="$t('oa.notice.scopeUser')" value="user" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('oa.notice.expireTime')" prop="expireTime">
              <el-date-picker
                v-model="form.expireTime"
                type="datetime"
                :placeholder="$t('oa.notice.placeholder.expireTime')"
                style="width:100%"
                value-format="yyyy-MM-dd HH:mm:ss"
                :disabled="isDetail"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="$t('common.status')" prop="status">
              <el-radio-group v-model="form.status" :disabled="isDetail">
                <el-radio :label="0">{{ $t('oa.notice.statusDraft') }}</el-radio>
                <el-radio :label="1">{{ $t('oa.notice.statusPublished') }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item>
              <el-checkbox v-model="form.top" :disabled="isDetail">{{ $t('oa.notice.top') }}</el-checkbox>
              <el-checkbox v-model="form.needConfirm" :disabled="isDetail">{{ $t('oa.notice.needConfirm') }}</el-checkbox>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item :label="$t('oa.notice.content')" prop="content">
              <editor v-if="!isDetail" v-model="form.content" :min-height="192" />
              <div v-else class="notice-content" v-html="form.content" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </template>
  </oa-form-page>
</template>

<script>
import OaFormPage from "@/views/oa/components/OaFormPage"
import { listCategory, getNotice, addNotice, updateNotice } from "@/api/oa/notice"

export default {
  name: "OaNoticeForm",
  components: {
    OaFormPage
  },
  data() {
    return {
      categoryList: [],
      form: {
        id: undefined,
        title: undefined,
        categoryId: undefined,
        content: undefined,
        scopeType: "all",
        scopeIds: undefined,
        needConfirm: false,
        top: false,
        status: 0,
        expireTime: undefined
      },
      rules: {
        title: [
          { required: true, message: this.$t('oa.notice.required.title'), trigger: "blur" }
        ],
        categoryId: [
          { required: true, message: this.$t('oa.notice.required.category'), trigger: "change" }
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
    this.getCategoryList()
    const id = this.$route.query.id
    if (id) {
      this.getNotice(id)
    }
  },
  methods: {
    getCategoryList() {
      listCategory({ pageNum: 1, pageSize: 1000, status: 1 }).then(response => {
        this.categoryList = response.rows
      })
    },
    getNotice(id) {
      getNotice(id).then(response => {
        this.form = response.data
        this.form.needConfirm = this.form.needConfirm === 1
        this.form.top = this.form.top === 1
      }).catch(() => {})
    },
    submitData() {
      const data = { ...this.form }
      data.needConfirm = data.needConfirm ? 1 : 0
      data.top = data.top ? 1 : 0
      if (data.id != undefined) {
        return updateNotice(data)
      }
      return addNotice(data).then(response => {
        if (response.data) {
          this.form.id = response.data
        }
      })
    },
    handleSave() {
      this.$refs["form"].validate(valid => {
        if (!valid) {
          return
        }
        this.submitData().then(() => {
          this.$modal.msgSuccess(this.form.id ? this.$t('common.editSuccess') : this.$t('common.addSuccess'))
        }).catch(() => {})
      })
    },
    handleSubmit() {
      this.$refs["form"].validate(valid => {
        if (!valid) {
          return
        }
        this.submitData().then(() => {
          this.$modal.msgSuccess(this.form.id ? this.$t('common.editSuccess') : this.$t('common.addSuccess'))
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
