package com.acq;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.acq.users.entity.AcqBankItEntity;
import com.acq.users.entity.AcqMapDeviceUserEntity;
import com.acq.users.entity.AcqMerchantEntity;
import com.acq.users.entity.AcqOrganization;
import com.acq.users.entity.AcqSettingEntity;
import com.acq.users.entity.AcqUser;
import com.acq.users.entity.RechargeEntity;
import com.acq.web.controller.model.AcqRechargeModel;
import com.acq.web.dao.AcqDao;
import com.acq.web.dao.impl.CheckRechargeStatusThread;
import com.acq.web.dao.impl.MoneyTransferDao;
import com.acq.web.dto.impl.DbDto2;
import com.google.gson.JsonObject;

public class MoneyTransferApis extends AcqDao {
	final static Logger logger = Logger.getLogger(MoneyTransferDao.class);

// Test Url Details
	/*String url = "http://125.63.96.115:9012/DMR/";
	String apiId = "10039";
	String AgentAuthId = "PAX TECHNOLOGIES PRIVATE LIMITED281649";
	String AgentAuthPassword = "kj74gvlai4";*/

// Live URL Details
	String url = "https://services.bankit.in:8443/DMRV1.1/";
	String apiId = "10039";
	String AgentAuthId = "PAX TECHNOLOGIES PRIVATE LIMITED281649";
	String AgentAuthPassword = "kj74gvlai4";
	
	
	public JSONObject callAddRecipient(String agentCode, String customerId, String bankName, String accountNo, String ifsc, String mobileNo,String recipientName) {			
		URL sendUrl;
		System.out.println("call for Money Transfer Create Recipient:");
		Map<String,String> map =  new HashMap<String,String>(); 
		JSONObject json = new JSONObject();
		try {
			sendUrl = new URL(url+"recipient/add");  
			HttpURLConnection httpConnection = (HttpURLConnection)sendUrl.openConnection();  
			httpConnection.setConnectTimeout(8000);
			httpConnection.setReadTimeout(8000);
			httpConnection.setRequestMethod("POST");
			httpConnection.setDoInput(true);
			httpConnection.setDoOutput(true);
			httpConnection.setUseCaches(false);
			httpConnection.setRequestProperty("Authorization","Basic UEFYIFRFQ0hOT0xPR0lFUyBQUklWQVRFIExJTUlURUQyODE2NDk6a2o3NGd2bGFpNA==");
			httpConnection.setRequestProperty("Content-Type", "application/json");
			httpConnection.setRequestProperty("apiId",apiId);
			httpConnection.setRequestProperty("AgentAuthId",AgentAuthId);
			httpConnection.setRequestProperty("AgentAuthPassword",AgentAuthPassword);
			DataOutputStream dataStreamToServer = new DataOutputStream(httpConnection.getOutputStream());
			dataStreamToServer.writeBytes("{\"agentCode\":\""+agentCode+"\",\n\"customerId\":\""+customerId+"\",\n\"bankName\":\""+bankName+"\",\n\"accountNo\":\""+accountNo+"\",\n\"ifsc\":\""+ifsc+"\",\n\"mobileNo\":\""+mobileNo+"\",\n\"recipientName\":\""+recipientName+"\"\n}");
			//dataStreamToServer.writeBytes("AgentAuthId=sandbox&AgentAuthPassword=test123&agentCode="+agentCode+"&customerId="+customerId+"&bankName="+bankName+"&accountNo="+accountNo+"&ifsc="+ifsc+"&mobileNo="+mobileNo+"&recipientName="+recipientName);
			System.out.println("Request ::"+"AgentAuthId=sandbox&AgentAuthPassword=test123&agentCode="+agentCode+"&customerId="+customerId+"&bankName="+bankName+"&accountNo="+accountNo+"&ifsc="+ifsc+"&mobileNo="+mobileNo+"&recipientName="+recipientName);
			dataStreamToServer.flush();
			dataStreamToServer.close();
			dataStreamToServer.flush();
			dataStreamToServer.close();			
			BufferedReader dataStreamFromUrl = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
			String dataFromUrl = "", dataBuffer = "";
			while ((dataBuffer = dataStreamFromUrl.readLine()) != null) {
				dataFromUrl += dataBuffer;
			}
			System.out.println("Request::"+dataBuffer);
			
			dataStreamFromUrl.close();
			System.out.println("Response::"+dataFromUrl);
			 json = (JSONObject)new JSONParser().parse(dataFromUrl);	
			return json;
		}catch(Exception e){
			map.put("status", "-1");
			map.put("message", "Wrong phone or access token");
			System.out.println("errror "+e);
			return json;
		}
	}
		
