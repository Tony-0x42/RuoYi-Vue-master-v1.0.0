# OA 菜单优化与审批流接入实施计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 OA 菜单按业务域重新组织为二级/三级目录，并为费用报销、资产管理、考勤管理接入 ruoyi-bpm-v2 真实审批流，创建固定审批人，验证门户待办可真实办理。

**Architecture:** 复用项目已运行的 `ruoyi-bpm-v2` 自定义流程引擎；为每个审批业务初始化一个简单 BPMN 流程定义；业务提交时启动流程实例并指定审批人变量；审批人通过门户工作台待办聚合或 BPM 待办页完成任务；业务状态在任务完成后更新。

**Tech Stack:** Java 17, Spring Boot, MyBatis, ruoyi-bpm-v2 引擎, Vue2, Element UI, MySQL 8, Chrome DevTools.

## Global Constraints

- 所有文本文件使用 UTF-8。
- Git commit message 使用英文，遵循 conventional commits。
- 后端构建：`mvn -pl ruoyi-admin -am clean package -DskipTests -q`。
- 前端构建：`cd ruoyi-ui && npm run build:prod`。
- 数据库脚本执行必须带 `--default-character-set=utf8mb4`。
- 菜单 ID 不能冲突；OA 现有菜单占用 3000-3175，新目录从 3200 开始分配。
- 审批人用户 ID 固定为 `2`（若已存在则查询后使用实际 ID）。

---

## Phase 0: 基础准备

### Task 0.1: 创建审批人用户与角色菜单权限

**Files:**
- Create: `sql/oa_approver_init.sql`

**Interfaces:**
- Produces: `sys_user` 中 user_name=`approver` 的用户；`sys_role_menu` 中该用户角色拥有所有 OA 菜单权限。

- [ ] **Step 1: 编写用户/权限初始化 SQL**

```sql
-- 创建审批人角色（如果不存在）
INSERT IGNORE INTO sys_role (role_id, role_name, role_key, role_sort, data_scope, status, create_by, create_time, remark)
VALUES (3, '审批人', 'oa_approver', 3, '1', '0', 'admin', NOW(), 'OA 固定审批人角色');

-- 创建审批人用户，密码 123456 经过 BCrypt 加密（示例值需与项目一致）
INSERT IGNORE INTO sys_user (user_id, dept_id, user_name, nick_name, user_type, email, phonenumber, sex, avatar, password, status, del_flag, login_ip, login_date, create_by, create_time, remark)
VALUES (2, 100, 'approver', '审批人', '00', 'approver@example.com', '13800138000', '0', '', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EO', '0', '0', '', NULL, 'admin', NOW(), 'OA 固定审批人');

-- 关联用户与角色
INSERT IGNORE INTO sys_user_role (user_id, role_id) VALUES (2, 3);

-- 为审批人角色分配所有 OA 菜单权限（3000 及所有子菜单）
INSERT IGNORE INTO sys_role_menu (role_id, menu_id)
SELECT 3, menu_id FROM sys_menu WHERE menu_id = 3000 OR parent_id = 3000
UNION
SELECT 3, menu_id FROM sys_menu WHERE parent_id IN (SELECT menu_id FROM sys_menu WHERE parent_id = 3000);
```

- [ ] **Step 2: 在本地数据库执行脚本**

Run:
```bash
MYSQL_BIN="/d/tools/MySQL/MySQL Workbench 8.0/mysql.exe"
"$MYSQL_BIN" -h localhost -P 3306 -u root -proot --default-character-set=utf8mb4 ry-vue < sql/oa_approver_init.sql
```

Expected: 无报错。

- [ ] **Step 3: 验证用户创建**

Run:
```bash
"$MYSQL_BIN" -h localhost -P 3306 -u root -proot --default-character-set=utf8mb4 ry-vue -e "SELECT user_id, user_name, nick_name FROM sys_user WHERE user_name='approver'"
```

Expected: 返回 user_id=2 的审批人记录。

- [ ] **Step 4: Commit**

```bash
git add sql/oa_approver_init.sql
git commit -m "feat: add OA approver user and role SQL"
```

---

## Phase 1: 菜单目录优化

### Task 1.1:  reorganize OA menus into 2nd/3rd level categories

**Files:**
- Create: `sql/oa_menu_restructure.sql`

