package com.livesound.live.user.infrastructure;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.livesound.live.user.core.User;
import com.livesound.live.user.core.UserRepository;

public interface MongoUserRepository extends UserRepository, ReactiveMongoRepository<User, String> {

}
