package com.acq.web.controller;

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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.acq.EnumStatusConstant;
import com.acq.NumberValidator;
import com.acq.web.controller.model.AcqRechargeMerchantBalModel;
import com.acq.web.controller.model.AcqRechargeModel;
import com.acq.web.controller.model.AcqRechargeStatusModel;
import com.acq.web.dto.ResponseInf;
import com.acq.web.dto.impl.ControllerResponse2;
import com.acq.web.dto.impl.ServiceDto2;
import com.acq.web.handler.RechargeHandlerInf;

@Controller
@RequestMapping(value = "/")
public class RechargeController {

	
	@Autowired
	RechargeHandlerInf rechargeHandler;
	
	@Autowired
	MessageSource resource;

	final static Logger logger = Logger.getLogger(RechargeController.class);
	
	@RequestMapping(value="/getOperators/version1", method=RequestMethod.POST)
	public @ResponseBody ResponseInf<Object> getOperators(@ModelAttribute AcqRechargeMerchantBalModel model,HttpServletRequest request) throws ParseException{
		logger.info("request landing get operator controller");
		System.out.println("request landing get operator version1 controller");
		ControllerResponse2<Object> response = new ControllerResponse2<Object>();
		ValidatorFactory vFactory = Validation.buildDefaultValidatorFactory();
		Validator validator = vFactory.getValidator();
		Set<ConstraintViolation<AcqRechargeMerchantBalModel>> inputErrorSet = validator.validate(model);
		if (inputErrorSet.size() > 0) {
			response. setStatusCode(EnumStatusConstant.InvalidCrediential.getId());
			Iterator<ConstraintViolation<AcqRechargeMerchantBalModel>> itr = inputErrorSet.iterator();
			while (itr.hasNext()) {
				ConstraintViolation<AcqRechargeMerchantBalModel> validate = (ConstraintViolation<AcqRechargeMerchantBalModel>) itr
						.next();
				response.setStatusMessage(validate.getMessage());
			}			
		} else {
			/*ServiceDto2 ServiceDto2 = AcqSession.isSessionRunning(request,model.getSessionId());
			if (ServiceDto2.getStatusCode() == EnumStatusConstant.OK.getId()) {*/
				ServiceDto2<Object> daoResponse = rechargeHandler.getOperators(model);
				System.out.println("response from get operator version1 controller");
				if (daoResponse.getStatusCode() == EnumStatusConstant.OK.getId()) {
					response. setStatusCode(daoResponse.getStatusCode());
					response.setStatusMessage(daoResponse.getStatusMessage());
					JSONObject json = (JSONObject)new JSONParser().parse(""+daoResponse.getBody());	
					response.setBody(json);
					logger.info("response returnging from get operator controller::");
					return response;
				}else{
					response. setStatusCode(daoResponse.getStatusCode());
					response.setStatusMessage(daoResponse.getStatusMessage());
					response.setBody(null);
					logger.info("Error in get operator controller");
					return response;
				}
			//}else return ControllerResponse2.createControllerResponse2(ServiceDto2);
		}
		return response;
	}
	
	
	@RequestMapping(value="/getOperators/version2", method=RequestMethod.POST)
	public @ResponseBody ResponseInf<Object> getOperators2(@RequestBody final AcqRechargeMerchantBalModel model,HttpServletRequest request) throws ParseException{
		logger.info("request landing get Operator controller");
		System.out.println("request landing get operator version1 controller");
		ControllerResponse2<Object> response = new ControllerResponse2<Object>();
		ValidatorFactory vFactory = Validation.buildDefaultValidatorFactory();
		Validator validator = vFactory.getValidator();
		Set<ConstraintViolation<AcqRechargeMerchantBalModel>> inputErrorSet = validator.validate(model);
		if (inputErrorSet.size() > 0) {
			response. setStatusCode(EnumStatusConstant.InvalidCrediential.getId());
			Iterator<ConstraintViolation<AcqRechargeMerchantBalModel>> itr = inputErrorSet.iterator();
			while (itr.hasNext()) {
				ConstraintViolation<AcqRechargeMerchantBalModel> validate = (ConstraintViolation<AcqRechargeMerchantBalModel>) itr
						.next();
				response.setStatusMessage(validate.getMessage());
			}			
		} else {
			/*ServiceDto2 ServiceDto2 = AcqSession.isSessionRunning(request,model.getSessionId());
			if (ServiceDto2.getStatusCode() == EnumStatusConstant.OK.getId()) {*/
				ServiceDto2<Object> daoResponse = rechargeHandler.getOperators(model);
				JSONObject json = (JSONObject)new JSONParser().parse(""+daoResponse.getBody());	
				System.out.println("response from get operator version1 controller");
				if (daoResponse.getStatusCode() == EnumStatusConstant.OK.getId()) {
					response. setStatusCode(daoResponse.getStatusCode());
					response.setStatusMessage(daoResponse.getStatusMessage());
					response.setBody(json);
					logger.info("response returnging from get Operator controller::");
					return response;
				}else{
					response. setStatusCode(daoResponse.getStatusCode());
					response.setStatusMessage(daoResponse.getStatusMessage());
					response.setBody(null);
					logger.info("Error in get Operator controller");
					return response;
				}
			//}else return ControllerResponse2.createControllerResponse2(ServiceDto2);
		}
		return response;
	}
	
