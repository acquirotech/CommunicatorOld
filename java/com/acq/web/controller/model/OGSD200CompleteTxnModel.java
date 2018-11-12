package com.acq.web.controller.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class OGSD200CompleteTxnModel {

	@NotNull
	@Pattern(regexp="^[0-9]{1,20}$",message="invalid SessionId")
	private String sessionId;
	
	@NotNull
	@Pattern(regexp="^[0-9]{1,20}$",message="Invalid TxnId")
	private String txnId;
	
	@NotNull
	@Pattern(regexp="^[0-9]{10,12}$",message="invalid Mobile No")
	private String mobileNo;
	
	private String amount;
	
	@Pattern(regexp="^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9_\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$|{0}$",message="invalid email")
	private String emailId;
	
	@NotNull
	@Pattern(regexp="^[0-9]{1,3}$",message="Invalid Status")
	private String status;
	
	@NotNull
	@Size(min=2,max=50,message="Invalid Description")
	private String description;
	
	private String maskPan;
	
	@NotNull
	@Size(min=2,max=50,message="Invalid Card HolderName")
	private String cardholderName;
	
	@NotNull
	@Size(min=2,max=50,message="Invalid AuthCode")
	private String authCode;
	
	private String batchNo;
	
	@NotNull
	@Size(min=2,max=30,message="Invalid RRNo")
	private String rrNo;
	
	@NotNull
	@Size(min=2,max=30,message="Invalid GmtDateTime")
	private String gmtDateTime;
	
	@NotNull
	@Size(min=2,max=30,message="Invalid LocalTime")
	private String localTime;
	
	@NotNull
	@Pattern(regexp="^([0-9]\\d{0,13})?(.[0-9]{0,3}$)?$",message="Invalid AdditionalAmount")
	private String additionalAmount;
	
	
	public String getAdditionalAmount() {
		return additionalAmount;
	}
	public void setAdditionalAmount(String additionalAmount) {
		this.additionalAmount = additionalAmount;
	}
	public String getLocalTime() {
		return localTime;
	}
	public void setLocalTime(String localTime) {
		this.localTime = localTime;
	}
	public String getGmtDateTime() {
		return gmtDateTime;
	}
	public void setGmtDateTime(String gmtDateTime) {
		this.gmtDateTime = gmtDateTime;
	}
	public String getRrNo() {
		return rrNo;
	}
	public void setRrNo(String rrNo) {
		this.rrNo = rrNo;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getMaskPan() {
		return maskPan;
	}
	public void setMaskPan(String maskPan) {
		this.maskPan = maskPan;
	}
	public String getCardholderName() {
		return cardholderName;
	}
	public void setCardholderName(String cardholderName) {
		this.cardholderName = cardholderName;
	}
	public String getAuthCode() {
		return authCode;
	}
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	public String getTxnId() {
		return txnId;
	}
	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}	
}
