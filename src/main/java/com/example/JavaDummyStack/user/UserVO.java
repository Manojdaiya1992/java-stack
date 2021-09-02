package com.example.JavaDummyStack.user;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema
@Data
public class UserVO {

	private long id;

	@Schema(defaultValue = "Manoj Dahiya", description = "enter name of user")
	@NotBlank(message = "Name is required")
	private String name;

	@Schema(defaultValue = "Manojdahiya", description = "enter username of user")
	@NotBlank(message = "User Name is required")
	@JsonProperty("user_name")
	private String userName;

	@Schema(defaultValue = "md@gmail.com", description = "enter email of user")
	private String email;

	@Schema(defaultValue = "manojd", description = "enter password of user", minLength = 6)
	@NotBlank(message = "Password is required")
	@Size(min = 6, message = "Password must have six digits")
	private String password;

	private boolean enabled;

	@Schema(defaultValue = "{id:1}", description = "enter role of user")
	@JsonProperty("user_role")
	private RoleVO role;

	public User userVOToUser() {
		User user = new User();
		user.setId(id);
		user.setName(name);
		user.setEmail(email);
		user.setEnabled(enabled);
		user.setUserName(userName);
		user.setPassword(password);
		return user;
	}

}
