package com.acq.web.controller.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


public class AcqMobiWalletDebitModel {
	
	@NotNull
	@Size(min=1,max=10,message="Invalid TotalAmount")
	@Pattern(regexp="^([0-9]\\d{0,13})?(.[0-9]{1,3}$)?$",message="Invalid TotalAmount")
	private String totalAmount;
	
	@NotNull
	@Pattern(regexp="^[0-9]{10,10}$",message="Invalid Mobile")
	private String mobile;
	
	private String email;
	
	@NotNull
	@Pattern(regexp="^[0-9]{4,6}$",message="Invalid Otp")
	private String otp;
	
	@NotNull
	@Pattern(regexp="^[0-9]{1,11}$",message="Invalid TxnId")
	private String txnId;
	
	private String status;
	
	@NotNull
	@Pattern(regexp="^[0-9]{1,20}$",message="invalid session id")
	private String sessionId;
	private String description;
	
	@Pattern(regexp="^[0-9]{10,20}$",message="Invalid WalletId")
	private String walletId;
	
	@Pattern(regexp="^[0-9]{1,20}$",message="Invalid point Used")
	private String pointUsed;
	
	@Pattern(regexp="^([0-9]\\d{0,13})?(.[0-9]{0,3}$)?$",message="Invalid Actual Amount")
	private String actualAmount;
	
	//@NotNull
	@Pattern(regexp="^(Mobikwik||Citrus Wallet||PayUMoney||Paytm||Freecharge||Airtel Money||Vodafone m-pesa)?$",message="Invalid Wallet Type")
	private String walletType;

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public String getTxnId() {
		return txnId;
	}

	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getWalletId() {
		return walletId;
	}

	public void setWalletId(String walletId) {
		this.walletId = walletId;
	}

	public String getPointUsed() {
		return pointUsed;
	}

	public void setPointUsed(String pointUsed) {
		this.pointUsed = pointUsed;
	}

	public String getActualAmount() {
		return actualAmount;
	}

	public void setActualAmount(String actualAmount) {
		this.actualAmount = actualAmount;
	}

	public String getWalletType() {
		return walletType;
	}

	public void setWalletType(String walletType) {
		this.walletType = walletType;
	}
	
	
	

}
