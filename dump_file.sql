-- MySQL dump 10.13  Distrib 9.0.1, for macos14.7 (arm64)
--
-- Host: localhost    Database: PoisePMS
-- ------------------------------------------------------
-- Server version	9.0.1


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
-- Table structure for table `Architect`
--


DROP TABLE IF EXISTS `Architect`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Architect` (
  `ArchitectID` int NOT NULL AUTO_INCREMENT,
  `Name` text NOT NULL,
  `PhoneNumber` text NOT NULL,
  `Email` text NOT NULL,
  `PhysicalAddress` text NOT NULL,
  PRIMARY KEY (`ArchitectID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Dumping data for table `Architect`
--


LOCK TABLES `Architect` WRITE;
/*!40000 ALTER TABLE `Architect` DISABLE KEYS */;
INSERT INTO `Architect` VALUES (1,'John Moseley','023-456-7890','john.moseley@example.com','100 Architect Rd'),(2,'Jane Steven','098-765-4321','jane.steven@example.com','200 Architect Ave');
/*!40000 ALTER TABLE `Architect` ENABLE KEYS */;
UNLOCK TABLES;


--
-- Table structure for table `Customer`
--


DROP TABLE IF EXISTS `Customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Customer` (
  `CustomerID` int NOT NULL AUTO_INCREMENT,
  `Name` text NOT NULL,
  `PhoneNumber` text NOT NULL,
  `Email` text NOT NULL,
  `PhysicalAddress` text NOT NULL,
  PRIMARY KEY (`CustomerID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Dumping data for table `Customer`
--


LOCK TABLES `Customer` WRITE;
/*!40000 ALTER TABLE `Customer` DISABLE KEYS */;
INSERT INTO `Customer` VALUES (1,'Alice Johnson','056-123-4567','alice.johnson@example.com','300 Customer St'),(2,'Bob Neville','087-987-6543','bob.neville@example.com','400 Customer Blvd');
/*!40000 ALTER TABLE `Customer` ENABLE KEYS */;
UNLOCK TABLES;


--
-- Table structure for table `Engineer`
--


DROP TABLE IF EXISTS `Engineer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Engineer` (
  `EngineerID` int NOT NULL AUTO_INCREMENT,
  `Name` text NOT NULL,
  `PhoneNumber` text NOT NULL,
  `Email` text NOT NULL,
  `PhysicalAddress` text NOT NULL,
  PRIMARY KEY (`EngineerID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Dumping data for table `Engineer`
--


LOCK TABLES `Engineer` WRITE;
/*!40000 ALTER TABLE `Engineer` DISABLE KEYS */;
INSERT INTO `Engineer` VALUES (1,'Jake Wilson','074-555-5555','jake.wilson@example.com','500 Engineer Way'),(2,'Sara Lee','081-424-4794','sara.lee@example.com','600 Engineer Lane');
/*!40000 ALTER TABLE `Engineer` ENABLE KEYS */;
UNLOCK TABLES;


--
-- Table structure for table `Project`
--


DROP TABLE IF EXISTS `Project`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Project` (
  `ProjectID` int NOT NULL AUTO_INCREMENT,
  `ProjectName` text,
  `BuildingType` text,
  `PhysicalAddress` text,
  `ERFNumber` text,
  `TotalFee` double DEFAULT NULL,
  `TotalPaid` double DEFAULT NULL,
  `Deadline` date DEFAULT NULL,
  `EngineerID` int DEFAULT NULL,
  `ArchitectID` int DEFAULT NULL,
  `CustomerID` int DEFAULT NULL,
  `Finalized` tinyint(1) DEFAULT NULL,
  `CompletionDate` date DEFAULT NULL,
  PRIMARY KEY (`ProjectID`),
  KEY `EngineerID` (`EngineerID`),
  KEY `ArchitectID` (`ArchitectID`),
  KEY `CustomerID` (`CustomerID`),
  CONSTRAINT `project_ibfk_1` FOREIGN KEY (`EngineerID`) REFERENCES `Engineer` (`EngineerID`),
  CONSTRAINT `project_ibfk_2` FOREIGN KEY (`ArchitectID`) REFERENCES `Architect` (`ArchitectID`),
  CONSTRAINT `project_ibfk_3` FOREIGN KEY (`CustomerID`) REFERENCES `Customer` (`CustomerID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Dumping data for table `Project`
--


LOCK TABLES `Project` WRITE;
/*!40000 ALTER TABLE `Project` DISABLE KEYS */;
INSERT INTO `Project` VALUES (1,'Bridge Construction','Civil','123 Bridge St','ERF001',500000,250000,'2024-06-01',1,1,1,1,'2024-05-01'),(2,'Skyscraper Design','Commercial','456 Skyscraper Ave','ERF002',1500000,500000,'2024-09-01',2,2,2,0,NULL);
/*!40000 ALTER TABLE `Project` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;


/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;


-- Dump completed on 2025-01-06 14:45:17
