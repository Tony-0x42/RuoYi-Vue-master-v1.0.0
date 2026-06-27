-- ========================================================
-- 自定义工作台模块初始化脚本
-- 包含：数据表、菜单、权限
-- ========================================================

-- --------------------------------------------------------
-- 1. 组件注册表
-- --------------------------------------------------------
DROP TABLE IF EXISTS `bpm_workbench_component`;
CREATE TABLE `bpm_workbench_component` (
  `component_id` bigint NOT NULL AUTO_INCREMENT COMMENT '组件ID',
  `component_name` varchar(64) NOT NULL COMMENT '组件英文名（唯一）',
  `component_label` varchar(64) NOT NULL COMMENT '组件显示名称',
  `component_desc` varchar(255) DEFAULT NULL COMMENT '组件描述',
  `component_path` varchar(255) NOT NULL COMMENT '前端组件路径',
  `icon` varchar(64) DEFAULT NULL COMMENT '组件图标',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`component_id`),
  UNIQUE KEY `uk_component_name` (`component_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='工作台组件注册表';

-- --------------------------------------------------------
-- 2. 工作台模板表
-- --------------------------------------------------------
DROP TABLE IF EXISTS `bpm_workbench_template`;
CREATE TABLE `bpm_workbench_template` (
  `template_id` bigint NOT NULL AUTO_INCREMENT COMMENT '模板ID',
  `template_name` varchar(64) NOT NULL COMMENT '模板名称',
  `template_code` varchar(64) DEFAULT NULL COMMENT '模板编码',
  `is_default` char(1) DEFAULT '0' COMMENT '是否默认模板（0否 1是）',
  `config_json` longtext COMMENT '模板布局配置JSON',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`template_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='工作台模板表';

-- --------------------------------------------------------
-- 3. 模板适用范围（角色/部门/人员）
-- --------------------------------------------------------
DROP TABLE IF EXISTS `bpm_workbench_template_scope`;
CREATE TABLE `bpm_workbench_template_scope` (
  `scope_id` bigint NOT NULL AUTO_INCREMENT COMMENT '范围ID',
  `template_id` bigint NOT NULL COMMENT '模板ID',
  `scope_type` char(1) NOT NULL COMMENT '范围类型（1角色 2部门 3人员）',
  `scope_target_id` bigint NOT NULL COMMENT '目标ID（角色ID/部门ID/用户ID）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`scope_id`),
  UNIQUE KEY `uk_template_scope` (`template_id`, `scope_type`, `scope_target_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='工作台模板适用范围';

-- --------------------------------------------------------
-- 4. 组件可用范围（角色/部门/人员）
-- --------------------------------------------------------
DROP TABLE IF EXISTS `bpm_workbench_component_scope`;
CREATE TABLE `bpm_workbench_component_scope` (
  `scope_id` bigint NOT NULL AUTO_INCREMENT COMMENT '范围ID',
  `component_id` bigint NOT NULL COMMENT '组件ID',
  `scope_type` char(1) NOT NULL COMMENT '范围类型（1角色 2部门 3人员）',
  `scope_target_id` bigint NOT NULL COMMENT '目标ID（角色ID/部门ID/用户ID）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`scope_id`),
  UNIQUE KEY `uk_component_scope` (`component_id`, `scope_type`, `scope_target_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='工作台组件可用范围';

-- --------------------------------------------------------
-- 5. 用户个人工作台配置
-- --------------------------------------------------------
DROP TABLE IF EXISTS `bpm_workbench_user_config`;
CREATE TABLE `bpm_workbench_user_config` (
  `config_id` bigint NOT NULL AUTO_INCREMENT COMMENT '配置ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `config_name` varchar(64) NOT NULL COMMENT '工作台名称',
  `template_id` bigint DEFAULT NULL COMMENT '基于的模板ID',
  `is_default` char(1) DEFAULT '0' COMMENT '是否默认（0否 1是）',
  `config_json` longtext COMMENT '自定义配置JSON',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`config_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户工作台配置';

-- --------------------------------------------------------
-- 6. 菜单与权限
-- --------------------------------------------------------
-- 新增顶级“工作台”菜单分类
INSERT INTO `sys_menu` VALUES
(2200, '工作台', 0, 5, 'workbench', 'Layout', '', '', 1, 0, 'M', '0', '0', '', 'el-icon-s-platform', 'admin', NOW(), '', NULL, '工作台顶级菜单');

-- 在“工作台”分类下新增子菜单
INSERT INTO `sys_menu` VALUES
(2103, '我的工作台', 2200, 1, 'myWorkbench', 'bpm/workbench/my/index', '', '', 1, 0, 'C', '0', '0', 'bpm:workbench:my', 'user', 'admin', NOW(), '', NULL, '我的工作台菜单'),
(2101, '组件管理', 2200, 2, 'component', 'bpm/workbench/component/index', '', '', 1, 0, 'C', '0', '0', 'bpm:workbench:component', 'component', 'admin', NOW(), '', NULL, '工作台组件管理菜单'),
(2102, '模板管理', 2200, 3, 'template', 'bpm/workbench/template/index', '', '', 1, 0, 'C', '0', '0', 'bpm:workbench:template', 'template', 'admin', NOW(), '', NULL, '工作台模板管理菜单');

-- 将菜单授权给超级管理员（角色1）
INSERT INTO `sys_role_menu` VALUES
(1, 2200), (1, 2101), (1, 2102), (1, 2103);

-- 将“我的工作台”授权给普通角色（角色2）
INSERT INTO `sys_role_menu` VALUES
(2, 2200), (2, 2103);

-- --------------------------------------------------------
-- 7. 默认组件与模板数据
-- --------------------------------------------------------
INSERT INTO `bpm_workbench_component` (`component_name`, `component_label`, `component_desc`, `component_path`, `icon`, `status`, `create_by`, `create_time`) VALUES
('WelcomeCard', '欢迎卡片', '显示欢迎语和日期', '@/views/bpm/workbench/components/WelcomeCard', 'el-icon-s-home', '0', 'admin', NOW()),
('TaskTodo', '待办任务', '展示待办任务列表', '@/views/bpm/workbench/components/TaskTodo', 'el-icon-s-check', '0', 'admin', NOW()),
('TaskDone', '已办任务', '展示已办任务列表', '@/views/bpm/workbench/components/TaskDone', 'el-icon-finished', '0', 'admin', NOW()),
('NoticeList', '通知公告', '展示通知公告列表', '@/views/bpm/workbench/components/NoticeList', 'el-icon-message', '0', 'admin', NOW());

INSERT INTO `bpm_workbench_template` (`template_name`, `template_code`, `is_default`, `config_json`, `status`, `create_by`, `create_time`) VALUES
('默认工作台', 'default', '1', '[{"component":"WelcomeCard","title":"欢迎","icon":"el-icon-s-home","span":24},{"component":"TaskTodo","title":"待办任务","icon":"el-icon-s-check","span":12},{"component":"NoticeList","title":"通知公告","icon":"el-icon-message","span":12}]', '0', 'admin', NOW());
