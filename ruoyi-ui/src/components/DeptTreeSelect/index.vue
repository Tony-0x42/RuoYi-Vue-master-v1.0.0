<template>
  <treeselect
    :value="selectedIds"
    :multiple="true"
    :options="deptOptions"
    :normalizer="normalizer"
    :placeholder="placeholder"
    :clearable="true"
    :value-consists-of="valueConsistsOf"
    style="width: 100%"
    @input="handleInput"
  />
</template>

<script>
import { listDept } from '@/api/system/dept'
import { handleTree } from '@/utils/ruoyi'
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'

export default {
  name: 'DeptTreeSelect',
  components: { Treeselect },
  props: {
    value: {
      type: String,
      default: ''
    },
    placeholder: {
      type: String,
      default: '请选择部门'
    },
    valueConsistsOf: {
      type: String,
      default: 'ALL'
    }
  },
  data() {
    return {
      selectedIds: [],
      deptOptions: []
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
    this.loadDepts()
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
    loadDepts() {
      listDept().then(response => {
        this.deptOptions = handleTree(response.data || [], 'deptId')
      })
    },
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children
      }
      return {
        id: this.stringId(node.deptId),
        label: node.deptName,
        children: node.children
      }
    },
    handleInput(val) {
      const ids = Array.isArray(val) ? val : []
      this.selectedIds = ids
      const str = this.stringifyValue(ids)
      this.$emit('input', str)
      this.$emit('change', str)
    }
  }
}
</script>
