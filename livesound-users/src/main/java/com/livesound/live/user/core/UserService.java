package com.livesound.live.user.core;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {
	Mono<User> addUser(User user);
	Flux<User> findByNameOrEmail(String text);
	Mono<User> findByUserName(String userName);

}
