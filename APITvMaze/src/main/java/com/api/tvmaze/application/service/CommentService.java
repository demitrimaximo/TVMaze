package com.api.tvmaze.application.service;

import org.springframework.stereotype.Service;

import com.api.tvmaze.domain.model.Comment;
import com.api.tvmaze.domain.port.in.CommentUseCase;
import com.api.tvmaze.domain.port.out.CommentRepositoryPort;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CommentService implements CommentUseCase {

	private final CommentRepositoryPort commentRepository;
	
	public CommentService(CommentRepositoryPort commentRepository) {
	    this.commentRepository = commentRepository;
	}
	
	@Override
	public Flux<Comment> getCommentsByShow(Long showId) {
		
		return commentRepository.findByShowId(showId);
	}

	@Override
	public Mono<Comment> addComment(Comment comment) {
		
        return commentRepository.save(comment);
	}

}
