package com.livesound.live.api.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;

@SpringBootApplication
@EnableOAuth2Sso
@EnableZuulProxy
@EnableDiscoveryClient
public class GatewayApplication extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(final HttpSecurity http) throws Exception {
        http.logout().and().antMatcher("/**").authorizeRequests()
                .antMatchers("/login", "/auth/**").permitAll()
                .antMatchers(HttpMethod.POST, "/users").permitAll()
                .anyRequest().authenticated().and().csrf().disable();
    }

    public static void main(final String[] args) {
        System.setProperty("spring.config.name", "live-gateway");
        SpringApplication.run(GatewayApplication.class, args);
    }

    @Bean
    public OAuth2RestTemplate oauth2RestTemplate(final OAuth2ProtectedResourceDetails resource, final OAuth2ClientContext context) {
        return new OAuth2RestTemplate(resource, context);
    }

}
