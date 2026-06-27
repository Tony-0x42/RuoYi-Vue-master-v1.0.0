# 列表规范改造设计文档

## 1. 背景与目标

规范要求：**所有列表不允许出现 id；列表第一列必须是序号；序号由前端计算。**

当前 `ruoyi-ui/src` 中 33 个列表页实现不一致：
- 大量页面将主键以“编号”形式展示（`userId`、`roleId`、`noticeId` 等）。
- 部分页面没有序号列，首列是 `type="selection"` 多选框。
- 部分页面用后端 ID 冒充序号（`noticeId`）。
- 仅有少量页面使用前端计算跨页序号。
- 代码生成器模板会继续生成违规的新页面。

本设计旨在统一所有列表展示，消除 ID/编号/主键列，并以全局组件方式保证第一列为前端计算的跨页序号。

## 2. 范围

### 2.1 前端范围

`ruoyi-ui/src` 下所有包含 `<el-table>` 的 Vue 文件，共 33 个：

- 系统管理：13 个
- 监控：6 个
- 工具：3 个
- BPM：9 个
- 公共组件：2 个（用户选择对话框、多选用户组件）

具体清单见《RuoYi-Vue 前端表格列表实现调研报告》。

### 2.2 生成器范围

需要同步修改代码生成器模板，防止新增页面回潮：

- `ruoyi-generator/src/main/resources/vm/vue/index.vue.vm`
- `ruoyi-generator/src/main/resources/vm/vue/index-tree.vue.vm`
- `ruoyi-generator/src/main/resources/vm/vue/v3/index.vue.vm`
- `ruoyi-generator/src/main/resources/vm/vue/v3/index-tree.vue.vm`
- `ruoyi-generator/src/main/resources/vm/vue/v3ts/index.vue.vm`
- `ruoyi-generator/src/main/resources/vm/vue/v3ts/index-tree.vue.vm`

## 3. 术语定义

- **id 列**：凡是以主键、编号、ID、主键等字段作为 `prop` 展示的列均属此类。典型形式包括 `prop="id"`、`prop="userId"`、`prop="noticeId"`、`label="用户编号"`、`label="参数主键"`、`label="组件ID"` 等。
- **序号列**：label 为“序号”的 `<el-table-column>`，显示当前页行号，且跨页连续计算。
- **前端计算序号**：使用 `(pageNum - 1) * pageSize + rowIndex + 1` 计算，不依赖后端返回的 index/id 字段。

## 4. 设计方案对比

### 方案 A：全局 IndexColumn 组件 + 批量修改 + 生成器模板（推荐）

1. 新建 `ruoyi-ui/src/components/IndexColumn/index.vue`，封装跨页序号列，全局注册。
2. 在所有 33 个列表页中：
   - 删除所有 id/编号/主键列。
   - 在 `<el-table>` 第一列放置 `<index-column :page="queryParams.pageNum" :size="queryParams.pageSize" />`。
   - 若原表有 `type="selection"`，selection 列移至序号列之后。
3. 修改生成器模板，删除非树形模板中主键列输出，并在 `<el-table>` 首列插入序号列。

**优点**：统一封装、可维护、一次改造长期有效、生成器同步保证新增页面合规。
**缺点**：改造文件较多，需保证每个页面的分页参数名一致。

### 方案 B：全局包装 `<el-table>`

封装一个 `RuoYiTable` 组件，自动注入序号列并过滤 id 列。

**优点**：对业务页面侵入最小。
**缺点**：表格差异大（树形、拖拽、子表、本地分页、自定义列模板），统一包装成本高、风险大，容易破坏现有交互。

### 方案 C：仅批量修改现有页面，不改造生成器

只处理当前 33 个页面，不创建全局组件，也不改生成器模板。

**优点**：改动最小。
**缺点**：无法防止新增页面继续违规；各页面序号列实现碎片化，后续维护困难。

## 5. 推荐方案详细设计

### 5.1 IndexColumn 组件

文件：`ruoyi-ui/src/components/IndexColumn/index.vue`

```vue
<template>
  <el-table-column label="序号" align="center" width="50">
    <template slot-scope="scope">
      <span>{{ (page - 1) * size + scope.$index + 1 }}</span>
    </template>
  </el-table-column>
</template>

<script>
export default {
  name: 'IndexColumn',
  props: {
    page: {
      type: Number,
      default: 1
    },
    size: {
      type: Number,
      default: 10
    }
  }
}
</script>
```

