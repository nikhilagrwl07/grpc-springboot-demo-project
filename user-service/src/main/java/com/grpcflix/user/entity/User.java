package com.grpcflix.user.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
@Table(value = "user_by_login")
public class User {
    @PrimaryKeyColumn(name = "login", ordinal = 0,type = PrimaryKeyType.PARTITIONED)
    private String login;

    @PrimaryKeyColumn(name = "genre", ordinal = 1,type = PrimaryKeyType.CLUSTERED)
    private String genre;

    @Column("name")
    private String name;
}

