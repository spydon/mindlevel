-- MySQL dump 10.14  Distrib 10.0.4-MariaDB, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: mindlevel
-- ------------------------------------------------------
-- Server version	10.0.4-MariaDB-1~raring-log

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
  `name` varchar(32) NOT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES ('adventurous'),('creative'),('funny'),('kind'),('outgoing');
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
  `category` varchar(64) NOT NULL,
  `description` text,
  `adult` tinyint(1) DEFAULT '0',
  `creator` varchar(64) DEFAULT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mission`
--

LOCK TABLES `mission` WRITE;
/*!40000 ALTER TABLE `mission` DISABLE KEYS */;
INSERT INTO `mission` VALUES (2,'Climb a flagpole','adventurous','Weeell, the title is kind of self explanatory. Just do it.',0,'link','2013-08-08 05:49:29'),(3,'Call somebody','outgoing','Call somebody that you barely know but would like to get to know more.',1,'link','2013-08-09 05:52:36'),(4,'Go to a demonstration','outgoing','Row, row! Fight the power!',0,'spydon','2013-08-09 05:56:57'),(5,'Don\'t use your cellphone','outgoing','Don\'t use your cellphone for a week...',0,'spydon','2013-08-09 06:08:19'),(6,'Do a barrelroll','funny','Well... Not so hard is it?',0,'spydon','2013-08-25 12:18:27');
/*!40000 ALTER TABLE `mission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mission_suggestion`
--

DROP TABLE IF EXISTS `mission_suggestion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mission_suggestion` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL,
  `category` varchar(64) NOT NULL,
  `description` text,
  `adult` tinyint(1) DEFAULT '0',
  `creator` varchar(64) DEFAULT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mission_suggestion`
--

LOCK TABLES `mission_suggestion` WRITE;
/*!40000 ALTER TABLE `mission_suggestion` DISABLE KEYS */;
/*!40000 ALTER TABLE `mission_suggestion` ENABLE KEYS */;
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
  `description` varchar(2056) NOT NULL DEFAULT '',
  `adult` tinyint(1) DEFAULT '0',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `owner` varchar(64) DEFAULT NULL,
  `mission_id` int(11) DEFAULT NULL,
  `score` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `picture`
--

