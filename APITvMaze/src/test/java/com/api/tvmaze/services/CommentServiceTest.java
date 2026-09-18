package com.api.tvmaze.services;

import com.api.tvmaze.application.service.CommentService;
import com.api.tvmaze.domain.model.Comment;
import com.api.tvmaze.domain.port.out.CommentRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Instant;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepositoryPort commentRepository;

    private CommentService service;

    @BeforeEach
    void setUp() {
        service = new CommentService(commentRepository);
    }

    @Test
    void addComment_shouldSaveAndReturnComment() {
        // given
        Comment toSave = new Comment(1L, "Excelente", 5);
        Comment saved = new Comment("c1", 1L, "Excelente", 5, Instant.now());

        when(commentRepository.save(toSave)).thenReturn(Mono.just(saved));

        // when / then
        StepVerifier.create(service.addComment(toSave))
                .expectNextMatches(result ->
                        result.id().equals("c1") &&
                        result.showId().equals(1L) &&
                        result.rating() == 5
                )
                .verifyComplete();
    }

    @Test
    void getCommentsByShow_shouldReturnFluxOfComments() {
        // given
        Comment c1 = new Comment("c1", 1L, "Excelente", 5, Instant.now());
        Comment c2 = new Comment("c2", 1L, "Bueno", 4, Instant.now());

        when(commentRepository.findByShowId(1L)).thenReturn(Flux.just(c1, c2));

        // when / then
        StepVerifier.create(service.getCommentsByShow(1L))
                .expectNextCount(2)
                .verifyComplete();
    }
}