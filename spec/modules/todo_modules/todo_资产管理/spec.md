# 资产管理

## 模块概述

资产管理为企业提供固定资产的全生命周期管理，包括资产台账、采购入库、领用、调拨、维修、盘点、报废，审批类操作复用流程引擎。

## 功能架构图

```text
资产管理
    ├── 基础设置
    │   ├── 资产分类
    │   ├── 存放地点
    │   └── 资产状态
    ├── 资产台账
    │   ├── 资产录入
    │   ├── 资产查询
    │   └── 资产详情
    ├── 资产变动
    │   ├── 领用
    │   ├── 归还
    │   ├── 调拨
    │   └── 维修
    ├── 资产盘点
    │   ├── 盘点任务
    │   ├── 扫码盘点
    │   └── 盘点差异
    └── 资产处置
        ├── 报废
        └── 变卖
```

## 功能列表

| 编号 | 功能 | 说明 |
|---|---|---|
| AM-1 | 资产分类 | 多级分类，如电子设备、办公家具 |
| AM-2 | 资产台账 | 资产基础信息和全生命周期记录 |
| AM-3 | 资产入库 | 新资产录入并生成编码和二维码 |
| AM-4 | 资产领用 | 员工申请领用资产 |
| AM-5 | 资产归还 | 领用人归还资产 |
| AM-6 | 资产调拨 | 资产在部门/人员间转移 |
| AM-7 | 资产维修 | 维修申请和记录 |
| AM-8 | 资产盘点 | 定期盘点任务和差异处理 |
| AM-9 | 资产报废 | 资产报废申请和处置 |
| AM-10 | 资产标签 | 生成二维码/RFID 标签 |

## 详细功能需求

### AM-1 资产分类

**功能说明**
建立资产分类体系。

**业务规则**
- 支持多级分类；
- 分类编码唯一；
- 停用分类后不可新增资产。

### AM-2 资产台账

**功能说明**
记录资产全生命周期信息。

**业务规则**
- 资产编码唯一，支持自动生成；
- 台账包含：名称、规格、型号、购置日期、价值、使用人、状态、存放地点；
- 所有变动记录写入资产履历。

### AM-3 资产入库

**功能说明**
新资产录入系统。

**业务规则**
- 支持批量入库；
- 入库后生成二维码标签；
- 支持关联采购合同/发票。

### AM-4 资产领用

**功能说明**
员工申请领用资产。

**业务规则**
- 通过流程引擎发起领用审批；
- 审批通过后更新使用人；
- 闲置资产才能领用。

### AM-5 资产归还

**功能说明**
领用人归还资产。

**业务规则**
- 归还需确认资产状态；
- 归还后资产状态恢复为闲置；
- 损坏资产进入维修流程。

### AM-6 资产调拨

**功能说明**
资产在部门或人员间转移。

**业务规则**
- 调拨需审批；
- 调拨后更新使用人和存放地点；
- 跨部门调拨需双方负责人审批。

### AM-7 资产维修

**功能说明**
记录资产维修申请和维修记录。

**业务规则**
- 维修申请需审批；
- 维修期间资产状态为维修中；
- 记录维修费用和维修商。

### AM-8 资产盘点

**功能说明**
定期对资产进行盘点。

**业务规则**
- 支持创建盘点任务，指定盘点范围和盘点人；
- 支持扫码盘点；
- 自动生成盘点差异报告；
- 盘亏/盘盈需审批处理。

### AM-9 资产报废

**功能说明**
对达到使用年限或损坏的资产进行报废。

**业务规则**
- 报废需审批；
- 报废后资产状态更新为已报废；
- 支持报废变卖记录。

### AM-10 资产标签

**功能说明**
为资产生成二维码或 RFID 标签。

**业务规则**
- 标签包含资产编码和名称；
- 支持批量打印标签；
- 扫码可查看资产详情。

## 用例描述

| 用例编号 | 用例名称 | 参与者 | 基本流程 |
|---|---|---|---|
| UC-AM-01 | 资产入库 | 资产管理员 | 录入资产信息 → 生成编码 → 打印标签 |
| UC-AM-02 | 申请领用 | 普通员工 | 选择闲置资产 → 提交领用申请 → 审批通过 → 领用 |
| UC-AM-03 | 资产盘点 | 资产管理员 | 创建盘点任务 → 扫码盘点 → 处理差异 |

## 状态机/时序图描述

### 资产状态机

```text
[闲置] --领用--> [在用]
[在用] --归还--> [闲置]
[在用] --调拨--> [在用]
[在用] --维修--> [维修中]
[维修中] --维修完成--> [闲置]/[在用]
[闲置] --报废--> [已报废]
[在用] --报废--> [已报废]
```

## 数据模型

| 实体 | 说明 | 核心字段 |
|---|---|---|
| oa_asset_category | 资产分类 | id, parent_id, name, code, depreciation_years, tenant_id |
| oa_asset | 资产台账 | id, code, name, category_id, model, spec, purchase_date, value, user_id, dept_id, location, status, tenant_id |
| oa_asset_receive | 领用记录 | id, asset_id, user_id, process_instance_id, receive_time, return_time, tenant_id |
| oa_asset_return | 归还记录 | id, asset_id, user_id, return_time, status, tenant_id |
| oa_asset_transfer | 调拨记录 | id, asset_id, from_user, to_user, process_instance_id, transfer_time, tenant_id |
| oa_asset_repair | 维修记录 | id, asset_id, reason, cost, repair_time, status, tenant_id |
| oa_asset_inventory | 盘点任务 | id, name, scope_type, scope_ids, status, start_time, end_time, tenant_id |
| oa_asset_inventory_item | 盘点明细 | id, inventory_id, asset_id, book_status, actual_status, result, tenant_id |
| oa_asset_scrap | 报废记录 | id, asset_id, reason, process_instance_id, scrap_time, tenant_id |

## 接口清单

| 接口 | 方法 | 路径 | 说明 |
|---|---|---|---|
| 资产分类管理 | - | /api/v1/oa/assets/categories | 分类 CRUD |
| 资产 CRUD | - | /api/v1/oa/assets | 资产台账管理 |
| 资产领用 | POST | /api/v1/oa/assets/{id}/receive | 申请领用 |
| 资产归还 | POST | /api/v1/oa/assets/{id}/return | 归还资产 |
| 资产调拨 | POST | /api/v1/oa/assets/{id}/transfer | 申请调拨 |
| 资产维修 | POST | /api/v1/oa/assets/{id}/repair | 申请维修 |
| 盘点任务 | - | /api/v1/oa/assets/inventories | 盘点管理 |
| 扫码查询 | GET | /api/v1/oa/assets/{code}/qrcode | 扫码查看资产 |
| 资产统计 | GET | /api/v1/oa/assets/statistics | 资产统计 |
