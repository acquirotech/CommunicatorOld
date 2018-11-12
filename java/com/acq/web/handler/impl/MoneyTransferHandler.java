package com.acq.web.handler.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.acq.EnumStatusConstant;
import com.acq.web.controller.model.AcqBankItCardLoadModel;
import com.acq.web.controller.model.AcqBankItTransactionListModel;
import com.acq.web.controller.model.AcqMoneyTransferAddBeneficiaryModel;
import com.acq.web.controller.model.AcqMoneyTransferAddSenderModel;
import com.acq.web.controller.model.AcqMoneyTransferBankListModel;
import com.acq.web.controller.model.AcqMoneyTransferCheckStatusModel;
import com.acq.web.controller.model.AcqMoneyTransferNeftModel;
import com.acq.web.controller.model.AcqMoneyTransferOtpModel;
import com.acq.web.dao.MoneyTransferDaoInf;
import com.acq.web.dao.RechargeDaoInf;
import com.acq.web.dto.impl.DbDto2;
import com.acq.web.dto.impl.ServiceDto2;
import com.acq.web.handler.MoneyTransferHandlerInf;

	@Service
	public class MoneyTransferHandler implements MoneyTransferHandlerInf{
		final static Logger logger = Logger.getLogger(MoneyTransferHandler.class);
		
		@Autowired
		MoneyTransferDaoInf moneyDao;
		
		@Autowired
		RechargeDaoInf rechargeDao;
		
		@Autowired
		MessageSource resource;
		
		@Override
		public ServiceDto2<Object> getTransactionList(AcqBankItTransactionListModel model) {
			ServiceDto2<Object> response = new ServiceDto2<Object>();
			try{
				DbDto2<Object>  daoResponse = moneyDao.getTransactionList(model);
				
				response.setStatusCode(daoResponse.getStatusCode());
			    response.setBody(daoResponse.getBody());
			    response.setStatusMessage(daoResponse.getStatusMessage());
			    logger.info("response from get dmt getTransactionList handler");		 
			}catch(Exception e){
				logger.error("Error in get dmt getTransactionList handler "+e);
			}
			return response;
		}
		
		
		@Override
		public ServiceDto2<Object> addBeneficiary(AcqMoneyTransferAddBeneficiaryModel model) {
			ServiceDto2<Object> response = new ServiceDto2<Object>();
			try{
				DbDto2<Object>  daoResponse = moneyDao.addBeneficiary(model);
				
				response.setStatusCode(daoResponse.getStatusCode());
			    response.setBody(daoResponse.getBody());
			    response.setStatusMessage(daoResponse.getStatusMessage());
			    logger.info("response from get dmt addBeneficiary handler");		 
			}catch(Exception e){
				logger.error("Error in get dmt addBeneficiary handler "+e);
			}
			return response;
		}
		
		@Override
		public ServiceDto2<Object> dmtgenerateOtp(AcqMoneyTransferOtpModel model) {
			ServiceDto2<Object> response = new ServiceDto2<Object>();
			try{
				DbDto2<Object>  daoResponse = moneyDao.dmtgenerateOtp(model);
				
				response.setStatusCode(daoResponse.getStatusCode());
			    response.setBody(daoResponse.getBody());
			    response.setStatusMessage(daoResponse.getStatusMessage());
			    logger.info("response from get dmt generateOtp handler");		 
			}catch(Exception e){
				logger.error("Error in get dmt generateOtp handler "+e);
			}
			return response;
		}
		
		
		@Override
		public ServiceDto2<Object> addSender(AcqMoneyTransferAddSenderModel model) {
			ServiceDto2<Object>  response = new ServiceDto2<Object>();
			try{
				DbDto2<Object>  daoResponse = moneyDao.addSender(model);
				response.setStatusCode(daoResponse.getStatusCode());
			    response.setBody(daoResponse.getBody());
			    response.setStatusMessage(daoResponse.getStatusMessage());
			    //logger.info(resource.getStatusMessage("return.response",new String[] { " from handler" }, Locale.ENGLISH));		 
			}catch(Exception e){
				response.setStatusCode(EnumStatusConstant.RollBackError.getId());
				response.setStatusMessage(EnumStatusConstant.RollBackError
						.getDescription());
				logger.error("Error in add Sender"+e);
			}
			return response;
		}
		
		@Override
		public ServiceDto2<Object> fetchBeneficiaryList(AcqMoneyTransferOtpModel model) {
			ServiceDto2<Object>  response = new ServiceDto2<Object>();
			try{
				DbDto2<Object>  daoResponse = moneyDao.fetchBeneficiaryList(model);
				response.setStatusCode(daoResponse.getStatusCode());
			     response.setStatusMessage(daoResponse.getStatusMessage());
			     response.setBody(daoResponse.getBody());
				   
			}catch(Exception e){
				response.setStatusCode(EnumStatusConstant.RollBackError.getId());
				response.setStatusMessage(EnumStatusConstant.RollBackError
						.getDescription());
				logger.error("Error in Fetch Customer Request"+e);
			}
			return response;
		}
		@Override
		public ServiceDto2<Object> dmtNeft(AcqMoneyTransferNeftModel model) {
			ServiceDto2<Object>  response = new ServiceDto2<Object>();
			try{
				DbDto2<Object>  daoResponse = moneyDao.dmtNeft(model);
				response.setStatusCode(daoResponse.getStatusCode());
			     response.setStatusMessage(daoResponse.getStatusMessage());
			     response.setBody(daoResponse.getBody());
				   
			}catch(Exception e){
				response.setStatusCode(EnumStatusConstant.RollBackError.getId());
				response.setStatusMessage(EnumStatusConstant.RollBackError
						.getDescription());
				logger.error("Error in dmt neft Request"+e);
			}
			return response;
		}
		@Override
		public ServiceDto2<Object> dmtImps(AcqMoneyTransferNeftModel model) {
			ServiceDto2<Object>  response = new ServiceDto2<Object>();
			try{
				DbDto2<Object>  daoResponse = moneyDao.dmtImps(model);
				response.setStatusCode(daoResponse.getStatusCode());
			     response.setStatusMessage(daoResponse.getStatusMessage());
			     response.setBody(daoResponse.getBody());
				   
			}catch(Exception e){
				response.setStatusCode(EnumStatusConstant.RollBackError.getId());
				response.setStatusMessage(EnumStatusConstant.RollBackError
						.getDescription());
				logger.error("Error in dmt imps Request"+e);
			}
			return response;
		}
		@Override
		public ServiceDto2<Object> dmtDeleteBeneficiary(AcqMoneyTransferNeftModel model) {
			ServiceDto2<Object>  response = new ServiceDto2<Object>();
			try{
				DbDto2<Object>  daoResponse = moneyDao.dmtDeleteBeneficiary(model);
				response.setStatusCode(daoResponse.getStatusCode());
			     response.setStatusMessage(daoResponse.getStatusMessage());
			     response.setBody(daoResponse.getBody());
				   
			}catch(Exception e){
				response.setStatusCode(EnumStatusConstant.RollBackError.getId());
				response.setStatusMessage(EnumStatusConstant.RollBackError
						.getDescription());
				logger.error("Error in delete dmt beneficiary Request"+e);
			}
			return response;
		}
		@Override
		public ServiceDto2<Object> checkStatus(AcqMoneyTransferCheckStatusModel model) {
			ServiceDto2<Object>  response = new ServiceDto2<Object>();
			try{
				DbDto2<Object>  daoResponse = moneyDao.checkStatus(model);
			    response.setStatusCode(daoResponse.getStatusCode());
			    response.setStatusMessage(daoResponse.getStatusMessage());
			    response.setBody(daoResponse.getBody());
			}catch(Exception e){
				logger.error("Error in dmt check Response"+e);
			}
			return response;
		}
		@Override
		public ServiceDto2<Object> getBankList(AcqMoneyTransferBankListModel model) {
			ServiceDto2<Object>  response = new ServiceDto2<Object>();
			try{
				DbDto2<Object>  daoResponse = moneyDao.bankList(model);
				response.setStatusCode(daoResponse.getStatusCode());
			    response.setBody(daoResponse.getBody());
			    response.setStatusMessage(daoResponse.getStatusMessage());
			}catch(Exception e){
				logger.error("Error in get bankList Request"+e);
			}
			return response;
		}


		@Override
		public ServiceDto2<Object> cardLoad(AcqBankItCardLoadModel model) {
			ServiceDto2<Object> response = new ServiceDto2<Object>();
			try{
				DbDto2<Object>  daoResponse = moneyDao.cardLoad(model);
				
				response.setStatusCode(daoResponse.getStatusCode());
			    response.setBody(daoResponse.getBody());
			    response.setStatusMessage(daoResponse.getStatusMessage());
			    logger.info("response from get dmt cardLoad handler");		 
			}catch(Exception e){
				logger.error("Error in get dmt cardLoad handler "+e);
			}
			return response;
		}
		@Override
		public ServiceDto2<Object> cardLoadStatus(AcqBankItCardLoadModel model) {
			ServiceDto2<Object> response = new ServiceDto2<Object>();
			try{
				DbDto2<Object>  daoResponse = moneyDao.cardLoadStatus(model);
				
				response.setStatusCode(daoResponse.getStatusCode());
			    response.setBody(daoResponse.getBody());
			    response.setStatusMessage(daoResponse.getStatusMessage());
			    logger.info("response from get dmt cardLoadStatus handler");		 
			}catch(Exception e){
				logger.error("Error in get dmt cardLoadStatus handler "+e);
			}
			return response;
		}
		
		@Override
		public ServiceDto2<Object> getCardBal(AcqBankItCardLoadModel model) {
			ServiceDto2<Object> response = new ServiceDto2<Object>();
			try{
				DbDto2<Object>  daoResponse = moneyDao.getCardBal(model);
				
				response.setStatusCode(daoResponse.getStatusCode());
			    response.setBody(daoResponse.getBody());
			    response.setStatusMessage(daoResponse.getStatusMessage());
			    logger.info("response from get dmt getCardBal handler");		 
			}catch(Exception e){
				logger.error("Error in get dmt getCardBal handler "+e);
			}
			return response;
		}
}
