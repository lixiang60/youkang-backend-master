-- 样品流程流转日志表
CREATE TABLE `yk_sample_flow_log` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `produce_id` BIGINT NOT NULL COMMENT '生产编号',
    `operation` VARCHAR(50) NOT NULL COMMENT '操作类型(添加模板板号/添加模板孔号/流程流转/设置状态/设置原浓度/退回等)',
    `flow_name` VARCHAR(50) DEFAULT NULL COMMENT '流程名称(模板排版/模板生产/模板成功/模板邮件/反应生产等)',
    `plate_no` VARCHAR(50) DEFAULT NULL COMMENT '板号',
    `hole_no` VARCHAR(20) DEFAULT NULL COMMENT '孔号',
    `template_plate_no` VARCHAR(50) DEFAULT NULL COMMENT '模板板号',
    `template_hole_no` VARCHAR(20) DEFAULT NULL COMMENT '模板孔号',
    `layout` VARCHAR(20) DEFAULT NULL COMMENT '排版方式(横排/竖排)',
    `hole_number` INT DEFAULT NULL COMMENT '孔号数',
    `origin_concentration` VARCHAR(50) DEFAULT NULL COMMENT '原浓度',
    `return_state` VARCHAR(50) DEFAULT NULL COMMENT '返回状态',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `operator` VARCHAR(64) DEFAULT NULL COMMENT '操作人',
    `operate_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    `before_flow_name` VARCHAR(50) DEFAULT NULL COMMENT '流转前流程名称',
    PRIMARY KEY (`id`),
    KEY `idx_produce_id` (`produce_id`),
    KEY `idx_operate_time` (`operate_time`),
    KEY `idx_flow_name` (`flow_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='样品流程流转日志表';
