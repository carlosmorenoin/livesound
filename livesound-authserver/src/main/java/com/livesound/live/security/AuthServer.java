package com.livesound.live.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableDiscoveryClient
public class AuthServer implements WebMvcConfigurer {

    private static final String LOGIN_PATH = "/login";

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController(LOGIN_PATH).setViewName("login");
    }

    @Configuration
    @Order(-20)
    static class WebSecurity extends WebSecurityConfigurerAdapter {

        @Override
        @Bean
        public UserDetailsService userDetailsService() {
            return new CustomerUserDetailsService();
        }

        @Override
        @Bean
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.formLogin()
                    .loginPage(LOGIN_PATH).permitAll()
                    .and()
                    .requestMatchers()
                    //specify urls handled
                    .antMatchers(LOGIN_PATH, "/oauth/authorize", "/oauth/confirm_access", "/oauth/token/revokeById/***")
                    .antMatchers("/fonts/**", "/js/**", "/css/**")
                    .and()
                    .authorizeRequests()
                    .antMatchers("/fonts/**", "/js/**", "/css/**").permitAll()
                    .anyRequest().authenticated();
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }

    @Configuration
    @EnableAuthorizationServer
    static class Oauth2Configuation extends AuthorizationServerConfigurerAdapter {

        @Value("${live.security.oauth2.keystore.path}")
        private String keyStorePath;

        @Value("${live.security.oauth2.keystore.keypair}")
        private String keyPair;

        @Value("${live.security.oauth2.client.id}")
        private String clientId;

        @Value("${live.security.oauth2.client.secret}")
        private String secret;

        @Autowired
        @Qualifier("authenticationManagerBean")
        AuthenticationManager authenticationManager;

        @Autowired
        @Qualifier("userDetailsService")
        UserDetailsService userDetailsService;

        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            clients.inMemory()
                    .withClient(clientId)
                    .secret(secret)
                    .scopes("auth")
                    .autoApprove(true)
                    .accessTokenValiditySeconds(600)
                    .refreshTokenValiditySeconds(1200)
                    .authorizedGrantTypes("implicit", "refresh_token", "password", "authorization_code");
        }

        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
            endpoints.tokenStore(tokenStore())
                    .tokenEnhancer(jwtTokenEnhancer())
                    .authenticationManager(authenticationManager)
                    .userDetailsService(userDetailsService)
                    .reuseRefreshTokens(false);
        }

        @Bean
        TokenStore tokenStore() {
            return new JwtTokenStore(jwtTokenEnhancer());
        }

        @Bean
        protected JwtAccessTokenConverter jwtTokenEnhancer() {
            KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource(keyStorePath), "secret".toCharArray());
            JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
            converter.setKeyPair(keyStoreKeyFactory.getKeyPair(keyPair));
            return converter;
        }
    }

    public static void main(String[] args) {
        System.setProperty("spring.config.name", "live-auth");
        SpringApplication.run(AuthServer.class, args);
    }
}
