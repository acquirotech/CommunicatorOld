package com.acq.web.dao.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Repository;

import com.acq.EnumStatusConstant;
import com.acq.RechargeApis;
import com.acq.users.entity.AcqMapDeviceUserEntity;
import com.acq.users.entity.AcqSettingEntity;
import com.acq.users.entity.RechargeEntity;
import com.acq.web.controller.model.AcqRechargeMerchantBalModel;
import com.acq.web.controller.model.AcqRechargeModel;
import com.acq.web.controller.model.AcqRechargeStatusModel;
import com.acq.web.dao.AcqDao;
import com.acq.web.dao.RechargeDaoInf;
import com.acq.web.dto.impl.DbDto2;

	@Repository
	public class RechargeDao extends AcqDao implements RechargeDaoInf {
		
		final static Logger logger = Logger.getLogger(RechargeDao.class);
		RechargeApis rechargeApi = new RechargeApis();
		
		String testUrl = "";
		
		
		@Override
		public DbDto2<Object> getOperators(AcqRechargeMerchantBalModel model) {
			DbDto2<Object> response = new DbDto2<Object>();
			logger.info("request in get Operator dao");
			Session session = null;
			try{				
				session = getNewSession();
				String responseString="";
				JSONObject operator1 = new JSONObject();
				try {
					operator1.put("operatorId", "1");
					operator1.put("operatorCode", "VODAFONE");
					operator1.put("operatorName", "VODAFONE");
				} catch (JSONException e) {
				    // TODO Auto-generated catch block
				    e.printStackTrace();
				}
				JSONObject operator2 = new JSONObject();
				try {
						operator2.put("operatorId", "2");
						operator2.put("operatorCode", "AIRTEL");
						operator2.put("operatorName", "AIRTEL");
				
				} catch (JSONException e) {
				   e.printStackTrace();
				}JSONObject operator3 = new JSONObject();
				try {
					operator3.put("operatorId", "3");
					operator3.put("operatorCode", "IDEA");
					operator3.put("operatorName", "IDEA");
			
			} catch (JSONException e) {
			    // TODO Auto-generated catch block
			    e.printStackTrace();
			}JSONObject operator4 = new JSONObject();
			try {
				operator4.put("operatorId", "4");
				operator4.put("operatorCode", "AIRCEL");
				operator4.put("operatorName", "AIRCEL");
		
		} catch (JSONException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}JSONObject operator5 = new JSONObject();
		try {
			operator5.put("operatorId", "5");
			operator5.put("operatorCode", "BSNL");
			operator5.put("operatorName", "BSNL");
	
	} catch (JSONException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
JSONObject operator6 = new JSONObject();
		try {
			operator6.put("operatorId", "6");
			operator6.put("operatorCode", "DOCOMO");
			operator6.put("operatorName", "DOCOMO");
	
	} catch (JSONException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	JSONObject operator7 = new JSONObject();
		try {
			operator7.put("operatorId", "7");
			operator7.put("operatorCode", "MTS");
			operator7.put("operatorName", "MTS");
	
	} catch (JSONException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	JSONObject operator8 = new JSONObject();
		try {
			operator8.put("operatorId", "8");
			operator8.put("operatorCode", "RELIANCE");
			operator8.put("operatorName", "RELIANCE");
	
	} catch (JSONException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}JSONObject operator9 = new JSONObject();
		try {
			operator9.put("operatorId", "9");
			operator9.put("operatorCode", "RELIANCECDMA");
			operator9.put("operatorName", "RELIANCECDMA");
	
	} catch (JSONException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}JSONObject operator10 = new JSONObject();
		try {
			operator10.put("operatorId", "10");
			operator10.put("operatorCode", "JIO");
			operator10.put("operatorName", "JIO");
	
	} catch (JSONException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}JSONObject operator11 = new JSONObject();
		try {
			operator11.put("operatorId", "11");
			operator11.put("operatorCode", "TATACDMA");
			operator11.put("operatorName", "TATACDMA");
	
	} catch (JSONException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}JSONObject operator12 = new JSONObject();
		try {
			operator12.put("operatorId", "12");
			operator12.put("operatorCode", "TELENOR");
			operator12.put("operatorName", "TELENOR");
	
	} catch (JSONException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}JSONObject operator13 = new JSONObject();
		try {
			operator13.put("operatorId", "13");
			operator13.put("operatorCode", "MTNL");
			operator13.put("operatorName", "MTNL");
	
	} catch (JSONException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}JSONObject operator14 = new JSONObject();
		try {
			operator14.put("operatorId", "20");
			operator14.put("operatorCode", "DOCOMOSPL");
			operator14.put("operatorName", "DOCOMO SPECIAL");
	
	} catch (JSONException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}JSONObject operator15 = new JSONObject();
		try {
			operator15.put("operatorId", "21");
			operator15.put("operatorCode", "TELENORSPL");
			operator15.put("operatorName", "TELENOR SPECIAL");
	
	} catch (JSONException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}JSONObject operator16 = new JSONObject();
		try {
			operator16.put("operatorId", "25");
			operator16.put("operatorCode", "BSNLSPL");
			operator16.put("operatorName", "BSNL STV");
	
	} catch (JSONException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
			JSONObject circleCode1 = new JSONObject();
			try {
				circleCode1.put("circleId", "1");
				circleCode1.put("circleCode", "AP");
				circleCode1.put("circleName", "Andhra Pradesh");
			} catch (JSONException e) {
			    // TODO Auto-generated catch block
			    e.printStackTrace();
			}
			JSONObject circleCode2 = new JSONObject();
			try {
				circleCode2.put("circleId", "2");
				circleCode2.put("circleCode", "CH");
				circleCode2.put("circleName", "Chennai");
			
			} catch (JSONException e) {
			   e.printStackTrace();
			}JSONObject circleCode3 = new JSONObject();
			try {
				circleCode3.put("circleId", "3");
				circleCode3.put("circleCode", "DH");
				circleCode3.put("circleName", "Delhi");
		
		} catch (JSONException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}JSONObject circleCode4 = new JSONObject();
		try {
			circleCode4.put("circleId", "4");
			circleCode4.put("circleCode", "GJ");
			circleCode4.put("circleName", "Gujarat");
	
	} catch (JSONException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
JSONObject circleCode5 = new JSONObject();
		try {
			circleCode5.put("circleId", "5");
			circleCode5.put("circleCode", "HR");
			circleCode5.put("circleName", "Haryana");
	
	} catch (JSONException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
JSONObject circleCode6 = new JSONObject();
		try {
			circleCode6.put("circleId", "6");
			circleCode6.put("circleCode", "KA");
			circleCode6.put("circleName", "Karnataka");
	
	} catch (JSONException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	JSONObject circleCode7 = new JSONObject();
		try {
			circleCode7.put("circleId", "7");
			circleCode7.put("circleCode", "KE");
			circleCode7.put("circleName", "Kerla");
	
	} catch (JSONException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	JSONObject circleCode8 = new JSONObject();
		try {
			circleCode8.put("circleId", "8");
			circleCode8.put("circleCode", "KO");
			circleCode8.put("circleName", "Kolkata");
	
	} catch (JSONException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}JSONObject circleCode9 = new JSONObject();
		try {
			circleCode9.put("circleId", "9");
			circleCode9.put("circleCode", "MH");
			circleCode9.put("circleName", "Maharashtra Goa");
	
	} catch (JSONException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}JSONObject circleCode10 = new JSONObject();
		try {
			circleCode10.put("circleId", "10");
			circleCode10.put("circleCode", "MU");
			circleCode10.put("circleName", "Mumbai");
	
	} catch (JSONException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}JSONObject circleCode11 = new JSONObject();
		try {
			circleCode11.put("circleId", "11");
			circleCode11.put("circleCode", "PB");
			circleCode11.put("circleName", "Punjab");
	
	} catch (JSONException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}JSONObject circleCode12 = new JSONObject();
		try {
			circleCode12.put("circleId", "12");
			circleCode12.put("circleCode", "RJ");
			circleCode12.put("circleName", "Rajasthan");
	
	} catch (JSONException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}JSONObject circleCode13 = new JSONObject();
		try {
			circleCode13.put("circleId", "13");
			circleCode13.put("circleCode", "WB");
			circleCode13.put("circleName", "West Bengal");
	
	} catch (JSONException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}JSONObject circleCode14 = new JSONObject();
		try {
			circleCode14.put("circleId", "14");
			circleCode14.put("circleCode", "TN");
			circleCode14.put("circleName", "Tamil Nadu");
	
	} catch (JSONException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}JSONObject circleCode15 = new JSONObject();
		try {
			circleCode15.put("circleId", "15");
			circleCode15.put("circleCode", "UE");
			circleCode15.put("circleName", "UP East");
	
	} catch (JSONException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}JSONObject circleCode16 = new JSONObject();
		try {
			circleCode16.put("circleId", "16");
			circleCode16.put("circleCode", "UW");
			circleCode16.put("circleName", "UP West");
	
	} catch (JSONException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}JSONObject circleCode17 = new JSONObject();
		try {
			circleCode17.put("circleId", "17");
			circleCode17.put("circleCode", "HP");
			circleCode17.put("circleName", "Himachal Pradesh");
	
	} catch (JSONException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}JSONObject circleCode18 = new JSONObject();
		try {
			circleCode18.put("circleId", "18");
			circleCode18.put("circleCode", "MP");
			circleCode18.put("circleName", "Madhya Pradesh");
	
	} catch (JSONException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}JSONObject circleCode19 = new JSONObject();
		try {
			circleCode19.put("circleId", "19");
			circleCode19.put("circleCode", "AS");
			circleCode19.put("circleName", "Assam");
	
	} catch (JSONException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}JSONObject circleCode20 = new JSONObject();
		try {
			circleCode20.put("circleId", "20");
			circleCode20.put("circleCode", "BR");
			circleCode20.put("circleName", "Bihar");
	
	} catch (JSONException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}JSONObject circleCode21 = new JSONObject();
		try {
			circleCode21.put("circleId", "21");
			circleCode21.put("circleCode", "JK");
			circleCode21.put("circleName", "Jammu Kashmir");
	
	} catch (JSONException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}JSONObject circleCode22 = new JSONObject();
		try {
			circleCode22.put("circleId", "22");
			circleCode22.put("circleCode", "NE");
			circleCode22.put("circleName", "North East");
	
	} catch (JSONException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}JSONObject circleCode23 = new JSONObject();
		try {
			circleCode23.put("circleId", "23");
			circleCode23.put("circleCode", "OR");
			circleCode23.put("circleName", "Orissa");
	
	} catch (JSONException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}JSONObject circleCode24 = new JSONObject();
		try {
			circleCode24.put("circleId", "51");
			circleCode24.put("circleCode", "UN");
			circleCode24.put("circleName", "Unknown");
	
	} catch (JSONException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}JSONObject dth1 = new JSONObject();
	try {
		dth1.put("dthCode", "14");
		dth1.put("provider", "Airtel DTH");
} catch (JSONException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
}JSONObject dth2 = new JSONObject();
	try {
		dth2.put("dthCode", "15");
		dth2.put("provider", "BigTV");
} catch (JSONException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
}JSONObject dth3 = new JSONObject();
	try {
		dth3.put("dthCode", "16");
		dth3.put("provider", "DishTV");
} catch (JSONException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
}JSONObject dth4 = new JSONObject();
	try {
		dth4.put("dthCode", "17");
		dth4.put("provider", "SunTV");
} catch (JSONException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
}JSONObject dth5 = new JSONObject();
	try {
		dth5.put("dthCode", "18");
		dth5.put("provider", "VideoconDTH");
} catch (JSONException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
}JSONObject dth6 = new JSONObject();
	try {
		dth6.put("dthCode", "19");
		dth6.put("provider", "TataSky");
} catch (JSONException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
}

				JSONArray jsonArray = new JSONArray();
				JSONArray jsonArray1 = new JSONArray();
				JSONArray jsonArray2 = new JSONArray();
				jsonArray.put(operator1);jsonArray.put(operator2);jsonArray.put(operator3);jsonArray.put(operator4);jsonArray.put(operator5);jsonArray.put(operator6);
				jsonArray.put(operator7);jsonArray.put(operator8);jsonArray.put(operator9);jsonArray.put(operator10);jsonArray.put(operator11);jsonArray.put(operator12);
				jsonArray.put(operator13);jsonArray.put(operator14);jsonArray.put(operator15);jsonArray.put(operator15);jsonArray.put(operator16);
				jsonArray1.put(circleCode1);jsonArray1.put(circleCode2);jsonArray1.put(circleCode3);jsonArray1.put(circleCode4);jsonArray1.put(circleCode5);jsonArray1.put(circleCode6);jsonArray1.put(circleCode7);jsonArray1.put(circleCode8);
				jsonArray1.put(circleCode9);jsonArray1.put(circleCode10);jsonArray1.put(circleCode11);jsonArray1.put(circleCode12);jsonArray1.put(circleCode13);jsonArray1.put(circleCode14);
				jsonArray1.put(circleCode15);jsonArray1.put(circleCode16);jsonArray1.put(circleCode17);jsonArray1.put(circleCode18);jsonArray1.put(circleCode19);jsonArray1.put(circleCode20);
				jsonArray1.put(circleCode21);jsonArray1.put(circleCode22);jsonArray1.put(circleCode23);jsonArray1.put(circleCode24);
				jsonArray2.put(dth1);jsonArray2.put(dth2);jsonArray2.put(dth3);jsonArray2.put(dth4);jsonArray2.put(dth5);jsonArray2.put(dth6);
				JSONObject studentsObj = new JSONObject();
				    studentsObj.put("Operators", jsonArray);
				    studentsObj.put("circleCodes", jsonArray1);
				    studentsObj.put("dth", jsonArray2);
				   // String jsonStr = studentsObj.toString();

				    //System.out.println("jsonString: "+studentsObj);
					response.setStatusCode(EnumStatusConstant.OK.getId());
					response.setStatusMessage(EnumStatusConstant.OK.getDescription());
					response.setBody(studentsObj);				
			}catch(Exception e){
				response.setStatusCode(EnumStatusConstant.RollBackError.getId());
				response.setStatusMessage(EnumStatusConstant.RollBackError.getDescription());
			}finally{
				session.close();
			}
			return response;
		}
	
		@Override
		public DbDto2<Object> doRecharge(AcqRechargeModel model) {
			DbDto2<Object> response = new DbDto2<Object>();
			HashMap<String, HashMap<String,String>> responseMap= new HashMap<String, HashMap<String,String>>();
			Session session = null;
			try{			
				session = getNewSession();
				Transaction tx = null;
				String id = null;
				String twId = null;
				Date date = new Date();
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String dateTime = format.format(date);
				HashMap<String, String> txnDetailMap = null;
				AcqMapDeviceUserEntity deviceMapDevice = (AcqMapDeviceUserEntity)session.createCriteria(AcqMapDeviceUserEntity.class).add(Restrictions.eq("userId", Long.valueOf(model.getSessionId()))).uniqueResult();
				if(Double.parseDouble(""+deviceMapDevice.getRechargeBal())>=Double.parseDouble(model.getAmount()))
					{
						RechargeEntity entity = new RechargeEntity();
						entity.setAmount(new BigDecimal(model.getAmount()));
						entity.setCardTransactionId(model.getCardTransactionId());
						entity.setCircle(model.getCircle());
						entity.setOperator(model.getOperator());
						entity.setRechargeType(model.getRechargeType());
						entity.setServiceType(model.getServiceType());
						entity.setSessionId(model.getSessionId());
						entity.setSubscriberId(model.getSubscriberId());
						entity.setStatus("Waiting");
						entity.setTwId("123");
						entity.setDateTime(dateTime);
						entity.setMessage("NA");
						AcqSettingEntity ent = (AcqSettingEntity)session.createCriteria(AcqSettingEntity.class).add(Restrictions.eq("id", Long.valueOf("1"))).uniqueResult();
						entity.setServiceTax(ent.getServiceTax());
						entity.setPaymentMode(model.getPaymentMode());
						tx = session.beginTransaction();
						session.save(entity);
						logger.info("Details Added In Rechrge Table");
						Double newBal = Double.parseDouble(""+deviceMapDevice.getRechargeBal()) - Double.parseDouble(model.getAmount());
						if(entity.getServiceType().equalsIgnoreCase("Mobile")){
						logger.info("Calling Mobile Recharge Api");
						String request = "&to="+entity.getSubscriberId()+"&transId="+""+entity.getId()+"&amount="+""+entity.getAmount()+"&type="+entity.getRechargeType()+"&operator="+entity.getOperator()+"&circle="+entity.getCircle();
						Map<String, String> mobileRecharge = rechargeApi.callRechargeTestApi("http://api.twd.bz/wallet/api/mobile.php",request);
						if(mobileRecharge.get("errCode").equalsIgnoreCase("-1")){
						logger.info("Mobile Recharge Pending");
						RechargeEntity  rechargeEntity = (RechargeEntity) session.createCriteria(RechargeEntity.class).add(Restrictions.eq("id",entity.getId())).uniqueResult();	
						rechargeEntity.setMessage(mobileRecharge.get("msg"));
						rechargeEntity.setStatus("Pending");
						rechargeEntity.setTwId(mobileRecharge.get("txnId"));
						rechargeEntity.setTwStatusCode(mobileRecharge.get("errCode"));
						session.update(rechargeEntity);
						twId = rechargeEntity.getTwId();
						id = rechargeEntity.getId();
						logger.info("Mobile Recharge Details Updated Succesfuly");
						deviceMapDevice.setRechargeBal(""+new DecimalFormat("#.###").format(Double.parseDouble(newBal.toString())));
						session.update(deviceMapDevice);

						JSONObject json = new JSONObject();
						json.put("rechargeStatus", entity.getTwStatusCode());
						json.put("twId", entity.getTwId());
						json.put("rechargeId", entity.getId());
						json.put("rechargeMessage", entity.getMessage());
						//String responseString = "{\"rechargeStatus\":\""+entity.getTwStatusCode()+"\",\"twId\":\""+entity.getTwId()+"\",\"rechargeId\":\""+entity.getId()+"\",\"rechargeMessage\":\""+entity.getMessage()+"\"}";
						response.setStatusCode(EnumStatusConstant.OK.getId());
						response.setStatusMessage(EnumStatusConstant.OK.getDescription());
						response.setBody(json);
						logger.info("Recharge Details Added Succesfuly");
						tx.commit();
						}else{
							logger.info("Mobile Recharge failed");
							JSONObject json = new JSONObject();
							json.put("rechargeStatus", mobileRecharge.get("errCode"));
							json.put("twId", mobileRecharge.get("txnId"));
							json.put("rechargeId", entity.getId());
							json.put("rechargeMessage", mobileRecharge.get("msg"));
							response.setStatusCode(EnumStatusConstant.OK.getId());
							response.setStatusMessage(EnumStatusConstant.OK.getDescription());
							response.setBody(json);
							return response;
						}}else if(entity.getServiceType().equalsIgnoreCase("DTH")){
							String request = "&to="+entity.getSubscriberId()+"&transId="+""+entity.getId()+"&amount="+""+entity.getAmount()+"&type="+entity.getRechargeType()+"&operator="+entity.getOperator();
							Map<String, String> dthRecharge = rechargeApi.callRechargeTestApi("http://api.twd.bz/wallet/api/dth.php",request);
							//Map<String, String> dthRecharge = rechargeApi.callRechargeLiveApi("http://api.twd.bz/wallet/api/dth.php",request);
							//Map<String, String> dthRecharge = rechargeApi.callDthRechargeLive(entity.getSubscriberId(), ""+entity.getId(), ""+entity.getAmount(), entity.getRechargeType(), entity.getOperator());							
							//Map<String, String> dthRecharge = rechargeApi.callDthRecharge(entity.getSubscriberId(), ""+entity.getId(), ""+entity.getAmount(), entity.getRechargeType(), entity.getOperator());
							if(dthRecharge.get("errCode").equalsIgnoreCase("-1")){
							RechargeEntity  rechargeEntity = (RechargeEntity) session.createCriteria(RechargeEntity.class).add(Restrictions.eq("id",entity.getId())).uniqueResult();	
							rechargeEntity.setMessage(dthRecharge.get("msg"));
							rechargeEntity.setStatus("Pending");
							rechargeEntity.setTwId(dthRecharge.get("txnId"));
							rechargeEntity.setTwStatusCode(dthRecharge.get("errCode"));
							session.update(rechargeEntity);
							twId = rechargeEntity.getTwId();
							id = rechargeEntity.getId();
							deviceMapDevice.setRechargeBal(""+new DecimalFormat("#.###").format(Double.parseDouble(newBal.toString())));
							session.update(deviceMapDevice);

							JSONObject json = new JSONObject();
							json.put("rechargeStatus", entity.getTwStatusCode());
							json.put("twId", entity.getTwId());
							json.put("rechargeId", entity.getId());
							json.put("rechargeMessage", entity.getMessage());
							//String responseString = "{\"rechargeStatus\":\""+entity.getTwStatusCode()+"\",\"twId\":\""+entity.getTwId()+"\",\"rechargeId\":\""+entity.getId()+"\",\"rechargeMessage\":\""+entity.getMessage()+"\"}";
							response.setStatusCode(EnumStatusConstant.OK.getId());
							response.setStatusMessage(EnumStatusConstant.OK.getDescription());
							response.setBody(json);
							logger.info("Recharge Details Added Succesfuly");
							tx.commit();
							}else{
								JSONObject json = new JSONObject();
								json.put("rechargeStatus", dthRecharge.get("errCode"));
								json.put("twId", dthRecharge.get("txnId"));
								json.put("rechargeId", entity.getId());
								json.put("rechargeMessage", dthRecharge.get("msg"));
								//String responseString = "{\"rechargeStatus\":\""+entity.getTwStatusCode()+"\",\"twId\":\""+dthRecharge.get("txnId")+"\",\"rechargeId\":\""+entity.getId()+"\",\"rechargeMessage\":\""+dthRecharge.get("msg")+"\"}";
								response.setStatusCode(EnumStatusConstant.OK.getId());
								response.setStatusMessage(EnumStatusConstant.OK.getDescription());
								response.setBody(json);
								return response;
							}
						}else{							
						}
						
						try{
								if(twId!=null&&twId!=""&&id!=null&&id!=""){
									CheckRechargeStatusThread r = new CheckRechargeStatusThread();
									r.setSession(getNewSession());
									r.setRefId(twId);
									r.setId(id);
									Thread th = new Thread(r);	
									th.start();
							}	
						}catch(Exception e){
							logger.error("error to start Check Status thread");
						}
						
				}else{
					response.setStatusCode(EnumStatusConstant.InsufficientBalance.getId());
					response.setStatusMessage(EnumStatusConstant.InsufficientBalance.getDescription());
					response.setBody(null);
					logger.info("No records in mdr report");		
			}}catch(Exception e){
				response.setStatusCode(EnumStatusConstant.RollBackError.getId());
				response.setStatusMessage(EnumStatusConstant.RollBackError.getDescription());
				logger.info("Erore in doRecharge " +e);
			}finally{
				session.close();			
			}
			return response;
		}		

	@Override
	public DbDto2<Object> getMerchantBalance(AcqRechargeMerchantBalModel model) {
		logger.info("request in Merchant Bal Request dao");
		DbDto2<Object> response = new DbDto2<Object>();
		Session session = null;
		try {
			session = getNewSession();
			 AcqMapDeviceUserEntity deviceMapDevice = (AcqMapDeviceUserEntity)session.createCriteria(AcqMapDeviceUserEntity.class).add(Restrictions.eq("userId", Long.valueOf(model.getSessionId()))).uniqueResult();				
					response.setStatusCode(EnumStatusConstant.OK.getId());
					response.setStatusMessage(EnumStatusConstant.OK.getDescription());
					String availBal = ""+new DecimalFormat("#.###").format(Double.parseDouble(deviceMapDevice.getRechargeBal()));
					JSONObject json = new JSONObject();
					json.put("availableBalance", availBal);
					
					//String responseString = "{\"availableBalance\":\""+availBal+"\"}";
					response.setBody(json);
					logger.info("Merchant Bal Request Succesfuly Done");				
			} catch (Exception e) {
				response.setStatusCode(EnumStatusConstant.RollBackError.getId());
				response.setStatusMessage(EnumStatusConstant.RollBackError
						.getDescription());
				logger.info("Error to Merchant Bal Request Dao " + e);
			}			
			
	finally{
			session.close();			
		}
		return response;
	}

	@Override
	public DbDto2<Object> getRechargeStatus(AcqRechargeStatusModel model) {
		logger.info("request in Merchant Bal Request dao");
		DbDto2<Object> response = new DbDto2<Object>();
		Session session = null;
		Transaction tx = null;
		try {
			session = getNewSession();
			RechargeEntity  rechargeEntity = (RechargeEntity) session.createCriteria(RechargeEntity.class).add(Restrictions.eq("sessionId",model.getSessionId())).add(Restrictions.eq("subscriberId",model.getSubscriberId())).add(Restrictions.eq("id",model.getRechargeId())).uniqueResult();	
			if(rechargeEntity==null||rechargeEntity+""==""){
				response.setStatusCode(101);
				response.setStatusMessage("Recharge Txn Not Found");
				response.setBody(null);
				logger.info("Recharge Details Not Found");
				return response; 
			}else{
				if(!rechargeEntity.getTwStatusCode().equals("0")){
					System.out.println("qqqqqqqqqqqqqq");
					String request = "&refId="+rechargeEntity.getId();
					Map<String, String> checkStatus = rechargeApi.callRechargeTestApi("http://api.twd.bz/wallet/api/checkStatus.php",request);
					if(checkStatus.get("errCode").equals("0")){
						rechargeEntity.setStatus("Success");
					}else{
						rechargeEntity.setStatus(checkStatus.get("msg"));
					}
					rechargeEntity.setMessage(checkStatus.get("msg"));
					rechargeEntity.setTwStatusCode(checkStatus.get("errCode"));
					rechargeEntity.setOptId(checkStatus.get("optId"));
					tx = session.beginTransaction();
					session.update(rechargeEntity);
					tx.commit();
					JSONObject json = new JSONObject();
					json.put("rechargeStatus", rechargeEntity.getTwStatusCode());
					json.put("rechargeMessage", rechargeEntity.getMessage());
					json.put("rechargeId", rechargeEntity.getId());
					
					//String responseString = "{\"rechargeStatus\":\""+rechargeEntity.getTwStatusCode()+"\",\"rechargeMessage\":\""+rechargeEntity.getMessage()+"\",\"rechargeId\":\""+rechargeEntity.getId()+"\"}";
					response.setStatusCode(EnumStatusConstant.OK.getId());
					response.setStatusMessage(EnumStatusConstant.OK.getDescription());
					response.setBody(json);
				}else{
					/*rechargeEntity.setStatus("Success");
					rechargeEntity.setTwStatusCode("0");
					rechargeEntity.setMessage("Success");
					tx = session.beginTransaction();
					session.update(rechargeEntity);
					tx.commit();*/
					JSONObject json = new JSONObject();
					json.put("rechargeStatus", rechargeEntity.getTwStatusCode());
					json.put("rechargeMessage", rechargeEntity.getMessage());
					json.put("rechargeId", rechargeEntity.getId());
					response.setStatusCode(EnumStatusConstant.OK.getId());
					response.setStatusMessage(EnumStatusConstant.OK.getDescription());
					response.setBody(json);	
				}
			} }catch (Exception e) {
				response.setStatusCode(EnumStatusConstant.RollBackError.getId());
				response.setStatusMessage(EnumStatusConstant.RollBackError
						.getDescription());
				logger.info("Error to Merchant Bal Request Dao " + e);
			}			
			
	finally{
			session.close();			
		}
		return response;
	}
	
	
	@Override
	public DbDto2<Object> getRechargeList(AcqRechargeMerchantBalModel model) {
		logger.info("request in get RechargeList Request dao");
		DbDto2<Object> response = new DbDto2<Object>();
		Session session = null;
		Transaction tx = null;
		try {
			session = getNewSession();
			List<RechargeEntity> lstTxn = null;
			HashMap<String, String> responseMap = new HashMap<String, String>();
			JSONObject studentsObj = new JSONObject();
			SimpleDateFormat sdfDestination = new SimpleDateFormat("YYYY-MM-dd");
		if(model.getDate()!=null&&model.getDate()!=""&&model.getDate().length()>1){
			SimpleDateFormat sdfSource = new SimpleDateFormat("ddMMyyyy");
			Date fdate =sdfSource.parse(model.getDate());
			String frmDate = sdfDestination.format(fdate);
			String fromDate= frmDate + " 00:00:00";
			String toDate= frmDate + " 23:59:59";	
			System.out.println("222222222::::"+fromDate);
			System.out.println("3333333333::::"+toDate);
			lstTxn = (List<RechargeEntity>) session.createCriteria(RechargeEntity.class).addOrder(Order.desc("id")).add(Restrictions.eq("sessionId",model.getSessionId())).add(Restrictions.between("dateTime",fromDate, toDate)).list();
		}else{
				Date date = new Date();
				String frmDate = sdfDestination.format(date);
				String fromDate= frmDate + " 00:00:00";
				String toDate= frmDate + " 23:59:59";	
				System.out.println("5555555::::"+fromDate);
				System.out.println("66666666666::::"+toDate);
			lstTxn = (List<RechargeEntity>) session.createCriteria(RechargeEntity.class).addOrder(Order.desc("id")).add(Restrictions.eq("sessionId",model.getSessionId())).add(Restrictions.between("dateTime",fromDate, toDate)).list();
				
		}
			Iterator<RechargeEntity> lstTxnItr = lstTxn.iterator();
			System.out.println("lstTxnlstTxn"+lstTxn);
			HashMap<String, String> txnMap = null;
			if (lstTxn.isEmpty()) {
				response.setStatusCode(EnumStatusConstant.NotFound.getId());
				response.setStatusMessage("Recharge Txn Not Found");
				response.setBody(null);
				logger.info("Successfully selected records in RechargeList");
			} else {
				JSONArray jsonArray =  jsonArray = new JSONArray();
				JSONObject rechargeList = new JSONObject();
				while (lstTxnItr.hasNext()) {
					RechargeEntity sngleTxn = (RechargeEntity) lstTxnItr.next();
					//String responseString = "{\"cardTxnId\":\""+sngleTxn.getCardTransactionId()+"\",\"amount\":\""+sngleTxn.getAmount()+"\",\"circle\":\""+sngleTxn.getCircle()+"\",\"dateTime\":\""+sngleTxn.getDateTime()+"\",\"message\":\""+sngleTxn.getMessage()+"\",\"operator\":\""+sngleTxn.getOperator()+"\",\"paymentMode\":\""+sngleTxn.getPaymentMode()+"\"}";
					rechargeList.put("subscriberId", sngleTxn.getSubscriberId());
					rechargeList.put("rechargeId", sngleTxn.getId());
					rechargeList.put("rechargeStatusCode", sngleTxn.getTwStatusCode());
					rechargeList.put("rechargeStatusMessage", sngleTxn.getMessage());
					rechargeList.put("rechargeAmount", sngleTxn.getAmount());
					rechargeList.put("operator", sngleTxn.getOperator());
					rechargeList.put("circle", sngleTxn.getCircle());
					rechargeList.put("rechargeType", sngleTxn.getRechargeType());
					rechargeList.put("serviceType", sngleTxn.getServiceType());
					rechargeList.put("cardTransactionId", sngleTxn.getCardTransactionId());
					jsonArray.put(rechargeList);					
				}
				studentsObj.put("rechargeList", jsonArray);
				    //String jsonStr = studentsObj.toString();
				   // System.out.println("JsonArray::0"+studentsObj);
				response.setStatusCode(EnumStatusConstant.OK.getId());
				response.setStatusMessage(EnumStatusConstant.OK.getDescription());
				response.setBody(studentsObj);
				logger.info("Successfully selected records in RechargeList");			
			}
			
			}catch (Exception e) {
				response.setStatusCode(EnumStatusConstant.RollBackError.getId());
				response.setStatusMessage(EnumStatusConstant.RollBackError
						.getDescription());
				logger.info("Error to get RechargeList Request Dao " + e);
			}			
			
	finally{
			session.close();			
		}
		return response;
	}
	
	
	
	
	
	
}
