CREATE TABLE `paas_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` tinyint(4) DEFAULT NULL COMMENT '1内部',
  `servers` varchar(500) DEFAULT NULL,
  `status` char(1) DEFAULT NULL COMMENT '1有效 ',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='配置中心信息';


CREATE TABLE `paas_service_instance` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `service_id` varchar(128) DEFAULT NULL COMMENT '服务标识 (服务类型-业务名称-服务序列号)',
  `servers` varchar(500) DEFAULT NULL,
  `server_info` varchar(500) DEFAULT NULL COMMENT '资源主机信息 可能需要密码等',
  `client_conf` varchar(2000) DEFAULT NULL COMMENT '配置信息（SDK初始化池子、字节数大小等参数）',
  `status` char(1) DEFAULT NULL COMMENT '1有效 ',
  `begin_time` datetime DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  `remark` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=634 DEFAULT CHARSET=utf8 COMMENT='服务的用户实例表';


CREATE TABLE `paas_service_resource` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `service_type` tinyint(4) DEFAULT NULL COMMENT '1 缓存 2队列 3mongo',
  `servers` varchar(500) DEFAULT NULL,
  `server_info` varchar(500) DEFAULT NULL COMMENT '资源主机信息 可能需要密码等',
  `status` char(1) DEFAULT NULL COMMENT '1有效 ',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='服务的资源信息表';


CREATE TABLE `sys_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(100) DEFAULT NULL COMMENT '用户名',
  `password` varchar(100) DEFAULT NULL COMMENT '密码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
