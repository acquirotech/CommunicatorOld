package com.acq.web.handler;

import java.util.HashMap;

import com.acq.web.controller.model.AcqTransactionHistryModel;
import com.acq.web.dto.impl.ServiceDto;

public interface HistoryHandlerInf {

	public ServiceDto<HashMap<String, HashMap<String, String>>> transactionHistry(
			AcqTransactionHistryModel model);	
	
}
