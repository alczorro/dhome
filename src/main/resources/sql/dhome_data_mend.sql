-----e-science/people生产环境下的数据一致性sql--------------

-----修复因更新个人资料的机构主页名称造成的机构主页不包含学者问题 2012/11/12 dhome v1.0.3的数据--------------
insert into institution_people(uid,institution_id) 
select work.uid,work.institution_id from `simple_user` ,work 
where work.`uid` =simple_user.id and (work.`uid`,work.`institution_id`) not in (select uid,institution_id from `institution_people` ) and institution_id>0;

insert into institution_people(uid,institution_id)
select education.`uid`,education.`insitution_id`from `simple_user` ,education 
where education.`uid` =simple_user.id and (education.`uid`,education.`insitution_id`) not in (select uid,institution_id from `institution_people` ) and education.`insitution_id`>0;
