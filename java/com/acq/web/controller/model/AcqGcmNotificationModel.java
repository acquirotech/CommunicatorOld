package com.acq.web.controller.model;

public class AcqGcmNotificationModel {
	private String sessionId;
	private String gcmTitle;
	private String gcmMessage;
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getGcmTitle() {
		return gcmTitle;
	}
	public void setGcmTitle(String gcmTitle) {
		this.gcmTitle = gcmTitle;
	}
	public String getGcmMessage() {
		return gcmMessage;
	}
	public void setGcmMessage(String gcmMessage) {
		this.gcmMessage = gcmMessage;
	}
	
}
