-- 为API信息表添加负责人字段
ALTER TABLE api_info ADD COLUMN responsible_user_id BIGINT COMMENT '负责人ID';
ALTER TABLE api_info ADD COLUMN responsible_user_name VARCHAR(100) COMMENT '负责人姓名';

-- 如果需要，可以添加索引
CREATE INDEX idx_api_info_responsible_user_id ON api_info(responsible_user_id);