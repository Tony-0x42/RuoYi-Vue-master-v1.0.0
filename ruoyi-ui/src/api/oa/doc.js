import request from '@/utils/request'

// 查询文件夹列表
export function listFolder(query) {
  return request({
    url: '/api/v1/oa/docs/folders/list',
    method: 'get',
    params: query
  })
}

// 查询文件夹树
export function treeFolder() {
  return request({
    url: '/api/v1/oa/docs/folders/tree',
    method: 'get'
  })
}

// 查询文件夹详细
export function getFolder(id) {
  return request({
    url: '/api/v1/oa/docs/folders/' + id,
    method: 'get'
  })
}

// 新增文件夹
export function addFolder(data) {
  return request({
    url: '/api/v1/oa/docs/folders',
    method: 'post',
    data: data
  })
}

// 修改文件夹
export function updateFolder(data) {
  return request({
    url: '/api/v1/oa/docs/folders',
    method: 'put',
    data: data
  })
}

// 删除文件夹
export function delFolder(id) {
  return request({
    url: '/api/v1/oa/docs/folders/' + id,
    method: 'delete'
  })
}

// 查询文件列表
export function listFile(query) {
  return request({
    url: '/api/v1/oa/docs/files/list',
    method: 'get',
    params: query
  })
}

// 查询文件详细
export function getFile(id) {
  return request({
    url: '/api/v1/oa/docs/files/' + id,
    method: 'get'
  })
}

// 上传文件
export function uploadFile(data) {
  return request({
    url: '/api/v1/oa/docs/files/upload',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

// 修改文件
export function updateFile(data) {
  return request({
    url: '/api/v1/oa/docs/files',
    method: 'put',
    data: data
  })
}

// 删除文件
export function delFile(id) {
  return request({
    url: '/api/v1/oa/docs/files/' + id,
    method: 'delete'
  })
}

// 下载文件
export function downloadFile(id) {
  return request({
    url: '/api/v1/oa/docs/files/' + id + '/download',
    method: 'get',
    responseType: 'blob'
  })
}

// 预览文件
export function previewFile(id) {
  return request({
    url: '/api/v1/oa/docs/files/' + id + '/preview',
    method: 'get',
    responseType: 'blob'
  })
}

// 查询版本列表
export function listVersions(id) {
  return request({
    url: '/api/v1/oa/docs/files/' + id + '/versions',
    method: 'get'
  })
}

// 回滚版本
export function rollbackVersion(id, versionId) {
  return request({
    url: '/api/v1/oa/docs/files/' + id + '/rollback',
    method: 'post',
    params: { versionId }
  })
}

// 搜索文件
export function searchFile(query) {
  return request({
    url: '/api/v1/oa/docs/files/search',
    method: 'get',
    params: query
  })
}

// 查询权限列表
export function listPermissions(id, objectType) {
  return request({
    url: '/api/v1/oa/docs/' + id + '/permissions',
    method: 'get',
    params: { objectType }
  })
}

// 新增权限
export function addPermission(id, objectType, data) {
  return request({
    url: '/api/v1/oa/docs/' + id + '/permissions',
    method: 'post',
    params: { objectType },
    data: data
  })
}

// 修改权限
export function updatePermission(data) {
  return request({
    url: '/api/v1/oa/docs/permissions',
    method: 'put',
    data: data
  })
}

// 删除权限
export function delPermission(id) {
  return request({
    url: '/api/v1/oa/docs/permissions/' + id,
    method: 'delete'
  })
}

// 查询回收站列表
export function listRecycle(query) {
  return request({
    url: '/api/v1/oa/docs/recycle/list',
    method: 'get',
    params: query
  })
}

// 恢复回收站对象
export function restoreRecycle(id) {
  return request({
    url: '/api/v1/oa/docs/recycle/' + id + '/restore',
    method: 'post'
  })
}

// 彻底删除回收站对象
export function purgeRecycle(id) {
  return request({
    url: '/api/v1/oa/docs/recycle/' + id,
    method: 'delete'
  })
}