	public JSONObject callCreateSender(String agentCode, String customerId, String name, String address, String dateOfBirth, String otp) {			
		URL sendUrl;
		System.out.println("call for Money Transfer Create Sender:");
		Map<String,String> map =  new HashMap<String,String>(); 
		JSONObject json = new JSONObject();
		try {
			sendUrl = new URL(url+"customer/create");  
			HttpURLConnection httpConnection = (HttpURLConnection)sendUrl.openConnection();  
			httpConnection.setConnectTimeout(8000);
			httpConnection.setReadTimeout(8000);
			httpConnection.setRequestMethod("POST");
			httpConnection.setDoInput(true);
			httpConnection.setDoOutput(true);
			httpConnection.setUseCaches(false);
			httpConnection.setRequestProperty("Authorization","Basic UEFYIFRFQ0hOT0xPR0lFUyBQUklWQVRFIExJTUlURUQyODE2NDk6a2o3NGd2bGFpNA==");
			httpConnection.setRequestProperty("Content-Type", "application/json");
			httpConnection.setRequestProperty("apiId",apiId);
			httpConnection.setRequestProperty("AgentAuthId",AgentAuthId);
			httpConnection.setRequestProperty("AgentAuthPassword",AgentAuthPassword);
			DataOutputStream dataStreamToServer = new DataOutputStream(httpConnection.getOutputStream());	
			dataStreamToServer.writeBytes("{\"agentCode\":\""+agentCode+"\",\n\"customerId\":\""+customerId+"\",\n\"name\":\""+name+"\",\n\"address\":\""+address+"\",\n\"dateOfBirth\":\""+dateOfBirth+"\",\n\"otp\":\""+otp+"\"\n}");
			System.out.println("Request ::"+"{\"agentCode\":\""+agentCode+"\",\n\"customerId\":\""+customerId+"\",\n\"name\":\""+name+"\",\n\"address\":\""+address+"\",\n\"dateOfBirth\":\""+dateOfBirth+"\",\n\"otp\":\""+otp+"\"\n}");
			dataStreamToServer.flush();
			dataStreamToServer.close();
			dataStreamToServer.flush();
			dataStreamToServer.close();
			BufferedReader dataStreamFromUrl = new BufferedReader(
					new InputStreamReader(httpConnection.getInputStream()));
			String dataFromUrl = "", dataBuffer = "";
			while ((dataBuffer = dataStreamFromUrl.readLine()) != null) {
				dataFromUrl += dataBuffer;
			}
			System.out.println("Request::"+dataBuffer);		
			dataStreamFromUrl.close();
			System.out.println("Response::"+dataFromUrl);
			 json = (JSONObject)new JSONParser().parse(dataFromUrl);	
			return json;
		}catch(Exception e){
			map.put("status", "-1");
			map.put("message", "Wrong phone or access token");
			System.out.println("errror "+e);
			return json;
		}
	}
	
	public JSONObject callDmrOtpGenerate(String agentCode, String customerId) {	
		URL sendUrl;
		System.out.println("call for Money Transfer Otp Generate");
		Map<String,String> map =  new HashMap<String,String>();  
		JSONObject json = new JSONObject();	
		
		try {
			sendUrl = new URL(url+"generic/otp");  
			HttpURLConnection httpConnection = (HttpURLConnection)sendUrl.openConnection();  
			httpConnection.setConnectTimeout(8000);
			httpConnection.setReadTimeout(8000);
			httpConnection.setRequestMethod("POST");
			httpConnection.setDoInput(true);
			httpConnection.setDoOutput(true);
			httpConnection.setUseCaches(false);
			httpConnection.setRequestProperty("Authorization"," Basic UEFYIFRFQ0hOT0xPR0lFUyBQUklWQVRFIExJTUlURUQyODE2NDk6a2o3NGd2bGFpNA==");
			httpConnection.setRequestProperty("Content-Type", "application/json");
			httpConnection.setRequestProperty("apiId",apiId);
			httpConnection.setRequestProperty("AgentAuthId",AgentAuthId);
			httpConnection.setRequestProperty("AgentAuthPassword",AgentAuthPassword);
			DataOutputStream dataStreamToServer = new DataOutputStream(httpConnection.getOutputStream());
			dataStreamToServer.writeBytes("{\"agentCode\":\""+agentCode+"\",\n\"customerId\":\""+customerId+"\"\n}");
			System.out.println("Header::"+httpConnection+":::Request :"+"{\"agentCode\":\""+agentCode+"\",\n\"customerId\":\""+customerId+"\"\n}");
			dataStreamToServer.flush();
			dataStreamToServer.close();
			dataStreamToServer.flush();
			dataStreamToServer.close();
			BufferedReader dataStreamFromUrl = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
			String dataFromUrl = "", dataBuffer = "";
			while ((dataBuffer = dataStreamFromUrl.readLine()) != null) {
				dataFromUrl += dataBuffer;
			}
			dataStreamFromUrl.close();
			System.out.println("Response::"+dataFromUrl);
			 json = (JSONObject)new JSONParser().parse(dataFromUrl);
			
			return json;
		}catch(IOException ioe){
			map.put("status", "-1");
			map.put("message", "Wrong phone or access token");
			System.out.println("errror "+ioe);
		}catch(Exception e){
			map.put("status", "-1");
			map.put("message", "Wrong phone or access token");
			System.out.println("errror "+e);
		}
		return json;
	}
	
