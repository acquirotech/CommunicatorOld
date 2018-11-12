package com.acq.web.handler;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import org.springframework.http.HttpEntity;

import com.acq.web.controller.model.AcqPostTransactionModel;
import com.acq.web.controller.model.AcqSearchTransactionModel;
import com.acq.web.controller.model.AcqSendSmsModel;
import com.acq.web.controller.model.AcqTransactionDetailModel;
import com.acq.web.controller.model.AcqTransactionModel;
import com.acq.web.controller.model.AcqTransactionSignModel;
import com.acq.web.controller.model.AcqVoidTransactionModel;
import com.acq.web.controller.model.LastTxnDetailsModel;
import com.acq.web.dto.impl.ServiceDto;

public interface TransactionHandlerInf {

	public ServiceDto<HashMap<String, HashMap<String, String>>> placeTransaction(AcqTransactionModel model, String clientIpAddress);
	public ServiceDto<Object> transactionSign(AcqTransactionSignModel model);

	public ServiceDto<HashMap<String, HashMap<String, String>>> searchTransaction(
			AcqSearchTransactionModel model);
	
	public ServiceDto<Object> voidtransaction(
			AcqVoidTransactionModel model);
	public ServiceDto<HashMap<String, HashMap<String, String>>> transactionDetail(
			AcqTransactionDetailModel model);
	public ServiceDto<HashMap<String, HashMap<String, String>>> placePostTransaction(
			AcqPostTransactionModel model, String clientIpAddress);
	public HttpEntity<byte[]> signImageS(String signImage);
	public ServiceDto<HashMap<String, String>> getCardTxnDetails(
			String signImage);
	public ServiceDto<List<HashMap<String, String>>> checkBalance(
			String walletid);
	
	public ServiceDto<HashMap<String, HashMap<String, String>>> lastTxnDetails(
			LastTxnDetailsModel model, String clientIpAddress);
	public ServiceDto<Object> sendSms(AcqSendSmsModel model);
	public InputStream txnsignImageS(String signImage);
//	public ServiceDto<Object> placeTxn(EzOgsTransactionModel model);
	
	
}
