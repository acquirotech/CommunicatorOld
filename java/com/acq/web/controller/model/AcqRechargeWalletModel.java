package com.acq.web.controller.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


public class AcqRechargeWalletModel {

	@NotNull
	@Pattern(regexp="^[0-9]{1,20}$",message="Invalid SessionId")
	private String sessionId;
	
	@NotNull
	@Pattern(regexp="^[0-9]{10,16}$",message="Invalid WalletId")
	private String walletId;
	
	
	@NotNull
	@Pattern(regexp="^[0-9]{1,11}$",message="Invalid Transaction Id")
	private String txnId;


	public String getSessionId() {
		return sessionId;
	}


	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}


	public String getWalletId() {
		return walletId;
	}


	public void setWalletId(String walletId) {
		this.walletId = walletId;
	}


	public String getTxnId() {
		return txnId;
	}


	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}
	
	
	
	
	
	
	
}
