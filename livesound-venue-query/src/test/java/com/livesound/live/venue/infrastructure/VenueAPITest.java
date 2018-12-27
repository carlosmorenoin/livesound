package com.livesound.live.venue.infrastructure;

import static org.mockito.BDDMockito.given;

import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.livesound.live.venue.core.Venue;
import com.livesound.live.venue.core.VenueService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
@WebFluxTest(VenueAPI.class)
class VenueAPITest {

	private static final String URI_PATTERN = "/host/%s/venues";
	@Autowired
	private WebTestClient webTestClient;

	@MockBean
	private VenueService venueService;

	@Test
	void fiendVenuesEmpty() {
		final String hostId = "testHostId";
		final String url = String.format(URI_PATTERN, hostId);
		given(venueService.findVenuesByHost(hostId)).willReturn(Flux.empty());

		webTestClient.get().uri(url)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
				.exchange()
				.expectStatus().isOk()
				.expectBody().json("[]");
	}

	@Test void findVenues() {
		final String hostId = "testHostId";
		final String url = String.format(URI_PATTERN, hostId);

		final Venue venue = mockVenue("");
		final Venue venue2 = mockVenue("2");

		given(venueService.findVenuesByHost(hostId)).willReturn(Flux.just(venue, venue2));

		WebTestClient.BodyContentSpec body = webTestClient.get().uri(url)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody();
		validateJsonForVenue(body, venue, ".[0]");
		validateJsonForVenue(body, venue2, ".[1]");
	}

	@Test void findVenue() {
		final String venueId = "testVenueId";
		final String hostId = "testHostId";
		final String url = String.format(URI_PATTERN, hostId) + "/" + venueId;
		final Venue venue = mockVenue("");
		given(venueService.findVenue(venueId)).willReturn(Mono.just(venue));

		WebTestClient.BodyContentSpec body = webTestClient.get().uri(url)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody();
		validateJsonForVenue(body, venue, StringUtils.EMPTY);
	}

	@Test void notExistentVenueNotFound() {
		final String venueId = "testVenueId";
		final String hostId = "testHostId";
		final String url = String.format(URI_PATTERN, hostId) + "/" + venueId;
		given(venueService.findVenue(venueId)).willReturn(Mono.empty());

		webTestClient.get().uri(url)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
				.exchange()
				.expectStatus()
				.isNotFound();
	}

	private WebTestClient.BodyContentSpec validateJsonForVenue(final WebTestClient.BodyContentSpec body, final Venue venue, final String indexText) {
		return body.jsonPath("$" + indexText + ".id").isEqualTo(venue.getId())
			.jsonPath("$" + indexText + ".name").isEqualTo(venue.getName())
			.jsonPath("$" + indexText + ".description").isEqualTo(venue.getDescription())
			.jsonPath("$" + indexText + ".email").isEqualTo(venue.getEmail())
			.jsonPath("$" + indexText + ".phone").isEqualTo(venue.getPhone());
	}

	private Venue mockVenue(final String suffix) {
		final Venue venue = new Venue();
		venue.setId("testId" + suffix);
		venue.setName("testName" + suffix);
		venue.setDescription("testDescription" + suffix);
		venue.setEmail("test@gmail.com" + suffix);
		venue.setPhone("672222332" + suffix);
		return venue;
	}
}
