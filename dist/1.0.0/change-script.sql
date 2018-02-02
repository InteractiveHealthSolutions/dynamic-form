ALTER TABLE `unfepi`.`field` 
ADD COLUMN `regex` VARCHAR(300) NULL AFTER `field_label`;

ALTER TABLE `unfepi`.`form_submission_field` 
CHANGE COLUMN `value` `value` VARCHAR(255) NULL DEFAULT NULL ,
ADD COLUMN `value_textarea` VARCHAR(2000) NULL AFTER `value`;

UPDATE `unfepi`.`field_type` SET `name`='text' WHERE `field_type_id`='1';