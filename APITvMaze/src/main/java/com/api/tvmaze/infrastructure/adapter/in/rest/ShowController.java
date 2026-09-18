package com.api.tvmaze.infrastructure.adapter.in.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.tvmaze.domain.port.in.GetShowUseCase;
import com.api.tvmaze.domain.port.in.SearchShowsUseCase;
import com.api.tvmaze.infrastructure.adapter.in.rest.dto.CommentResponse;
import com.api.tvmaze.infrastructure.adapter.in.rest.dto.SearchResponse;
import com.api.tvmaze.infrastructure.adapter.in.rest.dto.ShowResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Tag(name = "Shows", description = "Endpoints for searching and retrieving TV shows")
@RestController
@RequestMapping("/api/shows")
public class ShowController {
	
    private final SearchShowsUseCase searchShowsUseCase;
    private final GetShowUseCase getShowUseCase;
    
    public ShowController(SearchShowsUseCase searchShowsUseCase, GetShowUseCase getShowUseCase) {
    	this.searchShowsUseCase = searchShowsUseCase;
    	this.getShowUseCase = getShowUseCase;
    }

    @Operation( summary = "Search TV shows", description = "Searches TV shows by name using the provided query" ) 
    @ApiResponses({ @ApiResponse( responseCode = "200", description = "Shows found successfully" ), 
    	@ApiResponse( responseCode = "400", description = "The search parameter is missing or invalid" ),
    	@ApiResponse( responseCode = "500", description = "Internal server error" ) })
    @GetMapping("/search")
    public Flux<SearchResponse> search(@RequestParam("q") String query) {
        return searchShowsUseCase.search(query)
                .map(r -> new SearchResponse(
                        r.id(),
                        r.name(),
                        r.channel(),
                        r.summary(),
                        r.genres(),
                        r.comments().stream()
                                .map(c -> new CommentResponse(c.comment(), c.rating()))
                                .toList()
                ));
    }

    @Operation( summary = "Get TV show by ID", description = "Retrieves a TV show and its comments using its ID" ) 
    @ApiResponses({
    	@ApiResponse( responseCode = "200", description = "Show found successfully" ),
    	@ApiResponse( responseCode = "404", description = "Show not found" ), 
    	@ApiResponse( responseCode = "400", description = "The show ID is invalid" ), 
    	@ApiResponse( responseCode = "500", description = "Internal server error" ) })
    @GetMapping("/{showId}")
    public Mono<ShowResponse> getShow(@PathVariable Long showId) {
        return getShowUseCase.getShow(showId)
                .map(result -> new ShowResponse(
                        result.show().id(),
                        result.show().name(),
                        result.show().channel(),
                        result.show().summary(),
                        result.show().genres(),
                        result.comments().stream()
                                .map(c -> new CommentResponse(c.comment(), c.rating()))
                                .toList()
                ));
    }

}
