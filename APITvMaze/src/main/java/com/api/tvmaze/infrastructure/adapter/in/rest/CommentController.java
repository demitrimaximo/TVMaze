package com.api.tvmaze.infrastructure.adapter.in.rest;

import com.api.tvmaze.domain.model.Comment;
import com.api.tvmaze.domain.port.in.CommentUseCase;
import com.api.tvmaze.infrastructure.adapter.in.rest.dto.CommentRequest;
import jakarta.validation.Valid;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentUseCase commentUseCase;
    
    public CommentController(CommentUseCase commentUseCase){
    	this.commentUseCase = commentUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseEntity<Map<String, Object>>> addComment(@Valid @RequestBody CommentRequest request) {
        Comment comment = new Comment(request.showId(), request.comment(), request.rating());
        return commentUseCase.addComment(comment)
                .map(saved -> ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                        "status", "CREATED",
                        "id", saved.id()
                )));
    }

}