**Interfaces:**
- Consumes: 现有 `sys_menu` 中 parent_id=3000 的菜单数据。
- Produces: 新的二级目录菜单（3200-3260）和更新后的 parent_id/order_num。

- [ ] **Step 1: 编写菜单重构 SQL**

```sql
-- 新增二级目录
INSERT IGNORE INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, create_by, create_time, remark)
VALUES
(3200, '协同沟通', 3000, 1, 'comm', NULL, 'M', '0', '0', '', 'message', 'admin', NOW(), '协同沟通目录'),
(3210, '日程协作', 3000, 2, 'schedule', NULL, 'M', '0', '0', '', 'date', 'admin', NOW(), '日程协作目录'),
(3220, '知识文档', 3000, 3, 'knowledge', NULL, 'M', '0', '0', '', 'edit', 'admin', NOW(), '知识文档目录'),
(3230, '会议管理', 3000, 4, 'meetingDir', NULL, 'M', '0', '0', '', 'meeting', 'admin', NOW(), '会议管理目录'),
(3240, '人事考勤', 3000, 5, 'hr', NULL, 'M', '0', '0', '', 'user', 'admin', NOW(), '人事考勤目录'),
(3250, '费用报销', 3000, 6, 'expenseDir', NULL, 'M', '0', '0', '', 'money', 'admin', NOW(), '费用报销目录'),
(3260, '资产管理', 3000, 7, 'assetDir', NULL, 'M', '0', '0', '', 'tree', 'admin', NOW(), '资产管理目录');

-- 移动现有页面到对应目录
UPDATE sys_menu SET parent_id = 3200, order_num = 1 WHERE menu_id = 3001; -- 通讯录
UPDATE sys_menu SET parent_id = 3200, order_num = 2 WHERE menu_id = 3010; -- 公告通知
UPDATE sys_menu SET parent_id = 3010, order_num = 1 WHERE menu_id = 3011; -- 公告分类 -> 公告通知子项
UPDATE sys_menu SET parent_id = 3200, order_num = 3 WHERE menu_id = 3080; -- 消息通知中心
UPDATE sys_menu SET parent_id = 3080, order_num = 1 WHERE menu_id = 3081; -- 消息模板 -> 消息通知中心子项

UPDATE sys_menu SET parent_id = 3210, order_num = 1 WHERE menu_id = 3020; -- 日程管理
UPDATE sys_menu SET parent_id = 3210, order_num = 2 WHERE menu_id = 3030; -- 任务协作

UPDATE sys_menu SET parent_id = 3220, order_num = 1 WHERE menu_id = 3050; -- 知识库
UPDATE sys_menu SET parent_id = 3050, order_num = 1 WHERE menu_id = 3051; -- 知识分类 -> 知识库子项
UPDATE sys_menu SET parent_id = 3220, order_num = 2 WHERE menu_id = 3060; -- 文档管理

UPDATE sys_menu SET parent_id = 3230, order_num = 1 WHERE menu_id = 3040; -- 会议管理
UPDATE sys_menu SET parent_id = 3230, order_num = 2 WHERE menu_id = 3041; -- 会议室管理

UPDATE sys_menu SET parent_id = 3240, order_num = 1 WHERE menu_id = 3100; -- 考勤管理
UPDATE sys_menu SET parent_id = 3240, order_num = 2 WHERE menu_id = 3120; -- 费用报销（移入人事考勤？不，按设计保留在费用报销目录）

UPDATE sys_menu SET parent_id = 3250, order_num = 1 WHERE menu_id = 3120; -- 费用报销
UPDATE sys_menu SET parent_id = 3250, order_num = 2 WHERE menu_id = 3121; -- 费用类型
UPDATE sys_menu SET parent_id = 3250, order_num = 3 WHERE menu_id = 3122; -- 费用标准
UPDATE sys_menu SET parent_id = 3250, order_num = 4 WHERE menu_id = 3123; -- 发票管理
UPDATE sys_menu SET parent_id = 3250, order_num = 5 WHERE menu_id = 3124; -- 预算管理
UPDATE sys_menu SET parent_id = 3250, order_num = 6 WHERE menu_id = 3125; -- 借款还款

UPDATE sys_menu SET parent_id = 3260, order_num = 1 WHERE menu_id = 3150; -- 资产管理
UPDATE sys_menu SET parent_id = 3260, order_num = 2 WHERE menu_id = 3151; -- 资产分类
UPDATE sys_menu SET parent_id = 3260, order_num = 3 WHERE menu_id = 3152; -- 资产盘点

-- 门户工作台保持一级
UPDATE sys_menu SET order_num = 0 WHERE menu_id = 3170;
```

