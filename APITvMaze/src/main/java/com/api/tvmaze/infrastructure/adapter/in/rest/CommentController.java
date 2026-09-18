package com.api.tvmaze.infrastructure.adapter.in.rest;

import com.api.tvmaze.domain.model.Comment;
import com.api.tvmaze.domain.port.in.CommentUseCase;
import com.api.tvmaze.infrastructure.adapter.in.rest.dto.CommentRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;;

@Tag(name = "Comments", description = "Operations for managing show comments")
@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentUseCase commentUseCase;
    
    public CommentController(CommentUseCase commentUseCase){
    	this.commentUseCase = commentUseCase;
    }

    @Operation(summary = "Add a comment", description = "Adds a comment and rating to a TV show" ) 
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Comment created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
        @ApiResponse(responseCode = "404", description = "Show not found",  content = @Content)})
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
