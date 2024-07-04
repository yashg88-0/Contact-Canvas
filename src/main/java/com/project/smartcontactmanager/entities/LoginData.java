package com.project.smartcontactmanager.entities;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LoginData {
	
	@NotBlank(message = "Username can not be blank!!")
	@Size(min = 3, max = 10, message = "Username must be between 3 - 10 characters")
	private String userName;
	
	@Email(regexp = "/^\\S+@\\S+\\.\\S+$/", message = "Kindly provide valid email id!!")
	private String email;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@Override
	public String toString() {
		return "LoginData [userName=" + userName + ", email=" + email + "]";
	}
}
