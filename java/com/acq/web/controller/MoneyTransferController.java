package com.acq.web.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.acq.EnumStatusConstant;
import com.acq.web.controller.model.AcqBankItCardLoadModel;
import com.acq.web.controller.model.AcqBankItTransactionListModel;
import com.acq.web.controller.model.AcqMoneyTransferAddBeneficiaryModel;
import com.acq.web.controller.model.AcqMoneyTransferAddSenderModel;
import com.acq.web.controller.model.AcqMoneyTransferBankListModel;
import com.acq.web.controller.model.AcqMoneyTransferCheckStatusModel;
import com.acq.web.controller.model.AcqMoneyTransferNeftModel;
import com.acq.web.controller.model.AcqMoneyTransferOtpModel;
import com.acq.web.dto.ResponseInf;
import com.acq.web.dto.impl.ControllerResponse2;
import com.acq.web.dto.impl.ServiceDto2;
import com.acq.web.handler.MoneyTransferHandlerInf;
import com.acq.web.handler.RechargeHandlerInf;

@Controller
@RequestMapping(value = "/")
public class MoneyTransferController {

	@Autowired
	MoneyTransferHandlerInf moneyHandler;

	@Autowired
	RechargeHandlerInf rechargeHandler;
	
	@Autowired
	MessageSource resource;

	final static Logger logger = Logger.getLogger(MoneyTransferController.class);
		
	@RequestMapping(value="/dmt/getTransactionList/version1", method=RequestMethod.POST)
	public @ResponseBody ResponseInf<Object> getTransactionList(@RequestBody AcqBankItTransactionListModel model,HttpServletRequest request) throws ParseException{
		logger.info("request landing get dmt transaction list controller");
		ControllerResponse2<Object> response = new ControllerResponse2<Object>();
		ValidatorFactory vFactory = Validation.buildDefaultValidatorFactory();
		Validator validator = vFactory.getValidator();
		Set<ConstraintViolation<AcqBankItTransactionListModel>> inputErrorSet = validator.validate(model);
		if (inputErrorSet.size() > 0) {
			response. setStatusCode(EnumStatusConstant.InvalidCrediential.getId());
			Iterator<ConstraintViolation<AcqBankItTransactionListModel>> itr = inputErrorSet.iterator();
			while (itr.hasNext()) {
				ConstraintViolation<AcqBankItTransactionListModel> validate = (ConstraintViolation<AcqBankItTransactionListModel>) itr.next();
				response.setStatusMessage(validate.getMessage());
			}			
		} else {
			
			/*ServiceDto2 ServiceDto2 = AcqSession.isSessionRunning(request,model.getSessionId());
			if (ServiceDto2.getStatusCode() == EnumStatusConstant.OK.getId()) {*/
				ServiceDto2<Object> daoResponse = moneyHandler.getTransactionList(model);
				System.out.println("response from get dmt transaction list version1 controller");
				if (daoResponse.getStatusCode() == EnumStatusConstant.OK.getId()) {
					response. setStatusCode(daoResponse.getStatusCode());
					response.setStatusMessage(daoResponse.getStatusMessage());
					JSONObject json = (JSONObject)new JSONParser().parse(""+daoResponse.getBody());	
					
					response.setBody(json);
					logger.info("response returnging from get dmt transaction list controller::");
					return response;
				}else{
					response. setStatusCode(daoResponse.getStatusCode());
					response.setStatusMessage(daoResponse.getStatusMessage());
					response.setBody(null);
					logger.info("Error in get dmt dmt transaction list controller");
					return response;
				}
			//}else return ControllerResponse2.createControllerResponse2(ServiceDto2);
		}
		return response;
	}	
	
