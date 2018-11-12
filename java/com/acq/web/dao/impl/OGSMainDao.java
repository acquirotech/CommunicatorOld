package com.acq.web.dao.impl;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.WeakHashMap;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.GenericPackager;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.acq.EnumOgsConstant;
import com.acq.EnumStatusConstant;
import com.acq.AcqOGSApiValidator;
import com.acq.users.entity.AcqCardBinDesc;
import com.acq.users.entity.AcqCardDetails;
import com.acq.users.entity.AcqDevice;
import com.acq.users.entity.AcqKeyDetailsEntity;
import com.acq.users.entity.AcqKeyExchangeEntity;
import com.acq.users.entity.AcqMapDeviceUserEntity;
import com.acq.users.entity.AcqMerchantEntity;
import com.acq.users.entity.AcqPlaceTransactionEntity;
import com.acq.users.entity.AcqOgsISOEntity;
import com.acq.users.entity.AcqOrganization;
import com.acq.users.entity.AcqRiskManagement;
import com.acq.users.entity.AcqSettingEntity;
import com.acq.users.entity.AcqTxnEntity;
import com.acq.users.entity.AcqUserEntity;
import com.acq.web.controller.model.AcqOGSMainKeyExchangeModel;
import com.acq.web.controller.model.OGSMainKeyExModel;
import com.acq.web.controller.model.OGSMainLoginModel;
import com.acq.web.controller.model.OGSMainReversalTxnModel;
import com.acq.web.controller.model.OGSMainTxnCompleteModel;
import com.acq.web.controller.model.OGSMainTxnInitiateModel;
import com.acq.web.controller.model.OGSMainVoidTnxModel;
import com.acq.web.controller.model.OGSMainVoidTxnModel;
import com.acq.web.dao.AcqDao;
import com.acq.web.dao.OGSMainDaoInf;
import com.acq.web.dto.impl.DbDto;

@Repository
public class OGSMainDao extends AcqDao implements OGSMainDaoInf {
	
	final static Logger logger = Logger.getLogger(OGSMainDao.class);
	
	SimpleDateFormat df = null;

	SimpleDateFormat dateFormatGmt = null;	
	
	@Value("#{acqConfig['aidArray.location']}")
	private String aidArray;
	
	public String getAidArray(){
		return aidArray;
	}
	
	@Value("#{acqConfig['capkArray.location']}")
	private String capkArray;
	public String getCapkArray(){
		return capkArray;
	}
	
	
	
	@Override
	public DbDto<Object> getReversal(String txnId) {
		DbDto<Object> response = new DbDto<Object>();
		Session session = null;
		OGSMainTxnInitiateModel model = new OGSMainTxnInitiateModel();
		ISOMsg isoMsg = new ISOMsg();
		 String ksn = null;
		try{
			try{
			session = getNewSession();
			AcqPlaceTransactionEntity entity = (AcqPlaceTransactionEntity)session.createCriteria(AcqPlaceTransactionEntity.class).add(Restrictions.eq("id", Integer.valueOf(txnId))).uniqueResult();
			if(entity==null){
				response.setStatus(100);
				response.setMessage("Txn Not found");
				return response;
			}
			AcqCardDetails details = (AcqCardDetails)session.createCriteria(AcqCardDetails.class).add(Restrictions.eq("transactionId", Integer.valueOf(txnId))).uniqueResult();
			if(details==null){
				response.setStatus(100);
				response.setMessage("Txn Not found");
				return response;
			}
			AcqOgsISOEntity requestIsoEnt = (AcqOgsISOEntity)session.createCriteria(AcqOgsISOEntity.class).add(Restrictions.eq("transactionId",txnId)).uniqueResult();
			if(requestIsoEnt==null){
				response.setStatus(100);
				response.setMessage("Txn Not found");
				return response;
			}
			//OGSMainReversalTxnModel model = new OGSMainReversalTxnModel();
			
			model.setTxnId(txnId);
			 SimpleDateFormat dateFormatGmt = new SimpleDateFormat("MMddHHmmss");
			 String dateTime = dateFormatGmt.format(new Date());
			 //String MMdd = dateTime.substring(0, 4);
			 //String hhmmss = dateTime.substring(4, 10);
			
			 dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
			 String gmtDateTime = dateFormatGmt.format(new Date());			
			 GenericPackager packager = new GenericPackager("reversal_request.xml");
			 isoMsg.setPackager(packager);
			 isoMsg.setMTI("0420");			
			 //System.out.println("iso from db:"+requestIsoEnt.getIsoRequest());
			 String dbIso = requestIsoEnt.getIsoRequest();
			 ksn = dbIso.substring(0,20);
			 //System.out.println("ksnksnksn:"+ksn);
			 JSONObject preIso = preIsoParser(requestIsoEnt.getIsoRequest());
			// System.out.println("------------aaaaaaaaaaaa");
			 //System.out.println("db iso parsed:"+preIso);
			 //System.out.println("111111");
			 isoMsg.set(3, preIso.get("DE03").toString());
			 //System.out.println("22222");
			 isoMsg.set(4, preIso.get("DE04").toString());	
			// System.out.println("xxxxxxxxxxxxx");
			 isoMsg.set(7, gmtDateTime);
			 //System.out.println("444444");
			 isoMsg.set(11, preIso.get("DE11").toString());	
			 //System.out.println("555555");
			 isoMsg.set(12, preIso.get("DE12").toString());	
			 //System.out.println("666666");
			 isoMsg.set(13, preIso.get("DE13").toString());	
			 //System.out.println("777777");
			 isoMsg.set(22, preIso.get("DE22").toString());	
			 //System.out.println("888888");
			 isoMsg.set(23, preIso.get("DE23").toString());
			 //System.out.println("999999");
			 isoMsg.set(25, preIso.get("DE25").toString());
			// System.out.println("101001010");
			 isoMsg.set(32, preIso.get("DE32").toString());
			 //System.out.println("12121212121");
			 isoMsg.set(37, preIso.get("DE37").toString());
			 //System.out.println("131313131313");
			 //System.out.println("auth code:"+details.getAuthCode());
			 isoMsg.set(38, details.getAuthCode());
			 //System.out.println("yyyyyyyyyyyy");
			 isoMsg.set(39, "22");
			 //System.out.println("14141414141");
			 isoMsg.set(41, preIso.get("DE41").toString());
			 //System.out.println("151515151");
			 isoMsg.set(42, preIso.get("DE42").toString());
			 //.out.println("1616161616");
			 isoMsg.set(49, preIso.get("DE49").toString());
			// System.out.println("1717177171");
			 if(entity.getTxnType().equalsIgnoreCase("CASHBACK")){
				 //System.out.println("1818818181");
				 isoMsg.set(54, preIso.get("DE54").toString());
			 }
			 //System.out.println("zzzzzzzzzzzzzzzz");
			 isoMsg.set(61, preIso.get("DE61").toString());
			 String de90 = "0200" +  preIso.get("DE11").toString() + preIso.get("DE07").toString() +preIso.get("DE32").toString() +"00000000000";//"0000072039900000000000";
			 //System.out.println("de90:"+de90);
			 isoMsg.set(90, de90);
			 //isoMsg.set(106, preIso.get("DE106").toString());
			 
			 System.out.println("cccccccccccccccccccccccccccccccc");
			 
			}catch(Exception e){
				System.err.println("error to fetch txn details "+e);
				response.setStatus(EnumStatusConstant.RollBackError.getId());
				response.setMessage(EnumStatusConstant.RollBackError.getDescription());
				return response;
			}finally{
				session.close();
			}
			 try{
				 logISOMsg(isoMsg);
				 byte[] data = isoMsg.pack();
				 String plainIsoo = new String(data);
				 String ogsResponse = OGSCardDesc.newNetworkTransport(ksn+plainIsoo,"2");
				 String subStr = ogsResponse.substring(22);
				 JSONObject jsonn = getResponseCode(subStr,"doReversal");
				 if(jsonn.containsKey("DE39")){
					 if(jsonn.get("DE39").equals("00")){
						 Session sess = null;
						 Transaction tx = null;
						 try{
							 sess = getNewSession();
							 System.out.println("33333333333");
							 AcqPlaceTransactionEntity entity = (AcqPlaceTransactionEntity)sess.createCriteria(AcqPlaceTransactionEntity.class).add(Restrictions.eq("id",Integer.valueOf(model.getTxnId()))).uniqueResult();
							 if(entity==null){
								 logger.warn("error to get entity ");
							 }else{
								 entity.setStatus(504);
								 entity.setDescription("Reversal Successful");
							 }
							 tx = sess.beginTransaction();
							 sess.update(entity);
							 tx.commit();
							 System.out.println("reveral success");
						}catch(Exception e){
							logger.error("error to save ogs void txn");
						}finally{
							sess.close();
						}
						response.setStatus(EnumStatusConstant.OK.getId());
						response.setMessage(EnumStatusConstant.OK.getDescription());
					}else{
						response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
						response.setMessage("Reversal Failed");
					}
				}else{
					response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
					response.setMessage("Reversal Failed");
				}		
				 System.out.println("444444444");
			 }catch(Exception e){
				 System.err.println("error "+e);
			 }
			 System.out.println("55555555555555");
			 //System.out.println("reversal response:"+reversalRequestIso);
			 response.setStatus(0);
			 response.setMessage("OK");
		}catch(Exception e){
		 	System.out.println("error to get reversal "+e);
		}
		System.out.println("66666666666");
		return response;
	}
	
	JSONObject preIsoParser(String requestIsoo){		
		JSONObject obj = new JSONObject();	    
		String finalIso = requestIsoo.substring(20);
		
	      try {
	   	  ISOMsg isoMessage = new ISOMsg();
	   	  System.out.println("going to unparsing");
	      		packe  = new GenericPackager("purchaseTxn.xml");
	      		isoMessage.setPackager(packe);
		   	isoMessage.unpack(finalIso.getBytes());
		   	System.out.println("unparsing done");
	      		return logISOMsggReversalNew(isoMessage);
	      }catch(Exception e) {
	    	  System.err.println("return parsed void obj "+e);

		        
	         logger.error("return parsed void obj "+e);
	         return obj;
	      }
	}
	
	@Override
	public DbDto<Object> vm20DoPrchsWthCshBck(OGSMainTxnInitiateModel model) {
		DbDto<Object> response = new DbDto<Object>();
		logger.info("req in purchs wthcshbk dao");
		JSONObject jsonCashBackObj = new JSONObject();
		int txnId=0;
		Session session = null;
		Transaction tx = null;
		Timestamp currentTimestamp = null;
		Date date = null;
		try{
			if(model.getIsoData().get("DE02")==null||AcqOGSApiValidator.isDE02(model.getIsoData().get("DE02").toString())==false){
				response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
				response.setMessage("Invalid DE02");
				response.setResult(jsonCashBackObj);
				return response;
			}if(model.getIsoData().get("ksn")==null||AcqOGSApiValidator.isKsn(model.getIsoData().get("ksn").toString())==false){
				response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
				response.setMessage("Invalid KSN");
				response.setResult(jsonCashBackObj);
				return response;
			}if(model.getIsoData().get("DE04")==null||AcqOGSApiValidator.isDE04(model.getIsoData().get("DE04").toString())==false){
				response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
				response.setMessage("Invalid DE04");
				response.setResult(jsonCashBackObj);
				return response;
			}if(model.getIsoData().get("DE11")==null||AcqOGSApiValidator.isDE11(model.getIsoData().get("DE11").toString())==false){
				response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
				response.setMessage("Invalid DE11");
				response.setResult(jsonCashBackObj);
				return response;
			}if(model.getIsoData().get("DE14")==null||AcqOGSApiValidator.isDE14(model.getIsoData().get("DE14").toString())==false){
				response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
				response.setMessage("Invalid DE14");
				response.setResult(jsonCashBackObj);
				return response;
			}if(model.getIsoData().get("DE23")==null||AcqOGSApiValidator.isDE23(model.getIsoData().get("DE23").toString())==false){
				response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
				response.setMessage("Invalid DE23");
				response.setResult(jsonCashBackObj);
				return response;
			}if(model.getIsoData().get("DE35")==null||AcqOGSApiValidator.isDE35(model.getIsoData().get("DE35").toString())==false){
				response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
				response.setMessage("Invalid DE35");
				response.setResult(jsonCashBackObj);
				return response;
			}if(model.getIsoData().get("DE40")==null||AcqOGSApiValidator.isDE40(model.getIsoData().get("DE40").toString())==false){
				response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
				response.setMessage("Invalid DE40");
				response.setResult(jsonCashBackObj);
				return response;
			}if(model.getIsoData().get("DE52")==null||AcqOGSApiValidator.isDE52(model.getIsoData().get("DE52").toString())==false){
				response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
				response.setMessage("Invalid DE52");
				response.setResult(jsonCashBackObj);
				return response;
			}if(model.getIsoData().get("DE55")==null||AcqOGSApiValidator.isDE55(model.getIsoData().get("DE55").toString())==false){
				response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
				response.setMessage("Invalid DE55");
				response.setResult(jsonCashBackObj);
				return response;
			}if(model.getIsoData().get("DE106")==null||AcqOGSApiValidator.isDE106(model.getIsoData().get("DE106").toString())==false){
				response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
				response.setMessage("Invalid DE106");
				response.setResult(jsonCashBackObj);
				return response;
			}
			System.out.println("cashback data persistance started");			
			AcqSettingEntity settingEnt = null;
			AcqCardBinDesc binDescEnt = null;
			String de61 = null;
			try {			
			     //System.out.println("de61 cashback:"+model.getDe61());
				 SimpleDateFormat dateFormatGmt = new SimpleDateFormat("MMddHHmmss");
			        String dateTime = dateFormatGmt.format(new Date());
				 int hod =  Integer.valueOf(dateTime.substring(4,6));
			        String hods = "";
			        if(hod<10){
			            hods = "0"+hod;
			        }else{
			            hods = ""+hod;
			        }
			        Calendar cal = Calendar.getInstance();
			        String year = ""+cal.get(Calendar.YEAR);
			        String charYear = year.substring(3,4);
			        int doy = cal.get(Calendar.DAY_OF_YEAR);
			        String doys ="";
			        if(doy<10){
			            doys = "00"+doy;
			        }else if(doy>10&&doy<100){
			            doys = "0"+doy;
			        }else{
			            doys = ""+doy;
			        }
			        dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
			        String gmtDateTime = dateFormatGmt.format(new Date());
			        String rrNo = charYear+doys+hods;//+model.getIsoData().get("DE11");
				model.setGmtDateTime(gmtDateTime);
				System.out.println("sessionId : "+model.getSessionId());
				session = getNewSession();
				tx=session.beginTransaction();
				//jsonn
				
				AcqKeyExchangeEntity keyEntity = (AcqKeyExchangeEntity) session.createCriteria(AcqKeyExchangeEntity.class).add(Restrictions.eq("tid",model.getDeviceId())).add(Restrictions.eq("userId",Long.valueOf(model.getSessionId()))).uniqueResult();
		        if(keyEntity==null){
		        	response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
		        	response.setMessage("Transaction can't place,Key Exchange not verified");
		        	response.setResult(jsonCashBackObj);
		        	return response;
		        }
		        model.setMcc(keyEntity.getMcc());
		        model.setAcquirerId(keyEntity.getAcquirerId());
				AcqUserEntity userEnt = (AcqUserEntity) session.get(
						AcqUserEntity.class, Long.valueOf(model.getSessionId()));
				if(userEnt==null){
					response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
					response.setMessage("User not found for card transaction");
					response.setResult(jsonCashBackObj);
					return response;
				}
				AcqSettingEntity settingEnt1 = (AcqSettingEntity) session
						.createCriteria(AcqSettingEntity.class)
						.add(Restrictions.eq("id", 1l)).uniqueResult();
				if(settingEnt1.getMaintenanceMode().equals("1")&&!settingEnt1.getMaintenanceReason().equalsIgnoreCase("NA")){
					logger.info(",Maintenance mode is on, transaction can't be place");
					response.setStatus(EnumStatusConstant.MaintenanceMode.getId());
					response.setMessage(EnumStatusConstant.MaintenanceMode.getDescription());
					//response.setResult(responseMap);
					return response;
				}
				//System.out.println("ccccccccccccccc sssssssssssssssss");
				AcqOrganization org = (AcqOrganization)session.get(AcqOrganization.class, userEnt.getOrgId());					
				System.out.println("ddddddddddddddd sssssssssssssssss");
				AcqMerchantEntity merchantEnt = (AcqMerchantEntity)session.createCriteria(AcqMerchantEntity.class).add(Restrictions.eq("merchantId",""+org.getMerchantId())).uniqueResult();
				if(merchantEnt==null){
					logger.info("Merchant Id not found frm db");
					response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
					response.setMessage("Merchant not found for card transaction");
					response.setResult(jsonCashBackObj);
					return response;
				}
				model.setMid(merchantEnt.getMerchantTID());
				model.setMerchantAddress(merchantEnt.getAddress1());
				model.setMerchantPinCode(merchantEnt.getMerchantPinCode());
				AcqMapDeviceUserEntity deviceEnt = (AcqMapDeviceUserEntity) session.createCriteria(AcqMapDeviceUserEntity.class).add(Restrictions.eq("userId",Long.valueOf(model.getSessionId()))).add(Restrictions.or(Restrictions.or(Property.forName("deviceType").eq("credit"),Property.forName("deviceType").eq("wallet/credit")))).uniqueResult();
				if (deviceEnt == null || deviceEnt + "" == "") {
					response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
					response.setMessage("User details not found for card transction");
					response.setResult(jsonCashBackObj);
					return response;
				} else {
					System.out.println("eeeeeeeeeeeeeeeeee sssssssssssssssss");
					logger.info("user id is "+model.getSessionId());
					try {
						binDescEnt = new AcqCardBinDesc();
		                binDescEnt.setCardType(getCardFromPan(model.getMaskPan()));
		                model.setCardType(binDescEnt.getCardType());
						de61 = OGSCardDesc.generateDe61New(Boolean.valueOf(model.getPinEntered()),Boolean.valueOf(model.getEmv()),model.getIsoData().get("DE52").toString(),model.getMerchantAddress(),model.getMerchantPinCode());
					    model.setDe61(de61);
						model.setTid(deviceEnt.getBankTid());
						AcqTxnEntity txnEnt = new AcqTxnEntity();
						txnEnt.setUserid(Integer.valueOf(""+userEnt.getUserId()));						
						txnEnt.setOrgId(Integer.valueOf(""+userEnt.getOrgId()));
						txnEnt.setMerchantId(Integer.valueOf(""+org.getMerchantId()));
						txnEnt.setMobile("0000");
						Double amt = Double.valueOf(model.getIsoData().get("DE04").toString())/100;
						txnEnt.setAmount(amt.toString());
						if(!userEnt.getAcquirerCode().equalsIgnoreCase("Acquiro")){
							System.out.println("fffffffffffffff sssssssssssssssss");
							settingEnt = (AcqSettingEntity) session
									.createCriteria(AcqSettingEntity.class)
									.add(Restrictions.eq("acquirerCode", userEnt.getAcquirerCode())).uniqueResult();
							if(settingEnt.getMaintenanceMode().equalsIgnoreCase("1")&&!settingEnt.getMaintenanceReason().equalsIgnoreCase("NA")){
								logger.info(",Acquirer server maintenance mode is on, transaction can't be place");
								response.setStatus(EnumStatusConstant.MaintenanceMode.getId());
								response.setMessage(EnumStatusConstant.MaintenanceMode.getDescription());
								//response.setResult(responseMap);
								return response;
							}
								txnEnt.setEzMdr("0.0");
								txnEnt.setBankMdr("0.0");
								txnEnt.setAcquirerMdr("0.0");
								if(Double.valueOf(model.getIsoData().get("DE04").toString())<=Double.valueOf(settingEnt.getTxnTaxAmt())){
									txnEnt.setServiceTax("0.0");
								}else{
									txnEnt.setServiceTax(settingEnt.getServiceTax());
								}								
								if(Double.valueOf(model.getIsoData().get("DE04").toString())<Double.valueOf(settingEnt.getSystmUtltyAmt())){
									txnEnt.setSystemUtilityFee(settingEnt.getSystmUtltyFee());
								}else{
									txnEnt.setSystemUtilityFee("0.0");
								}
						}else{
							System.out.println("gggggggggggggggg sssssssssssssssss");
							settingEnt = (AcqSettingEntity) session
									.createCriteria(AcqSettingEntity.class)
									.add(Restrictions.eq("id", 1l)).uniqueResult();
								txnEnt.setEzMdr("0.0");
								txnEnt.setBankMdr("0.0");
								txnEnt.setAcquirerMdr("0.0");
								if(Double.valueOf(model.getIsoData().get("DE04").toString())<=Double.valueOf(settingEnt1.getTxnTaxAmt())){
									txnEnt.setServiceTax("0.0");
								}else{
									txnEnt.setServiceTax(settingEnt1.getServiceTax());
								}
								if(Double.valueOf(model.getIsoData().get("DE04").toString())<Double.valueOf(settingEnt.getSystmUtltyAmt())){
									txnEnt.setSystemUtilityFee(settingEnt.getSystmUtltyFee());
								}else{
									txnEnt.setSystemUtilityFee("0.0");
								}
							//}
						}
						date= new Date();
						currentTimestamp= new Timestamp(date.getTime());
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						txnEnt.setOtpDateTime(sdf.format(date));
						//txnEnt.setOtpDateTime(currentTimestamp);
						txnEnt.setDateTime(currentTimestamp);
						txnEnt.setStatus(503);
						txnEnt.setMobile("NA");
						txnEnt.setEmail("NA");
						txnEnt.setTxnType("CASHBACK");
						txnEnt.setDescription("Transaction Initiated");
						txnEnt.setCarPanNo(model.getMaskPan());
						txnEnt.setCustomerId("0");
						txnEnt.setPayoutStatus(700);
						txnEnt.setPayoutDateTime(currentTimestamp);
						txnEnt.setSwitchLab("OGS");
						txnEnt.setAcquirerCode(userEnt.getAcquirerCode());
						if(model.getIsoData().containsKey("DE54")){
							double cashBkAmt = Double.valueOf(model.getIsoData().get("DE54").toString())/100;
							txnEnt.setCashBackAmt(""+new DecimalFormat("#.##").format(cashBkAmt));
						}
						txnEnt.setAcqPayoutStatus(800);
						txnEnt.setAcqPayoutDateTime(currentTimestamp);
						if(model.getApplicationCertificate()==null||model.getApplicationCertificate()==""){
							txnEnt.setAppCertificate("NA");
						}else{
							txnEnt.setAppCertificate(model.getApplicationCertificate());
						}
						if(model.getAid()==null||model.getAid()==""){
							txnEnt.setAid("NA");
						}else
							txnEnt.setAid(model.getAid());
						txnEnt.setScriptResult("NA");			
						session.save(txnEnt);
						txnId = txnEnt.getId();
						AcqCardDetails cardEntity = new AcqCardDetails();
						//System.out.println("txnEnt.getId(): "+txnEnt.getId());
						cardEntity.setTransactionId(txnEnt.getId());
						//cardEntity.setTerminalId("123123");
						cardEntity.setTerminalId(deviceEnt.getBankTid());
						String stan = String.format("%06d", txnEnt.getId());
						if(stan.length()<=6){
							model.setStan(stan);
						}else{
							model.setStan(stan.substring(stan.length()-6));
						}
						cardEntity.setStan(model.getStan());
						stan=null;
						cardEntity.setRrNo(rrNo+model.getStan());
						cardEntity.setAuthCode("NA");
						cardEntity.setBatchNo("0");
						if(model.getCardHolderName()==null||model.getCardHolderName()==""){
							cardEntity.setCardHolderName("NA");
						}else{
							cardEntity.setCardHolderName(model.getCardHolderName());
						}
						cardEntity.setCardType("NA");
						cardEntity.setIpAddress(model.getIpAddress());
						cardEntity.setImeiNo("123123");
						cardEntity.setLatitude(model.getLatitude());
						cardEntity.setLongitude(model.getLongitude());
						//cardEntity.setStan("NA");//CustomerMobile(model.get)
						cardEntity.setGmtDateTime("NA");
						cardEntity.setDe61(model.getDe61());
						if(model.getEmv().equalsIgnoreCase("true")){
							cardEntity.setCardType(model.getCardType()+"|Chip");
						}else{
							cardEntity.setCardType(model.getCardType()+"|Swipe");
						}
						cardEntity.setPinEntered(model.getPinEntered());
						if(model.getApplicationName()==null||model.getApplicationName()==""){
							cardEntity.setApplicationName("NA");
						}else{
							cardEntity.setApplicationName(model.getApplicationName());
						}
						session.save(cardEntity);
						tx.commit();
						model.setDe12((""+txnEnt.getOtpDateTime()).substring(11,19).replace(":",""));
						model.setRrNo(cardEntity.getRrNo());
						//System.out.println("localtime::::::"+model.getDe12());							
					} catch (Exception e) {
						response.setStatus(EnumStatusConstant.RollBackError.getId());
						response.setMessage(EnumStatusConstant.RollBackError.getDescription());
						logger.error("error to save txn details" +e);
						System.err.println("error to save txn details" +e);
						return response;
					}
				}
			} catch (Exception e) {
				response.setStatus(EnumStatusConstant.RollBackError.getId());
				response.setMessage(EnumStatusConstant.RollBackError
						.getDescription());
				logger.error("error to persist data "+e);
				System.out.println("error to save entity "+e);
				return response;
			} finally {
				session.close();
			}			
			System.out.println("cashback persistance done");		
			jsonCashBackObj.put("transactionId", txnId+"");
			if(txnId==0||txnId<=1){
				response.setStatus(EnumStatusConstant.RollBackError.getId());
				response.setMessage("Technical Error,Trasaction id not generated");
				response.setResult(jsonCashBackObj);
				logger.warn("error to generate txnid");
				return response;
			}
			String purchaseRequestIso = purchaseCashBackTxn(model);
			model.setRequestIso(model.getIsoData().get("ksn")+purchaseRequestIso);
			String ogsResponse = OGSCardDesc.newNetworkTransport(model.getIsoData().get("ksn")+purchaseRequestIso,model.getUat());
			model.setRespnseIso(ogsResponse);
			jsonCashBackObj.put("transactionId", txnId+"");
			System.out.println("cash back ver111111: "+ogsResponse);
			if(ogsResponse!=null&&ogsResponse.equalsIgnoreCase("SocketException")){
				response.setStatus(EnumStatusConstant.TransactionTimeOut.getId());
				response.setMessage(EnumStatusConstant.TransactionTimeOut.getDescription());
				jsonCashBackObj.put("gmtDateTime",model.getGmtDateTime());
				response.setResult(jsonCashBackObj);
				return response;
			}
			String subStr = ogsResponse.substring(22);
			jsonCashBackObj = getResponseCode(subStr,"doCashBack");			
			System.out.println("cashback done: "+txnId);
			jsonCashBackObj.put("transactionId", txnId+"");
			response.setStatus(EnumStatusConstant.OK.getId());
			response.setMessage(EnumStatusConstant.OK.getDescription());
			response.setResult(jsonCashBackObj);
			System.out.println("cashback final jsonObj:"+jsonCashBackObj);
			Transaction txx = null;
			Session sessionn = null;
			try{
				sessionn = getNewSession();
				AcqCardDetails ent = (AcqCardDetails)sessionn.createCriteria(AcqCardDetails.class).add(Restrictions.eq("transactionId",Integer.valueOf(txnId))).uniqueResult();
				if(ent==null){
					logger.error("ent is null");
				}else{
					AcqPlaceTransactionEntity mainEnt = (AcqPlaceTransactionEntity)sessionn.createCriteria(AcqPlaceTransactionEntity.class).add(Restrictions.eq("id",Integer.valueOf(txnId))).uniqueResult();
					if(mainEnt!=null&&jsonCashBackObj.containsKey("responseMessage")){
						mainEnt.setDescription(jsonCashBackObj.get("responseMessage").toString());
						sessionn.update(mainEnt);
					}

					if(jsonCashBackObj.containsKey("DE38"))
						ent.setAuthCode(jsonCashBackObj.get("DE38").toString());
					if(jsonCashBackObj.containsKey("DE11"))
						ent.setStan(jsonCashBackObj.get("DE11").toString());
					if(jsonCashBackObj.containsKey("DE12"))
						ent.setBatchNo(jsonCashBackObj.get("DE12").toString());					
					if(model.getGmtDateTime()!=null)
						ent.setGmtDateTime(model.getGmtDateTime());						
					txx = sessionn.beginTransaction();
					sessionn.update(ent);
					try{
						date = new Date();
						currentTimestamp = new Timestamp(date.getTime());
						AcqOgsISOEntity isoEnt = new AcqOgsISOEntity();
						isoEnt.setTransactionId(txnId+"");//""+txnEnt.getId());
						isoEnt.setIsoRequest(model.getRequestIso());
						isoEnt.setIsoResponse(model.getRespnseIso());
						isoEnt.setSwitchLab("OGS");
						isoEnt.setRequestTime(model.getRequestTime());
						isoEnt.setResponseTime(currentTimestamp.toString());
						sessionn.save(isoEnt);
					}catch(Exception e){
						logger.error("error to save iso message" +e);
						System.err.println("error to save iso message" +e);
					}
					txx.commit();
				}
			}catch(Exception e){
				logger.error("error to update entity "+e);
				System.out.println("error to save auth code "+e);
			}finally{
				if(sessionn.isOpen()==true||sessionn.isConnected()==true){
					sessionn.close();
				}
			}
			System.out.println("finally done ");
			return response;
		}catch(Exception e){
			response.setStatus(EnumStatusConstant.RollBackError.getId());
			response.setMessage(EnumStatusConstant.RollBackError.getDescription());
			response.setResult(emptyJson);
			System.err.println("error to call purchase txn "+e);
			return response;
		}
	
	}
	
