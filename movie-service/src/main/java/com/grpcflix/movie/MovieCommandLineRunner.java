package com.grpcflix.movie;

import com.grpcflix.movie.entity.Movie;
import com.grpcflix.movie.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MovieCommandLineRunner implements CommandLineRunner {

    @Autowired
    MovieRepository movieRepository;

    @Override
    public void run(String... args) throws Exception {
        Movie firstMovie = new Movie("DRAMA", 1994, 1,  9.3, "The Shawshank Redemption" );
        Movie secondMovie = new Movie("ACTION",  2010, 5, 8.8, "Inception");
        Movie thirdMovie = new Movie("ACTION", 2003, 9, 8.4, "Oldboy");
        Movie fourthMovie = new Movie("DRAMA", 1972, 2, 9.2, "The godfather");

        movieRepository.save(firstMovie).block();
        movieRepository.save(secondMovie).block();
        movieRepository.save(thirdMovie).block();
        movieRepository.save(fourthMovie).block();
    }
}
