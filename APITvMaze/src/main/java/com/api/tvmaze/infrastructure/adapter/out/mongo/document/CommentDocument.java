package com.api.tvmaze.infrastructure.adapter.out.mongo.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "comments")
public class CommentDocument {

	@Id
	private String id;

	@Indexed
	private Long showId;
	private String comment;
	private Integer rating;
	private Instant createdAt;

	public CommentDocument() {
	}

	public static class Builder {
		private String id;
		private Long showId;
		private String comment;
		private Integer rating;
		private Instant createdAt;

		public Builder id(String id) {
			this.id = id;
			return this;
		}

		public Builder showId(Long showId) {
			this.showId = showId;
			return this;
		}

		public Builder comment(String comment) {
			this.comment = comment;
			return this;
		}

		public Builder rating(Integer rating) {
			this.rating = rating;
			return this;
		}

		public Builder createdAt(Instant createdAt) {
			this.createdAt = createdAt;
			return this;
		}

		public CommentDocument build() {
			return new CommentDocument(id, showId, comment, rating, createdAt);
		}
	}

	public static Builder builder() {
		return new Builder();
	}

	public CommentDocument(String id, Long showId, String comment, Integer rating, Instant createdAt) {
		this.id = id;
		this.showId = showId;
		this.comment = comment;
		this.rating = rating;
		this.createdAt = createdAt;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getShowId() {
		return showId;
	}

	public void setShowId(Long showId) {
		this.showId = showId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}
}
