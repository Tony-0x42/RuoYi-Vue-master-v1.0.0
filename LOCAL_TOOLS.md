# 本地工具路径说明

本项目在本地构建时，部分工具需要显式指定路径（系统默认环境可能不匹配）。

| 工具 | 路径 | 说明 |
|------|------|------|
| JDK 17 | `D:/tools/jdk17` | 项目要求 target 17，系统默认 Java 为 1.8，构建前需 `export JAVA_HOME=/d/tools/jdk17` 并加入 `PATH` |
| Maven | `D:/tools/apache-maven-3.9.6` | 已加入系统 PATH，可直接调用 `mvn` |
| MySQL | 本地未运行 | 完整联调需要自行启动 MySQL 并执行 `sql/` 下脚本 |
| Redis | 本地未运行 | 项目启动依赖 Redis，联调前需启动 |

## 常用构建命令

```bash
# 后端（注意 JAVA_HOME）
export JAVA_HOME=/d/tools/jdk17
export PATH=$JAVA_HOME/bin:$PATH
mvn -pl ruoyi-admin -am clean package -DskipTests

# 前端
cd ruoyi-ui && npm run build:stage
```
