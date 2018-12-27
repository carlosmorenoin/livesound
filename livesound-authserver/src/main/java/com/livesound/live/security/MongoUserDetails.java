package com.livesound.live.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;


public class MongoUserDetails implements UserDetails {

	private String userName;
	private String password;
	private List<GrantedAuthority> grantedAuthorities;

	public MongoUserDetails(String userName, String password, String[] authorities) {
		this.userName = userName;
		this.password = password;
		this.grantedAuthorities = AuthorityUtils.createAuthorityList(authorities);
	}

	@Override public Collection<? extends GrantedAuthority> getAuthorities() {
		return grantedAuthorities;
	}

	@Override public String getPassword() {
		return password;
	}

	@Override public String getUsername() {
		return userName;
	}

	@Override public boolean isAccountNonExpired() {
		return true;
	}

	@Override public boolean isAccountNonLocked() {
		return true;
	}

	@Override public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override public boolean isEnabled() {
		return true;
	}
}
