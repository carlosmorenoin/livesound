package com.livesound.live.profiles.infrastructure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.livesound.live.profiles.core.UserEventListener;

@Component
public class StreamsUserEventListener {

	private static final Logger LOG = LoggerFactory.getLogger(StreamsUserEventListener.class);

	private UserEventListener eventListener;

	public StreamsUserEventListener(UserEventListener eventListener) {
		this.eventListener = eventListener;
	}

	@StreamListener(UserStream.INPUT)
	public void handleUser(@Payload UserAddedEvent event) {
		LOG.info("User added {}", event);
		eventListener.handleEvent(event);
	}
}
