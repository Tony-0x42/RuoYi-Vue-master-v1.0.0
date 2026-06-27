import request from '@/utils/request'

export function previewStart(data) {
  return request({ url: '/api/v1/process/preview/start', method: 'post', data })
}

export function previewNext(data) {
  return request({ url: '/api/v1/process/preview/next', method: 'post', data })
}

export function getReturnTarget(taskId) {
  return request({ url: '/api/v1/process/preview/return-target', method: 'post', data: { taskId } })
}

export function startProcess(data) {
  return request({ url: '/api/v1/process/instances/start', method: 'post', data })
}

export function completeTask(taskId, data) {
  return request({ url: '/api/v1/process/tasks/' + taskId + '/complete', method: 'post', data })
}

export function returnToPrevious(taskId, data) {
  return request({ url: '/api/v1/process/tasks/' + taskId + '/returnToPrevious', method: 'post', data })
}
