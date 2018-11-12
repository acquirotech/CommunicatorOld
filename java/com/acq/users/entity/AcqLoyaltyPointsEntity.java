package com.acq.users.entity;


public class AcqLoyaltyPointsEntity {
	private String id;
	private String loyaltyId;
	private String amount;
	private String noOfPoints;
	private String minAmountForPoints;
	private String maxAmountForPoints;
	private String minUsePoints;
	private String maxUsePoints;
	private String maxPointContains;
	private String validityPoints;
	private String pointsPerVisit;
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
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getNoOfPoints() {
		return noOfPoints;
	}
	public void setNoOfPoints(String noOfPoints) {
		this.noOfPoints = noOfPoints;
	}
	public String getMinAmountForPoints() {
		return minAmountForPoints;
	}
	public void setMinAmountForPoints(String minAmountForPoints) {
		this.minAmountForPoints = minAmountForPoints;
	}
	public String getMaxAmountForPoints() {
		return maxAmountForPoints;
	}
	public void setMaxAmountForPoints(String maxAmountForPoints) {
		this.maxAmountForPoints = maxAmountForPoints;
	}
	public String getMinUsePoints() {
		return minUsePoints;
	}
	public void setMinUsePoints(String minUsePoints) {
		this.minUsePoints = minUsePoints;
	}
	public String getMaxUsePoints() {
		return maxUsePoints;
	}
	public void setMaxUsePoints(String maxUsePoints) {
		this.maxUsePoints = maxUsePoints;
	}
	public String getMaxPointContains() {
		return maxPointContains;
	}
	public void setMaxPointContains(String maxPointContains) {
		this.maxPointContains = maxPointContains;
	}
	public String getValidityPoints() {
		return validityPoints;
	}
	public void setValidityPoints(String validityPoints) {
		this.validityPoints = validityPoints;
	}
	public String getPointsPerVisit() {
		return pointsPerVisit;
	}
	public void setPointsPerVisit(String pointsPerVisit) {
		this.pointsPerVisit = pointsPerVisit;
	}
	public String getLastUpdateOn() {
		return lastUpdateOn;
	}
	public void setLastUpdateOn(String lastUpdateOn) {
		this.lastUpdateOn = lastUpdateOn;
	}
	
}