	@RequestMapping(value="/dmt/generateOtp/version1", method=RequestMethod.POST)
	public @ResponseBody ResponseInf<Object> dmtgenerateOtp(@RequestBody AcqMoneyTransferOtpModel model,HttpServletRequest request){
		logger.info("request landing get dmt generateOtp controller");
		ControllerResponse2<Object> response = new ControllerResponse2<Object>();
		System.out.println("request landing get dmtgenerateOtp version1 controller");
		
		ValidatorFactory vFactory = Validation.buildDefaultValidatorFactory();
		Validator validator = vFactory.getValidator();
		Set<ConstraintViolation<AcqMoneyTransferOtpModel>> inputErrorSet = validator.validate(model);
		if (inputErrorSet.size() > 0) {
			response. setStatusCode(EnumStatusConstant.InvalidCrediential.getId());
			Iterator<ConstraintViolation<AcqMoneyTransferOtpModel>> itr = inputErrorSet.iterator();
			while (itr.hasNext()) {
				ConstraintViolation<AcqMoneyTransferOtpModel> validate = (ConstraintViolation<AcqMoneyTransferOtpModel>) itr.next();
				response.setStatusMessage(validate.getMessage());
			}			
		} else {
			Date date = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:MM:ss");
			model.setDateTime(format.format(date));
			/*ServiceDto2 ServiceDto2 = AcqSession.isSessionRunning(request,model.getSessionId());
			if (ServiceDto2.getStatusCode() == EnumStatusConstant.OK.getId()) {*/
				ServiceDto2<Object> daoResponse = moneyHandler.dmtgenerateOtp(model);
				System.out.println("response from get dmtgenerateOtp version1 controller");
				if (daoResponse.getStatusCode() == EnumStatusConstant.OK.getId()) {
					response. setStatusCode(daoResponse.getStatusCode());
					response.setStatusMessage(daoResponse.getStatusMessage());
					response.setBody(daoResponse.getBody());
					logger.info("response returnging from get dmt generateOtp controller::");
					return response;
				}else{
					response. setStatusCode(daoResponse.getStatusCode());
					response.setStatusMessage(daoResponse.getStatusMessage());
					response.setBody(null);
					logger.info("Error in get dmt generateOtp controller");
					return response;
				}
			//}else return ControllerResponse2.createControllerResponse2(ServiceDto2);
		}
		return response;
	}	
	
	@RequestMapping(value="/dmt/addSender/version1", method=RequestMethod.POST)
	public @ResponseBody ResponseInf<Object> add(@RequestBody AcqMoneyTransferAddSenderModel model,HttpServletRequest request){
		logger.info("request landing get dmt add sender controller");
		System.out.println("request landing get add sender version1 controller");
		ControllerResponse2<Object> response = new ControllerResponse2<Object>();
		ValidatorFactory vFactory = Validation.buildDefaultValidatorFactory();
		Validator validator = vFactory.getValidator();
		Set<ConstraintViolation<AcqMoneyTransferAddSenderModel>> inputErrorSet = validator.validate(model);
		if (inputErrorSet.size() > 0) {
			response. setStatusCode(EnumStatusConstant.InvalidCrediential.getId());
			Iterator<ConstraintViolation<AcqMoneyTransferAddSenderModel>> itr = inputErrorSet.iterator();
			while (itr.hasNext()) {
				ConstraintViolation<AcqMoneyTransferAddSenderModel> validate = (ConstraintViolation<AcqMoneyTransferAddSenderModel>) itr.next();
				response.setStatusMessage(validate.getMessage());
			}			
		} else {
			Date date = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:MM:ss");
			model.setDateTime(format.format(date));
			/*ServiceDto2 ServiceDto2 = AcqSession.isSessionRunning(request,model.getSessionId());
			if (ServiceDto2.getStatusCode() == EnumStatusConstant.OK.getId()) {*/
			ServiceDto2<Object> daoResponse = moneyHandler.addSender(model);
				System.out.println("response from get add sender version1 controller");
				if (daoResponse.getStatusCode() == EnumStatusConstant.OK.getId()) {
					response. setStatusCode(daoResponse.getStatusCode());
					response.setStatusMessage(daoResponse.getStatusMessage());
					response.setBody(daoResponse.getBody());
					logger.info("response returnging from get dmt add sender controller::");
					return response;
				}else{
					response. setStatusCode(daoResponse.getStatusCode());
					response.setStatusMessage(daoResponse.getStatusMessage());
					response.setBody(null);
					logger.info("Error in get dmt add sender controller");
					return response;
				}
			//}else return ControllerResponse2.createControllerResponse2(ServiceDto2);
		}
		return response;
	}	
	
