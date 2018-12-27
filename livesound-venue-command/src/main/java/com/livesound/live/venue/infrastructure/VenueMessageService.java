package com.livesound.live.venue.infrastructure;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.MimeTypeUtils;

import com.livesound.live.venue.core.MessageService;
import com.livesound.live.venue.event.VenueEvent;


public class VenueMessageService implements MessageService<VenueEvent> {

	private VenuesStream venuesStream;

	public VenueMessageService(final VenuesStream venuesStream) {
		this.venuesStream = venuesStream;
	}

	@Override public void send(final VenueEvent venueEvent) {
		final Message<VenueEvent> message = MessageBuilder.withPayload(venueEvent)
				.setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON).build();
		venuesStream.outboundVenues().send(message);
	}
}
