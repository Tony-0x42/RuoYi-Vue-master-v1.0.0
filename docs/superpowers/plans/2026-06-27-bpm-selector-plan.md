# BPM 流程定义「去 ID 输入」改造计划

> **目标：** 消除流程定义、任务办理相关页面中所有需要用户直接输入 ID 的交互，统一替换为选择框（下拉/树形/弹窗表格等）。通过参考业界 BPM 设计器实践，让流程定义更易用、更易维护。

---

## 一、背景与问题

当前项目 BPM 模块中，流程设计器和待办任务页面仍存在让用户直接输入用户 ID / 角色 ID 的文本框。问题包括：

1. **可读性差**：用户记不住数字 ID，容易输错。
2. **体验差**：需要切换到用户管理/角色管理页面查询 ID 再回填。
3. **维护性差**：ID 与名称无绑定，后续人员变动后无法直观追溯。
4. **不符合业务习惯**：国内 BPM 系统普遍采用「人员选择器」「角色选择器」「部门选择器」等组件。

---

## 二、线上调研结论

参考 Flowable/Camunda 设计器、RuoYi Office BPM、FlowLong、Pu-Bpmn-Design 等案例，主流做法如下：

| 场景 | 推荐组件 | 说明 |
|---|---|---|
| **单个办理人** | 人员单选（远程搜索下拉 / 弹窗选择 / 树形选择） | 只选一人，通常用 `el-select` 远程搜索或弹窗表格 |
| **多个候选人** | 人员多选（多选下拉 / 弹窗多选表格） | 需要同时选多人，适合弹窗表格批量勾选 |
| **候选角色/组** | 角色多选（多选下拉 / 弹窗多选） | 角色数量可控，可用 `el-select` 多选 |
| **候选部门** | 部门树形多选 | 部门有层级，适合 `el-tree` 或 `treeselect` |
| **动态表达式** | 下拉 + 输入混合 | 如 `${initiator}`、`#{approveUser}` 等表达式与固定人员并存 |

**关键设计原则**：

- 界面上展示 **名称/昵称**，底层 BPMN XML / 变量中存储 **ID 或表达式**。
- 人员数据量大时，必须支持 **远程搜索 + 分页**，避免一次性加载全部用户。
- 选择组件支持 **清除/多选/回显**，与设计器属性面板解耦。

---

## 三、当前需要改造的位置

### 位置 1：流程设计器 - 用户任务属性面板

**文件：** `ruoyi-ui/src/views/bpm/designer/index.vue`

**当前问题代码（第 29-35 行）：**

```vue
<template v-if="isUserTask">
  <el-form-item label="办理人">
    <el-input v-model="elementAssignee" placeholder="用户ID" @change="updateProperties" />
  </el-form-item>
  <el-form-item label="候选角色">
    <el-input v-model="elementCandidateGroups" placeholder="角色ID，多个用逗号分隔" @change="updateProperties" />
  </el-form-item>
  ...
</template>
```

**读取/写入 BPMN 属性代码（第 190-225 行）：**

```javascript
// 读取
this.elementAssignee = businessObject.get('flowable:assignee') || ''
this.elementCandidateGroups = businessObject.get('flowable:candidateGroups') || ''

// 写入
props['flowable:assignee'] = this.elementAssignee
props['flowable:candidateGroups'] = this.elementCandidateGroups
```

**改造方案：**

| 字段 | 改造为 | 数据来源 | 存储形式 |
|---|---|---|---|
| `办理人` | 人员单选选择器（远程搜索 `el-select` + 弹窗备选） | `/system/user/list` | 用户 ID 字符串 |
| `候选角色` | 角色多选选择器（`el-select` 多选） | `/system/role/list` | 逗号分隔的角色 ID 字符串 |
| 新增 `候选用户` | 人员多选选择器 | `/system/user/list` | 逗号分隔的用户 ID 字符串 |
| 新增 `候选部门` | 部门树形多选 | `/system/dept/list` | 逗号分隔的部门 ID 字符串 |

**组件选型建议：**

- **办理人**：`el-select` 远程搜索（filterable + remote）即可，单选；或封装一个 `UserSelect` 组件支持「搜索 + 弹窗」两种模式。
- **候选角色**：`el-select` 多选，角色列表通常几百条以内，可一次性加载。
- **候选用户**：人员多时（可能上万），建议弹窗表格 + 分页 + 搜索，支持多选。
- **候选部门**：`el-tree` 或 `treeselect` 多选，部门有层级关系。

