import request from '@/utils/request'

// 查询待办任务列表
export function todoList(query) {
  return request({
    url: '/bpm/task/todoList',
    method: 'get',
    params: query
  })
}

// 查询已办任务列表
export function doneList(query) {
  return request({
    url: '/bpm/task/doneList',
    method: 'get',
    params: query
  })
}
