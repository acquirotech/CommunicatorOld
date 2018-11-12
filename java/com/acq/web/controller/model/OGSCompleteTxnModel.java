package com.acq.web.controller.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class OGSCompleteTxnModel {

	@NotNull
	@Pattern(regexp="^[0-9]{1,20}$",message="invalid SessionId")
	private String sessionId;
	
	@NotNull
	@Pattern(regexp="^[0-9]{1,20}$",message="Invalid TxnId")
	private String txnId;
	
	@NotNull
	@Size(min=0,max=50, message="Invalid Application Certificate")
	private String applicationCertificate;
	
	@NotNull
	@Size(min=0,max=50, message="Invalid AID")
	private String aid;
	
	@NotNull
	@Size(min=0,max=50, message="Invalid CardType")
	private String cardType; 
	
	private String mobileNo;
	private String amount;
	private String emailId;
	private String status;
	private String description;
	private String maskPan;
	private String cardholderName;
	private String authCode;
	private String batchNo;
	private String rrNo;
	private String gmtDateTime;
	private String localTime;
	private String additionalAmount;
	
	@Size(min=0,max=1000, message="Invalid ScriptResult")
	private String de55;
	
	
	
	
	public String getDe55() {
		return de55;
	}
	public void setDe55(String de55) {
		this.de55 = de55;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public String getAid() {
		return aid;
	}
	public void setAid(String aid) {
		this.aid = aid;
	}
	public String getApplicationCertificate() {
		return applicationCertificate;
	}
	public void setApplicationCertificate(String applicationCertificate) {
		this.applicationCertificate = applicationCertificate;
	}
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
