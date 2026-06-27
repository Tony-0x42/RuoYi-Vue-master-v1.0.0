import request from '@/utils/request'

// 查询任务列表
export function listTask(query) {
  return request({
    url: '/api/v1/oa/tasks',
    method: 'get',
    params: query
  })
}

// 查询任务详细
export function getTask(id) {
  return request({
    url: '/api/v1/oa/tasks/' + id,
    method: 'get'
  })
}

// 新增任务
export function addTask(data) {
  return request({
    url: '/api/v1/oa/tasks',
    method: 'post',
    data: data
  })
}

// 修改任务
export function updateTask(data) {
  return request({
    url: '/api/v1/oa/tasks/' + data.id,
    method: 'put',
    data: data
  })
}

// 删除任务
export function delTask(ids) {
  return request({
    url: '/api/v1/oa/tasks/' + ids,
    method: 'delete'
  })
}

// 看板数据
export function boardTask(query) {
  return request({
    url: '/api/v1/oa/tasks/board',
    method: 'get',
    params: query
  })
}

// 甘特图数据
export function ganttTask(query) {
  return request({
    url: '/api/v1/oa/tasks/gantt',
    method: 'get',
    params: query
  })
}

// 新增评论
export function addComment(id, data) {
  return request({
    url: '/api/v1/oa/tasks/' + id + '/comments',
    method: 'post',
    data: data
  })
}

// 查询评论列表
export function listComment(id) {
  return request({
    url: '/api/v1/oa/tasks/' + id + '/comments',
    method: 'get'
  })
}

// 任务统计
export function statisticsTask() {
  return request({
    url: '/api/v1/oa/tasks/statistics',
    method: 'get'
  })
}
