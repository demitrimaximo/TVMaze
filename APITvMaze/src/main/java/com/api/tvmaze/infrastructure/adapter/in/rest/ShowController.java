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

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/shows")
public class ShowController {
	
    private final SearchShowsUseCase searchShowsUseCase;
    private final GetShowUseCase getShowUseCase;
    
    public ShowController(SearchShowsUseCase searchShowsUseCase, GetShowUseCase getShowUseCase) {
    	this.searchShowsUseCase = searchShowsUseCase;
    	this.getShowUseCase = getShowUseCase;
    }

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
