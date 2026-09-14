-- Safe additive migration for dual exam system
USE `psychometric_db`;

-- 1. Create exam_types table
CREATE TABLE IF NOT EXISTS `exam_types` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(50) NOT NULL,
  `name_ar` varchar(255) NOT NULL,
  `name_en` varchar(255) DEFAULT NULL,
  `description_ar` text DEFAULT NULL,
  `is_active` boolean NOT NULL DEFAULT TRUE,
  `created_at` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_exam_types_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 2. Seed initial exam types
INSERT INTO `exam_types` (`code`, `name_ar`, `name_en`, `description_ar`, `is_active`, `created_at`)
SELECT 'PSYCHOMETRIC', 'التقييم السيكومتري القيادي', 'Psychometric Leadership Assessment', 'تقييم شامل للكفاءات القيادية ومعرقلات الأداء والقدرات المعرفية', TRUE, NOW(6)
WHERE NOT EXISTS (SELECT 1 FROM `exam_types` WHERE `code` = 'PSYCHOMETRIC');

INSERT INTO `exam_types` (`code`, `name_ar`, `name_en`, `description_ar`, `is_active`, `created_at`)
SELECT 'EMPLOYMENT', 'اختبار التوظيف', 'Employment Assessment', 'اختبار تقييم المرشحين للتوظيف والملاءمة الوظيفية', TRUE, NOW(6)
WHERE NOT EXISTS (SELECT 1 FROM `exam_types` WHERE `code` = 'EMPLOYMENT');

-- 3. Procedure to safely add column if not exists
DROP PROCEDURE IF EXISTS `AddColumnIfNotExists`;
DELIMITER $$
CREATE PROCEDURE `AddColumnIfNotExists`(
    IN in_table_name VARCHAR(64),
    IN in_column_name VARCHAR(64),
    IN in_column_def VARCHAR(255)
)
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.COLUMNS 
        WHERE table_schema = DATABASE() 
          AND table_name = in_table_name 
          AND column_name = in_column_name
    ) THEN
        SET @sql = CONCAT('ALTER TABLE `', in_table_name, '` ADD COLUMN `', in_column_name, '` ', in_column_def);
        PREPARE stmt FROM @sql;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
    END IF;
END$$
DELIMITER ;

CALL AddColumnIfNotExists('personality_items', 'exam_type', 'varchar(50) NOT NULL DEFAULT ''PSYCHOMETRIC''');
CALL AddColumnIfNotExists('derailer_items', 'exam_type', 'varchar(50) NOT NULL DEFAULT ''PSYCHOMETRIC''');
CALL AddColumnIfNotExists('gcat_questions', 'exam_type', 'varchar(50) NOT NULL DEFAULT ''PSYCHOMETRIC''');
CALL AddColumnIfNotExists('sjt_scenarios', 'exam_type', 'varchar(50) NOT NULL DEFAULT ''PSYCHOMETRIC''');
CALL AddColumnIfNotExists('assessment_attempts', 'exam_type', 'varchar(50) NOT NULL DEFAULT ''PSYCHOMETRIC''');
CALL AddColumnIfNotExists('competencies', 'exam_type', 'varchar(50) NOT NULL DEFAULT ''PSYCHOMETRIC''');
CALL AddColumnIfNotExists('competency_traits', 'exam_type', 'varchar(50) NOT NULL DEFAULT ''PSYCHOMETRIC''');

DROP PROCEDURE IF EXISTS `AddColumnIfNotExists`;

-- Ensure all existing rows have exam_type = 'PSYCHOMETRIC'
UPDATE `personality_items` SET `exam_type` = 'PSYCHOMETRIC' WHERE `exam_type` IS NULL OR `exam_type` = '';
UPDATE `derailer_items` SET `exam_type` = 'PSYCHOMETRIC' WHERE `exam_type` IS NULL OR `exam_type` = '';
UPDATE `gcat_questions` SET `exam_type` = 'PSYCHOMETRIC' WHERE `exam_type` IS NULL OR `exam_type` = '';
UPDATE `sjt_scenarios` SET `exam_type` = 'PSYCHOMETRIC' WHERE `exam_type` IS NULL OR `exam_type` = '';
UPDATE `assessment_attempts` SET `exam_type` = 'PSYCHOMETRIC' WHERE `exam_type` IS NULL OR `exam_type` = '';
UPDATE `competencies` SET `exam_type` = 'PSYCHOMETRIC' WHERE `exam_type` IS NULL OR `exam_type` = '';
UPDATE `competency_traits` SET `exam_type` = 'PSYCHOMETRIC' WHERE `exam_type` IS NULL OR `exam_type` = '';
