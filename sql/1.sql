create table history(
version int
);
insert into history (version) values (1);


create table if not exists account (
uid int not null auto_increment primary key,
name varchar(20) unique,
nick_name varchar(20),
enabled TINYINT NOT NULL DEFAULT 1 ,
passwd varchar(60)) character set=utf8;
alter table account auto_increment=101;



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
