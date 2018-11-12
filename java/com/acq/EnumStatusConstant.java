package com.acq;
public enum EnumStatusConstant {
	 ClientError("",1),
	 ServerError("Server Error",2),
	 RollBackError("Rollback Error",100),
	 UserValidationError("User validation Errors",102),
	 UserAccessError("Invalid User",101),
	 NotFound("Not Found",101),
	 LessLessThanOne("Amount is less than 1Rs.",100),
	 TransactionNotFound("Transaction Not found",100),
	 AlreadyInUse("Already In Use",101),
	 RecordNotFound("Records Not Found",101),
	 PGError("Payment gateway Connection Errors",301),
	 PGUserCredentialsError("User Card details Error",302),
	 OK("OK",0),
	 WrongFormat("Invalid Param Value",101),
	 InvalidSession("Session Expired",111),
	 InvalidCrediential("invalid crediential",100),
	 WalletAlreadyInUse("Walletid already in use",100),
	 TransactionTimeOut("Transaction Time Out",102),
	 IssuerConnectIssue("Issuer Not Responding",100),
	 CashTransactionAmount("Cash transaction amount should be less than 50000Rs",100),
	 TransactionAmountMismatch("Mismatch Transaction Amount",100),
	 MaintenanceMode("Server is in maintenance mode, please try after some time",100),
	 InvalidData("Records Not Found",100),
	 InsufficientBalance("Insufficient Balance",100);
	 
	 private String description;
	 private int id;
	 private EnumStatusConstant(String description, int id) {
		this.description = description;
		this.id = id;
	}
	public String getDescription() {
		return description ;
	}
	
	public int getId() {
		return id;
	}
	
	
}