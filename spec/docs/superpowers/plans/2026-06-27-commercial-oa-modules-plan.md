# 商用 OA 缺失模块规范补齐实施计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 在 `modules/todo_modules/` 下创建 12 个新增商用 OA 模块和 1 个现有模块增量更新的规范文档与需求文档，使现有「流程中台」规范扩展为完整商用 OA 产品规范。

**Architecture:** 每个新增模块一个文件夹，内含 `spec.md`（技术规范）与 `prd.md`（产品需求文档）；现有模块扩展使用 `modified_` 前缀文件夹，内含 `delta-spec.md`（增量规范）。所有文档遵循现有 `modules/流程管理/M*.md` 风格，统一 UTF-8 编码，审批类场景复用现有流程引擎。

**Tech Stack:** Markdown, UTF-8

## Global Constraints

- 所有文本文件必须使用 UTF-8 编码。
- 禁止以 GBK/GB2312/ANSI 等编码保存或覆盖项目文档。
- 新增模块文件夹统一使用 `todo_` 前缀。
- 现有模块更新文件夹统一使用 `modified_` 前缀。
- 每个新增模块文件夹必须包含 `spec.md` 和 `prd.md`。
- 现有模块更新文件夹必须包含 `delta-spec.md`。
- 模块规范 `spec.md` 结构须与现有 `modules/流程管理/M*.md` 保持一致：模块概述、功能架构、功能列表、详细功能需求、用例描述、状态机/时序图、数据模型、接口清单。
- 产品需求文档 `prd.md` 结构须包含：背景与目标、目标用户、用户故事、业务流程、功能详述、原型描述、验收标准、依赖与风险。
- 审批类场景（如请假、报销、资产领用）须复用现有流程管理模块能力，不重复建设审批流。
- 全局规范 `api.md`、`data-model.md`、`non-functional.md`、`users-and-scenarios.md`、`table-index-column.md` 继续适用。

---

## 文件结构总览

创建后的目录结构如下：

```text
modules/
├── 流程管理/
│   ├── M1-流程设计器核心.md
│   ├── ...
│   └── M15-BPM流程定义使用手册.md
└── todo_modules/
    ├── todo_门户工作台/
    │   ├── spec.md
    │   └── prd.md
    ├── todo_消息通知中心/
    │   ├── spec.md
    │   └── prd.md
    ├── todo_日程管理/
    │   ├── spec.md
    │   └── prd.md
    ├── todo_会议管理/
    │   ├── spec.md
    │   └── prd.md
    ├── todo_文档管理/
    │   ├── spec.md
    │   └── prd.md
    ├── todo_知识库/
    │   ├── spec.md
    │   └── prd.md
    ├── todo_公告通知/
    │   ├── spec.md
    │   └── prd.md
    ├── todo_通讯录/
    │   ├── spec.md
    │   └── prd.md
    ├── todo_任务协作/
    │   ├── spec.md
    │   └── prd.md
    ├── todo_考勤管理/
    │   ├── spec.md
    │   └── prd.md
    ├── todo_费用报销/
    │   ├── spec.md
    │   └── prd.md
    ├── todo_资产管理/
    │   ├── spec.md
    │   └── prd.md
    └── modified_系统管理与配置/
        └── delta-spec.md
```

---

### Task 1: 创建目录结构

**Files:**
- Create: `modules/todo_modules/todo_门户工作台/`
- Create: `modules/todo_modules/todo_消息通知中心/`
- Create: `modules/todo_modules/todo_日程管理/`
- Create: `modules/todo_modules/todo_会议管理/`
- Create: `modules/todo_modules/todo_文档管理/`
- Create: `modules/todo_modules/todo_知识库/`
- Create: `modules/todo_modules/todo_公告通知/`
- Create: `modules/todo_modules/todo_通讯录/`
- Create: `modules/todo_modules/todo_任务协作/`
- Create: `modules/todo_modules/todo_考勤管理/`
- Create: `modules/todo_modules/todo_费用报销/`
- Create: `modules/todo_modules/todo_资产管理/`
- Create: `modules/todo_modules/modified_系统管理与配置/`

- [ ] **Step 1: 创建所有模块文件夹**

Run:
```bash
mkdir -p "modules/todo_modules/todo_门户工作台" \
  "modules/todo_modules/todo_消息通知中心" \
  "modules/todo_modules/todo_日程管理" \
  "modules/todo_modules/todo_会议管理" \
  "modules/todo_modules/todo_文档管理" \
  "modules/todo_modules/todo_知识库" \
  "modules/todo_modules/todo_公告通知" \
  "modules/todo_modules/todo_通讯录" \
  "modules/todo_modules/todo_任务协作" \
  "modules/todo_modules/todo_考勤管理" \
  "modules/todo_modules/todo_费用报销" \
  "modules/todo_modules/todo_资产管理" \
  "modules/todo_modules/modified_系统管理与配置"
```

- [ ] **Step 2: 验证目录创建成功**

Run:
```bash
find modules/todo_modules -maxdepth 1 -type d | sort
```

Expected output: 13 个目录，包含 12 个 `todo_` 前缀和 1 个 `modified_` 前缀。

- [ ] **Step 3: Commit**

```bash
git add modules/todo_modules/
git commit -m "chore: create todo_modules directory structure for commercial OA modules"
```

---

### Task 2: 门户工作台模块

**Files:**
- Create: `modules/todo_modules/todo_门户工作台/spec.md`
- Create: `modules/todo_modules/todo_门户工作台/prd.md`

**Interfaces:**
- Consumes: 流程引擎待办数据、消息通知数据、应用权限数据、用户基础信息。
- Produces: 个人工作台页面配置、待办/已办/抄送聚合、快捷入口、数据看板。

- [ ] **Step 1: 编写 `todo_门户工作台/spec.md`**

内容：
```markdown
# 门户工作台

## 模块概述

门户工作台是用户登录系统后的首要入口，面向普通员工、管理者提供个性化聚合页面，集中展示待办事项、消息通知、快捷应用、数据看板等内容。

## 功能架构图

```text
门户工作台
    ├── 个人门户
    │   ├── 待办聚合
    │   ├── 已办/抄送
    │   ├── 消息提醒
    │   └── 日程预览
    ├── 应用中心
    │   ├── 常用应用
    │   ├── 全部应用
    │   └── 应用搜索
    └── 数据看板
        ├── 我的流程统计
        ├── 审批效率
        └── 团队动态