	@Override
	public DbDto<Object> mv20OGSVoidTxnInit(OGSMainVoidTxnModel model){
		logger.info("req in ogs main voidtxn dao");
		DbDto<Object> response = new DbDto<Object>();
		JSONObject voidJsonn = new JSONObject();
		try{
			if(model.getIsoData().get("DE02")==null||AcqOGSApiValidator.isDE02(model.getIsoData().get("DE02").toString())==false){
				response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
				response.setMessage("Invalid DE02");
				response.setResult(voidJsonn);
				return response;
			}if(model.getIsoData().get("ksn")==null||AcqOGSApiValidator.isKsn(model.getIsoData().get("ksn").toString())==false){
				response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
				response.setMessage("Invalid KSN");
				response.setResult(voidJsonn);
				return response;
			}if(model.getIsoData().get("DE04")==null||AcqOGSApiValidator.isDE04(model.getIsoData().get("DE04").toString())==false){
				response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
				response.setMessage("Invalid DE04");
				response.setResult(voidJsonn);
				return response;
			}if(model.getIsoData().get("DE11")==null||AcqOGSApiValidator.isDE11(model.getIsoData().get("DE11").toString())==false){
				response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
				response.setMessage("Invalid DE11");
				response.setResult(voidJsonn);
				return response;
			}if(model.getIsoData().get("DE23")==null||AcqOGSApiValidator.isDE23(model.getIsoData().get("DE23").toString())==false){
				response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
				response.setMessage("Invalid DE23");
				response.setResult(voidJsonn);
				return response;
			}				
			System.out.println("aaaaaaaaaaaaaa");
			
			Session session1 = null;
			try{
				session1  = getNewSession();
				AcqKeyExchangeEntity keyEntity = (AcqKeyExchangeEntity) session1.createCriteria(AcqKeyExchangeEntity.class).add(Restrictions.eq("tid",model.getDeviceId())).add(Restrictions.eq("userId",Long.valueOf(model.getSessionId()))).uniqueResult();
		        if(keyEntity==null){
		        	response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
		        	response.setMessage("Transaction can't void,Key Exchange not verified");
		        	response.setResult(voidJsonn);
		        	return response;
		        }
		        System.out.println("bbbbbbbbbbbbbbbb");
		        model.setAcquirerId(keyEntity.getAcquirerId());
				AcqUserEntity userEnt = (AcqUserEntity) session1.get(
						AcqUserEntity.class, Long.valueOf(model.getSessionId()));
				if(userEnt==null){
					response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
					response.setMessage("User not found for card transaction");
					response.setResult(voidJsonn);
					return response;
				}
				System.out.println("ccccccccccccccccc");
				AcqOrganization org = (AcqOrganization)session1.get(AcqOrganization.class, userEnt.getOrgId());					
				if(org==null){
					logger.info("Org not found frm db");
					response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
					response.setMessage("Org not found for card transaction");
					response.setResult(voidJsonn);
					return response;
				}
				System.out.println("dddddddddddddddddd");
				AcqMerchantEntity merchantEnt = (AcqMerchantEntity)session1.createCriteria(AcqMerchantEntity.class).add(Restrictions.eq("merchantId",""+org.getMerchantId())).uniqueResult();
				if(merchantEnt==null){
					logger.info("Merchant not found frm db");
					response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
					response.setMessage("Merchant not found for card transaction");
					response.setResult(voidJsonn);
					return response;
				}
				System.out.println("eeeeeeeeeeeeeeeee");
				model.setMid(merchantEnt.getMerchantTID());
				AcqMapDeviceUserEntity deviceEnt = (AcqMapDeviceUserEntity) session1.createCriteria(AcqMapDeviceUserEntity.class).add(Restrictions.eq("userId",Long.valueOf(model.getSessionId()))).add(Restrictions.or(Restrictions.or(Property.forName("deviceType").eq("credit"),Property.forName("deviceType").eq("wallet/credit")))).uniqueResult();
				if (deviceEnt == null || deviceEnt + "" == "") {
						response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
						response.setMessage("User details not found for card transction");
						response.setResult(voidJsonn);
						return response;
				}
				System.out.println("fffffffffffffffffffff");
				model.setTid(deviceEnt.getBankTid());
				System.out.println("transactionId is:"+model.getTransactionId());
				AcqCardDetails details = (AcqCardDetails)session1.createCriteria(AcqCardDetails.class).add(Restrictions.eq("transactionId",Integer.valueOf(model.getTransactionId()))).uniqueResult();
				System.out.println("ggggggggggggggggggg");
				if(details==null){
					response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
					response.setMessage("Invalid Transaction Id");
					response.setResult(voidJsonn);
					logger.warn("Invalid transaction id");
					return response;
				}else{
					model.setDe61(details.getDe61());
					model.setGmtDateTime(details.getGmtDateTime());
					model.setStan(details.getStan());
					model.setRrNo(details.getRrNo());
					//model.setRrNo(model.getRrNo());
				}
				System.out.println("de61:::::::in void: "+model.getDe61());
				System.out.println("de gmtdatetime:::::::in void: "+model.getGmtDateTime());
				AcqPlaceTransactionEntity txnEnt = (AcqPlaceTransactionEntity)session1.createCriteria(AcqPlaceTransactionEntity.class).add(Restrictions.eq("id",Integer.valueOf(model.getTransactionId()))).add(Restrictions.eq("userid",Integer.valueOf(model.getSessionId()))).uniqueResult();
				model.setLocalTime((""+txnEnt.getOtpDateTime()).substring(11,19).replace(":",""));
				//Double cashBkAmt = Double.valueOf(txnEnt.getCashBackAmt())/100;
				//model.setAddtionalAmount(""+new DecimalFormat("#.##").format(cashBkAmt));
				model.setAddtionalAmount(txnEnt.getCashBackAmt());
				System.out.println("model.getAdditionalAmt():"+model.getAddtionalAmount());
				//model.setAddtionalAmount(txnEnt.getCashBackAmt());
				model.setTxnType(txnEnt.getTxnType());
				System.out.println("model.localtime:"+model.getLocalTime());
				System.out.println("model.additionalamount:"+model.getLocalTime());
				System.out.println("model.txntype:"+model.getTxnType());
			}catch(Exception e){
				logger.error("error to get tid,mid "+e);
				response.setStatus(EnumStatusConstant.RollBackError.getId());
				response.setMessage(EnumStatusConstant.RollBackError.getDescription());
				response.setResult(voidJsonn);
				return response;
			}finally{
				session1.close();
			}
			System.out.println("going create request iso");
			String voidRequestIso = voidTxn(model);
			System.out.println("void RequestIso:"+voidRequestIso);
			String ogsResponse = OGSCardDesc.newNetworkTransport(model.getIsoData().get("ksn")+voidRequestIso,model.getUat());
			System.out.println("void ogsResponse:"+ogsResponse);
			if(ogsResponse!=null&&ogsResponse.equalsIgnoreCase("SocketException")){
				response.setStatus(EnumStatusConstant.TransactionTimeOut.getId());
				response.setMessage(EnumStatusConstant.TransactionTimeOut.getDescription());
				response.setResult(null);
				return response;
			}
			String subStr = ogsResponse.substring(22);
			System.out.println("subStr:"+subStr);
			voidJsonn = getResponseCode(subStr,"doVoid");
			if(voidJsonn.isEmpty()){
				response.setStatus(EnumStatusConstant.RollBackError.getId());
				response.setMessage("Error to parse iso or res xml not found");
				response.setResult(voidJsonn);
				return response;
			}
			if(voidJsonn.containsKey("DE39")){
				if(voidJsonn.get("DE39").equals("00")){
					response.setMessage(EnumStatusConstant.OK.getDescription());
					Session session = null;
					Transaction tx = null;
					try {
						System.out.println("aaaaaaaaaaaaaaa");
						System.out.println("rr no id :"+voidJsonn.get("DE37").toString());
						session = getNewSession();
						Disjunction or = Restrictions.disjunction();
						or.add(Restrictions.eq("rrNo",voidJsonn.get("DE37").toString()));
						or.add(Restrictions.eq("rrNo","-"+voidJsonn.get("DE37").toString()));
						List<AcqCardDetails> cardDetails = (List<AcqCardDetails>)session.createCriteria(AcqCardDetails.class).add(or).list();
						System.out.println("bbbbbbbbbbbbbbb");
						logger.info("txn cardDetailscardDetails: "+cardDetails.size());
						if (cardDetails.isEmpty()) {
							System.out.println("ccccccccccc");
							response.setStatus(EnumStatusConstant.TransactionNotFound.getId());
							response.setMessage(EnumStatusConstant.TransactionNotFound.getDescription());
							logger.info("ogs transaction not found agains given rrno");
							return response;
						}else if(cardDetails.size()>=2){
							System.out.println("ddddddddddd");
							response.setStatus(EnumStatusConstant.AlreadyInUse.getId());
							response.setMessage("Transaction already voided");
							logger.info("ogs transaction already voided");
							return response;
						}else{
							System.out.println("eeeeeeeeeeeee");
							logger.info("ogs txn card details are found agains given rrno");	
							AcqPlaceTransactionEntity entity = null;
							AcqPlaceTransactionEntity txnEntity =null;
							for(AcqCardDetails details:cardDetails){
								System.out.println("ffffffffffffffff");								
								List<String> txnStatus = new ArrayList<String>();
								try{
								txnStatus.add("CARD");txnStatus.add("CASHATPOS");txnStatus.add("CASHBACK");
								System.out.println("txnStatus:"+txnStatus);
								System.out.println("txnStatus:"+details.getTransactionId());
								
								txnEntity = (AcqPlaceTransactionEntity)session.createCriteria(AcqPlaceTransactionEntity.class).add(Restrictions.eq("switchLab","OGS")).add(Restrictions.eq("payoutStatus",700)).add(Restrictions.in("txnType",txnStatus)).add(Restrictions.eq("status",505)).add(Restrictions.eq("id",details.getTransactionId())).uniqueResult();
								if(txnEntity!=null){
									System.out.println("ggggggggggggggg");									
									logger.info("ogs transaction details found agains given rrno");
									entity = new AcqPlaceTransactionEntity();
									System.out.println("1111111");
									if(txnEntity.getMobile()==null||txnEntity.getMobile()==""){
										entity.setMobile("");
									}else{
										entity.setMobile(txnEntity.getMobile());
									}
									System.out.println("2222222222");
									entity.setAmount("-"+txnEntity.getAmount());
									System.out.println("333333");
									if(txnEntity.getEmail()==null||txnEntity.getEmail()==""){
										entity.setEmail("");
									}else{
										entity.setEmail(txnEntity.getEmail());
									}
									System.out.println("44444444");
									entity.setStatus(txnEntity.getStatus());
									System.out.println("5555555");
									entity.setUserid(txnEntity.getUserid());
									System.out.println("66666666");
									entity.setMerchantId(txnEntity.getMerchantId());
									System.out.println("7777777777");
									entity.setOrgId(txnEntity.getOrgId());
									System.out.println("888888888");
									entity.setBankMdr(txnEntity.getBankMdr());
									System.out.println("99999999");
									entity.setEzMdr(txnEntity.getEzMdr());
									System.out.println("101010101");
									entity.setServiceTax(txnEntity.getServiceTax());
									System.out.println("121212121212");
									entity.setDateTime(txnEntity.getDateTime());
									System.out.println("131313131313");
									Date date= new Date();
									System.out.println("hhhhhhhhhhhhhhh");
									Timestamp currentTimestamp= new Timestamp(date.getTime());
									entity.setOtpDateTime(currentTimestamp);
									//entity.setOtpDateTime(txnEntity.getOtpDateTime());
									entity.setPayoutStatus(700);
									System.out.println("iiiiiiiiiiiiiiiiii");
									entity.setPayoutDateTime(txnEntity.getPayoutDateTime());
									entity.setDescription("Transaction Void Successful");
									System.out.println("entity.getTxnType(): "+txnEntity.getTxnType());
									if(txnEntity.getTxnType().equalsIgnoreCase("CASHATPOS")){
										entity.setTxnType("CVOID");
									}else if(txnEntity.getTxnType().equalsIgnoreCase("CASHBACK")){
										entity.setTxnType("CBVOID");
									}else if(txnEntity.getTxnType().equalsIgnoreCase("CARD")){
										entity.setTxnType("VOID");
									}
									System.out.println("jjjjjjjjjjjjjjjjjjjj");
									entity.setCarPanNo(txnEntity.getCarPanNo());
									entity.setCustomerId(txnEntity.getCustomerId());
									entity.setAcquirerCode(txnEntity.getAcquirerCode());
									entity.setCashBackAmt(txnEntity.getCashBackAmt());
									entity.setSwitchLab(txnEntity.getSwitchLab());
									entity.setAcquirerMdr(txnEntity.getAcquirerMdr());
									session.save(entity);
									System.out.println("kkkkkkkkkkkkkkk");
									logger.info("transaction details saved");
									AcqCardDetails newCardDetails = new AcqCardDetails();
									newCardDetails.setTransactionId(entity.getId());
									newCardDetails.setTerminalId(details.getTerminalId());
									newCardDetails.setCardHolderName(details.getCardHolderName());
									newCardDetails.setCardType(details.getCardType());
									//newCardDetails.setCardExpDate(details.getCardExpDate());
									newCardDetails.setIpAddress(details.getIpAddress());
									newCardDetails.setImeiNo(details.getImeiNo());
									newCardDetails.setLatitude(details.getLatitude());
									newCardDetails.setLongitude(details.getLongitude());
									newCardDetails.setRrNo("-"+details.getRrNo());
									newCardDetails.setAuthCode(voidJsonn.get("DE38").toString());
									newCardDetails.setBatchNo(details.getBatchNo());
									System.out.println("LLLLlllllllllllll");
									//newCardDetails.setIssureAuthCode(details.getIssureAuthCode());
									//newCardDetails.setReferenceNo(details.getReferenceNo());
									newCardDetails.setStan(details.getStan());
									session.save(newCardDetails);
									logger.info("card details saved");									
									System.out.println("mmmmmmmmmmmmmmmmm");
									model.setAmount(txnEntity.getAmount());									
									System.out.println("nnnnnnnnnnnnnnn");
									try{
										AcqRiskManagement riskEnt = (AcqRiskManagement)session.createCriteria(AcqRiskManagement.class).add(Restrictions.eq("txnId",txnEntity.getId())).uniqueResult(); 
										if(riskEnt!=null||riskEnt+""!=""){
											riskEnt.setStatus("0");
											session.save(riskEnt);
										}
									}catch(Exception e){
										logger.error("error to update risk transaction status "+e);
									}
									System.out.println("ooooooooooooooo");	
									//model.setDescription(""+entity.getDescription());
									tx = session.beginTransaction();
									tx.commit();
									System.out.println("pppppppppppppppp");
									logger.info("transaction voided");									
								}
								}catch(Exception e){
									System.err.println("error in save void txn "+e);
								}
							}
							response.setStatus(EnumStatusConstant.OK.getId());
							response.setMessage(EnumStatusConstant.OK.getDescription());
							response.setResult(voidJsonn);
							logger.info("response returning from card transaction dao");
							return response;
						}
					}catch (Exception e) {
						response.setStatus(EnumStatusConstant.RollBackError.getId());
						response.setMessage(EnumStatusConstant.RollBackError.getDescription());
						logger.error("Error to select transaction details "+e);
						tx.rollback();
						return response;
					}finally{
						session.close();
					}
				}else{
					System.out.println("sssssssssssssss");
					response.setStatus(EnumStatusConstant.OK.getId());
					response.setMessage("Void Failed");
					response.setResult(voidJsonn);
					return response;
				}
			}else{
				System.out.println("tttttttttttttt");
				response.setStatus(EnumStatusConstant.OK.getId());
				response.setMessage("Void Failed");
				response.setResult(voidJsonn);
				return response;
			}
		}catch(Exception e){
			logger.error("error in vm20 void txn dao "+e);
			response.setMessage(EnumStatusConstant.RollBackError.getDescription());
			response.setStatus(EnumStatusConstant.RollBackError.getId());
			response.setResult(voidJsonn);
		}		
		System.out.println("vvvvvvvvvvvvvvvvvv");
		return response;
	}
	public static String voidTxn(OGSMainVoidTxnModel model)
            throws ISOException, IOException {

		
        String result = "";
        try {
            GenericPackager packager = new GenericPackager("purchaseTxn.xml");
            // Create ISO Message
            ISOMsg isoMsg = new ISOMsg();
            isoMsg.setPackager(packager);
            isoMsg.setMTI("0420");
            SimpleDateFormat dateFormatGmt = new SimpleDateFormat("MMddHHmmss");
            String dateTime = dateFormatGmt.format(new Date());
            int hod = Integer.valueOf(dateTime.substring(4, 6));
            String hods = "";
            if (hod < 10) {
                hods = "0" + hod;
            } else {
                hods = "" + hod;
            }
            String MMdd = dateTime.substring(0, 4);
            String hhmmss = dateTime.substring(4, 10);
            Calendar cal = Calendar.getInstance();
            String year = "" + cal.get(Calendar.YEAR);
            String charYear = year.substring(3, 4);
            int doy = cal.get(Calendar.DAY_OF_YEAR);
            String doys = "";
            if (doy < 10) {
                doys = "00" + doy;
            } else if (doy > 10 && doy < 100) {
                doys = "0" + doy;
            } else {
                doys = "" + doy;
            }
            //System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaa");
            //String rrNo = charYear+doys+hods+model.getStan();//model.getIsoData().get("DE11");
            dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
            isoMsg.set(2, model.getIsoData().get("DE02").toString());
            String gmtDateTime = dateFormatGmt.format(new Date());
            //System.out.println("bbbbbbbbbbbbbbbbbbbbbbbbbbbbb");
            if(model.getTxnType().equals("CASHBACK")) {
                isoMsg.set(3, "090000");  // procesing code
            }else if(model.getTxnType().equals("CARD")){
                isoMsg.set(3, "000000");  // procesing code
            }else if(model.getTxnType().equals("CASHATPOS")){
                isoMsg.set(3, "010000");  // procesing code
            }
            //System.out.println("cccccccccccccccccccccccccccccccc");
            String longAmount = String.format("%012d", Integer
                    .valueOf(model.getAmount().replace(".","")));
            isoMsg.set(4, longAmount);  // txn amount
            isoMsg.set(7, gmtDateTime); //txn date time
            //isoMsg.set(7, model.getGmtDateTime()); //txn date time
           // String resId = String.format("%06d", Integer.valueOf(model.getTxnId()));
            isoMsg.set(11, model.getStan()); //Stan value
            //String localTime = model.getLocalTime();
            isoMsg.set(12, model.getLocalTime()); //Time, local transaction hhmmss
            isoMsg.set(13, MMdd); //Date,local transaction // MMdd           
            if(model.getEmv().equals("")){
                isoMsg.set(22, "951"); //POSentrymode  //TODO change according to card type swipe -> 901/ chip -> 951
            }else{
                isoMsg.set(22, "901"); //POSentrymode  //TODO change according to card type swipe -> 901/ chip -> 951
            }
            if(model.getIsoData().get("DE23")!=null&&model.getIsoData().get("DE23").toString().length()>0&&!model.getIsoData().get("DE23").toString().equals("000")){
            	isoMsg.set(23, model.getIsoData().get("DE23").toString());
            }
            isoMsg.set(25, "00"); //POSconditioncode // space is coming for de25, have you any idea? are you there?wait
            //isoMsg.set(32, "720399"); //Acquiringinstitutioncode //apending 25
            //isoMsg.set(32, "00201000003"); 
            isoMsg.set(32, model.getAcquirerId());
            isoMsg.set(37, model.getRrNo()); //Retrieval reference number
            if(model.getIsoData().get("DE38")!=null&&model.getIsoData().get("DE38").toString().length()>0&&!model.getIsoData().get("DE38").toString().equals("000")){
            	isoMsg.set(38, model.getIsoData().get("DE38").toString());
            }            
            isoMsg.set(39, "17");
    		System.out.println("reversal tid:::"+model.getTid());
    		isoMsg.set(41, model.getTid()); //Cardacceptor terminalID
    		System.out.println("reversal mid:::"+model.getMid());
    		isoMsg.set(42, model.getMid());	
            isoMsg.set(49, "356");        //Currencycode, transaction //it will be static till new fields add
           if(model.getTxnType().equals("CASHBACK")) {
                String cashBackAmount = String.format("%012d",
                        Integer.valueOf(model.getAddtionalAmount().replace(".","")));
                isoMsg.set(54, "9090356D" +cashBackAmount); // cash back amount
            }

            if(model.getEmv().equals("True")){
                isoMsg.set(55,model.getScriptResponse());
            }
            if(model.getDe61()!=null){
            	isoMsg.set(61,model.getDe61());
            }
            //String de90 = "0200" + resId + voidTxnObject.getTxnTime() + "0000072039900000000000";
            String de90 = "0200" + model.getStan() +model.getGmtDateTime() +model.getAcquirerId() + "00000000000";//00000720399
            isoMsg.set(90, de90);  
            String voidIso = null;
            try{
            	logISOMsg(isoMsg);
            	byte[] data = isoMsg.pack();
            	voidIso = new String(data);
            	return voidIso;
            }catch(Exception e){
            	System.err.println("err log void iso msg "+e);
            }
            return voidIso;
        }catch (ISOException e) {
            e.printStackTrace();
            return null;
        }
        
    }
	
