package com.acq.web.handler.impl;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.acq.EnumStatusConstant;
import com.acq.ImageUtils;
import com.acq.NumberValidator;
import com.acq.mail.AcqEmailServiceHandler;
import com.acq.web.controller.model.AcqOGSMainKeyExchangeModel;
import com.acq.web.controller.model.OGSMainKeyExModel;
import com.acq.web.controller.model.OGSMainLoginModel;
import com.acq.web.controller.model.OGSMainReversalTxnModel;
import com.acq.web.controller.model.OGSMainTxnCompleteModel;
import com.acq.web.controller.model.OGSMainTxnInitiateModel;
import com.acq.web.controller.model.OGSMainVoidTnxModel;
import com.acq.web.controller.model.OGSMainVoidTxnModel;
import com.acq.web.dao.OGSMainDaoInf;
import com.acq.web.dto.impl.DbDto;
import com.acq.web.dto.impl.ServiceDto;
import com.acq.web.handler.OGSMainHandlerInf;

import security.AcquiroSeq;

@Service
public class OGSMainHandler implements OGSMainHandlerInf{
	
	private static Logger logger = Logger.getLogger(OGSMainHandler.class);
	
	@Autowired
	OGSMainDaoInf vm20OGSDaoInf;
	
	@Value("#{acqConfig['apphost.location']}")
	private String appHost;
	
	@Value("#{acqConfig['signimage.location']}")
	private String signImgLocation;
	
	@Autowired
	AcqEmailServiceHandler acqEmailServiceHandler;
	
	@Override
	public ServiceDto<Object> getReversal(String txnId) {
		ServiceDto<Object>  response = new ServiceDto<Object>();
		try{
			DbDto<Object> daoResponse = vm20OGSDaoInf.getReversal(txnId);
			response.setMessage(daoResponse.getMessage());
			response.setStatus(daoResponse.getStatus());
		}catch(Exception e){
			logger.error("Error ogs get reversal txn hndlr "+e);
		}
		return response;
	}
	
	@Override
	public ServiceDto<Object> mv20OGSVoidTxnInit(OGSMainVoidTxnModel model) {
		ServiceDto<Object>  response = new ServiceDto<Object>();
		try{
			DbDto<Object> daoResponse = vm20OGSDaoInf.mv20OGSVoidTxnInit(model);
			response.setMessage(daoResponse.getMessage());
			response.setResult(daoResponse.getResult());
			response.setStatus(daoResponse.getStatus());
			logger.info("res from ogs vm20void txn hndlr");
		}catch(Exception e){
			logger.error("Error ogs vm20void txn hndlr "+e);
		}
		return response;
	}
	
	@Override
	public ServiceDto<Object> mv20OGSRvrslTxnInit(OGSMainReversalTxnModel model) {
		ServiceDto<Object>  response = new ServiceDto<Object>();
		try{
			DbDto<Object> daoResponse = vm20OGSDaoInf.mv20OGSRvrslTxnInit(model);
			response.setMessage(daoResponse.getMessage());
			response.setResult(daoResponse.getResult());
			response.setStatus(daoResponse.getStatus());
			logger.info("res from ogs vm20reversal txn hndlr");
		}catch(Exception e){
			logger.error("Error ogs vm20reversal txn hndlr "+e);
		}
		return response;
	}
	
	@Override
	public ServiceDto<Object> getDUKPTOgsMain(AcqOGSMainKeyExchangeModel model) {
		ServiceDto<Object>  response = new ServiceDto<Object>();
		try{
			DbDto<Object> daoResponse = vm20OGSDaoInf.getDUKPTOgsMain(model);
		    response.setMessage(daoResponse.getMessage());
		    response.setResult(daoResponse.getResult());
		    response.setStatus(daoResponse.getStatus());
		    logger.info("res from getdukpt ack hndlr");		 
		}catch(Exception e){
			logger.error("Error in getdukpt ack handler "+e);
			System.err.println("Error in getdukpt ack handler "+e);
		}
		return response;
	}

	@Override
	public ServiceDto<Object> mv20OGSMainGetDUKPTACK(
			AcqOGSMainKeyExchangeModel model) {
		ServiceDto<Object>  response = new ServiceDto<Object>();
		try{
			DbDto<Object> daoResponse = vm20OGSDaoInf.mv20OGSMainGetDUKPTACK(model);
		    response.setMessage(daoResponse.getMessage());
		    response.setResult(daoResponse.getResult());
		    response.setStatus(daoResponse.getStatus());
		    logger.info("res from getdukpt hndlr");		 
		}catch(Exception e){
			logger.error("Error in getdukpt handler "+e);
		}
		return response;
	}
	