	public Map<String,String> callFetchCustomer(String agentCode, String customerId) {			
		URL sendUrl;
		System.out.println("call for Money Transfer customer fetch");
		Map<String,String> map =  new HashMap<String,String>();  
		try {
			sendUrl = new URL(url+"customer/fetch");  
			HttpURLConnection httpConnection = (HttpURLConnection)sendUrl.openConnection();  
			httpConnection.setConnectTimeout(8000);
			httpConnection.setReadTimeout(8000);
			httpConnection.setRequestMethod("POST");
			httpConnection.setDoInput(true);
			httpConnection.setDoOutput(true);
			httpConnection.setUseCaches(false);
			httpConnection.setRequestProperty("Authorization"," Basic UEFYIFRFQ0hOT0xPR0lFUyBQUklWQVRFIExJTUlURUQyODE2NDk6a2o3NGd2bGFpNA==");
			DataOutputStream dataStreamToServer = new DataOutputStream(httpConnection.getOutputStream());
			dataStreamToServer.writeBytes("AgentAuthId=sandbox&AgentAuthPassword=test123&agentCode="+agentCode+"&customerId="+customerId);
			System.out.println("Request :"+"AgentAuthId=sandbox&AgentAuthPassword=test123&agentCode="+agentCode+"&customerId="+customerId);
			dataStreamToServer.flush();
			dataStreamToServer.close();
			dataStreamToServer.flush();
			dataStreamToServer.close();
			BufferedReader dataStreamFromUrl = new BufferedReader(
					new InputStreamReader(httpConnection.getInputStream()));
			String dataFromUrl = "", dataBuffer = "";
			while ((dataBuffer = dataStreamFromUrl.readLine()) != null) {
				dataFromUrl += dataBuffer;
			}
			dataStreamFromUrl.close();
			System.out.println("Response::"+dataFromUrl);
			JSONObject json = (JSONObject)new JSONParser().parse(dataFromUrl);	
			map.put("txnDateTime",""+json.get("ts"));
			map.put("clientId",""+json.get("clientId"));	
			map.put("txnId",""+json.get("txnId"));	
			map.put("errCode",""+json.get("errCode"));	
			map.put("msg",""+json.get("errMsg"));	
			map.put("optId",""+json.get("optId"));	
			return map;
		}catch(IOException ioe){
			map.put("status", "-1");
			map.put("message", "Wrong phone or access token");
			return map;
		}catch(Exception e){
			map.put("status", "-1");
			map.put("message", "Wrong phone or access token");
			System.out.println("errror "+e);
			return map;
		}
	}
	
	public JSONObject callFetchSingleRecipient(String agentCode,String recipientId, String customerId) {			
		URL sendUrl;
		System.out.println("call for Money Transfer single customer fetch::recipientId"+recipientId+"customerId::"+customerId);
		JSONObject json = new JSONObject();
		try {
			sendUrl = new URL(url+"recipient/fetch");  
			HttpURLConnection httpConnection = (HttpURLConnection)sendUrl.openConnection();  
			httpConnection.setConnectTimeout(8000);
			httpConnection.setReadTimeout(8000);
			httpConnection.setRequestMethod("POST");
			httpConnection.setDoInput(true);
			httpConnection.setDoOutput(true);
			httpConnection.setUseCaches(false);
			httpConnection.setRequestProperty("Authorization"," Basic UEFYIFRFQ0hOT0xPR0lFUyBQUklWQVRFIExJTUlURUQyODE2NDk6a2o3NGd2bGFpNA==");
			httpConnection.setRequestProperty("Content-Type", "application/json");
			httpConnection.setRequestProperty("apiId",apiId);
			httpConnection.setRequestProperty("AgentAuthId",AgentAuthId);
			httpConnection.setRequestProperty("AgentAuthPassword",AgentAuthPassword);
			DataOutputStream dataStreamToServer = new DataOutputStream(httpConnection.getOutputStream());
			dataStreamToServer.writeBytes("{\"agentCode\":\""+agentCode+"\",\n\"customerId\":\""+customerId+"\",\n\"recipientId\":\""+recipientId+"\"\n}");
			//dataStreamToServer.writeBytes("AgentAuthId=sandbox&AgentAuthPassword=test123&agentCode="+agentCode+"&customerId="+customerId);
			System.out.println("Request :"+"{\"customerId\":\""+customerId+"\",\n\"recipientId\":\""+recipientId+"\"\n}");
			dataStreamToServer.flush();
			dataStreamToServer.close();
			dataStreamToServer.flush();
			dataStreamToServer.close();
			BufferedReader dataStreamFromUrl = new BufferedReader(
					new InputStreamReader(httpConnection.getInputStream()));
			String dataFromUrl = "", dataBuffer = "";
			while ((dataBuffer = dataStreamFromUrl.readLine()) != null) {
				dataFromUrl += dataBuffer;
			}
			dataStreamFromUrl.close();
			json = (JSONObject)new JSONParser().parse(dataFromUrl);	
			System.out.println("Response::"+json);			
			return json;
		}catch(IOException ioe){
			/*map.put("status", "-1");
			map.put("message", "Wrong phone or access token");*/
			return json;
		}catch(Exception e){
			/*map.put("status", "-1");
			map.put("message", "Wrong phone or access token");*/
			System.out.println("errror "+e);
			return json;
		}
	}
	
	
	public JSONObject callFetchAllRecipient(String agentCode, String customerId) {			
		URL sendUrl;
		System.out.println("call for Money Transfer customer fetch");
		JSONObject json = new JSONObject();
		try {
			sendUrl = new URL(url+"recipient/fetchAll");  
			HttpURLConnection httpConnection = (HttpURLConnection)sendUrl.openConnection();  
			httpConnection.setConnectTimeout(8000);
			httpConnection.setReadTimeout(8000);
			httpConnection.setRequestMethod("POST");
			httpConnection.setDoInput(true);
			httpConnection.setDoOutput(true);
			httpConnection.setUseCaches(false);
			httpConnection.setRequestProperty("Authorization"," Basic UEFYIFRFQ0hOT0xPR0lFUyBQUklWQVRFIExJTUlURUQyODE2NDk6a2o3NGd2bGFpNA==");
			httpConnection.setRequestProperty("Content-Type", "application/json");
			httpConnection.setRequestProperty("apiId",apiId);
			httpConnection.setRequestProperty("AgentAuthId",AgentAuthId);
			httpConnection.setRequestProperty("AgentAuthPassword",AgentAuthPassword);
			DataOutputStream dataStreamToServer = new DataOutputStream(httpConnection.getOutputStream());
			dataStreamToServer.writeBytes("{\"agentCode\":\""+agentCode+"\",\n\"customerId\":\""+customerId+"\"\n}");
			System.out.println("Abhishek : Fetch bene : Printing Request : "+ "{\"agentCode\":\""+agentCode+"\",\n\"customerId\":\""+customerId+"\"\n}");
			//dataStreamToServer.writeBytes("AgentAuthId=sandbox&AgentAuthPassword=test123&agentCode="+agentCode+"&customerId="+customerId);
			System.out.println("Request :"+"AgentAuthId=sandbox&AgentAuthPassword=test123&agentCode="+agentCode+"&customerId="+customerId);
			dataStreamToServer.flush();
			dataStreamToServer.close();
			dataStreamToServer.flush();
			dataStreamToServer.close();
			BufferedReader dataStreamFromUrl = new BufferedReader(
					new InputStreamReader(httpConnection.getInputStream()));
			String dataFromUrl = "", dataBuffer = "";
			while ((dataBuffer = dataStreamFromUrl.readLine()) != null) {
				dataFromUrl += dataBuffer;
			}
			dataStreamFromUrl.close();
			json = (JSONObject)new JSONParser().parse(dataFromUrl);	
			System.out.println("Response::"+json);			
			return json;
		}catch(IOException ioe){
			/*map.put("status", "-1");
			map.put("message", "Wrong phone or access token");*/
			return json;
		}catch(Exception e){
			/*map.put("status", "-1");
			map.put("message", "Wrong phone or access token");*/
			System.out.println("errror "+e);
			return json;
		}
	}
	
