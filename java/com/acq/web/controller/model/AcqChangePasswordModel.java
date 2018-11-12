package com.acq.web.controller.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


public class AcqChangePasswordModel {
	@NotNull
	@Pattern(regexp="^[0-9]{1,30}$",message="invalid session id")	
	private String sessionId;
	
	@NotNull
	@Size(min=8,max=20, message="please enter 8 to 20 digit password")
	private String newPassword;
	
	@NotNull
	@Size(min=8,max=20, message="invalid current password")
	private String currentPassword;

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getCurrentPassword() {
		return currentPassword;
	}

	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}
	
	
}
