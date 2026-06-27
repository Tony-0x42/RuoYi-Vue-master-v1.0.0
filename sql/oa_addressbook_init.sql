-- 通讯录模块初始化脚本
-- 字符集：utf8mb4

-- 创建个人通讯录分组表
CREATE TABLE IF NOT EXISTS `oa_contact_group` (
  `group_id` bigint NOT NULL AUTO_INCREMENT COMMENT '分组ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `group_name` varchar(50) NOT NULL COMMENT '分组名称',
  `group_sort` int DEFAULT '0' COMMENT '显示顺序',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`group_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='个人通讯录分组表';

-- 创建个人通讯录表
CREATE TABLE IF NOT EXISTS `oa_contact` (
  `contact_id` bigint NOT NULL AUTO_INCREMENT COMMENT '联系人记录ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `contact_user_id` bigint NOT NULL COMMENT '联系人用户ID',
  `group_id` bigint DEFAULT NULL COMMENT '分组ID',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`contact_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_contact_user_id` (`contact_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='个人通讯录表';

-- 创建最近联系人表
CREATE TABLE IF NOT EXISTS `oa_recent_contact` (
  `recent_id` bigint NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `contact_user_id` bigint NOT NULL COMMENT '联系人用户ID',
  `contact_time` datetime DEFAULT NULL COMMENT '联系时间',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`recent_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_contact_time` (`contact_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='最近联系人表';

-- 插入菜单数据
INSERT IGNORE INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
(3000, 'OA协同办公', 0, 10, 'oa', NULL, '', '', 1, 0, 'M', '0', '0', '', 'tree', 'admin', NOW(), '', NULL, 'OA协同办公目录'),
(3001, '通讯录', 3000, 1, 'addressbook', 'oa/addressbook/index', '', 'OaAddressBook', 1, 0, 'C', '0', '0', 'oa:addressbook:list', 'user', 'admin', NOW(), '', NULL, '通讯录菜单'),
(3002, '通讯录查询', 3001, 1, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'oa:addressbook:query', '#', 'admin', NOW(), '', NULL, ''),
(3003, '通讯录新增', 3001, 2, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'oa:addressbook:add', '#', 'admin', NOW(), '', NULL, ''),
(3004, '通讯录修改', 3001, 3, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'oa:addressbook:edit', '#', 'admin', NOW(), '', NULL, ''),
(3005, '通讯录删除', 3001, 4, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'oa:addressbook:remove', '#', 'admin', NOW(), '', NULL, '');

-- 为管理员角色分配通讯录菜单权限
INSERT IGNORE INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES
(1, 3000),
(1, 3001),
(1, 3002),
(1, 3003),
(1, 3004),
(1, 3005);