	@RequestMapping(value="/dmt/addBeneficiary/version1", method=RequestMethod.POST)
	public @ResponseBody ResponseInf<Object> addbene(@RequestBody AcqMoneyTransferAddBeneficiaryModel model,HttpServletRequest request){
		logger.info("request landing for add Beneficiary");
		ControllerResponse2<Object> response = new ControllerResponse2<Object>();
		ValidatorFactory vFactory = Validation.buildDefaultValidatorFactory();
		Validator validator = vFactory.getValidator();
		Set<ConstraintViolation<AcqMoneyTransferAddBeneficiaryModel>> inputErrorSet = validator.validate(model);
		if (inputErrorSet.size() > 0) {
			response. setStatusCode(EnumStatusConstant.InvalidCrediential.getId());
			Iterator<ConstraintViolation<AcqMoneyTransferAddBeneficiaryModel>> itr = inputErrorSet.iterator();
			while (itr.hasNext()) {
				ConstraintViolation<AcqMoneyTransferAddBeneficiaryModel> validate = (ConstraintViolation<AcqMoneyTransferAddBeneficiaryModel>) itr.next();
				response.setStatusMessage(validate.getMessage());
			}			
		} else {
			Date date = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:MM:ss");
			model.setDateTime(format.format(date));
			/*ServiceDto2 ServiceDto2 = AcqSession.isSessionRunning(request,model.getSessionId());
			if (ServiceDto2.getStatusCode() == EnumStatusConstant.OK.getId()) {*/
			ServiceDto2<Object> daoResponse = moneyHandler.addBeneficiary(model);
				System.out.println("response from get add Beneficiary version1 controller");
				if (daoResponse.getStatusCode() == EnumStatusConstant.OK.getId()) {
					response. setStatusCode(daoResponse.getStatusCode());
					response.setStatusMessage(daoResponse.getStatusMessage());
					response.setBody(daoResponse.getBody());
					logger.info("response returnging from add Beneficiary controller::");
					return response;
				}else{
					response. setStatusCode(daoResponse.getStatusCode());
					response.setStatusMessage(daoResponse.getStatusMessage());
					response.setBody(null);
					logger.info("Error in get dmt add Beneficiary controller");
					return response;
				}
			//}else return ControllerResponse2.createControllerResponse2(ServiceDto2);
		}
		return response;
	}	
	
