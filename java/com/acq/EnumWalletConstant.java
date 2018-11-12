package com.acq;

public enum EnumWalletConstant {
		OTPGenerated("OTP Generated","201"),	
		NoWalletFound("No Wallet Found","202"),
		InvalidOtp("Invalid OTP","203"),
		InsufficientAmount("Insufficient Amount","204"),
		AmountDebited("Amount Debited","205"),
		OrderIdAlreadyProcessed("OrderId Already Processed","206"),
		NoOfAttempts("You have exceeded maximum number of attempts","207"),
		Other("Unknown Response","299"),
		TransactionNotFound("TransactionId Not Found","208"),
		Failed("FAILED",""),
		MobileMismatch("Mobile No Mismatch","209"),
		AmountShouldNotBeLessThenOne("Amount should not be less than one","210"),
		AmountMismatch("Amount Mismatch","211"),
		Success("SUCCESS",""),
		VerificationCodeHasExpired("Verification code has expired","212"),
		VerificationSuccess("Verification success","213"),
		AuthenticationFailure("Authentication Failure","214"),
		TransactionInitiated("Transaction Inititated","215"),
		FailedCardTransaction("Transaction Failed","216");
		private String description;
		private String id;
		private EnumWalletConstant(String description, String id) {
			this.description = description;
			this.id = id;
		}
		public String getDescription() {
			return description ;
		}
		public String getId() {
			return id;
		}	
	}

