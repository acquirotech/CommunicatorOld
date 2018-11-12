package com.acq;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class RechargeApis {
	
	// For Test 
	/*String userPassword = "memberId=sandbox&passwd=test123";
	String authrization = " Basic 77de68daecd823babbb58edb1c8e14d7106e83bb";*/

	// For Live
	String userPassword = "memberId=TW1033&passwd=3FbwBrS5";		
	String authrization = " Basic af3e133428b9e25c55bc59fe534248e6a0c0f17b";
	
	public Map<String,String> callRechargeTestApi(String url, String request) {			
		URL sendUrl;
		System.out.println("call for Test Recharge::url::00"+url+";;;request:::0"+request);
		Map<String,String> map =  new HashMap<String,String>();  
		try {
			sendUrl = new URL(url);  
			HttpURLConnection httpConnection = (HttpURLConnection)sendUrl.openConnection();  
			httpConnection.setConnectTimeout(8000);
			httpConnection.setReadTimeout(8000);
			httpConnection.setRequestMethod("POST");
			httpConnection.setDoInput(true);
			httpConnection.setDoOutput(true);
			httpConnection.setUseCaches(false);
			httpConnection.setRequestProperty("Authorization",authrization);
			DataOutputStream dataStreamToServer = new DataOutputStream(httpConnection.getOutputStream());					  
			dataStreamToServer.writeBytes(userPassword+request);
			System.out.println("Request ::"+userPassword+request);
			dataStreamToServer.flush();
			dataStreamToServer.close();
			dataStreamToServer.flush();
			dataStreamToServer.close();
			
			BufferedReader dataStreamFromUrl = new BufferedReader(
					new InputStreamReader(httpConnection.getInputStream()));
			String dataFromUrl = "", dataBuffer = "";
			while ((dat4aBuffer = dataStreamFromUrl.readLine()) != null) {
				dataFromUrl += dataBuffer;
			}			
			dataStreamFromUrl.close();
			System.out.println("Response::"+dataFromUrl);
			JSONObject json = (JSONObject)new JSONParser().parse(dataFromUrl);	
			if(request.contains("refId")){
				map.put("optId",""+json.get("optId"));	
			}
			map.put("txnDateTime",""+json.get("ts"));
			map.put("clientId",""+json.get("clientId"));	
			map.put("txnId",""+json.get("txnId"));	
			map.put("errCode",""+json.get("errCode"));	
			map.put("msg",""+json.get("msg"));	
			return map;
		}catch(Exception e){
			map.put("status", "-1");
			map.put("message", "Wrong phone or access token");
			System.out.println("errror "+e);
			return map;
		}
	}
	

}
