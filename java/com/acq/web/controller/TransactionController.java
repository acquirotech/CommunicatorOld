package com.acq.web.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.acq.EnumStatusConstant;
import com.acq.AcqSession;
import com.acq.web.controller.model.AcqPostTransactionModel;
import com.acq.web.controller.model.AcqSearchTransactionModel;
import com.acq.web.controller.model.AcqTransactionDetailModel;
import com.acq.web.controller.model.AcqTransactionModel;
import com.acq.web.controller.model.AcqTransactionSignModel;
import com.acq.web.controller.model.AcqVoidTransactionModel;
import com.acq.web.controller.model.LastTxnDetailsModel;
import com.acq.web.dto.ResponseInf;
import com.acq.web.dto.impl.ControllerResponse;
import com.acq.web.dto.impl.ServiceDto;
import com.acq.web.handler.TransactionHandlerInf;

import security.AcquiroSeq;

@Controller
public class TransactionController {

	@Autowired
	TransactionHandlerInf transactionHanler;

	@Autowired
	MessageSource resource;

	@Value("#{acqConfig['loggers.location']}")
	private String loginLocation;

	public String getLoginLocation() {
		return loginLocation;
	}
	
	final static Logger logger = Logger.getLogger(TransactionController.class);
	
	
	
	@RequestMapping(value = "/lasttxn/details/version1", method = RequestMethod.POST)
	public @ResponseBody
	ControllerResponse<Object> lastTxnDetails(HttpServletRequest request, LastTxnDetailsModel model) {
		System.out.println("request for lasttxn details version1 controller");
		String status = "";
		StringBuffer logReport = new StringBuffer();
		logReport.append(",Info: Request intercepted in last txn details controller");
		String clientIpAddress = "";
		ControllerResponse<Object> response = new ControllerResponse<Object>();
		try {
			String ipAddress = request.getHeader("X-FORWARDED-FOR");
			if (ipAddress == null) {
				ipAddress = request.getRemoteAddr();
				clientIpAddress = ipAddress;
				logReport.append(",Info: Client ip addres is found");
			}
			logReport.append(",Info: Request params are validating in last txn details controller");
			ValidatorFactory vFactory = Validation.buildDefaultValidatorFactory();
			Validator validator = vFactory.getValidator();
			Set<ConstraintViolation<LastTxnDetailsModel>> inputErrorSet = validator.validate(model);
			if(inputErrorSet.size() > 0){
				response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
				Iterator<ConstraintViolation<LastTxnDetailsModel>> itr = inputErrorSet.iterator();
				while (itr.hasNext()) {
					ConstraintViolation<LastTxnDetailsModel> validate = (ConstraintViolation<LastTxnDetailsModel>) itr.next();
					response.setMessage(validate.getMessage());
					logReport.append(",Warning: " + validate.getMessage());
					status = "FAILED";
				}
			}else{
				logReport.append(",Info: Params validation done");
				ServiceDto serviceDto = AcqSession.isSessionRunning(request,model.getSessionId());
				logReport.append(",Info: Http session is verifing");
				if (serviceDto.getStatus() == EnumStatusConstant.OK.getId()) {
				logReport.append(",Info: Http session is verified and invoking hander/dao");					
				ServiceDto<HashMap<String, HashMap<String, String>>> daoResponse = transactionHanler.lastTxnDetails(model, clientIpAddress);
				logReport.append(",Info: Response returned from hander/dao");
				System.out.println("response from lasttxn details version1 controller");
				if (daoResponse.getStatus() == EnumStatusConstant.OK.getId()) {
					response.setStatus(daoResponse.getStatus());
					response.setMessage(daoResponse.getMessage());
					//response.setResult(AcquiroSeq.MakeSmart(new JSONObject(daoResponse.getResult()).toString()));
					response.setResult(daoResponse.getResult());
					logReport.append(",Info: Response successfully returned from last txn details controller");
					status = "SUCCESSFULL";
				} else {
					response.setStatus(daoResponse.getStatus());
					response.setMessage(daoResponse.getMessage());
					response.setResult(null);
					logReport.append(",Info: response from last txn details controller");
					status = "FAILED";
				}
				
				} else {
					response.setStatus(111);
					response.setMessage("Session expired");
					response.setResult(null);
					logReport.append(",Info: Session expired");
					status = "FAILED";
				}
			}
		} catch (Exception e) {
			response.setStatus(EnumStatusConstant.RollBackError.getId());
			response.setMessage(EnumStatusConstant.RollBackError.getDescription());
			response.setResult(null);
			logReport.append(",Error: in last txn details " + e);
			status = "FAILED";
		} finally {
			
		}
		return response;
	}
	
