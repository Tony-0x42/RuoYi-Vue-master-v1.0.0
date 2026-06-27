# 列表规范改造实施计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 统一 `ruoyi-ui` 所有列表：删除 id/编号/主键列，首列为前端计算的跨页序号列；同步改造代码生成器模板，防止新增页面违规。

**Architecture:** 新增全局 `IndexColumn` 组件封装跨页序号；按模块批量修改现有 33 个 Vue 列表页；修改 6 个 Velocity 生成器模板；最后通过前端构建与抽样页面验证。

**Tech Stack:** Vue 2/3, ElementUI/ElementPlus, Velocity 模板, Maven（生成器后端）, npm（前端构建）

## Global Constraints

- 所有列表不允许出现 id/编号/主键列。
- 列表第一列必须是序号列。
- 序号由前端计算：`(pageNum - 1) * pageSize + rowIndex + 1`。
- 改造需保持现有功能（选择、操作、导出、树形展开等）。
- 代码生成器模板同步改造，新生成页面自动合规。
- 所有文本文件使用 UTF-8 编码。

---

## File Structure

### 新增文件

- `ruoyi-ui/src/components/IndexColumn/index.vue` — 全局跨页序号列组件。

### 修改文件 — 前端列表页（共 33 个）

按模块分组：

- **系统管理 (13)**
  - `ruoyi-ui/src/views/system/user/index.vue`
  - `ruoyi-ui/src/views/system/user/authRole.vue`
  - `ruoyi-ui/src/views/system/role/index.vue`
  - `ruoyi-ui/src/views/system/role/authUser.vue`
  - `ruoyi-ui/src/views/system/role/selectUser.vue`
  - `ruoyi-ui/src/views/system/post/index.vue`
  - `ruoyi-ui/src/views/system/notice/index.vue`
  - `ruoyi-ui/src/views/system/notice/ReadUsers.vue`
  - `ruoyi-ui/src/views/system/dict/index.vue`
  - `ruoyi-ui/src/views/system/dict/data.vue`
  - `ruoyi-ui/src/views/system/dept/index.vue`
  - `ruoyi-ui/src/views/system/menu/index.vue`
  - `ruoyi-ui/src/views/system/config/index.vue`
- **监控 (6)**
  - `ruoyi-ui/src/views/monitor/job/index.vue`
  - `ruoyi-ui/src/views/monitor/job/log.vue`
  - `ruoyi-ui/src/views/monitor/online/index.vue`
  - `ruoyi-ui/src/views/monitor/operlog/index.vue`
  - `ruoyi-ui/src/views/monitor/logininfor/index.vue`
  - `ruoyi-ui/src/views/monitor/cache/list.vue`
- **工具 (3)**
  - `ruoyi-ui/src/views/tool/gen/index.vue`
  - `ruoyi-ui/src/views/tool/gen/importTable.vue`
  - `ruoyi-ui/src/views/tool/gen/editTable.vue`
- **BPM (9)**
  - `ruoyi-ui/src/views/bpm/task/todo.vue`
  - `ruoyi-ui/src/views/bpm/task/done.vue`
  - `ruoyi-ui/src/views/bpm/definition/index.vue`
  - `ruoyi-ui/src/views/bpm/instance/index.vue`
  - `ruoyi-ui/src/views/bpm/variable/index.vue`
  - `ruoyi-ui/src/views/bpm/workbench/template/index.vue`
  - `ruoyi-ui/src/views/bpm/workbench/my/index.vue`
  - `ruoyi-ui/src/views/bpm/workbench/component/index.vue`
  - `ruoyi-ui/src/views/bpm/workbench/components/TaskDone.vue`
  - `ruoyi-ui/src/views/bpm/workbench/components/TaskTodo.vue`
- **公共组件 (2)**
  - `ruoyi-ui/src/components/UserSelectDialog/index.vue`
  - `ruoyi-ui/src/components/UserMultiSelect/index.vue`

### 修改文件 — 全局注册

- `ruoyi-ui/src/main.js` — 全局注册 `IndexColumn` 组件。

### 修改文件 — 代码生成器模板（共 6 个）

