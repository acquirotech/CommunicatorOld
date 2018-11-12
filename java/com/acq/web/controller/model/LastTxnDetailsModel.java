package com.acq.web.controller.model;

public class LastTxnDetailsModel {

	private String sessionId;
	private String txnMessage;

	
	
	public String getTxnMessage() {
		return txnMessage;
	}

	public void setTxnMessage(String txnMessage) {
		this.txnMessage = txnMessage;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
}
