package com.acq.web.dao.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import com.acq.web.dao.AcqDao;
import com.acq.EnumStatusConstant;
import com.acq.EnumWalletConstant;
import com.acq.NumberValidator;
import com.acq.OtpGenerator;
import com.acq.users.entity.AcqCardDetails;
import com.acq.users.entity.AcqContactEntity;
import com.acq.users.entity.AcqDevice;
import com.acq.users.entity.AcqDeviceDetails;
import com.acq.users.entity.AcqLoyaltyCashbackEntity;
import com.acq.users.entity.AcqLoyaltyEntity;
import com.acq.users.entity.AcqLoyaltyPointsEntity;
import com.acq.users.entity.AcqMapDeviceUserEntity;
import com.acq.users.entity.AcqMerchantEntity;
import com.acq.users.entity.AcqPlaceTransactionEntity;
import com.acq.users.entity.AcqOrganization;
import com.acq.users.entity.AcqRiskManagement;
import com.acq.users.entity.AcqSettingEntity;
import com.acq.users.entity.AcqSignEntity;
import com.acq.users.entity.AcqTransactionDetailEntity;
import com.acq.users.entity.AcqTransactionEntity;
import com.acq.users.entity.AcqTransactionMdrDetailsEntity;
import com.acq.users.entity.AcqTxnEntity;
import com.acq.users.entity.AcqUserDetailsEntity;
import com.acq.users.entity.AcqUserEntity;
import com.acq.users.entity.AcqWalletQrCodeEntity;
import com.acq.users.entity.LoginAcqWalletEntity;
import com.acq.web.controller.model.AcqPostTransactionModel;
import com.acq.web.controller.model.AcqSearchTransactionModel;
import com.acq.web.controller.model.AcqSendSmsModel;
import com.acq.web.controller.model.AcqTransactionDetailModel;
import com.acq.web.controller.model.AcqTransactionModel;
import com.acq.web.controller.model.AcqTransactionSignModel;
import com.acq.web.controller.model.AcqVoidTransactionModel;
import com.acq.web.controller.model.LastTxnDetailsModel;
import com.acq.web.dao.TransactionDaoInf;
import com.acq.web.dto.impl.DbDto;

@Repository
public class TransactionDao extends AcqDao implements TransactionDaoInf {

	final static Logger logger = Logger.getLogger(TransactionDao.class);

	
	
	@Value("#{acqConfig['ags_hostname.location']}")
	private String agsGetSessionKeyHost;
	public String getAgsGetSessionKeyHost() {
		return agsGetSessionKeyHost;
	}
	
	@Value("#{acqConfig['loggers.location']}")
	private String loginLocation;

	public String getLoginLocation() {
		return loginLocation;
		
	}
	
	
	@Override
	public DbDto<HashMap<String,String>> sendCustomerSms(AcqSendSmsModel model) {
		DbDto<HashMap<String, String> > response = new DbDto<HashMap<String, String>>();
		HashMap<String, String> resultMap = new HashMap<String, String>();
		Session session = null;
		try{
			session = getNewSession();
			//System.out.println(model.getSessoinId()+"::::::"+model.getTxnId());
			AcqPlaceTransactionEntity entity = (AcqPlaceTransactionEntity)session.createCriteria(AcqPlaceTransactionEntity.class).add(Restrictions.eq("userid",Integer.valueOf(model.getSessionId()))).add(Restrictions.eq("status",504)).add(Restrictions.eq("id",Integer.valueOf(model.getTxnId()))).uniqueResult();
			if(entity==null||entity+""==""){
				response.setStatus(EnumStatusConstant.TransactionNotFound.getId());
				response.setMessage(EnumStatusConstant.TransactionNotFound.getDescription());
				response.setResult(resultMap);
				return response;
			}
			resultMap.put("txnId", ""+entity.getId());
			response.setStatus(EnumStatusConstant.OK.getId());
			response.setMessage(EnumStatusConstant.OK.getDescription());
			response.setResult(resultMap);
			System.out.println("entity select from dao");
		}catch(Exception e){
			System.out.println("error in send reversal sms dao "+e);
			response.setStatus(EnumStatusConstant.RollBackError.getId());
			response.setMessage(EnumStatusConstant.RollBackError.getDescription());
			response.setResult(resultMap);
		}finally{
			session.close();
		}
		return response;		
	}
	
	@Override
	public DbDto<HashMap<String, HashMap<String, String>>> lastTxnDetails(
			LastTxnDetailsModel model, String clientIpAddress){		
		DbDto<HashMap<String, HashMap<String, String>>> response = new DbDto<HashMap<String, HashMap<String, String>>>();
		try{
			if(model.getTxnMessage().startsWith("timeout")){
				logger.info("last txn status is time out");
				//String result = getLstTxnDetails(agsGetSessionKeyUrl,agsGetSessionKeyHost,"banktid");				
			}			
		}catch(Exception e){
			
		}
		return response;
	}
	
	public static String getLstTxnDetails(String agsUrl,final String agsHostName,String terminalId){
		try {
			System.out.println("agsUrl::"+agsUrl);
			System.out.println("agsHostName::"+agsHostName);
			System.out.println("terminalId::"+terminalId);
			String request = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"+
			"<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">"+
			  "<soap12:Body>"+
			    "<getLastTxnDetails xmlns=\"http://tempuri.org/\">"+
			      "<terminal_id>"+terminalId+"</terminal_id>"+
			      "<platform_type>Acquiro_</platform_type>"+
			      "<app_ver>1.3.1</app_ver>"+
			    "</getLastTxnDetails>"+
			  "</soap12:Body>"+
			"</soap12:Envelope>";
			
			
			//System.out.println("requestrequest:"+request);
			javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(
					new javax.net.ssl.HostnameVerifier(){
					    public boolean verify(String hostname,
						        javax.net.ssl.SSLSession sslSession) {
						        //return hostname.equals("221.135.139.43");
					    	return hostname.equals(agsHostName);
						    }
						});
			String outputStr = null;
			URL url = new URL(agsUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "text/xml");		
			OutputStream os = conn.getOutputStream();
			os.write(request.getBytes());
			os.flush();
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			String output;			
			while ((output = br.readLine()) != null) {
				outputStr = output;
			}
			//System.out.println("ags Response: "+outputStr);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new InputSource(new StringReader(outputStr)));
			Element rootElement = document.getDocumentElement();
			logger.info("merchant key is:"+rootElement.getTextContent());	
			return rootElement.getTextContent();
		} catch (java.net.ConnectException e) {
			e.printStackTrace();
			return "Bank server not responding";
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}	
	}
	