- `ruoyi-generator/src/main/resources/vm/vue/index.vue.vm`
- `ruoyi-generator/src/main/resources/vm/vue/index-tree.vue.vm`
- `ruoyi-generator/src/main/resources/vm/vue/v3/index.vue.vm`
- `ruoyi-generator/src/main/resources/vm/vue/v3/index-tree.vue.vm`
- `ruoyi-generator/src/main/resources/vm/vue/v3ts/index.vue.vm`
- `ruoyi-generator/src/main/resources/vm/vue/v3ts/index-tree.vue.vm`

---

## Task 1: Create global IndexColumn component

**Files:**
- Create: `ruoyi-ui/src/components/IndexColumn/index.vue`
- Modify: `ruoyi-ui/src/main.js`

**Interfaces:**
- Consumes: `page` (Number, default 1), `size` (Number, default 10)
- Produces: `<el-table-column>` rendering `(page - 1) * size + scope.$index + 1`

- [ ] **Step 1: Create component file**

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

- [ ] **Step 2: Register globally in main.js**

在 `ruoyi-ui/src/main.js` 中追加：

```js
import IndexColumn from '@/components/IndexColumn'
Vue.component('IndexColumn', IndexColumn)
```

- [ ] **Step 3: Verify registration**

Run:
```bash
cd ruoyi-ui && npm run build
```
Expected: 构建启动，无 `IndexColumn` 组件未找到报错。

- [ ] **Step 4: Commit**

```bash
git add ruoyi-ui/src/components/IndexColumn/index.vue ruoyi-ui/src/main.js
git commit -m "feat: add global IndexColumn component for cross-page row index"
```

---

## Task 2: Update system management list pages

**Files:**
- Modify: `ruoyi-ui/src/views/system/user/index.vue`
- Modify: `ruoyi-ui/src/views/system/user/authRole.vue`
- Modify: `ruoyi-ui/src/views/system/role/index.vue`
- Modify: `ruoyi-ui/src/views/system/role/authUser.vue`
- Modify: `ruoyi-ui/src/views/system/role/selectUser.vue`
- Modify: `ruoyi-ui/src/views/system/post/index.vue`
- Modify: `ruoyi-ui/src/views/system/notice/index.vue`
- Modify: `ruoyi-ui/src/views/system/notice/ReadUsers.vue`
- Modify: `ruoyi-ui/src/views/system/dict/index.vue`
- Modify: `ruoyi-ui/src/views/system/dict/data.vue`
- Modify: `ruoyi-ui/src/views/system/dept/index.vue`
- Modify: `ruoyi-ui/src/views/system/menu/index.vue`
- Modify: `ruoyi-ui/src/views/system/config/index.vue`

**Interfaces:**
- Consumes: `IndexColumn` component, each page's `queryParams.pageNum` / `queryParams.pageSize` (or local `pageNum`/`pageSize`)
- Produces: first `<el-table-column>` is `<index-column>`; no id/编号/主键 columns

- [ ] **Step 1: Modify `system/user/index.vue`**

Locate `<el-table` and remove:
```vue
<el-table-column label="用户编号" prop="userId" />
```

Insert as the very first child of `<el-table>`:
```vue
<index-column :page="queryParams.pageNum" :size="queryParams.pageSize" />
```

If `type="selection"` exists, keep it immediately after `<index-column>`.

- [ ] **Step 2: Modify `system/user/authRole.vue`**

Remove:
```vue
<el-table-column label="角色编号" prop="roleId" />
```

Insert:
```vue
<index-column :page="pageNum" :size="pageSize" />
```

- [ ] **Step 3: Modify `system/role/index.vue`**

Remove:
```vue
<el-table-column label="角色编号" prop="roleId" />
```

Insert:
```vue
<index-column :page="queryParams.pageNum" :size="queryParams.pageSize" />
```

- [ ] **Step 4: Modify `system/role/authUser.vue`**

Remove:
```vue
<el-table-column label="用户编号" prop="userId" />
```

Insert:
```vue
<index-column :page="queryParams.pageNum" :size="queryParams.pageSize" />
```

- [ ] **Step 5: Modify `system/role/selectUser.vue`**

Remove any `prop="userId"` column; insert `<index-column :page="queryParams.pageNum" :size="queryParams.pageSize" />` first.

