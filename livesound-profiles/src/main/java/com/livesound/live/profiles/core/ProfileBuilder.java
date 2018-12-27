package com.livesound.live.profiles.core;

class ProfileBuilder {

	private final String id;
	private String       firstName;
	private String       lastName;
	private String       email;

	ProfileBuilder(final String id) {
		this.id = id;
	}

	public ProfileBuilder firstName(final String firstName) {
		this.firstName = firstName;
		return this;
	}

	public ProfileBuilder lastName(final String lastName) {
		this.lastName = lastName;
		return this;
	}

	public ProfileBuilder email(final String email) {
		this.email = email;
		return this;
	}

	public Host buildHost() {
		final Host host = new Host(this.id);
		host.setUser(new Person(this.firstName, this.lastName));
		host.getContactInfo().setEmail(this.email);
		return host;
	}

	public Artist buildArtist() {
		final Artist artist = new Artist(this.id);
		artist.setUser(new Person(this.firstName, this.lastName));
		artist.getContactInfo().setEmail(this.email);
		return artist;
	}

	public User buildUser() {
		final User user = new User(this.id);
		user.setFirstName(this.firstName);
		user.setLastName(this.lastName);
		user.setEmail(this.email);
		return user;
	}
}
