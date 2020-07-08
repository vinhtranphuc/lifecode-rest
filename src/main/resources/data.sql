-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               10.3.10-MariaDB - mariadb.org binary distribution
-- Server OS:                    Win64
-- HeidiSQL Version:             9.4.0.5125
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Dumping database structure for lifecode
CREATE DATABASE IF NOT EXISTS `lifecode` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */;
USE `lifecode`;

-- Dumping structure for table lifecode.categories
CREATE TABLE IF NOT EXISTS `categories` (
  `category_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `category` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `category_img` varchar(70) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table lifecode.categories: ~4 rows (approximately)
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
INSERT IGNORE INTO `categories` (`category_id`, `category`, `category_img`) VALUES
	(1, 'Life', '/img/header-2.jpg'),
	(2, 'Code', '/img/header-2.jpg'),
	(3, 'Photographs', '/img/header-2.jpg'),
	(4, 'Travel', '/img/header-2.jpg');
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;

-- Dumping structure for table lifecode.comments
CREATE TABLE IF NOT EXISTS `comments` (
  `comment_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  `comment` longtext COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `comment_parent_id` bigint(20) DEFAULT NULL,
  `post_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`comment_id`),
  KEY `FKh4c7lvsc298whoyd4w9ta25cr` (`post_id`),
  KEY `FK8omq0tc18jd43bu5tjh6jvraq` (`user_id`),
  CONSTRAINT `FK8omq0tc18jd43bu5tjh6jvraq` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `FKh4c7lvsc298whoyd4w9ta25cr` FOREIGN KEY (`post_id`) REFERENCES `posts` (`post_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table lifecode.comments: ~0 rows (approximately)
/*!40000 ALTER TABLE `comments` DISABLE KEYS */;
/*!40000 ALTER TABLE `comments` ENABLE KEYS */;

-- Dumping structure for table lifecode.images
CREATE TABLE IF NOT EXISTS `images` (
  `image_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `file_name` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`image_id`)
) ENGINE=InnoDB AUTO_INCREMENT=66 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table lifecode.images: ~22 rows (approximately)
/*!40000 ALTER TABLE `images` DISABLE KEYS */;
INSERT IGNORE INTO `images` (`image_id`, `file_name`) VALUES
	(24, '20200617011718972_1.png'),
	(32, '20200617012549834_1.png'),
	(36, '20200617012719939_1.png'),
	(37, '20200617012808265_1.png'),
	(40, '20200617014153323_1.png'),
	(41, '20200617014229754_1.png'),
	(42, '20200617014351853_1.png'),
	(43, '20200617014436792_1.png'),
	(44, '20200617014601814_1.png'),
	(45, '20200617014739418_1.png'),
	(46, '20200617014805885_1.png'),
	(47, '20200617015021044_1.png'),
	(48, '20200617015537099_1.png'),
	(49, '20200617022009151_1.png'),
	(50, '20200617022056164_1.png'),
	(51, '20200617022122894_1.png'),
	(52, '20200617022201325_1.png'),
	(53, '20200617022302024_1.png'),
	(58, '20200617023027342_1.png'),
	(63, '20200617023811750_1.png'),
	(64, '20200617023811775_2.png'),
	(65, '20200617023811780_3.png');
/*!40000 ALTER TABLE `images` ENABLE KEYS */;

-- Dumping structure for table lifecode.posts
CREATE TABLE IF NOT EXISTS `posts` (
  `post_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  `content` longtext COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `level` int(11) NOT NULL,
  `sumary` longtext COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `times_of_view` int(11) NOT NULL,
  `title` longtext COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `category_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`post_id`),
  KEY `FKijnwr3brs8vaosl80jg9rp7uc` (`category_id`),
  CONSTRAINT `FKijnwr3brs8vaosl80jg9rp7uc` FOREIGN KEY (`category_id`) REFERENCES `categories` (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table lifecode.posts: ~20 rows (approximately)
/*!40000 ALTER TABLE `posts` DISABLE KEYS */;
INSERT IGNORE INTO `posts` (`post_id`, `created_at`, `updated_at`, `content`, `level`, `sumary`, `times_of_view`, `title`, `category_id`) VALUES
	(1, '2020-06-16 20:41:36', '2020-06-17 01:44:36', '<html>\n <head></head>\n <body>\n  <p>Post 1</p>\n  <figure class="image">\n   <img src="20200616204136634_1.png">\n  </figure>\n </body>\n</html>', 1, NULL, 0, 'Post 1', 1),
	(2, '2020-06-16 20:42:38', '2020-06-17 01:17:19', '<html>\n <head></head>\n <body>\n  <p>Post 2</p>\n  <figure class="image">\n   <img src="20200616204238616_1.png">\n  </figure>\n </body>\n</html>', 2, NULL, 0, 'Post 2', 1),
	(3, '2020-06-16 20:43:44', '2020-06-17 01:25:49', '<html>\n <head></head>\n <body>\n  <p>Post 3</p>\n  <figure class="image">\n   <img src="20200616204344495_1.png">\n  </figure>\n </body>\n</html>', 3, NULL, 0, 'Post 3', 1),
	(4, '2020-06-16 20:52:49', '2020-06-17 01:55:37', '<html>\n <head></head>\n <body>\n  <p>Post 4</p>\n  <figure class="image">\n   <img src="20200616205249697_1.png">\n  </figure>\n </body>\n</html>', 3, NULL, 0, 'Post 4', 3),
	(5, '2020-06-16 20:53:42', '2020-06-17 01:37:19', '<html>\n <head></head>\n <body>\n  <p>Post 5</p>\n  <figure class="image">\n   <img src="20200616205342091_1.png">\n  </figure>\n </body>\n</html>', 1, NULL, 0, 'Post 5', 3),
	(6, '2020-06-16 21:00:33', '2020-06-17 01:28:59', '<html>\n <head></head>\n <body>\n  <p>Post 6</p>\n  <figure class="image">\n   <img src="20200616210033542_1.png">\n  </figure>\n </body>\n</html>', 5, NULL, 0, 'Post 6', 2),
	(7, '2020-06-16 21:01:39', '2020-06-17 01:41:53', '<html>\n <head></head>\n <body>\n  <p>Post 7</p>\n  <figure class="image">\n   <img src="20200616210139853_1.png">\n  </figure>\n </body>\n</html>', 4, NULL, 0, 'Post 7', 1),
	(8, '2020-06-17 01:42:29', '2020-06-17 01:43:05', '<html>\n <head></head>\n <body>\n  <p>Post 8</p>\n  <figure class="image">\n   <img src="20200617014229723_1.png">\n  </figure>\n </body>\n</html>', 4, NULL, 0, 'Post 8', 2),
	(9, '2020-06-17 01:43:51', '2020-06-17 01:45:06', '<html>\n <head></head>\n <body>\n  <p>Post 9</p>\n  <figure class="image">\n   <img src="20200617014351836_1.png">\n  </figure>\n </body>\n</html>', 3, NULL, 0, 'Post 9', 1),
	(10, '2020-06-17 01:46:01', '2020-06-17 01:46:01', '<html>\n <head></head>\n <body>\n  <p>Post 10</p>\n  <figure class="image">\n   <img src="20200617014601745_1.png">\n  </figure>\n </body>\n</html>', 3, NULL, 0, 'Post 10', 1),
	(11, '2020-06-17 01:47:39', '2020-06-17 01:48:13', '<html>\n <head></head>\n <body>\n  <p>Post 11</p>\n  <figure class="image">\n   <img src="20200617014739412_1.png">\n  </figure>\n </body>\n</html>', 3, NULL, 0, 'Post 11', 3),
	(12, '2020-06-17 01:48:05', '2020-06-17 01:48:08', '<html>\n <head></head>\n <body>\n  <p>Post 12</p>\n  <figure class="image">\n   <img src="20200617014805836_1.png">\n  </figure>\n </body>\n</html>', 3, NULL, 0, 'Post 12', 1),
	(13, '2020-06-17 01:50:21', '2020-06-17 01:50:21', '<html>\n <head></head>\n <body>\n  <p>Post 13</p>\n  <figure class="image">\n   <img src="20200617015021036_1.png">\n  </figure>\n </body>\n</html>', 2, NULL, 0, 'Post 13', 3),
	(14, '2020-06-17 02:20:09', '2020-06-17 02:20:09', '<html>\n <head></head>\n <body>\n  <p>Post 14</p>\n  <figure class="image">\n   <img src="20200617022009130_1.png">\n  </figure>\n </body>\n</html>', 2, NULL, 0, 'Post 14', 2),
	(15, '2020-06-17 02:20:56', '2020-06-17 02:20:56', '<html>\n <head></head>\n <body>\n  <p>Post 15</p>\n  <figure class="image">\n   <img src="20200617022056112_1.png">\n  </figure>\n </body>\n</html>', 3, NULL, 0, 'Post 15', 2),
	(16, '2020-06-17 02:21:22', '2020-06-17 02:21:22', '<html>\n <head></head>\n <body>\n  <p>Post 16</p>\n  <figure class="image">\n   <img src="20200617022122849_1.png">\n  </figure>\n </body>\n</html>', 3, NULL, 0, 'Post 16', 2),
	(17, '2020-06-17 02:22:01', '2020-06-17 02:22:01', '<html>\n <head></head>\n <body>\n  <p>post 17</p>\n  <figure class="image">\n   <img src="20200617022201278_1.png">\n  </figure>\n </body>\n</html>', 2, NULL, 0, 'post 17', 2),
	(18, '2020-06-17 02:23:02', '2020-06-17 02:23:02', '<html>\n <head></head>\n <body>\n  <p>Post 18</p>\n  <figure class="image">\n   <img src="20200617022302013_1.png">\n  </figure>\n </body>\n</html>', 3, NULL, 0, 'Post 18', 3),
	(19, '2020-06-17 02:23:48', '2020-06-17 02:30:27', '<html>\n <head></head>\n <body>\n  <p>Post 19</p>\n  <figure class="image">\n   <img src="20200617022348688_1.png">\n  </figure>\n </body>\n</html>', 3, NULL, 0, 'Post 19', 3),
	(20, '2020-06-17 02:31:53', '2020-06-17 02:38:11', '<html>\n <head></head>\n <body>\n  <p>Post 20</p>\n  <figure class="image">\n   <img src="20200617023153383_1.png">\n  </figure>\n </body>\n</html>', 3, NULL, 0, 'Post 20', 3);
/*!40000 ALTER TABLE `posts` ENABLE KEYS */;

-- Dumping structure for table lifecode.posts_authors
CREATE TABLE IF NOT EXISTS `posts_authors` (
  `post_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`post_id`,`user_id`),
  KEY `FKet0txk8jynytashy09ehfbpp3` (`user_id`),
  CONSTRAINT `FKet0txk8jynytashy09ehfbpp3` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `FKpq7bkes3d57iv8hd6qrkkclee` FOREIGN KEY (`post_id`) REFERENCES `posts` (`post_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table lifecode.posts_authors: ~0 rows (approximately)
/*!40000 ALTER TABLE `posts_authors` DISABLE KEYS */;
/*!40000 ALTER TABLE `posts_authors` ENABLE KEYS */;

-- Dumping structure for table lifecode.posts_images
CREATE TABLE IF NOT EXISTS `posts_images` (
  `post_id` bigint(20) NOT NULL,
  `image_id` bigint(20) NOT NULL,
  PRIMARY KEY (`post_id`,`image_id`),
  KEY `FKhyokhj94vfhxityhbeeb51pmb` (`image_id`),
  CONSTRAINT `FKhyokhj94vfhxityhbeeb51pmb` FOREIGN KEY (`image_id`) REFERENCES `images` (`image_id`),
  CONSTRAINT `FKr315ebcveolhvos3mj05cqmit` FOREIGN KEY (`post_id`) REFERENCES `posts` (`post_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table lifecode.posts_images: ~22 rows (approximately)
/*!40000 ALTER TABLE `posts_images` DISABLE KEYS */;
INSERT IGNORE INTO `posts_images` (`post_id`, `image_id`) VALUES
	(1, 43),
	(2, 24),
	(3, 32),
	(4, 48),
	(5, 36),
	(6, 37),
	(7, 40),
	(8, 41),
	(9, 42),
	(10, 44),
	(11, 45),
	(12, 46),
	(13, 47),
	(14, 49),
	(15, 50),
	(16, 51),
	(17, 52),
	(18, 53),
	(19, 58),
	(20, 63),
	(20, 64),
	(20, 65);
/*!40000 ALTER TABLE `posts_images` ENABLE KEYS */;

-- Dumping structure for table lifecode.posts_tags
CREATE TABLE IF NOT EXISTS `posts_tags` (
  `post_id` bigint(20) NOT NULL,
  `tag_id` bigint(20) NOT NULL,
  PRIMARY KEY (`post_id`,`tag_id`),
  KEY `FK4svsmj4juqu2l8yaw6whr1v4v` (`tag_id`),
  CONSTRAINT `FK4svsmj4juqu2l8yaw6whr1v4v` FOREIGN KEY (`tag_id`) REFERENCES `tags` (`tag_id`),
  CONSTRAINT `FKcreclgob71ibo58gsm6l5wp6` FOREIGN KEY (`post_id`) REFERENCES `posts` (`post_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table lifecode.posts_tags: ~32 rows (approximately)
/*!40000 ALTER TABLE `posts_tags` DISABLE KEYS */;
INSERT IGNORE INTO `posts_tags` (`post_id`, `tag_id`) VALUES
	(1, 3),
	(1, 5),
	(2, 5),
	(3, 1),
	(4, 1),
	(4, 3),
	(5, 1),
	(6, 3),
	(6, 5),
	(7, 1),
	(8, 1),
	(8, 5),
	(9, 1),
	(9, 2),
	(10, 1),
	(11, 1),
	(12, 1),
	(13, 6),
	(13, 8),
	(14, 3),
	(14, 7),
	(15, 1),
	(16, 4),
	(16, 6),
	(17, 3),
	(17, 5),
	(18, 3),
	(18, 5),
	(19, 3),
	(19, 5),
	(20, 3),
	(20, 7);
/*!40000 ALTER TABLE `posts_tags` ENABLE KEYS */;

-- Dumping structure for table lifecode.roles
CREATE TABLE IF NOT EXISTS `roles` (
  `role_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(60) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`role_id`),
  UNIQUE KEY `UK_nb4h0p6txrmfc0xbrd1kglp9t` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table lifecode.roles: ~2 rows (approximately)
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT IGNORE INTO `roles` (`role_id`, `name`) VALUES
	(2, 'ROLE_ADMIN'),
	(1, 'ROLE_USER');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;

-- Dumping structure for table lifecode.tags
CREATE TABLE IF NOT EXISTS `tags` (
  `tag_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tag` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`tag_id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table lifecode.tags: ~16 rows (approximately)
/*!40000 ALTER TABLE `tags` DISABLE KEYS */;
INSERT IGNORE INTO `tags` (`tag_id`, `tag`) VALUES
	(1, 'Java'),
	(2, 'Java Script'),
	(3, 'Python'),
	(4, 'SQL'),
	(5, 'Lifestyle'),
	(6, 'Food'),
	(7, 'Adventure'),
	(8, 'Travel'),
	(9, 'Bussiness'),
	(10, 'Contry'),
	(11, 'Cultural'),
	(12, 'City'),
	(13, 'Life'),
	(14, 'Family'),
	(15, 'Friends'),
	(16, 'Company');
/*!40000 ALTER TABLE `tags` ENABLE KEYS */;

-- Dumping structure for table lifecode.users
CREATE TABLE IF NOT EXISTS `users` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  `address` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `avatar_img` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `city` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `company_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `country` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `email` varchar(40) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `facebook` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `instagram` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `join_date` datetime DEFAULT NULL,
  `linkedin` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `name` varchar(40) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `note` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `occupation` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `password` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `phone` int(11) NOT NULL,
  `twitter` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `type` varchar(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `user_type_cd` varchar(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `username` varchar(15) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `UKr43af9ap4edm43mmtq01oddj6` (`username`),
  UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table lifecode.users: ~4 rows (approximately)
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT IGNORE INTO `users` (`user_id`, `created_at`, `updated_at`, `address`, `avatar_img`, `city`, `company_name`, `country`, `email`, `facebook`, `instagram`, `join_date`, `linkedin`, `name`, `note`, `occupation`, `password`, `phone`, `twitter`, `type`, `user_type_cd`, `username`) VALUES
	(1, '2020-01-05 04:18:56', '2020-01-05 04:18:56', NULL, NULL, NULL, NULL, NULL, 'vinhtran@gmail.com', NULL, NULL, NULL, NULL, 'Tran Phuc Vinh', NULL, NULL, '$2a$10$5ERgZqDh0NwGqVA2qbwE0.oRfkSRrS.v.TkfIXaNP2Z6W16YYcvSa', 0, NULL, '0', '0', 'admin'),
	(2, '2020-02-08 14:06:39', '2020-02-08 14:06:39', NULL, NULL, NULL, NULL, NULL, 'vinhtran123@gmail.com', NULL, NULL, '2020-02-08 14:06:39', NULL, 'Tran Phuc Vinh', NULL, NULL, '$2a$10$LFN01fnOwOj5dU4U73PNi.g8wP.9ZrJ1ptdPPmrkOVj5UpJWYk1Sm', 0, NULL, NULL, NULL, 'vinhtran123'),
	(3, '2020-02-15 14:30:21', '2020-02-15 14:30:21', NULL, NULL, NULL, NULL, NULL, 'vinhtranphuc@gmail.com', NULL, NULL, '2020-02-15 14:30:21', NULL, 'Tran Phuc Vinh', NULL, NULL, '$2a$10$tzc6Pa6uOM0yzSZG8S/lYuDtVsQIN.X8OwyiPcLj5hnDKKZgnvasq', 0, NULL, NULL, NULL, 'tranphucvinh'),
	(4, '2020-03-02 11:32:11', '2020-03-02 11:32:11', NULL, NULL, NULL, NULL, NULL, 'thuy@mailinator.com', NULL, NULL, '2020-03-02 11:32:11', NULL, 'Nguyen Thi Thanh Thuy', NULL, NULL, '$2a$10$Z7fvDLKs8os5.iDAFBh3XObWKgSGPAOqY6VOI8D5NqH5YiDB9HlDS', 0, NULL, NULL, NULL, 'thanhthuy');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;

-- Dumping structure for table lifecode.user_roles
CREATE TABLE IF NOT EXISTS `user_roles` (
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `FKh8ciramu9cc9q3qcqiv4ue8a6` (`role_id`),
  CONSTRAINT `FKh8ciramu9cc9q3qcqiv4ue8a6` FOREIGN KEY (`role_id`) REFERENCES `roles` (`role_id`),
  CONSTRAINT `FKhfh9dx7w3ubf1co1vdev94g3f` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table lifecode.user_roles: ~4 rows (approximately)
/*!40000 ALTER TABLE `user_roles` DISABLE KEYS */;
INSERT IGNORE INTO `user_roles` (`user_id`, `role_id`) VALUES
	(1, 1),
	(2, 1),
	(3, 1),
	(4, 1);
/*!40000 ALTER TABLE `user_roles` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
