package com.acq.web.controller.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import security.AcquiroSeq;

public class LoginAcqWalletModel {
	@NotNull
	@Pattern(regexp="^[0-9]{1,20}$",message="Invalid SessionId")
	private String sessionId;
	
	@NotNull
	@Pattern(regexp="^[0-9]{1,20}$",message="Invalid TempId")
	private String tempId;
	
	@NotNull
	@Pattern(regexp="^[0-9]{6,6}$",message="Invalid OTP")
	private String otp;

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		//this.sessionId = sessionId;
		this.sessionId = AcquiroSeq.MakeNonSmart(sessionId);
	}

	public String getTempId() {
		return tempId;
	}

	public void setTempId(String tempId) {
		//this.tempId = tempId;
		this.tempId = AcquiroSeq.MakeNonSmart(tempId);
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		//this.otp = otp;
		this.otp = AcquiroSeq.MakeNonSmart(otp);
	}
	
}
