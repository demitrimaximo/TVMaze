package com.api.tvmaze.application.service;

import java.time.Duration;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.api.tvmaze.domain.model.ShowSearchResult;
import com.api.tvmaze.domain.port.in.SearchShowsUseCase;
import com.api.tvmaze.domain.port.out.CommentRepositoryPort;
import com.api.tvmaze.domain.port.out.TvMazeClientPort;

import reactor.core.publisher.Flux;
import reactor.util.retry.Retry;

@Service
public class SearchShowsService implements SearchShowsUseCase{

    private final TvMazeClientPort tvMazeClient;
    private final CommentRepositoryPort commentRepository;
    private final int maxConcurrency;
    private final int commentTimeoutSeconds;
    private final int retryBackoffM;
    private final int retryMaxAttempts;
    
    public SearchShowsService(TvMazeClientPort tvMazeClient, CommentRepositoryPort commentRepository, 
    		@Value("${tvmaze.concurrency.search-comments:8}") int maxConcurrency,
    		@Value("${tvmaze.timeout.comment-lookup-seconds:2}") int commentTimeoutSeconds,
    		@Value("${tvmaze.retry.backoff-ms:200}") int retryBackoffM,
    		@Value("${tvmaze.retry.max-attempts:2}") int retryMaxAttempts) {
    	this.tvMazeClient = tvMazeClient;
    	this.commentRepository = commentRepository;
    	this.maxConcurrency = maxConcurrency;
    	this.commentTimeoutSeconds = commentTimeoutSeconds;
    	this.retryBackoffM = retryBackoffM;
    	this.retryMaxAttempts = retryMaxAttempts;
    }
	
	@Override
	public Flux<ShowSearchResult> search(String query) {
		return tvMazeClient.searchShows(query)
	            .flatMap(
	                    show -> commentRepository.findByShowId(show.id())
	                            .collectList()
	                            .defaultIfEmpty(List.of())
	                            .timeout(Duration.ofSeconds(commentTimeoutSeconds))
	                            .retryWhen(
	                                    Retry.backoff(retryMaxAttempts, Duration.ofMillis(retryBackoffM))
	                            )
	                            .onErrorReturn(List.of())
	                            .map(comments -> new ShowSearchResult(
	                                    show.id(),
	                                    show.name(),
	                                    show.channel(),
	                                    show.summary(),
	                                    show.genres(),
	                                    comments
	                            )),
	                            maxConcurrency
	            );
        
	}

}
