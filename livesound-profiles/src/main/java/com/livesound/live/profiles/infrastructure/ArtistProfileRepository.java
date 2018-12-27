package com.livesound.live.profiles.infrastructure;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.livesound.live.profiles.core.Artist;
import com.livesound.live.profiles.core.ProfileRepository;

public interface ArtistProfileRepository extends ProfileRepository<Artist, String>, ReactiveMongoRepository<Artist, String> {

}