	@RequestMapping(value="/doRecharge/version1", method=RequestMethod.POST)
	public @ResponseBody ResponseInf<Object> mdrreport(@ModelAttribute AcqRechargeModel model,HttpServletRequest request){
		logger.info("request landing for on doRecharge");
		System.out.println("request landing for doRecharge version1");
		ControllerResponse2<Object> response = new ControllerResponse2<Object>();
			ValidatorFactory vFactory = Validation.buildDefaultValidatorFactory();
			Validator validator = vFactory.getValidator();
			Set<ConstraintViolation<AcqRechargeModel>> inputErrorSet = validator
					.validate(model);
			if (inputErrorSet.size() > 0) {
				response. setStatusCode(EnumStatusConstant.InvalidCrediential.getId());
				Iterator<ConstraintViolation<AcqRechargeModel>> itr = inputErrorSet.iterator();
				while (itr.hasNext()) {
					ConstraintViolation<AcqRechargeModel> validate = (ConstraintViolation<AcqRechargeModel>) itr.next();
					response.setStatusMessage(validate.getMessage());
				}
				return response;
			} else {
				/*ServiceDto2 ServiceDto2 = AcqSession.isSessionRunning(request,model.getSessionId());
				if (ServiceDto2.getStatusCode() == EnumStatusConstant.OK.getId()) {*/
					if(model.getServiceType().equalsIgnoreCase("Mobile")){
						if(!NumberValidator.isPhoneNo(model.getSubscriberId())){
							response. setStatusCode(EnumStatusConstant.UserValidationError.getId());
							response.setStatusMessage("Wrong Mobile Number");
							response.setBody(null);
							return response;
						}
					}
					ServiceDto2<Object> daoResponse = rechargeHandler.doRecharge(model);
					System.out.println("response from doRecharge version1");
					if (daoResponse.getStatusCode() == EnumStatusConstant.OK.getId()) {
						response. setStatusCode(daoResponse.getStatusCode());
						response.setStatusMessage(daoResponse.getStatusMessage());
						JSONObject json = new JSONObject();
						json = (JSONObject) daoResponse.getBody();
						response.setBody(json);
						logger.info("response returnging from doRecharge controller");
						return response;
					}else{
					response. setStatusCode(daoResponse.getStatusCode());
					response.setStatusMessage(daoResponse.getStatusMessage());
					response.setBody(null);
					logger.info("Error in doRecharge controller");
					return response;
					}
			//	}else return ControllerResponse2.createControllerResponse2(ServiceDto2);
			 }}
	
