package com.acq.web.controller.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;



public class AcqApiLogin {
	
	
	@NotNull
	@Pattern(regexp="^[a-zA-Z0-9]{10,30}$",message="Invalid Credentials")
	private String username;
	
	@NotNull
	@Pattern(regexp="^[a-zA-Z0-9\\!\\#\\@\\$\\_]{8,50}$",message="Invalid Credentials")
	private String password;
	
	//@Pattern(regexp="^[a-zA-Z]{3,30}$",message="invalid protocol")
	
	@Pattern(regexp="^[a-zA-Z]{3,30}$",message="Invalid Credentials")
	private String protocol;
	
	@NotNull
	@Size(min=10,max=300, message="Invalid Credentials")
	private String gcmNotification;
	
	@Size(min=0,max=20, message="Invalid Credentials")
	private String appVersion;
	
	private String acquirer;

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

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getGcmNotification() {
		return gcmNotification;
	}

	public void setGcmNotification(String gcmNotification) {
		this.gcmNotification = gcmNotification;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public String getAcquirer() {
		return acquirer;
	}

	public void setAcquirer(String acquirer) {
		this.acquirer = acquirer;
	}
	
	
	
	
}
