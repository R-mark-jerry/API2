-- 修复admin用户密码问题
-- 解决密码字段格式问题

-- 修复admin用户密码
UPDATE sys_user 
SET password = '$2a$10$7JB720yubVSOfvVWbfXCOOxjTOQcQjmrJF1ZM4nAVccp/.rkMlDW'
WHERE user_name = 'admin';

-- 验证修复结果
SELECT user_id, user_name, password, status FROM sys_user WHERE user_name = 'admin';