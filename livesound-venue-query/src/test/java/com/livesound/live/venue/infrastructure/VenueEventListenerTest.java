package com.livesound.live.venue.infrastructure;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.livesound.live.venue.core.Venue;
import com.livesound.live.venue.core.VenueService;
import com.livesound.live.venue.event.VenueEvent;

import reactor.core.publisher.Mono;

class VenueEventListenerTest {

	private static final String ID = "id";
	private VenueService venueService;
	private VenueEventListener venueEventListener;

	@BeforeEach void setUp() {
		venueService = Mockito.mock(VenueService.class);
		venueEventListener = new VenueEventListener(venueService);
	}

	@Test void handleVenueAdded() {
		Venue venue = new Venue();
		VenueEvent event = new VenueEvent("id");
		event.setType(VenueEvent.Type.ADDED);
		event.setVenue(venue);
		venue.setId("id");
		when(venueService.addVenue(venue)).thenReturn(Mono.just(venue));
		venueEventListener.handleVenue(event);

		verify(venueService, times(1)).addVenue(eq(venue));
	}

	@Test void handleVenueUpdated() {
		Venue venue = new Venue();
		VenueEvent event = new VenueEvent(ID);
		event.setType(VenueEvent.Type.UPDATED);
		event.setVenue(venue);
		venue.setId(ID);
		when(venueService.updateVenue(venue)).thenReturn(Mono.just(venue));
		venueEventListener.handleVenue(event);

		verify(venueService, times(1)).updateVenue(eq(venue));
	}

	@Test void handleVenueDeleted() {
		Venue venue = new Venue();
		VenueEvent event = new VenueEvent(ID);
		event.setType(VenueEvent.Type.DELETED);
		event.setVenue(venue);
		event.setVenueId(ID);

		when(venueService.deleteVenue(ID)).thenReturn(Mono.empty());
		venueEventListener.handleVenue(event);

		verify(venueService, times(1)).deleteVenue(ID);
	}
}