	@Override
	public DbDto<HashMap<String, HashMap<String, String>>> placePostTransaction(
			AcqPostTransactionModel model, String clientIpAddress){
		DbDto<HashMap<String, HashMap<String, String>>> response = new DbDto<HashMap<String, HashMap<String, String>>>();
		
		HashMap<String, HashMap<String, String>> responseMap = new HashMap<String, HashMap<String, String>>();
		StringBuffer logReport = new StringBuffer();
		String status = "";
		logReport.append(",Info: Post Transaction dao started");
		int txnId = 0;
		String amount = null;
		String cardPanNo = null;
		String dateTime = null;
		String terminalId = null;
		String txnStatus = "0";
		Session session = null;
		
		Transaction tx = null;
		Pattern pt = Pattern.compile("[^a-zA-Z0-9]");
      
		try {
			session = getNewSession();
			logReport.append(",Info: hibernate session object created");
			try {
				String rrNo = null;
				if(model.getRrNo()!=null&&model.getRrNo()!=""){
					rrNo = (String)session.createCriteria(AcqCardDetails.class).setProjection(Projections.property("rrNo")).add(Restrictions.eq("rrNo",model.getRrNo())).uniqueResult();
				}if(rrNo!=null&&rrNo!=""&&rrNo.equals(model.getRrNo())){
					response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
					response.setMessage("RR No already in use");
					logger.warn("RR No already in use");
					status = "FAILED";
					logReport.append(",Warning: RR no already in use");
				}else{
					AcqPlaceTransactionEntity entity = (AcqPlaceTransactionEntity)session.createCriteria(AcqPlaceTransactionEntity.class).add(Restrictions.eq("userid",Integer.valueOf(model.getSessionId()))).add(Restrictions.eq("id",Integer.valueOf(model.getTransactionId()))).uniqueResult();
					//System.out.println("222222222222222222222");
					if(entity==null||entity+""==""){
						response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
						response.setMessage("Transaction not found");
						response.setResult(responseMap);
						return response;
					}if(entity.getStatus()==505){
						response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
						response.setMessage("Transaction Already Processed");
						response.setResult(responseMap);
						return response;
					}
					logReport.append(",Info: transaction fetched successfully");
					//AcqTransactionEntity txnEnt = new AcqTransactionEntity();
					logReport.append(",Info: Saving txn data in db");
					//model.setActualAmount(entity.getAmount());
					if(Integer.valueOf(model.getStatusCode())==0){
						entity.setStatus(505);
					}else if(Integer.valueOf(model.getStatusCode())==1){
						entity.setStatus(504);
					}else {
						entity.setStatus(503);
					}if(model.getStatusDescription()!=null&&model.getStatusDescription()!=""){
						entity.setDescription(model.getStatusDescription());
					}if(model.getCardPanNo()!=null&&model.getCardPanNo()!=""){
						entity.setCarPanNo(model.getCardPanNo());
					}
					session.update(entity);
					AcqCardDetails cardEntity = (AcqCardDetails)session.createCriteria(AcqCardDetails.class).add(Restrictions.eq("transactionId",Integer.valueOf(model.getTransactionId()))).uniqueResult();
					
					if(entity==null||entity+""==""){
						response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
						response.setMessage("Transaction not found");
						response.setResult(responseMap);
						return response;
					}
					terminalId = cardEntity.getTerminalId();
					if(model.getRrNo()!=null&&model.getRrNo()!="")
						cardEntity.setRrNo(model.getRrNo());
					
					if(model.getAuthCode()!=null&&model.getAuthCode()!="")
						cardEntity.setAuthCode(model.getAuthCode());
					
					
					if(model.getBatchNo()!=null&&model.getBatchNo()!="")
						cardEntity.setBatchNo(model.getBatchNo());
					
					if(model.getStan()!=null&&model.getStan()!="")
						cardEntity.setStan(model.getStan());
					
					if(model.getCardHolderName()!=null&&model.getCardHolderName()!="")
						cardEntity.setCardHolderName(model.getCardHolderName());
					
					if(model.getCardType()!=null&&model.getCardType()!="")
						cardEntity.setCardType(model.getCardType());
					
					tx = session.beginTransaction();
					session.update(cardEntity);
					if(model.getWalletId()!=null&&model.getWalletId()!=""&&NumberValidator.isPhoneNo(model.getWalletId())){
						logger.info("card txn done");
						try{
							LoginAcqWalletEntity walletEntity = (LoginAcqWalletEntity)session.createCriteria(LoginAcqWalletEntity.class).add(Restrictions.eq("merchantId",""+entity.getMerchantId())).add(Restrictions.eq("userId",model.getSessionId())).add(Restrictions.eq("walletId",model.getWalletId())).uniqueResult();
							if(walletEntity==null||walletEntity+""==""){
								logger.warn("wallet not found for mobi");
							}else{
								//System.out.println("merchant id: "+txnEnt.getMerchantId()+":userid:"+txnEnt.getUserid()+":walletId:"+model.getWalletId());
								logger.info("acq-wallet found");
								AcqLoyaltyEntity loyaltyEnt = (AcqLoyaltyEntity)session.createCriteria(AcqLoyaltyEntity.class).add(Restrictions.eq("mid",""+entity.getMerchantId())).uniqueResult();
								if(loyaltyEnt==null||loyaltyEnt+""==""){
									logger.warn("loyalty not found for given merchant");
								}else{
									logger.info("acq-loyalty found card");
									if(loyaltyEnt.getLoyaltyType().equalsIgnoreCase("Points")){
										logger.info("acq-loyalty type point found phone");
										BigDecimal totAmount = new BigDecimal("0.0");
										BigDecimal totPoints = new BigDecimal("0.0");
										AcqLoyaltyPointsEntity pointEnt = (AcqLoyaltyPointsEntity)session.createCriteria(AcqLoyaltyPointsEntity.class).add(Restrictions.eq("loyaltyId",loyaltyEnt.getId())).uniqueResult();
										Double point = Math.floor(Double.valueOf(entity.getAmount())/Double.valueOf(pointEnt.getAmount()));
										if(Double.valueOf(entity.getAmount())>Double.valueOf(pointEnt.getMinAmountForPoints())){
											totPoints = new BigDecimal(""+point);
										}
										totPoints.add(walletEntity.getTotalPoints());
										if(model.getPointUsed()!=null&&model.getPointUsed()!=""){
											logger.info("point used found");											
											if(new BigDecimal(model.getPointUsed()).compareTo(walletEntity.getTotalPoints().add(new BigDecimal(""+point)))<=0){											
												Double usedPoints = Double.parseDouble(model.getPointUsed());		
												totPoints = new BigDecimal(""+point).add(walletEntity.getTotalPoints().subtract(BigDecimal.valueOf(usedPoints)));
												entity.setUsedPoints(Integer.parseInt(model.getPointUsed()));
												entity.setLoyaltyId(Integer.parseInt(loyaltyEnt.getId()));
											    walletEntity.setTotalPoints(totPoints);
											}
											//totAmount = walletEntity.getTotalAmtSpends().add(new BigDecimal(entity.getAmount()));
											logger.info("acq-loyalty point putted");
										}else{											
											logger.info("loyalty points not updated,amount is less then min amount");
										}
										totAmount = walletEntity.getTotalAmtSpends().add(new BigDecimal(entity.getAmount()));
										session.update(entity);										
										walletEntity.setTotalAmtSpends(totAmount);
										walletEntity.setTotalTxns(""+(Double.valueOf(walletEntity.getTotalTxns())+1));
										session.update(walletEntity);
										logger.info("loyalty point updated card");	
									}if(loyaltyEnt.getLoyaltyType().equalsIgnoreCase("Cashback")){
										logger.info("acq-loyalty type cashback found");									
										AcqLoyaltyCashbackEntity cashBackEntity = (AcqLoyaltyCashbackEntity)session.createCriteria(AcqLoyaltyCashbackEntity.class).add(Restrictions.eq("loyaltyId",loyaltyEnt.getId())).uniqueResult();
										//System.out.println("cashBackEntity.getMinAmountCashBack():"+cashBackEntity.getMinAmountCashBack());
										//System.out.println("entity.getAmount():"+entity.getAmount());
										if(Double.valueOf(entity.getAmount())<Double.valueOf(cashBackEntity.getMinAmountCashBack())){
											logger.info("loyalty cashback not updated,amount is less then min cash back amount in card");
										}else{
											logger.info("min amount is less then transaction amount");
											Double cashBack = 0.0;
											if(cashBackEntity.getType().equalsIgnoreCase("amount")){		
												cashBack = Double.valueOf(cashBackEntity.getCashBackAmount());
											}if(cashBackEntity.getType().equalsIgnoreCase("percentage")){	
												cashBack = (Double.valueOf(entity.getAmount())*Double.valueOf(cashBackEntity.getCashBackAmount()))/100;
											}
											walletEntity.setAmount(""+new BigDecimal(walletEntity.getAmount()).add(new BigDecimal(cashBack)));
											walletEntity.setTotalCashBack(walletEntity.getTotalCashBack().add(new BigDecimal(cashBack)));
											walletEntity.setTotalAmtSpends(walletEntity.getTotalAmtSpends().add(new BigDecimal(entity.getAmount())));
											walletEntity.setTotalTxns(""+(Double.valueOf(walletEntity.getTotalTxns())+1));
											session.update(walletEntity);
											logger.info("acq-loyalty cashback putted");
										}
									}
								}
							}
						}catch(Exception e){
							logger.error("error to update loyalty details " +e);
						} 
					}else{
						try{
							AcqWalletQrCodeEntity qrCodeEntity = (AcqWalletQrCodeEntity)session.createCriteria(AcqWalletQrCodeEntity.class).add(Restrictions.eq("mid",""+entity.getMerchantId())).add(Restrictions.eq("status","1")).add(Restrictions.eq("qrCode",model.getWalletId())).uniqueResult();
					        if(qrCodeEntity==null|| qrCodeEntity+""==""){
					         logger.warn("Card not found for given wallet");
					        }else{
					        	LoginAcqWalletEntity  walletEntity = (LoginAcqWalletEntity)session.createCriteria(LoginAcqWalletEntity.class).add(Restrictions.eq("merchantId",""+entity.getMerchantId())).add(Restrictions.eq("userId",model.getSessionId())).add(Restrictions.eq("walletId", qrCodeEntity.getMobileNo())).uniqueResult();
					        	logger.info("acq-wallet qr code found");
					        	AcqLoyaltyEntity loyaltyEnt = (AcqLoyaltyEntity)session.createCriteria(AcqLoyaltyEntity.class).add(Restrictions.eq("mid",""+entity.getMerchantId())).uniqueResult();
					        	if(loyaltyEnt==null|| loyaltyEnt+""==""){
					        		logger.warn("loyalty qr code not found for given merchant");
					        	}else{
					        		logger.info("acq-loyalty qr code found");
					        		if(loyaltyEnt.getLoyaltyType().equalsIgnoreCase("Points")){
					        			logger.info("acq-loyalty type point found card qrcode");
										BigDecimal totAmount = new BigDecimal("0.0");
										BigDecimal totPoints = new BigDecimal("0.0");
										AcqLoyaltyPointsEntity pointEnt = (AcqLoyaltyPointsEntity)session.createCriteria(AcqLoyaltyPointsEntity.class).add(Restrictions.eq("loyaltyId",loyaltyEnt.getId())).uniqueResult();
										Double point = Math.floor(Double.valueOf(entity.getAmount())/Double.valueOf(pointEnt.getAmount()));
										if(Double.valueOf(entity.getAmount())>Double.valueOf(pointEnt.getMinAmountForPoints())){
											totPoints = new BigDecimal(""+point);
										}
										totPoints.add(walletEntity.getTotalPoints());
										if(model.getPointUsed()!=null&&model.getPointUsed()!=""){
											logger.info("point used found");											
											if(new BigDecimal(model.getPointUsed()).compareTo(walletEntity.getTotalPoints().add(new BigDecimal(""+point)))<=0){											
												Double usedPoints = Double.parseDouble(model.getPointUsed());		
												totPoints = new BigDecimal(""+point).add(walletEntity.getTotalPoints().subtract(BigDecimal.valueOf(usedPoints)));
												entity.setUsedPoints(Integer.parseInt(model.getPointUsed()));
												entity.setLoyaltyId(Integer.parseInt(loyaltyEnt.getId()));
											    walletEntity.setTotalPoints(totPoints);
											}
											//totAmount = walletEntity.getTotalAmtSpends().add(new BigDecimal(entity.getAmount()));
											logger.info("acq-loyalty point putted");
										}else{											
											logger.info("loyalty points not updated,amount is less then min amount");
										}
										totAmount = walletEntity.getTotalAmtSpends().add(new BigDecimal(entity.getAmount()));
										session.update(entity);										
										walletEntity.setTotalAmtSpends(totAmount);
										walletEntity.setTotalTxns(""+(Double.valueOf(walletEntity.getTotalTxns())+1));
										session.update(walletEntity);
										logger.info("loyalty point updated card qrcode");
					        		}if(loyaltyEnt.getLoyaltyType().equalsIgnoreCase("Cashback")){
					        			logger.info("acq-loyalty type cashback found");
					        			AcqLoyaltyCashbackEntity cashBackEntity = (AcqLoyaltyCashbackEntity)session.createCriteria(AcqLoyaltyCashbackEntity.class).add(Restrictions.eq("loyaltyId",loyaltyEnt.getId())).uniqueResult();
					        			if(Double.valueOf(entity.getAmount())<Double.valueOf(cashBackEntity.getMinAmountCashBack())){
											logger.info("loyalty cashback not updated,amount is less then min cash back amount in card");
										}else{			        			
						        			Double cashBack = 0.0;
						        			if(cashBackEntity.getType().equalsIgnoreCase("amount")){          
						        				cashBack = Double.valueOf(cashBackEntity.getCashBackAmount());
						        			}if(cashBackEntity.getType().equalsIgnoreCase("percentage")){   
						        				cashBack = (Double.valueOf(entity.getAmount())*Double.valueOf(cashBackEntity.getCashBackAmount()))/100;
						        			}
						        			walletEntity.setAmount(""+new BigDecimal(walletEntity.getAmount()).add(new BigDecimal(cashBack)));
						        			walletEntity.setTotalCashBack(walletEntity.getTotalCashBack().add(new BigDecimal(cashBack)));
						        			walletEntity.setTotalAmtSpends(walletEntity.getTotalAmtSpends().add(new BigDecimal(entity.getAmount())));
						        			walletEntity.setTotalTxns(""+(Double.valueOf(walletEntity.getTotalTxns())+1));
						        			session.update(walletEntity);
						        			logger.info("acq-loyalty cashback putted");
										}
					        		}
					        	}
					        }
						}catch(Exception e){
							logger.error("error to update qr loyalty details " +e);
						}
					}
					// loyalty implementation end				
					HashMap<String, String> map = new HashMap<String,String>();
					AcqDeviceDetails  deviceCriteria = (AcqDeviceDetails)session.createCriteria(AcqDeviceDetails.class).add(Restrictions.eq("userId",model.getSessionId())).uniqueResult();
					if(deviceCriteria==null||deviceCriteria+""==""){
						logger.info("integrationkey /member id not found");
						map.put("memberId", "NA");
						map.put("terminalId", "NA");
					}else{
						map.put("memberId", deviceCriteria.getMemberId());
						map.put("terminalId", deviceCriteria.getTerminalId());
					}
					map.put("merchantId", ""+entity.getMerchantId());
					map.put("TxnId", entity.getId() + "");
					map.put("txn_Amt", entity.getAmount());
					map.put("rrNo", model.getRrNo());
					map.put("txnType", entity.getTxnType());					
					if(model.getRrNo()!=null&&Integer.valueOf(model.getStatusCode())==0){
						map.put("txnStatusCode", EnumWalletConstant.AmountDebited.getId());
						map.put("txnStatusDescription", EnumWalletConstant.AmountDebited.getDescription());
						txnId=entity.getId();
						amount=entity.getAmount();
						cardPanNo=entity.getCarPanNo();
						dateTime = ""+entity.getOtpDateTime();
						txnStatus = "505";
					}else{
						map.put("txnStatusCode", EnumWalletConstant.FailedCardTransaction.getId());
						map.put("txnStatusDescription", EnumWalletConstant.FailedCardTransaction.getDescription());
					}
					if(entity.getStatus()==505){
						map.put("status", "Success");
					}else{
						map.put("status", "Failed");
					}				
					map.put("description", entity.getDescription());
					try {
						String cardHolderName = model.getCardHolderName();
						if(cardHolderName!=null&&cardHolderName.length()>30){
							cardHolderName.substring(0,30);
						}
						//logReport.append("card holder name is "+cardHolderName);
						if (model.getCardPanNo() != null&&model.getCardPanNo()!= "") {	
							Matcher match= pt.matcher(cardHolderName);
							while(match.find()){
								String s= match.group();
								cardHolderName=cardHolderName.replaceAll("\\"+s, "");
						    }
							//logReport.append("111111111:"+model.getCardPanNo()+cardHolderName);
							AcqContactEntity ctEnt = (AcqContactEntity) session.createCriteria(AcqContactEntity.class).add(Restrictions.eq("cardPanNo",(model.getCardPanNo()+cardHolderName))).uniqueResult();
						if(ctEnt!=null&&ctEnt+""!=""){
							map.put("customerPhone",ctEnt.getCustPhone());
						}
						logReport.append(",Info: Customer Data load from customer table");
						}
					} catch (Exception e) {
						logReport.append(",Error: in select customer Entity "+ e);
					}
					tx.commit();
					responseMap.put("txnResponse", map);
					response.setStatus(EnumStatusConstant.OK.getId());
					response.setMessage(EnumStatusConstant.OK.getDescription());
					response.setResult(responseMap);
					status = "SUCCESSFULL";
					logReport.append(",Info: Post Transaction successfully placed");	
					try{
						//if(Double.valueOf(amount)>500){
							if(Double.valueOf(amount)>500&&Integer.valueOf(model.getStatusCode())==0){
								RiskManagement r = new RiskManagement();
								r.setSession(getNewSession());
								r.setTxnId(txnId);
								r.setAmount(amount);
								r.setCarPanNo(cardPanNo);
								r.setOtpDateTime(dateTime);
								r.setTerminalId(terminalId);
								r.setUserId(model.getSessionId());
								r.setStatus(txnStatus);
								Thread th = new Thread(r);					 
								th.start();
						}	//}
					}catch(Exception e){
						logger.error("error to start risk thread");
					}
				}
			} catch (Exception e) {
				response.setStatus(EnumStatusConstant.RollBackError.getId());
				response.setMessage(EnumStatusConstant.RollBackError.getDescription());
				status = "FAILED";
				logReport.append(",Error: To place transaction " + e);
				tx.rollback();
			}
		} catch (Exception e) {
			response.setStatus(EnumStatusConstant.RollBackError.getId());
			response.setMessage(EnumStatusConstant.RollBackError.getDescription());
			logReport.append(",Error: To select transaction " + e);
			status = "FAILED";	
			tx.rollback();
		} finally {
			session.close();	
			if(session.isOpen()==true||session.isConnected()==true){
				session.close();
			}
			
		}
		return response;
	}
	
	
		@Override
		public DbDto<List<HashMap<String, String>>> checkBalance(String walletId) {
			logger.info("Request landing in check balance Dao:"+walletId);
			DbDto<List<HashMap<String, String>>> response = new DbDto<List<HashMap<String, String>>>();
			List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
			Session session = null;
			try {
				session = getNewSession();			
			    	List<LoginAcqWalletEntity> ent = (List<LoginAcqWalletEntity>)session.createCriteria(LoginAcqWalletEntity.class).add(Restrictions.eq("walletId",walletId)).list();
			    	if(ent==null||ent+""==""){
			    		response.setStatus(EnumStatusConstant.NotFound.getId());
						response.setMessage(EnumStatusConstant.NotFound.getDescription());
			    	}else{			    		
			    		Iterator<LoginAcqWalletEntity> itr1 = ent.iterator();
			    		Set<String> merchantSet = new HashSet<String>();
			    		HashMap<String,String> merchantMap = null;
			    		try{
			    			Iterator<LoginAcqWalletEntity> itr2 = ent.iterator();
			    			
							while(itr2.hasNext()){
								LoginAcqWalletEntity txEntity = (LoginAcqWalletEntity)itr2.next();
								merchantSet.add(txEntity.getMerchantId());
							}
							//logger.info("set::"+merchantSet);
						}catch(Exception e){
							logger.error("error iterate merchant id in set", e);
						}
			    		try{
							Criteria merchant =session.createCriteria(AcqMerchantEntity.class);
						    ProjectionList proList = Projections.projectionList();
						    proList.add(Projections.property("merchantId"));
						    proList.add(Projections.property("merchantName"));
						    merchant.setProjection(proList).add((Restrictions.in("merchantId", merchantSet)));
						    List list1 = merchant.list();
						    merchantMap= new HashMap<String,String>();
						    for(Object obj:list1){
						        Object[] ob=(Object[])obj;
						        merchantMap.put("merchantName"+ob[0],""+ob[1]);					      
						    }
						}catch(Exception e){
							logger.error("error to select merchant "+e);
						}
			    	
			    		while(itr1.hasNext()){
			    			LoginAcqWalletEntity entit = (LoginAcqWalletEntity)itr1.next();
			    			HashMap<String, String> map = new HashMap<String, String>();
			    			map.put("merchantName", ""+merchantMap.get("merchantName"+entit.getMerchantId()));
			    		    map.put("balance", entit.getAmount());
			    			map.put("merchantId", entit.getMerchantId());
			    			 map.put("wid", entit.getId());
			    			logger.info("Map::"+map);
			    			list.add(map);							
			    	}
			    	response.setStatus(EnumStatusConstant.OK.getId());
					response.setMessage(EnumStatusConstant.OK.getDescription());
					response.setResult(list);
			    	}			    	
			    }			
			catch (Exception e) {
				response.setStatus(EnumStatusConstant.RollBackError.getId());
				response.setMessage(EnumStatusConstant.RollBackError.getDescription());
				
				logger.error("Error in login wallet step1"+e);
			}finally{
				session.close();
			}
			return response;
		}

		
		
		
		
		
		
		
		
