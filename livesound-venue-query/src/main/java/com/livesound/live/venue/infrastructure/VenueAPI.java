package com.livesound.live.venue.infrastructure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.livesound.live.venue.core.Venue;
import com.livesound.live.venue.core.VenueService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/host/{hostId}/venues")
public class VenueAPI {

	@Autowired
	private VenueService venueService;

	public VenueAPI(VenueService venueService) {
		this.venueService = venueService;
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public Flux<Venue> findVenues(@PathVariable("hostId") String hostId) {
		return this.venueService.findVenuesByHost(hostId);
	}

	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<ResponseEntity> findVenue(@PathVariable("id") final String venueId) {
		return this.venueService.findVenue(venueId)
				.map(v -> (ResponseEntity)ResponseEntity.ok().body(v))
				.defaultIfEmpty(ResponseEntity.notFound().build())
				.onErrorResume(e -> Mono.just(ResponseEntity.status(500).body(e.toString())));
	}
}