- [ ] **Step 6: Modify `system/post/index.vue`**

Remove:
```vue
<el-table-column label="岗位编号" prop="postId" />
```

Insert:
```vue
<index-column :page="queryParams.pageNum" :size="queryParams.pageSize" />
```

- [ ] **Step 7: Modify `system/notice/index.vue`**

Replace the column that uses `prop="noticeId"` and label "序号" with:
```vue
<index-column :page="queryParams.pageNum" :size="queryParams.pageSize" />
```

- [ ] **Step 8: Modify `system/notice/ReadUsers.vue`**

Replace `<el-table-column type="index" label="序号" .../>` with:
```vue
<index-column :page="queryParams.pageNum" :size="queryParams.pageSize" />
```

- [ ] **Step 9: Modify `system/dict/index.vue`**

Remove:
```vue
<el-table-column label="字典编号" prop="dictId" />
```

Insert:
```vue
<index-column :page="queryParams.pageNum" :size="queryParams.pageSize" />
```

- [ ] **Step 10: Modify `system/dict/data.vue`**

Remove any `prop="dictCode"` / label="字典编码" id-like column; insert `<index-column :page="queryParams.pageNum" :size="queryParams.pageSize" />` first.

- [ ] **Step 11: Modify `system/dept/index.vue`**

This is a tree table. Insert as first column:
```vue
<el-table-column label="序号" align="center" width="50">
  <template slot-scope="scope">
    <span>{{ scope.$index + 1 }}</span>
  </template>
</el-table-column>
```

Remove any `prop="deptId"` / label="部门编号" column.

- [ ] **Step 12: Modify `system/menu/index.vue`**

Tree table. Insert the tree-appropriate index column (same as dept) and remove any `prop="menuId"` / label="菜单编号" column.

- [ ] **Step 13: Modify `system/config/index.vue`**

Remove:
```vue
<el-table-column label="参数主键" prop="configId" />
```

Insert:
```vue
<index-column :page="queryParams.pageNum" :size="queryParams.pageSize" />
```

- [ ] **Step 14: Build check**

Run:
```bash
cd ruoyi-ui && npm run build
```
Expected: no new build errors.

- [ ] **Step 15: Commit**

```bash
git add ruoyi-ui/src/views/system
git commit -m "refactor(system): remove id columns and add front-end index column"
```

---

## Task 3: Update monitor list pages

**Files:**
- Modify: `ruoyi-ui/src/views/monitor/job/index.vue`
- Modify: `ruoyi-ui/src/views/monitor/job/log.vue`
- Modify: `ruoyi-ui/src/views/monitor/online/index.vue`
- Modify: `ruoyi-ui/src/views/monitor/operlog/index.vue`
- Modify: `ruoyi-ui/src/views/monitor/logininfor/index.vue`
- Modify: `ruoyi-ui/src/views/monitor/cache/list.vue`

**Interfaces:**
- Consumes: `IndexColumn`, `queryParams.pageNum` / `queryParams.pageSize` (or local equivalents)
- Produces: first column is index; no id columns

- [ ] **Step 1: Modify `monitor/job/index.vue`**

Remove `<el-table-column label="任务编号" prop="jobId" />`; insert `<index-column :page="queryParams.pageNum" :size="queryParams.pageSize" />` first.

- [ ] **Step 2: Modify `monitor/job/log.vue`**

Remove `<el-table-column label="日志编号" prop="jobLogId" />`; insert `<index-column :page="queryParams.pageNum" :size="queryParams.pageSize" />` first.

- [ ] **Step 3: Modify `monitor/online/index.vue`**

Remove `<el-table-column label="会话编号" prop="tokenId" />`; replace existing `type="index"` column with `<index-column :page="pageNum" :size="pageSize" />` first.

- [ ] **Step 4: Modify `monitor/operlog/index.vue`**

Remove `<el-table-column label="日志编号" prop="operId" />`; insert `<index-column :page="queryParams.pageNum" :size="queryParams.pageSize" />` first.

- [ ] **Step 5: Modify `monitor/logininfor/index.vue`**

Remove `<el-table-column label="访问编号" prop="infoId" />`; insert `<index-column :page="queryParams.pageNum" :size="queryParams.pageSize" />` first.

