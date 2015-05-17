alter table activity
add column task_state int;

update activity set task_state=0 where schedule_type=0;

alter table activity
modify column name text;

