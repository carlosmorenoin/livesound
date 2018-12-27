package com.livesound.live.user.infrastructure;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.livesound.live.user.core.User;
import com.livesound.live.user.core.UserRole;
import com.livesound.live.user.core.UserService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
@WebFluxTest(UserAPI.class)
class UserAPITest {

	public static final String PWD = "123";
	public static final String USERNAME = "username";

	@Autowired
	private WebTestClient webTestClient;

	@MockBean
	private UserService userService;

	@MockBean
	private PasswordEncoder passwordEncoder;

	@MockBean
	private UriComponentsBuilder uriBuilder;

	@Test void badRequestIfUserWithoutFirstName() throws URISyntaxException {
		final User user = mockUser();
		user.setFirstName(null);

		postRequestExecution(user).expectStatus().isBadRequest();
	}

	@Test void badRequestIfUserWithoutLastName() throws URISyntaxException {
		final User user = mockUser();
		user.setLastName(null);

		postRequestExecution(user).expectStatus().isBadRequest();
	}

	@Test void badRequestIfUserWithoutUsertName() throws URISyntaxException {
		final User user = mockUser();
		user.setUserName(null);

		postRequestExecution(user).expectStatus().isBadRequest();
	}

	@Test void badRequestIfUserWithoutEmail() throws URISyntaxException {
		final User user = mockUser();
		user.setEmail(null);

		postRequestExecution(user).expectStatus().isBadRequest();
	}

	@Test void badRequestIfUserWithoutPassword() throws URISyntaxException {
		final User user = mockUser();
		user.setPassword(null);

		postRequestExecution(user).expectStatus().isBadRequest();
	}

	@Test void badRequestIfUserWithoutRole() throws URISyntaxException {
		final User user = mockUser();
		user.setRole(null);

		postRequestExecution(user).expectStatus().isBadRequest();
	}

	@Test void addUser() throws URISyntaxException {
		final User user = mockUser();

		Mono<User> userMono = Mono.just(user);
		given(userService.findByUserName(user.getUserName())).willReturn(Mono.empty());
		given(userService.addUser(user)).willReturn(userMono);
		given(passwordEncoder.encode(PWD)).willReturn(PWD);
		given(uriBuilder.path(any())).willReturn(uriBuilder);

		UriComponents uriComponents = Mockito.mock(UriComponents.class);
		URI uri = new URI("/users/" + USERNAME);
		when(uriComponents.toUri()).thenReturn(uri);

		given(uriBuilder.buildAndExpand(any(Object.class))).willReturn(uriComponents);

		postRequestExecution(user)
				.expectStatus().isCreated()
				.expectHeader().valueMatches("location", "/users/.+$");
	}

	@Test void findUserNameByTextOrEmail() {
		final String email = "test@test.com";
		final User user = new User();
		user.setUserName("username");
		user.setEmail(email);

		Flux<User> users = Flux.just(user);
		given(userService.findByNameOrEmail(email)).willReturn(users);

		webTestClient.get().uri("/users/id?text=" + email)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.[0]").isEqualTo("username");
	}

	@Test void findByUserName() {
		final String username = "username";
		final User user = new User();
		user.setUserName(username);
		user.setFirstName("first name");
		user.setLastName("last name");
		user.setRole(UserRole.USER);
		user.setEmail("test@gmail.com");

		given(userService.findByUserName(username)).willReturn(Mono.just(user));

		webTestClient.get().uri("/users/" + username)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.userName").isEqualTo(username)
				.jsonPath("$.firstName").isEqualTo("first name")
				.jsonPath("$.lastName").isEqualTo("last name")
				.jsonPath("$.role").isEqualTo(UserRole.USER.toString())
				.jsonPath("$.email").isEqualTo("test@gmail.com");
	}

	@Test void findByUserNameIsEmpty() {
		final String username = "username";

		given(userService.findByUserName(username)).willReturn(Mono.empty());

		webTestClient.get().uri("/users/" + username)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
				.exchange()
				.expectStatus().isNotFound();
	}

	private WebTestClient.ResponseSpec postRequestExecution(User user) {
		return webTestClient.post().uri("/users").contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromObject(user)).exchange();
	}

	private User mockUser() {
		User user = new User();
		user.setUserName(USERNAME);
		user.setEmail("test@test.com");
		user.setPassword(PWD);
		user.setFirstName("first name");
		user.setLastName("last name");
		user.setRole(UserRole.USER);
		return user;
	}
}

