-- 费用报销模块初始化脚本
-- 字符集: utf8mb4

-- ----------------------------
-- 表结构: oa_expense_category
-- ----------------------------
DROP TABLE IF EXISTS `oa_expense_category`;
CREATE TABLE `oa_expense_category` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '类型ID',
  `parent_id` bigint DEFAULT '0' COMMENT '父类型ID',
  `name` varchar(100) NOT NULL COMMENT '类型名称',
  `code` varchar(100) NOT NULL COMMENT '类型编码',
  `process_key` varchar(100) DEFAULT NULL COMMENT '关联流程Key',
  `budget_item_id` bigint DEFAULT NULL COMMENT '预算科目ID',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态（0停用 1启用）',
  `sort` int DEFAULT '0' COMMENT '显示排序',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户ID',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='费用类型表';

-- ----------------------------
-- 表结构: oa_expense_standard
-- ----------------------------
DROP TABLE IF EXISTS `oa_expense_standard`;
CREATE TABLE `oa_expense_standard` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '标准ID',
  `category_id` bigint DEFAULT NULL COMMENT '费用类型ID',
  `level` varchar(50) DEFAULT NULL COMMENT '职级',
  `city` varchar(100) DEFAULT NULL COMMENT '城市',
  `dept_id` bigint DEFAULT NULL COMMENT '部门ID',
  `limit_amount` decimal(18,2) DEFAULT '0.00' COMMENT '限额',
  `period_type` varchar(20) DEFAULT 'time' COMMENT '周期类型（day按日 time按次 month按月 year按年）',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态（0停用 1启用）',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户ID',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_category_id` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='费用标准表';

-- ----------------------------
-- 表结构: oa_expense_invoice
-- ----------------------------
DROP TABLE IF EXISTS `oa_expense_invoice`;
CREATE TABLE `oa_expense_invoice` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '发票ID',
  `invoice_code` varchar(100) DEFAULT NULL COMMENT '发票代码',
  `invoice_no` varchar(100) NOT NULL COMMENT '发票号码',
  `invoice_type` varchar(50) DEFAULT NULL COMMENT '发票类型',
  `amount` decimal(18,2) DEFAULT '0.00' COMMENT '金额',
  `tax` decimal(18,2) DEFAULT '0.00' COMMENT '税额',
  `invoice_date` date DEFAULT NULL COMMENT '开票日期',
  `buyer` varchar(255) DEFAULT NULL COMMENT '购买方',
  `seller` varchar(255) DEFAULT NULL COMMENT '销售方',
  `status` tinyint(1) DEFAULT '0' COMMENT '发票状态（0待识别 1已识别 2已验真 3已报销）',
  `verify_result` varchar(500) DEFAULT NULL COMMENT '验真结果',
  `image_url` varchar(500) DEFAULT NULL COMMENT '发票图片URL',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户ID',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_invoice_no` (`invoice_no`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='发票表';

-- ----------------------------
-- 表结构: oa_expense_report
-- ----------------------------
DROP TABLE IF EXISTS `oa_expense_report`;
CREATE TABLE `oa_expense_report` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '报销单ID',
  `user_id` bigint DEFAULT NULL COMMENT '报销人用户ID',
  `user_name` varchar(100) DEFAULT NULL COMMENT '报销人姓名',
  `dept_id` bigint DEFAULT NULL COMMENT '部门ID',
  `total_amount` decimal(18,2) DEFAULT '0.00' COMMENT '合计金额',
  `category_id` bigint DEFAULT NULL COMMENT '费用类型ID',
  `process_instance_id` varchar(100) DEFAULT NULL COMMENT '流程实例ID',
  `status` tinyint(1) DEFAULT '0' COMMENT '报销状态（0草稿 1审批中 2财务审核中 3待付款 4已完成 9已拒绝）',
  `loan_id` bigint DEFAULT NULL COMMENT '关联借款单ID',
  `offset_amount` decimal(18,2) DEFAULT '0.00' COMMENT '冲销金额',
  `reason` varchar(500) DEFAULT NULL COMMENT '报销事由',
  `submit_time` datetime DEFAULT NULL COMMENT '提交时间',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户ID',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`),
  KEY `idx_category_id` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='报销单表';

-- ----------------------------
-- 表结构: oa_expense_item
-- ----------------------------
DROP TABLE IF EXISTS `oa_expense_item`;
CREATE TABLE `oa_expense_item` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '明细ID',
  `report_id` bigint NOT NULL COMMENT '报销单ID',
  `category_id` bigint DEFAULT NULL COMMENT '费用类型ID',
  `amount` decimal(18,2) DEFAULT '0.00' COMMENT '金额',
  `invoice_id` bigint DEFAULT NULL COMMENT '发票ID',
  `description` varchar(500) DEFAULT NULL COMMENT '费用说明',
  `expense_date` date DEFAULT NULL COMMENT '费用日期',
  PRIMARY KEY (`id`),
  KEY `idx_report_id` (`report_id`),
  KEY `idx_category_id` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='报销明细表';

-- ----------------------------
-- 表结构: oa_expense_budget
-- ----------------------------
DROP TABLE IF EXISTS `oa_expense_budget`;
CREATE TABLE `oa_expense_budget` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '预算ID',
  `dept_id` bigint DEFAULT NULL COMMENT '部门ID',
  `project_id` bigint DEFAULT NULL COMMENT '项目ID',
  `item_id` bigint DEFAULT NULL COMMENT '预算科目ID',
  `year` int DEFAULT NULL COMMENT '年度',
  `total_amount` decimal(18,2) DEFAULT '0.00' COMMENT '预算总额',
  `used_amount` decimal(18,2) DEFAULT '0.00' COMMENT '已用金额',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态（0停用 1启用）',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户ID',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_dept_id` (`dept_id`),
  KEY `idx_year` (`year`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='预算表';

-- ----------------------------
-- 表结构: oa_expense_loan
-- ----------------------------
DROP TABLE IF EXISTS `oa_expense_loan`;
CREATE TABLE `oa_expense_loan` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '借款单ID',
  `user_id` bigint DEFAULT NULL COMMENT '借款人用户ID',
  `user_name` varchar(100) DEFAULT NULL COMMENT '借款人姓名',
  `amount` decimal(18,2) DEFAULT '0.00' COMMENT '借款金额',
  `repaid_amount` decimal(18,2) DEFAULT '0.00' COMMENT '已还金额',
  `reason` varchar(500) DEFAULT NULL COMMENT '借款事由',
  `process_instance_id` varchar(100) DEFAULT NULL COMMENT '流程实例ID',
  `status` tinyint(1) DEFAULT '0' COMMENT '借款状态（0草稿 1审批中 2已通过 3已拒绝 9已还清）',
  `due_date` date DEFAULT NULL COMMENT '预计还款日期',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户ID',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='借款单表';

-- ----------------------------
-- 表结构: oa_expense_repayment
-- ----------------------------
DROP TABLE IF EXISTS `oa_expense_repayment`;
CREATE TABLE `oa_expense_repayment` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '还款ID',
  `loan_id` bigint NOT NULL COMMENT '借款单ID',
  `amount` decimal(18,2) DEFAULT '0.00' COMMENT '还款金额',
  `report_id` bigint DEFAULT NULL COMMENT '报销单ID',
  `repayment_time` datetime DEFAULT NULL COMMENT '还款时间',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户ID',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_loan_id` (`loan_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='还款记录表';

-- ----------------------------
-- 菜单数据
-- ----------------------------
-- 父目录: OA协同办公
INSERT IGNORE INTO `sys_menu` VALUES (3000, 'OA协同办公', 0, 10, 'oa', NULL, '', '', 1, 0, 'M', '0', '0', '', 'tree', 'admin', NOW(), '', NULL, 'OA协同办公目录');

-- 费用报销菜单
INSERT IGNORE INTO `sys_menu` VALUES (3120, '费用报销', 3000, 7, 'expense', 'oa/expense/index', '', '', 1, 0, 'C', '0', '0', 'oa:expenseReport:list', 'money', 'admin', NOW(), '', NULL, '费用报销菜单');

-- 费用类型菜单
INSERT IGNORE INTO `sys_menu` VALUES (3121, '费用类型', 3000, 8, 'expenseCategory', 'oa/expense/category', '', '', 1, 0, 'C', '0', '0', 'oa:expenseCategory:list', 'tree', 'admin', NOW(), '', NULL, '费用类型菜单');

-- 费用标准菜单
INSERT IGNORE INTO `sys_menu` VALUES (3122, '费用标准', 3000, 9, 'expenseStandard', 'oa/expense/standard', '', '', 1, 0, 'C', '0', '0', 'oa:expenseStandard:list', 'chart', 'admin', NOW(), '', NULL, '费用标准菜单');

-- 发票管理菜单
INSERT IGNORE INTO `sys_menu` VALUES (3123, '发票管理', 3000, 10, 'expenseInvoice', 'oa/expense/invoice', '', '', 1, 0, 'C', '0', '0', 'oa:expenseInvoice:list', 'edit', 'admin', NOW(), '', NULL, '发票管理菜单');

-- 预算管理菜单
INSERT IGNORE INTO `sys_menu` VALUES (3124, '预算管理', 3000, 11, 'expenseBudget', 'oa/expense/budget', '', '', 1, 0, 'C', '0', '0', 'oa:expenseBudget:list', 'dashboard', 'admin', NOW(), '', NULL, '预算管理菜单');

-- 借款还款菜单
INSERT IGNORE INTO `sys_menu` VALUES (3125, '借款还款', 3000, 12, 'expenseLoan', 'oa/expense/loan', '', '', 1, 0, 'C', '0', '0', 'oa:expenseLoan:list', 'borrow', 'admin', NOW(), '', NULL, '借款还款菜单');

-- 报销单按钮权限
INSERT IGNORE INTO `sys_menu` VALUES (3126, '报销单查询', 3120, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'oa:expenseReport:query', '#', 'admin', NOW(), '', NULL, '');
INSERT IGNORE INTO `sys_menu` VALUES (3127, '报销单新增', 3120, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'oa:expenseReport:add', '#', 'admin', NOW(), '', NULL, '');
INSERT IGNORE INTO `sys_menu` VALUES (3128, '报销单修改', 3120, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'oa:expenseReport:edit', '#', 'admin', NOW(), '', NULL, '');
INSERT IGNORE INTO `sys_menu` VALUES (3129, '报销单删除', 3120, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'oa:expenseReport:remove', '#', 'admin', NOW(), '', NULL, '');

-- 费用类型按钮权限
INSERT IGNORE INTO `sys_menu` VALUES (3130, '费用类型查询', 3121, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'oa:expenseCategory:query', '#', 'admin', NOW(), '', NULL, '');
INSERT IGNORE INTO `sys_menu` VALUES (3131, '费用类型新增', 3121, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'oa:expenseCategory:add', '#', 'admin', NOW(), '', NULL, '');
INSERT IGNORE INTO `sys_menu` VALUES (3132, '费用类型修改', 3121, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'oa:expenseCategory:edit', '#', 'admin', NOW(), '', NULL, '');
INSERT IGNORE INTO `sys_menu` VALUES (3133, '费用类型删除', 3121, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'oa:expenseCategory:remove', '#', 'admin', NOW(), '', NULL, '');

-- 费用标准按钮权限
INSERT IGNORE INTO `sys_menu` VALUES (3134, '费用标准查询', 3122, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'oa:expenseStandard:query', '#', 'admin', NOW(), '', NULL, '');
INSERT IGNORE INTO `sys_menu` VALUES (3135, '费用标准新增', 3122, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'oa:expenseStandard:add', '#', 'admin', NOW(), '', NULL, '');
INSERT IGNORE INTO `sys_menu` VALUES (3136, '费用标准修改', 3122, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'oa:expenseStandard:edit', '#', 'admin', NOW(), '', NULL, '');
INSERT IGNORE INTO `sys_menu` VALUES (3137, '费用标准删除', 3122, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'oa:expenseStandard:remove', '#', 'admin', NOW(), '', NULL, '');

-- 发票管理按钮权限
INSERT IGNORE INTO `sys_menu` VALUES (3138, '发票查询', 3123, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'oa:expenseInvoice:query', '#', 'admin', NOW(), '', NULL, '');
INSERT IGNORE INTO `sys_menu` VALUES (3139, '发票新增', 3123, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'oa:expenseInvoice:add', '#', 'admin', NOW(), '', NULL, '');
INSERT IGNORE INTO `sys_menu` VALUES (3140, '发票修改', 3123, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'oa:expenseInvoice:edit', '#', 'admin', NOW(), '', NULL, '');
INSERT IGNORE INTO `sys_menu` VALUES (3141, '发票删除', 3123, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'oa:expenseInvoice:remove', '#', 'admin', NOW(), '', NULL, '');

-- 预算管理按钮权限
INSERT IGNORE INTO `sys_menu` VALUES (3142, '预算查询', 3124, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'oa:expenseBudget:query', '#', 'admin', NOW(), '', NULL, '');
INSERT IGNORE INTO `sys_menu` VALUES (3143, '预算新增', 3124, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'oa:expenseBudget:add', '#', 'admin', NOW(), '', NULL, '');
INSERT IGNORE INTO `sys_menu` VALUES (3144, '预算修改', 3124, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'oa:expenseBudget:edit', '#', 'admin', NOW(), '', NULL, '');
INSERT IGNORE INTO `sys_menu` VALUES (3145, '预算删除', 3124, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'oa:expenseBudget:remove', '#', 'admin', NOW(), '', NULL, '');

-- 借款还款按钮权限
INSERT IGNORE INTO `sys_menu` VALUES (3146, '借款查询', 3125, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'oa:expenseLoan:query', '#', 'admin', NOW(), '', NULL, '');
INSERT IGNORE INTO `sys_menu` VALUES (3147, '借款新增', 3125, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'oa:expenseLoan:add', '#', 'admin', NOW(), '', NULL, '');
INSERT IGNORE INTO `sys_menu` VALUES (3148, '借款修改', 3125, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'oa:expenseLoan:edit', '#', 'admin', NOW(), '', NULL, '');
INSERT IGNORE INTO `sys_menu` VALUES (3149, '借款删除', 3125, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'oa:expenseLoan:remove', '#', 'admin', NOW(), '', NULL, '');

-- 费用审批按钮权限
INSERT IGNORE INTO `sys_menu` VALUES (3180, '报销审批', 3120, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'oa:expenseReport:approve', '#', 'admin', NOW(), '', NULL, '');
INSERT IGNORE INTO `sys_menu` VALUES (3181, '借款审批', 3125, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'oa:expenseLoan:approve', '#', 'admin', NOW(), '', NULL, '');

-- 为管理员角色分配费用报销菜单权限
INSERT IGNORE INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES
(1, 3000),
(1, 3120),
(1, 3121),
(1, 3122),
(1, 3123),
(1, 3124),
(1, 3125),
(1, 3126),
(1, 3127),
(1, 3128),
(1, 3129),
(1, 3130),
(1, 3131),
(1, 3132),
(1, 3133),
(1, 3134),
(1, 3135),
(1, 3136),
(1, 3137),
(1, 3138),
(1, 3139),
(1, 3140),
(1, 3141),
(1, 3142),
(1, 3143),
(1, 3144),
(1, 3145),
(1, 3146),
(1, 3147),
(1, 3148),
(1, 3149),
(1, 3180),
(1, 3181);
