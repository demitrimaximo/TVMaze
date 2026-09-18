package com.api.tvmaze.domain.port.out;

import com.api.tvmaze.domain.model.Show;

import reactor.core.publisher.Mono;

/**
 * Puerto de salida para el caché de shows.
 * Abstrae la persistencia de la caché del dominio.
 */
public interface ShowCachePort {
    Mono<Show> findById(Long id);
    Mono<Show> save(Show show);
}
