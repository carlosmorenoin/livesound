package com.livesound.live.user.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ReactiveUserServiceTest {

	private UserRepository userRepository;
	private UserEventService userEventService;

	private ReactiveUserService sut;

	@BeforeEach
	void setup() {
		userRepository = Mockito.mock(UserRepository.class);
		userEventService = Mockito.mock(UserEventService.class);
		sut = new ReactiveUserService(userRepository, userEventService);
	}

	@Test void addNullUser() {
		assertThrows(IllegalArgumentException.class, () -> sut.addUser(null));
	}

	@Test void addUser() {
		User user = mockUser();

		when(userRepository.save(user)).thenReturn(Mono.just(user));

		final Mono<User> added = sut.addUser(user);
		assertEquals(user, added.block());
		verify(userEventService, atLeastOnce()).send(any(UserAddedEvent.class));
	}

	@Test void findByNullUserName() {
		assertThrows(IllegalArgumentException.class, () -> sut.findByUserName(null));
	}

	@Test void findByNotExistentUserName() {
		when(userRepository.findByUserName("test")).thenReturn(Mono.empty());

		final Mono<User> test = sut.findByUserName("test");
		test.subscribe(user -> fail("Should not return any user"));
	}

	@Test void findByUserName() {
		User user = mockUser();

		when(userRepository.findByUserName("test")).thenReturn(Mono.just(user));

		final Mono<User> test = sut.findByUserName("test");
		assertEquals(user, test.block());
	}

	private User mockUser() {
		User user = new User();
		user.setUserName("username");
		user.setFirstName("first name");
		user.setLastName("last name");
		user.setEmail("email");
		return user;
	}
}
