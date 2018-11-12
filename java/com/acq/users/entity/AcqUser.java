package com.acq.users.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

public class AcqUser implements Serializable{	
	
	private Long id;
	private Long phoneNo;
	private AcqDevice acqDevice;
	private Long orgId;
	
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	@Id
	@Column(name = "id")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Column(name = "login_id", unique = true)
	public Long getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(Long phoneNo) {
		this.phoneNo = phoneNo;
	}
	
	@OneToOne(fetch = FetchType.LAZY, mappedBy = "ezUser", cascade = CascadeType.ALL)
	public AcqDevice getEzDevice() {
		return acqDevice;
	}
	public void setEzDevice(AcqDevice acqDevice) {
		this.acqDevice = acqDevice;
	}
}
