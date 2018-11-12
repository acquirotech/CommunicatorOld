package com.acq.web.handler.impl;

import java.util.Map;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.acq.mail.AcqEmailServiceHandler;
import com.acq.web.controller.model.AcqD200ApiLogin;
import com.acq.web.controller.model.AcqOgsLoginModel;
import com.acq.web.controller.model.OGSMainLoginModel;
import com.acq.web.dao.LoginDaoInf;
import com.acq.web.dto.impl.DbDto;
import com.acq.web.dto.impl.DbDto2;
import com.acq.web.dto.impl.ServiceDto;
import com.acq.web.dto.impl.ServiceDto2;
import com.acq.web.handler.LoginHandlerInf;

@Service
public class LoginHandler implements LoginHandlerInf{
	
	private static Logger logger = Logger.getLogger(LoginHandler.class);
	
	@Autowired
	LoginDaoInf loginDao;
	
	@Autowired
	MessageSource resource;
	
	
	@Autowired
	AcqEmailServiceHandler acqEmailServiceHandler;
	
	

	@Value("#{acqConfig['apphost.location']}")
   	private String appHost;
	public String getAppHost() {
		return appHost;
	}
	


	public ServiceDto2<Object> getLoginD200(AcqD200ApiLogin model,
			String clientIpAddress){
		ServiceDto2<Object>  response = new ServiceDto2<Object>();
		try{
			DbDto2<Object> daoResponse = loginDao.getLoginD200(model,clientIpAddress);
			Map<String,String> responseMap = (Map<String, String>) daoResponse.getBody();			
			logger.info("response returned for dao");
			if(responseMap.isEmpty()){
				logger.warn("result is empty in Login d200 version1 handler");
				response.setStatusMessage(daoResponse.getStatusMessage());
			    response.setStatusCode(daoResponse.getStatusCode());
			    response.setBody(null);
			}else{
				logger.info("user found, result not empty");
				JSONObject json = new JSONObject();
				json.put("sessionId", responseMap.get("sessionId"));
				json.put("TID", responseMap.get("banktid"));
				json.put("MID", responseMap.get("merchantBankMID"));
				json.put("keyExchange", "false");
				response.setStatusMessage(daoResponse.getStatusMessage());
				response.setStatusCode(daoResponse.getStatusCode());
				response.setBody(json);
				logger.info("responseString: "+json);
			}	
		
		    logger.info("response returned from login tpos version1");//sage("return.response",new String[] { " from handler" }, Locale.ENGLISH));		 
		}catch(Exception e){
			logger.error("Error in Login tpos handler "+e);
		}
		return response;
	}
	
	
	public ServiceDto<Object> getLoginD200(OGSMainLoginModel model,
			String clientIpAddress){
		ServiceDto<Object>  response = new ServiceDto<Object>();
		try{
			DbDto<Object> daoResponse = loginDao.getLoginD200(model,clientIpAddress);
			Map<String,String> responseMap = (Map<String, String>) daoResponse.getResult();			
			logger.info("res returned for dao");
			if(responseMap.isEmpty()){
				logger.warn("result is empty in de200 ver1 hndlr");
				response.setMessage(daoResponse.getMessage());
			    response.setStatus(daoResponse.getStatus());
			    response.setResult(null);
			}else{
				logger.info("user found, result not empty");
				JSONObject resJson = new JSONObject();
				if(responseMap.get("maintenanceMode").equals("0")&&responseMap.get("switchType").equals("AGS")){
					resJson.put("sessionId", responseMap.get("sessionId"));
					resJson.put("TID", responseMap.get("banktid"));
					resJson.put("MID", responseMap.get("merchantBankMID"));
									
				}else if(responseMap.get("maintenanceMode").equals("0")&&responseMap.get("switchType").equals("OGS")){
					resJson.put("sessionId", responseMap.get("sessionId"));
					resJson.put("TID", responseMap.get("banktid"));
					resJson.put("MID", responseMap.get("merchantBankMID"));
									
				}else if(responseMap.get("maintenanceReason").equals("mismatch")){
					resJson.put("appUpdate", "1");
				}else{
					resJson.put("maintenanceMode", responseMap.get("maintenanceReason"));
				}
				
				response.setMessage(daoResponse.getMessage());
				response.setStatus(daoResponse.getStatus());
				response.setResult(resJson);
			}
		    logger.info("res frm d200 login hndlr ver1");//sage("return.response",new String[] { " from handler" }, Locale.ENGLISH));		 
		}catch(Exception e){
			logger.error("Error in login d200 hndlr "+e);
		}
		return response;
	}
	@Override
	public ServiceDto<Object> getLoginVersion6(AcqOgsLoginModel model,
			String clientIpAddress){
		ServiceDto<Object>  response = new ServiceDto<Object>();
		try{
			DbDto<Object> daoResponse = loginDao.getLoginVersion6(model,clientIpAddress);
			Map<String,String> responseMap = (Map<String, String>) daoResponse.getResult();			
			logger.info("response returned for dao");
			if(responseMap.isEmpty()){
				logger.warn("result is empty in Login version5 handler");
				response.setMessage(daoResponse.getMessage());
			    response.setStatus(daoResponse.getStatus());
			    response.setResult(null);
			}else{
				logger.info("user found, result not empty");
				JSONObject resJson = new JSONObject();				
				if(responseMap.get("maintenanceMode").equals("0")&&responseMap.get("switchType").equals("AGS")){
					resJson.put("keyExchange",responseMap.get("keyExchange"));
					resJson.put("sessionId", responseMap.get("sessionId"));
					resJson.put("TID", responseMap.get("banktid"));
					resJson.put("MID", responseMap.get("merchantBankMID"));
					resJson.put("firstLogin", responseMap.get("firstLogin"));
					resJson.put("userStatus", responseMap.get("userStatus"));
					resJson.put("loyaltyStatus", responseMap.get("loyaltyStatus"));
					resJson.put("walletStatus", responseMap.get("walletStatus"));
					resJson.put("accountType", responseMap.get("accountType"));
					resJson.put("switchType", responseMap.get("switchType"));					
				}else if(responseMap.get("maintenanceMode").equals("0")&&responseMap.get("switchType").equals("OGS")){
					resJson.put("keyExchange",responseMap.get("keyExchange"));
					resJson.put("sessionId", responseMap.get("sessionId"));
					resJson.put("TID", responseMap.get("banktid"));
					resJson.put("MID", responseMap.get("merchantBankMID"));
					resJson.put("firstLogin", responseMap.get("firstLogin"));
					resJson.put("userStatus", responseMap.get("userStatus"));
					resJson.put("loyaltyStatus", responseMap.get("loyaltyStatus"));
					resJson.put("walletStatus", responseMap.get("walletStatus"));
					resJson.put("accountType", responseMap.get("accountType"));
					resJson.put("switchType", responseMap.get("switchType"));					
				}else if(responseMap.get("maintenanceReason").equals("mismatch")){
					resJson.put("appUpdate", "1");
				}else{
					resJson.put("maintenanceMode", responseMap.get("maintenanceReason"));
				}		
				response.setMessage(daoResponse.getMessage());
				response.setStatus(daoResponse.getStatus());
				response.setResult(resJson);
				logger.info("responseString: "+resJson);
			}		   
		    logger.info("response returned from login handler version5");//sage("return.response",new String[] { " from handler" }, Locale.ENGLISH));		 
		}catch(Exception e){
			logger.error("Error in Login handler "+e);
		}
		return response;
	}
	

	
	
}


	
