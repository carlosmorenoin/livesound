package com.livesound.live.venue.infrastructure;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface VenuesStream {
	String OUTPUT = "venues-out";

	@Output(OUTPUT) MessageChannel outboundVenues();
}
