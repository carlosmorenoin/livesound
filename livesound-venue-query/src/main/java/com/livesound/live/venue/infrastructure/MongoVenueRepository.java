package com.livesound.live.venue.infrastructure;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.livesound.live.venue.core.Venue;
import com.livesound.live.venue.core.VenueRepository;

public interface MongoVenueRepository extends VenueRepository, ReactiveMongoRepository<Venue, String> {

}
