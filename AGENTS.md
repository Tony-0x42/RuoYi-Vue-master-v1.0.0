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

## 本地数据库工具与连接方式

- MySQL 命令行客户端位置：`/d/tools/MySQL/MySQL Workbench 8.0/mysql.exe`
- MySQL 逻辑备份工具位置：`/d/tools/MySQL/MySQL Workbench 8.0/mysqldump.exe`
- 图形客户端：`/d/tools/dbeaver/`
- 本地默认连接信息（开发环境）：
  - host：`localhost`
  - port：`3306`
  - user：`root`
  - password：`root`
  - database：`ry-vue`
- 连接数据库示例（在 Bash 中执行）：
  ```bash
  MYSQL_BIN="/d/tools/MySQL/MySQL Workbench 8.0/mysql.exe"
  "$MYSQL_BIN" -h <host> -P <port> -u <user> -p<password> --default-character-set=utf8mb4 <database>
  ```
- 执行 SQL 脚本示例：
  ```bash
  MYSQL_BIN="/d/tools/MySQL/MySQL Workbench 8.0/mysql.exe"
  "$MYSQL_BIN" -h <host> -P <port> -u <user> -p<password> --default-character-set=utf8mb4 <database> < sql/bpm_variable_category_migration.sql
  ```
- 备份数据库示例：
  ```bash
  MYSQLDUMP_BIN="/d/tools/MySQL/MySQL Workbench 8.0/mysqldump.exe"
  "$MYSQLDUMP_BIN" -h <host> -P <port> -u <user> -p<password> --default-character-set=utf8mb4 --single-transaction --routines --triggers <database> > backup_$(date +%Y%m%d%H%M%S).sql
  ```
- 数据库变更原则：
  - 所有结构变更优先通过 `sql/` 目录下的脚本完成，并同时维护初始化脚本与增量迁移脚本。
  - 变更前必须备份；生产环境禁止自动连接修改，需由用户确认后手动或通过审批流程执行。
  - 命令行操作必须显式指定 `--default-character-set=utf8mb4`，避免中文乱码。
  - 在用户提供数据库连接信息后，Agent 应直接在本地执行 `sql/` 下的增量脚本完成变更，无需再向用户展示或交付 SQL。

## 本地环境说明

- 用户工具均位于 `D:/tools`，构建前请注意环境变量：
  - JDK 17：`D:/tools/jdk17`（系统默认 Java 为 1.8，构建前需执行 `export JAVA_HOME=/d/tools/jdk17 && export PATH=$JAVA_HOME/bin:$PATH`）
  - Maven：`D:/tools/apache-maven-3.9.6`
- 本地未运行 MySQL / Redis，完整联调需自行启动并执行 `sql/` 下脚本。
- 如需在本地自动执行数据库脚本，用户必须主动提供连接信息（URL、用户名、密码），Agent 不得自行猜测或使用默认凭证连接数据库。

## 规范文档索引

- 项目技术与业务规范统一维护在 `spec/` 目录下。
- 进入 `spec/` 前，请先阅读 **`spec/agent.md`**，根据任务类型选择对应的全局规范或模块规范。
- 全局规范位于 `spec/global/`，覆盖接口、数据模型、部署架构、国际化、列表展示等跨模块约束。
- 模块规范位于 `spec/modules/`，覆盖 M1 ~ M15 各功能模块的详细需求与业务规则。
