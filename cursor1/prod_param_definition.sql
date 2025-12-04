-- 自定义指标参数表
-- 用于存储生产系统中的自定义参数配置
CREATE TABLE prod_param_definition (
    id BIGSERIAL PRIMARY KEY,                    -- 主键，唯一标识
    param_key VARCHAR(255) NOT NULL,             -- 参数标识（英文），如：volume
    param_name VARCHAR(255) NOT NULL,            -- 参数中文名称，如：矿卡容量
    param_value TEXT NOT NULL,                   -- 参数默认值
    param_unit VARCHAR(255) NOT NULL,            -- 参数单位，m³、吨等
    description TEXT,                             -- 参数说明
    scope INTEGER NOT NULL,                      -- 自定义参数作用范围：1对应生产记录；2对应生产报表
    enabled INTEGER NOT NULL DEFAULT 1,          -- 参数是否启用：0对应不启用；1对应启用
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 创建时间
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP   -- 更新时间
);

-- 添加表注释
COMMENT ON TABLE prod_param_definition IS '自定义指标参数表';

-- 添加字段注释
COMMENT ON COLUMN prod_param_definition.id IS '主键，唯一标识';
COMMENT ON COLUMN prod_param_definition.param_key IS '参数标识（英文），如：volume';
COMMENT ON COLUMN prod_param_definition.param_name IS '参数中文名称，如：矿卡容量';
COMMENT ON COLUMN prod_param_definition.param_value IS '参数默认值';
COMMENT ON COLUMN prod_param_definition.param_unit IS '参数单位，m³、吨等';
COMMENT ON COLUMN prod_param_definition.description IS '参数说明';
COMMENT ON COLUMN prod_param_definition.scope IS '自定义参数作用范围：1对应生产记录；2对应生产报表';
COMMENT ON COLUMN prod_param_definition.enabled IS '参数是否启用：0对应不启用；1对应启用';
COMMENT ON COLUMN prod_param_definition.created_at IS '创建时间';
COMMENT ON COLUMN prod_param_definition.updated_at IS '更新时间';

-- 创建唯一索引，确保param_key的唯一性
CREATE UNIQUE INDEX idx_prod_param_definition_param_key ON prod_param_definition(param_key);

-- 创建复合索引，优化按scope和enabled的查询
CREATE INDEX idx_prod_param_definition_scope_enabled ON prod_param_definition(scope, enabled);

-- 创建更新时间触发器函数
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- 创建触发器，自动更新updated_at字段
CREATE TRIGGER update_prod_param_definition_updated_at 
    BEFORE UPDATE ON prod_param_definition 
    FOR EACH ROW 
    EXECUTE FUNCTION update_updated_at_column();

-- 插入示例数据
INSERT INTO prod_param_definition (param_key, param_name, param_value, param_unit, description, scope, enabled) VALUES
('volume', '矿卡容量', '50', 'm³', '单次运输的矿卡容量', 1, 1),
('weight', '载重', '80', '吨', '矿卡最大载重', 1, 1),
('speed', '运输速度', '30', 'km/h', '矿卡平均运输速度', 1, 1),
('fuel_consumption', '油耗', '25', 'L/100km', '矿卡百公里油耗', 1, 1),
('daily_output', '日产量', '1000', '吨', '每日生产目标产量', 2, 1),
('monthly_target', '月目标', '30000', '吨', '月度生产目标', 2, 1),
('efficiency_rate', '效率', '85', '%', '生产效率指标', 2, 1),
('safety_index', '安全指数', '95', '分', '安全生产指数', 2, 1);

-- 查询示例
-- 查询所有启用的生产记录参数
SELECT * FROM prod_param_definition WHERE scope = 1 AND enabled = 1;

-- 查询所有启用的生产报表参数
SELECT * FROM prod_param_definition WHERE scope = 2 AND enabled = 1;

-- 根据参数键查询
SELECT * FROM prod_param_definition WHERE param_key = 'volume';

