-- ----------------------------
-- 1、用户表
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `user_id`     bigint(20)      NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `user_name`   varchar(30)     NOT NULL                COMMENT '用户账号',
  `nick_name`   varchar(30)     NOT NULL                COMMENT '用户昵称',
  `user_type`   varchar(2)      DEFAULT '00'            COMMENT '用户类型（00系统用户）',
  `email`       varchar(50)     DEFAULT ''              COMMENT '用户邮箱',
  `phonenumber` varchar(11)     DEFAULT ''              COMMENT '手机号码',
  `sex`         char(1)         DEFAULT '0'             COMMENT '用户性别（0男 1女 2未知）',
  `avatar`      varchar(100)    DEFAULT ''              COMMENT '头像地址',
  `password`    varchar(100)    DEFAULT ''              COMMENT '密码',
  `status`      char(1)         DEFAULT '0'             COMMENT '帐号状态（0正常 1停用）',
  `login_ip`    varchar(128)    DEFAULT ''              COMMENT '最后登录IP',
  `login_date`  datetime                                 COMMENT '最后登录时间',
  `create_by`   varchar(64)     DEFAULT ''              COMMENT '创建者',
  `create_time` datetime                                 COMMENT '创建时间',
  `update_by`   varchar(64)     DEFAULT ''              COMMENT '更新者',
  `update_time` datetime                                 COMMENT '更新时间',
  `remark`      varchar(500)    DEFAULT NULL            COMMENT '备注',
  `deleted`     tinyint(1)      DEFAULT 0               COMMENT '删除标志（0代表存在 1代表删除）',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COMMENT='用户信息表';

-- ----------------------------
-- 2、角色表
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `role_id`              bigint(20)      NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name`            varchar(30)     NOT NULL                COMMENT '角色名称',
  `role_key`             varchar(100)    NOT NULL                COMMENT '角色权限字符串',
  `role_sort`            int(4)          NOT NULL                COMMENT '显示顺序',
  `data_scope`           char(1)         DEFAULT '1'             COMMENT '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
  `menu_check_strictly`  tinyint(1)      DEFAULT 1               COMMENT '菜单树选择项是否关联显示',
  `dept_check_strictly`  tinyint(1)      DEFAULT 1               COMMENT '部门树选择项是否关联显示',
  `status`               char(1)         NOT NULL                COMMENT '角色状态（0正常 1停用）',
  `create_by`            varchar(64)     DEFAULT ''              COMMENT '创建者',
  `create_time`          datetime                                 COMMENT '创建时间',
  `update_by`            varchar(64)     DEFAULT ''              COMMENT '更新者',
  `update_time`          datetime                                 COMMENT '更新时间',
  `remark`               varchar(500)    DEFAULT NULL            COMMENT '备注',
  `deleted`              tinyint(1)      DEFAULT 0               COMMENT '删除标志（0代表存在 1代表删除）',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COMMENT='角色信息表';

-- ----------------------------
-- 3、菜单权限表
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `menu_id`     bigint(20)      NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name`   varchar(50)     NOT NULL                COMMENT '菜单名称',
  `parent_id`   bigint(20)      DEFAULT 0               COMMENT '父菜单ID',
  `order_num`   int(4)          DEFAULT 0               COMMENT '显示顺序',
  `path`        varchar(200)    DEFAULT ''              COMMENT '路由地址',
  `component`   varchar(255)    DEFAULT NULL            COMMENT '组件路径',
  `query_param` varchar(255)    DEFAULT NULL            COMMENT '路由参数',
  `is_frame`    int(1)          DEFAULT 1               COMMENT '是否为外链（0是 1否）',
  `is_cache`    int(1)          DEFAULT 0               COMMENT '是否缓存（0缓存 1不缓存）',
  `menu_type`   char(1)         DEFAULT ''              COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `visible`     int(1)          DEFAULT 0               COMMENT '显示状态（0显示 1隐藏）',
  `status`      int(1)          DEFAULT 0               COMMENT '菜单状态（0正常 1停用）',
  `perms`       varchar(100)    DEFAULT NULL            COMMENT '权限标识',
  `icon`        varchar(100)    DEFAULT '#'             COMMENT '菜单图标',
  `create_by`   varchar(64)     DEFAULT ''              COMMENT '创建者',
  `create_time` datetime                                 COMMENT '创建时间',
  `update_by`   varchar(64)     DEFAULT ''              COMMENT '更新者',
  `update_time` datetime                                 COMMENT '更新时间',
  `remark`      varchar(500)    DEFAULT ''              COMMENT '备注',
  `deleted`     tinyint(1)      DEFAULT 0               COMMENT '删除标志（0代表存在 1代表删除）',
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2000 DEFAULT CHARSET=utf8mb4 COMMENT='菜单权限表';

