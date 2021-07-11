-- Skript sloužící k vytvoření databáze

SET NAMES utf8;
SET time_zone = '+00:00';
SET foreign_key_checks = 0;
SET sql_mode = 'NO_AUTO_VALUE_ON_ZERO';

SET NAMES utf8mb4;

DROP TABLE IF EXISTS `activity`;
CREATE TABLE `activity` (
  `id` int NOT NULL,
  `calories_per_minute` double NOT NULL,
  `name` varchar(255) COLLATE utf8mb4_czech_ci NOT NULL,
  `unit` varchar(255) COLLATE utf8mb4_czech_ci NOT NULL,
  `unit_to_minutes` double NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_czech_ci;

DROP TABLE IF EXISTS `food`;
CREATE TABLE `food` (
  `id` int NOT NULL,
  `carbohydrates` int NOT NULL,
  `fat` int NOT NULL,
  `name` varchar(255) COLLATE utf8mb4_czech_ci NOT NULL,
  `protein` int NOT NULL,
  `unit` varchar(255) COLLATE utf8mb4_czech_ci NOT NULL,
  `unit_to_grams` double NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_czech_ci;

DROP TABLE IF EXISTS `food_micronutrient`;
CREATE TABLE `food_micronutrient` (
  `id` int NOT NULL,
  `amount` int NOT NULL,
  `food_id` int NOT NULL,
  `micronutrient_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKm4xo8segfx9gcnxes5nl8gi1m` (`food_id`),
  KEY `FKfxxfoaelnqaeeitgeso3054u5` (`micronutrient_id`),
  CONSTRAINT `FKfxxfoaelnqaeeitgeso3054u5` FOREIGN KEY (`micronutrient_id`) REFERENCES `micronutrient` (`id`),
  CONSTRAINT `FKm4xo8segfx9gcnxes5nl8gi1m` FOREIGN KEY (`food_id`) REFERENCES `food` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_czech_ci;


DROP TABLE IF EXISTS `goal`;
CREATE TABLE `goal` (
  `id` int NOT NULL,
  `coefficient` double NOT NULL,
  `name` varchar(255) COLLATE utf8mb4_czech_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_czech_ci;

DROP TABLE IF EXISTS `meal`;
CREATE TABLE `meal` (
  `id` int NOT NULL,
  `name` varchar(255) COLLATE utf8mb4_czech_ci NOT NULL,
  `time_of_day` time NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_czech_ci;

DROP TABLE IF EXISTS `micronutrient`;
CREATE TABLE `micronutrient` (
  `id` int NOT NULL,
  `name` varchar(255) COLLATE utf8mb4_czech_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_czech_ci;


DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int NOT NULL,
  `birth` datetime(6) DEFAULT NULL,
  `code` varchar(255) COLLATE utf8mb4_czech_ci NOT NULL,
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_czech_ci NOT NULL,
  `height` varchar(255) COLLATE utf8mb4_czech_ci DEFAULT NULL,
  `password` varchar(255) COLLATE utf8mb4_czech_ci NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_czech_ci NOT NULL,
  `weight` varchar(255) COLLATE utf8mb4_czech_ci DEFAULT NULL,
  `goal_id` int DEFAULT NULL,
  `calories` int DEFAULT NULL,
  `carbohydrates` int DEFAULT NULL,
  `fat` int DEFAULT NULL,
  `protein` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_h1vneshxbwkd1ailk02vdy2qu` (`code`),
  KEY `FKp5fv9swb0fmirr198p1fpudps` (`goal_id`),
  CONSTRAINT `FKp5fv9swb0fmirr198p1fpudps` FOREIGN KEY (`goal_id`) REFERENCES `goal` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_czech_ci;

DROP TABLE IF EXISTS `user_activity`;
CREATE TABLE `user_activity` (
  `id` int NOT NULL,
  `date` date NOT NULL,
  `duration` int NOT NULL,
  `activity_id` int NOT NULL,
  `user_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKlw9o1xb2ki2hnwq1o3kk5dlja` (`activity_id`),
  KEY `FKp78clcyf5okycdv9teohsr2kq` (`user_id`),
  CONSTRAINT `FKlw9o1xb2ki2hnwq1o3kk5dlja` FOREIGN KEY (`activity_id`) REFERENCES `activity` (`id`),
  CONSTRAINT `FKp78clcyf5okycdv9teohsr2kq` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_czech_ci;

DROP TABLE IF EXISTS `user_meal_food`;
CREATE TABLE `user_meal_food` (
  `id` int NOT NULL,
  `amount` int NOT NULL,
  `date` date NOT NULL,
  `food_id` int NOT NULL,
  `meal_id` int NOT NULL,
  `user_id` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id_meal_id_food_id_date` (`user_id`,`meal_id`,`food_id`,`date`),
  KEY `FKrvpvd5bkfieqqmkputrdow24m` (`food_id`),
  KEY `FKbxp2pxoen9jrsd778blftaewc` (`meal_id`),
  CONSTRAINT `FKbxp2pxoen9jrsd778blftaewc` FOREIGN KEY (`meal_id`) REFERENCES `meal` (`id`),
  CONSTRAINT `FKrvpvd5bkfieqqmkputrdow24m` FOREIGN KEY (`food_id`) REFERENCES `food` (`id`),
  CONSTRAINT `FKsnm3kx1crvofdn7lhkbig4w5r` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_czech_ci;

-- 2021-05-05 17:52:25
