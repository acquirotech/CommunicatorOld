package com.acq.web.controller.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

	public class AcqOgsLoginModel {
	
	@NotNull
	@Pattern(regexp="^[a-zA-Z0-9]{10,30}$",message="Invalid Credentials")
	private String username;
	
	@NotNull
	@Pattern(regexp="^[a-zA-Z0-9\\!\\#\\@\\$\\_]{8,50}$",message="Invalid Credentials")
	private String password;
		
	@Pattern(regexp="^[a-zA-Z]{3,30}$",message="Invalid Credentials")
	private String protocol;
	
	@NotNull
	@Pattern(regexp="^[a-zA-Z0-9]{5,30}$",message="Invalid Credentials")
	private String deviceId;
	
	@NotNull
	@Size(min=10,max=300, message="Invalid Credentials")
	private String gcmNotification;
	
	@Size(min=0,max=20, message="Invalid Credentials")
	private String appVersion;
	
	private String acquirer;
	
	
	
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getAcquirer() {
		return acquirer;
	}
	public void setAcquirer(String acquirer){
		this.acquirer = acquirer;
	}
	public String getAppVersion() {
		return appVersion;
	}
	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}
	public String getGcmNotification() {
		return gcmNotification;
	}
	public void setGcmNotification(String gcmNotification) {
		this.gcmNotification = gcmNotification;
	}
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
}
