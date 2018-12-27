package com.livesound.live.venue.infrastructure;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface VenuesStream {
	String INPUT = "venues-in";

	@Input(INPUT) SubscribableChannel inboundVenues();
}
