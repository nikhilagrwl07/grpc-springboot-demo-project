package com.grpcflix.aggregator.controller;

import com.grpcflix.aggregator.dto.*;
import com.grpcflix.aggregator.exception.BadRequestException;
import com.grpcflix.aggregator.service.AggregatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/aggregator-service")
public class AggregatorController {

    @Autowired
    private AggregatorService aggregatorService;

    @GetMapping("/user/{loginId}")
    public Flux<RecommendedMovie> getMoviesByLoginId(@PathVariable String loginId) {
        return this.aggregatorService.getUserMovieSuggestions(loginId);
    }

    @PutMapping("/user/{loginId}")
    public Mono<UserResponse> setUserGenre(@RequestBody UserGenre userGenre, @PathVariable String loginId) throws BadRequestException {
        if(!loginId.equals(userGenre.getLoginId()))
            throw new BadRequestException("Login Id does not match"); // TODO: Use mono's exception

        return this.aggregatorService.setUserGenre(userGenre);
    }

    @PostMapping("/audit")
    public Mono<MediaStreamingResponse> sendAuditDetails(@RequestBody List<MediaStreamingRequest> mediaStreamingRequests) {
        return this.aggregatorService.sendAuditDetails(mediaStreamingRequests);
    }
}
