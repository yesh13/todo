
create table taglist(
id int not null auto_increment primary key,
uid int,
name varchar(16),
schedule_type int,
foreign key(uid) references account(uid)
);



create table act_tag(
id int not null auto_increment primary key,
aid int unsigned,
tid int,
foreign key(tid) references taglist(id),
foreign key(aid) references activity(aid)
)