	public JSONObject callDmtNeft(String agentCode, String recipientId, String customerId, String amount,String sessionId) {			
		URL sendUrl;
		System.out.println("call for dmt neft::recipientId::00"+recipientId+";;;customerId:::0"+customerId+"::::Amount::"+amount);
		Map<String,String> map =  new HashMap<String,String>();  
		JSONObject json = new JSONObject();
		try {
			sendUrl = new URL(url+"transact/NEFT/remit");  
			HttpURLConnection httpConnection = (HttpURLConnection)sendUrl.openConnection();  
			httpConnection.setConnectTimeout(8000);
			httpConnection.setReadTimeout(8000);
			httpConnection.setRequestMethod("POST");
			httpConnection.setDoInput(true);
			httpConnection.setDoOutput(true);
			httpConnection.setUseCaches(false);
			httpConnection.setRequestProperty("Authorization"," Basic UEFYIFRFQ0hOT0xPR0lFUyBQUklWQVRFIExJTUlURUQyODE2NDk6a2o3NGd2bGFpNA==");
			httpConnection.setRequestProperty("Content-Type", "application/json");
			httpConnection.setRequestProperty("apiId",apiId);
			httpConnection.setRequestProperty("AgentAuthId",AgentAuthId);
			httpConnection.setRequestProperty("AgentAuthPassword",AgentAuthPassword);
			String clientRefId = "10039"+OtpGenerator.RandomNumberGenerator().substring(0, OtpGenerator.RandomNumberGenerator().length() - sessionId.length())+sessionId;
			DataOutputStream dataStreamToServer = new DataOutputStream(httpConnection.getOutputStream());	
			dataStreamToServer.writeBytes("{\"agentCode\":\""+agentCode+"\",\n\"customerId\":\""+customerId+"\",\n\"recipientId\":\""+recipientId+"\",\n\"amount\":\""+amount+"\",\n\"clientRefId\":\""+clientRefId+"\"\n}");
			System.out.println("Headr::"+httpConnection.getOutputStream());
			System.out.println("Header::"+httpConnection+"Request:::"+"{\"agentCode\":\""+agentCode+"\",\n\"customerId\":\""+customerId+"\",\n\"recipientId\":\""+recipientId+"\",\n\"amount\":\""+amount+"\",\n\"clientRefId\":\""+clientRefId+"\"\n}");
			//dataStreamToServer.writeBytes("to="+to+"&memberId=TW1033&passwd=3FbwBrS5&transId="+transId+"&amount="+amount+"&type="+type+"&operator="+operator+"&circle=51");
			dataStreamToServer.flush();
			dataStreamToServer.close();
			dataStreamToServer.flush();
			dataStreamToServer.close();
			BufferedReader dataStreamFromUrl = new BufferedReader(
					new InputStreamReader(httpConnection.getInputStream()));
			String dataFromUrl = "", dataBuffer = "";
			while ((dataBuffer = dataStreamFromUrl.readLine()) != null) {
				dataFromUrl += dataBuffer;
			}
			dataStreamFromUrl.close();
			System.out.println("Response::"+dataFromUrl);
			//json = (JSONObject)new JSONParser().parse(dataFromUrl);	
			 json = new JSONObject();
			JSONObject json1 = new JSONObject();
			json1.put("customerId", customerId);
			json1.put("name", "dpk");
			json1.put("clientRefId", clientRefId);
			json1.put("txnId", "46346446456");
			json.put("errorMsg", "SUCCESS");
			json.put("errorCode", "00");
			json.put("data", json1);
				
			return json;
		}catch(Exception e){
			map.put("status", "-1");
			map.put("message", "Wrong phone or access token");
			System.out.println("errror "+e);
			return json;
		}
	}
	
