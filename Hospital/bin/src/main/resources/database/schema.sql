-- schemas
create database spinnyhost_test;
use spinnyhost_test;

CREATE TABLE `users` (
  `id` varchar(255) NOT NULL,
  `account_non_expired` bit(1) DEFAULT NULL,
  `account_non_locked` bit(1) DEFAULT NULL,
  `blocked` bit(1) NOT NULL,
  `created_on` datetime(6) DEFAULT NULL,
  `credentials_non_expired` bit(1) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `email_verified` bit(1) NOT NULL,
  `enabled` bit(1) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `last_updated_on` datetime(6) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_users_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

CREATE TABLE `users_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

CREATE TABLE `authority` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `authority` varchar(255) DEFAULT NULL,
  `created_on` datetime(6) DEFAULT NULL,
  `last_updated_on` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_authority_authority` (`authority`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;



CREATE TABLE `users_authorities` (
  `users_id` varchar(255) NOT NULL,
  `authorities_id` bigint NOT NULL,
  PRIMARY KEY (`users_id`,`authorities_id`),
  KEY `idx_users_authorities_authorities_id` (`authorities_id`),
  CONSTRAINT `FK_users_authorities_users_id` FOREIGN KEY (`users_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FK_users_authorities_authorities_id` FOREIGN KEY (`authorities_id`) REFERENCES `authority` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;




CREATE TABLE `address` (
  `id` int NOT NULL AUTO_INCREMENT,
  `address_line1` varchar(255) DEFAULT NULL,
  `address_line2` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `country` varchar(255) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `email` varchar(255) DEFAULT NULL,
  `fax` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `gstin` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `organization` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `postal_code` varchar(255) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  `user_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_address_user_id` (`user_id`),
  CONSTRAINT `FK_address_users_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;


CREATE TABLE `cc_avenue_transaction_details` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `amount` double NOT NULL,
  `bank_ref_no` varchar(255) DEFAULT NULL,
  `bene_account` varchar(255) DEFAULT NULL,
  `bene_bank` varchar(255) DEFAULT NULL,
  `bene_branch` varchar(255) DEFAULT NULL,
  `bene_ifsc` varchar(255) DEFAULT NULL,
  `bene_name` varchar(255) DEFAULT NULL,
  `billing_address` varchar(255) DEFAULT NULL,
  `billing_city` varchar(255) DEFAULT NULL,
  `billing_country` varchar(255) DEFAULT NULL,
  `billing_email` varchar(255) DEFAULT NULL,
  `billing_name` varchar(255) DEFAULT NULL,
  `billing_notes` varchar(255) DEFAULT NULL,
  `billing_state` varchar(255) DEFAULT NULL,
  `billing_tel` varchar(255) DEFAULT NULL,
  `billing_zip` varchar(255) DEFAULT NULL,
  `bin_country` varchar(255) DEFAULT NULL,
  `bin_supported` varchar(255) DEFAULT NULL,
  `card_name` varchar(255) DEFAULT NULL,
  `currency` varchar(255) DEFAULT NULL,
  `customer_card_id` double NOT NULL,
  `delivery_address` varchar(255) DEFAULT NULL,
  `delivery_city` varchar(255) DEFAULT NULL,
  `delivery_country` varchar(255) DEFAULT NULL,
  `delivery_name` varchar(255) DEFAULT NULL,
  `delivery_state` varchar(255) DEFAULT NULL,
  `delivery_tel` varchar(255) DEFAULT NULL,
  `delivery_zip` varchar(255) DEFAULT NULL,
  `discount_value` double NOT NULL,
  `eci_value` int NOT NULL,
  `failure_message` varchar(255) DEFAULT NULL,
  `inv_mer_reference_no` varchar(255) DEFAULT NULL,
  `mer_amount` double NOT NULL,
  `merchant_param1` varchar(255) DEFAULT NULL,
  `merchant_param2` varchar(255) DEFAULT NULL,
  `merchant_param3` varchar(255) DEFAULT NULL,
  `merchant_param4` varchar(255) DEFAULT NULL,
  `merchant_param5` varchar(255) DEFAULT NULL,
  `offer_code` varchar(255) DEFAULT NULL,
  `offer_type` varchar(255) DEFAULT NULL,
  `order_id` varchar(255) DEFAULT NULL,
  `order_status` varchar(255) DEFAULT NULL,
  `payment_mode` varchar(255) DEFAULT NULL,
  `response_code` varchar(255) DEFAULT NULL,
  `retry` varchar(255) DEFAULT NULL,
  `service_tax` double NOT NULL,
  `si_created` varchar(255) DEFAULT NULL,
  `si_error_desc` varchar(255) DEFAULT NULL,
  `si_mer_ref_no` varchar(255) DEFAULT NULL,
  `si_ref_no` varchar(255) DEFAULT NULL,
  `si_status` varchar(255) DEFAULT NULL,
  `si_sub_ref_no` varchar(255) DEFAULT NULL,
  `status_code` varchar(255) DEFAULT NULL,
  `status_message` varchar(255) DEFAULT NULL,
  `sub_account_id` varchar(255) DEFAULT NULL,
  `tracking_id` varchar(255) DEFAULT NULL,
  `trans_date` datetime(6) DEFAULT NULL,
  `trans_fee` double NOT NULL,
  `vault` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

CREATE TABLE `company_address` (
  `id` int NOT NULL AUTO_INCREMENT,
  `buildingbumber` varchar(255) NOT NULL,
  `city` varchar(255) NOT NULL,
  `country` varchar(255) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `postal_code` varchar(255) NOT NULL,
  `state` varchar(255) NOT NULL,
  `street_address` varchar(255) NOT NULL,
  `website` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

CREATE TABLE `contact` (
  `email` varchar(255) NOT NULL,
  `message` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

CREATE TABLE `domain_order` (
  `id` varchar(255) NOT NULL,
  `activated` bit NOT NULL,
  `created_on` datetime(6) DEFAULT NULL,
  `currency` tinyint DEFAULT NULL,
  `last_updated_on` datetime(6) DEFAULT NULL,
  `order_status` tinyint DEFAULT NULL,
  `reference_no` varchar(255) DEFAULT NULL,
  `sub_total` double DEFAULT NULL,
  `tax` double DEFAULT NULL,
  `total` double DEFAULT NULL,
  `user_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_domain_order_user_id` (`user_id`),
  CONSTRAINT `FK_domain_order_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

CREATE TABLE `domain_order_item` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `activated` bit NOT NULL,
  `created_on` datetime(6) DEFAULT NULL,
  `domain_name` varchar(255) DEFAULT NULL,
  `last_updated_on` datetime(6) DEFAULT NULL,
  `name_order_id` bigint DEFAULT NULL,
  `price` double DEFAULT NULL,
  `purchase_type` tinyint DEFAULT NULL,
  `years` int DEFAULT NULL,
  `domain_order_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_domain_order_item_domain_order_id` (`domain_order_id`),
  CONSTRAINT `FK_domain_order_item_domain_order_id` FOREIGN KEY (`domain_order_id`) REFERENCES `domain_order` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

CREATE TABLE `domain_order_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

CREATE TABLE `domain_service` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_on` datetime(6) DEFAULT NULL,
  `domain_name` varchar(255) DEFAULT NULL,
  `expires_at` datetime(6) DEFAULT NULL,
  `last_updated_on` datetime(6) DEFAULT NULL,
  `status` tinyint DEFAULT NULL,
  `domain_order_id` varchar(255) DEFAULT NULL,
  `user_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_domain_service_domain_order_id` (`domain_order_id`),
  KEY `idx_domain_service_user_id` (`user_id`),
  CONSTRAINT `FK_domain_service_domain_order_id` FOREIGN KEY (`domain_order_id`) REFERENCES `domain_order` (`id`),
  CONSTRAINT `FK_domain_service_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

CREATE TABLE `faq` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit NOT NULL,
  `answer` varchar(255) DEFAULT NULL,
  `created_on` datetime(6) DEFAULT NULL,
  `last_updated_on` datetime(6) DEFAULT NULL,
  `question` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;
CREATE TABLE `ticket` (
  `ticket_id` int NOT NULL AUTO_INCREMENT,
  `admin_id` varchar(255) DEFAULT NULL,
  `alt_email` varchar(255) DEFAULT NULL,
  `feedback` varchar(255) DEFAULT NULL,
  `image_link` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `ticket_topic` varchar(255) DEFAULT NULL,
  `time_stamp` datetime(6) DEFAULT NULL,
  `user_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ticket_id`),
  KEY `idx_ticket_user_id` (`user_id`),
  CONSTRAINT `FK_ticket_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;


CREATE TABLE `message` (
  `msg_id` int NOT NULL AUTO_INCREMENT,
  `content` varchar(255) DEFAULT NULL,
  `sender` varchar(255) DEFAULT NULL,
  `time_stamp` datetime(6) DEFAULT NULL,
  `ticket_ticket_id` int DEFAULT NULL,
  PRIMARY KEY (`msg_id`),
  KEY `idx_` (`ticket_ticket_id`),
  CONSTRAINT `FK_message_ticket_ticket_id` FOREIGN KEY (`ticket_ticket_id`) REFERENCES `ticket` (`ticket_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;



CREATE TABLE `total_subscriber` (
  `email` varchar(255) NOT NULL,
  PRIMARY KEY (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;



DELIMITER $$
CREATE PROCEDURE `GetDomainOrderSeqNextVal`()
BEGIN
    DECLARE current_val INT;
    START TRANSACTION;
    SELECT next_val INTO current_val
    FROM domain_order_seq
    FOR UPDATE;
    UPDATE domain_order_seq
    SET next_val = current_val + 1;
    COMMIT;
    SELECT next_val FROM domain_order_seq;
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE `GetUsersSeqNextVal`()
BEGIN
    DECLARE current_val INT;
    START TRANSACTION;
    SELECT next_val INTO current_val
    FROM users_seq
    FOR UPDATE;
    UPDATE users_seq
    SET next_val = current_val + 1;
    COMMIT;
    SELECT next_val FROM users_seq;
END$$
DELIMITER ;



-- data


insert into authority(authority) values('ROLE_ADMIN'),('ROLE_USER');
insert into users_seq value(0);
insert into domain_order_seq values(0);







/*


create table domain_order_seq (next_val bigint);
insert into domain_order_seq values ( 1 );
DELIMITER //

CREATE PROCEDURE GetDomainOrderSeqNextVal()
BEGIN
    DECLARE current_val INT;
    START TRANSACTION;
    SELECT next_val INTO current_val
    FROM domain_order_seq
    FOR UPDATE;
    UPDATE domain_order_seq
    SET next_val = current_val + 1;
    COMMIT;
    SELECT next_val FROM domain_order_seq;
END //
DELIMITER ;
-- call GetDomainOrderSeqNextVal() 

create table users_seq (next_val bigint);
insert into users_seq values ( 100 );

DELIMITER //
CREATE PROCEDURE GetUsersSeqNextVal()
BEGIN
    DECLARE current_val INT;
    
    START TRANSACTION;
    SELECT next_val INTO current_val
    FROM users_seq
    FOR UPDATE;
    UPDATE users_seq
    SET next_val = current_val + 1;
    COMMIT;
    SELECT next_val FROM users_seq;
END //
DELIMITER ;
 call GetUsersSeqNextVal();
 
 call GetUsersSeqNextVal();
 
 delete from users_seq where next_val = 8;

*/