	@Override
	public ServiceDto<Object> mv20OGSMainGetTMKACK(AcqOGSMainKeyExchangeModel model) {
		ServiceDto<Object>  response = new ServiceDto<Object>();
		try{
			DbDto<Object> daoResponse = vm20OGSDaoInf.mv20OGSMainGetTMKACK(model);
		    response.setMessage(daoResponse.getMessage());
		    response.setResult(daoResponse.getResult());
		    response.setStatus(daoResponse.getStatus());
		    logger.info("res from gettmk ack hndlr");		 
		}catch(Exception e){
			logger.error("Error in gettmk ack handler "+e);
		}
		return response;
	}
	
	@Override
	public ServiceDto<Object> getTMKOgsMain(AcqOGSMainKeyExchangeModel model) {
		ServiceDto<Object>  response = new ServiceDto<Object>();
		try{
			DbDto<Object> daoResponse = vm20OGSDaoInf.getTMKOgsMain(model);
		    response.setMessage(daoResponse.getMessage());
		    response.setResult(daoResponse.getResult());
		    response.setStatus(daoResponse.getStatus());
		    logger.info("res from gettmk hndlr");		 
		}catch(Exception e){
			logger.error("Error in gettmk handler "+e);
		}
		return response;
	}
	
	@Override
	public ServiceDto<Object> ogsVM20VoidTxnList(OGSMainVoidTnxModel model) {
		ServiceDto<Object>  response = new ServiceDto<Object>();
		try{
			DbDto<Object> daoResponse = vm20OGSDaoInf.ogsVM20VoidTxnList(model);
		    response.setMessage(daoResponse.getMessage());
		    response.setResult(daoResponse.getResult());
		    response.setStatus(daoResponse.getStatus());
		    logger.info("response returned from ogs void transaction list handler");		 
		}catch(Exception e){
			logger.error("Error in ogs void txn transaction initiate list handler "+e);
		}
		return response;
	}
	
	@Override
	public ServiceDto<Object> vm20DoPrchsWthCshBck(OGSMainTxnInitiateModel model) {
		ServiceDto<Object>  response = new ServiceDto<Object>();
		try{
			DbDto<Object> daoResponse = vm20OGSDaoInf.vm20DoPrchsWthCshBck(model);
		    response.setMessage(daoResponse.getMessage());
		    response.setResult(daoResponse.getResult());
		    response.setStatus(daoResponse.getStatus());
		    logger.info("res from vm20purchs wthcshbk hndlr");		 
		}catch(Exception e){
			logger.error("error in vm20purchs wthcshbk handler "+e);
		}
		return response;
	}
	
	@Override
	public ServiceDto<Object> doCashAtPosInitiate(OGSMainTxnInitiateModel model) {
		ServiceDto<Object>  response = new ServiceDto<Object>();
		try{
			DbDto<Object> daoResponse = vm20OGSDaoInf.doCashAtPosInitiate(model);
		    response.setMessage(daoResponse.getMessage());
		    response.setResult(daoResponse.getResult());
		    response.setStatus(daoResponse.getStatus());
		    logger.info("res from vm20 cashatpos inittxn hndlr");
		}catch(Exception e){
			logger.error("error in vm20 cashatpos inittxn handler "+e);
		}
		return response;
	}
	
	public ServiceDto<Object> vm20OgsLogin(OGSMainLoginModel model,
			String clientIpAddress){
		ServiceDto<Object>  response = new ServiceDto<Object>();
		try{
			DbDto<Object> daoResponse = vm20OGSDaoInf.vm20OgsLogin(model,clientIpAddress);
			response.setMessage(daoResponse.getMessage());
			response.setStatus(daoResponse.getStatus());	
			response.setResult(daoResponse.getResult());
		    logger.info("res from vm20login handler ver1");		 
		}catch(Exception e){
			logger.error("Error in Login handler "+e);
		}
		return response;
	}
	
	@Override
	public ServiceDto<Object> vm20OgsKeyExchange(OGSMainKeyExModel model) {
		ServiceDto<Object>  response = new ServiceDto<Object>();
		try{
			DbDto<Object> daoResponse = vm20OGSDaoInf.vm20OgsKeyExchange(model);
		    response.setMessage(daoResponse.getMessage());
		    response.setResult(daoResponse.getResult());
		    response.setStatus(daoResponse.getStatus());
		    logger.info("res from vm20keyex hndlr");		 
		}catch(Exception e){
			logger.error("Error in vm20keyex handler "+e);
		}
		return response;
	}

