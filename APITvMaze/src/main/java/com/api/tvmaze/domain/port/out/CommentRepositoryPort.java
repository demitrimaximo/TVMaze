package com.api.tvmaze.domain.port.out;

import com.api.tvmaze.domain.model.Comment;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CommentRepositoryPort {
	
	Flux<Comment> findByShowId(Long showId);
    Mono<Comment> save(Comment comment);
    
}
