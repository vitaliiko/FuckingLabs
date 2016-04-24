CREATE TABLE `lab2`.`goods` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` VARCHAR(45) NULL,
  `producer` VARCHAR(45) NULL,
  `model` VARCHAR(45) NULL,
  `price` INT NULL,
  `description` VARCHAR(255) NULL,
  PRIMARY KEY (`id`));

INSERT INTO `lab2`.`goods` (`type`, `producer`, `model`, `price`, `description`) VALUES ('Мат. плата', 'ASUS', 'Z170-A', '4700', 'Socket 1151 • Intel Z170 • 4xDDR4 3400, до 64 ГБ • видео: интегрировано в процессор Intel • 2xPCI-E 3.0 x16 (x16, x8/x8), 1xPCI-E 3.0 x16 (x4) • ATX, 305x244 мм');
INSERT INTO `lab2`.`goods` (`type`, `producer`, `model`, `price`, `description`) VALUES ('Мат. плата', 'ASUS', 'Z9PE-D16', '12600', '2xSocket 2011 • Intel C602-A PCH • 16xDDR3 RDIMM/UDIMM • видео: Aspeed AST2300 • 4xPCI-E 3.0 x16, 2xPCI-E x16 • SSI EEB, 304,8x330,2 мм');
INSERT INTO `lab2`.`goods` (`type`, `producer`, `model`, `price`, `description`) VALUES ('Мат. плата', 'MSI', '970 GAMING', '3000', 'Socket AM3+ • AMD 970 / AMD SB950 • 4xDDR3 2133 МГц до 32 ГБ • 2xPCI-E x16 2.0 (x16+x8) • ATX, 305x244 мм');
INSERT INTO `lab2`.`goods` (`type`, `producer`, `model`, `price`, `description`) VALUES ('Мат. плата', 'MSI', 'H110M PRO-VD', '1600', 'Socket AM3+ • AMD 970 / AMD SB950 • 4xDDR3 2133 МГц до 32 ГБ • 2xPCI-E x16 2.0 (x16+x8) • ATX, 305x244 мм');
INSERT INTO `lab2`.`goods` (`type`, `producer`, `model`, `price`, `description`) VALUES ('Процесоор', 'Intel', 'Core i7-4790K BX80646I74790K', '9500', 'Socket 1150 • 4 ГГц • кэш-память второго уровня: 1 МБ • кэш-память третьего уровня: 8 МБ • количество ядер: 4 • технология: 22 нм • Box');
INSERT INTO `lab2`.`goods` (`type`, `producer`, `model`, `price`, `description`) VALUES ('Процесоор', 'Intel', 'i3-6100 BX80662I36100', '3600', 'Socket 1151 • 3,7 ГГц • кэш-память второго уровня: 0,5 МБ • кэш-память третьего уровня: 3 МБ • количество ядер: 2 • технология: 14 нм • Box');
INSERT INTO `lab2`.`goods` (`type`, `producer`, `model`, `price`, `description`) VALUES ('Процесоор', 'Intel', 'Core i5-6600 BX80662I56600', '6300', 'Socket 1151 • 3,3 (3,9 Turbo) ГГц • кэш-память второго уровня: 1 МБ • кэш-память третьего уровня: 6 МБ • количество ядер: 4 • технология: 14 нм • Box');
INSERT INTO `lab2`.`goods` (`type`, `producer`, `model`, `price`, `description`) VALUES ('Процесоор', 'Intel', 'Core i5-6500 BX80662I56500', '5900', 'Socket 1151 • 3,2 (3,6 Turbo) ГГц • кэш-память второго уровня: 1 МБ • кэш-память третьего уровня: 6 МБ • количество ядер: 4 • технология: 14 нм • Box');
INSERT INTO `lab2`.`goods` (`type`, `producer`, `model`, `price`, `description`) VALUES ('Процесоор', 'AMD', 'FX-6300 FD6300WMHKBOX', '3100', 'Socket AM3+ • 3,5 ГГц • кэш-память второго уровня: 6 МБ • кэш-память третьего уровня: 8 МБ • количество ядер: 6 • технология: 32 нм • Box');
INSERT INTO `lab2`.`goods` (`type`, `producer`, `model`, `price`, `description`) VALUES ('Память', 'Kingston', 'KVR13S9S8/4BK', '590', 'DDR3 • 1333 МГц • PC3-10600 • штатные тайминги: CL9 • рабочее напряжение: 1,5 В');
INSERT INTO `lab2`.`goods` (`type`, `producer`, `model`, `price`, `description`) VALUES ('Память', 'Kingston', 'KVR800D2N6/2G', '740', 'DDR2 • 800 МГц • PC2-6400 • штатные тайминги: CL6 • рабочее напряжение: 1,8 В');
INSERT INTO `lab2`.`goods` (`type`, `producer`, `model`, `price`, `description`) VALUES ('Память', 'Kingston', 'KVR1333D3S9/4G', '880', 'DDR3 • 1333 МГц • PC3-10666 • штатные тайминги: CL9 • рабочее напряжение: 1,5 В ');
INSERT INTO `lab2`.`goods` (`type`, `producer`, `model`, `price`, `description`) VALUES ('Память', 'Kingston', 'KVR16N11/8', '1050', 'DDR3 • 1600 МГц • PC3-12800 • штатные тайминги: CL11 • рабочее напряжение: 1,5 В');
INSERT INTO `lab2`.`goods` (`type`, `producer`, `model`, `price`, `description`) VALUES ('Память', 'Kingston', 'KVR16N11S8/4', '600', 'DDR3 • 1600 МГц • PC3-12800 • штатные тайминги: CL11 • рабочее напряжение: 1,5 В');
INSERT INTO `lab2`.`goods` (`type`, `producer`, `model`, `price`, `description`) VALUES ('Память', 'Goodram', 'GR1600S364L11/8G', '900', 'DDR3 • 1600 МГц • PC3-12800 • штатные тайминги: CL11 • рабочее напряжение: 1,5 В');
INSERT INTO `lab2`.`goods` (`type`, `producer`, `model`, `price`, `description`) VALUES ('Память', 'Goodram', 'GR1333S364L9S/4G', '490', 'DDR3 • 1333 МГц • PC3-10600 • штатные тайминги: CL9 • рабочее напряжение: 1,5 В');
