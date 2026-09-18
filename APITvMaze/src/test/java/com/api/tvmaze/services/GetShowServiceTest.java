package com.api.tvmaze.services;

import com.api.tvmaze.application.service.GetShowService;
import com.api.tvmaze.domain.exception.ShowNotFoundException;
import com.api.tvmaze.domain.model.Comment;
import com.api.tvmaze.domain.model.Show;
import com.api.tvmaze.domain.port.out.CommentRepositoryPort;
import com.api.tvmaze.domain.port.out.ShowCachePort;
import com.api.tvmaze.domain.port.out.TvMazeClientPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetShowServiceTest {

    @Mock private TvMazeClientPort tvMazeClient;
    @Mock private CommentRepositoryPort commentRepository;
    @Mock private ShowCachePort showCachePort;

    private GetShowService service;

    @BeforeEach
    void setUp() {
        service = new GetShowService(tvMazeClient, commentRepository, showCachePort);
    }

    @Test
    void getShow_shouldReturnFromCacheWhenPresent() {
        // given
        Show cached = new Show(1L, "Under the Dome", "CBS",
                "summary", List.of("Drama"));

        when(showCachePort.findById(1L)).thenReturn(Mono.just(cached));
        when(commentRepository.findByShowId(1L)).thenReturn(Flux.empty());

        // when / then
        StepVerifier.create(service.getShow(1L))
                .expectNextMatches(result ->
                        result.show().id().equals(1L) &&
                        result.show().name().equals("Under the Dome") &&
                        result.comments().isEmpty()
                )
                .verifyComplete();

        // Verifica que NO se llamó a TV Maze ni se guardó en cache
        verify(tvMazeClient, never()).getShowById(anyLong());
        verify(showCachePort, never()).save(any());
    }

    @Test
    void getShow_shouldFetchFromApiAndSaveWhenCacheMiss() {
        // given
        Show fetched = new Show(1L, "Under the Dome", "CBS",
                "summary", List.of("Drama"));

        Comment comment = new Comment(
                "c1", 1L, "Excelente", 5, Instant.now()
        );

        when(showCachePort.findById(1L)).thenReturn(Mono.empty());
        when(tvMazeClient.getShowById(1L)).thenReturn(Mono.just(fetched));
        when(showCachePort.save(fetched)).thenReturn(Mono.just(fetched));
        when(commentRepository.findByShowId(1L)).thenReturn(Flux.just(comment));

        // when / then
        StepVerifier.create(service.getShow(1L))
                .expectNextMatches(result ->
                        result.show().id().equals(1L) &&
                        result.comments().size() == 1 &&
                        result.comments().get(0).rating() == 5
                )
                .verifyComplete();

        // Verifica que SÍ llamó a TV Maze y guardó en cache
        verify(tvMazeClient).getShowById(1L);
        verify(showCachePort).save(fetched);
    }

    @Test
    void getShow_shouldErrorWhenShowNotFoundAnywhere() {
        // given
        when(showCachePort.findById(999L)).thenReturn(Mono.empty());
        when(tvMazeClient.getShowById(999L)).thenReturn(Mono.empty());
        when(commentRepository.findByShowId(999L)).thenReturn(Flux.empty());   // ← AGREGAR ESTA LÍNEA

        // when / then
        StepVerifier.create(service.getShow(999L))
                .expectErrorMatches(ex ->
                        ex instanceof ShowNotFoundException &&
                        ex.getMessage().contains("999")
                )
                .verify();

        verify(showCachePort, never()).save(any());
    }
}