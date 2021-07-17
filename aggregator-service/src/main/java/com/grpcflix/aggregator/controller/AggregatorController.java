package com.grpcflix.aggregator.controller;

import com.grpcflix.aggregator.dto.RecommendedMovie;
import com.grpcflix.aggregator.dto.UserGenre;
import com.grpcflix.aggregator.exception.BadRequestException;
import com.grpcflix.aggregator.service.UserMovieService;
import com.movieservice.grpcflix.user.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/aggregator-service")
public class AggregatorController {

    @Autowired
    private UserMovieService userMovieService;

    @GetMapping("/user/{loginId}")
    public List<RecommendedMovie> getMoviesByLoginId(@PathVariable String loginId) {
        return this.userMovieService.getUserMovieSuggestions(loginId);
    }

    @PutMapping("/user/{loginId}")
    public UserResponse setUserGenre(@RequestBody UserGenre userGenre, @PathVariable String loginId) throws BadRequestException {
        if(loginId!=userGenre.getLoginId())
            throw new BadRequestException("Login Id does not match");

        return this.userMovieService.setUserGenre(userGenre);
    }
}
