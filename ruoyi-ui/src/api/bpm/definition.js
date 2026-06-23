import request from '@/utils/request'

// 查询流程定义列表
export function listDefinition(query) {
  return request({
    url: '/bpm/definition/list',
    method: 'get',
    params: query
  })
}

// 查询流程定义详细
export function getDefinition(definitionId) {
  return request({
    url: '/bpm/definition/' + definitionId,
    method: 'get'
  })
}

// 新增流程定义
export function addDefinition(data) {
  return request({
    url: '/bpm/definition',
    method: 'post',
    data: data
  })
}

// 修改流程定义
export function updateDefinition(data) {
  return request({
    url: '/bpm/definition',
    method: 'put',
    data: data
  })
}

// 删除流程定义
export function delDefinition(definitionId) {
  return request({
    url: '/bpm/definition/' + definitionId,
    method: 'delete'
  })
}

// 发布流程定义
export function publishDefinition(definitionId) {
  return request({
    url: '/bpm/definition/publish/' + definitionId,
    method: 'get'
  })
}

// 停用流程定义
export function stopDefinition(definitionId) {
  return request({
    url: '/bpm/definition/stop/' + definitionId,
    method: 'get'
  })
}

// 保存 BPMN 模型
export function saveModel(definitionId, modelXml) {
  return request({
    url: '/bpm/definition/saveModel/' + definitionId,
    method: 'post',
    data: { modelXml }
  })
}

// 获取 BPMN 模型
export function getModelXml(definitionId) {
  return request({
    url: '/bpm/definition/modelXml/' + definitionId,
    method: 'get'
  })
}

// 部署流程定义
export function deployDefinition(definitionId) {
  return request({
    url: '/bpm/definition/deploy/' + definitionId,
    method: 'post'
  })
}
