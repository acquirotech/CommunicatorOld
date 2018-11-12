package com.acq.web.handler.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import com.acq.EnumStatusConstant;
import com.acq.ImageUtils;
import com.acq.MessageSender;
import com.acq.NumberValidator;
import com.acq.mail.AcqEmailServiceHandler;
import com.acq.web.controller.model.AcqPostTransactionModel;
import com.acq.web.controller.model.AcqSearchTransactionModel;
import com.acq.web.controller.model.AcqSendSmsModel;
import com.acq.web.controller.model.AcqTransactionDetailModel;
import com.acq.web.controller.model.AcqTransactionModel;
import com.acq.web.controller.model.AcqTransactionSignModel;
import com.acq.web.controller.model.AcqVoidTransactionModel;
import com.acq.web.controller.model.LastTxnDetailsModel;
import com.acq.web.dao.AcqDao;
import com.acq.web.dao.TransactionDaoInf;
import com.acq.web.dto.impl.DbDto;
import com.acq.web.dto.impl.ServiceDto;
import com.acq.web.handler.TransactionHandlerInf;

import security.AcquiroSeq;

enum Merchant{
	MoralAssociate
}



@Service
public class TransactionHandler extends AcqDao implements TransactionHandlerInf{
	final static Logger logger = Logger.getLogger(TransactionHandler.class);
		
	@Autowired
	TransactionDaoInf transactionDao;
		
	@Autowired
	MessageSource resource;
		
	
	
	@Value("#{acqConfig['signimage.location']}")
	private String signImgLocation;
		
	@Value("#{acqConfig['apphost.location']}")
	private String appHost;
	
		
	
	@Autowired
	AcqEmailServiceHandler acqEmailServiceHandler;

	@Override
	public ServiceDto<HashMap<String, HashMap<String, String>>> lastTxnDetails(
			LastTxnDetailsModel model, String clientIpAddress){
		ServiceDto<HashMap<String, HashMap<String, String>>>  response = new ServiceDto<HashMap<String, HashMap<String, String>>>();
		try{
		DbDto<HashMap<String, HashMap<String, String>>>  daoResponse = transactionDao.lastTxnDetails(model,clientIpAddress);
		    response.setMessage(daoResponse.getMessage());
		    response.setResult(daoResponse.getResult());
		    response.setStatus(daoResponse.getStatus());
		    try{
		    	HashMap<String,String> result = daoResponse.getResult().get("txnResponse");			    
		    	}catch(Exception e){
		    	logger.error("error to call moral card txn "+e);
		    }
		}catch(Exception e){
			logger.error("Error in post place transaction handler "+e);
		}
		return response;
	}
	
	@Override
	public ServiceDto<Object> sendSms(AcqSendSmsModel model) {
		ServiceDto<Object>  response = new ServiceDto<Object>();
	try{
		DbDto<HashMap<String,String>> daoResponse = transactionDao.sendCustomerSms(model);
		if(daoResponse.getStatus()==EnumStatusConstant.OK.getId()&&daoResponse.getMessage().equals(EnumStatusConstant.OK.getDescription())){
			HashMap<String,String> resultMap = daoResponse.getResult();
			if(!resultMap.isEmpty()&&NumberValidator.isPhoneNo(model.getPhone())){						
				try{
					logger.info("Send sms and url shortner thread is starting in send sms");
					AcqSmsAndUrlShortnerThread thread = new AcqSmsAndUrlShortnerThread();
					thread.setTransactionId(resultMap.get("txnId"));
					thread.setCustPhone(model.getPhone());
					thread.setAppHost(appHost);
					thread.setTxnType("REVERSAL");
					Thread t = new Thread(thread);
					t.start();
					System.out.println("sms thread started");
				}catch(Exception e){
					System.out.println("error to send sms and url shortner " +e);
					logger.error("error to send sms and url shortner " +e);
				}
			}
			
			 response.setStatus(EnumStatusConstant.OK.getId());
			response.setMessage(EnumStatusConstant.OK.getDescription());
			response.setResult(null);
			logger.info("response returing for send sms handler");
		}else{
			response.setStatus(daoResponse.getStatus());
			response.setMessage(daoResponse.getMessage());
			response.setResult(null);
		}
		return response;
	}catch(Exception e){
		logger.error("Error in send sms handler "+e);
	}
	return response;
	}	
	
	
	

