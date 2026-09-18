package com.api.tvmaze.infrastructure.adapter.out.mongo.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "show_cache")
public class ShowCacheDocument {

	@Id
	private Long id;
	private String name;
	private String channel;
	private String summary;
	private List<String> genres;

	public ShowCacheDocument() {
	}

	public ShowCacheDocument(Long id, String name, String channel, String summary, List<String> genres) {
		this.id = id;
		this.name = name;
		this.channel = channel;
		this.summary = summary;
		this.genres = genres;
	}
	
	private ShowCacheDocument(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.channel = builder.channel;
        this.summary = builder.summary;
        this.genres = builder.genres;
    }
	
	 public static Builder builder() {
		 return new Builder();
	 }
	 
	 public static class Builder {

	        private Long id;
	        private String name;
	        private String channel;
	        private String summary;
	        private List<String> genres;

	        public Builder id(Long id) {
	            this.id = id;
	            return this;
	        }

	        public Builder name(String name) {
	            this.name = name;
	            return this;
	        }

	        public Builder channel(String channel) {
	            this.channel = channel;
	            return this;
	        }

	        public Builder summary(String summary) {
	            this.summary = summary;
	            return this;
	        }

	        public Builder genres(List<String> genres) {
	            this.genres = genres;
	            return this;
	        }

	        public ShowCacheDocument build() {
	            return new ShowCacheDocument(this);
	        }
	    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public List<String> getGenres() {
		return genres;
	}

	public void setGenres(List<String> genres) {
		this.genres = genres;
	}
}