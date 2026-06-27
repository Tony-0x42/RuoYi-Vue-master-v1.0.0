# 国际化（i18n）迁移指南

> 本指南面向后续需要继续翻译新模块、新增语言或维护后端文案的开发者。

---

## 1. 快速了解现状

- 前端框架：`vue-i18n@8.x`（Vue 2）。
- 语言包目录：`ruoyi-ui/src/i18n/locales/`。
- 当前支持语言：`zh-CN`、`en-US`。
- 回退语言：固定为 `zh-CN`。
- 后端国际化：Spring `MessageSource`，资源文件位于 `ruoyi-admin/src/main/resources/i18n/messages*.properties`。

---

## 2. Key 命名规范

统一采用三段式：**`模块.页面.元素`**，全部使用小写驼峰。

| Key | 说明 |
| --- | --- |
| `common.search` | 公共按钮：搜索 |
| `common.reset` | 公共按钮：重置 |
| `user.form.userName` | 用户管理 - 表单 - 用户名称 |
| `user.table.userName` | 用户管理 - 表格 - 用户名称列 |
| `login.title` | 登录页 - 标题 |
| `menu.system` | 菜单：系统管理 |
| `route.dashboard` | 路由标题：首页 |

### 命名建议

- 同模块同页面下的 key 集中放到一个对象中，避免扁平化。
- 如果一个文案在多个页面复用，优先放到 `common.*`。
- 菜单 key 建议用 `menu.<route-name>`，与前端路由 `name` 对应。

---

## 3. 如何新增一种语言

以新增 `ja-JP`（日语）为例：

### 3.1 新增 locale 文件

复制英文包作为模板：

```bash
cp ruoyi-ui/src/i18n/locales/en-US.js ruoyi-ui/src/i18n/locales/ja-JP.js
```

将 `ja-JP.js` 中所有 value 翻译为日语，保持 key 不变。

### 3.2 注册语言包

编辑 `ruoyi-ui/src/i18n/locales/index.js`：

```js
import zhCN from './zh-CN'
import enUS from './en-US'
import jaJP from './ja-JP'

export default {
  'zh-CN': zhCN,
  'en-US': enUS,
  'ja-JP': jaJP
}
```

### 3.3 切换组件中展示新语言

在 `ruoyi-ui/src/components/LangSelect/index.vue` 的 `el-dropdown-menu` 中新增选项：

```vue
<el-dropdown-item command="ja-JP" :disabled="language === 'ja-JP'">
  日本語
</el-dropdown-item>
```

> 新增语言后无需修改任何业务组件代码，只要所有中文都已替换为 `$t('...')`。

---

## 4. 如何翻译一个新模块

以 `monitor/server`（服务监控）模块为例：

### 4.1 抽取文案

打开 `ruoyi-ui/src/views/monitor/server/index.vue`，找出所有硬编码中文，例如：

```vue
<span>CPU</span>
<span>内存</span>
<span>服务器信息</span>
```

### 4.2 在 locale 文件中新增 key

在 `ruoyi-ui/src/i18n/locales/zh-CN.js` 和 `en-US.js` 中同时添加：

```js
monitor: {
  server: {
    cpu: 'CPU',
    memory: '内存',
    info: '服务器信息'
  }
}
```

```js
monitor: {
  server: {
    cpu: 'CPU',
    memory: 'Memory',
    info: 'Server Info'
  }
}
```

### 4.3 替换为 `$t`

```vue
<span>{{ $t('monitor.server.cpu') }}</span>
<span>{{ $t('monitor.server.memory') }}</span>
<span>{{ $t('monitor.server.info') }}</span>
```

### 4.4 常用替换场景

| 场景 | 示例 |
| --- | --- |
| 模板文本 | `<span>用户名称</span>` → `<span>{{ $t('user.form.userName') }}</span>` |
| 属性绑定 | `:label="'用户名称'"` → `:label="$t('user.form.userName')"` |
| JS 字符串 | `this.$modal.msgSuccess('删除成功')` → `this.$modal.msgSuccess(this.$t('common.deleteSuccess'))` |
| 表格列 | `label="用户名称"` → `:label="$t('user.table.userName')"` |
| 校验提示 | `message: '请输入用户名'` → `message: this.$t('user.validate.userNameRequired')` |
| placeholder | `placeholder="请输入用户名"` → `:placeholder="$t('user.form.userNamePlaceholder')"` |

### 4.5 扫描遗漏

运行扫描脚本，检查是否还有未翻译的中文：

```bash
cd ruoyi-ui
node scripts/find-untranslated.js
```

---

## 5. 后端文案国际化

后端基于 Spring `MessageSource`，已配置：

```yaml
spring:
  messages:
    basename: i18n/messages
```

### 5.1 资源文件位置

- 默认：`ruoyi-admin/src/main/resources/i18n/messages.properties`
- 中文：`ruoyi-admin/src/main/resources/i18n/messages_zh_CN.properties`（需要时新增）
- 英文：`ruoyi-admin/src/main/resources/i18n/messages_en_US.properties`（需要时新增）

### 5.2 新增/修改文案

在对应语言的 `.properties` 文件中添加 key-value：

```properties
# messages_en_US.properties
user.password.not.match=Invalid username or password
upload.exceed.maxSize=The file size exceeds the limit of {0} MB
```

> properties 文件默认使用 `ISO-8859-1`，中文需转义为 Unicode（如 `\u7528\u6237`），建议使用 IDE 的 Resource Bundle 编辑器或 native2ascii 处理。

### 5.3 在代码中使用

```java
import com.ruoyi.common.utils.MessageUtils;

String msg = MessageUtils.message("user.password.not.match");
```

带参数：

```java
String msg = MessageUtils.message("upload.exceed.maxSize", 10);
```

### 5.4 异常类

`BaseException` 已支持传入错误码，会自动通过 `MessageUtils` 解析：

```java
throw new BaseException("user.not.exists");
```

### 5.5 与前端语言同步

- 登录成功后，后端在 `/getInfo` 接口返回用户语言偏好 `lang`。
- 前端初始化时调用 `setLocale(lang)` 即可同步。
- 用户修改语言后，可调用 `/profile/update` 同步保存到后端。

---

## 6. 检查清单

- [ ] 新增 key 同时更新 `zh-CN.js` 和 `en-US.js`（及其他语言文件）。
- [ ] 保持 key 命名三段式：`模块.页面.元素`。
- [ ] 公共复用文案优先放入 `common.*`。
- [ ] 替换后用 `node scripts/find-untranslated.js` 检查遗漏。
- [ ] 后端新增文案时同步维护对应语言的 `.properties` 文件。
- [ ] 修改后执行 `npm run build:prod` 或 `mvn package` 验证构建通过。