	@RequestMapping(value="/doRecharge/version2", method=RequestMethod.POST)
	public @ResponseBody ResponseInf<Object> doRecharge(@RequestBody final AcqRechargeModel model,HttpServletRequest request){
		logger.info("request landing for on doRecharge");
		System.out.println("request landing for doRecharge version1");
		ControllerResponse2<Object> response = new ControllerResponse2<Object>();
			ValidatorFactory vFactory = Validation.buildDefaultValidatorFactory();
			Validator validator = vFactory.getValidator();
			Set<ConstraintViolation<AcqRechargeModel>> inputErrorSet = validator
					.validate(model);
			if (inputErrorSet.size() > 0) {
				response. setStatusCode(EnumStatusConstant.InvalidCrediential.getId());
				Iterator<ConstraintViolation<AcqRechargeModel>> itr = inputErrorSet.iterator();
				while (itr.hasNext()) {
					ConstraintViolation<AcqRechargeModel> validate = (ConstraintViolation<AcqRechargeModel>) itr.next();
					response.setStatusMessage(validate.getMessage());
				}
				return response;
			} else {
				/*ServiceDto2 ServiceDto2 = AcqSession.isSessionRunning(request,model.getSessionId());
				if (ServiceDto2.getStatusCode() == EnumStatusConstant.OK.getId()) {*/
					if(model.getServiceType().equalsIgnoreCase("Mobile")){
						if(!NumberValidator.isPhoneNo(model.getSubscriberId())){
							response. setStatusCode(EnumStatusConstant.UserValidationError.getId());
							response.setStatusMessage("Wrong Mobile Number");
							response.setBody(null);
							return response;
						}
					}
					ServiceDto2<Object> daoResponse = rechargeHandler.doRecharge(model);
					System.out.println("response from doRecharge version1");
					if (daoResponse.getStatusCode() == EnumStatusConstant.OK.getId()) {
						response. setStatusCode(daoResponse.getStatusCode());
						response.setStatusMessage(daoResponse.getStatusMessage());
						JSONObject json = new JSONObject();
						json = (JSONObject) daoResponse.getBody();
						response.setBody(json);
						logger.info("response returnging from doRecharge controller");
						return response;
					}else{
					response. setStatusCode(daoResponse.getStatusCode());
					response.setStatusMessage(daoResponse.getStatusMessage());
					response.setBody(null);
					logger.info("Error in doRecharge controller");
					return response;
					}
			//	}else return ControllerResponse2.createControllerResponse2(ServiceDto2);
			 }}
		
	@RequestMapping(value = "/getMerchantBalance/version1", method = RequestMethod.POST)
	public @ResponseBody
	ResponseInf<Object> getMerchantBalance(@ModelAttribute AcqRechargeMerchantBalModel model,
			HttpServletRequest request) {
		logger.info("request landing in Merchant Bal Request");
		ControllerResponse2<Object> response = new ControllerResponse2<Object>();
		try{
		ValidatorFactory vFactory = Validation.buildDefaultValidatorFactory();
		Validator validator = vFactory.getValidator();
		Set<ConstraintViolation<AcqRechargeMerchantBalModel>> inputErrorSet = validator.validate(model);
		if (inputErrorSet.size() > 0) {
			response. setStatusCode(EnumStatusConstant.InvalidCrediential.getId());
			Iterator<ConstraintViolation<AcqRechargeMerchantBalModel>> itr = inputErrorSet
					.iterator();
			while (itr.hasNext()) {
				ConstraintViolation<AcqRechargeMerchantBalModel> validate = (ConstraintViolation<AcqRechargeMerchantBalModel>) itr
						.next();
				response.setStatusMessage(validate.getMessage());
			}
			return response;
		} else {
			
			/*ServiceDto2 ServiceDto2 = AcqSession.isSessionRunning(request,
					model.getSessionId());
			if (ServiceDto2.getStatusCode() == EnumStatusConstant.OK.getId()) {*/
				ServiceDto2<Object> daoResponse = rechargeHandler
						.getMerchantBalance(model);
				
				System.out.println("response from Merchant Bal Request");
					response. setStatusCode(daoResponse.getStatusCode());
					response.setStatusMessage(daoResponse.getStatusMessage());
					JSONObject json = new JSONObject();
					json = (JSONObject) daoResponse.getBody();
					response.setBody(json);
					logger.info("response returning from Merchant Bal Request controller");
					return response;
				
			//} else return ControllerResponse2.createControllerResponse2(ServiceDto2);
		
		}}catch (Exception e) {
		response. setStatusCode(EnumStatusConstant.RollBackError.getId());
		response.setStatusMessage(EnumStatusConstant.RollBackError.getDescription());
		response.setBody(null);
		logger.info("Error to Merchant Bal Request Controller"+e);
	}
		return response;
		}
		