		@Override
		public DbDto<HashMap<String, String>> getCardTxnDetails(String signImage){
			DbDto<HashMap<String, String>> response = new DbDto<HashMap<String, String>>();
			Session session = null;
			HashMap<String, String> map = new HashMap<String, String>();
			String switchLab="";
			try{
				session = getNewSession();
				Criteria criteria = (Criteria) session.createCriteria(AcqPlaceTransactionEntity.class);
				/*ProjectionList projList = Projections.projectionList();
				projList.add(Projections.countDistinct("id"));
				projList.add(Projections.property("userid"));
				projList.add(Projections.property("orgId"));
				projList.add(Projections.property("merchantId"));
				projList.add(Projections.property("otpDateTime"));
				projList.add(Projections.property("amount"));	
				projList.add(Projections.property("acquirerCode"));	
				projList.add(Projections.property("switchLab"));*/	
				criteria.add(Restrictions.eq("id", Integer.valueOf(signImage)));					
				AcqPlaceTransactionEntity entity = (AcqPlaceTransactionEntity) criteria.uniqueResult();			
				if(entity==null||entity+""==""){
					logger.warn("No result found");
					response.setStatus(EnumStatusConstant.NotFound.getId());
					response.setMessage(EnumStatusConstant.NotFound.getDescription());
					response.setResult(null);
				}else{
					logger.info("txn details is fetched");
					if(entity.getTxnType().equalsIgnoreCase("CARD")||entity.getTxnType().equalsIgnoreCase("CASHATPOS")||entity.getTxnType().equalsIgnoreCase("VOID")||entity.getTxnType().equalsIgnoreCase("CVOID")){
						AcqCardDetails cardEnt = (AcqCardDetails)session.createCriteria(AcqCardDetails.class).add(Restrictions.eq("transactionId",entity.getId())).uniqueResult();
						if(cardEnt==null||cardEnt+""==""){
							logger.warn("Card details not found");						
						}else{
							switchLab = entity.getSwitchLab();
							logger.info("card details found");
							if(cardEnt.getCardHolderName()==null||cardEnt.getCardHolderName()==""){
								map.put("cardHolderName", "NA");
							}else{
								map.put("cardHolderName", cardEnt.getCardHolderName());
							}
							
							if(entity.getCarPanNo()==null||entity.getCarPanNo()==""){
								map.put("cardPanNo", "NA");
							}else{
								map.put("cardPanNo", entity.getCarPanNo());
							}					
							
							if(cardEnt.getTerminalId()==null||cardEnt.getTerminalId()==""){
								map.put("terminalId", "NA");
							}else{
								map.put("terminalId", cardEnt.getTerminalId());
							}
							if(cardEnt.getCardType()==null||cardEnt.getCardType()==""){
								map.put("cardType", "NA");
							}else{
								map.put("cardType", cardEnt.getCardType());
							}
							
							if(cardEnt.getIpAddress()==null||cardEnt.getIpAddress()==""){
								map.put("ipAddress", "NA");
							}else{
								map.put("ipAddress", cardEnt.getIpAddress());
							}
							
							if(cardEnt.getImeiNo()==null||cardEnt.getImeiNo()==""){
								map.put("imeiNo", "NA");
							}else{
								map.put("imeiNo", cardEnt.getImeiNo());
							}
							if(cardEnt.getLatitude()==null||cardEnt.getLatitude()==""){
								map.put("latitude", "NA");
							}else{
								map.put("latitude", cardEnt.getLatitude());
							}
							if(cardEnt.getLongitude()==null||cardEnt.getLongitude()==""){
								map.put("longitude", "NA");
							}else{
								map.put("longitude", cardEnt.getLongitude());
							}
							if(cardEnt.getStan()==null||cardEnt.getStan()==""){
								map.put("stan", "NA");
							}else{
								map.put("stan", cardEnt.getStan());
							}
							
							if(cardEnt.getRrNo()==null||cardEnt.getRrNo()==""){
								map.put("rrNo", "NA");
							}else{
								if(cardEnt.getRrNo().startsWith("-")){
									map.put("rrNo", cardEnt.getRrNo().substring(1));
								}else{
									map.put("rrNo", cardEnt.getRrNo());
								}
							}
							
							if(cardEnt.getAuthCode()==null||cardEnt.getAuthCode()==""){
								map.put("authCode", "NA");
							}else{
								map.put("authCode", cardEnt.getAuthCode());
							}
							
							
							if(cardEnt.getBatchNo()==null||cardEnt.getBatchNo()==""){
								map.put("batchNo", "NA");
							}else{
								map.put("batchNo", cardEnt.getBatchNo());
							}
							logger.info("details selected and putted in map");
						}
					}
					//System.out.println("entity "+entity.getId());
					map.put("txnId",""+ entity.getId());
					map.put("acquirerCode",""+ entity.getAcquirerCode());
					map.put("invoiceId",""+ entity.getId());
					map.put("amount",""+ entity.getAmount());
					if(entity.getDescription()==null|entity.getDescription()==""){
						map.put("description","NA");
					}else{
						map.put("description",""+ entity.getDescription());
					}					
					map.put("txnType", entity.getTxnType());
					//System.out.println("entity org id "+entity.getOrgId());
					
					AcqOrganization orgEntity = (AcqOrganization)session.createCriteria(AcqOrganization.class).add(Restrictions.eq("id", Long.valueOf(""+entity.getOrgId()))).uniqueResult();
					logger.info("organization details selected");
					if(orgEntity==null||orgEntity+""==""){
						map.put("orgName", "NA");
						map.put("orgAddress", "NA");
					}else{
						String address = "".concat(orgEntity.getOrgAddress1()).concat("|"+orgEntity.getOrgAddress2()).			
						concat("|"+orgEntity.getCity());
						
						String address2="".concat(orgEntity.getState()).concat("|"+orgEntity.getCountry()).concat("|"+orgEntity.getZip());
						map.put("orgName", orgEntity.getOrgName());
						map.put("orgAddress1", address);
						map.put("orgAddress2", address2);
					}
					AcqUserDetailsEntity ezUser = (AcqUserDetailsEntity)session.createCriteria(AcqUserDetailsEntity.class).add(Restrictions.eq("id",Long.valueOf(""+entity.getUserid()))).uniqueResult();
					if(ezUser==null||ezUser+""==""){
						map.put("rmn","NA");
						map.put("emailId","NA");
					}else{
						map.put("rmn", ezUser.getRmn());
						map.put("emailId",ezUser.getEmailId());
					}
					SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
					try{
						if(entity.getTxnType().equals("CARD")||entity.getTxnType().equals("CASHATPOS")||entity.getTxnType().equals("VOID")||entity.getTxnType().equalsIgnoreCase("CVOID")){
							String tid = (String)session.createCriteria(AcqDevice.class).setProjection(Projections.property("tid")).add(Restrictions.eq("userId",""+entity.getUserid())).uniqueResult();
							if(tid!=null){
								map.put("tid", tid);
							}else{
								map.put("tid", "NA");
							}
						}else{
							map.put("tid", "NA");
						}
						if(entity.getTxnType().equals("CARD")||entity.getTxnType().equals("CASHATPOS")||entity.getTxnType().equals("VOID")||entity.getTxnType().equalsIgnoreCase("CVOID")){
							String merchantTID = (String)session.createCriteria(AcqMerchantEntity.class).setProjection(Projections.property("merchantTID")).add(Restrictions.eq("merchantId",""+entity.getMerchantId())).uniqueResult();
							if(merchantTID!=null){
								map.put("mid", merchantTID);
							}else{
								map.put("mid", "NA");
							}
						}else{
							map.put("mid", "NA");
						}
				}catch(Exception e){
					logger.error("error to select tid,mid "+e);
				}
					//map.put("tid", "123456");
					//map.put("mid", "MBK6306");
					map.put("walletType",entity.getTxnType());
					//System.out.println("df.format(entity.getOtpDateTime())::: "+df.format(entity.getOtpDateTime()));
					map.put("dateTime", ""+df.format(entity.getOtpDateTime()));	
					if(entity.getEmail()==null||entity.getEmail()==""){
						map.put("custEmail", "NA");
					}else{
						map.put("custEmail", ""+entity.getEmail());
					}
					
					map.put("custMobileNo", ""+entity.getMobile());
					if(entity.getStatus()==505){
						map.put("txnStatus", "SUCCESS");
					}else{
						map.put("txnStatus", "FAILED");
					}
					map.put("txnDes", ""+entity.getDescription());
					map.put("switchLab", switchLab);
					//map.put("dateTime", ""+entity.getDateTime());	
					//System.out.println("map is "+map);
					response.setStatus(EnumStatusConstant.OK.getId());
					response.setMessage(EnumStatusConstant.OK.getDescription());
					response.setResult(map);
					logger.info("Wallet txn details selected");
				}				
			}catch(Exception e){
				response.setStatus(EnumStatusConstant.RollBackError.getId());
				response.setMessage(EnumStatusConstant.RollBackError.getDescription());
				logger.error("Error to select transaction details in receipt dao "+e);
			}finally{
				session.close();
			}
			return response;
		}
		
