package com.livesound.live.venue.infrastructure;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.livesound.live.venue.core.HostService;
import com.livesound.live.venue.core.Venue;
import com.livesound.live.venue.core.VenueService;

import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
@WebFluxTest(VenueAPI.class)
class VenueAPITest {

	@Autowired
	private WebTestClient webTestClient;

	@MockBean
	private VenueService venueService;

	@MockBean
	private HostService hostService;

	@MockBean
	private UriComponentsBuilder uriBuilder;

	@BeforeEach void setUp() {
	}

	@Test void badRequestIfVenueWithouttName() {
		final Venue venue = mockVenue();
		venue.setName(null);
		postRequestExecution(venue).expectStatus().isBadRequest();
	}

	@Test void badRequestIfVenueWithoutDescription()  {
		final Venue venue = mockVenue();
		venue.setDescription(null);
		postRequestExecution(venue).expectStatus().isBadRequest();
	}

	@Test void badRequestIfVenueWithoutEmail() {
		final Venue venue = mockVenue();
		venue.setEmail(null);
		postRequestExecution(venue).expectStatus().isBadRequest();
	}

	@Test void badRequestIfVenueWithWrongPhone() {
		final Venue venue = mockVenue();
		venue.setPhone("xxxxx");
		postRequestExecution(venue).expectStatus().isBadRequest();
	}

	@Test void badRequestIfNotValidHostId() {
		final Venue venue = mockVenue();
		given(hostService.isValidHostId(eq("1"), anyString())).willReturn(false);
		postRequestExecution(venue).expectStatus().isBadRequest();
	}

	@Test void addVenue() throws URISyntaxException {
		final Venue venue = mockVenue();
		venue.setHost("1");
		UriComponents uriComponents = Mockito.mock(UriComponents.class);
		URI uri = new URI("/host/1/venues/" + venue.getId());
		when(uriComponents.toUri()).thenReturn(uri);

		given(hostService.isValidHostId(eq("1"), anyString())).willReturn(true);
		given(venueService.addVenue(venue)).willReturn(Mono.just(venue));
		given(uriBuilder.path(any())).willReturn(uriBuilder);
		given(uriBuilder.buildAndExpand(any(Object.class))).willReturn(uriComponents);

		postRequestExecution(venue)
				.expectStatus().isCreated()
				.expectHeader().valueMatches("location", "/host/1/venues/.+$");
	}

	@Test void updateBadRequestIfVenueWithouttName() {
		final Venue venue = mockVenue();
		venue.setName(null);
		putRequestExecution(venue).expectStatus().isBadRequest();
	}

	@Test void updateBbadRequestIfVenueWithoutDescription()  {
		final Venue venue = mockVenue();
		venue.setDescription(null);
		putRequestExecution(venue).expectStatus().isBadRequest();
	}

	@Test void updateBadRequestIfVenueWithoutEmail() {
		final Venue venue = mockVenue();
		venue.setEmail(null);
		putRequestExecution(venue).expectStatus().isBadRequest();
	}

	@Test void updateBadRequestIfVenueWithWrongPhone() {
		final Venue venue = mockVenue();
		venue.setPhone("xxxxx");
		putRequestExecution(venue).expectStatus().isBadRequest();
	}

	@Test void updateBadRequestIfNotValidHostId() {
		final Venue venue = mockVenue();
		given(hostService.isValidHostId(eq("1"), anyString())).willReturn(false);
		putRequestExecution(venue).expectStatus().isBadRequest();
	}

	@Test void updateVenue() {
		final Venue venue = mockVenue();
		venue.setId("1");
		venue.setHost("1");
		given(venueService.updateVenue(venue)).willReturn(Mono.just(venue));
		putRequestExecution(venue).expectStatus().isOk();
	}

	@Test void updateVenueNotFound() {
		final Venue venue = mockVenue();
		venue.setId("1");
		venue.setHost("1");
		given(venueService.updateVenue(venue)).willReturn(Mono.empty());
		putRequestExecution(venue).expectStatus().isNotFound();
	}

	@Test void deleteNotFoundVenue() {
		given(venueService.deleteVenueById("1")).willReturn(Mono.empty());
		webTestClient.delete().uri("/host/1/venues/1")
				.header("Authorization", "token").exchange()
				.expectStatus().isNotFound();
	}


	@Test void deleteVenue() {
		final String id = "1";
		given(venueService.deleteVenueById(id)).willReturn(Mono.just(id));
		webTestClient.delete().uri("/host/1/venues/1")
				.header("Authorization", "token").exchange()
				.expectStatus().isOk();
	}

	private WebTestClient.ResponseSpec postRequestExecution(Venue venue) {
		return webTestClient.post().uri("/host/1/venues")
				.header("Authorization", "token").contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromObject(venue)).exchange();
	}

	private WebTestClient.ResponseSpec putRequestExecution(Venue venue) {
		return webTestClient.put().uri("/host/1/venues/1")
				.header("Authorization", "token").contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromObject(venue)).exchange();
	}

	private Venue mockVenue() {
		final Venue venue = new Venue();
		venue.setId("id");
		venue.setName("venue name");
		venue.setDescription("venue description");
		venue.setEmail("venue@gmail.com");
		venue.setPhone("672222332");
		return venue;
	}
}
