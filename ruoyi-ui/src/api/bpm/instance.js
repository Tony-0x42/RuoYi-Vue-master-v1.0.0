import request from '@/utils/request'

// 查询流程实例列表
export function listInstance(query) {
  return request({
    url: '/bpm/instance/list',
    method: 'get',
    params: query
  })
}

// 查询流程实例详细
export function getInstance(instanceId) {
  return request({
    url: '/bpm/instance/' + instanceId,
    method: 'get'
  })
}

// 新增流程实例
export function addInstance(data) {
  return request({
    url: '/bpm/instance',
    method: 'post',
    data: data
  })
}

// 修改流程实例
export function updateInstance(data) {
  return request({
    url: '/bpm/instance',
    method: 'put',
    data: data
  })
}

// 删除流程实例
export function delInstance(instanceId) {
  return request({
    url: '/bpm/instance/' + instanceId,
    method: 'delete'
  })
}
