package com.api.tvmaze.services;

import com.api.tvmaze.application.service.SearchShowsService;
import com.api.tvmaze.domain.model.Comment;
import com.api.tvmaze.domain.model.Show;
import com.api.tvmaze.domain.port.out.CommentRepositoryPort;
import com.api.tvmaze.domain.port.out.TvMazeClientPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Instant;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchShowsServiceTest {

    @Mock
    private TvMazeClientPort tvMazeClient;

    @Mock
    private CommentRepositoryPort commentRepository;

    private SearchShowsService service;

    private static final int MAX_CONCURRENCY = 8;
    private static final int COMMENT_TIMEOUT_SECONDS = 2;
    private static final int RETRY_BACKOFF_MS = 200;
    private static final int RETRY_MAX_ATTEMPTS = 2;

    @BeforeEach
    void setUp() {
        service = new SearchShowsService(
                tvMazeClient,
                commentRepository,
                MAX_CONCURRENCY,
                COMMENT_TIMEOUT_SECONDS,
                RETRY_BACKOFF_MS,
                RETRY_MAX_ATTEMPTS
        );
    }

    @Test
    void search_shouldEnrichShowsWithComments() {
        // given
        Show show = new Show(1L, "Under the Dome", "CBS",
                "summary", List.of("Drama", "Science-Fiction"));

        Comment comment = new Comment(
                "c1", 1L, "Excelente show", 5, Instant.now()
        );

        when(tvMazeClient.searchShows("Dome")).thenReturn(Flux.just(show));
        when(commentRepository.findByShowId(1L)).thenReturn(Flux.just(comment));

        // when / then
        StepVerifier.create(service.search("Dome"))
                .expectNextMatches(result ->
                        result.id().equals(1L) &&
                        result.name().equals("Under the Dome") &&
                        result.channel().equals("CBS") &&
                        result.genres().size() == 2 &&
                        result.comments().size() == 1 &&
                        result.comments().get(0).rating() == 5
                )
                .verifyComplete();
    }

    @Test
    void search_shouldReturnEmptyCommentsWhenNoCommentsExist() {
        // given
        Show show = new Show(1L, "Under the Dome", "CBS",
                "summary", List.of("Drama"));

        when(tvMazeClient.searchShows("Dome")).thenReturn(Flux.just(show));
        when(commentRepository.findByShowId(1L)).thenReturn(Flux.empty());

        // when / then
        StepVerifier.create(service.search("Dome"))
                .expectNextMatches(result -> result.comments().isEmpty())
                .verifyComplete();
    }

    @Test
    void search_shouldReturnEmptyCommentsWhenRepositoryFails() {
        // given
        Show show = new Show(1L, "Under the Dome", "CBS",
                "summary", List.of("Drama"));

        when(tvMazeClient.searchShows("Dome")).thenReturn(Flux.just(show));
        when(commentRepository.findByShowId(1L))
                .thenReturn(Flux.error(new RuntimeException("Mongo connection lost")));

        // when / then — el retry + onErrorReturn debe absorber el error
        StepVerifier.create(service.search("Dome"))
                .expectNextMatches(result -> result.comments().isEmpty())
                .verifyComplete();
    }

    @Test
    void search_shouldReturnEmptyFluxWhenNoShowsFound() {
        // given
        when(tvMazeClient.searchShows("Nonexistent")).thenReturn(Flux.empty());

        // when / then
        StepVerifier.create(service.search("Nonexistent"))
                .verifyComplete();
    }
}