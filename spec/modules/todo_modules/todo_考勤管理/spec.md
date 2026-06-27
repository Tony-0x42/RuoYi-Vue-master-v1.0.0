# 考勤管理

## 模块概述

考勤管理为企业提供考勤打卡、排班、请假/加班/出差管理、考勤统计能力。审批类操作复用流程引擎，考勤结果自动汇总生成月报。

## 功能架构图

```text
考勤管理
    ├── 考勤设置
    │   ├── 考勤组
    │   ├── 班次
    │   └── 排班
    ├── 考勤打卡
    │   ├── GPS 打卡
    │   ├── WiFi 打卡
    │   ├── 考勤机打卡
    │   └── 外勤打卡
    ├── 考勤申请
    │   ├── 请假
    │   ├── 加班
    │   ├── 出差
    │   └── 补卡
    ├── 考勤统计
    │   ├── 日报
    │   ├── 月报
    │   └── 异常处理
    └── 假期余额
        ├── 年假
        ├── 调休
        └── 其他假期
```

## 功能列表

| 编号 | 功能 | 说明 |
|---|---|---|
| AT-1 | 考勤组 | 按部门/人员设置考勤规则 |
| AT-2 | 班次 | 定义上下班时间和打卡规则 |
| AT-3 | 排班 | 固定班、轮班、自由工时 |
| AT-4 | 打卡 | 多种打卡方式 |
| AT-5 | 请假 | 通过流程引擎发起请假审批 |
| AT-6 | 加班 | 通过流程引擎发起加班审批 |
| AT-7 | 出差 | 通过流程引擎发起出差审批 |
| AT-8 | 补卡 | 漏打卡申请补卡 |
| AT-9 | 考勤统计 | 自动生成日报/月报 |
| AT-10 | 假期余额 | 管理员工假期余额 |

## 详细功能需求

### AT-1 考勤组

**功能说明**
为不同部门或人员设置考勤规则。

**业务规则**
- 一个人员只能属于一个考勤组；
- 考勤组关联班次和打卡方式；
- 支持按部门、角色、个人配置。

### AT-2 班次

**功能说明**
定义上下班时间、午休时间、迟到早退规则。

**业务规则**
- 支持多个班次；
- 支持弹性班次；
- 迟到/早退规则可配置。

### AT-3 排班

**功能说明**
为人员安排具体班次。

**业务规则**
- 支持固定班、轮班、自由工时；
- 支持批量排班；
- 排班变更需通知员工。

### AT-4 打卡

**功能说明**
员工通过多种方式完成考勤打卡。

**业务规则**
- GPS 打卡：在指定地点范围内；
- WiFi 打卡：连接指定 WiFi；
- 考勤机打卡：对接考勤机；
- 外勤打卡：需填写外勤事由和地点；
- 一天支持多次打卡。

### AT-5 请假

**功能说明**
员工发起请假申请，审批结果同步考勤。

**业务规则**
- 通过流程引擎发起请假审批；
- 请假期间不计入缺勤；
- 按假期类型扣减假期余额。

### AT-6 加班

**功能说明**
员工发起加班申请，审批结果同步考勤。

**业务规则**
- 通过流程引擎发起加班审批；
- 加班时长可用于调休或计加班费；
- 加班需符合公司加班规则。

### AT-7 出差

**功能说明**
员工发起出差申请，审批结果同步考勤。

**业务规则**
- 通过流程引擎发起出差审批；
- 出差期间按工作日处理；
- 支持出差打卡。

### AT-8 补卡

**功能说明**
漏打卡时申请补卡。

**业务规则**
- 每月补卡次数限制（可配置）；
- 需填写补卡原因；
- 走审批流程。

### AT-9 考勤统计

**功能说明**
自动生成员工考勤日报和月报。

**业务规则**
- 日报展示每天出勤状态；
- 月报汇总出勤、迟到、早退、缺勤、请假、加班等；
- 支持导出 Excel。

### AT-10 假期余额

**功能说明**
管理员工各类假期余额。

**业务规则**
- 年假按入职时间自动计算；
- 调休按加班时长生成；
- 请假时自动扣减，离职时清算。

## 用例描述

| 用例编号 | 用例名称 | 参与者 | 基本流程 |
|---|---|---|---|
| UC-AT-01 | 每日打卡 | 普通员工 | 打开考勤 → 选择打卡方式 → 打卡 |
| UC-AT-02 | 申请请假 | 普通员工 | 发起请假流程 → 审批通过 → 同步考勤 |
| UC-AT-03 | 查看月报 | 员工/HR | 进入考勤统计 → 选择月份 → 查看/导出 |

## 状态机/时序图描述

### 考勤日状态机

```text
[正常] --迟到--> [迟到]
[正常] --早退--> [早退]
[正常] --缺卡--> [缺卡]
[缺卡] --补卡通过--> [正常]
```

## 数据模型

| 实体 | 说明 | 核心字段 |
|---|---|---|
| oa_attendance_group | 考勤组 | id, name, rule_json, member_type, member_ids, tenant_id |
| oa_attendance_shift | 班次 | id, name, work_time_json, late_rule, early_rule, tenant_id |
| oa_attendance_schedule | 排班 | id, user_id, date, shift_id, tenant_id |
| oa_attendance_record | 打卡记录 | id, user_id, type, time, location, wifi, photo, status, tenant_id |
| oa_attendance_leave | 请假记录 | id, user_id, type, start_time, end_time, days, process_instance_id, tenant_id |
| oa_attendance_overtime | 加班记录 | id, user_id, start_time, end_time, hours, process_instance_id, tenant_id |
| oa_attendance_trip | 出差记录 | id, user_id, start_time, end_time, days, process_instance_id, tenant_id |
| oa_attendance_makeup | 补卡申请 | id, user_id, date, type, reason, process_instance_id, status, tenant_id |
| oa_attendance_monthly | 月度统计 | id, user_id, year_month, normal_days, late_count, early_count, absent_days, leave_days, overtime_hours, tenant_id |
| oa_leave_balance | 假期余额 | id, user_id, leave_type, total_days, used_days, tenant_id |

## 接口清单

| 接口 | 方法 | 路径 | 说明 |
|---|---|---|---|
| 考勤打卡 | POST | /api/v1/oa/attendance/check-in | 员工打卡 |
| 打卡记录 | GET | /api/v1/oa/attendance/records | 查询打卡记录 |
| 考勤组管理 | - | /api/v1/oa/attendance/groups | 考勤组 CRUD |
| 班次管理 | - | /api/v1/oa/attendance/shifts | 班次 CRUD |
| 排班管理 | - | /api/v1/oa/attendance/schedules | 排班管理 |
| 考勤月报 | GET | /api/v1/oa/attendance/monthly | 查询月报 |
| 假期余额 | GET | /api/v1/oa/attendance/leave-balance | 查询假期余额 |
