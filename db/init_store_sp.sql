-- Shopping website database initialization script
-- Tech stack target: Spring Boot + MyBatis-Plus + MySQL 8.x

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

DROP DATABASE IF EXISTS `store_sp`;
CREATE DATABASE `store_sp`
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE `store_sp`;

-- ----------------------------
-- 1. User table
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Primary key',
    `username` VARCHAR(50) NOT NULL COMMENT 'Username',
    `password` VARCHAR(100) NOT NULL COMMENT 'Encrypted password',
    `nickname` VARCHAR(50) DEFAULT NULL COMMENT 'Nickname',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT 'Phone number',
    `avatar` VARCHAR(255) DEFAULT NULL COMMENT 'Avatar URL',
    `role` TINYINT NOT NULL DEFAULT 0 COMMENT 'Role: 0-user',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT 'Status: 0-disabled, 1-enabled',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT 'Logical delete flag',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Create time',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update time',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_username` (`username`),
    UNIQUE KEY `uk_user_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='User table';

-- ----------------------------
-- 2. Admin table
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Primary key',
    `username` VARCHAR(50) NOT NULL COMMENT 'Admin username',
    `password` VARCHAR(100) NOT NULL COMMENT 'Encrypted password',
    `name` VARCHAR(50) NOT NULL COMMENT 'Admin name',
    `role` TINYINT NOT NULL DEFAULT 1 COMMENT 'Role: 1-super admin, 2-operator',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT 'Status: 0-disabled, 1-enabled',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT 'Logical delete flag',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Create time',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update time',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_admin_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Admin table';

