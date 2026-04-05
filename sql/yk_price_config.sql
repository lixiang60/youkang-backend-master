-- 价格配置表
-- 用于存储基础价格模板和课题组个性化价格配置

DROP TABLE IF EXISTS yk_price_config;

CREATE TABLE yk_price_config (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    group_id INT COMMENT '课题组ID（NULL = 基础模板）',
    category VARCHAR(50) COMMENT '类别',
    charge_name VARCHAR(100) COMMENT '收费名称',
    sample_type VARCHAR(50) COMMENT '样品类型',
    project VARCHAR(50) COMMENT '测序项目',
    plasmid_length_min INT COMMENT '质粒长度下限',
    plasmid_length_max INT COMMENT '质粒长度上限（NULL = 无上限）',
    fragment_size_min INT COMMENT '片段大小下限',
    fragment_size_max INT COMMENT '片段大小上限（NULL = 无上限）',
    unit_price DECIMAL(10,2) COMMENT '单价',
    calc_method VARCHAR(50) COMMENT '计算方式',
    status TINYINT DEFAULT 1 COMMENT '状态（1启用 0禁用）',
    create_by VARCHAR(64) COMMENT '创建人',
    create_time DATETIME COMMENT '创建时间',
    update_by VARCHAR(64) COMMENT '更新人',
    update_time DATETIME COMMENT '更新时间',
    remark VARCHAR(500) COMMENT '备注'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='价格配置表';

-- 创建索引
CREATE INDEX idx_price_group ON yk_price_config(group_id);
CREATE INDEX idx_price_sample_project ON yk_price_config(sample_type, project);