	@Override
	public DbDto<Object> mv20OGSRvrslTxnInit(OGSMainReversalTxnModel model) {
		logger.info("req in rvrsl txn dao");
		DbDto<Object> response = new DbDto<Object>();
		JSONObject jsonn = new JSONObject();
		try{			
			if(model.getIsoData().get("DE02")==null||AcqOGSApiValidator.isDE02(model.getIsoData().get("DE02").toString())==false){
				response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
				response.setMessage("Invalid DE02");
				response.setResult(jsonn);
				return response;
			}if(model.getIsoData().get("ksn")==null||AcqOGSApiValidator.isKsn(model.getIsoData().get("ksn").toString())==false){
				response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
				response.setMessage("Invalid KSN");
				response.setResult(jsonn);
				return response;
			}if(model.getIsoData().get("DE04")==null||AcqOGSApiValidator.isDE04(model.getIsoData().get("DE04").toString())==false){
				response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
				response.setMessage("Invalid DE04");
				response.setResult(jsonn);
				return response;
			}if(model.getIsoData().get("DE07")==null||AcqOGSApiValidator.isDE04(model.getIsoData().get("DE04").toString())==false){
				response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
				response.setMessage("Invalid DE07");
				response.setResult(jsonn);
				return response;
			}if(model.getIsoData().get("DE11")==null||AcqOGSApiValidator.isDE11(model.getIsoData().get("DE11").toString())==false){
				response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
				response.setMessage("Invalid DE11");
				response.setResult(jsonn);
				return response;
			}if(model.getIsoData().get("DE23")==null||AcqOGSApiValidator.isDE23(model.getIsoData().get("DE23").toString())==false){
				response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
				response.setMessage("Invalid DE23");
				response.setResult(jsonn);
				return response;
			}if(model.getIsoData().get("DE39")==null||AcqOGSApiValidator.isDE39(model.getIsoData().get("DE39").toString())==false){
				response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
				response.setMessage("Invalid DE39");
				response.setResult(jsonn);
				return response;
			}if(model.getIsoData().get("DE55")==null||AcqOGSApiValidator.isDE55(model.getIsoData().get("DE55").toString())==false){
				response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
				response.setMessage("Invalid DE55");
				response.setResult(jsonn);
				return response;
			}
			Session session1 = null;
			try{				
				session1  = getNewSession();
				AcqKeyExchangeEntity keyEntity = (AcqKeyExchangeEntity) session1.createCriteria(AcqKeyExchangeEntity.class).add(Restrictions.eq("tid",model.getDeviceId())).add(Restrictions.eq("userId",Long.valueOf(model.getSessionId()))).uniqueResult();
		        if(keyEntity==null){
		        	response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
		        	response.setMessage("Transaction can't reversal,Key Exchange not verified");
		        	response.setResult(jsonn);
		        	return response;
		        }
		        model.setAcquirerId(keyEntity.getAcquirerId());
				AcqUserEntity userEnt = (AcqUserEntity) session1.get(
						AcqUserEntity.class, Long.valueOf(model.getSessionId()));
				if(userEnt==null){
					response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
					response.setMessage("User not found for card transaction");
					response.setResult(jsonn);
					return response;
				}
				AcqOrganization org = (AcqOrganization)session1.get(AcqOrganization.class, userEnt.getOrgId());					
				if(org==null){
					logger.info("Org Id not found frm db");
					response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
					response.setMessage("Org not found for card transaction");
					response.setResult(jsonn);
					return response;
				}
				AcqMerchantEntity merchantEnt = (AcqMerchantEntity)session1.createCriteria(AcqMerchantEntity.class).add(Restrictions.eq("merchantId",""+org.getMerchantId())).uniqueResult();
				if(merchantEnt==null){
					logger.info("Merchant Id not found frm db");
					response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
					response.setMessage("Merchant not found for card transaction");
					response.setResult(jsonn);
					return response;
				}
				model.setMid(merchantEnt.getMerchantTID());
				model.setMerchantAddress(merchantEnt.getAddress1());
				model.setMerchantPinCode(merchantEnt.getMerchantPinCode());
				AcqMapDeviceUserEntity deviceEnt = (AcqMapDeviceUserEntity) session1.createCriteria(AcqMapDeviceUserEntity.class).add(Restrictions.eq("userId",Long.valueOf(model.getSessionId()))).add(Restrictions.or(Restrictions.or(Property.forName("deviceType").eq("credit"),Property.forName("deviceType").eq("wallet/credit")))).uniqueResult();
				if (deviceEnt == null || deviceEnt + "" == "") {
					response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
					response.setMessage("User details not found for card transction");
					response.setResult(jsonn);
					return response;
				}
				model.setTid(deviceEnt.getBankTid());				
				AcqCardDetails details = (AcqCardDetails)session1.createCriteria(AcqCardDetails.class).add(Restrictions.eq("transactionId",Integer.valueOf(model.getTransactionId()))).uniqueResult();
				if(details==null){
					logger.warn("card details is empty");
				}else{
					model.setDe61(details.getDe61());
					model.setDe38(details.getAuthCode());
					model.setStan(details.getStan());
					model.setRrNo(details.getRrNo());
				}
				System.out.println("de61:::::::in reversal: "+model.getDe61());
				System.out.println("de38:::::::in reversal: "+model.getDe38());
				AcqPlaceTransactionEntity txnEnt = (AcqPlaceTransactionEntity)session1.createCriteria(AcqPlaceTransactionEntity.class).add(Restrictions.eq("id",Integer.valueOf(model.getTransactionId()))).add(Restrictions.eq("userid",Integer.valueOf(model.getSessionId()))).uniqueResult();
				model.setDe12((""+txnEnt.getOtpDateTime()).substring(11,19).replace(":",""));
				model.setDe54(txnEnt.getCashBackAmt());
				model.setTxnType(txnEnt.getTxnType());
				System.out.println("model.geDe12:"+model.getDe12());
				
			}catch(Exception e){
				System.out.println("error to fetch de61 "+e);
				response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
				response.setMessage("Technical Issue, Please try again");
				response.setResult(jsonn);
				logger.error("error to fetch de61"+e);//1231-12-12 				
				return response;
			}finally{
				session1.close();
			}
			String purchaseRequestIso = reversal(model);
			System.out.println("rvrsl RequestIso:"+purchaseRequestIso);
			String ogsResponse = OGSCardDesc.newNetworkTransport(model.getIsoData().get("ksn")+purchaseRequestIso,model.getUat());
			if(ogsResponse!=null&&ogsResponse.equalsIgnoreCase("SocketException")){
				response.setStatus(EnumStatusConstant.TransactionTimeOut.getId());
				response.setMessage(EnumStatusConstant.TransactionTimeOut.getDescription());
				response.setResult(null);
				return response;
			}
			System.out.println("rvrsl ogsResponse:"+ogsResponse);
			String subStr = ogsResponse.substring(22);
			System.out.println("subStr:"+subStr);
			jsonn = getResponseCode(subStr,"doReversal");
			if(jsonn.containsKey("DE39")){
				if(jsonn.get("DE39").equals("00")){
					Session session = null;
					Transaction tx = null;
					try{
						session = getNewSession();
						AcqPlaceTransactionEntity entity = (AcqPlaceTransactionEntity)session.createCriteria(AcqPlaceTransactionEntity.class).add(Restrictions.eq("id",Integer.valueOf(model.getTransactionId()))).uniqueResult();
						if(entity==null){
							logger.warn("error to get entity ");
						}else{
							entity.setStatus(504);
							entity.setDescription("Reversal Successful");
						}
						tx = session.beginTransaction();
						session.update(entity);
						tx.commit();
						System.out.println("reveral success");
					}catch(Exception e){
						logger.error("error to save ogs void txn");
					}finally{
						session.close();
					}
					response.setStatus(EnumStatusConstant.OK.getId());
					response.setMessage(EnumStatusConstant.OK.getDescription());
				}else{
					response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
					response.setMessage("Reversal Failed");
				}
			}else{
				response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
				response.setMessage("Reversal Failed");
			}			
			response.setResult(jsonn);
			
		}catch(Exception e){
			logger.error("error in rvrsl txn dao "+e);
			response.setMessage(EnumStatusConstant.RollBackError.getDescription());
			response.setStatus(EnumStatusConstant.RollBackError.getId());
			response.setResult(jsonn);
		}
		return response;
	}
	
	 static String plainIso = "";
	 public static String reversal(OGSMainReversalTxnModel model){ /*reversalObjectString id,String rrn,
	            String cardPan,String amount,String track2Data,String txnTime){*/
		 try {
			 SimpleDateFormat dateFormatGmt = new SimpleDateFormat("MMddHHmmss");
			 String dateTime = dateFormatGmt.format(new Date());
			 String MMdd = dateTime.substring(0, 4);
			 String hhmmss = dateTime.substring(4, 10);
			 dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
			 String gmtDateTime = dateFormatGmt.format(new Date());			
			 GenericPackager packager = new GenericPackager("purchaseTxn.xml");
			 ISOMsg isoMsg = new ISOMsg();
			 isoMsg.setPackager(packager);
			 isoMsg.setMTI("0420");
			 int hod =  Integer.valueOf(dateTime.substring(4,6));
			 String hods = "";
			 if(hod<10){
				 hods = "0"+hod;
	         }else{
	        	 hods = ""+hod;
	         }
			 Calendar cal = Calendar.getInstance();
			 String year = ""+cal.get(Calendar.YEAR);
			 String charYear = year.substring(3,4);
			 int doy = cal.get(Calendar.DAY_OF_YEAR);
			 String doys ="";
			 if(doy<10){
				 doys = "00"+doy;
	         }else if(doy>10&&doy<100){
	        	 doys = "0"+doy;
	         }else{
	        	 doys = ""+doy;
	         }    		
			 isoMsg.set(2, model.getIsoData().get("DE02").toString());
			 //System.out.println("------------aaaaaaaaaaaa");
			 if(model.getIsoData().containsKey("DE03")==false||model.getIsoData().get("DE03")==null||model.getIsoData().get("DE03").equals("")){
				 isoMsg.set(3, "000000");  // procesing code
				 System.out.println("------------bbbbbbbbbbbb");
			 }else{
				 isoMsg.set(3,model.getIsoData().get("DE03").toString());
				 System.out.println("------------cccccccccccccccccc");
			 }
			 //String rrNo = charYear+doys+hods+model.getStan();//model.getIsoData().get("DE11");
			 isoMsg.set(4, model.getIsoData().get("DE04").toString());  // txn amount
			 isoMsg.set(7, gmtDateTime); //txn date time
			 //isoMsg.set(11, model.getIsoData().get("DE11").toString());//system trace audit number
			 System.out.println("stan is:"+model.getStan());
			 isoMsg.set(11,model.getStan());
			 //isoMsg.set(12, hhmmss); //Time, local transaction hhmmss
			 System.out.println("de12::::::::"+model.getDe12());
			 isoMsg.set(12,model.getDe12());
			 isoMsg.set(13, MMdd); //Date,local transaction // MMdd
			 int i = 1;
			 //System.out.println("mcc:" + mcc);
			 if(model.getEmv().equals("")){
				 isoMsg.set(22, "951"); //POSentrymode  //TODO change according to card type swipe -> 901/ chip -> 951
			 }else{
				 isoMsg.set(22, "901"); //POSentrymode  //TODO change according to card type swipe -> 901/ chip -> 951
			 }
			 if(model.getIsoData().containsKey("DE23")&&model.getIsoData().get("DE23").toString().length()>1){
				 isoMsg.set(23, model.getIsoData().get("DE23").toString());
			 }
			 //isoMsg.set(23, model.getIsoData().get("DE23").toString());
			 //isoMsg.set(22, "901"); //POSentrymode  //static
			 isoMsg.set(25, "00"); //POSconditioncode // space is coming for de25, have you any idea? are you there?wait
			 //isoMsg.set(32, "720399"); //Acquiringinstitutioncode //apending 25
			 //isoMsg.set(32, "00201000003"); 
			 isoMsg.set(32, model.getAcquirerId());
			 //isoMsg.set(35, reversalObject.getTrack2Data());
			 //System.out.println("bbbbbbbbbbbbbbbbbbbbb");
			 isoMsg.set(37, model.getRrNo());
			 System.out.println("model.getDe38():::"+model.getDe38());
			 if(model.getDe38()==null||model.getDe38()==""||model.getDe38().equalsIgnoreCase("NA")){
			 }else{				 
				 isoMsg.set(38, model.getDe38());
			 }
			 isoMsg.set(39, model.getIsoData().get("DE39").toString());
			 System.out.println("reversal tid:::"+model.getTid());
			 isoMsg.set(41, model.getTid()); //Cardacceptor terminalID
			 System.out.println("reversal mid:::"+model.getMid());
			 isoMsg.set(42, model.getMid());	    		
			 isoMsg.set(49, "356");        //Currencycode, transaction //it will be static till new fields add
			 System.out.println("ppppppppppppppp");
			 String de90 = "0200" + model.getStan() + model.getIsoData().get("DE07").toString() + model.getAcquirerId()+"00000000000";//"0000072039900000000000";
			 System.out.println("de90:"+de90);
			 if(model.getTxnType().equals("CASHBACK")) {
				 int index = model.getDe54().indexOf('.');
				 String cashbackAmt = model.getDe54().substring(0,index);
	             //System.out.println("cashbackAmt:"+cashbackAmt);
	             String cashBackAmount = String.format("%012d",
	             Integer.valueOf(cashbackAmt));
	             isoMsg.set(54, "9090356D" +cashBackAmount); // cash back amount
	             //System.out.println("final cashBackAmount:"+cashBackAmount);
	         }			 
			 System.out.println("de55:" + model.getIsoData().containsKey("DE55"));
			 if(model.getIsoData().containsKey("DE55")&&model.getIsoData().get("DE55").toString().length()>1){
				 isoMsg.set(55, model.getIsoData().get("DE55").toString());
			 }
			 System.out.println("555 55555555555");
			 /*if(model.getIsoData().get("DE61")!=null&&model.getIsoData().get("DE61").toString().length()>0&&!model.getIsoData().get("DE61").toString().equals("000")){
				 isoMsg.set(61, model.getIsoData().get("DE61").toString());
			 }*/
			 System.out.println("model.getDe61():::"+model.getDe61());
			 if(model.getDe61()==null||model.getDe61()==""){
				 
			 }else{
				 isoMsg.set(61, model.getDe61());
			 }
			 isoMsg.set(90, de90);
			 //System.out.println("cccccccccccccccccccccccccccccccc");
			 try{
				 logISOMsg(isoMsg);
				 byte[] data = isoMsg.pack();
				 plainIso = new String(data);
				 System.out.println("plainIso:"+plainIso);
			 }catch(Exception e){
				 System.err.println("error "+e);
			 }
			 return  plainIso;
	    }catch (Exception e) {	
	    	System.err.println("erroreeeer  "+e);
	    	logger.error("error to parse revesal iso "+e);
	    	return plainIso;
	    }
	 }
	
	@Override
	public DbDto<Object> mv20OGSMainGetDUKPTACK(AcqOGSMainKeyExchangeModel model) {
		logger.info("req in getdukpt ack dao");
		DbDto<Object> response = new DbDto<Object>();
		GenericPackager packager;
		JSONObject json = new JSONObject();
		try{
			//System.out.println("dukptack aaaaaaaaaaaa");
			packager = new GenericPackager("keyExchange.xml");
			//System.out.println("dukptack bbbbbbbbbbbbbb");
			dateFormatGmt = new SimpleDateFormat("MMddHHmmss");
			ISOMsg isoMsg = new ISOMsg();
			//System.out.println("dukptack cccccccccccc");
			isoMsg.setPackager(packager);
			isoMsg.setMTI(EnumOgsConstant.KeyExchangeMTI.getId());
			isoMsg.set(7,dateFormatGmt.format(new Date()).toString());
			//String sytmTraceNo = String.format("%06d",model.getStan());
			//System.out.println("dukptack ddddddddddddddddd");
			isoMsg.set(11,model.getStan());
	        Calendar cal1 = Calendar.getInstance();
	        //System.out.println("dukptack eeeeeeeeeeeeeeeee");
	        String dateTime = dateFormatGmt.format(new Date());
			int hod =  Integer.valueOf(dateTime.substring(4,6));
			//System.out.println("dukptack fffffffffffffffff");
			String hods = "";
			if(hod<10){
				hods = "0"+hod;
			}else{
				hods = ""+hod;
			}
			//System.out.println("dukptack ggggggggggggg");
			String year = ""+cal1.get(Calendar.YEAR);
			String charYear = year.substring(3,4);
			int doy = cal1.get(Calendar.DAY_OF_YEAR);
			logger.info("doy:"+doy);
			//System.out.println("dukptack hhhhhhhhhhhhhhh");
			String doys ="";
			if(doy<10){
				doys = "00"+doy;
			}else if(doy>10&&doy<100){
				doys = "0"+doy;
			}else{
				doys = ""+doy;
			}
			//System.out.println("dukptack iiiiiiiiiiiiii");
			//isoMsg.set(44,"1025160122123457");
			//System.out.println("model.getDe44()::"+model.getDe44());
			isoMsg.set(44,model.getDe44());
			//System.out.println("dukptack jjjjjjjjjjjjjj");
	      //  isoMsg.set(37,(charYear+doys+hods+model.getStan()));
			isoMsg.set(37,(charYear+doys+hods+"100004"));
	        //System.out.println("dukptack kkkkkkkkkkkkkk");
	        isoMsg.set(61,model.getDeviceId());
	        isoMsg.set(70,"192");
	        System.out.println("dukptack lllllllll");
	        logISOMasssge(isoMsg);
	        System.out.println("key eeeeeeeeeeee");
			logger.info("iso params completed");
			byte[] data = isoMsg.pack();
			String res = null;
			//System.out.println("key fffffffffffffffff");
			try {
				res = OGSCardDesc.keyExNetworkTransport(new String(data),model.getUat());
				if(res!=null&&res.equalsIgnoreCase("SocketException")){
					response.setStatus(EnumStatusConstant.TransactionTimeOut.getId());
					response.setMessage(EnumStatusConstant.TransactionTimeOut.getDescription());
					response.setResult(null);
					return response;
				}
			}catch(Exception e){
				logger.error("error to connect ogs "+e);
			}
			System.out.println("dukpt ogs res:"+res);
			//System.out.println("key ggggggggggggggggg");
			if(res!=null&&res.equals("error to connect ogs")){
				response.setStatus(EnumStatusConstant.IssuerConnectIssue.getId());
				response.setMessage(EnumStatusConstant.IssuerConnectIssue.getDescription());
				response.setResult(null);
				return response;
			}
			//System.out.println("hhhhhhhhhhhhh");
			String ress = res.substring(2);
			String bitmapStr1 = ress.substring(4,20);
			String hex2Binary1 = getHex2Binary(bitmapStr1);
			String bitmapStr2 ="";
			//System.out.println("iiiiiiiiiiiii");
			if(hex2Binary1.startsWith("1")){
				bitmapStr2 = ress.substring(4,36);
				//System.out.println("bitmapStr2:"+bitmapStr2);
				bitmapStr2 = getHex2Binary(bitmapStr2);
			}
			//System.out.println("jjjjjjjjjjjjjjjj");
			logger.info("BITMAP:"+bitmapStr2);
			try {
				System.out.println("kkkkkk");
				 GenericPackager packe = new GenericPackager("key_response.xml");
					System.out.println("LLLLLLLLLLLl");
				 ISOMsg isoMassage = new ISOMsg();
					//System.out.println("mmmmmmmmmmm");
		    	 isoMassage.setPackager(packe);
		    		//System.out.println("nnnnnnnnnnnnn");
		    	 isoMassage.unpack(ress.getBytes());
		    		//System.out.println("ooooooo");
		    	 // print the DE list
		    	 json=  keyExResDUKPTACKISOMasssge(isoMassage);
		    	 if(json.containsKey("DE39")&&json.get("DE39").toString().equalsIgnoreCase("00")){
		    		System.out.println("ppppp");
		    		Session resses = null;
		    		Transaction tx = null;
		    		try{
		    			resses = getNewSession();
		    			AcqKeyExchangeEntity keyEntity = (AcqKeyExchangeEntity)resses.createCriteria(AcqKeyExchangeEntity.class).add(Restrictions.eq("tid", model.getDeviceId())).add(Restrictions.eq("userId", Long.valueOf(model.getSessionId()))).uniqueResult();
		    			if(keyEntity==null){	    					
		    			 	logger.info("not updated");	    					
		    			}else{
		    				keyEntity.setStatus("False");
		    				tx = resses.beginTransaction();
		    				resses.update(keyEntity);
		    				tx.commit();
		    			 	logger.info("sts updated");
		    			}
		    		 }catch(Exception e){
		    			 logger.error("error update key "+e);
		    			 tx.rollback();
		    		 }finally{
		    			 resses.close();
		    		 }
		    	 }
//		    	 json.put("aidArray", getAidArray());
//		    	 json.put("capkArray", getCapkArray());
		    	 
		    	 System.out.println("getTMK response: "+json);
		    	 response.setMessage(EnumStatusConstant.OK.getDescription());
		    	 response.setStatus(EnumStatusConstant.OK.getId());
		    	 response.setResult(json);	
			}catch(Exception e){
				logger.error("error in rvrsl txn dao "+e);
				response.setMessage(EnumStatusConstant.RollBackError.getDescription());
				response.setStatus(EnumStatusConstant.RollBackError.getId());
				response.setResult(json);
			}
		}catch(Exception e){
			logger.error("error in dukptack dao "+e);
			response.setMessage(EnumStatusConstant.RollBackError.getDescription());
			response.setStatus(EnumStatusConstant.RollBackError.getId());
			response.setResult(json);
		}
		return response;
	}

	@Override
	public DbDto<Object> getDUKPTOgsMain(AcqOGSMainKeyExchangeModel model) {
		logger.info("req in getdukpt dao");
		DbDto<Object> response = new DbDto<Object>();
		GenericPackager packager;
		Session session = null;
		JSONObject dukptJson = new JSONObject();
		AcqKeyExchangeEntity keyEntity = null;
		try{
			ISOMsg isoMsg = new ISOMsg();			
			try{				
				session =  getNewSession();
				AcqOrganization orgEnt = null;
				AcqMerchantEntity merchantEnt = null;				
				AcqUserEntity usrEnt = (AcqUserEntity)session.createCriteria(AcqUserEntity.class).add(Restrictions.eq("userId", Long.valueOf(model.getSessionId()))).uniqueResult();
				if(usrEnt==null){
					response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
					response.setMessage("Invalid User");
					response.setResult(null);
					return response;
				}
				orgEnt = (AcqOrganization)session.createCriteria(AcqOrganization.class).add(Restrictions.eq("id", Long.valueOf(usrEnt.getOrgId()))).uniqueResult();
				if(orgEnt==null){
					response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
					response.setMessage("Invalid User");
					response.setResult(null);
					return response;
				}
				merchantEnt = (AcqMerchantEntity)session.createCriteria(AcqMerchantEntity.class).add(Restrictions.eq("merchantId", ""+orgEnt.getMerchantId())).uniqueResult();
				if(merchantEnt==null){
					response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
					response.setMessage("Invalid User");
					response.setResult(null);
					return response;
				}
				System.out.println("merchant d: "+merchantEnt);
				System.out.println("merchant dddd: "+merchantEnt.getMerchantId());
				packager = new GenericPackager("keyExchange.xml");
				dateFormatGmt = new SimpleDateFormat("MMddHHmmss");	
				isoMsg.setPackager(packager);
				isoMsg.setMTI(EnumOgsConstant.KeyExchangeMTI.getId());
				isoMsg.set(7,dateFormatGmt.format(new Date()).toString());
		        isoMsg.set(11,model.getStan());
		        Calendar cal1 = Calendar.getInstance();
		        String dateTime = dateFormatGmt.format(new Date());
				int hod =  Integer.valueOf(dateTime.substring(4,6));
				System.out.println("key bbbbbbbbbbbbbbbb");
				String hods = "";
				if(hod<10){
					hods = "0"+hod;
				}else{
					hods = ""+hod;
				}
				String year = ""+cal1.get(Calendar.YEAR);
				String charYear = year.substring(3,4);
				int doy = cal1.get(Calendar.DAY_OF_YEAR);
				logger.info("doy:"+doy);
				String doys ="";
				if(doy<10){
					doys = "00"+doy;
				}else if(doy>10&&doy<100){
					doys = "0"+doy;
				}else{
					doys = ""+doy;
				}
				System.out.println("aaaa 1111111111");
		       // isoMsg.set(37,(charYear+doys+hods+model.getStan()));
				isoMsg.set(37,(charYear+doys+hods+"100003"));
		        //System.out.println("merchantEnt.getMerchantTID():"+merchantEnt.getMerchantTID());
		        isoMsg.set(42,merchantEnt.getMerchantTID());
		        System.out.println("aaaa 22222222222");
		        isoMsg.set(61,model.getDeviceId());
		        System.out.println("aaaa 333333333333");
		        isoMsg.set(70,"191");
		        System.out.println("key 4444444444444444");
			}catch(Exception e){
				logger.error("error in get dukpt dao "+e);
				System.err.println("error in get dukpt dao "+e);
				response.setMessage(EnumStatusConstant.RollBackError.getDescription());
				response.setStatus(EnumStatusConstant.RollBackError.getId());
				response.setResult(dukptJson);
				return response;
			}finally{
				session.close();
			}
			System.out.println("dukpt request iso:"+isoMsg);
	        logISOMasssge(isoMsg);
	        System.out.println("key 5555555555555");
			logger.info("iso params completed");
			byte[] data = isoMsg.pack();
			String res = null;
			System.out.println("key fffffffffffffffff");			
			try {
				res = OGSCardDesc.keyExNetworkTransport(new String(data),model.getUat());
				if(res!=null&&res.equalsIgnoreCase("SocketException")){
					response.setStatus(EnumStatusConstant.TransactionTimeOut.getId());
					response.setMessage(EnumStatusConstant.TransactionTimeOut.getDescription());
					response.setResult(null);
					return response;
				}
			}catch(Exception e){
				logger.error("error to connect ogs "+e);
				System.err.println("error to connect ogs"+e);
			}
			System.out.println("key ggggggggggggggggg");
			System.out.println("ogs res for dukpt:"+res);
			if(res!=null&&res.equals("error to connect ogs")){
				response.setStatus(EnumStatusConstant.IssuerConnectIssue.getId());
				response.setMessage(EnumStatusConstant.IssuerConnectIssue.getDescription());
				response.setResult(null);
				return response;
			}
			System.out.println("ressress:"+res);
			String ress = res.substring(2);
			String bitmapStr1 = ress.substring(4,20);
			String hex2Binary1 = getHex2Binary(bitmapStr1);
			String bitmapStr2 ="";
			System.out.println("kkkkkkkkkkkkkkkkk");
			if(hex2Binary1.startsWith("1")){
				bitmapStr2 = ress.substring(4,36);
				//System.out.println("bitmapStr2:"+bitmapStr2);
				bitmapStr2 = getHex2Binary(bitmapStr2);
			}
			//logger.info("BITMAP:"+bitmapStr2);
			try {
				System.out.println("res aaaaaaaaaaaa");
				GenericPackager packe = new GenericPackager("key_response.xml");
				System.out.println("res bbbbbbbbbbbbb");
				ISOMsg isoMassage = new ISOMsg();
				System.out.println("res 222222222");
				isoMassage.setPackager(packe);
		    	//System.out.println("res 3333333333333");
		    	isoMassage.unpack(ress.getBytes());
		    	dukptJson=  keyExResDUKPTISOMasssge(isoMassage);
		    	System.out.println("res parsed");
		    	System.out.println("getDUKPT response: "+dukptJson);
		    	if(dukptJson.containsKey("DE63")){
		    		Session resses = null;
		    		String mcc="";
		    		String acquirerId="";
		    		//String termintalOwnerName = "";
 			        //String terminalCity = "";	    			        
 			       // String terminalState = "";
		    		try{
		    			resses = getNewSession();
		    			keyEntity = (AcqKeyExchangeEntity)resses.createCriteria(AcqKeyExchangeEntity.class).add(Restrictions.eq("tid", model.getDeviceId())).add(Restrictions.eq("userId", Long.valueOf(model.getSessionId()))).uniqueResult();
		    			String[] merchantDataArray = dukptJson.get("DE63").toString().split("\\|"); //Merchant Data from key exchange's response
	    				mcc = merchantDataArray[4];
	    				acquirerId = merchantDataArray[3];
	    				if(keyEntity==null){
	    					AcqKeyExchangeEntity ent = new AcqKeyExchangeEntity();
		    				dateFormatGmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    			 	ent.setUserId(Long.valueOf(model.getSessionId()));
		    			 	ent.setDateTime(dateFormatGmt.format(new Date()));
		    			 	ent.setTid(model.getDeviceId());
		    			 	ent.setMcc(mcc);
		    			 	ent.setAcquirerId(acquirerId);
		    			 	ent.setStatus("True");
		    			 	resses.save(ent);
		    			 	logger.info("device details saved");	    					
		    			}else{
		    				keyEntity.setMcc(mcc);
		    				keyEntity.setAcquirerId(acquirerId);
		    				keyEntity.setStatus("True");
		    				resses.update(keyEntity);
		    			 	logger.info("device updated");
		    			}
		    		 }catch(Exception e){
		    			 logger.error("error save/update "+e);
		    		 }finally{
		    			 resses.close();
		    		 }
		    	 }
		    	 response.setMessage(EnumStatusConstant.OK.getDescription());
		    	 response.setStatus(EnumStatusConstant.OK.getId());
		    	 response.setResult(dukptJson);	
		    	 logger.info("res from dukpt dao");
			}catch(Exception e){
				System.err.println("error in dukpt txn dao "+e);
				response.setMessage(EnumStatusConstant.RollBackError.getDescription());
				response.setStatus(EnumStatusConstant.RollBackError.getId());
				response.setResult(dukptJson);
			}
		}catch(Exception e){
			logger.error("error in get dukpt dao "+e);
			System.err.println("error in get dukpt dao "+e);
			response.setMessage(EnumStatusConstant.RollBackError.getDescription());
			response.setStatus(EnumStatusConstant.RollBackError.getId());
			response.setResult(dukptJson);
		}
		return response;
	}
	
