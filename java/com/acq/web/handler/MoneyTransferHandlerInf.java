package com.acq.web.handler;

import com.acq.web.controller.model.AcqBankItCardLoadModel;
import com.acq.web.controller.model.AcqBankItTransactionListModel;
import com.acq.web.controller.model.AcqMoneyTransferAddBeneficiaryModel;
import com.acq.web.controller.model.AcqMoneyTransferAddSenderModel;
import com.acq.web.controller.model.AcqMoneyTransferBankListModel;
import com.acq.web.controller.model.AcqMoneyTransferCheckStatusModel;
import com.acq.web.controller.model.AcqMoneyTransferNeftModel;
import com.acq.web.controller.model.AcqMoneyTransferOtpModel;
import com.acq.web.dto.impl.ServiceDto2;

public interface MoneyTransferHandlerInf {

	public ServiceDto2<Object> dmtgenerateOtp(AcqMoneyTransferOtpModel model);
	public ServiceDto2<Object> addSender(AcqMoneyTransferAddSenderModel model);
	public ServiceDto2<Object> fetchBeneficiaryList(AcqMoneyTransferOtpModel model);
	public ServiceDto2<Object> addBeneficiary(AcqMoneyTransferAddBeneficiaryModel model);
	public ServiceDto2<Object> dmtNeft(AcqMoneyTransferNeftModel model);
	public ServiceDto2<Object> dmtDeleteBeneficiary(AcqMoneyTransferNeftModel model);
	public ServiceDto2<Object> getBankList(AcqMoneyTransferBankListModel model);
	public ServiceDto2<Object> checkStatus(AcqMoneyTransferCheckStatusModel model);
	public ServiceDto2<Object> dmtImps(AcqMoneyTransferNeftModel model);
	public ServiceDto2<Object> getTransactionList(AcqBankItTransactionListModel model);
	public ServiceDto2<Object> cardLoad(AcqBankItCardLoadModel model);
	public ServiceDto2<Object> cardLoadStatus(AcqBankItCardLoadModel model);
	public ServiceDto2<Object> getCardBal(AcqBankItCardLoadModel model);
	
	
	
}
