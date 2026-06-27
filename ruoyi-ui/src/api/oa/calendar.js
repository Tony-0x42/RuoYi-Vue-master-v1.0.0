import request from '@/utils/request'

// 查询日程列表
export function listCalendarEvent(query) {
  return request({
    url: '/oa/calendar/events/list',
    method: 'get',
    params: query
  })
}

// 查询日程详细
export function getCalendarEvent(eventId) {
  return request({
    url: '/oa/calendar/events/' + eventId,
    method: 'get'
  })
}

// 新增日程
export function addCalendarEvent(data) {
  return request({
    url: '/oa/calendar/events',
    method: 'post',
    data: data
  })
}

// 修改日程
export function updateCalendarEvent(data) {
  return request({
    url: '/oa/calendar/events',
    method: 'put',
    data: data
  })
}

// 删除日程
export function delCalendarEvent(eventId) {
  return request({
    url: '/oa/calendar/events/' + eventId,
    method: 'delete'
  })
}
