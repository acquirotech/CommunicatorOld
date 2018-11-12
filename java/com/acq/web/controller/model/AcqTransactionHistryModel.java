package com.acq.web.controller.model;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import security.AcquiroSeq;

public class AcqTransactionHistryModel {

	@NotNull
	@Pattern(regexp="^[0-9]{1,20}$",message="invalid session id")	
	private String sessionId;

	@NotNull
	@Pattern(regexp="^[0-9]{1,3}$",message="invalid transaction count")
	private String txnCount;

	@Pattern(regexp="^[a-zA-Z]{3,7}$",message="invalid protocol")
	private String protocol;	
	
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = AcquiroSeq.MakeNonSmart(protocol);
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = AcquiroSeq.MakeNonSmart(sessionId);
	}	
	public String getTxnCount() {
		return txnCount;
	}
	public void setTxnCount(String txnCount) {
		this.txnCount = AcquiroSeq.MakeNonSmart(txnCount);
	} 
}