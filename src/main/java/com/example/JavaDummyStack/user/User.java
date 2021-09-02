package com.example.JavaDummyStack.user;

import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "users")
@Data
@EqualsAndHashCode(of = { "id" })
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(length = 50)
	private String name;

	@Column(length = 100)
	private String userName;

	private String email;

	private String password;

	private boolean enabled;

	@ManyToOne(cascade = CascadeType.MERGE)
	private Role role;

	public List<GrantedAuthority> getUserGrantedAuthority() {
		return Collections.singletonList(new SimpleGrantedAuthority(this.getRole().getRole()));
	}

	public UserVO userToUserVO() {
		UserVO userVO = new UserVO();
		userVO.setId(id);
		userVO.setName(name);
		userVO.setEmail(email);
		userVO.setUserName(userName);
		userVO.setEnabled(enabled);
		userVO.setPassword(password);
		return userVO;
	}

}
