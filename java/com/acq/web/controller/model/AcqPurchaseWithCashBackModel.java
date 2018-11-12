package com.acq.web.controller.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class AcqPurchaseWithCashBackModel {

	@NotNull
	@Pattern(regexp="^[0-9]{4,4}$",message="Invalid Expiry Date")
	private String expiryDate;
	
	@NotNull
	@Pattern(regexp="^[0-9]{14,24}$",message="Invalid CardPANNo")
	private String cardPanNo;
	
	@NotNull
	@Size(min=5,max=60,message="Invalid Track2Data")
	private String track2Data;
	
	@NotNull
	@Size(min=1,max=14,message="Invalid Amount")
	@Pattern(regexp="^([0-9]\\d{0,13})?(.[0-9]{1,3}$)?$", message="Invalid Amount")
	private String amount;
	
	@NotNull
	@Size(min=1,max=14,message="Invalid CashBack Amount")
	@Pattern(regexp="^([0-9]\\d{0,13})?(.[0-9]{1,3}$)?$", message="Invalid CashBack Amount")
	private String cashBackAmount;
	
	@NotNull
	@Size(min=1,max=50,message="Invalid Amount")
	private String ksn;
	
	@NotNull
	@Size(min=1,max=50,message="Invalid PinBlock")
	private String pinBlock;
	
	//@NotNull
	//@Size(min=1,max=500,message="Invalid Data Element63")
	private String dataElement63;
	
	
	
	
	public String getCashBackAmount() {
		return cashBackAmount;
	}
	public void setCashBackAmount(String cashBackAmount) {
		this.cashBackAmount = cashBackAmount;
	}
	public String getDataElement63() {
		return dataElement63;
	}
	public void setDataElement63(String dataElement63) {
		this.dataElement63 = dataElement63;
	}
	public String getPinBlock() {
		return pinBlock;
	}
	public void setPinBlock(String pinBlock) {
		this.pinBlock = pinBlock;
	}
	public String getKsn() {
		return ksn;
	}
	public void setKsn(String ksn) {
		this.ksn = ksn;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}
	public String getCardPanNo() {
		return cardPanNo;
	}
	public void setCardPanNo(String cardPanNo) {
		this.cardPanNo = cardPanNo;
	}
	public String getTrack2Data() {
		return track2Data;
	}
	public void setTrack2Data(String track2Data) {
		this.track2Data = track2Data;
	}
	
	
}
