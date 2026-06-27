SET NAMES utf8mb4;

-- ----------------------------
-- 表结构: oa_meeting_room
-- ----------------------------
CREATE TABLE IF NOT EXISTS `oa_meeting_room` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '会议室ID',
  `code` varchar(100) NOT NULL COMMENT '会议室编码',
  `name` varchar(200) NOT NULL COMMENT '会议室名称',
  `location` varchar(200) DEFAULT NULL COMMENT '位置',
  `capacity` int NOT NULL COMMENT '容量',
  `devices` varchar(500) DEFAULT NULL COMMENT '设备',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态（0停用 1启用）',
  `pic_url` varchar(500) DEFAULT NULL COMMENT '图片地址',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户ID',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_room_code` (`code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='会议室表';

-- ----------------------------
-- 表结构: oa_meeting
-- ----------------------------
CREATE TABLE IF NOT EXISTS `oa_meeting` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '会议ID',
  `title` varchar(200) NOT NULL COMMENT '会议主题',
  `start_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime NOT NULL COMMENT '结束时间',
  `room_id` bigint NOT NULL COMMENT '会议室ID',
  `organizer` bigint DEFAULT NULL COMMENT '组织者用户ID',
  `content` text COMMENT '会议内容',
  `status` tinyint(1) DEFAULT '0' COMMENT '状态（0待开始 1进行中 2已结束 3已取消）',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户ID',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_room_time` (`room_id`,`start_time`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='会议表';

-- ----------------------------
-- 表结构: oa_meeting_attendee
-- ----------------------------
CREATE TABLE IF NOT EXISTS `oa_meeting_attendee` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `meeting_id` bigint NOT NULL COMMENT '会议ID',
  `user_id` bigint DEFAULT NULL COMMENT '用户ID',
  `email` varchar(100) DEFAULT NULL COMMENT '外部邮箱',
  `status` tinyint(1) DEFAULT '0' COMMENT '参与状态（0待确认 1接受 2拒绝）',
  `sign_in_time` datetime DEFAULT NULL COMMENT '签到时间',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_meeting_id` (`meeting_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='会议参与人表';

-- ----------------------------
-- 表结构: oa_meeting_minutes
-- ----------------------------
CREATE TABLE IF NOT EXISTS `oa_meeting_minutes` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '纪要ID',
  `meeting_id` bigint NOT NULL COMMENT '会议ID',
  `content` text COMMENT '纪要内容',
  `attachments` varchar(1000) DEFAULT NULL COMMENT '附件',
  `scope` varchar(20) DEFAULT 'all' COMMENT '可见范围（all全部 dept部门 user用户）',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态（0草稿 1已发布）',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户ID',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_minutes_meeting_id` (`meeting_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='会议纪要表';

-- ----------------------------
-- 菜单数据
-- ----------------------------
INSERT IGNORE INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES (3000, 'OA协同办公', 0, 10, 'oa', NULL, 1, 0, 'M', '0', '0', '', 'tree', 'admin', NOW(), '', NULL, 'OA协同办公目录');

-- 会议管理菜单
INSERT IGNORE INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES (3040, '会议管理', 3000, 5, 'meeting', 'oa/meeting/index', 1, 0, 'C', '0', '0', 'oa:meeting:list', 'meeting', 'admin', NOW(), '', NULL, '会议管理菜单');

-- 会议室管理菜单
INSERT IGNORE INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES (3041, '会议室管理', 3000, 6, 'meetingRoom', 'oa/meeting/room', 1, 0, 'C', '0', '0', 'oa:meeting:room:list', 'tree', 'admin', NOW(), '', NULL, '会议室管理菜单');

-- 会议按钮权限
INSERT IGNORE INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES (3042, '会议查询', 3040, 1, '', '', 1, 0, 'F', '0', '0', 'oa:meeting:query', '#', 'admin', NOW(), '', NULL, ''),
       (3043, '会议新增', 3040, 2, '', '', 1, 0, 'F', '0', '0', 'oa:meeting:add', '#', 'admin', NOW(), '', NULL, ''),
       (3044, '会议修改', 3040, 3, '', '', 1, 0, 'F', '0', '0', 'oa:meeting:edit', '#', 'admin', NOW(), '', NULL, ''),
       (3045, '会议删除', 3040, 4, '', '', 1, 0, 'F', '0', '0', 'oa:meeting:remove', '#', 'admin', NOW(), '', NULL, '');

-- 会议室按钮权限
INSERT IGNORE INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES (3046, '会议室查询', 3041, 1, '', '', 1, 0, 'F', '0', '0', 'oa:meetingRoom:query', '#', 'admin', NOW(), '', NULL, ''),
       (3047, '会议室新增', 3041, 2, '', '', 1, 0, 'F', '0', '0', 'oa:meetingRoom:add', '#', 'admin', NOW(), '', NULL, ''),
       (3048, '会议室修改', 3041, 3, '', '', 1, 0, 'F', '0', '0', 'oa:meetingRoom:edit', '#', 'admin', NOW(), '', NULL, ''),
       (3049, '会议室删除', 3041, 4, '', '', 1, 0, 'F', '0', '0', 'oa:meetingRoom:remove', '#', 'admin', NOW(), '', NULL, '');

-- 为管理员角色分配会议管理菜单权限
INSERT IGNORE INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES
(1, 3000),
(1, 3040),
(1, 3041),
(1, 3042),
(1, 3043),
(1, 3044),
(1, 3045),
(1, 3046),
(1, 3047),
(1, 3048),
(1, 3049);
