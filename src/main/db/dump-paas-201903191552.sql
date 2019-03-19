-- MySQL dump 10.13  Distrib 5.7.25, for Linux (x86_64)
--
-- Host: localhost    Database: paas
-- ------------------------------------------------------
-- Server version	5.7.25

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `paas_config`
--

DROP TABLE IF EXISTS `paas_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `paas_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` tinyint(4) DEFAULT NULL COMMENT '1内部',
  `servers` varchar(500) DEFAULT NULL,
  `status` char(1) DEFAULT NULL COMMENT '1有效 ',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='配置中心信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `paas_config`
--

LOCK TABLES `paas_config` WRITE;
/*!40000 ALTER TABLE `paas_config` DISABLE KEYS */;
INSERT INTO `paas_config` VALUES (1,1,'114.215.202.56:32181','1');
/*!40000 ALTER TABLE `paas_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `paas_instance_log`
--

DROP TABLE IF EXISTS `paas_instance_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `paas_instance_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `before_version` varchar(500) NOT NULL COMMENT '更新前数据',
  `after_version` varchar(500) NOT NULL COMMENT '更新后数据',
  `user_id` int(11) NOT NULL COMMENT '操作人id',
  `service_id` varchar(200) DEFAULT NULL COMMENT '服务id',
  `create_time` datetime NOT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `paas_instance_log`
--

