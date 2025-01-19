/*
 Navicat Premium Data Transfer

 Source Server         : mysql
 Source Server Type    : MySQL
 Source Server Version : 50744
 Source Host           : localhost:3306
 Source Schema         : paas_user

 Target Server Type    : MySQL
 Target Server Version : 50744
 File Encoding         : 65001

 Date: 12/01/2025 21:10:21
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for address
-- ----------------------------
DROP TABLE IF EXISTS `address`;
CREATE TABLE `address`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '⽤户id',
  `default_status` int(1) NULL DEFAULT NULL COMMENT '是否默认收货地址：0->否；1->是',
  `receive_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收发货⼈姓名',
  `phone` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收货⼈电话',
  `province` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '省/直辖市',
  `city` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '市',
  `region` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区',
  `detail_address` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '详细地址',
  `create_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '电商-公司收发货地址表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of address
-- ----------------------------
INSERT INTO `address` VALUES (1, 9, 0, 'GG老熊', '123123123123', '安徽省', '花山区', '马鞍山市', '123123', '2024-12-23 15:03:11');
INSERT INTO `address` VALUES (6, 9, 0, '小张', '13812345678', '北京市', '北京市', '朝阳区', '北京市朝阳区大屯路', '2024-12-29 15:39:26');

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
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of undo_log
-- ----------------------------

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `pwd` varchar(124) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密\r\n码',
  `head_img` varchar(524) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像',
  `slogan` varchar(524) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '⽤户签名',
  `sex` tinyint(2) NULL DEFAULT 1 COMMENT '0表示\r\n⼥，1表示男',
  `points` int(10) NULL DEFAULT 0 COMMENT '积\r\n分',
  `create_time` datetime NULL DEFAULT NULL,
  `mail` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮\r\n箱',
  `secret` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '盐，⽤于个⼈敏感信息处理',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `mail_idx`(`mail`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (9, 'yeyc', '$1$zVy3u74b$PYk1pHYIs8rJu//xS9nK.1', 'https://rey-paas-cloud.oss-cn-hangzhou.aliyuncs.com/test/2024/12/25/3d84327e213d4094914bfecb5581c115.jpg?Expires=1735103372&OSSAccessKeyId=TMP.3KhRwHPvYfzAwmrxytuJE7mbN1V5KD2HqmvxUF2ZBYZBEDSC3VPx2c2j7UVMqzeBMAsbTovUPyrtNh3c9VUKfSP2qisETd&Signature=MA8Mp0o3MT%2BWIv7a0ql%2FrDCXQNs%3D', 'https://rey-paas-cloud.oss-cn-hangzhou.aliyuncs.com/test/2024/12/25/3d84327e213d4094914bfecb5581c115.jpg?Expires=1735103372&OSSAccessKeyId=TMP.3KhRwHPvYfzAwmrxytuJE7mbN1V5KD2HqmvxUF2ZBYZBEDSC3VPx2c2j7UVMqzeBMAsbTovUPyrtNh3c9VUKfSP2qisETd&Signature=MA8Mp0o3MT%2BWIv7a0ql%2FrDCXQNs%3D', 1, 0, '2024-12-26 19:28:03', '1735529974@qq.com', '$1$zVy3u74b');
INSERT INTO `user` VALUES (21, 'Jhony', '$1$fDU7HnIW$Q6SskhaVhykqOHOtQpbN2/', 'https://rey-paas-cloud.oss-cn-hangzhou.aliyuncs.com/test/2024/12/25/3d84327e213d4094914bfecb5581c115.jpg?Expires=1735103372&OSSAccessKeyId=TMP.3KhRwHPvYfzAwmrxytuJE7mbN1V5KD2HqmvxUF2ZBYZBEDSC3VPx2c2j7UVMqzeBMAsbTovUPyrtNh3c9VUKfSP2qisETd&Signature=MA8Mp0o3MT%2BWIv7a0ql%2FrDCXQNs%3D', 'https://rey-paas-cloud.oss-cn-hangzhou.aliyuncs.com/test/2024/12/25/3d84327e213d4094914bfecb5581c115.jpg?Expires=1735103372&OSSAccessKeyId=TMP.3KhRwHPvYfzAwmrxytuJE7mbN1V5KD2HqmvxUF2ZBYZBEDSC3VPx2c2j7UVMqzeBMAsbTovUPyrtNh3c9VUKfSP2qisETd&Signature=MA8Mp0o3MT%2BWIv7a0ql%2FrDCXQNs%3D', 1, 0, NULL, '215598070@qq.com', '$1$fDU7HnIW');

SET FOREIGN_KEY_CHECKS = 1;
