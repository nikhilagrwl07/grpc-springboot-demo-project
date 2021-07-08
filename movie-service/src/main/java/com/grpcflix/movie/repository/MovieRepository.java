package com.grpcflix.movie.repository;

import com.grpcflix.movie.entity.Movie;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface MovieRepository extends ReactiveCassandraRepository<Movie, String> {
    Flux<Movie> getMovieByGenreOrderByYearDesc(String genre);
}
