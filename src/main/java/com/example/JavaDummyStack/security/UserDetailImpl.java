package com.example.JavaDummyStack.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.JavaDummyStack.user.User;

public class UserDetailImpl implements UserDetails {

	private User user = null;

	// private String name;

	public UserDetailImpl(User user) {
		this.user = user;
	}

	private static final long serialVersionUID = 2260334639827445449L;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.user.getUserGrantedAuthority();
	}

	@Override
	public String getPassword() {
		return this.user.getPassword();
	}

	@Override
	public String getUsername() {
		return this.user.getUserName();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return this.user.isEnabled();
	}

}
