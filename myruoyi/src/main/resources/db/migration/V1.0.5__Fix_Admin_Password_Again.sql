-- 修复admin用户密码问题
-- 更新为正确的BCrypt密码哈希

-- 修复admin用户密码为123456的正确BCrypt哈希
UPDATE sys_user 
SET password = '$2a$10$s2Zu/J.4SgqFRzxK1.oT9O0RjLQWhTIDd//JFZit/7JEyCSf/z.nG'
WHERE user_name = 'admin';

-- 验证修复结果
SELECT user_id, user_name, password, status, deleted FROM sys_user WHERE user_name = 'admin';