-- 资产领用/调拨/维修/报废审批工作流接入补丁
-- 用途：为已部署环境补充状态字段、审批按钮权限，并分配给审批人角色
-- 字符集: utf8mb4

-- ----------------------------
-- 结构变更
-- ----------------------------
ALTER TABLE `oa_asset_receive`
    MODIFY COLUMN `status` tinyint(1) DEFAULT '0' COMMENT '状态（0待审批 1已领用 2已驳回）';

ALTER TABLE `oa_asset_transfer`
    ADD COLUMN `status` tinyint(1) DEFAULT '0' COMMENT '状态（0待审批 1已调拨 2已驳回）' AFTER `transfer_time`;

ALTER TABLE `oa_asset_repair`
    ADD COLUMN `process_instance_id` varchar(100) DEFAULT NULL COMMENT '流程实例ID' AFTER `vendor`,
    MODIFY COLUMN `status` tinyint(1) DEFAULT '0' COMMENT '状态（0待审批 1维修中 2已驳回 3已完成）';

ALTER TABLE `oa_asset_scrap`
    ADD COLUMN `status` tinyint(1) DEFAULT '0' COMMENT '状态（0待审批 1已报废 2已驳回）' AFTER `scrap_time`;

-- ----------------------------
-- 权限数据
-- ----------------------------
INSERT IGNORE INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
(3164, '资产审批', 3150, 6, '#', NULL, '', '', 1, 0, 'F', '0', '0', 'oa:asset:approve', '#', 'admin', NOW(), '', NULL, '');

-- 为管理员角色分配审批按钮权限
INSERT IGNORE INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES
(1, 3164);

-- 为审批人角色分配审批按钮权限
INSERT IGNORE INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES
(3, 3164);
