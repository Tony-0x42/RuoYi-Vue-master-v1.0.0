<template>
  <div class="app-container">
    <el-page-header :content="article.title || $t('oa.knowledgebase.detail')" @back="goBack" />
    <div v-loading="loading" class="kb-detail">
      <h2 class="kb-title">
        <el-tag v-if="article.top === 1" size="small" type="danger">{{ $t('oa.knowledgebase.top') }}</el-tag>
        {{ article.title }}
      </h2>
      <div class="kb-meta">
        <span>{{ $t('oa.knowledgebase.category') }}: {{ article.categoryName || '-' }}</span>
        <span>{{ $t('oa.knowledgebase.author') }}: {{ article.author || article.createBy || '-' }}</span>
        <span>{{ $t('common.createTime') }}: {{ parseTime(article.createTime) }}</span>
      </div>
      <div class="kb-tags" v-if="article.tags">
        <el-tag v-for="tag in tagList" :key="tag" size="small" style="margin-right:5px">{{ tag }}</el-tag>
      </div>
      <div class="kb-summary" v-if="article.summary">
        <strong>{{ $t('oa.knowledgebase.summary') }}:</strong> {{ article.summary }}
      </div>
      <div class="kb-content" v-html="article.content"></div>
      <div class="kb-actions">
        <el-button :type="article.favorite ? 'warning' : 'default'" size="small" icon="el-icon-star-off" @click="handleFavorite">
          {{ article.favorite ? $t('oa.knowledgebase.favorited') : $t('oa.knowledgebase.favorite') }}
        </el-button>
      </div>
      <div class="kb-stats">
        <span>{{ $t('oa.knowledgebase.viewCount') }}: {{ stats.viewCount || 0 }}</span>
        <span>{{ $t('oa.knowledgebase.favoriteCount') }}: {{ stats.favoriteCount || 0 }}</span>
        <span>{{ $t('oa.knowledgebase.commentCount') }}: {{ stats.commentCount || 0 }}</span>
      </div>
      <div class="kb-comment">
        <h4>{{ $t('oa.knowledgebase.comment') }}</h4>
        <el-input
          type="textarea"
          :rows="3"
          v-model="commentContent"
          :placeholder="$t('oa.knowledgebase.commentPlaceholder')"
          maxlength="500"
          show-word-limit
        />
        <div style="margin-top:10px;text-align:right">
          <el-button type="primary" size="small" @click="handleComment">{{ $t('common.submit') }}</el-button>
        </div>
        <el-divider />
        <div v-for="item in comments" :key="item.id" class="comment-item">
          <div class="comment-header">
            <span class="comment-author">{{ item.createBy || item.userId }}</span>
            <span class="comment-time">{{ parseTime(item.createTime) }}</span>
          </div>
          <div class="comment-content">{{ item.content }}</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getArticle, articleStats, listComments, addComment, toggleFavorite } from "@/api/oa/knowledgebase"

export default {
  name: "OaKnowledgebaseDetail",
  data() {
    return {
      loading: false,
      article: {},
      stats: {},
      comments: [],
      commentContent: ""
    }
  },
  computed: {
    tagList() {
      return this.article.tags ? this.article.tags.split(',').filter(t => t) : []
    }
  },
  created() {
    this.loadData()
  },
  methods: {
    loadData() {
      const id = this.$route.params.id
      if (!id) {
        this.goBack()
        return
      }
      this.loading = true
      getArticle(id).then(response => {
        this.article = response.data || {}
        this.loading = false
      })
      articleStats(id).then(response => {
        this.stats = response.data || {}
      })
      listComments(id).then(response => {
        this.comments = response.rows || []
      })
    },
    goBack() {
      this.$router.push('/oa/knowledgebase')
    },
    handleFavorite() {
      toggleFavorite(this.article.id).then(response => {
        this.article.favorite = response.data
        this.$modal.msgSuccess(this.$t('common.success'))
        articleStats(this.article.id).then(res => {
          this.stats = res.data || {}
        })
      })
    },
    handleComment() {
      if (!this.commentContent || !this.commentContent.trim()) {
        this.$modal.msgError(this.$t('oa.knowledgebase.commentPlaceholder'))
        return
      }
      addComment(this.article.id, this.commentContent.trim()).then(() => {
        this.$modal.msgSuccess(this.$t('common.success'))
        this.commentContent = ""
        listComments(this.article.id).then(response => {
          this.comments = response.rows || []
        })
        articleStats(this.article.id).then(res => {
          this.stats = res.data || {}
        })
      })
    }
  }
}
</script>

<style scoped>
.kb-detail {
  margin-top: 20px;
}
.kb-title {
  text-align: center;
  margin-bottom: 10px;
}
.kb-meta {
  text-align: center;
  color: #909399;
  font-size: 13px;
  margin-bottom: 15px;
}
.kb-meta span {
  margin: 0 10px;
}
.kb-tags {
  text-align: center;
  margin-bottom: 15px;
}
.kb-summary {
  background: #f5f7fa;
  padding: 15px;
  border-radius: 4px;
  margin-bottom: 20px;
}
.kb-content {
  min-height: 150px;
  line-height: 1.8;
  margin-bottom: 20px;
}
.kb-actions {
  margin-bottom: 15px;
}
.kb-stats {
  color: #909399;
  font-size: 13px;
  margin-bottom: 20px;
}
.kb-stats span {
  margin-right: 20px;
}
.kb-comment {
  margin-top: 20px;
}
.comment-item {
  margin-bottom: 15px;
}
.comment-header {
  color: #606266;
  font-size: 13px;
  margin-bottom: 5px;
}
.comment-author {
  font-weight: bold;
  margin-right: 10px;
}
.comment-time {
  color: #909399;
}
.comment-content {
  color: #303133;
  line-height: 1.6;
}
</style>
