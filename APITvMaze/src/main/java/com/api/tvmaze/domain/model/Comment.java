package com.api.tvmaze.domain.model;

import java.time.Instant;

public record Comment(
		 String id,
		 Long showId,
		 String comment,
		 Integer rating,
		 Instant createdAt
){
	
		public Comment(Long showId, String comment, Integer rating) {
	        this(null, showId, comment, rating, Instant.now());
	    }
}
