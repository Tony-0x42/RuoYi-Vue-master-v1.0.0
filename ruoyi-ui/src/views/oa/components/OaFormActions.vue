<template>
  <div class="form-actions" style="margin-top: 20px; text-align: center;">
    <el-button v-if="mode !== 'detail'" type="primary" plain icon="el-icon-check" @click="handleSave">保存</el-button>
    <el-button v-if="mode !== 'detail'" type="primary" icon="el-icon-s-promotion" @click="handleSubmit">提交</el-button>
    <el-button icon="el-icon-close" @click="handleClose">关闭</el-button>
  </div>
</template>

<script>
export default {
  name: 'OaFormActions',
  props: {
    mode: {
      type: String,
      default: 'add'
    }
  },
  methods: {
    handleSave() {
      this.$emit('save')
    },
    handleSubmit() {
      this.$emit('submit')
    },
    handleClose() {
      this.$emit('close')
    }
  }
}
</script>
