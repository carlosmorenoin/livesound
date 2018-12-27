package com.livesound.live.user.infrastructure;

import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.livesound.live.user.core.User;
import com.livesound.live.user.core.UserService;

import reactor.core.publisher.Mono;

@RestController
public class UserAPI {

    private UserService userService;

    private PasswordEncoder passwordEncoder;

    public UserAPI(final UserService userService, final PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping(value = "/users", produces = "application/json")
    public Mono<ResponseEntity<String>> addUser(@RequestBody @Valid final User user, final UriComponentsBuilder uriBuilder) {
        return Mono.just(user)
                .filter(this::isUserNameAvailable)
                .filter(this::isPasswordValid)
                .doOnNext(this::encodePassoword)
                .flatMap(userService::addUser)
                .map(newUser ->  uriBuilder.path("/users/{userName}").buildAndExpand(newUser.getUserName()).toUri())
                .map(location -> ResponseEntity.created(location).body("User created"))
                .defaultIfEmpty(ResponseEntity.badRequest().body("Cannot create an user with the specified username and password"));

    }

    @GetMapping(value = "/users/id", produces = "application/json")
    public Mono<List<String>> findUserNameByTextOrEmail(@RequestParam final String text) {
        return userService.findByNameOrEmail(text).filter(Objects::nonNull).map(User::getUserName).collectList();
    }

    @GetMapping(value = "/users/{userName}", produces = "application/json")
    public Mono<ResponseEntity<User>> findByUserName(@PathVariable("userName") final String userName) {
        return userService.findByUserName(userName)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    private boolean isPasswordValid(final User user) {
        return true;
    }

    private boolean isUserNameAvailable(final User user) {
        return !userService.findByUserName(user.getUserName()).hasElement().block();
    }

    private void encodePassoword(final User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
    }
}
