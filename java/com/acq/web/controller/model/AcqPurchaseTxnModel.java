package com.acq.web.controller.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class AcqPurchaseTxnModel {

	
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
	@Size(min=1,max=50,message="Invalid Amount")
	private String ksn;
	
	@NotNull
	@Size(min=1,max=50,message="Invalid PinBlock")
	private String pinBlock;
		
	/*@NotNull
	@Pattern(regexp="^[0-9]{1,20}$",message="Invalid Session Id")
	private String sessionId;
	
	@NotNull
	@Pattern(regexp="^(CARD||CASHATPOS)||CASHBACK)?$",message="Invalid TxnType")
	private String txnType;
	
	@NotNull
	@Pattern(regexp="^[0-9\\.]{2,20}$",message="Invalid UserId")
	private String ipAddress;

	@NotNull
	@Size(min=2,max=50, message="Invalid Latitude")
	private String latitude;

	@NotNull
	@Size(min=2,max=50, message="Invalid Longitude")
	private String logitude;

	@NotNull
	@Size(min=2,max=50, message="Invalid IMEINo")
	private String imeiNo;
	
	@NotNull
	@Size(min=2,max=50, message="Invalid CardHolder Name")
	private String cardHolderName;
	
	@NotNull
	@Size(min=2,max=50, message="Invalid Card Type")
	private String cardType;
	
	
	public String getCardHolderName() {
		return cardHolderName;
	}
	public void setCardHolderName(String cardHolderName) {
		this.cardHolderName = cardHolderName;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLogitude() {
		return logitude;
	}
	public void setLogitude(String logitude) {
		this.logitude = logitude;
	}
	public String getImeiNo() {
		return imeiNo;
	}
	public void setImeiNo(String imeiNo) {
		this.imeiNo = imeiNo;
	}
	public String getTxnType() {
		return txnType;
	}
	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}	*/
	
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
