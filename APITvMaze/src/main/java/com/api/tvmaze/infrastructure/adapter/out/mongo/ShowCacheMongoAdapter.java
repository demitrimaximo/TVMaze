package com.api.tvmaze.infrastructure.adapter.out.mongo;

import org.springframework.stereotype.Repository;

import com.api.tvmaze.domain.model.Show;
import com.api.tvmaze.domain.port.out.ShowCachePort;
import com.api.tvmaze.infrastructure.adapter.out.mongo.document.ShowCacheDocument;
import com.api.tvmaze.infrastructure.adapter.out.mongo.repository.ShowCacheMongoRepository;

import reactor.core.publisher.Mono;

@Repository
public class ShowCacheMongoAdapter implements ShowCachePort {

	private final ShowCacheMongoRepository repository;
	
	public ShowCacheMongoAdapter(ShowCacheMongoRepository repository) {
		this.repository = repository;
	}
	
	@Override
    public Mono<Show> findById(Long id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public Mono<Show> save(Show show) {
    	
        if (show.id() == null) {
            return Mono.error(new IllegalArgumentException("Cannot cache show without id"));
        }
        
        ShowCacheDocument doc = ShowCacheDocument.builder()
                .id(show.id())
                .name(show.name())
                .channel(show.channel())
                .summary(show.summary())
                .genres(show.genres())
                .build();
        return repository.save(doc).map(this::toDomain);
    }

    private Show toDomain(ShowCacheDocument doc) {
        return new Show(
                doc.getId(),
                doc.getName(),
                doc.getChannel(),
                doc.getSummary(),
                doc.getGenres()
        );
    }
}
