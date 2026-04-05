-- 订单状态流转相关表结构和定时任务配置

-- 1. 订单表新增状态字段和状态变更时间
ALTER TABLE yk_order_info ADD COLUMN order_status VARCHAR(20) NOT NULL DEFAULT '订单生成' COMMENT '订单状态：订单生成、订单出库、订单完成';
ALTER TABLE yk_order_info ADD COLUMN status_time DATETIME DEFAULT NULL COMMENT '订单状态变更时间';

-- 2. 注册定时任务：订单状态流转（订单生成 -> 订单出库）
-- 每10分钟扫描一次，检查订单下所有样品是否已完成
INSERT INTO sys_job (job_name, job_group, invoke_target, cron_expression, misfire_policy, concurrent, status, create_by, create_time)
VALUES ('订单状态流转-出库检查', 'DEFAULT', 'orderStatusTask.checkOrderOutbound()', '0 0/10 * * * ?', '1', '1', '0', 'admin', NOW());

-- 3. 注册定时任务：订单出库超时自动完成（订单出库 -> 订单完成）
-- 每10分钟扫描一次，出库超过4小时自动完成
INSERT INTO sys_job (job_name, job_group, invoke_target, cron_expression, misfire_policy, concurrent, status, create_by, create_time)
VALUES ('订单状态流转-自动完成', 'DEFAULT', 'orderStatusTask.checkOrderComplete()', '0 0/10 * * * ?', '1', '1', '0', 'admin', NOW());
