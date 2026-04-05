-- 课题组测序单价表
-- 用于存储课题组的基础测序单价，当价格配置模板中 unit_price 为空时，从此表兜底取值
CREATE TABLE IF NOT EXISTS yk_unit_price (
    group_id   INT NOT NULL COMMENT '课题组ID（主键）',
    unit_price DECIMAL(10,2) NOT NULL COMMENT '测序单价',
    create_by  VARCHAR(64) DEFAULT NULL COMMENT '创建人',
    create_time DATETIME DEFAULT NULL COMMENT '创建时间',
    update_by  VARCHAR(64) DEFAULT NULL COMMENT '更新人',
    update_time DATETIME DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (group_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课题组测序单价表';
