package com.acq.web.controller.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


public class AcqPostTransactionModel {
	
	@NotNull
	@Pattern(regexp="^[0-9]{1,20}$",message="Invalid SessionId")
	private String sessionId;
	
	@NotNull
	@Pattern(regexp="^[0-9]{1,20}$",message="Invalid TransactionId")
	private String transactionId;
	
	@NotNull
	@Size(min=2,max=100, message="Invalid Status Description")
	private String statusDescription;
	
	@NotNull
	@Pattern(regexp="^[0-9]{1,1}$",message="Invalid Status Code")
	private String statusCode;
	
	@Size(min=1,max=100, message="Invalid Stan")
	private String stan;	
	
	@Pattern(regexp="^[0-9]{10,20}$",message="Invalid WalletId")
	private String walletId;
	
	//@Pattern(regexp="^[0-9\\x]{4,20}$",message="Invalid Card Pan No")
	//@Size(min=4,max=20, message="Invalid Card Pan No")
	
	
	@Size(min=4,max=20, message="Invalid Card Pan No")
	private String cardPanNo;

	
	@Size(min=2,max=100, message="Invalid Card Holder Name")
	private String cardHolderName;

	
	@Size(min=2,max=50, message="Invalid Card Type")
	private String cardType;
	
	
	@Pattern(regexp="^[0-9]{1,20}$",message="Invalid RRNo")
	private String rrNo;
	
	@Pattern(regexp="^[0-9a-zA-Z]{1,20}$",message="Invalid AuthCode")
	private String authCode;
	
	@Pattern(regexp="^[0-9]{1,20}$",message="Invalid BatchNo")
	private String batchNo;	
	
	@Pattern(regexp="^[0-9]{1,20}$",message="Invalid point Used")
	private String pointUsed;
	
	@Pattern(regexp="^([0-9]\\d{0,13})?(.[0-9]{0,3}$)?$",message="Invalid Actual Amount")
	private String actualAmount;
	 
	public String getPointUsed() {
		return pointUsed;
	}

	public void setPointUsed(String pointUsed) {
		//System.out.println("pointUsed:"+pointUsed);
		//this.pointUsed = AcquiroSeq.MakeNonSmart(pointUsed);
		this.pointUsed = pointUsed;
	}

	public String getActualAmount() {
		return actualAmount;
	}

	public void setActualAmount(String actualAmount) {
		//System.out.println("actualAmount:"+actualAmount);
		//this.actualAmount = AcquiroSeq.MakeNonSmart(actualAmount);
		this.actualAmount = actualAmount;
	}



	public String getWalletId() {
		return walletId;
	}

	public void setWalletId(String walletId) {
		//System.out.println("walletId:"+walletId);
		this.walletId = walletId;
		//this.walletId = AcquiroSeq.MakeNonSmart(walletId);
	}
	
	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
		//System.out.println("sessionId:"+sessionId);
		//this.sessionId = AcquiroSeq.MakeNonSmart(sessionId);
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
		//System.out.println("transactionId:"+transactionId);
		//this.transactionId = AcquiroSeq.MakeNonSmart(transactionId);
	}

	public String getStan() {
		return stan;
	}

	public void setStan(String stan) {
		this.stan = stan;
		//System.out.println("stan:"+stan);
		//this.stan = AcquiroSeq.MakeNonSmart(stan);
	}

	public String getCardPanNo() {
		return cardPanNo;
	}

	public void setCardPanNo(String cardPanNo) {
		//System.out.println("cardPanNo:"+cardPanNo);
		this.cardPanNo = cardPanNo;
		//this.cardPanNo = AcquiroSeq.MakeNonSmart(cardPanNo);
	}

	public String getCardHolderName() {
		return cardHolderName;
	}

	public void setCardHolderName(String cardHolderName) {
		//System.out.println("cardHolderName:"+cardHolderName);
		this.cardHolderName = cardHolderName;
		//this.cardHolderName = AcquiroSeq.MakeNonSmart(cardHolderName);
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		//System.out.println("cardType:"+cardType);
		this.cardType = cardType;
		//this.cardType = AcquiroSeq.MakeNonSmart(cardType);
	}

	public String getRrNo() {
		return rrNo;
	}

	public void setRrNo(String rrNo) {
		//System.out.println("rrNo:"+rrNo);
		this.rrNo = rrNo;
		//this.rrNo = AcquiroSeq.MakeNonSmart(rrNo);
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		//System.out.println("authCode:"+authCode);
		this.authCode = authCode;
		//this.authCode = AcquiroSeq.MakeNonSmart(authCode);
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		//System.out.println("batchNo:"+batchNo);
		this.batchNo = batchNo;
		//this.batchNo = AcquiroSeq.MakeNonSmart(batchNo);
	}

	public String getStatusDescription() {
		return statusDescription;
	}

	public void setStatusDescription(String statusDescription) {
		//System.out.println("statusDescription:"+statusDescription);
		this.statusDescription = statusDescription;
		//this.statusDescription = AcquiroSeq.MakeNonSmart(statusDescription);
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		//System.out.println("statusCode"+statusCode);
		this.statusCode = statusCode;
		//this.statusCode = AcquiroSeq.MakeNonSmart(statusCode);
	}
	
	
	
	
}
