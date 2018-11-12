package com.acq.web.dao.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;

import com.acq.EnumStatusConstant;
import com.acq.AcqSHA256;
import com.acq.OtpGenerator;
import com.acq.users.entity.AcqGCMNotificationEntity;
import com.acq.users.entity.AcqKeyExchangeEntity;
import com.acq.users.entity.AcqLoginEntity;
import com.acq.users.entity.AcqMapDeviceUserEntity;
import com.acq.users.entity.AcqMerchantEntity;
import com.acq.users.entity.AcqPlaceTransactionEntity;
import com.acq.users.entity.AcqOrganization;
import com.acq.users.entity.AcqUser;
import com.acq.users.entity.AcqUserDetailsEntity;
import com.acq.web.controller.model.AcqApiLogin;
import com.acq.web.controller.model.AcqChangePasswordModel;
import com.acq.web.controller.model.AcqD200ApiLogin;
import com.acq.web.controller.model.AcqMobiWalletDebitModel;
import com.acq.web.controller.model.AcqOgsLoginModel;
import com.acq.web.controller.model.OGSMainLoginModel;
import com.acq.web.dao.AcqDao;
import com.acq.web.dao.LoginDaoInf;
import com.acq.web.dto.impl.DbDto;
import com.acq.web.dto.impl.DbDto2;

@Repository
public class LoginDao extends AcqDao implements LoginDaoInf {
	final static Logger logger = Logger.getLogger(LoginDao.class);
	/*final static java.util.logging.Logger fileLogger = java.util.logging.Logger
			.getLogger(LoginDao.class.getName());*/

	@Autowired
	MessageSource resource;

	@Value("#{acqConfig['loggers.location']}")
	private String loginLocation;

	
	
	
	
	
	
