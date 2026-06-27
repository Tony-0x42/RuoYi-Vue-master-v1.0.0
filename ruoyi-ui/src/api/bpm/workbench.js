import request from '@/utils/request'

// ==================== 组件管理 ====================

export function listComponent(query) {
  return request({
    url: '/bpm/workbench/component/list',
    method: 'get',
    params: query
  })
}

export function getComponent(componentId) {
  return request({
    url: '/bpm/workbench/component/' + componentId,
    method: 'get'
  })
}

export function addComponent(data) {
  return request({
    url: '/bpm/workbench/component',
    method: 'post',
    data: data
  })
}

export function updateComponent(data) {
  return request({
    url: '/bpm/workbench/component',
    method: 'put',
    data: data
  })
}

export function delComponent(componentIds) {
  return request({
    url: '/bpm/workbench/component/' + componentIds,
    method: 'delete'
  })
}

export function listAvailableComponents() {
  return request({
    url: '/bpm/workbench/component/available',
    method: 'get'
  })
}

// ==================== 模板管理 ====================

export function listTemplate(query) {
  return request({
    url: '/bpm/workbench/template/list',
    method: 'get',
    params: query
  })
}

export function getTemplate(templateId) {
  return request({
    url: '/bpm/workbench/template/' + templateId,
    method: 'get'
  })
}

export function addTemplate(data) {
  return request({
    url: '/bpm/workbench/template',
    method: 'post',
    data: data
  })
}

export function updateTemplate(data) {
  return request({
    url: '/bpm/workbench/template',
    method: 'put',
    data: data
  })
}

export function delTemplate(templateIds) {
  return request({
    url: '/bpm/workbench/template/' + templateIds,
    method: 'delete'
  })
}

export function listAvailableTemplates() {
  return request({
    url: '/bpm/workbench/template/available',
    method: 'get'
  })
}

export function getDefaultTemplate() {
  return request({
    url: '/bpm/workbench/template/default',
    method: 'get'
  })
}

// ==================== 用户配置 ====================

export function listUserConfig(query) {
  return request({
    url: '/bpm/workbench/config/list',
    method: 'get',
    params: query
  })
}

export function getUserConfig(configId) {
  return request({
    url: '/bpm/workbench/config/' + configId,
    method: 'get'
  })
}

export function addUserConfig(data) {
  return request({
    url: '/bpm/workbench/config',
    method: 'post',
    data: data
  })
}

export function updateUserConfig(data) {
  return request({
    url: '/bpm/workbench/config',
    method: 'put',
    data: data
  })
}

export function delUserConfig(configIds) {
  return request({
    url: '/bpm/workbench/config/' + configIds,
    method: 'delete'
  })
}

export function setDefaultUserConfig(configId) {
  return request({
    url: '/bpm/workbench/config/setDefault/' + configId,
    method: 'put'
  })
}

export function getDefaultUserConfig() {
  return request({
    url: '/bpm/workbench/config/default',
    method: 'get'
  })
}
