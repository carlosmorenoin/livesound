package com.livesound.live.venue.infrastructure;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.livesound.live.venue.core.VenueRepository;
import com.livesound.live.venue.event.VenueEvent;

public interface MongoVenueRepository extends VenueRepository, ReactiveMongoRepository<VenueEvent, String> {

}
