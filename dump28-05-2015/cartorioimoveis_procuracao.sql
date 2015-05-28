-- MySQL dump 10.13  Distrib 5.6.23, for Win32 (x86)
--
-- Host: 192.168.2.170    Database: cartorioimoveis
-- ------------------------------------------------------
-- Server version	5.6.24-log

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
-- Table structure for table `procuracao`
--

DROP TABLE IF EXISTS `procuracao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `procuracao` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `idprocurador` int(11) DEFAULT NULL,
  `identidade` int(11) DEFAULT NULL,
  `dtinicial` date DEFAULT NULL,
  `dtfinal` date DEFAULT NULL,
  `conjunto` enum('Isoladamente','Em Conjunto','Em conjunto ou Isoladamente','Nao Consta') DEFAULT NULL,
  `caminho` varchar(100) DEFAULT NULL,
  `idgeral` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `proc_procu_idx` (`idprocurador`),
  KEY `proc_ent_idx` (`identidade`),
  CONSTRAINT `proc_ent` FOREIGN KEY (`identidade`) REFERENCES `entidade` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `proc_procu` FOREIGN KEY (`idprocurador`) REFERENCES `procurador` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=94 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `procuracao`
--

LOCK TABLES `procuracao` WRITE;
/*!40000 ALTER TABLE `procuracao` DISABLE KEYS */;
INSERT INTO `procuracao` VALUES (1,3,1,'2009-01-13','2013-01-13','Nao Consta','\\\\servidor\\procuracoes\\mezzariproc\\1.pdf',1),(2,2,1,'2009-01-13','2013-01-13','Nao Consta','\\\\servidor\\procuracoes\\mezzariproc\\1.pdf',1),(3,1,1,'2009-01-13','2013-01-13','Nao Consta','\\\\servidor\\procuracoes\\mezzariproc\\1.pdf',1),(4,4,2,'2013-01-07','2013-01-05','Nao Consta','\\\\servidor\\procuracoes\\mezzariproc\\2.pdf',2),(5,5,2,'2013-01-07','2013-01-05','Nao Consta','\\\\servidor\\procuracoes\\mezzariproc\\2.pdf',2),(6,6,3,'2013-02-25',NULL,'Em Conjunto','\\\\servidor\\procuracoes\\mezzariproc\\3.pdf',3),(7,7,3,'2013-02-25',NULL,'Em Conjunto','\\\\servidor\\procuracoes\\mezzariproc\\3.pdf',3),(30,8,4,'2010-02-25',NULL,'Em conjunto ou Isoladamente','\\\\servidor\\procuracoes\\mezzariproc\\4.pdf',4),(31,9,4,'2010-02-25',NULL,'Em conjunto ou Isoladamente','\\\\servidor\\procuracoes\\mezzariproc\\4.pdf',4),(32,10,4,'2010-02-25',NULL,'Em conjunto ou Isoladamente','\\\\servidor\\procuracoes\\mezzariproc\\4.pdf',4),(33,11,4,'2010-02-25',NULL,'Em conjunto ou Isoladamente','\\\\servidor\\procuracoes\\mezzariproc\\4.pdf',4),(34,12,4,'2010-02-25',NULL,'Em conjunto ou Isoladamente','\\\\servidor\\procuracoes\\mezzariproc\\4.pdf',4),(35,13,4,'2010-02-25',NULL,'Em conjunto ou Isoladamente','\\\\servidor\\procuracoes\\mezzariproc\\4.pdf',4),(36,8,5,'2010-02-25',NULL,'Em conjunto ou Isoladamente','\\\\servidor\\procuracoes\\mezzariproc\\4.pdf',4),(37,9,5,'2010-02-25',NULL,'Em conjunto ou Isoladamente','\\\\servidor\\procuracoes\\mezzariproc\\4.pdf',4),(38,10,5,'2010-02-25',NULL,'Em conjunto ou Isoladamente','\\\\servidor\\procuracoes\\mezzariproc\\4.pdf',4),(39,11,5,'2010-02-25',NULL,'Em conjunto ou Isoladamente','\\\\servidor\\procuracoes\\mezzariproc\\4.pdf',4),(40,12,5,'2010-02-25',NULL,'Em conjunto ou Isoladamente','\\\\servidor\\procuracoes\\mezzariproc\\4.pdf',4),(41,13,5,'2010-02-25',NULL,'Em conjunto ou Isoladamente','\\\\servidor\\procuracoes\\mezzariproc\\4.pdf',4),(42,15,6,'2010-02-25',NULL,'Em conjunto ou Isoladamente','\\\\servidor\\procuracoes\\mezzariproc\\5.pdf',5),(43,14,6,'2010-02-25',NULL,'Em conjunto ou Isoladamente','\\\\servidor\\procuracoes\\mezzariproc\\5.pdf',5),(52,22,11,'2010-02-25','2014-04-07','Em conjunto ou Isoladamente','\\\\servidor\\procuracoes\\mezzariproc\\6.pdf',6),(53,21,11,'2010-02-25','2014-04-07','Em conjunto ou Isoladamente','\\\\servidor\\procuracoes\\mezzariproc\\6.pdf',6),(54,20,11,'2010-02-25','2014-04-07','Em conjunto ou Isoladamente','\\\\servidor\\procuracoes\\mezzariproc\\6.pdf',6),(55,25,12,'2010-05-05','2011-05-05','Em Conjunto','\\\\servidor\\procuracoes\\mezzariproc\\7.pdf',7),(56,24,12,'2010-05-05','2011-05-05','Em Conjunto','\\\\servidor\\procuracoes\\mezzariproc\\7.pdf',7),(57,23,12,'2010-05-05','2011-05-05','Em Conjunto','\\\\servidor\\procuracoes\\mezzariproc\\7.pdf',7),(58,30,13,'2010-02-25',NULL,'Em Conjunto','\\\\servidor\\procuracoes\\mezzariproc\\8.pdf',8),(59,29,13,'2010-02-25',NULL,'Em Conjunto','\\\\servidor\\procuracoes\\mezzariproc\\8.pdf',8),(60,28,13,'2010-02-25',NULL,'Em Conjunto','\\\\servidor\\procuracoes\\mezzariproc\\8.pdf',8),(61,27,13,'2010-02-25',NULL,'Em Conjunto','\\\\servidor\\procuracoes\\mezzariproc\\8.pdf',8),(62,26,13,'2010-02-25',NULL,'Em Conjunto','\\\\servidor\\procuracoes\\mezzariproc\\8.pdf',8),(63,35,13,'2010-02-25','2014-04-07','Em conjunto ou Isoladamente','\\\\servidor\\procuracoes\\mezzariproc\\9.pdf',9),(64,34,13,'2010-02-25','2014-04-07','Em conjunto ou Isoladamente','\\\\servidor\\procuracoes\\mezzariproc\\9.pdf',9),(65,33,13,'2010-02-25','2014-04-07','Em conjunto ou Isoladamente','\\\\servidor\\procuracoes\\mezzariproc\\9.pdf',9),(66,32,13,'2010-02-25','2014-04-07','Em conjunto ou Isoladamente','\\\\servidor\\procuracoes\\mezzariproc\\9.pdf',9),(67,31,13,'2010-02-25','2014-04-07','Em conjunto ou Isoladamente','\\\\servidor\\procuracoes\\mezzariproc\\9.pdf',9),(84,56,19,'2010-02-25','2199-01-01','Em Conjunto','\\\\servidor\\procuracoes\\mezzariproc\\10.pdf',10),(85,55,19,'2010-02-25','2199-01-01','Em Conjunto','\\\\servidor\\procuracoes\\mezzariproc\\10.pdf',10),(86,54,19,'2010-02-25','2199-01-01','Em Conjunto','\\\\servidor\\procuracoes\\mezzariproc\\10.pdf',10),(87,53,19,'2010-02-25','2199-01-01','Em Conjunto','\\\\servidor\\procuracoes\\mezzariproc\\10.pdf',10),(88,52,19,'2010-02-25','2199-01-01','Em Conjunto','\\\\servidor\\procuracoes\\mezzariproc\\10.pdf',10),(89,61,20,'2010-02-25','2014-04-07','Em conjunto ou Isoladamente','\\\\servidor\\procuracoes\\mezzariproc\\11.pdf',11),(90,60,20,'2010-02-25','2014-04-07','Em conjunto ou Isoladamente','\\\\servidor\\procuracoes\\mezzariproc\\11.pdf',11),(91,59,20,'2010-02-25','2014-04-07','Em conjunto ou Isoladamente','\\\\servidor\\procuracoes\\mezzariproc\\11.pdf',11),(92,58,20,'2010-02-25','2014-04-07','Em conjunto ou Isoladamente','\\\\servidor\\procuracoes\\mezzariproc\\11.pdf',11),(93,57,20,'2010-02-25','2014-04-07','Em conjunto ou Isoladamente','\\\\servidor\\procuracoes\\mezzariproc\\11.pdf',11);
/*!40000 ALTER TABLE `procuracao` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-05-27  9:56:58
