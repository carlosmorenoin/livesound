package com.livesound.live.user.core;

import lombok.Data;

@Data
public class UserAddedEvent {

	public UserAddedEvent(User user) {
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.userName = user.getUserName();
		this.email = user.getEmail();
		this.role = user.getRole();
	}

	private String firstName;
	private String lastName;
	private String userName;
	private String email;
	private UserRole role;
}
