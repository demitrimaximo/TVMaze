package com.api.tvmaze.application.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.api.tvmaze.domain.exception.ShowNotFoundException;
import com.api.tvmaze.domain.model.Show;
import com.api.tvmaze.domain.model.ShowWithComments;
import com.api.tvmaze.domain.port.in.GetShowUseCase;
import com.api.tvmaze.domain.port.out.CommentRepositoryPort;
import com.api.tvmaze.domain.port.out.ShowCachePort;
import com.api.tvmaze.domain.port.out.TvMazeClientPort;

import reactor.core.publisher.Mono;

@Service
public class GetShowService implements GetShowUseCase{

	private static final Logger log = LoggerFactory.getLogger(GetShowService.class);
	private final TvMazeClientPort tvMazeClient;
    private final CommentRepositoryPort commentRepository;
    private final ShowCachePort showCachePort;
    
    public GetShowService(TvMazeClientPort tvMazeClient, CommentRepositoryPort commentRepository, ShowCachePort showCachePort) {
    	this.tvMazeClient = tvMazeClient;
    	this.commentRepository = commentRepository;
    	this.showCachePort = showCachePort;
    }

    
    @Override
    public Mono<ShowWithComments> getShow(Long showId) {
        Mono<Show> showMono = showCachePort.findById(showId)
                .doOnNext(s -> log.debug("Activando caché for show {}", showId))
                .switchIfEmpty(Mono.defer(() -> {
                    log.debug("Sin cache para el show {}, yendo a TV Maze", showId);
                    return tvMazeClient.getShowById(showId)
                            .switchIfEmpty(Mono.error(new ShowNotFoundException(showId)))
                            .flatMap(showCachePort::save)
                            .doOnNext(s -> log.debug("Show {} guardado en cache", showId));
                }));

        return Mono.zip(
                showMono,
                commentRepository.findByShowId(showId).collectList()
        ).map(tuple -> new ShowWithComments(tuple.getT1(), tuple.getT2()));
    }
}