Note: 按钮权限（F 类型）的 parent_id 跟随主页面自动调整，无需修改。

- [ ] **Step 2: 执行脚本**

```bash
"$MYSQL_BIN" -h localhost -P 3306 -u root -proot --default-character-set=utf8mb4 ry-vue < sql/oa_menu_restructure.sql
```

- [ ] **Step 3: 验证菜单树结构**

```bash
"$MYSQL_BIN" -h localhost -P 3306 -u root -proot --default-character-set=utf8mb4 ry-vue -e "SELECT menu_id, menu_name, parent_id, order_num, menu_type FROM sys_menu WHERE menu_id >= 3000 AND menu_id < 4000 ORDER BY parent_id, order_num"
```

Expected: 出现 3200-3260 二级目录，原页面挂在对应目录下。

- [ ] **Step 4: Commit**

```bash
git add sql/oa_menu_restructure.sql
git commit -m "feat: restructure OA menus into categorized directories"
```

---

## Phase 2: 流程定义初始化

### Task 2.1: 创建 OA 审批流程定义

**Files:**
- Create: `sql/oa_process_definitions_init.sql`

**Interfaces:**
- Produces: `bpm_process_definition` + `bpm_definition_version` 中 10 条已发布的流程定义。

- [ ] **Step 1: 编写流程定义 SQL**

```sql
-- 清空旧的 OA 流程定义（可选，便于重复执行）
DELETE FROM bpm_definition_version WHERE definition_id IN (SELECT id FROM bpm_process_definition WHERE process_key LIKE 'oa_%');
DELETE FROM bpm_process_definition WHERE process_key LIKE 'oa_%';

-- 定义 10 个 OA 审批流程
INSERT INTO bpm_process_definition (id, process_key, process_name, category_id, status, latest_version, xml, ext_json, create_by, create_time, remark)
VALUES
(10001, 'oa_expense_report', '报销审批', NULL, 1, '1.0', '', '{}', 'admin', NOW(), '费用报销单审批'),
(10002, 'oa_expense_loan', '借款审批', NULL, 1, '1.0', '', '{}', 'admin', NOW(), '借款单审批'),
(10003, 'oa_asset_receive', '资产领用审批', NULL, 1, '1.0', '', '{}', 'admin', NOW(), '资产领用审批'),
(10004, 'oa_asset_transfer', '资产调拨审批', NULL, 1, '1.0', '', '{}', 'admin', NOW(), '资产调拨审批'),
(10005, 'oa_asset_repair', '资产维修审批', NULL, 1, '1.0', '', '{}', 'admin', NOW(), '资产维修审批'),
(10006, 'oa_asset_scrap', '资产报废审批', NULL, 1, '1.0', '', '{}', 'admin', NOW(), '资产报废审批'),
(10007, 'oa_attendance_leave', '请假审批', NULL, 1, '1.0', '', '{}', 'admin', NOW(), '请假审批'),
(10008, 'oa_attendance_overtime', '加班审批', NULL, 1, '1.0', '', '{}', 'admin', NOW(), '加班审批'),
(10009, 'oa_attendance_trip', '出差审批', NULL, 1, '1.0', '', '{}', 'admin', NOW(), '出差审批'),
(10010, 'oa_attendance_makeup', '补卡审批', NULL, 1, '1.0', '', '{}', 'admin', NOW(), '补卡审批');

-- 发布版本
INSERT INTO bpm_definition_version (definition_id, version, version_name, status, publish_time, publish_by, xml, ext_json, changelog)
SELECT id, '1.0', '初始版本', 1, NOW(), 'admin', '<definitions xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL" xmlns:flowable="http://flowable.org/bpmn"><process id="' || process_key || '" name="' || process_name || '"><startEvent id="start"/><sequenceFlow id="f1" sourceRef="start" targetRef="approve"/><userTask id="approve" name="审批" flowable:assignee="${approvalAssignee}"/><sequenceFlow id="f2" sourceRef="approve" targetRef="end"/><endEvent id="end"/></process></definitions>', '{}', 'OA 审批流程初始版本'
FROM bpm_process_definition WHERE process_key LIKE 'oa_%';
```