		@Override
		public DbDto<Object> voidtransaction(AcqVoidTransactionModel model) {
			DbDto<Object> response = new DbDto<Object>();
			Session session = null;
			Transaction tx = null;
			String acquirerCode="";
			try {
				//System.out.println("111111111111111111");
				session = getNewSession();	
				Disjunction or = Restrictions.disjunction();
				or.add(Restrictions.eq("rrNo",model.getRrNo()));
				or.add(Restrictions.eq("rrNo","-"+model.getRrNo()));
				List<AcqCardDetails> cardDetails = (List<AcqCardDetails>)session.createCriteria(AcqCardDetails.class).add(or).list();
				logger.info("cardDetailscardDetails: "+cardDetails.size());
				if (cardDetails.isEmpty()) {
					response.setStatus(EnumStatusConstant.TransactionNotFound.getId());
					response.setMessage(EnumStatusConstant.TransactionNotFound.getDescription());
					logger.info("Transaction not found agains given rrno");
				}else if(cardDetails.size()>=2){
					response.setStatus(EnumStatusConstant.AlreadyInUse.getId());
					response.setMessage("Transaction already voided");
					logger.info("Transaction already voided");
				}else{
					logger.info("card details are found agains given rrno");
				//	System.out.println("aaaaaaaaaaaaaaaaaaaa");
					for(AcqCardDetails details:cardDetails){
						List<String> txnStatus = new ArrayList<String>();
						txnStatus.add("CARD");txnStatus.add("CASHATPOS");
						AcqPlaceTransactionEntity txnEntity = (AcqPlaceTransactionEntity)session.createCriteria(AcqPlaceTransactionEntity.class).add(Restrictions.eq("payoutStatus",700)).add(Restrictions.in("txnType",txnStatus)).add(Restrictions.eq("status",505)).add(Restrictions.eq("id",details.getTransactionId())).uniqueResult();
						if(txnEntity!=null||txnEntity+""!=""){
							//System.out.println("bbbbbbbbbbbbbbbbbbbbbbb");
							acquirerCode = txnEntity.getAcquirerCode();
							logger.info("transaction details found agains given rrno");
							AcqPlaceTransactionEntity entity = new AcqPlaceTransactionEntity();
							entity.setMobile(txnEntity.getMobile());
							entity.setAmount("-"+txnEntity.getAmount());
							entity.setEmail(txnEntity.getEmail());
							entity.setStatus(txnEntity.getStatus());
							entity.setUserid(txnEntity.getUserid());
							entity.setMerchantId(txnEntity.getMerchantId());
							entity.setOrgId(txnEntity.getOrgId());
							entity.setBankMdr(txnEntity.getBankMdr());
							entity.setEzMdr(txnEntity.getEzMdr());
							entity.setServiceTax(txnEntity.getServiceTax());
							entity.setDateTime(txnEntity.getDateTime());
							Date date= new Date();
							Timestamp currentTimestamp= new Timestamp(date.getTime());
							entity.setOtpDateTime(currentTimestamp);
							entity.setPayoutStatus(701);
							entity.setPayoutDateTime(txnEntity.getPayoutDateTime());
							entity.setDescription("Transaction Void Successful");							
							if(txnEntity.getTxnType().equalsIgnoreCase("CASHATPOS")){
								entity.setTxnType("CVOID");
							}else{
								entity.setTxnType("VOID");
							}
							entity.setCarPanNo(txnEntity.getCarPanNo());
							entity.setCustomerId(txnEntity.getCustomerId());
							entity.setAcquirerCode(txnEntity.getAcquirerCode());
							entity.setCashBackAmt(txnEntity.getCashBackAmt());
							entity.setSwitchLab(txnEntity.getSwitchLab());
							entity.setAcquirerMdr(txnEntity.getAcquirerMdr());
							entity.setSystemUtilityFee(txnEntity.getSystemUtilityFee());
							txnEntity.setPayoutStatus(701);
							session.save(entity);
							session.update(txnEntity);
							logger.info("transaction details saved");
							AcqCardDetails newCardDetails = new AcqCardDetails();
							newCardDetails.setTransactionId(entity.getId());
							newCardDetails.setTerminalId(details.getTerminalId());
							newCardDetails.setCardHolderName(details.getCardHolderName());
							newCardDetails.setCardType(details.getCardType());
							newCardDetails.setIpAddress(details.getIpAddress());
							newCardDetails.setImeiNo(details.getImeiNo());
							newCardDetails.setLatitude(details.getLatitude());
							newCardDetails.setLongitude(details.getLongitude());
							newCardDetails.setRrNo("-"+details.getRrNo());
							newCardDetails.setAuthCode(details.getAuthCode());
							newCardDetails.setBatchNo(details.getBatchNo());
							newCardDetails.setStan(details.getStan());
							session.save(newCardDetails);
							logger.info("card details saved");
							if(entity.getEmail()!=null&&entity.getEmail()!=""){
								model.setEmailId(entity.getEmail());
							}else{
								model.setEmailId("NA");
							}
							model.setMobile(entity.getMobile());
							model.setAmount(txnEntity.getAmount());
							model.setCardPanNo(entity.getCarPanNo());
							model.setDateTime((""+txnEntity.getOtpDateTime()).substring(0, 19));
							//System.out.println("ccccccccccccccccccccccccccccccc");
							if(entity.getUserid()!=null){
								String rmn = (String)session.createCriteria(AcqUserDetailsEntity.class).setProjection(Projections.property("rmn")).add(Restrictions.eq("id",Long.valueOf(""+txnEntity.getUserid()))).uniqueResult(); 
								model.setRmn(rmn);
							}if(entity.getMerchantId()!=null){
								String marketingName = (String)session.createCriteria(AcqOrganization.class).setProjection(Projections.property("orgName")).add(Restrictions.eq("id",Long.valueOf(""+txnEntity.getOrgId()))).uniqueResult(); 
								model.setMarketingName(marketingName);
								//merchantId.equals("8247")||merchantId.equals("8246")||merchantId.equals("8245")||merchantId.equals("8244")||merchantId.equals("8243")||merchantId.equals("8242")||merchantId.equals("8241")||merchantId.equals("8240")||merchantId.equals("8239")||merchantId.equals("8238")||merchantId.equals("8237")||merchantId.equals("8236")||merchantId.equals("8235")
								if((""+entity.getMerchantId()).equals("8247")||(""+entity.getMerchantId()).equals("8246")||(""+entity.getMerchantId()).equals("8245")||(""+entity.getMerchantId()).equals("8244")||(""+entity.getMerchantId()).equals("8243")||(""+entity.getMerchantId()).equals("8242")||(""+entity.getMerchantId()).equals("8241")||(""+entity.getMerchantId()).equals("8240")||(""+entity.getMerchantId()).equals("8239")||(""+entity.getMerchantId()).equals("8238")||(""+entity.getMerchantId()).equals("8237")||(""+entity.getMerchantId()).equals("8236")||(""+entity.getMerchantId()).equals("8235")||(""+entity.getMerchantId()).equals("8232")||(""+entity.getMerchantId()).equals("8230")||(""+entity.getMerchantId()).equals("8229")||(""+entity.getMerchantId()).equals("8227")||(""+entity.getMerchantId()).equals("8205")||(""+entity.getMerchantId()).equals("8204")||(""+entity.getMerchantId()).equals("8203")||(""+entity.getMerchantId()).equals("8202")||(""+entity.getMerchantId()).equals("8201")||(""+entity.getMerchantId()).equals("8200")||(""+entity.getMerchantId()).equals("8199")||(""+entity.getMerchantId()).equals("8198")||(""+entity.getMerchantId()).equals("8197")||(""+entity.getMerchantId()).equals("8196")||(""+entity.getMerchantId()).equals("8195")||(""+entity.getMerchantId()).equals("8184")||(""+entity.getMerchantId()).equals("8183")||(""+entity.getMerchantId()).equals("8182")||(""+entity.getMerchantId()).equals("8181")||(""+entity.getMerchantId()).equals("8180")||(""+entity.getMerchantId()).equals("8179")||(""+entity.getMerchantId()).equals("8178")||(""+entity.getMerchantId()).equals("8177")||(""+entity.getMerchantId()).equals("8176")||(""+entity.getMerchantId()).equals("8174")||(""+entity.getMerchantId()).equals("8151")||(""+entity.getMerchantId()).equals("8150")||(""+entity.getMerchantId()).equals("8149")||(""+entity.getMerchantId()).equals("8148")||(""+entity.getMerchantId()).equals("8147")||(""+entity.getMerchantId()).equals("8118")||(""+entity.getMerchantId()).equals("8117")||(""+entity.getMerchantId()).equals("8116")||(""+entity.getMerchantId()).equals("8115")||(""+entity.getMerchantId()).equals("8114")||(""+entity.getMerchantId()).equals("8113")||(""+entity.getMerchantId()).equals("8112")||(""+entity.getMerchantId()).equals("8101")||(""+entity.getMerchantId()).equals("8100")||(""+entity.getMerchantId()).equals("8099")||(""+entity.getMerchantId()).equals("8098")||(""+entity.getMerchantId()).equals("8097")||(""+entity.getMerchantId()).equals("8096")||(""+entity.getMerchantId()).equals("8095")||(""+entity.getMerchantId()).equals("8070")||(""+entity.getMerchantId()).equals("8069")||(""+entity.getMerchantId()).equals("8068")||(""+entity.getMerchantId()).equals("8067")||(""+entity.getMerchantId()).equals("8066")||(""+entity.getMerchantId()).equals("8065")||(""+entity.getMerchantId()).equals("8064")||(""+entity.getMerchantId()).equals("8063")||(""+entity.getMerchantId()).equals("8062")||(""+entity.getMerchantId()).equals("8061")||(""+entity.getMerchantId()).equals("8057")||(""+entity.getMerchantId()).equals("8033")||(""+entity.getMerchantId()).equals("8032")||(""+entity.getMerchantId()).equals("8031")||(""+entity.getMerchantId()).equals("8027")||(""+entity.getMerchantId()).equals("8026")||(""+entity.getMerchantId()).equals("8025")||(""+entity.getMerchantId()).equals("8024")||(""+entity.getMerchantId()).equals("8023")||(""+entity.getMerchantId()).equals("8022")||(""+entity.getMerchantId()).equals("8021")||(""+entity.getMerchantId()).equals("8020")||(""+entity.getMerchantId()).equals("8019")||(""+entity.getMerchantId()).equals("8017")||(""+entity.getMerchantId()).equals("8016")||(""+entity.getMerchantId()).equals("8015")||(""+entity.getMerchantId()).equals("8014")||(""+entity.getMerchantId()).equals("8013")||(""+entity.getMerchantId()).equals("8012")||(""+entity.getMerchantId()).equals("8011")||(""+entity.getMerchantId()).equals("8010")||(""+entity.getMerchantId()).equals("8008")||(""+entity.getMerchantId()).equals("8007")||(""+entity.getMerchantId()).equals("8006")||(""+entity.getMerchantId()).equals("8005")||(""+entity.getMerchantId()).equals("7997")||(""+entity.getMerchantId()).equals("7996")||(""+entity.getMerchantId()).equals("7995")||(""+entity.getMerchantId()).equals("7994")||(""+entity.getMerchantId()).equals("7993")||(""+entity.getMerchantId()).equals("7992")||(""+entity.getMerchantId()).equals("7991")||(""+entity.getMerchantId()).equals("7990")||(""+entity.getMerchantId()).equals("7989")||(""+entity.getMerchantId()).equals("7976")||(""+entity.getMerchantId()).equals("7942")||(""+entity.getMerchantId()).equals("7923")||(""+entity.getMerchantId()).equals("7911")||(""+entity.getMerchantId()).equals("7910")||(""+entity.getMerchantId()).equals("7909")||(""+entity.getMerchantId()).equals("7899")||(""+entity.getMerchantId()).equals("7898")||(""+entity.getMerchantId()).equals("7853")||(""+entity.getMerchantId()).equals("7852")||(""+entity.getMerchantId()).equals("7851")||(""+entity.getMerchantId()).equals("7850")||(""+entity.getMerchantId()).equals("7849")||(""+entity.getMerchantId()).equals("7839")||(""+entity.getMerchantId()).equals("7838")||(""+entity.getMerchantId()).equals("7807")||(""+entity.getMerchantId()).equals("7205")||(""+entity.getMerchantId()).equals("7232")||(""+entity.getMerchantId()).equals("7270")||(""+entity.getMerchantId()).equals("7222")||(""+entity.getMerchantId()).equals("7739")||(""+entity.getMerchantId()).equals("7738")||(""+entity.getMerchantId()).equals("7731")||(""+entity.getMerchantId()).equals("7704")||(""+entity.getMerchantId()).equals("7703")||(""+entity.getMerchantId()).equals("7702")||(""+entity.getMerchantId()).equals("7695")||(""+entity.getMerchantId()).equals("7694")||(""+entity.getMerchantId()).equals("7693")||(""+entity.getMerchantId()).equals("7676")||(""+entity.getMerchantId()).equals("7675")||(""+entity.getMerchantId()).equals("7674")||(""+entity.getMerchantId()).equals("7602")||(""+entity.getMerchantId()).equals("7601")||(""+entity.getMerchantId()).equals("7600")||(""+entity.getMerchantId()).equals("7553")){
									AcqDeviceDetails  deviceCriteria = (AcqDeviceDetails)session.createCriteria(AcqDeviceDetails.class).add(Restrictions.eq("userId",model.getSessionId())).uniqueResult();
									if(deviceCriteria==null||deviceCriteria+""==""){
										logger.info("integrationkey /member id not found");							
									}else{
										model.setMemberId(deviceCriteria.getMemberId());
										model.setTerminalId(deviceCriteria.getTerminalId());
									}
								}
							}
							//System.out.println("ddddddddddddddddddddddddddd");
							try{
								AcqRiskManagement riskEnt = (AcqRiskManagement)session.createCriteria(AcqRiskManagement.class).add(Restrictions.eq("txnId",txnEntity.getId())).uniqueResult(); 
								if(riskEnt!=null||riskEnt+""!=""){
									riskEnt.setStatus("0");
									session.save(riskEnt);
								}
							}catch(Exception e){
								logger.error("error to update risk transaction status "+e);
							}
							//System.out.println("eeeeeeeeeeeeeeeeeeeeeeeeeeee");
							model.setMerchantId(""+entity.getMerchantId());
							//System.out.println("entity.getTxnType(): "+entity.getTxnType());
							model.setTxnType(entity.getTxnType());
							model.setTxnId(""+entity.getId());
							model.setAcquirerCode(acquirerCode);
							if(entity.getStatus()==505){
								model.setStatus("Success");
							}else{
								model.setStatus("Failed");
							}
							//System.out.println("fffffffffffffffffffffffff");
							//HashMap<String,String> map = new HashMap<String,String>();
							//map.put("id", ""+entity.getId());
							//map.put("acquirerCode", entity.getAcquirerCode());
							model.setDescription(""+entity.getDescription());
							tx = session.beginTransaction();
							tx.commit();
							//System.out.println("ggggggggggggggggg");
							logger.info("transaction voided");
							response.setStatus(EnumStatusConstant.OK.getId());
							response.setMessage(EnumStatusConstant.OK.getDescription());
							response.setResult(null);
							logger.info("response returning from card transaction dao");
						}
					}
				}
			} catch (Exception e) {
				response.setStatus(EnumStatusConstant.RollBackError.getId());
				response.setMessage(EnumStatusConstant.RollBackError.getDescription());
				logger.error("Error to select transaction details "+e);
				tx.rollback();
			}finally{
				session.close();
			}
			return response;
		}

