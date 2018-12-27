package com.livesound.live.profiles.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.livesound.live.profiles.infrastructure.UserAddedEvent;

import reactor.core.publisher.Mono;

public class UserEventListener {

	private static final Logger                            LOG = LoggerFactory.getLogger(UserEventListener.class);
	private              ProfileRepository<Host, String>   hostRepository;
	private              ProfileRepository<Artist, String> artistRepository;
	private              ProfileRepository<User, String>   userRepository;

	public UserEventListener(final ProfileRepository<Host, String> hostRepository, final ProfileRepository<Artist, String> artistRepository,
			final ProfileRepository<User, String> userRepository) {
		this.hostRepository = hostRepository;
		this.artistRepository = artistRepository;
		this.userRepository = userRepository;
	}

	public void handleEvent(final UserAddedEvent event) {
		final ProfileBuilder profileBuilder = new ProfileBuilder(event.getUserName());
		profileBuilder.firstName(event.getFirstName()).lastName(event.getLastName()).email(event.getEmail());
		switch (event.getRole()) {
			case HOST:
				Mono.just(profileBuilder)
						.flatMap(this::addHostProfile)
						.subscribe(host -> LOG.info("Host added {}", host)); break;
			case ARTIST:
				Mono.just(profileBuilder)
						.flatMap(this::addArtistProfile)
						.subscribe(artist -> LOG.info(" 	 added {}", artist)); break;
			case USER:
				Mono.just(profileBuilder)
						.flatMap(this::addUserProfile)
						.subscribe(user -> LOG.info("User added {}", user)); break;
			default:
				LOG.error("Cannot recognise the type of the added user {}", event);
		}
	}

	private Mono<Host> addHostProfile(final ProfileBuilder profileBuilder) {
		final Host host = profileBuilder.buildHost();
		return hostRepository.save(host);
	}

	private Mono<Artist> addArtistProfile(final ProfileBuilder profileBuilder) {
		final Artist artist = profileBuilder.buildArtist();
		return artistRepository.save(artist);
	}

	private Mono<User> addUserProfile(final ProfileBuilder profileBuilder) {
		final User user = profileBuilder.buildUser();
		return userRepository.save(user);
	}
}
