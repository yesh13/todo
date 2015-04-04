alter table activity
add column schedule_type int(8) not null default 0;



alter table activity drop primary key, add primary key(aid);


alter table activity
add column finish_time datetime;
update activity set finish_time=start_time;
update activity set start_time=null where schedule_type=2;


/*
*/