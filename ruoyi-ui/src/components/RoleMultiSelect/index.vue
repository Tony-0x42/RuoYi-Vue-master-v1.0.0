<template>
  <el-select
    v-model="selectedIds"
    :placeholder="placeholder"
    multiple
    filterable
    clearable
    collapse-tags
    style="width: 100%"
    @change="handleChange"
  >
    <el-option
      v-for="item in roleOptions"
      :key="stringId(item.roleId)"
      :label="item.roleName"
      :value="stringId(item.roleId)"
    />
  </el-select>
</template>

<script>
import { listRole } from '@/api/system/role'

export default {
  name: 'RoleMultiSelect',
  props: {
    value: {
      type: String,
      default: ''
    },
    placeholder: {
      type: String,
      default: '请选择角色'
    }
  },
  data() {
    return {
      selectedIds: [],
      roleOptions: []
    }
  },
  watch: {
    value: {
      immediate: true,
      handler(val) {
        const ids = this.parseValue(val)
        if (this.stringifyValue(ids) !== this.stringifyValue(this.selectedIds)) {
          this.selectedIds = ids
        }
      }
    }
  },
  mounted() {
    this.loadRoles()
  },
  methods: {
    stringId(id) {
      return id == null ? '' : String(id)
    },
    parseValue(val) {
      if (!val || typeof val !== 'string') {
        return []
      }
      return val.split(',').map(v => v.trim()).filter(v => v !== '')
    },
    stringifyValue(ids) {
      if (!Array.isArray(ids)) {
        return ''
      }
      return ids.map(this.stringId).join(',')
    },
    loadRoles() {
      listRole({ pageSize: 1000 }).then(response => {
        this.roleOptions = response.rows || []
      })
    },
    handleChange(val) {
      const str = this.stringifyValue(val)
      this.$emit('input', str)
      this.$emit('change', str)
    }
  }
}
</script>
