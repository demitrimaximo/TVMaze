package com.api.tvmaze.domain.port.in;

import com.api.tvmaze.domain.model.ShowWithComments;

import reactor.core.publisher.Mono;

public interface GetShowUseCase {

	Mono<ShowWithComments> getShow(Long showId);
	
}
