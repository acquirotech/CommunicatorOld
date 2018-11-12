package com.acq.users.entity;




public class AcqLoginEntity {
		
	private Long userId;
	private String username;
	private String password;
	private String status;
	private String orgId;
	private String userStatus;
	private String loyaltyStatus;
	private String walletStatus;
	private String switchType;
	private String acquirerCode;
	
	
	
	public String getAcquirerCode() {
		return acquirerCode;
	}
	public void setAcquirerCode(String acquirerCode) {
		this.acquirerCode = acquirerCode;
	}
	public String getSwitchType() {
		return switchType;
	}
	public void setSwitchType(String switchType) {
		this.switchType = switchType;
	}
	public String getLoyaltyStatus() {
		return loyaltyStatus;
	}
	public void setLoyaltyStatus(String loyaltyStatus) {
		this.loyaltyStatus = loyaltyStatus;
	}
	public String getWalletStatus() {
		return walletStatus;
	}
	public void setWalletStatus(String walletStatus) {
		this.walletStatus = walletStatus;
	}
	public String getUserStatus() {
		return userStatus;
	}
	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
}
