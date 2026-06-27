# 五、接口需求

## 5.1 接口设计总则

- 协议：HTTPS，Content-Type 默认 `application/json`；
- 鉴权：Header 中携带 `Authorization: Bearer {accessToken}`，以及 `X-Tenant-Id: {tenantId}`；
- 字符编码：UTF-8；
- 时间格式：ISO 8601，如 `2026-06-24T11:20:16+08:00`；
- 幂等：写操作支持 `Idempotency-Key` Header；
- 版本：URL 路径前缀 `/api/v1`。

## 5.2 统一响应结构

```json
{
  "code": 0,
  "message": "success",
  "data": {},
  "traceId": "abc123"
}
```

- `code`: 业务状态码，0 表示成功；
- `message`: 提示信息；
- `data`: 业务数据；
- `traceId`: 请求追踪 ID。

## 5.3 核心 API

### 5.3.1 流程发起

**请求**

```http
POST /api/v1/process/instances/start
Content-Type: application/json
Authorization: Bearer {token}
X-Tenant-Id: {tenantId}
Idempotency-Key: {key}

{
  "processDefinitionKey": "leave",
  "businessKey": "LEAVE-20260624-001",
  "starter": "10001",
  "formData": {
    "leaveType": "annual",
    "startDate": "2026-07-01",
    "endDate": "2026-07-03",
    "reason": "年假"
  },
  "variables": {
    "approvalAssignee": "10002"
  },
  "callbackUrl": "https://example.com/callback"
}
```

**响应**

```json
{
  "code": 0,
  "message": "success",
  "data": {
    "instanceId": "inst_123456",
    "processDefinitionId": "leave:1:abc",
    "currentNode": "部门主管审批",
    "status": "RUNNING",
    "startTime": "2026-06-24T11:20:16+08:00",
    "taskList": [
      {
        "taskId": "task_001",
        "nodeName": "部门主管审批",
        "assignee": "10002",
        "createTime": "2026-06-24T11:20:16+08:00"
      }
    ]
  }
}
```

**错误码**

| 错误码 | 说明 |
|---|---|
| 400001 | 参数校验失败 |
| 400002 | 流程定义未发布或不存在 |
| 400003 | 发起人无权限发起该流程 |
| 409001 | 幂等键重复 |
| 429001 | 触发限流 |

### 5.3.2 流程状态查询

**请求**

```http
GET /api/v1/process/instances/{instanceId}
Authorization: Bearer {token}
X-Tenant-Id: {tenantId}
```

**响应**

```json
{
  "code": 0,
  "data": {
    "instanceId": "inst_123456",
    "processDefinitionKey": "leave",
    "status": "RUNNING",
    "starter": "10001",
    "currentNode": "部门主管审批",
    "startTime": "2026-06-24T11:20:16+08:00",
    "history": [
      {
        "nodeName": "发起",
        "operator": "10001",
        "action": "START",
        "opinion": "",
        "time": "2026-06-24T11:20:16+08:00"
      }
    ]
  }
}
```

### 5.3.3 流程操作

**请求**

```http
POST /api/v1/process/tasks/{taskId}/complete
Authorization: Bearer {token}
X-Tenant-Id: {tenantId}

{
  "operator": "10002",
  "action": "AGREE",
  "opinion": "同意",
  "formData": {},
  "variables": {}
}
```

**响应**

```json
{
  "code": 0,
  "data": {
    "taskId": "task_001",
    "instanceId": "inst_123456",
    "nextNode": "HR 审批",
    "nextTasks": []
  }
}
```

**action 枚举**

| 值 | 说明 |
|---|---|
| AGREE | 同意 |
| REJECT | 拒绝 |
| TRANSFER | 转交 |
| ADD_SIGN | 加签 |
| RETURN | 退回 |
| REVOKE | 撤回 |
| TERMINATE | 终止 |
| COMMENT | 评论 |

### 5.3.4 待办列表查询

**请求**

```http
GET /api/v1/process/tasks/todo?userId=10002&pageNum=1&pageSize=20
Authorization: Bearer {token}
X-Tenant-Id: {tenantId}
```

**响应**

```json
{
  "code": 0,
  "data": {
    "total": 35,
    "list": [
      {
        "taskId": "task_001",
        "instanceId": "inst_123456",
        "processName": "请假申请",
        "nodeName": "部门主管审批",
        "starterName": "张三",
        "createTime": "2026-06-24T11:20:16+08:00",
        "dueTime": "2026-06-25T11:20:16+08:00"
      }
    ]
  }
}
```

### 5.3.5 回调推送格式

**请求**

```http
POST {callbackUrl}
Content-Type: application/json
X-Callback-Signature: sha256=...

{
  "eventType": "PROCESS_COMPLETED",
  "instanceId": "inst_123456",
  "processDefinitionKey": "leave",
  "businessKey": "LEAVE-20260624-001",
  "status": "COMPLETED",
  "eventTime": "2026-06-24T12:00:00+08:00",
  "idempotencyKey": "callback_abc"
}
```

**事件类型枚举**

| 事件类型 | 说明 |
|---|---|
| PROCESS_STARTED | 流程发起 |
| PROCESS_COMPLETED | 流程完成 |
| PROCESS_REJECTED | 流程被拒绝 |
| PROCESS_TERMINATED | 流程被终止 |
| TASK_CREATED | 任务创建 |
| TASK_COMPLETED | 任务完成 |
| TASK_TRANSFERRED | 任务转交 |

## 5.4 接口安全

- 所有接口必须使用 HTTPS；
- Token 有效期默认 2 小时，支持 RefreshToken；
- 回调请求需签名验证；
- 写接口必须携带幂等键；
- 敏感接口限制访问频率。