	public JSONObject callDmtImps(String agentCode, String recipientId, String customerId, String amount, String sessionId) {			
		URL sendUrl;
		System.out.println("call for dmt imps::recipientId::00"+recipientId+";;;customerId:::0"+customerId+"::::Amount::"+amount);
		JSONObject json = new JSONObject();
		Map<String,String> map =  new HashMap<String,String>();  
		try {
			sendUrl = new URL(url+"transact/IMPS/remit");  
			HttpURLConnection httpConnection = (HttpURLConnection)sendUrl.openConnection();  
			httpConnection.setConnectTimeout(8000);
			httpConnection.setReadTimeout(8000);
			httpConnection.setRequestMethod("POST");
			httpConnection.setDoInput(true);
			httpConnection.setDoOutput(true);
			httpConnection.setUseCaches(false);
			httpConnection.setRequestProperty("Authorization"," Basic UEFYIFRFQ0hOT0xPR0lFUyBQUklWQVRFIExJTUlURUQyODE2NDk6a2o3NGd2bGFpNA==");
			httpConnection.setRequestProperty("Content-Type", "application/json");
			httpConnection.setRequestProperty("apiId",apiId);
			httpConnection.setRequestProperty("AgentAuthId",AgentAuthId);
			httpConnection.setRequestProperty("AgentAuthPassword",AgentAuthPassword);
			String clientRefId = "10039"+OtpGenerator.RandomNumberGenerator().substring(0, OtpGenerator.RandomNumberGenerator().length() - sessionId.length())+sessionId;
			DataOutputStream dataStreamToServer = new DataOutputStream(httpConnection.getOutputStream());	
			dataStreamToServer.writeBytes("{\"agentCode\":\""+agentCode+"\",\n\"customerId\":\""+customerId+"\",\n\"recipientId\":\""+recipientId+"\",\n\"amount\":\""+amount+"\",\n\"clientRefId\":\""+clientRefId+"\"\n}");
			System.out.println("Request:::"+"{\"agentCode\":\""+agentCode+"\",\n\"customerId\":\""+customerId+"\",\n\"recipientId\":\""+recipientId+"\",\n\"amount\":\""+amount+"\",\n\"clientRefId\":\""+clientRefId+"\"\n}");
			//dataStreamToServer.writeBytes("to="+to+"&memberId=TW1033&passwd=3FbwBrS5&transId="+transId+"&amount="+amount+"&type="+type+"&operator="+operator+"&circle=51");
			dataStreamToServer.flush();
			dataStreamToServer.close();
			dataStreamToServer.flush();
			dataStreamToServer.close();
			
			BufferedReader dataStreamFromUrl = new BufferedReader(
					new InputStreamReader(httpConnection.getInputStream()));
			String dataFromUrl = "", dataBuffer = "";
			while ((dataBuffer = dataStreamFromUrl.readLine()) != null) {
				dataFromUrl += dataBuffer;
			}
			System.out.println("Request::"+dataBuffer);
			
			dataStreamFromUrl.close();
			System.out.println("Response::"+dataFromUrl);
			//json = (JSONObject)new JSONParser().parse(dataFromUrl);	
			 json = new JSONObject();
				JSONObject json1 = new JSONObject();
				json1.put("customerId", customerId);
				json1.put("name", "dpk");
				json1.put("clientRefId", clientRefId);
				json1.put("txnId", "46346446456");
				json.put("errorMsg", "SUCCESS");
				json.put("errorCode", "00");
				json.put("data", json1);	
			return json;
		}catch(Exception e){
			map.put("status", "-1");
			map.put("message", "Wrong phone or access token");
			System.out.println("errror "+e);
			return json;
		}
	}
	
	public JSONObject callDeleteBeneficiary(String agentCode, String customerId, String recipientId) {			
		URL sendUrl;
		System.out.println("call for Delete Beneficiary");
		JSONObject json = new JSONObject();
		Map<String,String> map =  new HashMap<String,String>();  
		try {
			sendUrl = new URL(url+"recipient/delete");  
			HttpURLConnection httpConnection = (HttpURLConnection)sendUrl.openConnection();  
			httpConnection.setConnectTimeout(8000);
			httpConnection.setReadTimeout(8000);
			httpConnection.setRequestMethod("POST");
			httpConnection.setDoInput(true);
			httpConnection.setDoOutput(true);
			httpConnection.setUseCaches(false);
			httpConnection.setRequestProperty("Authorization"," Basic UEFYIFRFQ0hOT0xPR0lFUyBQUklWQVRFIExJTUlURUQyODE2NDk6a2o3NGd2bGFpNA==");
			httpConnection.setRequestProperty("Content-Type", "application/json");
			httpConnection.setRequestProperty("apiId",apiId);
			httpConnection.setRequestProperty("AgentAuthId",AgentAuthId);
			httpConnection.setRequestProperty("AgentAuthPassword",AgentAuthPassword);
			DataOutputStream dataStreamToServer = new DataOutputStream(httpConnection.getOutputStream());
			dataStreamToServer.writeBytes("{\"agentCode\":\""+agentCode+"\",\n\"customerId\":\""+customerId+"\",\n\"recipientId\":\""+recipientId+"\"\n}");
			System.out.println("Request::"+dataStreamToServer);
			dataStreamToServer.flush();
			dataStreamToServer.close();
			dataStreamToServer.flush();
			dataStreamToServer.close();
			BufferedReader dataStreamFromUrl = new BufferedReader(
					new InputStreamReader(httpConnection.getInputStream()));
			String dataFromUrl = "", dataBuffer = "";
			while ((dataBuffer = dataStreamFromUrl.readLine()) != null) {
				dataFromUrl += dataBuffer;
			}
			dataStreamFromUrl.close();
			System.out.println("Response::"+dataFromUrl);

			json = (JSONObject)new JSONParser().parse(dataFromUrl);	
			map.put("errCode",""+json.get("errorCode"));	
			map.put("msg",""+json.get("errorMsg"));		
			return json;
		}catch(IOException ioe){
			map.put("status", "-1");
			map.put("message", "Wrong phone or access token");
			return json;
		}catch(Exception e){
			map.put("status", "-1");
			map.put("message", "Wrong phone or access token");
			System.out.println("errror "+e);
			return json;
		}
	}
	
