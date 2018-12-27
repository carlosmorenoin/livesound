package com.livesound.live.profiles.core;

import lombok.Data;

@Data
public class Host {

	private final String      id;
	private Type        type;
	private Person      user;
	private Company     company;
	private ContactInfo contactInfo = new ContactInfo();

	public enum Type {
		COMPANY, PERSONAL
	}

	@Data public static class Company {

		private String id;
		private String name;
		private String address;
	}
}
