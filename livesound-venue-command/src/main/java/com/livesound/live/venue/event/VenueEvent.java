package com.livesound.live.venue.event;

import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;

import com.livesound.live.venue.core.Address;

import lombok.Data;

import java.util.Date;

@Data
public class VenueEvent {

	@Id
	private String id;
	private String venueId;
	private Type type;
	private Venue venue;

	@CreatedDate
	private Date creationDate;

	public VenueEvent() {
		// Used by spring data
	}

	private VenueEvent(final String venueId, final Type type, final Venue venue) {
		this.venueId = venueId;
		this.type = type;
		this.venue = venue;

	}

	private VenueEvent(final Type type, final Venue venue) {
		this.type = type;
		this.venue = venue;
	}

	private VenueEvent(final String venueId, final Type type) {
		this.venueId = venueId;
		this.type = type;
	}

	@Data
	public static class Venue {
		private String name;
		private String description;
		private Address address;
		private String email;
		private String phone;
		private String host;
	}

	public enum Type {
		ADDED, UPDATED, DELETED
	}

	public static VenueEvent createAdded(final com.livesound.live.venue.core.Venue venue) {
		return new VenueEvent(Type.ADDED, getPayloadFromVenue(venue));
	}
	public static VenueEvent createUpdated(final com.livesound.live.venue.core.Venue venue) {
		return new VenueEvent(venue.getId(), Type.UPDATED, getPayloadFromVenue(venue));
	}
	public static VenueEvent createDeleted(final String venueId) {
		return new VenueEvent(venueId, Type.DELETED);
	}

	private static Venue getPayloadFromVenue(final com.livesound.live.venue.core.Venue venue) {
		Venue payload = new Venue();
		BeanUtils.copyProperties(venue, payload);
		return payload;
	}
}
