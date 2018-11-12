package com.acq.web.dao.impl;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class OGSCardDesc {

	public static String VISA = "Visa";
    public static String RUPAY = "Rupay";
    public static String MAESTRO = "Maestro";
    public static String MASTER = "MasterCard";
    public static String DINERS = "DINERS";
    public static String JCB = "JCB";
    public static String AMERICAN_EXPRESS = "American Express";
    public static String DISCOVER = "Discover";

    
    public static String newNetworkTransport(String isoMessage,String uat)
			throws UnknownHostException, IOException{
		String ogsResponse = "";
		try{
			System.out.println("iso message:"+isoMessage);
			//System.out.println("iso message length:"+isoMessage.length());
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
			byte[] bcdArray = str2Bcd(hexString);
			System.out.println("byte arr: "+bcdArray);
			byte[] isoMessageBytes=isoMessage.getBytes();
			byte[] totalMsgBytes=new byte[bcdArray.length+isoMessageBytes.length];
			System.arraycopy(bcdArray, 0, totalMsgBytes, 0, bcdArray.length);
			System.arraycopy(isoMessageBytes, 0, totalMsgBytes, bcdArray.length, isoMessageBytes.length);
			
			Socket connection = null;			
			if(uat!=null&&uat.equals("1")){
				connection = new Socket("180.151.52.26", 9888);
			}else if(uat!=null&&uat.equals("2")){
				connection = new Socket("180.151.52.26", 9887);
			}	
			connection.setSoTimeout (100);
    		connection.setSoTimeout (75000);
    		BufferedOutputStream bos = new BufferedOutputStream(connection.getOutputStream());
    		System.out.println("totalMsgBytes:::::::"+totalMsgBytes);
			bos.write(totalMsgBytes);
			bos.flush();
			byte[] arrOutut = new byte[4096];
			System.out.println("soket connection going connect");
			int count = connection.getInputStream().read(arrOutut, 0, 4096);
			System.out.println("response from ogs");
			for (int outputCount = 0; outputCount < count; outputCount++){
				char response = (char)arrOutut[outputCount];
				ogsResponse = ogsResponse + response;
			}
			connection.close();
		}catch(java.net.SocketTimeoutException e){
			System.err.println("error to connect ogs "+e);
			return "SocketException";
		}catch(Exception e){
			System.err.println("error to connect ogs "+e);
			return "error to connect ogs";
		}
		return ogsResponse;
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
    public static String getCardType(String cardNo,String aid){
        String cardType = "";

        if(aid.length()==0){
            cardType =  getCardFromPan(cardNo);
        }else{
            String rid = aid.substring(0,10);
           // Log.d(TAG, "getCardType() called with: cardNo = [" + cardNo + "], aid = [" + aid + "]");
            switch (rid){
                case "A000000524":
                    cardType =  RUPAY;
                    break;
                case "A000000003":
                    cardType =  VISA;
                    break;
                case "A000000004":
                    cardType =  MASTER;
                    break;
                case "A000000025":
                    cardType =  AMERICAN_EXPRESS;
                    break;
                case "A000000152":
                    cardType =  DISCOVER;
                    break;
                case "A000000065":
                    cardType =  JCB;
                    break;
            }
        }
        //return cardType;
        return "RUPAY";
    }

    /**
     * Get Card Issuer from Card pan
     * @param cardNo
     * @return Get Card from Pan
     */
	

    static String getCardFromPan(String cardNo){
        String regVisa = "^4[0-9]{12}(?:[0-9]{3})?$";
        String regMaster = "^5[1-5][0-9]{14}$";
        String regExpress = "^3[47][0-9]{13}$";
        String regDiners = "^3(?:0[0-5]|[68][0-9])[0-9]{11}$";
        String regDiscover = "^6(?:011|5[0-9]{2})[0-9]{12}$";
        String regJCB= "^(?:2131|1800|35\\d{3})\\d{11}$";
        String regRupay= "^6[0-9]{15}$";
        String regMaestro= "^(?:5[0678]\\d\\d|6304|6390|67\\d\\d)\\d{8,15}$";
        if(cardNo.matches(regVisa))
            return VISA;
        if (cardNo.matches(regMaster))
            return MASTER;
        if (cardNo.matches(regExpress))
            return "Amex";
        if (cardNo.matches(regDiners))
            return DINERS;
        if (cardNo.matches(regDiscover))
            return DISCOVER;
        if (cardNo.matches(regJCB))
            return JCB;
        if (cardNo.matches(regRupay))
            return RUPAY;
        if (cardNo.matches(regMaestro))
            return MAESTRO;
        return "";
    }
    //generateDe61New
    
    static String generateDe61New(boolean cardType,boolean isEMV,String pinBlock,String address,String pinCode){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("4"); // 1
        if(cardType){
            //stringBuilder.append("2"); // 2
            if(pinBlock.equals("0000000000000000")){
                stringBuilder.append("1"); // 8
            }else{
                stringBuilder.append("2"); // 8
            }
        }else{
            stringBuilder.append("1"); // 2
        }
        if(cardType){
            stringBuilder.append("0"); // PIN
        }else{
            stringBuilder.append("1"); // SIGNATURE
        }
        stringBuilder.append("0"); // 4
        //stringBuilder.append("7"); // 4
        stringBuilder.append("1"); // 5
        stringBuilder.append("2"); // 6
        if(isEMV){
            stringBuilder.append("3"); // 7 //EMV -> 3
        }else{
            stringBuilder.append("2"); // 7 //EMV -> 3
        }
        if(cardType){
            if(pinBlock.equals("0000000000000000")){
                stringBuilder.append("3"); // 8
            }else{
                stringBuilder.append("2"); // 8
            }
        }else{
            stringBuilder.append("3"); // 8
        }
        if(isEMV){
            stringBuilder.append("1"); // 9 PIN EMV - >1
        }else{
            stringBuilder.append("0"); // 9 PIN EMV - >1
        }
        
        if(isEMV){
            stringBuilder.append("2"); // 10 
        }else{
            stringBuilder.append("0"); // 10 
        }
       // stringBuilder.append("2"); // 10 
        stringBuilder.append("3"); // 11
        stringBuilder.append("3"); // 12
        //stringBuilder.append("000122001"); // 13
        stringBuilder.append("000"+pinCode); // 13
        //stringBuilder.append("nungambakkam        "); // 14
        stringBuilder.append(address); // 14
       // Log.d(TAG, "DE 61:  value" +stringBuilder.toString());
        return stringBuilder.toString();
    }
    
    static String generateDe61(boolean cardType,boolean isEMV,String pinBlock){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("4"); // 1
        if(cardType){
            //stringBuilder.append("2"); // 2
            if(pinBlock.equals("0000000000000000")){
                stringBuilder.append("1"); // 8
            }else{
                stringBuilder.append("2"); // 8
            }
        }else{
            stringBuilder.append("1"); // 2
        }
        if(cardType){
            stringBuilder.append("0"); // PIN
        }else{
            stringBuilder.append("1"); // SIGNATURE
        }
        stringBuilder.append("0"); // 4
        stringBuilder.append("1"); // 5
        stringBuilder.append("2"); // 6
        if(isEMV){
            stringBuilder.append("3"); // 7 //EMV -> 3
        }else{
            stringBuilder.append("2"); // 7 //EMV -> 3
        }
        if(cardType){
            if(pinBlock.equals("0000000000000000")){
                stringBuilder.append("3"); // 8
            }else{
                stringBuilder.append("2"); // 8
            }
        }else{
            stringBuilder.append("3"); // 8
        }
        if(isEMV){
            stringBuilder.append("1"); // 9 PIN EMV - >1
        }else{
            stringBuilder.append("0"); // 9 PIN EMV - >1
        }
        
        if(isEMV){
            stringBuilder.append("2"); // 10 
        }else{
            stringBuilder.append("0"); // 10 
        }
       // stringBuilder.append("2"); // 10 
        stringBuilder.append("3"); // 11
        stringBuilder.append("3"); // 12
        stringBuilder.append("000122001"); // 13
        stringBuilder.append("nungambakkam        "); // 14

       // Log.d(TAG, "DE 61:  value" +stringBuilder.toString());
        return stringBuilder.toString();
    }
    
    public static String keyExNetworkTransport(String isomsg,String uat)
			throws UnknownHostException, IOException{
		String ogsResponse = "";
		try{
			String isoMessage = "00000000000000000000"+isomsg;
			System.out.println("iso message: "+isoMessage);
		System.out.println("isoMessage:"+isoMessage.length());
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
			System.out.println("uat is:"+uat);
			
			if(uat!=null&&uat.equals("1")){
				connection = new Socket("180.151.52.26", 9888);
			}else if(uat!=null&&uat.equals("2")){
				connection = new Socket("180.151.52.26", 9887);
			}	
			connection.setSoTimeout (75000);
    		BufferedOutputStream bos = new BufferedOutputStream(connection.getOutputStream());
    		System.out.println("totalMsgBytes:::::::"+totalMsgBytes);
			bos.write(totalMsgBytes);
			bos.flush();
			byte[] arrOutut = new byte[4096];
			System.out.println("soket connection going connect");
			int count = connection.getInputStream().read(arrOutut, 0, 4096);
			System.out.println("response from ogs");
			for (int outputCount = 0; outputCount < count; outputCount++){
				char response = (char)arrOutut[outputCount];
				ogsResponse = ogsResponse + response;
			}
			System.out.println("ogs response: "+ogsResponse);
			connection.close();
		}catch(java.net.SocketTimeoutException e){
			System.err.println("error to connect ogs "+e);
			return "SocketException";
		}catch(Exception e){
			System.err.println("error to connect ogs "+e);
			return "error to connect ogs";
		}
		return ogsResponse;
	}
    
    public static String getFailReason(String code){
        switch (code){
            case "00":
                return "Approved or Completed Successfully";
            case "03":
                return "Invalid Merchant";
            case "04":
                return "Pick-Up";
            case "05":
                return "Do not honour";
            case "06":
                return "Error";
            case "12":
                return "Invalid Transaction";
            case "13":
                return "Invalid Amount";
            case "14":
                return "Invalid Card Number";
            case "15":
                return "No Such Issuer";
            case "17":
                return "Customer Cancellation";
            case "20":
                return "Invalid Response";
            case "21":
                return "No action Taken";
            case "22":
                return "Suspected malfunction";
            case "30":
                return "Format error";
            case "31":
                return "Bank not supported by switch";
            case "33":
                return "Expired card, capture";
            case "34":
                return "Suspected fraud, capture";
            case "36":
                return "Restricted card, capture";
            case "38":
                return "Allowable PIN tries exceeded, capture";
            case "39":
                return "No credit account";
            case "40":
                return "Requested function not supported";
            case "41":
                return "Lost card, capture";
            case "42":
                return "No universal account";
            case "43":
                return "Stolen card, capture";
            case "51":
                return "Not sufficient funds";
            case "52":
                return "No checking account";
            case "53":
                return "No savings account";
            case "54":
                return "Expired card, decline";
            case "55":
                return "Incorrect personal identification number";
            case "56":
                return "No card record";
            case "57":
                return "Transaction not permitted to Cardholder";
            case "58":
                return "Transaction not permitted to terminal";
            case "59":
                return "Suspected fraud, decline/ Transaction declined based on Risk Score";
            case "60":
                return "Card acceptor contact acquirer, decline";
            case "61":
                return "Exceeds withdrawal amount limit";
            case "62":
                return "Restricted card, decline";
            case "63":
                return "Security violation";
            case "65":
                return "Exceeds withdrawal frequency limit";
            case "66":
                return "Card acceptor calls acquirer's ";
            case "67":
                return "Hard capture";
            case "68":
                return "Acquirer time-out";
            case "74":
                return "Transaction declined by issuer based on Risk Score";
            case "75":
                return "Allowable number of PIN tries exceeded, decline";
            case "90":
                return "Cut-off is in process";
            case "91":
                return "Issuer or switch is inoperative";
            case "92":
                return "No routing available";
            case "93":
                return "Transaction cannot be completed. Compliance violation";
            case "94":
                return "Duplicate transmission";
            case "95":
                return "Reconcile error";
            case "96":
                return "System malfunction";
            case "E3":
                return "ARQC validation failed by issuer";
            case "E4":
                return "TVR validation failed by issuer";
            case "E5":
                return "CVR validation failed by issuer";
        }
        return code;
    }
    
}