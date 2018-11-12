package com.acq.web.controller.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


public class AcqSearchTransactionModel {

	
	@NotNull
	@Pattern(regexp="^[0-9]{1,20}$",message="invalid session id")	
	private String sessionId;
	
	
	@NotNull
	@Pattern(regexp="^[0-9]{1,20}$",message="invalid card pan no")	
	private String cardPanNo;


	public String getSessionId() {
		return sessionId;
	}


	public String getCardPanNo() {
		return cardPanNo;
	}


	public void setCardPanNo(String cardPanNo) {
		this.cardPanNo = cardPanNo;
	}


	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	
}