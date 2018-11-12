package com.acq.web.controller.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class OGSMainLoginModel{
	
	@NotNull
	@Pattern(regexp="^[a-zA-Z0-9]{10,30}$",message="Invalid Credentials")
	private String username;
	
	@NotNull
	@Pattern(regexp="^[a-zA-Z0-9\\!\\#\\@\\$\\_]{8,50}$",message="Invalid Credentials")
	private String password;		
	
	@NotNull
	@Pattern(regexp="^[0-9a-zA-Z]{3,30}$",message="Invalid Credentials")
	private String deviceId;


	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	
	
	
	
}
