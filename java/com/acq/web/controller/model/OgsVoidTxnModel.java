package com.acq.web.controller.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class OgsVoidTxnModel {
	
	@NotNull
	@Pattern(regexp="^[0-9]{1,20}$",message="Invalid TxnId")
	private String txnId;	
	
	@NotNull
	@Pattern(regexp="^[0-9]{14,24}$",message="Invalid CardPANNo")
	private String cardPanNo;
	
	@NotNull
	@Size(min=1,max=14,message="Invalid Amount")
	@Pattern(regexp="^([0-9]\\d{0,13})?(.[0-9]{1,3}$)?$", message="Invalid Amount")
	private String amount;
	
	@NotNull
	@Size(min=1,max=50,message="Invalid RRNo")
	private String rrNo;
	
	@NotNull
	@Size(min=1,max=20,message="Invalid DE7")
	private String de7;
	
	
	public String getDe7() {
		return de7;
	}
	public void setDe7(String de7) {
		this.de7 = de7;
	}
	public String getRrNo() {
		return rrNo;
	}
	public void setRrNo(String rrNo) {
		this.rrNo = rrNo;
	}
	public String getTxnId() {
		return txnId;
	}
	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}
	public String getCardPanNo() {
		return cardPanNo;
	}
	public void setCardPanNo(String cardPanNo) {
		this.cardPanNo = cardPanNo;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}	
}
