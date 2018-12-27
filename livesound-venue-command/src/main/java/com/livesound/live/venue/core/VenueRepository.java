package com.livesound.live.venue.core;

import com.livesound.live.venue.event.VenueEvent;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface VenueRepository {
	<S extends VenueEvent> Mono<S> save(S venue);
	Mono<VenueEvent> findById(String eventId);
	Flux<VenueEvent> findByVenueIdAndType(String venueId, VenueEvent.Type type);
}
