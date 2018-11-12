package com.acq.users.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class LoginAcqWalletEntity {
	
	private String id;
	private String walletId;
	private String merchantId;
	private String userId;
	private String emailId;
	private Timestamp createdOn;
	private String amount;
	private Timestamp lastRechargeOn;
	private String firstName;
	private String lastName;
	private BigDecimal totalPoints;
	private BigDecimal totalCashBack;
	private BigDecimal totalAmtSpends;
	private String totalTxns;
	private String acquirerCode;
	
	public LoginAcqWalletEntity(String walletId, String merchantId,
			String userId, String emailId, Timestamp createdOn, String amount,
			Timestamp lastRechargeOn,String firstName,String lastName,BigDecimal totalPoints,BigDecimal totalCashBack,BigDecimal totalAmtSpends,String totalTxns) {
		super();
		this.walletId = walletId;
		this.merchantId = merchantId;
		this.userId = userId;
		this.emailId = emailId;
		this.createdOn = createdOn;
		this.amount = amount;
		this.lastRechargeOn = lastRechargeOn;
		this.firstName = firstName;
		this.lastName = lastName;
		this.totalPoints = totalPoints;
		this.totalCashBack = totalCashBack;
		this.totalAmtSpends = totalAmtSpends;
		this.totalTxns = totalTxns;
	}
	public LoginAcqWalletEntity(){}
	
	
	public String getAcquirerCode() {
		return acquirerCode;
	}
	public void setAcquirerCode(String acquirerCode) {
		this.acquirerCode = acquirerCode;
	}
	public BigDecimal getTotalPoints() {
		return totalPoints;
	}
	public void setTotalPoints(BigDecimal totalPoints) {
		this.totalPoints = totalPoints;
	}
	public BigDecimal getTotalCashBack() {
		return totalCashBack;
	}
	public void setTotalCashBack(BigDecimal totalCashBack) {
		this.totalCashBack = totalCashBack;
	}
	public BigDecimal getTotalAmtSpends() {
		return totalAmtSpends;
	}
	public void setTotalAmtSpends(BigDecimal totalAmtSpends) {
		this.totalAmtSpends = totalAmtSpends;
	}
	public String getTotalTxns() {
		return totalTxns;
	}
	public void setTotalTxns(String totalTxns) {
		this.totalTxns = totalTxns;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getWalletId() {
		return walletId;
	}
	public void setWalletId(String walletId) {
		this.walletId = walletId;
	}
	public String getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public Timestamp getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Timestamp createdOn) {
		this.createdOn = createdOn;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public Timestamp getLastRechargeOn() {
		return lastRechargeOn;
	}
	public void setLastRechargeOn(Timestamp lastRechargeOn) {
		this.lastRechargeOn = lastRechargeOn;
	}	
}
