# OA 菜单优化与审批流接入设计

## 1. 背景与目标

### 1.1 背景

当前 OA 协同办公模块下所有页面均为 `OA协同办公` 的一级直接子菜单，菜单数量已达 23 个，呈现扁平、难以定位的问题。同时，费用报销、资产管理、考勤管理等模块的业务逻辑中均明确需要审批，但当前实现仅为状态字段硬编码流转，未接入真实流程引擎，也没有统一待办入口。

### 1.2 目标

1. 将 OA 菜单按业务域重新组织为二级/三级目录，提升导航清晰度。
2. 为需要审批的 OA 业务接入项目已运行的 `ruoyi-bpm-v2` 流程引擎，实现真实的“提交 → 待办 → 审批通过/驳回”闭环。
3. 创建一个固定的审批人员账号，所有 OA 审批流程默认路由到该人员。
4. 门户工作台的“待办聚合”从 mock 数据改为读取真实 `bpm_task` 待办，并支持点击办理。
5. 在 Chrome 中验证：提交审批 → 审批人登录看到待办 → 办理通过/驳回 → 业务状态正确更新。

## 2. 现有状态

- 流程引擎：项目未启用 Flowable Runtime，实际使用的是 `ruoyi-bpm-v2` 自定义流程引擎（`BpmRuntimeEngine`、`bpm_process_definition`、`bpm_process_instance`、`bpm_task` 等表）。已提供 `/api/v1/process/instances/start`、`/api/v1/process/tasks/todo`、`/api/v1/process/tasks/{taskId}/complete` 等接口。
- 待办聚合：门户工作台 `/api/v1/oa/portal/todos` 当前返回硬编码 mock 数据。
- 菜单：`sys_menu.parent_id = 3000` 下有 23 个直接子项，多数配置页面与主页面平级。

## 3. 菜单优化方案

将 `OA协同办公` 下的一级菜单收缩为 6 个二级目录 + 1 个一级页面（门户工作台），目录下再挂三级页面：

```text
OA协同办公 (3000)
├── 门户工作台 (3170)              [一级页面]
├── 协同沟通 (3200)
│   ├── 通讯录 (3001)
│   ├── 公告通知 (3010)
│   └── 消息通知中心 (3080)
├── 日程协作 (3210)
│   ├── 日程管理 (3020)
│   └── 任务协作 (3030)
├── 会议管理 (3230)
│   ├── 会议管理 (3040)
│   └── 会议室管理 (3041)
├── 知识文档 (3220)
│   ├── 知识库 (3050)
│   └── 文档管理 (3060)
├── 人事考勤 (3240)
│   ├── 考勤管理 (3100)
│   │   ├── 考勤组 (3101)
│   │   └── 打卡记录 (3102)
│   └── 请假/加班/出差/补卡申请页面 [新增]
├── 费用报销 (3250)
│   ├── 费用报销 (3120)
│   ├── 费用类型 (3121)
│   ├── 费用标准 (3122)
│   ├── 发票管理 (3123)
│   ├── 预算管理 (3124)
│   └── 借款还款 (3125)
└── 资产管理 (3260)
    ├── 资产管理 (3150)
    ├── 资产分类 (3151)
    └── 资产盘点 (3152)
```

说明：
- 原“公告分类 (3011)”作为“公告通知”的子页面；“知识分类 (3051)”作为“知识库”的子页面；“消息模板 (3081)”作为“消息通知中心”的子页面。
- 考勤模块除原有“考勤组/打卡记录”外，新增请假、加班、出差、补卡申请页面（当前表模型未完全包含，需补充）。

## 4. 审批流接入方案

### 4.1 审批人账号

在 `sys_user` 中新增一个固定审批账号：

| 字段 | 值 |
|---|---|
| user_id | 2 （或自动分配） |
| user_name | approver |
| nick_name | 审批人 |
| password | 与 admin 相同或默认 `123456` 加密 |
| status | 0 |
| role | 普通角色 + 审批角色 |

为简化，直接创建一个普通用户并赋予查看所有 OA 菜单的权限；审批任务通过 `variables.approvalAssignee=2` 直接指定给该用户。

### 4.2 需要接入审批的模块与流程定义

