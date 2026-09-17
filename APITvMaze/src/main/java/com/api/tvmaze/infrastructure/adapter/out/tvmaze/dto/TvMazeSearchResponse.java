package com.api.tvmaze.infrastructure.adapter.out.tvmaze.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TvMazeSearchResponse(Double score, TvMazeShowResponse show) {}
