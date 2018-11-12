package com.acq.web.controller.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


public class AcqLoyaltyDetailsModel {
	
	@NotNull
	@Pattern(regexp="^[0-9]{1,20}$",message="Invalid Session Id")	
	private String sessionId;
	
	@NotNull
	@Pattern(regexp="^[0-9]{1,20}$",message="Invalid walletId")
	private String walletId;
	
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
		//this.sessionId = AcquiroSeq.MakeNonSmart(sessionId);
	}
	public String getWalletId() {
		return walletId;
	}
	public void setWalletId(String walletId) {
		this.walletId = walletId;
		//this.walletId = AcquiroSeq.MakeNonSmart(walletId);
	}

	
}	
