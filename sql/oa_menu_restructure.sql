-- 菜单重构：OA 协同办公目录下的页面按业务分类整理为二级目录
-- Task 1.1: Restructure OA menus into categories

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

-- 移动现有页面到对应目录（按钮权限 menu_type='F' 会随父页面自动移动，无需修改）
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

UPDATE sys_menu SET parent_id = 3250, order_num = 1 WHERE menu_id = 3120; -- 费用报销
UPDATE sys_menu SET parent_id = 3250, order_num = 2 WHERE menu_id = 3121; -- 费用类型
UPDATE sys_menu SET parent_id = 3250, order_num = 3 WHERE menu_id = 3122; -- 费用标准
UPDATE sys_menu SET parent_id = 3250, order_num = 4 WHERE menu_id = 3123; -- 发票管理
UPDATE sys_menu SET parent_id = 3250, order_num = 5 WHERE menu_id = 3124; -- 预算管理
UPDATE sys_menu SET parent_id = 3250, order_num = 6 WHERE menu_id = 3125; -- 借款还款

UPDATE sys_menu SET parent_id = 3260, order_num = 1 WHERE menu_id = 3150; -- 资产管理
UPDATE sys_menu SET parent_id = 3260, order_num = 2 WHERE menu_id = 3151; -- 资产分类
UPDATE sys_menu SET parent_id = 3260, order_num = 3 WHERE menu_id = 3152; -- 资产盘点

-- 门户工作台保持一级，但顺序置顶
UPDATE sys_menu SET order_num = 0 WHERE menu_id = 3170;
