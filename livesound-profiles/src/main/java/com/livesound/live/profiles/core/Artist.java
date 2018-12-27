package com.livesound.live.profiles.core;

import lombok.Data;

@Data
public class Artist {

	private final String id;
	private String name;
	private Type type;
	private Person user;
	private ContactInfo contactInfo = new ContactInfo();

	public enum Type {
		GROUP, INDIVIDUAL
	}
}