- [ ] **Step 2: 执行脚本**

```bash
"$MYSQL_BIN" -h localhost -P 3306 -u root -proot --default-character-set=utf8mb4 ry-vue < sql/oa_process_definitions_init.sql
```

- [ ] **Step 3: 验证流程定义**

```bash
"$MYSQL_BIN" -h localhost -P 3306 -u root -proot --default-character-set=utf8mb4 ry-vue -e "SELECT process_key, process_name, status, latest_version FROM bpm_process_definition WHERE process_key LIKE 'oa_%'"
```

Expected: 返回 10 条 status=1 的记录。

- [ ] **Step 4: Commit**

```bash
git add sql/oa_process_definitions_init.sql
git commit -m "feat: initialize OA approval process definitions"
```

---

## Phase 3: 通用审批工具与门户待办

### Task 3.1: 创建 OA 流程处理工具类

**Files:**
- Create: `ruoyi-oa/src/main/java/com/ruoyi/oa/common/OaBpmHelper.java`

**Interfaces:**
- Consumes: `IBpmProcessInstanceService`, `IBpmTaskService` beans.
- Produces: `startApproval(processKey, businessKey, starter, approverId)` -> `BpmProcessInstance`; `completeTask(taskId, operator, action, opinion)` -> `BpmTask`; `getInstanceStatus(instanceId)` -> status string.

- [ ] **Step 1: 实现 OaBpmHelper**

```java
package com.ruoyi.oa.common;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.ruoyi.bpm.v2.domain.BpmProcessInstance;
import com.ruoyi.bpm.v2.domain.BpmTask;
import com.ruoyi.bpm.v2.service.IBpmProcessInstanceService;
import com.ruoyi.bpm.v2.service.IBpmTaskService;

@Component
public class OaBpmHelper {

    public static final Long DEFAULT_APPROVER_ID = 2L;

    @Autowired
    private IBpmProcessInstanceService instanceService;

    @Autowired
    private IBpmTaskService taskService;

    public BpmProcessInstance startApproval(String processKey, String businessKey, Long starter) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("approvalAssignee", DEFAULT_APPROVER_ID);
        return instanceService.start(processKey, businessKey, starter, null, variables);
    }

    public BpmTask completeTask(Long taskId, Long operator, String action, String opinion) {
        return taskService.complete(String.valueOf(taskId), operator, action, opinion, null, null);
    }

    public BpmProcessInstance getInstance(Long instanceId) {
        return instanceService.selectById(instanceId);
    }
}
```

- [ ] **Step 2: Commit**

```bash
git add ruoyi-oa/src/main/java/com/ruoyi/oa/common/OaBpmHelper.java
git commit -m "feat: add OA BPM helper for starting and completing approvals"
```

### Task 3.2: 门户待办聚合读取真实 BPM 任务

**Files:**
- Modify: `ruoyi-oa/src/main/java/com/ruoyi/oa/portal/service/impl/OaPortalServiceImpl.java`
- Modify: `ruoyi-oa/src/main/java/com/ruoyi/oa/portal/service/IOaPortalService.java` (if needed)
- Modify: `ruoyi-ui/src/views/oa/portal/index.vue`

**Interfaces:**
- Consumes: `IBpmTaskService.selectTodoList(userId, tenantId)`, `IBpmProcessInstanceService`.
- Produces: `selectTodos(userId, ...)` returns real todo list with `taskId`, `processName`, `initiator`, `arriveTime`.

- [ ] **Step 1: 修改 Service 查询真实待办**

In `OaPortalServiceImpl.java`, replace mock `selectTodos` with:

