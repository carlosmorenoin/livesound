package com.livesound.live.profiles.core;

import reactor.core.publisher.Mono;

public interface ProfileRepository<T,ID> {
	<S extends T> Mono<S> save(S entity);
	Mono<T> findById(ID id);
}
