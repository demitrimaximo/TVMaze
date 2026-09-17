package com.api.tvmaze.domain.port.in;

import com.api.tvmaze.domain.model.Comment;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CommentUseCase {
	
    Flux<Comment> getCommentsByShow(Long showId);
    Mono<Comment> addComment(Comment comment);

}
