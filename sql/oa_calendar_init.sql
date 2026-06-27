SET NAMES utf8mb4;

-- 日程事件表
CREATE TABLE IF NOT EXISTS `oa_calendar_event` (
  `event_id` bigint NOT NULL AUTO_INCREMENT COMMENT '日程ID',
  `title` varchar(100) NOT NULL COMMENT '日程标题',
  `start_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime NOT NULL COMMENT '结束时间',
  `all_day` tinyint(1) DEFAULT '0' COMMENT '是否全天（0否 1是）',
  `location` varchar(200) DEFAULT NULL COMMENT '地点',
  `creator` bigint DEFAULT NULL COMMENT '创建人',
  `type` varchar(32) DEFAULT 'personal' COMMENT '日程类型（personal/meeting/leave/trip）',
  `source` varchar(32) DEFAULT 'manual' COMMENT '来源（manual/meeting/leave/trip）',
  `source_id` varchar(64) DEFAULT NULL COMMENT '来源业务ID',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1已删除）',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户ID',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`event_id`),
  KEY `idx_creator_time` (`creator`,`start_time`),
  KEY `idx_type` (`type`),
  KEY `idx_source` (`source`,`source_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='日程事件表';

-- 日程参与人表
CREATE TABLE IF NOT EXISTS `oa_calendar_event_attendee` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `event_id` bigint NOT NULL COMMENT '日程ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `status` char(1) DEFAULT '0' COMMENT '参与状态（0待确认 1接受 2拒绝）',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_event_id` (`event_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='日程参与人表';

-- 菜单：OA协同办公目录
INSERT IGNORE INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES (3000, 'OA协同办公', 0, 10, 'oa', NULL, 1, 0, 'M', '0', '0', '', 'tree', 'admin', NOW(), '', NULL, 'OA协同办公目录');

-- 菜单：日程管理
INSERT IGNORE INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES (3020, '日程管理', 3000, 4, 'calendar', 'oa/calendar/index', 1, 0, 'C', '0', '0', 'oa:calendar:list', 'date', 'admin', NOW(), '', NULL, '日程管理菜单');

-- 按钮权限
INSERT IGNORE INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES (3021, '日程查询', 3020, 1, '', '', 1, 0, 'F', '0', '0', 'oa:calendar:query', '#', 'admin', NOW(), '', NULL, ''),
       (3022, '日程新增', 3020, 2, '', '', 1, 0, 'F', '0', '0', 'oa:calendar:add', '#', 'admin', NOW(), '', NULL, ''),
       (3023, '日程修改', 3020, 3, '', '', 1, 0, 'F', '0', '0', 'oa:calendar:edit', '#', 'admin', NOW(), '', NULL, ''),
       (3024, '日程删除', 3020, 4, '', '', 1, 0, 'F', '0', '0', 'oa:calendar:remove', '#', 'admin', NOW(), '', NULL, '');

-- 将菜单授权给超级管理员
INSERT IGNORE INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES
(1, 3000), (1, 3020), (1, 3021), (1, 3022), (1, 3023), (1, 3024);
