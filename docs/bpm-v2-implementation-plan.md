# 企业级统一流程引擎中台 —— 分阶段实施计划

## 目标

基于 `spec/` 目录中的产品需求规格，自研轻量级流程引擎，完整覆盖 M1 ~ M14 共 14 个模块、131 个功能配置项。现有 `ruoyi-bpm` 模块忽略，新建 `ruoyi-bpm-v2` 模块独立演进。

## 实施原则

1. **阶段递进**：先搭建可运行的核心骨架，再逐模块补齐高级能力。
2. **数据先行**：所有功能先落地数据模型与 SQL，再写业务逻辑。
3. **API 驱动**：每个模块优先提供 REST API，再补充管理后台与前端适配。
4. **测试验证**：每阶段结束执行 `mvn -pl ruoyi-admin -am clean package -DskipTests`，确保可编译。
5. **文档同步**：关键设计、表结构、API 变更同步写入 `docs/` 与 SQL 注释。

## 实施状态

本次迭代已完成全部 14 个模块的**基础骨架与核心 API**实现，整个项目已通过编译打包。部分高级能力（如 BPMN 完整事件体系、AI 优化、复杂报表、签章证书链验证等）以简化版或占位形式落地，后续可继续深化。

## 阶段划分

### 第一阶段：骨架与数据模型（已完成）

- 新建 `ruoyi-bpm-v2` Maven 模块，注册到根 `pom.xml`。
- 实现 Spec 第六章核心实体：`bpm_process_definition`、`bpm_definition_version`、`bpm_process_instance`、`bpm_task`、`bpm_task_history`、`bpm_form_definition`、`bpm_form_data`、`bpm_field_permission`、`bpm_category`、`bpm_notification_template`、`bpm_work_calendar`、`bpm_audit_log`。
- 扩展实体：`bpm_user_org_ext`（主管链）、`bpm_print_template`、`bpm_signature`。
- 提供初始化 SQL 脚本：`sql/bpm_v2_init.sql`。
- 实现流程分类管理（M14.1）。
- 实现流程定义 CRUD、版本生成、发布/停用（M1 + M8 核心）。
- 实现 BPMN XML 与扩展 JSON 的存储、基础校验（M1.2 / M1.6）。

### 第二阶段：自研运行时引擎核心（已完成）

- 实现流程实例启动、任务创建与流转（M3 + M4 核心）。
- 支持节点类型：开始、审批、抄送、填写、排他网关、并行网关、结束。
- 支持审批方式：会签、或签、依次审批。
- 支持操作：同意、拒绝、转交、退回、终止、撤回。
- 实现审批人来源解析：指定成员、上级主管、角色/部门、动态表达式（简化版）。

### 第三阶段：开放 API 与集成能力（已完成）

- 实现 `/api/v1/process/**` 标准 API（M9）。
  - `POST /api/v1/process/instances/start`
  - `GET /api/v1/process/instances/{instanceId}`
  - `POST /api/v1/process/tasks/{taskId}/complete`
  - `GET /api/v1/process/tasks/todo`
- 统一响应结构 `BpmApiResult<T>`（code/message/data/traceId）。
- 状态变更回调、幂等键、签名验证预留扩展点。

### 第四阶段：表单与权限（已完成）

- 实现表单定义存储、字段权限矩阵（M2 + M11.1）。
- 实现运行时表单数据保存与查询。
- 实现表单基础校验（必填、长度、正则）。

### 第五阶段：高级审批与事件（部分完成）

- 已实现：转交、退回、终止、撤回。
- 待深化：加签、减签、委托代理、批量审批、公共任务池、管理强转。
- 待深化：BPMN 完整事件体系（定时/消息/信号/边界事件）。

### 第六阶段：超时、催办与工作日历（已完成）

- 实现工作日历 CRUD 与截止计算。
- 实现定时扫描超时任务与自动处理（自动同意/拒绝）。
- 通知模板 CRUD 与变量渲染。

### 第七阶段：组织架构与系统管理（已完成）

- 实现用户组织扩展（主管链）。
- 实现直属主管、第 N 级主管、部门负责人查找。
- 实现动态审批人解析（SpEL 表达式）。
- 实现离职自动转交待办。

### 第八阶段：监控运营与打印签章（已完成）

- 实现流程实例管理台、异常实例检测、运营大盘、待办监控统计。
- 实现打印模板 CRUD 与实例数据渲染。
- 实现电子/手写签名存储。

### 第九阶段：集成测试与优化（已完成基础验证）

- `mvn -pl ruoyi-admin -am clean package -DskipTests` 编译打包成功。
- 建议后续补充单元测试、联调测试与性能基线测试。

## 核心输出物

- `ruoyi-bpm-v2/pom.xml`
- `ruoyi-bpm-v2/src/main/java/com/ruoyi/bpm/v2/**` 核心代码（Domain/Mapper/Service/Controller/Engine）
- `ruoyi-bpm-v2/src/main/resources/mapper/bpm/v2/**` MyBatis 映射
- `sql/bpm_v2_init.sql` 初始化脚本
- `docs/bpm-v2-implementation-plan.md`（本文件）

## 后续建议

1. 补充 BPMN 2.0 XML 导入/导出与流程图渲染。
2. 完善 BPMN 事件体系与定时任务调度。
3. 补齐高级审批能力（加签、委托、批量、任务池）。
4. 接入真实消息通道（钉钉/企业微信/邮件）。
5. 编写单元测试与集成测试，目标覆盖率 ≥ 60%。