	public JSONObject callBankList() {			
		URL sendUrl;
		JSONObject json = new JSONObject();
		System.out.println("call for Check bank List");
		Map<String,String> map =  new HashMap<String,String>();  
		try {
			sendUrl = new URL(url+"generic/bankList");  
			HttpURLConnection httpConnection = (HttpURLConnection)sendUrl.openConnection();  
			httpConnection.setConnectTimeout(8000);
			httpConnection.setReadTimeout(8000);
			httpConnection.setRequestMethod("GET");
			httpConnection.setDoInput(true);
			httpConnection.setDoOutput(true);
			httpConnection.setUseCaches(false);
			httpConnection.setRequestProperty("Authorization"," Basic UEFYIFRFQ0hOT0xPR0lFUyBQUklWQVRFIExJTUlURUQyODE2NDk6a2o3NGd2bGFpNA==");
			httpConnection.setRequestProperty("Content-Type", "application/json");
			httpConnection.setRequestProperty("apiId",apiId);
			httpConnection.setRequestProperty("AgentAuthId",AgentAuthId);
			httpConnection.setRequestProperty("AgentAuthPassword",AgentAuthPassword);
			DataOutputStream dataStreamToServer = new DataOutputStream(httpConnection.getOutputStream());
			//dataStreamToServer.writeBytes("memberId=TW1033&passwd=3FbwBrS5&refId="+refId);
			//System.out.println("Request::"+"memberId=TW1033&passwd=3FbwBrS5&refId="+refId);
			dataStreamToServer.flush();
			dataStreamToServer.close();
			dataStreamToServer.flush();
			dataStreamToServer.close();
			BufferedReader dataStreamFromUrl = new BufferedReader(
					new InputStreamReader(httpConnection.getInputStream()));
			String dataFromUrl = "", dataBuffer = "";
			while ((dataBuffer = dataStreamFromUrl.readLine()) != null) {
				dataFromUrl += dataBuffer;
			}
			System.out.println("Response::::"+dataFromUrl);
			dataStreamFromUrl.close();
			 json = (JSONObject)new JSONParser().parse(dataFromUrl);	
			map.put("data",""+json.get("data"));	
			map.put("errCode",""+json.get("errorCode"));	
			map.put("msg",""+json.get("errorMsg"));	
			return json;
		}catch(IOException ioe){
			map.put("status", "-1");
			map.put("message", "Wrong phone or access token");
			return json;
		}catch(Exception e){
			map.put("status", "-1");
			map.put("message", "Wrong phone or access token");
			System.out.println("errror "+e);
			return json;
		}
	}
	
	public JSONObject callCheckStatus(String clientRefId) {			
		URL sendUrl;
		System.out.println("call for Check status");
		Map<String,String> map =  new HashMap<String,String>();  
		JSONObject json = new JSONObject();
		try {
			sendUrl = new URL(url+"transact/searchtxn");  
			HttpURLConnection httpConnection = (HttpURLConnection)sendUrl.openConnection();  
			httpConnection.setConnectTimeout(8000);
			httpConnection.setReadTimeout(8000);
			httpConnection.setRequestMethod("POST");
			httpConnection.setDoInput(true);
			httpConnection.setDoOutput(true);
			httpConnection.setUseCaches(false);
			httpConnection.setRequestProperty("Authorization"," Basic UEFYIFRFQ0hOT0xPR0lFUyBQUklWQVRFIExJTUlURUQyODE2NDk6a2o3NGd2bGFpNA==");
			httpConnection.setRequestProperty("Content-Type", "application/json");
			httpConnection.setRequestProperty("apiId",apiId);
			httpConnection.setRequestProperty("AgentAuthId",AgentAuthId);
			httpConnection.setRequestProperty("AgentAuthPassword",AgentAuthPassword);
			DataOutputStream dataStreamToServer = new DataOutputStream(httpConnection.getOutputStream());
			dataStreamToServer.writeBytes("{\"clientRefId\":\""+clientRefId+"\"\n}");
			//System.out.println("Request::"+"memberId=TW1033&passwd=3FbwBrS5&refId="+refId);
			dataStreamToServer.flush();
			dataStreamToServer.close();
			dataStreamToServer.flush();
			dataStreamToServer.close();
			BufferedReader dataStreamFromUrl = new BufferedReader(
					new InputStreamReader(httpConnection.getInputStream()));
			String dataFromUrl = "", dataBuffer = "";
			while ((dataBuffer = dataStreamFromUrl.readLine()) != null) {
				dataFromUrl += dataBuffer;
			}
			System.out.println("Response::::"+dataFromUrl);
			dataStreamFromUrl.close();
			 json = (JSONObject)new JSONParser().parse(dataFromUrl);	
			map.put("data",""+json.get("data"));	
			map.put("errCode",""+json.get("errorCode"));	
			map.put("msg",""+json.get("errorMsg"));	
			return json;
		}catch(IOException ioe){
			map.put("status", "-1");
			map.put("message", "Wrong phone or access token");
			return json;
		}catch(Exception e){
			map.put("status", "-1");
			map.put("message", "Wrong phone or access token");
			System.out.println("errror "+e);
			return json;
		}
	}
	