	 @RequestMapping(value = "/sign/receipt", method = RequestMethod.GET)
	 public ModelAndView signReceipt(@RequestParam (required=true) String signImage,HttpServletRequest request){
		logger.info("request transaction receipt "+signImage);
		System.out.println("request in cash sing receipt controller");
		ModelAndView model = new ModelAndView();
		try{
			if(signImage.contains("%27")){
				signImage.replace("%27","");
			}
			byte[] valueDecoded= Base64.decodeBase64(signImage.getBytes());			
			String requestString = AcquiroSeq.MakeNonSmart(new String(valueDecoded));
			//String requestString = new String(valueDecoded);
			HashMap<String, String> map = new HashMap<String,String>();
			ServiceDto<HashMap<String, String>> daoResponse = transactionHanler.getCardTxnDetails(requestString);
			System.out.println("response from cash sing receipt controller");
			map = daoResponse.getResult();
			//String custPhone = map.get("custMobileNo");
			if(map.isEmpty()==true){
				model.setViewName("noreceipt");
				return model;
			}
			if(map.get("txnType").equalsIgnoreCase("CARD")==true||map.get("txnType").equalsIgnoreCase("VOID")==true||map.get("txnType").equalsIgnoreCase("CASHATPOS")==true||map.get("txnType").equalsIgnoreCase("CVOID")==true||map.get("txnType").equalsIgnoreCase("CASHBACK")==true||map.get("txnType").equalsIgnoreCase("CBVOID")==true){
				model.addObject("orgName",map.get("orgName"));
				String address = map.get("orgAddress1")+","+map.get("orgAddress2");
				String strAddress = address.replace("|", ", ");
				model.addObject("address",strAddress);
				model.addObject("emailId",map.get("emailId"));
				model.addObject("custPhone",map.get("custMobileNo"));
				model.addObject("dateTime",map.get("dateTime"));
				model.addObject("rmn",map.get("rmn"));
				model.addObject("mid",map.get("mid"));
				model.addObject("tid",map.get("tid"));
				if(map.get("switchLab").equalsIgnoreCase("ogs")){
					model.addObject("receiptType","ogsbank");
				}else{
				if(map.get("tid").startsWith("IB")){
					model.addObject("receiptType","indus");
				}else{
					model.addObject("receiptType","rbl");
				}}
				model.addObject("batchNo",map.get("batchNo"));
				model.addObject("invoiceId",map.get("invoiceId"));
				model.addObject("cardType",map.get("cardType"));
				model.addObject("authCode",map.get("authCode"));
				model.addObject("cardHolderName",map.get("cardHolderName"));
				model.addObject("cardPanNo",map.get("cardPanNo"));
				model.addObject("rrNo",map.get("rrNo"));
				model.addObject("amount",map.get("amount"));
				model.addObject("custEmail",map.get("custEmail"));
				model.addObject("custPhone",map.get("custMobileNo"));
				model.addObject("description",map.get("description"));	
				if(map.get("txnType").equalsIgnoreCase("VOID")||map.get("txnType").equalsIgnoreCase("CVOID")||map.get("txnType").equalsIgnoreCase("CBVOID")){
					model.setViewName("voidreceipt");
				}else{
					model.setViewName("receipt");
				}				
				model.addObject("img","logo");
		    	 model.addObject("webSiteUrl",map.get("www.acquiropayments.com")); 
			     model.addObject("acquirerName",map.get("Powered by Acquiro Payments."));
			}else{
				model.setViewName("noreceipt");
			}
			logger.info("response returning");
			return model;			
		} catch(Exception e){
			model.setViewName("noreceipt");
			logger.error("error to select transaction receipt "+e);
			return model;
		}
	 }
	 
	 
	@RequestMapping(value = "/sign/image", method = RequestMethod.GET)
	public HttpEntity<byte[]> signImageS(@RequestParam (required=true) String signImage) {
		System.out.println("request in sign/image controller");
		return transactionHanler.signImageS(signImage);
	}
	
