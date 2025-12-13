-- ----------------------------
-- 更新管理员密码为正确的BCrypt加密格式
-- ----------------------------

-- 更新管理员密码（admin123的BCrypt加密）
UPDATE `sys_user` SET `password` = '$2a$10$RGiXu0k2W5ovXLAM5xxnv.mLlyN/ccy1BG5WnCw9PYrlEOfPCtaKe' WHERE `user_name` = 'admin';