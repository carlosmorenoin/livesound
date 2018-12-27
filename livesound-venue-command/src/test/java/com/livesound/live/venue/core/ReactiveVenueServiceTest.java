package com.livesound.live.venue.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.livesound.live.venue.event.VenueEvent;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

class ReactiveVenueServiceTest {

	private static final String VENUE_NAME = "The venue";

	private VenueRepository repo;
	private MessageService<VenueEvent> venuesMessageService;

	private ReactiveVenueService       venueService;

	@BeforeEach
	void prepare() {
		repo = Mockito.mock(VenueRepository.class);
		venuesMessageService = Mockito.mock(MessageService.class);
		venueService = new ReactiveVenueService(repo, venuesMessageService);
	}

	@Test
	void addNullVenueThrowsException() {
		assertThrows(IllegalArgumentException.class, () -> venueService.addVenue(null));
	}

	@Test
	void addVenueNullHostThrowsException() {
		final Venue venue = new Venue();
		assertThrows(IllegalArgumentException.class, () -> venueService.addVenue(venue));
	}

	@Test void addVenue() {
		final String id = "Id";
		final Venue venue = new Venue();
		venue.setName(VENUE_NAME);
		venue.setHost("host");
		final VenueEvent event = VenueEvent.createAdded(venue);
		final VenueEvent resultEvent = VenueEvent.createAdded(venue);
		resultEvent.setId(id);

		when(repo.save(event)).thenReturn(Mono.just(resultEvent));
		final Venue resultVenue = venueService.addVenue(venue).block();

		assertEquals(id, resultVenue.getId());
		venue.setId(id);
		assertEquals(venue, resultVenue);
		Mockito.verify(venuesMessageService, times(1)).send(resultEvent);
	}

	@Test
	void updateNullVenueThrowsException() {
		assertThrows(IllegalArgumentException.class, () -> venueService.updateVenue(null));
	}

	@Test
	void updateVenueNullHostThrowsException() {
		final Venue venue = new Venue();
		assertThrows(IllegalArgumentException.class, () -> venueService.updateVenue(venue));
	}

	@Test void updateNotExistingVenueThrowsException() {
		final String id = "Id";
		final Venue venue = new Venue();
		venue.setId(id);
		venue.setName(VENUE_NAME);
		venue.setHost("host");
		final VenueEvent event = VenueEvent.createUpdated(venue);

		when(repo.findById(id)).thenReturn(Mono.empty());
		final Venue resultVenue = venueService.updateVenue(venue).block();

		assertNull(resultVenue);
		Mockito.verify(venuesMessageService, never()).send(any());
	}

	@Test void updateVenue() {
		final String id = "Id";
		final Venue venue = new Venue();
		venue.setId(id);
		venue.setName(VENUE_NAME);
		venue.setHost("host");
		final VenueEvent addEvent = VenueEvent.createAdded(venue);
		venue.setName(VENUE_NAME + "-UPDATED");
		final VenueEvent updateEvent = VenueEvent.createUpdated(venue);

		when(repo.findById(id)).thenReturn(Mono.just(addEvent));
		when(repo.save(any())).thenReturn(Mono.just(updateEvent));
		final Venue resultVenue = venueService.updateVenue(venue).block();

		assertEquals(venue, resultVenue);
		Mockito.verify(venuesMessageService, times(1)).send(updateEvent);
	}

	@Test void deleteNotExistentVenueDoesNothing() {
		final String id = "Id";

		when(repo.findById(id)).thenReturn(Mono.empty());
		assertNull(venueService.deleteVenueById(id).block());
		Mockito.verify(venuesMessageService, never()).send(any());
	}
	@Test void deleteAlreadyDeletedVenueDoesNothing() {
		final String id = "Id";
		final Venue venue = new Venue();
		venue.setId(id);
		venue.setName(VENUE_NAME);
		venue.setHost("host");
		final VenueEvent addEvent = VenueEvent.createAdded(venue);
		addEvent.setId(id);
		final VenueEvent deletedEvent = VenueEvent.createDeleted(id);

		when(repo.findById(id)).thenReturn(Mono.just(addEvent));
		when(repo.findByVenueIdAndType(id, VenueEvent.Type.DELETED)).thenReturn(Flux.just(deletedEvent));

		assertNull(venueService.deleteVenueById(id).block());
		Mockito.verify(venuesMessageService, never()).send(any());
	}
	@Test void deleteVenueById() {
		final String id = "Id";
		final Venue venue = new Venue();
		venue.setId(id);
		venue.setName(VENUE_NAME);
		venue.setHost("host");
		final VenueEvent addEvent = VenueEvent.createAdded(venue);
		addEvent.setId(id);
		final VenueEvent deletedEvent = VenueEvent.createDeleted(id);

		when(repo.findById(id)).thenReturn(Mono.just(addEvent));
		when(repo.findByVenueIdAndType(id, VenueEvent.Type.DELETED)).thenReturn(Flux.empty());
		when(repo.save(any())).thenReturn(Mono.just(deletedEvent));

		assertEquals(id, venueService.deleteVenueById(id).block());
		Mockito.verify(venuesMessageService, times(1)).send(deletedEvent);
	}
}
