create table if not exists account (
uid int not null auto_increment primary key,
name varchar(20) unique,
nick_name varchar(20),
enabled TINYINT NOT NULL DEFAULT 1 ,
passwd varchar(60)) character set=utf8;

CREATE TABLE user_roles (
  user_role_id INT(11) NOT NULL AUTO_INCREMENT,
  uid int not null,
  ROLE VARCHAR(45) NOT NULL,
  PRIMARY KEY (user_role_id),
  UNIQUE KEY uni_uid_role (ROLE,uid),
  KEY fk_uid_idx (uid),
  CONSTRAINT fk_uid FOREIGN KEY (uid) REFERENCES account (uid));

drop table users;
drop table user_roles;
CREATE  TABLE users (
  name VARCHAR(45) NOT NULL ,
  password VARCHAR(60) NOT NULL ,
  enabled TINYINT NOT NULL DEFAULT 1 ,
  PRIMARY KEY (name));
 
CREATE TABLE user_roles (
  user_role_id INT(11) NOT NULL AUTO_INCREMENT,
  username VARCHAR(45) NOT NULL,
  ROLE VARCHAR(45) NOT NULL,
  PRIMARY KEY (user_role_id),
  UNIQUE KEY uni_username_role (ROLE,username),
  KEY fk_username_idx (username),
  CONSTRAINT fk_username FOREIGN KEY (username) REFERENCES users (name));


create table if not exists activity (
aid int unsigned not null auto_increment,
uid int unsigned not null,
parent int unsigned,
name varchar(30),
location varchar(30),
start_time datetime,
end_time datetime,
note text,
primary key (aid,uid)
) character set=utf8;
alter table activity auto_increment=101;
