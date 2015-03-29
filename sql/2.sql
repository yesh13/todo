alter table activity
add column schedule_type int(8) not null default 0;






create table taglist(
id int not null auto_increment primary key,
uid int,
name varchar(16),
schedule_type int,
foreign key(uid) references account(uid)
);



create table act_taglist(
id int not null auto_increment primary key,
aid int,
tid int,
foreign key(aid) references activity(aid),
foreign key(tid) references taglist(id)
)