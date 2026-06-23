# Agent Rules

## 编码规范

- 本项目所有文本文件在读取、写入、编辑时必须使用 **UTF-8** 编码。
- 禁止以 GBK/GB2312/ANSI 等编码保存或覆盖项目源码、配置文件、SQL 脚本及文档。
- 使用 `Read` / `Write` / `Edit` 等工具时，默认按 UTF-8 处理。

## 流程管理模块注意事项

- BPMN 设计器使用 `flowable-bpmn-moddle` 扩展，所有办理人/候选人/候选组等属性必须使用 `flowable:` 命名空间（如 `flowable:assignee`）。
- 流程发布后会生成新的 Flowable `ProcessDefinition` 版本，并同步写入 `bpm_definition_version`。
- 启动流程时如需指定办理人，请通过流程变量 `approvalAssignee` 传入用户 ID（如 `1`）。

## 构建与启动

- 后端：`mvn -pl ruoyi-admin -am clean package -DskipTests` 后 `java -jar ruoyi-admin/target/ruoyi-admin.jar --spring.profiles.active=druid`
- 前端：`cd ruoyi-ui && npm run dev`

## 本地环境说明

- 用户工具均位于 `D:/tools`，构建前请注意环境变量：
  - JDK 17：`D:/tools/jdk17`（系统默认 Java 为 1.8，构建前需执行 `export JAVA_HOME=/d/tools/jdk17 && export PATH=$JAVA_HOME/bin:$PATH`）
  - Maven：`D:/tools/apache-maven-3.9.6`
- 本地未运行 MySQL / Redis，完整联调需自行启动并执行 `sql/` 下脚本。