	@RequestMapping(value = "/getMerchantBalance/version2", method = RequestMethod.POST)
	public @ResponseBody
	ResponseInf<Object> getMerchantBalance2(@RequestBody final AcqRechargeMerchantBalModel model,
			HttpServletRequest request) {
		logger.info("request landing in Merchant Bal Request::");
		
		ControllerResponse2<Object> response = new ControllerResponse2<Object>();
		
		try{
		ValidatorFactory vFactory = Validation.buildDefaultValidatorFactory();
		Validator validator = vFactory.getValidator();
		Set<ConstraintViolation<AcqRechargeMerchantBalModel>> inputErrorSet = validator
				.validate(model);
		if (inputErrorSet.size() > 0) {
			response. setStatusCode(EnumStatusConstant.InvalidCrediential.getId());
			Iterator<ConstraintViolation<AcqRechargeMerchantBalModel>> itr = inputErrorSet
					.iterator();
			while (itr.hasNext()) {
				ConstraintViolation<AcqRechargeMerchantBalModel> validate = (ConstraintViolation<AcqRechargeMerchantBalModel>) itr
						.next();
				response.setStatusMessage(validate.getMessage());
			}
			return response;
		} else {
			
			/*ServiceDto2 ServiceDto2 = AcqSession.isSessionRunning(request,
					model.getSessionId());
			if (ServiceDto2.getStatusCode() == EnumStatusConstant.OK.getId()) {*/
				ServiceDto2<Object> daoResponse = rechargeHandler
						.getMerchantBalance(model);
				System.out.println("response from Merchant Bal Request");
					response. setStatusCode(daoResponse.getStatusCode());
					response.setStatusMessage(daoResponse.getStatusMessage());
					JSONObject json = new JSONObject();
					json = (JSONObject) daoResponse.getBody();
					response.setBody(json);
					logger.info("response returning from Merchant Bal Request controller");
					return response;
				
			//} else return ControllerResponse2.createControllerResponse2(ServiceDto2);
		}}catch (Exception e) {
		response. setStatusCode(EnumStatusConstant.RollBackError.getId());
		response.setStatusMessage(EnumStatusConstant.RollBackError.getDescription());
		response.setBody(null);
		logger.info("Error to Merchant Bal Request Controller"+e);
	}
		return response;
		}
	
