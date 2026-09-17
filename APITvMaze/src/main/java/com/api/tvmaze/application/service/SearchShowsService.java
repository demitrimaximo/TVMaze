package com.api.tvmaze.application.service;

import java.time.Duration;
import java.util.List;

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
    
    public SearchShowsService(TvMazeClientPort tvMazeClient, CommentRepositoryPort commentRepository) {
    	this.tvMazeClient = tvMazeClient;
    	this.commentRepository = commentRepository;
    }
	
	@Override
	public Flux<ShowSearchResult> search(String query) {
		return tvMazeClient.searchShows(query)
	            .flatMap(
	                    show -> commentRepository.findByShowId(show.id())
	                            .collectList()
	                            .defaultIfEmpty(List.of())
	                            .timeout(Duration.ofSeconds(2))
	                            .retryWhen(
	                                    Retry.backoff(2, Duration.ofMillis(200))
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
	                    8
	            );
        
	}

}