```

## 功能列表

| 编号 | 功能 | 说明 |
|---|---|---|
| PW-1 | 个人门户 | 用户登录后默认首页，按角色展示不同内容 |
| PW-2 | 待办聚合 | 汇总所有流程待办，支持一键跳转审批 |
| PW-3 | 已办/抄送 | 展示已处理事项和抄送我的事项 |
| PW-4 | 消息提醒 | 展示未读消息，支持标记已读 |
| PW-5 | 日程预览 | 展示今日/近三日日程 |
| PW-6 | 应用中心 | 应用入口聚合，支持收藏与搜索 |
| PW-7 | 数据看板 | 个人/团队流程统计与效率指标 |

## 详细功能需求

### PW-1 个人门户

**功能说明**
提供可配置的默认首页，不同角色展示不同布局和内容。

**业务规则**
- 支持管理员配置默认门户布局；
- 用户可在管理员允许范围内调整组件位置和显隐；
- 门户组件支持懒加载；
- 移动端与 PC 端布局独立配置。

**前置条件**
用户已登录且已分配角色。

**后置条件**
用户看到个性化的工作台首页。

**异常处理**
- 组件数据加载失败：展示占位图和重试按钮。

**界面原型描述**
左侧导航 + 右侧卡片式布局，顶部为搜索和快捷操作，中部为待办、消息、日程等卡片。

### PW-2 待办聚合

**功能说明**
聚合来自流程引擎的所有待办任务，按流程类型、紧急程度排序。

**业务规则**
- 默认按到达时间倒序；
- 支持按流程名称、发起人、时间筛选；
- 单页展示 10 条，支持分页；
- 点击待办进入对应审批详情。

**前置条件**
用户有待办任务。

**后置条件**
用户可快速处理待办。

**异常处理**
- 流程引擎不可用：展示友好提示。

### PW-3 已办/抄送

**功能说明**
展示用户已审批事项和抄送事项。

**业务规则**
- 已办展示最近 30 天数据，支持时间筛选；
- 抄送事项标记未读/已读；
- 支持批量标记已读。

### PW-4 消息提醒

**功能说明**
展示系统消息、流程通知、公告等未读消息。

**业务规则**
- 未读消息右上角显示数字角标；
- 支持一键全部已读；
- 消息类型包括：流程待办、流程结果、公告、系统通知。

### PW-5 日程预览

**功能说明**
展示今日及未来三日日程。

**业务规则**
- 点击日程跳转日程详情；
- 无日程时展示空状态。

### PW-6 应用中心

**功能说明**
汇集系统所有应用入口，支持收藏、搜索、分组展示。

**业务规则**
- 应用按权限过滤，无权限应用不显示；
- 用户可收藏常用应用到首页；
- 支持按名称拼音/首字母搜索。

### PW-7 数据看板

**功能说明**
展示个人审批效率、流程发起量、团队平均耗时等指标。

**业务规则**
- 普通员工看个人指标；
- 管理者看团队指标；
- 指标默认展示近 7 天，支持切换 30 天/90 天。

## 用例描述

| 用例编号 | 用例名称 | 参与者 | 基本流程 |
|---|---|---|---|
| UC-PW-01 | 查看待办 | 普通员工 | 登录系统 → 进入工作台 → 查看待办卡片 → 点击处理 |
| UC-PW-02 | 配置首页 | 普通员工 | 进入工作台 → 点击自定义 → 调整组件 → 保存 |
| UC-PW-03 | 查看数据看板 | 部门经理 | 登录系统 → 查看团队效率指标 → 切换时间维度 |

## 状态机/时序图描述

### 门户组件状态机

```text
[未加载] --加载中--> [加载中]
[加载中] --成功--> [已展示]
[加载中] --失败--> [错误]
[错误] --重试--> [加载中]
```

## 数据模型

| 实体 | 说明 | 核心字段 |
|---|---|---|
| oa_portal_layout | 门户布局 | id, user_id, role_id, layout_json, is_default, tenant_id |
| oa_portal_widget | 门户组件 | id, code, name, type, config_schema, sort, status |
| oa_favorite_app | 用户收藏应用 | id, user_id, app_code, sort, tenant_id |

## 接口清单

| 接口 | 方法 | 路径 | 说明 |
|---|---|---|---|
| 获取工作台首页 | GET | /api/v1/oa/portal/home | 返回用户个性化首页数据 |
| 保存布局配置 | POST | /api/v1/oa/portal/layout | 保存用户自定义布局 |
| 获取待办聚合 | GET | /api/v1/oa/portal/todos | 返回聚合待办列表 |
| 获取消息提醒 | GET | /api/v1/oa/portal/messages | 返回未读消息列表 |
| 获取应用中心 | GET | /api/v1/oa/portal/apps | 返回有权限的应用列表 |
| 收藏/取消应用 | POST | /api/v1/oa/portal/apps/favorite | 收藏或取消收藏应用 |
| 获取数据看板 | GET | /api/v1/oa/portal/dashboard | 返回个人/团队指标 |
```

- [ ] **Step 2: 编写 `todo_门户工作台/prd.md`**

内容：
```markdown
# 门户工作台产品需求文档

## 1. 背景与目标

### 1.1 背景

OA 系统功能繁多，用户登录后需要快速找到待办、消息和常用应用。现有系统缺乏统一的个人工作台，导致用户需要在多个菜单间切换。

### 1.2 目标

建设个性化门户工作台，聚合待办、消息、日程、应用和数据看板，提升用户办公效率。

## 2. 目标用户

| 用户角色 | 主要诉求 |
|---|---|
| 普通员工 | 快速处理待办、查看消息、进入常用应用 |
| 部门经理 | 查看团队审批效率、掌握团队动态 |
| 系统管理员 | 配置默认门户布局、管理门户组件 |

## 3. 用户故事

- **作为** 普通员工，**我希望** 登录后第一眼看到所有待办，**以便** 快速处理审批。
- **作为** 部门经理，**我希望** 工作台展示团队效率指标，**以便** 识别审批瓶颈。
- **作为** 系统管理员，**我希望** 按角色配置默认工作台布局，**以便** 统一员工体验。

## 4. 业务流程

```text
用户登录
  └── 系统根据用户角色加载默认门户布局
        └── 各组件异步加载数据
              ├── 待办组件 → 调用流程引擎
              ├── 消息组件 → 调用消息中心
              ├── 日程组件 → 调用日程管理
              └── 看板组件 → 调用报表服务
```

## 5. 功能详述

### 5.1 个人门户

- 默认首页，展示待办、消息、日程、看板等卡片。
- 支持用户自定义组件顺序和显隐（在管理员允许范围内）。

### 5.2 待办聚合

- 汇总所有流程待办，按到达时间倒序。
- 支持按流程名称、发起人筛选。

### 5.3 应用中心

- 展示用户有权限的所有应用。
- 支持收藏常用应用到首页快捷区。
- 支持按名称搜索应用。

### 5.4 数据看板

- 普通员工：我的待办数、已办数、平均审批耗时。
- 部门经理：团队发起量、通过率、人均审批耗时。

## 6. 原型描述

### PC 端工作台

- 顶部：全局搜索 + 消息图标 + 用户头像。
- 左侧：固定导航菜单。
- 中部：卡片式布局，两行三列。
  - 第一行：待办聚合（2/3 宽度）、消息提醒（1/3 宽度）。
  - 第二行：日程预览（1/3 宽度）、应用中心（1/3 宽度）、数据看板（1/3 宽度）。

### 移动端工作台

- 顶部：待办、已办、抄送快捷 Tab。
- 中部：常用应用宫格。
- 底部：消息、日程、我的。

## 7. 验收标准

- [ ] 用户登录后默认进入工作台首页。
- [ ] 工作台展示用户个性化布局，组件数据加载时间 < 2s。
- [ ] 待办聚合支持按流程名称、发起人筛选。
- [ ] 应用中心按权限过滤，无权限应用不可见。
- [ ] 用户可收藏/取消收藏应用，首页快捷区实时更新。
- [ ] 数据看板指标与个人/角色匹配。
- [ ] 移动端与 PC 端布局独立适配。

## 8. 依赖与风险

### 依赖

- 流程引擎待办接口；
- 消息中心未读消息接口；
- 日程管理接口；
- 用户权限体系。

### 风险

- 多源数据聚合可能导致首页加载慢，需做好异步加载和缓存。
```

- [ ] **Step 3: 验证文件存在且为 UTF-8**

Run:
```bash
file -i modules/todo_modules/todo_门户工作台/spec.md modules/todo_modules/todo_门户工作台/prd.md
```

Expected output: 两个文件均为 `text/plain; charset=utf-8`。

- [ ] **Step 4: Commit**

```bash
git add modules/todo_modules/todo_门户工作台/
git commit -m "docs: add portal workbench spec and prd"
```

---

### Task 3: 消息通知中心模块

**Files:**
- Create: `modules/todo_modules/todo_消息通知中心/spec.md`
- Create: `modules/todo_modules/todo_消息通知中心/prd.md`

**Interfaces:**
- Consumes: 流程事件、公告内容、系统告警、用户联系方式、第三方通道配置。
- Produces: 站内信、邮件、短信、IM（钉钉/企微/飞书）消息、消息模板、订阅规则。

- [ ] **Step 1: 编写 `todo_消息通知中心/spec.md`**

内容：
```markdown
# 消息通知中心

## 模块概述

消息通知中心为全系统提供统一的消息发送、接收、存储、检索能力，支持多渠道（站内信、邮件、短信、IM）和多种消息类型（流程通知、公告、系统通知、告警）。

## 功能架构图

```text
消息通知中心
    ├── 消息发送
    │   ├── 站内信
    │   ├── 邮件
    │   ├── 短信
    │   └── IM 消息
    ├── 消息模板
    │   ├── 模板管理
    │   └── 变量引擎
    ├── 消息接收
    │   ├── 消息收件箱
    │   ├── 已读/未读
    │   └── 消息删除
    ├── 订阅管理
    │   ├── 订阅项
    │   └── 渠道偏好
    └── 发送记录
        ├── 发送日志
        └── 失败重试
