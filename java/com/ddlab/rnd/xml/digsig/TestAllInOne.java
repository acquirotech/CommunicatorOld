/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddlab.rnd.xml.digsig;

import com.acq.RechargeApis;
import com.ddlab.rnd.crypto.KryptoUtil;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This is a test class to show how XML digital signature works. It performs the
 * following functionalities 1. Generates Private and Public Keys 2. Using
 * Private and Public Keys, sign the document 3. Verify the signed XML document
 * using public key
 *
 * @author <a href="mailto:debadatta.mishra@gmail.com">Debadatta Mishra</a>
 * @since 2013
 */
public class TestAllInOne {
	

       //Generate the keys
	Map<String,String> callMobileRecharge(String to, String transId, String amount, String type, String operator, String circle) {			
		URL sendUrl;
		System.out.println("call for Mobile Recharge");
		Map<String,String> map =  new HashMap<String,String>();  
		try {
			sendUrl = new URL("http://api.twd.bz/wallet/api/mobile.php");  
			HttpURLConnection httpConnection = (HttpURLConnection)sendUrl.openConnection();  
			httpConnection.setConnectTimeout(8000);
			httpConnection.setReadTimeout(8000);
			httpConnection.setRequestMethod("POST");
			httpConnection.setDoInput(true);
			httpConnection.setDoOutput(true);
			httpConnection.setUseCaches(false);
			httpConnection.setRequestProperty("Authorization"," Basic 77de68daecd823babbb58edb1c8e14d7106e83bb");
			DataOutputStream dataStreamToServer = new DataOutputStream(httpConnection.getOutputStream());					  
			dataStreamToServer.writeBytes("to="+to+"&memberId=sandbox&passwd=test123&transId="+transId+"&amount="+amount+"&type="+type+"&operator="+operator+"&circle="+circle);
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
			JSONObject json = (JSONObject)new JSONParser().parse(dataFromUrl);	
			map.put("txnDateTime",""+json.get("ts"));
			map.put("clientId",""+json.get("clientId"));	
			map.put("txnId",""+json.get("txnId"));	
			map.put("errCode",""+json.get("errCode"));	
			map.put("msg",""+json.get("msg"));	
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
  
    	 public static void main(String[] args) {
    		 TestAllInOne t = new TestAllInOne();
    		System.out.println("qqqqqqqqqqqqqq::::::"+t.callMobileRecharge("3003984028", "11156", "100", "TOPUP", "13", "51"));
    		    
}
    
    
}