		@Override
		public DbDto<HashMap<String, HashMap<String, String>>> searchTransaction(
				AcqSearchTransactionModel model) {
			DbDto<HashMap<String, HashMap<String, String>>> response = new DbDto<HashMap<String, HashMap<String, String>>>();
			Session session = null;
			try {
				session = getNewSession();
				HashMap<String, HashMap<String, String>> responseMap = new HashMap<String, HashMap<String, String>>();
				List<AcqTransactionEntity> lstTxn = (List<AcqTransactionEntity>) session
						.createCriteria(AcqTransactionEntity.class)
						.add(Restrictions.eq("userId",
								Long.valueOf(model.getSessionId())))
						.add(Restrictions.eq("cardPanNo", model.getCardPanNo()))
						.add(Restrictions.eq("batchStatus", "UNSETTLED"))
						.add(Restrictions.eq("txStatus", "SUCCESSFULL")).list();
				Iterator<AcqTransactionEntity> lstTxnItr = lstTxn.iterator();
				if (lstTxn.isEmpty()) {
					response.setStatus(EnumStatusConstant.NotFound.getId());
					response.setMessage(EnumStatusConstant.NotFound
							.getDescription());
					logger.info("details not found");
				} else {
					HashMap<String, String> txnMap = null;
					while (lstTxnItr.hasNext()) {
						AcqTransactionEntity lstTxnEnt = (AcqTransactionEntity) lstTxnItr.next();
						txnMap = new HashMap<String, String>();
						txnMap.put("amount", lstTxnEnt.getTotalAmount());
						txnMap.put("cardPanNo", lstTxnEnt.getCardPanNo());
						txnMap.put("dateTime", lstTxnEnt.getDateTime());
						txnMap.put("cardType", lstTxnEnt.getCardType());
						txnMap.put("transactionId", "" + lstTxnEnt.getId());
						txnMap.put("txStatus", lstTxnEnt.getTxStatus());
						responseMap.put("" + lstTxnEnt.getId(), txnMap);
					}
					logger.info("Successful selected card pan no");
					response.setStatus(EnumStatusConstant.OK.getId());
					response.setMessage(EnumStatusConstant.OK.getDescription());
					response.setResult(responseMap);
				}
			} catch (Exception e) {
				response.setStatus(EnumStatusConstant.RollBackError.getId());
				response.setMessage(EnumStatusConstant.RollBackError
						.getDescription());
				logger.info("Error to search card pan no " + e);
			}finally{
				session.close();
			}
			return response;
		}

