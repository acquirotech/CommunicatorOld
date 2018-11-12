package com.acq;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AcqSHA256 {
	public static String getSHA256(String param){
		MessageDigest md;
		StringBuffer sbstr = new StringBuffer();
		try {
			md = MessageDigest.getInstance("SHA-256");
			md.update(param.getBytes());	 
		    byte byteData[] = md.digest();	 
		    for (int i = 0; i < byteData.length; i++) {
		        sbstr.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
		    }	
		    return sbstr.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}      
	}
}
