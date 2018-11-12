package com.acq.web.controller.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import security.AcquiroSeq;

public class AcqSendSmsModel {

	@NotNull
	@Pattern(regexp="^[0-9]{1,20}$",message="Invalid SessionId")
	private String sessionId;
	
	@NotNull
	@Pattern(regexp="^[0-9]{1,20}$",message="Invalid TxnId")
	private String txnId;
	
	@NotNull
	@Pattern(regexp="^[0-9]{10,10}$",message="Invalid Phone")
	private String phone;
	
	
	
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		//this.sessionId = sessionId;
		this.sessionId = AcquiroSeq.MakeNonSmart(sessionId);
	}
	public String getTxnId() {
		return txnId;
	}
	public void setTxnId(String txnId) {
		//this.txnId = txnId;
		this.txnId = AcquiroSeq.MakeNonSmart(txnId);
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		//this.phone = phone;
		this.phone = AcquiroSeq.MakeNonSmart(phone);
	}
	
	
}
