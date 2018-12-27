package com.livesound.live.venue.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

class ReactiveVenueServiceTest {

	public static final String TEST_ID = "testId";
	private VenueRepository repo;
	private ReactiveVenueService       venueService;

	@BeforeEach
	void prepare() {
		repo = Mockito.mock(VenueRepository.class);
		venueService = new ReactiveVenueService(repo);
	}

	@Test
	void addNullVenueThrowsException() {
		assertThrows(IllegalArgumentException.class, () -> venueService.addVenue(null));
	}

	@Test
	void addVenueWithoutHostThrowsException() {
		final Venue venue = new Venue();
		assertThrows(IllegalArgumentException.class, () -> venueService.addVenue(venue));
	}

	@Test
	void addVenue() {
		final String host = "testHost";
		final Venue venue = new Venue();
		venue.setHost(host);
		final Venue resultVenue = new Venue();
		resultVenue.setId("1");
		resultVenue.setHost(host);
		when(repo.save(venue)).thenReturn(Mono.just(resultVenue));

		assertEquals(resultVenue, venueService.addVenue(venue).block());
	}

	@Test
	void updateNullVenueThrowsException() {
		assertThrows(IllegalArgumentException.class, () -> venueService.updateVenue(null));
	}

	@Test
	void updateVenueWithoutHostThrowsException() {
		final Venue venue = new Venue();
		assertThrows(IllegalArgumentException.class, () -> venueService.updateVenue(venue));
	}

	@Test
	void updateNotExistingVenueDoesNothing() {
		final String id = "testId";
		final String host = "testHost";
		final Venue venue = new Venue();
		venue.setHost(host);
		venue.setId(id);
		when(repo.findById(id)).thenReturn(Mono.empty());

		venueService.updateVenue(venue).subscribe(v -> fail());
		verify(repo, never()).save(venue);
	}

	@Test
	void updateVenue() {
		final String id = "testId";
		final String host = "testHost";
		final Venue venue = new Venue();
		venue.setHost(host);
		venue.setId(id);
		final Venue resultVenue = new Venue();
		resultVenue.setId(id);
		resultVenue.setHost(host);
		when(repo.findById(id)).thenReturn(Mono.just(venue));
		when(repo.save(venue)).thenReturn(Mono.just(resultVenue));

		assertEquals(resultVenue, venueService.updateVenue(venue).block());
	}

	@Test
	void findVenue() {
		final String venueId = "venue1";
		Venue venue = Mockito.mock(Venue.class);
		when(repo.findById(venueId)).thenReturn(Mono.just(venue));

		Mono<Venue> venueMono = venueService.findVenue(venueId);
		assertEquals(venue, venueMono.block());
		verify(repo, times(1)).findById(venueId);
	}

	@Test
	void findVenueWithEmptyIdThrowsException() {
		final String venueId = StringUtils.EMPTY;

		assertThrows(IllegalArgumentException.class, () -> venueService.findVenue(venueId));
		verify(repo, never()).findById(venueId);
	}

	@Test void findVenuesByHost() {
		final String hostId = "host1";
		Venue venue = Mockito.mock(Venue.class);
		Flux<Venue> venueFlux = Flux.just(venue);
		when(repo.findByHost(hostId)).thenReturn(venueFlux);

		Flux<Venue> result = venueService.findVenuesByHost(hostId);

		assertEquals(venue, result.blockFirst());
		verify(repo, times(1)).findByHost(hostId);
	}

	@Test
	void findVenueWithEmptyHostIdThrowsException() {
		final String hostId = StringUtils.EMPTY;

		assertThrows(IllegalArgumentException.class, () -> venueService.findVenuesByHost(hostId));
		verify(repo, never()).findById(hostId);
	}

	@Test
	void deleteVenueWithNullIdThrowsException() {
		assertThrows(IllegalArgumentException.class, () -> venueService.deleteVenue(null));
	}

	@Test
	void deleteVenueWithEmptyIdThrowsException() {
		assertThrows(IllegalArgumentException.class, () -> venueService.deleteVenue(StringUtils.EMPTY));
	}

	@Test
	void deleteVenueForNotExistingId() {
		when(repo.findById(TEST_ID)).thenReturn(Mono.empty());
		venueService.deleteVenue(TEST_ID);
		verify(repo, never()).delete(any(Venue.class));
	}

	@Test
	void deleteVenue() {
		final Venue venue = new Venue();
		when(repo.findById(TEST_ID)).thenReturn(Mono.just(venue));
		venueService.deleteVenue(TEST_ID);
		verify(repo, never()).delete(venue);
	}
}
