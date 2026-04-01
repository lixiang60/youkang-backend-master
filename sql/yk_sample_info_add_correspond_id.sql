-- ============================================
-- 样品信息表新增样品对应号字段
-- 执行时间: 2026-04-01
-- ============================================

ALTER TABLE yk_sample_info
ADD COLUMN sample_correspond_id BIGINT NULL COMMENT '样品对应号' AFTER reimburse_status;

-- 为新字段添加索引（可选，如果需要根据样品对应号查询）
-- ALTER TABLE yk_sample_info ADD INDEX idx_sample_correspond_id (sample_correspond_id);
