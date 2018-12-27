package com.livesound.live.user.infrastructure;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface UsersStream {
	String OUTPUT = "users-out";

	@Output(OUTPUT) MessageChannel outboundUsers();
}

