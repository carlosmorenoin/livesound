package com.livesound.live.venue.core;


public interface MessageService<T> {
	void send(T message);
}