	public JSONObject callCardLoadApi(String cardNumber, String amount, String mobile, String transactionId) {			
		URL sendUrl;
		System.out.println("call for Card Load Api");
		Map<String,String> map =  new HashMap<String,String>();  
		JSONObject json = new JSONObject();
		try {
			sendUrl = new URL("https://portal.bankit.in:9090/BANKITMRA/resources/AESPPC/InstitutionLoadCard");  
			HttpURLConnection httpConnection = (HttpURLConnection)sendUrl.openConnection();  
			httpConnection.setConnectTimeout(8000);
			httpConnection.setReadTimeout(8000);
			httpConnection.setRequestMethod("POST");
			httpConnection.setDoInput(true);
			httpConnection.setDoOutput(true);
			httpConnection.setUseCaches(false);
			httpConnection.setRequestProperty("Authorization"," Basic UEFYIFRFQ0hOT0xPR0lFUyBQUklWQVRFIExJTUlURUQyODE2NDk6a2o3NGd2bGFpNA==");
			httpConnection.setRequestProperty("Content-Type", "application/json");
			/*httpConnection.setRequestProperty("apiId","10039");
			httpConnection.setRequestProperty("AgentAuthId","PAX TECHNOLOGIES PRIVATE LIMITED281649");
			httpConnection.setRequestProperty("AgentAuthPassword","kj74gvlai4");
			httpConnection.setRequestProperty("InstitutionKey","8860180472AG21961D4019");
			httpConnection.setRequestProperty("AgentID","21961");*/
			DataOutputStream dataStreamToServer = new DataOutputStream(httpConnection.getOutputStream());
			dataStreamToServer.writeBytes("{\"cardNumber\":\""+cardNumber+"\",\n\"amount\":\""+amount+"\",\n\"mobile\":\""+mobile+"\",\n\"transactionId\":\""+transactionId+"\",\n\"InstitutionKey\":\""+"8860180472AG21961D4019"+"\",\n\"AgentID\":\""+"21961"+"\"\n}");
			System.out.println("Headr::"+httpConnection.getOutputStream());
			System.out.println("Header::"+httpConnection+":::Request::"+"{\"cardNumber\":\""+cardNumber+"\",\n\"amount\":\""+amount+"\",\n\"mobile\":\""+mobile+"\",\n\"transactionId\":\""+transactionId+"\",\n\"InstitutionKey\":\""+"8860180472AG21961D4019"+"\",\n\"AgentID\":\""+"21961"+"\"\n}");
			dataStreamToServer.flush();
			dataStreamToServer.close();
			dataStreamToServer.flush();
			dataStreamToServer.close();
			BufferedReader dataStreamFromUrl = new BufferedReader(
					new InputStreamReader(httpConnection.getInputStream()));
			String dataFromUrl = "", dataBuffer = "";
			while ((dataBuffer = dataStreamFromUrl.readLine()) != null) {
				dataFromUrl += dataBuffer;
			}
			System.out.println("Response::::"+dataFromUrl);
			dataStreamFromUrl.close();
			 json = (JSONObject)new JSONParser().parse(dataFromUrl);	
			map.put("data",""+json.get("data"));	
			map.put("errCode",""+json.get("errorCode"));	
			map.put("msg",""+json.get("errorMsg"));	
			return json;
		}catch(IOException ioe){
			map.put("status", "-1");
			map.put("message", "Wrong phone or access token");
			return json;
		}catch(Exception e){
			map.put("status", "-1");
			map.put("message", "Wrong phone or access token");
			System.out.println("errror "+e);
			return json;
		}
	}
	
