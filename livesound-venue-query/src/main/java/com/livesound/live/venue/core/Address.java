package com.livesound.live.venue.core;

import lombok.Data;

@Data
public class Address {
	private String addressText;
	private String more;
	private String city;
	private String region;
	private String zipCode;
}
