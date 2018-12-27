package com.livesound.live;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.livesound.live.user.core.ReactiveUserService;
import com.livesound.live.user.core.UserService;
import com.livesound.live.user.infrastructure.MongoUserRepository;
import com.livesound.live.user.infrastructure.StreamsUserEventServie;
import com.livesound.live.user.infrastructure.UsersStream;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.io.IOException;
import java.nio.charset.Charset;

@Configuration
@EnableBinding(UsersStream.class)
public class AppConfig {

	@Autowired
	private MongoUserRepository userRepository;

	@Autowired
	private StreamsUserEventServie userEventService;

	@Bean
	public UserService usersService() {
		return new ReactiveUserService(userRepository, userEventService);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Configuration
	@EnableResourceServer
	public static class ResourceServiceConfiguration extends ResourceServerConfigurerAdapter {

		@Override
		public void configure(final HttpSecurity http) throws Exception {
			http.csrf().disable()
					.authorizeRequests()
					.antMatchers(HttpMethod.POST, "/users").permitAll()
					.antMatchers("/**").hasAuthority("HOST");
		}

		@Override
		public void configure(final ResourceServerSecurityConfigurer config) {
			config.tokenServices(tokenServices());
		}

		@Bean
		public TokenStore tokenStore() {
			return new JwtTokenStore(accessTokenConverter());
		}

		@Bean
		public JwtAccessTokenConverter accessTokenConverter() {
			final JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
			final Resource resource = new ClassPathResource("public.txt");
			final String publicKey;
			try {
				publicKey = IOUtils.toString(resource.getInputStream(), Charset.defaultCharset());
			} catch (final IOException e) {
				throw new RuntimeException(e);
			}
			converter.setVerifierKey(publicKey);
			return converter;
		}

		@Bean
		@Primary
		public DefaultTokenServices tokenServices() {
			final DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
			defaultTokenServices.setTokenStore(tokenStore());
			return defaultTokenServices;
		}
	}
}
