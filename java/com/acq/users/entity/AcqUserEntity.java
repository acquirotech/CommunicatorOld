package com.acq.users.entity;



public class AcqUserEntity {
	
	private Long userId;
	private Long orgId;
	private String acquirerCode;
	
	public AcqUserEntity(){}
	
	
	
	public String getAcquirerCode() {
		return acquirerCode;
	}



	public void setAcquirerCode(String acquirerCode) {
		this.acquirerCode = acquirerCode;
	}



	public Long getUserId() {
		return userId;
	}
	
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	
	
	
	
}