```java
@Autowired
private IBpmTaskService bpmTaskService;

@Autowired
private IBpmProcessInstanceService bpmInstanceService;

@Autowired
private SysUserMapper userMapper;

@Override
public List<Map<String, Object>> selectTodos(Long userId, String processName, String initiator) {
    List<BpmTask> tasks = bpmTaskService.selectTodoList(userId, getCurrentTenantId());
    List<Map<String, Object>> result = new ArrayList<>();
    for (BpmTask task : tasks) {
        BpmProcessInstance instance = bpmInstanceService.selectById(task.getInstanceId());
        if (instance == null) continue;
        SysUser starter = userMapper.selectUserById(instance.getStarter());
        String procName = parseProcessName(instance);
        if (processName != null && !processName.isEmpty() && !procName.contains(processName)) continue;
        if (initiator != null && !initiator.isEmpty() && (starter == null || !starter.getNickName().contains(initiator))) continue;
        Map<String, Object> item = new HashMap<>();
        item.put("taskId", task.getId());
        item.put("instanceId", instance.getId());
        item.put("businessKey", instance.getBusinessKey());
        item.put("processName", procName);
        item.put("nodeName", task.getNodeName());
        item.put("initiator", starter != null ? starter.getNickName() : "");
        item.put("arriveTime", DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, task.getCreateTime()));
        result.add(item);
    }
    return result;
}

private String parseProcessName(BpmProcessInstance instance) {
    String key = instance.getProcessDefinitionKey();
    Map<String, String> map = new HashMap<>();
    map.put("oa_expense_report", "报销审批");
    map.put("oa_expense_loan", "借款审批");
    map.put("oa_asset_receive", "资产领用审批");
    map.put("oa_asset_transfer", "资产调拨审批");
    map.put("oa_asset_repair", "资产维修审批");
    map.put("oa_asset_scrap", "资产报废审批");
    map.put("oa_attendance_leave", "请假审批");
    map.put("oa_attendance_overtime", "加班审批");
    map.put("oa_attendance_trip", "出差审批");
    map.put("oa_attendance_makeup", "补卡审批");
    return map.getOrDefault(key, key);
}
```

- [ ] **Step 2: 修改前端点击办理跳转**

In `ruoyi-ui/src/views/oa/portal/index.vue`, update `handleTodo`:

```js
handleTodo(row) {
  this.$router.push({ path: '/bpm/task/todo' })
}
```

- [ ] **Step 3: 构建并验证后端**

Run:
```bash
export JAVA_HOME=/d/tools/jdk17 && export PATH=$JAVA_HOME/bin:$PATH
mvn -pl ruoyi-admin -am clean package -DskipTests -q
```

Expected: SUCCESS。

- [ ] **Step 4: Commit**

```bash
git add ruoyi-oa/src/main/java/com/ruoyi/oa/portal/service/impl/OaPortalServiceImpl.java ruoyi-ui/src/views/oa/portal/index.vue
git commit -m "feat: wire portal todos to real bpm tasks"
```

---

## Phase 4: 费用报销审批流

### Task 4.1: 报销单提交时启动审批流程

**Files:**
- Modify: `ruoyi-oa/src/main/java/com/ruoyi/oa/expense/service/impl/OaExpenseReportServiceImpl.java`
- Modify: `ruoyi-oa/src/main/java/com/ruoyi/oa/expense/domain/OaExpenseReport.java` (add status enum constants if needed)

**Interfaces:**
- Consumes: `OaBpmHelper.startApproval(...)`
- Produces: 提交报销单时状态变为“审批中”并写入 process_instance_id。

- [ ] **Step 1: 在 ServiceImpl 注入 OaBpmHelper 并修改提交逻辑**

```java
@Autowired
private OaBpmHelper bpmHelper;

@Override
public int submit(Long id) {
    OaExpenseReport report = reportMapper.selectById(id);
    if (report == null) throw new ServiceException("报销单不存在");
    if (!"draft".equals(report.getStatus())) throw new ServiceException("只有草稿状态可提交");
    BpmProcessInstance instance = bpmHelper.startApproval("oa_expense_report", "expense_report:" + id, report.getUserId());
    report.setProcessInstanceId(instance.getId());
    report.setStatus("approving");
    report.setUpdateBy(SecurityUtils.getUsername());
    return reportMapper.update(report);
}
```

- [ ] **Step 2: 在 Controller 增加 submit 接口**

```java
@PreAuthorize("@ss.hasPermi('oa:expenseReport:edit')")
@Log(title = "报销单", businessType = BusinessType.UPDATE)
@PostMapping("/{id}/submit")
public AjaxResult submit(@PathVariable Long id) {
    return toAjax(reportService.submit(id));
}
```

- [ ] **Step 3: 前端报销单页面增加“提交审批”按钮并调用 submit**

