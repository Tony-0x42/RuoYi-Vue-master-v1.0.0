-- OA 审批流程定义初始化脚本
-- 生成 10 个简单 OA 审批流程定义及已发布版本
-- 适用于 MySQL 8 / utf8mb4

-- 1. 清理旧的 OA 流程定义（便于重复执行）
DELETE FROM bpm_definition_version WHERE definition_id IN (SELECT id FROM bpm_process_definition WHERE process_key LIKE 'oa_%');
DELETE FROM bpm_process_definition WHERE process_key LIKE 'oa_%';

-- 2. 创建 OA 流程定义
INSERT INTO bpm_process_definition (id, process_key, name, category_id, tenant_id, status, latest_version, xml, ext_json, create_by, create_time, update_by, update_time, remark)
VALUES
(10001, 'oa_expense_report', '报销审批', 0, 0, 1, '1.0', '', '{}', 'admin', NOW(), 'admin', NOW(), '费用报销单审批'),
(10002, 'oa_expense_loan', '借款审批', 0, 0, 1, '1.0', '', '{}', 'admin', NOW(), 'admin', NOW(), '借款单审批'),
(10003, 'oa_asset_receive', '资产领用审批', 0, 0, 1, '1.0', '', '{}', 'admin', NOW(), 'admin', NOW(), '资产领用审批'),
(10004, 'oa_asset_transfer', '资产调拨审批', 0, 0, 1, '1.0', '', '{}', 'admin', NOW(), 'admin', NOW(), '资产调拨审批'),
(10005, 'oa_asset_repair', '资产维修审批', 0, 0, 1, '1.0', '', '{}', 'admin', NOW(), 'admin', NOW(), '资产维修审批'),
(10006, 'oa_asset_scrap', '资产报废审批', 0, 0, 1, '1.0', '', '{}', 'admin', NOW(), 'admin', NOW(), '资产报废审批'),
(10007, 'oa_attendance_leave', '请假审批', 0, 0, 1, '1.0', '', '{}', 'admin', NOW(), 'admin', NOW(), '请假审批'),
(10008, 'oa_attendance_overtime', '加班审批', 0, 0, 1, '1.0', '', '{}', 'admin', NOW(), 'admin', NOW(), '加班审批'),
(10009, 'oa_attendance_trip', '出差审批', 0, 0, 1, '1.0', '', '{}', 'admin', NOW(), 'admin', NOW(), '出差审批'),
(10010, 'oa_attendance_makeup', '补卡审批', 0, 0, 1, '1.0', '', '{}', 'admin', NOW(), 'admin', NOW(), '补卡审批');

