-- 为API模块表添加负责人字段
ALTER TABLE api_module ADD COLUMN responsible_user_id BIGINT COMMENT '负责人ID';
ALTER TABLE api_module ADD COLUMN responsible_user_name VARCHAR(100) COMMENT '负责人姓名';

-- 为API权限表添加负责人字段
ALTER TABLE api_permission ADD COLUMN responsible_user_id BIGINT COMMENT '负责人ID';
ALTER TABLE api_permission ADD COLUMN responsible_user_name VARCHAR(100) COMMENT '负责人姓名';

-- 如果需要，可以添加索引
CREATE INDEX idx_api_module_responsible_user_id ON api_module(responsible_user_id);
CREATE INDEX idx_api_permission_responsible_user_id ON api_permission(responsible_user_id);