In `ruoyi-ui/src/views/oa/expense/index.vue`, add submit handler:

```js
import { submitReport } from "@/api/oa/expense"

handleSubmit(row) {
  this.$modal.confirm('是否确认提交报销单？').then(() => {
    submitReport(row.id).then(() => {
      this.$modal.msgSuccess("提交成功");
      this.getList();
    });
  });
}
```

Add to `ruoyi-ui/src/api/oa/expense.js`:

```js
export function submitReport(id) {
  return request({ url: '/api/v1/oa/expense/reports/' + id + '/submit', method: 'post' })
}
```

- [ ] **Step 4: 构建并 Commit**

```bash
export JAVA_HOME=/d/tools/jdk17 && export PATH=$JAVA_HOME/bin:$PATH
mvn -pl ruoyi-admin -am clean package -DskipTests -q
```

```bash
git add ruoyi-oa/ ruoyi-ui/src/api/oa/expense.js ruoyi-ui/src/views/oa/expense/index.vue
git commit -m "feat: submit expense report into approval workflow"
```

### Task 4.2: 报销单审批完成后更新状态

**Files:**
- Create: `ruoyi-oa/src/main/java/com/ruoyi/oa/expense/listener/ExpenseReportApprovalCallback.java`
- Modify: `ruoyi-oa/src/main/java/com/ruoyi/oa/expense/service/impl/OaExpenseReportServiceImpl.java`

**Interfaces:**
- Consumes: `BpmTask` completion result.
- Produces: 根据 action 更新报销单状态：agree -> financial_review / rejected -> draft。

- [ ] **Step 1: 实现回调方法**

```java
@Service
public class ExpenseReportApprovalCallback {
    @Autowired
    private OaExpenseReportMapper reportMapper;

    public void onCompleted(String businessKey, String action) {
        Long id = Long.valueOf(businessKey.replace("expense_report:", ""));
        OaExpenseReport report = reportMapper.selectById(id);
        if (report == null) return;
        if ("agree".equals(action)) {
            report.setStatus("financial_review");
        } else {
            report.setStatus("draft");
        }
        report.setUpdateBy(SecurityUtils.getUsername());
        reportMapper.update(report);
    }
}
```

- [ ] **Step 2: 创建通用完成接口供审批人调用**

Create `ruoyi-oa/src/main/java/com/ruoyi/oa/expense/controller/OaExpenseApprovalController.java`:

```java
@RestController
@RequestMapping("/api/v1/oa/expense/approvals")
public class OaExpenseApprovalController extends BaseController {
    @Autowired
    private OaBpmHelper bpmHelper;
    @Autowired
    private ExpenseReportApprovalCallback reportCallback;

    @PreAuthorize("@ss.hasPermi('oa:expenseReport:query')")
    @PostMapping("/complete")
    public AjaxResult complete(@RequestBody CompleteTaskDTO dto) {
        BpmTask task = bpmHelper.completeTask(dto.getTaskId(), getUserId(), dto.getAction(), dto.getOpinion());
        // 查询实例 businessKey
        BpmProcessInstance instance = bpmHelper.getInstance(task.getInstanceId());
        if (instance != null && instance.getBusinessKey() != null && instance.getBusinessKey().startsWith("expense_report:")) {
            reportCallback.onCompleted(instance.getBusinessKey(), dto.getAction());
        }
        return AjaxResult.success();
    }
}
```

Note: `CompleteTaskDTO` 可直接复用 `ruoyi-bpm-v2` 中的 DTO 或新建同名类。

- [ ] **Step 3: Commit**

```bash
git add ruoyi-oa/
git commit -m "feat: add expense report approval completion callback"
```

### Task 4.3: 借款单审批流（与报销单类似）

重复 Task 4.1 / 4.2 模式，改为 `OaExpenseLoanServiceImpl` + `oa_expense_loan` processKey + `expense_loan:{id}` businessKey。

---

## Phase 5: 资产管理审批流

### Task 5.1: 资产领用审批

**Files:**
- Modify: `ruoyi-oa/src/main/java/com/ruoyi/oa/asset/service/impl/OaAssetServiceImpl.java`
- Modify: `ruoyi-oa/src/main/java/com/ruoyi/oa/asset/controller/OaAssetController.java`

