import request from '@/utils/request'

// 查询知识分类列表
export function listCategory(query) {
  return request({
    url: '/api/v1/oa/kb/categories/list',
    method: 'get',
    params: query
  })
}

// 查询知识分类详细
export function getCategory(id) {
  return request({
    url: '/api/v1/oa/kb/categories/' + id,
    method: 'get'
  })
}

// 新增知识分类
export function addCategory(data) {
  return request({
    url: '/api/v1/oa/kb/categories',
    method: 'post',
    data: data
  })
}

// 修改知识分类
export function updateCategory(data) {
  return request({
    url: '/api/v1/oa/kb/categories',
    method: 'put',
    data: data
  })
}

// 删除知识分类
export function delCategory(id) {
  return request({
    url: '/api/v1/oa/kb/categories/' + id,
    method: 'delete'
  })
}

// 查询知识词条列表
export function listArticle(query) {
  return request({
    url: '/api/v1/oa/kb/articles/list',
    method: 'get',
    params: query
  })
}

// 搜索知识词条
export function searchArticle(query) {
  return request({
    url: '/api/v1/oa/kb/articles/search',
    method: 'get',
    params: query
  })
}

// 查询推荐知识
export function listRecommend() {
  return request({
    url: '/api/v1/oa/kb/articles/recommendations',
    method: 'get'
  })
}

// 查询知识词条详细
export function getArticle(id) {
  return request({
    url: '/api/v1/oa/kb/articles/' + id,
    method: 'get'
  })
}

// 新增知识词条
export function addArticle(data) {
  return request({
    url: '/api/v1/oa/kb/articles',
    method: 'post',
    data: data
  })
}

// 修改知识词条
export function updateArticle(data) {
  return request({
    url: '/api/v1/oa/kb/articles',
    method: 'put',
    data: data
  })
}

// 删除知识词条
export function delArticle(id) {
  return request({
    url: '/api/v1/oa/kb/articles/' + id,
    method: 'delete'
  })
}

// 上架/下架知识词条
export function updateArticleStatus(id, status) {
  return request({
    url: '/api/v1/oa/kb/articles/' + id + '/status',
    method: 'post',
    params: { status }
  })
}

// 收藏/取消收藏
export function toggleFavorite(id) {
  return request({
    url: '/api/v1/oa/kb/articles/' + id + '/favorite',
    method: 'post'
  })
}

// 查询评论列表
export function listComments(id) {
  return request({
    url: '/api/v1/oa/kb/articles/' + id + '/comments',
    method: 'get'
  })
}

// 发表评论
export function addComment(id, content) {
  return request({
    url: '/api/v1/oa/kb/articles/' + id + '/comments',
    method: 'post',
    data: { content }
  })
}

// 阅读统计
export function articleStats(id) {
  return request({
    url: '/api/v1/oa/kb/articles/' + id + '/stats',
    method: 'get'
  })
}
