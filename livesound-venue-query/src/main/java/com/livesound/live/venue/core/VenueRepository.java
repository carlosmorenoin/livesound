package com.livesound.live.venue.core;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface VenueRepository {
	<S extends Venue> Mono<S> save(S venue);
	Mono<Venue> findById(String id);
	Flux<Venue> findByHost(String host);
	Mono<Void> delete(Venue venue);
}
