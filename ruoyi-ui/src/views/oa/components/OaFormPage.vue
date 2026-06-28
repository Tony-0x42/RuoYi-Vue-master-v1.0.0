<template>
  <div class="app-container">
    <div v-if="computedTitle" class="form-title">{{ computedTitle }}</div>
    <slot name="form" />
    <slot v-if="!$slots.form" />
    <oa-form-actions :mode="mode" @save="$emit('save')" @submit="$emit('submit')" @close="$emit('close')" />
  </div>
</template>

<script>
import OaFormActions from './OaFormActions'

export default {
  name: 'OaFormPage',
  components: { OaFormActions },
  props: {
    title: {
      type: String,
      default: ''
    },
    mode: {
      type: String,
      default: 'add'
    }
  },
  computed: {
    computedTitle() {
      if (this.title) {
        return this.title
      }
      const metaTitle = this.$route && this.$route.meta && this.$route.meta.title
      if (!metaTitle) {
        return ''
      }
      const prefix = this.mode === 'add' ? '新增' : this.mode === 'edit' ? '编辑' : this.mode === 'detail' ? '查看' : ''
      return prefix + metaTitle
    }
  }
}
</script>

<style scoped>
.form-title {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 1px solid #eee;
}
</style>
