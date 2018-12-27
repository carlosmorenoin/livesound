package com.livesound.live.profiles.infrastructure;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.livesound.live.profiles.core.Host;
import com.livesound.live.profiles.core.ProfileRepository;

public interface HostProfileRepository extends ProfileRepository<Host, String>, ReactiveMongoRepository<Host, String> {

}
