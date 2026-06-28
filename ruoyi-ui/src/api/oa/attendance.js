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

// 查询请假申请列表
export function listLeave(query) {
  return request({
    url: '/api/v1/oa/attendance/leaves',
    method: 'get',
    params: query
  })
}

// 查询请假申请详细
export function getLeave(id) {
  return request({
    url: '/api/v1/oa/attendance/leaves/' + id,
    method: 'get'
  })
}

// 新增请假申请
export function addLeave(data) {
  return request({
    url: '/api/v1/oa/attendance/leaves',
    method: 'post',
    data: data
  })
}

// 修改请假申请
export function updateLeave(data) {
  return request({
    url: '/api/v1/oa/attendance/leaves/' + data.id,
    method: 'put',
    data: data
  })
}

// 删除请假申请
export function delLeave(ids) {
  return request({
    url: '/api/v1/oa/attendance/leaves/' + ids,
    method: 'delete'
  })
}

// 提交请假申请
export function submitLeave(id, data) {
  return request({
    url: '/api/v1/oa/attendance/leaves/' + id + '/submit',
    method: 'post',
    data: data
  })
}

// 查询加班申请列表
export function listOvertime(query) {
  return request({
    url: '/api/v1/oa/attendance/overtimes',
    method: 'get',
    params: query
  })
}

// 查询加班申请详细
export function getOvertime(id) {
  return request({
    url: '/api/v1/oa/attendance/overtimes/' + id,
    method: 'get'
  })
}

// 新增加班申请
export function addOvertime(data) {
  return request({
    url: '/api/v1/oa/attendance/overtimes',
    method: 'post',
    data: data
  })
}

// 修改加班申请
export function updateOvertime(data) {
  return request({
    url: '/api/v1/oa/attendance/overtimes/' + data.id,
    method: 'put',
    data: data
  })
}

// 删除加班申请
export function delOvertime(ids) {
  return request({
    url: '/api/v1/oa/attendance/overtimes/' + ids,
    method: 'delete'
  })
}

// 提交加班申请
export function submitOvertime(id, data) {
  return request({
    url: '/api/v1/oa/attendance/overtimes/' + id + '/submit',
    method: 'post',
    data: data
  })
}

// 查询出差申请列表
export function listTrip(query) {
  return request({
    url: '/api/v1/oa/attendance/trips',
    method: 'get',
    params: query
  })
}

// 查询出差申请详细
export function getTrip(id) {
  return request({
    url: '/api/v1/oa/attendance/trips/' + id,
    method: 'get'
  })
}

// 新增出差申请
export function addTrip(data) {
  return request({
    url: '/api/v1/oa/attendance/trips',
    method: 'post',
    data: data
  })
}

// 修改出差申请
export function updateTrip(data) {
  return request({
    url: '/api/v1/oa/attendance/trips/' + data.id,
    method: 'put',
    data: data
  })
}

// 删除出差申请
export function delTrip(ids) {
  return request({
    url: '/api/v1/oa/attendance/trips/' + ids,
    method: 'delete'
  })
}

// 提交出差申请
export function submitTrip(id, data) {
  return request({
    url: '/api/v1/oa/attendance/trips/' + id + '/submit',
    method: 'post',
    data: data
  })
}

// 查询补卡申请列表
export function listMakeup(query) {
  return request({
    url: '/api/v1/oa/attendance/makeups',
    method: 'get',
    params: query
  })
}

// 查询补卡申请详细
export function getMakeup(id) {
  return request({
    url: '/api/v1/oa/attendance/makeups/' + id,
    method: 'get'
  })
}

// 新增补卡申请
export function addMakeup(data) {
  return request({
    url: '/api/v1/oa/attendance/makeups',
    method: 'post',
    data: data
  })
}

// 修改补卡申请
export function updateMakeup(data) {
  return request({
    url: '/api/v1/oa/attendance/makeups/' + data.id,
    method: 'put',
    data: data
  })
}

// 删除补卡申请
export function delMakeup(ids) {
  return request({
    url: '/api/v1/oa/attendance/makeups/' + ids,
    method: 'delete'
  })
}

// 提交补卡申请
export function submitMakeup(id, data) {
  return request({
    url: '/api/v1/oa/attendance/makeups/' + id + '/submit',
    method: 'post',
    data: data
  })
}

// 考勤审批完成回调
export function completeAttendanceApproval(data) {
  return request({
    url: '/api/v1/oa/attendance/approvals/complete',
    method: 'post',
    data: data
  })
}
