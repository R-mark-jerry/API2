-- ----------------------------
-- 10、API应用表
-- ----------------------------
DROP TABLE IF EXISTS `api_app`;
CREATE TABLE `api_app` (
  `app_id`       bigint(20)      NOT NULL AUTO_INCREMENT COMMENT '应用ID',
  `app_code`     varchar(50)     NOT NULL                COMMENT '应用编码',
  `app_name`     varchar(100)    NOT NULL                COMMENT '应用名称',
  `app_desc`     varchar(500)    DEFAULT ''              COMMENT '应用描述',
  `app_version`  varchar(20)     DEFAULT '1.0.0'         COMMENT '应用版本',
  `owner_id`     bigint(20)      NOT NULL                COMMENT '负责人ID',
  `owner_name`   varchar(50)     NOT NULL                COMMENT '负责人姓名',
  `status`       char(1)         DEFAULT '0'             COMMENT '应用状态（0正常 1停用）',
  `create_by`    varchar(64)     DEFAULT ''              COMMENT '创建者',
  `create_time`  datetime                                 COMMENT '创建时间',
  `update_by`    varchar(64)     DEFAULT ''              COMMENT '更新者',
  `update_time`  datetime                                 COMMENT '更新时间',
  `remark`       varchar(500)    DEFAULT NULL            COMMENT '备注',
  `deleted`      tinyint(1)      DEFAULT 0               COMMENT '删除标志（0代表存在 1代表删除）',
  PRIMARY KEY (`app_id`),
  UNIQUE KEY `uk_app_code` (`app_code`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COMMENT='API应用表';

-- ----------------------------
-- 11、API模块表
-- ----------------------------
DROP TABLE IF EXISTS `api_module`;
CREATE TABLE `api_module` (
  `module_id`    bigint(20)      NOT NULL AUTO_INCREMENT COMMENT '模块ID',
  `app_id`       bigint(20)      NOT NULL                COMMENT '应用ID',
  `module_code`  varchar(50)     NOT NULL                COMMENT '模块编码',
  `module_name`  varchar(100)    NOT NULL                COMMENT '模块名称',
  `module_desc`  varchar(500)    DEFAULT ''              COMMENT '模块描述',
  `parent_id`    bigint(20)      DEFAULT 0               COMMENT '父模块ID',
  `order_num`    int(4)          DEFAULT 0               COMMENT '显示顺序',
  `create_by`    varchar(64)     DEFAULT ''              COMMENT '创建者',
  `create_time`  datetime                                 COMMENT '创建时间',
  `update_by`    varchar(64)     DEFAULT ''              COMMENT '更新者',
  `update_time`  datetime                                 COMMENT '更新时间',
  `deleted`      tinyint(1)      DEFAULT 0               COMMENT '删除标志（0代表存在 1代表删除）',
  PRIMARY KEY (`module_id`),
  KEY `idx_app_id` (`app_id`),
  UNIQUE KEY `uk_app_module_code` (`app_id`, `module_code`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COMMENT='API模块表';

-- ----------------------------
-- 12、API接口表
-- ----------------------------
DROP TABLE IF EXISTS `api_info`;
CREATE TABLE `api_info` (
  `api_id`           bigint(20)      NOT NULL AUTO_INCREMENT COMMENT 'API ID',
  `app_id`           bigint(20)      NOT NULL                COMMENT '应用ID',
  `module_id`        bigint(20)      NOT NULL                COMMENT '模块ID',
  `api_code`         varchar(100)    NOT NULL                COMMENT 'API编码',
  `api_name`         varchar(200)    NOT NULL                COMMENT 'API名称',
  `api_desc`         varchar(500)    DEFAULT ''              COMMENT 'API描述',
  `request_method`   varchar(10)     NOT NULL                COMMENT '请求方法（GET/POST/PUT/DELETE等）',
  `request_url`      varchar(500)    NOT NULL                COMMENT '请求URL',
  `content_type`     varchar(50)     DEFAULT 'application/json' COMMENT '内容类型',
  `request_params`   text                                     COMMENT '请求参数（JSON格式）',
  `response_params`  text                                     COMMENT '响应参数（JSON格式）',
  `request_example`  text                                     COMMENT '请求示例（JSON格式）',
  `response_example` text                                     COMMENT '响应示例（JSON格式）',
  `api_status`       char(1)         DEFAULT '0'             COMMENT 'API状态（0开发中 1测试中 2已发布 3已下线）',
  `publish_status`   char(1)         DEFAULT '0'             COMMENT '发布状态（0未发布 1已发布 2灰度发布）',
  `owner_id`         bigint(20)      NOT NULL                COMMENT '负责人ID',
  `owner_name`       varchar(50)     NOT NULL                COMMENT '负责人姓名',
  `create_by`        varchar(64)     DEFAULT ''              COMMENT '创建者',
  `create_time`      datetime                                 COMMENT '创建时间',
  `update_by`        varchar(64)     DEFAULT ''              COMMENT '更新者',
  `update_time`      datetime                                 COMMENT '更新时间',
  `remark`           varchar(500)    DEFAULT NULL            COMMENT '备注',
  `deleted`          tinyint(1)      DEFAULT 0               COMMENT '删除标志（0代表存在 1代表删除）',
  PRIMARY KEY (`api_id`),
  KEY `idx_app_id` (`app_id`),
  KEY `idx_module_id` (`module_id`),
  KEY `idx_api_code` (`api_code`),
  UNIQUE KEY `uk_app_api_code` (`app_id`, `api_code`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COMMENT='API接口表';

-- ----------------------------
-- 13、API权限表
-- ----------------------------
DROP TABLE IF EXISTS `api_permission`;
CREATE TABLE `api_permission` (
  `permission_id`    bigint(20)      NOT NULL AUTO_INCREMENT COMMENT '权限ID',
  `api_id`           bigint(20)      NOT NULL                COMMENT 'API ID',
  `permission_type`  char(1)         NOT NULL                COMMENT '权限类型（1角色 2用户）',
  `permission_target` bigint(20)    NOT NULL                COMMENT '权限目标（角色ID或用户ID）',
  `permission_scope` char(1)         DEFAULT '1'             COMMENT '权限范围（1调用 2调试 3管理）',
  `create_by`        varchar(64)     DEFAULT ''              COMMENT '创建者',
  `create_time`      datetime                                 COMMENT '创建时间',
  `update_by`        varchar(64)     DEFAULT ''              COMMENT '更新者',
  `update_time`      datetime                                 COMMENT '更新时间',
  `remark`           varchar(500)    DEFAULT NULL            COMMENT '备注',
  PRIMARY KEY (`permission_id`),
  KEY `idx_api_id` (`api_id`),
  KEY `idx_permission_target` (`permission_type`, `permission_target`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COMMENT='API权限表';

-- ----------------------------
-- 14、API调用记录表
-- ----------------------------
DROP TABLE IF EXISTS `api_call_log`;
CREATE TABLE `api_call_log` (
  `call_id`          bigint(20)      NOT NULL AUTO_INCREMENT COMMENT '调用记录ID',
  `api_id`           bigint(20)      NOT NULL                COMMENT 'API ID',
  `api_code`         varchar(100)    NOT NULL                COMMENT 'API编码',
  `call_user_id`     bigint(20)                              COMMENT '调用用户ID',
  `call_user_name`   varchar(50)                              COMMENT '调用用户名',
  `call_ip`          varchar(128)    DEFAULT ''              COMMENT '调用IP',
  `request_method`   varchar(10)     NOT NULL                COMMENT '请求方法',
  `request_url`      varchar(500)    NOT NULL                COMMENT '请求URL',
  `request_params`   text                                     COMMENT '请求参数',
  `response_status`  int(3)          NOT NULL                COMMENT '响应状态码',
  `response_data`    text                                     COMMENT '响应数据',
  `response_time`    bigint(20)      DEFAULT 0               COMMENT '响应时间（毫秒）',
  `call_result`      char(1)         DEFAULT '0'             COMMENT '调用结果（0成功 1失败）',
  `error_message`   varchar(1000)   DEFAULT ''              COMMENT '错误信息',
  `call_time`        datetime                                 COMMENT '调用时间',
  PRIMARY KEY (`call_id`),
  KEY `idx_api_id` (`api_id`),
  KEY `idx_call_user_id` (`call_user_id`),
  KEY `idx_call_time` (`call_time`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COMMENT='API调用记录表';

-- ----------------------------
-- 15、API统计表
-- ----------------------------
DROP TABLE IF EXISTS `api_statistics`;
CREATE TABLE `api_statistics` (
  `stat_id`          bigint(20)      NOT NULL AUTO_INCREMENT COMMENT '统计ID',
  `api_id`           bigint(20)      NOT NULL                COMMENT 'API ID',
  `api_code`         varchar(100)    NOT NULL                COMMENT 'API编码',
  `stat_date`        date            NOT NULL                COMMENT '统计日期',
  `total_calls`      bigint(20)      DEFAULT 0               COMMENT '总调用次数',
  `success_calls`    bigint(20)      DEFAULT 0               COMMENT '成功调用次数',
  `fail_calls`       bigint(20)      DEFAULT 0               COMMENT '失败调用次数',
  `avg_response_time` decimal(10,2)  DEFAULT 0.00           COMMENT '平均响应时间（毫秒）',
  `max_response_time` bigint(20)      DEFAULT 0               COMMENT '最大响应时间（毫秒）',
  `min_response_time` bigint(20)      DEFAULT 0               COMMENT '最小响应时间（毫秒）',
  `create_time`      datetime                                 COMMENT '创建时间',
  `update_time`      datetime                                 COMMENT '更新时间',
  PRIMARY KEY (`stat_id`),
  UNIQUE KEY `uk_api_stat_date` (`api_id`, `stat_date`),
  KEY `idx_stat_date` (`stat_date`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COMMENT='API统计表';

-- ----------------------------
-- 16、API发布记录表
-- ----------------------------
DROP TABLE IF EXISTS `api_publish_record`;
CREATE TABLE `api_publish_record` (
  `publish_id`       bigint(20)      NOT NULL AUTO_INCREMENT COMMENT '发布记录ID',
  `api_id`           bigint(20)      NOT NULL                COMMENT 'API ID',
  `api_code`         varchar(100)    NOT NULL                COMMENT 'API编码',
  `publish_type`     char(1)         NOT NULL                COMMENT '发布类型（1正式发布 2灰度发布）',
  `publish_status`   char(1)         DEFAULT '0'             COMMENT '发布状态（0发布中 1发布成功 2发布失败）',
  `old_version`      varchar(20)                              COMMENT '旧版本',
  `new_version`      varchar(20)                              COMMENT '新版本',
  `gray_rules`       text                                     COMMENT '灰度规则（JSON格式）',
  `publish_user_id`  bigint(20)      NOT NULL                COMMENT '发布人ID',
  `publish_user_name` varchar(50)     NOT NULL                COMMENT '发布人姓名',
  `publish_time`     datetime                                 COMMENT '发布时间',
  `finish_time`      datetime                                 COMMENT '完成时间',
  `rollback_time`    datetime                                 COMMENT '回滚时间',
  `create_by`        varchar(64)     DEFAULT ''              COMMENT '创建者',
  `create_time`      datetime                                 COMMENT '创建时间',
  `update_by`        varchar(64)     DEFAULT ''              COMMENT '更新者',
  `update_time`      datetime                                 COMMENT '更新时间',
  `remark`           varchar(500)    DEFAULT NULL            COMMENT '备注',
  PRIMARY KEY (`publish_id`),
  KEY `idx_api_id` (`api_id`),
  KEY `idx_publish_time` (`publish_time`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COMMENT='API发布记录表';

-- ----------------------------
-- 初始化基础数据
-- ----------------------------

-- 插入默认管理员用户
INSERT INTO `sys_user` VALUES (1, 'admin', '管理员', '00', 'admin@myruoyi.com', '15888888888', '1', '', '$2a$10$7JB720yubVSOfvVWbfXCOOxjTOQcQjmrJF1ZM4nAVccp/.rkMlDW', '0', '127.0.0.1', NOW(), 'admin', NOW(), '', NULL, '管理员', 0);

-- 插入默认角色
INSERT INTO `sys_role` VALUES (1, '超级管理员', 'admin', 1, '1', 1, 1, '0', 'admin', NOW(), '', NULL, '超级管理员', 0);
INSERT INTO `sys_role` VALUES (2, 'API管理员', 'api_admin', 2, '1', 1, 1, '0', 'admin', NOW(), '', NULL, 'API管理员', 0);
INSERT INTO `sys_role` VALUES (3, 'API开发者', 'api_developer', 3, '1', 1, 1, '0', 'admin', NOW(), '', NULL, 'API开发者', 0);
INSERT INTO `sys_role` VALUES (4, 'API使用者', 'api_user', 4, '1', 1, 1, '0', 'admin', NOW(), '', NULL, 'API使用者', 0);

-- 插入用户角色关联
INSERT INTO `sys_user_role` VALUES (1, 1);

-- 插入默认部门
INSERT INTO `sys_dept` VALUES (1, 0, '0', '总公司', 0, 'admin', '15888888888', 'admin@myruoyi.com', '0', 'admin', NOW(), '', NULL, 0);
INSERT INTO `sys_dept` VALUES (2, 1, '0,1', '研发部门', 1, 'admin', '15888888888', 'admin@myruoyi.com', '0', 'admin', NOW(), '', NULL, 0);

-- 插入默认菜单
INSERT INTO `sys_menu` VALUES (1, '系统管理', 0, 1, 'system', NULL, '', 1, 0, 'M', 0, 0, '', 'system', 'admin', NOW(), '', NULL, '系统管理目录', 0);
INSERT INTO `sys_menu` VALUES (2, '用户管理', 1, 1, 'user', 'system/user/index', '', 1, 0, 'C', 0, 0, 'system:user:list', 'user', 'admin', NOW(), '', NULL, '用户管理菜单', 0);
INSERT INTO `sys_menu` VALUES (3, '角色管理', 1, 2, 'role', 'system/role/index', '', 1, 0, 'C', 0, 0, 'system:role:list', 'peoples', 'admin', NOW(), '', NULL, '角色管理菜单', 0);
INSERT INTO `sys_menu` VALUES (4, '菜单管理', 1, 3, 'menu', 'system/menu/index', '', 1, 0, 'C', 0, 0, 'system:menu:list', 'tree-table', 'admin', NOW(), '', NULL, '菜单管理菜单', 0);
INSERT INTO `sys_menu` VALUES (5, '部门管理', 1, 4, 'dept', 'system/dept/index', '', 1, 0, 'C', 0, 0, 'system:dept:list', 'tree', 'admin', NOW(), '', NULL, '部门管理菜单', 0);

INSERT INTO `sys_menu` VALUES (100, 'API管理', 0, 2, 'api', NULL, '', 1, 0, 'M', 0, 0, '', 'api', 'admin', NOW(), '', NULL, 'API管理目录', 0);
INSERT INTO `sys_menu` VALUES (101, '应用管理', 100, 1, 'app', 'api/app/index', '', 1, 0, 'C', 0, 0, 'api:app:list', 'app', 'admin', NOW(), '', NULL, '应用管理菜单', 0);
INSERT INTO `sys_menu` VALUES (102, '模块管理', 100, 2, 'module', 'api/module/index', '', 1, 0, 'C', 0, 0, 'api:module:list', 'module', 'admin', NOW(), '', NULL, '模块管理菜单', 0);
INSERT INTO `sys_menu` VALUES (103, '接口管理', 100, 3, 'info', 'api/info/index', '', 1, 0, 'C', 0, 0, 'api:info:list', 'interface', 'admin', NOW(), '', NULL, '接口管理菜单', 0);
INSERT INTO `sys_menu` VALUES (104, '接口调试', 100, 4, 'debug', 'api/debug/index', '', 1, 0, 'C', 0, 0, 'api:debug:list', 'debug', 'admin', NOW(), '', NULL, '接口调试菜单', 0);
INSERT INTO `sys_menu` VALUES (105, '权限管理', 100, 5, 'permission', 'api/permission/index', '', 1, 0, 'C', 0, 0, 'api:permission:list', 'permission', 'admin', NOW(), '', NULL, '权限管理菜单', 0);
INSERT INTO `sys_menu` VALUES (106, '调用统计', 100, 6, 'statistics', 'api/statistics/index', '', 1, 0, 'C', 0, 0, 'api:statistics:list', 'chart', 'admin', NOW(), '', NULL, '调用统计菜单', 0);

-- 插入角色菜单关联
INSERT INTO `sys_role_menu` VALUES (1, 1);
INSERT INTO `sys_role_menu` VALUES (1, 2);
INSERT INTO `sys_role_menu` VALUES (1, 3);
INSERT INTO `sys_role_menu` VALUES (1, 4);
INSERT INTO `sys_role_menu` VALUES (1, 5);
INSERT INTO `sys_role_menu` VALUES (1, 100);
INSERT INTO `sys_role_menu` VALUES (1, 101);
INSERT INTO `sys_role_menu` VALUES (1, 102);
INSERT INTO `sys_role_menu` VALUES (1, 103);
INSERT INTO `sys_role_menu` VALUES (1, 104);
INSERT INTO `sys_role_menu` VALUES (1, 105);
INSERT INTO `sys_role_menu` VALUES (1, 106);