package com.acq.users.entity;

public class AcqLoyaltyEntity {
	private String id;
	private String mid;
	private String loyaltyType;
	private String lastUpdatedOn;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}
	
	public String getLoyaltyType() {
		return loyaltyType;
	}
	public void setLoyaltyType(String loyaltyType) {
		this.loyaltyType = loyaltyType;
	}
	public String getLastUpdatedOn() {
		return lastUpdatedOn;
	}
	public void setLastUpdatedOn(String lastUpdatedOn) {
		this.lastUpdatedOn = lastUpdatedOn;
	}
	
}
