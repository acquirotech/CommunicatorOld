package com.acq;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class CheckSum {

	public String calculateChecksum(String secretKey, String allParamValue) throws Exception{
		byte[] dataToEncryptByte = allParamValue.getBytes(); 
		byte[] keyBytes = secretKey.getBytes();
		SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "HmacSHA256");
		Mac mac = Mac.getInstance("HmacSHA256");
		mac.init(secretKeySpec);
		byte[] checksumByte = mac.doFinal(dataToEncryptByte);
		String checksum = toHex(checksumByte);
		return checksum;
	}	
	public String toHex(byte[] bytes){
		StringBuilder buffer = new StringBuilder(bytes.length * 2);
		String str;
		for (Byte b : bytes)
		{
			str = Integer.toHexString(b);
			int len = str.length();
			if (len == 8)
				buffer.append(str.substring(6));
			else if (str.length() == 2)
				buffer.append(str);
			else 
				buffer.append("0" + str);
		}
		return buffer.toString();
	}
}