在 `main.js` 中全局注册：

```js
import IndexColumn from '@/components/IndexColumn'
Vue.component('IndexColumn', IndexColumn)
```

### 5.2 页面改造模式

#### 有 selection 的表格

改造前：
```vue
<el-table v-loading="loading" :data="userList" @selection-change="handleSelectionChange">
  <el-table-column type="selection" width="55" align="center" />
  <el-table-column label="用户编号" prop="userId" />
  ...
</el-table>
```

改造后：
```vue
<el-table v-loading="loading" :data="userList" @selection-change="handleSelectionChange">
  <index-column :page="queryParams.pageNum" :size="queryParams.pageSize" />
  <el-table-column type="selection" width="55" align="center" />
  ...
</el-table>
```

#### 无 selection 的表格

改造前：
```vue
<el-table v-loading="loading" :data="cacheList">
  <el-table-column label="序号" width="60" type="index"></el-table-column>
  ...
</el-table>
```

改造后：
```vue
<el-table v-loading="loading" :data="cacheList">
  <index-column :page="queryParams.pageNum" :size="queryParams.pageSize" />
  ...
</el-table>
```

#### 弹窗/子表/树形表格

- 弹窗中的选择表格同样适用。
- 树形表格由于整棵树一次性加载，使用 `type="index"` 或 `$index + 1` 计算当前行序号；第一列仍为序号。
- 子表（编辑弹窗中的明细表）已在模板中使用 `$index + 1` 计算序号，只需确保序号列位于第一列。

### 5.3 生成器模板改造

#### 非树形 index.vue.vm

在 `<el-table>` 内首列插入：
```velocity
      <el-table-column label="序号" align="center" width="50">
        <template slot-scope="scope">
          <span>{{ (queryParams.pageNum - 1) * queryParams.pageSize + scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column type="selection" width="55" align="center" />
```

并删除或注释 `#if($column.pk)` 分支中的可见列输出。

#### 树形 index-tree.vue.vm

在 `<el-table>` 内首列插入跨页序号列（树形表格整树加载，可直接使用 `scope.$index + 1`）。

#### v3 / v3ts 模板

保持相同结构，仅将 slot 语法改为 `#default="scope"`。

#### 子表

子表序号列已合规，只需确保其位于 `type="selection"` 之前。

## 6. 实施步骤

1. 创建 `IndexColumn` 组件并全局注册。
2. 批量修改 `ruoyi-ui/src` 下 33 个列表页：删除 id/编号/主键列，首列替换为 `<index-column>`。
3. 修改 6 个生成器 `index.vue.vm` / `index-tree.vue.vm` 模板。
4. 本地构建前端，检查无编译错误。
5. 抽样验证若干页面，确认：
   - 列表首列为“序号”。
   - 序号跨页连续。
   - 不再显示任何 id/编号/主键列。

## 7. 验收标准

- [ ] `ruoyi-ui/src` 下所有包含 `<el-table>` 的 Vue 文件，其第一个 `<el-table-column>` 为序号列。
- [ ] 所有序号列数值由前端根据 `pageNum`/`pageSize`/`$index` 计算，不依赖后端字段。
- [ ] 所有页面不再包含 `prop="id"`、`prop="*Id"`、`label="*编号"`、`label="*主键"`、`label="*ID"` 等 id 列。
- [ ] 代码生成器模板生成的新页面同样满足以上两条。
- [ ] 前端构建通过（`npm run build:prod` 或 `npm run build`）。

## 8. 风险与注意事项

1. **分页参数名不一致**：部分页面可能使用 `pageNum`/`pageSize` 局部变量而非 `queryParams`。需逐个核对并传递正确值给 `IndexColumn`。
2. **树形表格与本地分页**：树形表格通常一次性返回全部数据，`pageNum`/`pageSize` 不适用，应使用 `$index + 1`。
3. **selection 全选/导出**：删除 id 列不影响现有操作列和导出功能；若导出模板中包含 id 字段，应同步移除。
4. **生成器历史模板**：仅修改当前使用的 `vue`、`vue/v3`、`vue/v3ts` 模板；若项目存在其他历史模板，需一并评估。
5. **回归风险**：改造文件多，建议分模块提交并逐个模块验证。