```

## 功能列表

| 编号 | 功能 | 说明 |
|---|---|---|
| NC-1 | 站内信 | 系统内消息收发，支持富文本 |
| NC-2 | 邮件通知 | 通过 SMTP 发送邮件 |
| NC-3 | 短信通知 | 通过短信网关发送短信 |
| NC-4 | IM 通知 | 对接钉钉/企微/飞书机器人 |
| NC-5 | 消息模板 | 模板化消息内容，支持变量 |
| NC-6 | 消息收件箱 | 用户查看接收到的消息 |
| NC-7 | 订阅管理 | 用户配置接收渠道和开关 |
| NC-8 | 发送记录 | 管理员查看发送日志和重试 |

## 详细功能需求

### NC-1 站内信

**功能说明**
在系统内发送和接收消息，支持文本、富文本、链接。

**业务规则**
- 站内信默认保存 1 年，到期按 M14.7 策略归档；
- 单用户消息列表按时间倒序；
- 支持单条/批量标记已读；
- 支持消息撤回（发送后 5 分钟内）。

### NC-2 邮件通知

**功能说明**
通过配置的 SMTP 服务发送邮件。

**业务规则**
- 支持 HTML 和纯文本；
- 支持附件（单邮件最大 20MB）；
- 发送失败进入重试队列，最多重试 3 次；
- 邮件模板按租户隔离。

### NC-3 短信通知

**功能说明**
通过短信网关发送短信。

**业务规则**
- 短信内容长度按运营商规则拆分；
- 发送记录保存 6 个月；
- 支持验证码、通知类短信分类。

### NC-4 IM 通知

**功能说明**
对接钉钉、企业微信、飞书等 IM 平台的机器人或应用消息接口。

**业务规则**
- 支持按用户绑定 IM 账号；
- 支持文本、Markdown、卡片消息；
- 发送失败记录日志并告警。

### NC-5 消息模板

**功能说明**
统一管理各类消息模板，支持变量替换。

**业务规则**
- 变量语法 `{{变量名}}`；
- 同一模板支持多渠道内容配置；
- 模板版本化管理，修改后需发布生效。

### NC-6 消息收件箱

**功能说明**
用户查看所有接收到的消息。

**业务规则**
- 支持按类型、时间、已读状态筛选；
- 支持关键词搜索；
- 未读消息显示数字角标。

### NC-7 订阅管理

**功能说明**
用户配置各类消息是否接收及接收渠道。

**业务规则**
- 订阅项由管理员预定义；
- 用户可关闭非强制类通知；
- 渠道偏好支持多选（站内信+邮件+IM）。

### NC-8 发送记录

**功能说明**
管理员查看消息发送记录，支持重试失败消息。

**业务规则**
- 记录接收人、渠道、内容摘要、状态、时间；
- 仅管理员可查看全量记录；
- 失败消息支持手动重试。

## 用例描述

| 用例编号 | 用例名称 | 参与者 | 基本流程 |
|---|---|---|---|
| UC-NC-01 | 发送流程通知 | 流程引擎 | 流程节点触发 → 选择模板 → 替换变量 → 按渠道发送 |
| UC-NC-02 | 查看消息 | 普通员工 | 进入消息中心 → 查看未读 → 点击阅读 |
| UC-NC-03 | 配置订阅 | 普通员工 | 进入订阅管理 → 关闭某类邮件通知 → 保存 |

## 状态机/时序图描述

### 消息发送状态机

```text
[待发送] --发送--> [发送中]
[发送中] --成功--> [已送达]
[发送中] --失败--> [失败]
[失败] --重试--> [发送中]
[失败] --超过重试--> [发送终止]
```

## 数据模型

| 实体 | 说明 | 核心字段 |
|---|---|---|
| oa_message | 消息主表 | id, type, title, content, sender, priority, tenant_id, create_time |
| oa_message_recipient | 消息接收人 | id, message_id, user_id, channel, status, read_time, tenant_id |
| oa_message_template | 消息模板 | id, code, name, channels_json, content_json, variables, status, tenant_id |
| oa_message_subscription | 订阅配置 | id, user_id, item_code, channels, enabled, tenant_id |
| oa_message_log | 发送日志 | id, message_id, channel, request, response, status, retry_count, tenant_id |

## 接口清单

| 接口 | 方法 | 路径 | 说明 |
|---|---|---|---|
| 发送消息 | POST | /api/v1/oa/messages/send | 发送单条或多条消息 |
| 获取消息列表 | GET | /api/v1/oa/messages | 当前用户消息列表 |
| 标记已读 | POST | /api/v1/oa/messages/read | 标记消息已读 |
| 获取订阅配置 | GET | /api/v1/oa/messages/subscriptions | 当前用户订阅配置 |
| 更新订阅配置 | PUT | /api/v1/oa/messages/subscriptions | 更新订阅配置 |
| 获取发送记录 | GET | /api/v1/oa/messages/logs | 管理员查询发送日志 |
| 重试发送 | POST | /api/v1/oa/messages/{id}/retry | 重试失败消息 |
```

- [ ] **Step 2: 编写 `todo_消息通知中心/prd.md`**

内容：
```markdown
# 消息通知中心产品需求文档

## 1. 背景与目标

### 1.1 背景

现有系统各模块各自发送通知，导致消息分散、模板不统一、用户订阅配置困难，且无法统一追踪发送状态。

### 1.2 目标

建设统一消息通知中心，集中管理消息模板、发送渠道、用户订阅和发送记录，为流程、公告、系统等提供统一消息能力。

## 2. 目标用户

| 用户角色 | 主要诉求 |
|---|---|
| 普通员工 | 在一个地方查看所有消息，能控制接收渠道 |
| 流程管理员 | 配置流程通知模板 |
| 系统管理员 | 监控消息发送状态，排查发送失败 |

## 3. 用户故事

- **作为** 普通员工，**我希望** 所有通知集中在一个收件箱，**以便** 不遗漏重要消息。
- **作为** 系统管理员，**我希望** 查看消息发送日志，**以便** 排查发送失败原因。
- **作为** 流程管理员，**我希望** 自定义通知模板内容，**以便** 统一企业通知风格。

## 4. 业务流程

```text
消息触发
  └── 消息中心接收请求
        ├── 选择消息模板
        ├── 替换变量
        ├── 查询用户订阅偏好
        └── 按渠道发送
              ├── 站内信 → 写入收件箱
              ├── 邮件 → SMTP 发送
              ├── 短信 → 短信网关
              └── IM → 钉钉/企微/飞书接口
```

## 5. 功能详述

### 5.1 消息收件箱

- 展示所有接收到的消息，支持按类型筛选。
- 未读消息高亮显示，支持一键标记已读。

### 5.2 消息模板

- 模板按类型（流程待办、流程结果、公告、系统通知）分类。
- 支持多渠道内容配置，如站内信、邮件、短信可分别编辑。

### 5.3 订阅管理

- 用户可开启/关闭某类消息，可选择接收渠道。
- 强制类通知（如安全告警）不可关闭。

### 5.4 发送记录

- 管理员可查看所有发送记录，包括成功、失败、重试状态。
- 支持按时间、接收人、渠道、状态筛选。

## 6. 原型描述

### 消息中心页面

- 左侧：全部消息、未读消息、已读消息、发送记录（管理员）。
- 右侧：消息列表，展示标题、类型、时间、已读状态。
- 顶部：搜索框、标记全部已读按钮。

### 消息模板管理

- 列表展示模板编码、名称、适用渠道、状态。
- 编辑页支持富文本编辑和变量插入。

### 订阅管理

- 表格展示订阅项、默认渠道、当前用户设置。
- 支持开关和渠道多选。

## 7. 验收标准

- [ ] 支持站内信、邮件、短信、IM 四种消息渠道。
- [ ] 消息模板支持变量替换，修改后发布生效。
- [ ] 用户可在订阅管理中配置接收渠道和开关。
- [ ] 发送失败的消息支持自动重试 3 次和手动重试。
- [ ] 管理员可查看全量发送记录并筛选。
- [ ] 消息收件箱支持按类型、时间、状态筛选和搜索。

## 8. 依赖与风险

### 依赖

- SMTP/短信/IM 第三方通道配置；
- 用户联系方式（手机号、邮箱、IM 账号）；
- 流程引擎事件回调。

### 风险

- 第三方通道不稳定可能导致消息延迟或丢失，需有重试和告警机制。
```

