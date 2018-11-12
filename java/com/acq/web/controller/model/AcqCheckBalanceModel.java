package com.acq.web.controller.model;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.json.simple.JSONObject;


public class AcqCheckBalanceModel {

	private List<JSONObject> accessTokens;
	
	
	@NotNull
	@Pattern(regexp="^[0-9]{1,20}$",message="Invalid CustomerId")
	private String customerId;


	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		//this.customerId = AcquiroSeq.MakeNonSmart(customerId);
		this.customerId = customerId;
	}

	public List<JSONObject> getAccessTokens() {
		return accessTokens;
	}

	public void setAccessTokens(List<JSONObject> accessTokens) {
		this.accessTokens = accessTokens;
		//this.accessTokens = AcquiroSeq.MakeNonSmart(accessTokens);
	}
	
	
	
}
