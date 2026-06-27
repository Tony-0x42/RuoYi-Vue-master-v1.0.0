import request from '@/utils/request'

// 获取工作台首页数据
export function getHome() {
  return request({
    url: '/api/v1/oa/portal/home',
    method: 'get'
  })
}

// 保存布局配置
export function saveLayout(data) {
  return request({
    url: '/api/v1/oa/portal/layout',
    method: 'post',
    data: data
  })
}

// 获取当前用户布局
export function getLayout() {
  return request({
    url: '/api/v1/oa/portal/layout',
    method: 'get'
  })
}

// 获取待办聚合
export function listTodos(query) {
  return request({
    url: '/api/v1/oa/portal/todos',
    method: 'get',
    params: query
  })
}

// 获取消息提醒
export function listMessages(query) {
  return request({
    url: '/api/v1/oa/portal/messages',
    method: 'get',
    params: query
  })
}

// 获取应用中心
export function listApps(query) {
  return request({
    url: '/api/v1/oa/portal/apps',
    method: 'get',
    params: query
  })
}

// 收藏/取消应用
export function toggleFavorite(data) {
  return request({
    url: '/api/v1/oa/portal/apps/favorite',
    method: 'post',
    data: data
  })
}

// 获取数据看板
export function getDashboard(query) {
  return request({
    url: '/api/v1/oa/portal/dashboard',
    method: 'get',
    params: query
  })
}

// 查询门户布局列表
export function listLayouts(query) {
  return request({
    url: '/api/v1/oa/portal/layouts/list',
    method: 'get',
    params: query
  })
}

// 查询门户组件列表
export function listWidgets(query) {
  return request({
    url: '/api/v1/oa/portal/widgets/list',
    method: 'get',
    params: query
  })
}

// 新增门户组件
export function addWidget(data) {
  return request({
    url: '/api/v1/oa/portal/widgets',
    method: 'post',
    data: data
  })
}

// 修改门户组件
export function updateWidget(data) {
  return request({
    url: '/api/v1/oa/portal/widgets',
    method: 'put',
    data: data
  })
}

// 删除门户组件
export function delWidget(id) {
  return request({
    url: '/api/v1/oa/portal/widgets/' + id,
    method: 'delete'
  })
}