**BPMN 属性映射：**

| 字段 | BPMN XML 属性 |
|---|---|
| 办理人 | `flowable:assignee` |
| 候选用户 | `flowable:candidateUsers` |
| 候选角色 | `flowable:candidateGroups` |
| 候选部门 | 通过扩展属性 `flowable:candidateDepts` 或写入 `extensionElements` |

**与 v2 运行时的衔接：**

当前 v2 运行时实际解析的是 `BpmProcessDefinition.extJson` 中的 `BpmNode.assignees`（按类型分用户/角色/部门）。因此前端保存模型时，除了把 ID 写入 BPMN XML，还需要在保存前把选择结果同步到 `extJson` 的节点配置中。具体做法：

1. 设计器保存时调用 `saveModel(definitionId, xml)`。
2. 后端 `saveModel` 解析 XML，将每个 `UserTask` 的 `assignee/candidateUsers/candidateGroups` 转换为 `BpmNode.assignees` 列表。
3. 或者前端在保存前先把选择结果写入 `extensionElements`（如 `<flowable:assigneeType>USER</flowable:assigneeType>`），后端解析扩展元素生成 `extJson`。

**推荐：** 采用「扩展元素」方式，既保留 BPMN 标准属性，又方便后端解析。例如：

```xml
<bpmn2:userTask id="Task_1" name="审批" flowable:assignee="1">
  <bpmn2:extensionElements>
    <flowable:assigneeType>USER</flowable:assigneeType>
    <flowable:candidateTypes>ROLE</flowable:candidateTypes>
    <flowable:candidateRoleIds>2,3</flowable:candidateRoleIds>
  </bpmn2:extensionElements>
</bpmn2:userTask>
```

---

### 位置 2：待办任务 - 任务指派 / 特送

**文件：** `ruoyi-ui/src/views/bpm/task/todo.vue`

**当前问题代码（第 315-340 行）：**

```javascript
handleAssign(row) {
  this.$prompt('请输入指派用户ID', '任务指派', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    inputPattern: /^\d+$/,
    inputErrorMessage: '用户ID必须为数字'
  }).then(({ value }) => {
    assignTask(row.flowableTaskId, value).then(() => {
      this.$modal.msgSuccess("指派成功")
      this.getList()
    })
  })
},
handleDelegate(row) {
  this.$prompt('请输入特送用户ID', '任务特送', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    inputPattern: /^\d+$/,
    inputErrorMessage: '用户ID必须为数字'
  }).then(({ value }) => {
    delegateTask(row.flowableTaskId, value).then(() => {
      this.$modal.msgSuccess("特送成功")
      this.getList()
    })
  })
}
```

**改造方案：**

将 `this.$prompt` 弹窗改为统一的用户选择弹窗组件，弹窗内嵌：

- 搜索框（按用户名/昵称模糊搜索）
- 用户表格（分页）
- 单选 / 多选（指派/特送均为单用户，用单选）

**推荐组件：** 复用或新建 `UserSelectDialog` 组件，点击「指派」「特送」时打开，选中用户后调用原 `assignTask` / `delegateTask` API。

---

## 四、新增/复用的公共组件

为保持统一，建议新增以下公共组件（放在 `ruoyi-ui/src/components/`）：

| 组件名 | 用途 | 位置建议 |
|---|---|---|
| `UserSelect` | 人员单选（下拉远程搜索） | `ruoyi-ui/src/components/UserSelect/index.vue` |
| `UserMultiSelect` | 人员多选（弹窗表格） | `ruoyi-ui/src/components/UserMultiSelect/index.vue` |
| `RoleMultiSelect` | 角色多选（下拉多选） | `ruoyi-ui/src/components/RoleMultiSelect/index.vue` |
| `DeptTreeSelect` | 部门树形多选 | `ruoyi-ui/src/components/DeptTreeSelect/index.vue` |
| `UserSelectDialog` | 用户单选弹窗（任务指派/特送） | `ruoyi-ui/src/components/UserSelectDialog/index.vue` |

**组件设计要点：**

- 统一 props：`value` / `v-model` 传 ID 字符串（单选为字符串，多选为数组或逗号分隔字符串）。
- 统一显示：组件内部维护 `label` 显示名称，回显时通过 ID 反查名称。
- 统一事件：`change` 事件返回 ID。

---

## 五、后端配合点

当前后端接口已足够支撑大部分选择器：

