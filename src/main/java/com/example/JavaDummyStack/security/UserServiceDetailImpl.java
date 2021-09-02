package com.example.JavaDummyStack.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.example.JavaDummyStack.user.IUserDao;
import com.example.JavaDummyStack.user.User;

@Component
public class UserServiceDetailImpl implements UserDetailsService {

	@Autowired
	private IUserDao userDao;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("User name   " + username);
		User user = this.userDao.findByUserName(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));
		return new UserDetailImpl(user);
	}

}
