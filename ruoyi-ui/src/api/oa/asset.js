import request from '@/utils/request'

// 资产分类
export function listCategory(query) {
  return request({ url: '/api/v1/oa/assets/categories/list', method: 'get', params: query })
}

export function getCategory(id) {
  return request({ url: '/api/v1/oa/assets/categories/' + id, method: 'get' })
}

export function addCategory(data) {
  return request({ url: '/api/v1/oa/assets/categories', method: 'post', data })
}

export function updateCategory(data) {
  return request({ url: '/api/v1/oa/assets/categories', method: 'put', data })
}

export function delCategory(id) {
  return request({ url: '/api/v1/oa/assets/categories/' + id, method: 'delete' })
}

// 资产台账
export function listAsset(query) {
  return request({ url: '/api/v1/oa/assets/list', method: 'get', params: query })
}

export function getAsset(id) {
  return request({ url: '/api/v1/oa/assets/' + id, method: 'get' })
}

export function addAsset(data) {
  return request({ url: '/api/v1/oa/assets', method: 'post', data })
}

export function updateAsset(data) {
  return request({ url: '/api/v1/oa/assets', method: 'put', data })
}

export function delAsset(id) {
  return request({ url: '/api/v1/oa/assets/' + id, method: 'delete' })
}

// 资产操作
export function receiveAsset(id, userId, userName) {
  return request({ url: '/api/v1/oa/assets/' + id + '/receive', method: 'post', params: { userId, userName } })
}

export function returnAsset(id, userId, userName) {
  return request({ url: '/api/v1/oa/assets/' + id + '/return', method: 'post', params: { userId, userName } })
}

export function transferAsset(id, fromUserId, fromUserName, toUserId, toUserName) {
  return request({ url: '/api/v1/oa/assets/' + id + '/transfer', method: 'post', params: { fromUserId, fromUserName, toUserId, toUserName } })
}

export function repairAsset(id, reason, cost, vendor) {
  return request({ url: '/api/v1/oa/assets/' + id + '/repair', method: 'post', params: { reason, cost, vendor } })
}

export function scrapAsset(id, reason, disposalMethod) {
  return request({ url: '/api/v1/oa/assets/' + id + '/scrap', method: 'post', params: { reason, disposalMethod } })
}

export function finishRepair(repairId) {
  return request({ url: '/api/v1/oa/assets/repairs/' + repairId + '/finish', method: 'post' })
}

export function completeAssetApproval(data) {
  return request({ url: '/api/v1/oa/assets/approvals/complete', method: 'post', data })
}

// 二维码与统计
export function qrcodeAsset(code) {
  return request({ url: '/api/v1/oa/assets/' + code + '/qrcode', method: 'get' })
}

export function statisticsAsset() {
  return request({ url: '/api/v1/oa/assets/statistics', method: 'get' })
}

// 盘点任务
export function listInventory(query) {
  return request({ url: '/api/v1/oa/assets/inventories/list', method: 'get', params: query })
}

export function getInventory(id) {
  return request({ url: '/api/v1/oa/assets/inventories/' + id, method: 'get' })
}

export function addInventory(data) {
  return request({ url: '/api/v1/oa/assets/inventories', method: 'post', data })
}

export function updateInventory(data) {
  return request({ url: '/api/v1/oa/assets/inventories', method: 'put', data })
}

export function delInventory(id) {
  return request({ url: '/api/v1/oa/assets/inventories/' + id, method: 'delete' })
}

export function generateInventory(id) {
  return request({ url: '/api/v1/oa/assets/inventories/' + id + '/generate', method: 'post' })
}

export function diffInventory(id) {
  return request({ url: '/api/v1/oa/assets/inventories/' + id + '/diff', method: 'get' })
}

export function listInventoryItems(id) {
  return request({ url: '/api/v1/oa/assets/inventories/' + id + '/items', method: 'get' })
}

export function updateInventoryItem(id, itemId, data) {
  return request({ url: '/api/v1/oa/assets/inventories/' + id + '/items/' + itemId, method: 'put', data })
}
