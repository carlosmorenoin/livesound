package com.livesound.live.venue.core;

@FunctionalInterface
public interface HostService {
    boolean isValidHostId(String hostId, String token);
}
