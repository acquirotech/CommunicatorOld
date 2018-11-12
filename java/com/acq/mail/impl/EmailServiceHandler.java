package com.acq.mail.impl;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.acq.mail.AcqEmailAttachment;
import com.acq.mail.AcqEmailServiceHandler;

@Service
public class EmailServiceHandler implements AcqEmailServiceHandler {

	private static Logger logger = Logger.getLogger(EmailServiceHandler.class);
	private static String newline = System.getProperty("line.separator");

	@Autowired
	VelocityEngine velocityEngine;
	
	 String from = "no-reply@acquiropayments.com";
   
	String host = "localhost";
    
    public void sendEmailWithCC(HashMap<String, Object> model,
			String templateLocation, String orgEmail, String toEmail) throws MailException{
    	try {
		    Properties properties = System.getProperties();
		    properties.setProperty("mail.smtp.host", host);
		    Session session = Session.getDefaultInstance(properties);
			try{
		         MimeMessage message = new MimeMessage(session);
		         message.setFrom(new InternetAddress(from));
		         message.addRecipient(Message.RecipientType.TO,new InternetAddress(toEmail));
		         message.addRecipient(Message.RecipientType.CC,new InternetAddress(orgEmail));
		         //String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine,getTemplateRootPath()+ templateLocation, model);
		         String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine,templateLocation, model);
		         String subject = text.substring(0, text.indexOf(newline));
					String body = text.substring(text.indexOf(newline) + 1,
							text.length());
		         message.setSubject(subject);
		         message.setContent(body,"text/html" ); 		         	         
		         javax.mail.Transport.send(message);
		      }catch (MessagingException mex) {
		    	  mex.printStackTrace();
		    	  logger.info("Error to sent email");
		      }
			logger.info("Mail sent " + model);
		} catch (Exception ex) {
			logger.error("Error sending email " + model, ex);
		}
    }
    public void sendVirtualEmail(final HashMap<String, Object> model,
			final String templateLocation, final String toEmail, String fromEmail) throws MailException{
		try {	
			System.out.println("Email going to send");
		      Properties properties = System.getProperties();
		      properties.setProperty("mail.smtp.host", host);
			Session session = Session.getDefaultInstance(properties);
			try{
		         MimeMessage message = new MimeMessage(session);
		         message.setFrom(new InternetAddress(fromEmail));
		         message.addRecipient(Message.RecipientType.TO,new InternetAddress(toEmail));			      
		         String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, templateLocation, model);
		         String subject = text.substring(0, text.indexOf(newline));
					String body = text.substring(text.indexOf(newline) + 1,
							text.length());
		         message.setSubject(subject);
		         message.setContent(body,"text/html"); 		
		         javax.mail.Transport.send(message);
		      }catch (MessagingException mex) {
		    	  //mex.printStackTrace();
		    	  logger.warn("there is email service proble to send email "+mex);
		      }catch(Exception e){
		    	  logger.warn("Permission denied: connect to email server "+e);
		      }
			logger.info("Mail sent " + model);
		} catch (Exception ex) {
			logger.error("Error sending email " + model, ex);			
		}
	}
	public void sendEmail(final HashMap<String, Object> model,
			final String templateLocation, final String toEmail) throws MailException{
		try {	
			System.out.println("Email going to send");
		      Properties properties = System.getProperties();
		      properties.setProperty("mail.smtp.host", host);
			Session session = Session.getDefaultInstance(properties);
			try{
		         MimeMessage message = new MimeMessage(session);
		         
		        	 message.setFrom(new InternetAddress(from));				        
		         
		         message.addRecipient(Message.RecipientType.TO,new InternetAddress(toEmail));			      
		         String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, templateLocation, model);
		         String subject = text.substring(0, text.indexOf(newline));
					String body = text.substring(text.indexOf(newline) + 1,
							text.length());
		         message.setSubject(subject);
		         message.setContent(body,"text/html"); 		
		         javax.mail.Transport.send(message);
		      }catch (MessagingException mex) {
		    	  //mex.printStackTrace();
		    	  logger.warn("there is email service proble to send email "+mex);
		      }catch(Exception e){
		    	  logger.warn("Permission denied: connect to email server "+e);
		      }			
			logger.info("Mail sent " + model);
		} catch (Exception ex) {
			logger.error("Error sending email " + model, ex);			
		}
	}
	
	public void sendEmailWithCcWriter(HashMap<String, Object> model,
		      String templateLocation, String orgEmail, String toEmail) throws MailException{
		        try {   
		         Properties properties = System.getProperties();
		         properties.setProperty("mail.smtp.host", host);
		         Session session = Session.getDefaultInstance(properties);
		      try{
		              MimeMessage message = new MimeMessage(session);
		              message.setFrom(new InternetAddress(from));
		              message.addRecipient(Message.RecipientType.TO,new InternetAddress(toEmail));
		              message.addRecipient(Message.RecipientType.CC,new InternetAddress(orgEmail));
		           //   String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine,getTemplateRootPath()+ templateLocation, model);
		            
		              final StringWriter writer = new StringWriter();
		              VelocityEngineUtils.mergeTemplate(velocityEngine,  templateLocation, model, writer);
		      String text=writer.toString();
		              
		              String subject = text.substring(0, text.indexOf(newline));
		        String body = text.substring(text.indexOf(newline) + 1,
		          text.length());
		              message.setSubject(subject);
		              message.setContent(body,"text/html" );                      
		              javax.mail.Transport.send(message);
		           }catch (MessagingException mex) {
		            mex.printStackTrace();
		            logger.info("Error to sent email");
		           }
		      logger.info("Mail sent " + model);
		     } catch (Exception ex) {
		      logger.error("Error sending email " + model, ex);
		     }
		       }
	
	private static void addAttachment(Multipart multipart, String filename) throws MessagingException{
	    DataSource source = new FileDataSource(filename);
	    BodyPart messageBodyPart = new MimeBodyPart();        
	    messageBodyPart.setDataHandler(new DataHandler(source));
	    messageBodyPart.setFileName(new File(filename).getName());
	    multipart.addBodyPart(messageBodyPart);
	}
	public void sendEmailWithAttachment(String orgEmail,String filePath,String htmlTemplate,HashMap<String,Object> model){
		try{
			System.out.println("filePath:-------------------------------------"+filePath);
	  		Properties properties = System.getProperties();
	  		properties.setProperty("mail.smtp.host", host);
	  		Session sess = Session.getDefaultInstance(properties);
	  		Message message = new MimeMessage(sess);
	  		message.setFrom(new InternetAddress(from));
	  		message.addRecipient(Message.RecipientType.TO,new InternetAddress(orgEmail));
	  		String text = VelocityEngineUtils.mergeTemplateIntoString(
					velocityEngine,htmlTemplate, model);
	  		String subject = text.substring(0, text.indexOf(newline));
			String body = text.substring(text.indexOf(newline) + 1,
					text.length());
	  		Multipart multipart = new MimeMultipart();
	  		BodyPart htmlPart = new MimeBodyPart();
	  		htmlPart.setContent(body, "text/html");
	  		htmlPart.setDisposition(BodyPart.INLINE);
			multipart.addBodyPart(htmlPart);
			message.setSubject(subject);	  		
	  		addAttachment(multipart, filePath);
	  		message.setContent(multipart);
	  		Transport.send(message);
	  		System.out.println("Msg Send ....");	
		}catch(Exception e){
			logger.info("Error to send product report email");
		}
	}
	public void sendAttachedEmail(String[] files)throws MessagingException{
		String host = "localhost";
		String from = "no-reply@acquiropayments.com";
		String to = "parul@acquiropayments.com";
		 Properties props = System.getProperties();
		 props.put("mail.smtp.host", host);
		  Session session = 
			      Session.getInstance(props, null);
		 Message msg = new MimeMessage(session);
		 msg.setFrom(new InternetAddress(from));
	        InternetAddress[] toAddresses = { new InternetAddress(to) };
	        msg.setRecipients(Message.RecipientType.TO, toAddresses);
	        msg.setSubject("Dumy Message for Attachements");
	        msg.setSentDate(new Date());
		 MimeBodyPart messageBodyPart = new MimeBodyPart();
		 Multipart multipart = new MimeMultipart();
		 multipart.addBodyPart(messageBodyPart);
		 //messageBodyPart.setContent(multipart);		 
		 if (files != null && files.length > 0) {
	            for (String filePath : files) {
	                MimeBodyPart attachPart = new MimeBodyPart();	 
	                try {
	                    attachPart.attachFile(filePath);
	                } catch (IOException ex) {
	                    ex.printStackTrace();
	                }	 
	                multipart.addBodyPart(attachPart);
	            }
	        }
		 	msg.setContent(multipart);
	        // sends the e-mail
	        javax.mail.Transport.send(msg);
	}
	
	
	public void sendEmailWithAttachments(final HashMap<String, Object> model,
			final String templateLocation, final String toEmail,final List<AcqEmailAttachment> attachments) throws MailException{
		try {
			String from = "no-reply@acquiropayments.com";	// Sms sending via SendEmail email server
		      String host = "localhost";
		      Properties properties = System.getProperties();
		      properties.setProperty("mail.smtp.host", host);
			Session session = Session.getDefaultInstance(properties);
			
			try{
		         MimeMessage message = new MimeMessage(session);
		        // MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true);
		         message.setFrom(new InternetAddress(from));
		         message.addRecipient(Message.RecipientType.TO,new InternetAddress(toEmail));
		         String text = VelocityEngineUtils.mergeTemplateIntoString(
							velocityEngine,templateLocation, model);
		         String subject = text.substring(0, text.indexOf(newline));
					String body = text.substring(text.indexOf(newline) + 1,
							text.length());
		         message.setSubject(subject);
		         message.setContent(body,"text/html" ); 
		         MimeBodyPart attachPart = new MimeBodyPart();
		         String attachFile = "C:/Users/Parul Tripathi/Desktop/Reports/avn.jpg";		          
		         DataSource source = new FileDataSource(attachFile);
		         attachPart.setDataHandler(new DataHandler(source));
		         attachPart.setFileName(new File(attachFile).getName());
		         Multipart multipart = new MimeMultipart();
		         multipart.addBodyPart(attachPart);
		         javax.mail.Transport.send(message);		         
		      }catch (MessagingException mex) {
		    	  mex.printStackTrace();
		    	  logger.info("Error to sent email");
		      }
			
			logger.info("Mail sent " + model);
		} catch (Exception ex) {
			logger.error("Error sending email " + model, ex);
			
		}
	}
}
