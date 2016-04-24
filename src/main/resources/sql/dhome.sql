-- MySQL dump 10.11
--
-- Host: localhost    Database: dhome
-- ------------------------------------------------------
-- Server version	5.0.95

--
-- Table structure for table `simple_user`
--

CREATE TABLE `simple_user` (
  `id` int(11) NOT NULL auto_increment,
  `zh_name` varchar(255) default NULL,
  `en_name` varchar(255) default NULL,
  `image` varchar(255) default NULL,
  `email` varchar(255) NOT NULL,
  `salutation` varchar(255) default NULL,
  `regist_time` timestamp NULL default CURRENT_TIMESTAMP,
  `pinyin` varchar(255) default NULL,
  `step` varchar(255) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `template`
--

CREATE TABLE `template` (
  `id` int(11) NOT NULL,
  `name` varchar(50) NOT NULL,
  `layout` varchar(50) NOT NULL,
  `skin` varchar(50) NOT NULL,
  `create_time` timestamp NOT NULL default CURRENT_TIMESTAMP,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `institution`
--

CREATE TABLE `institution` (
  `id` int(11) NOT NULL auto_increment,
  `zh_name` text NOT NULL,
  `pinyin` varchar(128) NOT NULL,
  `en_name` text,
  `short_name` varchar(50) default NULL,
  `address` text,
  `dsn_id` int(11) default NULL,
  `status` varchar(10) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


--
-- Table structure for table `custom_page`
--

CREATE TABLE `custom_page` (
  `id` int(11) NOT NULL auto_increment,
  `uid` int(11) NOT NULL,
  `title` varchar(255) NOT NULL,
  `content` longtext,
  `create_time` timestamp NOT NULL default CURRENT_TIMESTAMP,
  `used_for` varchar(255) default NULL,
  `url` varchar(255) NOT NULL,
  `key_word` varchar(255) default NULL,
  PRIMARY KEY  (`id`),
  KEY `uid` (`uid`),
  CONSTRAINT `cutom_user_key` FOREIGN KEY (`uid`) REFERENCES `simple_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `detailed_user`
--

CREATE TABLE `detailed_user` (
  `id` int(11) NOT NULL auto_increment,
  `uid` int(11) NOT NULL,
  `birthday` date default NULL,
  `gender` varchar(5) default NULL,
  `blog_url` varchar(255) default NULL,
  `weibo_url` varchar(255) default NULL,
  `introduction` text,
  `first_class_discipline` int(11) NOT NULL,
  `second_class_discipline` int(11) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `uid` (`uid`),
  CONSTRAINT `detail_user_key` FOREIGN KEY (`uid`) REFERENCES `simple_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `discipline`
--

CREATE TABLE `discipline` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(255) default NULL,
  `parent_id` int(11) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `education`
--

CREATE TABLE `education` (
  `id` int(11) NOT NULL auto_increment,
  `uid` int(11) NOT NULL,
  `insitution_id` int(11) NOT NULL,
  `degree` varchar(20) NOT NULL,
  `department` varchar(255) NOT NULL,
  `begin_time` date default NULL,
  `end_time` date default NULL,
  `is_carry_on` bit(1) default NULL,
  PRIMARY KEY  (`id`),
  KEY `uid` (`uid`),
  KEY `insitution_id` (`insitution_id`),
  CONSTRAINT `edu_instition_key` FOREIGN KEY (`insitution_id`) REFERENCES `institution` (`id`),
  CONSTRAINT `edu_user_key` FOREIGN KEY (`uid`) REFERENCES `simple_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


--
-- Table structure for table `home`
--

CREATE TABLE `home` (
  `id` int(11) NOT NULL auto_increment,
  `uid` int(11) NOT NULL,
  `template_id` int(11) default NULL,
  `language` varchar(255) default NULL,
  `create_time` timestamp NOT NULL default CURRENT_TIMESTAMP,
  `url` varchar(255) default NULL,
  `themeid` int(11) default NULL,
  PRIMARY KEY  (`id`),
  KEY `uid` (`uid`),
  KEY `template_id` (`template_id`),
  CONSTRAINT `home_template_key` FOREIGN KEY (`template_id`) REFERENCES `template` (`id`),
  CONSTRAINT `home_user_key` FOREIGN KEY (`uid`) REFERENCES `simple_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `interest`
--

CREATE TABLE `interest` (
  `id` int(11) NOT NULL auto_increment,
  `uid` int(11) NOT NULL,
  `keyword` varchar(255) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `uid` (`uid`),
  CONSTRAINT `interest_user_key` FOREIGN KEY (`uid`) REFERENCES `simple_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `key_value`
--

CREATE TABLE `key_value` (
  `id` int(11) NOT NULL auto_increment,
  `key` varchar(255) default NULL,
  `value` varchar(255) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `menu_item`
--

CREATE TABLE `menu_item` (
  `id` int(11) NOT NULL auto_increment,
  `uid` int(11) NOT NULL,
  `title` varchar(128) NOT NULL default '',
  `url` varchar(255) default NULL,
  `sequence` int(11) NOT NULL,
  `status` int(11) NOT NULL default '1',
  PRIMARY KEY  (`id`),
  KEY `uid` (`uid`),
  CONSTRAINT `menu_user_key` FOREIGN KEY (`uid`) REFERENCES `simple_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `page_img`
--

CREATE TABLE `page_img` (
  `id` int(11) NOT NULL auto_increment,
  `uid` int(11) NOT NULL,
  `clb_id` int(11) NOT NULL,
  `create_time` timestamp NOT NULL default CURRENT_TIMESTAMP,
  PRIMARY KEY  (`id`),
  KEY `page_user_key` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `paper`
--

CREATE TABLE `paper` (
  `id` int(11) NOT NULL auto_increment,
  `uid` int(11) NOT NULL,
  `title` text NOT NULL,
  `authors` text,
  `source` varchar(1024) default NULL,
  `volumeIssue` varchar(128) default NULL,
  `publishedTime` varchar(128) default NULL,
  `timeCited` varchar(20) default NULL,
  `summary` text,
  `language` varchar(64) default NULL,
  `keywords` text,
  `localFulltextURL` text,
  `paperURL` text,
  `clbId` int(11) default NULL,
  `sequence` int(11) NOT NULL,
  `dsnPaperId` int(11) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `uid` (`uid`),
  CONSTRAINT `paper_user_key` FOREIGN KEY (`uid`) REFERENCES `simple_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


--
-- Table structure for table `theme`
--

CREATE TABLE `theme` (
  `id` int(11) NOT NULL auto_increment,
  `title` varchar(128) default NULL,
  `descript` varchar(255) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `work`
--

CREATE TABLE `work` (
  `id` int(11) NOT NULL auto_increment,
  `uid` int(11) NOT NULL,
  `institution_id` int(255) NOT NULL,
  `department` varchar(255) NOT NULL,
  `position` varchar(255) NOT NULL,
  `begin_time` date default NULL,
  `end_time` date default NULL,
  PRIMARY KEY  (`id`),
  KEY `uid` (`uid`),
  KEY `institution_id` (`institution_id`),
  CONSTRAINT `work_insitution_key` FOREIGN KEY (`institution_id`) REFERENCES `institution` (`id`),
  CONSTRAINT `work_user_key` FOREIGN KEY (`uid`) REFERENCES `simple_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


--
-- Table structure for table `app_config`
--

CREATE TABLE `app_config` (
  `id` int(11) NOT NULL auto_increment,
  `property` varchar(1024) NOT NULL,
  `value` varchar(1024) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- table url-mapping
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



CREATE TABLE `user_setting` (
  `id` int(11) NOT NULL auto_increment,
  `uid` int(11) NOT NULL,
  `key` varchar(255) NOT NULL,
  `value` varchar(255) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `comment` (
  `id` int(11) NOT NULL auto_increment,
  `comment_host_user_name` varchar(255) default NULL,
  `comment_host_uid` int(11) default NULL,
  `uid` int(11) default NULL,
  `content` text,
  `comment_time` timestamp NULL default CURRENT_TIMESTAMP,
  `name` varchar(255) default NULL,
  `comment_host_image` int(255) default NULL,
  `comment_host_domain` varchar(255) default NULL,
  `image` int(11) default NULL,
  `domain` varchar(255) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
-- Table "comment_reply" DDL

CREATE TABLE `comment_reply` (
  `id` int(11) NOT NULL auto_increment,
  `comment_id` int(11) default NULL,
  `reply_user_name` varchar(11) default NULL,
  `reply_uid` int(11) default NULL,
  `reply_content` varchar(255) default NULL,
  `reply_time` timestamp NULL default CURRENT_TIMESTAMP,
  `reply_image` int(11) default NULL,
  `reply_domain` varchar(255) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


