-- 考勤申请实体初始化脚本
-- 创建请假/加班/出差/补卡申请表及菜单权限
-- 适用于 MySQL 8 / utf8mb4

-- 1. 请假申请表
CREATE TABLE IF NOT EXISTS `oa_attendance_leave` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `leave_type` varchar(50) DEFAULT '',
  `start_time` datetime DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  `days` decimal(5,2) DEFAULT 0,
  `reason` varchar(500) DEFAULT '',
  `status` varchar(20) DEFAULT 'draft',
  `process_instance_id` varchar(64) DEFAULT NULL,
  `tenant_id` bigint DEFAULT NULL,
  `create_by` varchar(64) DEFAULT '',
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(64) DEFAULT '',
  `update_time` datetime DEFAULT NULL,
  `remark` varchar(500) DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='请假申请';

-- 2. 加班申请表
CREATE TABLE IF NOT EXISTS `oa_attendance_overtime` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `start_time` datetime DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  `hours` decimal(5,2) DEFAULT 0,
  `reason` varchar(500) DEFAULT '',
  `status` varchar(20) DEFAULT 'draft',
  `process_instance_id` varchar(64) DEFAULT NULL,
  `tenant_id` bigint DEFAULT NULL,
  `create_by` varchar(64) DEFAULT '',
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(64) DEFAULT '',
  `update_time` datetime DEFAULT NULL,
  `remark` varchar(500) DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='加班申请';

-- 3. 出差申请表
CREATE TABLE IF NOT EXISTS `oa_attendance_trip` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `destination` varchar(200) DEFAULT '',
  `start_time` datetime DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  `days` decimal(5,2) DEFAULT 0,
  `reason` varchar(500) DEFAULT '',
  `status` varchar(20) DEFAULT 'draft',
  `process_instance_id` varchar(64) DEFAULT NULL,
  `tenant_id` bigint DEFAULT NULL,
  `create_by` varchar(64) DEFAULT '',
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(64) DEFAULT '',
  `update_time` datetime DEFAULT NULL,
  `remark` varchar(500) DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='出差申请';

-- 4. 补卡申请表
CREATE TABLE IF NOT EXISTS `oa_attendance_makeup` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `makeup_date` date DEFAULT NULL,
  `check_type` varchar(20) DEFAULT '',
  `check_time` datetime DEFAULT NULL,
  `reason` varchar(500) DEFAULT '',
  `status` varchar(20) DEFAULT 'draft',
  `process_instance_id` varchar(64) DEFAULT NULL,
  `tenant_id` bigint DEFAULT NULL,
  `create_by` varchar(64) DEFAULT '',
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(64) DEFAULT '',
  `update_time` datetime DEFAULT NULL,
  `remark` varchar(500) DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='补卡申请';

-- 5. 菜单及权限（父菜单 3240 人事考勤）
INSERT IGNORE INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `remark`) VALUES
(3270, '请假申请', 3240, 2, 'leave', 'oa/attendance/leave', 'C', '0', '0', 'oa:attendanceLeave:list', 'edit', 'admin', NOW(), '请假申请菜单'),
(3271, '加班申请', 3240, 3, 'overtime', 'oa/attendance/overtime', 'C', '0', '0', 'oa:attendanceOvertime:list', 'time', 'admin', NOW(), '加班申请菜单'),
(3272, '出差申请', 3240, 4, 'trip', 'oa/attendance/trip', 'C', '0', '0', 'oa:attendanceTrip:list', 'guide', 'admin', NOW(), '出差申请菜单'),
(3273, '补卡申请', 3240, 5, 'makeup', 'oa/attendance/makeup', 'C', '0', '0', 'oa:attendanceMakeup:list', 'calendar', 'admin', NOW(), '补卡申请菜单');

-- 6. 按钮权限
INSERT IGNORE INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `remark`) VALUES
(3274, '请假查询', 3270, 1, '', '', 'F', '0', '0', 'oa:attendanceLeave:query', '#', 'admin', NOW(), ''),
(3275, '请假新增', 3270, 2, '', '', 'F', '0', '0', 'oa:attendanceLeave:add', '#', 'admin', NOW(), ''),
(3276, '请假修改', 3270, 3, '', '', 'F', '0', '0', 'oa:attendanceLeave:edit', '#', 'admin', NOW(), ''),
(3277, '请假删除', 3270, 4, '', '', 'F', '0', '0', 'oa:attendanceLeave:remove', '#', 'admin', NOW(), ''),
(3278, '加班查询', 3271, 1, '', '', 'F', '0', '0', 'oa:attendanceOvertime:query', '#', 'admin', NOW(), ''),
(3279, '加班新增', 3271, 2, '', '', 'F', '0', '0', 'oa:attendanceOvertime:add', '#', 'admin', NOW(), ''),
(3280, '加班修改', 3271, 3, '', '', 'F', '0', '0', 'oa:attendanceOvertime:edit', '#', 'admin', NOW(), ''),
(3281, '加班删除', 3271, 4, '', '', 'F', '0', '0', 'oa:attendanceOvertime:remove', '#', 'admin', NOW(), ''),
(3282, '出差查询', 3272, 1, '', '', 'F', '0', '0', 'oa:attendanceTrip:query', '#', 'admin', NOW(), ''),
(3283, '出差新增', 3272, 2, '', '', 'F', '0', '0', 'oa:attendanceTrip:add', '#', 'admin', NOW(), ''),
(3284, '出差修改', 3272, 3, '', '', 'F', '0', '0', 'oa:attendanceTrip:edit', '#', 'admin', NOW(), ''),
(3285, '出差删除', 3272, 4, '', '', 'F', '0', '0', 'oa:attendanceTrip:remove', '#', 'admin', NOW(), ''),
(3286, '补卡查询', 3273, 1, '', '', 'F', '0', '0', 'oa:attendanceMakeup:query', '#', 'admin', NOW(), ''),
(3287, '补卡新增', 3273, 2, '', '', 'F', '0', '0', 'oa:attendanceMakeup:add', '#', 'admin', NOW(), ''),
(3288, '补卡修改', 3273, 3, '', '', 'F', '0', '0', 'oa:attendanceMakeup:edit', '#', 'admin', NOW(), ''),
(3289, '补卡删除', 3273, 4, '', '', 'F', '0', '0', 'oa:attendanceMakeup:remove', '#', 'admin', NOW(), '');

-- 7. 通用考勤审批权限（用于审批完成回调）
INSERT IGNORE INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `remark`) VALUES
(3290, '考勤审批', 3270, 5, '', '', 'F', '0', '0', 'oa:attendance:approve', '#', 'admin', NOW(), '');
