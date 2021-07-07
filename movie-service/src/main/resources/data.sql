DROP table if exists movie;

create table movie AS select * from CSVREAD('classpath:movie.csv');