- [ ] **Step 3: 验证文件存在且为 UTF-8**

Run:
```bash
file -i modules/todo_modules/todo_消息通知中心/spec.md modules/todo_modules/todo_消息通知中心/prd.md
```

Expected output: 两个文件均为 `text/plain; charset=utf-8`。

- [ ] **Step 4: Commit**

```bash
git add modules/todo_modules/todo_消息通知中心/
git commit -m "docs: add notification center spec and prd"
```

---

### Task 4: 日程管理模块

**Files:**
- Create: `modules/todo_modules/todo_日程管理/spec.md`
- Create: `modules/todo_modules/todo_日程管理/prd.md`

**Interfaces:**
- Consumes: 用户基础信息、组织架构、会议/请假/出差等业务事件。
- Produces: 日程数据、提醒通知、日历视图。

- [ ] **Step 1: 编写 `todo_日程管理/spec.md`**

内容：
```markdown
# 日程管理

## 模块概述

日程管理为用户提供个人日程创建、共享、提醒和日历视图能力，支持与会议、请假、出差等业务模块联动，自动同步相关日程。

## 功能架构图

```text
日程管理
    ├── 我的日程
    │   ├── 日程创建
    │   ├── 日程编辑
    │   ├── 日程删除
    │   └── 日程共享
    ├── 日历视图
    │   ├── 日视图
    │   ├── 周视图
    │   └── 月视图
    ├── 提醒设置
    │   ├── 提醒时间
    │   └── 提醒方式
    └── 业务联动
        ├── 会议同步
        ├── 请假同步
        └── 出差同步
```

## 功能列表

| 编号 | 功能 | 说明 |
|---|---|---|
| CA-1 | 日程创建 | 创建个人或共享日程 |
| CA-2 | 日程编辑/删除 | 维护日程信息 |
| CA-3 | 日程共享 | 将日程共享给指定人员 |
| CA-4 | 日历视图 | 多维度展示日程 |
| CA-5 | 提醒设置 | 配置日程提醒 |
| CA-6 | 业务联动 | 自动同步会议、请假、出差等日程 |

## 详细功能需求

### CA-1 日程创建

**功能说明**
用户创建日程，填写标题、时间、地点、参与人、备注、提醒方式等。

**业务规则**
- 日程时间不能重叠（可选校验）；
- 支持全天日程；
- 支持周期性日程（按天/周/月）；
- 参与人默认收到日程邀请通知。

### CA-2 日程编辑/删除

**功能说明**
修改或删除已创建日程。

**业务规则**
- 仅创建人可编辑/删除；
- 修改后通知参与人；
- 删除可选择是否通知参与人。

### CA-3 日程共享

**功能说明**
将个人日程共享给其他人查看。

**业务规则**
- 支持共享给个人、部门、角色；
- 共享权限可配置为仅查看或编辑；
- 共享关系按租户隔离。

### CA-4 日历视图

**功能说明**
提供日/周/月三种视图展示日程。

**业务规则**
- 支持切换视图；
- 支持点击日期创建日程；
- 不同来源日程用颜色区分。

### CA-5 提醒设置

**功能说明**
为日程设置提醒时间和方式。

**业务规则**
- 提醒时间支持提前 5/15/30 分钟、1 小时、1 天、自定义；
- 提醒方式支持站内信、邮件、IM；
- 可设置默认提醒偏好。

### CA-6 业务联动

**功能说明**
会议、请假、出差等模块自动在日程中生成对应记录。

**业务规则**
- 业务系统调用日程 API 写入日程；
- 业务状态变更时同步更新日程；
- 用户可在日程中跳转业务详情。

## 用例描述

| 用例编号 | 用例名称 | 参与者 | 基本流程 |
|---|---|---|---|
| UC-CA-01 | 创建日程 | 普通员工 | 进入日程 → 选择时间 → 填写信息 → 保存 |
| UC-CA-02 | 共享日程 | 普通员工 | 选择日程 → 设置共享人 → 保存 |
| UC-CA-03 | 接收日程提醒 | 普通员工 | 日程到期 → 按偏好发送提醒 |

## 状态机/时序图描述

### 日程状态机

```text
[正常] --编辑--> [正常]
[正常] --删除--> [已删除]
[正常] --过期--> [已结束]
```

## 数据模型

| 实体 | 说明 | 核心字段 |
|---|---|---|
| oa_calendar_event | 日程事件 | id, title, start_time, end_time, all_day, location, creator, type, source, source_id, tenant_id |
| oa_calendar_event_attendee | 日程参与人 | id, event_id, user_id, status, tenant_id |
| oa_calendar_share | 日程共享 | id, owner_id, shared_to, permission, tenant_id |
| oa_calendar_reminder | 日程提醒 | id, event_id, remind_before, channel, status, tenant_id |

## 接口清单

| 接口 | 方法 | 路径 | 说明 |
|---|---|---|---|
| 创建日程 | POST | /api/v1/oa/calendar/events | 创建新日程 |
| 更新日程 | PUT | /api/v1/oa/calendar/events/{id} | 更新日程 |
| 删除日程 | DELETE | /api/v1/oa/calendar/events/{id} | 删除日程 |
| 查询日程列表 | GET | /api/v1/oa/calendar/events | 按时间范围查询 |
| 共享日程 | POST | /api/v1/oa/calendar/events/{id}/share | 共享日程 |
| 设置提醒 | POST | /api/v1/oa/calendar/events/{id}/reminder | 设置提醒 |
```

- [ ] **Step 2: 编写 `todo_日程管理/prd.md`**

内容：
```markdown
# 日程管理产品需求文档

## 1. 背景与目标

### 1.1 背景

员工日常需要管理会议、请假、出差等多种时间相关事项，现有系统缺乏统一的日程视图，导致时间安排容易冲突。

### 1.2 目标

提供个人日程管理和日历视图，支持与会议、请假、出差等业务联动，帮助用户合理安排时间。

## 2. 目标用户

| 用户角色 | 主要诉求 |
|---|---|
| 普通员工 | 管理个人日程，接收提醒 |
| 行政人员 | 安排会议、活动日程 |
| 管理者 | 查看团队日程安排 |

## 3. 用户故事

- **作为** 普通员工，**我希望** 在日历上查看所有日程，**以便** 合理安排工作。
- **作为** 行政人员，**我希望** 创建会议日程并邀请参与人，**以便** 统一协调时间。
- **作为** 用户，**我希望** 日程到期前收到提醒，**以便** 不错过重要事项。

## 4. 业务流程

```text
创建日程
  ├── 填写日程信息
  ├── 选择参与人
  ├── 设置提醒
  └── 保存并发送邀请
        └── 参与人收到通知
              └── 参与人确认/拒绝
```

## 5. 功能详述

### 5.1 我的日程

- 支持创建、编辑、删除日程。
- 支持设置全天日程和周期性日程。
- 支持添加地点、备注、附件。

### 5.2 日历视图

- 提供日、周、月三种视图。
- 不同来源日程用不同颜色标识。
- 支持拖拽调整日程时间。

### 5.3 日程共享

- 用户可将日程共享给同事查看。
- 支持设置共享人的查看或编辑权限。

### 5.4 业务联动

- 会议预定后自动写入日程。
- 请假、出差审批通过后自动写入日程。
- 业务变更时同步更新日程状态。

## 6. 原型描述

### PC 端日程页面

- 左侧：日历小部件和日程分类筛选。
- 中部：主日历区域，支持日/周/月切换。
- 右侧：选中日期/日程的详情面板。

### 创建日程弹窗

- 标题、时间、地点、参与人、提醒、备注。
- 高级选项：周期性、共享设置。

## 7. 验收标准

- [ ] 支持日/周/月三种日历视图。
- [ ] 支持创建、编辑、删除日程。
- [ ] 支持周期性日程和全天日程。
- [ ] 支持日程共享和权限控制。
- [ ] 支持会议、请假、出差等业务自动同步日程。
- [ ] 支持多种提醒方式和时间配置。

## 8. 依赖与风险

### 依赖

- 用户组织架构数据；
- 消息通知中心提醒能力；
- 会议、请假、出差等业务模块接口。

### 风险

- 多来源日程同步可能出现时间冲突，需提供冲突提示。
```