	@Override
	public DbDto<Object> mv20OGSMainGetTMKACK(AcqOGSMainKeyExchangeModel model) {
		logger.info("req in gettmk ack dao");
		DbDto<Object> response = new DbDto<Object>();
		GenericPackager packager;
		JSONObject json = new JSONObject();
		if(model.getDe44()==null){
			response.setMessage("Invalid DE44");
			response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
			response.setResult(json);
			return response;
		}
		try{
			packager = new GenericPackager("keyExchange.xml");
			dateFormatGmt = new SimpleDateFormat("MMddHHmmss");
			ISOMsg isoMsg = new ISOMsg();
			isoMsg.setPackager(packager);
			isoMsg.setMTI(EnumOgsConstant.KeyExchangeMTI.getId());
			isoMsg.set(7,dateFormatGmt.format(new Date()).toString());
			//String sytmTraceNo = String.format("%06d",model.getStan());
	        isoMsg.set(11,model.getStan());
	        Calendar cal1 = Calendar.getInstance();
	        String dateTime = dateFormatGmt.format(new Date());
			int hod =  Integer.valueOf(dateTime.substring(4,6));
			//System.out.println("key bbbbbbbbbbbbbbbb");
			String hods = "";
			if(hod<10){
				hods = "0"+hod;
			}else{
				hods = ""+hod;
			}
			String year = ""+cal1.get(Calendar.YEAR);
			String charYear = year.substring(3,4);
			int doy = cal1.get(Calendar.DAY_OF_YEAR);
			logger.info("doy:"+doy);
			String doys ="";
			if(doy<10){
				doys = "00"+doy;
			}else if(doy>10&&doy<100){
				doys = "0"+doy;
			}else{
				doys = ""+doy;
			}
			//System.out.println("key ccccccccccccccccccccccc");
			isoMsg.set(44,model.getDe44());
	      //  isoMsg.set(37,(charYear+doys+hods+model.getStan()));
			isoMsg.set(37,(charYear+doys+hods+"100002"));
	        isoMsg.set(61,model.getDeviceId());
	        isoMsg.set(70,"183");
	        //System.out.println("key dddddddddddddd");
	        logISOMasssge(isoMsg);
	        //System.out.println("key eeeeeeeeeeee");
			logger.info("iso params completed");
			byte[] data = isoMsg.pack();
			String res = null;
			//System.out.println("key fffffffffffffffff");
			try {
				res = OGSCardDesc.keyExNetworkTransport(new String(data),model.getUat());
				if(res!=null&&res.equalsIgnoreCase("SocketException")){
					response.setStatus(EnumStatusConstant.TransactionTimeOut.getId());
					response.setMessage(EnumStatusConstant.TransactionTimeOut.getDescription());
					response.setResult(null);
					return response;
				}
			}catch(Exception e){
				logger.error("error to connect ogs "+e);
			}
			//System.out.println("key ggggggggggggggggg");
			if(res!=null&&res.equals("error to connect ogs")){
				response.setStatus(EnumStatusConstant.IssuerConnectIssue.getId());
				response.setMessage(EnumStatusConstant.IssuerConnectIssue.getDescription());
				response.setResult(null);
				return response;
			}
			//System.out.println("key ack hhhhhhhhhhhh");
			//System.out.println("res:::"+res);
			String ress = res.substring(2);
			String bitmapStr1 = ress.substring(4,20);
			String hex2Binary1 = getHex2Binary(bitmapStr1);
			String bitmapStr2 ="";
			System.out.println("key ack iiiiiiiiiiiii");
			if(hex2Binary1.startsWith("1")){
				bitmapStr2 = ress.substring(4,36);
				//System.out.println("bitmapStr2:"+bitmapStr2);
				bitmapStr2 = getHex2Binary(bitmapStr2);
			}
			//System.out.println("key ack jjjjjjjjjjjjjjj");
			logger.info("BITMAP:"+bitmapStr2);
			try {
				//System.out.println("key ack kkkkkkkkkkkkk");
				 GenericPackager packe = new GenericPackager("key_response.xml");
				 //System.out.println("key ack lllllllLLLLLLLL");
				 ISOMsg isoMassage = new ISOMsg();
				 //System.out.println("key ack mmmmmmmmmmmm");
		    	 isoMassage.setPackager(packe);
		    	 //System.out.println("key ack nnnnnnnnnnnnnnn");
		    	 isoMassage.unpack(ress.getBytes());
		    	 //System.out.println("key ack oooooooooooooo");
		    	 json=  keyExResISOMasssgeACK(isoMassage);
		    	 System.out.println("getTMK response: "+json);
		    	 response.setMessage(EnumStatusConstant.OK.getDescription());
		    	 response.setStatus(EnumStatusConstant.OK.getId());
		    	 response.setResult(json);	
			}catch(Exception e){
				logger.error("error in rvrsl txn dao "+e);
				//System.err.println("res parsing err "+e);
				response.setMessage(EnumStatusConstant.RollBackError.getDescription());
				response.setStatus(EnumStatusConstant.RollBackError.getId());
				response.setResult(json);
			}
		}catch(Exception e){
			logger.error("error in rvrsl txn dao "+e);
			System.err.println("error in tmk ack "+e);
			response.setMessage(EnumStatusConstant.RollBackError.getDescription());
			response.setStatus(EnumStatusConstant.RollBackError.getId());
			response.setResult(json);
		}
		return response;
	}
	
	@Override
	public DbDto<Object> getTMKOgsMain(AcqOGSMainKeyExchangeModel model) {
		logger.info("req in gettmk dao");
		DbDto<Object> response = new DbDto<Object>();
		GenericPackager packager;
		JSONObject jsonn = new JSONObject();
		Session session = null;
		try {
			session = getNewSession();
			AcqDevice acqDevice = (AcqDevice)session.createCriteria(AcqDevice.class).add(Restrictions.eq("deviceSerialNo",model.getDeviceId())).add(Restrictions.eq("userId",model.getSessionId())).uniqueResult();
			if(acqDevice==null){
				response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
				response.setMessage("Mismatch Device Id");
				response.setResult(null);
				return response;
			}
			packager = new GenericPackager("keyExchange.xml");
			dateFormatGmt = new SimpleDateFormat("MMddHHmmss");
			ISOMsg isoMsg = new ISOMsg();
			isoMsg.setPackager(packager);
			isoMsg.setMTI(EnumOgsConstant.KeyExchangeMTI.getId());
			isoMsg.set(7,dateFormatGmt.format(new Date()).toString());
			isoMsg.set(11,model.getStan());
			Calendar cal1 = Calendar.getInstance();
			String dateTime = dateFormatGmt.format(new Date());
			int hod =  Integer.valueOf(dateTime.substring(4,6));
			String hods = "";
			if(hod<10){
				hods = "0"+hod;
			}else{
				hods = ""+hod;
			}
			String year = ""+cal1.get(Calendar.YEAR);
			String charYear = year.substring(3,4);
			int doy = cal1.get(Calendar.DAY_OF_YEAR);
			logger.info("doy:"+doy);
			String doys ="";
			if(doy<10){
				doys = "00"+doy;
			}else if(doy>10&&doy<100){
				doys = "0"+doy;
			}else{
				doys = ""+doy;
			}
			isoMsg.set(37,(charYear+doys+hods+"100001"));
			//isoMsg.set(61,model.getDeviceId()+"|" + model.getKin() );
			isoMsg.set(61,model.getDeviceId());
			isoMsg.set(70,"181");
			logISOMasssge(isoMsg);
			logger.info("iso params completed");
			byte[] data = isoMsg.pack();
			String res = null;
			try {
				res = OGSCardDesc.keyExNetworkTransport(new String(data),model.getUat());
				if(res!=null&&res.equalsIgnoreCase("SocketException")){
					response.setStatus(EnumStatusConstant.TransactionTimeOut.getId());
					response.setMessage(EnumStatusConstant.TransactionTimeOut.getDescription());
					response.setResult(null);
					return response;
				}
			}catch(Exception e){
				logger.error("error to connect ogs "+e);
			}
			//System.out.println("key ggggggggggggg");
			if(res!=null&&res.equals("error to connect ogs")){
				response.setStatus(EnumStatusConstant.IssuerConnectIssue.getId());
				response.setMessage(EnumStatusConstant.IssuerConnectIssue.getDescription());
				response.setResult(null);
				return response;
			}
			String ress = res.substring(2);
			String bitmapStr1 = ress.substring(4,20);
			String hex2Binary1 = getHex2Binary(bitmapStr1);
			String bitmapStr2 ="";
			if(hex2Binary1.startsWith("1")){
				bitmapStr2 = ress.substring(4,36);
				//System.out.println("bitmapStr2:"+bitmapStr2);
				bitmapStr2 = getHex2Binary(bitmapStr2);
			}
			logger.info("BITMAP:"+bitmapStr2);
			try {
				GenericPackager packe = new GenericPackager("key_response.xml");
			 	ISOMsg isoMassage = new ISOMsg();
			  	isoMassage.setPackager(packe);
			   	isoMassage.unpack(ress.getBytes());
			   	// print the DE list
			   	jsonn=  keyExResISOMasssge(isoMassage);
			   	System.out.println("getTMK response: "+json);
			   	response.setMessage(EnumStatusConstant.OK.getDescription());
			   	response.setStatus(EnumStatusConstant.OK.getId());
			   	response.setResult(jsonn);	
			}catch(Exception e){
				logger.error("error in rvrsl txn dao "+e);
				response.setMessage(EnumStatusConstant.RollBackError.getDescription());
				response.setStatus(EnumStatusConstant.RollBackError.getId());
				response.setResult(jsonn);
			}					
		}catch(Exception e){
			logger.error("error in get tmk dao "+e);
			System.err.println("error in gettmk dao "+e);
			response.setMessage(EnumStatusConstant.RollBackError.getDescription());
			response.setStatus(EnumStatusConstant.RollBackError.getId());
			response.setResult(jsonn);
		}finally{
			session.close();
		}
		return response;
	}
	
	private static JSONObject keyExResISOMasssgeACK(ISOMsg msg){
		System.out.println("----ISO MESSAGE-----");
		try {
			json = new JSONObject();
			System.out.println("  MTI : " + msg.getMTI());
			for (int i=1;i<=msg.getMaxField();i++) {
				if (msg.hasField(i)) {
					System.out.println("    Field-"+i+" : "+msg.getString(i));
					if(i==7)
						json.put("DE07", msg.getString(i));
					if(i==11)
						json.put("DE11", msg.getString(i));
					if(i==37)
						json.put("DE37", msg.getString(i));
					if(i==39){
						json.put("DE39", msg.getString(i));
						json.put("responseMessage", OGSCardDesc.getFailReason(msg.getString(i)));
					}
					if(i==44)
						json.put("DE44", msg.getString(i));
					if(i==61)
						json.put("DE61", msg.getString(i));
					if(i==70)
						json.put("DE70", msg.getString(i));
					
				}
			}
		} catch (ISOException e) {
			e.printStackTrace();
		} finally {
			System.out.println("--------------------");
		}
		return json;
	}
	private static JSONObject keyExResISOMasssge(ISOMsg msg){
		System.out.println("----ISO MESSAGE-----");
		try {
			json = new JSONObject();
			System.out.println("  MTI : " + msg.getMTI());
			for (int i=1;i<=msg.getMaxField();i++) {
				if (msg.hasField(i)) {
					System.out.println("    Field-"+i+" : "+msg.getString(i));
					if(i==7)
						json.put("DE07", msg.getString(i));
					if(i==11)
						json.put("DE11", msg.getString(i));
					if(i==37)
						json.put("DE37", msg.getString(i));
					if(i==39){
						json.put("DE39", msg.getString(i));
						json.put("responseMessage", OGSCardDesc.getFailReason(msg.getString(i)));
					}
					if(i==61)
						json.put("DE61", msg.getString(i));
					if(i==70)
						json.put("DE70", msg.getString(i));
					if(i==98)
						json.put("DE98", msg.getString(i));
				}
			}
		} catch (ISOException e) {
			e.printStackTrace();
		} finally {
			System.out.println("--------------------");
		}
		return json;
	}
	
	@Override
	public DbDto<Object> ogsVM20VoidTxnList(
			OGSMainVoidTnxModel model) {
		DbDto<Object> response = new DbDto<Object>();
		System.out.println("111111111:"+model.getTid());
		System.out.println("222222222:"+model.getMaskPan());
		System.out.println("333333333:"+model.getSessionId());
		df = new SimpleDateFormat("yyyy-MM-dd");		
		WeakHashMap<String,WeakHashMap<String,String>> responseMap = new WeakHashMap<String,WeakHashMap<String,String>>();
		Session session = null;
		WeakHashMap<String,String> singleMap = null;
		try{
			session = getNewSession();
			String fromDate = "2017-06-07 00:00:00";
			String toDate =  "2017-06-07 23:59:59";
			String queryStr = "select m.id,m.txntype from ez_mobikwik m,ez_card_details c where m.id=c.transactionid and m.CARDPANNO='"+model.getMaskPan()+"' and m.txntype in('VOID','CARD','CASHATPOS','CVOID','CASHBACK','CBVOID') and m.status=505 and m.payoutstatus=700 and m.otpdatetime between '2017-06-07 00:00:00' and '2017-06-07 23:23:59' and m.amount not in (select amount from ez_mobikwik where amount like '-%') and m.amount not in (select REPLACE(amount,'-','') from ez_mobikwik where amount like '-%') order by m.id desc";
			SQLQuery query = (SQLQuery)session.createSQLQuery(queryStr);
			List txnList = query.list();
			JSONObject json = null;
			for(Object ob:txnList){
				Object obj[] = (Object[]) ob;
				json = new JSONObject();
				json.put("transactionId", obj[0].toString());
				System.out.println(obj[0]+":::::"+obj[1]);
				singleMap = new WeakHashMap<String,String>();
				singleMap.put("transactionId",obj[0].toString());
			}	
			response.setMessage("OK");
			response.setStatus(0);
			response.setResult(json);
			//System.out.println("responseList:"+responseList);
			System.out.println("response from transaction list dao");
			return response;
		}catch(Exception e){
			System.err.println("error to connect ogs "+e);
			response.setMessage(EnumStatusConstant.RollBackError.getDescription());
			response.setStatus(100);
			return response; 
		}finally{
			session.close();
		}
	}
	@Override
	public DbDto<Object> doCashAtPosInitiate(OGSMainTxnInitiateModel model) {
		DbDto<Object> response = new DbDto<Object>();		
		JSONObject jsonn = new JSONObject();
		int txnId=0;
		Timestamp currentTimestamp = null;
		AcqCardBinDesc binDescEnt = null;
		Date date = null;
		try{
			if(model.getIsoData().get("DE02")==null||AcqOGSApiValidator.isDE02(model.getIsoData().get("DE02").toString())==false){
				response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
				response.setMessage("Invalid DE02");
				response.setResult(jsonn);
				return response;
			}if(model.getIsoData().get("ksn")==null||AcqOGSApiValidator.isKsn(model.getIsoData().get("ksn").toString())==false){
				response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
				response.setMessage("Invalid KSN");
				response.setResult(jsonn);
				return response;
			}if(model.getIsoData().get("DE04")==null||AcqOGSApiValidator.isDE04(model.getIsoData().get("DE04").toString())==false){
				response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
				response.setMessage("Invalid DE04");
				response.setResult(jsonn);
				return response;
			}if(model.getIsoData().get("DE11")==null||AcqOGSApiValidator.isDE11(model.getIsoData().get("DE11").toString())==false){
				response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
				response.setMessage("Invalid DE11");
				response.setResult(jsonn);
				return response;
			}if(model.getIsoData().get("DE14")==null||AcqOGSApiValidator.isDE14(model.getIsoData().get("DE14").toString())==false){
				response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
				response.setMessage("Invalid DE14");
				response.setResult(jsonn);
				return response;
			}if(model.getIsoData().get("DE23")==null||AcqOGSApiValidator.isDE23(model.getIsoData().get("DE23").toString())==false){
				response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
				response.setMessage("Invalid DE23");
				response.setResult(jsonn);
				return response;
			}if(model.getIsoData().get("DE35")==null||AcqOGSApiValidator.isDE35(model.getIsoData().get("DE35").toString())==false){
				response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
				response.setMessage("Invalid DE35");
				response.setResult(jsonn);
				return response;
			}if(model.getIsoData().get("DE40")==null||AcqOGSApiValidator.isDE40(model.getIsoData().get("DE40").toString())==false){
				response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
				response.setMessage("Invalid DE40");
				response.setResult(jsonn);
				return response;
			}if(model.getIsoData().get("DE52")==null||AcqOGSApiValidator.isDE52(model.getIsoData().get("DE52").toString())==false){
				response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
				response.setMessage("Invalid DE52");
				response.setResult(jsonn);
				return response;
			}if(model.getIsoData().get("DE55")==null||AcqOGSApiValidator.isDE55(model.getIsoData().get("DE55").toString())==false){
				response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
				response.setMessage("Invalid DE55");
				response.setResult(jsonn);
				return response;
			}if(model.getIsoData().get("DE106")==null||AcqOGSApiValidator.isDE106(model.getIsoData().get("DE106").toString())==false){
				response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
				response.setMessage("Invalid DE106");
				response.setResult(jsonn);
				return response;
			}
			System.out.println("data persistance started");
			Session session = null;
			AcqSettingEntity settingEnt = null;
			Transaction tx = null;
			String de61 = null;
			try {				
				SimpleDateFormat dateFormatGmt = new SimpleDateFormat("MMddHHmmss");
				String dateTime = dateFormatGmt.format(new Date());
				int hod =  Integer.valueOf(dateTime.substring(4,6));
				String hods = "";
				if(hod<10){
					hods = "0"+hod;
				}else{
					hods = ""+hod;
				}
				Calendar cal = Calendar.getInstance();
				String year = ""+cal.get(Calendar.YEAR);
				String charYear = year.substring(3,4);
				int doy = cal.get(Calendar.DAY_OF_YEAR);
				String doys ="";
				if(doy<10){
					doys = "00"+doy;
				}else if(doy>10&&doy<100){
					doys = "0"+doy;
			    }else{
			    	doys = ""+doy;
			    }
				dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
				String gmtDateTime = dateFormatGmt.format(new Date());
				model.setGmtDateTime(gmtDateTime);
				String rrNo = charYear+doys+hods;//+model.getIsoData().get("DE11");				
				System.out.println("sessionId : "+model.getSessionId());
				session = getNewSession();
				tx=session.beginTransaction();
				AcqKeyExchangeEntity keyEntity = (AcqKeyExchangeEntity) session.createCriteria(AcqKeyExchangeEntity.class).add(Restrictions.eq("tid",model.getDeviceId())).add(Restrictions.eq("userId",Long.valueOf(model.getSessionId()))).uniqueResult();
			    if(keyEntity==null){
			       	response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
			       	response.setMessage("Transaction can't place,Key Exchange not verified");
			       	response.setResult(jsonn);
			       	return response;
			    }
			    model.setMcc(keyEntity.getMcc());
			    model.setAcquirerId(keyEntity.getAcquirerId());
				AcqUserEntity userEnt = (AcqUserEntity) session.get(
						AcqUserEntity.class, Long.valueOf(model.getSessionId()));
				if(userEnt==null){
					response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
					response.setMessage("User not found for card transaction");
					response.setResult(jsonn);
					return response;
				}
				AcqSettingEntity settingEnt1 = (AcqSettingEntity) session
						.createCriteria(AcqSettingEntity.class)
						.add(Restrictions.eq("id", 1l)).uniqueResult();
				if(settingEnt1.getMaintenanceMode().equals("1")&&!settingEnt1.getMaintenanceReason().equalsIgnoreCase("NA")){
					logger.info(",Maintenance mode is on, transaction can't be place");
					response.setStatus(EnumStatusConstant.MaintenanceMode.getId());
					response.setMessage(EnumStatusConstant.MaintenanceMode.getDescription());
					//response.setResult(responseMap);
					return response;
				}
				AcqOrganization org = (AcqOrganization)session.get(AcqOrganization.class, userEnt.getOrgId());					
				if(org==null){
					logger.info("Merchant Id not found frm db");
					response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
					response.setMessage("Merchant not found for card transaction");
					response.setResult(jsonn);
					return response;
				}
				AcqMerchantEntity merchantEnt = (AcqMerchantEntity)session.createCriteria(AcqMerchantEntity.class).add(Restrictions.eq("merchantId",""+org.getMerchantId())).uniqueResult();
				if(merchantEnt==null){
					logger.info("Merchant Id not found frm db");
					response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
					response.setMessage("Merchant not found for card transaction");
					response.setResult(jsonn);
					return response;
				}
				model.setMid(merchantEnt.getMerchantTID());
				model.setMerchantAddress(merchantEnt.getAddress1());
				model.setMerchantPinCode(merchantEnt.getMerchantPinCode());
				AcqMapDeviceUserEntity deviceEnt = (AcqMapDeviceUserEntity) session.createCriteria(AcqMapDeviceUserEntity.class).add(Restrictions.eq("userId",Long.valueOf(model.getSessionId()))).add(Restrictions.or(Restrictions.or(Property.forName("deviceType").eq("credit"),Property.forName("deviceType").eq("wallet/credit")))).uniqueResult();
				if (deviceEnt == null || deviceEnt + "" == "") {
					response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
					response.setMessage("User details not found for card transction");
					response.setResult(jsonn);
					return response;
				} else {
					logger.info("user id is "+model.getSessionId());
					try {
						binDescEnt = new AcqCardBinDesc();
		                binDescEnt.setCardType(getCardFromPan(model.getMaskPan()));
		                model.setCardType(binDescEnt.getCardType());						
						de61 = OGSCardDesc.generateDe61New(Boolean.valueOf(model.getPinEntered()),Boolean.valueOf(model.getEmv()),model.getIsoData().get("DE52").toString(),model.getMerchantAddress(),model.getMerchantPinCode());
				        model.setDe61(de61);
						model.setTid(deviceEnt.getBankTid());
						AcqTxnEntity txnEnt = new AcqTxnEntity();
						txnEnt.setUserid(Integer.valueOf(""+userEnt.getUserId()));						
						txnEnt.setOrgId(Integer.valueOf(""+userEnt.getOrgId()));
						txnEnt.setMerchantId(Integer.valueOf(""+org.getMerchantId()));
						txnEnt.setMobile("0000");
						Double amt = Double.valueOf(model.getIsoData().get("DE04").toString())/100;
						txnEnt.setAmount(amt.toString());
						if(!userEnt.getAcquirerCode().equalsIgnoreCase("Acquiro")){
							settingEnt = (AcqSettingEntity) session
									.createCriteria(AcqSettingEntity.class)
									.add(Restrictions.eq("acquirerCode", userEnt.getAcquirerCode())).uniqueResult();
							if(settingEnt.getMaintenanceMode().equalsIgnoreCase("1")&&!settingEnt.getMaintenanceReason().equalsIgnoreCase("NA")){
								logger.info(",Acquirer server maintenance mode is on, transaction can't be place");
								response.setStatus(EnumStatusConstant.MaintenanceMode.getId());
								response.setMessage(EnumStatusConstant.MaintenanceMode.getDescription());
								//response.setResult(responseMap);
								return response;
							}
								txnEnt.setEzMdr("0.0");
								txnEnt.setBankMdr("0.0");
								txnEnt.setAcquirerMdr("0.0");
								if(Double.valueOf(model.getIsoData().get("DE04").toString())<=Double.valueOf(settingEnt.getTxnTaxAmt())){
									txnEnt.setServiceTax("0.0");
								}else{
									txnEnt.setServiceTax(settingEnt.getServiceTax());
								}								
								if(Double.valueOf(model.getIsoData().get("DE04").toString())<Double.valueOf(settingEnt.getSystmUtltyAmt())){
									txnEnt.setSystemUtilityFee(settingEnt.getSystmUtltyFee());
								}else{
									txnEnt.setSystemUtilityFee("0.0");
								}
						}else{
							settingEnt = (AcqSettingEntity) session
									.createCriteria(AcqSettingEntity.class)
									.add(Restrictions.eq("id", 1l)).uniqueResult();
								txnEnt.setEzMdr("0.0");
								txnEnt.setBankMdr("0.0");
								txnEnt.setAcquirerMdr("0.0");
								if(Double.valueOf(model.getIsoData().get("DE04").toString())<=Double.valueOf(settingEnt1.getTxnTaxAmt())){
									txnEnt.setServiceTax("0.0");
								}else{
									txnEnt.setServiceTax(settingEnt1.getServiceTax());
								}
								if(Double.valueOf(model.getIsoData().get("DE04").toString())<Double.valueOf(settingEnt.getSystmUtltyAmt())){
									txnEnt.setSystemUtilityFee(settingEnt.getSystmUtltyFee());
								}else{
									txnEnt.setSystemUtilityFee("0.0");
								}
							//}
						}
						date= new Date();
						currentTimestamp= new Timestamp(date.getTime());						
						txnEnt.setDateTime(currentTimestamp);
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						txnEnt.setOtpDateTime(sdf.format(date));
						//txnEnt.setOtpDateTime(currentTimestamp);
						txnEnt.setStatus(503);
						txnEnt.setMobile("NA");
						txnEnt.setEmail("NA");
						txnEnt.setTxnType("CASHATPOS");
						txnEnt.setDescription("Transaction Initiated");
						txnEnt.setCarPanNo(model.getMaskPan());
						txnEnt.setCustomerId("0");
						txnEnt.setPayoutStatus(700);
						txnEnt.setPayoutDateTime(currentTimestamp);
						txnEnt.setSwitchLab("OGS");
						txnEnt.setAcquirerCode(userEnt.getAcquirerCode());
						txnEnt.setCashBackAmt("0.0");
						txnEnt.setAcqPayoutStatus(800);
						txnEnt.setAcqPayoutDateTime(currentTimestamp);
						if(model.getApplicationCertificate()==null||model.getApplicationCertificate()==""){
							txnEnt.setAppCertificate("NA");
						}else{
							txnEnt.setAppCertificate(model.getApplicationCertificate());
						}
						if(model.getAid()==null||model.getAid()==""){
							txnEnt.setAid("NA");
						}else
							txnEnt.setAid(model.getAid());
						
						txnEnt.setScriptResult("NA");						
						session.save(txnEnt);
						txnId = txnEnt.getId();
						//model.setDe12((""+txnEnt.getOtpDateTime()).substring(11,19).replace(":",""));
						//System.out.println("localtime::::::"+model.getDe12());							
						AcqCardDetails cardEntity = new AcqCardDetails();
						//System.out.println("txnEnt.getId(): "+txnEnt.getId());
						cardEntity.setTransactionId(txnEnt.getId());
						//cardEntity.setTerminalId("123123");
						cardEntity.setTerminalId(deviceEnt.getBankTid());
						/*cardEntity.setCardHolderName(model.getCardHolderName());
						cardEntity.setCardType(model.getCardType());
						cardEntity.setCardExpDate(model.getCardExpDate());*/
						String stan = String.format("%06d", txnEnt.getId());
						if(stan.length()<=6){
							model.setStan(stan);
						}else{
							model.setStan(stan.substring(stan.length()-6));
						}
						cardEntity.setStan(model.getStan());
						stan=null;
						cardEntity.setRrNo(rrNo+model.getStan());
						cardEntity.setAuthCode("NA");
						cardEntity.setBatchNo("0");
						if(model.getCardHolderName()==null||model.getCardHolderName()==""){
							cardEntity.setCardHolderName("NA");
						}else{
							cardEntity.setCardHolderName(model.getCardHolderName());
						}
						cardEntity.setCardType("NA");
						cardEntity.setIpAddress(model.getIpAddress());
						cardEntity.setImeiNo("123123");
						cardEntity.setLatitude(model.getLatitude());
						cardEntity.setLongitude(model.getLongitude());
						//cardEntity.setStan("NA");//CustomerMobile(model.get)
						cardEntity.setDe61(model.getDe61());
						cardEntity.setGmtDateTime(model.getGmtDateTime());	
						if(model.getEmv().equalsIgnoreCase("true")){
							cardEntity.setCardType(model.getCardType()+"|Chip");
						}else{
							cardEntity.setCardType(model.getCardType()+"|Swipe");
						}cardEntity.setPinEntered(model.getPinEntered());
						if(model.getApplicationName()==null||model.getApplicationName()==""){
							cardEntity.setApplicationName("NA");
						}else{
							cardEntity.setApplicationName(model.getApplicationName());
						}
						session.save(cardEntity);
						tx.commit();
						model.setDe12((""+txnEnt.getOtpDateTime()).substring(11,19).replace(":",""));
						model.setRrNo(cardEntity.getRrNo());
						System.out.println("localtime::::::"+model.getDe12());
					} catch (Exception e) {
						response.setStatus(EnumStatusConstant.RollBackError.getId());
						response.setMessage(EnumStatusConstant.RollBackError.getDescription());
						logger.error("error to save txn details" +e);
						System.err.println("error to save txn details" +e);
						return response;
					}
				}
			} catch (Exception e) {
				response.setStatus(EnumStatusConstant.RollBackError.getId());
				response.setMessage(EnumStatusConstant.RollBackError
						.getDescription());
				logger.error("error to persist data "+e);
				System.out.println("error to save entity "+e);
				return response;
			} finally {
				session.close();
			}
			System.out.println("data persistance done");
			jsonn.put("transactionId", String.valueOf(txnId));
			if(txnId==0||txnId<=1){
				response.setStatus(EnumStatusConstant.RollBackError.getId());
				response.setMessage("Technical Error,Trasaction id not generated");
				response.setResult(jsonn);
				logger.warn("error to generate txnid");
				return response;
			}
			System.out.println("txnIdtxnId:"+txnId);			
			logger.info("req in cashatpos init dao");
			String cashAtPosRequestIso = cashAtPosprchsTxn(model);
			model.setRequestIso(model.getIsoData().get("ksn")+cashAtPosRequestIso);
			String ogsResponse = OGSCardDesc.newNetworkTransport(model.getIsoData().get("ksn")+cashAtPosRequestIso,model.getUat());
			model.setRespnseIso(ogsResponse);
			System.out.println("cast at pos ogsResponse:"+ogsResponse);	
			if(ogsResponse!=null&&ogsResponse.equalsIgnoreCase("SocketException")){
				response.setStatus(EnumStatusConstant.TransactionTimeOut.getId());
				response.setMessage(EnumStatusConstant.TransactionTimeOut.getDescription());
				jsonn.put("gmtDateTime",model.getGmtDateTime());
				response.setResult(jsonn);
				return response;
			}
			String subStr = ogsResponse.substring(22);
			jsonn = getResponseCode(subStr,"doCashAtPos");
			jsonn.put("transactionId", String.valueOf(txnId));
			System.out.println("jsonnjsonn::"+jsonn);
			response.setStatus(EnumStatusConstant.OK.getId());
			response.setMessage(EnumStatusConstant.OK.getDescription());
			response.setResult(jsonn);
			Transaction txx = null;
			Session sessionn = null;
			try{
				sessionn = getNewSession();
				AcqCardDetails ent = (AcqCardDetails)sessionn.createCriteria(AcqCardDetails.class).add(Restrictions.eq("transactionId",Integer.valueOf(txnId))).uniqueResult();
				if(ent==null){
					logger.error("ent is null");
				}else{
					AcqPlaceTransactionEntity mainEnt = (AcqPlaceTransactionEntity)sessionn.createCriteria(AcqPlaceTransactionEntity.class).add(Restrictions.eq("id",Integer.valueOf(txnId))).uniqueResult();
					if(mainEnt!=null&&jsonn.containsKey("responseMessage")){
						mainEnt.setDescription(jsonn.get("responseMessage").toString());
						sessionn.update(mainEnt);
					}
					if(jsonn.containsKey("DE38"))
						ent.setAuthCode(jsonn.get("DE38").toString());
					if(jsonn.containsKey("DE11"))
						ent.setStan(jsonn.get("DE11").toString());
					if(jsonn.containsKey("DE12"))
						ent.setBatchNo(jsonn.get("DE12").toString());
					if(model.getGmtDateTime()!=null)
						ent.setGmtDateTime(model.getGmtDateTime());		
					
					txx = sessionn.beginTransaction();
					sessionn.update(ent);
					try{
						date = new Date();
						currentTimestamp = new Timestamp(date.getTime());
						AcqOgsISOEntity isoEnt = new AcqOgsISOEntity();
						isoEnt.setTransactionId(txnId+"");//""+txnEnt.getId());
						isoEnt.setIsoRequest(model.getRequestIso());
						isoEnt.setIsoResponse(model.getRespnseIso());
						isoEnt.setSwitchLab("OGS");
						isoEnt.setRequestTime(model.getRequestTime());
						isoEnt.setResponseTime(currentTimestamp.toString());
						sessionn.save(isoEnt);
					}catch(Exception e){
						logger.error("error to save iso message" +e);
						System.err.println("error to save iso message" +e);
					}	
					txx.commit();
				}
			}catch(Exception e){
				logger.error("error to update entity "+e);
				System.out.println("error to save auth code "+e);
			}finally{
				if(sessionn.isOpen()==true||sessionn.isConnected()==true){
					sessionn.close();
				}
			}			
			return response;
		}catch(Exception e){
			response.setStatus(EnumStatusConstant.RollBackError.getId());
			response.setMessage(EnumStatusConstant.RollBackError.getDescription());
			response.setResult(jsonn);
			return response;
		}
	}
	


