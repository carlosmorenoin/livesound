package com.livesound.live.user.core;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ReactiveUserService implements UserService {

	private UserRepository userRepository;

	private UserEventService userEventService;

	public ReactiveUserService(final UserRepository userRepository, final UserEventService userEventService) {
		this.userRepository = userRepository;
		this.userEventService = userEventService;
	}

	@Override public Mono<User> addUser(final User user) {
		if (user == null) {
			throw new IllegalArgumentException("A valid user have to be provided to the addUser method");
		}
		return userRepository.save(user).doOnNext(usr -> userEventService.send(new UserAddedEvent(usr)));
	}

	@Override public Flux<User> findByNameOrEmail(final String text) {
		// Mocked implementation
		final User user = new User();
		user.setEmail("carlosdm14@gmail.com");
		user.setFirstName("Carlos");
		user.setLastName("Moreno");
		user.setPassword("123");
		user.setUserName("carlosmorenoin");

		final User user2 = new User();
		user2.setEmail("pedro@gmail.com");
		user2.setFirstName("Pedro");
		user2.setLastName("Perez");
		user2.setPassword("123");
		user2.setUserName("pedrito");
		return Flux.just(user, user2);
	}

	@Override
	public Mono<User> findByUserName(final String userName) {
		if (userName == null) {
			throw new IllegalArgumentException("A valid user name have to be provided to retrieve the user");
		}
		return userRepository.findByUserName(userName);
	}
}
