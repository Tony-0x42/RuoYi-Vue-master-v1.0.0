import request from '@/utils/request'

// 查询费用类型列表
export function listCategory(query) {
  return request({
    url: '/api/v1/oa/expense/categories/list',
    method: 'get',
    params: query
  })
}

// 查询费用类型详细
export function getCategory(id) {
  return request({
    url: '/api/v1/oa/expense/categories/' + id,
    method: 'get'
  })
}

// 新增费用类型
export function addCategory(data) {
  return request({
    url: '/api/v1/oa/expense/categories',
    method: 'post',
    data: data
  })
}

// 修改费用类型
export function updateCategory(data) {
  return request({
    url: '/api/v1/oa/expense/categories',
    method: 'put',
    data: data
  })
}

// 删除费用类型
export function delCategory(id) {
  return request({
    url: '/api/v1/oa/expense/categories/' + id,
    method: 'delete'
  })
}

// 查询费用标准列表
export function listStandard(query) {
  return request({
    url: '/api/v1/oa/expense/standards/list',
    method: 'get',
    params: query
  })
}

// 查询费用标准详细
export function getStandard(id) {
  return request({
    url: '/api/v1/oa/expense/standards/' + id,
    method: 'get'
  })
}

// 新增费用标准
export function addStandard(data) {
  return request({
    url: '/api/v1/oa/expense/standards',
    method: 'post',
    data: data
  })
}

// 修改费用标准
export function updateStandard(data) {
  return request({
    url: '/api/v1/oa/expense/standards',
    method: 'put',
    data: data
  })
}

// 删除费用标准
export function delStandard(id) {
  return request({
    url: '/api/v1/oa/expense/standards/' + id,
    method: 'delete'
  })
}

// 查询发票列表
export function listInvoice(query) {
  return request({
    url: '/api/v1/oa/expense/invoices/list',
    method: 'get',
    params: query
  })
}

// 查询发票详细
export function getInvoice(id) {
  return request({
    url: '/api/v1/oa/expense/invoices/' + id,
    method: 'get'
  })
}

// 新增发票
export function addInvoice(data) {
  return request({
    url: '/api/v1/oa/expense/invoices',
    method: 'post',
    data: data
  })
}

// 修改发票
export function updateInvoice(data) {
  return request({
    url: '/api/v1/oa/expense/invoices',
    method: 'put',
    data: data
  })
}

// 删除发票
export function delInvoice(id) {
  return request({
    url: '/api/v1/oa/expense/invoices/' + id,
    method: 'delete'
  })
}

// 发票识别
export function recognizeInvoice(data) {
  return request({
    url: '/api/v1/oa/expense/invoices/recognize',
    method: 'post',
    data: data
  })
}

// 发票验真
export function verifyInvoice(data) {
  return request({
    url: '/api/v1/oa/expense/invoices/verify',
    method: 'post',
    data: data
  })
}

// 查询报销单列表
export function listReport(query) {
  return request({
    url: '/api/v1/oa/expense/reports/list',
    method: 'get',
    params: query
  })
}

// 查询报销单详细
export function getReport(id) {
  return request({
    url: '/api/v1/oa/expense/reports/' + id,
    method: 'get'
  })
}

// 新增报销单
export function addReport(data) {
  return request({
    url: '/api/v1/oa/expense/reports',
    method: 'post',
    data: data
  })
}

// 修改报销单
export function updateReport(data) {
  return request({
    url: '/api/v1/oa/expense/reports',
    method: 'put',
    data: data
  })
}

// 删除报销单
export function delReport(id) {
  return request({
    url: '/api/v1/oa/expense/reports/' + id,
    method: 'delete'
  })
}

// 提交报销单
export function submitReport(id) {
  return request({
    url: '/api/v1/oa/expense/reports/' + id + '/submit',
    method: 'post'
  })
}

// 费用统计
export function reportStatistics(query) {
  return request({
    url: '/api/v1/oa/expense/reports/statistics',
    method: 'get',
    params: query
  })
}

// 查询预算列表
export function listBudget(query) {
  return request({
    url: '/api/v1/oa/expense/budgets/list',
    method: 'get',
    params: query
  })
}

// 查询预算详细
export function getBudget(id) {
  return request({
    url: '/api/v1/oa/expense/budgets/' + id,
    method: 'get'
  })
}

// 新增预算
export function addBudget(data) {
  return request({
    url: '/api/v1/oa/expense/budgets',
    method: 'post',
    data: data
  })
}

// 修改预算
export function updateBudget(data) {
  return request({
    url: '/api/v1/oa/expense/budgets',
    method: 'put',
    data: data
  })
}

// 删除预算
export function delBudget(id) {
  return request({
    url: '/api/v1/oa/expense/budgets/' + id,
    method: 'delete'
  })
}

// 查询借款单列表
export function listLoan(query) {
  return request({
    url: '/api/v1/oa/expense/loans/list',
    method: 'get',
    params: query
  })
}

// 查询可冲销借款
export function listAvailableLoan() {
  return request({
    url: '/api/v1/oa/expense/loans/available',
    method: 'get'
  })
}

// 查询借款单详细
export function getLoan(id) {
  return request({
    url: '/api/v1/oa/expense/loans/' + id,
    method: 'get'
  })
}

// 新增借款单
export function addLoan(data) {
  return request({
    url: '/api/v1/oa/expense/loans',
    method: 'post',
    data: data
  })
}

// 修改借款单
export function updateLoan(data) {
  return request({
    url: '/api/v1/oa/expense/loans',
    method: 'put',
    data: data
  })
}

// 删除借款单
export function delLoan(id) {
  return request({
    url: '/api/v1/oa/expense/loans/' + id,
    method: 'delete'
  })
}

// 借款还款
export function repaymentLoan(id, data) {
  return request({
    url: '/api/v1/oa/expense/loans/' + id + '/repayment',
    method: 'post',
    data: data
  })
}

// 提交借款单
export function submitLoan(id, data) {
  return request({
    url: '/api/v1/oa/expense/loans/' + id + '/submit',
    method: 'post',
    data: data
  })
}