	public static String cashAtPosprchsTxn(OGSMainTxnInitiateModel model)
            throws ISOException, IOException {

        try {
        	System.out.println("aaaaaaaaaaaaaaaaaa");
        	/*AcqCardBinDesc binDescEnt = new AcqCardBinDesc();
        	binDescEnt.setCardType(getCardFromPan(model.getMaskPan()));
        	model.setCardType(binDescEnt.getCardType());*/
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat dateFormatGmt = new SimpleDateFormat("MMddHHmmss");
            String dateTime = dateFormatGmt.format(new Date());
            int hod = Integer.valueOf(dateTime.substring(4, 6));
            System.out.println("hod:" + hod);
            String hods = "";
            if (hod < 10) {
                hods = "0" + hod;
            } else {
                hods = "" + hod;
            }
            System.out.println("bbbbbbbbbbbbbbb");
            String MMdd = dateTime.substring(0, 4);
            String hhmmss = dateTime.substring(4, 10);
            System.out.println(MMdd + ":hhmmss:" + hhmmss);
            Calendar cal = Calendar.getInstance();
            String year = "" + cal.get(Calendar.YEAR);
            String charYear = year.substring(3, 4);
            int doy = cal.get(Calendar.DAY_OF_YEAR);
            System.out.println("doy:" + doy);
            String doys = "";
            if (doy < 10) {
                doys = "00" + doy;
            } else if (doy > 10 && doy < 100) {
                doys = "0" + doy;
            } else {
                doys = "" + doy;
            }
            System.out.println("doys:::::" + doys);
            //String resId = String.format("%06d", Integer.valueOf(model.getIsoData().get("DE11").toString()));
            //System.out.println("txnId:" + resId);
            System.out.println("charYear+doys+hods+resId:" + charYear + doys + hods + model.getIsoData().get("DE11").toString());
            //String rrNo = (charYear + doys + hods + model.getIsoData().get("DE11").toString());
            String mcc = model.getMcc();
            //String terminalState = "";
            System.out.println("cccccccccccccccccccccc");
            GenericPackager packager = new GenericPackager("purchaseTxn.xml");
            // Create ISO Message
            ISOMsg isoMsg = new ISOMsg();
            isoMsg.setPackager(packager);
            isoMsg.setMTI("0200");
            System.out.println("eeeeeeeeeeeeeeeeeeeeeee");
            dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
            isoMsg.set(2, model.getIsoData().get("DE02").toString());
            String gmtDateTime = dateFormatGmt.format(new Date());            
            isoMsg.set(3, "010000");
            System.out.println("ffffffffffffffffffff");
            /*if(binDescEnt.getCardType().equals(OGSCardDesc.RUPAY)){
            	isoMsg.set(4, model.getIsoData().get("DE04").toString());  // txn amount
            }else{
            	isoMsg.set(4, "000000000000");
            }*/
            isoMsg.set(4, model.getIsoData().get("DE04").toString());
            System.out.println("gggggggggggggggggggg");
            isoMsg.set(7, gmtDateTime); //txn date time
            model.setGmtDateTime(gmtDateTime);
            //isoMsg.set(11, "000016"); //Systemtrace audit number //txn id
            //String id = ""+entity.getId();
            System.out.println("hhhhhhhhhhhhhhhhhhhhhhhhh");
            isoMsg.set(11, model.getStan());
            SimpleDateFormat s = new SimpleDateFormat("HHmmss");
            String localFormat = s.format(new Date());
            //isoMsg.set(12, localFormat); //Time, local transaction hhmmss
            System.out.println("de12 in cashatpos:"+model.getDe12());
            isoMsg.set(12,model.getDe12());
            isoMsg.set(13, MMdd); //Date,local transaction // MMdd
            //isoMsg.set(14, "2105");  //expiry date
            System.out.println("iiiiiiiiiiiiiiiiiiiiiiiii");
            isoMsg.set(14, model.getIsoData().get("DE14").toString());  //expiry date
            int i = 1;
            //StringTokenizer st = new StringTokenizer("Acquiro01|00000002|5999|Haryana|Acquiro|Second Floor, Shri JP Tower, 1164/1, New Railway Rd,Gurugram|20170325162753", "|");

            System.out.println("jjjjjjjjjjjjjjjj");
            System.out.println("mcc:" + mcc);//merchant id
            //isoMsg.set(18, "5999");
            //isoMsg.set(18, mcc);//Merchantcategory code // from ogs
           // isoMsg.set(19, "356"); //Acquiringinstitutioncountrycode // it will be static           
            System.out.println("kkkkkkkkkkkkkkkkkkkkkkkkkk");
            
            if(model.getEmv().equalsIgnoreCase("true")){        	
           	if(model.getCardType().equals(OGSCardDesc.VISA)||model.getCardType().equals(OGSCardDesc.MASTER)||model.getCardType().equals(OGSCardDesc.MAESTRO)){
                    isoMsg.set(22, "051"); //VISA
                }else{
                    isoMsg.set(22, "951"); //Rupay
                }
           }else{
           	if(model.getFallBack().equalsIgnoreCase("true")){ 
                   if(model.getCardType().equals(OGSCardDesc.RUPAY) ||
                		   model.getCardType().equals(OGSCardDesc.DISCOVER) ||
                		   model.getCardType().equals(OGSCardDesc.DINERS) ||
                		   model.getCardType().equals(OGSCardDesc.MASTER)||
                		   model.getCardType().equals(OGSCardDesc.MAESTRO)){
                       isoMsg.set(22, "801"); //for rupay
                   }else{
                       isoMsg.set(22, "901"); //for visa
                   }
               }else{
                   isoMsg.set(22, "901"); //POSentrymode
               }
           }           
            System.out.println("LLLLLLLLLLLLLLLLLL lllll");
            if(model.getIsoData().get("DE23")!=null&&model.getIsoData().get("DE23").toString().length()>0){
                isoMsg.set(23, "0"+model.getIsoData().get("DE23").toString());
            }
            System.out.println("mmmmmmmmmmmmmmmmmmmm");
            isoMsg.set(25, "00"); //POSconditioncode // space is coming for de25, have you any idea? are you there?wait
            //isoMsg.set(32, "720399"); //Acquiringinstitutioncode //apending 25
            isoMsg.set(32, model.getAcquirerId());
            //isoMsg.set(32, "00201000003"); 
            //isoMsg.set(35, "6085008000004927");  //in between data element 32 and 37 data element values. //pls check chat screen
            isoMsg.set(35, model.getIsoData().get("DE35").toString());
            System.out.println("nnnnnnnnnnnnnnnnnnnn");
            //isoMsg.set(35,"6085008000004927=21055219990000000000");
            System.out.println("model.getRrNo():"+model.getRrNo());
            isoMsg.set(37, model.getRrNo());
            //isoMsg.set(37, "707516000013"); //Retrieval reference number
            isoMsg.set(40, model.getIsoData().get("DE40").toString());
            System.out.println("oooooooooooooooooooooooooo");
            isoMsg.set(41, model.getTid());
            isoMsg.set(42,model.getMid());
            isoMsg.set(49, "356");        //Currencycode, transaction //it will be static till new fields add
            System.out.println("rrrrrrrrrrrrrrrrrrrrrrrrrr");            
            if(model.getPinEntered().equalsIgnoreCase("true")){
                String pinblock =model.getIsoData().get("DE52").toString();
                if(!pinblock.equals("0000000000000000")){
                    isoMsg.set(52, pinblock); // Pin block
                    if(model.getCardType().equals(OGSCardDesc.VISA)){
                        isoMsg.set(53, "2001010100000000"); // for visa only
                    }
                }
            }
            System.out.println("sssssssssssssssssss");
            if(model.getCardType().equals(OGSCardDesc.VISA) ||model.getCardType().equals(OGSCardDesc.MASTER)){
            	if(!mcc.equals("6010") && !mcc.equals("6011")){
                    isoMsg.set(54, "9090356D"+model.getIsoData().get("DE04")); // cash back amount
                }else{
                	//isoMsg.set(54, "9090356D"+model.getIsoData().get("DE04")); // txn amount
                }
            }// txn amount
            System.out.println("ttttttttttttttttttttttt");
            if(model.getIsoData().get("DE55")==null||model.getIsoData().get("DE55").toString()==""){
            	logger.info("de50 is not part of iso ");
            }else{
            	isoMsg.set(55, model.getIsoData().get("DE55").toString());     
            }                   
           /* if(binDescEnt.getCardType().equals(OGSCardDesc.VISA)){
                if(model.getEmv().equalsIgnoreCase("true")){
                    isoMsg.set(60, "950000100000"); // for EMV
                }else{
                    if(model.getFallBack().equalsIgnoreCase("true")){
                        isoMsg.set(60, "951000000000"); // for EMV fallback txn
                    }else{
                        isoMsg.set(60, "920000000000"); // for MAG Strip
                    }
                }
            }*/
            System.out.println("uuuuuuuuuuuuuuuuuuu");
            System.out.println("model.getAddress:"+model.getMerchantAddress());
            System.out.println("model.getPincode:"+model.getMerchantPinCode());
            // isoMsg.set(61, de61);//POSdata code
            isoMsg.set(61,model.getDe61());
            //model.setDe61(de61);
            System.out.println("vvvvvvvvvvvvvvvvvvvvvvvv");          
            //isoMsg.set(106,model.getIsoData().get("DE106").toString());
            System.out.println("xxxxxxxxxxxxxx");
            reqLogISOMsg(isoMsg);
            System.out.println("yyyyyyyyyyyyyyyyy");
            byte[] data = isoMsg.pack();
            System.out.println("zzzzzzzzzzzzzzzzzzz");
            String plainIso = new String(data);
            System.out.println("purchase request iso: "+plainIso);
            return plainIso;//+"||"+de61+"||"+gmtDateTime;
        }catch (Exception e) {
            System.err.println("error to parse castatpos iso "+e);
            logger.error("error to parse castatpos iso "+e);
        }
        return null;
	}
	
	
	 public static void reqLogISOMsg(ISOMsg msg) {
	       System.out.println("----ISO MESSAGE-----");
	       try {
	           System.out.println("  MTI : " + msg.getMTI());
	           for (int i = 1; i <= msg.getMaxField(); i++) {
	               if (msg.hasField(i)) {
	                   System.out.println("    Field-" + i + " : " + msg.getString(i));
	               }
	           }
	       } catch (ISOException e) {
	           e.printStackTrace();
	       } finally {
	           System.out.println("--------------------");
	       }
	   }
	 
	 @Override
		public DbDto<Object> mv20OGSTxnComplete(OGSMainTxnCompleteModel model) {
			DbDto<Object> response = new DbDto<Object>();
			JSONObject obj = new JSONObject();
			List<String> list = new ArrayList<String>();
			logger.info("req in purchase dao");
			try{
				Session session = null;
				Transaction tx = null;
				
				try {
					session = getNewSession();
					AcqPlaceTransactionEntity entity = (AcqPlaceTransactionEntity)session.createCriteria(AcqPlaceTransactionEntity.class).add(Restrictions.eq("id",Integer.valueOf(model.getTransactionId()))).uniqueResult();
					tx = session.beginTransaction();
					if(model.getStatusCode().equalsIgnoreCase("0")){
						entity.setStatus(505);
						entity.setDescription("Transaction Successful");
					}else
						entity.setStatus(504);
					
					if(model.getScriptResponse()!=null)
						entity.setScriptResult(model.getScriptResponse());
					
					session.update(entity);
					tx.commit();
						list.add(0,""+entity.getAmount());
						list.add(1,""+entity.getStatus());
						String dat = ""+entity.getOtpDateTime();
						list.add(2,""+dat.substring(0, 19));
						list.add(3,""+entity.getOrgId());
						list.add(4,""+model.getTransactionId());
						String txnType = ""+entity.getTxnType();
					if(!list.isEmpty()){
						String marketingName = (String)session.createCriteria(AcqOrganization.class).setProjection(Projections.property("orgName")).add(Restrictions.eq("id",Long.valueOf(""+list.get(3)))).uniqueResult();
						list.add(5,marketingName);
					}
					String userQuery = "select rmn,smsStatus,acquirerCode from AcqUserDetailsEntity where id =:userId";
					Query query=session.createQuery(userQuery);
					query.setParameter("userId", Long.valueOf(model.getSessionId()));
					String rmn = "";String smsStatus="";String acquirerCode="";						
					for(Object obj1 : query.list()){
						Object[] ob = (Object[])obj1;	
						rmn=""+ob[0];
						smsStatus=""+ob[1];
						acquirerCode=""+ob[2];
						list.add(6,rmn);
						list.add(7,smsStatus);
						list.add(8,acquirerCode);
					}						
					//String rmn= (String) session.createCriteria(AcqUserDetailsEntity.class).setProjection(Projections.property("rmn")).add(Restrictions.eq("id",Long.valueOf(model.getSessionId()))).uniqueResult();
					//list.add(6,rmn);
					list.add(9,txnType);
					list.add(10,""+entity.getCarPanNo());
					response.setStatus(EnumStatusConstant.OK.getId());
					response.setMessage(EnumStatusConstant.OK.getDescription());
					response.setResult(list);
				}catch(Exception e){
					logger.error("error to complete ogs purchase "+e);
				}
				finally {				
					if(session.isOpen()==true||session.isConnected()==true){
						session.close();
					}
				}
				return response;
			}catch(Exception e){
				response.setStatus(EnumStatusConstant.RollBackError.getId());
				response.setMessage(EnumStatusConstant.RollBackError.getDescription());
				response.setResult(obj);
				return response;
			}
		}
	
