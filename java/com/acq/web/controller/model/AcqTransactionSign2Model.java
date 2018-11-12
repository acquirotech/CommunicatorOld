package com.acq.web.controller.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.web.multipart.MultipartFile;

import security.AcquiroSeq;

public class AcqTransactionSign2Model {
	
    private MultipartFile signImg;
    
    
    @NotNull
	@Pattern(regexp="^[0-9]{1,20}$",message="invalid card pan no")	
    private String cardPanNo;
    
    public String getCardPanNo() {
		return cardPanNo;
	}

	public void setCardPanNo(String cardPanNo) {
		this.cardPanNo = AcquiroSeq.MakeNonSmart(cardPanNo);
	}
    
    @NotNull
	@Pattern(regexp="^[0-9]{10,10}$",message="invalid Phone no")
    private String custPhone;
   
	

	public String getCustPhone() {
		return custPhone;
	}

	public void setCustPhone(String custPhone) {
		this.custPhone = AcquiroSeq.MakeNonSmart(custPhone);
	}

	@NotNull
    @Pattern(regexp="^[0-9]{1,20}$",message="invalid session id")
    private String sessionId;
    
    @NotNull
    @Pattern(regexp="^[0-9]{1,20}$",message="invalid invoice no")
    private String invoiceNo;
    
    @Pattern(regexp="^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9_\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$|{0}$",message="invalid credentials")
    private String custEmail;

	public MultipartFile getSignImg() {
		return signImg;
	}

	public void setSignImg(MultipartFile signImg) {
		this.signImg = signImg;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = AcquiroSeq.MakeNonSmart(sessionId);
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = AcquiroSeq.MakeNonSmart(invoiceNo);
	}

	public String getCustEmail() {
		return custEmail;
	}

	public void setCustEmail(String custEmail) {
		this.custEmail = AcquiroSeq.MakeNonSmart(custEmail);
	}
	

    
}