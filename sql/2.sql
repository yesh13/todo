alter table activity
add column schedule_type int(8) not null default 0;


alter table activity drop primary key, add primary key(aid);