package com.acq.web.controller.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class OGSTxnModel {

	private String messageType;
	private String isoMessage;
	private String uat;
	private String authCode;
	
	
	
	@NotNull
	@Pattern(regexp="^[0-9]{1,20}$",message="invalid session id")
	private String sessionId;
	
	private String maskedPan;
	
	private String tid;
	private String rrn;
	private String amount;
	private String stan;
	private String de7;
	
	
	
	public String getAuthCode() {
		return authCode;
	}
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}
	public String getUat() {
		return uat;
	}
	public void setUat(String uat) {
		this.uat = uat;
	}
	
	public String getDe7() {
		return de7;
	}
	public void setDe7(String de7) {
		this.de7 = de7;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getMaskedPan() {
		return maskedPan;
	}
	public void setMaskedPan(String maskedPan) {
		this.maskedPan = maskedPan;
	}
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	public String getRrn() {
		return rrn;
	}
	public void setRrn(String rrn) {
		this.rrn = rrn;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getStan() {
		return stan;
	}
	public void setStan(String stan) {
		this.stan = stan;
	}
	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	public String getIsoMessage() {
		return isoMessage;
	}
	public void setIsoMessage(String isoMessage) {
		this.isoMessage = isoMessage;
	}
	
}
