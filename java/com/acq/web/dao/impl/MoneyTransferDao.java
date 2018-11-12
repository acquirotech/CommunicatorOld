package com.acq.web.dao.impl;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Repository;

import com.acq.EnumStatusConstant;
import com.acq.MoneyTransferApis;
import com.acq.OtpGenerator;
import com.acq.RechargeApis;
import com.acq.users.entity.AcqBankItEntity;
import com.acq.users.entity.AcqBankItTransactionEntity;
import com.acq.users.entity.AcqMapDeviceUserEntity;
import com.acq.users.entity.AcqOrganization;
import com.acq.users.entity.AcqUser;
import com.acq.web.controller.model.AcqBankItCardLoadModel;
import com.acq.web.controller.model.AcqBankItTransactionListModel;
import com.acq.web.controller.model.AcqBankItTransactionModel;
import com.acq.web.controller.model.AcqMoneyTransferAddBeneficiaryModel;
import com.acq.web.controller.model.AcqMoneyTransferAddSenderModel;
import com.acq.web.controller.model.AcqMoneyTransferBankListModel;
import com.acq.web.controller.model.AcqMoneyTransferCheckStatusModel;
import com.acq.web.controller.model.AcqMoneyTransferNeftModel;
import com.acq.web.controller.model.AcqMoneyTransferOtpModel;
import com.acq.web.dao.AcqDao;
import com.acq.web.dao.MoneyTransferDaoInf;
import com.acq.web.dto.impl.DbDto2;

	@Repository
	public class MoneyTransferDao extends AcqDao implements MoneyTransferDaoInf {
		
		final static Logger logger = Logger.getLogger(MoneyTransferDao.class);
		RechargeApis rechargeApi = new RechargeApis();

		@Override
		public DbDto2<Object> getTransactionList(AcqBankItTransactionListModel model) {
			logger.info("request in get getTransactionList Request dao");
			DbDto2<Object> response = new DbDto2<Object>();
			Session session = null;
			try {
				session = getNewSession();
				List<AcqBankItTransactionEntity> lstTxn = null;
				JSONObject jsonObj = new JSONObject();
				SimpleDateFormat sdfDestination = new SimpleDateFormat("YYYY-MM-dd");
				Criteria criteria = session.createCriteria(AcqBankItTransactionEntity.class).addOrder(Order.desc("id")).add(Restrictions.eq("sessionId",model.getSessionId()));
			if(model.getFromDate()!=null&&model.getFromDate()!=""&&model.getFromDate().length()>1&&(model.getToDate()==null||model.getToDate()=="")){
				SimpleDateFormat sdfSource = new SimpleDateFormat("ddMMyyyy");
				Date fdate =sdfSource.parse(model.getFromDate());
				String frmDate = sdfDestination.format(fdate);
				Date date = new Date();
				String todate = sdfDestination.format(date);
				String fromDate= frmDate + " 00:00:00";
				String toDate= todate + " 23:59:59";	
				System.out.println("222222222::::"+fromDate);
				System.out.println("3333333333::::"+toDate);
				criteria.add(Restrictions.between("dateTime",fromDate, toDate));
				//lstTxn = (List<AcqBankItTransactionEntity>) session.createCriteria(AcqBankItTransactionEntity.class).addOrder(Order.desc("id")).add(Restrictions.eq("sessionId",model.getSessionId())).add(Restrictions.between("dateTime",fromDate, toDate)).list();
			}else if(model.getFromDate()!=null&&model.getFromDate()!=""&&model.getToDate()!=null&&model.getToDate()!=""){
				SimpleDateFormat sdfSource = new SimpleDateFormat("ddMMyyyy");
				Date fdate =sdfSource.parse(model.getFromDate());
				String frmDate = sdfDestination.format(fdate);
				Date date = sdfSource.parse(model.getToDate());
				String todate = sdfDestination.format(date);
				String fromDate= frmDate + " 00:00:00";
				String toDate= todate + " 23:59:59";	
				System.out.println("222222222::::"+fromDate);
				System.out.println("3333333333::::"+toDate);
				criteria.add(Restrictions.between("dateTime",fromDate, toDate));
			}else if(model.getToDate()!=null&&model.getToDate()!=""&&(model.getFromDate()==null||model.getFromDate()!="")){
				SimpleDateFormat sdfSource = new SimpleDateFormat("ddMMyyyy");
				Date fdate =sdfSource.parse(model.getFromDate());
				String frmDate = sdfDestination.format(fdate);
				Date date = sdfSource.parse(model.getToDate());
				String todate = sdfDestination.format(date);
				String fromDate= frmDate + " 00:00:00";
				String toDate= todate + " 23:59:59";	
				criteria.add(Restrictions.between("dateTime",fromDate, toDate));
			}else if(!model.getTxnStatus().equalsIgnoreCase("1")){
					Date date = new Date();
					String frmDate = sdfDestination.format(date);
					String fromDate= frmDate + " 00:00:00";
					String toDate= frmDate + " 23:59:59";	
					System.out.println("5555555::::"+fromDate);
					System.out.println("66666666666::::"+toDate);
					criteria.add(Restrictions.between("dateTime",fromDate, toDate));		
			}else{
				
			}			
			if(model.getCustomerId()!=null&&model.getCustomerId()!=""){
				criteria.add(Restrictions.eq("customerId",model.getCustomerId()));
			}if(model.getTxnStatus().equalsIgnoreCase("0")){
				criteria.add(Restrictions.eq("status","00"));
			}else if(model.getTxnStatus().equalsIgnoreCase("1")){
				criteria.add(Restrictions.eq("status","01"));
			}else if(model.getTxnStatus().equalsIgnoreCase("2")){
				criteria.add(Restrictions.eq("status","02"));
			}
			List<AcqBankItTransactionEntity> txList = criteria.list();
			Iterator<AcqBankItTransactionEntity> lstTxnItr = null;
			try{
				 lstTxnItr = txList.iterator();
			}catch(Exception e){
				logger.info("eeeeeeeeeeeee:::::"+e);
			}
				try{
				}catch(Exception e){
					logger.info("eeeeeeeeeeee::"+e);
				}
				if (txList.isEmpty()) {
					response.setStatusCode(EnumStatusConstant.NotFound.getId());
					response.setStatusMessage(" Txn Not Found");
					response.setBody(null);
					logger.info("Successfully selected records in RechargeList");
				} else {
					JSONArray jsonArray =  jsonArray = new JSONArray();
					JSONObject list = new JSONObject();
					while (lstTxnItr.hasNext()) {
						AcqBankItTransactionEntity sngleTxn = (AcqBankItTransactionEntity) lstTxnItr.next();
						list.put("id", sngleTxn.getId());
						list.put("customerId", sngleTxn.getCustomerId());
						list.put("clientRefId", sngleTxn.getClientRefId());
						list.put("ifsc", sngleTxn.getIfsc());
						list.put("accountNo", sngleTxn.getAccountNo());
						list.put("amount", sngleTxn.getAmount());
						list.put("dateTime", sngleTxn.getDateTime());
						list.put("bankName", sngleTxn.getBankName());
						list.put("mobileNo", sngleTxn.getMobileNo());
						if(sngleTxn.getStatus().equals("00")){
							list.put("status", "Success");	
						}else if(sngleTxn.getStatus().equals("01")){
							list.put("status", "Pending");
						}else if(sngleTxn.getStatus().equals("02")){
							list.put("status", "Failed");
						}else{
							list.put("status", sngleTxn.getStatus());
						}
						
						jsonArray.put(list);					
					}
					jsonObj.put("transferList", jsonArray);
					response.setStatusCode(EnumStatusConstant.OK.getId());
					response.setStatusMessage(EnumStatusConstant.OK.getDescription());
					response.setBody(jsonObj);
					logger.info("Successfully selected records in getTransactionList");			
				}
				
				}catch (Exception e) {
					response.setStatusCode(EnumStatusConstant.RollBackError.getId());
					response.setStatusMessage(EnumStatusConstant.RollBackError.getDescription());
					logger.info("Error to get getTransactionList Request Dao " + e);
				}			
		finally{
				session.close();			
			}
			return response;
		}
		
		
		public DbDto2<Object> persistBankItTransferApi(AcqBankItTransactionModel model) {
			DbDto2<Object> response = new DbDto2<Object>();
			HashMap<String, HashMap<String,String>> responseMap= new HashMap<String, HashMap<String,String>>();
			Session session = null;
			try{		
				System.out.println("Request for persist data::"+model.getSessionId());
				session = getNewSession();
				Transaction tx = null;
				Date date = new Date();
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String dateTime = format.format(date);
				HashMap<String, String> txnDetailMap = null;
				AcqUser orgIdEnt = (AcqUser)session.createCriteria(AcqUser.class).add(Restrictions.eq("id", Long.valueOf(model.getSessionId()))).uniqueResult();
				if(orgIdEnt!=null&&orgIdEnt+""!=""){
				AcqOrganization merchantIdEnt = (AcqOrganization)session.createCriteria(AcqOrganization.class).add(Restrictions.eq("id", Long.valueOf(orgIdEnt.getOrgId()))).uniqueResult();
				if(merchantIdEnt!=null&&merchantIdEnt+""!="")
					{
					JSONObject json1 = new JSONObject();
					/*MoneyTransferApis moneyApis = new MoneyTransferApis();
					json1= moneyApis.callFetchSingleRecipient(model.getRecipientId(),model.getCustomerId());
					JSONObject json = new JSONObject();
					json = (JSONObject) json1.get("data");
					System.out.println("bbbbbbbbbbbbbbbbbb"+json.get("udf2"));
					model.setIfsc(""+json.get("udf2"));
					model.setAccountNo(""+json.get("udf1"));
					model.setRecipientName(""+json.get("recipientName"));*/
					AcqBankItTransactionEntity entity = new AcqBankItTransactionEntity();
						entity.setMerchantId(""+merchantIdEnt.getMerchantId());
						entity.setSessionId(model.getSessionId());
						entity.setAccountNo(model.getAccountNo());
						entity.setAmount(""+model.getAmount());
						entity.setClientRefId(model.getClientRefId());
						entity.setCustomerId(model.getCustomerId());
						entity.setIfsc(model.getIfsc());
						entity.setMerchantId(""+merchantIdEnt.getMerchantId());
						entity.setRecipientId(model.getRecipientId());
						entity.setRecipientName(model.getRecipientName());
						entity.setStatus(model.getStatus());
						entity.setTransactionType(model.getTransactionType());
						entity.setTxnId(model.getTxnId());
						entity.setDateTime(dateTime);
						entity.setMessage(model.getMessage());
						entity.setBankName(model.getBankName());
						entity.setMobileNo(model.getMobileNo());
						tx = session.beginTransaction();
						session.save(entity);
						logger.info("Details Added In BankIt Table");				
						response.setStatusCode(EnumStatusConstant.OK.getId());
						response.setStatusMessage(EnumStatusConstant.OK.getDescription());
						logger.info("BankIt Details Added Succesfuly");
						tx.commit();					
			}}}catch(Exception e){		
				response.setStatusCode(EnumStatusConstant.RollBackError.getId());
				response.setStatusMessage(EnumStatusConstant.RollBackError.getDescription());
				logger.info("Erore in BankIt " +e);
				}finally{
				session.close();			
			}
			return response;
		}		

		
		
		public DbDto2<Object> persistBankItApi(String serviceName,String sessionId,String request, JSONObject bankItResponse, String requestDateTime,String responseDateTime) {
			DbDto2<Object> response = new DbDto2<Object>();
			HashMap<String, HashMap<String,String>> responseMap= new HashMap<String, HashMap<String,String>>();
			Session session = null;
			try{		
				System.out.println("Request for persist data");
				session = getNewSession();
				Transaction tx = null;
				Date date = new Date();
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String dateTime = format.format(date);
				HashMap<String, String> txnDetailMap = null;
				AcqUser orgIdEnt = (AcqUser)session.createCriteria(AcqUser.class).add(Restrictions.eq("id", Long.valueOf(sessionId))).uniqueResult();
				if(orgIdEnt!=null&&orgIdEnt+""!=""){
				AcqOrganization merchantIdEnt = (AcqOrganization)session.createCriteria(AcqOrganization.class).add(Restrictions.eq("id", Long.valueOf(orgIdEnt.getOrgId()))).uniqueResult();
				if(merchantIdEnt!=null&&merchantIdEnt+""!="")
					{
					AcqBankItEntity entity = new AcqBankItEntity();
						entity.setMerchantId(""+merchantIdEnt.getMerchantId());
						entity.setTerminalId(sessionId);
						entity.setRequest(request);
						entity.setResponse(""+bankItResponse);
						entity.setRequestTime(requestDateTime);
						entity.setResponseTime(dateTime);
						entity.setServiceName(serviceName);
						entity.setResponseTime(responseDateTime);
						tx = session.beginTransaction();
						session.save(entity);
						logger.info("Details Added In BankIt Table");				
						response.setStatusCode(EnumStatusConstant.OK.getId());
						response.setStatusMessage(EnumStatusConstant.OK.getDescription());
						//response.setBody(responseString);
						logger.info("BankIt Details Added Succesfuly");
						tx.commit();					
			}}}catch(Exception e){		
				response.setStatusCode(EnumStatusConstant.RollBackError.getId());
				response.setStatusMessage(EnumStatusConstant.RollBackError.getDescription());
				logger.info("Erore in BankIt " +e);
				}finally{
				session.close();			
			}
			return response;
		}		

		
		
		@Override
		public DbDto2<Object> dmtgenerateOtp(AcqMoneyTransferOtpModel model) {
			DbDto2<Object> response = new DbDto2<Object>();
			logger.info("request in get dmt generateOtp dao");
			Session session = null;
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			try{				
				Map<String, String> map= new HashMap<String, String>();
				MoneyTransferApis moneyApis = new MoneyTransferApis();
				JSONObject json = new JSONObject();
				String request ="{\"customerId\":\""+model.getCustomerId()+"\"\n}";
				Date date = new Date();
				String dateTime = format.format(date);
				json = moneyApis.callDmrOtpGenerate(model.getSessionId(), model.getCustomerId());
				String code = json.get("errorCode").toString();
				try{
					System.out.println("wedwed");
					persistBankItApi("Otp", model.getSessionId(), request, json, ""+model.getDateTime(),dateTime);
					}catch(Exception e){
						
					}				
				response.setStatusCode(EnumStatusConstant.OK.getId());
				response.setStatusMessage(EnumStatusConstant.OK.getDescription());
				response.setBody(json);
				return response;
			}
			catch(Exception e){
				logger.error("Error ::"+e);
			}
			//return response;
			return response;
		}
	
		@Override
		public DbDto2<Object> addSender(AcqMoneyTransferAddSenderModel model) {
			DbDto2<Object> response = new DbDto2<Object>();
			Map<String, String> map= new HashMap<String, String>();
			MoneyTransferApis moneyApis = new MoneyTransferApis();
			JSONObject json = new JSONObject();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String request ="{\"customerId\":\""+model.getCustomerId()+"\",\n\"name\":\""+model.getName()+"\",\n\"address\":\""+model.getAddress()+"\",\n\"dateOfBirth\":\""+model.getDateOfBirth()+"\",\n\"otp\":\""+model.getOtp()+"\"\n}";
			Date date = new Date();
			String dateTime = format.format(date);
			json = moneyApis.callCreateSender(model.getSessionId(), model.getCustomerId(), model.getName(), model.getAddress(), model.getDateOfBirth(), model.getOtp());
			String code = json.get("errorCode").toString();
			try{
				System.out.println("wedwed");
				persistBankItApi("AddSender", model.getSessionId(), request, json, ""+model.getDateTime(),dateTime);
				}catch(Exception e){
					
				}			
			response.setStatusCode(EnumStatusConstant.OK.getId());
			response.setStatusMessage(EnumStatusConstant.OK.getDescription());
			response.setBody(json);
			return response;
		}		

		@Override
		public DbDto2<Object> addBeneficiary(AcqMoneyTransferAddBeneficiaryModel model) {
			DbDto2<Object> response = new DbDto2<Object>();
			Map<String, String> map= new HashMap<String, String>();
			MoneyTransferApis moneyApis = new MoneyTransferApis();
			JSONObject json = new JSONObject();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String request ="{\"customerId\":\""+model.getCustomerId()+"\",\n\"bankName\":\""+model.getBankName()+"\",\n\"accountNo\":\""+model.getAccountNo()+"\",\n\"ifsc\":\""+model.getIfsc()+"\",\n\"mobileNo\":\""+model.getMobileNo()+"\",\n\"recipientName\":\""+model.getRecipientName()+"\"\n}";
			Date date = new Date();
			String dateTime = format.format(date);
			json = moneyApis.callAddRecipient(model.getSessionId(), model.getCustomerId(), model.getBankName(), model.getAccountNo(), model.getIfsc(), model.getMobileNo(),model.getRecipientName());
			String code = json.get("errorCode").toString();
			JSONObject json1 = new JSONObject();
			JSONObject json2 = new JSONObject();
			JSONObject finalJson = new JSONObject();
		//	{"data":{"walletbal":25000.0,"customerId":"8285458311","name":"Deepak","kycstatus":0,"recipientId":"AES_1127450","recipientName":"Abc","dateOfBirth":"1990-09-09","mobileNo":"82854585678"},"errorCode":"00","errorMsg":"Success"}
			if(json.get("errorCode").equals("00")){
			json1 = (JSONObject) json.get("data");
			json2.put("walletbal", json1.get("walletbal").toString());
			json2.put("customerId", json1.get("customerId"));
			json2.put("name", json1.get("name"));
			json2.put("kycstatus", json1.get("kycstatus").toString());
			json2.put("recipientId", json1.get("recipientId"));
			json2.put("recipientName", json1.get("recipientName"));
			json2.put("dateOfBirth", json1.get("dateOfBirth"));
			json2.put("mobileNo", json1.get("mobileNo"));
			finalJson.put("errorCode", json.get("errorCode"));
			finalJson.put("errorMsg", json.get("errorMsg"));
			finalJson.put("data", json2);
			//json3.pu
			response.setBody(finalJson);
			}else{
				response.setBody(json);
			}
			try{
				System.out.println("wedwed");
				persistBankItApi("AddBeneficiary", model.getSessionId(), request, json, ""+model.getDateTime(),dateTime);
				}catch(Exception e){					
				}			
			response.setStatusCode(EnumStatusConstant.OK.getId());
			response.setStatusMessage(EnumStatusConstant.OK.getDescription());
			
			return response;
		}		

		
		
	@Override
	public DbDto2<Object> fetchBeneficiaryList(AcqMoneyTransferOtpModel model) {
		logger.info("request in Fetch Customer dao");
		DbDto2<Object> response = new DbDto2<Object>();
		Session session = null;
		try {
			Map<String, String> map= new HashMap<String, String>();
			MoneyTransferApis moneyApis = new MoneyTransferApis();
			JSONObject json = new JSONObject();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String request ="{\"customerId\":\""+model.getCustomerId()+"\"\n}";
			Date date = new Date();
			String dateTime = format.format(date);
			json = moneyApis.callFetchAllRecipient(model.getSessionId(), model.getCustomerId());
			String code = json.get("errorCode").toString();
			try{
				System.out.println("wedwed");
				persistBankItApi("BeneficiaryList", model.getSessionId(), request, json, ""+model.getDateTime(),dateTime);
				}catch(Exception e){
					
				}
			
			response.setStatusCode(EnumStatusConstant.OK.getId());
			response.setStatusMessage(EnumStatusConstant.OK.getDescription());
			response.setBody(json);
			logger.info("Fetch Customer Request Succesfuly Done::");		
			//return response;
							
			} catch (Exception e) {
				response.setStatusCode(EnumStatusConstant.RollBackError.getId());
				response.setStatusMessage(EnumStatusConstant.RollBackError
						.getDescription());
				logger.info("Error to Fetch Customer Request Dao " + e);
			}			
			
	/*finally{
			session.close();			
		}*/
		return response;
	}

	
	@Override
	public DbDto2<Object> dmtNeft(AcqMoneyTransferNeftModel model) {
		logger.info("request in dmt neft dao");
		DbDto2<Object> response = new DbDto2<Object>();
		Session session = null;
		Transaction tx = null;
		session = getNewSession();
		try {
			Map<String, String> map= new HashMap<String, String>();
			MoneyTransferApis moneyApis = new MoneyTransferApis();
			JSONObject json = new JSONObject();
			JSONObject json2 = new JSONObject();
			try{
				JSONObject json1 = new JSONObject();
				AcqMapDeviceUserEntity deviceMapDevice = (AcqMapDeviceUserEntity)session.createCriteria(AcqMapDeviceUserEntity.class).add(Restrictions.eq("userId", Long.valueOf(model.getSessionId()))).uniqueResult();
				if(Double.parseDouble(""+deviceMapDevice.getRechargeBal())>=Double.parseDouble(model.getAmount()))
					{
				/*json1= moneyApis.callFetchSingleRecipient(model.getSessionId(),model.getRecipientId(),model.getCustomerId());
				json2 = (JSONObject) json1.get("data");
				if(json1.isEmpty()){
					response.setStatusCode(EnumStatusConstant.RecordNotFound.getId());
					response.setStatusMessage("Not Able To Fetch Beneficiary Details");
					response.setBody(json);
				}else{
				if(json1.get("errorCode").equals("00")||json1.get("errorCode").equals("01")){
					Double newBal = Double.parseDouble(""+deviceMapDevice.getRechargeBal()) - Double.parseDouble(model.getAmount());
					deviceMapDevice.setRechargeBal(""+new DecimalFormat("#.###").format(Double.parseDouble(newBal.toString())));
					session.update(deviceMapDevice);
					tx = session.beginTransaction();
					tx.commit();
					}else{
						System.out.println(" NEFT Transaction Failed");		
					}
				System.out.println("bbbbbbbbbbbbbbbbbb"+json2.get("udf2"));	
				}*/
					}else{
						response.setStatusCode(EnumStatusConstant.InsufficientBalance.getId());
						response.setStatusMessage(EnumStatusConstant.InsufficientBalance.getDescription());
						response.setBody(null);
						logger.info("Insufficient Amount");		
				}
			}catch(Exception e){
				response.setStatusCode(EnumStatusConstant.RollBackError.getId());
				response.setStatusMessage(EnumStatusConstant.RollBackError.getDescription());
				logger.info("Error to dmt neft Request Dao " + e);
				return response;
			}
			System.out.println("Abhishek 2 : NEFT : Printing Session ID" + model.getSessionId());
			json = moneyApis.callDmtNeft(model.getSessionId(), model.getRecipientId(), model.getCustomerId(), model.getAmount(),model.getSessionId());
			if(json.isEmpty()){
				response.setStatusCode(EnumStatusConstant.TransactionTimeOut.getId());
				response.setStatusMessage(EnumStatusConstant.TransactionTimeOut.getDescription());
				response.setBody(json);
			}else{
			response.setStatusCode(EnumStatusConstant.OK.getId());
			response.setStatusMessage(EnumStatusConstant.OK.getDescription());
			response.setBody(json);
			}
			logger.info("Fetch dmt neft Succesfuly Done::");		
			try{
				JSONObject json1 = new JSONObject();
				json1 = (JSONObject) json.get("data");
				System.out.println("wedwed");
				AcqBankItTransactionModel txnModel = new AcqBankItTransactionModel();
				txnModel.setAmount(model.getAmount());
				txnModel.setSessionId(model.getSessionId());
				txnModel.setClientRefId(""+json1.get("clientRefId"));
				txnModel.setRecipientId(model.getRecipientId());
				txnModel.setStatus(""+json.get("errorCode"));
				txnModel.setTransactionType("NEFT");
				txnModel.setTxnId(""+json1.get("txnId"));
				txnModel.setCustomerId(model.getCustomerId());
				txnModel.setMessage(""+json.get("errorMsg"));
				txnModel.setIfsc(""+json2.get("udf2"));
				txnModel.setMobileNo(""+json2.get("mobileNo"));
				txnModel.setAccountNo(""+json2.get("udf1"));
				txnModel.setRecipientName(""+json2.get("recipientName"));
				txnModel.setBankName(model.getBankName());
				persistBankItTransferApi(txnModel);
				}catch(Exception e){					
				}
				
			} catch (Exception e) {
				response.setStatusCode(EnumStatusConstant.RollBackError.getId());
				response.setStatusMessage(EnumStatusConstant.RollBackError.getDescription());
				logger.info("Error to dmt neft Request Dao " + e);
			}				
	/*finally{
			session.close();			
		}*/
		return response;
	}

	@Override
	public DbDto2<Object> dmtImps(AcqMoneyTransferNeftModel model) {
		logger.info("request in dmt imps dao");
		DbDto2<Object> response = new DbDto2<Object>();
		Session session = null;
		Transaction tx = null;
		try {
			session = getNewSession();
			JSONObject json = new JSONObject();
			JSONObject json1 = new JSONObject();
			JSONObject json2 = new JSONObject();
			Map<String, String> map= new HashMap<String, String>();
			MoneyTransferApis moneyApis = new MoneyTransferApis();
			try{
				AcqMapDeviceUserEntity deviceMapDevice = (AcqMapDeviceUserEntity)session.createCriteria(AcqMapDeviceUserEntity.class).add(Restrictions.eq("userId", Long.valueOf(model.getSessionId()))).uniqueResult();
				if(Double.parseDouble(""+deviceMapDevice.getRechargeBal())>=Double.parseDouble(model.getAmount()))
					{/*
				json1= moneyApis.callFetchSingleRecipient(model.getSessionId(),model.getRecipientId(),model.getCustomerId());
				json2 = (JSONObject) json1.get("data");
				if(json1.isEmpty()){
					response.setStatusCode(EnumStatusConstant.RecordNotFound.getId());
					response.setStatusMessage("Not Able To Fetch Beneficiary Details");
					response.setBody(json);
				}else{
				if(json1.get("errorCode").equals("00")||json1.get("errorCode").equals("01")){
				Double newBal = Double.parseDouble(""+deviceMapDevice.getRechargeBal()) - Double.parseDouble(model.getAmount());
				deviceMapDevice.setRechargeBal(""+new DecimalFormat("#.###").format(Double.parseDouble(newBal.toString())));
				session.update(deviceMapDevice);
				tx = session.beginTransaction();
				tx.commit();
				}else{
					System.out.println("Transaction Failed");		
				}
				}
				System.out.println("bbbbbbbbbbbbbbbbbb"+json2.get("udf2"));	
				*/}else{
					response.setStatusCode(EnumStatusConstant.InsufficientBalance.getId());
					response.setStatusMessage(EnumStatusConstant.InsufficientBalance.getDescription());
					response.setBody(null);
					logger.info("Insufficient Amount");		
			}
			}catch(Exception e){
				response.setStatusCode(EnumStatusConstant.RollBackError.getId());
				response.setStatusMessage(EnumStatusConstant.RollBackError.getDescription());
				logger.info("Error to dmt neft Request Dao " + e);
				return response;
			}
			System.out.println("Abhishek 1 : IMPS : Printing Session ID" + model.getSessionId());
			json = moneyApis.callDmtImps(model.getSessionId(), model.getRecipientId(), model.getCustomerId(), model.getAmount(), model.getSessionId());
			if(json.isEmpty()){
				response.setStatusCode(EnumStatusConstant.TransactionTimeOut.getId());
				response.setStatusMessage(EnumStatusConstant.TransactionTimeOut.getDescription());
				response.setBody(json);
			}else{
			response.setStatusCode(EnumStatusConstant.OK.getId());
			response.setStatusMessage(EnumStatusConstant.OK.getDescription());
			response.setBody(json);
			}
			logger.info("Fetch dmt neft Succesfuly Done::");		
			try{
				json1 = (JSONObject) json.get("data");
				System.out.println("wedwed");
				AcqBankItTransactionModel txnModel = new AcqBankItTransactionModel();
				txnModel.setAmount(model.getAmount());
				txnModel.setSessionId(model.getSessionId());
				txnModel.setClientRefId(""+json1.get("clientRefId"));
				txnModel.setRecipientId(model.getRecipientId());
				txnModel.setStatus(""+json.get("errorCode"));
				txnModel.setTransactionType("IMPS");
				txnModel.setTxnId(""+json1.get("txnId"));
				txnModel.setCustomerId(model.getCustomerId());
				txnModel.setMessage(""+json.get("errorMsg"));
				txnModel.setIfsc(""+json2.get("udf2"));
				txnModel.setMobileNo(""+json2.get("mobileNo"));
				txnModel.setAccountNo(""+json2.get("udf1"));
				txnModel.setRecipientName(""+json2.get("recipientName"));
				txnModel.setBankName(model.getBankName());
				persistBankItTransferApi(txnModel);
				}catch(Exception e){					
				}			
			} catch (Exception e) {
				response.setStatusCode(EnumStatusConstant.RollBackError.getId());
				response.setStatusMessage(EnumStatusConstant.RollBackError
						.getDescription());
				logger.info("Error to dmt imps Request Dao " + e);
			}			
			
	/*finally{
			session.close();			
		}*/
		return response;
	}
		
	@Override
	public DbDto2<Object> bankList(AcqMoneyTransferBankListModel model) {
		logger.info("request in bank LIst dao");
		DbDto2<Object> response = new DbDto2<Object>();
		Session session = null;
		try {
			JSONObject json = new JSONObject();
			Map<String, String> map= new HashMap<String, String>();
			MoneyTransferApis moneyApis = new MoneyTransferApis();
			json = moneyApis.callBankList();
			response.setStatusCode(EnumStatusConstant.OK.getId());
			response.setStatusMessage(EnumStatusConstant.OK.getDescription());
			response.setBody(json);
			logger.info("bank LIst Request Succesfuly Done::");		
			//return response;							
			} catch (Exception e) {
				response.setStatusCode(EnumStatusConstant.RollBackError.getId());
				response.setStatusMessage(EnumStatusConstant.RollBackError
						.getDescription());
				logger.info("Error to bank LIst Request Dao " + e);
			}			
			
	/*finally{
			session.close();			
		}*/
		return response;
	}

	
	@Override
	public DbDto2<Object> dmtDeleteBeneficiary(AcqMoneyTransferNeftModel model) {
		logger.info("request in dmt delete Beneficiary dao");
		DbDto2<Object> response = new DbDto2<Object>();
		Session session = null;
		JSONObject json = new JSONObject();
		try {
			Map<String, String> map= new HashMap<String, String>();
			
			MoneyTransferApis moneyApis = new MoneyTransferApis();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String request ="{\"customerId\":\""+model.getCustomerId()+"\",\n\"recipientId\":\""+model.getRecipientId()+"\"\n}";
			Date date = new Date();
			String dateTime = format.format(date);
			json = moneyApis.callDeleteBeneficiary(model.getSessionId(), model.getCustomerId(),model.getRecipientId());
			String code = json.get("errorCode").toString();
			try{
				System.out.println("wedwed");
				persistBankItApi("DeleteBeneficiary", model.getSessionId(), request, json, ""+model.getDateTime(),dateTime);
				}catch(Exception e){
					
				}
			
			response.setStatusCode(EnumStatusConstant.OK.getId());
			response.setStatusMessage(EnumStatusConstant.OK.getDescription());
			response.setBody(json);
			logger.info("Fetch dmt delete Beneficiary Succesfuly Done::");		
			//return response;
							
			} catch (Exception e) {
				response.setStatusCode(EnumStatusConstant.RollBackError.getId());
				response.setStatusMessage(EnumStatusConstant.RollBackError
						.getDescription());
				logger.info("Error to dmt delete Beneficiary Request Dao " + e);
			}			
			
	/*finally{
			session.close();			
		}*/
		return response;
	}
	
	@Override
	public DbDto2<Object> checkStatus(AcqMoneyTransferCheckStatusModel model) {
		logger.info("request in check Status Request dao");
		DbDto2<Object> response = new DbDto2<Object>();
		Session session = null;
		Transaction tx = null;
		JSONObject json = new JSONObject();
		try {
			MoneyTransferApis moneyApis = new MoneyTransferApis();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String request ="{\"clientRefId\":\""+model.getClientRefId()+"\"\n}";
			json = moneyApis.callCheckStatus(model.getClientRefId());
			Date date = new Date();
			String dateTime = format.format(date);
//{"errorMsg":"Success","errorCode":"00","data":{"clientRefId":"10037559577512843166","txnId":"819010034340","transactionDate":"2018-08-08","amount":100.00}}
			JSONObject json1 = new JSONObject();
			JSONObject json2 = new JSONObject();
			JSONObject finalJson = new JSONObject();
			if(json.get("errorCode").equals("00")){
				json1 = (JSONObject) json.get("data");
				json2.put("clientRefId", json1.get("clientRefId"));
				json2.put("txnId", json1.get("txnId"));
				json2.put("transactionDate", json1.get("transactionDate"));
				json2.put("amount", json1.get("amount").toString());
				finalJson.put("errorCode", json.get("errorCode"));
				finalJson.put("errorMsg", json.get("errorMsg"));
				finalJson.put("data", json2);
				//json3.pu
				response.setBody(finalJson);
				}else{
					response.setBody(json);
				}
			try{
				System.out.println("wedwed");
				persistBankItApi("CheckStatus", model.getSessionId(), request, json, ""+model.getDateTime(),dateTime);
				}catch(Exception e){
					
				}
			response.setStatusCode(EnumStatusConstant.OK.getId());
			response.setStatusMessage(EnumStatusConstant.OK.getDescription());
			response.setBody(json);
			logger.info("Fetch dmt check Status Succesfuly Done::");	
			return response;
			//return response;
		 }catch (Exception e) {
				response.setStatusCode(EnumStatusConstant.RollBackError.getId());
				response.setStatusMessage(EnumStatusConstant.RollBackError.getDescription());
				logger.info("Error to check Status Request Dao " + e);
			}						
	/*finally{
			session.close();			
		}*/
		return response;
	}


	@Override
	public DbDto2<Object> cardLoad(AcqBankItCardLoadModel model) {
		logger.info("request in card load Request dao");
		DbDto2<Object> response = new DbDto2<Object>();
		Session session = null;
		Transaction tx = null;
		JSONObject json = new JSONObject();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			MoneyTransferApis moneyApis = new MoneyTransferApis();
			String clientRefId = OtpGenerator.RandomNumberGenerator().substring(0, OtpGenerator.RandomNumberGenerator().length() - model.getSessionId().length())+model.getSessionId();
			model.setTransactionId(clientRefId);
			String request ="{\"cardNumber\":\""+model.getCardNumber()+"\",\n\"amount\":\""+model.getAmount()+"\",\n\"mobile\":\""+model.getMobile()+"\",\n\"transactionId\":\""+model.getTransactionId()+"\",\n\"ipAddress\":\""+model.getIpAddress()+"\"\n}";
			json = moneyApis.callCardLoadApi(model.getCardNumber(), model.getAmount(), model.getMobile(), model.getTransactionId());
			Date date = new Date();
			String dateTime = format.format(date);
			try{
				System.out.println("wedwed");
				persistBankItApi("CardLoad", model.getSessionId(), request, json, ""+model.getDateTime(),dateTime);
				}catch(Exception e){
					
				}
			response.setStatusCode(EnumStatusConstant.OK.getId());
			response.setStatusMessage(EnumStatusConstant.OK.getDescription());
			response.setBody(json);
			logger.info("Fetch dmt card load Succesfuly Done::");	
			return response;
			//return response;
		 }catch (Exception e) {
				response.setStatusCode(EnumStatusConstant.RollBackError.getId());
				response.setStatusMessage(EnumStatusConstant.RollBackError.getDescription());
				logger.info("Error to card load Request Dao " + e);
			}			
			
	/*finally{
			session.close();			
		}*/
		return response;
	}
	
	
	
	@Override
	public DbDto2<Object> cardLoadStatus(AcqBankItCardLoadModel model) {
		logger.info("request in card load Request dao");
		DbDto2<Object> response = new DbDto2<Object>();
		Session session = null;
		Transaction tx = null;
		JSONObject json = new JSONObject();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			MoneyTransferApis moneyApis = new MoneyTransferApis();
			String request ="{\"transactionId\":\""+model.getTransactionId()+"\"\n}";
			json = moneyApis.callCardLoadStsusApi(model.getTransactionId());
			Date date = new Date();
			String dateTime = format.format(date);
			try{
				System.out.println("wedwed");
				persistBankItApi("CardLoadStatus", model.getSessionId(), request, json, ""+model.getDateTime(),dateTime);
				}catch(Exception e){
					
				}
			response.setStatusCode(EnumStatusConstant.OK.getId());
			response.setStatusMessage(EnumStatusConstant.OK.getDescription());
			response.setBody(json);
			logger.info("Fetch dmt card load Status Succesfuly Done::");	
			return response;
			//return response;
		 }catch (Exception e) {
				response.setStatusCode(EnumStatusConstant.RollBackError.getId());
				response.setStatusMessage(EnumStatusConstant.RollBackError.getDescription());
				logger.info("Error to card load Status Request Dao " + e);
			}			
			
	/*finally{
			session.close();			
		}*/
		return response;
	}
	
	@Override
	public DbDto2<Object> getCardBal(AcqBankItCardLoadModel model) {
		logger.info("request in get CardBal Request dao");
		DbDto2<Object> response = new DbDto2<Object>();
		Session session = null;
		Transaction tx = null;
		JSONObject json = new JSONObject();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			MoneyTransferApis moneyApis = new MoneyTransferApis();
			String request ="{\"cardNumber\":\""+model.getCardNumber()+"\"\n}";
			json = moneyApis.callCardLoadBalApi(model.getCardNumber(),model.getMobile());
			Date date = new Date();
			String dateTime = format.format(date);
			try{
				System.out.println("wedwed");
				persistBankItApi("CardLoadBal", model.getSessionId(), request, json, ""+model.getDateTime(),dateTime);
				}catch(Exception e){
					
				}
			response.setStatusCode(EnumStatusConstant.OK.getId());
			response.setStatusMessage(EnumStatusConstant.OK.getDescription());
			response.setBody(json);
			logger.info("Fetch dmt get CardBal Succesfuly Done::"+json);	
			return response;
			//return response;
		 }catch (Exception e) {
				response.setStatusCode(EnumStatusConstant.RollBackError.getId());
				response.setStatusMessage(EnumStatusConstant.RollBackError.getDescription());
				logger.info("Error to get CardBal Request Dao " + e);
			}			
			
	/*finally{
			session.close();			
		}*/
		return response;
	}
	
	
	
}