		@Override
		public DbDto<HashMap<String, HashMap<String, String>>> transactionDetail(
				AcqTransactionDetailModel model) {
			DbDto<HashMap<String, HashMap<String, String>>> response = new DbDto<HashMap<String, HashMap<String, String>>>();
			Session s = null;
			try {				
				s = getNewSession();
				HashMap<String, HashMap<String, String>> responseMap = new HashMap<String, HashMap<String, String>>();

				AcqTransactionDetailEntity txnDetailEnt = (AcqTransactionDetailEntity) s
						.createCriteria(AcqTransactionDetailEntity.class)
						.add(Restrictions.eq("userId", Long.valueOf(model.getSessionId())))
						.add(Restrictions.eq("id", Long.valueOf(model.getTransactionId())))
						.uniqueResult();

				if (txnDetailEnt != null && txnDetailEnt + "" != "") {
					HashMap<String, String> txnDetailMap = new HashMap<String, String>();
					txnDetailMap.put("cardExpDate", txnDetailEnt.getCardExpDate());
					txnDetailMap.put("cardHolderName",
							txnDetailEnt.getCardHolderName());
					txnDetailMap.put("cardPanNo", txnDetailEnt.getCardPanNo());
					txnDetailMap.put("cardType", txnDetailEnt.getCardType());
					txnDetailMap.put("custEmail", txnDetailEnt.getCustEmail());
					txnDetailMap.put("custMobile", txnDetailEnt.getCustMobile());
					txnDetailMap.put("dateTime", txnDetailEnt.getDateTime());
					txnDetailMap.put("imeiNo", txnDetailEnt.getImeiNo());
					txnDetailMap.put("ipAddress", txnDetailEnt.getIpAddress());
					txnDetailMap.put("amount", txnDetailEnt.getTotalAmount());
					txnDetailMap.put("latitude", txnDetailEnt.getLatitude());
					txnDetailMap.put("logitude", txnDetailEnt.getLogitude());
					AcqTransactionMdrDetailsEntity mdrDetail = txnDetailEnt
							.getmdrdetailentity();
					txnDetailMap.put("bankMdr", mdrDetail.getBankMdr());
					txnDetailMap.put("acqMdr", mdrDetail.getEzMdr());
					txnDetailMap.put("serviceTax", mdrDetail.getServiceTax());
					txnDetailMap.put("mdrCat", mdrDetail.getMdrCat());
					responseMap.put("", txnDetailMap);
					response.setStatus(EnumStatusConstant.OK.getId());
					response.setMessage(EnumStatusConstant.OK.getDescription());
					response.setResult(responseMap);
				} else {
					response.setStatus(EnumStatusConstant.NotFound.getId());
					response.setMessage(EnumStatusConstant.NotFound
							.getDescription());
					response.setResult(null);
				}
			} catch (Exception e) {
				response.setStatus(EnumStatusConstant.RollBackError.getId());
				response.setMessage(EnumStatusConstant.RollBackError
						.getDescription());
				response.setResult(null);
			}finally{
				s.close();
			}
			return response;
		}
	