	@RequestMapping(value="/dmt/fetchBeneficiaryList/version1", method=RequestMethod.POST)
	public @ResponseBody ResponseInf<Object> addbeneList(@RequestBody AcqMoneyTransferOtpModel model,HttpServletRequest request){
		logger.info("request landing for Beneficiary List");
		ControllerResponse2<Object> response = new ControllerResponse2<Object>();
		ValidatorFactory vFactory = Validation.buildDefaultValidatorFactory();
		Validator validator = vFactory.getValidator();
		Set<ConstraintViolation<AcqMoneyTransferOtpModel>> inputErrorSet = validator.validate(model);
		if (inputErrorSet.size() > 0) {
			response. setStatusCode(EnumStatusConstant.InvalidCrediential.getId());
			Iterator<ConstraintViolation<AcqMoneyTransferOtpModel>> itr = inputErrorSet.iterator();
			while (itr.hasNext()) {
				ConstraintViolation<AcqMoneyTransferOtpModel> validate = (ConstraintViolation<AcqMoneyTransferOtpModel>) itr.next();
				response.setStatusMessage(validate.getMessage());
			}			
		} else {
			Date date = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:MM:ss");
			model.setDateTime(format.format(date));
			/*ServiceDto2 ServiceDto2 = AcqSession.isSessionRunning(request,model.getSessionId());
			if (ServiceDto2.getStatusCode() == EnumStatusConstant.OK.getId()) {*/
			ServiceDto2<Object> daoResponse = moneyHandler
					.fetchBeneficiaryList(model);
				System.out.println("response from get Beneficiary List version1 controller");
				if (daoResponse.getStatusCode() == EnumStatusConstant.OK.getId()) {
					response. setStatusCode(daoResponse.getStatusCode());
					response.setStatusMessage(daoResponse.getStatusMessage());
					response.setBody(daoResponse.getBody());
					logger.info("response returnging from Beneficiary List controller::");
					return response;
				}else{
					response. setStatusCode(daoResponse.getStatusCode());
					response.setStatusMessage(daoResponse.getStatusMessage());
					response.setBody(null);
					logger.info("Error in get dmt Beneficiary List controller");
					return response;
				}
			//}else return ControllerResponse2.createControllerResponse2(ServiceDto2);
		}
		return response;
	}	
	
	@RequestMapping(value="/dmt/neft/version1", method=RequestMethod.POST)
	public @ResponseBody ResponseInf<Object> neft(@RequestBody AcqMoneyTransferNeftModel model,HttpServletRequest request){
		logger.info("request landing for add neft ");
		ControllerResponse2<Object> response = new ControllerResponse2<Object>();
		ValidatorFactory vFactory = Validation.buildDefaultValidatorFactory();
		Validator validator = vFactory.getValidator();
		Set<ConstraintViolation<AcqMoneyTransferNeftModel>> inputErrorSet = validator.validate(model);
		if (inputErrorSet.size() > 0) {
			response. setStatusCode(EnumStatusConstant.InvalidCrediential.getId());
			Iterator<ConstraintViolation<AcqMoneyTransferNeftModel>> itr = inputErrorSet.iterator();
			while (itr.hasNext()) {
				ConstraintViolation<AcqMoneyTransferNeftModel> validate = (ConstraintViolation<AcqMoneyTransferNeftModel>) itr.next();
				response.setStatusMessage(validate.getMessage());
			}			
		} else {
			/*ServiceDto2 ServiceDto2 = AcqSession.isSessionRunning(request,model.getSessionId());
			if (ServiceDto2.getStatusCode() == EnumStatusConstant.OK.getId()) {*/
			ServiceDto2<Object> daoResponse = moneyHandler.dmtNeft(model);
				System.out.println("response from get add neft version1 controller");
				if (daoResponse.getStatusCode() == EnumStatusConstant.OK.getId()) {
					response. setStatusCode(daoResponse.getStatusCode());
					response.setStatusMessage(daoResponse.getStatusMessage());
					response.setBody(daoResponse.getBody());
					logger.info("response returnging from add neft controller::");
					return response;
				}else{
					response. setStatusCode(daoResponse.getStatusCode());
					response.setStatusMessage(daoResponse.getStatusMessage());
					response.setBody(null);
					logger.info("Error in get dmt add neft controller");
					return response;
				}
			//}else return ControllerResponse2.createControllerResponse2(ServiceDto2);
		}
		return response;
	}	
	
