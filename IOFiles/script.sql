CREATE TABLE `lab4`.`goods` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `type` VARCHAR(45) NULL,
  `producer` VARCHAR(45) NULL,
  `model` VARCHAR(255) NULL,
  `description` VARCHAR(255) NULL,
  `goodscol` VARCHAR(45) NULL,
  `price` INT NULL,
  PRIMARY KEY (`id`));