	@Override
	public DbDto<Object> mv20OGSTxnInitiate(OGSMainTxnInitiateModel model) {
		DbDto<Object> response = new DbDto<Object>();
		logger.info("req in purchase dao");
		JSONObject jsonObj = new JSONObject();
		int txnId=0;
		Session session = null;
		Transaction tx = null;	
		Timestamp currentTimestamp = null;
		Date date = null;
		try{
			if(model.getIsoData().get("DE02")==null||AcqOGSApiValidator.isDE02(model.getIsoData().get("DE02").toString())==false){
				response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
				response.setMessage("Invalid DE02");
				response.setResult(jsonObj);
				return response;
			}if(model.getIsoData().get("ksn")==null||AcqOGSApiValidator.isKsn(model.getIsoData().get("ksn").toString())==false){
				response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
				response.setMessage("Invalid KSN");
				response.setResult(jsonObj);
				return response;
			}if(model.getIsoData().get("DE04")==null||AcqOGSApiValidator.isDE04(model.getIsoData().get("DE04").toString())==false){
				response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
				response.setMessage("Invalid DE04");
				response.setResult(jsonObj);
				return response;
			}if(model.getIsoData().get("DE11")==null||AcqOGSApiValidator.isDE11(model.getIsoData().get("DE11").toString())==false){
				response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
				response.setMessage("Invalid DE11");
				response.setResult(jsonObj);
				return response;
			}if(model.getIsoData().get("DE14")==null||AcqOGSApiValidator.isDE14(model.getIsoData().get("DE14").toString())==false){
				response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
				response.setMessage("Invalid DE14");
				response.setResult(jsonObj);
				return response;
			}if(model.getIsoData().get("DE23")==null||AcqOGSApiValidator.isDE23(model.getIsoData().get("DE23").toString())==false){
				response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
				response.setMessage("Invalid DE23");
				response.setResult(jsonObj);
				return response;
			}if(model.getIsoData().get("DE35")==null||AcqOGSApiValidator.isDE35(model.getIsoData().get("DE35").toString())==false){
				response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
				response.setMessage("Invalid DE35");
				response.setResult(jsonObj);
				return response;
			}if(model.getIsoData().get("DE40")==null||AcqOGSApiValidator.isDE40(model.getIsoData().get("DE40").toString())==false){
				response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
				response.setMessage("Invalid DE40");
				response.setResult(jsonObj);
				return response;
			}if(model.getIsoData().get("DE52")==null||AcqOGSApiValidator.isDE52(model.getIsoData().get("DE52").toString())==false){
				response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
				response.setMessage("Invalid DE52");
				response.setResult(jsonObj);
				return response;
			}if(model.getIsoData().get("DE55")==null||AcqOGSApiValidator.isDE55(model.getIsoData().get("DE55").toString())==false){
				response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
				response.setMessage("Invalid DE55");
				response.setResult(jsonObj);
				return response;
			}if(model.getIsoData().get("DE106")==null||AcqOGSApiValidator.isDE106(model.getIsoData().get("DE106").toString())==false){
				response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
				response.setMessage("Invalid DE106");
				response.setResult(jsonObj);
				return response;
			}
			System.out.println("data persistance started");
			AcqSettingEntity settingEnt = null;
			AcqCardBinDesc binDescEnt = null;
			String de61 = null;
			try{				
				System.out.println("de61:"+model.getDe61());
				 SimpleDateFormat dateFormatGmt = new SimpleDateFormat("MMddHHmmss");
			     String dateTime = dateFormatGmt.format(new Date());
				 int hod =  Integer.valueOf(dateTime.substring(4,6));
			        String hods = "";
			        if(hod<10){
			            hods = "0"+hod;
			        }else{
			            hods = ""+hod;
			        }
			        Calendar cal = Calendar.getInstance();
			        String year = ""+cal.get(Calendar.YEAR);
			        String charYear = year.substring(3,4);
			        int doy = cal.get(Calendar.DAY_OF_YEAR);
			        String doys ="";
			        if(doy<10){
			            doys = "00"+doy;
			        }else if(doy>10&&doy<100){
			            doys = "0"+doy;
			        }else{
			            doys = ""+doy;
			        }
			        dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
			        String gmtDateTime = dateFormatGmt.format(new Date());
			        String rrNo = charYear+doys+hods;//+model.getIsoData().get("DE11");
			        //System.out.println("sessionId : "+model.getSessionId());
			        session = getNewSession();
			        tx=session.beginTransaction();
			        logger.info("sesion created");
			        //System.out.println("tid:"+model.getDeviceId());
			        AcqKeyExchangeEntity keyEntity = (AcqKeyExchangeEntity) session.createCriteria(AcqKeyExchangeEntity.class).add(Restrictions.eq("tid",model.getDeviceId())).add(Restrictions.eq("userId",Long.valueOf(model.getSessionId()))).uniqueResult();
			        if(keyEntity==null){
			        	response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
			        	response.setMessage("Transaction can't place,Key Exchange not verified");
			        	response.setResult(jsonObj);
			        	return response;
			        }
			        model.setMcc(keyEntity.getMcc());
			        model.setAcquirerId(keyEntity.getAcquirerId());
			        AcqUserEntity userEnt = (AcqUserEntity) session.get(
						AcqUserEntity.class, Long.valueOf(model.getSessionId()));
			        if(userEnt==null){
			        	response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
			        	response.setMessage("User not found for card transaction");
			        	response.setResult(jsonObj);
			        	return response;
			        }
			        AcqSettingEntity settingEnt1 = (AcqSettingEntity) session
						.createCriteria(AcqSettingEntity.class)
						.add(Restrictions.eq("id", 1l)).uniqueResult();
			        if(settingEnt1.getMaintenanceMode().equals("1")&&!settingEnt1.getMaintenanceReason().equalsIgnoreCase("NA")){
			        	logger.info(",Maintenance mode is on, transaction can't be place");
			        	response.setStatus(EnumStatusConstant.MaintenanceMode.getId());
			        	response.setMessage(EnumStatusConstant.MaintenanceMode.getDescription());
			        	//response.setResult(responseMap);
			        	return response;
			        }
			        //System.out.println("ccccccccccccccc sssssssssssssssss");
			        AcqOrganization org = (AcqOrganization)session.get(AcqOrganization.class, userEnt.getOrgId());					
			        //System.out.println("ddddddddddddddd sssssssssssssssss");
			        AcqMerchantEntity merchantEnt = (AcqMerchantEntity)session.createCriteria(AcqMerchantEntity.class).add(Restrictions.eq("merchantId",""+org.getMerchantId())).uniqueResult();
			        if(merchantEnt==null){
			        	logger.info("Merchant Id not found frm db");
			        	response.setStatus(EnumStatusConstant.MaintenanceMode.getId());
			        	response.setMessage("Merchant not found for card transaction");
			        	//response.setResult(responseMap);
			        	return response;
			        }
			        model.setMid(merchantEnt.getMerchantTID());
			        System.out.println(merchantEnt.getAddress1()+":merchantnamepincode:"+merchantEnt.getMerchantPinCode());
			        model.setMerchantAddress(merchantEnt.getAddress1());
			        model.setMerchantPinCode(merchantEnt.getMerchantPinCode());
			        AcqMapDeviceUserEntity deviceEnt = (AcqMapDeviceUserEntity) session.createCriteria(AcqMapDeviceUserEntity.class).add(Restrictions.eq("userId",Long.valueOf(model.getSessionId()))).add(Restrictions.or(Restrictions.or(Property.forName("deviceType").eq("credit"),Property.forName("deviceType").eq("wallet/credit")))).uniqueResult();
			        if (deviceEnt == null || deviceEnt + "" == "") {
			        	response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
			        	response.setMessage("User details not found for card transction");
			        	response.setResult(jsonObj);
			        	return response;
			        }else{
			        	//System.out.println("eeeeeeeeeeeeeeeeee sssssssssssssssss");
			        	logger.info("user id is "+model.getSessionId());
			        	try {
			        		binDescEnt = new AcqCardBinDesc();
			                binDescEnt.setCardType(getCardFromPan(model.getMaskPan()));
			                model.setCardType(binDescEnt.getCardType());
			        		de61 = OGSCardDesc.generateDe61New(Boolean.valueOf(model.getPinEntered()),Boolean.valueOf(model.getEmv()),model.getIsoData().get("DE52").toString(),model.getMerchantAddress(),model.getMerchantPinCode());
							model.setDe61(de61);
			        		model.setTid(deviceEnt.getBankTid());
			        		AcqTxnEntity txnEnt = new AcqTxnEntity();
			        		txnEnt.setUserid(Integer.valueOf(""+userEnt.getUserId()));						
			        		txnEnt.setOrgId(Integer.valueOf(""+userEnt.getOrgId()));
			        		txnEnt.setMerchantId(Integer.valueOf(""+org.getMerchantId()));
			        		txnEnt.setMobile("0000");
			        		Double amt = Double.valueOf(model.getIsoData().get("DE04").toString())/100;
			        		txnEnt.setAmount(amt.toString());
			        		if(!userEnt.getAcquirerCode().equalsIgnoreCase("Acquiro")){
			        			//System.out.println("fffffffffffffff sssssssssssssssss");
			        			settingEnt = (AcqSettingEntity) session
			        				.createCriteria(AcqSettingEntity.class)
									.add(Restrictions.eq("acquirerCode", userEnt.getAcquirerCode())).uniqueResult();
			        			if(settingEnt.getMaintenanceMode().equalsIgnoreCase("1")&&!settingEnt.getMaintenanceReason().equalsIgnoreCase("NA")){
			        				logger.info(",Acquirer server maintenance mode is on, transaction can't be place");
			        				response.setStatus(EnumStatusConstant.MaintenanceMode.getId());
			        				response.setMessage(EnumStatusConstant.MaintenanceMode.getDescription());
			        				//response.setResult(responseMap);
			        				return response;
			        			}
								txnEnt.setEzMdr("0.0");
								txnEnt.setBankMdr("0.0");
								txnEnt.setAcquirerMdr("0.0");
								if(Double.valueOf(model.getIsoData().get("DE04").toString())<=Double.valueOf(settingEnt.getTxnTaxAmt())){
									txnEnt.setServiceTax("0.0");
								}else{
									txnEnt.setServiceTax(settingEnt.getServiceTax());
								}								
								if(Double.valueOf(model.getIsoData().get("DE04").toString())<Double.valueOf(settingEnt.getSystmUtltyAmt())){
									txnEnt.setSystemUtilityFee(settingEnt.getSystmUtltyFee());
								}else{
									txnEnt.setSystemUtilityFee("0.0");
								}
						}else{
							//System.out.println("gggggggggggggggg sssssssssssssssss");
							settingEnt = (AcqSettingEntity) session
									.createCriteria(AcqSettingEntity.class)
									.add(Restrictions.eq("id", 1l)).uniqueResult();
								txnEnt.setEzMdr("0.0");
								txnEnt.setBankMdr("0.0");
								txnEnt.setAcquirerMdr("0.0");
								if(Double.valueOf(model.getIsoData().get("DE04").toString())<=Double.valueOf(settingEnt1.getTxnTaxAmt())){
									txnEnt.setServiceTax("0.0");
								}else{
									txnEnt.setServiceTax(settingEnt1.getServiceTax());
								}
								if(Double.valueOf(model.getIsoData().get("DE04").toString())<Double.valueOf(settingEnt.getSystmUtltyAmt())){
									txnEnt.setSystemUtilityFee(settingEnt.getSystmUtltyFee());
								}else{
									txnEnt.setSystemUtilityFee("0.0");
								}
							//}
						}
						date = new Date();
						currentTimestamp = new Timestamp(date.getTime());						
						txnEnt.setDateTime(currentTimestamp);
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						txnEnt.setOtpDateTime(sdf.format(date));
						txnEnt.setStatus(503);
						txnEnt.setMobile("NA");
						txnEnt.setEmail("NA");
						txnEnt.setTxnType("CARD");
						txnEnt.setDescription("Transaction Initiated");
						txnEnt.setCarPanNo(model.getMaskPan());
						txnEnt.setCustomerId("0");
						txnEnt.setPayoutStatus(700);
						txnEnt.setPayoutDateTime(currentTimestamp);
						txnEnt.setSwitchLab("OGS");
						txnEnt.setAcquirerCode(userEnt.getAcquirerCode());
						txnEnt.setCashBackAmt("0.0");
						txnEnt.setAcqPayoutStatus(800);
						txnEnt.setAcqPayoutDateTime(currentTimestamp);
						model.setRequestTime(currentTimestamp.toString());
						if(model.getApplicationCertificate()==null||model.getApplicationCertificate()==""){
							txnEnt.setAppCertificate("NA");
						}else{
							txnEnt.setAppCertificate(model.getApplicationCertificate());
						}
						
						if(model.getAid()==null||model.getAid()==""){
							txnEnt.setAid("NA");
						}else
							txnEnt.setAid(model.getAid());
						txnEnt.setScriptResult("NA");		
						session.save(txnEnt);
						txnId = txnEnt.getId();	
						String stan = String.format("%06d", txnEnt.getId());
						if(stan.length()<=6){
							model.setStan(stan);
						}else{
							model.setStan(stan.substring(stan.length()-6));
						}
						
						stan=null;
						//System.out.println("model.getStan():"+model.getStan());
						model.setDe12((""+txnEnt.getOtpDateTime()).substring(11,19).replace(":",""));
						//System.out.println("localtime::purchase::::"+model.getDe12());
						AcqCardDetails cardEntity = new AcqCardDetails();
						//System.out.println("txnEnt.getId(): "+txnEnt.getId());
						cardEntity.setTransactionId(txnEnt.getId());
						cardEntity.setStan(model.getStan());
						cardEntity.setTerminalId(deviceEnt.getBankTid());						
						cardEntity.setRrNo(rrNo+model.getStan());
						cardEntity.setAuthCode("NA");
						cardEntity.setBatchNo("0");
						if(model.getCardHolderName()==null||model.getCardHolderName()==""){
							cardEntity.setCardHolderName("NA");
						}else{
							cardEntity.setCardHolderName(model.getCardHolderName());
						}
						if(model.getEmv().equalsIgnoreCase("true")){
							cardEntity.setCardType(model.getCardType()+"|Chip");
						}else{
							cardEntity.setCardType(model.getCardType()+"|Swipe");
						}		
						cardEntity.setIpAddress(model.getIpAddress());
						cardEntity.setImeiNo("123123");
						cardEntity.setLatitude(model.getLatitude());
						cardEntity.setLongitude(model.getLongitude());
						//cardEntity.setStan("NA");//CustomerMobile(model.get)
						cardEntity.setDe61(model.getDe61());
						cardEntity.setGmtDateTime(gmtDateTime);						
						model.setGmtDateTime(gmtDateTime);
						cardEntity.setPinEntered(model.getPinEntered());
						if(model.getApplicationName()==null||model.getApplicationName()==""){
							cardEntity.setApplicationName("NA");
						}else{
							cardEntity.setApplicationName(model.getApplicationName());
						}
						session.save(cardEntity);
						tx.commit();
						model.setDe12((""+txnEnt.getOtpDateTime()).substring(11,19).replace(":",""));
						model.setRrNo(cardEntity.getRrNo());
						//System.out.println("localtime::::::"+model.getDe12());
					} catch (Exception e) {
						response.setStatus(EnumStatusConstant.RollBackError.getId());
						response.setMessage(EnumStatusConstant.RollBackError.getDescription());
						logger.error("error to save txn details" +e);
						System.err.println("error to save txn details" +e);
						return response;
					}
				}
			} catch (Exception e) {
				response.setStatus(EnumStatusConstant.RollBackError.getId());
				response.setMessage(EnumStatusConstant.RollBackError
						.getDescription());
				logger.error("error to persist data "+e);
				System.out.println("error to save entity "+e);
				return response;
			} finally {
				session.close();
			}			
			if(txnId==0||txnId<=1){
				response.setStatus(EnumStatusConstant.RollBackError.getId());
				response.setMessage("Technical Error,Trasaction id not generated");
				response.setResult(jsonObj);
				logger.warn("error to generate txnid");
				return response;
			}
			System.out.println("txnIdtxnId:"+txnId);
			jsonObj.put("transactionId", txnId+"");
			System.out.println("data persistance done");			
			String purchaseRequestIso = purchaseTxn(model);
			model.setRequestIso(model.getIsoData().get("ksn")+purchaseRequestIso);
			String ogsResponse = OGSCardDesc.newNetworkTransport(model.getIsoData().get("ksn")+purchaseRequestIso,model.getUat());
			System.out.println("purchaseResponseIso ver111111: "+ogsResponse);
			if(ogsResponse!=null&&ogsResponse.equalsIgnoreCase("SocketException")){
				response.setStatus(EnumStatusConstant.TransactionTimeOut.getId());
				response.setMessage(EnumStatusConstant.TransactionTimeOut.getDescription());
				jsonObj.put("gmtDateTime", model.getGmtDateTime());
				response.setResult(jsonObj);
				return response;
			}
			model.setRespnseIso(ogsResponse);			
			String subStr = ogsResponse.substring(22);
			jsonObj = getResponseCode(subStr,"doPurchase");			
			System.out.println(jsonObj+":doneeeeeeeeeeeeee: "+txnId);
			if(jsonObj.isEmpty()||jsonObj.size()<4){
				response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
				response.setMessage("Error parsing response");
			}else{
				response.setStatus(EnumStatusConstant.OK.getId());
				response.setMessage(EnumStatusConstant.OK.getDescription());
			}
			jsonObj.put("transactionId", txnId+"");
			response.setResult(jsonObj);
			System.out.println("final jsonObj:"+jsonObj);
			Transaction txx = null;
			Session sessionn = null;
			try{
				sessionn = getNewSession();
				AcqCardDetails ent = (AcqCardDetails)sessionn.createCriteria(AcqCardDetails.class).add(Restrictions.eq("transactionId",Integer.valueOf(txnId))).uniqueResult();
				if(ent==null){
					logger.error("ent is null");
				}else{
					AcqPlaceTransactionEntity mainEnt = (AcqPlaceTransactionEntity)sessionn.createCriteria(AcqPlaceTransactionEntity.class).add(Restrictions.eq("id",Integer.valueOf(txnId))).uniqueResult();
					if(mainEnt!=null&&jsonObj.containsKey("responseMessage")){
						mainEnt.setDescription(jsonObj.get("responseMessage").toString());
						sessionn.update(mainEnt);
					}
					if(jsonObj.containsKey("DE38")){
						ent.setAuthCode(jsonObj.get("DE38").toString());					
					if(jsonObj.containsKey("DE12"))
						ent.setBatchNo(jsonObj.get("DE12").toString());					
					if(model.getGmtDateTime()!=null)
						ent.setGmtDateTime(model.getGmtDateTime());		
					
					txx = sessionn.beginTransaction();
					sessionn.update(ent);
					try{
						date = new Date();
						currentTimestamp = new Timestamp(date.getTime());
						AcqOgsISOEntity isoEnt = new AcqOgsISOEntity();
						isoEnt.setTransactionId(txnId+"");//""+txnEnt.getId());
						isoEnt.setIsoRequest(model.getRequestIso());
						isoEnt.setIsoResponse(model.getRespnseIso());
						isoEnt.setSwitchLab("OGS");
						isoEnt.setRequestTime(model.getRequestTime());
						isoEnt.setResponseTime(currentTimestamp.toString());
						sessionn.save(isoEnt);
					}catch(Exception e){
						logger.error("error to save iso message" +e);
						System.err.println("error to save iso message" +e);
					}	
					txx.commit();
				}				
					
				}
			}catch(Exception e){
				logger.error("error to update entity "+e);
				System.out.println("error to save auth code "+e);
			}finally{
				if(sessionn.isOpen()==true||sessionn.isConnected()==true){
					sessionn.close();
				}
			}
			System.out.println("finally done ");
			return response;
		}catch(Exception e){
			response.setStatus(EnumStatusConstant.RollBackError.getId());
			response.setMessage(EnumStatusConstant.RollBackError.getDescription());
			response.setResult(jsonObj);
			System.err.println("error to call purchase txn "+e);
			return response;
		}
	}
	
	static GenericPackager packe = null;
	JSONObject emptyJson = new JSONObject();

	public static JSONObject getResponseCode(String responseIso,String reqType){
	      // PedApi pedApi = new PedApi();
	      // pedApi.PedDukptIncKSN_Api(1);
		JSONObject voidObj = new JSONObject();
	      // Log.d(TAG, "getResponseCode() called with: responseIso = [" + responseIso + "]");
	       String ress = responseIso; //.substring(2);
	       String bitmapStr1 = ress.substring(4,20);
	       String hex2Binary1 = hexToBin(bitmapStr1);
	       String bitmapStr2 ="";
	       if(hex2Binary1.startsWith("1")){
	           bitmapStr2 = ress.substring(4,36);
	           System.out.println("bitmapStr2:"+bitmapStr2);
	           bitmapStr2 = hexToBin(bitmapStr2);
	       }else{
	           bitmapStr2=hex2Binary1;
	       }
	       System.out.println("BITMAP:"+bitmapStr2);
	       try {
	    	   ISOMsg isoMessage = new ISOMsg();	    	   
	       	if(reqType.equals("doPurchase")){
	       		packe  = new GenericPackager("purchaseTxn.xml");
	       		isoMessage.setPackager(packe);
		    	isoMessage.unpack(ress.getBytes());
	       		return logISOMsgg(isoMessage);
	       	}else if(reqType.equals("doCashBack")){
	       		packe  = new GenericPackager("purchaseTxn.xml");
	       		isoMessage.setPackager(packe);
		    	isoMessage.unpack(ress.getBytes());
	       		return logISOMssCashBack(isoMessage);
	       	}else if(reqType.equals("doCashAtPos")){
	       		packe  = new GenericPackager("purchaseTxn.xml");
	       		isoMessage.setPackager(packe);
		    	   isoMessage.unpack(ress.getBytes());
	       		return logISOMsgCashAtPos(isoMessage);
	       	}else if(reqType.equals("doVoid")){
	       		packe  = new GenericPackager("purchaseTxn.xml");
	       		isoMessage.setPackager(packe);
		    	   isoMessage.unpack(ress.getBytes());
	       		return logISOMsgVoid(isoMessage);
	       	}else if(reqType.equals("doReversal")){
	       		packe  = new GenericPackager("purchaseTxn.xml");
	       		isoMessage.setPackager(packe);
		    	isoMessage.unpack(ress.getBytes());
	       		return logISOMsgReversal(isoMessage);
	       	}
	       }catch(Exception e) {
	    	  System.err.println("error to parse response "+e);
	          logger.error("return parsed void obj "+e);
	          return voidObj;
	       }
	       return voidObj;
	   }


	private static JSONObject logISOMsgCashAtPos(ISOMsg msg) {
		json = new JSONObject();
		System.out.println("----ISO MESSAGE-----");
		try {
			System.out.println("  MTI : " + msg.getMTI());
			for (int i=1;i<=msg.getMaxField();i++) {
				if (msg.hasField(i)){
					System.out.println("    Field-"+i+" : "+msg.getString(i));
					if(i==2)
						json.put("DE02", msg.getString(i));
					if(i==7)
						json.put("DE07", msg.getString(i));
					if(i==11)
						json.put("DE11", msg.getString(i));
					if(i==12)
						json.put("DE12", msg.getString(i));
					if(i==37)
						json.put("DE37", msg.getString(i));
					if(i==38)
						json.put("DE38", msg.getString(i));
					if(i==39){
						json.put("DE39", msg.getString(i));
						json.put("responseMessage", OGSCardDesc.getFailReason(msg.getString(i)));
					}
					if(i==55)
						json.put("DE55", msg.getString(i));	
					if(i==61)
						json.put("DE61", msg.getString(i));
				}
			}
			if(!json.containsKey("DE38"))
				json.put("DE38", "");
			if(!json.containsKey("DE55"))
				json.put("DE55", "");
			System.out.println("json obj:"+json);
		} catch (Exception e) {
			System.err.println("errr in parse iso response "+e);
		} finally {
			System.out.println("--------------------");
		}
		return json;
	}
	
	static JSONObject json = null;
	int i=0;
		private static JSONObject logISOMsgg(ISOMsg msg) {
			JSONObject json = new JSONObject();
			System.out.println("----ISO MESSAGE-----");
			try {
				System.out.println("  MTI : " + msg.getMTI());
				for (int i=1;i<=msg.getMaxField();i++) {
					if (msg.hasField(i)){
						System.out.println("    Field-"+i+" : "+msg.getString(i));
						if(i==2)
							json.put("DE02", msg.getString(i));
						if(i==7)
							json.put("DE07", msg.getString(i));
						if(i==11)
							json.put("DE11", msg.getString(i));
						if(i==12)
							json.put("DE12", msg.getString(i));
						if(i==37)
							json.put("DE37", msg.getString(i));
						if(i==38)
							json.put("DE38", msg.getString(i));
						if(i==39){
							json.put("DE39", msg.getString(i));
							json.put("responseMessage", OGSCardDesc.getFailReason(msg.getString(i)));
						}
						if(i==55)
							json.put("DE55", msg.getString(i));	
						if(i==61)
							json.put("DE61", msg.getString(i));
					}
				}
				if(!json.containsKey("DE38"))
					json.put("DE38", "");
				if(!json.containsKey("DE55"))
					json.put("DE55", "");
				System.out.println("json obj:"+json);
			} catch (Exception e) {
				System.err.println("errr in parse iso response "+e);
			} finally {
				System.out.println("--------------------");
			}
			return json;
		}
		private static JSONObject logISOMsggReversalNew(ISOMsg msg) {
			json = new JSONObject();
			System.out.println("----ISO MESSAGE-----");
			try {
				System.out.println("  MTI : " + msg.getMTI());
				for (int i=1;i<=msg.getMaxField();i++) {
					if (msg.hasField(i)){
						System.out.println("    Field-"+i+" : "+msg.getString(i));
						if(i==2)
							json.put("DE02", msg.getString(i));
						if(i==3)
							json.put("DE03", msg.getString(i));
						if(i==4)
							json.put("DE04", msg.getString(i));
						if(i==7)
							json.put("DE07", msg.getString(i));
						if(i==11)
							json.put("DE11", msg.getString(i));
						if(i==12)
							json.put("DE12", msg.getString(i));
						if(i==13)
							json.put("DE13", msg.getString(i));
						if(i==14)
							json.put("DE14", msg.getString(i));
						if(i==22)
							json.put("DE22", msg.getString(i));
						if(i==23)
							json.put("DE23", msg.getString(i));
						if(i==25)
							json.put("DE25", msg.getString(i));
						if(i==32)
							json.put("DE32", msg.getString(i));
						if(i==37)
							json.put("DE37", msg.getString(i));
						if(i==38)
							json.put("DE38", msg.getString(i));
						if(i==39){
							json.put("DE39", msg.getString(i));
							json.put("responseMessage", OGSCardDesc.getFailReason(msg.getString(i)));
						}if(i==41)
							json.put("DE41", msg.getString(i));
						if(i==42)
							json.put("DE42", msg.getString(i));
						if(i==49)
							json.put("DE49", msg.getString(i));
						if(i==54)
							json.put("DE54", msg.getString(i));
						if(i==55)
							json.put("DE55", msg.getString(i));	
						if(i==61)
							json.put("DE61", msg.getString(i));
						if(i==106)
							json.put("DE106", msg.getString(i));
					}
				}
				if(!json.containsKey("DE38"))
					json.put("DE38", "");
				if(!json.containsKey("DE55"))
					json.put("DE55", "");
				System.out.println("json obj:"+json);
			} catch (Exception e) {
				System.err.println("errr in parse iso response "+e);
			} finally {
				System.out.println("--------------------");
			}
			return json;
		}
		
		
		private static JSONObject logISOMssCashBack(ISOMsg msg) {
			json = new JSONObject();
			System.out.println("----ISO MESSAGE-----");
			try {
				System.out.println("  MTI : " + msg.getMTI());
				for (int i=1;i<=msg.getMaxField();i++) {
					if (msg.hasField(i)){
						System.out.println("    Field-"+i+" : "+msg.getString(i));
						if(i==2)
							json.put("DE02", msg.getString(i));
						if(i==7)
							json.put("DE07", msg.getString(i));
						if(i==11)
							json.put("DE11", msg.getString(i));
						if(i==12)
							json.put("DE12", msg.getString(i));
						if(i==37)
							json.put("DE37", msg.getString(i));
						if(i==38)
							json.put("DE38", msg.getString(i));
						if(i==39){
							json.put("DE39", msg.getString(i));
							json.put("responseMessage", OGSCardDesc.getFailReason(msg.getString(i)));
						}
						if(i==54)
							json.put("DE54", msg.getString(i));	
						if(i==55)
							json.put("DE55", msg.getString(i));	
						if(i==61)
							json.put("DE61", msg.getString(i));
					}
				}
				if(!json.containsKey("DE38"))
					json.put("DE38", "");
				if(!json.containsKey("DE55"))
					json.put("DE55", "");
				System.out.println("json obj:"+json);
			}catch (Exception e) {
				System.err.println("errr in parse cashback response iso "+e);
			}  finally {
				System.out.println("--------------------");
			}
			return json;
		}
		private static JSONObject logISOMsgVoid(ISOMsg msg) {
			JSONObject voidJson = new JSONObject();
			System.out.println("----ISO MESSAGE-----");
			try {
				System.out.println("  MTI : " + msg.getMTI());
				for (int i=1;i<=msg.getMaxField();i++) {
					if (msg.hasField(i)){
						System.out.println("    Field-"+i+" : "+msg.getString(i));
						if(i==2)
							voidJson.put("DE02", msg.getString(i));
						if(i==37)
							voidJson.put("DE37", msg.getString(i));
						if(i==38)
							voidJson.put("DE38", msg.getString(i));
						if(i==39){
							voidJson.put("DE39", msg.getString(i));
							voidJson.put("responseMessage", OGSCardDesc.getFailReason(msg.getString(i)));
						}
						if(i==55)
							voidJson.put("DE55", msg.getString(i));						
					}
				}
				System.out.println("parsing done");
				if(!voidJson.containsKey("DE38"))
					voidJson.put("DE38", "");
				if(!voidJson.containsKey("DE55"))
					voidJson.put("DE55", "");
				System.out.println("voidJson obj:"+voidJson);
			} catch (Exception e) {
				System.err.println("error to parse iso "+e);
			} finally {
				System.out.println("--------------------");
			}
			return voidJson;
		}
		
		private static JSONObject logISOMsgReversal(ISOMsg msg) {
			json = new JSONObject();
			System.out.println("----ISO MESSAGE-----");
			try {
				System.out.println("  MTI : " + msg.getMTI());
				for (int i=1;i<=msg.getMaxField();i++) {
					if (msg.hasField(i)){
						System.out.println("    Field-"+i+" : "+msg.getString(i));
						if(i==2)
							json.put("DE02", msg.getString(i));
						if(i==37)
							json.put("DE37", msg.getString(i));
						if(i==38)
							json.put("DE38", msg.getString(i));
						if(i==39){
							json.put("DE39", msg.getString(i));
							json.put("responseMessage", OGSCardDesc.getFailReason(msg.getString(i)));
						}
						if(i==55)
							json.put("DE55", msg.getString(i));						
					}
				}
				if(!json.containsKey("DE38"))
					json.put("DE38", "");
				if(!json.containsKey("DE55"))
					json.put("DE55", "");
				System.out.println("json obj:"+json);
			} catch (Exception e) {
				System.err.println("errr in parse iso response "+e);
			}  finally {
				System.out.println("--------------------");
			}
			return json;
		}
	
	public static String getResponseCode(String responseIso){
	       // PedApi pedApi = new PedApi();
	       // pedApi.PedDukptIncKSN_Api(1);

	       // Log.d(TAG, "getResponseCode() called with: responseIso = [" + responseIso + "]");
	        String ress = responseIso; //.substring(2);
	        String bitmapStr1 = ress.substring(4,20);
	        System.out.println("Bitmap string:"+bitmapStr1);
	        //String hex2Binary1 = Utils.hexToBin(bitmapStr1);
	        String hex2Binary1 = hexToBin(bitmapStr1);
	        String bitmapStr2 ="";
	        if(hex2Binary1.startsWith("1")){
	            bitmapStr2 = ress.substring(4,36);
	            System.out.println("bitmapStr2:"+bitmapStr2);
	            bitmapStr2 = hexToBin(bitmapStr2);
	        }else{
	            bitmapStr2=hex2Binary1;
	        }
	        System.out.println("BITMAP:"+bitmapStr2);
	        try {
	            //InputStream insResponse = context.getResources().openRawResource(getXml(type));
	            GenericPackager packe = new GenericPackager("purchaseTxn.xml");
	            ISOMsg isoMessage = new ISOMsg();
	            isoMessage.setPackager(packe);
	            isoMessage.unpack(ress.getBytes());
	            // print the DE list
	            //return Utils.logISOMessageStr(isoMassage);
	            //isoMessage.get
	            logISOMsg(isoMessage);
	            //return getIsoParameter(isoMessage,"39");
	            return "stringstirng";
	        }catch(Exception e) {
	            e.printStackTrace();
	            return "-1";
	        }
	    }
	
