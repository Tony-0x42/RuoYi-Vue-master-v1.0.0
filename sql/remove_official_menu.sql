-- 增量迁移脚本：移除侧边栏“官方官网”菜单
-- 适用场景：已有数据的数据库升级
-- 操作说明：直接删除 menu_id = 4 的菜单记录，并同步清理角色菜单关联

DELETE FROM `sys_role_menu` WHERE `menu_id` = 4;
DELETE FROM `sys_menu` WHERE `menu_id` = 4;
