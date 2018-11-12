package com.acq.web.handler.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.acq.web.controller.model.AcqRechargeMerchantBalModel;
import com.acq.web.controller.model.AcqRechargeModel;
import com.acq.web.controller.model.AcqRechargeStatusModel;
import com.acq.web.dao.RechargeDaoInf;
import com.acq.web.dto.impl.DbDto2;
import com.acq.web.dto.impl.ServiceDto2;
import com.acq.web.handler.RechargeHandlerInf;

	@Service
	public class RechargeHandler implements RechargeHandlerInf{
		final static Logger logger = Logger.getLogger(RechargeHandler.class);
		
		
		@Autowired
		RechargeDaoInf rechargeDao;
		
		@Autowired
		MessageSource resource;
		
		@Override
		public ServiceDto2<Object> getOperators(AcqRechargeMerchantBalModel model) {
			ServiceDto2<Object> response = new ServiceDto2<Object>();
			try{
				DbDto2<Object>  daoResponse = rechargeDao.getOperators(model);
				response.setStatusCode(daoResponse.getStatusCode());
			    response.setBody(daoResponse.getBody());
			    response.setStatusMessage(daoResponse.getStatusMessage());
			    logger.info("response from get Operator handler");		 
			}catch(Exception e){
				logger.error("Error in get Operator handler "+e);
			}
			return response;
		}
		
		
		@Override
		public ServiceDto2<Object> doRecharge(AcqRechargeModel model) {
			ServiceDto2<Object>  response = new ServiceDto2<Object>();
			try{
				DbDto2<Object>  daoResponse = rechargeDao.doRecharge(model);
				response.setStatusCode(daoResponse.getStatusCode());
			    response.setBody(daoResponse.getBody());
			    response.setStatusMessage(daoResponse.getStatusMessage());
			    //logger.info(resource.getStatusMessage("return.response",new String[] { " from handler" }, Locale.ENGLISH));		 
			}catch(Exception e){
				logger.error("Error in doRecharge"+e);
			}
			return response;
		}
		
		@Override
		public ServiceDto2<Object> getMerchantBalance(AcqRechargeMerchantBalModel model) {
			ServiceDto2<Object>  response = new ServiceDto2<Object>();
			try{
				DbDto2<Object>  daoResponse = rechargeDao.getMerchantBalance(model);
				response.setStatusCode(daoResponse.getStatusCode());
			    response.setBody(daoResponse.getBody());
			    response.setStatusMessage(daoResponse.getStatusMessage());
			}catch(Exception e){
				logger.error("Error in Merchant Bal Request"+e);
			}
			return response;
		}
		
		@Override
		public ServiceDto2<Object> getRechargeStatus(AcqRechargeStatusModel model) {
			ServiceDto2<Object>  response = new ServiceDto2<Object>();
			try{
				DbDto2<Object>  daoResponse = rechargeDao.getRechargeStatus(model);
			    response.setStatusCode(daoResponse.getStatusCode());
			    response.setBody(daoResponse.getBody());
			    response.setStatusMessage(daoResponse.getStatusMessage());
			   // logger.info(resource.getStatusMessage("return.response",new String[] { " from handler" }, Locale.ENGLISH));		 
			}catch(Exception e){
				logger.error("Error in Merchant Bal Request"+e);
			}
			return response;
		}
		@Override
		public ServiceDto2<Object> getRechargeList(AcqRechargeMerchantBalModel model) {
			ServiceDto2<Object>  response = new ServiceDto2<Object>();
			try{
				DbDto2<Object>  daoResponse = rechargeDao.getRechargeList(model);
				response.setStatusCode(daoResponse.getStatusCode());
			    response.setBody(daoResponse.getBody());
			    response.setStatusMessage(daoResponse.getStatusMessage());
			   // logger.info(resource.getStatusMessage("return.response",new String[] { " from handler" }, Locale.ENGLISH));		 
			}catch(Exception e){
				logger.error("Error in get RechargeList Request"+e);
			}
			return response;
		}
		
}
