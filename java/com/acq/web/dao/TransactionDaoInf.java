package com.acq.web.dao;

import java.util.HashMap;
import java.util.List;

import com.acq.web.controller.model.AcqPostTransactionModel;
import com.acq.web.controller.model.AcqSearchTransactionModel;
import com.acq.web.controller.model.AcqSendSmsModel;
import com.acq.web.controller.model.AcqTransactionDetailModel;
import com.acq.web.controller.model.AcqTransactionModel;
import com.acq.web.controller.model.AcqTransactionSignModel;
import com.acq.web.controller.model.AcqVoidTransactionModel;
import com.acq.web.controller.model.LastTxnDetailsModel;
import com.acq.web.dto.impl.DbDto;

public interface TransactionDaoInf {

	public DbDto<HashMap<String, HashMap<String, String>>> placeTransaction(AcqTransactionModel model, String clientIpAddress);

	public DbDto<Object> transactionSign(AcqTransactionSignModel model);

	public DbDto<HashMap<String, HashMap<String, String>>> searchTransaction(AcqSearchTransactionModel model);

	public DbDto<Object> voidtransaction(AcqVoidTransactionModel model);

	public DbDto<HashMap<String, HashMap<String, String>>> transactionDetail(
			AcqTransactionDetailModel model);

	public DbDto<HashMap<String, HashMap<String, String>>> placePostTransaction(
			AcqPostTransactionModel model, String clientIpAddress);

	public DbDto<HashMap<String, String>> getCardTxnDetails(String signImage);

	public DbDto<List<HashMap<String, String>>> checkBalance(String walletId);

	public DbDto<HashMap<String, HashMap<String, String>>> lastTxnDetails(
			LastTxnDetailsModel model, String clientIpAddress);

	public DbDto<HashMap<String, String>> sendCustomerSms(AcqSendSmsModel model);

}