	@Override
	public DbDto2<Object> getLoginD200(AcqD200ApiLogin model,String clientIpAddress) {
		System.out.println("request in login tpos version1 dao");
		StringBuffer logReport = new StringBuffer();
		String status = "";
		logReport.append(",Info: Login dao started tpos version1");
		DbDto2<Object> responseMap = new DbDto2<Object>();
		Map<String,String> resultMap = new HashMap<String,String>();
		Session session = null;
		try {
			session = getNewSession();
			logReport.append(",Info: Hibernate session object is created tpos version1");
			AcqLoginEntity loginEnt = (AcqLoginEntity) session
					.createCriteria(AcqLoginEntity.class)
					.add(Restrictions.eq("username", model.getLoginId()))
					.add(Restrictions.eq("password",AcqSHA256.getSHA256(model.getPassword())))
					.uniqueResult();
			if (loginEnt == null || loginEnt + "" == "") {
				responseMap.setStatusCode(EnumStatusConstant.InvalidCrediential.getId());
				responseMap.setStatusMessage(EnumStatusConstant.InvalidCrediential.getDescription());
				responseMap.setBody(resultMap);
				logReport.append(",Error: User not found version5");
				status = "FAILED";
			} else {
				logReport.append(",Info: User is found password verifying");
				try{
					String settingQuery = "select maintenanceMode,maintenanceReason,appVersion from AcqSettingEntity where id=:accessKey";
					Query query=session.createQuery(settingQuery);
					query.setParameter("accessKey", 1l);
					List<Object[]> listResult = query.list();
					for (Object[] aRow : listResult) {
						System.out.println(aRow[0] + " ---------- " + aRow[1]);
						resultMap.put("maintenanceMode",""+aRow[0]);
						resultMap.put("maintenanceReason",""+aRow[1]);
						resultMap.put("appVersion",""+aRow[2]);
						resultMap.put("sessionId",""+loginEnt.getUserId());
					}
					if(!resultMap.get("maintenanceMode").equals("0")){
						responseMap.setStatusCode(EnumStatusConstant.OK.getId());
						responseMap.setStatusMessage(EnumStatusConstant.OK.getDescription());
						responseMap.setBody(resultMap);
						logReport.append(",Warn: application is in maintenance mode");
						status = "WARN";
					}
					/*if(model.getAppVersion()!=null&&!resultMap.get("appVersion").equals(model.getAppVersion())){
						responseMap.setStatus(EnumStatusConstant.OK.getId());
						responseMap.setMessage(EnumStatusConstant.OK.getDescription());
						resultMap.put("maintenanceMode","1");
						resultMap.put("maintenanceReason","mismatch");
						responseMap.setResult(resultMap);
						logReport.append(",Warn: application version mismatch");
						status = "WARN";
					}*/
				}catch(Exception e){
					logger.error("error to select ez-settings "+e);
				}
				logger.info("ez-setting selected");
				if (loginEnt.getPassword().equals(
						AcqSHA256.getSHA256(model.getPassword()))){				
					ProjectionList p1=Projections.projectionList();
			        p1.add(Projections.property("bankTid"));
			        p1.add(Projections.property("deviceType"));
			        System.out.println("hibernate session created");
					Criteria deviceMapDevice = session.createCriteria(AcqMapDeviceUserEntity.class).setProjection(p1).add(Restrictions.eq("userId", loginEnt.getUserId()));
					List list = deviceMapDevice.list();
					String banktid = "";
					String accountType = "";
					Iterator itr = list.iterator();
					while(itr.hasNext()){
						Object ob[] = (Object[])itr.next();
						banktid = ""+ ob[0];accountType=""+ob[1];
					}
					System.out.println("device details selected");
					Long merchantid = (Long)session.createCriteria(AcqOrganization.class)
						      .setProjection(Projections.property("merchantId"))
						      .add(Restrictions.eq("id", Long.valueOf(loginEnt.getOrgId()))).uniqueResult();
						    String merchantBankMID = (String)session.createCriteria(AcqMerchantEntity.class)
								      .setProjection(Projections.property("merchantTID"))
								      .add(Restrictions.eq("merchantId",""+ merchantid)).uniqueResult();
					logReport.append(",Info: Response putted in result");
					System.out.println("org details selected");
					resultMap.put("sessionId",""+loginEnt.getUserId());
					resultMap.put("banktid",""+banktid);
					resultMap.put("merchantBankMID",""+merchantBankMID);
					resultMap.put("firstLogin",""+loginEnt.getStatus());
					resultMap.put("userStatus",""+loginEnt.getUserStatus());
					resultMap.put("loyaltyStatus",""+loginEnt.getLoyaltyStatus());
					resultMap.put("walletStatus",""+loginEnt.getWalletStatus());
					resultMap.put("accountType",""+accountType);
					/*resultMap.put("agsGetSessionKeyUrl",agsGetSessionKeyUrl);
					resultMap.put("agsGetSessionKeyHost",agsGetSessionKeyHost);*/
					resultMap.put("switchType", loginEnt.getSwitchType());
					responseMap.setBody(resultMap);
					responseMap.setStatusCode(EnumStatusConstant.OK.getId());
					responseMap.setStatusMessage("" + loginEnt.getUserId());
					logReport.append(",Info: User successfully login");
					status = "SUCCESSFULL";
				} else {
					System.out.println("invalid user");
					responseMap.setStatusCode(EnumStatusConstant.InvalidCrediential.getId());
					responseMap.setStatusMessage(EnumStatusConstant.InvalidCrediential.getDescription());
					responseMap.setBody(responseMap);
					logReport.append(",Error: User verification failed");
					status = "FAILED";
				}
			}
		} catch (Exception e) {
			responseMap.setStatusCode(EnumStatusConstant.RollBackError.getId());
			responseMap.setStatusMessage(EnumStatusConstant.RollBackError.getDescription());
			responseMap.setBody(responseMap);
			logReport.append(",Error: in user authenticate " + e);
			status = "FAILED";
		} finally {
			session.flush();
			session.close();			
			/*FileHandler fh;
			try {
				fh = new FileHandler(loginLocation + File.separator	+ "ezlogin.log", true);
				fileLogger.addHandler(fh);
				SimpleFormatter formatter = new SimpleFormatter();
				fh.setFormatter(formatter);
				fileLogger.info(clientIpAddress + logReport + "," + status+";");
				fh.close();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}*/
		}
		return responseMap;
	}
	