	@RequestMapping(value="/dmt/imps/version1", method=RequestMethod.POST)
	public @ResponseBody ResponseInf<Object> imps(@RequestBody AcqMoneyTransferNeftModel model,HttpServletRequest request){
		logger.info("request landing for add imps ");
		ControllerResponse2<Object> response = new ControllerResponse2<Object>();
		ValidatorFactory vFactory = Validation.buildDefaultValidatorFactory();
		Validator validator = vFactory.getValidator();
		Set<ConstraintViolation<AcqMoneyTransferNeftModel>> inputErrorSet = validator.validate(model);
		if (inputErrorSet.size() > 0) {
			response. setStatusCode(EnumStatusConstant.InvalidCrediential.getId());
			Iterator<ConstraintViolation<AcqMoneyTransferNeftModel>> itr = inputErrorSet.iterator();
			while (itr.hasNext()) {
				ConstraintViolation<AcqMoneyTransferNeftModel> validate = (ConstraintViolation<AcqMoneyTransferNeftModel>) itr.next();
				response.setStatusMessage(validate.getMessage());
			}			
		} else {
			/*ServiceDto2 ServiceDto2 = AcqSession.isSessionRunning(request,model.getSessionId());
			if (ServiceDto2.getStatusCode() == EnumStatusConstant.OK.getId()) {*/
			ServiceDto2<Object> daoResponse = moneyHandler.dmtImps(model);
				System.out.println("response from get add imps version1 controller");
				if (daoResponse.getStatusCode() == EnumStatusConstant.OK.getId()) {
					response. setStatusCode(daoResponse.getStatusCode());
					response.setStatusMessage(daoResponse.getStatusMessage());
					response.setBody(daoResponse.getBody());
					logger.info("response returnging from add imps controller::");
					return response;
				}else{
					response. setStatusCode(daoResponse.getStatusCode());
					response.setStatusMessage(daoResponse.getStatusMessage());
					response.setBody(null);
					logger.info("Error in get dmt add imps controller");
					return response;
				}
			//}else return ControllerResponse2.createControllerResponse2(ServiceDto2);
		}
		return response;
	}	
	