- [ ] **Step 3: 验证文件存在且为 UTF-8**

Run:
```bash
file -i modules/todo_modules/todo_日程管理/spec.md modules/todo_modules/todo_日程管理/prd.md
```

Expected output: 两个文件均为 `text/plain; charset=utf-8`。

- [ ] **Step 4: Commit**

```bash
git add modules/todo_modules/todo_日程管理/
git commit -m "docs: add calendar management spec and prd"
```

---

### Task 5: 会议管理模块

**Files:**
- Create: `modules/todo_modules/todo_会议管理/spec.md`
- Create: `modules/todo_modules/todo_会议管理/prd.md`

**Interfaces:**
- Consumes: 会议室资源数据、用户/组织架构、日程管理接口、消息通知中心。
- Produces: 会议预定、会议通知、会议纪要、会议室占用数据。

- [ ] **Step 1: 编写 `todo_会议管理/spec.md`**

内容要点：
- 模块概述：提供会议室资源管理、会议预定、会议通知、会议纪要能力。
- 功能列表：会议室管理、会议预定、周期会议、会议通知、会议纪要、会议签到、占用查询。
- 详细需求：
  - 会议室管理：名称、容量、设备、位置、启用/停用、图片。
  - 会议预定：选择时间、会议室、参与人、提醒方式；冲突检测。
  - 周期会议：按天/周/月重复，支持结束条件。
  - 会议通知：预定、变更、取消、开始前提醒。
  - 会议纪要：会后上传纪要，关联会议。
  - 会议签到：支持扫码/手动签到，统计出席情况。
- 用例：预定会议、取消会议、上传纪要。
- 状态机：待开始 → 进行中 → 已结束；已取消。
- 数据模型：
  - oa_meeting_room：会议室
  - oa_meeting：会议主表
  - oa_meeting_attendee：参与人
  - oa_meeting_minutes：会议纪要
  - oa_meeting_sign_in：签到记录
- 接口：会议室列表、预定会议、查询会议、取消会议、上传纪要、签到。

- [ ] **Step 2: 编写 `todo_会议管理/prd.md`**

内容要点：
- 背景：会议资源冲突、通知不到位、纪要难管理。
- 目标：统一管理会议室和会议，减少冲突，提升会议效率。
- 用户：员工、行政人员、会议组织者。
- 用户故事：快速预定会议室、自动通知参与人、会后上传纪要。
- 业务流程：选择时间 → 检测冲突 → 预定 → 通知 → 开会 → 纪要 → 归档。
- 功能详述：会议室展示、会议预定、周期会议、通知提醒、纪要管理、签到统计。
- 原型：会议室视图（日历格子）、会议列表、预定弹窗。
- 验收标准：冲突检测准确、通知及时、纪要可检索、签到数据可导出。
- 依赖：会议室资源、组织架构、消息通知、日程管理。

- [ ] **Step 3: 验证文件存在且为 UTF-8**

Run:
```bash
file -i modules/todo_modules/todo_会议管理/spec.md modules/todo_modules/todo_会议管理/prd.md
```

Expected output: 两个文件均为 `text/plain; charset=utf-8`。

- [ ] **Step 4: Commit**

```bash
git add modules/todo_modules/todo_会议管理/
git commit -m "docs: add meeting management spec and prd"
```

---

### Task 6: 文档管理模块

**Files:**
- Create: `modules/todo_modules/todo_文档管理/spec.md`
- Create: `modules/todo_modules/todo_文档管理/prd.md`

**Interfaces:**
- Consumes: 用户/组织架构、文件存储服务、权限体系。
- Produces: 文件/文件夹管理、版本控制、权限控制、在线预览、共享链接。

- [ ] **Step 1: 编写 `todo_文档管理/spec.md`**

内容要点：
- 模块概述：提供企业文档的存储、管理、共享、协作能力。
- 功能列表：文件夹管理、文件上传/下载、版本管理、权限控制、在线预览、共享链接、回收站、全文检索。
- 详细需求：
  - 文件夹：支持多级目录、移动、复制、重命名。
  - 文件上传：单文件/批量上传，断点续传，大小限制。
  - 版本管理：保留历史版本，支持回滚。
  - 权限控制：按个人/部门/角色设置读/写/管理权限。
  - 在线预览：Office、PDF、图片、文本。
  - 共享链接：可设密码、有效期、访问次数。
  - 回收站：删除后保留 30 天可恢复。
- 用例：上传文件、共享文件、恢复删除文件。
- 状态机：正常 → 已删除 → 已彻底删除；正常 → 已归档。
- 数据模型：
  - oa_doc_folder：文件夹
  - oa_doc_file：文件主表
  - oa_doc_version：文件版本
  - oa_doc_permission：权限
  - oa_doc_share：共享链接
  - oa_doc_recycle：回收站
- 接口：文件夹 CRUD、文件上传/下载、版本列表、权限设置、共享链接、回收站操作。

- [ ] **Step 2: 编写 `todo_文档管理/prd.md`**

内容要点：
- 背景：企业文档分散、版本混乱、权限难控。
- 目标：建立统一文档管理平台，支持安全共享和协作。
- 用户：全体员工、文档管理员。
- 用户故事：上传工作文档、按权限共享、查看历史版本。
- 业务流程：创建文件夹 → 上传文件 → 设置权限 → 共享/协作 → 归档/删除。
- 功能详述：文件树、上传下载、版本管理、权限矩阵、在线预览、共享外链、回收站。
- 原型：左侧文件树、中间文件列表、右侧详情面板。
- 验收标准：支持大文件上传、版本回滚、权限精确到文件、预览常见格式、共享链接可控。
- 依赖：文件存储、全文检索引擎、在线预览服务。

- [ ] **Step 3: 验证文件存在且为 UTF-8**

Run:
```bash
file -i modules/todo_modules/todo_文档管理/spec.md modules/todo_modules/todo_文档管理/prd.md
```

Expected output: 两个文件均为 `text/plain; charset=utf-8`。

- [ ] **Step 4: Commit**

```bash
git add modules/todo_modules/todo_文档管理/
git commit -m "docs: add document management spec and prd"
```

---

### Task 7: 知识库模块

**Files:**
- Create: `modules/todo_modules/todo_知识库/spec.md`
- Create: `modules/todo_modules/todo_知识库/prd.md`

**Interfaces:**
- Consumes: 用户/组织架构、文档管理文件、标签体系、全文检索。
- Produces: 知识分类、知识词条、收藏、评论、检索结果。

- [ ] **Step 1: 编写 `todo_知识库/spec.md`**

内容要点：
- 模块概述：沉淀企业知识资产，支持分类管理、词条编辑、全文检索、权限控制。
- 功能列表：知识分类、知识词条、富文本编辑、附件、标签、收藏、评论、全文检索、阅读统计、权限管理。
- 详细需求：
  - 分类：多级分类，支持排序和启用/停用。
  - 词条：标题、摘要、正文、附件、标签、分类、作者。
  - 编辑器：富文本编辑，支持图片/视频/表格。
  - 权限：按分类/词条设置读/写/管理权限。
  - 检索：支持标题、正文、标签、附件内容检索。
  - 互动：点赞、收藏、评论。
- 用例：创建词条、检索知识、收藏词条。
- 状态机：草稿 → 已发布 → 已下架；已删除。
- 数据模型：
  - oa_kb_category：知识分类
  - oa_kb_article：知识词条
  - oa_kb_tag：标签
  - oa_kb_article_tag：词条标签关系
  - oa_kb_favorite：收藏
  - oa_kb_comment：评论