-- ----------------------------
-- 3. Banner table
-- ----------------------------
DROP TABLE IF EXISTS `banner`;
CREATE TABLE `banner` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Primary key',
    `title` VARCHAR(100) NOT NULL COMMENT 'Banner title',
    `subtitle` VARCHAR(255) DEFAULT NULL COMMENT 'Banner subtitle',
    `image_url` VARCHAR(255) NOT NULL COMMENT 'Banner image url',
    `link_url` VARCHAR(255) DEFAULT NULL COMMENT 'Jump link',
    `sort` INT NOT NULL DEFAULT 0 COMMENT 'Sort order',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT 'Status: 0-disabled, 1-enabled',
    `create_by` BIGINT DEFAULT NULL COMMENT 'Creator admin id',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT 'Logical delete flag',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Create time',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update time',
    PRIMARY KEY (`id`),
    KEY `idx_banner_status_sort` (`status`, `sort`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Homepage banner table';

-- ----------------------------
-- 4. Category table
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Primary key',
    `name` VARCHAR(100) NOT NULL COMMENT 'Category name',
    `sort` INT NOT NULL DEFAULT 0 COMMENT 'Sort order',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT 'Status: 0-disabled, 1-enabled',
    `create_by` BIGINT DEFAULT NULL COMMENT 'Creator admin id',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT 'Logical delete flag',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Create time',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update time',
    PRIMARY KEY (`id`),
    KEY `idx_category_status_sort` (`status`, `sort`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Product category table';

-- ----------------------------
-- 5. Product table
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Primary key',
    `category_id` BIGINT NOT NULL COMMENT 'Category id',
    `name` VARCHAR(150) NOT NULL COMMENT 'Product name',
    `subtitle` VARCHAR(255) DEFAULT NULL COMMENT 'Product subtitle',
    `price` DECIMAL(10, 2) NOT NULL DEFAULT 0.00 COMMENT 'Sale price',
    `stock` INT NOT NULL DEFAULT 0 COMMENT 'Inventory',
    `sales` INT NOT NULL DEFAULT 0 COMMENT 'Sales volume',
    `cover_image` VARCHAR(255) DEFAULT NULL COMMENT 'Cover image',
    `images` TEXT DEFAULT NULL COMMENT 'Product image list',
    `detail` TEXT DEFAULT NULL COMMENT 'Product detail',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT 'Status: 0-off shelf, 1-on shelf',
    `create_by` BIGINT DEFAULT NULL COMMENT 'Creator admin id',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT 'Logical delete flag',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Create time',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update time',
    PRIMARY KEY (`id`),
    KEY `idx_product_category_id` (`category_id`),
    KEY `idx_product_status` (`status`),
    KEY `idx_product_name` (`name`),
    CONSTRAINT `fk_product_category` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Product table';

-- ----------------------------
-- 6. Shopping cart table
-- ----------------------------
DROP TABLE IF EXISTS `cart`;
CREATE TABLE `cart` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Primary key',
    `user_id` BIGINT NOT NULL COMMENT 'User id',
    `product_id` BIGINT NOT NULL COMMENT 'Product id',
    `quantity` INT NOT NULL DEFAULT 1 COMMENT 'Purchase quantity',
    `checked` TINYINT NOT NULL DEFAULT 1 COMMENT 'Whether selected: 0-no, 1-yes',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT 'Logical delete flag',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Create time',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update time',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_cart_user_product` (`user_id`, `product_id`),
    KEY `idx_cart_user_id` (`user_id`),
    KEY `idx_cart_product_id` (`product_id`),
    CONSTRAINT `fk_cart_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
    CONSTRAINT `fk_cart_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Shopping cart table';

-- ----------------------------
-- 7. User address table
-- ----------------------------
DROP TABLE IF EXISTS `user_address`;
CREATE TABLE `user_address` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Primary key',
    `user_id` BIGINT NOT NULL COMMENT 'User id',
    `receiver_name` VARCHAR(50) NOT NULL COMMENT 'Receiver name',
    `receiver_phone` VARCHAR(20) NOT NULL COMMENT 'Receiver phone',
    `province` VARCHAR(50) NOT NULL COMMENT 'Province',
    `city` VARCHAR(50) NOT NULL COMMENT 'City',
    `district` VARCHAR(50) DEFAULT NULL COMMENT 'District',
    `detail_address` VARCHAR(255) NOT NULL COMMENT 'Detail address',
    `is_default` TINYINT NOT NULL DEFAULT 0 COMMENT 'Default address: 0-no, 1-yes',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT 'Logical delete flag',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Create time',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update time',
    PRIMARY KEY (`id`),
    KEY `idx_address_user_id` (`user_id`),
    KEY `idx_address_user_default` (`user_id`, `is_default`),
    CONSTRAINT `fk_address_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='User address table';

-- ----------------------------
-- 8. Order table
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Primary key',
    `order_no` VARCHAR(32) NOT NULL COMMENT 'Order number',
    `user_id` BIGINT NOT NULL COMMENT 'User id',
    `address_id` BIGINT DEFAULT NULL COMMENT 'Address id',
    `total_amount` DECIMAL(10, 2) NOT NULL DEFAULT 0.00 COMMENT 'Order total amount',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT 'Status: 0-pending payment, 1-pending shipment, 2-shipped, 3-completed, 4-cancelled',
    `remark` VARCHAR(255) DEFAULT NULL COMMENT 'Order remark',
    `receiver_name` VARCHAR(50) NOT NULL COMMENT 'Receiver name snapshot',
    `receiver_phone` VARCHAR(20) NOT NULL COMMENT 'Receiver phone snapshot',
    `receiver_address` VARCHAR(255) NOT NULL COMMENT 'Receiver full address snapshot',
    `payment_time` DATETIME DEFAULT NULL COMMENT 'Payment time',
    `delivery_time` DATETIME DEFAULT NULL COMMENT 'Delivery time',
    `receive_time` DATETIME DEFAULT NULL COMMENT 'Receive time',
    `express_company` VARCHAR(100) DEFAULT NULL COMMENT 'Express company',
    `express_no` VARCHAR(64) DEFAULT NULL COMMENT 'Express number',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT 'Logical delete flag',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Create time',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update time',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_orders_order_no` (`order_no`),
    KEY `idx_orders_user_id` (`user_id`),
    KEY `idx_orders_status` (`status`),
    KEY `idx_orders_create_time` (`create_time`),
    CONSTRAINT `fk_orders_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
    CONSTRAINT `fk_orders_address` FOREIGN KEY (`address_id`) REFERENCES `user_address` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Order table';

-- ----------------------------
-- 9. Order item table
-- ----------------------------
DROP TABLE IF EXISTS `order_item`;
CREATE TABLE `order_item` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Primary key',
    `order_id` BIGINT NOT NULL COMMENT 'Order id',
    `product_id` BIGINT NOT NULL COMMENT 'Product id',
    `product_name` VARCHAR(150) NOT NULL COMMENT 'Product name snapshot',
    `product_price` DECIMAL(10, 2) NOT NULL DEFAULT 0.00 COMMENT 'Product price snapshot',
    `product_image` VARCHAR(255) DEFAULT NULL COMMENT 'Product image snapshot',
    `quantity` INT NOT NULL DEFAULT 1 COMMENT 'Purchase quantity',
    `subtotal` DECIMAL(10, 2) NOT NULL DEFAULT 0.00 COMMENT 'Subtotal amount',
    PRIMARY KEY (`id`),
    KEY `idx_order_item_order_id` (`order_id`),
    KEY `idx_order_item_product_id` (`product_id`),
    CONSTRAINT `fk_order_item_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
    CONSTRAINT `fk_order_item_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Order item table';

-- ----------------------------
-- Seed data
-- ----------------------------
INSERT INTO `admin` (`username`, `password`, `name`, `role`, `status`)
VALUES ('admin', '123456', 'Super Admin', 1, 1);

INSERT INTO `category` (`name`, `sort`, `status`, `create_by`)
VALUES
    ('手机数码', 1, 1, 1),
    ('电脑办公', 2, 1, 1),
    ('家用电器', 3, 1, 1);

INSERT INTO `banner` (`title`, `subtitle`, `image_url`, `link_url`, `sort`, `status`, `create_by`)
VALUES
    ('春季数码焕新', '精选热门商品，打造更具品质感的购物首页视觉', '/upload/banner/banner-1.jpg', '/product/list?categoryId=1', 1, 1, 1),
    ('轻薄办公专场', '聚焦电脑办公品类，适合首页轮播与专题跳转', '/upload/banner/banner-2.jpg', '/product/list?categoryId=2', 2, 1, 1),
    ('家居生活灵感', '让家电与生活方式内容一起进入首页推荐区', '/upload/banner/banner-3.jpg', '/product/list?categoryId=3', 3, 1, 1);

INSERT INTO `product` (`category_id`, `name`, `subtitle`, `price`, `stock`, `sales`, `cover_image`, `images`, `detail`, `status`, `create_by`)
VALUES
    (1, '智能手机', '高性能 5G 手机', 3999.00, 100, 0, '/upload/product/phone.jpg', '/upload/product/phone.jpg', '第一阶段演示商品', 1, 1),
    (2, '轻薄笔记本', '适合学习与办公', 5299.00, 50, 0, '/upload/product/laptop.jpg', '/upload/product/laptop.jpg', '第二阶段演示商品', 1, 1),
    (3, '空气炸锅', '家庭厨房小家电', 299.00, 200, 0, '/upload/product/fryer.jpg', '/upload/product/fryer.jpg', '商品详情可在后台维护', 1, 1);

SET FOREIGN_KEY_CHECKS = 1;