LOCK TABLES `paas_instance_log` WRITE;
/*!40000 ALTER TABLE `paas_instance_log` DISABLE KEYS */;
INSERT INTO `paas_instance_log` VALUES (1,'{\"servers\":\"10.0.0.29:6379\",\"conf\":{\"maxIdle\":100,\"testOnBorrow\":\"false\",\"testOnReturn\":\"true\",\"maxWait\":3000,\"maxActive\":1024}}','{\"servers\":\"10.0.0.29:6379\",\"serverInfo\":\"\",\"conf\":{\"maxIdle\":100,\"testOnBorrow\":\"false\",\"testOnReturn\":\"true\",\"maxWait\":3000,\"maxActive\":1024}}',1,'biz_t1-cache-349868d1aa67415689208c806431cc15','2019-03-18 17:12:07'),(2,'{\"password\":\"hyww@1z3\",\"port\":\"5672\",\"host\":\"114.55.104.39\",\"routings\":[{\"count\":\"1\",\"key\":\"biz_t2_key_144912b0fe1a475fae18061d1fca5254\",\"queue\":\"biz_t2_queue_5abef61fcf554a66a3c6c62e22928236\"}],\"exchange\":\"biz_t2_ex_cdc2f02b99ca41699d6a11c88e73761c\",\"username\":\"bbtree\"}','{\"password\":\"hyww@1z3\",\"port\":\"5672\",\"host\":\"114.55.104.39\",\"routings\":[{\"count\":\"2\",\"key\":\"biz_t2_key_144912b0fe1a475fae18061d1fca5254\",\"queue\":\"biz_t2_queue_5abef61fcf554a66a3c6c62e22928236\"}],\"exchange\":\"biz_t2_ex_cdc2f02b99ca41699d6a11c88e73761c\",\"username\":\"bbtree\"}',1,'biz_t2-mq-0e02adf076524b26992dd571486bd5ca','2019-03-19 11:49:10'),(3,'{\"password\":\"hyww@1z3\",\"port\":\"5672\",\"host\":\"114.55.104.39\",\"routings\":[{\"count\":\"2\",\"key\":\"biz_t2_key_144912b0fe1a475fae18061d1fca5254\",\"queue\":\"biz_t2_queue_5abef61fcf554a66a3c6c62e22928236\"}],\"exchange\":\"biz_t2_ex_cdc2f02b99ca41699d6a11c88e73761c\",\"username\":\"bbtree\"}','{\"password\":\"hyww@1z3\",\"port\":\"5672\",\"host\":\"114.55.104.39\",\"routings\":[{\"count\":\"1\",\"key\":\"biz_t2_key_144912b0fe1a475fae18061d1fca5254\",\"queue\":\"biz_t2_queue_5abef61fcf554a66a3c6c62e22928236\"}],\"exchange\":\"biz_t2_ex_cdc2f02b99ca41699d6a11c88e73761c\",\"username\":\"bbtree\"}',1,'biz_t2-mq-0e02adf076524b26992dd571486bd5ca','2019-03-19 11:49:27');
/*!40000 ALTER TABLE `paas_instance_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `paas_service_instance`
--

DROP TABLE IF EXISTS `paas_service_instance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
  `content` varchar(500) DEFAULT NULL COMMENT '完整的配置内容，json格式，跟zk节点内容一致',
  PRIMARY KEY (`id`),
  UNIQUE KEY `paas_service_instance_service_id_IDX` (`service_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='服务的用户实例表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `paas_service_instance`
--

LOCK TABLES `paas_service_instance` WRITE;
/*!40000 ALTER TABLE `paas_service_instance` DISABLE KEYS */;
INSERT INTO `paas_service_instance` VALUES (1,'biz_t2-mq-0e02adf076524b26992dd571486bd5ca','114.55.104.39:5672','{\"username\":\"bbtree\",\"password\":\"hyww@1z3\",\"exchange\":\"\",\"routings\":[]}',NULL,'1','2019-03-19 11:28:55',NULL,'缴费服务申请rabbitmq服务','{\"password\":\"hyww@1z3\",\"port\":\"5672\",\"host\":\"114.55.104.39\",\"routings\":[{\"count\":\"1\",\"key\":\"biz_t2_key_144912b0fe1a475fae18061d1fca5254\",\"queue\":\"biz_t2_queue_5abef61fcf554a66a3c6c62e22928236\"}],\"exchange\":\"biz_t2_ex_cdc2f02b99ca41699d6a11c88e73761c\",\"username\":\"bbtree\"}'),(2,'biz_t1-mq-19899952e4604a2391946f2b7bba877b','114.55.104.39:5672','{\"username\":\"bbtree\",\"password\":\"hyww@1z3\",\"exchange\":\"\",\"routings\":[]}',NULL,'1','2019-03-19 11:35:40',NULL,'测试申请队列信息','{\"password\":\"hyww@1z3\",\"port\":\"5672\",\"host\":\"114.55.104.39\",\"routings\":[{\"count\":\"1\",\"key\":\"biz_t1_key_f4150f97f31f492d9a09df6d61c0dc34\",\"queue\":\"biz_t1_queue_627ff0dfb3744e9eab1eb8da88b84741\"}],\"exchange\":\"biz_t1_ex_8634bfd7f352496886a895022b6a2317\",\"username\":\"bbtree\"}');
/*!40000 ALTER TABLE `paas_service_instance` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `paas_service_resource`
--

DROP TABLE IF EXISTS `paas_service_resource`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `paas_service_resource` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `service_type` tinyint(4) DEFAULT NULL COMMENT '1 缓存 2队列 3mongo',
  `servers` varchar(500) DEFAULT NULL,
  `server_info` varchar(500) DEFAULT NULL COMMENT '资源主机信息 可能需要密码等',
  `status` char(1) DEFAULT NULL COMMENT '1有效 ',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='服务的资源信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `paas_service_resource`
--

LOCK TABLES `paas_service_resource` WRITE;
/*!40000 ALTER TABLE `paas_service_resource` DISABLE KEYS */;
INSERT INTO `paas_service_resource` VALUES (1,1,'10.0.0.29:6379',NULL,'1'),(2,1,'120.26.128.71:19000',NULL,'2'),(3,2,'114.55.104.39:5672','{\"username\":\"bbtree\",\"password\":\"hyww@1z3\",\"exchange\":\"\",\"routings\":[]}','1'),(4,3,'10.0.13.46:28020',NULL,'1'),(5,2,'10.172.24.81:5672','{\"username\":\"bbtree\",\"password\":\"hyww@bbtree2015\",\"exchange\":\"\",\"routings\":[]}','0');
/*!40000 ALTER TABLE `paas_service_resource` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user`
--

DROP TABLE IF EXISTS `sys_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(100) DEFAULT NULL COMMENT '用户名',
  `password` varchar(100) DEFAULT NULL COMMENT '密码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user`
--

LOCK TABLES `sys_user` WRITE;
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
INSERT INTO `sys_user` VALUES (1,'root','e10adc3949ba59abbe56e057f20f883e');
/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'paas'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-03-19 15:52:43