LOCK TABLES `picture` WRITE;
/*!40000 ALTER TABLE `picture` DISABLE KEYS */;
INSERT INTO `picture` VALUES (2,'9557f3da-9878-43fc-b1c2-6d400f99a087.jpeg','Hard work work','Uppsala','Don\'t even remember which course it was...',0,'2013-06-25 11:26:32','lolboll',1,NULL),(4,'df195518-aeaa-476f-bfb1-f079d82effb2.JPG','White car','Sydney, blää','I am in car. /Amit',1,'2013-06-29 08:50:01','linki',1,NULL),(5,'02d1ebf4-48b9-44ce-b4fd-8e92dfda83cd.jpg','linki','Brisbane','asdas',0,'2013-06-29 12:58:36','bbbb',1,NULL),(6,'d5edc879-fe9b-4980-92e3-9bb4ac020abb.jpg','AlpacaNiggah','Eumundi','An alpaca making a smiling face.',1,'2013-08-04 05:57:18','aaaa',1,NULL),(7,'1bfb08bb-4d32-4179-be23-498c7109581b.jpg','Bag','Eumundi','The best bags evaaar.',0,'2013-08-09 15:49:01','spydon',3,NULL);
/*!40000 ALTER TABLE `picture` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `picture_suggestion`
--

DROP TABLE IF EXISTS `picture_suggestion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `picture_suggestion` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `filename` varchar(64) NOT NULL,
  `title` varchar(256) NOT NULL DEFAULT '',
  `location` varchar(256) NOT NULL DEFAULT '',
  `description` varchar(2056) NOT NULL DEFAULT '',
  `adult` tinyint(1) DEFAULT '0',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `owner` varchar(64) DEFAULT NULL,
  `mission_id` int(11) DEFAULT NULL,
  `score` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `picture_suggestion`
--

LOCK TABLES `picture_suggestion` WRITE;
/*!40000 ALTER TABLE `picture_suggestion` DISABLE KEYS */;
/*!40000 ALTER TABLE `picture_suggestion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rating`
--

DROP TABLE IF EXISTS `rating`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rating` (
  `user` varchar(64) NOT NULL,
  `category` varchar(21) DEFAULT NULL,
  `score` int(11) NOT NULL DEFAULT '0',
  `picture_id` int(11) NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`user`,`picture_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rating`
--

LOCK TABLES `rating` WRITE;
/*!40000 ALTER TABLE `rating` DISABLE KEYS */;
INSERT INTO `rating` VALUES ('aaaa',NULL,5,2,'0000-00-00 00:00:00'),('aaaa',NULL,5,4,'0000-00-00 00:00:00'),('aaaa',NULL,2,6,'2013-08-18 06:57:54'),('bbbb',NULL,1,4,'0000-00-00 00:00:00'),('link',NULL,5,2,'0000-00-00 00:00:00'),('link',NULL,5,4,'0000-00-00 00:00:00'),('link',NULL,5,5,'0000-00-00 00:00:00'),('link',NULL,4,6,'2013-08-18 06:56:47'),('link',NULL,3,7,'2013-10-25 12:58:23'),('linki',NULL,5,2,'0000-00-00 00:00:00'),('linki',NULL,5,4,'0000-00-00 00:00:00'),('lolboll',NULL,4,2,'0000-00-00 00:00:00'),('lolboll',NULL,4,4,'0000-00-00 00:00:00'),('lukas',NULL,1,2,'0000-00-00 00:00:00'),('lukas',NULL,1,4,'0000-00-00 00:00:00'),('spydon',NULL,1,2,'2013-10-04 15:24:23'),('spydon',NULL,5,4,'2013-10-04 15:24:18'),('spydon',NULL,4,5,'2013-10-04 15:24:14'),('spydon',NULL,5,6,'2013-10-04 15:23:54'),('spydon',NULL,5,7,'2013-10-04 15:23:47');
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
  `description` varchar(2056) DEFAULT NULL,
  `adult` tinyint(1) NOT NULL,
  `admin` tinyint(1) DEFAULT '0',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `token` varchar(64) DEFAULT NULL,
  `last_login` int(11) DEFAULT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES ('aaaa','6ab9324bd53f68f7a5bc45fc132b0a3c5ede2c60e72451a10a9aa079645e076b305d73c8e57781963531a82c5527223dabc6f2e77cc9f2546db11f948a0e0daf',NULL,NULL,1,0,'2013-06-25 06:21:54',NULL,1382712796),('bbbb','90377be3de98cbbd004c2730c19260dc22c477c117e8838c93591402533a69fec8046bef5d03ea20801e3a962c2a2ce2b43e8afaf737a7281c0df91eef59831d',NULL,NULL,1,0,'2013-06-25 06:22:33',NULL,1375967375),('kuken','b050e176df86948b9cca15be57b60f9c24bdaf72d73922dbe475de0c9070ff563bf23dcf0a6e0a1ece6b305bfc0da6322a1c2d2848c8153e5ca26b2926b28fab',NULL,NULL,0,0,'2013-07-07 14:20:50','6ca7029d-e710-11e2-9dd4-001e378e6d26',1373206850),('link','9b9ce002e0ce1535ff609b34176f41e8df2c3acefdf685f7c40781392502b689855dd94c9d7ef3f531955a7ec1295462100f37b6bff6045a561b0f253d49a13d',NULL,NULL,0,0,'2013-06-24 05:52:03','4f9f4fd1-4138-11e3-ab9f-2a8a3876aaf1',1383119586),('linki','2b7daa8ace4e20a680965d5d04fc987bc2a102545439eee6d886760e4178852149305d1fd32d53ae0d0b4aec67c5b55ec73310cccdc943bd4b8b3fa4d96a8ee0',NULL,NULL,0,0,'2013-06-24 06:00:31',NULL,1372502421),('lolboll','5694ee1de6dcbcbe876e92401a025405281945317a26a1339b5e66344204419d1fbd6f65ccd7ba1de6a363c91cc2fd98dfab53a237869bef22ab23799a979a33',NULL,NULL,0,0,'2013-06-24 07:24:59',NULL,1372502390),('lukas','a81b1b77af7a0bb3704f7b11dbc5fd5b14e3dc91b4dff3abc39030dccf7c37c5b3f37c8d1dfad9e09d778b2f73c9bc4d008cc1ca553bf828e6d4be4cb8a6c6bd',NULL,NULL,1,0,'2013-06-29 12:40:41',NULL,1372509665),('spydon','09cbb85ceed21bb97f5c13cbf26a5660a72f3e98dc065b697c9b4e78439b2cbbbf422e73edc5c0e77c31c4a540d9b88d5be539c98704958098b5d91d7ef0a429',NULL,NULL,1,1,'2013-08-09 04:57:51',NULL,1380900176);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_picture`
--

DROP TABLE IF EXISTS `user_picture`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_picture` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `picture_id` int(11) NOT NULL,
  `user_id` varchar(64) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_picture`
--

LOCK TABLES `user_picture` WRITE;
/*!40000 ALTER TABLE `user_picture` DISABLE KEYS */;
INSERT INTO `user_picture` VALUES (1,1,'spydon');
/*!40000 ALTER TABLE `user_picture` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_picture_suggestion`
--

DROP TABLE IF EXISTS `user_picture_suggestion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_picture_suggestion` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `picture_id` int(11) NOT NULL,
  `user_id` varchar(64) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_picture_suggestion`
--

LOCK TABLES `user_picture_suggestion` WRITE;
/*!40000 ALTER TABLE `user_picture_suggestion` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_picture_suggestion` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-11-01 22:29:57
