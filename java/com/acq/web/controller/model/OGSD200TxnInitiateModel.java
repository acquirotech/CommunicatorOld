package com.acq.web.controller.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class OGSD200TxnInitiateModel {

	@NotNull
	@Pattern(regexp="^[0-9]{1,20}$",message="Invalid Session Id")
	private String sessionId;
	
	@NotNull
	@Size(min=1,max=13,message="Invalid Amount")
	@Pattern(regexp="^([0-9]\\d{0,13})?(.[0-9]{1,3}$)?$",message="Invalid Amount")
	private String amount;
	
	@NotNull
	@Size(min=1,max=50, message="Invalid Device Id")
	private String deviceId;
	
	@NotNull
	@Size(min=1,max=50, message="Invalid TID")
	private String tid;
	
	
	@NotNull
	@Size(min=1,max=50, message="Invalid ImeiNo")
	private String imeiNo;
	
	@NotNull
	@Size(min=1,max=50, message="Invalid Latitude")
	private String latitude;
	
	@NotNull
	@Size(min=1,max=50, message="Invalid Longitude")
	private String longitude;
	
	@NotNull
	@Size(min=1,max=50, message="Invalid IP Address")
	private String ipAddress;
	
	@NotNull
	@Pattern(regexp="^(CARD||CASHATPOS||CASHBACK)?$",message="Invalid TxnType")
	private String txnType;
	
	@NotNull
	@Size(min=1,max=50, message="Invalid CardType")
	private String cardType;
	
	@Size(min=0,max=13,message="Invalid Amount")
	@Pattern(regexp="^([0-9]\\d{0,13})?(.[0-9]{0,3}$)?$",message="Invalid Amount")
	private String cashBackAmount;
	
	@NotNull
	@Size(min=0,max=20,message="Invalid MaskPan")
	private String maskPan;
	
	
	public String getMaskPan() {
		return maskPan;
	}
	public void setMaskPan(String maskPan) {
		this.maskPan = maskPan;
	}
	public String getCashBackAmount() {
		return cashBackAmount;
	}
	public void setCashBackAmount(String cashBackAmount) {
		this.cashBackAmount = cashBackAmount;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	public String getImeiNo() {
		return imeiNo;
	}
	public void setImeiNo(String imeiNo) {
		this.imeiNo = imeiNo;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getTxnType() {
		return txnType;
	}
	public void setTxnType(String txnType) {
		this.txnType = txnType;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	
}
