package com.livesound.live.profiles.infrastructure;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.livesound.live.profiles.core.ProfileRepository;
import com.livesound.live.profiles.core.User;

public interface UserProfileRepository extends ProfileRepository<User, String>, ReactiveMongoRepository<User, String> {

}