	@Override
	public ServiceDto<List<HashMap<String, String>>> checkBalance(
			String walletId) {
		ServiceDto<List<HashMap<String, String>>>  response = new ServiceDto<List<HashMap<String, String>>>();
		  try{
		  DbDto<List<HashMap<String, String>>>  daoResponse = transactionDao.checkBalance(walletId);
		      response.setMessage(daoResponse.getMessage());
		      response.setResult(daoResponse.getResult());
		      response.setStatus(daoResponse.getStatus());
		  }catch(Exception e){
		   logger.error("Error in check balance handler "+e);
		  }
		  return response;
	}


	
	
	
	
	
	
	
	
	@Override
	public ServiceDto<HashMap<String, String>> getCardTxnDetails(
			String signImage){
		ServiceDto<HashMap<String, String>>  response = new ServiceDto<HashMap<String, String>>();
		try{
			DbDto<HashMap<String, String>>  daoResponse = transactionDao.getCardTxnDetails(signImage);
		    response.setMessage(daoResponse.getMessage());
			    response.setResult(daoResponse.getResult());
			    response.setStatus(daoResponse.getStatus());	 
			}catch(Exception e){
				logger.error("Error in transaction receipt handler "+e);
			}
			return response;
		}
	
	@Override
	public InputStream txnsignImageS(String signImage) {
		File image = new File(signImgLocation + File.separator+signImage+".jpeg");
		System.out.println("image:"+image);
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(image);
			return inputStream;		
		} catch (Exception ex) {
			System.out.println("error to get image");
		}
		System.out.println("inputStream:"+inputStream);
		return inputStream;
	}
	
		@Override
		public HttpEntity<byte[]> signImageS(String signImage) {
			HttpHeaders responseHeaders = new HttpHeaders();
			File image = new File(signImgLocation + File.separator+signImage+".jpeg");
			byte[] fileArray = null;
			FileInputStream inputStream = null;
			try {
				inputStream = new FileInputStream(image);
				fileArray = IOUtils.toByteArray(inputStream);
				inputStream.close();
			} catch (Exception ex) {
				responseHeaders.setContentType(MediaType.TEXT_PLAIN);
				return new HttpEntity<byte[]>(ex.getMessage().getBytes(),
						responseHeaders);
			}
			String fullFileName = signImgLocation + File.separator + signImage
					+ ".png";
			String extension = "";
			if (fullFileName.endsWith("jpeg") || fullFileName.endsWith("jpg")
					|| fullFileName.endsWith("JPEG")
					|| fullFileName.endsWith("JPG")) {
				responseHeaders.setContentType(MediaType.IMAGE_JPEG);
				extension = ".jpeg";

			}
			if (fullFileName.endsWith("png") || fullFileName.endsWith("PNG")) {
				responseHeaders.setContentType(MediaType.IMAGE_PNG);
				extension = ".png";
			}
			if (fullFileName.endsWith("gif") || fullFileName.endsWith("GIF")) {
				responseHeaders.setContentType(MediaType.IMAGE_GIF);
				extension = ".gif";
			}
			responseHeaders.setContentLength(fileArray.length);
			return new HttpEntity<byte[]>(fileArray, responseHeaders);
		}
		
		@Override
		public ServiceDto<HashMap<String, HashMap<String, String>>> placePostTransaction(
				AcqPostTransactionModel model, String clientIpAddress) {
			ServiceDto<HashMap<String, HashMap<String, String>>>  response = new ServiceDto<HashMap<String, HashMap<String, String>>>();
			try{
				DbDto<HashMap<String, HashMap<String, String>>>  daoResponse = transactionDao.placePostTransaction(model,clientIpAddress);
			    response.setMessage(daoResponse.getMessage());
			    response.setResult(daoResponse.getResult());
			    response.setStatus(daoResponse.getStatus());
			  }catch(Exception e){
				logger.error("Error in post place transaction handler "+e);
			}
			return response;
		}
		
		@Override
		public ServiceDto<Object> voidtransaction(AcqVoidTransactionModel model) {
			ServiceDto<Object>  response = new ServiceDto<Object>();
			try{
			DbDto<Object>  daoResponse = transactionDao.voidtransaction(model);
			    response.setMessage(daoResponse.getMessage());			    
			    response.setStatus(daoResponse.getStatus());			    
			    if(daoResponse.getStatus()==EnumStatusConstant.OK.getId()&&daoResponse.getMessage().equalsIgnoreCase(EnumStatusConstant.OK.getDescription())){
			    
			    	if(model.getMobile()!=null&&NumberValidator.isPhoneNo(model.getMobile())){
			    		logger.info("void txn send sms and url shortner thread going to start");
			    		
			    			AcqSmsAndUrlShortnerThread thread = new AcqSmsAndUrlShortnerThread();
				    		thread.setTransactionId(model.getTxnId());
				    		thread.setAmount(model.getAmount());
				    		thread.setDateTime(model.getDateTime());
				    		thread.setCardPanNo(model.getCardPanNo());
				    		thread.setMarketingName(model.getMarketingName());
				    		thread.setAppHost(appHost);
				    		thread.setRmn(model.getRmn());
				    		thread.setCustPhone(model.getMobile());
				    		thread.setTxnType("VOID");
				    		Thread t = new Thread(thread);
				    		t.start();
				    		logger.info("void txn send sms and url shortner thread started ");	
			    	}	
					if(model.getEmailId()!=null&&model.getEmailId()!=""&&!model.getEmailId().equals("NA")){
						try{
							//System.out.println("sssss:::::::::::::::"+map);
							HashMap<String,Object> pageData= new HashMap<String,Object>();
							pageData.put("email", model.getEmailId());
							pageData.put("fname", "Customer");
							pageData.put("amount", model.getAmount());
							pageData.put("datetime", model.getDateTime());
							pageData.put("cardNo",model.getCardPanNo());
							pageData.put("marketingName",model.getMarketingName());
							}catch(Exception e){
							logger.error("error to send email in wallet debit "+e);
						}
					}					
			    }
			}catch(Exception e){
				logger.error("Error in void hanlder "+e);
				System.out.println("error in void handler "+e);
			}
			return response;
		}
		
		
		@Override
		public ServiceDto<HashMap<String, HashMap<String, String>>> transactionDetail(
				AcqTransactionDetailModel model) {
			ServiceDto<HashMap<String,HashMap<String, String>>>  response = new ServiceDto<HashMap<String,HashMap<String, String>>>();
			try{
				DbDto<HashMap<String, HashMap<String, String>>>  daoResponse = transactionDao.transactionDetail(model);
			    response.setMessage(daoResponse.getMessage());
			    response.setResult(daoResponse.getResult());
			    response.setStatus(daoResponse.getStatus());
			   // logger.info(resource.getMessage("return.response",new String[] { " from handler" }, Locale.ENGLISH));		 
			}catch(Exception e){
				logger.error("Error in device on Transaction Detail hanlder "+e);
			}
			return response;
		}

		
		@Override
		public ServiceDto<Object> transactionSign(AcqTransactionSignModel model) {
			ServiceDto<Object> response = new ServiceDto<Object>();
			if(model.getSignImg()!=null){
				logger.info("image is not null");
				try{
					String location = signImgLocation + File.separator+model.getTransactionId()+".jpeg";
					DbDto<Object>  daoResponse = transactionDao.transactionSign(model);
					logger.info("response from txn sign dao");
					try{
						List<String> list = (List<String>) daoResponse.getResult();
						System.out.println("list:"+list.get(9));
						if(list.size()>=5&&list.get(7).equals("0")&&NumberValidator.isPhoneNo(model.getCustPhone())){					
							try{
								logger.info("Send sms and url shortner thread is starting");
								String transactionIds= ""+list.get(4);
								
									if(list.get(9)!=null&&list.get(9)!=""&&!list.get(9).equalsIgnoreCase("CASHATPOS")){
									AcqSmsAndUrlShortnerThread thread = new AcqSmsAndUrlShortnerThread();
									thread.setTransactionId(transactionIds);
									thread.setAmount(list.get(0));
									thread.setTxnStatus(list.get(1));
									thread.setDateTime(list.get(2));
									thread.setCardPanNo(model.getCardPanNo());
									thread.setAppHost(appHost);
									thread.setMarketingName(list.get(5));
									thread.setRmn(list.get(6));
									thread.setCustPhone(model.getCustPhone());
									thread.setTxnType("PLACE");
									Thread t = new Thread(thread);
									t.start();
									System.out.println("sms thread started");
									}
																
							}catch(Exception e){
								System.out.println("error to send sms and url shortner " +e);
								logger.error("error to send sms and url shortner " +e);
							}
						}
						if(list.size()>=5&&model.getCustEmail()!=null&&model.getCustEmail()!=""){							
							try{
								logger.info("Email sending");
								String transactionIds= ""+list.get(4);
								String txnId = AcquiroSeq.MakeSmart(transactionIds);
								byte[]   bytesEncoded = Base64.encodeBase64(txnId .getBytes());
								HashMap<String,Object> pageData= new HashMap<String,Object>();
								if(Integer.valueOf(list.get(1))==505){
									pageData.put("status", "debited");
								}else{
									pageData.put("status", "failed");
								}
								pageData.put("email", model.getCustEmail());
								pageData.put("fname", "Customer");
								pageData.put("amount",  list.get(0));
								pageData.put("cardNo",  model.getCardPanNo());
								pageData.put("marketingName",  list.get(5));
								pageData.put("txnId", new String(bytesEncoded));
								pageData.put("datetime", list.get(2));
								pageData.put("acquirerCode", list.get(8).toUpperCase());
							 	
							}catch(Exception e){
								logger.error("error to send email in card sing service handler "+e);
							}
						}
					}catch(Exception e){
						logger.error("error to send sms in card sign service "+e);
					}
					try{
						if (ImageUtils.decode64AndSave(model.getSignImg(),location) == false){
							System.out.println("image is fales");
							response.setStatus(EnumStatusConstant.RollBackError.getId());
							response.setMessage(EnumStatusConstant.RollBackError.getDescription());
							logger.info("Error in transaction/sign service in TransactionHandler");				
						}
					}catch(Exception e){
						logger.error("error to save sign img "+e);
					}
					response.setMessage(daoResponse.getMessage());
					response.setResult(daoResponse.getResult());
					response.setStatus(daoResponse.getStatus());
				}catch(Exception e){
					logger.error("Error in place transaction handler "+e);
				}
			}else{
				response.setMessage("Image can't be null");			
				response.setStatus(EnumStatusConstant.RollBackError.getId());	
				logger.info("Image can't be null ");
			}
			return response;
		}
		
		
		@Override
		public ServiceDto<HashMap<String, HashMap<String, String>>> placeTransaction(AcqTransactionModel model,String clientIpAddress) {
			ServiceDto<HashMap<String, HashMap<String, String>>> response = new ServiceDto<HashMap<String, HashMap<String, String>>>();
			try{
				DbDto<HashMap<String, HashMap<String, String>>> daoResponse = transactionDao.placeTransaction(model,clientIpAddress);
				response.setMessage(daoResponse.getMessage());
				response.setResult(daoResponse.getResult());
				response.setStatus(daoResponse.getStatus());
			}catch(Exception e){
				logger.error("Error in place transaction handler "+e);
			}
			return response;
		}

		@Override
		public ServiceDto<HashMap<String,HashMap<String, String>>> searchTransaction(
				AcqSearchTransactionModel model) {
			ServiceDto<HashMap<String,HashMap<String, String>>> response = new ServiceDto<HashMap<String,HashMap<String, String>>>();
			try{
				DbDto<HashMap<String,HashMap<String, String>>>  daoResponse = transactionDao.searchTransaction(model);
			    response.setMessage(daoResponse.getMessage());
			    response.setResult(daoResponse.getResult());
			    response.setStatus(daoResponse.getStatus());
			}catch(Exception e){
				logger.error("Error in device on search hanlder "+e);
			}
			return response;		
		}			
}
