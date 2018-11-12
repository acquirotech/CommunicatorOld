package com.acq.web.controller.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import security.AcquiroSeq;

public class AcqTransactionSignModel {
	
    private String signImg;

	@NotNull
	@Size(min=4,max=20, message="Invalid Card Pan No")
	private String cardPanNo;

    @NotNull
    @Pattern(regexp="^[0-9]{10,10}$",message="invalid Phone no")
	private String custPhone;

    @NotNull
    @Pattern(regexp="^[0-9]{1,20}$",message="invalid session id")
    private String sessionId;

    @NotNull
    @Pattern(regexp="^[0-9]{1,20}$",message="invalid transaction id")
    private String transactionId;

    @Pattern(regexp="^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9_\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$|{0}$",message="invalid email")
    private String custEmail;
    
	

	public String getSignImg() {
		return signImg;
	}

	public void setSignImg(String signImg) {
		this.signImg = signImg;
	}

	public String getCardPanNo() {
		return cardPanNo;
	}

	public void setCardPanNo(String cardPanNo) {
		//this.cardPanNo = cardPanNo;
		this.cardPanNo = AcquiroSeq.MakeNonSmart(cardPanNo);
	}

	public String getCustPhone() {
		return custPhone;
	}

	public void setCustPhone(String custPhone) {
		//this.custPhone = custPhone;
		this.custPhone = AcquiroSeq.MakeNonSmart(custPhone);
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		//this.sessionId = sessionId;
		this.sessionId = AcquiroSeq.MakeNonSmart(sessionId);
		System.out.println("sessionId:"+sessionId);
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		//this.transactionId = transactionId;
		this.transactionId = AcquiroSeq.MakeNonSmart(transactionId);
	}

	public String getCustEmail() {
		return custEmail;
	}

	public void setCustEmail(String custEmail) {
		this.custEmail = custEmail;
	}

	
	
}