- 接口：分类 CRUD、词条 CRUD、搜索、收藏、评论、阅读统计。

- [ ] **Step 2: 编写 `todo_知识库/prd.md`**

内容要点：
- 背景：企业知识分散，重复问题多，新人培训成本高。
- 目标：建立企业知识库，促进知识共享和复用。
- 用户：普通员工、知识管理员、新人。
- 用户故事：快速找到解决方案、沉淀项目经验、学习公司制度。
- 业务流程：创建分类 → 编写词条 → 设置权限 → 发布 → 检索/互动。
- 功能详述：知识首页、分类树、词条编辑、搜索、详情页、收藏评论。
- 原型：左侧分类树、中间词条列表、右侧热门/推荐。
- 验收标准：支持多级分类、富文本编辑、全文检索、权限控制、互动功能。
- 依赖：文档管理、全文检索、用户权限。

- [ ] **Step 3: 验证文件存在且为 UTF-8**

Run:
```bash
file -i modules/todo_modules/todo_知识库/spec.md modules/todo_modules/todo_知识库/prd.md
```

Expected output: 两个文件均为 `text/plain; charset=utf-8`。

- [ ] **Step 4: Commit**

```bash
git add modules/todo_modules/todo_知识库/
git commit -m "docs: add knowledge base spec and prd"
```

---

### Task 8: 公告通知模块

**Files:**
- Create: `modules/todo_modules/todo_公告通知/spec.md`
- Create: `modules/todo_modules/todo_公告通知/prd.md`

**Interfaces:**
- Consumes: 用户/组织架构、消息通知中心、附件存储。
- Produces: 公告内容、阅读记录、置顶公告、分类公告。

- [ ] **Step 1: 编写 `todo_公告通知/spec.md`**

内容要点：
- 模块概述：向组织内用户发布公告通知，支持分类、置顶、阅读确认、附件。
- 功能列表：公告分类、公告发布、公告编辑/下架、置顶、阅读确认、阅读统计、附件。
- 详细需求：
  - 分类：多级分类，如公司公告、部门通知。
  - 发布：标题、内容、分类、可见范围、附件、有效期、是否需阅读确认。
  - 置顶：支持多条置顶，按置顶时间排序。
  - 阅读确认：需确认的公告，用户阅读后标记已读。
  - 统计：查看已读/未读人数和名单。
- 用例：发布公告、阅读公告、查看阅读统计。
- 状态机：草稿 → 已发布 → 已过期 → 已下架；已删除。
- 数据模型：
  - oa_notice_category：公告分类
  - oa_notice：公告主表
  - oa_notice_attachment：附件
  - oa_notice_read：阅读记录
- 接口：公告列表、公告详情、发布公告、下架公告、阅读确认、阅读统计。

- [ ] **Step 2: 编写 `todo_公告通知/prd.md`**

内容要点：
- 背景：企业通知渠道分散，重要信息传达不到位。
- 目标：建立统一公告发布和阅读确认机制。
- 用户：行政人员、HR、管理层、全体员工。
- 用户故事：发布公司制度、确认员工已读、查看通知到达率。
- 业务流程：起草公告 → 设置可见范围 → 发布 → 推送通知 → 用户阅读 → 统计已读。
- 功能详述：公告列表、公告详情、发布公告、阅读确认、阅读统计、置顶管理。
- 原型：公告列表页、公告详情页、发布页、统计页。
- 验收标准：支持可见范围控制、阅读确认、附件、置顶、阅读统计。
- 依赖：消息通知中心、组织架构、文件存储。

- [ ] **Step 3: 验证文件存在且为 UTF-8**

Run:
```bash
file -i modules/todo_modules/todo_公告通知/spec.md modules/todo_modules/todo_公告通知/prd.md
```

Expected output: 两个文件均为 `text/plain; charset=utf-8`。

- [ ] **Step 4: Commit**

```bash
git add modules/todo_modules/todo_公告通知/
git commit -m "docs: add notice management spec and prd"
```

---

### Task 9: 通讯录模块

**Files:**
- Create: `modules/todo_modules/todo_通讯录/spec.md`
- Create: `modules/todo_modules/todo_通讯录/prd.md`

**Interfaces:**
- Consumes: 组织架构数据（sys_user, sys_dept, sys_role）、用户联系方式。
- Produces: 组织架构树、人员搜索、个人通讯录、联系人详情。

- [ ] **Step 1: 编写 `todo_通讯录/spec.md`**

内容要点：
- 模块概述：提供企业组织架构浏览、人员搜索、联系方式查询、个人通讯录管理能力。
- 功能列表：组织架构树、人员搜索、人员详情、个人通讯录、联系人分组、最近联系人。
- 详细需求：
  - 组织架构树：按部门层级展示，支持展开折叠。
  - 人员搜索：按姓名、拼音、部门、岗位、手机号搜索。
  - 人员详情：姓名、部门、岗位、电话、邮箱、IM、头像。
  - 个人通讯录：用户可添加常用联系人。
  - 联系人分组：自定义分组，如项目组成员。
  - 隐私控制：部分联系方式可按权限或用户设置隐藏。
- 用例：查找同事、查看联系方式、添加常用联系人。
- 状态机：正常 → 离职（不可见/只读）。
- 数据模型：
  - 复用 sys_user, sys_dept, sys_post, sys_role。
  - oa_contact：个人通讯录
  - oa_contact_group：联系人分组
  - oa_recent_contact：最近联系人
- 接口：组织架构树、人员列表、人员详情、搜索、个人通讯录 CRUD。

- [ ] **Step 2: 编写 `todo_通讯录/prd.md`**

内容要点：
- 背景：员工查找同事联系方式效率低，组织架构不透明。
- 目标：提供统一通讯录，快速找到同事和联系方式。
- 用户：全体员工。
- 用户故事：查找同事手机号、查看部门架构、保存项目组成员。
- 业务流程：进入通讯录 → 搜索/浏览部门 → 查看详情 → 拨打电话/发送消息。
- 功能详述：组织架构树、搜索、详情页、个人通讯录、最近联系人。
- 原型：左侧部门树、中间人员列表、右侧详情面板。
- 验收标准：支持组织架构浏览、多维度搜索、个人通讯录、最近联系人、隐私控制。
- 依赖：组织架构数据、用户基础信息。

- [ ] **Step 3: 验证文件存在且为 UTF-8**

Run:
```bash
file -i modules/todo_modules/todo_通讯录/spec.md modules/todo_modules/todo_通讯录/prd.md
```

Expected output: 两个文件均为 `text/plain; charset=utf-8`。

- [ ] **Step 4: Commit**

```bash
git add modules/todo_modules/todo_通讯录/
git commit -m "docs: add address book spec and prd"
```

---

### Task 10: 任务协作模块

**Files:**
- Create: `modules/todo_modules/todo_任务协作/spec.md`
- Create: `modules/todo_modules/todo_任务协作/prd.md`

**Interfaces:**
- Consumes: 用户/组织架构、消息通知中心、文件存储。
- Produces: 任务数据、看板视图、甘特图、任务统计、协作评论。

- [ ] **Step 1: 编写 `todo_任务协作/spec.md`**

内容要点：
- 模块概述：提供任务创建、分配、跟踪、协作能力，支持看板视图和基础项目管理。
- 功能列表：任务创建、任务分配、任务状态、优先级、截止日期、子任务、标签、看板、甘特图、评论、附件、任务统计。
- 详细需求：
  - 任务：标题、描述、负责人、参与人、起止时间、优先级、状态、标签。
  - 状态：待处理、进行中、已完成、已取消。
  - 看板：按状态拖拽切换，支持自定义列。
  - 甘特图：按时间轴展示任务。
  - 评论/附件：任务内协作。
  - 提醒：截止前提醒。
- 用例：创建任务、分配任务、更新状态、查看看板。
- 状态机：待处理 → 进行中 → 已完成；待处理 → 已取消。
- 数据模型：
  - oa_task：任务主表
  - oa_task_member：任务成员
  - oa_task_subtask：子任务
  - oa_task_comment：评论
  - oa_task_attachment：附件
  - oa_task_board：看板
