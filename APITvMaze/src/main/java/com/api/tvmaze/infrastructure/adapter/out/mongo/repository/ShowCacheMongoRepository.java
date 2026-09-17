package com.api.tvmaze.infrastructure.adapter.out.mongo.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.api.tvmaze.infrastructure.adapter.out.mongo.document.ShowCacheDocument;

public interface ShowCacheMongoRepository extends ReactiveMongoRepository<ShowCacheDocument, Long> {

}
