-- 门户工作台模块初始化脚本
-- 字符集: utf8mb4

SET NAMES utf8mb4;

-- ----------------------------
-- 表结构: oa_portal_layout
-- ----------------------------
CREATE TABLE IF NOT EXISTS `oa_portal_layout` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '布局ID',
  `user_id` bigint DEFAULT NULL COMMENT '用户ID',
  `role_id` bigint DEFAULT NULL COMMENT '角色ID',
  `layout_json` longtext COMMENT '布局JSON',
  `is_default` tinyint(1) DEFAULT '0' COMMENT '是否默认（0否 1是）',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户ID',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_role_id` (`role_id`),
  KEY `idx_is_default` (`is_default`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='门户布局表';

-- ----------------------------
-- 表结构: oa_portal_widget
-- ----------------------------
CREATE TABLE IF NOT EXISTS `oa_portal_widget` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '组件ID',
  `code` varchar(100) NOT NULL COMMENT '组件编码',
  `name` varchar(100) NOT NULL COMMENT '组件名称',
  `type` varchar(50) DEFAULT NULL COMMENT '组件类型',
  `config_schema` longtext COMMENT '配置Schema',
  `sort` int DEFAULT '0' COMMENT '显示排序',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态（0停用 1启用）',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户ID',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_widget_code` (`code`) USING BTREE,
  KEY `idx_status` (`status`),
  KEY `idx_sort` (`sort`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='门户组件表';

-- ----------------------------
-- 表结构: oa_favorite_app
-- ----------------------------
CREATE TABLE IF NOT EXISTS `oa_favorite_app` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '收藏ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `app_code` varchar(100) NOT NULL COMMENT '应用编码',
  `sort` int DEFAULT '0' COMMENT '显示排序',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户ID',
  `create_time` datetime DEFAULT NULL COMMENT '收藏时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_favorite_user_app` (`user_id`,`app_code`) USING BTREE,
  KEY `idx_app_code` (`app_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户收藏应用表';

-- ----------------------------
-- 菜单数据
-- ----------------------------
INSERT IGNORE INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
(3000, 'OA协同办公', 0, 10, 'oa', NULL, '', '', 1, 0, 'M', '0', '0', '', 'tree', 'admin', NOW(), '', NULL, 'OA协同办公目录');

-- 门户工作台菜单
INSERT IGNORE INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
(3170, '门户工作台', 3000, 16, 'portal', 'oa/portal/index', '', 'OaPortal', 1, 0, 'C', '0', '0', 'oa:portal:list', 'dashboard', 'admin', NOW(), '', NULL, '门户工作台菜单');

-- 门户工作台按钮权限
INSERT IGNORE INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
(3171, '工作台查询', 3170, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'oa:portal:query', '#', 'admin', NOW(), '', NULL, ''),
(3172, '工作台新增', 3170, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'oa:portal:add', '#', 'admin', NOW(), '', NULL, ''),
(3173, '工作台修改', 3170, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'oa:portal:edit', '#', 'admin', NOW(), '', NULL, ''),
(3174, '工作台删除', 3170, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'oa:portal:remove', '#', 'admin', NOW(), '', NULL, ''),
(3175, '布局配置', 3170, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'oa:portal:layout', '#', 'admin', NOW(), '', NULL, '');

-- 默认门户组件
INSERT IGNORE INTO `oa_portal_widget` (`code`, `name`, `type`, `config_schema`, `sort`, `status`, `create_by`, `create_time`, `remark`) VALUES
('todo', '待办聚合', 'list', '{"span":16}', 1, 1, 'admin', NOW(), '待办聚合组件'),
('message', '消息提醒', 'list', '{"span":8}', 2, 1, 'admin', NOW(), '消息提醒组件'),
('schedule', '日程预览', 'list', '{"span":8}', 3, 1, 'admin', NOW(), '日程预览组件'),
('apps', '应用中心', 'grid', '{"span":8}', 4, 1, 'admin', NOW(), '应用中心组件'),
('dashboard', '数据看板', 'stats', '{"span":8}', 5, 1, 'admin', NOW(), '数据看板组件');

-- 为管理员角色分配门户工作台菜单权限
INSERT IGNORE INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES
(1, 3000),
(1, 3170),
(1, 3171),
(1, 3172),
(1, 3173),
(1, 3174),
(1, 3175);
