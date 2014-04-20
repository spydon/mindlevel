-- MySQL dump 10.14  Distrib 10.0.7-MariaDB, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: mindlevel
-- ------------------------------------------------------
-- Server version	10.0.7-MariaDB-1~wheezy-log

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
INSERT INTO `category` VALUES (1,'adventurous'),(2,'artistic'),(3,'funny'),(4,'kind'),(5,'outgoing');
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `captcha`
--

DROP TABLE IF EXISTS `captcha`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `captcha` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `question` varchar(32) NOT NULL,
  `answer` varchar(32) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `captcha`
--

LOCK TABLES `captcha` WRITE;
/*!40000 ALTER TABLE `captcha` DISABLE KEYS */;
INSERT INTO `captcha` (question, answer) VALUES ('five','5'),('100+56','156'),('five+4','9');
/*!40000 ALTER TABLE `captcha` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `captcha`
--

DROP TABLE IF EXISTS `delivered_captcha`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `delivered_captcha` (
  `id` int(11) NOT NULL,
  `token` varchar(64) DEFAULT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`, `token`),
  KEY `id` (`id`),
  CONSTRAINT `delivered_captcha_ibfk_1` FOREIGN KEY (`id`) REFERENCES `captcha` (`id`) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `captcha`
--

LOCK TABLES `delivered_captcha` WRITE;
/*!40000 ALTER TABLE `delivered_captcha` DISABLE KEYS */;
/*!40000 ALTER TABLE `delivered_captcha` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comment_thread`
--

DROP TABLE IF EXISTS `comment_thread`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `comment_thread` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment_thread`
--

LOCK TABLES `comment_thread` WRITE;
/*!40000 ALTER TABLE `comment_thread` DISABLE KEYS */;
INSERT INTO comment_thread (id) VALUE (0);
/*!40000 ALTER TABLE `comment_thread` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `comment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(64) NOT NULL,
  `comment` text NOT NULL,
  `parent_id` int(11) NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `thread_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  /*INDEX(`thread_id`),*/
  KEY `parent_id` (`parent_id`),
  KEY `username` (`username`),
  KEY `thread_id` (`thread_id`),
  CONSTRAINT `comment_ibfk_1` FOREIGN KEY (`parent_id`) REFERENCES `comment` (`id`) ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT `comment_ibfk_2` FOREIGN KEY (`username`) REFERENCES `user` (`username`) ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT `comment_ibfk_3` FOREIGN KEY (`thread_id`) REFERENCES `comment_thread` (`id`) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment`
--

LOCK TABLES `comment` WRITE;
/*!40000 ALTER TABLE `comment` DISABLE KEYS */;
INSERT INTO comment (id, username, parent_id, thread_id) VALUES (0, 'system', 0, 0);
/*!40000 ALTER TABLE `comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `news`
--

DROP TABLE IF EXISTS `news`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `news` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(64) NOT NULL,
  `content` text NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `username` (`username`),
  CONSTRAINT `news_ibfk_2` FOREIGN KEY (`username`) REFERENCES `user` (`username`) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `news`
--

LOCK TABLES `news` WRITE;
/*!40000 ALTER TABLE `news` DISABLE KEYS */;
/*!40000 ALTER TABLE `news` ENABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mission`
--

LOCK TABLES `mission` WRITE;
/*!40000 ALTER TABLE `mission` DISABLE KEYS */;
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
  CONSTRAINT `mission_category_ibfk_1` FOREIGN KEY (`mission_id`) REFERENCES `mission` (`id`) ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT `mission_category_ibfk_2` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mission_category`
--

LOCK TABLES `mission_category` WRITE;
/*!40000 ALTER TABLE `mission_category` DISABLE KEYS */;
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
  `thread_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `owner` (`owner`),
  KEY `mission_id` (`mission_id`),
  KEY `thread_id` (`thread_id`),
  CONSTRAINT `picture_ibfk_3` FOREIGN KEY (`thread_id`) REFERENCES `comment_thread` (`id`),
  CONSTRAINT `picture_ibfk_1` FOREIGN KEY (`owner`) REFERENCES `user` (`username`) ON UPDATE CASCADE,
  CONSTRAINT `picture_ibfk_2` FOREIGN KEY (`mission_id`) REFERENCES `mission` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
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
  CONSTRAINT `rating_ibfk_1` FOREIGN KEY (`picture_id`) REFERENCES `picture` (`id`) ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT `rating_ibfk_2` FOREIGN KEY (`username`) REFERENCES `user` (`username`) ON UPDATE CASCADE ON DELETE CASCADE
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
  `location` varchar(256) DEFAULT '',
  `about` varchar(2048) NOT NULL DEFAULT '',
  `adult` tinyint(1) NOT NULL DEFAULT '0',
  `permission_id` int(11) DEFAULT '0',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `token` varchar(64) DEFAULT NULL,
  `last_login` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `picture` varchar(64) DEFAULT '../pictures/default.jpg',
  `picture_adult` tinyint(1) DEFAULT '0',
  `name` varchar(64) DEFAULT '',
  `score` int(11) DEFAULT '5',
  `email` varchar(128) NOT NULL,
  `validated` tinyint(1) NOT NULL DEFAULT '0',
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
INSERT INTO `user` (username, password, adult, permission_id, email, validated)  VALUES ('system','ThisIsALoginBlock',1,1,'noemail@system.com',1);
INSERT INTO `user` (username, password, adult, permission_id, email, validated) VALUES ('spydon','6b753344106bdd9ee9358ec03a62762d9e12bc2fa865ab37e296c37f2d0956341d95f9e1362c1a9798ce6f22dd5c0060788979dd92b964e2ec7bf1daa9232e86'
                            ,1,1,'admin@mindlevel.net',1);
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
  CONSTRAINT `user_picture_ibfk_1` FOREIGN KEY (`picture_id`) REFERENCES `picture` (`id`) ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT `user_picture_ibfk_2` FOREIGN KEY (`username`) REFERENCES `user` (`username`) ON UPDATE CASCADE ON DELETE CASCADE
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

-- Dump completed on 2014-02-02 15:23:59
