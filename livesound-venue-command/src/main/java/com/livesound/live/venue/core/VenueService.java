package com.livesound.live.venue.core;

import reactor.core.publisher.Mono;

public interface VenueService {
	Mono<Venue> addVenue(Venue venue);
	Mono<Venue> updateVenue(Venue venue);
	Mono<String> deleteVenueById(String venueId);
}