- [ ] **Step 6: Modify `monitor/cache/list.vue`**

Replace existing `type="index"` column with the tree/simple index column:
```vue
<el-table-column label="序号" align="center" width="60">
  <template slot-scope="scope">
    <span>{{ scope.$index + 1 }}</span>
  </template>
</el-table-column>
```

- [ ] **Step 7: Build check**

Run:
```bash
cd ruoyi-ui && npm run build
```

- [ ] **Step 8: Commit**

```bash
git add ruoyi-ui/src/views/monitor
git commit -m "refactor(monitor): remove id columns and add front-end index column"
```

---

## Task 4: Update tool list pages

**Files:**
- Modify: `ruoyi-ui/src/views/tool/gen/index.vue`
- Modify: `ruoyi-ui/src/views/tool/gen/importTable.vue`
- Modify: `ruoyi-ui/src/views/tool/gen/editTable.vue`

**Interfaces:**
- Consumes: `IndexColumn`, `queryParams.pageNum` / `queryParams.pageSize`
- Produces: first column is index; no id columns

- [ ] **Step 1: Modify `tool/gen/index.vue`**

Replace existing custom index column with:
```vue
<index-column :page="queryParams.pageNum" :size="queryParams.pageSize" />
```

Ensure it is the first column.

- [ ] **Step 2: Modify `tool/gen/importTable.vue`**

Insert `<index-column :page="queryParams.pageNum" :size="queryParams.pageSize" />` first.

- [ ] **Step 3: Modify `tool/gen/editTable.vue`**

Replace existing `type="index"` column with the simple index column (`scope.$index + 1`) first.

- [ ] **Step 4: Build check**

Run:
```bash
cd ruoyi-ui && npm run build
```

- [ ] **Step 5: Commit**

```bash
git add ruoyi-ui/src/views/tool
git commit -m "refactor(tool): remove id columns and add front-end index column"
```

---

## Task 5: Update BPM list pages

**Files:**
- Modify: `ruoyi-ui/src/views/bpm/task/todo.vue`
- Modify: `ruoyi-ui/src/views/bpm/task/done.vue`
- Modify: `ruoyi-ui/src/views/bpm/definition/index.vue`
- Modify: `ruoyi-ui/src/views/bpm/instance/index.vue`
- Modify: `ruoyi-ui/src/views/bpm/variable/index.vue`
- Modify: `ruoyi-ui/src/views/bpm/workbench/template/index.vue`
- Modify: `ruoyi-ui/src/views/bpm/workbench/my/index.vue`
- Modify: `ruoyi-ui/src/views/bpm/workbench/component/index.vue`
- Modify: `ruoyi-ui/src/views/bpm/workbench/components/TaskDone.vue`
- Modify: `ruoyi-ui/src/views/bpm/workbench/components/TaskTodo.vue`

**Interfaces:**
- Consumes: `IndexColumn`, each page's `queryParams.pageNum` / `queryParams.pageSize`
- Produces: first column is index; no id columns

- [ ] **Step 1: Modify `bpm/task/todo.vue`**

Remove `<el-table-column label="Flowable任务ID" prop="flowableTaskId" />`; insert `<index-column :page="queryParams.pageNum" :size="queryParams.pageSize" />` first.

- [ ] **Step 2: Modify `bpm/task/done.vue`**

Remove any id-like column; insert `<index-column :page="queryParams.pageNum" :size="queryParams.pageSize" />` first.

- [ ] **Step 3: Modify `bpm/definition/index.vue`**

Remove `<el-table-column label="流程编号" prop="definitionId" />`; insert `<index-column :page="queryParams.pageNum" :size="queryParams.pageSize" />` first.

- [ ] **Step 4: Modify `bpm/instance/index.vue`**

Remove `<el-table-column label="实例编号" prop="instanceId" />`; insert `<index-column :page="queryParams.pageNum" :size="queryParams.pageSize" />` first.

- [ ] **Step 5: Modify `bpm/variable/index.vue`**

Remove `<el-table-column label="变量编号" prop="variableId" />`; insert `<index-column :page="queryParams.pageNum" :size="queryParams.pageSize" />` first.

