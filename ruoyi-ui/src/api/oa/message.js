import request from '@/utils/request'

// 查询消息列表
export function listMessage(query) {
  return request({
    url: '/api/v1/oa/messages/list',
    method: 'get',
    params: query
  })
}

// 发送消息
export function sendMessage(data) {
  return request({
    url: '/api/v1/oa/messages/send',
    method: 'post',
    data: data
  })
}

// 标记已读
export function readMessage(data) {
  return request({
    url: '/api/v1/oa/messages/read',
    method: 'post',
    data: data
  })
}

// 删除消息
export function delMessage(ids) {
  return request({
    url: '/api/v1/oa/messages/' + ids,
    method: 'delete'
  })
}

// 查询发送日志列表
export function listMessageLog(query) {
  return request({
    url: '/api/v1/oa/messages/logs',
    method: 'get',
    params: query
  })
}

// 重试发送
export function retryMessage(id) {
  return request({
    url: '/api/v1/oa/messages/' + id + '/retry',
    method: 'post'
  })
}

// 查询消息模板列表
export function listTemplate(query) {
  return request({
    url: '/api/v1/oa/messageTemplates/list',
    method: 'get',
    params: query
  })
}

// 查询消息模板详细
export function getTemplate(id) {
  return request({
    url: '/api/v1/oa/messageTemplates/' + id,
    method: 'get'
  })
}

// 新增消息模板
export function addTemplate(data) {
  return request({
    url: '/api/v1/oa/messageTemplates',
    method: 'post',
    data: data
  })
}

// 修改消息模板
export function updateTemplate(data) {
  return request({
    url: '/api/v1/oa/messageTemplates',
    method: 'put',
    data: data
  })
}

// 删除消息模板
export function delTemplate(ids) {
  return request({
    url: '/api/v1/oa/messageTemplates/' + ids,
    method: 'delete'
  })
}
