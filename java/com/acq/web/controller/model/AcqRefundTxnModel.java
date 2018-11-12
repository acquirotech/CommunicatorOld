package com.acq.web.controller.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class AcqRefundTxnModel {

	@NotNull
	@Pattern(regexp="^[0-9]{1,20}$",message="Invalid TransactionId")
	private String txnId;
	
	@NotNull
	@Pattern(regexp="^[0-9]{1,20}$",message="Invalid DE11")
	private String de11;
	
	@NotNull
	@Pattern(regexp="^[0-9]{1,6}$",message="Invalid PurhaseTxn Status")
	private String purhaseTxnStatus;
	
	
	
	public String getDe11() {
		return de11;
	}

	public void setDe11(String de11) {
		this.de11 = de11;
	}

	public String getPurhaseTxnStatus() {
		return purhaseTxnStatus;
	}

	public void setPurhaseTxnStatus(String purhaseTxnStatus) {
		this.purhaseTxnStatus = purhaseTxnStatus;
	}

	public String getTxnId() {
		return txnId;
	}

	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}
	
	

	
	
}
