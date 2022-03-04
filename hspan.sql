-- MySQL dump 10.13  Distrib 8.0.23, for osx10.15 (x86_64)
--
-- Host: localhost    Database: hspan
-- ------------------------------------------------------
-- Server version	8.0.23

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `hfile`
--

DROP TABLE IF EXISTS `hfile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hfile` (
  `id` bigint NOT NULL,
  `created_at` bigint DEFAULT NULL,
  `updated_at` bigint DEFAULT NULL,
  `access` int DEFAULT NULL,
  `file_path` varchar(255) DEFAULT NULL,
  `size` bigint NOT NULL DEFAULT '0' COMMENT '大小',
  `file_name` varchar(1024) NOT NULL DEFAULT '未命名' COMMENT '文件名',
  `owners` varchar(4096) NOT NULL DEFAULT '' COMMENT '所有者',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hfile`
--

LOCK TABLES `hfile` WRITE;
/*!40000 ALTER TABLE `hfile` DISABLE KEYS */;
INSERT INTO `hfile` VALUES (189859885879296,1624160511201,1624160511201,1,'/Users/bytedance/java/hspanFiles/软件工程过程复习v2.pdf',687206,'软件工程过程复习v2.pdf','188882260504576'),(189927040049152,1624176906262,1624176906262,1,'/Users/bytedance/java/hspanFiles/venv.zip',4611423,'venv.zip',''),(189928527663104,1624177269449,1624177269449,1,'/Users/bytedance/java/hspanFiles/venv(1).zip',4611423,'venv.zip','');
/*!40000 ALTER TABLE `hfile` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hfile_owners`
--

DROP TABLE IF EXISTS `hfile_owners`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hfile_owners` (
  `hfile_id` bigint NOT NULL,
  `owners` bigint DEFAULT NULL,
  KEY `FKphjie6lwxw70kawer43su6ld4` (`hfile_id`),
  CONSTRAINT `FKphjie6lwxw70kawer43su6ld4` FOREIGN KEY (`hfile_id`) REFERENCES `hfile` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hfile_owners`
--

LOCK TABLES `hfile_owners` WRITE;
/*!40000 ALTER TABLE `hfile_owners` DISABLE KEYS */;
INSERT INTO `hfile_owners` VALUES (189927040049152,189926974713856),(189859885879296,189928277098496),(189928527663104,189928277098496);
/*!40000 ALTER TABLE `hfile_owners` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `own`
--

DROP TABLE IF EXISTS `own`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `own` (
  `id` bigint NOT NULL,
  `created_at` bigint DEFAULT NULL,
  `updated_at` bigint DEFAULT NULL,
  `fileid` bigint DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `own`
--

LOCK TABLES `own` WRITE;
/*!40000 ALTER TABLE `own` DISABLE KEYS */;
/*!40000 ALTER TABLE `own` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `share`
--

DROP TABLE IF EXISTS `share`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `share` (
  `id` bigint NOT NULL,
  `created_at` bigint DEFAULT NULL,
  `updated_at` bigint DEFAULT NULL,
  `fromid` bigint DEFAULT NULL,
  `is_canceled` bit(1) NOT NULL,
  `toid` bigint DEFAULT NULL,
  `fileid` bigint DEFAULT NULL,
  `is_public` tinyint(1) NOT NULL DEFAULT '1',
  `code` varchar(32) DEFAULT NULL COMMENT '提取码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `share`
--

LOCK TABLES `share` WRITE;
/*!40000 ALTER TABLE `share` DISABLE KEYS */;
INSERT INTO `share` VALUES (189887485497344,1624167249389,1624167249389,188882260504576,_binary '\0',NULL,189859885879296,0,'KjnXb'),(189901723983872,1624170725582,1624170725582,188882260504576,_binary '\0',NULL,189859885879296,1,NULL);
/*!40000 ALTER TABLE `share` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` bigint NOT NULL,
  `created_at` bigint DEFAULT NULL,
  `updated_at` bigint DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (188882260504576,1623921833131,1623921833131,'123456','root'),(189553815425024,1624085786969,1624085786969,'123456','test1'),(189556947886080,1624086551730,1624086551730,'123456','test2'),(189560270168064,1624087362834,1624087362834,'123456','test3'),(189562144972800,1624087820550,1624087820550,'123456','test111'),(189926974713856,1624176890311,1624176890311,'123456','lhs123'),(189928277098496,1624177208276,1624177208276,'123456','lhs234');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_files`
--

DROP TABLE IF EXISTS `user_files`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_files` (
  `user_id` bigint NOT NULL,
  `files` bigint DEFAULT NULL,
  KEY `FK58770a6ppqd0j8k5lkskpxxos` (`user_id`),
  CONSTRAINT `FK58770a6ppqd0j8k5lkskpxxos` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_files`
--

LOCK TABLES `user_files` WRITE;
/*!40000 ALTER TABLE `user_files` DISABLE KEYS */;
INSERT INTO `user_files` VALUES (188882260504576,189859885879296),(189926974713856,189927040049152),(189928277098496,189859885879296),(189928277098496,189928527663104);
/*!40000 ALTER TABLE `user_files` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-06-20 16:40:34
