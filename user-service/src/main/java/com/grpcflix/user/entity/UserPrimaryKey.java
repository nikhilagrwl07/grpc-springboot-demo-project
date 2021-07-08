//package com.grpcflix.user.entity;
//
//import lombok.AllArgsConstructor;
//import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
//import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
//import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
//
//@PrimaryKeyClass
//public class UserPrimaryKey {
//
//    @PrimaryKeyColumn(name = "login", ordinal = 0,type = PrimaryKeyType.PARTITIONED)
//    private String login;
//
//    @PrimaryKeyColumn(name = "genre", ordinal = 1,type = PrimaryKeyType.CLUSTERED)
//    private String genre;
//
//    public UserPrimaryKey() {
//    }
//
//    public UserPrimaryKey(String login, String genre) {
//        this.login = login;
//        this.genre = genre;
//    }
//}