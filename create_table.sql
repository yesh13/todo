create table if not exists account (
uid int unsigned not null auto_increment primary key,
name varchar(20) unique,
nickname varchar(20),
passwd varchar(20));

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