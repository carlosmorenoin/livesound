package com.livesound.live.profiles.infrastructure;

import com.livesound.live.profiles.core.UserRole;

import lombok.Data;

@Data
public class UserAddedEvent {
	private String   firstName;
	private String   lastName;
	private String   userName;
	private String   email;
	private UserRole role;
}
