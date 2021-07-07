DROP table if exists user;

create table user AS select * from CSVREAD('classpath:user.csv');