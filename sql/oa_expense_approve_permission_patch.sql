-- 费用审批权限补丁
-- 用途：为已部署环境补充报销审批、借款审批按钮权限，并分配给审批人角色（role_id=3）
-- 字符集: utf8mb4

-- 报销审批按钮权限
INSERT IGNORE INTO `sys_menu` VALUES (3180, '报销审批', 3120, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'oa:expenseReport:approve', '#', 'admin', NOW(), '', NULL, '');

-- 借款审批按钮权限
INSERT IGNORE INTO `sys_menu` VALUES (3181, '借款审批', 3125, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'oa:expenseLoan:approve', '#', 'admin', NOW(), '', NULL, '');

-- 为管理员角色分配审批按钮权限
INSERT IGNORE INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES
(1, 3180),
(1, 3181);

-- 为审批人角色分配审批按钮权限
INSERT IGNORE INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES
(3, 3180),
(3, 3181);
