package com.api.tvmaze.application.service;

import org.springframework.stereotype.Service;

import com.api.tvmaze.domain.exception.ShowNotFoundException;
import com.api.tvmaze.domain.model.Show;
import com.api.tvmaze.domain.model.ShowWithComments;
import com.api.tvmaze.domain.port.in.GetShowUseCase;
import com.api.tvmaze.domain.port.out.CommentRepositoryPort;
import com.api.tvmaze.domain.port.out.TvMazeClientPort;

import reactor.core.publisher.Mono;

@Service
public class GetShowService implements GetShowUseCase{

	private final TvMazeClientPort tvMazeClient;
    private final CommentRepositoryPort commentRepository;
    
    public GetShowService(TvMazeClientPort tvMazeClient, CommentRepositoryPort commentRepository) {
    	this.tvMazeClient = tvMazeClient;
    	this.commentRepository = commentRepository;
    }

    @Override
    public Mono<ShowWithComments> getShow(Long showId) {
        Mono<Show> showMono = tvMazeClient.getShowById(showId)
                .switchIfEmpty(Mono.error(new ShowNotFoundException(showId)));

        // Se ejecutan ambas consultas en paralelo y se combinan
        return Mono.zip(
                showMono,
                commentRepository.findByShowId(showId).collectList()
        ).map(tuple -> new ShowWithComments(tuple.getT1(), tuple.getT2()));
    }

}
