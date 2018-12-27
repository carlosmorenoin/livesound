package com.livesound.live.venue.infrastructure;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

import com.livesound.live.venue.core.Venue;
import com.livesound.live.venue.event.VenueEvent;

class VenueMessageServiceTest {


	@Test
	void sendEvent() {
		VenuesStream venuesStream = Mockito.mock(VenuesStream.class);
		MessageChannel messageChannel = Mockito.mock(MessageChannel.class);
		when(venuesStream.outboundVenues()).thenReturn(messageChannel);

		Venue venue = new Venue();
		venue.setId("test");
		VenueEvent event = VenueEvent.createAdded(venue);

		VenueMessageService venueMessageService = new VenueMessageService(venuesStream);
		venueMessageService.send(event);

		verify(venuesStream, times(1)).outboundVenues();
		verify(messageChannel, times(1)).send(any(Message.class));
	}
}
