DROP SCHEMA IF EXISTS `usermanager`;

CREATE SCHEMA `usermanager` ;

USE `usermanager`;

CREATE TABLE `usermanager`.`users` (
  `id` INT(8) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(25) NOT NULL,
  `age` INT NOT NULL,
  `isAdmin` BIT(1) NOT NULL,
  `createDate` TIMESTAMP(6) NOT NULL,
  PRIMARY KEY (`id`));
  
INSERT INTO	`usermanager`.`users` (`id`, `name`, `age`, `isAdmin`, `createDate`)
VALUES
	(1, "User1", 10, 0, "2016-11-02 16:50:27"),
    (2, "User2", 11, 0, "2016-10-02 15:50:27"),
    (3, "User3", 12, 1, "2015-10-02 15:50:27"),
    (4, "User4", 13, 0, "2014-10-02 15:50:27"),
    (5, "User5", 14, 0, "2013-09-02 15:50:27"),
    (6, "User6", 15, 1, "2012-10-02 09:50:27"),
    (7, "User7", 16, 0, "2011-10-02 15:50:27"),
    (8, "User8", 17, 0, "2010-10-02 15:50:27"),
    (9, "User9", 18, 1, "2009-10-01 15:50:30"),
    (10, "User10", 19, 0, "2008-10-02 15:50:27"),
    (11, "User11", 20, 0, "2007-10-02 15:50:27"),
    (12, "User12", 21, 1, "2006-02-15 15:50:27");