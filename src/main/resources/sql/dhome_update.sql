-----机构主页-20121019更新--------------
alter table key_value change  column `key` `prop_key` varchar(255) default NULL;
alter table key_value change  column `value` `prop_value` varchar(255) default NULL;
-----机构主页-20121016更新--------------
alter table work add column alias_institution_name varchar(255) default null;
update work a inner join institution b on a.institution_id=b.id set a.alias_institution_name = b.zh_name;

alter table education add column alias_institution_name varchar(255) default null;
update education a inner join institution b on a.insitution_id=b.id set a.alias_institution_name = b.zh_name;

insert into institution(zh_name) values('systemempty');
update institution set id=0 where zh_name = 'systemempty';

INSERT INTO `theme` VALUES (4,'时尚简约主题','时尚主题，让空间充满色彩');
-----机构主页-20121012更新--------------
DROP TABLE IF EXISTS `user_guide`;
CREATE TABLE `user_guide` (
  `id` int(11) NOT NULL auto_increment,
  `uid` int(11) NOT NULL,
  `module` varchar(20) NOT NULL,
  `step` int(11) NOT NULL,
  PRIMARY KEY  (`id`),
  foreign key (`uid`) references simple_user(`id`) on delete cascade on update cascade
) ENGINE=InnoDB AUTO_INCREMENT=199 DEFAULT CHARSET=utf8;

-----机构主页-20121009更新--------------

CREATE TABLE `interest_dic` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `keyword` VARCHAR(255) COLLATE utf8_general_ci DEFAULT NULL,
  `status` INTEGER(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;
alter table institution_name_mapping add column `is_full` int(11) default NULL;
alter table institution_home add column `home_status` varchar(32) default 'valid'; 
alter table institution_publication_statistic add unique(institution_id,year);

-----机构主页-20120924更新--------------

CREATE TABLE `institution_home` (
  `id` int(11) NOT NULL auto_increment,
  `institution_id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `introduction` text,
  `domain` varchar(255) default NULL,
  `logo_id` int(11) default '0',
  `creator` int(11) NOT NULL,
  `create_time` timestamp NOT NULL default CURRENT_TIMESTAMP,
  `last_editor` int(11) NOT NULL,
  `last_edit_time` timestamp NOT NULL default '0000-00-00 00:00:00',
  `paper_count` int(11) default NULL,
  `citation_count` int(11) default NULL,
  `hindex` int(11) default NULL,
  `gindex` int(11) default NULL,
  PRIMARY KEY  (`id`),
  KEY `institution_id` (`institution_id`),
  CONSTRAINT `institution_home_fk` FOREIGN KEY (`institution_id`) REFERENCES `institution` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `institution_name_mapping` (
  `id` int(11) NOT NULL auto_increment,
  `alias_name` text NOT NULL,
  `institution_id` int(11) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `institution_id` (`institution_id`),
  CONSTRAINT `inst_fkey` FOREIGN KEY (`institution_id`) REFERENCES `institution` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `institution_people` (
  `id` int(11) NOT NULL auto_increment,
  `uid` int(11) NOT NULL,
  `institution_id` int(11) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `institution_id` (`institution_id`),
  KEY `uid` (`uid`),
  CONSTRAINT `inst_key` FOREIGN KEY (`institution_id`) REFERENCES `institution` (`id`),
  CONSTRAINT `user_key` FOREIGN KEY (`uid`) REFERENCES `simple_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `institution_publication_statistic` (
  `id` int(11) NOT NULL auto_increment,
  `year` int(11) NOT NULL,
  `institution_id` int(11) NOT NULL,
  `annual_paper_count` int(11) NOT NULL,
  `annual_citation_count` int(11) NOT NULL,
  `total_paper_count` int(11) NOT NULL,
  `total_citation_count` int(11) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `instituion_id` (`institution_id`),
  CONSTRAINT `institution_publication_statistic_fk` FOREIGN KEY (`institution_id`) REFERENCES `institution` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `scholar_event` (
  `id` int(11) NOT NULL auto_increment,
  `title` varchar(255) NOT NULL,
  `reporter` varchar(255) NOT NULL default '',
  `start_time` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `end_time` timestamp NOT NULL default '0000-00-00 00:00:00',
  `creator` int(11) NOT NULL,
  `create_time` timestamp NOT NULL default '0000-00-00 00:00:00',
  `introduction` text NOT NULL,
  `logo_id` int(11) NOT NULL,
  `institution_id` int(11) NOT NULL,
  `place` varchar(255) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--------------------end-------------------




-- add last_edit_time column in custom_page -20120906-dhome v0.3.5
alter table custom_page add column `last_edit_time` timestamp;
update custom_page set last_edit_time=create_time ;
alter table institution change pinyin pinyin text;

--add by lvly@2012-09-11

--是否是管理员账户
alter table simple_user add column `is_admin` varchar(10) default '0';
--状态，未审核，删除，审核中等等
alter table simple_user add column `status` varchar(20) default 'auditNeed';
--权重，查询的时候排前面
alter table simple_user add column `weight` int(5) default 0;
--审核意见
alter table simple_user add column `audit_propose` varchar(255) default NULL;
--table url-mapping
CREATE TABLE `url_mapping` (
  `id` int(11) NOT NULL auto_increment,
  `uid` int(11) NOT NULL,
  `domain` varchar(255) NOT NULL,
  `url` varchar(255) NOT NULL,
  `status` varchar(10) default NULL,
  PRIMARY KEY  (`id`),
  KEY `url_mapping_uid` (`uid`),
  CONSTRAINT `url_mapping_uid` FOREIGN KEY (`uid`) REFERENCES `simple_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;




