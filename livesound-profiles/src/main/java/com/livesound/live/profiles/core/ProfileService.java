package com.livesound.live.profiles.core;

import reactor.core.publisher.Mono;

public interface ProfileService {
	Mono<Host> getHostProfile(String userId);
	Mono<Artist> getArtistProfile(String userId);
	Mono<User> getUserProfile(String userId);
}
