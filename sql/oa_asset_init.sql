-- 资产管理模块初始化脚本
-- 字符集: utf8mb4

-- ----------------------------
-- 表结构: oa_asset_category
-- ----------------------------
CREATE TABLE IF NOT EXISTS `oa_asset_category` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `parent_id` bigint DEFAULT '0' COMMENT '父分类ID',
  `name` varchar(100) NOT NULL COMMENT '分类名称',
  `code` varchar(100) NOT NULL COMMENT '分类编码',
  `depreciation_years` int DEFAULT '0' COMMENT '折旧年限',
  `sort` int DEFAULT '0' COMMENT '显示排序',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态（0停用 1启用）',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户ID',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_asset_category_code` (`code`) USING BTREE,
  KEY `idx_asset_category_parent_id` (`parent_id`),
  KEY `idx_asset_category_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='资产分类表';

-- ----------------------------
-- 表结构: oa_asset
-- ----------------------------
CREATE TABLE IF NOT EXISTS `oa_asset` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '资产ID',
  `code` varchar(100) NOT NULL COMMENT '资产编码',
  `name` varchar(200) NOT NULL COMMENT '资产名称',
  `category_id` bigint DEFAULT NULL COMMENT '分类ID',
  `model` varchar(200) DEFAULT NULL COMMENT '规格型号',
  `spec` varchar(500) DEFAULT NULL COMMENT '规格配置',
  `purchase_date` date DEFAULT NULL COMMENT '购置日期',
  `value` decimal(18,2) DEFAULT '0.00' COMMENT '资产价值',
  `user_id` bigint DEFAULT NULL COMMENT '使用人ID',
  `user_name` varchar(100) DEFAULT NULL COMMENT '使用人名称',
  `dept_id` bigint DEFAULT NULL COMMENT '部门ID',
  `dept_name` varchar(100) DEFAULT NULL COMMENT '部门名称',
  `location` varchar(200) DEFAULT NULL COMMENT '存放地点',
  `status` tinyint(1) DEFAULT '0' COMMENT '状态（0闲置 1在用 2维修中 3已报废）',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户ID',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_asset_code` (`code`) USING BTREE,
  KEY `idx_asset_category_id` (`category_id`),
  KEY `idx_asset_status` (`status`),
  KEY `idx_asset_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='资产台账表';

-- ----------------------------
-- 表结构: oa_asset_receive
-- ----------------------------
CREATE TABLE IF NOT EXISTS `oa_asset_receive` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '领用ID',
  `asset_id` bigint NOT NULL COMMENT '资产ID',
  `user_id` bigint NOT NULL COMMENT '领用人ID',
  `user_name` varchar(100) DEFAULT NULL COMMENT '领用人名称',
  `process_instance_id` varchar(100) DEFAULT NULL COMMENT '流程实例ID',
  `receive_time` datetime DEFAULT NULL COMMENT '领用时间',
  `return_time` datetime DEFAULT NULL COMMENT '归还时间',
  `status` tinyint(1) DEFAULT '0' COMMENT '状态（0待审批 1已领用 2已归还）',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户ID',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_receive_asset_id` (`asset_id`),
  KEY `idx_receive_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='资产领用记录表';

-- ----------------------------
-- 表结构: oa_asset_return
-- ----------------------------
CREATE TABLE IF NOT EXISTS `oa_asset_return` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '归还ID',
  `asset_id` bigint NOT NULL COMMENT '资产ID',
  `user_id` bigint NOT NULL COMMENT '归还人ID',
  `user_name` varchar(100) DEFAULT NULL COMMENT '归还人名称',
  `return_time` datetime DEFAULT NULL COMMENT '归还时间',
  `status` tinyint(1) DEFAULT '0' COMMENT '状态（0正常 1损坏）',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户ID',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_return_asset_id` (`asset_id`),
  KEY `idx_return_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='资产归还记录表';

-- ----------------------------
-- 表结构: oa_asset_transfer
-- ----------------------------
CREATE TABLE IF NOT EXISTS `oa_asset_transfer` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '调拨ID',
  `asset_id` bigint NOT NULL COMMENT '资产ID',
  `from_user_id` bigint DEFAULT NULL COMMENT '原使用人ID',
  `from_user_name` varchar(100) DEFAULT NULL COMMENT '原使用人名称',
  `to_user_id` bigint DEFAULT NULL COMMENT '目标使用人ID',
  `to_user_name` varchar(100) DEFAULT NULL COMMENT '目标使用人名称',
  `process_instance_id` varchar(100) DEFAULT NULL COMMENT '流程实例ID',
  `transfer_time` datetime DEFAULT NULL COMMENT '调拨时间',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户ID',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_transfer_asset_id` (`asset_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='资产调拨记录表';

-- ----------------------------
-- 表结构: oa_asset_repair
-- ----------------------------
CREATE TABLE IF NOT EXISTS `oa_asset_repair` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '维修ID',
  `asset_id` bigint NOT NULL COMMENT '资产ID',
  `reason` varchar(500) DEFAULT NULL COMMENT '维修原因',
  `cost` decimal(18,2) DEFAULT '0.00' COMMENT '维修费用',
  `vendor` varchar(200) DEFAULT NULL COMMENT '维修商',
  `repair_time` datetime DEFAULT NULL COMMENT '维修时间',
  `status` tinyint(1) DEFAULT '0' COMMENT '状态（0维修中 1已完成）',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户ID',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_repair_asset_id` (`asset_id`),
  KEY `idx_repair_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='资产维修记录表';

-- ----------------------------
-- 表结构: oa_asset_inventory
-- ----------------------------
CREATE TABLE IF NOT EXISTS `oa_asset_inventory` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '盘点ID',
  `name` varchar(200) NOT NULL COMMENT '盘点任务名称',
  `scope_type` tinyint(1) DEFAULT '0' COMMENT '盘点范围类型（0全部 1分类 2存放地点）',
  `scope_ids` varchar(500) DEFAULT NULL COMMENT '盘点范围IDs',
  `status` tinyint(1) DEFAULT '0' COMMENT '状态（0待盘点 1盘点中 2已完成）',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户ID',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_inventory_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='资产盘点任务表';

-- ----------------------------
-- 表结构: oa_asset_inventory_item
-- ----------------------------
CREATE TABLE IF NOT EXISTS `oa_asset_inventory_item` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '明细ID',
  `inventory_id` bigint NOT NULL COMMENT '盘点任务ID',
  `asset_id` bigint NOT NULL COMMENT '资产ID',
  `asset_code` varchar(100) DEFAULT NULL COMMENT '资产编码',
  `asset_name` varchar(200) DEFAULT NULL COMMENT '资产名称',
  `book_status` tinyint(1) DEFAULT NULL COMMENT '账面状态',
  `actual_status` tinyint(1) DEFAULT NULL COMMENT '实际状态',
  `result` tinyint(1) DEFAULT '0' COMMENT '盘点结果（0正常 1盘盈 2盘亏）',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户ID',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_item_inventory_id` (`inventory_id`),
  KEY `idx_item_asset_id` (`asset_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='资产盘点明细表';

-- ----------------------------
-- 表结构: oa_asset_scrap
-- ----------------------------
CREATE TABLE IF NOT EXISTS `oa_asset_scrap` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '报废ID',
  `asset_id` bigint NOT NULL COMMENT '资产ID',
  `reason` varchar(500) DEFAULT NULL COMMENT '报废原因',
  `disposal_method` varchar(200) DEFAULT NULL COMMENT '处置方式',
  `process_instance_id` varchar(100) DEFAULT NULL COMMENT '流程实例ID',
  `scrap_time` datetime DEFAULT NULL COMMENT '报废时间',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户ID',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_scrap_asset_id` (`asset_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='资产报废记录表';

-- ----------------------------
-- 菜单数据
-- ----------------------------
INSERT IGNORE INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
(3000, 'OA协同办公', 0, 10, 'oa', NULL, '', '', 1, 0, 'M', '0', '0', '', 'tree', 'admin', NOW(), '', NULL, 'OA协同办公目录');

-- 资产管理菜单
INSERT IGNORE INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
(3150, '资产管理', 3000, 13, 'asset', 'oa/asset/index', '', 'OaAsset', 1, 0, 'C', '0', '0', 'oa:asset:list', 'money', 'admin', NOW(), '', NULL, '资产管理菜单');

-- 资产分类菜单
INSERT IGNORE INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
(3151, '资产分类', 3000, 14, 'assetCategory', 'oa/asset/category', '', 'OaAssetCategory', 1, 0, 'C', '0', '0', 'oa:assetCategory:list', 'tree', 'admin', NOW(), '', NULL, '资产分类菜单');

-- 资产盘点菜单
INSERT IGNORE INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
(3152, '资产盘点', 3000, 15, 'assetInventory', 'oa/asset/inventory', '', 'OaAssetInventory', 1, 0, 'C', '0', '0', 'oa:assetInventory:list', 'list', 'admin', NOW(), '', NULL, '资产盘点菜单');

-- 资产台账按钮权限
INSERT IGNORE INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
(3153, '资产查询', 3150, 1, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'oa:asset:query', '#', 'admin', NOW(), '', NULL, ''),
(3154, '资产新增', 3150, 2, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'oa:asset:add', '#', 'admin', NOW(), '', NULL, ''),
(3155, '资产修改', 3150, 3, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'oa:asset:edit', '#', 'admin', NOW(), '', NULL, ''),
(3156, '资产删除', 3150, 4, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'oa:asset:remove', '#', 'admin', NOW(), '', NULL, ''),
(3157, '资产统计', 3150, 5, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'oa:asset:statistics', '#', 'admin', NOW(), '', NULL, '');

-- 资产分类按钮权限
INSERT IGNORE INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
(3158, '分类查询', 3151, 1, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'oa:assetCategory:query', '#', 'admin', NOW(), '', NULL, ''),
(3159, '分类新增/修改/删除', 3151, 2, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'oa:assetCategory:add,oa:assetCategory:edit,oa:assetCategory:remove', '#', 'admin', NOW(), '', NULL, '');

-- 资产盘点按钮权限
INSERT IGNORE INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
(3160, '盘点查询', 3152, 1, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'oa:assetInventory:query', '#', 'admin', NOW(), '', NULL, ''),
(3161, '盘点新增', 3152, 2, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'oa:assetInventory:add', '#', 'admin', NOW(), '', NULL, ''),
(3162, '盘点修改', 3152, 3, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'oa:assetInventory:edit', '#', 'admin', NOW(), '', NULL, ''),
(3163, '盘点删除', 3152, 4, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'oa:assetInventory:remove', '#', 'admin', NOW(), '', NULL, '');

-- 为管理员角色分配资产管理菜单权限
INSERT IGNORE INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES
(1, 3000),
(1, 3150),
(1, 3151),
(1, 3152),
(1, 3153),
(1, 3154),
(1, 3155),
(1, 3156),
(1, 3157),
(1, 3158),
(1, 3159),
(1, 3160),
(1, 3161),
(1, 3162),
(1, 3163);
