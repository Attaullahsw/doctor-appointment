/*
SQLyog Job Agent Version 10.0 Beta1 Copyright(c) Webyog Inc. All Rights Reserved.


MySQL - 5.5.5-10.1.9-MariaDB : Database - patient_appointment_db
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
/*Database structure for database `patient_appointment_db` */

CREATE DATABASE /*!32312 IF NOT EXISTS*/`patient_appointment_db` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci */;

USE `patient_appointment_db`;

/*Table structure for table `admin_tbl` */

DROP TABLE IF EXISTS `admin_tbl`;

CREATE TABLE `admin_tbl` (
  `admin_id` int(10) NOT NULL AUTO_INCREMENT,
  `admin_username` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `admin_password` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`admin_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `admin_tbl` */

insert  into `admin_tbl` values (1,'admin','pass');

/*Table structure for table `appointment_tbl` */

DROP TABLE IF EXISTS `appointment_tbl`;

CREATE TABLE `appointment_tbl` (
  `appointment_id` int(11) NOT NULL AUTO_INCREMENT,
  `appointment_date` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `appointment_start_time` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `appointment_end_time` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `appointment_per_patient_time` int(11) DEFAULT NULL,
  `doctor_id` int(11) DEFAULT NULL,
  `hospital_department_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`appointment_id`),
  KEY `doctor_id` (`doctor_id`),
  KEY `hospital_department_id` (`hospital_department_id`),
  CONSTRAINT `appointment_tbl_ibfk_1` FOREIGN KEY (`doctor_id`) REFERENCES `doctor_tbl` (`doctor_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `appointment_tbl_ibfk_2` FOREIGN KEY (`hospital_department_id`) REFERENCES `hospital_department_tbl` (`hospital_assign_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `appointment_tbl` */

insert  into `appointment_tbl` values (1,'2019-11-12','1:00','6:30',15,1,1),(2,'2019-10-15','1:30','6:00',30,1,1),(3,'2019-10-23','1:00','6:00',15,1,1),(4,'2019-10-22','1:00','6:00',15,2,1),(5,'2019-10-31','1:00','3:00',10,2,1),(6,'2019-11-08','1:00','1:30',30,1,1);

/*Table structure for table `department_tbl` */

DROP TABLE IF EXISTS `department_tbl`;

CREATE TABLE `department_tbl` (
  `department_id` int(11) NOT NULL AUTO_INCREMENT,
  `department_title` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `department_image` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`department_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `department_tbl` */

insert  into `department_tbl` values (1,'Ears','IMG-20181120-WA000515575.jpg'),(3,'ENT','IMG-20191015-WA006520339.jpg'),(4,'ICO','IMG-20191017-WA000821355.jpg');

/*Table structure for table `doctor_tbl` */

DROP TABLE IF EXISTS `doctor_tbl`;

CREATE TABLE `doctor_tbl` (
  `doctor_id` int(11) NOT NULL AUTO_INCREMENT,
  `doctor_name` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL,
  `doctor_image` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `doctor_address` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `doctor_gender` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL,
  `doctor_contact_no` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `doctor_username` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `doctor_password` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `hospital_department_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`doctor_id`),
  KEY `hospital_department_id` (`hospital_department_id`),
  CONSTRAINT `doctor_tbl_ibfk_1` FOREIGN KEY (`hospital_department_id`) REFERENCES `hospital_tbl` (`hospital_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `doctor_tbl` */

insert  into `doctor_tbl` values (1,'Hassan khan','IMG-20180616-WA000817617.jpg','saidu sharif road mingora swat','Male','03457578973','hassan@gmail.com','pass',1),(2,'Kashif khan','IMG-20180616-WA000817617.jpg','neshat chock mingora swat','Male','034557785859','kashif@gmail.com','pass',1),(3,'Umar Rahman','IMG-20180616-WA000817617.jpg','amankot mingora swat','Male','03456274748','umar','pass',1),(4,'Umar Rahman khan','IMG-20180616-WA001230805.jpg','amankot mingora swat','Male','03456274748','umar1','pass',1),(5,'inam','IMG-20180616-WA001618618.jpg','address','Male','03457800543','inam','pass',2);

/*Table structure for table `hospital_department_tbl` */

DROP TABLE IF EXISTS `hospital_department_tbl`;

CREATE TABLE `hospital_department_tbl` (
  `hospital_assign_id` int(11) NOT NULL AUTO_INCREMENT,
  `hospital_id` int(11) DEFAULT NULL,
  `department_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`hospital_assign_id`),
  KEY `hospital_id` (`hospital_id`),
  KEY `hospital_department_tbl_ibfk_2` (`department_id`),
  CONSTRAINT `hospital_department_tbl_ibfk_1` FOREIGN KEY (`hospital_id`) REFERENCES `hospital_tbl` (`hospital_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `hospital_department_tbl_ibfk_2` FOREIGN KEY (`department_id`) REFERENCES `department_tbl` (`department_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `hospital_department_tbl` */

insert  into `hospital_department_tbl` values (1,1,1),(2,2,3),(3,2,1);

/*Table structure for table `hospital_tbl` */

DROP TABLE IF EXISTS `hospital_tbl`;

CREATE TABLE `hospital_tbl` (
  `hospital_id` int(11) NOT NULL AUTO_INCREMENT,
  `hospital_name` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `hospital_address` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `hospital_contact_no` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `hospital_image` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`hospital_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `hospital_tbl` */

insert  into `hospital_tbl` values (1,'Shifa Medical Complex','Saidu road mingora swat','034682838748','IMG-20190709-WA004526800.jpg'),(2,'swat medical complex','sadiu sharif swat','03456789123','images (1)4066.jpeg');

/*Table structure for table `patient_appointment_tbl` */

DROP TABLE IF EXISTS `patient_appointment_tbl`;

CREATE TABLE `patient_appointment_tbl` (
  `patient_appointment_id` int(11) NOT NULL AUTO_INCREMENT,
  `patient_id` int(11) DEFAULT NULL,
  `appointment_id` int(11) DEFAULT NULL,
  `appointment_time` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`patient_appointment_id`),
  KEY `patient_id` (`patient_id`),
  KEY `appointment_id` (`appointment_id`),
  CONSTRAINT `patient_appointment_tbl_ibfk_1` FOREIGN KEY (`patient_id`) REFERENCES `patient_tbl` (`patient_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `patient_appointment_tbl_ibfk_2` FOREIGN KEY (`appointment_id`) REFERENCES `appointment_tbl` (`appointment_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `patient_appointment_tbl` */

insert  into `patient_appointment_tbl` values (6,1,1,'2:30'),(7,1,2,'5:30'),(15,2,1,'1:15'),(16,2,1,'1:45'),(17,2,5,'1:20'),(18,2,6,'1:30'),(19,1,6,'1:00');

/*Table structure for table `patient_tbl` */

DROP TABLE IF EXISTS `patient_tbl`;

CREATE TABLE `patient_tbl` (
  `patient_id` int(11) NOT NULL AUTO_INCREMENT,
  `patient_name` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `patient_username` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `patient_password` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `patient_address` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `patient_gender` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `patient_contact_no` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`patient_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `patient_tbl` */

insert  into `patient_tbl` values (1,'ali','ali123','pass','address','male','123456789'),(2,'Ali khan','ali12','pass','saidu road neshta chock mingora swat','Male','0364647489');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
