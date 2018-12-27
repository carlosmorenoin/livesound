package com.livesound.live.venue.core;

import lombok.Data;

@Data
public class Venue {
	private String id;
	private String name;
	private String description;
	private Address address;
	private String email;
	private String phone;
	private String host;

}
