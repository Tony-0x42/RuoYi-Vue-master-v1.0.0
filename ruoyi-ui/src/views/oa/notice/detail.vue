<template>
  <el-dialog :title="$t('oa.notice.detail')" :visible.sync="open" width="800px" append-to-body>
    <div v-loading="loading" class="notice-detail">
      <h2 class="notice-title">
        <el-tag v-if="notice.top === 1" size="small" type="danger">{{ $t('oa.notice.top') }}</el-tag>
        {{ notice.title }}
      </h2>
      <div class="notice-meta">
        <span>{{ $t('oa.notice.category') }}: {{ notice.categoryName || '-' }}</span>
        <span>{{ $t('oa.notice.publisher') }}: {{ notice.createBy || '-' }}</span>
        <span>{{ $t('common.createTime') }}: {{ parseTime(notice.createTime) }}</span>
      </div>
      <div class="notice-content" v-html="notice.content"></div>
      <div v-if="notice.attachments &amp;&amp; notice.attachments.length > 0" class="notice-attachments">
        <h4>{{ $t('oa.notice.attachment') }}</h4>
        <ul>
          <li v-for="file in notice.attachments" :key="file.id">
            <a :href="file.filePath" target="_blank">{{ file.fileName }}</a>
            <span v-if="file.fileSize">({{ formatSize(file.fileSize) }})</span>
          </li>
        </ul>
      </div>
    </div>
    <div slot="footer" class="dialog-footer">
      <el-button v-if="notice.needConfirm === 1 &amp;&amp; !notice.isConfirmed" type="primary" @click="handleConfirm">
        {{ $t('oa.notice.confirmRead') }}
      </el-button>
      <el-button @click="open = false">{{ $t('oa.notice.close') }}</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getNotice, confirmNotice } from "@/api/oa/notice"

export default {
  name: "NoticeDetail",
  props: {
    categoryList: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      open: false,
      loading: false,
      notice: {}
    }
  },
  methods: {
    open(id) {
      this.open = true
      this.loading = true
      getNotice(id).then(response => {
        this.notice = response.data || {}
        this.loading = false
      })
    },
    handleConfirm() {
      confirmNotice(this.notice.id).then(() => {
        this.$modal.msgSuccess(this.$t('oa.notice.confirmSuccess'))
        this.notice.isConfirmed = true
        this.notice.isRead = true
      })
    },
    formatSize(bytes) {
      if (!bytes) return '0 B'
      const k = 1024
      const sizes = ['B', 'KB', 'MB', 'GB']
      const i = Math.floor(Math.log(bytes) / Math.log(k))
      return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
    }
  }
}
</script>

<style scoped>
.notice-detail {
  max-height: 60vh;
  overflow-y: auto;
}
.notice-title {
  text-align: center;
  margin-bottom: 10px;
}
.notice-meta {
  text-align: center;
  color: #909399;
  font-size: 13px;
  margin-bottom: 20px;
}
.notice-meta span {
  margin: 0 10px;
}
.notice-content {
  min-height: 100px;
  line-height: 1.8;
}
.notice-attachments {
  margin-top: 20px;
  padding-top: 15px;
  border-top: 1px solid #ebeef5;
}
</style>
