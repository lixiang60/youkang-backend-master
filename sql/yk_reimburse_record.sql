-- 返还记录表
CREATE TABLE `yk_reimburse_record` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `customer_name` VARCHAR(100) DEFAULT NULL COMMENT '客户姓名',
    `order_id` VARCHAR(50) DEFAULT NULL COMMENT '订单号',
    `schedule_time` DATETIME DEFAULT NULL COMMENT '返还安排时间',
    `scheduler` VARCHAR(64) DEFAULT NULL COMMENT '安排人',
    `reimburse_type` VARCHAR(50) DEFAULT NULL COMMENT '返还类型',
    `reimburse_count` INT DEFAULT NULL COMMENT '返还数量',
    `produce_ids` TEXT DEFAULT NULL COMMENT '生产编号集（逗号分隔）',
    `status` VARCHAR(20) DEFAULT '待返还' COMMENT '状态：待返还/已返还',
    `reimburse_time` DATETIME DEFAULT NULL COMMENT '返还时间',
    `reimburser` VARCHAR(64) DEFAULT NULL COMMENT '返还人',
    `belong_company` VARCHAR(100) DEFAULT NULL COMMENT '所属公司',
    `produce_company` VARCHAR(100) DEFAULT NULL COMMENT '生产公司',
    `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_user` VARCHAR(64) DEFAULT NULL COMMENT '创建人',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_user` VARCHAR(64) DEFAULT NULL COMMENT '更新人',
    `update_time` DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_order_id` (`order_id`),
    KEY `idx_status` (`status`),
    KEY `idx_schedule_time` (`schedule_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='返还记录表';

-- 样品信息表添加返还状态字段
ALTER TABLE `yk_sample_info` ADD COLUMN `reimburse_status` VARCHAR(20) DEFAULT NULL COMMENT '返还状态' AFTER `report_status`;
