-- Database: `petpulse`

CREATE DATABASE IF NOT EXISTS `petpulse`;
USE `petpulse`;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE IF NOT EXISTS `users` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `full_name` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `otp` varchar(6) DEFAULT NULL,
  `is_verified` tinyint(1) DEFAULT 0,
  `is_premium` tinyint(1) DEFAULT 0,
  `created_at` timestamp DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `email` (`email`)
);

-- --------------------------------------------------------

--
-- Table structure for table `pets`
--

CREATE TABLE IF NOT EXISTS `pets` (
  `pet_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `species` varchar(50) DEFAULT NULL,
  `breed` varchar(100) DEFAULT NULL,
  `gender` varchar(10) DEFAULT NULL,
  `age` varchar(20) DEFAULT NULL,
  `weight` varchar(20) DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `created_at` timestamp DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`pet_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `fk_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
);

-- --------------------------------------------------------

--
-- Table structure for table `pet_medical_history`
--

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
  CONSTRAINT `fk_pet_medical` FOREIGN KEY (`pet_id`) REFERENCES `pets` (`pet_id`) ON DELETE CASCADE
);

-- --------------------------------------------------------

--
-- Table structure for table `vaccines`
--

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
  CONSTRAINT `fk_pet_vaccine` FOREIGN KEY (`pet_id`) REFERENCES `pets` (`pet_id`) ON DELETE CASCADE
);

