package com.livesound.live.profiles.infrastructure;

import static com.livesound.live.profiles.core.Host.Type.COMPANY;
import static org.mockito.BDDMockito.given;

import com.livesound.live.profiles.core.Artist;
import com.livesound.live.profiles.core.ContactInfo;
import com.livesound.live.profiles.core.Person;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.livesound.live.profiles.core.Host;
import com.livesound.live.profiles.core.ProfileService;

import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
@WebFluxTest(ProfileAPI.class)
class ProfileAPITest {

	private static final String TEST_FIRST_NAME = "Test first name";
	private static final String TEST_LAST_NAME = "Test last name";
	@Autowired
	private WebTestClient webTestClient;

	@MockBean
	private ProfileService profileService;

	@Test void getHostProfileCompany() {
		final String profileId = "1";
		final Host host = mockCompanyHost(profileId);

		Mono<Host> hostMono = Mono.just(host);
		given(profileService.getHostProfile(profileId)).willReturn(hostMono);

		webTestClient.get().uri("/host/1/profile")
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.id").isEqualTo("1")
				.jsonPath("$.type").isEqualTo(COMPANY.toString())
				.jsonPath("$.company.id").isEqualTo("Company id")
				.jsonPath("$.company.name").isEqualTo("Test name")
				.jsonPath("$.company.address").isEqualTo("Test address")
				.jsonPath("$.contactInfo.email").isEqualTo("test email")
				.jsonPath("$.contactInfo.mobile").isEqualTo("test mobile");
	}

	@Test void getHostProfilePersonal() {
		final String profileId = "1";
		final Host host = mockPersonalHost(profileId);

		Mono<Host> hostMono = Mono.just(host);
		given(profileService.getHostProfile(profileId)).willReturn(hostMono);

		webTestClient.get().uri("/host/1/profile")
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.id").isEqualTo("1")
				.jsonPath("$.type").isEqualTo(COMPANY.toString())
				.jsonPath("$.user.firstName").isEqualTo(TEST_FIRST_NAME)
				.jsonPath("$.user.lastName").isEqualTo(TEST_LAST_NAME)
				.jsonPath("$.contactInfo.email").isEqualTo("test email")
				.jsonPath("$.contactInfo.mobile").isEqualTo("test mobile");
	}

	@Test void getArtistProfile() {
		final String profileId = "1";
		final Artist artist = mockArtist(profileId);

		Mono<Artist> artistMono = Mono.just(artist);
		given(profileService.getArtistProfile(profileId)).willReturn(artistMono);

		webTestClient.get().uri("/artist/1/profile")
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.id").isEqualTo("1")
				.jsonPath("$.name").isEqualTo("Test name")
				.jsonPath("$.type").isEqualTo(Artist.Type.INDIVIDUAL.toString())
				.jsonPath("$.user.firstName").isEqualTo(TEST_FIRST_NAME)
				.jsonPath("$.user.lastName").isEqualTo(TEST_LAST_NAME)
				.jsonPath("$.contactInfo.email").isEqualTo("test email")
				.jsonPath("$.contactInfo.mobile").isEqualTo("test mobile");
	}

	private Host mockCompanyHost(String profileId) {
		final Host.Company company = new Host.Company();
		company.setId("Company id");
		company.setAddress("Test address");
		company.setName("Test name");

		final Host host = new Host(profileId);
		host.setType(COMPANY);
		host.setCompany(company);
		host.setContactInfo(mockContactInfo());
		return host;
	}
	private Host mockPersonalHost(String profileId) {
		final Host host = new Host(profileId);
		host.setType(COMPANY);
		host.setUser(mockPerson());
		host.setContactInfo(mockContactInfo());
		return host;
	}

	private Artist mockArtist(String profileId) {
		final Artist artist = new Artist(profileId);
		artist.setName("Test name");
		artist.setType(Artist.Type.INDIVIDUAL);
		artist.setUser(mockPerson());
		artist.setContactInfo(mockContactInfo());

		return artist;
	}

	private ContactInfo mockContactInfo() {
		final ContactInfo contactInfo = new ContactInfo();
		contactInfo.setEmail("test email");
		contactInfo.setMobile("test mobile");
		return contactInfo;
	}

	private Person mockPerson() {
		final Person person = new Person();
		person.setFirstName(TEST_FIRST_NAME);
		person.setLastName(TEST_LAST_NAME);
		return person;
	}
}
