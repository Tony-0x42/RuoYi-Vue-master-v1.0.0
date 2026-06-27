-- OA 固定审批人用户与角色菜单权限初始化脚本
-- 说明：由于 user_id=2 已被占用，本脚本使用下一个可用的 user_id（3）。
-- 角色使用 role_id=3（若依默认角色 1=超级管理员，2=普通角色）。

-- 创建审批人角色（如果不存在）
INSERT IGNORE INTO sys_role (role_id, role_name, role_key, role_sort, data_scope, status, create_by, create_time, remark)
VALUES (3, '审批人', 'oa_approver', 3, '1', '0', 'admin', NOW(), 'OA 固定审批人角色');

-- 创建审批人用户，密码 123456 经过 BCrypt 加密
INSERT IGNORE INTO sys_user (user_id, dept_id, user_name, nick_name, user_type, email, phonenumber, sex, avatar, password, status, del_flag, login_ip, login_date, create_by, create_time, remark)
VALUES (3, 100, 'approver', '审批人', '00', 'approver@example.com', '13800138000', '0', '', '$2a$10$6.EC95EYbdxnZ82bbKBXf./1r5w6rKhXIoemRV0kmkLDWnG4lCn9a', '0', '0', '', NULL, 'admin', NOW(), 'OA 固定审批人');

-- 关联用户与角色
INSERT IGNORE INTO sys_user_role (user_id, role_id) VALUES (3, 3);

-- 为审批人角色分配所有 OA 菜单权限（3000 及其所有子菜单、孙菜单）
INSERT IGNORE INTO sys_role_menu (role_id, menu_id)
SELECT 3, menu_id FROM sys_menu WHERE menu_id = 3000 OR parent_id = 3000
UNION
SELECT 3, menu_id FROM sys_menu WHERE parent_id IN (SELECT menu_id FROM sys_menu WHERE parent_id = 3000);
