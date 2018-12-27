package com.livesound.live.user.infrastructure;

import com.livesound.live.user.core.User;
import com.livesound.live.user.core.UserAddedEvent;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.messaging.MessageChannel;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class StreamsUserEventServieTest {

	StreamsUserEventServie sut;

	@Test void send() {
		final UserAddedEvent event = new UserAddedEvent(new User());
		final UsersStream usersStream = Mockito.mock(UsersStream.class);
		final MessageChannel messageChannel = Mockito.mock(MessageChannel.class);

		when(usersStream.outboundUsers()).thenReturn(messageChannel);

		sut = new StreamsUserEventServie(usersStream);
		sut.send(event);
		verify(messageChannel, times(1)).send(any());
	}
}
