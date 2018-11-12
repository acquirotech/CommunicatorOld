package com.acq.web.controller.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class OGSD200VoidTransactionModel {

	
	@NotNull
	@Size(min=0,max=20,message="Invalid MaskPan")
	private String maskPan;
	
	@NotNull
	@Size(min=1,max=50, message="Invalid TID")
	private String tid;
	
	
	@NotNull
	@Pattern(regexp="^[0-9]{1,20}$",message="Invalid Session Id")
	private String sessionId;
	
	public String getMaskPan() {
		return maskPan;
	}
	public void setMaskPan(String maskPan) {
		this.maskPan = maskPan;
	}
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	
}
