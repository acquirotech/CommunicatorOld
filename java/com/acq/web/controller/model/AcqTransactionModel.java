package com.acq.web.controller.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import security.AcquiroSeq;

public class AcqTransactionModel {

	@NotNull
	@Pattern(regexp="^[0-9]{1,20}$",message="Invalid UserId")
	private String userId;

	@NotNull
	@Size(min=1,max=10,message="Invalid Amount")
	@Pattern(regexp="^([0-9]\\d{0,13})?(.[0-9]{1,3}$)?$",message="Invalid Amount")
	private String totalAmount;
	
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
	@Size(min=1,max=50, message="Invalid Device Id")
	private String deviceId;
	
	
	@NotNull
	@Pattern(regexp="^(CARD||CASHATPOS)?$",message="Invalid CardTxnType")
	private String cardTxnType;

	
	
	public String getCardTxnType() {
		return cardTxnType;
	}

	public void setCardTxnType(String cardTxnType) {
		this.cardTxnType = cardTxnType;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {		
		this.userId = userId;
		//this.userId = AcquiroSeq.MakeNonSmart(userId);
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
		//this.totalAmount = AcquiroSeq.MakeNonSmart(totalAmount);
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
		//this.ipAddress = AcquiroSeq.MakeNonSmart(ipAddress);
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
		//this.latitude = AcquiroSeq.MakeNonSmart(latitude);
	}

	public String getLogitude() {
		return logitude;
	}

	public void setLogitude(String logitude) {
		this.logitude = logitude;
		//this.logitude = AcquiroSeq.MakeNonSmart(logitude);
	}

	public String getImeiNo() {
		return imeiNo;
	}

	public void setImeiNo(String imeiNo) {
		this.imeiNo = imeiNo;
		//this.imeiNo = AcquiroSeq.MakeNonSmart(imeiNo);
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
		//this.deviceId = AcquiroSeq.MakeNonSmart(deviceId);
	}
	
	
	
	
}