		@Override
		public DbDto<Object> transactionSign(AcqTransactionSignModel model) {
			//System.out.println("rquest in transaction sign dao");
			DbDto<Object> response = new DbDto<Object>();
			AcqSignEntity signEnt = null;
			List<String> list = new ArrayList<String>();
			AcqContactEntity cntctEnt = new AcqContactEntity();
			Session session = null;
			String cardHolderName="";
			String txnType="";
			try {
				//System.out.println("rquest in transaction sign dao step111111111111111");
				session = getNewSession();
				signEnt = (AcqSignEntity) session.get(AcqSignEntity.class,
						Integer.valueOf(model.getTransactionId()));				
				cardHolderName = (String) session.createCriteria(AcqCardDetails.class).setProjection(Projections.property("cardHolderName")).add(Restrictions.eq("transactionId",Integer.valueOf(model.getTransactionId()))).uniqueResult();
				if(cardHolderName!=null&&cardHolderName.length()>30){
					cardHolderName.substring(0,30);
				}
				//System.out.println("cardHolderName:"+cardHolderName);
				try{
					//logger.info("card holder name in sign is "+cardHolderName);
					Pattern pt = Pattern.compile("[^a-zA-Z0-9]");
					Matcher match= pt.matcher(cardHolderName);
					while(match.find()){
						String s= match.group();
						cardHolderName=cardHolderName.replaceAll("\\"+s, "");
					}
				}catch(Exception e){
					logger.error("error to extract card holder name "+e);
				}
		        logger.info(""+model.getCardPanNo()+cardHolderName);
		       // System.out.println("rquest in transaction sign dao step222222222222222222222222");
		        AcqContactEntity cntEnt = (AcqContactEntity)session.createCriteria(AcqContactEntity.class).add(Restrictions.eq("cardPanNo",(model.getCardPanNo()+cardHolderName))).uniqueResult();
				if(cntEnt!=null&&cntEnt+""!=""){
					cntEnt.setCustPhone(model.getCustPhone());
					session.update(cntEnt);
					//System.out.println("if model.getCustPhone():"+model.getCustPhone());					
				}else{
					cntctEnt.setCardPanNo((model.getCardPanNo()+cardHolderName));
					cntctEnt.setCustPhone(model.getCustPhone());					
					session.save(cntctEnt);
					//System.out.println("else model.getCustPhone():"+model.getCustPhone());
				}				
			}catch(Exception e){
				logger.error("exception in getting the entities " + e);				
			}			
			try {
				if (signEnt == null || signEnt + "" == "") {
					response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
					response.setMessage(" not found");
					logger.info("TransactionId not found");
				} else {
					Transaction txn = null;
					try {
						logger.info("txn info found");
						//System.out.println("aaaaaaaaaaaaaaaaaaaaaaaa:"+model.getCustPhone());
						//System.out.println("bbbbbbbbbbbbbbbbbbbbbbb:"+model.getCustEmail());
						if((model.getCustPhone() != null && model.getCustPhone() != "")) {
							signEnt.setCustPhone(model.getCustPhone());
						}
						if ((model.getCustEmail() != null && model.getCustEmail() != "")) {
							signEnt.setCustEmail(model.getCustEmail());
						}
						//System.out.println("ccccccccccccccccccccccccccc");
						response.setStatus(EnumStatusConstant.OK.getId());
						response.setMessage(EnumStatusConstant.OK.getDescription());
						ProjectionList pList = Projections.projectionList();
						pList.add(Projections.property("amount"));
						pList.add(Projections.property("status"));
						pList.add(Projections.property("otpDateTime"));
						pList.add(Projections.property("orgId"));
						pList.add(Projections.property("txnType"));
						try{
							Criteria criteria = session.createCriteria(AcqPlaceTransactionEntity.class).setProjection(pList).add(Restrictions.eq("carPanNo",model.getCardPanNo())).add(Restrictions.eq("id",Integer.valueOf(model.getTransactionId())));
							List list2 = criteria.list();
							if(!list2.isEmpty()){
								for(Object obj:list2){
									Object[] ob=(Object[])obj;
									list.add(0,""+ob[0]);
									list.add(1,""+ob[1]);
									String dat = ""+ob[2];
									list.add(2,""+dat.substring(0, 19));
									list.add(3,""+ob[3]);
									list.add(4,""+model.getTransactionId());
									txnType = ""+ob[4];
								}
								if(!list.isEmpty()){
									String marketingName = (String)session.createCriteria(AcqOrganization.class).setProjection(Projections.property("orgName")).add(Restrictions.eq("id",Long.valueOf(""+list.get(3)))).uniqueResult();
									list.add(5,marketingName);
								}
								String userQuery = "select rmn,smsStatus,acquirerCode from AcqUserDetailsEntity where id =:userId";
								Query query=session.createQuery(userQuery);
								query.setParameter("userId", Long.valueOf(model.getSessionId()));
								//List risklist1 = query.list();
								String rmn = "";String smsStatus="";String acquirerCode="";						
								for(Object obj : query.list()){
									Object[] ob = (Object[])obj;	
									rmn=""+ob[0];
									smsStatus=""+ob[1];
									acquirerCode=""+ob[2];
									list.add(6,rmn);
									list.add(7,smsStatus);
									list.add(8,acquirerCode);
								}						
								//String rmn= (String) session.createCriteria(AcqUserDetailsEntity.class).setProjection(Projections.property("rmn")).add(Restrictions.eq("id",Long.valueOf(model.getSessionId()))).uniqueResult();
								//list.add(6,rmn);
							}
						}catch(Exception e){
							logger.error("error to fetch transaction details "+e);
						}
						list.add(9,txnType);
						txn = session.beginTransaction();
						session.update(signEnt);		
						txn.commit();
						System.out.println("pan no and phone updated");						
					} catch (Exception e) {
						txn.rollback();
						System.out.println("error to sign service" + e);
						logger.info("error to sign service1" + e);
						response.setStatus(EnumStatusConstant.RollBackError.getId());
						response.setMessage(EnumStatusConstant.RollBackError
								.getDescription());
					}
				}
			} catch (Exception e) {
				logger.error("error to sign service2" + e);
				response.setStatus(EnumStatusConstant.RollBackError.getId());
				response.setMessage(EnumStatusConstant.RollBackError
						.getDescription());
			}finally{
				session.close();
			}
			response.setResult(list);
			return response;
		}

