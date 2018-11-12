package com.acq.users.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "acq_terminal_user_info",schema="acquiro_ver1")
public class AcqDevice implements Serializable {
	
	private Long id;
	private String deviceSerialNo;	
	private String userId;
	private String tid;
	private String avgTxnAmount;
	private AcqUser acqUser;
	
	@Id
	@Column(name = "id")
	public Long getId() {
		return id;
	}
	
	@OneToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
	public AcqUser getEzUser() {
		return acqUser;
	}
	
	public void setEzUser(AcqUser acqUser) {
		this.acqUser = acqUser;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "serial_no")
	public String getDeviceSerialNo() {
		return deviceSerialNo;
	}
	public void setDeviceSerialNo(String deviceSerialNo) {
		this.deviceSerialNo = deviceSerialNo;
	}
	
	@Column(name = "user_id")
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "avgtxnamount")
	public String getAvgTxnAmount() {
		return avgTxnAmount;
	}

	public void setAvgTxnAmount(String avgTxnAmount) {
		this.avgTxnAmount = avgTxnAmount;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}
	
}
