package com.acq.web.controller.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class AcqOgsVoidTxnModel {

	@NotNull
	@Pattern(regexp="^[0-9]{1,20}$",message="Invalid TxnId")
	private String txnId;
	
	@NotNull
	@Pattern(regexp="^[0-9]{1,20}$",message="invalid SessionId")
	private String sessionId;
	
	@NotNull
	@Pattern(regexp="^[0-9]{1,3}$",message="Invalid Status")
	private String status;
	
	@NotNull
	private String txnType;
	
	@NotNull
	private String rrNo;
	
	private String emailId;
	private String mobile;
	private String amount;
	private String cardPanNo;
	private String dateTime;
	private String rmn;
	private String marketingName;
	private String memberId;
	private String terminalId;
	private String merchantId;
	private String description;
	
	
	public String getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	public String getRmn() {
		return rmn;
	}
	public void setRmn(String rmn) {
		this.rmn = rmn;
	}
	public String getMarketingName() {
		return marketingName;
	}
	public void setMarketingName(String marketingName) {
		this.marketingName = marketingName;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getTerminalId() {
		return terminalId;
	}
	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getCardPanNo() {
		return cardPanNo;
	}
	public void setCardPanNo(String cardPanNo) {
		this.cardPanNo = cardPanNo;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getRrNo() {
		return rrNo;
	}
	public void setRrNo(String rrNo) {
		this.rrNo = rrNo;
	}
	public String getTxnType() {
		return txnType;
	}
	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}
	public String getTxnId() {
		return txnId;
	}
	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
