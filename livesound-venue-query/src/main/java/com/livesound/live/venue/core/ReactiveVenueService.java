package com.livesound.live.venue.core;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.util.StringUtils;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;


public class ReactiveVenueService implements VenueService {

	private VenueRepository venueRepository;
	private Scheduler       scheduler;
	ReactiveMongoRepository<Venue, String> repo;

	public ReactiveVenueService(VenueRepository venueRepository) {
		this.venueRepository = venueRepository;
		final ExecutorService executorService = Executors.newSingleThreadExecutor();
		scheduler = Schedulers.fromExecutorService(executorService);
	}

	@Override public Mono<Venue> addVenue(final Venue venue) {
		if (venue == null) {
			throw new IllegalArgumentException("Adding a null venue");
		}
		if (StringUtils.isEmpty(venue.getHost())) {
			throw new IllegalArgumentException("Adding venue without a host");
		}
		return Mono.just(venue).publishOn(reactor.core.scheduler.Schedulers.elastic())
				.flatMap(venueRepository::save)
				.publishOn(scheduler);
	}

	@Override public Mono<Venue> updateVenue(final Venue venue) {
		if (venue == null) {
			throw new IllegalArgumentException("Adding a null venue");
		}
		if (StringUtils.isEmpty(venue.getHost())) {
			throw new IllegalArgumentException("Adding venue without a host");
		}
		return Mono.just(venue).publishOn(Schedulers.elastic())
				.flatMap(v -> venueRepository.findById(v.getId()))
				.flatMap(v -> venueRepository.save(venue))
				.publishOn(scheduler);
	}

	@Override public Mono<Venue> findVenue(final String venueId) {
		if (StringUtils.isEmpty(venueId)) {
			throw new IllegalArgumentException("Cannot find venue with empty ID");
		}
		return Mono.just(venueId)
				.publishOn(Schedulers.elastic())
				.flatMap(venueRepository::findById)
				.publishOn(scheduler);
	}

	@Override public Flux<Venue> findVenuesByHost(final String hostId) {
		if (StringUtils.isEmpty(hostId)) {
			throw new IllegalArgumentException("Cannot find venue with empty host ID");
		}
		return Flux.just(hostId)
				.publishOn(Schedulers.elastic())
				.flatMap(venueRepository::findByHost)
				.publishOn(scheduler);
	}

	@Override public Mono<Void> deleteVenue(final String venueId) {
		if (StringUtils.isEmpty(venueId)) {
			throw new IllegalArgumentException("Cannot delete venue with empty ID");
		}
		return Mono.just(venueId)
				.publishOn(Schedulers.elastic())
				.flatMap(venueRepository::findById)
				.flatMap(venueRepository::delete)
				.publishOn(scheduler);
	}

}