	@RequestMapping(value = "/getRechargeStatus/version1", method = RequestMethod.POST)
	public @ResponseBody
	ResponseInf<Object> getRechargeStatus(@ModelAttribute AcqRechargeStatusModel model,
			HttpServletRequest request) {
		logger.info("request landing in Recharge Status Request");
		ControllerResponse2<Object> response = new ControllerResponse2<Object>();
		try{
		ValidatorFactory vFactory = Validation.buildDefaultValidatorFactory();
		Validator validator = vFactory.getValidator();
		Set<ConstraintViolation<AcqRechargeStatusModel>> inputErrorSet = validator
				.validate(model);
		if (inputErrorSet.size() > 0) {
			response. setStatusCode(EnumStatusConstant.InvalidCrediential.getId());
			Iterator<ConstraintViolation<AcqRechargeStatusModel>> itr = inputErrorSet
					.iterator();
			while (itr.hasNext()) {
				ConstraintViolation<AcqRechargeStatusModel> validate = (ConstraintViolation<AcqRechargeStatusModel>) itr
						.next();
				response.setStatusMessage(validate.getMessage());
			}
			return response;
		} else {
			
			/*ServiceDto2 ServiceDto2 = AcqSession.isSessionRunning(request,
					model.getSessionId());
			if (ServiceDto2.getStatusCode() == EnumStatusConstant.OK.getId()) {*/
				ServiceDto2<Object> daoResponse = rechargeHandler.getRechargeStatus(model);
					response. setStatusCode(daoResponse.getStatusCode());
					response.setStatusMessage(daoResponse.getStatusMessage());
					JSONObject json = (JSONObject)new JSONParser().parse(""+daoResponse.getBody());
					response.setBody(json);
					logger.info("response returning from Merchant Bal Request controller");
					return response;
				
			//} else return ControllerResponse2.createControllerResponse2(ServiceDto2);
		}
		}catch (Exception e) {
		response. setStatusCode(EnumStatusConstant.RollBackError.getId());
		response.setStatusMessage(EnumStatusConstant.RollBackError
				.getDescription());
		response.setBody(null);
		logger.info("Error to Merchant Bal Request Controller"+e);
	}
		return response;
		}
	
