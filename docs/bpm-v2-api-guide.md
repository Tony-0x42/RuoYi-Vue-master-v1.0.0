# BPM V2 流程引擎中台 API 指南

## 概述

本指南面向前端开发者与第三方系统集成者，描述 `ruoyi-bpm-v2` 模块提供的标准 REST API。

- 基础路径：`/api/v1/process`（标准开放 API）或 `/bpm/v2/...`（管理后台 API）
- 认证方式：复用本系统 `Authorization` Token 或 session
- 统一响应：`{ "code": 200, "msg": "", "data": {} }`
- 标准 API 额外封装：`BpmApiResult<T>` 含 `traceId`

## 标准开放 API

### 启动流程

```http
POST /api/v1/process/instances/start
```

**请求体：**

```json
{
  "processKey": "leave",
  "businessKey": "LEAVE-2024-001",
  "starter": 1,
  "formData": {
    "days": 3,
    "reason": "事假"
  },
  "variables": {
    "approvalAssignee": 2
  }
}
```

**响应：**

```json
{
  "code": 200,
  "msg": "success",
  "data": {
    "instanceId": "1891234567890",
    "processKey": "leave",
    "status": "running"
  }
}
```

### 查询流程实例

```http
GET /api/v1/process/instances/{instanceId}
```

**响应：** 返回实例状态、当前节点、已处理任务列表。

### 完成任务

```http
POST /api/v1/process/tasks/{taskId}/complete
```

**请求体：**

```json
{
  "operator": 2,
  "action": "agree",
  "opinion": "同意",
  "formData": {},
  "variables": {}
}
```

`action` 可选值：`agree`/`reject`/`transfer`/`return`/`terminate`/`recall`

### 查询待办任务

```http
GET /api/v1/process/tasks/todo?userId=2&pageNum=1&pageSize=10
```

### 查询已办任务

```http
GET /api/v1/process/tasks/done?userId=2&pageNum=1&pageSize=10
```

## 管理后台 API

### 流程分类

- `GET /bpm/v2/category/list`
- `POST /bpm/v2/category`
- `PUT /bpm/v2/category`
- `DELETE /bpm/v2/category/{id}`

### 流程定义

- `GET /bpm/v2/definition/list`
- `POST /bpm/v2/definition`
- `PUT /bpm/v2/definition`
- `DELETE /bpm/v2/definition/{id}`
- `POST /bpm/v2/definition/publish/{versionId}`
- `POST /bpm/v2/definition/disable/{definitionId}`
- `POST /bpm/v2/definition/enable/{definitionId}`

### 表单定义

- `GET /bpm/v2/form/list`
- `POST /bpm/v2/form`
- `PUT /bpm/v2/form`
- `DELETE /bpm/v2/form/{id}`
- `GET /bpm/v2/form/{id}/render?instanceId=xxx`

### 字段权限

- `GET /bpm/v2/field-permission/list`
- `POST /bpm/v2/field-permission`
- `PUT /bpm/v2/field-permission`
- `DELETE /bpm/v2/field-permission/{id}`

### 工作日历

- `GET /bpm/v2/calendar/list`
- `POST /bpm/v2/calendar`
- `PUT /bpm/v2/calendar`
- `DELETE /bpm/v2/calendar/{id}`

### 通知模板

- `GET /bpm/v2/notification-template/list`
- `POST /bpm/v2/notification-template`
- `PUT /bpm/v2/notification-template`
- `DELETE /bpm/v2/notification-template/{id}`

### 打印模板

- `GET /bpm/v2/print-template/list`
- `POST /bpm/v2/print-template`
- `PUT /bpm/v2/print-template`
- `DELETE /bpm/v2/print-template/{id}`
- `GET /bpm/v2/print-template/{id}/render?instanceId=xxx`

### 签名

- `GET /bpm/v2/signature/list`
- `POST /bpm/v2/signature`
- `DELETE /bpm/v2/signature/{id}`

### 用户组织扩展

- `GET /bpm/v2/user-org-ext/list`
- `POST /bpm/v2/user-org-ext`
- `PUT /bpm/v2/user-org-ext`
- `DELETE /bpm/v2/user-org-ext/{id}`

### 监控运营

- `GET /bpm/v2/monitor/dashboard`
- `GET /bpm/v2/monitor/abnormal`
- `GET /bpm/v2/monitor/todo-stats`

## 流程模型 JSON 说明

流程定义使用扩展 JSON 描述节点与边：

```json
{
  "nodes": [
    { "id": "start", "type": "start", "name": "开始" },
    { "id": "approve", "type": "approve", "name": "审批", "assigneeType": "user", "assignees": [2] },
    { "id": "end", "type": "end", "name": "结束" }
  ],
  "edges": [
    { "id": "e1", "source": "start", "target": "approve", "condition": "" },
    { "id": "e2", "source": "approve", "target": "end", "condition": "" }
  ]
}
```

节点类型：`start`、`approve`、`cc`、`fill`、`exclusiveGateway`、`parallelGateway`、`end`。

审批方式：`sequential`（依次审批）、`joint`（会签）、`any`（或签）。

## 常用变量

- `approvalAssignee`：启动流程时指定的办理人用户 ID
- `starter`：发起人用户 ID
- `formData`：表单数据对象

## 后续扩展

- BPMN 2.0 XML 导入/导出
- Webhook / 连接器节点
- 完整事件体系（定时、消息、信号）
- 高级审批（加签、委托、批量、任务池）
