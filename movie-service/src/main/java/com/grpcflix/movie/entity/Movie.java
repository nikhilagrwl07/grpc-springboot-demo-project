package com.grpcflix.movie.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
@Table(value = "movie_by_genre")
public class Movie {

    @PrimaryKeyColumn(name = "genre", ordinal = 0,type = PrimaryKeyType.PARTITIONED)
    private String genre;

    @PrimaryKeyColumn(name = "year", ordinal = 1,type = PrimaryKeyType.CLUSTERED)
    private int year;

    @Column("id")
    private int id;

    @Column("rating")
    private double rating;

    @Column("title")
    private String title;
}
