package com.livesound.live.venue.infrastructure;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.livesound.live.venue.core.HostService;
import com.livesound.live.venue.core.Venue;
import com.livesound.live.venue.core.VenueService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/host/{hostId}/venues")
public class VenueAPI {

	@Autowired
	private VenueService venueService;

	@Autowired
	private HostService hostService;

	public VenueAPI(VenueService venueService) {
		this.venueService = venueService;
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public Mono<ResponseEntity<String>> addVenue(@RequestHeader("Authorization") String token, @RequestBody @Valid Venue venue,
			@PathVariable("hostId") String hostId, final ServletUriComponentsBuilder uriBuilder) {

		if (this.hostService.isValidHostId(hostId, token)) {
			venue.setHost(hostId);
			return this.venueService.addVenue(venue)
					.map(newVenue ->  uriBuilder.path("/host/{hostId}/venues/{venueId}").buildAndExpand(hostId, newVenue.getId()).toUri())
					.map(location -> ResponseEntity.created(location).build());
		} else {
			return Mono.just(ResponseEntity.badRequest().body("Wrong host ID"));
		}
	}

	@PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<ResponseEntity> updateVenue(@RequestBody  @Valid Venue venue, @PathVariable("hostId") String hostId, @PathVariable("id") final String venueId) {
		if (!StringUtils.isEmpty(venue.getId()) && !venue.getId().equals(venueId)) {
			return Mono.just(ResponseEntity.badRequest().build());
		}
		venue.setId(venueId);
		venue.setHost(hostId);
		return this.venueService.updateVenue(venue)
				.map(updatedVenue -> (ResponseEntity)ResponseEntity.ok().body(updatedVenue))
				.defaultIfEmpty(ResponseEntity.notFound().build())
				.onErrorResume(e -> Mono.just(ResponseEntity.status(500).body(e.toString())));
	}

	@DeleteMapping(value = "/{id}")
	public Mono<ResponseEntity> deleteVenue(@PathVariable("id") final String venueId) {
		return this.venueService.deleteVenueById(venueId)
				.map(deletedId -> (ResponseEntity)ResponseEntity.ok().build())
				.defaultIfEmpty(ResponseEntity.notFound().build())
				.onErrorResume(e -> Mono.just(ResponseEntity.status(500).body(e.toString())));
	}
}
