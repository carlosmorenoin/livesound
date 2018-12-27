package com.livesound.live.profiles.infrastructure;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface UserStream {
	String INPUT = "users-in";

	@Input(INPUT) SubscribableChannel inboundUsers();
}
