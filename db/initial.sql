--
-- Table structure for table `agent`
--

DROP TABLE IF EXISTS `agent`;
CREATE TABLE `agent` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `email` varchar(45) DEFAULT NULL,
  `category` int(10) unsigned NOT NULL,
  `series` varchar(45) DEFAULT NULL,
  `wmn` tinyint(1) NOT NULL,
  `size` char(1) DEFAULT NULL,
  `minPrice` decimal(5,2) DEFAULT NULL,
  `maxPrice` decimal(5,2) DEFAULT NULL,
  `minDiff` decimal(5,2) DEFAULT NULL,
  `modelYear` year(4) DEFAULT NULL,
  `lastCheck` timestamp NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`),
  KEY `fk_agent_category_idx` (`category`),
  CONSTRAINT `fk_agent_category` FOREIGN KEY (`category`) REFERENCES `bicycle_category` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Table structure for table `archived_bicycle`
--

DROP TABLE IF EXISTS `archived_bicycle`;
CREATE TABLE `archived_bicycle` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `externalId` varchar(128) NOT NULL,
  `category` int(10) unsigned NOT NULL,
  `series` varchar(200) NOT NULL,
  `size` char(1) NOT NULL,
  `wmn` tinyint(1) NOT NULL,
  `price` decimal(5,2) NOT NULL,
  `diff` decimal(5,2) DEFAULT NULL,
  `url` varchar(200) NOT NULL,
  `photoUrl` varchar(200) DEFAULT NULL,
  `createdAt` timestamp NULL DEFAULT NULL,
  `importedAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `archivedAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `externalId_UNIQUE` (`externalId`),
  KEY `fk_category_idx` (`category`),
  CONSTRAINT `fk_archive_bicycle_category` FOREIGN KEY (`category`) REFERENCES `bicycle_category` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Table structure for table `bicycle`
--

DROP TABLE IF EXISTS `bicycle`;
CREATE TABLE `bicycle` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `externalId` varchar(128) NOT NULL,
  `category` int(10) unsigned NOT NULL,
  `series` varchar(200) NOT NULL,
  `size` char(1) NOT NULL,
  `wmn` tinyint(1) NOT NULL,
  `price` decimal(5,2) NOT NULL,
  `diff` decimal(5,2) DEFAULT NULL,
  `url` varchar(200) NOT NULL,
  `photoUrl` varchar(200) DEFAULT NULL,
  `createdAt` timestamp NULL DEFAULT NULL,
  `importedAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `externalId_UNIQUE` (`externalId`),
  KEY `fk_category_idx` (`category`),
  CONSTRAINT `fk_bicycle_category` FOREIGN KEY (`category`) REFERENCES `bicycle_category` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Table structure for table `bicycle_category`
--

DROP TABLE IF EXISTS `bicycle_category`;
CREATE TABLE `bicycle_category` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `externalUrl` varchar(200) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`),
  UNIQUE KEY `externalUrl_UNIQUE` (`externalUrl`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `bicycle_category`
--

INSERT INTO `bicycle_category` VALUES (1,'MTB','/articles.html?category=mtb&type=html'),(2,'Road','/articles.html?category=road&type=html'),(3,'Triathlon','/articles.html?category=triathlon&type=html'),(4,'Urban','/articles.html?category=urban&type=html'),(5,'Fitness','/articles.html?category=fitness&type=html');

--
-- Table structure for table `notification`
--

DROP TABLE IF EXISTS `notification`;
CREATE TABLE `notification` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `agent` int(10) unsigned NOT NULL,
  `bicycle` int(10) unsigned NOT NULL,
  `createdAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `emailSent` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_notification_agent_idx` (`agent`),
  KEY `fk_notification_bicycle_idx` (`bicycle`),
  CONSTRAINT `fk_notification_agent` FOREIGN KEY (`agent`) REFERENCES `agent` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_notification_bicycle` FOREIGN KEY (`bicycle`) REFERENCES `bicycle` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Table structure for table `setting`
--

DROP TABLE IF EXISTS `setting`;
CREATE TABLE `setting` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `key` varchar(45) NOT NULL,
  `value` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `key_UNIQUE` (`key`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `setting`
--

INSERT INTO `setting` VALUES (1,'url','https://www.canyon.com/en-sk/factory-outlet/ajax');

-- Dump completed on 2018-12-02 15:17:46