	/*@Override
	public DbDto<Object> mv20OGSTxnInitiate(OGSMainTxnInitiateModel model) {
		DbDto<Object> response = new DbDto<Object>();
		HashMap<String, String> responseMap = new HashMap<String, String>();
		try{
			System.out.println("request in purchase dao");
			String purchaseRequestIso = purchaseTxn(model);
			System.out.println("purchaseRequestIso:"+purchaseRequestIso);
			String ogsResponse = OGSCardDesc.newNetworkTransport(purchaseRequestIso,model.getUat());
			System.out.println("ogsResponse:"+ogsResponse);
			//JSONObject switchResponse = new JSONObject();
			responseMap.put("DE39", "abc");
			responseMap.put("DE55", "abc");
			responseMap.put("DE38", "abc");
			responseMap.put("responseMessage", "Success");
			response.setStatus(EnumStatusConstant.OK.getId());
			response.setMessage(EnumStatusConstant.OK.getDescription());
			response.setResult(responseMap);
			return response;
		}catch(Exception e){
			response.setStatus(EnumStatusConstant.RollBackError.getId());
			response.setMessage(EnumStatusConstant.RollBackError.getDescription());
			response.setResult(responseMap);
			return response;
		}
	}*/
		
	public static String purchaseCashBackTxn(OGSMainTxnInitiateModel model)
            throws ISOException, IOException {
    SimpleDateFormat dateFormatGmt = new SimpleDateFormat("MMddHHmmss");
        String dateTime = dateFormatGmt.format(new Date());
        int hod =  Integer.valueOf(dateTime.substring(4,6));
        String hods = "";
        if(hod<10){
            hods = "0"+hod;
        }else{
            hods = ""+hod;
        }
        String MMdd = dateTime.substring(0,4);
        String hhmmss = dateTime.substring(4, 10);
        Calendar cal = Calendar.getInstance();
        String year = ""+cal.get(Calendar.YEAR);
        String charYear = year.substring(3,4);
        int doy = cal.get(Calendar.DAY_OF_YEAR);
        String doys ="";
        if(doy<10){
            doys = "00"+doy;
        }else if(doy>10&&doy<100){
            doys = "0"+doy;
        }else{
            doys = ""+doy;
        }
        dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
        //String rrNo = charYear+doys+hods+model.getIsoData().get("DE11");
        ISOMsg isoMsg = new ISOMsg();
        GenericPackager packager = new GenericPackager("cashback_request.xml");
        isoMsg.setPackager(packager);
        isoMsg.setMTI("0200");
        isoMsg.set(2,model.getIsoData().get("DE02").toString());
        isoMsg.set(3, "090000");  // procesing code
        isoMsg.set(4, model.getIsoData().get("DE04").toString());  // txn amount
       // System.out.println("0004444444444 done");
        isoMsg.set(7, model.getGmtDateTime()); //txn date time
       // System.out.println("11 11 11 ");
        //isoMsg.set(11,model.getIsoData().get("DE11").toString());
        isoMsg.set(11,model.getStan());
        /*SimpleDateFormat s = new SimpleDateFormat("HHmmss");
        String localFormat = s.format(new Date());*/
        //isoMsg.set(12,hhmmss); //Time, local transaction hhmmss
        System.out.println("de12 in cashback:"+model.getDe12());
        isoMsg.set(12,model.getDe12());
        isoMsg.set(13, MMdd); //Date,local transaction // MMdd
       // System.out.println("14 14 14 ");
        isoMsg.set(14, model.getIsoData().get("DE14").toString());  //expiry date
     /* AcqCardBinDesc binDescEnt = new AcqCardBinDesc();
      binDescEnt.setCardType(getCardFromPan(model.getMaskPan()));
      model.setCardType(binDescEnt.getCardType());*/
      
        if(model.getEmv().equalsIgnoreCase("true")){        	
        	 if(model.getCardType().equals(OGSCardDesc.VISA)||model.getCardType().equals(OGSCardDesc.MASTER)||model.getCardType().equals(OGSCardDesc.MAESTRO)){
                 isoMsg.set(22, "051"); //VISA
             }else{
                 isoMsg.set(22, "951"); //Rupay
             }
        }else{
        	if(model.getFallBack().equalsIgnoreCase("true")){        		
                if(model.getCardType().equals(OGSCardDesc.RUPAY) ||
                		model.getCardType().equals(OGSCardDesc.DISCOVER) ||
                		model.getCardType().equals(OGSCardDesc.DINERS) ||
                		model.getCardType().equals(OGSCardDesc.MASTER)||
                		model.getCardType().equals(OGSCardDesc.MAESTRO)){
                    isoMsg.set(22, "801"); //for rupay
                }else{
                    isoMsg.set(22, "901"); //for visa
                }
            }else{
                isoMsg.set(22, "901"); //POSentrymode
            }
        }      
        try{
            if(model.getIsoData().get("DE23")!=null&&model.getIsoData().get("DE23").toString().length()>0&&!model.getIsoData().get("DE23").toString().equals("000")){
                isoMsg.set(23, model.getIsoData().get("DE23").toString());
            }
        }catch (NullPointerException e){}
        //System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaa");
        isoMsg.set(25, "00"); //POSconditioncode //ppo
        //isoMsg.set(32, "720399"); //Acquiringinstitutioncode //apending 25
        //isoMsg.set(32, "00201000003"); 
        isoMsg.set(32, model.getAcquirerId());
        isoMsg.set(35, model.getIsoData().get("DE35").toString()); //Track2data
        System.out.println("model.getRRno():"+model.getRrNo());
        isoMsg.set(37,model.getRrNo());
        isoMsg.set(40, model.getIsoData().get("DE40").toString());
        //System.out.println("tidddddddddddddd:"+model.getTid());
        isoMsg.set(41, model.getTid());
       // System.out.println("midddddddddddddd:"+model.getMid());
        isoMsg.set(42,model.getMid());
        isoMsg.set(49, "356");		//Currencycode, transaction
        if(model.getPinEntered().equalsIgnoreCase("true")){
            String pinblock =model.getIsoData().get("DE52").toString();
            if(!pinblock.equals("0000000000000000")){
                isoMsg.set(52, pinblock); // Pin block
                if(model.getCardType().equals(OGSCardDesc.VISA)){
                    isoMsg.set(53, "2001010100000000"); // for visa only
                }
            }
        }
        isoMsg.set(54, "9090356D"+model.getIsoData().get("DE54").toString());
        if(model.getIsoData().get("DE55")==null||model.getIsoData().get("DE55").toString()==""){
        	logger.info("de50 is not part of iso ");
        }else{
        	isoMsg.set(55, model.getIsoData().get("DE55").toString());     
        }
        //DE60 For Visa only
        /*if(binDescEnt.getCardType().equals(OGSCardDesc.VISA)){
            if(model.getEmv().equalsIgnoreCase("true")){
                isoMsg.set(60, "950000100000"); // for EMV
            }else{
                if(model.getFallBack().equalsIgnoreCase("true")){
                    isoMsg.set(60, "951000000000"); // for EMV fallback txn
                }else{
                    isoMsg.set(60, "920000000000"); // for MAG Strip
                }
            }
        }        */
        //String de61 = OGSCardDesc.generateDe61(Boolean.valueOf(model.getPinEntered()),Boolean.valueOf(model.getEmv()),model.getIsoData().get("DE52").toString());
        isoMsg.set(61, model.getDe61());//POSdata code
       // model.setDe61(de61);
        String plainIso  = null;
	    try{
	        isoMsg.set(106,model.getIsoData().get("DE106").toString());
	        logISOMsg(isoMsg);
	        byte[] data = isoMsg.pack();
	        plainIso = new String(data);
	        System.out.println("purchase request iso: "+plainIso);
	    }catch(Exception e){
        	logger.error("error to parse prchscshbck "+e);
        }
        return plainIso;//+"||"+de61+"||"+gmtDateTime;
    }
	
	public static String purchaseTxn(OGSMainTxnInitiateModel model)
            throws ISOException, IOException {
    SimpleDateFormat dateFormatGmt = new SimpleDateFormat("MMddHHmmss");
        String dateTime = dateFormatGmt.format(new Date());
        int hod =  Integer.valueOf(dateTime.substring(4,6));
        String hods = "";
        if(hod<10){
            hods = "0"+hod;
        }else{
            hods = ""+hod;
        }
        String MMdd = dateTime.substring(0,4);
        String hhmmss = dateTime.substring(4, 10);
        Calendar cal = Calendar.getInstance();
        String year = ""+cal.get(Calendar.YEAR);
        String charYear = year.substring(3,4);
        int doy = cal.get(Calendar.DAY_OF_YEAR);
        String doys ="";
        if(doy<10){
            doys = "00"+doy;
        }else if(doy>10&&doy<100){
            doys = "0"+doy;
        }else{
            doys = ""+doy;
        }
        dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
        ISOMsg isoMsg = new ISOMsg();
        GenericPackager packager = new GenericPackager("purchaseTxn.xml");
        isoMsg.setPackager(packager);
        isoMsg.setMTI("0200");
        isoMsg.set(2,model.getIsoData().get("DE02").toString());
        isoMsg.set(3, "000000");  // procesing code
      //  String longAmount = String.format("%012d",Integer.valueOf(model.getIsoData().get("DE11")).replace(".","")));
        System.out.println("000004444444444444");
        isoMsg.set(4, model.getIsoData().get("DE04").toString());  // txn amount
        System.out.println("0004444444444 done");
        //isoMsg.set(7, gmtDateTime); //txn date time
        isoMsg.set(7, model.getGmtDateTime()); //txn date time
        //System.out.println("11 11 11 ");
        //isoMsg.set(11,model.getIsoData().get("DE11").toString());
        System.out.println("model.getStan():"+model.getStan());		
        isoMsg.set(11,model.getStan());
        /*SimpleDateFormat s = new SimpleDateFormat("HHmmss");
        String localFormat = s.format(new Date());*/
        //isoMsg.set(12,hhmmss); //Time, local transaction hhmmss
        System.out.println("de12 in purchase:"+model.getDe12());
        isoMsg.set(12,model.getDe12());
        isoMsg.set(13, MMdd); //Date,local transaction // MMdd
        //System.out.println("14 14 14 ");
        isoMsg.set(14, model.getIsoData().get("DE14").toString());  //expiry date
        /*AcqCardBinDesc binDescEnt = new AcqCardBinDesc();
        binDescEnt.setCardType(getCardFromPan(model.getMaskPan()));
        model.setCardType(binDescEnt.getCardType());*/
        
        
        
        if(model.getEmv().equalsIgnoreCase("true")){        	
        	 if(model.getCardType().equals(OGSCardDesc.VISA)||model.getCardType().equals(OGSCardDesc.MASTER)||model.getCardType().equals(OGSCardDesc.MAESTRO)){
                 isoMsg.set(22, "051"); //VISA
             }else{
                 isoMsg.set(22, "951"); //Rupay
             }
        }else{
        	if(model.getFallBack().equalsIgnoreCase("true")){        		
                if(model.getCardType().equals(OGSCardDesc.RUPAY) ||
                		model.getCardType().equals(OGSCardDesc.DISCOVER) ||
                		model.getCardType().equals(OGSCardDesc.DINERS) ||
                		model.getCardType().equals(OGSCardDesc.MASTER)||
                		model.getCardType().equals(OGSCardDesc.MAESTRO)){
                    isoMsg.set(22, "801"); //for rupay
                }else{
                    isoMsg.set(22, "901"); //for visa
                }
            }else{
                isoMsg.set(22, "901"); //POSentrymode
            }
        }
        //isoMsg.set(22, "051"); //VISA
        //System.out.println("99999999999999999999");
      
        try{
            if(model.getIsoData().get("DE23")!=null&&model.getIsoData().get("DE23").toString().length()>0&&!model.getIsoData().get("DE23").toString().equals("000")){
                isoMsg.set(23, "0"+model.getIsoData().get("DE23").toString());
            }
        }catch (NullPointerException e){}
        //System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaa");
        isoMsg.set(25, "00"); //POSconditioncode //ppo
       // isoMsg.set(32, "720399"); //Acquiringinstitutioncode //apending 25
        //isoMsg.set(32, "00201000003");  
        isoMsg.set(32, model.getAcquirerId());
        isoMsg.set(35, model.getIsoData().get("DE35").toString()); //Track2data
        System.out.println("rr no: "+model.getRrNo());
        isoMsg.set(37,model.getRrNo());
        isoMsg.set(40, model.getIsoData().get("DE40").toString());
        isoMsg.set(41, model.getTid());
        isoMsg.set(42,model.getMid());
        //isoMsg.set(48, "POS01"); //Additional data
        isoMsg.set(49, "356");		//Currencycode, transaction
        if(model.getPinEntered().equalsIgnoreCase("true")){
        	System.out.println("52222222222:"+model.getIsoData().get("DE52"));
            String pinblock =model.getIsoData().get("DE52").toString();
            if(!pinblock.equals("0000000000000000")){ 
                isoMsg.set(52, pinblock); // Pin block
                if(model.getCardType().equals(OGSCardDesc.VISA)){
                    isoMsg.set(53,"2001010100000000"); // for visa only
                }
            }
        }
        //System.out.println("dddddddddddd ddddddddddd ddddddddd");
        if(model.getIsoData().get("DE55")==null||model.getIsoData().get("DE55").toString()==""){
        	logger.info("de50 is not part of iso ");
        }else{
        	isoMsg.set(55, model.getIsoData().get("DE55").toString());     
        }
        //DE60 For Visa only
        /*if(binDescEnt.getCardType().equals(OGSCardDesc.VISA)){
        	 System.out.println(" fffffffff deee");
            if(model.getEmv().equalsIgnoreCase("true")){
                isoMsg.set(60, "950000100000"); // for EMV
            }else{
                if(model.getFallBack().equalsIgnoreCase("true")){
                    isoMsg.set(60, "951000000000"); // for EMV fallback txn
                }else{
                    isoMsg.set(60, "920000000000"); // for MAG Strip
                }
            }
        }*/
        //System.out.println("model.getPinEntered():"+model.getPinEntered());
       // System.out.println("model.getEmv():"+model.getEmv());
       // System.out.println("model.getAddress()"+model.getMerchantAddress());
       // System.out.println("model.pincode()"+model.getMerchantPinCode());
        //String de61 = OGSCardDesc.generateDe61(Boolean.valueOf(model.getPinEntered()),Boolean.valueOf(model.getEmv()),model.getIsoData().get("DE52").toString());
        
        isoMsg.set(61, model.getDe61());//POSdata code
        //model.setGmtDateTime(gmtDateTime);
        String plainIso  = null;
	    try{
	    	isoMsg.set(106,model.getIsoData().get("DE106").toString());
	    	// System.out.println("ggggggggggggggg");
	    	logISOMsg(isoMsg);
	    	// System.out.println("hhhhhhhhhhhhhhh");
	    	byte[] data = isoMsg.pack();
	    	plainIso = new String(data);
	    	System.out.println("purchase request iso: "+plainIso);
	    }catch(Exception e){
        	System.err.println("error "+e);
        }
	    model.setRequestIso(plainIso);
        //return plainIso+"||"+de61+"||"+gmtDateTime;
	    return plainIso;
    }
	
	static String getCardFromPan(String card){ 
		String cardNo = card.replaceAll("X","0");		
		String regVisa = "^4[0-9]{12}(?:[0-9]{3})?$";
		String regMaster = "^5[1-5][0-9]{14}$"; 
		String regExpress = "^3[47][0-9]{13}$"; 
		String regDiners = "^3(?:0[0-5]|[68][0-9])[0-9]{11}$"; 
		String regDiscover = "^6(?:011|5[0-9]{2})[0-9]{12}$"; 
		String regJCB= "^(?:2131|1800|35\\d{3})\\d{11}$"; 
		String regRupay= "^6[0-9]{15}$"; 
		String regMaestro= "^(?:5[0678]\\d\\d|6304|6390|67\\d\\d)\\d{8,15}$"; 
		if(cardNo.matches(regVisa)) 
			return "Visa"; 
		if (cardNo.matches(regMaster)) 
			return "MasterCard"; 
		if (cardNo.matches(regExpress)) 
			return "American Express"; 
		if (cardNo.matches(regDiners)) 
			return "DINERS"; 
		if (cardNo.matches(regDiscover)) 
			return "Discover"; 
		if (cardNo.matches(regJCB)) 
			return "JCB"; 
		if (cardNo.matches(regRupay)) 
			return "Rupay"; 
		if (cardNo.matches(regMaestro)) 
			return "Maestro"; 
		return ""; 
	}

	
	@Override
	public DbDto<Object> vm20OgsLogin(OGSMainLoginModel model,
			String clientIpAddress) {
		DbDto<Object> response = new DbDto<Object>();
		
		//String result ="{\"status"\":\"0",	"message":"",	"maintenanceMode":"",	"sessionId":"",	"TID":"",	"MID":""};"
		JSONObject json = new JSONObject();
		json.put("sessionId", "9914");
		json.put("maintenanceMode", "123123");
		json.put("TID", "123123");
		json.put("MID", "123123");
		response.setStatus(EnumStatusConstant.OK.getId());
		response.setMessage(EnumStatusConstant.OK.getDescription());
		response.setResult(json);
		/*try {
			session = getNewSession();
			//logReport.append(",Info: Hibernate session object is created version5");
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
						responseMap.purchaseTxnsetStatus(EnumStatusConstant.OK.getId());
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
					resultMap.put("sessionId",""+loginEnt.getUserId());
					resultMap.put("banktid",""+banktid);
					resultMap.put("merchantBankMID",""+merchantBankMID);
					resultMap.put("merchantData","NA");
					resultMap.put("de98","NA");
					responseMap.setResult(resultMap);
					responseMap.setStatus(EnumStatusConstant.OK.getId());
					responseMap.setMessage("" + loginEnt.getUserId());
				} else {
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
		}*/
		return response;	
	}
	
	@Override
	public DbDto<Object> vm20OgsKeyExchange(OGSMainKeyExModel model) {
		logger.info("req in vm20ogs keyex dao");
		DbDto<Object>  response = new DbDto<Object> ();
		HashMap<String, String> result = new HashMap<String,String>();
		Session session = null;
		Transaction tx = null;		
		try{
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			session = getNewSession();
			AcqDevice acqDevice = (AcqDevice)session.createCriteria(AcqDevice.class).add(Restrictions.eq("userId",model.getSessionId())).uniqueResult();
			if(acqDevice==null){
				response.setStatus(EnumStatusConstant.NotFound.getId());
				response.setMessage(EnumStatusConstant.UserAccessError.getDescription());
				response.setResult(result);
				return response;
			}
			logger.info("user found");
			AcqKeyExchangeEntity entity = new AcqKeyExchangeEntity();
			entity.setUserId(Long.valueOf(model.getSessionId()));
			entity.setDateTime(sd.format(new Date()));
			tx = session.beginTransaction();
			saveOrUpdate(entity);
			AcqKeyDetailsEntity detailsEnt = (AcqKeyDetailsEntity)session.createCriteria(AcqKeyDetailsEntity.class).add(Restrictions.eq("deviceId",acqDevice.getDeviceSerialNo())).add(Restrictions.eq("userId",acqDevice.getUserId())).uniqueResult();
			logger.info("key details fetched");
			GenericPackager packager = new GenericPackager("keyExchange.xml");
			logger.info("validation xml found");
			ISOMsg isoMsg = new ISOMsg();
			isoMsg.setPackager(packager);
			isoMsg.setMTI(EnumOgsConstant.KeyExchangeMTI.getId());
			SimpleDateFormat dateFormatGmt = new SimpleDateFormat("MMddHHmmss");
			String dateTime = dateFormatGmt.format(new Date());
			Calendar cal1 = Calendar.getInstance();
			int hod =  Integer.valueOf(dateTime.substring(4,6));
			String hods = "";
			if(hod<10){
				hods = "0"+hod;
			}else{
				hods = ""+hod;
			}
			String year = ""+cal1.get(Calendar.YEAR);
			String charYear = year.substring(3,4);
			int doy = cal1.get(Calendar.DAY_OF_YEAR);
			logger.info("doy:"+doy);
			String doys ="";
			if(doy<10){
				doys = "00"+doy;
			}else if(doy>10&&doy<100){
				doys = "0"+doy;
			}else{
				doys = ""+doy;
			}	
			dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
	        isoMsg.set(7,dateFormatGmt.format(new Date())+"");
	        String sytmTraceNo = String.format("%06d",entity.getId());
	        isoMsg.set(11,sytmTraceNo);
	        // isoMsg.set(11, "000"+entity.getId().toString());  	
	        // isoMsg.set(32, "500000");
	        isoMsg.set(32,EnumOgsConstant.AcquiringInstitutionIdentificationcode.getId());
	        isoMsg.set(37,(charYear+doys+hods+sytmTraceNo));//Retrieval reference number // last digit of year(7) +current day of year(69)+current hour+de11
	        
	        isoMsg.set(42,"Acquiro01      ");
	        isoMsg.set(61,"867723020616917");
	        isoMsg.set(70, EnumOgsConstant.NetworkManagementCode.getId());
			logISOMsg(isoMsg);
			logger.info("iso params completed");
			byte[] data = isoMsg.pack();
			String res = vm20OgsKeyExNetworkTransport(new String(data),model.getUat());
			if(res!=null&&res.equals("error to connect ogs")){
				response.setStatus(EnumStatusConstant.IssuerConnectIssue.getId());
				response.setMessage(EnumStatusConstant.IssuerConnectIssue.getDescription());
				response.setResult(null);
				return response;
			}
			String ress = res.substring(2);
			String bitmapStr1 = ress.substring(4,20);
			String hex2Binary1 = getHex2Binary(bitmapStr1);
			String bitmapStr2 ="";
			if(hex2Binary1.startsWith("1")){
				bitmapStr2 = ress.substring(4,36);
				//System.out.println("bitmapStr2:"+bitmapStr2);
				bitmapStr2 = getHex2Binary(bitmapStr2);
			}
			logger.info("BITMAP:"+bitmapStr2);
			try {
				 GenericPackager packe = new GenericPackager("key_response.xml");
		    	 ISOMsg isoMassage = new ISOMsg();
		    	 isoMassage.setPackager(packe);
		    	 isoMassage.unpack(ress.getBytes());
		    	 // print the DE list
		    	 result = logISOMasssgeMap(isoMassage);
		    	 if(result.get("keyExStatus")!=null&&result.get("keyExStatus").equals("00")){
		    		 if(detailsEnt==null){
		    			 AcqKeyDetailsEntity subEnt = new AcqKeyDetailsEntity();
		    			 subEnt.setUserId(acqDevice.getUserId());
		    			 subEnt.setDeviceId(acqDevice.getDeviceSerialNo());
		    			 subEnt.setMessage(result.get("merchantData"));
		    			 saveOrUpdate(subEnt);
		    			 logger.info("key details inserted");
		    		 }else{
		    			 detailsEnt.setDeviceId(acqDevice.getDeviceSerialNo());
		    			 detailsEnt.setMessage(result.get("merchantData"));
		    			 detailsEnt.setDateTime(sd.format(new Date()));
		    			 session.update(detailsEnt);
		    			 logger.info("key details updated");
		    		 }
		    		 // System.out.println("result999999999:"+result);
		    	 }
		    	 tx.commit();
		    	 logger.info("response from key exchange dao");
		    }catch(Exception e){
		    	System.err.println("error to parse ogs message "+e);
		    	response.setStatus(EnumStatusConstant.RollBackError.getId());
				response.setMessage(EnumStatusConstant.RollBackError.getDescription());
				response.setResult(null);
		    }
			response.setStatus(EnumStatusConstant.OK.getId());
			response.setMessage(EnumStatusConstant.OK.getDescription());
			response.setResult(result);
		}catch(java.net.SocketTimeoutException e){
			System.err.println("error to connect ogs "+e);
			response.setStatus(EnumStatusConstant.IssuerConnectIssue.getId());
			response.setMessage(EnumStatusConstant.IssuerConnectIssue.getDescription());
			response.setResult(null);
			return response;
		}catch(java.net.NoRouteToHostException ne){
			System.err.println("issuer is stoped in ogs key exchange dao "+ne);
			response.setStatus(EnumStatusConstant.IssuerConnectIssue.getId());
			response.setMessage(EnumStatusConstant.IssuerConnectIssue.getDescription());
			response.setResult(null);
			return response;
		}catch(Exception e){
			System.err.println("error in ogs key exchange dao "+e);
			response.setStatus(EnumStatusConstant.RollBackError.getId());
			response.setMessage(EnumStatusConstant.RollBackError.getDescription());
			response.setResult(null);
			return response;
		}finally{
			session.close();
		}
		return response;
	}
		public static void main(String arr[]){

		System.out.println("request in void txn dao");
		Session session = null;
		Transaction tx = null;
		String result ="";
		String cardPanNo ="6069850000118656";
		String amount ="000000000100";
		String de7 ="0327145045";
		String rrNo ="708620002298";
		String txnId ="2298";
		try{
			GenericPackager packager = new GenericPackager("voidTxn.xml");	 
			// Create ISO Message
			ISOMsg isoMsg = new ISOMsg();
			isoMsg.setPackager(packager);
			isoMsg.setMTI("0420");
			SimpleDateFormat dateFormatGmt = new SimpleDateFormat("MMddHHmmss");
			String dateTime = dateFormatGmt.format(new Date());			
			String MMdd = dateTime.substring(0,4);
	        String hhmmss = dateTime.substring(4, 10);
			dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
			isoMsg.set(2, cardPanNo);
			String gmtDateTime = dateFormatGmt.format(new Date());
			//System.out.println("gmtdateTime:"+gmtDateTime);
			isoMsg.set(3, "000000");  // procesing code
			String longAmount = String.format("%012d",Integer.valueOf(amount));
			System.out.println("void txn tAmt:"+longAmount);
			isoMsg.set(4, longAmount);  // txn amount			
			System.out.println("model.getDe7()::"+de7);
	        isoMsg.set(7, de7); //txn date time
	        isoMsg.set(7, gmtDateTime); //txn date time
	        String resId = String.format("%06d",Integer.valueOf(txnId));
	        System.out.println("void txn txnId:"+resId);
	        isoMsg.set(11,resId); //Stan value
	        isoMsg.set(12, hhmmss); //Time, local transaction hhmmss
	        isoMsg.set(13, MMdd); //Date,local transaction // MMdd
	        System.out.println("aaaaaaaaaaaaaaaaaaa");      
	        int i=1;
			StringTokenizer st = new StringTokenizer("Acquiro01|00000002|5999|Haryana|Acquiro|Second Floor, Shri JP Tower, 1164/1, New Railway Rd,Gurugram|20170325162753","|");  
			String cardAcceptorId = "";//Merchant ID;
			String cardAcceptorTerminalId = "";
			String termintalOwnerName = "";
			String terminalCity = "";
			String mcc="";
		    String terminalState = "";
			while (st.hasMoreTokens()){
				if(i==1){
					cardAcceptorId=st.nextToken().toString();
				}else if(i==2){
					cardAcceptorTerminalId=st.nextToken().toString();
				}else if(i==3){
					mcc=st.nextToken().toString();
				}else if(i==4){
					terminalState=st.nextToken().toString();
				}else if(i==5){
					termintalOwnerName=st.nextToken().toString();
				}else if(i==6){
					String lastWord = st.nextToken().toString();
					terminalCity = lastWord.substring(lastWord.lastIndexOf(",")+1);
				}else{
					System.out.println(i+":::"+st.nextToken());
				}
				i++;
			}
			System.out.println("mcc:"+mcc);//merchant id
			isoMsg.set(18,mcc);//Merchantcategory code // from ogs
		    isoMsg.set(19, "356"); //Acquiringinstitutioncountrycode // it will be static     
			isoMsg.set(22, "901"); //POSentrymode  //static
		    isoMsg.set(25, "00"); //POSconditioncode //
		    isoMsg.set(32, "720399"); //Acquiringinstitutioncode //apending 25
		    //isoMsg.set(37,(charYear+doys+hods+resId));
		    System.out.println("model.getRrNo():"+rrNo);
		    isoMsg.set(37, rrNo); //Retrieval reference number
		    isoMsg.set(39,"17");
			isoMsg.set(41, cardAcceptorTerminalId); //Cardacceptor terminalID
	        System.out.println("cardAcceptorId:"+cardAcceptorId);
			isoMsg.set(42,cardAcceptorId);
			//isoMsg.set(43, "PACE                   CHENNAI      TNIN"); //Cardacceptor name /location 40-this is length of this element. no need to pass it in request.
	        String terminalOwnNmn = String.format("%-23s",termintalOwnerName);
			String terminalCty = String.format("%-13s",terminalCity);//yes save it runit ok...
	        String de43 = terminalOwnNmn+terminalCty+"HRIN";
	        System.out.println("void txn DE43::"+de43);
	        isoMsg.set(43,de43);
	        isoMsg.set(49, "356");		//Currencycode, transaction //it will be static till new fields add
	        String de90 = "0200"+resId+de7+"0000072039900000000000";
	        System.out.println("de90:"+de90);
	        isoMsg.set(90,de90);
	        System.out.println("cccccccccccccccccccccccccccccccc");
			logISOMsg(isoMsg);
			byte[] data = isoMsg.pack();
			System.out.println("Request iso msg void txn  : " + new String(data));
			String res = networkTransport(new String(data));
			System.out.println("void txn response: "+res);
			String ress = res.substring(2);
			String bitmapStr1 = ress.substring(4,20);
			System.out.println("Bitmap string:"+bitmapStr1);
			String hex2Binary1 = hexToBin(bitmapStr1);
			String bitmapStr2 ="";
			if(hex2Binary1.startsWith("1")){
				bitmapStr2 = ress.substring(4,36);
				System.out.println("bitmapStr2:"+bitmapStr2);
				bitmapStr2 = hexToBin(bitmapStr2);
			}else{
				bitmapStr2=hex2Binary1;
			}
			System.out.println("BITMAP:"+bitmapStr2);
			try {
				GenericPackager packe = new GenericPackager("void_response.xml");
		    	ISOMsg isoMassage = new ISOMsg();
		    	isoMassage.setPackager(packe);
		    	isoMassage.unpack(ress.getBytes());
		    	// print the DE list
		    	result = logISOMasssgeStr(isoMassage);
		    }catch(Exception e) {
		    	e.printStackTrace();
		    }
			System.out.println("response from void transaction dao");			
			System.out.println("eeeeeeeeeeeeee");
		}catch(java.net.SocketTimeoutException ste){			
			logger.error("error in ogs void transaction "+ste);
			System.out.println("error 1 "+ste);
		}catch(Exception e){			
			logger.error("error in ogs void txn transaction "+e);
			System.out.println("error 2 "+e);
		}finally{
			//session.close();
		}
		//return response;
	
	}
	
		
	private static String getHex2Binary(String string){
		return new BigInteger(string, 16).toString(2);
	}
	private static String hexToBin(String hex){
	     String bin = "";
	     String binFragment = "";
	     int iHex;
	     hex = hex.trim();
	     hex = hex.replaceFirst("0x", "");

	     for(int i = 0; i < hex.length(); i++){
	         iHex = Integer.parseInt(""+hex.charAt(i),16);
	         binFragment = Integer.toBinaryString(iHex);
	         while(binFragment.length() < 4){
	             binFragment = "0" + binFragment;
	         }
	         bin += binFragment;
	     }
	     return bin;
	}
	
	
	
