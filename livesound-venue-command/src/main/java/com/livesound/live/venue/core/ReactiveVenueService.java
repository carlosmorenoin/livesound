package com.livesound.live.venue.core;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.util.StringUtils;

import com.livesound.live.venue.event.VenueEvent;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

public class ReactiveVenueService implements VenueService {

	private VenueRepository venueRepository;
	private Scheduler       scheduler;
	private MessageService<VenueEvent> venuesMessageService;

	public ReactiveVenueService(VenueRepository venueRepository, MessageService<VenueEvent> venuesMessageService) {
		this.venueRepository = venueRepository;
		this.venuesMessageService = venuesMessageService;
		final ExecutorService executorService = Executors.newSingleThreadExecutor();
		scheduler = Schedulers.fromExecutorService(executorService);
	}

	@Override
	public Mono<Venue> addVenue(final Venue venue) {
		if (venue == null) {
			throw new IllegalArgumentException("Adding a null venue");
		}
		if (StringUtils.isEmpty(venue.getHost())) {
			throw new IllegalArgumentException("Adding venue without a host");
		}
		return Mono.just(venue).publishOn(Schedulers.elastic())
				.map(VenueEvent::createAdded)
				.flatMap(venueRepository::save)
				.doOnNext(event -> event.setVenueId(event.getId()))
				.doOnNext(venuesMessageService::send)
				.doOnNext(event -> venue.setId(event.getId()))
				.map(event -> venue)
				.publishOn(scheduler);
	}

	@Override
	public Mono<Venue> updateVenue(Venue venue) {
		if (venue == null) {
			throw new IllegalArgumentException("Adding a null venue");
		}
		if (StringUtils.isEmpty(venue.getHost())) {
			throw new IllegalArgumentException("Adding venue without a host");
		}
		return Mono.just(venue).publishOn(Schedulers.elastic())
				.filter(this::doesVenueExist)
				.map(VenueEvent::createUpdated)
				.flatMap(venueRepository::save)
				.doOnNext(venuesMessageService::send)
				.map(updateEvent -> venue)
				.publishOn(scheduler);
	}

	@Override public Mono<String> deleteVenueById(String venueId) {
		return Mono.just(venueId)
				.flatMap(venueRepository::findById)
				.map(VenueEvent::getId)
				.filterWhen(this::haveNotBeenDeleted)
				.map(VenueEvent::createDeleted)
				.flatMap(venueRepository::save)
				.doOnNext(venuesMessageService::send)
				.map(VenueEvent::getVenueId)
				.publishOn(scheduler);
	}

	private boolean doesVenueExist(final Venue venue) {
		if (venue != null && !StringUtils.isEmpty(venue.getId())){
			return venueRepository.findById(venue.getId()).filter(e -> e.getType() == VenueEvent.Type.ADDED).hasElement().block();
		}
		return false;
	}

	private Mono<Boolean> haveNotBeenDeleted(final String venueId) {
		return venueRepository.findByVenueIdAndType(venueId, VenueEvent.Type.DELETED)
				.hasElements().map(foundDeleted -> !foundDeleted);
	}
}
