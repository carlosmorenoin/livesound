package com.livesound.live.user.core;

import reactor.core.publisher.Mono;

public interface UserRepository {
	<S extends User> Mono<S> save(S user);
	<S extends User> Mono<S> findByUserName(String userName);
}
