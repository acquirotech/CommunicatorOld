package com.acq.web.controller.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import security.AcquiroSeq;

public class AcqVoidTransactionModel {
	
	
	  	@NotNull
	    @Pattern(regexp="^[0-9]{1,20}$",message="Invalid session id")
	    private String sessionId;
	    
	    @NotNull
	    @Pattern(regexp="^[0-9]{1,20}$",message="Invalid RR No")
	    private String rrNo;
	    
	    private String mobile;
	    private String amount;
	    private String dateTime;
	    private String cardPanNo;
	    private String marketingName;
	    private String rmn;
	    private String emailId;
	    private String memberId;
	    private String terminalId;
	    private String txnId;
	    private String merchantId;
	    private String txnType;
	    private String status;
	    private String description;
	    private String acquirerCode;
	    
	    
	    
		public String getAcquirerCode() {
			return acquirerCode;
		}

		public void setAcquirerCode(String acquirerCode) {
			this.acquirerCode = acquirerCode;
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

		public String getMerchantId() {
			return merchantId;
		}

		public void setMerchantId(String merchantId) {
			this.merchantId = merchantId;
		}

		public String getTxnId() {
			return txnId;
		}

		public void setTxnId(String txnId) {
			this.txnId = txnId;
		}

		public String getTxnType() {
			return txnType;
		}

		public void setTxnType(String txnType) {
			this.txnType = txnType;
		}

		public String getEmailId() {
			return emailId;
		}

		public void setEmailId(String emailId) {
			this.emailId = emailId;
		}

		public String getRmn() {			
			return rmn;
		}

		public void setRmn(String rmn) {			
			this.rmn = rmn;
		}

		public String getDateTime() {
			return dateTime;
		}

		public void setDateTime(String dateTime) {
			this.dateTime = dateTime;
		}

		public String getCardPanNo() {
			return cardPanNo;
		}

		public void setCardPanNo(String cardPanNo) {
			this.cardPanNo = cardPanNo;
		}

		public String getMarketingName() {
			return marketingName;
		}

		public void setMarketingName(String marketingName) {
			this.marketingName = marketingName;
		}

		public String getAmount() {
			return amount;
		}

		public void setAmount(String amount) {
			this.amount = amount;
		}

		public String getMobile() {
			return mobile;
		}

		public void setMobile(String mobile) {
			this.mobile = mobile;
		}

		public String getSessionId() {
			return sessionId;
		}

		public void setSessionId(String sessionId) {
			this.sessionId = AcquiroSeq.MakeNonSmart(sessionId);
			//this.sessionId = sessionId; 
		}

		public String getRrNo() {
			return rrNo;
		}

		public void setRrNo(String rrNo) {
			this.rrNo = AcquiroSeq.MakeNonSmart(rrNo);
			//this.rrNo = rrNo;
		}

		   
}
