package com.api.tvmaze.infrastructure.adapter.out.mongo.repository;

import com.api.tvmaze.infrastructure.adapter.out.mongo.document.CommentDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface CommentMongoRepository extends ReactiveMongoRepository<CommentDocument, String> {
	Flux<CommentDocument> findByShowId(Long showId);
}