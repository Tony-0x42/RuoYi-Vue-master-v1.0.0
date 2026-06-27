# xxx管理系统

基于 SpringBoot + Vue 前后端分离的 Java 快速开发框架。

## 开发环境

- JDK 17
- Maven 3.9.6
- MySQL 8.0
- Node.js 16+

## 模块说明

- `ruoyi-admin`：Web 服务入口
- `ruoyi-framework`：框架核心
- `ruoyi-system`：系统模块
- `ruoyi-common`：公共模块
- `ruoyi-bpm` / `ruoyi-bpm-v2`：流程管理模块
- `ruoyi-quartz`：定时任务模块
- `ruoyi-generator`：代码生成模块
- `ruoyi-ui`：前端工程

## 启动方式

### 后端

```bash
mvn -pl ruoyi-admin -am clean package -DskipTests
java -jar ruoyi-admin/target/ruoyi-admin.jar --spring.profiles.active=druid
```

### 前端

```bash
cd ruoyi-ui
npm install
npm run dev
```

浏览器访问 http://localhost:80

## 数据库

初始化脚本位于 `sql/` 目录，请按顺序执行。
