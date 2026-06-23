import request from '@/utils/request'

// 查询流程分类列表
export function listCategory(query) {
  return request({
    url: '/bpm/category/list',
    method: 'get',
    params: query
  })
}

// 查询流程分类树
export function treeCategory(query) {
  return request({
    url: '/bpm/category/tree',
    method: 'get',
    params: query
  })
}

// 查询流程分类详细
export function getCategory(categoryId) {
  return request({
    url: '/bpm/category/' + categoryId,
    method: 'get'
  })
}

// 新增流程分类
export function addCategory(data) {
  return request({
    url: '/bpm/category',
    method: 'post',
    data: data
  })
}

// 修改流程分类
export function updateCategory(data) {
  return request({
    url: '/bpm/category',
    method: 'put',
    data: data
  })
}

// 删除流程分类
export function delCategory(categoryId) {
  return request({
    url: '/bpm/category/' + categoryId,
    method: 'delete'
  })
}
