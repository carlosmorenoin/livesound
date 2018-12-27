package com.livesound.live.user.core;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class User {
	@NotNull
	private String userName;
	@NotNull @Email
	private String email;
	@NotNull
	private String password;
	@NotNull
	private String firstName;
	@NotNull
	private String lastName;
	@NotNull
	private UserRole role;
}