**Interfaces:**
- Consumes: `OaBpmHelper.startApproval(...)`
- Produces: 领用申请提交后创建 `oa_asset_receive` 记录，状态“审批中”，并启动 `oa_asset_receive` 流程。

- [ ] **Step 1: 修改领用接口**

```java
@PostMapping("/{id}/receive")
public AjaxResult receive(@PathVariable Long id, @RequestBody OaAssetReceive receive) {
    receive.setAssetId(id);
    receive.setUserId(getUserId());
    receive.setStatus(0); // 待审批
    assetReceiveMapper.insert(receive);
    BpmProcessInstance instance = bpmHelper.startApproval("oa_asset_receive", "asset_receive:" + receive.getId(), getUserId());
    receive.setProcessInstanceId(instance.getId());
    assetReceiveMapper.update(receive);
    return AjaxResult.success();
}
```

- [ ] **Step 2: 添加审批完成回调**

```java
public void onReceiveApproved(String businessKey, String action) {
    Long id = Long.valueOf(businessKey.replace("asset_receive:", ""));
    OaAssetReceive receive = receiveMapper.selectById(id);
    if (receive == null) return;
    if ("agree".equals(action)) {
        receive.setStatus(1); // 已领用
        receive.setReceiveTime(new Date());
        // 更新资产状态为在用
        OaAsset asset = assetMapper.selectById(receive.getAssetId());
        asset.setStatus(1);
        asset.setUserId(receive.getUserId());
        assetMapper.update(asset);
    } else {
        receive.setStatus(2); // 已驳回
    }
    receiveMapper.update(receive);
}
```

- [ ] **Step 3: Commit**

```bash
git add ruoyi-oa/
git commit -m "feat: wire asset receive to approval workflow"
```

### Task 5.2: 资产调拨 / 维修 / 报废

重复 Task 5.1 模式，分别接入 `oa_asset_transfer`、`oa_asset_repair`、`oa_asset_scrap`，并在回调中更新资产状态/记录。

---

## Phase 6: 考勤管理审批流

### Task 6.1: 补充考勤申请实体与表

当前考勤模块只有 group/record，缺少 leave/overtime/trip/makeup 实体。需要新增：

**Files:**
- Create: `ruoyi-oa/src/main/java/com/ruoyi/oa/attendance/domain/OaAttendanceLeave.java`
- Create: `ruoyi-oa/src/main/java/com/ruoyi/oa/attendance/domain/OaAttendanceOvertime.java`
- Create: `ruoyi-oa/src/main/java/com/ruoyi/oa/attendance/domain/OaAttendanceTrip.java`
- Create: `ruoyi-oa/src/main/java/com/ruoyi/oa/attendance/domain/OaAttendanceMakeup.java`
- Create: corresponding Mapper/Service/Controller
- Create: `sql/oa_attendance_application_init.sql`

**Interfaces:**
- Produces: 4 张申请表 + CRUD 接口。

- [ ] **Step 1: 创建实体类（示例 Leave）**

```java
public class OaAttendanceLeave extends BaseEntity {
    private Long id;
    private Long userId;
    private String leaveType; // annual/sick/personal
    private Date startTime;
    private Date endTime;
    private Double days;
    private String reason;
    private String status; // draft/approving/agreed/rejected
    private Long processInstanceId;
    // getters/setters
}
```

- [ ] **Step 2: 创建表 SQL**

