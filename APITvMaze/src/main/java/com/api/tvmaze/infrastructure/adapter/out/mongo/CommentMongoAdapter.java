package com.api.tvmaze.infrastructure.adapter.out.mongo;

import org.springframework.stereotype.Component;

import com.api.tvmaze.domain.model.Comment;
import com.api.tvmaze.domain.port.out.CommentRepositoryPort;
import com.api.tvmaze.infrastructure.adapter.out.mongo.document.CommentDocument;
import com.api.tvmaze.infrastructure.adapter.out.mongo.repository.CommentMongoRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Component
public class CommentMongoAdapter implements CommentRepositoryPort {

	private final CommentMongoRepository repository;
	
	public CommentMongoAdapter(CommentMongoRepository repository) {
		this.repository = repository;
	}
	
	
	
    @Override
    public Mono<Comment> save(Comment comment) {
        CommentDocument doc = CommentDocument.builder()
                .showId(comment.showId())
                .comment(comment.comment())
                .rating(comment.rating())
                .createdAt(comment.createdAt())
                .build();

        return repository.save(doc).map(this::toDomain);
    }

    @Override
    public Flux<Comment> findByShowId(Long showId) {
        return repository.findByShowId(showId).map(this::toDomain);
    }

    private Comment toDomain(CommentDocument doc) {
        return new Comment(doc.getId(), doc.getShowId(), doc.getComment(), doc.getRating(), doc.getCreatedAt());
    }

}
