<template>
  <el-select
    v-model="selectedId"
    :placeholder="placeholder"
    clearable
    filterable
    remote
    reserve-keyword
    :remote-method="remoteSearch"
    :loading="loading"
    size="small"
    @change="handleChange"
  >
    <el-option
      v-for="item in userOptions"
      :key="stringId(item.userId)"
      :label="formatLabel(item)"
      :value="stringId(item.userId)"
    />
  </el-select>
</template>

<script>
import { listUser, getUser } from '@/api/system/user'

export default {
  name: 'UserSelect',
  props: {
    value: {
      type: String,
      default: ''
    },
    placeholder: {
      type: String,
      default: '请选择用户'
    }
  },
  data() {
    return {
      selectedId: '',
      userOptions: [],
      loading: false
    }
  },
  watch: {
    value: {
      immediate: true,
      handler(val) {
        const id = val || ''
        if (this.selectedId !== id) {
          this.selectedId = id
        }
        if (id) {
          const exists = this.userOptions.some(item => this.stringId(item.userId) === id)
          if (!exists) {
            this.loadSelectedUser(id)
          }
        } else {
          this.userOptions = []
        }
      }
    }
  },
  methods: {
    stringId(id) {
      return id == null ? '' : String(id)
    },
    formatLabel(user) {
      if (!user) {
        return ''
      }
      const nickName = user.nickName || ''
      const userName = user.userName || ''
      return nickName ? `${nickName} (${userName})` : userName
    },
    remoteSearch(query) {
      const keyword = query ? query.trim() : ''
      if (!keyword) {
        this.userOptions = []
        return
      }
      this.loading = true
      listUser({
        userName: keyword,
        pageNum: 1,
        pageSize: 20
      }).then(response => {
        this.userOptions = response.rows || []
      }).finally(() => {
        this.loading = false
      })
    },
    loadSelectedUser(userId) {
      this.loading = true
      getUser(userId).then(response => {
        const user = response.data
        if (user) {
          this.userOptions = [user]
        }
      }).finally(() => {
        this.loading = false
      })
    },
    handleChange(val) {
      this.$emit('input', val || '')
      this.$emit('change', val || '')
    }
  }
}
</script>