- 接口：任务 CRUD、看板数据、甘特图数据、评论、统计。

- [ ] **Step 2: 编写 `todo_任务协作/prd.md`**

内容要点：
- 背景：团队任务分配和跟踪依赖线下工具，进度不透明。
- 目标：提供轻量级任务协作工具，支持看板和甘特图。
- 用户：团队成员、项目经理、部门经理。
- 用户故事：分配任务给同事、跟踪任务进度、通过看板管理团队工作。
- 业务流程：创建任务 → 分配负责人 → 设置截止时间 → 跟踪状态 → 完成归档。
- 功能详述：任务列表、任务详情、看板、甘特图、评论、附件、统计。
- 原型：看板视图为主，支持列表/甘特图切换。
- 验收标准：支持任务全生命周期、看板拖拽、甘特图、评论协作、截止提醒。
- 依赖：用户权限、消息通知、文件存储。

- [ ] **Step 3: 验证文件存在且为 UTF-8**

Run:
```bash
file -i modules/todo_modules/todo_任务协作/spec.md modules/todo_modules/todo_任务协作/prd.md
```

Expected output: 两个文件均为 `text/plain; charset=utf-8`。

- [ ] **Step 4: Commit**

```bash
git add modules/todo_modules/todo_任务协作/
git commit -m "docs: add task collaboration spec and prd"
```

---

### Task 11: 考勤管理模块

**Files:**
- Create: `modules/todo_modules/todo_考勤管理/spec.md`
- Create: `modules/todo_modules/todo_考勤管理/prd.md`

**Interfaces:**
- Consumes: 用户/组织架构、流程引擎（请假/加班/出差审批）、打卡设备/定位服务、工作日历。
- Produces: 考勤记录、排班数据、请假/加班/出差记录、考勤统计报表。

- [ ] **Step 1: 编写 `todo_考勤管理/spec.md`**

内容要点：
- 模块概述：提供考勤打卡、排班、请假/加班/出差管理、考勤统计能力，审批类操作复用流程引擎。
- 功能列表：考勤组、排班、打卡（GPS/WiFi/考勤机）、请假管理、加班管理、出差管理、外勤、补卡、考勤统计、异常处理。
- 详细需求：
  - 考勤组：按部门/人员设置考勤规则、班次、打卡方式。
  - 排班：支持固定班、轮班、自由工时。
  - 打卡：支持 GPS、WiFi、考勤机、人脸识别；支持多次打卡。
  - 请假/加班/出差：通过流程引擎发起审批，审批结果同步考勤。
  - 外勤：外出打卡需填写事由和地点。
  - 补卡：漏打卡可申请补卡，走审批流程。
  - 统计：生成月考勤报表，包含出勤、迟到、早退、缺勤、请假、加班等。
- 用例：每日打卡、申请请假、查看考勤月报。
- 状态机：正常 → 迟到 → 早退 → 缺勤；可申请补卡 → 审批中 → 已通过。
- 数据模型：
  - oa_attendance_group：考勤组
  - oa_attendance_shift：班次
  - oa_attendance_schedule：排班
  - oa_attendance_record：打卡记录
  - oa_attendance_leave：请假记录（同步流程实例）
  - oa_attendance_overtime：加班记录
  - oa_attendance_trip：出差记录
  - oa_attendance_makeup：补卡申请
  - oa_attendance_monthly：月度统计
- 接口：打卡、打卡记录查询、请假/加班/出差记录、补卡申请、考勤统计。

- [ ] **Step 2: 编写 `todo_考勤管理/prd.md`**

内容要点：
- 背景：企业考勤管理依赖人工统计，效率低且易出错。
- 目标：实现自动化考勤管理，支持多种打卡方式和审批联动。
- 用户：员工、HR、部门经理。
- 用户故事：手机打卡、申请请假、查看考勤异常、导出考勤报表。
- 业务流程：设置考勤组 → 员工打卡 → 异常申请 → 审批 → 生成月报。
- 功能详述：考勤组设置、排班、打卡、请假/加班/出差申请、补卡、统计报表。
- 原型：打卡页面、考勤日历、申请列表、统计报表页。
- 验收标准：支持多打卡方式、与请假/加班流程联动、自动生成月报、异常提醒。
- 依赖：流程引擎、工作日历、定位服务、组织架构。

- [ ] **Step 3: 验证文件存在且为 UTF-8**

Run:
```bash
file -i modules/todo_modules/todo_考勤管理/spec.md modules/todo_modules/todo_考勤管理/prd.md
```

Expected output: 两个文件均为 `text/plain; charset=utf-8`。

- [ ] **Step 4: Commit**

```bash
git add modules/todo_modules/todo_考勤管理/
git commit -m "docs: add attendance management spec and prd"
```

---

### Task 12: 费用报销模块

**Files:**
- Create: `modules/todo_modules/todo_费用报销/spec.md`
- Create: `modules/todo_modules/todo_费用报销/prd.md`

**Interfaces:**
- Consumes: 用户/组织架构、流程引擎（报销审批）、发票识别服务、预算数据、支付/财务系统。
- Produces: 报销单、费用明细、发票数据、预算占用、支付申请。

- [ ] **Step 1: 编写 `todo_费用报销/spec.md`**

内容要点：
- 模块概述：提供费用申请、报销单管理、发票识别、费用标准控制、预算控制、审批流转能力。
- 功能列表：费用类型、费用标准、报销单、发票管理、发票识别、预算控制、借款/还款、审批流程、支付状态、统计报表。
- 详细需求：
  - 费用类型：差旅、招待、办公、交通等，支持多级分类。
  - 费用标准：按职级/部门/城市设置限额，超标准需特殊审批。
  - 报销单：关联发票、填写费用明细、选择审批流程。
  - 发票：支持 OCR 识别、扫码验真、发票查重。
  - 预算控制：报销时校验预算余额，超预算预警或禁止提交。
  - 借款/还款：关联借款单，报销时自动冲销。
  - 审批：通过流程引擎发起，支持多级审批和财务审核。
  - 支付：财务审核通过后生成付款申请。
- 用例：提交报销单、识别发票、查看报销进度、财务付款。
- 状态机：草稿 → 审批中 → 财务审核 → 已付款 → 已完成；可退回/拒绝。
- 数据模型：
  - oa_expense_category：费用类型
  - oa_expense_standard：费用标准
  - oa_expense_report：报销单
  - oa_expense_item：费用明细
  - oa_expense_invoice：发票
  - oa_expense_budget：预算
  - oa_expense_loan：借款单
  - oa_expense_repayment：还款记录
- 接口：报销单 CRUD、发票识别、预算查询、借款查询、统计报表。

- [ ] **Step 2: 编写 `todo_费用报销/prd.md`**

内容要点：
- 背景：报销流程繁琐，发票管理混乱，预算控制滞后。
- 目标：实现费用报销全流程线上化，提升财务审核效率。
- 用户：员工、财务人员、审批人、管理者。
- 用户故事：拍照识别发票、自动校验费用标准、实时查看报销进度。
- 业务流程：填写报销单 → 上传发票 → 提交审批 → 多级审批 → 财务审核 → 付款。
- 功能详述：报销单填写、发票识别、费用标准校验、预算控制、审批跟踪、付款管理、统计报表。
- 原型：报销单列表、报销单详情、发票上传页、审批进度页、统计页。
- 验收标准：支持 OCR 识别、发票查重、费用标准控制、预算校验、审批联动。
- 依赖：流程引擎、OCR 服务、发票验真服务、预算数据。

- [ ] **Step 3: 验证文件存在且为 UTF-8**

Run:
```bash
file -i modules/todo_modules/todo_费用报销/spec.md modules/todo_modules/todo_费用报销/prd.md
```

Expected output: 两个文件均为 `text/plain; charset=utf-8`。

- [ ] **Step 4: Commit**

```bash
git add modules/todo_modules/todo_费用报销/
git commit -m "docs: add expense reimbursement spec and prd"
```

---

### Task 13: 资产管理模块

**Files:**
- Create: `modules/todo_modules/todo_资产管理/spec.md`
- Create: `modules/todo_modules/todo_资产管理/prd.md`

