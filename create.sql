create table if not exists account (
uid int unsigned not null primary key,
name varchar(20),
passwd varchar(20));

create table if not exists activity (
uid int unsigned not null,
aid int unsigned not null,
name varchar(20),
passwd varchar(20));