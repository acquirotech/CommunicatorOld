/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddlab.rnd.xml.digsig;

import java.io.File;

/**
 * This class is used as a test class to sign an xml document digitally.
 *
 * @author <a href="mailto:debadatta.mishra@gmail.com">Debadatta Mishra</a>
 * @since 2013
 */
public class TestDigitalSignature {
    /*
     * Main method to generate a digitally signed xml document.
     */
	
	public static void getValue(){
		
		String rootPath="C:\\Users\\Avnish Java\\Desktop\\airtel_money\\new\\";
		String xmlFilePath = rootPath+"request.xml";
	    String signedXmlFilePath = rootPath;//+"digitallysignedreq.xml";
	    //String keystoreFilePath = rootPath+"voltproductionkeystore";
	    //String keystoreFilePath = rootPath+"volt61keystore";
	    String keystoreFilePath = rootPath+"ezkeystore";
	    
	    //String keystoreFilePath = "keys" + File.separator + "volt61_public.cer";
	    String privateKeyAlias = "ezalias";
	    //String keyStorePassword = "Airtel123";
	    String keyStorePassword = "ezkeystorepass";
	    //String keyPassword = "Airtel123";
	    String keyPassword = "ezkeypass";
	    XmlDigitalSignatureGenerator xmlSig = new XmlDigitalSignatureGenerator();      
	   // System.out.println("aaaaaaaaaaaaaaaaaaaaaa");
	  //  System.out.println("xmlFilePath: "+xmlFilePath);
	   // System.out.println("signedXmlFilePath: "+signedXmlFilePath);
	   // System.out.println("keystoreFilePath: "+keystoreFilePath);
	  //  System.out.println("privateKeyAlias: "+privateKeyAlias);
	   // System.out.println("keyStorePassword: "+keyStorePassword);
	  //  System.out.println("keyPassword: "+keyPassword);
	    
	    xmlSig.generateXMLDigitalSignature(xmlFilePath, signedXmlFilePath, keystoreFilePath, privateKeyAlias,keyStorePassword.toCharArray(),keyPassword.toCharArray());
	    //System.out.println("zzzzzzzzzzzzzzzzzzzzzz");
	}
	
    public static void main(String[] args) {
    	getValue();
       /* String xmlFilePath = "xml" + File.separator + "request.xml";
        String signedXmlFilePath = "xml" + File.separator + "digitallysignedreq.xml";        
        String keystoreFilePath = "keys" + File.separator + "volt61keystore";
        //String keystoreFilePath = "keys" + File.separator + "volt61_public.cer";        
        String privateKeyAlias = "airtel_61";
        String keyStorePassword = "Airtel123";
        String keyPassword = "Airtel123";
        XmlDigitalSignatureGenerator xmlSig = new XmlDigitalSignatureGenerator();        
        xmlSig.generateXMLDigitalSignature(xmlFilePath, signedXmlFilePath, keystoreFilePath, privateKeyAlias,keyStorePassword.toCharArray(),keyPassword.toCharArray());
    */
    }
}
