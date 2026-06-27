import request from '@/utils/request'

// 查询公告分类列表
export function listCategory(query) {
  return request({
    url: '/api/v1/oa/notices/categories/list',
    method: 'get',
    params: query
  })
}

// 查询公告分类详细
export function getCategory(id) {
  return request({
    url: '/api/v1/oa/notices/categories/' + id,
    method: 'get'
  })
}

// 新增公告分类
export function addCategory(data) {
  return request({
    url: '/api/v1/oa/notices/categories',
    method: 'post',
    data: data
  })
}

// 修改公告分类
export function updateCategory(data) {
  return request({
    url: '/api/v1/oa/notices/categories',
    method: 'put',
    data: data
  })
}

// 删除公告分类
export function delCategory(id) {
  return request({
    url: '/api/v1/oa/notices/categories/' + id,
    method: 'delete'
  })
}

// 管理端查询公告列表
export function listNotice(query) {
  return request({
    url: '/api/v1/oa/notices/list',
    method: 'get',
    params: query
  })
}

// 当前用户可见公告列表
export function listVisibleNotice(query) {
  return request({
    url: '/api/v1/oa/notices/visible/list',
    method: 'get',
    params: query
  })
}

// 查询公告详细
export function getNotice(id) {
  return request({
    url: '/api/v1/oa/notices/' + id,
    method: 'get'
  })
}

// 新增公告
export function addNotice(data) {
  return request({
    url: '/api/v1/oa/notices',
    method: 'post',
    data: data
  })
}

// 修改公告
export function updateNotice(data) {
  return request({
    url: '/api/v1/oa/notices',
    method: 'put',
    data: data
  })
}

// 删除公告
export function delNotice(id) {
  return request({
    url: '/api/v1/oa/notices/' + id,
    method: 'delete'
  })
}

// 下架公告
export function offlineNotice(id) {
  return request({
    url: '/api/v1/oa/notices/' + id + '/offline',
    method: 'post'
  })
}

// 阅读确认
export function confirmNotice(id) {
  return request({
    url: '/api/v1/oa/notices/' + id + '/confirm',
    method: 'post'
  })
}

// 阅读统计
export function noticeStats(id) {
  return request({
    url: '/api/v1/oa/notices/' + id + '/stats',
    method: 'get'
  })
}

// 已读用户列表
export function listReadUsers(id, query) {
  return request({
    url: '/api/v1/oa/notices/' + id + '/readUsers',
    method: 'get',
    params: query
  })
}

// 未读用户列表
export function listUnreadUsers(id, query) {
  return request({
    url: '/api/v1/oa/notices/' + id + '/unreadUsers',
    method: 'get',
    params: query
  })
}