	@RequestMapping(value="/dmt/deleteBeneficiary/version1", method=RequestMethod.POST)
	public @ResponseBody ResponseInf<Object> deleteBeneficiary(@RequestBody AcqMoneyTransferNeftModel model,HttpServletRequest request){
		logger.info("request landing for deleteBeneficiary");
		ControllerResponse2<Object> response = new ControllerResponse2<Object>();
		ValidatorFactory vFactory = Validation.buildDefaultValidatorFactory();
		Validator validator = vFactory.getValidator();
		Set<ConstraintViolation<AcqMoneyTransferNeftModel>> inputErrorSet = validator.validate(model);
		if (inputErrorSet.size() > 0) {
			response. setStatusCode(EnumStatusConstant.InvalidCrediential.getId());
			Iterator<ConstraintViolation<AcqMoneyTransferNeftModel>> itr = inputErrorSet.iterator();
			while (itr.hasNext()) {
				ConstraintViolation<AcqMoneyTransferNeftModel> validate = (ConstraintViolation<AcqMoneyTransferNeftModel>) itr.next();
				response.setStatusMessage(validate.getMessage());
			}			
		} else {
			Date date = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:MM:ss");
			model.setDateTime(format.format(date));
			/*ServiceDto2 ServiceDto2 = AcqSession.isSessionRunning(request,model.getSessionId());
			if (ServiceDto2.getStatusCode() == EnumStatusConstant.OK.getId()) {*/
			ServiceDto2<Object> daoResponse = moneyHandler
					.dmtDeleteBeneficiary(model);
				System.out.println("response from get deleteBeneficiary version1 controller");
				if (daoResponse.getStatusCode() == EnumStatusConstant.OK.getId()) {
					response. setStatusCode(daoResponse.getStatusCode());
					response.setStatusMessage(daoResponse.getStatusMessage());
					response.setBody(daoResponse.getBody());
					logger.info("response returnging from deleteBeneficiary controller::");
					return response;
				}else{
					response. setStatusCode(daoResponse.getStatusCode());
					response.setStatusMessage(daoResponse.getStatusMessage());
					response.setBody(null);
					logger.info("Error in get dmt deleteBeneficiary controller");
					return response;
				}
			//}else return ControllerResponse2.createControllerResponse2(ServiceDto2);
		}
		return response;
	}
	@RequestMapping(value="/dmt/getBankList/version1", method=RequestMethod.POST)
	public @ResponseBody ResponseInf<Object> getBankList(@RequestBody AcqMoneyTransferBankListModel model,HttpServletRequest request){
		logger.info("request landing for getBankList");
		ControllerResponse2<Object> response = new ControllerResponse2<Object>();
		ValidatorFactory vFactory = Validation.buildDefaultValidatorFactory();
		Validator validator = vFactory.getValidator();
		Set<ConstraintViolation<AcqMoneyTransferBankListModel>> inputErrorSet = validator.validate(model);
		if (inputErrorSet.size() > 0) {
			response. setStatusCode(EnumStatusConstant.InvalidCrediential.getId());
			Iterator<ConstraintViolation<AcqMoneyTransferBankListModel>> itr = inputErrorSet.iterator();
			while (itr.hasNext()) {
				ConstraintViolation<AcqMoneyTransferBankListModel> validate = (ConstraintViolation<AcqMoneyTransferBankListModel>) itr.next();
				response.setStatusMessage(validate.getMessage());
			}			
		} else {
			/*ServiceDto2 ServiceDto2 = AcqSession.isSessionRunning(request,model.getSessionId());
			if (ServiceDto2.getStatusCode() == EnumStatusConstant.OK.getId()) {*/
			ServiceDto2<Object> daoResponse = moneyHandler
					.getBankList(model);
				System.out.println("response from get getBankList version1 controller");
				if (daoResponse.getStatusCode() == EnumStatusConstant.OK.getId()) {
					response. setStatusCode(daoResponse.getStatusCode());
					response.setStatusMessage(daoResponse.getStatusMessage());
					response.setBody(daoResponse.getBody());
					logger.info("response returnging from getBankList controller::");
					return response;
				}else{
					response. setStatusCode(daoResponse.getStatusCode());
					response.setStatusMessage(daoResponse.getStatusMessage());
					response.setBody(null);
					logger.info("Error in get dmt getBankList controller");
					return response;
				}
			//}else return ControllerResponse2.createControllerResponse2(ServiceDto2);
		}
		return response;
	}
	
	@RequestMapping(value="/dmt/checkStatus/version1", method=RequestMethod.POST)
	public @ResponseBody ResponseInf<Object> checkStatus(@RequestBody AcqMoneyTransferCheckStatusModel model,HttpServletRequest request){
		logger.info("request landing for checkStatus");
		ControllerResponse2<Object> response = new ControllerResponse2<Object>();
		ValidatorFactory vFactory = Validation.buildDefaultValidatorFactory();
		Validator validator = vFactory.getValidator();
		Set<ConstraintViolation<AcqMoneyTransferCheckStatusModel>> inputErrorSet = validator.validate(model);
		if (inputErrorSet.size() > 0) {
			response. setStatusCode(EnumStatusConstant.InvalidCrediential.getId());
			Iterator<ConstraintViolation<AcqMoneyTransferCheckStatusModel>> itr = inputErrorSet.iterator();
			while (itr.hasNext()) {
				ConstraintViolation<AcqMoneyTransferCheckStatusModel> validate = (ConstraintViolation<AcqMoneyTransferCheckStatusModel>) itr.next();
				response.setStatusMessage(validate.getMessage());
			}			
		} else {
			Date date = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:MM:ss");
			model.setDateTime(format.format(date));
			/*ServiceDto2 ServiceDto2 = AcqSession.isSessionRunning(request,model.getSessionId());
			if (ServiceDto2.getStatusCode() == EnumStatusConstant.OK.getId()) {*/
			ServiceDto2<Object> daoResponse = moneyHandler.checkStatus(model);
				System.out.println("response from get checkStatus version1 controller");
				if (daoResponse.getStatusCode() == EnumStatusConstant.OK.getId()) {
					response. setStatusCode(daoResponse.getStatusCode());
					response.setStatusMessage(daoResponse.getStatusMessage());
					response.setBody(daoResponse.getBody());
					logger.info("response returnging from checkStatus controller::");
					return response;
				}else{
					response. setStatusCode(daoResponse.getStatusCode());
					response.setStatusMessage(daoResponse.getStatusMessage());
					response.setBody(null);
					logger.info("Error in get dmt checkStatus controller");
					return response;
				}
			//}else return ControllerResponse2.createControllerResponse2(ServiceDto2);
		}
		return response;
	}
	
