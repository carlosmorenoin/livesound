package com.livesound.live.profiles.infrastructure;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.livesound.live.profiles.core.Artist;
import com.livesound.live.profiles.core.Host;
import com.livesound.live.profiles.core.ProfileService;

import reactor.core.publisher.Mono;

@RestController
public class ProfileAPI {

	private ProfileService profileService;

	public ProfileAPI(final ProfileService profileService) {
		this.profileService = profileService;
	}

	@GetMapping(value = "/host/{hostId}/profile", produces = "application/json")
	public Mono<Host> getHostProfile(@PathVariable final String hostId) {
		return profileService.getHostProfile(hostId);
	}

	@GetMapping(value = "/artist/{artistId}/profile", produces = "application/json")
	public Mono<Artist> getArtistProfile(@PathVariable final String artistId) {
		return profileService.getArtistProfile(artistId);
	}
}
