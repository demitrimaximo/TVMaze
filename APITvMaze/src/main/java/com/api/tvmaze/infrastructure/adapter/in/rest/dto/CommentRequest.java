package com.api.tvmaze.infrastructure.adapter.in.rest.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CommentRequest(
        @NotNull Long showId,
        @NotBlank String comment,
        @NotNull @Min(0) @Max(5) Integer rating
) {}