		@Override
		public DbDto<HashMap<String, HashMap<String, String>>> placeTransaction(AcqTransactionModel model,
				String clientIpAddress) {
			System.out.println("Request landing in Transaction Init Dao");
			DbDto<HashMap<String, HashMap<String, String>>> response = new DbDto<HashMap<String, HashMap<String, String>>>();
			HashMap<String, HashMap<String, String>> responseMap = new HashMap<String, HashMap<String, String>>();
			StringBuffer logReport = new StringBuffer();
			String status = "";
			System.out.println("111111111111111111111");
			logReport.append(",Info: Transaction dao started");
			Session session = null;
			AcqSettingEntity settingEnt = null;
			try {
				logReport.append(",Info: Creating hibernate session object");				
				session = getNewSession();
				logReport.append(",Info: hibernate session object created");
				AcqUserEntity userEnt = (AcqUserEntity) session.get(
						AcqUserEntity.class, Long.valueOf(model.getUserId()));
				if(userEnt==null||userEnt+""==""){
					logReport.append(",User not found for card transaction");
					response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
					response.setMessage("User not found for card transaction");
					response.setResult(responseMap);
					return response;
				}
				System.out.println("Loading Setting entity");
				logReport.append(",Info: Loading Setting entity");
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
				System.out.println("Feching mdr from user device");
				logReport.append(",Info: Feching mdr from user device");				
				AcqMapDeviceUserEntity deviceEnt = (AcqMapDeviceUserEntity) session.createCriteria(AcqMapDeviceUserEntity.class).add(Restrictions.eq("userId",Long.valueOf(model.getUserId()))).add(Restrictions.or(Restrictions.or(Property.forName("deviceType").eq("credit"),Property.forName("deviceType").eq("wallet/credit")))).uniqueResult();
				if (deviceEnt == null || deviceEnt + "" == "") {
					response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
					response.setMessage("User details not found for card transction");
					logReport.append(",Info: User details not found");
					status = "FAILED";
					response.setResult(responseMap);
					return response;
				} else {
					logger.info("user id is "+model.getUserId());
					if(!model.getDeviceId().equalsIgnoreCase(deviceEnt.getSerialNo())){
						logReport.append("Transaction heppening from different mapping device");
						response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
						response.setMessage("Mismatch Device Id");
						response.setResult(responseMap);
						return response;
					}
					try {
						logReport.append(",Info: orgnization and merchant fetched successfully");
						AcqPlaceTransactionEntity txnEnt = new AcqPlaceTransactionEntity();
						logReport.append(",Info: Saving txn data in db");
						txnEnt.setUserid(Integer.valueOf(""+userEnt.getUserId()));						
						txnEnt.setOrgId(Integer.valueOf(""+userEnt.getOrgId()));
						txnEnt.setMerchantId(Integer.valueOf(""+org.getMerchantId()));
						txnEnt.setAmount(model.getTotalAmount());
						txnEnt.setMobile("0000");
						txnEnt.setTxnType(model.getCardTxnType());
						if(!userEnt.getAcquirerCode().equalsIgnoreCase("Acquiro")){
							settingEnt = (AcqSettingEntity) session
									.createCriteria(AcqSettingEntity.class)
									.add(Restrictions.eq("acquirerCode", userEnt.getAcquirerCode())).uniqueResult();
							if(settingEnt.getMaintenanceMode().equalsIgnoreCase("1")&&!settingEnt.getMaintenanceReason().equalsIgnoreCase("NA")){
								logger.info(",Acquirer server maintenance mode is on, transaction can't be place");
								response.setStatus(EnumStatusConstant.MaintenanceMode.getId());
								response.setMessage(EnumStatusConstant.MaintenanceMode.getDescription());
								return response;
							}
							if(model.getCardTxnType().equalsIgnoreCase("CASHATPOS")){
								txnEnt.setBankMdr(settingEnt1.getMdrCashAtPos());
								txnEnt.setEzMdr(settingEnt.getMdrCashAtPos());
								txnEnt.setAcquirerMdr(deviceEnt.getMdrCashAtPos());
								txnEnt.setServiceTax(settingEnt.getServiceTaxCashAtPos());	
								txnEnt.setSystemUtilityFee("0.0");
							}else{
								txnEnt.setEzMdr("0.0");
								txnEnt.setBankMdr("0.0");
								txnEnt.setAcquirerMdr("0.0");
								if(Double.valueOf(model.getTotalAmount())<=Double.valueOf(settingEnt.getTxnTaxAmt())){
									txnEnt.setServiceTax("0.0");
								}else{
									txnEnt.setServiceTax(settingEnt.getServiceTax());
								}								
								if(Double.valueOf(model.getTotalAmount())<Double.valueOf(settingEnt.getSystmUtltyAmt())){
									txnEnt.setSystemUtilityFee(settingEnt.getSystmUtltyFee());
								}else{
									txnEnt.setSystemUtilityFee("0.0");
								}
							}
						}else{
							 settingEnt = (AcqSettingEntity) session
									.createCriteria(AcqSettingEntity.class)
									.add(Restrictions.eq("id", 1l)).uniqueResult();
							if(model.getCardTxnType().equalsIgnoreCase("CASHATPOS")){
								txnEnt.setBankMdr(settingEnt1.getMdrCashAtPos());
								txnEnt.setEzMdr(deviceEnt.getMdrCashAtPos());
								txnEnt.setAcquirerMdr("0.0");
								txnEnt.setServiceTax(settingEnt1.getServiceTaxCashAtPos());	
								txnEnt.setSystemUtilityFee("0.0");
							}else{
								txnEnt.setEzMdr("0.0");
								txnEnt.setBankMdr("0.0");
								txnEnt.setAcquirerMdr("0.0");
								if(Double.valueOf(model.getTotalAmount())<=Double.valueOf(settingEnt1.getTxnTaxAmt())){
									txnEnt.setServiceTax("0.0");
								}else{
									txnEnt.setServiceTax(settingEnt1.getServiceTax());
								}
								if(Double.valueOf(model.getTotalAmount())<Double.valueOf(settingEnt.getSystmUtltyAmt())){
									txnEnt.setSystemUtilityFee(settingEnt.getSystmUtltyFee());
								}else{
									txnEnt.setSystemUtilityFee("0.0");
								}
							}
						}
						Date date= new Date();
						Timestamp currentTimestamp= new Timestamp(date.getTime());
						txnEnt.setOtpDateTime(currentTimestamp);
						txnEnt.setDateTime(currentTimestamp);
						txnEnt.setStatus(503);
						txnEnt.setCarPanNo("0");
						txnEnt.setCustomerId("0");
						txnEnt.setPayoutStatus(700);
						txnEnt.setPayoutDateTime(currentTimestamp);
						txnEnt.setSwitchLab("AGS");
						txnEnt.setAcquirerCode(userEnt.getAcquirerCode());
						txnEnt.setCashBackAmt("0.0");
						txnEnt.setAcqPayoutStatus(800);
						txnEnt.setAcqPayoutDateTime(currentTimestamp);
						txnEnt.setAppCertificate("NA");
						txnEnt.setAid("NA");
						txnEnt.setScriptResult("NA");
						Transaction tx = session.beginTransaction();
						session.save(txnEnt);
						AcqCardDetails cardEntity = new AcqCardDetails();
						//System.out.println("txnEnt.getId(): "+txnEnt.getId());
						cardEntity.setTransactionId(txnEnt.getId());
						//cardEntity.setTerminalId("123123");
						cardEntity.setTerminalId(deviceEnt.getBankTid());
						/*cardEntity.setCardHolderName(model.getCardHolderName());
						cardEntity.setCardType(model.getCardType());
						cardEntity.setCardExpDate(model.getCardExpDate());*/
						cardEntity.setCardHolderName("NA");
						cardEntity.setCardType("NA");
						cardEntity.setIpAddress(model.getIpAddress());
						cardEntity.setImeiNo(model.getImeiNo());
						cardEntity.setLatitude(model.getLatitude());
						cardEntity.setLongitude(model.getLogitude());
						cardEntity.setGmtDateTime("NA");
						cardEntity.setDe61("NA");//GmtDateTime("NA");
						cardEntity.setStan("NA");//CustomerMobile(model.get)
						cardEntity.setRrNo("NA");
						cardEntity.setBatchNo("0");
						cardEntity.setAuthCode("NA");
						//cardEntity.set
						session.save(cardEntity);
						tx.commit();
						logger.info("card transaction is initiated");
						logReport.append(",Info: Txn data successfully puted in txn table");
						//String mdrCat = model.get						
						logReport.append(",Info: Putted mdr values in table");			
						//session.save(adddetailentity);
						logReport.append(",Info: Data successfully putted in 2nd table");					
						Transaction addDetailTxn = session.beginTransaction();
						addDetailTxn.commit();
						HashMap<String, String> map = new HashMap<String,String>();
						map.put("TxnId", txnEnt.getId() + "");
						map.put("txnStatusCode", EnumWalletConstant.TransactionInitiated.getId());
						map.put("txnStatusDescription", EnumWalletConstant.TransactionInitiated.getDescription());						
						responseMap.put("txnResponse", map);
						response.setStatus(EnumStatusConstant.OK.getId());
						response.setMessage(EnumStatusConstant.OK.getDescription());
						response.setResult(responseMap);
						status = "SUCCESSFULL";
						logReport.append(",Info: Transaction successfully placed");
					} catch (Exception e) {
						response.setStatus(EnumStatusConstant.RollBackError.getId());
						response.setMessage(EnumStatusConstant.RollBackError.getDescription());
						status = "FAILED";
						logReport.append(",Error: To place transaction " + e);
					}
				}
			} catch (Exception e) {
				response.setStatus(EnumStatusConstant.RollBackError.getId());
				response.setMessage(EnumStatusConstant.RollBackError
						.getDescription());
				logReport.append(",Error: To select user " + e);
				status = "FAILED";	
				System.out.println("Error to select User");
			} finally {				
				//System.out.println(session.isOpen()+":open card init txn session:"+session.isConnected());
				session.close();	
				if(session.isOpen()==true||session.isConnected()==true){
					System.out.println(session.isOpen()+":close init card111:"+session.isConnected());
					session.close();
				}
				
			}			
			return response;
		}
		
		
	
	
}
