package com.acq;
public enum EnumOgsConstant {
	
	 KeyExchangeMTI("Message Type Identifier","0800"),
	 AcquiringInstitutionIdentificationcode("Acquiring Institution Identificationcode","500000"),
	 NetworkManagementCode("Network Management Code","191");
	 
	 private String message;
	 private String id;
	 
	 private EnumOgsConstant(String message, String id) {
		this.message = message;
		this.id = id;
	 }
	 public String getMessage() {
		return message ;
	 }
	
	 public String getId() {
		return id;
	 }
	
	
}