-- 修复用户表中deleted字段为null的问题
UPDATE sys_user SET deleted = 0 WHERE deleted IS NULL;

-- 修复角色表中deleted字段为null的问题
UPDATE sys_role SET deleted = 0 WHERE deleted IS NULL;

-- 修复菜单表中deleted字段为null的问题
UPDATE sys_menu SET deleted = 0 WHERE deleted IS NULL;

-- 修复部门表中deleted字段为null的问题
UPDATE sys_dept SET deleted = 0 WHERE deleted IS NULL;