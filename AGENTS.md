# Agent Rules

## 编码规范

- 本项目所有文本文件在读取、写入、编辑时必须使用 **UTF-8** 编码。
- 禁止以 GBK/GB2312/ANSI 等编码保存或覆盖项目源码、配置文件、SQL 脚本及文档。
- 使用 `Read` / `Write` / `Edit` 等工具时，默认按 UTF-8 处理。

## 流程管理模块注意事项

- BPMN 设计器使用 `flowable-bpmn-moddle` 扩展，所有办理人/候选人/候选组等属性必须使用 `flowable:` 命名空间（如 `flowable:assignee`）。
- 流程发布后会生成新的 Flowable `ProcessDefinition` 版本，并同步写入 `bpm_definition_version`。
- 启动流程时如需指定办理人，请通过流程变量 `approvalAssignee` 传入用户 ID（如 `1`）。

## Git 提交规范

- 所有 Git commit message 必须使用英文撰写，保持简洁、清晰，遵循常规提交格式（如 `feat:`, `fix:`, `docs:`, `refactor:` 等前缀）。
- 禁止使用中文或无意义的提交信息。

## 构建与启动

- 后端：`mvn -pl ruoyi-admin -am clean package -DskipTests` 后 `java -jar ruoyi-admin/target/ruoyi-admin.jar --spring.profiles.active=druid`
- 前端：`cd ruoyi-ui && npm run dev`

## 数据库变更规范

- 所有数据库结构变更必须通过 SQL 脚本完成，脚本统一放在 `sql/` 目录。
- 同时维护两类脚本：
  - **初始化脚本**：如 `ry_init_*.sql`，用于新环境一次性创建所有表结构。
  - **增量迁移脚本**：如 `bpm_*_migration.sql`，用于已运行数据库升级，命名需体现变更内容和日期。
- 当需要修改数据库时，遵循以下流程：
  1. **备份**：变更前必须先备份目标数据库。
  2. **脚本化**：将变更写成可重复执行的 SQL 脚本，避免直接在生产环境手工操作。
  3. **本地验证**：在本地或测试环境先执行并验证表结构、索引、数据一致性。
  4. **自动执行**：只有在用户明确授权并提供连接信息后，才可在本地开发环境通过命令行工具（如 `mysql` 客户端）自动执行脚本；**生产环境禁止自动连接修改**。
  5. **回滚准备**：复杂变更应提供回滚 SQL，或在事务中执行以便异常时回退。
- 数据库连接优先使用 UTF-8 / utf8mb4 编码，命令行执行时显式指定 `--default-character-set=utf8mb4`。

## 本地环境说明

- 用户工具均位于 `D:/tools`，构建前请注意环境变量：
  - JDK 17：`D:/tools/jdk17`（系统默认 Java 为 1.8，构建前需执行 `export JAVA_HOME=/d/tools/jdk17 && export PATH=$JAVA_HOME/bin:$PATH`）
  - Maven：`D:/tools/apache-maven-3.9.6`
- 本地未运行 MySQL / Redis，完整联调需自行启动并执行 `sql/` 下脚本。
- 如需在本地自动执行数据库脚本，用户必须主动提供连接信息（URL、用户名、密码），Agent 不得自行猜测或使用默认凭证连接数据库。
