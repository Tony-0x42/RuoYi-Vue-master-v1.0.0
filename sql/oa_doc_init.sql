-- 文档管理模块初始化脚本
-- 字符集: utf8mb4

-- ----------------------------
-- 表结构: oa_doc_folder
-- ----------------------------
CREATE TABLE IF NOT EXISTS `oa_doc_folder` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '文件夹ID',
  `parent_id` bigint DEFAULT '0' COMMENT '父文件夹ID',
  `name` varchar(100) NOT NULL COMMENT '文件夹名称',
  `owner` bigint DEFAULT NULL COMMENT '所有者用户ID',
  `sort` int DEFAULT '0' COMMENT '显示排序',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户ID',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_doc_folder_parent_id` (`parent_id`),
  KEY `idx_doc_folder_owner` (`owner`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='文档文件夹表';

-- ----------------------------
-- 表结构: oa_doc_file
-- ----------------------------
CREATE TABLE IF NOT EXISTS `oa_doc_file` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '文件ID',
  `folder_id` bigint DEFAULT NULL COMMENT '文件夹ID',
  `name` varchar(255) NOT NULL COMMENT '文件名称',
  `current_version` int DEFAULT '1' COMMENT '当前版本号',
  `owner` bigint DEFAULT NULL COMMENT '所有者用户ID',
  `file_type` varchar(50) DEFAULT NULL COMMENT '文件类型',
  `size` bigint DEFAULT '0' COMMENT '文件大小',
  `storage_path` varchar(500) DEFAULT NULL COMMENT '存储路径',
  `status` tinyint(1) DEFAULT '0' COMMENT '状态（0正常 1已删除）',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户ID',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_doc_file_folder_id` (`folder_id`),
  KEY `idx_doc_file_owner` (`owner`),
  KEY `idx_doc_file_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='文档文件表';

-- ----------------------------
-- 表结构: oa_doc_version
-- ----------------------------
CREATE TABLE IF NOT EXISTS `oa_doc_version` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '版本ID',
  `file_id` bigint NOT NULL COMMENT '文件ID',
  `version_no` int NOT NULL COMMENT '版本号',
  `storage_path` varchar(500) DEFAULT NULL COMMENT '存储路径',
  `size` bigint DEFAULT '0' COMMENT '文件大小',
  `remark` varchar(500) DEFAULT NULL COMMENT '版本备注',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_doc_version_file_id` (`file_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='文档版本表';

-- ----------------------------
-- 表结构: oa_doc_permission
-- ----------------------------
CREATE TABLE IF NOT EXISTS `oa_doc_permission` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '权限ID',
  `object_type` varchar(20) NOT NULL COMMENT '对象类型（folder file）',
  `object_id` bigint NOT NULL COMMENT '对象ID',
  `grantee_type` varchar(20) NOT NULL COMMENT '授权对象类型（user dept role）',
  `grantee_id` bigint NOT NULL COMMENT '授权对象ID',
  `permission` tinyint(1) DEFAULT '1' COMMENT '权限级别（1查看 2编辑 3管理）',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_doc_permission_object` (`object_type`,`object_id`),
  KEY `idx_doc_permission_grantee` (`grantee_type`,`grantee_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='文档权限表';

-- ----------------------------
-- 表结构: oa_doc_recycle
-- ----------------------------
CREATE TABLE IF NOT EXISTS `oa_doc_recycle` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '回收ID',
  `object_type` varchar(20) NOT NULL COMMENT '对象类型（folder file）',
  `object_id` bigint NOT NULL COMMENT '对象ID',
  `name` varchar(255) DEFAULT NULL COMMENT '对象名称',
  `delete_by` varchar(64) DEFAULT '' COMMENT '删除人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_doc_recycle_object` (`object_type`,`object_id`),
  KEY `idx_doc_recycle_delete_time` (`delete_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='文档回收站表';

-- ----------------------------
-- 菜单数据
-- ----------------------------
INSERT IGNORE INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
(3000, 'OA协同办公', 0, 10, 'oa', NULL, '', '', 1, 0, 'M', '0', '0', '', 'tree', 'admin', NOW(), '', NULL, 'OA协同办公目录');

-- 文档管理菜单
INSERT IGNORE INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
(3060, '文档管理', 3000, 6, 'doc', 'oa/doc/index', '', 'OaDoc', 1, 0, 'C', '0', '0', 'oa:doc:list', 'tree', 'admin', NOW(), '', NULL, '文档管理菜单');

-- 文档管理按钮权限
INSERT IGNORE INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
(3061, '文档查询', 3060, 1, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'oa:doc:query', '#', 'admin', NOW(), '', NULL, ''),
(3062, '文档新增', 3060, 2, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'oa:doc:add', '#', 'admin', NOW(), '', NULL, ''),
(3063, '文档修改', 3060, 3, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'oa:doc:edit', '#', 'admin', NOW(), '', NULL, ''),
(3064, '文档删除', 3060, 4, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'oa:doc:remove', '#', 'admin', NOW(), '', NULL, '');

-- 为管理员角色分配文档管理菜单权限
INSERT IGNORE INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES
(1, 3000),
(1, 3060),
(1, 3061),
(1, 3062),
(1, 3063),
(1, 3064);
