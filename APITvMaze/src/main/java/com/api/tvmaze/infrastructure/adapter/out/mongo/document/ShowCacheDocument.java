package com.api.tvmaze.infrastructure.adapter.out.mongo.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "show_cache")
public class ShowCacheDocument {

	@Id
	private Long id; // showId
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