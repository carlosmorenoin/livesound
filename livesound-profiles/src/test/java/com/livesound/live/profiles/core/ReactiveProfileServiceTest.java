package com.livesound.live.profiles.core;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


class ReactiveProfileServiceTest {

	private ReactiveProfileService reactiveProfileService;

	@BeforeEach
	void setup() {
		final ProfileRepository hostRepository = Mockito.mock(ProfileRepository.class);
		final ProfileRepository artistRepository = Mockito.mock(ProfileRepository.class);
		final ProfileRepository userRepository = Mockito.mock(ProfileRepository.class);
		reactiveProfileService = new ReactiveProfileService(hostRepository, artistRepository, userRepository);
	}

	@Test
	void getHostProfileWithNullUserId() {
		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
				() -> reactiveProfileService.getHostProfile(null));
		assertTrue(ex.getMessage().contains("host"));
	}

	@Test
	void getArtistProfileWithNullUserId() {
		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
				() -> reactiveProfileService.getArtistProfile(null));
		assertTrue(ex.getMessage().contains("artist"));
	}

	@Test
	void getUserProfileWithNullUserId() {
		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
				() -> reactiveProfileService.getUserProfile(null));
		assertTrue(ex.getMessage().contains("user"));
	}
}
