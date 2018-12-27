package com.livesound.live.venue.core;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.Data;

@Data
public class Venue {

	private static final String PHONE_NUMBER_REGEX = "^\\+?[\\d\\ \\(\\)]{3,15}";

	private String id;

	@NotNull
	private String name;
	@NotNull
	private String description;
	private Address address;
	@NotNull
	@Email
	private String email;
	@Pattern(regexp = PHONE_NUMBER_REGEX)
	private String phone;
	private String host;

}
