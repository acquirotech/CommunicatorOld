package com.acq.users.entity;

public class AcqGCMNotificationEntity {
	private Integer Id;
	private Long userId;
	private String gcmNotification;
	
	
	public AcqGCMNotificationEntity(){}
	public Integer getId() {
		return Id;
	}
	public void setId(Integer id) {
		Id = id;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getGcmNotification() {
		return gcmNotification;
	}
	public void setGcmNotification(String gcmNotification) {
		this.gcmNotification = gcmNotification;
	}
	
}
