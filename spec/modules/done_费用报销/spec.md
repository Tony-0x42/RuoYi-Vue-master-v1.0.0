# 费用报销

## 模块概述

费用报销为企业提供费用申请、报销单管理、发票识别、费用标准控制、预算控制和审批流转能力，审批流程复用流程引擎。

## 功能架构图

```text
费用报销
    ├── 基础设置
    │   ├── 费用类型
    │   ├── 费用标准
    │   └── 预算科目
    ├── 发票管理
    │   ├── 发票录入
    │   ├── OCR 识别
    │   └── 发票验真
    ├── 报销单
    │   ├── 报销单创建
    │   ├── 关联发票
    │   └── 审批流转
    ├── 借款还款
    │   ├── 借款申请
    │   └── 还款冲销
    └── 统计分析
        ├── 费用报表
        └── 预算执行
```

## 功能列表

| 编号 | 功能 | 说明 |
|---|---|---|
| ER-1 | 费用类型 | 差旅、招待、办公、交通等费用分类 |
| ER-2 | 费用标准 | 按职级/城市/部门设置限额 |
| ER-3 | 发票管理 | 发票录入、OCR 识别、验真、查重 |
| ER-4 | 报销单 | 创建报销单、关联发票、填写明细 |
| ER-5 | 预算控制 | 报销时校验预算余额 |
| ER-6 | 借款还款 | 借款申请和报销冲销 |
| ER-7 | 审批流转 | 通过流程引擎发起报销审批 |
| ER-8 | 统计分析 | 费用报表和预算执行分析 |

## 详细功能需求

### ER-1 费用类型

**功能说明**
建立费用类型体系。

**业务规则**
- 支持多级分类；
- 费用类型关联默认审批流程和预算科目；
- 停用后不可选择。

### ER-2 费用标准

**功能说明**
设置不同职级、城市、部门的费用限额。

**业务规则**
- 超标准需特殊审批或禁止提交；
- 支持按日、按次、按月设置限额；
- 标准可按费用类型单独配置。

### ER-3 发票管理

**功能说明**
管理报销发票，支持 OCR 识别和验真。

**业务规则**
- 支持增值税发票、电子发票、火车票等；
- OCR 识别发票抬头、金额、税号、日期；
- 发票验真对接税务接口；
- 同一张发票不可重复报销。

### ER-4 报销单

**功能说明**
员工创建报销单并提交审批。

**业务规则**
- 报销单可包含多个费用明细；
- 每个明细关联发票；
- 报销单可关联借款单进行冲销；
- 提交时校验费用标准和预算。

### ER-5 预算控制

**功能说明**
报销时校验预算余额。

**业务规则**
- 预算按部门/项目/科目设置；
- 超预算时预警或禁止提交（可配置）；
- 审批通过后占用预算，拒绝/撤回释放预算。

### ER-6 借款还款

**功能说明**
管理员工借款和还款。

**业务规则**
- 借款需审批；
- 报销时可选择冲销借款；
- 逾期未还款可发送催还通知。

### ER-7 审批流转

**功能说明**
报销单通过流程引擎发起审批。

**业务规则**
- 审批人可按金额、部门、费用类型动态确定；
- 审批通过进入财务审核；
- 财务审核通过后生成付款申请。

### ER-8 统计分析

**功能说明**
提供费用和预算分析报表。

**业务规则**
- 按部门、个人、费用类型、时间维度统计；
- 展示预算执行率；
- 支持导出报表。

## 用例描述

| 用例编号 | 用例名称 | 参与者 | 基本流程 |
|---|---|---|---|
| UC-ER-01 | 提交报销单 | 普通员工 | 创建报销单 → 上传发票 → 填写明细 → 提交审批 |
| UC-ER-02 | 发票识别 | 普通员工 | 拍照上传发票 → OCR 识别 → 核对信息 |
| UC-ER-03 | 财务付款 | 财务人员 | 审批通过 → 财务审核 → 生成付款申请 |

## 状态机/时序图描述

### 报销单状态机

```text
[草稿] --提交--> [审批中]
[审批中] --通过--> [财务审核中]
[审批中] --退回--> [草稿]
[审批中] --拒绝--> [已拒绝]
[财务审核中] --通过--> [待付款]
[财务审核中] --退回--> [草稿]
[待付款] --付款--> [已完成]
```

## 数据模型

| 实体 | 说明 | 核心字段 |
|---|---|---|
| oa_expense_category | 费用类型 | id, parent_id, name, code, process_key, budget_item_id, tenant_id |
| oa_expense_standard | 费用标准 | id, category_id, level, city, limit_amount, period_type, tenant_id |
| oa_expense_invoice | 发票 | id, invoice_code, invoice_no, amount, tax, date, buyer, seller, status, tenant_id |
| oa_expense_report | 报销单 | id, user_id, total_amount, category_id, process_instance_id, status, loan_id, tenant_id |
| oa_expense_item | 报销明细 | id, report_id, category_id, amount, invoice_id, description, date |
| oa_expense_budget | 预算 | id, dept_id, project_id, item_id, year, total_amount, used_amount, tenant_id |
| oa_expense_loan | 借款单 | id, user_id, amount, reason, process_instance_id, status, tenant_id |
| oa_expense_repayment | 还款记录 | id, loan_id, amount, report_id, create_time, tenant_id |

## 接口清单

| 接口 | 方法 | 路径 | 说明 |
|---|---|---|---|
| 费用类型管理 | - | /api/v1/oa/expense/categories | 费用类型 CRUD |
| 费用标准管理 | - | /api/v1/oa/expense/standards | 费用标准 CRUD |
| 发票识别 | POST | /api/v1/oa/expense/invoices/recognize | OCR 识别发票 |
| 发票验真 | POST | /api/v1/oa/expense/invoices/verify | 验真发票 |
| 报销单 CRUD | - | /api/v1/oa/expense/reports | 报销单管理 |
| 预算查询 | GET | /api/v1/oa/expense/budgets | 查询预算 |
| 借款单管理 | - | /api/v1/oa/expense/loans | 借款管理 |
| 费用统计 | GET | /api/v1/oa/expense/statistics | 费用统计 |
