package com.api.tvmaze.domain.port.in;

import com.api.tvmaze.domain.model.ShowSearchResult;

import reactor.core.publisher.Flux;

public interface SearchShowsUseCase {

	Flux<ShowSearchResult> search(String query);
}
