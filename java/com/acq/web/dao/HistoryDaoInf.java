package com.acq.web.dao;

import java.util.HashMap;

import com.acq.web.controller.model.AcqTransactionHistryModel;
import com.acq.web.dto.impl.DbDto;

public interface HistoryDaoInf {

	public DbDto<HashMap<String, HashMap<String, String>>> transactionHistry(
			AcqTransactionHistryModel model);

}
