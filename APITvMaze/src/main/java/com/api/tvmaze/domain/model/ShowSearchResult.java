package com.api.tvmaze.domain.model;

import java.util.List;

public record ShowSearchResult(
		Long id,
        String name,
        String channel,
        String summary,
        List<String> genres,
        List<Comment> comments
){}
