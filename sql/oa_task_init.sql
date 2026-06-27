-- 任务协作模块初始化脚本
-- 字符集: utf8mb4

-- ----------------------------
-- 表结构: oa_task
-- ----------------------------
CREATE TABLE IF NOT EXISTS `oa_task` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '任务ID',
  `title` varchar(200) NOT NULL COMMENT '任务标题',
  `description` longtext COMMENT '任务描述',
  `owner_id` bigint DEFAULT NULL COMMENT '负责人ID',
  `project_id` bigint DEFAULT NULL COMMENT '关联项目ID',
  `priority` varchar(20) DEFAULT 'medium' COMMENT '优先级（urgent紧急 high高 medium中 low低）',
  `status` tinyint(1) DEFAULT '0' COMMENT '状态（0待处理 1进行中 2已完成 3已取消）',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '截止时间',
  `progress` int DEFAULT '0' COMMENT '进度（0-100）',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户ID',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_owner_id` (`owner_id`),
  KEY `idx_status` (`status`),
  KEY `idx_priority` (`priority`),
  KEY `idx_end_time` (`end_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='任务表';

-- ----------------------------
-- 表结构: oa_task_member
-- ----------------------------
CREATE TABLE IF NOT EXISTS `oa_task_member` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '成员ID',
  `task_id` bigint NOT NULL COMMENT '任务ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role` varchar(20) DEFAULT 'participant' COMMENT '角色（owner负责人 participant参与人）',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户ID',
  `create_time` datetime DEFAULT NULL COMMENT '加入时间',
  PRIMARY KEY (`id`),
  KEY `idx_task_id` (`task_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='任务成员表';

-- ----------------------------
-- 表结构: oa_task_subtask
-- ----------------------------
CREATE TABLE IF NOT EXISTS `oa_task_subtask` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '子任务ID',
  `parent_id` bigint NOT NULL COMMENT '父任务ID',
  `title` varchar(200) NOT NULL COMMENT '子任务标题',
  `status` tinyint(1) DEFAULT '0' COMMENT '状态（0待处理 1进行中 2已完成 3已取消）',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='任务子任务表';

-- ----------------------------
-- 表结构: oa_task_comment
-- ----------------------------
CREATE TABLE IF NOT EXISTS `oa_task_comment` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '评论ID',
  `task_id` bigint NOT NULL COMMENT '任务ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `content` text NOT NULL COMMENT '评论内容',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户ID',
  `create_time` datetime DEFAULT NULL COMMENT '评论时间',
  PRIMARY KEY (`id`),
  KEY `idx_task_id` (`task_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='任务评论表';

-- ----------------------------
-- 表结构: oa_task_attachment
-- ----------------------------
CREATE TABLE IF NOT EXISTS `oa_task_attachment` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '附件ID',
  `task_id` bigint NOT NULL COMMENT '任务ID',
  `file_name` varchar(255) NOT NULL COMMENT '文件名称',
  `file_path` varchar(500) NOT NULL COMMENT '文件路径',
  `file_size` bigint DEFAULT '0' COMMENT '文件大小（字节）',
  `create_time` datetime DEFAULT NULL COMMENT '上传时间',
  PRIMARY KEY (`id`),
  KEY `idx_task_id` (`task_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='任务附件表';

-- ----------------------------
-- 表结构: oa_task_board
-- ----------------------------
CREATE TABLE IF NOT EXISTS `oa_task_board` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '看板ID',
  `name` varchar(100) NOT NULL COMMENT '看板名称',
  `columns_json` json COMMENT '看板列配置（JSON）',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户ID',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='任务看板表';

-- ----------------------------
-- 菜单数据
-- ----------------------------
-- 父目录: OA协同办公（已存在，请勿重复插入）

-- 任务协作菜单
INSERT IGNORE INTO `sys_menu` VALUES (3030, '任务协作', 3000, 4, 'task', 'oa/task/index', '', '', 1, 0, 'C', '0', '0', 'oa:task:list', 'task', 'admin', NOW(), '', NULL, '任务协作菜单');

-- 任务按钮权限
INSERT IGNORE INTO `sys_menu` VALUES (3031, '任务查询', 3030, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'oa:task:query', '#', 'admin', NOW(), '', NULL, '');
INSERT IGNORE INTO `sys_menu` VALUES (3032, '任务新增', 3030, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'oa:task:add', '#', 'admin', NOW(), '', NULL, '');
INSERT IGNORE INTO `sys_menu` VALUES (3033, '任务修改', 3030, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'oa:task:edit', '#', 'admin', NOW(), '', NULL, '');
INSERT IGNORE INTO `sys_menu` VALUES (3034, '任务删除', 3030, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'oa:task:remove', '#', 'admin', NOW(), '', NULL, '');
INSERT IGNORE INTO `sys_menu` VALUES (3035, '任务导出', 3030, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'oa:task:export', '#', 'admin', NOW(), '', NULL, '');

-- 为管理员角色分配任务协作菜单权限
INSERT IGNORE INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES
(1, 3000),
(1, 3030),
(1, 3031),
(1, 3032),
(1, 3033),
(1, 3034),
(1, 3035);
