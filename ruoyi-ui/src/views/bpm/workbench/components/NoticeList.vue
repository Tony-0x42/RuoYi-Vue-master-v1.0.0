<template>
  <div class="app-container" style="padding: 0; background: transparent;">
    <div v-loading="loading" class="notice-list">
      <div
        v-for="item in noticeList"
        :key="item.noticeId"
        class="notice-item"
        :class="{ 'is-read': item.isRead }"
        @click="previewNotice(item)"
      >
        <div class="notice-main">
          <el-tag size="mini" :type="item.noticeType === '1' ? 'warning' : 'success'">
            {{ item.noticeType === '1' ? '通知' : '公告' }}
          </el-tag>
          <span class="notice-title-text">{{ item.noticeTitle }}</span>
          <span v-if="!item.isRead" class="notice-unread-dot"></span>
        </div>
        <div class="notice-meta-line">
          <span>{{ item.createBy || '系统' }}</span>
          <span>{{ item.createTime }}</span>
        </div>
      </div>
      <div v-if="!loading && noticeList.length === 0" class="empty-block">
        <i class="el-icon-document"></i>
        <div>暂无公告</div>
      </div>
    </div>
    <notice-detail-view ref="noticeViewRef" />
  </div>
</template>

<script>
import { listNoticeTop, markNoticeRead } from '@/api/system/notice'
import NoticeDetailView from '@/layout/components/HeaderNotice/DetailView'

export default {
  name: 'NoticeList',
  components: { NoticeDetailView },
  data() {
    return {
      loading: true,
      noticeList: []
    }
  },
  created() {
    this.loadList()
  },
  methods: {
    loadList() {
      this.loading = true
      listNoticeTop().then(response => {
        this.noticeList = response.data || []
      }).finally(() => {
        this.loading = false
      })
    },
    previewNotice(item) {
      if (!item.isRead) {
        markNoticeRead(item.noticeId).catch(() => {})
        item.isRead = true
        this.$set(this.noticeList, this.noticeList.indexOf(item), { ...item, isRead: true })
      }
      this.$refs.noticeViewRef.open(item.noticeId)
    }
  }
}
</script>

<style scoped lang="scss">
.notice-list {
  .notice-item {
    padding: 12px 0;
    border-bottom: 1px solid #f5f5f5;
    cursor: pointer;
    &:last-child { border-bottom: none; }
    &:hover { background: #f7f9fb; border-radius: 4px; }
    &.is-read {
      .notice-title-text { color: #999; }
      .el-tag { opacity: 0.55; }
    }
  }
  .notice-main {
    display: flex;
    align-items: center;
    gap: 8px;
    margin-bottom: 6px;
  }
  .notice-title-text {
    flex: 1;
    font-size: 14px;
    color: #303133;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
  }
  .notice-unread-dot {
    width: 6px;
    height: 6px;
    border-radius: 50%;
    background: #f56c6c;
    flex-shrink: 0;
  }
  .notice-meta-line {
    display: flex;
    justify-content: space-between;
    font-size: 12px;
    color: #909399;
    padding-left: 46px;
  }
}
.empty-block {
  text-align: center;
  padding: 30px 0;
  color: #c0c4cc;
  font-size: 13px;
  i {
    font-size: 32px;
    display: block;
    margin-bottom: 8px;
  }
}
</style>
