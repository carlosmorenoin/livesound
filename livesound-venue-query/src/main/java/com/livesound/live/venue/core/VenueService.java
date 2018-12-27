package com.livesound.live.venue.core;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface VenueService {
	Mono<Venue> addVenue(Venue venue);
	Mono<Venue> updateVenue(Venue venue);
	Mono<Venue> findVenue(String venueId);
	Flux<Venue> findVenuesByHost(String hostId);
	Mono<Void> deleteVenue(String venueId);
}
