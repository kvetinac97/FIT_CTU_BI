-- Skript sloužící pro vložení dat do databáze

SET NAMES utf8;
SET time_zone = '+00:00';
SET foreign_key_checks = 0;
SET sql_mode = 'NO_AUTO_VALUE_ON_ZERO';

SET NAMES utf8mb4;

INSERT INTO `activity` (`id`, `calories_per_minute`, `name`, `unit`, `unit_to_minutes`) VALUES
(1,	0.057,	'Chůze 4km/h',	'min',	1),
(2,	0.06,	'Fotbal',	'min',	1),
(3,	0.15,	'Běh 12 km/h',	'min',	1),
(4,	0.27,	'Plavání',	'min',	1),
(5,	0.13,	'Švihadlo',	'min',	1),
(6,	0.0044,	'Zpěv',	'min',	1),
(7,	0.015,	'Programování',	'min',	1),
(8,	0.2,	'Sprint 24 km/h',	'min',	1);

INSERT INTO `food` (`id`, `carbohydrates`, `fat`, `name`, `protein`, `unit`, `unit_to_grams`) VALUES
(1,	12,	20,	'Kebab',	25,	'tortilla',	250),
(2,	5,	10,	'Hovězí pečeně',	12,	'porce',	200),
(3,	12,	2,	'Kyselé žížalky',	0,	'sáček',	80),
(4,	12,	3,	'Vexty',	0,	'sáček',	90),
(5,	22,	0,	'Banán',	1,	'kus',	169),
(6,	12,	8,	'Mléko',	8,	' sklenice',	250),
(7,	35,	0,	'Coca-Cola',	0,	'plechovka',	250),
(8,	25,	0,	'Jablko',	0,	'kus',	138),
(9,	0,	7,	'Vejce',	6,	'kus',	50),
(10,	68,	8,	'Margherita',	14,	'kus',	148),
(11,	1,	4,	'Šunka',	5,	'1 deka',	10);

INSERT INTO `goal` (`id`, `coefficient`, `name`) VALUES
(1,	0.8,	'Hubnutí'),
(2,	1,	'Udržování hmotnosti'),
(3,	1.2,	'Přibírání');

INSERT INTO `meal` (`id`, `name`, `time_of_day`) VALUES
(1,	'Snídaně',	'09:00:00'),
(2,	'Oběd',	'12:00:00'),
(3,	'Odpolední svačina',	'15:00:00'),
(4,	'Večeře',	'18:00:00');

INSERT INTO `user` (`id`, `birth`, `code`, `email`, `height`, `password`, `name`, `weight`, `goal_id`, `calories`, `carbohydrates`, `fat`, `protein`)
VALUES ('1', '1983-04-02', '1234-5678-9012-3456', 'karel.novak@seznam.cz', '180', 'password', 'Karel Novák', '82', '1', '2000', '100', '400', '500');

-- 2021-05-05 17:52:25
