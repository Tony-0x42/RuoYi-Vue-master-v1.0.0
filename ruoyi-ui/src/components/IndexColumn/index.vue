<template>
  <el-table-column label="序号" align="center" width="50">
    <template slot-scope="scope">
      <span>{{ (page - 1) * size + scope.$index + 1 }}</span>
    </template>
  </el-table-column>
</template>

<script>
export default {
  name: 'IndexColumn',
  props: {
    page: {
      type: Number,
      default: 1
    },
    size: {
      type: Number,
      default: 10
    }
  }
}
</script>
