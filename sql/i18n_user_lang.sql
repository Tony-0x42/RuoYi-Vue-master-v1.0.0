-- 用户语言偏好字段
ALTER TABLE sys_user ADD COLUMN lang VARCHAR(10) DEFAULT 'zh-CN' COMMENT '语言偏好' AFTER status;