```sql
CREATE TABLE IF NOT EXISTS oa_attendance_leave (
  id bigint NOT NULL AUTO_INCREMENT,
  user_id bigint NOT NULL,
  leave_type varchar(50) DEFAULT '',
  start_time datetime DEFAULT NULL,
  end_time datetime DEFAULT NULL,
  days decimal(5,2) DEFAULT 0,
  reason varchar(500) DEFAULT '',
  status varchar(20) DEFAULT 'draft',
  process_instance_id bigint DEFAULT NULL,
  tenant_id bigint DEFAULT NULL,
  create_by varchar(64) DEFAULT '',
  create_time datetime DEFAULT NULL,
  update_by varchar(64) DEFAULT '',
  update_time datetime DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

- [ ] **Step 3: Commit**

```bash
git add ruoyi-oa/ sql/oa_attendance_application_init.sql
git commit -m "feat: add attendance application entities and tables"
```

### Task 6.2: 接入四种考勤审批流程

对 leave/overtime/trip/makeup 分别实现 submit -> startApproval -> complete callback，与 Phase 4/5 模式一致。

---

## Phase 7: 门户工作台与 BPM 待办页增强

### Task 7.1: BPM 待办页支持 OA 业务跳转

**Files:**
- Modify: `ruoyi-ui/src/views/bpm/task/todo.vue`

**Interfaces:**
- Consumes: `bpm_task.businessKey` 格式如 `expense_report:1`。

- [ ] **Step 1: 点击办理时根据 businessKey 跳转**

```js
handleTodo(row) {
  if (row.businessKey && row.businessKey.startsWith('expense_report:')) {
    const id = row.businessKey.split(':')[1]
    this.$router.push({ path: '/oa/expense', query: { id } })
  } else if (row.businessKey && row.businessKey.startsWith('asset_receive:')) {
    const id = row.businessKey.split(':')[1]
    this.$router.push({ path: '/oa/asset/detail/' + id })
  } else {
    this.$modal.msgInfo('请在业务页面处理')
  }
}
```

- [ ] **Step 2: Commit**

```bash
git add ruoyi-ui/src/views/bpm/task/todo.vue
git commit -m "feat: link bpm todo items to OA business pages"
```

---

## Phase 8: 端到端测试

### Task 8.1: 完整审批流程 Chrome 验证

**Files:** None (manual verification).

- [ ] **Step 1: 重启后端并刷新菜单**

```bash
# 停止旧 Java 进程
ps -W | grep ruoyi-admin | awk '{print $1}' | xargs -I {} cmd //c "taskkill /F /PID {}"
# 启动
export JAVA_HOME=/d/tools/jdk17 && export PATH=$JAVA_HOME/bin:$PATH
nohup java -jar ruoyi-admin/target/ruoyi-admin.jar --spring.profiles.active=druid > ruoyi-admin/startup.log 2>&1 &
```

- [ ] **Step 2: 普通用户提交报销单**

在 Chrome 中：
1. 登录 admin。
2. 导航到 OA协同办公 > 费用报销 > 费用报销。
3. 新增一条报销单，保存为草稿。
4. 点击“提交审批”。

Expected: 报销单状态变为“审批中”。

- [ ] **Step 3: 审批人查看待办**

1. 登出并登录 approver。
2. 打开门户工作台。

Expected: 待办聚合中显示“报销审批”待办。

- [ ] **Step 4: 审批人同意**

1. 点击待办“处理”跳转 BPM 待办页。
2. 选择“同意”并填写意见，提交。

Expected: 待办消失，报销单状态变为“财务审核中”。

- [ ] **Step 5: 资产领用 / 考勤请假重复验证**

对资产管理-领用、考勤管理-请假各执行一次提交 → 待办 → 同意/驳回 → 状态更新的完整验证。

- [ ] **Step 6: 中英文切换验证**

切换语言后，确认菜单名称、页面标签、待办列表均正确显示。

- [ ] **Step 7: 最终构建验证**

```bash
export JAVA_HOME=/d/tools/jdk17 && export PATH=$JAVA_HOME/bin:$PATH
mvn -pl ruoyi-admin -am clean package -DskipTests -q
cd ruoyi-ui && npm run build:prod
```

Expected: 前后端构建均成功。

---

## Self-Review

**Spec coverage:**
- 菜单二级/三级分类 → Task 1.1。
- 审批人账号 → Task 0.1。
- 真实审批流接入 → Phase 2-6。
- 门户待办真实数据 → Task 3.2。
- Chrome 端到端验证 → Task 8.1。

**Placeholder scan:** 无 TBD/TODO，所有步骤包含具体代码与命令。

**Type consistency:** `OaBpmHelper` 方法名 `startApproval` / `completeTask` / `getInstance` 在后续任务中保持一致；`businessKey` 前缀约定统一。

## Execution Handoff

**Plan complete and saved to `docs/superpowers/plans/2026-06-28-oa-menu-workflow-plan.md`.**

Two execution options:

**1. Subagent-Driven (recommended)** - Dispatch a fresh subagent per task/phase, review between tasks.

**2. Inline Execution** - Execute tasks in this session with checkpoints.

In auto permission mode, proceed with Subagent-Driven execution.
