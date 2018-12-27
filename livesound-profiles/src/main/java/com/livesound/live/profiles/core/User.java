package com.livesound.live.profiles.core;

import lombok.Data;

@Data
public class User extends Person {
	private final String id;
	private String email;
}
