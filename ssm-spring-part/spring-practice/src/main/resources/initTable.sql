create database if not exists studb;

use studb;

drop table students;

create table if not exists students
(
    id     int primary key auto_increment,
    name   varchar(50) not null,
    gender varchar(10) not null,
    age    int         not null,
    class  varchar(50) not null
);

insert into students (name, gender, age, class)
values ('张三', '男', 20, '计算机科学1班'),
       ('李四', '女', 21, '计算机科学1班'),
       ('王五', '男', 19, '软件工程2班'),
       ('赵六', '女', 22, '计算机科学2班'),
       ('孙七', '男', 20, '软件工程1班'),
       ('周八', '女', 21, '数据科学1班'),
       ('吴九', '男', 23, '计算机科学2班'),
       ('郑十', '女', 19, '软件工程2班'),
       ('刘一', '男', 22, '数据科学1班'),
       ('陈二', '女', 20, '计算机科学1班');