	public JSONObject callCardLoadStsusApi(String transactionId) {			
		URL sendUrl;
		System.out.println("call for Card Load Api");
		Map<String,String> map =  new HashMap<String,String>();  
		JSONObject json = new JSONObject();
		try {
			sendUrl = new URL("https://portal.bankit.in:9090/BANKITMRA/resources/AESPPC/InstitutionLoadCardStatus");  
			HttpURLConnection httpConnection = (HttpURLConnection)sendUrl.openConnection();  
			httpConnection.setConnectTimeout(8000);
			httpConnection.setReadTimeout(8000);
			httpConnection.setRequestMethod("POST");
			httpConnection.setDoInput(true);
			httpConnection.setDoOutput(true);
			httpConnection.setUseCaches(false);
			httpConnection.setRequestProperty("Authorization"," Basic UEFYIFRFQ0hOT0xPR0lFUyBQUklWQVRFIExJTUlURUQyODE2NDk6a2o3NGd2bGFpNA==");
			httpConnection.setRequestProperty("Content-Type", "application/json");
			/*httpConnection.setRequestProperty("apiId","10039");
			httpConnection.setRequestProperty("AgentAuthId","PAX TECHNOLOGIES PRIVATE LIMITED281649");
			httpConnection.setRequestProperty("AgentAuthPassword","kj74gvlai4");
			httpConnection.setRequestProperty("InstitutionKey","8860180472AG21961D4019");
			httpConnection.setRequestProperty("AgentID","21961");*/
			DataOutputStream dataStreamToServer = new DataOutputStream(httpConnection.getOutputStream());
			dataStreamToServer.writeBytes("{\"transactionId\":\""+transactionId+"\",\n\"InstitutionKey\":\""+"8860180472AG21961D4019"+"\",\n\"AgentID\":\""+"21961"+"\"\n}");
			System.out.println("Headr::"+httpConnection.getOutputStream());
			
			System.out.println("Header::"+httpConnection+":::Request::"+"{\"transactionId\":\""+transactionId+"\",\n\"InstitutionKey\":\""+"8860180472AG21961D4019"+"\",\n\"AgentID\":\""+"21961"+"\"\n}");
			dataStreamToServer.flush();
			dataStreamToServer.close();
			dataStreamToServer.flush();
			dataStreamToServer.close();
			BufferedReader dataStreamFromUrl = new BufferedReader(
					new InputStreamReader(httpConnection.getInputStream()));
			String dataFromUrl = "", dataBuffer = "";
			while ((dataBuffer = dataStreamFromUrl.readLine()) != null) {
				dataFromUrl += dataBuffer;
			}
			System.out.println("Response::::"+dataFromUrl);
			dataStreamFromUrl.close();
			 json = (JSONObject)new JSONParser().parse(dataFromUrl);	
			map.put("data",""+json.get("data"));	
			map.put("errCode",""+json.get("errorCode"));	
			map.put("msg",""+json.get("errorMsg"));	
			return json;
		}catch(IOException ioe){
			map.put("status", "-1");
			map.put("message", "Wrong phone or access token");
			return json;
		}catch(Exception e){
			map.put("status", "-1");
			map.put("message", "Wrong phone or access token");
			System.out.println("errror "+e);
			return json;
		}
	}
	public JSONObject callCardLoadBalApi(String cardNumber,String mobileNumber) {			
		URL sendUrl;
		System.out.println("call for Card Load Bal Api");
		Map<String,String> map =  new HashMap<String,String>();  
		JSONObject json = new JSONObject();
		try {
			sendUrl = new URL("https://portal.bankit.in:9090/BANKITMRA/resources/AESPPC/InstitutionCardBalance");  
			HttpURLConnection httpConnection = (HttpURLConnection)sendUrl.openConnection();  
			httpConnection.setConnectTimeout(8000);
			httpConnection.setReadTimeout(8000);
			httpConnection.setRequestMethod("POST");
			httpConnection.setDoInput(true);
			httpConnection.setDoOutput(true);
			httpConnection.setUseCaches(false);
			httpConnection.setRequestProperty("Authorization"," Basic UEFYIFRFQ0hOT0xPR0lFUyBQUklWQVRFIExJTUlURUQyODE2NDk6a2o3NGd2bGFpNA==");
			httpConnection.setRequestProperty("Content-Type", "application/json");
			/*httpConnection.setRequestProperty("apiId","10039");
			httpConnection.setRequestProperty("AgentAuthId","PAX TECHNOLOGIES PRIVATE LIMITED281649");
			httpConnection.setRequestProperty("AgentAuthPassword","kj74gvlai4");
			httpConnection.setRequestProperty("InstitutionKey","8860180472AG21961D4019");
			httpConnection.setRequestProperty("AgentID","21961");*/
			DataOutputStream dataStreamToServer = new DataOutputStream(httpConnection.getOutputStream());
			dataStreamToServer.writeBytes("{\"cardNumber\":\""+cardNumber+"\",\n\"mobile\":\""+mobileNumber+"\",\n\"InstitutionKey\":\""+"8860180472AG21961D4019"+"\",\n\"AgentID\":\""+"21961"+"\"\n}");
			System.out.println("Headr::"+httpConnection.getOutputStream());
			
			System.out.println("Header::"+httpConnection+":::Request::"+"{\"cardNumber\":\""+cardNumber+"\",\n\"mobile\":\""+mobileNumber+"\",\n\"InstitutionKey\":\""+"8860180472AG21961D4019"+"\",\n\"AgentID\":\""+"21961"+"\"\n}");
			dataStreamToServer.flush();
			dataStreamToServer.close();
			dataStreamToServer.flush();
			dataStreamToServer.close();
			BufferedReader dataStreamFromUrl = new BufferedReader(
					new InputStreamReader(httpConnection.getInputStream()));
			String dataFromUrl = "", dataBuffer = "";
			while ((dataBuffer = dataStreamFromUrl.readLine()) != null) {
				dataFromUrl += dataBuffer;
			}
			System.out.println("Response::::"+dataFromUrl);
			dataStreamFromUrl.close();
			 json = (JSONObject)new JSONParser().parse(dataFromUrl);	
			map.put("data",""+json.get("data"));	
			map.put("errCode",""+json.get("errorCode"));	
			map.put("msg",""+json.get("errorMsg"));	
			return json;
		}catch(IOException ioe){
			map.put("status", "-1");
			map.put("message", "Wrong phone or access token");
			return json;
		}catch(Exception e){
			map.put("status", "-1");
			map.put("message", "Wrong phone or access token");
			System.out.println("errror "+e);
			return json;
		}
	}
}
