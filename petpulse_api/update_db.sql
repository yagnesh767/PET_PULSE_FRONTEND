-- Safe Update Script for PetPulse Database
-- Run this in phpMyAdmin to add missing tables and columns.

USE `petpulse`;

-- 1. Update `users` table
SET @dbname = DATABASE();
SET @tablename = "users";

-- Add 'otp'
SET @columnname = "otp";
SET @preparedStatement = (SELECT IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE (table_name = @tablename) AND (table_schema = @dbname) AND (column_name = @columnname)) > 0,
  "SELECT 1",
  "ALTER TABLE users ADD COLUMN otp VARCHAR(6) DEFAULT NULL;"
));
PREPARE alterIfNotExists FROM @preparedStatement; EXECUTE alterIfNotExists; DEALLOCATE PREPARE alterIfNotExists;

-- Add 'is_verified'
SET @columnname = "is_verified";
SET @preparedStatement = (SELECT IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE (table_name = @tablename) AND (table_schema = @dbname) AND (column_name = @columnname)) > 0,
  "SELECT 1",
  "ALTER TABLE users ADD COLUMN is_verified TINYINT(1) DEFAULT 0;"
));
PREPARE alterIfNotExists FROM @preparedStatement; EXECUTE alterIfNotExists; DEALLOCATE PREPARE alterIfNotExists;

-- Add 'is_premium'
SET @columnname = "is_premium";
SET @preparedStatement = (SELECT IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE (table_name = @tablename) AND (table_schema = @dbname) AND (column_name = @columnname)) > 0,
  "SELECT 1",
  "ALTER TABLE users ADD COLUMN is_premium TINYINT(1) DEFAULT 0;"
));
PREPARE alterIfNotExists FROM @preparedStatement; EXECUTE alterIfNotExists; DEALLOCATE PREPARE alterIfNotExists;

-- 2. Update `pets` table
SET @tablename = "pets";
-- Add 'image_url'
SET @columnname = "image_url";
SET @preparedStatement = (SELECT IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE (table_name = @tablename) AND (table_schema = @dbname) AND (column_name = @columnname)) > 0,
  "SELECT 1",
  "ALTER TABLE pets ADD COLUMN image_url VARCHAR(255) DEFAULT NULL;"
));
PREPARE alterIfNotExists FROM @preparedStatement; EXECUTE alterIfNotExists; DEALLOCATE PREPARE alterIfNotExists;

-- 3. Update `vaccines` table
CREATE TABLE IF NOT EXISTS `vaccines` (
  `vaccine_id` int(11) NOT NULL AUTO_INCREMENT,
  `pet_id` int(11) NOT NULL,
  `vaccine_name` varchar(100) NOT NULL,
  `date_administered` date NOT NULL,
  `next_due_date` date DEFAULT NULL,
  `status` varchar(20) DEFAULT 'Completed',
  `notes` text DEFAULT NULL,
  `created_at` timestamp DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`vaccine_id`),
  KEY `pet_id` (`pet_id`),
  CONSTRAINT `fk_pet_vaccine` FOREIGN KEY (`pet_id`) REFERENCES `pets` (`id`) ON DELETE CASCADE
);

SET @tablename = "vaccines";
-- Add 'file_path'
SET @columnname = "file_path";
SET @preparedStatement = (SELECT IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE (table_name = @tablename) AND (table_schema = @dbname) AND (column_name = @columnname)) > 0,
  "SELECT 1",
  "ALTER TABLE vaccines ADD COLUMN file_path VARCHAR(255) DEFAULT NULL;"
));
PREPARE alterIfNotExists FROM @preparedStatement; EXECUTE alterIfNotExists; DEALLOCATE PREPARE alterIfNotExists;

-- 4. Create `pet_medical_history` table (Code uses 'pet_medical_history')
CREATE TABLE IF NOT EXISTS `pet_medical_history` (
  `record_id` int(11) NOT NULL AUTO_INCREMENT,
  `pet_id` int(11) NOT NULL,
  `record_type` varchar(50) NOT NULL,
  `title` varchar(150) NOT NULL,
  `record_date` date NOT NULL,
  `veterinarian` varchar(100) DEFAULT NULL,
  `file_path` varchar(255) DEFAULT NULL,
  `created_at` timestamp DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`record_id`),
  KEY `pet_id` (`pet_id`),
  CONSTRAINT `fk_pet_medical` FOREIGN KEY (`pet_id`) REFERENCES `pets` (`id`) ON DELETE CASCADE
);
