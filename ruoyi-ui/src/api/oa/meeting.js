import request from '@/utils/request'

// 查询会议室列表
export function listRoom(query) {
  return request({
    url: '/api/v1/oa/meetings/rooms/list',
    method: 'get',
    params: query
  })
}

// 查询可用会议室
export function listAvailableRoom(query) {
  return request({
    url: '/api/v1/oa/meetings/rooms/available',
    method: 'get',
    params: query
  })
}

// 查询会议室详细
export function getRoom(id) {
  return request({
    url: '/api/v1/oa/meetings/rooms/' + id,
    method: 'get'
  })
}

// 新增会议室
export function addRoom(data) {
  return request({
    url: '/api/v1/oa/meetings/rooms',
    method: 'post',
    data: data
  })
}

// 修改会议室
export function updateRoom(data) {
  return request({
    url: '/api/v1/oa/meetings/rooms',
    method: 'put',
    data: data
  })
}

// 删除会议室
export function delRoom(id) {
  return request({
    url: '/api/v1/oa/meetings/rooms/' + id,
    method: 'delete'
  })
}

// 查询会议列表
export function listMeeting(query) {
  return request({
    url: '/api/v1/oa/meetings/list',
    method: 'get',
    params: query
  })
}

// 查询会议占用
export function listOccupancy(query) {
  return request({
    url: '/api/v1/oa/meetings/occupancy',
    method: 'get',
    params: query
  })
}

// 查询会议详细
export function getMeeting(id) {
  return request({
    url: '/api/v1/oa/meetings/' + id,
    method: 'get'
  })
}

// 新增会议
export function addMeeting(data) {
  return request({
    url: '/api/v1/oa/meetings',
    method: 'post',
    data: data
  })
}

// 修改会议
export function updateMeeting(data) {
  return request({
    url: '/api/v1/oa/meetings',
    method: 'put',
    data: data
  })
}

// 取消会议
export function cancelMeeting(id) {
  return request({
    url: '/api/v1/oa/meetings/' + id + '/cancel',
    method: 'post'
  })
}

// 删除会议
export function delMeeting(id) {
  return request({
    url: '/api/v1/oa/meetings/' + id,
    method: 'delete'
  })
}

// 会议签到
export function signInMeeting(id) {
  return request({
    url: '/api/v1/oa/meetings/' + id + '/sign-in',
    method: 'post'
  })
}

// 查询会议纪要
export function getMeetingMinutes(id) {
  return request({
    url: '/api/v1/oa/meetings/' + id + '/minutes',
    method: 'get'
  })
}

// 保存会议纪要
export function saveMeetingMinutes(id, data) {
  return request({
    url: '/api/v1/oa/meetings/' + id + '/minutes',
    method: 'post',
    data: data
  })
}
