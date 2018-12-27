package com.livesound.live.profiles.core;

import org.apache.commons.lang.StringUtils;

import reactor.core.publisher.Mono;

public class ReactiveProfileService implements ProfileService {

	private ProfileRepository<Host, String> hostRepository;
	private ProfileRepository<Artist, String> artistRepository;
	private ProfileRepository<User, String> userRepository;

	public ReactiveProfileService(final ProfileRepository<Host, String> hostRepository, final ProfileRepository<Artist, String> artistRepository,
			final ProfileRepository<User, String> userRepository) {
		this.hostRepository = hostRepository;
		this.artistRepository = artistRepository;
		this.userRepository = userRepository;
	}

	@Override public Mono<Host> getHostProfile(final String userId) {
		validateUserIdForProfile(userId, ProfileType.HOST);
		return hostRepository.findById(userId);
	}

	@Override public Mono<Artist> getArtistProfile(final String userId) {
		validateUserIdForProfile(userId, ProfileType.ARTIST);
		return artistRepository.findById(userId);
	}

	@Override public Mono<User> getUserProfile(final String userId) {
		validateUserIdForProfile(userId, ProfileType.USER);
		return userRepository.findById(userId);
	}

	private void validateUserIdForProfile(final String userId, final ProfileType type) {
		if (StringUtils.isBlank(userId)) {
			String message = String.format("A valid user ID have to be provided to retrieve a %s profile", type.toString().toLowerCase());
			throw new IllegalArgumentException(message);
		}
	}

	private enum ProfileType {
		HOST, ARTIST, USER
	}
}
