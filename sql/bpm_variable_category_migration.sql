-- BPM 变量定义支持分类归属的增量迁移脚本
-- 适用场景：已在运行的数据库，需要升级到支持按分类继承变量定义

-- 1. 新增分类ID列
ALTER TABLE `bpm_variable`
    ADD COLUMN `category_id` bigint DEFAULT NULL COMMENT '分类ID' AFTER `variable_id`;

-- 2. 将原非空定义ID改为可空
ALTER TABLE `bpm_variable`
    MODIFY COLUMN `definition_id` bigint DEFAULT NULL COMMENT '定义ID';

-- 3. 增加唯一索引，防止同一分类或同一流程定义下变量编码重复
ALTER TABLE `bpm_variable`
    ADD UNIQUE KEY `uk_category_variable_code` (`category_id`,`variable_code`),
    ADD UNIQUE KEY `uk_definition_variable_code` (`definition_id`,`variable_code`);
