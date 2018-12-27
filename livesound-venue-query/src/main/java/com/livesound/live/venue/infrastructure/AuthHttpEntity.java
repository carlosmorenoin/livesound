package com.livesound.live.venue.infrastructure;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

public class AuthHttpEntity {

    private HttpHeaders headers = new HttpHeaders();

    public AuthHttpEntity(final String token) {
        this.headers.set(HttpHeaders.AUTHORIZATION, token);
    }

    public HttpEntity getEntity() {
        return new HttpEntity(headers);
    }
}
