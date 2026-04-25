/*
 Navicat Premium Dump SQL

 Source Server         : link
 Source Server Type    : MySQL
 Source Server Version : 80042 (8.0.42)
 Source Host           : localhost:3306
 Source Schema         : store_sp

 Target Server Type    : MySQL
 Target Server Version : 80042 (8.0.42)
 File Encoding         : 65001

 Date: 25/04/2026 14:19:00
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'Primary key',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Admin username',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Encrypted password',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Admin name',
  `role` tinyint NOT NULL DEFAULT 1 COMMENT 'Role: 1-super admin, 2-operator',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT 'Status: 0-disabled, 1-enabled',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT 'Logical delete flag',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Create time',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update time',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_admin_username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'Admin table' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES (1, 'admin', '123456', 'Super Admin', 1, 1, 0, '2026-04-24 22:50:14', '2026-04-24 22:50:14');

-- ----------------------------
-- Table structure for banner
-- ----------------------------
DROP TABLE IF EXISTS `banner`;
CREATE TABLE `banner`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'Primary key',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Banner title',
  `subtitle` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'Banner subtitle',
  `image_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Banner image url',
  `link_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'Jump link',
  `sort` int NOT NULL DEFAULT 0 COMMENT 'Sort order',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT 'Status: 0-disabled, 1-enabled',
  `create_by` bigint NULL DEFAULT NULL COMMENT 'Creator admin id',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT 'Logical delete flag',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Create time',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update time',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_banner_status_sort`(`status` ASC, `sort` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'Homepage banner table' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of banner
-- ----------------------------
INSERT INTO `banner` VALUES (1, '春季数码焕新', '精选热门商品，打造更具品质感的购物首页视觉', '/upload/2026/04/e2777c34-746a-4f8c-a862-92a0472f4b09.png', '/product/list?categoryId=1', 1, 1, 1, 0, '2026-04-24 22:50:14', '2026-04-25 00:11:17');
INSERT INTO `banner` VALUES (2, '轻薄办公专场', '聚焦电脑办公品类，适合首页轮播与专题跳转', '/upload/2026/04/4649299b-7d9b-43f1-af53-9105e61653c4.png', '/product/list?categoryId=2', 2, 1, 1, 0, '2026-04-24 22:50:14', '2026-04-25 00:09:21');
INSERT INTO `banner` VALUES (3, '家居生活灵感', '让家电与生活方式内容一起进入首页推荐区', '/upload/2026/04/a5828487-db86-4693-b1ff-c3fe122f0907.png', '/product/list?categoryId=3', 3, 1, 1, 0, '2026-04-24 22:50:14', '2026-04-25 00:07:14');

-- ----------------------------
-- Table structure for cart
-- ----------------------------
DROP TABLE IF EXISTS `cart`;
CREATE TABLE `cart`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'Primary key',
  `user_id` bigint NOT NULL COMMENT 'User id',
  `product_id` bigint NOT NULL COMMENT 'Product id',
  `quantity` int NOT NULL DEFAULT 1 COMMENT 'Purchase quantity',
  `checked` tinyint NOT NULL DEFAULT 1 COMMENT 'Whether selected: 0-no, 1-yes',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT 'Logical delete flag',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Create time',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update time',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_cart_user_product`(`user_id` ASC, `product_id` ASC) USING BTREE,
  INDEX `idx_cart_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_cart_product_id`(`product_id` ASC) USING BTREE,
  CONSTRAINT `fk_cart_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_cart_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'Shopping cart table' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cart
-- ----------------------------
INSERT INTO `cart` VALUES (1, 1, 1, 3, 0, 0, '2026-04-24 23:45:40', '2026-04-25 01:08:18');
INSERT INTO `cart` VALUES (2, 1, 2, 4, 1, 1, '2026-04-25 00:03:27', '2026-04-25 14:07:20');

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'Primary key',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Category name',
  `sort` int NOT NULL DEFAULT 0 COMMENT 'Sort order',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT 'Status: 0-disabled, 1-enabled',
  `create_by` bigint NULL DEFAULT NULL COMMENT 'Creator admin id',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT 'Logical delete flag',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Create time',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update time',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_category_status_sort`(`status` ASC, `sort` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'Product category table' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of category
-- ----------------------------
INSERT INTO `category` VALUES (1, '手机数码', 1, 1, 1, 0, '2026-04-24 22:50:14', '2026-04-24 22:50:14');
INSERT INTO `category` VALUES (2, '电脑办公', 2, 1, 1, 0, '2026-04-24 22:50:14', '2026-04-24 22:50:14');
INSERT INTO `category` VALUES (3, '家用电器', 3, 1, 1, 0, '2026-04-24 22:50:14', '2026-04-24 22:50:14');

-- ----------------------------
-- Table structure for order_item
-- ----------------------------
DROP TABLE IF EXISTS `order_item`;
CREATE TABLE `order_item`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'Primary key',
  `order_id` bigint NOT NULL COMMENT 'Order id',
  `product_id` bigint NOT NULL COMMENT 'Product id',
  `product_name` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Product name snapshot',
  `product_price` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT 'Product price snapshot',
  `product_image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'Product image snapshot',
  `quantity` int NOT NULL DEFAULT 1 COMMENT 'Purchase quantity',
  `subtotal` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT 'Subtotal amount',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_order_item_order_id`(`order_id` ASC) USING BTREE,
  INDEX `idx_order_item_product_id`(`product_id` ASC) USING BTREE,
  CONSTRAINT `fk_order_item_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_order_item_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'Order item table' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_item
-- ----------------------------
INSERT INTO `order_item` VALUES (1, 1, 2, '小米（MI）小米 17 Pro Max妙享背屏 第五代骁龙8至尊版 7500mAh金沙江电池 白色12GB+512GB 5G手机 国家补贴', 5489.00, '/upload/2026/04/07c16deb-31d3-4682-9d5f-51532fd3b381.png', 4, 21956.00);

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'Primary key',
  `order_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Order number',
  `user_id` bigint NOT NULL COMMENT 'User id',
  `address_id` bigint NULL DEFAULT NULL COMMENT 'Address id',
  `total_amount` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT 'Order total amount',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT 'Status: 0-pending payment, 1-pending shipment, 2-shipped, 3-completed, 4-cancelled',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'Order remark',
  `receiver_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Receiver name snapshot',
  `receiver_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Receiver phone snapshot',
  `receiver_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Receiver full address snapshot',
  `payment_time` datetime NULL DEFAULT NULL COMMENT 'Payment time',
  `delivery_time` datetime NULL DEFAULT NULL COMMENT 'Delivery time',
  `receive_time` datetime NULL DEFAULT NULL COMMENT 'Receive time',
  `express_company` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'Express company',
  `express_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'Express number',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT 'Logical delete flag',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Create time',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update time',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_orders_order_no`(`order_no` ASC) USING BTREE,
  INDEX `idx_orders_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_orders_status`(`status` ASC) USING BTREE,
  INDEX `idx_orders_create_time`(`create_time` ASC) USING BTREE,
  INDEX `fk_orders_address`(`address_id` ASC) USING BTREE,
  CONSTRAINT `fk_orders_address` FOREIGN KEY (`address_id`) REFERENCES `user_address` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_orders_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'Order table' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of orders
-- ----------------------------
INSERT INTO `orders` VALUES (1, '202604251407208113363', 1, 1, 21956.00, 2, '', '王满友', '15161792131', '江苏常州市武进区江苏省常州市武进区鸣新东路28号', NULL, '2026-04-25 14:13:17', NULL, '顺丰速运', '123456789', 0, '2026-04-25 14:07:20', '2026-04-25 14:07:20');

-- ----------------------------
-- Table structure for product
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'Primary key',
  `category_id` bigint NOT NULL COMMENT 'Category id',
  `name` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Product name',
  `subtitle` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'Product subtitle',
  `price` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT 'Sale price',
  `stock` int NOT NULL DEFAULT 0 COMMENT 'Inventory',
  `sales` int NOT NULL DEFAULT 0 COMMENT 'Sales volume',
  `cover_image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'Cover image',
  `images` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT 'Product image list',
  `detail` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT 'Product detail',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT 'Status: 0-off shelf, 1-on shelf',
  `create_by` bigint NULL DEFAULT NULL COMMENT 'Creator admin id',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT 'Logical delete flag',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Create time',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update time',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_product_category_id`(`category_id` ASC) USING BTREE,
  INDEX `idx_product_status`(`status` ASC) USING BTREE,
  INDEX `idx_product_name`(`name` ASC) USING BTREE,
  CONSTRAINT `fk_product_category` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'Product table' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of product
-- ----------------------------
INSERT INTO `product` VALUES (1, 1, '小米（MI）小米 17 Pro Max妙享背屏 第五代骁龙8至尊版 7500mAh金沙江电池 白色12GB+512GB 5G手机 国家补贴', '高性能 5G 手机', 5999.00, 100, 0, '/upload/2026/04/1e1b6f48-c249-4a49-b520-5dc48ac863b1.png', '/upload/2026/04/fcc4b5e0-eada-4e68-b8ff-7f4146b02ca7.png,/upload/2026/04/a47376b7-af92-4382-8ac7-09dae0b0d64d.png', '第一阶段演示商品', 1, 1, 0, '2026-04-24 22:50:14', '2026-04-25 00:28:25');
INSERT INTO `product` VALUES (2, 2, '小米（MI）小米 17 Pro Max妙享背屏 第五代骁龙8至尊版 7500mAh金沙江电池 白色12GB+512GB 5G手机 国家补贴', '适合学习与办公', 5489.00, 46, 4, '/upload/2026/04/07c16deb-31d3-4682-9d5f-51532fd3b381.png', '/upload/2026/04/a80f6438-b06d-4ede-b9e9-4a510b92675b.png,/upload/2026/04/96bf5e8b-3c5f-47a8-981e-0e1d98d25c3a.png,/upload/2026/04/5f224248-3b67-4473-94fb-1b162a9b4504.png', '第二阶段演示商品', 1, 1, 0, '2026-04-24 22:50:14', '2026-04-25 00:32:54');
INSERT INTO `product` VALUES (3, 3, '九阳（Joyoung）免翻面高效烘烤蒸汽嫩炸 蒸烤炸一体空气炸锅5.3L大容量家用实用多功能全自动不用翻面 KL50-V515', '家庭厨房小家电', 148.59, 200, 0, '/upload/2026/04/81c52da0-4e42-490f-a208-ff226631fb73.png', '/upload/product/fryer.jpg', '商品详情可在后台维护', 1, 1, 0, '2026-04-24 22:50:14', '2026-04-24 23:49:32');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'Primary key',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Username',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Encrypted password',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'Nickname',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'Phone number',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'Avatar URL',
  `role` tinyint NOT NULL DEFAULT 0 COMMENT 'Role: 0-user',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT 'Status: 0-disabled, 1-enabled',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT 'Logical delete flag',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Create time',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update time',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_username`(`username` ASC) USING BTREE,
  UNIQUE INDEX `uk_user_phone`(`phone` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'User table' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'wangmanyou', '123456', 'wangmanyou', '13800000000', NULL, 0, 1, 0, '2026-04-24 23:32:06', '2026-04-24 23:32:06');

-- ----------------------------
-- Table structure for user_address
-- ----------------------------
DROP TABLE IF EXISTS `user_address`;
CREATE TABLE `user_address`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'Primary key',
  `user_id` bigint NOT NULL COMMENT 'User id',
  `receiver_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Receiver name',
  `receiver_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Receiver phone',
  `province` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Province',
  `city` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'City',
  `district` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'District',
  `detail_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'Detail address',
  `is_default` tinyint NOT NULL DEFAULT 0 COMMENT 'Default address: 0-no, 1-yes',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT 'Logical delete flag',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Create time',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update time',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_address_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_address_user_default`(`user_id` ASC, `is_default` ASC) USING BTREE,
  CONSTRAINT `fk_address_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'User address table' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_address
-- ----------------------------
INSERT INTO `user_address` VALUES (1, 1, '王满友', '15161792131', '江苏', '常州市', '武进区', '江苏省常州市武进区鸣新东路28号', 1, 0, '2026-04-25 14:07:16', '2026-04-25 14:07:16');

SET FOREIGN_KEY_CHECKS = 1;