	@Override
	public ServiceDto<Object> mv20OGSTxnInitiate(OGSMainTxnInitiateModel model) {
		ServiceDto<Object>  response = new ServiceDto<Object>();
		try{
			DbDto<Object> daoResponse = vm20OGSDaoInf.mv20OGSTxnInitiate(model);
		    response.setMessage(daoResponse.getMessage());
		    response.setResult(daoResponse.getResult());
		    response.setStatus(daoResponse.getStatus());
		    logger.info("res from vm20inittxn hndlr");		 
		}catch(Exception e){
			logger.error("error in vm20inittxn handler "+e);
		}
		return response;
	}

	@Override
	public ServiceDto<Object> mv20OGSTxnComplete(OGSMainTxnCompleteModel model) {
		ServiceDto<Object>  response = new ServiceDto<Object>();
		DbDto<Object> daoResponse = vm20OGSDaoInf.mv20OGSTxnComplete(model);
		String location = signImgLocation + File.separator+model.getTransactionId()+".jpeg";
		try{
			List<String> list = (List<String>) daoResponse.getResult();
			System.out.println("list:"+list.get(9));
			if(list.size()>=5&&list.get(7).equals("0")&&NumberValidator.isPhoneNo(model.getMobileNumber())){					
				try{
					logger.info("Send sms and url shortner thread is starting");
					String transactionIds= ""+list.get(4);
					    if(list.get(9)!=null&&list.get(9)!=""&&!list.get(9).equalsIgnoreCase("CASHATPOS")){
						AcqSmsAndUrlShortnerThread thread = new AcqSmsAndUrlShortnerThread();
						thread.setTransactionId(transactionIds);
						thread.setAmount(list.get(0));
						thread.setTxnStatus(list.get(1));
						thread.setDateTime(list.get(2));
						thread.setCardPanNo(list.get(10));
						thread.setAppHost(appHost);
						thread.setMarketingName(list.get(5));
						thread.setRmn(list.get(6));
						thread.setCustPhone(model.getMobileNumber());
						thread.setTxnType("PLACE");
						Thread t = new Thread(thread);
						t.start();
						System.out.println("sms thread started");
						}
													
				}catch(Exception e){
					System.out.println("error to send sms and url shortner " +e);
					logger.error("error to send sms and url shortner " +e);
				}
			}if(list.size()>=5&&model.getEmailId()!=null&&model.getEmailId()!=""){							
				try{
					logger.info("Email sending");
					String transactionIds= ""+list.get(4);
					String txnId = AcquiroSeq.MakeSmart(transactionIds);
					byte[]   bytesEncoded = Base64.encodeBase64(txnId .getBytes());
					HashMap<String,Object> pageData= new HashMap<String,Object>();
					if(Integer.valueOf(list.get(1))==505){
						pageData.put("status", "debited");
					}else{
						pageData.put("status", "failed");
					}
					pageData.put("email", model.getEmailId());
					pageData.put("fname", "Customer");
					pageData.put("amount",  list.get(0));
					pageData.put("cardNo",  list.get(10));
					pageData.put("marketingName",  list.get(5));
					pageData.put("txnId", new String(bytesEncoded));
					pageData.put("datetime", list.get(2));
					pageData.put("acquirerCode", list.get(8));
					pageData.put("txnid", new String(bytesEncoded));
					 acqEmailServiceHandler.sendEmail(pageData,"/emailtemplate/cardtxn.html" , model.getEmailId());
					
						
				}catch(Exception e){
					logger.error("error to send email in card sing service handler "+e);
				}
			}
			try{
				if (ImageUtils.decode64AndSave(model.getSignImage(),location) == false){
					System.out.println("image is fales");
					response.setStatus(EnumStatusConstant.RollBackError.getId());
					response.setMessage(EnumStatusConstant.RollBackError.getDescription());
					logger.info("Error in transaction/sign service in TransactionHandler");				
				}
			}catch(Exception e){
				logger.error("error to save sign img "+e);
			}
		   response.setMessage(daoResponse.getMessage());
		   //response.setResult(daoResponse.getResult());
		   response.setStatus(daoResponse.getStatus());
		   logger.info("res from vm20cmplt txn hndlr");
		}catch(Exception e){
			logger.error("error in vm20cmplt txn handler "+e);
		}
		return response;
	}

	

	
}


	
