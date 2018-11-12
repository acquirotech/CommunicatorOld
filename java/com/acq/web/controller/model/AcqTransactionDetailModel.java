
package com.acq.web.controller.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import security.AcquiroSeq;

public class AcqTransactionDetailModel {


@NotNull
@Pattern(regexp="^[0-9]{1,20}$",message="invalid session id")	
private String sessionId;

@NotNull
@Pattern(regexp="^[0-9]{1,20}$",message="invalid transaction id")
private String transactionId;

public String getSessionId() {
	return sessionId;
}

public void setSessionId(String sessionId) {
	this.sessionId = AcquiroSeq.MakeNonSmart(sessionId);
}

public String getTransactionId() {
	return transactionId;
}

public void setTransactionId(String transactionId) {
	this.transactionId = AcquiroSeq.MakeNonSmart(transactionId);
}


}