| 模块 | 流程定义 Key | 触发动作 | 说明 |
|---|---|---|---|
| 费用报销 | `oa_expense_report` | 提交报销单 | 审批通过后进入“财务审核中/待付款” |
| 借款还款 | `oa_expense_loan` | 提交借款单 | 审批通过后可被报销单冲销 |
| 资产管理-领用 | `oa_asset_receive` | 申请领用 | 审批通过后资产状态变为“在用” |
| 资产管理-调拨 | `oa_asset_transfer` | 申请调拨 | 审批通过后更新使用人/部门 |
| 资产管理-维修 | `oa_asset_repair` | 申请维修 | 审批通过后资产状态变为“维修中” |
| 资产管理-报废 | `oa_asset_scrap` | 申请报废 | 审批通过后资产状态变为“已报废” |
| 考勤管理-请假 | `oa_attendance_leave` | 提交请假申请 | 审批通过后计入考勤统计 |
| 考勤管理-加班 | `oa_attendance_overtime` | 提交加班申请 | 审批通过后计入考勤统计 |
| 考勤管理-出差 | `oa_attendance_trip` | 提交出差申请 | 审批通过后计入考勤统计 |
| 考勤管理-补卡 | `oa_attendance_makeup` | 提交补卡申请 | 审批通过后恢复考勤日状态 |

每个流程定义均为最简单的“开始 → 用户任务 → 结束”模型，用户任务使用 `flowable:assignee="${approvalAssignee}"`。

### 4.3 业务侧改动

1. **提交时启动流程**
   - 在对应 Controller/Service 中调用 `IBpmProcessInstanceService.start(processKey, businessKey, starter, formData, variables)`。
   - `businessKey` 使用业务实体 ID（如 `expense_report:{id}`）。
   - `variables` 中必须包含 `approvalAssignee=2`。
   - 实体状态更新为“审批中”。

2. **审批通过后回调**
   - 通过 `BpmTaskServiceImpl.complete(...)` 办理任务，引擎会推进到结束节点。
   - 在流程结束或节点进入时，目前没有自动回调机制。采用“业务方在 complete 后主动查询实例状态并更新业务表”的方式：
     - 提供一个通用监听器/工具方法 `onProcessCompleted(instanceId, action)`，根据 `businessKey` 前缀路由到对应模块的状态更新逻辑。
     - 同意：更新业务表状态为已通过，并执行业务后续动作（如资产状态变更、预算占用）。
     - 驳回：更新业务表状态为已驳回/草稿。

3. **门户待办聚合**
   - `OaPortalServiceImpl.selectTodos(userId, ...)` 改为调用 `IBpmTaskService.selectTodoList(userId, tenantId)`。
   - 将 `BpmTask` 转换为前端需要的 `processName/initiator/arriveTime` 结构：
     - `processName` 从流程定义名称 + businessKey 解析。
     - `initiator` 从 `bpm_process_instance.starter` 关联 `sys_user` 获取。
     - `arriveTime` = `bpm_task.create_time`。
   - 前端点击“处理”跳转至 `/bpm/task/todo` 或打开通用审批弹窗。

### 4.4 流程定义初始化

通过 SQL 脚本在 `bpm_process_definition` 和 `bpm_definition_version` 中插入上述 10 个流程定义的初始草稿，并发布状态为 1。每个流程 XML 均为标准 BPMN：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<definitions ...>
  <process id="oa_expense_report" name="报销审批">
    <startEvent id="start" />
    <sequenceFlow id="flow1" sourceRef="start" targetRef="approve" />
    <userTask id="approve" name="审批" flowable:assignee="${approvalAssignee}" />
    <sequenceFlow id="flow2" sourceRef="approve" targetRef="end" />
    <endEvent id="end" />
  </process>
</definitions>
```

## 5. 测试验证方案

1. 重新执行菜单 SQL，刷新后确认 OA 菜单为二级/三级结构。
2. 创建审批人账号并登录，确认无待办。
3. 普通用户（admin）登录，进入费用报销/资产管理/考勤管理，提交一条记录。
4. 切换审批人账号，在门户工作台“待办聚合”中看到真实待办。
5. 点击办理，选择“同意”或“驳回”。
6. 切换回普通用户，确认业务状态已更新。
7. 中/英文界面均验证一次。

## 6. 范围与排除

- 本次会议纪要和知识库评论审核暂不接入工作流（需求中仅轻度提及，非流程引擎审批）。
- 不引入真实 Flowable 引擎，复用现有 `ruoyi-bpm-v2`。
- 不改动流程设计器前端，流程定义通过初始化 SQL 写入。
