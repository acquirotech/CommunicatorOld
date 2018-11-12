package com.acq.web.controller.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class AcqReversalTxnModel {

	@NotNull
	@Pattern(regexp="^[0-9]{1,20}$",message="Invalid TransactionId")
	private String txnId;

	@NotNull
	@Pattern(regexp="^[0-9]{14,24}$",message="Invalid CardPANNo")
	private String cardPanNo;

	@NotNull
	@Size(min=5,max=60,message="Invalid Track2Data")
	private String track2Data;


	public String getTxnId() {
		return txnId;
	}

	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}


	public String getCardPanNo() {
		return cardPanNo;
	}

	public void setCardPanNo(String cardPanNo) {
		this.cardPanNo = cardPanNo;
	}

	public String getTrack2Data() {
		return track2Data;
	}

	public void setTrack2Data(String track2Data) {
		this.track2Data = track2Data;
	}
}