	@Override
	public DbDto<Object> getLoginD200(OGSMainLoginModel model,
			String clientIpAddress) {
		System.out.println("request in login version6 dao");
		DbDto<Object> responseMap = new DbDto<Object>();
		Map<String,String> resultMap = new HashMap<String,String>();
		Session session = null;
		try {
			session = getNewSession();				
			AcqLoginEntity loginEnt = (AcqLoginEntity) session
					.createCriteria(AcqLoginEntity.class)
					.add(Restrictions.eq("username", model.getUsername()))
					.add(Restrictions.eq("password",
							AcqSHA256.getSHA256(model.getPassword())))
					.uniqueResult();
			if (loginEnt == null || loginEnt + "" == "") {
				responseMap.setStatus(EnumStatusConstant.InvalidCrediential.getId());
				responseMap.setMessage(EnumStatusConstant.InvalidCrediential.getDescription());
				responseMap.setResult(resultMap);
			} else {
				//logReport.append(",Info: User is found password verifying");
				try{
					String settingQuery = "select maintenanceMode,maintenanceReason,appVersion from AcqSettingEntity where id=:accessKey";
					Query query=session.createQuery(settingQuery);
					query.setParameter("accessKey", 1l);
					List<Object[]> listResult = query.list();
					for (Object[] aRow : listResult) {
						System.out.println(aRow[0] + " ---------- " + aRow[1]);
						resultMap.put("maintenanceMode",""+aRow[0]);
						resultMap.put("maintenanceReason",""+aRow[1]);
						resultMap.put("appVersion",""+aRow[2]);
					}
					if(!resultMap.get("maintenanceMode").equals("0")){
						responseMap.setStatus(EnumStatusConstant.OK.getId());
						responseMap.setMessage(EnumStatusConstant.OK.getDescription());
						responseMap.setResult(resultMap);						
					}
				}catch(Exception e){
					logger.error("error to select ez-settings "+e);
				}
				logger.info("ez-setting selected");
				if (loginEnt.getPassword().equals(
						AcqSHA256.getSHA256(model.getPassword()))){				
					ProjectionList p1=Projections.projectionList();
			        p1.add(Projections.property("bankTid"));
			        p1.add(Projections.property("deviceType"));
			        p1.add(Projections.property("serialNo"));
			        System.out.println("hibernate session created");
					Criteria deviceMapDevice = session.createCriteria(AcqMapDeviceUserEntity.class).setProjection(p1).add(Restrictions.eq("userId", loginEnt.getUserId()));
					List list = deviceMapDevice.list();
					String banktid = "";
					String accountType = "";
					String deviceSrNo = "";
					Iterator itr = list.iterator();
					while(itr.hasNext()){
						Object ob[] = (Object[])itr.next();
						banktid = ""+ ob[0];accountType=""+ob[1];
						deviceSrNo = ""+ ob[2];
						
						//System.out.println(ob[0]+" "+ob[1]);
					}
					System.out.println("device details selected");
					Long merchantid = (Long)session.createCriteria(AcqOrganization.class)
						      .setProjection(Projections.property("merchantId"))
						      .add(Restrictions.eq("id", Long.valueOf(loginEnt.getOrgId()))).uniqueResult();
						    String merchantBankMID = (String)session.createCriteria(AcqMerchantEntity.class)
								      .setProjection(Projections.property("merchantTID"))
								      .add(Restrictions.eq("merchantId",""+ merchantid)).uniqueResult();
						    
					System.out.println(banktid+"::::login::::"+loginEnt.getUserId());
					AcqKeyExchangeEntity keyEnt = (AcqKeyExchangeEntity)session.createCriteria(AcqKeyExchangeEntity.class).add(Restrictions.eq("tid",deviceSrNo)).add(Restrictions.eq("userId",loginEnt.getUserId())).uniqueResult();
					if(keyEnt==null){
						resultMap.put("keyExchange", "True");			
					}else{
						resultMap.put("keyExchange", keyEnt.getStatus());
					}
					System.out.println("key exchange:"+resultMap.get("keyExchange"));
						    
					//logReport.append(",Info: Response putted in result");
					System.out.println("org details selected");
					//String responseString = "{\"sessionId\":\""+loginEnt.getUserId()+"\",\"TID\":\""+banktid+"\",\"MID\":\""+merchantBankMID+"\",\"firstLogin\":\""+loginEnt.getStatus()+"\",\"userStatus\":\""+loginEnt.getUserStatus()+"\",\"loyaltyStatus\":\""+loginEnt.getLoyaltyStatus()+"\",\"walletStatus\":\""+loginEnt.getWalletStatus()+"\",\"agsSessionKey\":\""+agsAccessKey+"\",\"agsKeyResponse\":\""+agsResponse+"\",\"accountType\":\""+accountType+"\"}";
					//logger.info("responseString: "+responseString);
					resultMap.put("sessionId",""+loginEnt.getUserId());
					resultMap.put("banktid",""+banktid);
					resultMap.put("merchantBankMID",""+merchantBankMID);
					resultMap.put("firstLogin",""+loginEnt.getStatus());
					resultMap.put("userStatus",""+loginEnt.getUserStatus());
					resultMap.put("loyaltyStatus",""+loginEnt.getLoyaltyStatus());
					resultMap.put("walletStatus",""+loginEnt.getWalletStatus());
					resultMap.put("accountType",""+accountType);
					/*resultMap.put("agsGetSessionKeyUrl",agsGetSessionKeyUrl);
					resultMap.put("agsGetSessionKeyHost",agsGetSessionKeyHost);*/
					resultMap.put("switchType", loginEnt.getSwitchType());
					responseMap.setResult(resultMap);
					responseMap.setStatus(EnumStatusConstant.OK.getId());
					responseMap.setMessage("" + loginEnt.getUserId());
					
				} else {
					System.out.println("invalid user");
					responseMap.setStatus(EnumStatusConstant.InvalidCrediential.getId());
					responseMap.setMessage(EnumStatusConstant.InvalidCrediential.getDescription());
					responseMap.setResult(responseMap);					
				}
			}
		} catch (Exception e) {
			responseMap.setStatus(EnumStatusConstant.RollBackError.getId());
			responseMap.setMessage(EnumStatusConstant.RollBackError.getDescription());
			responseMap.setResult(responseMap);
		} finally {
			session.flush();
			session.close();
		}
		return responseMap;
	}
	
