# 六、数据模型

## 6.1 核心实体

| 实体 | 说明 | 核心字段 |
|---|---|---|
| bpm_process_definition | 流程定义 | id, key, name, category_id, tenant_id, latest_version, status, xml, ext_json, create_by, create_time |
| bpm_definition_version | 流程定义版本 | id, definition_id, version, version_name, changelog, xml, ext_json, status, publish_time, publish_by |
| bpm_process_instance | 流程实例 | id, definition_id, version_id, business_key, starter, status, variables, start_time, end_time, tenant_id |
| bpm_task | 任务/待办 | id, instance_id, node_id, node_name, assignee, candidates, status, create_time, due_time, end_time, action, opinion |
| bpm_task_history | 任务历史 | id, task_id, instance_id, node_id, operator, action, opinion, form_data, operate_time |
| bpm_form_definition | 表单定义 | id, definition_id, version_id, name, schema_json, tenant_id |
| bpm_form_data | 表单数据 | id, instance_id, field_code, field_value, field_type, create_time |
| bpm_field_permission | 字段权限 | id, definition_id, node_id, field_code, permission, tenant_id |
| bpm_category | 流程分类 | id, parent_id, name, code, sort, status, tenant_id |
| bpm_notification_template | 通知模板 | id, type, channel, subject, content, tenant_id |
| bpm_work_calendar | 工作日历 | id, tenant_id, year, date, type, work_hours |
| bpm_audit_log | 审计日志 | id, tenant_id, user_id, ip, action, object_type, object_id, before_value, after_value, operate_time |
| sys_user / sys_dept / sys_role | 组织架构 | 复用或扩展现有用户/部门/角色表 |

## 6.2 ER 图描述

```text
bpm_process_definition ||--o{ bpm_definition_version : 拥有多个版本
bpm_process_definition ||--o{ bpm_process_instance : 产生实例
bpm_definition_version ||--o{ bpm_process_instance : 实例绑定版本
bpm_process_definition ||--o{ bpm_form_definition : 关联表单
bpm_process_definition ||--o{ bpm_field_permission : 字段权限
bpm_process_definition }o--|| bpm_category : 属于分类
bpm_process_instance ||--o{ bpm_task : 产生任务
bpm_process_instance ||--o{ bpm_task_history : 产生历史
bpm_process_instance ||--o{ bpm_form_data : 表单数据
bpm_task ||--|| bpm_task_history : 历史来源
sys_user ||--o{ bpm_process_instance : 发起
sys_user ||--o{ bpm_task : 处理
sys_dept ||--o{ sys_user : 属于
sys_role ||--o{ sys_user : 授权
```

## 6.3 关键字段说明

### bpm_process_definition

| 字段 | 类型 | 说明 |
|---|---|---|
| id | BIGINT | 主键 |
| key | VARCHAR(64) | 流程定义唯一标识 |
| name | VARCHAR(128) | 流程名称 |
| category_id | BIGINT | 分类 ID |
| tenant_id | BIGINT | 租户 ID |
| latest_version | VARCHAR(32) | 最新发布版本号 |
| status | TINYINT | 0 草稿 1 已发布 2 已停用 |
| xml | LONGTEXT | BPMN 2.0 XML |
| ext_json | JSON | 扩展配置 |

### bpm_process_instance

| 字段 | 类型 | 说明 |
|---|---|---|
| id | VARCHAR(64) | 实例 ID |
| definition_id | BIGINT | 流程定义 ID |
| version_id | BIGINT | 创建时版本 ID |
| business_key | VARCHAR(128) | 业务标识 |
| starter | BIGINT | 发起人用户 ID |
| status | VARCHAR(32) | RUNNING/COMPLETED/REJECTED/TERMINATED/SUSPENDED |
| variables | JSON | 流程变量 |
| tenant_id | BIGINT | 租户 ID |

### bpm_task

| 字段 | 类型 | 说明 |
|---|---|---|
| id | VARCHAR(64) | 任务 ID |
| instance_id | VARCHAR(64) | 所属实例 |
| node_id | VARCHAR(64) | 节点 ID |
| node_name | VARCHAR(128) | 节点名称 |
| assignee | BIGINT | 办理人 |
| candidates | JSON | 候选人列表 |
| status | VARCHAR(32) | PENDING/CLAIMED/COMPLETED/TRANSFERRED/RETURNED |
| due_time | DATETIME | 截止时间 |

## 6.4 索引设计

- `bpm_process_definition`: idx_key_tenant, idx_category, idx_status
- `bpm_process_instance`: idx_definition, idx_starter, idx_status, idx_business_key, idx_tenant
- `bpm_task`: idx_instance, idx_assignee_status, idx_tenant_status, idx_due_time
- `bpm_task_history`: idx_instance, idx_operator
- `bpm_form_data`: idx_instance_field

## 6.5 数据归档

- 运行中数据保留在核心库；
- 已完成/终止实例按 M14.7 策略迁移到归档库；
- 归档库使用独立索引，支持只读查询与报表分析。