	@RequestMapping(value = "/void/transaction/version1", method = RequestMethod.POST)
	public @ResponseBody
	ResponseInf<Object> voidtxn(@ModelAttribute AcqVoidTransactionModel model,
			HttpServletRequest request) {
		System.out.println("request landing in void transaction controller");
		logger.info("request landing in void transaction controller");
		ControllerResponse<Object> response = new ControllerResponse<Object>();
		try{
		ValidatorFactory vFactory = Validation.buildDefaultValidatorFactory();
		Validator validator = vFactory.getValidator();
		Set<ConstraintViolation<AcqVoidTransactionModel>> inputErrorSet = validator
				.validate(model);
		if (inputErrorSet.size() > 0){
			response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
			Iterator<ConstraintViolation<AcqVoidTransactionModel>> itr = inputErrorSet.iterator();
			while (itr.hasNext()) {
				ConstraintViolation<AcqVoidTransactionModel> validate = (ConstraintViolation<AcqVoidTransactionModel>) itr
						.next();
				response.setMessage(validate.getMessage());
			}
			return response;
		} else {
			ServiceDto serviceDto = AcqSession.isSessionRunning(request,	model.getSessionId());
			if (serviceDto.getStatus() == EnumStatusConstant.OK.getId()){
				ServiceDto<Object> daoResponse = transactionHanler.voidtransaction(model);
				System.out.println("response from  void transaction controller");
				response.setStatus(daoResponse.getStatus());
				response.setMessage(daoResponse.getMessage());			
			} else return ControllerResponse.createControllerResponse(serviceDto);
		}
	}catch (Exception e) {
		response.setStatus(EnumStatusConstant.RollBackError.getId());
		response.setMessage(EnumStatusConstant.RollBackError
				.getDescription());
		response.setResult(null);
		logger.info("Error in void transaction"+e);
	}
		return response;
	}

	
	@RequestMapping(value = "/void/searchtransaction/version1", method = RequestMethod.POST)
	public @ResponseBody
	ResponseInf<Object> txnsrch(@ModelAttribute AcqSearchTransactionModel model,
			HttpServletRequest request) {
		logger.info("request landing in void search transaction controller");
		ValidatorFactory vFactory = Validation.buildDefaultValidatorFactory();
		Validator validator = vFactory.getValidator();
		Set<ConstraintViolation<AcqSearchTransactionModel>> inputErrorSet = validator
				.validate(model);
		ControllerResponse<Object> response = new ControllerResponse<Object>();
		try{
		if (inputErrorSet.size() > 0) {
			response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
			Iterator<ConstraintViolation<AcqSearchTransactionModel>> itr = inputErrorSet
					.iterator();
			while (itr.hasNext()) {
				ConstraintViolation<AcqSearchTransactionModel> validate = (ConstraintViolation<AcqSearchTransactionModel>) itr
						.next();
				response.setMessage(validate.getMessage());
			}
			return response;
		} else {
			ServiceDto serviceDto = AcqSession.isSessionRunning(request,
					model.getSessionId());
			if (serviceDto.getStatus() == EnumStatusConstant.OK.getId()) {
				ServiceDto<HashMap<String, HashMap<String, String>>> daoResponse = transactionHanler
						.searchTransaction(model);
				System.out.println("response from void search transaction controller");
				if (daoResponse.getStatus() == EnumStatusConstant.OK.getId()) {
					response.setStatus(daoResponse.getStatus());
					response.setMessage(daoResponse.getMessage());
					//response.setResult(AcquiroSeq.MakeSmart(new JSONObject(daoResponse.getResult()).toString()));
					response.setResult(daoResponse.getResult());
					logger.info("response returning form search transaction controller");
					return response;
				} else {
					response.setStatus(daoResponse.getStatus());
					response.setMessage(daoResponse.getMessage());
					response.setResult(null);
					logger.warn("response returning form search transaction controller");
					return response;
				}
			} else
				return ControllerResponse.createControllerResponse(serviceDto);
		}

	}catch (Exception e) {
		response.setStatus(EnumStatusConstant.RollBackError.getId());
		response.setMessage(EnumStatusConstant.RollBackError
				.getDescription());
		response.setResult(null);
		logger.info("Error in search void transaction"+e);
	}
		return response;
		}

	
	@RequestMapping(value = "/transactiondetail/version1", method = RequestMethod.POST)
	public @ResponseBody
	ResponseInf<Object> txndetail(
			@ModelAttribute AcqTransactionDetailModel model,
			HttpServletRequest request) {
		logger.info("request landing in transaction details controller");
		System.out.println("request landing in transaction details controlle");
		ValidatorFactory vFactory = Validation.buildDefaultValidatorFactory();
		Validator validator = vFactory.getValidator();
		Set<ConstraintViolation<AcqTransactionDetailModel>> inputErrorSet = validator.validate(model);
		ControllerResponse<Object> response = new ControllerResponse<Object>();
		try{
		if (inputErrorSet.size() > 0) {
			response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
			Iterator<ConstraintViolation<AcqTransactionDetailModel>> itr = inputErrorSet
					.iterator();
			while (itr.hasNext()) {
				ConstraintViolation<AcqTransactionDetailModel> validate = (ConstraintViolation<AcqTransactionDetailModel>) itr
						.next();
				response.setMessage(validate.getMessage());
			}
			return response;
		} else {
			ServiceDto serviceDto = AcqSession.isSessionRunning(request,
					model.getSessionId());
			if (serviceDto.getStatus() == EnumStatusConstant.OK.getId()) 
			{
				ServiceDto<HashMap<String, HashMap<String, String>>> daoResponse = transactionHanler
						.transactionDetail(model);
				System.out.println("response from transaction details controlle");
				if (daoResponse.getStatus() == EnumStatusConstant.OK.getId()) {
					response.setStatus(daoResponse.getStatus());
					response.setMessage(daoResponse.getMessage());
				//	response.setResult(AcquiroSeq.MakeSmart(new JSONObject(daoResponse.getResult()).toString()));
					response.setResult(daoResponse.getResult());
					logger.info("response returning form transaction details controller");
					return response;
				} else {
					response.setStatus(daoResponse.getStatus());
					response.setMessage(daoResponse.getMessage());
					response.setResult(null);
					logger.info("response returning form transaction details controller");
					return response;
				}
			} 
			else return ControllerResponse.createControllerResponse(serviceDto);
		}
	}catch (Exception e) {
		response.setStatus(EnumStatusConstant.RollBackError.getId());
		response.setMessage(EnumStatusConstant.RollBackError
				.getDescription());
		response.setResult(null);
		logger.info("Error in transaction detail"+e);
	}
		return response;
		}

	
	@RequestMapping(value = "/transaction/complete/version1", method = RequestMethod.POST)
	public @ResponseBody
	ControllerResponse<Object> placePostTransaction(HttpServletRequest request,	AcqPostTransactionModel model) {
		//System.out.println("request landing in transaction complete version1 controlle");
		String status = "";
		StringBuffer logReport = new StringBuffer();
		logReport.append(",Info: Request intercepted in post paymentsale controller");
		String clientIpAddress = "";
		ControllerResponse<Object> response = new ControllerResponse<Object>();
		try {
			String ipAddress = request.getHeader("X-FORWARDED-FOR");
			if (ipAddress == null) {
				ipAddress = request.getRemoteAddr();
				clientIpAddress = ipAddress;
				logReport.append(",Info: Client ip addres is found");
			}
			logReport.append(",Info: Request params are validating in post payment sale controller");
			ValidatorFactory vFactory = Validation.buildDefaultValidatorFactory();
			Validator validator = vFactory.getValidator();
			Set<ConstraintViolation<AcqPostTransactionModel>> inputErrorSet = validator.validate(model);
			if(inputErrorSet.size() > 0){
				response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
				Iterator<ConstraintViolation<AcqPostTransactionModel>> itr = inputErrorSet.iterator();
				while (itr.hasNext()) {
					ConstraintViolation<AcqPostTransactionModel> validate = (ConstraintViolation<AcqPostTransactionModel>) itr.next();
					response.setMessage(validate.getMessage());
					logReport.append(",Warning: " + validate.getMessage());
					status = "FAILED";
				}
			}else{
				logReport.append(",Info: Params validation done");
			/*	ServiceDto serviceDto = AcqSession.isSessionRunning(request,model.getSessionId());
				logReport.append(",Info: Http session is verifing");
				if (serviceDto.getStatus() == EnumStatusConstant.OK.getId()) {*/
				logReport.append(",Info: Http session is verified and invoking hander/dao");					
				ServiceDto<HashMap<String, HashMap<String, String>>> daoResponse = transactionHanler.placePostTransaction(model, clientIpAddress);
				//System.out.println("response from transaction complete version1 controlle");
				logReport.append(",Info: Response returned from hander/dao");
				if (daoResponse.getStatus() == EnumStatusConstant.OK.getId()) {
					response.setStatus(daoResponse.getStatus());
					response.setMessage(daoResponse.getMessage());
					//response.setResult(AcquiroSeq.MakeSmart(new JSONObject(daoResponse.getResult()).toString()));
					response.setResult(daoResponse.getResult());
					logReport.append(",Info: Response successfully returned from post payment sale controller");
					status = "SUCCESSFULL";
				} else {
					response.setStatus(daoResponse.getStatus());
					response.setMessage(daoResponse.getMessage());
					response.setResult(null);
					logReport.append(",Info: response from txn complete controller");
					status = "FAILED";
				}
				
				/*} else {
					response.setStatus(111);
					response.setMessage("Session expired");
					response.setResult(null);
					logReport.append(",Info: Session expired");
					status = "FAILED";
				}*/
			}
		} catch (Exception e) {
			response.setStatus(EnumStatusConstant.RollBackError.getId());
			response.setMessage(EnumStatusConstant.RollBackError.getDescription());
			response.setResult(null);
			logReport.append(",Error: in user authenticate " + e);
			status = "FAILED";
		} finally {
			/*FileHandler fh;
			try {
				fh = new FileHandler(loginLocation + File.separator	+ "eztxn.log", true);
				utilLogger.addHandler(fh);
				SimpleFormatter formatter = new SimpleFormatter();
				fh.setFormatter(formatter);
				logReport.append("," + status + ";");
				logReport
						.append("\n -----------------------------------------------------------------------------------------------------------------------------------------------------------");
				utilLogger.info(model.getSessionId() + "," + clientIpAddress+ logReport);
				fh.close();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}*/
		}
		return response;
	}
	
