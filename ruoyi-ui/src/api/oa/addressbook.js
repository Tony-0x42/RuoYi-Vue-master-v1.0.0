import request from '@/utils/request'

// 获取组织架构树
export function getDeptTree(query) {
  return request({
    url: '/api/v1/oa/address-book/dept-tree',
    method: 'get',
    params: query
  })
}

// 获取部门下人员
export function getDeptUsers(deptId) {
  return request({
    url: '/api/v1/oa/address-book/dept/' + deptId + '/users',
    method: 'get'
  })
}

// 搜索人员
export function searchUsers(query) {
  return request({
    url: '/api/v1/oa/address-book/search',
    method: 'get',
    params: query
  })
}

// 获取人员详情
export function getUserDetail(userId) {
  return request({
    url: '/api/v1/oa/address-book/users/' + userId,
    method: 'get'
  })
}

// 获取常用联系人
export function listContacts() {
  return request({
    url: '/api/v1/oa/address-book/contacts',
    method: 'get'
  })
}

// 添加常用联系人
export function addContact(data) {
  return request({
    url: '/api/v1/oa/address-book/contacts',
    method: 'post',
    data: data
  })
}

// 删除常用联系人
export function delContact(contactId) {
  return request({
    url: '/api/v1/oa/address-book/contacts/' + contactId,
    method: 'delete'
  })
}

// 获取联系人分组
export function listContactGroups() {
  return request({
    url: '/api/v1/oa/address-book/contact-groups',
    method: 'get'
  })
}

// 新增联系人分组
export function addContactGroup(data) {
  return request({
    url: '/api/v1/oa/address-book/contact-groups',
    method: 'post',
    data: data
  })
}

// 修改联系人分组
export function updateContactGroup(data) {
  return request({
    url: '/api/v1/oa/address-book/contact-groups',
    method: 'put',
    data: data
  })
}

// 删除联系人分组
export function delContactGroup(groupId) {
  return request({
    url: '/api/v1/oa/address-book/contact-groups/' + groupId,
    method: 'delete'
  })
}

// 获取最近联系人
export function listRecentContacts() {
  return request({
    url: '/api/v1/oa/address-book/recent-contacts',
    method: 'get'
  })
}
