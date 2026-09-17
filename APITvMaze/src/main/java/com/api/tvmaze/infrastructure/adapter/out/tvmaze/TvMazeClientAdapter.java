package com.api.tvmaze.infrastructure.adapter.out.tvmaze;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.api.tvmaze.domain.exception.ExternalApiException;
import com.api.tvmaze.domain.model.Show;
import com.api.tvmaze.domain.port.out.TvMazeClientPort;
import com.api.tvmaze.infrastructure.adapter.out.tvmaze.dto.TvMazeSearchResponse;
import com.api.tvmaze.infrastructure.adapter.out.tvmaze.dto.TvMazeShowResponse;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class TvMazeClientAdapter implements TvMazeClientPort{
	
	private final WebClient tvMazeWebClient;
	
	public TvMazeClientAdapter(WebClient tvMazeWebClient) {
		this.tvMazeWebClient = tvMazeWebClient;
	}

	@Override
    public Flux<Show> searchShows(String query) {
        return tvMazeWebClient.get()
                .uri(uri -> uri.path("/search/shows")
                		.queryParam("q", query)
                		.queryParam("embed", "network")
                        .queryParam("embed", "webchannel")
                		.build())
                .retrieve()
                .onStatus(HttpStatusCode::isError, resp ->
                        resp.bodyToMono(String.class)
                                .map(body -> new ExternalApiException("TV Maze search error: " + body, null)))
                .bodyToFlux(TvMazeSearchResponse.class)
                .map(TvMazeSearchResponse::show)
                .map(this::toDomain);
    }

	
    @Override
    public Mono<Show> getShowById(Long showId) {
        return tvMazeWebClient.get()
                .uri(uri -> uri.path("/shows/{id}")
                        .queryParam("embed", "network")
                        .queryParam("embed", "webchannel")
                        .build(showId))
                .retrieve()
                .onStatus(status -> status.value() == 404, resp -> Mono.empty())
                .onStatus(HttpStatusCode::isError, resp ->
                        resp.bodyToMono(String.class)
                                .map(body -> new ExternalApiException("TV Maze show error: " + body, null)))
                .bodyToMono(TvMazeShowResponse.class)
                .map(this::toDomain);
    }

    private Show toDomain(TvMazeShowResponse dto) {
     
        String channelName = Stream.of(dto.network(), dto.webChannel())
                .filter(Objects::nonNull)
                .map(TvMazeShowResponse.Network::name)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);

        return new Show(
                dto.id(),
                dto.name(),
                channelName,
                dto.summary(),
                Optional.ofNullable(dto.genres()).orElseGet(List::of)
        );
    }

}
