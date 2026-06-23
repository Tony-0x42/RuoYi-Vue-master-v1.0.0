import request from '@/utils/request'

// 查询变量定义列表
export function listVariable(query) {
  return request({
    url: '/bpm/variable/list',
    method: 'get',
    params: query
  })
}

// 查询变量定义详细
export function getVariable(variableId) {
  return request({
    url: '/bpm/variable/' + variableId,
    method: 'get'
  })
}

// 新增变量定义
export function addVariable(data) {
  return request({
    url: '/bpm/variable',
    method: 'post',
    data: data
  })
}

// 修改变量定义
export function updateVariable(data) {
  return request({
    url: '/bpm/variable',
    method: 'put',
    data: data
  })
}

// 删除变量定义
export function delVariable(variableId) {
  return request({
    url: '/bpm/variable/' + variableId,
    method: 'delete'
  })
}
