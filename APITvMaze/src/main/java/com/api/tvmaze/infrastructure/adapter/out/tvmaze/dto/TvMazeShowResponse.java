package com.api.tvmaze.infrastructure.adapter.out.tvmaze.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TvMazeShowResponse(
        Long id,
        String name,
        String summary,
        List<String> genres,
        Network network,
        @JsonProperty("webChannel") Network webChannel
) {
	
	 @JsonIgnoreProperties(ignoreUnknown = true)
	 public record Network(String name) {}

}
