package com.acq.users.entity;


public class AcqLoyaltyGiftEntity {
	 private String id;
	 private String loyaltyId;
	 private String giftName1;
	 private String giftName2;
	 private String giftName3;
	 private String giftAmount;
	 private String txnAmount;
	 private String lastUpdateOn;
	 
	 
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLoyaltyId() {
		return loyaltyId;
	}
	public void setLoyaltyId(String loyaltyId) {
		this.loyaltyId = loyaltyId;
	}
	
	public String getGiftName1() {
		return giftName1;
	}
	public void setGiftName1(String giftName1) {
		this.giftName1 = giftName1;
	}
	public String getGiftName2() {
		return giftName2;
	}
	public void setGiftName2(String giftName2) {
		this.giftName2 = giftName2;
	}
	public String getGiftName3() {
		return giftName3;
	}
	public void setGiftName3(String giftName3) {
		this.giftName3 = giftName3;
	}
	public String getGiftAmount() {
		return giftAmount;
	}
	public void setGiftAmount(String giftAmount) {
		this.giftAmount = giftAmount;
	}
	public String getTxnAmount() {
		return txnAmount;
	}
	public void setTxnAmount(String txnAmount) {
		this.txnAmount = txnAmount;
	}
	public String getLastUpdateOn() {
		return lastUpdateOn;
	}
	public void setLastUpdateOn(String lastUpdateOn) {
		this.lastUpdateOn = lastUpdateOn;
	}
	
}
