-- MySQL dump 10.14  Distrib 10.0.6-MariaDB, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: mindlevel
-- ------------------------------------------------------
-- Server version	10.0.6-MariaDB-1~wheezy-log

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
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `category` (
  `id` int(11) NOT NULL,
  `name` varchar(32) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (1,'adventurous'),(2,'funny');
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mission`
--

DROP TABLE IF EXISTS `mission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL,
  `description` text NOT NULL,
  `adult` tinyint(1) NOT NULL DEFAULT '0',
  `creator` varchar(64) NOT NULL,
  `validated` tinyint(1) NOT NULL DEFAULT '0',
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `validator` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `mission_ibfk_1` (`creator`),
  KEY `validator` (`validator`),
  CONSTRAINT `mission_ibfk_1` FOREIGN KEY (`creator`) REFERENCES `user` (`username`) ON UPDATE CASCADE,
  CONSTRAINT `mission_ibfk_2` FOREIGN KEY (`validator`) REFERENCES `user` (`username`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mission`
--

LOCK TABLES `mission` WRITE;
/*!40000 ALTER TABLE `mission` DISABLE KEYS */;
INSERT INTO `mission` VALUES (6,'test','test',0,'aaaa',1,'2013-12-29 15:50:29','spydon'),(7,'No more cellphone','Give away your cellphone to somebody that needs it more than you.',1,'aaaa',1,'2013-12-29 16:54:52','spydon'),(8,'A day without pants','How comfortable are you a whole day without pants?',1,'spydon',1,'2014-01-06 20:56:42','spydon');
/*!40000 ALTER TABLE `mission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mission_category`
--

DROP TABLE IF EXISTS `mission_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mission_category` (
  `mission_id` int(11) NOT NULL,
  `category_id` int(11) NOT NULL,
  PRIMARY KEY (`mission_id`,`category_id`),
  KEY `mission_category_ibfk_2` (`category_id`),
  CONSTRAINT `mission_category_ibfk_1` FOREIGN KEY (`mission_id`) REFERENCES `mission` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `mission_category_ibfk_2` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mission_category`
--

LOCK TABLES `mission_category` WRITE;
/*!40000 ALTER TABLE `mission_category` DISABLE KEYS */;
INSERT INTO `mission_category` VALUES (6,1),(7,1),(8,1),(8,2);
/*!40000 ALTER TABLE `mission_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `permission`
--

DROP TABLE IF EXISTS `permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `permission` (
  `id` int(11) NOT NULL,
  `name` varchar(32) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `permission`
--

LOCK TABLES `permission` WRITE;
/*!40000 ALTER TABLE `permission` DISABLE KEYS */;
INSERT INTO `permission` VALUES (0,'user'),(1,'admin'),(2,'moderator');
/*!40000 ALTER TABLE `permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `picture`
--

DROP TABLE IF EXISTS `picture`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `picture` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `filename` varchar(64) NOT NULL,
  `title` varchar(256) NOT NULL DEFAULT '',
  `location` varchar(256) NOT NULL DEFAULT '',
  `description` text NOT NULL,
  `adult` tinyint(1) DEFAULT '0',
  `validated` tinyint(1) NOT NULL DEFAULT '0',
  `owner` varchar(64) DEFAULT NULL,
  `mission_id` int(11) DEFAULT NULL,
  `score` int(11) DEFAULT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `owner` (`owner`),
  KEY `mission_id` (`mission_id`),
  CONSTRAINT `picture_ibfk_1` FOREIGN KEY (`owner`) REFERENCES `user` (`username`) ON UPDATE CASCADE,
  CONSTRAINT `picture_ibfk_2` FOREIGN KEY (`mission_id`) REFERENCES `mission` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `picture`
--

LOCK TABLES `picture` WRITE;
/*!40000 ALTER TABLE `picture` DISABLE KEYS */;
/*!40000 ALTER TABLE `picture` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rating`
--

DROP TABLE IF EXISTS `rating`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rating` (
  `picture_id` int(11) NOT NULL,
  `username` varchar(64) NOT NULL,
  `score` int(11) NOT NULL DEFAULT '0',
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`picture_id`,`username`),
  KEY `picture_id` (`picture_id`),
  KEY `username` (`username`),
  CONSTRAINT `rating_ibfk_1` FOREIGN KEY (`picture_id`) REFERENCES `picture` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `rating_ibfk_2` FOREIGN KEY (`username`) REFERENCES `user` (`username`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rating`
--

LOCK TABLES `rating` WRITE;
/*!40000 ALTER TABLE `rating` DISABLE KEYS */;
/*!40000 ALTER TABLE `rating` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `username` varchar(64) NOT NULL,
  `password` varchar(128) NOT NULL,
  `location` varchar(256) DEFAULT NULL,
  `about` text,
  `adult` tinyint(1) NOT NULL DEFAULT '0',
  `permission_id` int(11) DEFAULT '0',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `token` varchar(64) DEFAULT NULL,
  `last_login` int(11) DEFAULT NULL,
  `picture` varchar(64) DEFAULT NULL,
  `picture_adult` tinyint(1) DEFAULT '0',
  `name` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`username`),
  KEY `permission_id` (`permission_id`),
  CONSTRAINT `user_ibfk_1` FOREIGN KEY (`permission_id`) REFERENCES `permission` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES ('aaaa','6dd2462d6399baca7619994288653b0998946d6a912b83c2013f53c0f92fddf5cea0cda44ca1b04744179237051a81b995efec25eacf2b6048dea5d16b79d8ce',NULL,NULL,1,0,'2013-12-29 09:49:58',NULL,1388336031,'test.jpg',0,NULL),('spydon','6b753344106bdd9ee9358ec03a62762d9e12bc2fa865ab37e296c37f2d0956341d95f9e1362c1a9798ce6f22dd5c0060788979dd92b964e2ec7bf1daa9232e86','Uppsala','Hur jävla långt kan man skriva här då? Vaaaa? Hur långt? Vad sa du? Lorem ipsum blablabal... asdasdasdasd asdasdasd asdasdas dasdasda sdHur jävla långt kan man skriva här då? Vaaaa? Hur långt? Vad sa du? Lorem ipsum blablabal... asdasdasdasd asdasdasd asdasdas dasdasda sdHur jävla långt kan man skriva här då? Vaaaa? Hur långt? Vad sa du? Lorem ipsum blablabal... asdasdasdasd asdasdasd asdasdas dasdasda sdHur jävla långt kan man skriva här då? Vaaaa? Hur långt? Vad sa du? Lorem ipsum blablabal... asdasdasdasd asdasdasd asdasdas dasdasda sd',1,1,'2013-12-29 09:32:13','de22d1ce-7973-11e3-a3c6-14288d4cd549',1389302430,'test.jpg',1,'Lukas K');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_picture`
--

DROP TABLE IF EXISTS `user_picture`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_picture` (
  `picture_id` int(11) NOT NULL,
  `username` varchar(64) NOT NULL,
  PRIMARY KEY (`picture_id`,`username`),
  KEY `username` (`username`),
  CONSTRAINT `user_picture_ibfk_1` FOREIGN KEY (`picture_id`) REFERENCES `picture` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `user_picture_ibfk_2` FOREIGN KEY (`username`) REFERENCES `user` (`username`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_picture`
--

LOCK TABLES `user_picture` WRITE;
/*!40000 ALTER TABLE `user_picture` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_picture` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2014-01-09 23:44:39
