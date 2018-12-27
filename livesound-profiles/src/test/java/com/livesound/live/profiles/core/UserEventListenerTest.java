package com.livesound.live.profiles.core;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.livesound.live.profiles.infrastructure.UserAddedEvent;

import reactor.core.publisher.Mono;

class UserEventListenerTest {

	final ProfileRepository hostRepository = Mockito.mock(ProfileRepository.class);
	final ProfileRepository artistRepository = Mockito.mock(ProfileRepository.class);
	final ProfileRepository userRepository = Mockito.mock(ProfileRepository.class);

	private UserEventListener sut;
	@BeforeEach void setUp() {

		sut = new UserEventListener(hostRepository, artistRepository, userRepository);
	}

	@Test void hostEventSavesOnHostRepo() {
		UserAddedEvent event = new UserAddedEvent();
		event.setRole(UserRole.HOST);

		when(hostRepository.save(any(Host.class))).thenAnswer((invocation) -> Mono.just(invocation.getArguments()[0]));

		sut.handleEvent(event);
		verify(hostRepository).save(any(Host.class));
		verify(artistRepository, never()).save(any());
		verify(userRepository, never()).save(any());
	}

	@Test void artistEventSavesOnArtistRepo() {
		UserAddedEvent event = new UserAddedEvent();
		event.setRole(UserRole.ARTIST);

		when(artistRepository.save(any(Artist.class))).thenAnswer((invocation) -> Mono.just(invocation.getArguments()[0]));

		sut.handleEvent(event);
		verify(artistRepository).save(any(Artist.class));
		verify(hostRepository, never()).save(any());
		verify(userRepository, never()).save(any());
	}

	@Test void userEventSavesOnHostRepo() {
		UserAddedEvent event = new UserAddedEvent();
		event.setRole(UserRole.USER);

		when(userRepository.save(any(User.class))).thenAnswer((invocation) -> Mono.just(invocation.getArguments()[0]));

		sut.handleEvent(event);
		verify(userRepository).save(any(User.class));
		verify(hostRepository, never()).save(any());
		verify(artistRepository, never()).save(any());
	}

	@Test void notKnownEventNotSavedOnAnyRepo() {
		UserAddedEvent event = new UserAddedEvent();
		event.setRole(UserRole.ADMIN);

		sut.handleEvent(event);
		verify(hostRepository, never()).save(any());
		verify(artistRepository, never()).save(any());
		verify(userRepository, never()).save(any());
	}
}
