package com.acq.web.controller.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class OGSMainKeyExModel {
	
	@NotNull
	@Pattern(regexp="^[0-9]{1,20}$",message="Invalid Session Id")
	private String sessionId;
	
	@NotNull
	@Pattern(regexp="^[0-9]{1,3}$",message="Invalid UAT")
	private String uat;
	
	/*@NotNull
	@Size(min=5,max=60,message="Invalid POS DeviceId")
	private String posDeviceId;
	
	@NotNull
	@Size(min=5,max=60,message="Invalid AcquirInstIdentificationCode")
	private String acquirInstIdntfctnCode;	*/
	
	
	
	public String getSessionId() {
		return sessionId;
	}
	public String getUat() {
		return uat;
	}
	public void setUat(String uat) {
		this.uat = uat;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
	/*public String getPosDeviceId() {
		return posDeviceId;
	}
	public void setPosDeviceId(String posDeviceId) {
		this.posDeviceId = posDeviceId;
	}
	public String getAcquirInstIdntfctnCode() {
		return acquirInstIdntfctnCode;
	}
	public void setAcquirInstIdntfctnCode(String acquirInstIdntfctnCode) {
		this.acquirInstIdntfctnCode = acquirInstIdntfctnCode;
	}*/
	
	
}