	@RequestMapping(value="/dmt/cardLoad/version1", method=RequestMethod.POST)
	public @ResponseBody ResponseInf<Object> cardLoad(@RequestBody AcqBankItCardLoadModel model,HttpServletRequest request){
		logger.info("request landing for cardLoad");
		ControllerResponse2<Object> response = new ControllerResponse2<Object>();
		ValidatorFactory vFactory = Validation.buildDefaultValidatorFactory();
		Validator validator = vFactory.getValidator();
		Set<ConstraintViolation<AcqBankItCardLoadModel>> inputErrorSet = validator.validate(model);
		if (inputErrorSet.size() > 0) {
			response. setStatusCode(EnumStatusConstant.InvalidCrediential.getId());
			Iterator<ConstraintViolation<AcqBankItCardLoadModel>> itr = inputErrorSet.iterator();
			while (itr.hasNext()) {
				ConstraintViolation<AcqBankItCardLoadModel> validate = (ConstraintViolation<AcqBankItCardLoadModel>) itr.next();
				response.setStatusMessage(validate.getMessage());
			}			
		} else {
			String ipAddress = request.getHeader("X-FORWARDED-FOR");
			if (ipAddress == null) {
				ipAddress = request.getRemoteAddr();
				//model.setIpAddress(ipAddress); 
				model.setIpAddress("18.218.17.153");
				System.out.println(",Info: Client ip addres is found");
			}	
			Date date = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:MM:ss");
			model.setDateTime(format.format(date));
			/*ServiceDto2 ServiceDto2 = AcqSession.isSessionRunning(request,model.getSessionId());
			if (ServiceDto2.getStatusCode() == EnumStatusConstant.OK.getId()) {*/
			ServiceDto2<Object> daoResponse = moneyHandler.cardLoad(model);
				System.out.println("response from get cardLoad version1 controller");
				if (daoResponse.getStatusCode() == EnumStatusConstant.OK.getId()){
					response. setStatusCode(daoResponse.getStatusCode());
					response.setStatusMessage(daoResponse.getStatusMessage());
					response.setBody(daoResponse.getBody());
					logger.info("response returnging from cardLoad controller::");
					return response;
				}else{
					response. setStatusCode(daoResponse.getStatusCode());
					response.setStatusMessage(daoResponse.getStatusMessage());
					response.setBody(null);
					logger.info("Error in get dmt cardLoad controller");
					return response;
				}
			//}else return ControllerResponse2.createControllerResponse2(ServiceDto2);
		}
		return response;
	}	
	
