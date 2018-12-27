package com.livesound.live.profiles.infrastructure;

import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import com.livesound.live.profiles.core.ProfileService;
import com.livesound.live.profiles.core.ReactiveProfileService;
import com.livesound.live.profiles.core.UserEventListener;

@Configuration
@EnableBinding(UserStream.class)
public class AppConfig {

	@Autowired
	private ArtistProfileRepository artistProfileRepository;
	@Autowired
	private HostProfileRepository hostProfileRepository;
	@Autowired
	private UserProfileRepository userProfileRepository;

	@Bean
	public UserEventListener userEventListener() {
		return new UserEventListener(hostProfileRepository, artistProfileRepository, userProfileRepository);
	}

	@Bean
	public ProfileService profileService() {
		return new ReactiveProfileService(hostProfileRepository, artistProfileRepository, userProfileRepository);
	}

	@Configuration
	@EnableResourceServer
	public static class ResourceServiceConfiguration extends ResourceServerConfigurerAdapter {

		@Override
		public void configure(HttpSecurity http) throws Exception {
			http.csrf().disable()
					.authorizeRequests()
					.antMatchers("/**").authenticated();
		}

		@Override
		public void configure(ResourceServerSecurityConfigurer config) {
			config.tokenServices(tokenServices());
		}

		@Bean
		public TokenStore tokenStore() {
			return new JwtTokenStore(accessTokenConverter());
		}

		@Bean
		public JwtAccessTokenConverter accessTokenConverter() {
			JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
			Resource resource = new ClassPathResource("public.txt");
			String publicKey;
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
			DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
			defaultTokenServices.setTokenStore(tokenStore());
			return defaultTokenServices;
		}
	}
}
