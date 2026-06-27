# Spec 目录索引（Agent 阅读指南）

> 本文件为 Agent 提供 `spec/` 目录的快速导航。在处理相关任务前，请根据任务类型优先阅读对应规范。

## 目录结构

```text
spec/
├── agent.md                    # 本文件：目录索引与阅读指南
├── global/                     # 全局规范：跨模块的技术与业务约束
│   ├── overview.md             # 项目背景、目标、范围、术语表
│   ├── users-and-scenarios.md  # 用户角色、使用场景、权限矩阵
│   ├── non-functional.md       # 性能、安全、可用性、扩展性等非功能需求
│   ├── api.md                  # 接口设计总则、统一响应、核心 API、回调格式
│   ├── data-model.md           # 核心实体、ER 图、关键字段、索引设计
│   ├── integration.md          # 与 HR/ERP/CRM/SSO/消息/存储等集成方式
│   ├── deployment.md           # 微服务部署架构、服务划分、高可用设计
│   ├── appendices.md           # 状态码、枚举值、权限矩阵、修订记录
│   ├── i18n.md                 # 系统国际化设计规范（前端/后端）
│   └── table-index-column.md   # 前端列表展示规范（无 ID、首列序号）
└── modules/                    # 模块规范：各功能模块的详细需求与业务规则
    ├── M1-流程设计器核心.md
    ├── M2-表单设计器.md
    ├── M3-审批节点配置.md
    ├── M4-流程控制与网关.md
    ├── M5-BPMN事件体系.md
    ├── M6-高级审批功能.md
    ├── M7-超时与催办.md
    ├── M8-流程版本管理.md
    ├── M9-中台集成与开放能力.md
    ├── M10-组织架构集成.md
    ├── M11-数据权限与安全.md
    ├── M12-流程监控与运营.md
    ├── M13-打印与签章.md
    ├── M14-系统管理与配置.md
    └── M15-BPM流程定义使用手册.md
```

## 按任务类型推荐阅读

| 任务类型 | 推荐阅读 |
|---|---|
| 首次了解项目背景、目标、术语 | [global/overview.md](./global/overview.md) |
| 理解用户角色、权限、使用场景 | [global/users-and-scenarios.md](./global/users-and-scenarios.md) |
| 编码前了解性能/安全/扩展性约束 | [global/non-functional.md](./global/non-functional.md) |
| 开发/调用 REST API | [global/api.md](./global/api.md) |
| 数据库设计、表结构、索引设计 | [global/data-model.md](./global/data-model.md) |
| 与外部系统（HR/ERP/CRM/SSO）集成 | [global/integration.md](./global/integration.md) + [M9-中台集成与开放能力.md](./modules/M9-中台集成与开放能力.md) |
| 部署、运维、扩容、高可用 | [global/deployment.md](./global/deployment.md) |
| 国际化改造 | [global/i18n.md](./global/i18n.md) |
| 前端列表页改造/新增列表页 | [global/table-index-column.md](./global/table-index-column.md) |
| 流程设计器、BPMN 建模 | [M1-流程设计器核心.md](./modules/M1-流程设计器核心.md) |
| 表单设计、组件、校验、联动 | [M2-表单设计器.md](./modules/M2-表单设计器.md) |
| 审批人、审批方式、退回、加签等 | [M3-审批节点配置.md](./modules/M3-审批节点配置.md) |
| 网关、条件表达式、子流程、循环 | [M4-流程控制与网关.md](./modules/M4-流程控制与网关.md) |
| BPMN 事件、定时器、边界事件 | [M5-BPMN事件体系.md](./modules/M5-BPMN事件体系.md) |
| 加签、转交、委托、批量审批、任务池 | [M6-高级审批功能.md](./modules/M6-高级审批功能.md) |
| 超时提醒、催办、SLA、升级 | [M7-超时与催办.md](./modules/M7-超时与催办.md) |
| 版本发布、回滚、灰度、差异对比 | [M8-流程版本管理.md](./modules/M8-流程版本管理.md) |
| 多租户、连接器、Webhook、数据映射 | [M9-中台集成与开放能力.md](./modules/M9-中台集成与开放能力.md) |
| 组织架构同步、主管链、角色、标签 | [M10-组织架构集成.md](./modules/M10-组织架构集成.md) |
| 字段权限、数据脱敏、加密、审计 | [M11-数据权限与安全.md](./modules/M11-数据权限与安全.md) |
| 运营大盘、异常检测、效率/瓶颈分析 | [M12-流程监控与运营.md](./modules/M12-流程监控与运营.md) |
| 打印模板、电子签名、手写签名 | [M13-打印与签章.md](./modules/M13-打印与签章.md) |
| 流程分类、通知模板、工作日历、归档 | [M14-系统管理与配置.md](./modules/M14-系统管理与配置.md) |
| 流程定义实际操作、办理人配置、会签、条件分支、版本管理 | [M15-BPM流程定义使用手册.md](./modules/M15-BPM流程定义使用手册.md) |

## 快速参考

- **状态码与枚举**：见 [global/appendices.md](./global/appendices.md)。
- **接口基础约定**：协议、鉴权 Header、幂等、版本前缀见 [global/api.md](./global/api.md) 第 1 节。
- **BPMN 属性命名空间**：所有办理人/候选人/候选组属性使用 `flowable:` 命名空间（如 `flowable:assignee`）。
- **启动流程指定办理人**：通过流程变量 `approvalAssignee` 传入用户 ID（如 `1`）。
