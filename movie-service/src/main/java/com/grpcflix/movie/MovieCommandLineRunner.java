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

        Movie firstMovie = new Movie(1, "The Shawshank Redemption", 1994, 9.3, "DRAMA");
        Movie fourthMovie = new Movie(2, "The godfather", 1972, 9.2, "DRAMA");
        Movie secondMovie = new Movie(5, "Inception", 2010, 8.8, "ACTION");
        Movie thirdMovie = new Movie(9, "Oldboy", 2003, 8.4, "ACTION");

        movieRepository.save(firstMovie);
        movieRepository.save(fourthMovie);
        movieRepository.save(secondMovie);
        movieRepository.save(thirdMovie);
    }
}