**Interfaces:**
- Consumes: 用户/组织架构、流程引擎（领用/调拨/报废审批）、库存/财务系统。
- Produces: 资产台账、领用/归还记录、盘点数据、维修/报废记录、资产统计。

- [ ] **Step 1: 编写 `todo_资产管理/spec.md`**

内容要点：
- 模块概述：管理企业固定资产的全生命周期，包括采购入库、领用、调拨、维修、盘点、报废。
- 功能列表：资产分类、资产台账、资产入库、领用申请、归还、调拨、维修、盘点、报废、资产标签/二维码、统计报表。
- 详细需求：
  - 资产分类：多级分类，如电子设备、办公家具。
  - 资产台账：资产编码、名称、规格、型号、购置日期、价值、存放位置、使用人、状态。
  - 入库：录入资产信息，生成资产编码和二维码。
  - 领用/归还：通过流程引擎审批，审批通过后更新使用人。
  - 调拨：资产在部门/人员间转移，需审批。
  - 维修：记录维修申请、维修记录、费用。
  - 盘点：创建盘点任务，扫码盘点，生成盘点差异。
  - 报废：达到使用年限或损坏的资产申请报废，走审批流程。
- 用例：资产入库、申请领用、资产盘点、报废申请。
- 状态机：闲置 → 在用 → 维修中 → 报废；闲置 → 已报废。
- 数据模型：
  - oa_asset_category：资产分类
  - oa_asset：资产台账
  - oa_asset_receive：领用记录
  - oa_asset_return：归还记录
  - oa_asset_transfer：调拨记录
  - oa_asset_repair：维修记录
  - oa_asset_inventory：盘点任务
  - oa_asset_inventory_item：盘点明细
  - oa_asset_scrap：报废记录
- 接口：资产 CRUD、领用/归还/调拨/维修/盘点/报废操作、统计报表。

- [ ] **Step 2: 编写 `todo_资产管理/prd.md`**

内容要点：
- 背景：企业资产分散，台账不清，盘点困难。
- 目标：建立资产全生命周期管理，实现账实一致。
- 用户：行政人员、资产管理员、员工、财务人员。
- 用户故事：扫码查看资产信息、申请领用资产、定期盘点资产、报废老旧设备。
- 业务流程：资产入库 → 领用审批 → 日常使用 → 维修/调拨 → 盘点 → 报废。
- 功能详述：资产台账、入库、领用、归还、调拨、维修、盘点、报废、二维码、统计。
- 原型：资产列表、资产详情、入库页、盘点页、统计页。
- 验收标准：支持资产二维码、全流程审批联动、盘点差异生成、资产状态跟踪。
- 依赖：流程引擎、二维码生成、组织架构。

- [ ] **Step 3: 验证文件存在且为 UTF-8**

Run:
```bash
file -i modules/todo_modules/todo_资产管理/spec.md modules/todo_modules/todo_资产管理/prd.md
```

Expected output: 两个文件均为 `text/plain; charset=utf-8`。

- [ ] **Step 4: Commit**

```bash
git add modules/todo_modules/todo_资产管理/
git commit -m "docs: add asset management spec and prd"
```

---

### Task 14: 系统管理与配置增量更新

**Files:**
- Create: `modules/todo_modules/modified_系统管理与配置/delta-spec.md`

**Interfaces:**
- Consumes: 现有 M14 系统管理与配置规范、全局规范。
- Produces: 面向完整 OA 系统的通用系统管理增量规范。

- [ ] **Step 1: 编写 `modified_系统管理与配置/delta-spec.md`**

内容要点：
- 更新范围：现有 M14 面向流程中台，需扩展为通用系统管理，覆盖用户/角色/菜单/参数/字典/日志/租户/应用管理。
- 新增功能点：
  - 用户管理：用户增删改查、导入导出、启用停用、重置密码、角色分配。
  - 角色管理：角色定义、权限分配、数据范围、角色继承。
  - 菜单管理：菜单/按钮权限、路由、图标、排序、启用停用。
  - 参数管理：系统参数配置，支持热更新。
  - 字典管理：数据字典维护。
  - 操作日志：扩展为全系统操作日志，不仅限于流程。
  - 登录日志：记录用户登录/登出。
  - 应用管理：管理 OA 系统内各应用入口和权限。
  - 租户管理：多租户配置（若适用）。
- 修改点：
  - M14.3 管理员设置：从流程管理员扩展为系统级管理员。
  - M14.4 操作日志：从流程配置变更日志扩展为全系统操作日志。
- 数据模型变更：
  - 复用 sys_user, sys_role, sys_menu, sys_dept, sys_post。
  - 新增 oa_system_param, oa_system_dict, oa_login_log, oa_application。
- 接口变更：新增用户/角色/菜单/参数/字典管理接口。
- 兼容性说明：流程相关配置保持原有语义，新增通用管理项不破坏现有流程中台能力。

- [ ] **Step 2: 验证文件存在且为 UTF-8**

Run:
```bash
file -i modules/todo_modules/modified_系统管理与配置/delta-spec.md
```

Expected output: 文件为 `text/plain; charset=utf-8`。

- [ ] **Step 3: Commit**

```bash
git add modules/todo_modules/modified_系统管理与配置/
git commit -m "docs: add system management delta spec"
```

---

### Task 15: 最终验证与整体提交

**Files:**
- Verify: `modules/todo_modules/` 下所有目录和文件。

- [ ] **Step 1: 验证目录结构完整性**

Run:
```bash
find modules/todo_modules -type f -name "*.md" | sort
```

Expected output: 25 个 Markdown 文件，包括 12 个 `todo_` 模块的 `spec.md` 和 `prd.md`，以及 1 个 `modified_` 的 `delta-spec.md`。

- [ ] **Step 2: 验证所有文件为 UTF-8**

Run:
```bash
find modules/todo_modules -type f -name "*.md" -exec file -i {} \; | grep -v "utf-8"
```

Expected output: 无输出（所有文件均为 UTF-8）。

- [ ] **Step 3: 验证文档结构**

Run:
```bash
for f in modules/todo_modules/todo_*/spec.md; do echo "=== $f ==="; head -n 5 "$f"; done
```

Expected output: 每个 spec.md 以 `# 模块名称` 开头，包含模块概述。

- [ ] **Step 4: 更新 agent.md 目录索引**

Modify: `agent.md`

在 `modules/` 目录结构描述后追加 `todo_modules/` 说明：

```markdown
modules/
├── 流程管理/                    # 现有流程中台模块规范
└── todo_modules/                # 商用 OA 缺失模块待补齐规范
    ├── todo_门户工作台/
    ├── todo_消息通知中心/
    ├── todo_日程管理/
    ├── todo_会议管理/
    ├── todo_文档管理/
    ├── todo_知识库/
    ├── todo_公告通知/
    ├── todo_通讯录/
    ├── todo_任务协作/
    ├── todo_考勤管理/
    ├── todo_费用报销/
    ├── todo_资产管理/
    └── modified_系统管理与配置/
```

- [ ] **Step 5: Commit 最终变更**

```bash
git add agent.md
git commit -m "docs: update spec index with todo_modules"
```

---

## 自我审查清单

### 1. Spec 覆盖度

| Spec 要求 | 对应 Task |
|---|---|
| 创建 todo_modules 目录结构 | Task 1 |
| 12 个新增模块各含 spec.md 和 prd.md | Task 2-13 |
| 1 个 modified 增量规范 | Task 14 |
| 命名前缀约定（todo_/modified_） | 所有 Task |
| UTF-8 编码要求 | 所有 Task |
| 更新 agent.md 索引 | Task 15 |

### 2. Placeholder 扫描

- 无 TBD、TODO、implement later、fill in details。
- 每个 Task 包含明确的文件路径、内容要求、验证命令、提交命令。

### 3. 一致性检查

- 所有新增模块文件夹使用 `todo_` 前缀；
- 现有模块更新文件夹使用 `modified_` 前缀；
- 每个 todo 模块包含 `spec.md` 和 `prd.md`；
- modified 模块包含 `delta-spec.md`；
- 所有模块遵循现有 `modules/流程管理/M*.md` 的编写风格。