	private static String logISOMasssgeStr(ISOMsg msg) {
		System.out.println("----ISO MESSAGE-----");
		String result = "";
		try {
			System.out.println("  MTI : " + msg.getMTI());
			for (int i=1;i<=msg.getMaxField();i++) {
				if (msg.hasField(i)) {
					System.out.println("    Field-"+i+" : "+msg.getString(i));
					/*if(i==42){
						System.out.println("42length:"+msg.getString(i).length());
					}
					if(i==61){
						System.out.println("61length:"+msg.getString(i).length());
					}*/
					result+="    Field-"+i+" : "+msg.getString(i);
				}
			}
		} catch (ISOException e) {
			e.printStackTrace();
		} finally {
			System.out.println("--------------------");
		}
		return result;
	}
	private static HashMap<String,String> logISOMasssgeMap(ISOMsg msg) {
		System.out.println("----ISO MESSAGE-----");
		HashMap<String,String> isoResult = new HashMap<String,String> ();
		String result = "";
		try {
			System.out.println("  MTI : " + msg.getMTI());
			for (int i=1;i<=msg.getMaxField();i++) {
				if (msg.hasField(i)) {
					System.out.println("    Field-"+i+" : "+msg.getString(i));
					if(i==39){
						isoResult.put("keyExStatus", msg.getString(i));
						//System.out.println("42length:"+msg.getString(i).length());
					}
					if(i==63){
						//System.out.println("61length:"+msg.getString(i).length());
						isoResult.put("merchantData", msg.getString(i));
					}if(i==98){
						//System.out.println("61length:"+msg.getString(i).length());
						isoResult.put("de98", msg.getString(i));
					}
					result+="    Field-"+i+" : "+msg.getString(i);
				}
			}
		} catch (ISOException e) {
			e.printStackTrace();
		} finally {
			System.out.println("--------------------");
		}
		System.out.println("isoResult:"+isoResult);
		return isoResult;
	}
	
	
	 private static void logISOMasssge(ISOMsg msg) {
			System.out.println("----ISO MESSAGE-----");
			try {
				System.out.println("  MTI : " + msg.getMTI());
				for (int i=1;i<=msg.getMaxField();i++) {
					if (msg.hasField(i)) {
						System.out.println("    Field-"+i+" : "+msg.getString(i));
					}
				}
			} catch (ISOException e) {
				e.printStackTrace();
			} finally {
				System.out.println("--------------------");
			}
	 
		}
	
	private static void logISOMsg(ISOMsg msg) {
		System.out.println("----ISO MESSAGE-----");
		try {
			System.out.println("  MTI : " + msg.getMTI());
			for (int i=1;i<=msg.getMaxField();i++) {
				if (msg.hasField(i)){
					System.out.println("    Field-"+i+" : "+msg.getString(i));
				}
			}
		} catch (ISOException e) {
			e.printStackTrace();
		} finally {
			System.out.println("--------------------");
		}
	}
	private static void unpackIsoMsg(ISOMsg msg) {
		System.out.println("----Unpack ISO MESSAGE-----");
		try {
			System.out.println("  MTI : " + msg.getMTI());
			for (int i=1;i<=msg.getMaxField();i++) {
				if (msg.hasField(i)) {
					System.out.println("    Field-"+i+" : "+msg.getString(i));
				}
			}
		} catch (ISOException e) {
			e.printStackTrace();
		} finally {
			System.out.println("--------------------");
		}
 
	}
	public static byte[] hexStringToByte(String hex) { 
		int len = (hex.length() / 2); 
		byte[] result = new byte[len]; 
		char[] achar = hex.toCharArray(); 
		for (int i = 0; i < len; i++) { 
		int pos = i * 2; 
		result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1])); 
		} 
		return result; 
		} 
	private static byte toByte(char c) { 
		byte b = (byte) "0123456789ABCDEF".indexOf(c); 
		return b; 
	}
	public static byte[] str2Bcd(String asc) { 
		int len = asc.length(); 
		int mod = len % 2; 
		if (mod != 0) { 
		asc = "0" + asc; 
		len = asc.length(); 
		} 
		byte abt[] = new byte[len]; 
		if (len >= 2) { 
		len = len / 2; 
		} 
		byte bbt[] = new byte[len]; 
		abt = asc.getBytes(); 
		int j, k; 
		for (int p = 0; p < asc.length() / 2; p++) {
		if ((abt[2 * p] >= '0') && (abt[2 * p] <= '9')) { 
		j = abt[2 * p] - '0'; 
		} else if ((abt[2 * p] >= 'a') && (abt[2 * p] <= 'z')) { 
		j = abt[2 * p] - 'a' + 0x0a; 
		} else { 
		j = abt[2 * p] - 'A' + 0x0a; 
		} 
		if ((abt[2 * p + 1] >= '0') && (abt[2 * p + 1] <= '9')) { 
		k = abt[2 * p + 1] - '0'; 
		} else if ((abt[2 * p + 1] >= 'a') && (abt[2 * p + 1] <= 'z')) { 
		k = abt[2 * p + 1] - 'a' + 0x0a; 
		} else { 
		k = abt[2 * p + 1] - 'A' + 0x0a; 
		} 
		int a = (j << 4) + k; 
		byte b = (byte) a; 
		bbt[p] = b; 
		}
		return bbt; 
	} 
	public static String vm20OgsKeyExNetworkTransport(String isomsg,String uat)
			throws UnknownHostException, IOException{
		String ogsResponse = "";
		try{
			String isoMessage = "00000000000000000000"+isomsg;
			//System.out.println("iso message: "+isoMessage);
			//System.out.println("isoMessage:"+isoMessage.length());
			Integer isoLength = isoMessage.length()+2;
			String strlength = Integer.toHexString(isoLength).toString();
			//System.out.println("Strlength:"+strlength);
			String hexString = "";	
			if(strlength.length()==3){
				hexString = "0"+Integer.toHexString(isoLength);
			}else{
				hexString = "00"+Integer.toHexString(isoLength);
			}
			//System.out.println("Integer.toHexString(isoLength):"+hexString);
			byte[] bcdArray = str2Bcd(hexString);
			//System.out.println("byte arr: "+bcdArray);
			byte[] isoMessageBytes=isoMessage.getBytes();
			byte[] totalMsgBytes=new byte[bcdArray.length+isoMessageBytes.length];
			System.arraycopy(bcdArray, 0, totalMsgBytes, 0, bcdArray.length);
			System.arraycopy(isoMessageBytes, 0, totalMsgBytes, bcdArray.length, isoMessageBytes.length);
			Socket connection = null;
			//System.out.println("uat is:"+uat);
			if(uat!=null&&uat.equals("1")){
				connection = new Socket("115.112.71.226", 9888);
			}else if(uat!=null&&uat.equals("2")){
				connection = new Socket("115.112.71.226", 9887);
			}else if(uat!=null&&uat.equals("3")){
				connection = new Socket("115.112.71.226", 9886);
			}else if(uat!=null&&uat.equals("4")){
				connection = new Socket("115.112.71.226", 9885);
			}else{
				//connection = new Socket("115.112.71.226", 3555);
				connection = new Socket("115.112.71.226", 10236);
			}//uat 4 -> PORT 9885
    		connection.setSoTimeout (30000);
    		BufferedOutputStream bos = new BufferedOutputStream(connection.getOutputStream());
    		//System.out.println("totalMsgBytes:::::::"+totalMsgBytes);
			bos.write(totalMsgBytes);
			bos.flush();
			byte[] arrOutut = new byte[4096];
			//System.out.println("soket connection going connect");
			int count = connection.getInputStream().read(arrOutut, 0, 4096);
			//System.out.println("response from ogs");
			for (int outputCount = 0; outputCount < count; outputCount++){
				char response = (char)arrOutut[outputCount];
				ogsResponse = ogsResponse + response;
			}// in morning also comming same exception i said to vijay he resolved it
			System.out.println("ogs response: "+ogsResponse);
			connection.close();
		}catch(java.net.SocketTimeoutException e){
			System.err.println("error to connect ogs "+e);
			return "error to connect ogs";
		}catch(Exception e){
			System.err.println("error to connect ogs "+e);
			return "error to connect ogs";
		}
		return ogsResponse;
	}
	
	
	
	public static String networkTransport(String isoMessage)
			throws UnknownHostException, IOException{
		String ogsResponse = "";
		try{
			//System.out.println("iso message:"+isoMessage);
			System.out.println("iso message length:"+isoMessage.length());
			//isoMessage="00000000000000000000"+isoMessage;
			Integer isoLength = isoMessage.length()+2;
			String strlength = Integer.toHexString(isoLength).toString();
			System.out.println("Strlength:"+strlength);
			String hexString = "";// wait here// without cp1252 its working...		
			if(strlength.length()==3){
				hexString = "0"+Integer.toHexString(isoLength);
			}else{
				hexString = "00"+Integer.toHexString(isoLength);
			}
			System.out.println("Integer.toHexString(isoLength):"+hexString);
			//byte[] bcdArray = toBcd(hexString);// issue with the length that is sent. we are not able to parse it.
			byte[] bcdArray = str2Bcd(hexString);
			System.out.println("byte arr: "+bcdArray);
			byte[] isoMessageBytes=isoMessage.getBytes();
			byte[] totalMsgBytes=new byte[bcdArray.length+isoMessageBytes.length];
			System.arraycopy(bcdArray, 0, totalMsgBytes, 0, bcdArray.length);
			System.arraycopy(isoMessageBytes, 0, totalMsgBytes, bcdArray.length, isoMessageBytes.length);
			/*for(int i=0;i<totalMsgBytes.length;i++){
				System.out.println(totalMsgBytes[i]);//now try
			}*/
			//String bcdString = new String(bcdArray);
			//String completeIsoMsg = bcdString+isoMessage;
			//System.out.println("completeIsoMsg: "+completeIsoMsg);
			//Socket connection = new Socket("115.112.71.226", 9888);
			Socket connection = new Socket("115.112.71.226", 3555);
    		connection.setSoTimeout (30000);
    		BufferedOutputStream bos = new BufferedOutputStream(connection.getOutputStream());
    		//OutputStreamWriter osw = (new OutputStreamWriter(connection.getOutputStream()));
    		//OutputStreamWriter osw = new OutputStreamWriter(bos);
    		//System.out.println("completeIsoMsg length:"+completeIsoMsg.length());
    		//osw.write(completeIsoMsg);
			bos.write(totalMsgBytes);
			//osw.write(completeIsoMsg.getBytes("UTF-8"));
			//osw.flush();
			bos.flush();
			byte[] arrOutut = new byte[4096];
			System.out.println("soket connection going connect");
			int count = connection.getInputStream().read(arrOutut, 0, 4096);
			System.out.println("response from ogs");
			connection.close();
			for (int outputCount = 0; outputCount < count; outputCount++){
				char response = (char)arrOutut[outputCount];
				ogsResponse = ogsResponse + response;
			}
			// in morning also comming same exception i said to vijay he resolved it
			System.out.println("ogs response: "+ogsResponse);
			//unpackIsoMsg(response);
			//System.out.println("Unpacked iso8583 Message" + response);			
		}catch(java.net.SocketTimeoutException e){
			System.err.println("error to connect ogs "+e);
			return "error to connect ogs";
		}catch(Exception e){
			System.err.println("error to connect ogs "+e);
			return "error to connect ogs";
		}
		return ogsResponse;
	}
	private static byte[] toBcd(String s) {
        int size = s.length();
        byte[] bytes = new byte[(size+1)/2];
        int index = 0;
        boolean advance = size%2 != 0;
        for ( char c : s.toCharArray()) {
            byte b = (byte)( c - '0');
            if( advance ) {
                bytes[index++] |= b;
            }else {
                bytes[index] |= (byte)(b<<4);
            }
            advance = !advance;
        }
        return bytes;
    }	
	
	private static JSONObject keyExResDUKPTISOMasssge(ISOMsg msg){
		System.out.println("----ISO MESSAGE-----");
		JSONObject dukptResJson = new JSONObject();
		try {			
			System.out.println("  MTI : " + msg.getMTI());
			for (int i=1;i<=msg.getMaxField();i++) {
				if (msg.hasField(i)) {
					System.out.println("    Field-"+i+" : "+msg.getString(i));
					if(i==7)
						dukptResJson.put("DE07", msg.getString(i));
					if(i==11)
						dukptResJson.put("DE11", msg.getString(i));
					if(i==37)
						dukptResJson.put("DE37", msg.getString(i));
					if(i==42)
						dukptResJson.put("DE42", msg.getString(i));					
					if(i==39){
						dukptResJson.put("DE39", msg.getString(i));
						dukptResJson.put("responseMessage", OGSCardDesc.getFailReason(msg.getString(i)));
					}
					if(i==63)
						dukptResJson.put("DE63", msg.getString(i));
					if(i==70)
						dukptResJson.put("DE70", msg.getString(i));
					if(i==98)
						dukptResJson.put("DE98", msg.getString(i));
				}
			}
		} catch (ISOException e) {
			e.printStackTrace();
		} finally {
			System.out.println("--------------------");
		}
		return dukptResJson;
	}
	private static JSONObject keyExResDUKPTACKISOMasssge(ISOMsg msg){
		System.out.println("----ISO MESSAGE-----");
		try {
			JSONObject json = new JSONObject();
			System.out.println("  MTI : " + msg.getMTI());
			for (int i=1;i<=msg.getMaxField();i++) {
				if (msg.hasField(i)) {
					System.out.println("    Field-"+i+" : "+msg.getString(i));
					if(i==7)
						json.put("DE07", msg.getString(i));
					if(i==11)
						json.put("DE11", msg.getString(i));
					if(i==37)
						json.put("DE37", msg.getString(i));
					if(i==39){
						json.put("DE39", msg.getString(i));
						json.put("responseMessage", OGSCardDesc.getFailReason(msg.getString(i)));
					}
					if(i==44)
						json.put("DE44", msg.getString(i));					
					if(i==61)
						json.put("DE61", msg.getString(i));
					if(i==70)
						json.put("DE70", msg.getString(i));
				}
			}
		} catch (ISOException e) {
			e.printStackTrace();
		} finally {
			System.out.println("--------------------");
		}
		return json;
	}
	
	public static String getStateCode(String state){
		HashMap<String,String> stateMap = new HashMap<String,String>();
		stateMap.put("Andaman and Nicobar Islands", "AN");
		stateMap.put("Andhra Pradesh","AP");
		stateMap.put("Arunachal Pradesh","AR");
		stateMap.put("Assam","AS");
		stateMap.put("Bihar","BR");
		stateMap.put("Chandigarh","CH");
		stateMap.put("Chhattisgarh","CT");
		stateMap.put("Dadra and Nagar Haveli","DN");
		stateMap.put("Daman and Diu","DD");
		stateMap.put("Delhi","DL");
		stateMap.put("Goa","GA");
		stateMap.put("Gujarat","GJ");
		stateMap.put("Haryana","HR");
		stateMap.put("Himachal Pradesh","HP");
		stateMap.put("Jammu and Kashmir","JK");
		stateMap.put("Jharkhand","JH");
		stateMap.put("Karnataka","KA");
		stateMap.put("Kerala","KL");
		stateMap.put("Lakshadweep","LD");
		stateMap.put("Madhya Pradesh","MP");
		stateMap.put("Maharashtra","MH");
		stateMap.put("Manipur","MN");
		stateMap.put("Meghalaya","ML");
		stateMap.put("Mizoram","MZ");
		stateMap.put("Nagaland","NL");
		stateMap.put("Odisha","OR");
		stateMap.put("Puducherry","PY");
		stateMap.put("Punjab","PB");
		stateMap.put("Rajasthan","RJ");
		stateMap.put("Sikkim","SK");
		stateMap.put("Tamil Nadu","TN");
		stateMap.put("Telangana","TG");
		stateMap.put("Tripura","AS");
		stateMap.put("Uttar Pradesh","UP");
		stateMap.put("Uttarakhand","UT");
		stateMap.put("West Bengal","WB");		
		return stateMap.get(state);
	}
	

	
}
/*DbDto<Object> response = new DbDto<Object>();
HashMap<String, String> responseMap = new HashMap<String, String>();
Session session = null;
Transaction tx = null;
try{
	session = getNewSession();
	AcqUserEntity userEnt = (AcqUserEntity) session.get(
			AcqUserEntity.class, Long.valueOf(model.getSessionId()));
	if(userEnt==null||userEnt+""==""){
		logger.info("User not found");
		response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
		response.setMessage("User not found for card transaction");
		response.setResult(responseMap);
		return response;
	}
	AcqOrganization org = (AcqOrganization)session.get(AcqOrganization.class, userEnt.getOrgId());
	AcqSettingEntity settingEnt = (AcqSettingEntity) session
			.createCriteria(AcqSettingEntity.class)
			.add(Restrictions.eq("acquirerCode", userEnt.getAcquirerCode())).uniqueResult();
	AcqMapDeviceUserEntity deviceEnt = (AcqMapDeviceUserEntity) session.createCriteria(AcqMapDeviceUserEntity.class).add(Restrictions.eq("userId",Long.valueOf(model.getSessionId()))).add(Restrictions.or(Restrictions.or(Property.forName("deviceType").eq("credit"),Property.forName("deviceType").eq("wallet/credit")))).uniqueResult();
	if (deviceEnt == null || deviceEnt + "" == "") {
		response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
		response.setMessage("User details not found for card transction");
		response.setResult(responseMap);
		return response;
	}else{
		if(!model.getDeviceId().equalsIgnoreCase(deviceEnt.getSerialNo())){
			logger.info("Transaction heppening from different mapping device");
			response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
			response.setMessage("Mismatch Device Id");
			response.setResult(responseMap);
			return response;
		}
		try {
			AcqPlaceTransactionEntity txnEnt = new AcqPlaceTransactionEntity();
			txnEnt.setUserid(Integer.valueOf(""+userEnt.getUserId()));	
			txnEnt.setOrgId(Integer.valueOf(""+userEnt.getOrgId()));
			txnEnt.setMerchantId(Integer.valueOf(""+org.getMerchantId()));
			txnEnt.setAmount(model.getAmount());
			txnEnt.setMobile("0000");
			if(model.getCashBackAmount()!=null&&model.getTxnType().equalsIgnoreCase("CASHBACK")){
				txnEnt.setCashBackAmt(model.getCashBackAmount());
			}else {
				txnEnt.setCashBackAmt("0.0");
			}
			txnEnt.setTxnType(model.getTxnType());
			//txnEnt.setCardExpDate(model.getCardExpDate());
			if(model.getTxnType().equalsIgnoreCase("CASHATPOS")){
				txnEnt.setBankMdr(settingEnt.getMdrCashAtPos());
				txnEnt.setEzMdr(deviceEnt.getMdrCashAtPos());
				txnEnt.setServiceTax(settingEnt.getServiceTaxCashAtPos());
			}else{
				txnEnt.setEzMdr("0.0");
				txnEnt.setBankMdr("0.0");
				txnEnt.setServiceTax(settingEnt.getServiceTax());
			}
			Date date= new Date();
			Timestamp currentTimestamp= new Timestamp(date.getTime());
			txnEnt.setOtpDateTime(currentTimestamp);
			txnEnt.setDateTime(currentTimestamp);
			txnEnt.setStatus(503);
			txnEnt.setCarPanNo(model.getMaskPan());
			txnEnt.setCustomerId("0");
			txnEnt.setPayoutStatus(700);
			txnEnt.setPayoutDateTime(currentTimestamp);
			txnEnt.setAcquirerCode(userEnt.getAcquirerCode());
			txnEnt.setAcquirerMdr("0.0");
			txnEnt.setSwitchLab("OGS");
			txnEnt.setDescription("ogs transaction initiated");
			txnEnt.setAcqPayoutStatus(800);
			txnEnt.setAcqPayoutDateTime(currentTimestamp);
			txnEnt.setAppCertificate("NA");
			txnEnt.setAid("NA");
			txnEnt.setSystemUtilityFee("0.0");
			txnEnt.setScriptResult("NA");
			tx = session.beginTransaction();
			session.save(txnEnt);
			AcqCardDetails cardEntity = new AcqCardDetails();
			cardEntity.setTransactionId(txnEnt.getId());
			cardEntity.setTerminalId(deviceEnt.getBankTid());
			cardEntity.setCardHolderName("NA");
			cardEntity.setCardType(model.getCardType());
			cardEntity.setIpAddress(model.getIpAddress());
			cardEntity.setImeiNo(model.getImeiNo());
			cardEntity.setLatitude(model.getLatitude());
			cardEntity.setLongitude(model.getLongitude());
			cardEntity.setStan("NA");//CustomerMobile(model.get)
			cardEntity.setCardType("NA");
			session.save(cardEntity);
			tx.commit();
			logger.info("card transaction is initiated");
			String resTxnId = "";
			String txnId = ""+txnEnt.getId();
			responseMap.put("txnId", txnId);
			if(txnId.length()>6){
				resTxnId = txnId.substring(txnId.length()-6);
			}else{
				resTxnId = txnId;
			}
			responseMap.put("stan", String.format("%06d",Integer.valueOf(resTxnId)));
			response.setStatus(EnumStatusConstant.OK.getId());
			response.setMessage(EnumStatusConstant.OK.getDescription());
			response.setResult(responseMap);
		} catch (Exception e) {
			response.setStatus(EnumStatusConstant.RollBackError.getId());
			response.setMessage(EnumStatusConstant.RollBackError.getDescription());
			logger.error("error in insert ogs txn init dao "+e);
		}			
	}
}catch(Exception e){
	System.err.println("error to connect ogs "+e);
	response.setMessage(EnumStatusConstant.RollBackError.getDescription());
	response.setStatus(100);
	return response; 
}finally{
	session.close();
}
return response;*/

