package com.acq.web.dao;

import com.acq.web.controller.model.AcqBankItCardLoadModel;
import com.acq.web.controller.model.AcqBankItTransactionListModel;
import com.acq.web.controller.model.AcqMoneyTransferAddBeneficiaryModel;
import com.acq.web.controller.model.AcqMoneyTransferAddSenderModel;
import com.acq.web.controller.model.AcqMoneyTransferBankListModel;
import com.acq.web.controller.model.AcqMoneyTransferCheckStatusModel;
import com.acq.web.controller.model.AcqMoneyTransferNeftModel;
import com.acq.web.controller.model.AcqMoneyTransferOtpModel;
import com.acq.web.dto.impl.DbDto2;

public interface MoneyTransferDaoInf {

	
	
	//public DbDto2<Object> getOperators(AcqRechargeMerchantBalModel model);

//	public DbDto2<Object> doRecharge(AcqRechargeModel model);



	//public DbDto2<String> getMerchantBalance(AcqRechargeMerchantBalModel model);

	
	//public DbDto2<String> getRechargeList(AcqRechargeMerchantBalModel model);

	//public DbDto2<String> getRechargeStatus(AcqRechargeStatusModel model);

	public DbDto2<Object> dmtgenerateOtp(AcqMoneyTransferOtpModel model);



	public DbDto2<Object> addSender(AcqMoneyTransferAddSenderModel model);


	
	public DbDto2<Object> fetchBeneficiaryList(AcqMoneyTransferOtpModel model);

	public DbDto2<Object> addBeneficiary(AcqMoneyTransferAddBeneficiaryModel model);

	public DbDto2<Object> dmtNeft(AcqMoneyTransferNeftModel model);

	public DbDto2<Object> dmtDeleteBeneficiary(AcqMoneyTransferNeftModel model);

	public DbDto2<Object> bankList(AcqMoneyTransferBankListModel model);

	public DbDto2<Object> checkStatus(AcqMoneyTransferCheckStatusModel model);



	public DbDto2<Object> dmtImps(AcqMoneyTransferNeftModel model);



	public DbDto2<Object> getTransactionList(AcqBankItTransactionListModel model);



	public DbDto2<Object> cardLoad(AcqBankItCardLoadModel model);



	public DbDto2<Object> cardLoadStatus(AcqBankItCardLoadModel model);



	public DbDto2<Object> getCardBal(AcqBankItCardLoadModel model);

}