	@RequestMapping(value = "/getRechargeStatus/version2", method = RequestMethod.POST)
	public @ResponseBody
	ResponseInf<Object> getRechargeStatus2(@RequestBody final AcqRechargeStatusModel model,
			HttpServletRequest request) {
		logger.info("request landing in Recharge Status Request");
		ControllerResponse2<Object> response = new ControllerResponse2<Object>();
		try{
		ValidatorFactory vFactory = Validation.buildDefaultValidatorFactory();
		Validator validator = vFactory.getValidator();
		Set<ConstraintViolation<AcqRechargeStatusModel>> inputErrorSet = validator
				.validate(model);
		if (inputErrorSet.size() > 0) {
			response. setStatusCode(EnumStatusConstant.InvalidCrediential.getId());
			Iterator<ConstraintViolation<AcqRechargeStatusModel>> itr = inputErrorSet
					.iterator();
			while (itr.hasNext()) {
				ConstraintViolation<AcqRechargeStatusModel> validate = (ConstraintViolation<AcqRechargeStatusModel>) itr
						.next();
				response.setStatusMessage(validate.getMessage());
			}
			return response;
		} else {
			
			/*ServiceDto2 ServiceDto2 = AcqSession.isSessionRunning(request,
					model.getSessionId());
			if (ServiceDto2.getStatusCode() == EnumStatusConstant.OK.getId()) {*/
				ServiceDto2<Object> daoResponse = rechargeHandler
						.getRechargeStatus(model);
				System.out.println("response from Merchant Bal Request");
					response. setStatusCode(daoResponse.getStatusCode());
					response.setStatusMessage(daoResponse.getStatusMessage());
					JSONObject json = (JSONObject)new JSONParser().parse(""+daoResponse.getBody());
					response.setBody(json);
					logger.info("response returning from Merchant Bal Request controller");
					return response;
				
			//} else return ControllerResponse2.createControllerResponse2(ServiceDto2);
		}
		}catch (Exception e) {
		response. setStatusCode(EnumStatusConstant.RollBackError.getId());
		response.setStatusMessage(EnumStatusConstant.RollBackError
				.getDescription());
		response.setBody(null);
		logger.info("Error to Merchant Bal Request Controller"+e);
	}
		return response;
		}
	
	
	@RequestMapping(value = "/getRechargeList/version1", method = RequestMethod.POST)
	public @ResponseBody
	ResponseInf<Object> getRechargeList(@ModelAttribute AcqRechargeMerchantBalModel model,
			HttpServletRequest request) {
		logger.info("request landing in get RechargeList Request");
		ControllerResponse2<Object> response = new ControllerResponse2<Object>();
		try{
		ValidatorFactory vFactory = Validation.buildDefaultValidatorFactory();
		Validator validator = vFactory.getValidator();
		Set<ConstraintViolation<AcqRechargeMerchantBalModel>> inputErrorSet = validator
				.validate(model);
		if (inputErrorSet.size() > 0) {
			response. setStatusCode(EnumStatusConstant.InvalidCrediential.getId());
			Iterator<ConstraintViolation<AcqRechargeMerchantBalModel>> itr = inputErrorSet
					.iterator();
			while (itr.hasNext()) {
				ConstraintViolation<AcqRechargeMerchantBalModel> validate = (ConstraintViolation<AcqRechargeMerchantBalModel>) itr
						.next();
				response.setStatusMessage(validate.getMessage());
			}
			return response;
		} else {			
			/*ServiceDto2 ServiceDto2 = AcqSession.isSessionRunning(request,
					model.getSessionId());
			if (ServiceDto2.getStatusCode() == EnumStatusConstant.OK.getId()) {*/
				ServiceDto2<Object> daoResponse = rechargeHandler
						.getRechargeList(model);
				System.out.println("response from get RechargeList Request");
					response. setStatusCode(daoResponse.getStatusCode());
					response.setStatusMessage(daoResponse.getStatusMessage());
					JSONObject json = (JSONObject)new JSONParser().parse(""+daoResponse.getBody());
					response.setBody(json);
					logger.info("response returning from get RechargeList Request controller");
					return response;
				
		//	} else return ControllerResponse2.createControllerResponse2(ServiceDto2);
		}
		}catch (Exception e) {
		response.setStatusCode(EnumStatusConstant.RollBackError.getId());
		response.setStatusMessage(EnumStatusConstant.RollBackError
				.getDescription());
		response.setBody(null);
		logger.info("Error to Merchant Bal Request Controller"+e);
	}
		return response;
		}
	
	
	@RequestMapping(value = "/getRechargeList/version2", method = RequestMethod.POST)
	public @ResponseBody
	ResponseInf<Object> getRechargeList2(@RequestBody final AcqRechargeMerchantBalModel model,
			HttpServletRequest request) {
		logger.info("request landing in get RechargeList Request");
		ControllerResponse2<Object> response = new ControllerResponse2<Object>();
		try{
		ValidatorFactory vFactory = Validation.buildDefaultValidatorFactory();
		Validator validator = vFactory.getValidator();
		Set<ConstraintViolation<AcqRechargeMerchantBalModel>> inputErrorSet = validator
				.validate(model);
		if (inputErrorSet.size() > 0) {
			response. setStatusCode(EnumStatusConstant.InvalidCrediential.getId());
			Iterator<ConstraintViolation<AcqRechargeMerchantBalModel>> itr = inputErrorSet
					.iterator();
			while (itr.hasNext()) {
				ConstraintViolation<AcqRechargeMerchantBalModel> validate = (ConstraintViolation<AcqRechargeMerchantBalModel>) itr
						.next();
				response.setStatusMessage(validate.getMessage());
			}
			return response;
		} else {
			
			/*ServiceDto2 ServiceDto2 = AcqSession.isSessionRunning(request,
					model.getSessionId());
			if (ServiceDto2.getStatusCode() == EnumStatusConstant.OK.getId()) {*/
				ServiceDto2<Object> daoResponse = rechargeHandler
						.getRechargeList(model);
				System.out.println("response from get RechargeList Request");
					response. setStatusCode(daoResponse.getStatusCode());
					response.setStatusMessage(daoResponse.getStatusMessage());
					JSONObject json = (JSONObject)new JSONParser().parse(""+daoResponse.getBody());	
					response.setBody(json);
					logger.info("response returning from get RechargeList Request controller");
					return response;
				
		//	} else return ControllerResponse2.createControllerResponse2(ServiceDto2);
		}
		}catch (Exception e) {
		response.setStatusCode(EnumStatusConstant.RollBackError.getId());
		response.setStatusMessage(EnumStatusConstant.RollBackError
				.getDescription());
		response.setBody(null);
		logger.info("Error to Merchant Bal Request Controller"+e);
	}
		return response;
		}
	
}
