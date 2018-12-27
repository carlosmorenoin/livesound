package com.livesound.live.profiles.core;

import lombok.Data;

@Data
public class Person {
	private String firstName;
	private String lastName;

	public Person() {

	}

	public Person(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}
}