	@RequestMapping(value = "/transaction/initiate/version1", method = RequestMethod.POST)
	public @ResponseBody
	ControllerResponse<Object> placeTransaction(HttpServletRequest request,
			AcqTransactionModel model) {
		String status = "";
		logger.info("Request Landing in Transaction Init Controller");
		String clientIpAddress = "";
		ControllerResponse<Object> response = new ControllerResponse<Object>();
		try {
			String ipAddress = request.getHeader("X-FORWARDED-FOR");
			if (ipAddress == null) {
				ipAddress = request.getRemoteAddr();
				clientIpAddress = ipAddress;
				//logReport.append(",Info: Client ip addres is found");
			}
			//logReport.append(",Info: Request params are validating in controller");
			ValidatorFactory vFactory = Validation.buildDefaultValidatorFactory();
			Validator validator = vFactory.getValidator();
			Set<ConstraintViolation<AcqTransactionModel>> inputErrorSet = validator.validate(model);
			if(inputErrorSet.size() > 0){
				response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
				Iterator<ConstraintViolation<AcqTransactionModel>> itr = inputErrorSet.iterator();
				while (itr.hasNext()) {
					ConstraintViolation<AcqTransactionModel> validate = (ConstraintViolation<AcqTransactionModel>) itr.next();
					response.setMessage(validate.getMessage());
					status = "FAILED";
				}
			}else{
				/*ServiceDto serviceDto = AcqSession.isSessionRunning(request,model.getUserId());
				if(model.getCardTxnType().equalsIgnoreCase("CASHATPOS")&&Double.valueOf(model.getTotalAmount())<200){
					response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
					response.setMessage("Minimum Amount Allowed INR 200");
					response.setResult(null);
					return response;
				}				
				if (serviceDto.getStatus() == EnumStatusConstant.OK.getId()){*/
					ServiceDto<HashMap<String, HashMap<String, String>>> daoResponse = transactionHanler.placeTransaction(model, clientIpAddress);
						logger.info("res frm txn init ver1");
					if (daoResponse.getStatus() == EnumStatusConstant.OK.getId()) {
						response.setStatus(daoResponse.getStatus());
						response.setMessage(daoResponse.getMessage());
						response.setResult(daoResponse.getResult());
						status = "SUCCESSFULL";
					}else {
						response.setStatus(daoResponse.getStatus());
						response.setMessage(daoResponse.getMessage());
						response.setResult(null);
						status = "FAILED";
					}
				/*} else {
					response.setStatus(111);
					response.setMessage("Session expired");
					response.setResult(null);
					status = "FAILED";
				}*/
			}
		} catch (Exception e) {
			response.setStatus(EnumStatusConstant.RollBackError.getId());
			response.setMessage(EnumStatusConstant.RollBackError.getDescription());
			response.setResult(null);
			status = "FAILED";
		} finally {
			/*FileHandler fh;
			try {
				fh = new FileHandler(loginLocation + File.separator	+ "eztxn.log", true);
				utilLogger.addHandler(fh);
				SimpleFormatter formatter = new SimpleFormatter();
				fh.setFormatter(formatter);
				logReport.append("," + status + ";");
				logReport
						.append("\n -----------------------------------------------------------------------------------------------------------------------------------------------------------");
				utilLogger.info(model.getUserId() + "," + clientIpAddress+ logReport);
				fh.close();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}*/
		}
		return response;
	}

