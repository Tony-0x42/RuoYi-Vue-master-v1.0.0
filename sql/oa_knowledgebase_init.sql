-- 知识库模块初始化脚本
-- 字符集: utf8mb4

-- ----------------------------
-- 表结构: oa_kb_category
-- ----------------------------
CREATE TABLE IF NOT EXISTS `oa_kb_category` (
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
  UNIQUE KEY `uk_kb_category_code` (`code`) USING BTREE,
  KEY `idx_kb_category_parent_id` (`parent_id`),
  KEY `idx_kb_category_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='知识分类表';

-- ----------------------------
-- 表结构: oa_kb_article
-- ----------------------------
CREATE TABLE IF NOT EXISTS `oa_kb_article` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '词条ID',
  `category_id` bigint DEFAULT NULL COMMENT '分类ID',
  `title` varchar(200) NOT NULL COMMENT '词条标题',
  `summary` varchar(500) DEFAULT NULL COMMENT '摘要',
  `content` longtext COMMENT '正文内容',
  `tags` varchar(500) DEFAULT NULL COMMENT '标签',
  `author` varchar(64) DEFAULT '' COMMENT '作者',
  `status` tinyint(1) DEFAULT '0' COMMENT '状态（0草稿 1已发布 2已下架）',
  `version` int DEFAULT '1' COMMENT '版本号',
  `view_count` bigint DEFAULT '0' COMMENT '阅读量',
  `like_count` bigint DEFAULT '0' COMMENT '点赞数',
  `favorite_count` bigint DEFAULT '0' COMMENT '收藏数',
  `comment_count` bigint DEFAULT '0' COMMENT '评论数',
  `top` tinyint(1) DEFAULT '0' COMMENT '是否置顶（0否 1是）',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户ID',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_kb_article_category_id` (`category_id`),
  KEY `idx_kb_article_status` (`status`),
  KEY `idx_kb_article_top` (`top`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='知识词条表';

-- ----------------------------
-- 表结构: oa_kb_favorite
-- ----------------------------
CREATE TABLE IF NOT EXISTS `oa_kb_favorite` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '收藏ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `article_id` bigint NOT NULL COMMENT '词条ID',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户ID',
  `create_time` datetime DEFAULT NULL COMMENT '收藏时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_kb_favorite_user_article` (`user_id`,`article_id`) USING BTREE,
  KEY `idx_kb_favorite_article_id` (`article_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='知识收藏表';

-- ----------------------------
-- 表结构: oa_kb_comment
-- ----------------------------
CREATE TABLE IF NOT EXISTS `oa_kb_comment` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '评论ID',
  `article_id` bigint NOT NULL COMMENT '词条ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `content` text COMMENT '评论内容',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态（0待审核 1显示 2拒绝）',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户ID',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_kb_comment_article_id` (`article_id`),
  KEY `idx_kb_comment_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='知识评论表';

-- ----------------------------
-- 表结构: oa_kb_read_log
-- ----------------------------
CREATE TABLE IF NOT EXISTS `oa_kb_read_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `article_id` bigint NOT NULL COMMENT '词条ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `read_time` datetime DEFAULT NULL COMMENT '阅读时间',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`),
  KEY `idx_kb_read_log_article_id` (`article_id`),
  KEY `idx_kb_read_log_user_id` (`user_id`),
  KEY `idx_kb_read_log_time` (`read_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='知识阅读记录表';

-- ----------------------------
-- 菜单数据
-- ----------------------------
INSERT IGNORE INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
(3000, 'OA协同办公', 0, 10, 'oa', NULL, '', '', 1, 0, 'M', '0', '0', '', 'tree', 'admin', NOW(), '', NULL, 'OA协同办公目录');

-- 知识库菜单
INSERT IGNORE INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
(3050, '知识库', 3000, 4, 'knowledgebase', 'oa/knowledgebase/index', '', 'OaKnowledgebase', 1, 0, 'C', '0', '0', 'oa:knowledgebase:list', 'edit', 'admin', NOW(), '', NULL, '知识库菜单');

-- 知识分类菜单
INSERT IGNORE INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
(3051, '知识分类', 3000, 5, 'knowledgebaseCategory', 'oa/knowledgebase/category', '', 'OaKnowledgebaseCategory', 1, 0, 'C', '0', '0', 'oa:knowledgebase:category:list', 'tree', 'admin', NOW(), '', NULL, '知识分类菜单');

-- 知识库按钮权限
INSERT IGNORE INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
(3052, '知识查询', 3050, 1, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'oa:knowledgebase:query', '#', 'admin', NOW(), '', NULL, ''),
(3053, '知识新增', 3050, 2, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'oa:knowledgebase:add', '#', 'admin', NOW(), '', NULL, ''),
(3054, '知识修改', 3050, 3, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'oa:knowledgebase:edit', '#', 'admin', NOW(), '', NULL, ''),
(3055, '知识删除', 3050, 4, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'oa:knowledgebase:remove', '#', 'admin', NOW(), '', NULL, '');

-- 知识分类按钮权限
INSERT IGNORE INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
(3056, '分类查询', 3051, 1, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'oa:knowledgebaseCategory:query', '#', 'admin', NOW(), '', NULL, ''),
(3057, '分类新增/修改/删除', 3051, 2, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'oa:knowledgebaseCategory:add,oa:knowledgebaseCategory:edit,oa:knowledgebaseCategory:remove', '#', 'admin', NOW(), '', NULL, '');

-- 为管理员角色分配知识库菜单权限
INSERT IGNORE INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES
(1, 3000),
(1, 3050),
(1, 3051),
(1, 3052),
(1, 3053),
(1, 3054),
(1, 3055),
(1, 3056),
(1, 3057);
