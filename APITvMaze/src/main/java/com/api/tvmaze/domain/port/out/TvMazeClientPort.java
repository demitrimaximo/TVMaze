package com.api.tvmaze.domain.port.out;

import com.api.tvmaze.domain.model.Show;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TvMazeClientPort {

    Flux<Show> searchShows(String query);
    Mono<Show> getShowById(Long showId);
}
