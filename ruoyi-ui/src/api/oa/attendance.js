import request from '@/utils/request'

// 考勤打卡
export function checkIn(data) {
  return request({
    url: '/api/v1/oa/attendance/check-in',
    method: 'post',
    data: data
  })
}

// 查询个人打卡记录
export function listMyRecord(query) {
  return request({
    url: '/api/v1/oa/attendance/records',
    method: 'get',
    params: query
  })
}

// 考勤统计
export function attendanceStatistics() {
  return request({
    url: '/api/v1/oa/attendance/statistics',
    method: 'get'
  })
}

// 查询月度考勤
export function listMonthly(query) {
  return request({
    url: '/api/v1/oa/attendance/monthly',
    method: 'get',
    params: query
  })
}

// 查询假期余额
export function listLeaveBalance(query) {
  return request({
    url: '/api/v1/oa/attendance/leave-balance',
    method: 'get',
    params: query
  })
}

// 查询班次列表
export function listShift(query) {
  return request({
    url: '/api/v1/oa/attendance/shifts',
    method: 'get',
    params: query
  })
}

// 查询班次详细
export function getShift(id) {
  return request({
    url: '/api/v1/oa/attendance/shifts/' + id,
    method: 'get'
  })
}

// 新增班次
export function addShift(data) {
  return request({
    url: '/api/v1/oa/attendance/shifts',
    method: 'post',
    data: data
  })
}

// 修改班次
export function updateShift(data) {
  return request({
    url: '/api/v1/oa/attendance/shifts/' + data.id,
    method: 'put',
    data: data
  })
}

// 删除班次
export function delShift(ids) {
  return request({
    url: '/api/v1/oa/attendance/shifts/' + ids,
    method: 'delete'
  })
}

// 查询排班列表
export function listSchedule(query) {
  return request({
    url: '/api/v1/oa/attendance/schedules',
    method: 'get',
    params: query
  })
}

// 查询排班详细
export function getSchedule(id) {
  return request({
    url: '/api/v1/oa/attendance/schedules/' + id,
    method: 'get'
  })
}

// 新增排班
export function addSchedule(data) {
  return request({
    url: '/api/v1/oa/attendance/schedules',
    method: 'post',
    data: data
  })
}

// 修改排班
export function updateSchedule(data) {
  return request({
    url: '/api/v1/oa/attendance/schedules/' + data.id,
    method: 'put',
    data: data
  })
}

// 删除排班
export function delSchedule(ids) {
  return request({
    url: '/api/v1/oa/attendance/schedules/' + ids,
    method: 'delete'
  })
}

// 查询考勤组列表
export function listGroup(query) {
  return request({
    url: '/api/v1/oa/attendance/groups',
    method: 'get',
    params: query
  })
}

// 查询考勤组详细
export function getGroup(id) {
  return request({
    url: '/api/v1/oa/attendance/groups/' + id,
    method: 'get'
  })
}

// 新增考勤组
export function addGroup(data) {
  return request({
    url: '/api/v1/oa/attendance/groups',
    method: 'post',
    data: data
  })
}

// 修改考勤组
export function updateGroup(data) {
  return request({
    url: '/api/v1/oa/attendance/groups/' + data.id,
    method: 'put',
    data: data
  })
}

// 删除考勤组
export function delGroup(ids) {
  return request({
    url: '/api/v1/oa/attendance/groups/' + ids,
    method: 'delete'
  })
}

// 查询全部打卡记录（管理）
export function listRecord(query) {
  return request({
    url: '/api/v1/oa/attendance/records',
    method: 'get',
    params: query
  })
}

// 查询打卡记录详细
export function getRecord(id) {
  return request({
    url: '/api/v1/oa/attendance/records/' + id,
    method: 'get'
  })
}

// 新增打卡记录
export function addRecord(data) {
  return request({
    url: '/api/v1/oa/attendance/records',
    method: 'post',
    data: data
  })
}

// 修改打卡记录
export function updateRecord(data) {
  return request({
    url: '/api/v1/oa/attendance/records/' + data.id,
    method: 'put',
    data: data
  })
}

// 删除打卡记录
export function delRecord(ids) {
  return request({
    url: '/api/v1/oa/attendance/records/' + ids,
    method: 'delete'
  })
}
