<template>
  <div class="form-actions">
    <el-button v-if="mode !== 'detail'" type="primary" @click="$emit('submit')">提交</el-button>
    <el-button v-if="mode !== 'detail'" type="primary" plain @click="$emit('save')">保存</el-button>
    <el-button @click="$emit('close')">关闭</el-button>
  </div>
</template>

<script>
export default {
  name: "OaFormActions",
  props: {
    mode: {
      type: String,
      default: "add"
    }
  }
}
</script>

<style scoped>
.form-actions {
  margin-top: 20px;
  text-align: center;
}
</style>
