-- 考勤管理模块初始化脚本
-- 字符集: utf8mb4

-- ----------------------------
-- 表结构: oa_attendance_group
-- ----------------------------
CREATE TABLE IF NOT EXISTS `oa_attendance_group` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '考勤组ID',
  `name` varchar(100) NOT NULL COMMENT '考勤组名称',
  `member_type` varchar(20) DEFAULT 'user' COMMENT '成员类型（dept部门 role角色 user用户）',
  `member_ids` varchar(500) DEFAULT NULL COMMENT '成员ID集合（逗号分隔）',
  `shift_id` bigint DEFAULT NULL COMMENT '班次ID',
  `check_in_type` varchar(20) DEFAULT 'gps' COMMENT '打卡方式（gps/wifi/device/field/multiple）',
  `work_days` varchar(20) DEFAULT '1,2,3,4,5' COMMENT '工作日（1-7逗号分隔）',
  `rule_json` varchar(500) DEFAULT NULL COMMENT '考勤规则JSON',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户ID',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_shift_id` (`shift_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='考勤组表';

-- ----------------------------
-- 表结构: oa_attendance_shift
-- ----------------------------
CREATE TABLE IF NOT EXISTS `oa_attendance_shift` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '班次ID',
  `name` varchar(100) NOT NULL COMMENT '班次名称',
  `start_time` varchar(10) DEFAULT '09:00' COMMENT '上班时间',
  `end_time` varchar(10) DEFAULT '18:00' COMMENT '下班时间',
  `late_rule` int DEFAULT '0' COMMENT '允许迟到分钟数',
  `early_rule` int DEFAULT '0' COMMENT '允许早退分钟数',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户ID',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='班次表';

