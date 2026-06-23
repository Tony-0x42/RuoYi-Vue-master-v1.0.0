import request from '@/utils/request'

// 发起流程
export function startProcess(definitionId, data) {
  return request({
    url: '/bpm/runtime/start/' + definitionId,
    method: 'post',
    data: data
  })
}

// 提交任务
export function submitTask(taskId, data) {
  return request({
    url: '/bpm/runtime/submit/' + taskId,
    method: 'post',
    data: data
  })
}

// 退回任务
export function returnTask(taskId, comment) {
  return request({
    url: '/bpm/runtime/return/' + taskId,
    method: 'post',
    params: { comment }
  })
}

// 拒绝任务
export function rejectTask(taskId, comment) {
  return request({
    url: '/bpm/runtime/reject/' + taskId,
    method: 'post',
    params: { comment }
  })
}

// 签收任务
export function claimTask(taskId) {
  return request({
    url: '/bpm/runtime/claim/' + taskId,
    method: 'get'
  })
}

// 指派任务
export function assignTask(taskId, userId) {
  return request({
    url: '/bpm/runtime/assign/' + taskId,
    method: 'post',
    params: { userId }
  })
}

// 特送任务（委派）
export function delegateTask(taskId, userId) {
  return request({
    url: '/bpm/runtime/delegate/' + taskId,
    method: 'post',
    params: { userId }
  })
}

// 获取流程跟踪图 XML
export function getTraceXml(processInstanceId) {
  return request({
    url: '/bpm/runtime/trace/' + processInstanceId,
    method: 'get'
  })
}
