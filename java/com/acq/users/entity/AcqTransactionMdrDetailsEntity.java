package com.acq.users.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
public class AcqTransactionMdrDetailsEntity {

	private Long id;
	private String mdrCat;
	private String bankMdr;
	private String ezMdr;
	private String serviceTax;
	private AcqTransactionDetailEntity detailsEntity;
	
	
	public AcqTransactionDetailEntity getDetailsEntity() {
		return detailsEntity;
	}
	public void setDetailsEntity(AcqTransactionDetailEntity detailsEntity) {
		this.detailsEntity = detailsEntity;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	
	public String getMdrCat() {
		return mdrCat;
	}
	public void setMdrCat(String mdrCat) {
		this.mdrCat = mdrCat;
	}
	
	
	public String getBankMdr() {
		return bankMdr;
	}
	public void setBankMdr(String bankMdr) {
		this.bankMdr = bankMdr;
	}
	
	
	public String getEzMdr() {
		return ezMdr;
	}
	public void setEzMdr(String ezMdr) {
		this.ezMdr = ezMdr;
	}
	
	
	public String getServiceTax() {
		return serviceTax;
	}
	public void setServiceTax(String serviceTax) {
		this.serviceTax = serviceTax;
	}
}
