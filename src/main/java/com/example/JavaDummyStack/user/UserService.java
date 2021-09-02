package com.example.JavaDummyStack.user;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {

	@Autowired
	private IUserDao userDao;

	private final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

	@Override
	public UserVO createUser(UserVO userVO) {
		User user = userVO.userVOToUser();
		user.setPassword(PASSWORD_ENCODER.encode(user.getPassword()));
		Role role = userVO.getRole().roleVOToRole();
		user.setRole(role);
		return this.userDao.save(user).userToUserVO();
	}

	@Override
	public List<UserVO> getAllUsers() {
		return this.userDao.findAll().stream().map(user -> user.userToUserVO()).collect(Collectors.toList());
	}

	@Override
	public void changePassword(Long userId, ChangePasswordVO changePasswordVO) throws Exception {
		User user = this.findUserById(userId);
		if (this.PASSWORD_ENCODER.matches(changePasswordVO.getOldPassword(), user.getPassword())) {
			user.setPassword(this.PASSWORD_ENCODER.encode(changePasswordVO.getNewPassword()));
			this.userDao.save(user);
		} else
			throw new Exception("Old password is not correct");
	}

	@Override
	public User findUserById(Long userId) {
		return this.userDao.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
	}

}
