-- ============================================================
-- 统一流程引擎中台 v2 初始化脚本
-- 依据 spec/06-数据模型.md 设计
-- 字符集：utf8mb4
-- ============================================================

-- 流程分类
DROP TABLE IF EXISTS `bpm_category`;
CREATE TABLE `bpm_category`
(
    `id`          bigint(20)   NOT NULL AUTO_INCREMENT COMMENT '分类ID',
    `parent_id`   bigint(20)   DEFAULT '0' COMMENT '父分类ID',
    `name`        varchar(128) NOT NULL COMMENT '分类名称',
    `code`        varchar(64)  NOT NULL COMMENT '分类编码',
    `sort`        int(11)      DEFAULT '0' COMMENT '显示排序',
    `status`      tinyint(1)   DEFAULT '1' COMMENT '状态（0停用 1启用）',
    `tenant_id`   bigint(20)   DEFAULT '0' COMMENT '租户ID',
    `create_by`   varchar(64)  DEFAULT '' COMMENT '创建者',
    `create_time` datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`   varchar(64)  DEFAULT '' COMMENT '更新者',
    `update_time` datetime     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `remark`      varchar(500) DEFAULT '' COMMENT '备注',
    PRIMARY KEY (`id`),
    KEY `idx_code` (`code`),
    KEY `idx_status` (`status`),
    KEY `idx_tenant` (`tenant_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COMMENT = '流程分类';

-- 流程定义
DROP TABLE IF EXISTS `bpm_process_definition`;
CREATE TABLE `bpm_process_definition`
(
    `id`              bigint(20)    NOT NULL AUTO_INCREMENT COMMENT '流程定义ID',
    `process_key`     varchar(64)   NOT NULL COMMENT '流程定义唯一标识',
    `name`            varchar(128)  NOT NULL COMMENT '流程名称',
    `category_id`     bigint(20)    DEFAULT '0' COMMENT '分类ID',
    `tenant_id`       bigint(20)    DEFAULT '0' COMMENT '租户ID',
    `latest_version`  varchar(32)   DEFAULT '' COMMENT '最新发布版本号',
    `status`          tinyint(1)    DEFAULT '0' COMMENT '状态：0草稿 1已发布 2已停用',
    `xml`             longtext COMMENT 'BPMN 2.0 XML',
    `ext_json`        json COMMENT '扩展配置',
    `create_by`       varchar(64)   DEFAULT '' COMMENT '创建者',
    `create_time`     datetime      DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`       varchar(64)   DEFAULT '' COMMENT '更新者',
    `update_time`     datetime      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `remark`          varchar(500)  DEFAULT '' COMMENT '备注',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_key_tenant` (`process_key`, `tenant_id`),
    KEY `idx_category` (`category_id`),
    KEY `idx_status` (`status`),
    KEY `idx_tenant` (`tenant_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COMMENT = '流程定义';

-- 流程定义版本
DROP TABLE IF EXISTS `bpm_definition_version`;
CREATE TABLE `bpm_definition_version`
(
    `id`            bigint(20)    NOT NULL AUTO_INCREMENT COMMENT '版本ID',
    `definition_id` bigint(20)    NOT NULL COMMENT '流程定义ID',
    `version`       varchar(32)   NOT NULL COMMENT '版本号',
    `version_name`  varchar(128)  DEFAULT '' COMMENT '版本名称',
    `changelog`     varchar(500)  DEFAULT '' COMMENT '变更日志',
    `xml`           longtext COMMENT 'BPMN XML',
    `ext_json`      json COMMENT '扩展配置',
    `status`        tinyint(1)    DEFAULT '0' COMMENT '状态：0草稿 1已发布 2已停用',
    `publish_time`  datetime COMMENT '发布时间',
    `publish_by`    varchar(64)   DEFAULT '' COMMENT '发布人',
    `create_by`     varchar(64)   DEFAULT '' COMMENT '创建者',
    `create_time`   datetime      DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`     varchar(64)   DEFAULT '' COMMENT '更新者',
    `update_time`   datetime      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `remark`        varchar(500)  DEFAULT '' COMMENT '备注',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_definition_version` (`definition_id`, `version`),
    KEY `idx_definition` (`definition_id`),
    KEY `idx_status` (`status`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COMMENT = '流程定义版本';

-- 流程实例
DROP TABLE IF EXISTS `bpm_process_instance`;
CREATE TABLE `bpm_process_instance`
(
    `id`             varchar(64)   NOT NULL COMMENT '实例ID',
    `definition_id`  bigint(20)    NOT NULL COMMENT '流程定义ID',
    `version_id`     bigint(20)    NOT NULL COMMENT '版本ID',
    `business_key`   varchar(128)  DEFAULT '' COMMENT '业务标识',
    `starter`        bigint(20)    NOT NULL COMMENT '发起人用户ID',
    `status`         varchar(32)   NOT NULL COMMENT '状态：RUNNING/COMPLETED/REJECTED/TERMINATED/SUSPENDED',
    `variables`      json COMMENT '流程变量',
    `tenant_id`      bigint(20)    DEFAULT '0' COMMENT '租户ID',
    `start_time`     datetime      DEFAULT CURRENT_TIMESTAMP COMMENT '开始时间',
    `end_time`       datetime COMMENT '结束时间',
    `create_by`      varchar(64)   DEFAULT '' COMMENT '创建者',
    `create_time`    datetime      DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_definition` (`definition_id`),
    KEY `idx_starter` (`starter`),
    KEY `idx_status` (`status`),
    KEY `idx_business_key` (`business_key`),
    KEY `idx_tenant` (`tenant_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COMMENT = '流程实例';

-- 任务/待办
DROP TABLE IF EXISTS `bpm_task`;
CREATE TABLE `bpm_task`
(
    `id`           varchar(64)   NOT NULL COMMENT '任务ID',
    `instance_id`  varchar(64)   NOT NULL COMMENT '所属实例ID',
    `node_id`      varchar(64)   NOT NULL COMMENT '节点ID',
    `node_name`    varchar(128)  DEFAULT '' COMMENT '节点名称',
    `assignee`     bigint(20) COMMENT '办理人',
    `candidates`   json COMMENT '候选人列表',
    `status`       varchar(32)   NOT NULL COMMENT '状态：PENDING/CLAIMED/COMPLETED/TRANSFERRED/RETURNED',
    `due_time`     datetime COMMENT '截止时间',
    `end_time`     datetime COMMENT '完成时间',
    `action`       varchar(32) COMMENT '操作动作',
    `opinion`      varchar(2000) COMMENT '审批意见',
    `create_time`  datetime      DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_instance` (`instance_id`),
    KEY `idx_assignee_status` (`assignee`, `status`),
    KEY `idx_tenant_status` (`status`),
    KEY `idx_due_time` (`due_time`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COMMENT = '任务/待办';

-- 任务历史
DROP TABLE IF EXISTS `bpm_task_history`;
CREATE TABLE `bpm_task_history`
(
    `id`            bigint(20)    NOT NULL AUTO_INCREMENT COMMENT '历史ID',
    `task_id`       varchar(64)   DEFAULT '' COMMENT '任务ID',
    `instance_id`   varchar(64)   NOT NULL COMMENT '实例ID',
    `node_id`       varchar(64)   DEFAULT '' COMMENT '节点ID',
    `operator`      bigint(20) COMMENT '操作人',
    `action`        varchar(32)   DEFAULT '' COMMENT '操作动作',
    `opinion`       varchar(2000) COMMENT '审批意见',
    `form_data`     json COMMENT '表单数据',
    `operate_time`  datetime      DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    `create_time`   datetime      DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_instance` (`instance_id`),
    KEY `idx_operator` (`operator`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COMMENT = '任务历史';

-- 表单定义
DROP TABLE IF EXISTS `bpm_form_definition`;
CREATE TABLE `bpm_form_definition`
(
    `id`            bigint(20)    NOT NULL AUTO_INCREMENT COMMENT '表单ID',
    `definition_id` bigint(20)    NOT NULL COMMENT '流程定义ID',
    `version_id`    bigint(20)    DEFAULT '0' COMMENT '版本ID',
    `name`          varchar(128)  NOT NULL COMMENT '表单名称',
    `schema_json`   longtext COMMENT '表单 Schema JSON',
    `tenant_id`     bigint(20)    DEFAULT '0' COMMENT '租户ID',
    `create_by`     varchar(64)   DEFAULT '' COMMENT '创建者',
    `create_time`   datetime      DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`     varchar(64)   DEFAULT '' COMMENT '更新者',
    `update_time`   datetime      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_definition` (`definition_id`),
    KEY `idx_tenant` (`tenant_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COMMENT = '表单定义';

-- 表单数据
DROP TABLE IF EXISTS `bpm_form_data`;
CREATE TABLE `bpm_form_data`
(
    `id`           bigint(20)   NOT NULL AUTO_INCREMENT COMMENT '数据ID',
    `instance_id`  varchar(64)  NOT NULL COMMENT '实例ID',
    `field_code`   varchar(128) NOT NULL COMMENT '字段编码',
    `field_value`  text COMMENT '字段值',
    `field_type`   varchar(64)  DEFAULT '' COMMENT '字段类型',
    `create_time`  datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_instance_field` (`instance_id`, `field_code`),
    KEY `idx_instance` (`instance_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COMMENT = '表单数据';

-- 字段权限
DROP TABLE IF EXISTS `bpm_field_permission`;
CREATE TABLE `bpm_field_permission`
(
    `id`            bigint(20)   NOT NULL AUTO_INCREMENT COMMENT '权限ID',
    `definition_id` bigint(20)   NOT NULL COMMENT '流程定义ID',
    `node_id`       varchar(64)  NOT NULL COMMENT '节点ID',
    `field_code`    varchar(128) NOT NULL COMMENT '字段编码',
    `permission`    varchar(32)  NOT NULL COMMENT '权限：VISIBLE/EDITABLE/HIDDEN/REQUIRED',
    `tenant_id`     bigint(20)   DEFAULT '0' COMMENT '租户ID',
    `create_time`   datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_definition_node_field` (`definition_id`, `node_id`, `field_code`),
    KEY `idx_definition` (`definition_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COMMENT = '字段权限';

-- 通知模板
DROP TABLE IF EXISTS `bpm_notification_template`;
CREATE TABLE `bpm_notification_template`
(
    `id`          bigint(20)    NOT NULL AUTO_INCREMENT COMMENT '模板ID',
    `type`        varchar(64)   NOT NULL COMMENT '模板类型',
    `channel`     varchar(64)   NOT NULL COMMENT '渠道：SITE/EMAIL/SMS/DINGTALK/WECHAT/FEISHU',
    `subject`     varchar(256)  DEFAULT '' COMMENT '主题',
    `content`     text COMMENT '内容',
    `tenant_id`   bigint(20)    DEFAULT '0' COMMENT '租户ID',
    `create_by`   varchar(64)   DEFAULT '' COMMENT '创建者',
    `create_time` datetime      DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`   varchar(64)   DEFAULT '' COMMENT '更新者',
    `update_time` datetime      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_type_channel_tenant` (`type`, `channel`, `tenant_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COMMENT = '通知模板';

-- 工作日历
DROP TABLE IF EXISTS `bpm_work_calendar`;
CREATE TABLE `bpm_work_calendar`
(
    `id`          bigint(20)     NOT NULL AUTO_INCREMENT COMMENT '日历ID',
    `tenant_id`   bigint(20)     DEFAULT '0' COMMENT '租户ID',
    `year`        int(11)        NOT NULL COMMENT '年份',
    `date`        date           NOT NULL COMMENT '日期',
    `type`        varchar(32)    NOT NULL COMMENT '类型：WORK/DAYOFF/HOLIDAY/ADJUST',
    `work_hours`  decimal(4,2)   DEFAULT '8.00' COMMENT '工作小时数',
    `create_by`   varchar(64)    DEFAULT '' COMMENT '创建者',
    `create_time` datetime       DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_tenant_date` (`tenant_id`, `date`),
    KEY `idx_year` (`year`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COMMENT = '工作日历';

-- 打印模板
DROP TABLE IF EXISTS `bpm_print_template`;
CREATE TABLE `bpm_print_template`
(
    `id`             bigint(20)    NOT NULL AUTO_INCREMENT COMMENT '模板ID',
    `definition_id`  bigint(20)    DEFAULT '0' COMMENT '流程定义ID',
    `version_id`     bigint(20)    DEFAULT '0' COMMENT '版本ID',
    `name`           varchar(128)  NOT NULL COMMENT '模板名称',
    `content`        longtext COMMENT '模板内容（HTML/Word）',
    `field_mapping`  json COMMENT '字段映射',
    `page_setting`   json COMMENT '页面设置',
    `tenant_id`      bigint(20)    DEFAULT '0' COMMENT '租户ID',
    `create_by`      varchar(64)   DEFAULT '' COMMENT '创建者',
    `create_time`    datetime      DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`      varchar(64)   DEFAULT '' COMMENT '更新者',
    `update_time`    datetime      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_definition` (`definition_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COMMENT = '打印模板';

-- 电子签名
DROP TABLE IF EXISTS `bpm_signature`;
CREATE TABLE `bpm_signature`
(
    `id`              bigint(20)    NOT NULL AUTO_INCREMENT COMMENT '签名ID',
    `user_id`         bigint(20)    NOT NULL COMMENT '用户ID',
    `signature_type`  varchar(64)   DEFAULT 'handwrite' COMMENT '签名类型：handwrite/electronic',
    `signature_data`  text COMMENT '签名数据（Base64/URL）',
    `certificate`     text COMMENT '数字证书',
    `tenant_id`       bigint(20)    DEFAULT '0' COMMENT '租户ID',
    `create_time`     datetime      DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_tenant` (`user_id`, `tenant_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COMMENT = '电子签名';

-- 用户组织扩展
DROP TABLE IF EXISTS `bpm_user_org_ext`;
CREATE TABLE `bpm_user_org_ext`
(
    `id`          bigint(20)   NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id`     bigint(20)   NOT NULL COMMENT '用户ID',
    `leader_id`   bigint(20)   DEFAULT NULL COMMENT '直属主管ID',
    `dept_id`     bigint(20)   DEFAULT NULL COMMENT '部门ID',
    `position`    varchar(128) DEFAULT '' COMMENT '岗位',
    `tags`        varchar(500) DEFAULT '' COMMENT '标签（逗号分隔）',
    `status`      tinyint(1)   DEFAULT '0' COMMENT '状态：0正常 1停用',
    `tenant_id`   bigint(20)   DEFAULT '0' COMMENT '租户ID',
    `create_by`   varchar(64)  DEFAULT '' COMMENT '创建者',
    `create_time` datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_by`   varchar(64)  DEFAULT '' COMMENT '更新者',
    `update_time` datetime     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_tenant` (`user_id`, `tenant_id`),
    KEY `idx_leader` (`leader_id`),
    KEY `idx_dept` (`dept_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COMMENT = '用户组织扩展';

-- 审计日志
DROP TABLE IF EXISTS `bpm_audit_log`;
CREATE TABLE `bpm_audit_log`
(
    `id`           bigint(20)    NOT NULL AUTO_INCREMENT COMMENT '日志ID',
    `tenant_id`    bigint(20)    DEFAULT '0' COMMENT '租户ID',
    `user_id`      bigint(20) COMMENT '用户ID',
    `ip`           varchar(128)  DEFAULT '' COMMENT 'IP地址',
    `action`       varchar(128)  NOT NULL COMMENT '操作动作',
    `object_type`  varchar(128)  DEFAULT '' COMMENT '对象类型',
    `object_id`    varchar(128)  DEFAULT '' COMMENT '对象ID',
    `before_value` json COMMENT '变更前值',
    `after_value`  json COMMENT '变更后值',
    `operate_time` datetime      DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    `create_time`  datetime      DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_user` (`user_id`),
    KEY `idx_action` (`action`),
    KEY `idx_object` (`object_type`, `object_id`),
    KEY `idx_operate_time` (`operate_time`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COMMENT = '审计日志';