-- 3. 发布初始版本
INSERT INTO bpm_definition_version (definition_id, version, version_name, status, publish_time, publish_by, xml, ext_json, changelog, create_by, create_time, update_by, update_time, remark)
VALUES
(10001, '1.0', '初始版本', 1, NOW(), 'admin', '<definitions xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL" xmlns:flowable="http://flowable.org/bpmn"><process id="oa_expense_report" name="报销审批"><startEvent id="start"/><sequenceFlow id="f1" sourceRef="start" targetRef="approve"/><userTask id="approve" name="审批" flowable:assignee="${approvalAssignee}"/><sequenceFlow id="f2" sourceRef="approve" targetRef="end"/><endEvent id="end"/></process></definitions>', '{}', 'OA 审批流程初始版本', 'admin', NOW(), 'admin', NOW(), ''),
(10002, '1.0', '初始版本', 1, NOW(), 'admin', '<definitions xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL" xmlns:flowable="http://flowable.org/bpmn"><process id="oa_expense_loan" name="借款审批"><startEvent id="start"/><sequenceFlow id="f1" sourceRef="start" targetRef="approve"/><userTask id="approve" name="审批" flowable:assignee="${approvalAssignee}"/><sequenceFlow id="f2" sourceRef="approve" targetRef="end"/><endEvent id="end"/></process></definitions>', '{}', 'OA 审批流程初始版本', 'admin', NOW(), 'admin', NOW(), ''),
(10003, '1.0', '初始版本', 1, NOW(), 'admin', '<definitions xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL" xmlns:flowable="http://flowable.org/bpmn"><process id="oa_asset_receive" name="资产领用审批"><startEvent id="start"/><sequenceFlow id="f1" sourceRef="start" targetRef="approve"/><userTask id="approve" name="审批" flowable:assignee="${approvalAssignee}"/><sequenceFlow id="f2" sourceRef="approve" targetRef="end"/><endEvent id="end"/></process></definitions>', '{}', 'OA 审批流程初始版本', 'admin', NOW(), 'admin', NOW(), ''),
(10004, '1.0', '初始版本', 1, NOW(), 'admin', '<definitions xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL" xmlns:flowable="http://flowable.org/bpmn"><process id="oa_asset_transfer" name="资产调拨审批"><startEvent id="start"/><sequenceFlow id="f1" sourceRef="start" targetRef="approve"/><userTask id="approve" name="审批" flowable:assignee="${approvalAssignee}"/><sequenceFlow id="f2" sourceRef="approve" targetRef="end"/><endEvent id="end"/></process></definitions>', '{}', 'OA 审批流程初始版本', 'admin', NOW(), 'admin', NOW(), ''),
(10005, '1.0', '初始版本', 1, NOW(), 'admin', '<definitions xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL" xmlns:flowable="http://flowable.org/bpmn"><process id="oa_asset_repair" name="资产维修审批"><startEvent id="start"/><sequenceFlow id="f1" sourceRef="start" targetRef="approve"/><userTask id="approve" name="审批" flowable:assignee="${approvalAssignee}"/><sequenceFlow id="f2" sourceRef="approve" targetRef="end"/><endEvent id="end"/></process></definitions>', '{}', 'OA 审批流程初始版本', 'admin', NOW(), 'admin', NOW(), ''),
(10006, '1.0', '初始版本', 1, NOW(), 'admin', '<definitions xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL" xmlns:flowable="http://flowable.org/bpmn"><process id="oa_asset_scrap" name="资产报废审批"><startEvent id="start"/><sequenceFlow id="f1" sourceRef="start" targetRef="approve"/><userTask id="approve" name="审批" flowable:assignee="${approvalAssignee}"/><sequenceFlow id="f2" sourceRef="approve" targetRef="end"/><endEvent id="end"/></process></definitions>', '{}', 'OA 审批流程初始版本', 'admin', NOW(), 'admin', NOW(), ''),
(10007, '1.0', '初始版本', 1, NOW(), 'admin', '<definitions xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL" xmlns:flowable="http://flowable.org/bpmn"><process id="oa_attendance_leave" name="请假审批"><startEvent id="start"/><sequenceFlow id="f1" sourceRef="start" targetRef="approve"/><userTask id="approve" name="审批" flowable:assignee="${approvalAssignee}"/><sequenceFlow id="f2" sourceRef="approve" targetRef="end"/><endEvent id="end"/></process></definitions>', '{}', 'OA 审批流程初始版本', 'admin', NOW(), 'admin', NOW(), ''),
(10008, '1.0', '初始版本', 1, NOW(), 'admin', '<definitions xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL" xmlns:flowable="http://flowable.org/bpmn"><process id="oa_attendance_overtime" name="加班审批"><startEvent id="start"/><sequenceFlow id="f1" sourceRef="start" targetRef="approve"/><userTask id="approve" name="审批" flowable:assignee="${approvalAssignee}"/><sequenceFlow id="f2" sourceRef="approve" targetRef="end"/><endEvent id="end"/></process></definitions>', '{}', 'OA 审批流程初始版本', 'admin', NOW(), 'admin', NOW(), ''),
(10009, '1.0', '初始版本', 1, NOW(), 'admin', '<definitions xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL" xmlns:flowable="http://flowable.org/bpmn"><process id="oa_attendance_trip" name="出差审批"><startEvent id="start"/><sequenceFlow id="f1" sourceRef="start" targetRef="approve"/><userTask id="approve" name="审批" flowable:assignee="${approvalAssignee}"/><sequenceFlow id="f2" sourceRef="approve" targetRef="end"/><endEvent id="end"/></process></definitions>', '{}', 'OA 审批流程初始版本', 'admin', NOW(), 'admin', NOW(), ''),
(10010, '1.0', '初始版本', 1, NOW(), 'admin', '<definitions xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL" xmlns:flowable="http://flowable.org/bpmn"><process id="oa_attendance_makeup" name="补卡审批"><startEvent id="start"/><sequenceFlow id="f1" sourceRef="start" targetRef="approve"/><userTask id="approve" name="审批" flowable:assignee="${approvalAssignee}"/><sequenceFlow id="f2" sourceRef="approve" targetRef="end"/><endEvent id="end"/></process></definitions>', '{}', 'OA 审批流程初始版本', 'admin', NOW(), 'admin', NOW(), '');