-- ----------------------------
-- 4、部门表
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept` (
  `dept_id`     bigint(20)      NOT NULL AUTO_INCREMENT COMMENT '部门id',
  `parent_id`   bigint(20)      DEFAULT 0               COMMENT '父部门id',
  `ancestors`   varchar(500)    DEFAULT ''              COMMENT '祖级列表',
  `dept_name`   varchar(30)     DEFAULT ''              COMMENT '部门名称',
  `order_num`   int(4)          DEFAULT 0               COMMENT '显示顺序',
  `leader`      varchar(20)     DEFAULT NULL            COMMENT '负责人',
  `phone`       varchar(11)     DEFAULT NULL            COMMENT '联系电话',
  `email`       varchar(50)     DEFAULT NULL            COMMENT '邮箱',
  `status`      char(1)         DEFAULT '0'             COMMENT '部门状态（0正常 1停用）',
  `create_by`   varchar(64)     DEFAULT ''              COMMENT '创建者',
  `create_time` datetime                                 COMMENT '创建时间',
  `update_by`   varchar(64)     DEFAULT ''              COMMENT '更新者',
  `update_time` datetime                                 COMMENT '更新时间',
  `deleted`     tinyint(1)      DEFAULT 0               COMMENT '删除标志（0代表存在 1代表删除）',
  PRIMARY KEY (`dept_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COMMENT='部门表';

-- ----------------------------
-- 5、用户和角色关联表
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户和角色关联表';

-- ----------------------------
-- 6、角色和菜单关联表
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `menu_id` bigint(20) NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`,`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色和菜单关联表';

-- ----------------------------
-- 7、角色和部门关联表
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_dept`;
CREATE TABLE `sys_role_dept` (
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `dept_id` bigint(20) NOT NULL COMMENT '部门ID',
  PRIMARY KEY (`role_id`,`dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色和部门关联表';

-- ----------------------------
-- 8、操作日志记录
-- ----------------------------
DROP TABLE IF EXISTS `sys_oper_log`;
CREATE TABLE `sys_oper_log` (
  `oper_id`        bigint(20)      NOT NULL AUTO_INCREMENT COMMENT '日志主键',
  `title`          varchar(50)     DEFAULT ''              COMMENT '模块标题',
  `business_type`  int(2)          DEFAULT 0               COMMENT '业务类型（0其它 1新增 2修改 3删除）',
  `method`         varchar(100)    DEFAULT ''              COMMENT '方法名称',
  `request_method` varchar(10)     DEFAULT ''              COMMENT '请求方式',
  `operator_type`  int(1)          DEFAULT 0               COMMENT '操作类别（0其它 1后台用户 2手机端用户）',
  `oper_name`      varchar(50)     DEFAULT ''              COMMENT '操作人员',
  `dept_name`      varchar(50)     DEFAULT ''              COMMENT '部门名称',
  `oper_url`       varchar(255)    DEFAULT ''              COMMENT '请求URL',
  `oper_ip`        varchar(128)    DEFAULT ''              COMMENT '主机地址',
  `oper_location`  varchar(255)    DEFAULT ''              COMMENT '操作地点',
  `oper_param`     varchar(2000)   DEFAULT ''              COMMENT '请求参数',
  `json_result`    varchar(2000)   DEFAULT ''              COMMENT '返回参数',
  `status`         int(1)          DEFAULT 0               COMMENT '操作状态（0正常 1异常）',
  `error_msg`      varchar(2000)   DEFAULT ''              COMMENT '错误消息',
  `oper_time`      datetime                                 COMMENT '操作时间',
  `cost_time`      bigint(20)      DEFAULT 0               COMMENT '消耗时间',
  PRIMARY KEY (`oper_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COMMENT='操作日志记录';

-- ----------------------------
-- 9、系统访问记录
-- ----------------------------
DROP TABLE IF EXISTS `sys_logininfor`;
CREATE TABLE `sys_logininfor` (
  `info_id`        bigint(20)      NOT NULL AUTO_INCREMENT COMMENT '访问ID',
  `user_name`      varchar(50)     DEFAULT ''              COMMENT '用户账号',
  `ipaddr`         varchar(128)    DEFAULT ''              COMMENT '登录IP地址',
  `login_location` varchar(255)    DEFAULT ''              COMMENT '登录地点',
  `browser`        varchar(50)     DEFAULT ''              COMMENT '浏览器类型',
  `os`             varchar(50)     DEFAULT ''              COMMENT '操作系统',
  `status`         char(1)         DEFAULT '0'             COMMENT '登录状态（0成功 1失败）',
  `msg`            varchar(255)    DEFAULT ''              COMMENT '提示消息',
  `login_time`     datetime                                 COMMENT '登录时间',
  PRIMARY KEY (`info_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COMMENT='系统访问记录';