| 数据 | 已有接口 | 是否满足 |
|---|---|---|
| 用户列表 | `GET /system/user/list` | ✅ 支持分页、按 `userName` 搜索 |
| 角色列表 | `GET /system/role/list` | ✅ 满足 |
| 部门列表 | `GET /system/dept/list` | ✅ 返回树形 |
| 用户部门树 | `GET /system/user/deptTree` | ✅ 可选备用 |

**需要新增/修改的后端逻辑：**

1. **`BpmDefinitionService.saveModel()` 解析 BPMN 扩展元素**
   - 在保存 XML 时，解析每个 `UserTask` 的 `flowable:assignee`、`flowable:candidateUsers`、`flowable:candidateGroups` 以及扩展元素。
   - 生成/更新 `BpmProcessDefinition.extJson` 中的 `BpmNode.assignees` 配置。

2. **提供「按 ID 批量查询用户/角色/部门名称」接口（可选但推荐）**
   - 用于设计器回显时，根据已保存的 ID 反查名称。
   - 例如：`POST /system/user/listByIds` 返回 `{userId, nickName}` 列表。
   - 若已有 `/system/user/list` 支持 `userIds` 参数，可复用。

3. **任务指派/特送 API 校验**
   - `assignTask` / `delegateTask` 接收用户 ID 即可，无需改动；前端保证只传合法 ID。

---

## 六、实施步骤

建议按以下顺序实施，每个步骤独立可测：

### 步骤 1：封装公共选择组件

- 新建 `UserSelect`、`RoleMultiSelect`、`DeptTreeSelect`、`UserMultiSelect`、`UserSelectDialog`。
- 每个组件提供独立的示例页面或 Story，验证单选/多选/回显/清除。

### 步骤 2：改造流程设计器属性面板

- 修改 `ruoyi-ui/src/views/bpm/designer/index.vue`：
  - 将「办理人」`el-input` 替换为 `UserSelect`。
  - 将「候选角色」`el-input` 替换为 `RoleMultiSelect`。
  - 新增「候选用户」使用 `UserMultiSelect`。
  - 新增「候选部门」使用 `DeptTreeSelect`。
  - 调整 `loadElementProperties` 和 `updateProperties`，读取/写入新的 BPMN 属性。
- 设计器保存时，确保 XML 中包含扩展元素供后端解析。

### 步骤 3：改造任务指派/特送

- 修改 `ruoyi-ui/src/views/bpm/task/todo.vue`：
  - 将 `handleAssign` / `handleDelegate` 中的 `$prompt` 替换为 `UserSelectDialog`。
  - 选中用户后调用原 API。

### 步骤 4：后端解析 BPMN 扩展元素生成 extJson

- 修改 `ruoyi-bpm-v2` 中的保存/发布逻辑：
  - 在 `saveModel` 或 `deployDefinition` 时解析 XML。
  - 将解析结果写入 `BpmProcessDefinition.extJson`。
  - 确保启动流程时 `BpmTaskCandidateInvoker` 能正确读取。

### 步骤 5：回归测试

- 测试设计器保存、发布、启动流程后任务分配是否正确。
- 测试任务指派/特送功能是否正常。
- 测试已发布流程的历史版本加载时，设计器能否正确回显选择结果。

---

## 七、验收标准

1. 流程设计器属性面板中不再有要求用户输入 ID 的文本框。
2. 待办任务的「指派」「特送」不再使用 `$prompt` 输入 ID。
3. 所有选择器在界面上展示名称，底层存储 ID 或表达式。
4. 保存/发布后，流程能正确分配任务给指定用户/角色/部门。
5. 重新打开设计器时，已配置的人员/角色/部门能正确回显。

---

## 八、风险与注意事项

1. **BPMN XML 兼容性**：新增扩展元素需确保 `flowable-bpmn-moddle` 能正常序列化/反序列化。建议在 `flowable-bpmn-moddle/resources/camunda.json` 中确认已有属性支持，或自定义 moddle 扩展。
2. **v2 运行时依赖 extJson**：若只改前端不改后端解析，v2 运行时仍会按旧的 `extJson` 分配任务，导致设计器选择无效。
3. **人员数据量大**：用户表可能很大，必须分页 + 远程搜索，禁止一次性加载全部用户。
4. **历史流程版本**：旧版本 XML 中可能已有直接写的 ID 字符串，需要兼容读取；写入新数据时统一走选择器。
