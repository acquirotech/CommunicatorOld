package com.acq.web.dao.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.acq.RechargeApis;
import com.acq.users.entity.AcqDevice;
import com.acq.users.entity.AcqPlaceTransactionEntity;
import com.acq.users.entity.AcqRiskManagement;
import com.acq.users.entity.RechargeEntity;

@Repository
public class CheckRechargeStatusThread implements Runnable {
	 private String refId;
	 private Session session;
	 private String id;
	 
	 final static Logger logger = Logger.getLogger(CheckRechargeStatusThread.class);
	
	 RechargeApis  rechargeApi = new RechargeApis();

	 public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Session getSession() {
			return session;
		}

		public void setSession(Session session) {
			this.session = session;
		}
	public String getRefId() {
		return refId;
	}
	public void setRefId(String refId) {
		this.refId = refId;
	}
	public CheckRechargeStatusThread(){}
	
	public void run(){
		try{
			logger.info("custom thread started");
			if(refId!=null||refId+""!=""){
				String request = "&refId="+id;
				Map<String, String> checkStatus = rechargeApi.callRechargeTestApi("http://api.twd.bz/wallet/api/checkStatus.php",request);
				RechargeEntity  rechargeEntity = (RechargeEntity) session.createCriteria(RechargeEntity.class).add(Restrictions.eq("id",id)).uniqueResult();	
				if(checkStatus.get("errCode").equals("0")){
					rechargeEntity.setStatus("Success");
				}else{
					rechargeEntity.setStatus(checkStatus.get("msg"));
				}
				rechargeEntity.setMessage(checkStatus.get("msg"));
				rechargeEntity.setTwStatusCode(checkStatus.get("errCode"));
				rechargeEntity.setOptId(checkStatus.get("optId"));
				session.update(rechargeEntity);
					Transaction tx = session.beginTransaction();
					session.update(rechargeEntity);
					tx.commit();
					logger.info("Status thread is completed");
				}
			 else{
				 logger.info("Ref Id Not Found");
			 }
		  }catch(Exception e){
			  logger.error("error to execute risk thread: "+e);
		  }finally{
			  session.close();
		  } 
		
	
	 }
	
	}