- [ ] **Step 6: Modify `bpm/workbench/template/index.vue`**

Remove `<el-table-column label="模板ID" prop="templateId" />`; insert `<index-column :page="queryParams.pageNum" :size="queryParams.pageSize" />` first.

- [ ] **Step 7: Modify `bpm/workbench/my/index.vue`**

Remove any id-like column; insert `<index-column :page="queryParams.pageNum" :size="queryParams.pageSize" />` first.

- [ ] **Step 8: Modify `bpm/workbench/component/index.vue`**

Remove `<el-table-column label="组件ID" prop="componentId" />`; insert `<index-column :page="queryParams.pageNum" :size="queryParams.pageSize" />` first.

- [ ] **Step 9: Modify `bpm/workbench/components/TaskDone.vue` and `TaskTodo.vue`**

For both files: remove any id-like column; insert `<index-column :page="queryParams.pageNum" :size="queryParams.pageSize" />` first.

- [ ] **Step 10: Build check**

Run:
```bash
cd ruoyi-ui && npm run build
```

- [ ] **Step 11: Commit**

```bash
git add ruoyi-ui/src/views/bpm
git commit -m "refactor(bpm): remove id columns and add front-end index column"
```

---

## Task 6: Update shared components

**Files:**
- Modify: `ruoyi-ui/src/components/UserSelectDialog/index.vue`
- Modify: `ruoyi-ui/src/components/UserMultiSelect/index.vue`

**Interfaces:**
- Consumes: `IndexColumn`, `queryParams.pageNum` / `queryParams.pageSize`
- Produces: first column is index; no `userId` column

- [ ] **Step 1: Modify `UserSelectDialog/index.vue`**

Remove `<el-table-column label="用户编号" prop="userId" />`; insert `<index-column :page="queryParams.pageNum" :size="queryParams.pageSize" />` first.

- [ ] **Step 2: Modify `UserMultiSelect/index.vue`**

Remove `<el-table-column label="用户编号" prop="userId" />`; insert `<index-column :page="queryParams.pageNum" :size="queryParams.pageSize" />` first.

- [ ] **Step 3: Build check**

Run:
```bash
cd ruoyi-ui && npm run build
```

- [ ] **Step 4: Commit**

```bash
git add ruoyi-ui/src/components/UserSelectDialog ruoyi-ui/src/components/UserMultiSelect
git commit -m "refactor(components): remove id columns and add front-end index column"
```

---

## Task 7: Update code generator templates

**Files:**
- Modify: `ruoyi-generator/src/main/resources/vm/vue/index.vue.vm`
- Modify: `ruoyi-generator/src/main/resources/vm/vue/index-tree.vue.vm`
- Modify: `ruoyi-generator/src/main/resources/vm/vue/v3/index.vue.vm`
- Modify: `ruoyi-generator/src/main/resources/vm/vue/v3/index-tree.vue.vm`
- Modify: `ruoyi-generator/src/main/resources/vm/vue/v3ts/index.vue.vm`
- Modify: `ruoyi-generator/src/main/resources/vm/vue/v3ts/index-tree.vue.vm`

**Interfaces:**
- Consumes: Velocity variables `$column.pk`, `$column.list`, `$comment`, `$javaField`
- Produces: generated Vue files with index column first and no visible primary-key column

- [ ] **Step 1: Modify `vm/vue/index.vue.vm`**

Locate `<el-table v-loading="loading" :data="${businessName}List" @selection-change="handleSelectionChange">`.

After the opening tag, replace the existing first child:
```velocity
      <el-table-column type="selection" width="55" align="center" />
```
with:
```velocity
      <el-table-column label="序号" align="center" width="50">
        <template slot-scope="scope">
          <span>{{ (queryParams.pageNum - 1) * queryParams.pageSize + scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column type="selection" width="55" align="center" />
```

Find the `#if($column.pk)` branch and change it to output nothing:
```velocity
#if($column.pk)
#elseif($column.list && $column.htmlType == "datetime")
```

- [ ] **Step 2: Modify `vm/vue/v3/index.vue.vm`**

Same as Step 1 but use Vue 3 slot syntax:
```velocity
      <el-table-column label="序号" align="center" width="50">
        <template #default="scope">
          <span>{{ (queryParams.pageNum - 1) * queryParams.pageSize + scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column type="selection" width="55" align="center" />
```

