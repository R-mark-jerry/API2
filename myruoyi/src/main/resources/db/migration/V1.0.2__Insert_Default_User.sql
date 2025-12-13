-- ----------------------------
-- 插入默认用户数据
-- ----------------------------

-- 插入默认部门
INSERT INTO `sys_dept` VALUES 
(100,  0,   '0',          '总公司',   0, '若依', '15888888888', 'ry@qq.com', '0', 'admin', sysdate(), '', NULL, 0);

-- 插入默认角色
INSERT INTO `sys_role` VALUES 
(1, '超级管理员', 'admin',  1, '1', 1, 1, '0', 'admin', sysdate(), '', NULL, '超级管理员', 0),
(2, '普通角色',   'common', 2, '2', 1, 1, '0', 'admin', sysdate(), '', NULL, '普通角色', 0);

-- 插入默认用户（密码：admin123）
INSERT INTO `sys_user` VALUES 
(1,  'admin', '管理员', '00', 'admin@myruoyi.com', '15888888888', '1', '', '$2a$10$7JB720yubVSOfvVWbfXCOOxjTOQcQjmrJF1ZM4nAVccp/.rkMlDWy', '0', '127.0.0.1', sysdate(), 'admin', sysdate(), '', NULL, '管理员', 0);

-- 插入用户角色关联
INSERT INTO `sys_user_role` VALUES (1, 1);

-- 插入角色菜单关联（给超级管理员分配所有菜单权限）
-- 这里先不插入具体菜单，等菜单数据初始化后再分配