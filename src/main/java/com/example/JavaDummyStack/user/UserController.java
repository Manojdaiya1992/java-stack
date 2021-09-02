package com.example.JavaDummyStack.user;

import javax.mail.MessagingException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.JavaDummyStack.api.response.ApiResponse;
import com.example.JavaDummyStack.mail.MailSender;
import com.example.JavaDummyStack.security.JwtTokenUtil;
import com.example.JavaDummyStack.security.UserServiceDetailImpl;

@Validated
@RestController
public class UserController {

	@Autowired
	private UserServiceDetailImpl userServiceDetailImpl;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtility;

	@Autowired
	private IUserService userService;

	@Autowired
	private JavaMailSender javaMailSender;

	@PostMapping("/user")
	public ResponseEntity<ApiResponse> createUser(@Valid @RequestBody UserVO userVO) {
		return new ResponseEntity<ApiResponse>(new ApiResponse(HttpStatus.OK.value(), "User created Successfully",
				this.userService.createUser(userVO), null), HttpStatus.OK);
	}

	@PostMapping("/user/login")
	public ResponseEntity<ApiResponse> userLogin(@RequestBody UserVO userVO) {

		this.authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(userVO.getUserName(), userVO.getPassword(), null));
		UserDetails userDetails = this.userServiceDetailImpl.loadUserByUsername(userVO.getUserName());
		// System.out.println(userDetails.getAuthorities());
		String token = this.jwtTokenUtility.generateToken(userDetails);
		// System.out.println(token);
		return new ResponseEntity<ApiResponse>(
				new ApiResponse(HttpStatus.OK.value(), "Login Successfully", token, null), HttpStatus.OK);
	}

	@GetMapping("/users")
	public ResponseEntity<ApiResponse> getUsers() {
		return new ResponseEntity<ApiResponse>(new ApiResponse(HttpStatus.OK.value(), "User fetched Successfully",
				this.userService.getAllUsers(), null), HttpStatus.OK);
	}

	@PutMapping("/user/{id}/change/password")
	public ResponseEntity<ApiResponse> changePassword(@PathVariable("id") Long userId,
			@RequestBody ChangePasswordVO changePasswordVO) throws Exception {
		this.userService.changePassword(userId, changePasswordVO);
		return new ResponseEntity<ApiResponse>(
				new ApiResponse(HttpStatus.OK.value(), "Password changed Successfully", null, null), HttpStatus.OK);
	}

	@PostMapping("/user/{to}/mail")
	public ResponseEntity<ApiResponse> sendMail(@PathVariable("to") String to) throws MessagingException {
		new MailSender().sendPlainMail(javaMailSender, to, "Testing mail configuration of Java Stack");
		return new ResponseEntity<ApiResponse>(
				new ApiResponse(HttpStatus.OK.value(), "Mail sent Successfully", null, null), HttpStatus.OK);
	}

}
