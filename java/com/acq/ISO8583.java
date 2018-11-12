package com.acq;
/*
 *	<iso8583 Message Pack & Unpack lib for Java. Works well with Android too. Supports:Tertiary Bitmap -> Subfields>
 *  Copyright (C) 2013  Vikrant Labde <vikrant@cuelogic.co.in>
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.apache.commons.io.IOUtils;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.GenericPackager;


public class ISO8583 {
	public static HashMap<String, String> ISOSCHEMA = new HashMap<String, String>();
	public static HashMap<String, String> ISOMESSAGE = new HashMap<String, String>();
	public static HashMap<String, String> PARSEDISOMESSAGE = new HashMap<String, String>();
	public static HashMap<Integer, ArrayList<Integer>> SUBFIELDSMAPPING = new HashMap<Integer, ArrayList<Integer>>();
	public static boolean ISSUBFIELDPARSING = false;
	public static String SUBFIELDID = "";

	//public void getOgs() throws Exception {
	public static void main(String[] args) throws Exception {
		//String isoMessage = buildJposISOMessage("0200");
		GenericPackager packager = new GenericPackager("purchaseTxn.xml");			 
		// Create ISO Message
		ISOMsg isoMsg = new ISOMsg();
		isoMsg.setPackager(packager);
		isoMsg.setMTI("0200");
		SimpleDateFormat dateFormatGmt = new SimpleDateFormat("MMddHHmmss");
		dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
		//System.out.println("nowAsISODateTime:"+dateFormatGmt.format(new Date()));
		//till here is correct iso message		
		//isoMsg.set(2, "6085008000004927");  // read data after card swipe // pls change that one
		isoMsg.set(2,"6070930058609470");
		isoMsg.set(3, "000000");  // procesing code
		isoMsg.set(4, "000000010000");  // txn amount
        isoMsg.set(7, dateFormatGmt.format(new Date())); //txn date time
		//isoMsg.set(7,"0316105014");
        isoMsg.set(11, "000016"); //Systemtrace audit number //txn id
        isoMsg.set(12, "162014"); //Time, local transaction
        isoMsg.set(13, "0316"); //Date,local transaction // are we going correct? yes the value is in plain format.
        isoMsg.set(14, "2105");
        isoMsg.set(18, "5999"); //Merchantcategory code
        isoMsg.set(19, "356"); //Acquiringinstitutioncountrycode
        isoMsg.set(22, "901"); //POSentrymode
        isoMsg.set(25, "00"); //POSconditioncode // space is coming for de25, have you any idea? are you there?wait
        isoMsg.set(32, "720399"); //Acquiringinstitutioncode //apending 25
        //isoMsg.set(35, "6085008000004927");  //in between data element 32 and 37 data element values. //pls check chat screen
        //isoMsg.set(35,"6085008000004927=21055219990000000000");//now run it.
        isoMsg.set(35,"6070930058609470=21085202890000000000");
        isoMsg.set(37, "707516000013"); //Retrieval reference number // chat screen not coming plz reconnect team viewer
        isoMsg.set(40, "520");
        isoMsg.set(41, "00000005"); //Cardacceptor terminalID
        isoMsg.set(42, "00000005       "); //Cardacceptor ID // 000005 instead   u have to add space at the end
        isoMsg.set(43, "PACE                   CHENNAI      TNIN"); //Cardacceptor name /location 40-this is length of this element. no need to pass it in request.
        isoMsg.set(48, "POS01"); //Additional data        //we are not putting, its appending autometically check in XML file.ensure the class what u using in that DE.
        isoMsg.set(49, "356");		//Currencycode, transaction
        isoMsg.set(52, "398445126D1900B8");
        isoMsg.set(61, "420012220033000600034OMR");//POSdata code          
        
		logISOMsg(isoMsg);
		byte[] data = isoMsg.pack();
		System.out.println("RESULT : " + new String(data));		
		networkTransport(new String(data));
		//ISO8583 iso = new ISO8583();
		//ISOMsg isoMessage = iso.parseISOMessage(response);
		//iso.printISOMessage(isoMessage);
		//unpackIsoMsg(response);
		//System.out.println("Unpacked iso8583 Message" + response);
	}
	public static String convertStringToBCD(String val) {
		//byte 48485456 //97=104
		byte[] bytes = val.getBytes();
		StringBuilder binary = new StringBuilder();
		for (byte b : bytes){
			int vall = b;
			for (int i = 0; i < 8; i++){
				binary.append((vall & 128) == 0 ? 0 : 1);
				vall <<= 1;
			}
		}
		return binary.toString();
	}
	private static void logISOMsg(ISOMsg msg) {
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
	public ISOMsg parseISOMessage(String message) throws Exception {
        //String message = "02003220000000808000000010000000001500120604120000000112340001840";
        System.out.printf("Message = %s%n", message);
        try {
            InputStream is = getClass().getResourceAsStream("/fields.xml");
            GenericPackager packager = new GenericPackager(is);
            ISOMsg isoMsg = new ISOMsg();
            isoMsg.setPackager(packager);
            isoMsg.unpack(message.getBytes());
            return isoMsg;
        } catch (ISOException e) {
            throw new Exception(e);
        }
    }
	public void printISOMessage(ISOMsg isoMsg) {
        try {
            System.out.printf("MTI = %s%n", isoMsg.getMTI());
            for (int i = 1; i <= isoMsg.getMaxField(); i++) {
                if (isoMsg.hasField(i)) {
                    System.out.printf("Field (%s) = %s%n", i, isoMsg.getString(i));
                }
            }
        } catch (ISOException e) {
            e.printStackTrace();
        }
    }
	    /**
	     * 
	     * This method packs the message into ISO8583 standards. 
	     * This method requires HASHMAP representation as an input
	     * 
	     * @param MTI
	     * @param isofields
	     * @return String
	     * @throws Exception
	     */
		 public static String buildJposISOMessage(String MI) throws Exception { //JPOS iso message
			 try {
				 System.out.println("qqqqqqqqqqqqqqqqqq");
				 //InputStream is = getClass().getResourceAsStream("/fields.xml");
		            String source = "<isopackager>" +
		            	/*"<isofield id=\"0\" length=\"4\" name=\"MESSAGE TYPE INDICATOR\" class=\"org.jpos.iso.IFA_NUMERIC\"/>"+
		            	"<isofield id=\"1\" length=\"16\" name=\"BIT MAP\" class=\"org.jpos.iso.IFA_BITMAP\"/>"+
		            	"<isofield id=\"2\" length=\"19\" name=\"SECRET ID\" class=\"org.jpos.iso.IFA_LLNUM\"/>"+
		            	"<isofield id=\"3\" length=\"6\" name=\"PROCESSING CODE\" class=\"org.jpos.iso.IFA_NUMERIC\"/>"+
		            	"<isofield id=\"4\" length=\"12\" name=\"AMOUNT, TRANSACTION\" class=\"org.jpos.iso.IFA_NUMERIC\"/>"+
		            	"<isofield id=\"5\" length=\"12\" name=\"AMOUNT, SETTLEMENT\" class=\"org.jpos.iso.IFA_NUMERIC\"/>"+
		            	"<isofield id=\"6\" length=\"12\" name=\"AMOUNT, CARDHOLDER BILLING\" class=\"org.jpos.iso.IFA_NUMERIC\"/>"+
		            	"<isofield id=\"7\" length=\"10\" name=\"TRANSMISSION DATE AND TIME\" class=\"org.jpos.iso.IFA_NUMERIC\"/>"+
		            	"<isofield id=\"8\" length=\"8\" name=\"AMOUNT, CARDHOLDER BILLING FEE\" class=\"org.jpos.iso.IFA_NUMERIC\"/>"+
		            	"<isofield id=\"9\" length=\"8\" name=\"CONVERSION RATE, SETTLEMENT\" class=\"org.jpos.iso.IFA_NUMERIC\"/>"+
		            	"<isofield id=\"10\" length=\"8\" name=\"CONVERSION RATE, CARDHOLDER BILLING\" class=\"org.jpos.iso.IFA_NUMERIC\"/>"+
		            	"<isofield id=\"11\" length=\"6\" name=\"SYSTEM TRACE AUDIT NUMBER\" class=\"org.jpos.iso.IFA_NUMERIC\"/>"+
		            	"<isofield id=\"12\" length=\"6\" name=\"TIME, LOCAL TRANSACTION\" class=\"org.jpos.iso.IFA_NUMERIC\"/>"+
		            	"<isofield id=\"13\" length=\"4\" name=\"DATE, LOCAL TRANSACTION\" class=\"org.jpos.iso.IFA_NUMERIC\"/>"+
		            	"<isofield id=\"14\" length=\"4\" name=\"DATE, EXPIRATION\" class=\"org.jpos.iso.IFA_NUMERIC\"/>"+
		            	"<isofield id=\"15\" length=\"4\" name=\"DATE, SETTLEMENT\" class=\"org.jpos.iso.IFA_NUMERIC\"/>"+
		            	"<isofield id=\"16\" length=\"4\" name=\"DATE, CONVERSION\" class=\"org.jpos.iso.IFA_NUMERIC\"/>"+
		            	"<isofield id=\"17\" length=\"4\" name=\"DATE, CAPTURE\" class=\"org.jpos.iso.IFA_NUMERIC\"/>"+
		            	"<isofield id=\"18\" length=\"4\" name=\"MERCHANTS TYPE\" class=\"org.jpos.iso.IFA_NUMERIC\"/>"+
		            	"<isofield id=\"19\" length=\"3\" name=\"ACQUIRING INSTITUTION COUNTRY CODE\" class=\"org.jpos.iso.IFA_NUMERIC\"/>"+
		            	"<isofield id=\"20\" length=\"3\" name=\"PAN EXTENDED COUNTRY CODE\" class=\"org.jpos.iso.IFA_NUMERIC\"/>"+
		            	"<isofield id=\"21\" length=\"3\" name=\"FORWARDING INSTITUTION COUNTRY CODE\" class=\"org.jpos.iso.IFA_NUMERIC\"/>"+
		            	"<isofield id=\"22\" length=\"3\" name=\"POINT OF SERVICE ENTRY MODE\" class=\"org.jpos.iso.IFA_NUMERIC\"/>"+
		            	"<isofield id=\"23\" length=\"3\" name=\"CARD SEQUENCE NUMBER\" class=\"org.jpos.iso.IFA_NUMERIC\"/>"+
		            	"<isofield id=\"24\" length=\"3\" name=\"NETWORK INTERNATIONAL IDENTIFIEER\" class=\"org.jpos.iso.IFA_NUMERIC\"/>"+
		            	"<isofield id=\"25\" length=\"2\" name=\"POINT OF SERVICE CONDITION CODE\" class=\"org.jpos.iso.IFA_NUMERIC\"/>"+
		            	"<isofield id=\"26\" length=\"2\" name=\"POINT OF SERVICE PIN CAPTURE CODE\" class=\"org.jpos.iso.IFA_NUMERIC\"/>"+
		            	"<isofield id=\"27\" length=\"1\" name=\"AUTHORIZATION IDENTIFICATION RESP LEN\" class=\"org.jpos.iso.IFA_NUMERIC\"/>"+
		            	"<isofield id=\"28\" length=\"9\" name=\"AMOUNT, TRANSACTION FEE\" class=\"org.jpos.iso.IFA_AMOUNT\"/>"+
		            	"<isofield id=\"29\" length=\"9\" name=\"AMOUNT, SETTLEMENT FEE\" class=\"org.jpos.iso.IFA_AMOUNT\"/>"+
		            	"<isofield id=\"30\" length=\"9\" name=\"AMOUNT, TRANSACTION PROCESSING FEE\" class=\"org.jpos.iso.IFA_AMOUNT\"/>"+
		            	"<isofield id=\"31\" length=\"9\" name=\"AMOUNT, SETTLEMENT PROCESSING FEE\" class=\"org.jpos.iso.IFA_AMOUNT\"/>"+
		            	"<isofield id=\"32\" length=\"11\" name=\"ACQUIRING INSTITUTION IDENT CODE\" class=\"org.jpos.iso.IFA_LLNUM\"/>"+
		            	"<isofield id=\"33\" length=\"11\" name=\"FORWARDING INSTITUTION IDENT CODE\" class=\"org.jpos.iso.IFA_LLNUM\"/>"+
		            	"<isofield id=\"34\" length=\"28\" name=\"PAN EXTENDED\" class=\"org.jpos.iso.IFA_LLCHAR\"/>"+
		            	"<isofield id=\"35\" length=\"37\" name=\"TRACK 2 DATA\" class=\"org.jpos.iso.IFA_LLNUM\"/>"+
		            	"<isofield id=\"36\" length=\"104\" name=\"TRACK 3 DATA\" class=\"org.jpos.iso.IFA_LLLCHAR\"/>"+
		            	"<isofield id=\"37\" length=\"12\" name=\"RETRIEVAL REFERENCE NUMBER\" class=\"org.jpos.iso.IF_CHAR\"/>"+
		            	"<isofield id=\"38\" length=\"6\" name=\"AUTHORIZATION IDENTIFICATION RESPONSE\" class=\"org.jpos.iso.IF_CHAR\"/>"+
		            	"<isofield id=\"39\" length=\"2\" name=\"RESPONSE CODE\" class=\"org.jpos.iso.IF_CHAR\"/>"+
		            	"<isofield id=\"40\" length=\"3\" name=\"SERVICE RESTRICTION CODE\" class=\"org.jpos.iso.IF_CHAR\"/>"+
		            	"<isofield id=\"41\" length=\"8\" name=\"CARD ACCEPTOR TERMINAL IDENTIFICACION\" class=\"org.jpos.iso.IF_CHAR\"/>"+
		            	"<isofield id=\"42\" length=\"15\" name=\"CARD ACCEPTOR IDENTIFICATION CODE\" class=\"org.jpos.iso.IF_CHAR\"/>"+
		            	"<isofield id=\"43\" length=\"40\" name=\"CARD ACCEPTOR NAME/LOCATION\" class=\"org.jpos.iso.IF_CHAR\"/>"+
		            	"<isofield id=\"44\" length=\"25\" name=\"ADITIONAL RESPONSE DATA\" class=\"org.jpos.iso.IFA_LLCHAR\"/>"+
		            	"<isofield id=\"45\" length=\"76\" name=\"TRACK 1 DATA\" class=\"org.jpos.iso.IFA_LLCHAR\"/>"+
		            	"<isofield id=\"46\" length=\"999\" name=\"ADITIONAL DATA - ISO\" class=\"org.jpos.iso.IFA_LLLCHAR\"/>"+
		            	"<isofield id=\"47\" length=\"999\" name=\"ADITIONAL DATA - NATIONAL\" class=\"org.jpos.iso.IFA_LLLCHAR\"/>"+
		            	"<isofield id=\"48\" length=\"999\" name=\"ADITIONAL DATA - PRIVATE\" class=\"org.jpos.iso.IFA_LLLCHAR\"/>"+
		            	"<isofield id=\"49\" length=\"3\" name=\"CURRENCY CODE, TRANSACTION\" class=\"org.jpos.iso.IF_CHAR\"/>"+
		            	"<isofield id=\"50\" length=\"3\" name=\"CURRENCY CODE, SETTLEMENT\" class=\"org.jpos.iso.IF_CHAR\"/>"+
		            	"<isofield id=\"51\" length=\"3\" name=\"CURRENCY CODE, CARDHOLDER BILLING\" class=\"org.jpos.iso.IF_CHAR\"/>"+
		            	"<isofield id=\"52\" length=\"16\" name=\"PIN DATA\" class=\"org.jpos.iso.IFA_NUMERIC\"/>"+
		            	"<isofield id=\"53\" length=\"16\" name=\"SECURITY RELATED CONTROL INFORMATION\" class=\"org.jpos.iso.IFA_NUMERIC\"/>"+
		            	"<isofield id=\"54\" length=\"120\" name=\"ADDITIONAL AMOUNTS\" class=\"org.jpos.iso.IFA_LLLCHAR\"/>"+
		            	"<isofield id=\"55\" length=\"999\" name=\"RESERVED ISO\" class=\"org.jpos.iso.IFA_LLLCHAR\"/>"+
		            	"<isofield id=\"56\" length=\"999\" name=\"RESERVED ISO\" class=\"org.jpos.iso.IFA_LLLCHAR\"/>"+
		            	"<isofield id=\"57\" length=\"999\" name=\"RESERVED NATIONAL\" class=\"org.jpos.iso.IFA_LLLCHAR\"/>"+
		            	"<isofield id=\"58\" length=\"999\" name=\"RESERVED NATIONAL\" class=\"org.jpos.iso.IFA_LLLCHAR\"/>"+
		            	"<isofield id=\"59\" length=\"999\" name=\"RESERVED NATIONAL\" class=\"org.jpos.iso.IFA_LLLCHAR\"/>"+
		            	"<isofield id=\"60\" length=\"999\" name=\"RESERVED PRIVATE\" class=\"org.jpos.iso.IFA_LLLCHAR\"/>"+
		            	"<isofield id=\"61\" length=\"999\" name=\"RESERVED PRIVATE\" class=\"org.jpos.iso.IFA_LLLCHAR\"/>"+
		            	"<isofield id=\"62\" length=\"999\" name=\"RESERVED PRIVATE\" class=\"org.jpos.iso.IFA_LLLCHAR\"/>"+
		            	"<isofield id=\"63\" length=\"999\" name=\"RESERVED PRIVATE\" class=\"org.jpos.iso.IFA_LLLCHAR\"/>"+
		            	"<isofield id=\"64\" length=\"8\" name=\"MESSAGE AUTHENTICATION CODE FIELD\" class=\"org.jpos.iso.IFA_BINARY\"/>"+
		            	"<isofield id=\"65\" length=\"1\" name=\"BITMAP, EXTENDED\" class=\"org.jpos.iso.IFA_BINARY\"/>"+
		            	"<isofield id=\"66\" length=\"1\" name=\"SETTLEMENT CODE\" class=\"org.jpos.iso.IFA_NUMERIC\"/>"+
		            	"<isofield id=\"67\" length=\"2\" name=\"EXTENDED PAYMENT CODE\" class=\"org.jpos.iso.IFA_NUMERIC\"/>"+
		            	"<isofield id=\"68\" length=\"3\" name=\"RECEIVING INSTITUTION COUNTRY CODE\" class=\"org.jpos.iso.IFA_NUMERIC\"/>"+
		            	"<isofield id=\"69\" length=\"3\" name=\"SETTLEMENT INSTITUTION COUNTRY CODE\" class=\"org.jpos.iso.IFA_NUMERIC\"/>"+
		            	"<isofield id=\"70\" length=\"3\" name=\"NETWORK MANAGEMENT INFORMATION CODE\" class=\"org.jpos.iso.IFA_NUMERIC\"/>"+
		            	"<isofield id=\"71\" length=\"4\" name=\"MESSAGE NUMBER\" class=\"org.jpos.iso.IFA_NUMERIC\"/>"+
		            	"<isofield id=\"72\" length=\"4\" name=\"MESSAGE NUMBER LAST\" class=\"org.jpos.iso.IFA_NUMERIC\"/>"+
		            	"<isofield id=\"73\" length=\"6\" name=\"DATE ACTION\" class=\"org.jpos.iso.IFA_NUMERIC\"/>"+
		            	"<isofield id=\"74\" length=\"10\" name=\"CREDITS NUMBER\" class=\"org.jpos.iso.IFA_NUMERIC\"/>"+
		            	"<isofield id=\"75\" length=\"10\" name=\"CREDITS REVERSAL NUMBER\" class=\"org.jpos.iso.IFA_NUMERIC\"/>"+
		            	"<isofield id=\"76\" length=\"10\" name=\"DEBITS NUMBER\" class=\"org.jpos.iso.IFA_NUMERIC\"/>"+
		            	"<isofield id=\"77\" length=\"10\" name=\"DEBITS REVERSAL NUMBER\" class=\"org.jpos.iso.IFA_NUMERIC\"/>"+
		            	"<isofield id=\"78\" length=\"10\" name=\"TRANSFER NUMBER\" class=\"org.jpos.iso.IFA_NUMERIC\"/>"+
		            	"<isofield id=\"79\" length=\"10\" name=\"TRANSFER REVERSAL NUMBER\" class=\"org.jpos.iso.IFA_NUMERIC\"/>"+
		            	"<isofield id=\"80\" length=\"10\" name=\"INQUIRIES NUMBER\" class=\"org.jpos.iso.IFA_NUMERIC\"/>"+
		            	"<isofield id=\"81\" length=\"10\" name=\"AUTHORIZATION NUMBER\" class=\"org.jpos.iso.IFA_NUMERIC\"/>"+
		            	"<isofield id=\"82\" length=\"12\" name=\"CREDITS, PROCESSING FEE AMOUNT\" class=\"org.jpos.iso.IFA_NUMERIC\"/>"+
		            	"<isofield id=\"83\" length=\"12\" name=\"CREDITS, TRANSACTION FEE AMOUNT\" class=\"org.jpos.iso.IFA_NUMERIC\"/>"+
		            	"<isofield id=\"84\" length=\"12\" name=\"DEBITS, PROCESSING FEE AMOUNT\" class=\"org.jpos.iso.IFA_NUMERIC\"/>"+
		            	"<isofield id=\"85\" length=\"12\" name=\"DEBITS, TRANSACTION FEE AMOUNT\" class=\"org.jpos.iso.IFA_NUMERIC\"/>"+
		            	"<isofield id=\"86\" length=\"16\" name=\"CREDITS, AMOUNT\" class=\"org.jpos.iso.IFA_NUMERIC\"/>"+
		            	"<isofield id=\"87\" length=\"16\" name=\"CREDITS, REVERSAL AMOUNT\" class=\"org.jpos.iso.IFA_NUMERIC\"/>"+
		            	"<isofield id=\"88\" length=\"16\" name=\"DEBITS, AMOUNT\" class=\"org.jpos.iso.IFA_NUMERIC\"/>"+
		            	"<isofield id=\"89\" length=\"16\" name=\"DEBITS, REVERSAL AMOUNT\" class=\"org.jpos.iso.IFA_NUMERIC\"/>"+
		            	"<isofield id=\"90\" length=\"42\" name=\"ORIGINAL DATA ELEMENTS\" class=\"org.jpos.iso.IFA_NUMERIC\"/>"+
		            	"<isofield id=\"91\" length=\"1\" name=\"FILE UPDATE CODE\" class=\"org.jpos.iso.IF_CHAR\"/>"+
		            	"<isofield id=\"92\" length=\"2\" name=\"FILE SECURITY CODE\" class=\"org.jpos.iso.IF_CHAR\"/>"+
		            	"<isofield id=\"93\" length=\"6\" name=\"RESPONSE INDICATOR\" class=\"org.jpos.iso.IF_CHAR\"/>"+
		            	"<isofield id=\"94\" length=\"7\" name=\"SERVICE INDICATOR\" class=\"org.jpos.iso.IF_CHAR\"/>"+
		            	"<isofield id=\"95\" length=\"42\" name=\"REPLACEMENT AMOUNTS\" class=\"org.jpos.iso.IF_CHAR\"/>"+
		            	"<isofield id=\"96\" length=\"16\" name=\"MESSAGE SECURITY CODE\" class=\"org.jpos.iso.IFA_BINARY\"/>"+
		            	"<isofield id=\"97\" length=\"17\" name=\"AMOUNT, NET SETTLEMENT\" class=\"org.jpos.iso.IFA_AMOUNT\"/>"+
		            	"<isofield id=\"98\" length=\"25\" name=\"PAYEE\" class=\"org.jpos.iso.IF_CHAR\"/>"+
		            	"<isofield id=\"99\" length=\"11\" name=\"SETTLEMENT INSTITUTION IDENT CODE\" class=\"org.jpos.iso.IFA_LLNUM\"/>"+
		            	"<isofield id=\"100\" length=\"11\" name=\"RECEIVING INSTITUTION IDENT CODE\" class=\"org.jpos.iso.IFA_LLNUM\"/>"+
		            	"<isofield id=\"101\" length=\"17\" name=\"FILE NAME\" class=\"org.jpos.iso.IFA_LLCHAR\"/>"+
		            	"<isofield id=\"102\" length=\"28\" name=\"FROM ACCOUNT\" class=\"org.jpos.iso.IFA_LLCHAR\"/>"+
		            	"<isofield id=\"103\" length=\"10\" name=\"ACCOUNT IDENTIFICATION 2\" class=\"org.jpos.iso.IFA_LLCHAR\"/>"+
		            	"<isofield id=\"104\" length=\"100\" name=\"TRANSACTION DESCRIPTION\" class=\"org.jpos.iso.IFA_LLLCHAR\"/>"+
		            	"<isofield id=\"105\" length=\"999\" name=\"RESERVED ISO USE\" class=\"org.jpos.iso.IFA_LLLCHAR\"/>"+
		            	"<isofield id=\"106\" length=\"999\" name=\"RESERVED ISO USE\" class=\"org.jpos.iso.IFA_LLLCHAR\"/>"+
		            	"<isofield id=\"107\" length=\"999\" name=\"RESERVED ISO USE\" class=\"org.jpos.iso.IFA_LLLCHAR\"/>"+
		            	"<isofield id=\"108\" length=\"999\" name=\"RESERVED ISO USE\" class=\"org.jpos.iso.IFA_LLLCHAR\"/>"+
		            	"<isofield id=\"109\" length=\"999\" name=\"RESERVED ISO USE\" class=\"org.jpos.iso.IFA_LLLCHAR\"/>"+
		            	"<isofield id=\"110\" length=\"999\" name=\"RESERVED ISO USE\" class=\"org.jpos.iso.IFA_LLLCHAR\"/>"+
		            	"<isofield id=\"111\" length=\"999\" name=\"RESERVED ISO USE\" class=\"org.jpos.iso.IFA_LLLCHAR\"/>"+
		            	"<isofield id=\"112\" length=\"999\" name=\"RESERVED NATIONAL USE\" class=\"org.jpos.iso.IFA_LLLCHAR\"/>"+
		            	"<isofield id=\"113\" length=\"999\" name=\"RESERVED NATIONAL USE\" class=\"org.jpos.iso.IFA_LLLCHAR\"/>"+
		            	"<isofield id=\"114\" length=\"999\" name=\"RESERVED NATIONAL USE\" class=\"org.jpos.iso.IFA_LLLCHAR\"/>"+
		            	"<isofield id=\"115\" length=\"999\" name=\"RESERVED NATIONAL USE\" class=\"org.jpos.iso.IFA_LLLCHAR\"/>"+
		            	"<isofield id=\"116\" length=\"999\" name=\"RESERVED NATIONAL USE\" class=\"org.jpos.iso.IFA_LLLCHAR\"/>"+
		            	"<isofield id=\"117\" length=\"999\" name=\"RESERVED NATIONAL USE\" class=\"org.jpos.iso.IFA_LLLCHAR\"/>"+
		            	"<isofield id=\"118\" length=\"999\" name=\"RESERVED NATIONAL USE\" class=\"org.jpos.iso.IFA_LLLCHAR\"/>"+
		            	"<isofield id=\"119\" length=\"999\" name=\"RESERVED NATIONAL USE\" class=\"org.jpos.iso.IFA_LLLCHAR\"/>"+
		            	"<isofield id=\"120\" length=\"999\" name=\"RESERVED PRIVATE USE\" class=\"org.jpos.iso.IFA_LLLCHAR\"/>"+
		            	"<isofield id=\"121\" length=\"999\" name=\"RESERVED PRIVATE USE\" class=\"org.jpos.iso.IFA_LLLCHAR\"/>"+
		            	"<isofield id=\"122\" length=\"999\" name=\"RESERVED PRIVATE USE\" class=\"org.jpos.iso.IFA_LLLCHAR\"/>"+
		            	"<isofield id=\"123\" length=\"999\" name=\"RESERVED PRIVATE USE\" class=\"org.jpos.iso.IFA_LLLCHAR\"/>"+
		            	"<isofield id=\"124\" length=\"999\" name=\"RESERVED PRIVATE USE\" class=\"org.jpos.iso.IFA_LLLCHAR\"/>"+
		            	"<isofield id=\"125\" length=\"999\" name=\"RESERVED PRIVATE USE\" class=\"org.jpos.iso.IFA_LLLCHAR\"/>"+
		            	"<isofield id=\"126\" length=\"999\" name=\"RESERVED PRIVATE USE\" class=\"org.jpos.iso.IFA_LLLCHAR\"/>"+
		            	"<isofield id=\"127\" length=\"999\" name=\"RESERVED PRIVATE USE\" class=\"org.jpos.iso.IFA_LLLCHAR\"/>"+
		            	"<isofield id=\"128\" length=\"8\" name=\"MAC 2\" class=\"org.jpos.iso.IFA_BINARY\"/>" +*/
		            	"</isopackager>";
		           // System.out.println("source:"+source);
		           
		            
		            InputStream is = IOUtils.toInputStream(source, "UTF-8");
		            //byte[] bytes = source.getBytes();
		            System.out.println("qqqqqqqq");
		            GenericPackager packager = new GenericPackager(is);
		            System.out.println("rrrrrrrrrrrrrrrrrr");
		            ISOMsg isoMsg = new ISOMsg();
		            isoMsg.setPackager(packager);
		            isoMsg.setMTI(MI);
		            isoMsg.set(3, "000010");
		            isoMsg.set(4, "1500");
		            isoMsg.set(7, "1206041200");
		            isoMsg.set(11, "000001");
		            isoMsg.set(41, "12340001");
		            isoMsg.set(49, "840");
		            //printISOMessage(isoMsg);
		            byte[] result = isoMsg.pack();
		            return new String(result);
		        } catch (ISOException e) {
		            throw new Exception(e);
		        }
		    }
		 public static void callMain() throws Exception {
				ISOSCHEMA.put("1","BITMAP");
				//ISOSCHEMA.put("2","NUMERIC-0-8-0_0");
				ISOSCHEMA.put("4","NUM-2-10-0_0");
				ISOSCHEMA.put("9","NUMERIC-0-8-0_0");
				ISOSCHEMA.put("18","FCHAR-10-10-0_0");
				ISOSCHEMA.put("52","CHAR-2-64-0_0");
				ISOSCHEMA.put("57","CHAR-2-50-0_0");
				ISOSCHEMA.put("58","CHAR-2-99-0_0");
				ISOSCHEMA.put("65","NUMERIC-1-1-0_0");
				ISOSCHEMA.put("100","NUM-2-8-0_0");
				ISOSCHEMA.put("101","CHAR-2-99-0_0");
				ISOSCHEMA.put("114","CHAR-4-9999-1_1");
				ISOSCHEMA.put("114.1","BITMAP");
				ISOSCHEMA.put("114.7","CHAR-2-99-0_0");
				ISOSCHEMA.put("114.10","NUM-2-5-0_0");
				ISOSCHEMA.put("114.23","CHAR-2-20-0_0");
				ISOSCHEMA.put("114.24","CHAR-2-20-0_0");
				ISOSCHEMA.put("114.65","NUMERIC-0-1-0_0");
				ISOSCHEMA.put("114.117","CHAR-2-99-0_0");
				ISOSCHEMA.put("114.150","CHAR-2-99-0_0");
				ISOSCHEMA.put("120","CHAR-4-9999-1_1");
				ISOSCHEMA.put("120.1","BITMAP");
				ISOSCHEMA.put("120.64","CHAR-2-99-0_0");
				ISOSCHEMA.put("120.120","CHAR-2-99-0_0");
				ISOSCHEMA.put("130","CHAR-2-90-0_0");
					
				//Packing ISO MESSAGE
				HashMap <String, String> isofields = new HashMap<String, String>();
				//isofields.put("2", "7001");
				isofields.put("4", "10");
				isofields.put("9", "99999999");
				isofields.put("18", "V");
				isofields.put("100", "3");
				isofields.put("101", "EzSwype India, India");
				isofields.put("130", "Gurgaon");
				isofields.put("114.10", "99");
				isofields.put("114.117", "Avnish Kumar");
				isofields.put("114.150", "Deepak Kumar");
				isofields.put("120.64", "EzSwype Sever side building ISO8583 message for sub fields");
				isofields.put("120.120", "Here parsing one more subfield");
				//TESTING: ISOMESSAGE PACKING
				String isoMessage = packIsoMsg("0200",isofields);
				System.out.println("Packed iso8583 Message: "+isoMessage+"\n");	
				//Sends data to server
				//-------- // networkTransport(isoMessage);
				networkTransport(isoMessage);
				//TESTING: ISOMESSAGE UNPACKING
				//unpackIsoMsg(isoMessage);		
				//System.out.println("Unpacked iso8583 Message" + PARSEDISOMESSAGE);
			}
		public static String packIsoMsg(String MTI, HashMap <String, String> isofields) throws Exception{
			Set<?> set = isofields.entrySet();
			Iterator<?> iii = set.iterator();
			ArrayList<String> keys = new ArrayList<String>();
			HashMap<String, ArrayList<Integer>> subFiledBitMapHolder = new HashMap<String, ArrayList<Integer>>(); 
			//Lets start building the ISO Message
			ISOMESSAGE.put("MTI", MTI);
			while(iii.hasNext()){
			   @SuppressWarnings("rawtypes")
			   Map.Entry me = (Map.Entry)iii.next();
			   String fields = me.getKey().toString();
			   keys.add(fields);
		       //Get Schema of fields
		       String dataType = null;
			   String fieldLenType = null;
			   String fieldMaxLen = null;
			   String subfieldIndicator = null;
			   @SuppressWarnings("unused")
			   String hasSubfieldBitmap = null;
			   String breakFieldForSubField[] = null;
		       String baseField = null;
		       Integer subField = null;      
		       try{
			       breakFieldForSubField = fields.split("[.]");
			       baseField = breakFieldForSubField[0].toString();
			       subField = Integer.parseInt(breakFieldForSubField[1]);
			       try{
			    	   subFiledBitMapHolder.get(baseField).add(subField);
			       }catch(Exception e){
			    	   ArrayList<Integer> arrSubFields = new ArrayList<Integer>();
			    	   arrSubFields.add(subField);
			    	   subFiledBitMapHolder.put(baseField, arrSubFields);
			       }			       
			   }catch(Exception e){
				   //Nothing to do here
		       }		      
		       String schema = ISOSCHEMA.get(fields);
		       String arrSchema[] = schema.split("-");
		       dataType = arrSchema[0];
		       fieldLenType = arrSchema[1];
		       fieldMaxLen = arrSchema[2];
		       subfieldIndicator = arrSchema[3];
		       String arrSubField[] = subfieldIndicator.split("_");		       
		       hasSubfieldBitmap = arrSubField[1];		       
		       if(dataType.equalsIgnoreCase("NUM") && fieldLenType.equalsIgnoreCase("1")){
		    	   throw new IOException("Field:" +fields + " has data type NUM is having field-size = 1 in ISOSCHEMA. Try assign NUMERIC data type");
		       }		       
	    	   String fieldVlaue = isofields.get(fields);
	    	   Integer fieldLength = fieldVlaue.length();	    	   
	    	   String strfieldLength = fieldLength.toString();	    	   
	    	   if(dataType.equalsIgnoreCase("NUMERIC")){   
	    		   if(Integer.parseInt(fieldMaxLen) >= fieldLength){
	    			   String newFieldValue = "";
	    			   if (fieldVlaue.equals("")){
	    				   newFieldValue = String.format("%0"+ fieldMaxLen +"d", 0);
	    			   }else if (fieldLength == Integer.parseInt(fieldMaxLen)){
	    				   newFieldValue = fieldVlaue;
	    			   }else{
	    				   newFieldValue = String.format("%0"+ fieldMaxLen +"d", Long.parseLong(fieldVlaue));
	    			   }	            	   
	    			   ISOMESSAGE.put(fields, newFieldValue);
	    		   }else{
	    			   throw new IOException("Field:"+fields + " Has bigger value. Its set "+fieldMaxLen +" in ISOSCHEMA and you have entered" + fieldLength );
	    		   }
	    	   }else if (dataType.equalsIgnoreCase("CHAR") || dataType.equalsIgnoreCase("NUM")){
	    		   if(Integer.parseInt(fieldMaxLen) >= fieldLength) //?????????? fieldLength OR fieldMaxLen -- Discuss with Sagar
	    			   {
	    			   String newFieldLen = String.format("%0"+ fieldLenType +"d", Long.parseLong(strfieldLength));
	    			   
	    			   /////
		    		   ISOMESSAGE.put(fields.toString(), newFieldLen + fieldVlaue);
		    		   ////
	    		   }else{
	    			   throw new IOException("Field:"+fields + " Has bigger value. Its set "+fieldMaxLen +" in ISOSCHEMA and you have entered" + fieldLength );
	    		   }
	    	   }else if (dataType.equalsIgnoreCase("FCHAR")){
	    		   if(Integer.parseInt(fieldMaxLen) >= fieldLength) {	    			   
	    			   String newFieldValue = String.format("%-"+ fieldMaxLen +"s", fieldVlaue);
		    		   ISOMESSAGE.put(fields.toString(),  newFieldValue);
	    		   }else{
	    			   throw new IOException("Field:"+fields + " Has bigger value. Its set "+fieldMaxLen +" in ISOSCHEMA and you have entered" + fieldLength );
	    		   }
	    	   }else if (dataType.equalsIgnoreCase("PCHAR")) {
	    		   if(Integer.parseInt(fieldMaxLen) >= fieldLength) {
	    			   String newFieldValue = String.format("%"+ fieldMaxLen +"s", fieldVlaue);
	    			   newFieldValue = newFieldValue.replaceAll(" ", "0");
	    			   /////
		    		   ISOMESSAGE.put(fields.toString(),  newFieldValue);
		    		   ////
	    		   }else{
	    			   throw new IOException("Field:"+fields + " Has bigger value. Its set "+fieldMaxLen +" in ISOSCHEMA and you have entered" + fieldLength );
	    		   }
	    	   }else if (dataType.equalsIgnoreCase("BINARY")){
	    		   if(Integer.parseInt(fieldMaxLen) >= fieldLength){
	    				StringBuffer sb = new StringBuffer();
	    				String fPa;
	    				if (fieldLength % 2 == 0){ // Even number of chars, so there's no padding at the end	    				
	    				  for (int iRe = 0; iRe < fieldLength; iRe += 2){    		
	    					  fPa = fieldVlaue.substring(iRe, iRe + 2);
	    					  sb.append(CtoX(fPa));
	    				  }
	    				}else{
	    				  int iRe;
	    				  fPa = "";
	    				  fPa = fPa + fieldVlaue.charAt(0);
	    				  sb.append(CtoX(fPa)); // Get the first char from the second nibble
	    				  for (iRe = 1; iRe < fieldLength; iRe += 2){
	    					  fPa = fieldVlaue.substring(iRe, iRe + 2);
	    					  sb.append(CtoX(fPa));
	    				  }
	    				}
		    		   ISOMESSAGE.put(fields.toString(),  sb.substring(0, sb.length()));
	    		   }else{
	    			   throw new IOException("Field:"+fields + " Has bigger value. Its set "+fieldMaxLen +" in ISOSCHEMA and you have entered" + fieldLength );
	    		   }
	    	   }
		  }
			System.out.println("keys::::"+keys);
		  //Process Bitmap - Add 1 and 65 number Data Elements
		  ArrayList<Integer> finalFields = new ArrayList<Integer>();	
		  finalFields = processBitmap(parseFields(keys)); 		  
		  //Process Subfield bitmap - Add 1 and 65 for each field that has subfields
		   processSubFieldBitmap(subFiledBitMapHolder);		    
		   return buildISOMessage(finalFields);		   
		}
		/**
		 * This method assembles the entire ISO Message in a String
		 * @param finalFields
		 * @return String
		 * @throws Exception
		 */
		public static String buildISOMessage(ArrayList<Integer> finalFields) throws Exception{
			String isoMessage = ISOMESSAGE.get("MTI");
			isoMessage = isoMessage + ISOMESSAGE.get("1");			
			Iterator<?> j = finalFields.iterator();
			while(j.hasNext()){
				   String dataElement = j.next().toString();	
				   String schema = ISOSCHEMA.get(dataElement);
				   try{
					   String arrSchema[] = schema.split("-");					   
				       String fieldLenType = arrSchema[1];					   
					   String subfieldIndicator = arrSchema[3];
					   String arrSubField[] = subfieldIndicator.split("_");
				       String hasSubfield = arrSubField[0];			           
				       if(hasSubfield.equalsIgnoreCase("1")){
				    	   //Traverse subfields
				    	  ArrayList <Integer> subFields = new ArrayList<Integer>();
				    	  subFields = SUBFIELDSMAPPING.get(Integer.parseInt(dataElement));
				    	  Iterator<?> i = subFields.iterator();
				    	  String isoSubMessage = "";
				    	  if(!subFields.contains(1)){
				    		  isoSubMessage = isoSubMessage + ISOMESSAGE.get(dataElement + ".1");
				    		  
				    	  }while(i.hasNext()){
				 			  String subDataElement = i.next().toString();
				 			  String mainDataElement = dataElement + "." + subDataElement;
				 			  isoSubMessage = isoSubMessage + ISOMESSAGE.get(mainDataElement);
				 		  }
			    		  String subMessageLen = String.format("%0"+ fieldLenType +"d", isoSubMessage.length());
				 		  isoMessage = 	isoMessage + subMessageLen + isoSubMessage; 
				       }else{			    	   
				    	   isoMessage = isoMessage + ISOMESSAGE.get(dataElement);
				       }				       
				   }catch(Exception e){
					   //System.out.println(" has problem with schema");
				   }
			}
			return isoMessage;
		}		
		/**
		 * Create array list of all mail Data Elements that are available. This method is used in method packIsoMsg
		 * @param fields
		 * @return ArrayList
		 * @throws Exception
		 */
		public static ArrayList<Integer> parseFields(ArrayList<String> fields) throws Exception{			
			ArrayList<Integer> newFiledMap = new ArrayList<Integer>();
			Iterator<?> j = fields.iterator();
			  while(j.hasNext()){
				  try{
					  String breakFieldForSubField[] = j.next().toString().split("[.]");
				      Integer baseField = Integer.parseInt(breakFieldForSubField[0].toString());				      
				      if(!newFiledMap.contains(baseField)){
				    	  newFiledMap.add(baseField);
				      }
				  }catch(Exception e){
					  Integer keys = Integer.parseInt(j.next().toString());
					  newFiledMap.add(keys);
				  }
			  }			
			//Sort fields in assending order
			Collections.sort(newFiledMap);    
			return newFiledMap;
		}
		
		/**
		 * This is helper method used in the packIsoMsg method to process BITMAP
		 * @param fields
		 * @return Arraylist
		 * @throws Exception
		 */
		public static ArrayList<Integer> processBitmap(ArrayList<Integer> fields) throws Exception{
			String bitmapType = "primary";
			char[] bitMap;			
			//Retrive heighest Data Element from the list
			Integer DE = fields.get(fields.size() - 1);			
			//Know the type of bitmap (primary, secondary, tertiary
			if(DE > 65 && DE <= 128){
				fields.add(1); //Add 1st Bit for secondary bitmap
				bitmapType = "secondary";
				bitMap = new char[16];
			}else if(DE > 128){
				fields.add(1); //Add 1st Bit for secondary bitmap
				fields.add(65); //Add 65th Bit for tertiary bitmap
				ISOMESSAGE.put("65", "1");
				bitmapType = "tertiary";
				bitMap = new char[24];				
			}else{
				bitmapType = "primary";
				bitMap = new char[8];
			}
			//Sort ArrayList again 
			System.out.println("fields::::::::"+fields);
			Collections.sort(fields);
			//Append BITMAP to the message
			CalcBitMap(bitMap, fields);
			ISOMESSAGE.put("1", String.valueOf(bitMap, 0, bitMap.length));		
			return fields;
		}
		/**
		 * This is helper method used in the packIsoMsg method. To process SubFields
		 * @param subFieldMap
		 * @throws Exception
		 */
		@SuppressWarnings("unchecked")
		public static void processSubFieldBitmap(HashMap<String, ArrayList<Integer>> subFieldMap) throws Exception{
			Set<?> set = subFieldMap.entrySet();
			Iterator<?> i = set.iterator();
			char[] bitMap;
			while(i.hasNext()){
				@SuppressWarnings("rawtypes")
				Map.Entry me = (Map.Entry)i.next();
				ArrayList <Integer> fields = new ArrayList<Integer>();
				fields = (ArrayList<Integer>) me.getValue();
				String subFieldId = me.getKey().toString();
				//System.out.println(fields);
				Collections.sort(fields); //Sort the arrayList		
				String bitmapType = "primary";
				//Retrive heighest Data Element from the list
				Integer DE = fields.get(fields.size() - 1);				
				//Know the type of bitmap (primary, secondary, tertiary
				if(DE > 65 && DE <= 128){
					fields.add(1); //Add 1st Bit for secondary bitmap
					bitmapType = "secondary";
					bitMap = new char[16];
				}else if(DE > 128){
					fields.add(1); //Add 1st Bit for secondary bitmap
					fields.add(65); //Add 65th Bit for tertiary bitmap
					ISOMESSAGE.put(subFieldId + ".65", "1");
					bitmapType = "tertiary";
					bitMap = new char[24];
				}else{
					bitmapType = "primary";
					bitMap = new char[8];
				}
				System.out.println("bitMap:"+bitMap.toString());
				//Sort ArrayList again 
				Collections.sort(fields);				
				//Maintain Subfield Mapping
				SUBFIELDSMAPPING.put(Integer.parseInt(subFieldId), fields);				
				//Append BITMAP to the message
				CalcBitMap(bitMap, fields);
				ISOMESSAGE.put(subFieldId+".1", String.valueOf(bitMap, 0, bitMap.length));		 	
			}
		}
		
		/**
		 * This function uppack/Parse ISO8583 encoded message and stores the output in HASHMAP -> PARSEDISOMESSAGE
		 * @param isoMessage
		 * @throws Exception
		 */
		public static void unpackIsoMsg(String isoMessage) throws Exception{
			 String overallBitmap = null;			 
			 String messageAfterBitMap = null;			 
			 if(ISSUBFIELDPARSING == false)
				 PARSEDISOMESSAGE.put("MTI",isoMessage.substring(0, 4));			 
			 String priMaryHexBitMap = "";
			 if(ISSUBFIELDPARSING == false)
				 priMaryHexBitMap = isoMessage.substring(4, 12);
			 else
				 priMaryHexBitMap = isoMessage.substring(0, 8);			 
			 //Convert BITMAP to Binary
			 String primaryBitMap = GetBitMap(priMaryHexBitMap ,1);			 
			 overallBitmap = primaryBitMap;
			 //Check if Secondary bitMap is available or not
			 Integer firstBit = Integer.parseInt(primaryBitMap.substring(0, 1));			 
			 //if firstBit = 1 it means secondary bitmap is available
			 String secondaryHexBitmap = null;
			 String secondaryBitMap = null;
			 String tertiaryHexBitmap = null;
			 String tertiaryBitMap = null;
			 int bitmaplength = 64; 			 
			 if(firstBit > 0){
				bitmaplength = 128;
				if(ISSUBFIELDPARSING == false)
					secondaryHexBitmap = isoMessage.substring(12, 20);
				else
					secondaryHexBitmap = isoMessage.substring(8, 16);
				
				secondaryBitMap = GetBitMap(secondaryHexBitmap, 1);
				overallBitmap = overallBitmap + secondaryBitMap;
				//if 65th field is binary then there is a tertiary bitmap
				Integer firstBitOfSecBitmap = Integer.parseInt(secondaryBitMap.substring(0, 1));
				if(firstBitOfSecBitmap > 0){
					 bitmaplength = 192;
					 if(ISSUBFIELDPARSING == false)
						 tertiaryHexBitmap = isoMessage.substring(20, 28);
					 else
						 tertiaryHexBitmap = isoMessage.substring(16, 24);
					 
					 tertiaryBitMap = GetBitMap(tertiaryHexBitmap, 1);
					 overallBitmap = overallBitmap + tertiaryBitMap;
					 if(ISSUBFIELDPARSING == false)
						 messageAfterBitMap = isoMessage.substring(28); //After MTI and Primary bitmap
					 else
						 messageAfterBitMap = isoMessage.substring(24); //After MTI and Primary bitmap
				}else{
					if(ISSUBFIELDPARSING == false)
						messageAfterBitMap = isoMessage.substring(20); //After MTI and Primary bitmap
					else
						messageAfterBitMap = isoMessage.substring(16); //After MTI and Primary bitmap
				}
			 }
			 else{ //Secondary bitmap is not available so remaining message is actual data			 
				 if(ISSUBFIELDPARSING == false)
					 messageAfterBitMap = isoMessage.substring(12); //After MTI and Primary bitmap
				 else
					 messageAfterBitMap = isoMessage.substring(8); //After MTI and Primary bitmap
			 }			 
			 //Traverse the overall bitmap string
			 ArrayList<Integer> debugList = new ArrayList<Integer>(); //This is just for debugging purpose			 
			 String remainingMessage = null;
			 for(int i=0;i<bitmaplength;i++){
				 //now figure out which fields are available
				 char bit =  overallBitmap.charAt(i);
				 if(bit == '1'){
					 debugList.add(i+1); //This is just for debugging purpose.
					 Integer field = i+1;					 
					 String dataType = null;
					 String fieldLenType = null;
					 String fieldMaxLen = null;
					 String subfieldIndicator = null;
					 String hasSubfield = null;
					 String hasSubfieldBitmap = null;
					 if(field > 1){ //Exclude 1st Field which is reserve for bitmap
				     	   String schema = null;
						   try{
							   schema = ISOSCHEMA.get(SUBFIELDID+field.toString());
						   }catch(Exception e){
							   throw new IOException(field + " has problem with schema.");
						   }						   
						   try{
						       String arrSchema[] = schema.split("-");						       
						       dataType = arrSchema[0];
						       fieldLenType = arrSchema[1];
						       fieldMaxLen = arrSchema[2];
						       subfieldIndicator = arrSchema[3];
						       String arrSubField[] = subfieldIndicator.split("_");
						       hasSubfield = arrSubField[0];
						       hasSubfieldBitmap = arrSubField[1];
						   }catch(Exception e){
							   throw new IOException(field + " has problem with schema.");
						   }
						   
						   if(dataType.equalsIgnoreCase("NUMERIC")){
					    	   if(remainingMessage == null){
					    		   String fieldValue = messageAfterBitMap.substring(0,Integer.parseInt(fieldMaxLen));
					    		   if(fieldValue == null){
					    			   throw new IOException(field + " Has null or inappropriate value");
					    		   }					    		   
					    		   if(hasSubfield.equalsIgnoreCase("1")){
							    	   ISSUBFIELDPARSING = true;
							    	   SUBFIELDID = field+".";
							    	   unpackIsoMsg(fieldValue);
							       }else{
							    	    PARSEDISOMESSAGE.put(SUBFIELDID+field.toString(), fieldValue); //Lets start pushing parsed fields into Hash Map
							       }
					    		   remainingMessage = messageAfterBitMap.substring(Integer.parseInt(fieldMaxLen));
					    	   }
					    	   else{ //operation on remainingMessage
					    		   String fieldValue = remainingMessage.substring(0,Integer.parseInt(fieldMaxLen));					    		   
					    		   if(fieldValue == null){
					    			   throw new IOException(field + " Has null or inappropriate value");
					    		   }					    		   
					    		   if(hasSubfield.equalsIgnoreCase("1")){
							    	   ISSUBFIELDPARSING = true;
							    	   SUBFIELDID = field+".";
							    	   unpackIsoMsg(fieldValue);
							       }else{
							    	    PARSEDISOMESSAGE.put(SUBFIELDID+field.toString(), fieldValue); //Lets start pushing parsed fields into Hash Map
							       }
					    		   remainingMessage = remainingMessage.substring(Integer.parseInt(fieldMaxLen));
					    	   }
				    	   }else if(dataType.equalsIgnoreCase("FCHAR")){
					    	   if(remainingMessage == null){
					    		   String fieldValue = messageAfterBitMap.substring(0,Integer.parseInt(fieldMaxLen)).trim();
					    		   
					    		   if(fieldValue == null){
					    			   throw new IOException(field + " Has null or inappropriate value");
					    		   }
					    		   
					    		   if(hasSubfield.equalsIgnoreCase("1")){
							    	   ISSUBFIELDPARSING = true;
							    	   SUBFIELDID = field+".";
							    	   unpackIsoMsg(fieldValue);
							       }else{
							    	    PARSEDISOMESSAGE.put(SUBFIELDID+field.toString(), fieldValue); //Lets start pushing parsed fields into Hash Map
							       }
					    		   remainingMessage = messageAfterBitMap.substring(Integer.parseInt(fieldMaxLen));
					    	   }else{ //operation on remainingMessage
					    		   String fieldValue = remainingMessage.substring(0,Integer.parseInt(fieldMaxLen)).trim();
					    		   if(fieldValue == null){
					    			   throw new IOException(field + " Has null or inappropriate value");
					    		   }					    		   
					    		   if(hasSubfield.equalsIgnoreCase("1")){
							    	   ISSUBFIELDPARSING = true;
							    	   SUBFIELDID = field+".";
							    	   unpackIsoMsg(fieldValue);
							       }else{
							    	    PARSEDISOMESSAGE.put(SUBFIELDID+field.toString(), fieldValue); //Lets start pushing parsed fields into Hash Map
							       }
					    		   remainingMessage = remainingMessage.substring(Integer.parseInt(fieldMaxLen));
					    	   }
				    	   }else if(dataType.equalsIgnoreCase("PCHAR")){
					    	   if(remainingMessage == null){
					    		   String fieldValue = messageAfterBitMap.substring(0,Integer.parseInt(fieldMaxLen)).replaceFirst("^0+(?!$)", "");
					    		   if(fieldValue == null){
					    			   throw new IOException(field + " Has null or inappropriate value");
					    		   }					    		   
					    		   if(hasSubfield.equalsIgnoreCase("1")){
							    	   ISSUBFIELDPARSING = true;
							    	   SUBFIELDID = field+".";
							    	   unpackIsoMsg(fieldValue);
							       }else{
							    	    PARSEDISOMESSAGE.put(SUBFIELDID+field.toString(), fieldValue); //Lets start pushing parsed fields into Hash Map
							       }
					    		   remainingMessage = messageAfterBitMap.substring(Integer.parseInt(fieldMaxLen));
					    	   }else{ //operation on remainingMessage
					    		   String fieldValue = remainingMessage.substring(0,Integer.parseInt(fieldMaxLen)).replaceFirst("^0+(?!$)", "");
					    		   if(fieldValue == null){
					    			   throw new IOException(field + " Has null or inappropriate value");
					    		   }					    		   
					    		   if(hasSubfield.equalsIgnoreCase("1")){
							    	   ISSUBFIELDPARSING = true;
							    	   SUBFIELDID = field+".";
							    	   unpackIsoMsg(fieldValue);
							       }else{
							    	    PARSEDISOMESSAGE.put(SUBFIELDID+field.toString(), fieldValue); //Lets start pushing parsed fields into Hash Map
							       }
					    		   remainingMessage = remainingMessage.substring(Integer.parseInt(fieldMaxLen));
					    	   }
				    	   }else if(dataType.equalsIgnoreCase("BINARY")){
					    	   if(remainingMessage == null){
					    		   String fieldValue = messageAfterBitMap.substring(0,(Integer.parseInt(fieldMaxLen)/2));
					    		   System.out.println(fieldValue);
					    		   if(fieldValue == null){
					    			   throw new IOException(field + " Has null or inappropriate value");
					    		   }					    		   
					    		   String originalMsg="";
					    		   for (int x = 0; x < fieldValue.length(); x++) {
					    		    	int value = (int)fieldValue.charAt(x);
					    		    	String originalValue = Integer.toHexString(value);

					    		    	originalMsg = originalMsg + originalValue;
					    		    }					    		   
					    		   fieldValue = Integer.toString(Integer.parseInt(originalMsg,16));
					    		   PARSEDISOMESSAGE.put(SUBFIELDID+field.toString(), fieldValue); //Lets start pushing parsed fields into Hash Map
							       remainingMessage = messageAfterBitMap.substring(Integer.parseInt(fieldMaxLen)/2);
					    	   }else{ //operation on remainingMessage
					    		   String fieldValue = remainingMessage.substring(0,(Integer.parseInt(fieldMaxLen)/2));
					    		   if(fieldValue == null){
					    			   throw new IOException(field + " Has null or inappropriate value");
					    		   }
					    		   String originalMsg="";
					    		   for (int x = 0; x < fieldValue.length(); x++) {
					    		    	int value = (int)fieldValue.charAt(x);
					    		    	String originalValue = Integer.toHexString(value);

					    		    	originalMsg = originalMsg + originalValue;
					    		    }					    		   
					    		   fieldValue = Integer.toString(Integer.parseInt(originalMsg,16));
					    		   PARSEDISOMESSAGE.put(SUBFIELDID+field.toString(), fieldValue); //Lets start pushing parsed fields into Hash Map
					    		   remainingMessage = remainingMessage.substring(Integer.parseInt(fieldMaxLen)/2);
					    	   }
				    	   }else if(dataType.equalsIgnoreCase("CHAR") || dataType.equalsIgnoreCase("NUM")){
				    		   if(remainingMessage == null){
				    			   String fieldlength = messageAfterBitMap.substring(0,Integer.parseInt(fieldLenType));
				    			   remainingMessage = messageAfterBitMap.substring(Integer.parseInt(fieldLenType));
				    			   String fieldValue = remainingMessage.substring(0,Integer.parseInt(fieldlength));
				    			   if(fieldValue == null){
					    			   throw new IOException(field + " Has null or inappropriate value");
					    		   }
				    			   if(hasSubfield.equalsIgnoreCase("1")){
							    	   ISSUBFIELDPARSING = true;
							    	   SUBFIELDID = field+".";
							    	   unpackIsoMsg(fieldValue);
							       }else{
							    	    PARSEDISOMESSAGE.put(SUBFIELDID+field.toString(), fieldValue); //Lets start pushing parsed fields into Hash Map
							       }
				    			   remainingMessage = remainingMessage.substring(Integer.parseInt(fieldlength));				    			   
					    	   }else{ //operation on remaining message				    		   
				    			   String fieldlength = remainingMessage.substring(0,Integer.parseInt(fieldLenType));				    		
				    			   remainingMessage = remainingMessage.substring(Integer.parseInt(fieldLenType));				    			   
				    			   String fieldValue = remainingMessage.substring(0,Integer.parseInt(fieldlength));
				    			   if(fieldValue == null){
					    			   throw new IOException(field + " Has null or inappropriate value");
					    		   }				    			   
				    			   if(hasSubfield.equalsIgnoreCase("1")){
							    	   ISSUBFIELDPARSING = true;
							    	   SUBFIELDID = field+".";
							    	   unpackIsoMsg(fieldValue);
							       }else{
							    	    PARSEDISOMESSAGE.put(SUBFIELDID+field.toString(), fieldValue); //Lets start pushing parsed fields into Hash Map
							       }
				    			   remainingMessage = remainingMessage.substring(Integer.parseInt(fieldlength));
				    		   }
				    	   }
				       }
				 }
			 }			 
			 ISSUBFIELDPARSING = false;
		     SUBFIELDID = "";			 
			 //System.out.println("Parsed Message " + PARSEDISOMESSAGE); 
		}	
		/**
		 * This Method generates the binary representation of the BITMAP.
		 * @input= Arraylist for the subfields present 
		 * @return String containing bitmap msg
		 * @throws Exception 
		 * 
		 */
		public static String CalcBitMap(char[] bitMap, ArrayList<Integer> list) throws Exception{
			for(int i=0;i<list.size();i++){
				int iPos = (list.get(i) / 8);
				int iPosIn = list.get(i) - (iPos * 8);
				if(iPosIn == 0){
					iPos--;
					iPosIn = 8;
				}	
				if((65 <= list.get(i))  && (list.get(i) < 128))
					bitMap[ 0 ] |= 1 << 7;
				if((128 <= list.get(i))  && (list.get(i) < 192))
					bitMap[ 8 ] |= 1 << 7;
					
				bitMap[ iPos ] |= 1 << (8 - iPosIn);
			}
			return "OK";
		}	
		/**
		 * This method calculates the BITMAP of available fields in the message.
		 * It converts the binary representation into Hex representation
		 * and then Hex representation into ACSCII and binds to the ISO8583 message
		 * @param bin
		 * @throws Exception
		 */
		public static String GetBitMap(String bin, int nouse) throws Exception  {  
			int iCo = 0;
			StringBuffer binRes = new StringBuffer(64);
			for(iCo = 0; iCo < 8; iCo++){
				if((bin.charAt(iCo) & 128) > 0) binRes.append('1'); else binRes.append('0');
				if((bin.charAt(iCo) & 64) > 0) binRes.append('1'); else binRes.append('0');
				if((bin.charAt(iCo) & 32) > 0) binRes.append('1'); else binRes.append('0');
				if((bin.charAt(iCo) & 16) > 0) binRes.append('1'); else binRes.append('0');
				if((bin.charAt(iCo) & 8) > 0) binRes.append('1'); else binRes.append('0');
				if((bin.charAt(iCo) & 4) > 0) binRes.append('1'); else binRes.append('0');
				if((bin.charAt(iCo) & 2) > 0) binRes.append('1'); else binRes.append('0');
				if((bin.charAt(iCo) & 1) > 0) binRes.append('1'); else binRes.append('0');
			}
			return binRes.toString();
		}
	  /**
	   * Method for Replacing the char in string at provided location
	   * @input pos-Int position where to replace character
	   *        char-char to be replace at provided pos
	   *        String- s which is to be formated
	   * 
	   * @return Re-formated String       
	   * 
	   * */
		public static String replaceCharAt(String s, int pos, char c) throws Exception{
			   return s.substring(0,pos) + c + s.substring(pos+1);
		}
		
		/**
		 * Type cast to char data type
		 * @param x
		 * @return
		 */
		public static char CtoX(String x){
			int r = 0;
			r = Integer.parseInt(x,16);
			return (char)r;
		}
		
		/**
		 * This method sends ISO8583 message to server and accepts the response.
		 * 
		 * @param isoMessage
		 * @return String
		 * @throws UnknownHostException
		 * @throws IOException
		 */
		
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
		public static String networkTransport(String isoMessage)
				throws UnknownHostException, IOException{
			String ogsResponse = "";
			try{
				System.out.println("purchage txn iso message:"+isoMessage);
				//isoMessage="00000000000000000000"+isoMessage;// This is required with DUKPT enabled on our side to send a clear message
				// Ensure the xml that you are using has the correct data types and length as mentioned in the specification otherwise Switch will not be able to parse.
				Integer isoLength = isoMessage.length()+2;
				System.out.println("Integer.toHexString(isoLength):"+Integer.toHexString(isoLength));
				String Strlength = Integer.toHexString(isoLength).toString();
				String hexString = "";
				if(Strlength.length()==3){
					hexString = "0"+Integer.toHexString(isoLength);
				}else{
					hexString = "00"+Integer.toHexString(isoLength);
				}
				
				System.out.println("purchage txn hex string is :"+hexString);
				//byte[] bcdArray = toBcd(hexString);// issue with the length that is sent. we are not able to parse it.
				byte[] bcdArray = str2Bcd(hexString);
				System.out.println("purchage txn byte arr: "+bcdArray);
				String bcdString = new String(bcdArray);
	    		String completeIsoMsg = bcdString+isoMessage;
	    		System.out.println("purchage txn completeIsoMsg: "+completeIsoMsg);
	    		Socket connection = new Socket("115.112.71.226", 3555);
	    		connection.setSoTimeout (30000);
	    		BufferedOutputStream bos = new BufferedOutputStream(connection.getOutputStream());
				OutputStreamWriter osw = new OutputStreamWriter(bos);
				System.out.println("purchage txn completeIsoMsg length:"+completeIsoMsg.length());
				osw.write(completeIsoMsg);
				osw.flush();
				byte[] arrOutut = new byte[4096];
				System.out.println("purchage txn soket connection going connect");
				int count = connection.getInputStream().read(arrOutut, 0, 4096);
				System.out.println("purchage txn response from ogs");
				for (int outputCount = 0; outputCount < count; outputCount++){
					char response = (char)arrOutut[outputCount];
					ogsResponse = ogsResponse + response;
				}// in morning also comming same exception i said to vijay he resolved it
				System.out.println("purchage txn ogs response: "+ogsResponse);
				//unpackIsoMsg(response);
				//System.out.println("Unpacked iso8583 Message" + response);
				connection.close();
			}catch(Exception e){
				System.err.println("error to purchage txn "+e);
			}
			
			/*try{
				System.out.println("isoMessage:"+isoMessage);
				Integer isoLength = isoMessage.length()+2;
				String hexString ="00"+Integer.toHexString(isoLength);
				System.out.println("hexString is :"+hexString);
				byte[] bcdArray = toBcd(hexString);
				System.out.println("byteArr: "+bcdArray);
				String bcdString = new String(bcdArray);
	    		String completeIsoMsg = bcdString+isoMessage;
	    		System.out.println("completeIsoMsg:"+completeIsoMsg);	    		
	    		Socket connection = new Socket("115.112.71.226", 3555);
				BufferedOutputStream bos = new BufferedOutputStream(connection.getOutputStream());
				OutputStreamWriter osw = new OutputStreamWriter(bos);
				System.out.println("completeIsoMsg:::::"+completeIsoMsg.length());
				osw.write(completeIsoMsg);
				osw.flush();
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
			}catch(Exception e){
				System.err.println("error "+e);
			}		*/	
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
}