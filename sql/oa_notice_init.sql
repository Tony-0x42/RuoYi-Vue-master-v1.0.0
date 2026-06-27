-- 公告通知模块初始化脚本
-- 字符集: utf8mb4

-- ----------------------------
-- 表结构: oa_notice_category
-- ----------------------------
DROP TABLE IF EXISTS `oa_notice_category`;
CREATE TABLE `oa_notice_category` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `parent_id` bigint DEFAULT '0' COMMENT '父分类ID',
  `name` varchar(100) NOT NULL COMMENT '分类名称',
  `code` varchar(100) NOT NULL COMMENT '分类编码',
  `sort` int DEFAULT '0' COMMENT '显示排序',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态（0停用 1启用）',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户ID',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='公告分类表';

-- ----------------------------
-- 表结构: oa_notice
-- ----------------------------
DROP TABLE IF EXISTS `oa_notice`;
CREATE TABLE `oa_notice` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '公告ID',
  `category_id` bigint DEFAULT NULL COMMENT '分类ID',
  `title` varchar(200) NOT NULL COMMENT '公告标题',
  `content` longtext COMMENT '公告内容',
  `scope_type` varchar(20) DEFAULT 'all' COMMENT '可见范围类型（all全部 dept部门 role角色 user用户）',
  `scope_ids` varchar(500) DEFAULT NULL COMMENT '可见范围ID集合',
  `need_confirm` tinyint(1) DEFAULT '0' COMMENT '是否需要阅读确认（0否 1是）',
  `top` tinyint(1) DEFAULT '0' COMMENT '是否置顶（0否 1是）',
  `status` tinyint(1) DEFAULT '0' COMMENT '公告状态（0草稿 1已发布 2已下架）',
  `expire_time` datetime DEFAULT NULL COMMENT '有效期至',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户ID',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_category_id` (`category_id`),
  KEY `idx_status` (`status`),
  KEY `idx_top` (`top`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='公告表';

-- ----------------------------
-- 表结构: oa_notice_attachment
-- ----------------------------
DROP TABLE IF EXISTS `oa_notice_attachment`;
CREATE TABLE `oa_notice_attachment` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '附件ID',
  `notice_id` bigint NOT NULL COMMENT '公告ID',
  `file_name` varchar(255) NOT NULL COMMENT '文件名称',
  `file_path` varchar(500) NOT NULL COMMENT '文件路径',
  `file_size` bigint DEFAULT '0' COMMENT '文件大小（字节）',
  PRIMARY KEY (`id`),
  KEY `idx_notice_id` (`notice_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='公告附件表';

-- ----------------------------
-- 表结构: oa_notice_read
-- ----------------------------
DROP TABLE IF EXISTS `oa_notice_read`;
CREATE TABLE `oa_notice_read` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `notice_id` bigint NOT NULL COMMENT '公告ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `read_time` datetime DEFAULT NULL COMMENT '阅读时间',
  `confirmed` tinyint(1) DEFAULT '0' COMMENT '是否确认（0否 1是）',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_notice_user` (`notice_id`,`user_id`) USING BTREE,
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='公告阅读记录表';

-- ----------------------------
-- 菜单数据
-- ----------------------------
-- 父目录: OA协同办公
INSERT IGNORE INTO `sys_menu` VALUES (3000, 'OA协同办公', 0, 10, 'oa', NULL, '', '', 1, 0, 'M', '0', '0', '', 'tree', 'admin', NOW(), '', NULL, 'OA协同办公目录');

-- 公告通知菜单
INSERT IGNORE INTO `sys_menu` VALUES (3010, '公告通知', 3000, 2, 'notice', 'oa/notice/index', '', '', 1, 0, 'C', '0', '0', 'oa:notice:list', 'message', 'admin', NOW(), '', NULL, '公告通知菜单');

-- 公告分类菜单
INSERT IGNORE INTO `sys_menu` VALUES (3011, '公告分类', 3000, 3, 'noticeCategory', 'oa/notice/category', '', '', 1, 0, 'C', '0', '0', 'oa:noticeCategory:list', 'tree', 'admin', NOW(), '', NULL, '公告分类菜单');

-- 公告按钮权限
INSERT IGNORE INTO `sys_menu` VALUES (3012, '公告查询', 3010, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'oa:notice:query', '#', 'admin', NOW(), '', NULL, '');
INSERT IGNORE INTO `sys_menu` VALUES (3013, '公告新增', 3010, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'oa:notice:add', '#', 'admin', NOW(), '', NULL, '');
INSERT IGNORE INTO `sys_menu` VALUES (3014, '公告修改', 3010, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'oa:notice:edit', '#', 'admin', NOW(), '', NULL, '');
INSERT IGNORE INTO `sys_menu` VALUES (3015, '公告删除', 3010, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'oa:notice:remove', '#', 'admin', NOW(), '', NULL, '');

-- 公告分类按钮权限
INSERT IGNORE INTO `sys_menu` VALUES (3016, '分类查询', 3011, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'oa:noticeCategory:query', '#', 'admin', NOW(), '', NULL, '');
INSERT IGNORE INTO `sys_menu` VALUES (3017, '分类新增', 3011, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'oa:noticeCategory:add', '#', 'admin', NOW(), '', NULL, '');
INSERT IGNORE INTO `sys_menu` VALUES (3018, '分类修改', 3011, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'oa:noticeCategory:edit', '#', 'admin', NOW(), '', NULL, '');
INSERT IGNORE INTO `sys_menu` VALUES (3019, '分类删除', 3011, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'oa:noticeCategory:remove', '#', 'admin', NOW(), '', NULL, '');

-- 为管理员角色分配公告通知菜单权限
INSERT IGNORE INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES
(1, 3000),
(1, 3010),
(1, 3011),
(1, 3012),
(1, 3013),
(1, 3014),
(1, 3015),
(1, 3016),
(1, 3017),
(1, 3018),
(1, 3019);
