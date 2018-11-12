package com.acq.web.handler.impl;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.acq.web.controller.model.AcqTransactionHistryModel;
import com.acq.web.dao.HistoryDaoInf;
import com.acq.web.dto.impl.DbDto;
import com.acq.web.dto.impl.ServiceDto;
import com.acq.web.handler.HistoryHandlerInf;

	@Service
	public class HistoryHandler implements HistoryHandlerInf{
		final static Logger logger = Logger.getLogger(HistoryHandler.class);
		
		@Autowired
		HistoryDaoInf historyDao;
		
		@Autowired
		MessageSource resource;
		
		@Override
		public ServiceDto<HashMap<String, HashMap<String, String>>> transactionHistry(
				AcqTransactionHistryModel model) {
			ServiceDto<HashMap<String,HashMap<String, String>>>  response = new ServiceDto<HashMap<String,HashMap<String, String>>>();
			try{
				DbDto<HashMap<String, HashMap<String, String>>>  daoResponse = historyDao.transactionHistry(model);
			    response.setMessage(daoResponse.getMessage());
			    response.setResult(daoResponse.getResult());
			    response.setStatus(daoResponse.getStatus());
			  //  logger.info(resource.getMessage("return.response",new String[] { " from handler" }, Locale.ENGLISH));		 
			}catch(Exception e){
				logger.error("Error in transaction history handlder "+e);
			}
			return response;
		}	
}