	@RequestMapping(value = "/sign/version1", method = RequestMethod.POST)
	public @ResponseBody
	ResponseInf<Object> txSign(@ModelAttribute AcqTransactionSignModel model,HttpServletRequest request) {
		logger.info("request landing in sign controller");
		ValidatorFactory vFactory = Validation.buildDefaultValidatorFactory();
		Validator validator = vFactory.getValidator();
		Set<ConstraintViolation<AcqTransactionSignModel>> inputErrorSet = validator
				.validate(model);
		ControllerResponse<Object> response = new ControllerResponse<Object>();
		try{
			if (inputErrorSet.size() > 0) {
				response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
				Iterator<ConstraintViolation<AcqTransactionSignModel>> itr = inputErrorSet.iterator();
				while (itr.hasNext()) {
					ConstraintViolation<AcqTransactionSignModel> validate = (ConstraintViolation<AcqTransactionSignModel>) itr.next();
					response.setMessage(validate.getMessage());
				}
				return response;
			} else {
				ServiceDto serviceDto = AcqSession.isSessionRunning(request,model.getSessionId());
				if (serviceDto.getStatus() == EnumStatusConstant.OK.getId()) {
					ServiceDto<Object> daoResponse = transactionHanler.transactionSign(model);
					if (daoResponse.getStatus() == EnumStatusConstant.OK.getId()) {
						response.setStatus(daoResponse.getStatus());
						response.setMessage(daoResponse.getMessage());
						response.setResult(null);
						logger.info("response returning form sign controller");
						return response;
					} else {
						response.setStatus(daoResponse.getStatus());
						response.setMessage(daoResponse.getMessage());
						response.setResult(null);
						logger.warn("response returning form sign controller");
						return response;
					}
				} else
					return ControllerResponse.createControllerResponse(serviceDto);
			}
		}catch (Exception e) {
			response.setStatus(EnumStatusConstant.RollBackError.getId());
			response.setMessage(EnumStatusConstant.RollBackError.getDescription());
			response.setResult(null);
			logger.info("Error in sign image"+e);
		}
		return response;	
	}
}