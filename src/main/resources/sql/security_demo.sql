/*
Navicat MySQL Data Transfer

Source Server         : demo
Source Server Version : 50721
Source Host           : localhost:3306
Source Database       : security_demo

Target Server Type    : MYSQL
Target Server Version : 50721
File Encoding         : 65001

Date: 2020-11-20 09:00:36
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `menu_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `pid` bigint(20) DEFAULT NULL COMMENT '上级菜单ID',
  `title` varchar(100) DEFAULT NULL COMMENT '菜单标题',
  `permission` varchar(255) DEFAULT NULL COMMENT '权限',
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES ('1', null, '系统管理', null);
INSERT INTO `sys_menu` VALUES ('2', '1', '用户管理', 'user:list');
INSERT INTO `sys_menu` VALUES ('3', '1', '角色管理', 'roles:list');
INSERT INTO `sys_menu` VALUES ('4', '1', '菜单管理', 'menu:list');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `role_id` bigint(50) NOT NULL,
  `name` varchar(255) NOT NULL COMMENT '名称',
  `level` int(255) DEFAULT NULL COMMENT '角色级别',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('1', '超级管理员', '1');

-- ----------------------------
-- Table structure for sys_role_menus
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menus`;
CREATE TABLE `sys_role_menus` (
  `role_id` bigint(50) NOT NULL,
  `menu_id` bigint(50) NOT NULL,
  PRIMARY KEY (`role_id`,`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_role_menus
-- ----------------------------
INSERT INTO `sys_role_menus` VALUES ('1', '1');
INSERT INTO `sys_role_menus` VALUES ('1', '2');
INSERT INTO `sys_role_menus` VALUES ('1', '3');
INSERT INTO `sys_role_menus` VALUES ('1', '4');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` bigint(50) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `enabled` bigint(20) DEFAULT NULL COMMENT '状态：1启用、0禁用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('1', 'admin', '$2a$10$jr24h8BL.brN7S0mxenZ4.KhxRcpdhDH4L7L4o3I6nJM6ZjE27tsW', '1');
INSERT INTO `sys_user` VALUES ('2', 'test1', '$2a$10$piWsNgBtkJknECVeJ2Taz.BkXyfibv1Ve6rIG0w.4PxCB.ZoaqAFu', '1');

-- ----------------------------
-- Table structure for sys_users_roles
-- ----------------------------
DROP TABLE IF EXISTS `sys_users_roles`;
CREATE TABLE `sys_users_roles` (
  `user_id` bigint(50) NOT NULL,
  `role_id` bigint(50) NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_users_roles
-- ----------------------------
INSERT INTO `sys_users_roles` VALUES ('1', '1');
