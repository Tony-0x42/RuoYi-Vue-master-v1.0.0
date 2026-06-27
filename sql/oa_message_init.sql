-- 消息通知中心模块初始化脚本
-- 字符集: utf8mb4

-- ----------------------------
-- 表结构: oa_message
-- ----------------------------
CREATE TABLE IF NOT EXISTS `oa_message` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '消息ID',
  `type` varchar(50) DEFAULT 'system' COMMENT '消息类型（todo待办 result结果 notice公告 system系统）',
  `title` varchar(200) NOT NULL COMMENT '消息标题',
  `content` text COMMENT '消息内容',
  `sender` varchar(64) DEFAULT '' COMMENT '发送人',
  `priority` varchar(20) DEFAULT 'normal' COMMENT '优先级（low normal high）',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态（0停用 1正常）',
  `tenant_id` bigint DEFAULT '0' COMMENT '租户ID',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_type` (`type`),
  KEY `idx_sender` (`sender`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='消息主表';

-- ----------------------------
-- 表结构: oa_message_recipient
-- ----------------------------
CREATE TABLE IF NOT EXISTS `oa_message_recipient` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `message_id` bigint NOT NULL COMMENT '消息ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `channel` varchar(50) DEFAULT 'site' COMMENT '发送渠道（site站内信 email邮件 sms短信 im）',
  `status` tinyint(1) DEFAULT '0' COMMENT '阅读状态（0未读 1已读）',
  `read_time` datetime DEFAULT NULL COMMENT '阅读时间',
  `tenant_id` bigint DEFAULT '0' COMMENT '租户ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_message_id` (`message_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='消息接收人表';

-- ----------------------------
-- 表结构: oa_message_template
-- ----------------------------
CREATE TABLE IF NOT EXISTS `oa_message_template` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '模板ID',
  `code` varchar(100) NOT NULL COMMENT '模板编码',
  `name` varchar(200) NOT NULL COMMENT '模板名称',
  `type` varchar(50) DEFAULT 'system' COMMENT '模板类型',
  `channels_json` text COMMENT '适用渠道JSON',
  `content_json` text COMMENT '渠道内容JSON',
  `variables` varchar(500) DEFAULT NULL COMMENT '变量定义',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态（0停用 1启用）',
  `tenant_id` bigint DEFAULT '0' COMMENT '租户ID',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`code`) USING BTREE,
  KEY `idx_type` (`type`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='消息模板表';

-- ----------------------------
-- 表结构: oa_message_log
-- ----------------------------
CREATE TABLE IF NOT EXISTS `oa_message_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `message_id` bigint DEFAULT NULL COMMENT '消息ID',
  `channel` varchar(50) DEFAULT NULL COMMENT '发送渠道',
  `request` text COMMENT '请求内容',
  `response` text COMMENT '响应内容',
  `status` varchar(20) DEFAULT NULL COMMENT '发送状态（pending success fail retry）',
  `retry_count` int DEFAULT '0' COMMENT '重试次数',
  `tenant_id` bigint DEFAULT '0' COMMENT '租户ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_message_id` (`message_id`),
  KEY `idx_channel` (`channel`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='消息发送日志表';

-- ----------------------------
-- 表结构: oa_message_subscription
-- ----------------------------
CREATE TABLE IF NOT EXISTS `oa_message_subscription` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '订阅ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `item_code` varchar(100) NOT NULL COMMENT '订阅项编码',
  `channels` varchar(255) DEFAULT NULL COMMENT '接收渠道',
  `enabled` tinyint(1) DEFAULT '1' COMMENT '是否启用（0否 1是）',
  `tenant_id` bigint DEFAULT '0' COMMENT '租户ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_item_code` (`item_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='消息订阅配置表';

-- ----------------------------
-- 菜单数据
-- ----------------------------
-- 父目录 OA协同办公 已存在，请勿重复插入

-- 消息通知中心菜单
INSERT IGNORE INTO `sys_menu` VALUES (3080, '消息通知中心', 3000, 4, 'message', 'oa/message/index', '', '', 1, 0, 'C', '0', '0', 'oa:message:list', 'message', 'admin', NOW(), '', NULL, '消息通知中心菜单');

-- 消息模板菜单
INSERT IGNORE INTO `sys_menu` VALUES (3081, '消息模板', 3000, 5, 'messageTemplate', 'oa/message/template', '', '', 1, 0, 'C', '0', '0', 'oa:messageTemplate:list', 'tree', 'admin', NOW(), '', NULL, '消息模板菜单');

-- 消息按钮权限
INSERT IGNORE INTO `sys_menu` VALUES (3082, '消息查询', 3080, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'oa:message:query', '#', 'admin', NOW(), '', NULL, '');
INSERT IGNORE INTO `sys_menu` VALUES (3083, '消息发送', 3080, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'oa:message:send', '#', 'admin', NOW(), '', NULL, '');
INSERT IGNORE INTO `sys_menu` VALUES (3084, '消息删除', 3080, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'oa:message:remove', '#', 'admin', NOW(), '', NULL, '');

-- 消息模板按钮权限
INSERT IGNORE INTO `sys_menu` VALUES (3085, '模板查询', 3081, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'oa:messageTemplate:query', '#', 'admin', NOW(), '', NULL, '');
INSERT IGNORE INTO `sys_menu` VALUES (3086, '模板新增', 3081, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'oa:messageTemplate:add', '#', 'admin', NOW(), '', NULL, '');
INSERT IGNORE INTO `sys_menu` VALUES (3087, '模板修改', 3081, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'oa:messageTemplate:edit', '#', 'admin', NOW(), '', NULL, '');
INSERT IGNORE INTO `sys_menu` VALUES (3088, '模板删除', 3081, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'oa:messageTemplate:remove', '#', 'admin', NOW(), '', NULL, '');

-- 为管理员角色分配消息通知中心菜单权限
INSERT IGNORE INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES
(1, 3000),
(1, 3080),
(1, 3081),
(1, 3082),
(1, 3083),
(1, 3084),
(1, 3085),
(1, 3086),
(1, 3087),
(1, 3088);
