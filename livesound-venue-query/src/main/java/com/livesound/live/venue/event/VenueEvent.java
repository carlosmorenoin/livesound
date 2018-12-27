package com.livesound.live.venue.event;

import org.springframework.data.annotation.Id;

import com.livesound.live.venue.core.Venue;

import lombok.Data;

@Data
public class VenueEvent {

	@Id
	private String id;
	private String venueId;
	private Type  type;
	private Venue venue;

	public VenueEvent() {
		// Used by spring data
	}

	public VenueEvent(String id) {
		this.id = id;
	}

	public enum Type {
		ADDED, UPDATED, DELETED
	}
}