	@RequestMapping(value="/dmt/cardLoadStatus/version1", method=RequestMethod.POST)
	public @ResponseBody ResponseInf<Object> cardLoadStatus(@RequestBody AcqBankItCardLoadModel model,HttpServletRequest request){
		logger.info("request landing for cardLoad");
		ControllerResponse2<Object> response = new ControllerResponse2<Object>();
		ValidatorFactory vFactory = Validation.buildDefaultValidatorFactory();
		Validator validator = vFactory.getValidator();
		Set<ConstraintViolation<AcqBankItCardLoadModel>> inputErrorSet = validator.validate(model);
		if (inputErrorSet.size() > 0) {
			response. setStatusCode(EnumStatusConstant.InvalidCrediential.getId());
			Iterator<ConstraintViolation<AcqBankItCardLoadModel>> itr = inputErrorSet.iterator();
			while (itr.hasNext()) {
				ConstraintViolation<AcqBankItCardLoadModel> validate = (ConstraintViolation<AcqBankItCardLoadModel>) itr.next();
				response.setStatusMessage(validate.getMessage());
			}			
		} else {
			String ipAddress = request.getHeader("X-FORWARDED-FOR");	
			Date date = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:MM:ss");
			model.setDateTime(format.format(date));
			/*ServiceDto2 ServiceDto2 = AcqSession.isSessionRunning(request,model.getSessionId());
			if (ServiceDto2.getStatusCode() == EnumStatusConstant.OK.getId()) {*/
			ServiceDto2<Object> daoResponse = moneyHandler.cardLoadStatus(model);
				System.out.println("response from get cardLoadStatus version1 controller");
				if (daoResponse.getStatusCode() == EnumStatusConstant.OK.getId()){
					response. setStatusCode(daoResponse.getStatusCode());
					response.setStatusMessage(daoResponse.getStatusMessage());
					response.setBody(daoResponse.getBody());
					logger.info("response returnging from cardLoadStatus controller::");
					return response;
				}else{
					response. setStatusCode(daoResponse.getStatusCode());
					response.setStatusMessage(daoResponse.getStatusMessage());
					response.setBody(null);
					logger.info("Error in get dmt cardLoad controller");
					return response;
				}
			//}else return ControllerResponse2.createControllerResponse2(ServiceDto2);
		}
		return response;
	}	
	
	@RequestMapping(value="/dmt/getCardBal/version1", method=RequestMethod.POST)
	public @ResponseBody ResponseInf<Object> getCardBal(@RequestBody AcqBankItCardLoadModel model,HttpServletRequest request){
		logger.info("request landing for getCardBal");
		ControllerResponse2<Object> response = new ControllerResponse2<Object>();
		ValidatorFactory vFactory = Validation.buildDefaultValidatorFactory();
		Validator validator = vFactory.getValidator();
		Set<ConstraintViolation<AcqBankItCardLoadModel>> inputErrorSet = validator.validate(model);
		if (inputErrorSet.size() > 0) {
			response. setStatusCode(EnumStatusConstant.InvalidCrediential.getId());
			Iterator<ConstraintViolation<AcqBankItCardLoadModel>> itr = inputErrorSet.iterator();
			while (itr.hasNext()) {
				ConstraintViolation<AcqBankItCardLoadModel> validate = (ConstraintViolation<AcqBankItCardLoadModel>) itr.next();
				response.setStatusMessage(validate.getMessage());
			}			
		} else {
			String ipAddress = request.getHeader("X-FORWARDED-FOR");	
			Date date = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:MM:ss");
			model.setDateTime(format.format(date));
			/*ServiceDto2 ServiceDto2 = AcqSession.isSessionRunning(request,model.getSessionId());
			if (ServiceDto2.getStatusCode() == EnumStatusConstant.OK.getId()) {*/
			ServiceDto2<Object> daoResponse = moneyHandler.getCardBal(model);
				System.out.println("response from getCardBal version1 controller");
				if (daoResponse.getStatusCode() == EnumStatusConstant.OK.getId()){
					response. setStatusCode(daoResponse.getStatusCode());
					response.setStatusMessage(daoResponse.getStatusMessage());
					response.setBody(daoResponse.getBody());
					logger.info("response returnging from getCardBal controller::");
					return response;
				}else{
					response. setStatusCode(daoResponse.getStatusCode());
					response.setStatusMessage(daoResponse.getStatusMessage());
					response.setBody(null);
					logger.info("Error in get dmt getCardBal controller");
					return response;
				}
			//}else return ControllerResponse2.createControllerResponse2(ServiceDto2);
		}
		return response;
	}	
}
