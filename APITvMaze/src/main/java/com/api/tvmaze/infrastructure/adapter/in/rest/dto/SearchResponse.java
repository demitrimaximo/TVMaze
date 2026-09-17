package com.api.tvmaze.infrastructure.adapter.in.rest.dto;

import java.util.List;

public record SearchResponse(
        Long id,
        String name,
        String channel,
        String summary,
        List<String> genres,
        List<CommentResponse> comments
) {}