Empty `#if($column.pk)` branch.

- [ ] **Step 3: Modify `vm/vue/v3ts/index.vue.vm`**

Same slot syntax as v3. Empty `#if($column.pk)` branch.

- [ ] **Step 4: Modify `vm/vue/index-tree.vue.vm`**

After the `<el-table` opening tag, insert:
```velocity
      <el-table-column label="序号" align="center" width="50">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>
```

Ensure the `#if($column.pk)` branch is already empty (it is).

- [ ] **Step 5: Modify `vm/vue/v3/index-tree.vue.vm` and `vm/vue/v3ts/index-tree.vue.vm`**

Same as Step 4, using `#default="scope"` for Vue 3.

- [ ] **Step 6: Verify template syntax**

Run a dry generation or inspect the templates for matching `#if`/`#end` and unclosed Velocity references.

- [ ] **Step 7: Commit**

```bash
git add ruoyi-generator/src/main/resources/vm/vue
git commit -m "refactor(generator): hide pk columns and prepend index column in list templates"
```

---

## Task 8: Final verification

**Files:**
- All files listed above

- [ ] **Step 1: Run a global search for remaining id columns**

Run:
```bash
cd ruoyi-ui/src
grep -R -n "label=\".*编号\"" views components | grep -v "node_modules"
grep -R -n "label=\".*主键\"" views components | grep -v "node_modules"
grep -R -n "label=\".*ID\"" views components | grep -v "node_modules"
grep -R -n "prop=\"id\"" views components | grep -v "node_modules"
```
Expected: no matches inside table column definitions. (Matches in form labels or other UI elements may be acceptable if not inside `<el-table-column>`.)

- [ ] **Step 2: Verify every el-table starts with index column**

Run:
```bash
cd ruoyi-ui/src/views
for f in $(grep -l "<el-table" -r .); do
  echo "=== $f ==="
  grep -A 2 "<el-table" "$f" | head -3
done
```
Expected: first `<el-table-column>` after `<el-table>` is either `<index-column>` or an inline `<el-table-column label="序号">`.

- [ ] **Step 3: Build frontend**

Run:
```bash
cd ruoyi-ui && npm run build
```
Expected: build succeeds with no new errors.

- [ ] **Step 4: Optional backend build**

Run:
```bash
export JAVA_HOME=/d/tools/jdk17 && export PATH=$JAVA_HOME/bin:$PATH
mvn -pl ruoyi-generator -am clean package -DskipTests
```
Expected: generator module builds successfully.

- [ ] **Step 5: Commit verification fixes (if any)**

```bash
git add -A
git commit -m "fix: address verification findings for table index column"
```

---

## Self-Review

### Spec coverage

| Spec requirement | Covered by task |
|---|---|
| 所有列表不允许出现 id/编号/主键列 | Task 2–6 remove id columns; Task 7 prevents new id columns |
| 列表第一列必须是序号 | Task 2–6 insert index column first; Task 7 inserts it in templates |
| 序号由前端计算 | Task 1 `IndexColumn` uses `(page-1)*size + index + 1`; Task 7 same formula |
| 生成器模板同步改造 | Task 7 |
| 前端构建通过 | Task 8 Step 3 |

### Placeholder scan

No TBD/TODO/"implement later"/"similar to" placeholders. Each step provides exact file paths, code snippets, and commands.

### Type consistency

- `IndexColumn` props: `page` and `size` are consistently `Number`.
- Pagination variable names: `queryParams.pageNum`/`queryParams.pageSize` used by most pages; tree tables and special pages use `scope.$index + 1`.
- Vue 2 templates use `slot-scope`; Vue 3 templates use `#default="scope"` consistently.

---

## Execution Handoff

Plan complete and saved to `docs/superpowers/plans/2026-06-27-table-no-id-index-column-plan.md`.

**Two execution options:**

1. **Subagent-Driven (recommended)** — dispatch a fresh subagent per task, review between tasks, fast iteration.
2. **Inline Execution** — execute tasks in this session using `executing-plans`, batch execution with checkpoints.

Choose an approach to proceed.
