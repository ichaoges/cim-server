create database cim;


CREATE TABLE `cim_session`
(
    `id`          bigint(20)  NOT NULL AUTO_INCREMENT,
    `app_version` varchar(255) DEFAULT NULL,
    `bind_time`   bigint(20)   DEFAULT NULL,
    `channel`     varchar(10) NOT NULL,
    `device_id`   varchar(64) NOT NULL,
    `device_name` varchar(255) DEFAULT NULL,
    `host`        varchar(15) NOT NULL,
    `language`    varchar(255) DEFAULT NULL,
    `latitude`    double       DEFAULT NULL,
    `location`    varchar(255) DEFAULT NULL,
    `longitude`   double       DEFAULT NULL,
    `nid`         varchar(32) NOT NULL,
    `os_version`  varchar(255) DEFAULT NULL,
    `state`       int(11)     NOT NULL,
    `uid`         varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `i_uid` (`uid`)
) ENGINE = InnoDB;
