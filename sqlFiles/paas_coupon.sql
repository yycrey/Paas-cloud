/*
 Navicat Premium Data Transfer

 Source Server         : mysql
 Source Server Type    : MySQL
 Source Server Version : 50744
 Source Host           : localhost:3306
 Source Schema         : paas_coupon

 Target Server Type    : MySQL
 Target Server Version : 50744
 File Encoding         : 65001

 Date: 12/01/2025 21:10:05
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for coupon
-- ----------------------------
DROP TABLE IF EXISTS `coupon`;
CREATE TABLE `coupon`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `category` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '优惠卷类型[NEW_USER注册赠券，TASK任务卷，PROMOTION促销劵]',
  `publish` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发布状态, PUBLISH发布，DRAFT草稿，OFFLINE下线',
  `coupon_img` varchar(524) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '优惠券图片',
  `coupon_title` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '优惠券标题',
  `price` decimal(16, 2) NULL DEFAULT NULL COMMENT '抵扣价格',
  `user_limit` int(11) NULL DEFAULT NULL COMMENT '每人限制张数',
  `start_time` datetime NULL DEFAULT NULL COMMENT '优惠券开始有效时间',
  `end_time` datetime NULL DEFAULT NULL COMMENT '优惠券失效时间',
  `publish_count` int(11) NULL DEFAULT NULL COMMENT '优惠券总量',
  `stock` int(11) NULL DEFAULT 0 COMMENT '库存',
  `create_time` datetime NULL DEFAULT NULL,
  `condition_price` decimal(16, 2) NULL DEFAULT NULL COMMENT '满多少才可以使用',
  `version` int(11) NULL DEFAULT NULL COMMENT '版本号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '优惠券' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of coupon
-- ----------------------------
INSERT INTO `coupon` VALUES (18, 'NEW_USER', 'PUBLISH', 'https://file.xdclass.net/video/2020/alibabacloud/zt-alibabacloud.png', '永久有效-新人注册-0元满减-5元抵扣劵-限领取2张-不可叠加使用', 5.00, 2, '2000-01-01 00:00:00', '2099-01-29 00:00:00', 100000000, 99999991, '2020-12-26 16:33:02', 0.00, NULL);
INSERT INTO `coupon` VALUES (19, 'NEW_USER', 'PUBLISH', 'https://file.xdclass.net/video/2020/alibabacloud/zt-alibabacloud.png', '有效中-21年1月到25年1月-20元满减-5元抵扣劵-限领取2张-不可叠加使用', 5.00, 2, '2000-01-29 00:00:00', '2025-01-29 00:00:00', 10, 0, '2020-12-26 16:33:03', 20.00, 20);
INSERT INTO `coupon` VALUES (20, 'PROMOTION', 'PUBLISH', 'https://file.xdclass.net/video/2020/alibabacloud/zt-alibabacloud.png', '有效中-20年8月到21年9月-商品id1-8.8元抵扣劵-限领取2张-不可叠加使用', 8.80, 2, '2020-08-01 00:00:00', '2021-09-29 00:00:00', 100, 96, '2020-12-26 16:33:03', 0.00, NULL);
INSERT INTO `coupon` VALUES (21, 'PROMOTION', 'PUBLISH', 'https://file.xdclass.net/video/2020/alibabacloud/zt-alibabacloud.png', '有效中-20年8月到21年9月-商品id2-9.9元抵扣劵-限领取2张-可叠加使用', 8.80, 2, '2020-08-01 00:00:00', '2021-09-29 00:00:00', 100, 96, '2020-12-26 16:33:03', 0.00, NULL);
INSERT INTO `coupon` VALUES (22, 'PROMOTION', 'PUBLISH', 'https://file.xdclass.net/video/2020/alibabacloud/zt-alibabacloud.png', '过期-20年8月到20年9月-商品id3-6元抵扣劵-限领取1张-可叠加使用', 6.00, 1, '2020-08-01 00:00:00', '2020-09-29 00:00:00', 100, 100, '2020-12-26 16:33:03', 0.00, NULL);

-- ----------------------------
-- Table structure for coupon_record
-- ----------------------------
DROP TABLE IF EXISTS `coupon_record`;
CREATE TABLE `coupon_record`  (
  `id` bigint(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `coupon_id` bigint(11) NULL DEFAULT NULL COMMENT '优惠券id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间获得时间',
  `use_state` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '使用状态  可用 NEW,已使用USED,过期 EXPIRED;',
  `user_id` bigint(11) NULL DEFAULT NULL COMMENT '用户id',
  `user_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户昵称',
  `coupon_title` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '优惠券标题',
  `start_time` datetime NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime NULL DEFAULT NULL COMMENT '结束时间',
  `order_id` bigint(11) NULL DEFAULT NULL COMMENT '订单id',
  `price` decimal(16, 2) NULL DEFAULT NULL COMMENT '抵扣价格',
  `condition_price` decimal(16, 2) NULL DEFAULT NULL COMMENT '满多少才可以使用',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '优惠券领劵记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of coupon_record
-- ----------------------------
INSERT INTO `coupon_record` VALUES (1, 19, '2025-01-02 20:32:14', 'USED', 9, 'yeyc', '有效中-21年1月到25年1月-20元满减-5元抵扣劵-限领取2张-不可叠加使用', '2000-01-29 00:00:00', '2025-01-29 00:00:00', NULL, 5.00, 20.00);
INSERT INTO `coupon_record` VALUES (3, 19, '2025-01-02 20:45:16', 'NEW', 9, 'yeyc', '有效中-21年1月到25年1月-20元满减-5元抵扣劵-限领取2张-不可叠加使用', '2000-01-29 00:00:00', '2025-01-29 00:00:00', NULL, 5.00, 20.00);
INSERT INTO `coupon_record` VALUES (4, 19, '2025-01-02 20:45:16', 'NEW', 9, 'yeyc', '有效中-21年1月到25年1月-20元满减-5元抵扣劵-限领取2张-不可叠加使用', '2000-01-29 00:00:00', '2025-01-29 00:00:00', NULL, 5.00, 20.00);

-- ----------------------------
-- Table structure for coupon_task
-- ----------------------------
DROP TABLE IF EXISTS `coupon_task`;
CREATE TABLE `coupon_task`  (
  `id` bigint(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `coupon_record_id` bigint(11) NULL DEFAULT NULL COMMENT '优惠券记录id',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `out_trade_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单号',
  `lock_state` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '锁定状态 锁定LOCK-完成FINISH 取消CANCEL',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '优惠券记录锁定任务表设计' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of coupon_task
-- ----------------------------
INSERT INTO `coupon_task` VALUES (1, 1, '2021-02-27 16:05:11', '123456abc', 'FINISH');

-- ----------------------------
-- Table structure for undo_log
-- ----------------------------
DROP TABLE IF EXISTS `undo_log`;
CREATE TABLE `undo_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `branch_id` bigint(20) NOT NULL,
  `xid` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `context` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `rollback_info` longblob NOT NULL,
  `log_status` int(11) NOT NULL,
  `log_created` datetime NOT NULL,
  `log_modified` datetime NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ux_undo_log`(`xid`, `branch_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'seata 日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of undo_log
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
