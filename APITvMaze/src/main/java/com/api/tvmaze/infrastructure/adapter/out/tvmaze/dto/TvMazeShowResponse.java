package com.api.tvmaze.infrastructure.adapter.out.tvmaze.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

public record TvMazeShowResponse(
        Long id,
        String name,
        String summary,
        List<String> genres,
        @JsonProperty("_embedded") Embedded embedded
) {
	
	 @JsonIgnoreProperties(ignoreUnknown = true)
	 public record Embedded(Network network, @JsonProperty("webchannel") Network webChannel) {}

	 @JsonIgnoreProperties(ignoreUnknown = true)
	 public record Network(String name) {}

}
