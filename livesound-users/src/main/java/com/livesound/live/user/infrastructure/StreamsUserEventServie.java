package com.livesound.live.user.infrastructure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;

import com.livesound.live.user.core.UserAddedEvent;
import com.livesound.live.user.core.UserEventService;

@Component
public class StreamsUserEventServie implements UserEventService {

	private UsersStream userStream;

	public StreamsUserEventServie(final UsersStream userStream) {
		this.userStream = userStream;
	}

	@Override public void send(final UserAddedEvent event) {
		final Message<UserAddedEvent> message = MessageBuilder.withPayload(event)
				.setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON).build();
		userStream.outboundUsers().send(message);
	}
}
