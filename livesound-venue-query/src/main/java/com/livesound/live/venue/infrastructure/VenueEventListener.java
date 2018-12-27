package com.livesound.live.venue.infrastructure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.livesound.live.venue.core.Venue;
import com.livesound.live.venue.core.VenueService;
import com.livesound.live.venue.event.VenueEvent;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class VenueEventListener {

	private static final Logger LOG = LoggerFactory.getLogger(VenueEventListener.class);

	private VenueService venueService;

	public VenueEventListener(final VenueService venueService) {
		this.venueService = venueService;
	}

	@StreamListener(VenuesStream.INPUT)
	public void handleVenue(@Payload VenueEvent venueEvent) {
		LOG.info("Received event: {}", venueEvent);
		switch (venueEvent.getType()) {
			case ADDED:
				venueService.addVenue(getVenueFromEvent(venueEvent))
						.subscribe(v -> LOG.info("Venue {} added", v));
				break;
			case UPDATED:
				venueService.updateVenue(getVenueFromEvent(venueEvent))
						.subscribe(v -> LOG.info("Venue {} updated", v));
				break;
			case DELETED:
				venueService.deleteVenue(venueEvent.getVenueId())
						.subscribe(v -> LOG.info("Venue {} deleted", venueEvent.getVenueId()));
				break;
			default:
				LOG.warn("Received an unknown event {}", venueEvent);
		}
	}

	private Venue getVenueFromEvent(final VenueEvent venueEvent) {
		final Venue venue = venueEvent.getVenue();
		venue.setId(venueEvent.getVenueId());
		return venue;
	}
}