	@Override
	public DbDto<Object> getLoginVersion6(AcqOgsLoginModel model,
			String clientIpAddress) {
		System.out.println("request in login version6 dao");
		DbDto<Object> responseMap = new DbDto<Object>();
		Map<String,String> resultMap = new HashMap<String,String>();
		Session session = null;
		try {
			session = getNewSession();				
			AcqLoginEntity loginEnt = (AcqLoginEntity) session
					.createCriteria(AcqLoginEntity.class)
					.add(Restrictions.eq("username", model.getUsername()))
					.add(Restrictions.eq("password",
							AcqSHA256.getSHA256(model.getPassword())))
					.uniqueResult();
			if (loginEnt == null || loginEnt + "" == "") {
				responseMap.setStatus(EnumStatusConstant.InvalidCrediential.getId());
				responseMap.setMessage(EnumStatusConstant.InvalidCrediential.getDescription());
				responseMap.setResult(resultMap);
			} else {
				//logReport.append(",Info: User is found password verifying");
				try{
					String settingQuery = "select maintenanceMode,maintenanceReason,appVersion from AcqSettingEntity where id=:accessKey";
					Query query=session.createQuery(settingQuery);
					query.setParameter("accessKey", 1l);
					List<Object[]> listResult = query.list();
					for (Object[] aRow : listResult) {
						System.out.println(aRow[0] + " ---------- " + aRow[1]);
						resultMap.put("maintenanceMode",""+aRow[0]);
						resultMap.put("maintenanceReason",""+aRow[1]);
						resultMap.put("appVersion",""+aRow[2]);
					}
					if(!resultMap.get("maintenanceMode").equals("0")){
						responseMap.setStatus(EnumStatusConstant.OK.getId());
						responseMap.setMessage(EnumStatusConstant.OK.getDescription());
						responseMap.setResult(resultMap);						
					}
				}catch(Exception e){
					logger.error("error to select ez-settings "+e);
				}
				logger.info("ez-setting selected");
				if (loginEnt.getPassword().equals(
						AcqSHA256.getSHA256(model.getPassword()))){				
					ProjectionList p1=Projections.projectionList();
			        p1.add(Projections.property("bankTid"));
			        p1.add(Projections.property("deviceType"));
			        p1.add(Projections.property("serialNo"));
			        System.out.println("hibernate session created");
					Criteria deviceMapDevice = session.createCriteria(AcqMapDeviceUserEntity.class).setProjection(p1).add(Restrictions.eq("userId", loginEnt.getUserId()));
					List list = deviceMapDevice.list();
					String banktid = "";
					String accountType = "";
					String deviceSrNo = "";
					Iterator itr = list.iterator();
					while(itr.hasNext()){
						Object ob[] = (Object[])itr.next();
						banktid = ""+ ob[0];accountType=""+ob[1];
						deviceSrNo = ""+ ob[2];
						
						//System.out.println(ob[0]+" "+ob[1]);
					}
					System.out.println("device details selected");
					Long merchantid = (Long)session.createCriteria(AcqOrganization.class)
						      .setProjection(Projections.property("merchantId"))
						      .add(Restrictions.eq("id", Long.valueOf(loginEnt.getOrgId()))).uniqueResult();
						    String merchantBankMID = (String)session.createCriteria(AcqMerchantEntity.class)
								      .setProjection(Projections.property("merchantTID"))
								      .add(Restrictions.eq("merchantId",""+ merchantid)).uniqueResult();
						    
					System.out.println(banktid+"::::login::::"+loginEnt.getUserId());
					AcqKeyExchangeEntity keyEnt = (AcqKeyExchangeEntity)session.createCriteria(AcqKeyExchangeEntity.class).add(Restrictions.eq("tid",deviceSrNo)).add(Restrictions.eq("userId",loginEnt.getUserId())).uniqueResult();
					if(keyEnt==null){
						resultMap.put("keyExchange", "True");			
					}else{
						resultMap.put("keyExchange", keyEnt.getStatus());
					}
					System.out.println("key exchange:"+resultMap.get("keyExchange"));
						    
					//logReport.append(",Info: Response putted in result");
					System.out.println("org details selected");
					//String responseString = "{\"sessionId\":\""+loginEnt.getUserId()+"\",\"TID\":\""+banktid+"\",\"MID\":\""+merchantBankMID+"\",\"firstLogin\":\""+loginEnt.getStatus()+"\",\"userStatus\":\""+loginEnt.getUserStatus()+"\",\"loyaltyStatus\":\""+loginEnt.getLoyaltyStatus()+"\",\"walletStatus\":\""+loginEnt.getWalletStatus()+"\",\"agsSessionKey\":\""+agsAccessKey+"\",\"agsKeyResponse\":\""+agsResponse+"\",\"accountType\":\""+accountType+"\"}";
					//logger.info("responseString: "+responseString);
					resultMap.put("sessionId",""+loginEnt.getUserId());
					resultMap.put("banktid",""+banktid);
					resultMap.put("merchantBankMID",""+merchantBankMID);
					resultMap.put("firstLogin",""+loginEnt.getStatus());
					resultMap.put("userStatus",""+loginEnt.getUserStatus());
					resultMap.put("loyaltyStatus",""+loginEnt.getLoyaltyStatus());
					resultMap.put("walletStatus",""+loginEnt.getWalletStatus());
					resultMap.put("accountType",""+accountType);
					/*resultMap.put("agsGetSessionKeyUrl",agsGetSessionKeyUrl);
					resultMap.put("agsGetSessionKeyHost",agsGetSessionKeyHost)*/;
					resultMap.put("switchType", loginEnt.getSwitchType());
					responseMap.setResult(resultMap);
					responseMap.setStatus(EnumStatusConstant.OK.getId());
					responseMap.setMessage("" + loginEnt.getUserId());
					
				} else {
					System.out.println("invalid user");
					responseMap.setStatus(EnumStatusConstant.InvalidCrediential.getId());
					responseMap.setMessage(EnumStatusConstant.InvalidCrediential.getDescription());
					responseMap.setResult(responseMap);					
				}
			}
		} catch (Exception e) {
			responseMap.setStatus(EnumStatusConstant.RollBackError.getId());
			responseMap.setMessage(EnumStatusConstant.RollBackError.getDescription());
			responseMap.setResult(responseMap);
		} finally {
			session.flush();
			session.close();
		}
		return responseMap;
	}
	
	
	@Override
	public DbDto<Object> getTxnDetails(AcqMobiWalletDebitModel model) {
		DbDto<Object> response = new DbDto<Object>();
				
		Session session = null;
		try {	
			session = getNewSession();
			AcqPlaceTransactionEntity entity = (AcqPlaceTransactionEntity)session.createCriteria(AcqPlaceTransactionEntity.class).add(Restrictions.eq("mobile", model.getMobile())).add(Restrictions.eq("id", Integer.parseInt(model.getTxnId()))).add(Restrictions.eq("txnType",model.getWalletType())).uniqueResult();		
			//tx = session.beginTransaction();
			if(entity==null||entity+""==""){			
				logger.warn("debit txn not found");
				response.setStatus(EnumStatusConstant.NotFound.getId());
				response.setMessage(EnumStatusConstant.NotFound.getDescription());
				return response;
			}else if(entity.getStatus()==505){
				logger.warn("Transaction id already in use");
				response.setStatus(EnumStatusConstant.AlreadyInUse.getId());
				response.setMessage(EnumStatusConstant.AlreadyInUse.getDescription());
				return response;
			}else{
				logger.info("going process");
				response.setStatus(EnumStatusConstant.OK.getId());
				response.setMessage(EnumStatusConstant.OK.getDescription());
				return response;
			}
		}catch(Exception e){
			logger.error("error to call debit txn "+e);
		}finally{
			session.flush();
			session.close();
		}
		return response;
	}
	
	
	}
