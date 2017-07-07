# ************************************************************
# Sequel Pro SQL dump
# Version 4541
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 127.0.0.1 (MySQL 5.5.5-10.1.23-MariaDB)
# Database: approval_service
# Generation Time: 2017-07-07 05:23:43 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table approval_attributes
# ------------------------------------------------------------

DROP TABLE IF EXISTS `approval_attributes`;

CREATE TABLE `approval_attributes` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `approval_id` varchar(255) NOT NULL DEFAULT '',
  `approval_key` varchar(255) NOT NULL DEFAULT '',
  `approval_value` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table approval_domain_mapping
# ------------------------------------------------------------

DROP TABLE IF EXISTS `approval_domain_mapping`;

CREATE TABLE `approval_domain_mapping` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `approval_id` varchar(255) NOT NULL DEFAULT '',
  `domain_id` int(11) NOT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table approval_status_history
# ------------------------------------------------------------

DROP TABLE IF EXISTS `approval_status_history`;

CREATE TABLE `approval_status_history` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `approval_id` varchar(255) NOT NULL DEFAULT '',
  `status` varchar(255) NOT NULL DEFAULT '',
  `updated_by` varchar(255) DEFAULT NULL,
  `message_id` varchar(255) DEFAULT NULL,
  `start_time` datetime DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  `version` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table approvals
# ------------------------------------------------------------

DROP TABLE IF EXISTS `approvals`;

CREATE TABLE `approvals` (
  `id` varchar(255) NOT NULL,
  `type` varchar(255) NOT NULL DEFAULT '',
  `type_id` varchar(255) NOT NULL DEFAULT '',
  `status` varchar(255) NOT NULL DEFAULT '',
  `requester_id` varchar(255) DEFAULT NULL,
  `source_domain_id` int(11) DEFAULT NULL,
  `conversation_id` varchar(255) DEFAULT NULL,
  `approver_queues_count` int(11) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `expire_at` datetime DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  `version` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table approver_queue
# ------------------------------------------------------------

DROP TABLE IF EXISTS `approver_queue`;

CREATE TABLE `approver_queue` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` varchar(255) NOT NULL DEFAULT '',
  `approval_id` varchar(255) NOT NULL DEFAULT '',
  `approver_status` varchar(255) NOT NULL DEFAULT '',
  `type` varchar(255) DEFAULT NULL,
  `queue_id` int(11) DEFAULT NULL,
  `start_time` datetime DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table tasks
# ------------------------------------------------------------

DROP TABLE IF EXISTS `tasks`;

CREATE TABLE `tasks` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `approval_id` varchar(255) NOT NULL DEFAULT '',
  `queue_id` int(11) NOT NULL,
  `type` varchar(255) NOT NULL DEFAULT '',
  `run_time` datetime NOT NULL,
  `status` varchar(255) NOT NULL DEFAULT '',
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;




/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
