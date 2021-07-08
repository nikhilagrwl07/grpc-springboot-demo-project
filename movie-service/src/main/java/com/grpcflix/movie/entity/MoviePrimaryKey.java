//package com.grpcflix.movie.entity;
//
//import lombok.Data;
//import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
//import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
//import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
//
//@Data
//@PrimaryKeyClass
//public class MoviePrimaryKey {
//
//    @PrimaryKeyColumn(name = "genre", ordinal = 0,type = PrimaryKeyType.PARTITIONED)
//    private String genre;
//
//    @PrimaryKeyColumn(name = "year", ordinal = 1,type = PrimaryKeyType.CLUSTERED)
//    private int year;
//
//}