-- ----------------------------
-- 表结构: oa_attendance_record
-- ----------------------------
CREATE TABLE IF NOT EXISTS `oa_attendance_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `user_id` bigint DEFAULT NULL COMMENT '用户ID',
  `check_in_date` date DEFAULT NULL COMMENT '打卡日期',
  `check_in_time` datetime DEFAULT NULL COMMENT '打卡时间',
  `type` varchar(20) DEFAULT 'gps' COMMENT '打卡类型（gps/wifi/device/field）',
  `location` varchar(255) DEFAULT NULL COMMENT '打卡地点',
  `wifi` varchar(100) DEFAULT NULL COMMENT 'WiFi名称',
  `photo` varchar(255) DEFAULT NULL COMMENT '打卡照片',
  `status` varchar(20) DEFAULT 'normal' COMMENT '考勤状态（normal正常 late迟到 early早退 absent缺卡）',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户ID',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_check_in_date` (`check_in_date`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='打卡记录表';

-- ----------------------------
-- 表结构: oa_attendance_schedule
-- ----------------------------
CREATE TABLE IF NOT EXISTS `oa_attendance_schedule` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '排班ID',
  `user_id` bigint DEFAULT NULL COMMENT '用户ID',
  `schedule_date` date DEFAULT NULL COMMENT '排班日期',
  `shift_id` bigint DEFAULT NULL COMMENT '班次ID',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户ID',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_schedule_date` (`schedule_date`),
  KEY `idx_shift_id` (`shift_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='排班表';

-- ----------------------------
-- 表结构: oa_attendance_monthly
-- ----------------------------
CREATE TABLE IF NOT EXISTS `oa_attendance_monthly` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '统计ID',
  `user_id` bigint DEFAULT NULL COMMENT '用户ID',
  `year_month` varchar(10) DEFAULT NULL COMMENT '年月',
  `normal_days` int DEFAULT '0' COMMENT '正常出勤天数',
  `late_count` int DEFAULT '0' COMMENT '迟到次数',
  `early_count` int DEFAULT '0' COMMENT '早退次数',
  `absent_days` int DEFAULT '0' COMMENT '缺勤天数',
  `leave_days` int DEFAULT '0' COMMENT '请假天数',
  `overtime_hours` decimal(10,2) DEFAULT '0.00' COMMENT '加班小时数',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户ID',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_year_month` (`year_month`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='月度考勤统计表';

-- ----------------------------
-- 表结构: oa_leave_balance
-- ----------------------------
CREATE TABLE IF NOT EXISTS `oa_leave_balance` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '余额ID',
  `user_id` bigint DEFAULT NULL COMMENT '用户ID',
  `leave_type` varchar(20) DEFAULT NULL COMMENT '假期类型（annual年假 compensated调休 other其他）',
  `total_days` decimal(10,2) DEFAULT '0.00' COMMENT '总天数',
  `used_days` decimal(10,2) DEFAULT '0.00' COMMENT '已用天数',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户ID',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_leave_type` (`leave_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='假期余额表';

-- ----------------------------
-- 菜单数据
-- ----------------------------
-- 父目录: OA协同办公（已存在，请勿重复插入）

-- 考勤管理菜单
INSERT IGNORE INTO `sys_menu` VALUES (3100, '考勤管理', 3000, 6, 'attendance', 'oa/attendance/index', '', '', 1, 0, 'C', '0', '0', 'oa:attendance:list', 'date', 'admin', NOW(), '', NULL, '考勤管理菜单');
-- 考勤组菜单
INSERT IGNORE INTO `sys_menu` VALUES (3101, '考勤组', 3100, 1, 'group', 'oa/attendance/group', '', '', 1, 0, 'C', '0', '0', 'oa:attendanceGroup:list', 'tree', 'admin', NOW(), '', NULL, '考勤组菜单');
-- 打卡记录菜单
INSERT IGNORE INTO `sys_menu` VALUES (3102, '打卡记录', 3100, 2, 'record', 'oa/attendance/record', '', '', 1, 0, 'C', '0', '0', 'oa:attendanceRecord:list', 'list', 'admin', NOW(), '', NULL, '打卡记录菜单');

-- 考勤管理按钮权限
INSERT IGNORE INTO `sys_menu` VALUES (3103, '考勤查询', 3100, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'oa:attendance:query', '#', 'admin', NOW(), '', NULL, '');
INSERT IGNORE INTO `sys_menu` VALUES (3104, '考勤统计', 3100, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'oa:attendance:statistics', '#', 'admin', NOW(), '', NULL, '');

-- 考勤组按钮权限
INSERT IGNORE INTO `sys_menu` VALUES (3105, '考勤组查询', 3101, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'oa:attendanceGroup:query', '#', 'admin', NOW(), '', NULL, '');
INSERT IGNORE INTO `sys_menu` VALUES (3106, '考勤组新增', 3101, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'oa:attendanceGroup:add', '#', 'admin', NOW(), '', NULL, '');
INSERT IGNORE INTO `sys_menu` VALUES (3107, '考勤组修改', 3101, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'oa:attendanceGroup:edit', '#', 'admin', NOW(), '', NULL, '');
INSERT IGNORE INTO `sys_menu` VALUES (3108, '考勤组删除', 3101, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'oa:attendanceGroup:remove', '#', 'admin', NOW(), '', NULL, '');

-- 打卡记录按钮权限
INSERT IGNORE INTO `sys_menu` VALUES (3109, '打卡记录查询', 3102, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'oa:attendanceRecord:query', '#', 'admin', NOW(), '', NULL, '');
INSERT IGNORE INTO `sys_menu` VALUES (3110, '打卡记录新增', 3102, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'oa:attendanceRecord:add', '#', 'admin', NOW(), '', NULL, '');
INSERT IGNORE INTO `sys_menu` VALUES (3111, '打卡记录修改', 3102, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'oa:attendanceRecord:edit', '#', 'admin', NOW(), '', NULL, '');
INSERT IGNORE INTO `sys_menu` VALUES (3112, '打卡记录删除', 3102, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'oa:attendanceRecord:remove', '#', 'admin', NOW(), '', NULL, '');

-- 为管理员角色分配考勤管理菜单权限
INSERT IGNORE INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES
(1, 3000),
(1, 3100),
(1, 3101),
(1, 3102),
(1, 3103),
(1, 3104),
(1, 3105),
(1, 3106),
(1, 3107),
(1, 3108),
(1, 3109),
(1, 3110),
(1, 3111),
(1, 3112);
