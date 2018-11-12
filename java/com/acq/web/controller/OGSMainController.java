package com.acq.web.controller;

import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.acq.EnumStatusConstant;
import com.acq.web.controller.model.AcqOGSMainKeyExchangeModel;
import com.acq.web.controller.model.OGSMainKeyExModel;
import com.acq.web.controller.model.OGSMainLoginModel;
import com.acq.web.controller.model.OGSMainReversalTxnModel;
import com.acq.web.controller.model.OGSMainTxnCompleteModel;
import com.acq.web.controller.model.OGSMainTxnInitiateModel;
import com.acq.web.controller.model.OGSMainVoidTnxModel;
import com.acq.web.controller.model.OGSMainVoidTxnModel;
import com.acq.web.dto.ResponseInf;
import com.acq.web.dto.impl.ControllerResponse;
import com.acq.web.dto.impl.ControllerResponse2;
import com.acq.web.dto.impl.ServiceDto;
import com.acq.web.handler.OGSMainHandlerInf;

@Controller
@RequestMapping(value = "/ogs")
public class OGSMainController{
	
	final static Logger logger = Logger.getLogger(OGSMainController.class);
	
	@Autowired
	OGSMainHandlerInf ogsMainHandler;
	
	
	
	@RequestMapping(value = "/reversal", method = RequestMethod.POST)
	public @ResponseBody
	ResponseInf<Object> getReversalNew(@RequestParam String txnId,HttpServletRequest request) {
		ControllerResponse<Object> response = new ControllerResponse<Object>();
		logger.info("req in ogs txn init cntrl ver1");
		try{
			ServiceDto<Object> daoResponse = ogsMainHandler.getReversal(txnId);	
			response.setStatus(daoResponse.getStatus());
			response.setMessage(daoResponse.getMessage());
		}catch(Exception e){
			logger.error("error to start reversal thread ");
		}
		return response;
	}
	
	@RequestMapping(value = "/doVoid/version1", method = RequestMethod.POST)
	public @ResponseBody
	ResponseInf<Object> mv20OGSVoidTxnInit(@RequestBody OGSMainVoidTxnModel model,
			HttpServletRequest request) {
		ControllerResponse2<Object> response = new ControllerResponse2<Object>();
		logger.info("req in ogs voidtxn cntrl ver1");
		try{
			ValidatorFactory vFactory = Validation.buildDefaultValidatorFactory();
			Validator validator = vFactory.getValidator();
			Set<ConstraintViolation<OGSMainVoidTxnModel>> inputErrorSet = validator.validate(model);
			if(inputErrorSet.size() > 0){
				response.setStatusCode(EnumStatusConstant.InvalidCrediential.getId());
				Iterator<ConstraintViolation<OGSMainVoidTxnModel>> itr = inputErrorSet.iterator();
				while(itr.hasNext()){
					ConstraintViolation<OGSMainVoidTxnModel> validate = (ConstraintViolation<OGSMainVoidTxnModel>) itr
							.next();
					response.setStatusMessage(validate.getMessage());
				}
				return response;
			}else{
				ServiceDto<Object> daoResponse = ogsMainHandler.mv20OGSVoidTxnInit(model);	
				response.setStatusCode(daoResponse.getStatus());
				response.setStatusMessage(daoResponse.getMessage());
				response.setBody(daoResponse.getResult());
				logger.info("res from vm20ogs void cntlr");
				return response;
			}
		}catch(Exception e){
			response.setStatusCode(EnumStatusConstant.RollBackError.getId());
			response.setStatusMessage(EnumStatusConstant.RollBackError
					.getDescription());
			response.setBody(null);
			logger.error("Error in vm20ogstxn void cntrlr "+e);
		}
		return response;
	}	
	
	
	@RequestMapping(value = "/doReversal/version1", method = RequestMethod.POST)
	public @ResponseBody
	ResponseInf<Object> mv20OGSRvrslTxnInit(@RequestBody OGSMainReversalTxnModel model,
			HttpServletRequest request) {
		ControllerResponse2<Object> response = new ControllerResponse2<Object>();
		logger.info("req in ogs rvrsl txn cntrl ver1");
		try{
			ValidatorFactory vFactory = Validation.buildDefaultValidatorFactory();
			Validator validator = vFactory.getValidator();
			Set<ConstraintViolation<OGSMainReversalTxnModel>> inputErrorSet = validator.validate(model);
			if(inputErrorSet.size() > 0){
				response.setStatusCode(EnumStatusConstant.InvalidCrediential.getId());
				Iterator<ConstraintViolation<OGSMainReversalTxnModel>> itr = inputErrorSet.iterator();
				while(itr.hasNext()){
					ConstraintViolation<OGSMainReversalTxnModel> validate = (ConstraintViolation<OGSMainReversalTxnModel>) itr
							.next();
					response.setStatusMessage(validate.getMessage());
				}
				return response;
			}else{
				ServiceDto<Object> daoResponse = ogsMainHandler.mv20OGSRvrslTxnInit(model);	
				response.setStatusCode(daoResponse.getStatus());
				response.setStatusMessage(daoResponse.getMessage());
				response.setBody(daoResponse.getResult());
				logger.info("res from rvrsl inittxn cntlr");
				return response;
			}
		}catch(Exception e){
			response.setStatusCode(EnumStatusConstant.RollBackError.getId());
			response.setStatusMessage(EnumStatusConstant.RollBackError
					.getDescription());
			response.setBody(null);
			logger.error("Error in rvrsl txn cntrlr "+e);
		}
		return response;
	}
	
		
	
	@RequestMapping(value = "/getVoidList/version1111", method = RequestMethod.POST)
	public @ResponseBody
	ResponseInf<Object> ogsvoidTxnList(@RequestBody OGSMainVoidTnxModel model,
			HttpServletRequest request) {
		ControllerResponse<Object> response = new ControllerResponse<Object>();
		logger.info("req in vm20 void txnlist cntrlr");
		ValidatorFactory vFactory = Validation.buildDefaultValidatorFactory();
		Validator validator = vFactory.getValidator();
		Set<ConstraintViolation<OGSMainVoidTnxModel>> inputErrorSet = validator.validate(model);
		try{
			if(inputErrorSet.size() > 0){
				response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
				Iterator<ConstraintViolation<OGSMainVoidTnxModel>> itr = inputErrorSet.iterator();
				while(itr.hasNext()){
					ConstraintViolation<OGSMainVoidTnxModel> validate = (ConstraintViolation<OGSMainVoidTnxModel>) itr
							.next();
					response.setMessage(validate.getMessage());
				}
				return response;
			}else{
				ServiceDto<Object> daoResponse  = ogsMainHandler.ogsVM20VoidTxnList(model);
				response.setStatus(daoResponse.getStatus());
				response.setMessage(daoResponse.getMessage());
				response.setResult(daoResponse.getResult());
				System.out.println("daoResponse.getResult()333:"+daoResponse.getResult());
				logger.info("response returning from ogs txn void list controller");
				return response;
			}
		}catch(Exception e){
			response.setStatus(EnumStatusConstant.RollBackError.getId());
			response.setMessage(EnumStatusConstant.RollBackError.getDescription());
			response.setResult(null);
			logger.info("Error in ogs vm20 void list cntrlr "+e);
		}
		return response;
	}
	//@RequestMapping(value="/followUpAdd", method=RequestMethod.POST)

	@RequestMapping(value = "/doPurchaseComplete/version1", method = RequestMethod.POST)
	public @ResponseBody
	ResponseInf<Object> mv20OGSTxnComplete(@RequestBody OGSMainTxnCompleteModel model,
			HttpServletRequest request) {
		ControllerResponse<Object> response = new ControllerResponse<Object>();
		logger.info("req in ogs txn init cntrl ver1");
		try{
			ValidatorFactory vFactory = Validation.buildDefaultValidatorFactory();
			Validator validator = vFactory.getValidator();
			Set<ConstraintViolation<OGSMainTxnCompleteModel>> inputErrorSet = validator.validate(model);
			if(inputErrorSet.size() > 0){
				response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
				Iterator<ConstraintViolation<OGSMainTxnCompleteModel>> itr = inputErrorSet.iterator();
				while(itr.hasNext()){
					ConstraintViolation<OGSMainTxnCompleteModel> validate = (ConstraintViolation<OGSMainTxnCompleteModel>) itr
							.next();
					response.setMessage(validate.getMessage());
				}
				return response;
			}else{
				ServiceDto<Object> daoResponse = ogsMainHandler.mv20OGSTxnComplete(model);	
				response.setStatus(daoResponse.getStatus());
				response.setMessage(daoResponse.getMessage());
				response.setResult(daoResponse.getResult());
				logger.info("res from vm20ogs inittxn cntlr");
				return response;
			}
		}catch(Exception e){
			response.setStatus(EnumStatusConstant.RollBackError.getId());
			response.setMessage(EnumStatusConstant.RollBackError
					.getDescription());
			response.setResult(null);
			logger.error("Error in vm20ogstxn init cntrlr "+e);
		}
		return response;
	}
	
	@RequestMapping(value = "/doPurchaseWithCashBack/version1", method = RequestMethod.POST)
	public @ResponseBody
	ResponseInf<Object> vm20DoPrchsWthCshBck(@RequestBody OGSMainTxnInitiateModel model,
			HttpServletRequest request) {
		ControllerResponse<Object> response = new ControllerResponse<Object>();
		logger.info("req in ogs txn purchs wth cshbck cntrl ver1");		
		try{
			ValidatorFactory vFactory = Validation.buildDefaultValidatorFactory();
			Validator validator = vFactory.getValidator();
			Set<ConstraintViolation<OGSMainTxnInitiateModel>> inputErrorSet = validator.validate(model);
			if(inputErrorSet.size() > 0){
				response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
				Iterator<ConstraintViolation<OGSMainTxnInitiateModel>> itr = inputErrorSet.iterator();
				while(itr.hasNext()){
					ConstraintViolation<OGSMainTxnInitiateModel> validate = (ConstraintViolation<OGSMainTxnInitiateModel>) itr
							.next();
					response.setMessage(validate.getMessage());
				}
				return response;
			}else{
				ServiceDto<Object> daoResponse = ogsMainHandler.vm20DoPrchsWthCshBck(model);	
				response.setStatus(daoResponse.getStatus());
				response.setMessage(daoResponse.getMessage());
				response.setResult(daoResponse.getResult());
				logger.info("res from vm20ogs  purchs wth cshbck cntlr");
				return response;
			}
		}catch(Exception e){
			response.setStatus(EnumStatusConstant.RollBackError.getId());
			response.setMessage(EnumStatusConstant.RollBackError
					.getDescription());
			response.setResult(null);
			logger.error("Error in vm20ogstxn init cntrlr "+e);
		}
		return response;
	}
	
	@RequestMapping(value = "/doCashAtPos/version1", method = RequestMethod.POST)
	public @ResponseBody
	ResponseInf<Object> doCashAtPosInitiate(@RequestBody OGSMainTxnInitiateModel model,
			HttpServletRequest request) {
//		ControllerResponse<Object> response = new ControllerResponse<Object>();
		ControllerResponse2<Object> response = new ControllerResponse2<Object>();
		logger.info("req in ogs castatpos init cntrl ver1");		
		try{
			ValidatorFactory vFactory = Validation.buildDefaultValidatorFactory();
			Validator validator = vFactory.getValidator();
			Set<ConstraintViolation<OGSMainTxnInitiateModel>> inputErrorSet = validator.validate(model);
			if(inputErrorSet.size() > 0){
				response.setStatusCode(EnumStatusConstant.InvalidCrediential.getId());
				Iterator<ConstraintViolation<OGSMainTxnInitiateModel>> itr = inputErrorSet.iterator();
				while(itr.hasNext()){
					ConstraintViolation<OGSMainTxnInitiateModel> validate = (ConstraintViolation<OGSMainTxnInitiateModel>) itr
							.next();
					response.setStatusMessage(validate.getMessage());
				}
				return response;
			}else{
				ServiceDto<Object> daoResponse = ogsMainHandler.doCashAtPosInitiate(model);	
				response.setStatusCode(daoResponse.getStatus());
				response.setStatusMessage(daoResponse.getMessage());
				response.setBody(daoResponse.getResult());
				logger.info("res from vm20ogs castatpos inittxn cntlr");
				return response;
			}
		}catch(Exception e){
			response.setStatusCode(EnumStatusConstant.RollBackError.getId());
			response.setStatusMessage(EnumStatusConstant.RollBackError
					.getDescription());
			response.setBody(null);
			logger.error("Error in vm20ogs castatpos txn init cntrlr "+e);
		}
		return response;
	}
	
	@RequestMapping(value = "/doPurchase/version1", method = RequestMethod.POST)
	public @ResponseBody
	ResponseInf<Object> mv20OGSTxnInitiate(@RequestBody OGSMainTxnInitiateModel model,
			HttpServletRequest request) {
//		ControllerResponse<Object> response = new ControllerResponse<Object>();
		ControllerResponse2<Object> response = new ControllerResponse2<Object>();
		logger.info("req in ogs txn init cntrl ver1");
		try{
			ValidatorFactory vFactory = Validation.buildDefaultValidatorFactory();
			Validator validator = vFactory.getValidator();
			Set<ConstraintViolation<OGSMainTxnInitiateModel>> inputErrorSet = validator.validate(model);
			if(inputErrorSet.size() > 0){
				response.setStatusCode(EnumStatusConstant.InvalidCrediential.getId());
				Iterator<ConstraintViolation<OGSMainTxnInitiateModel>> itr = inputErrorSet.iterator();
				while(itr.hasNext()){
					ConstraintViolation<OGSMainTxnInitiateModel> validate = (ConstraintViolation<OGSMainTxnInitiateModel>) itr
							.next();
					response.setStatusMessage(validate.getMessage());					
				}
				return response;
			}else{
				ServiceDto<Object> daoResponse = ogsMainHandler.mv20OGSTxnInitiate(model);	
				response.setStatusCode(daoResponse.getStatus());
				response.setStatusMessage(daoResponse.getMessage());
				response.setBody(daoResponse.getResult());
				logger.info("res from vm20ogs inittxn cntlr");
				return response;
			}
		}catch(Exception e){
			response.setStatusCode(EnumStatusConstant.RollBackError.getId());
			response.setStatusMessage(EnumStatusConstant.RollBackError
					.getDescription());
			response.setBody(null);
			logger.error("Error in vm20ogstxn init cntrlr "+e);
		}
		return response;
	}
	
	@RequestMapping(value = "/getDUKPTACK/version1", method = RequestMethod.POST)
	public @ResponseBody
	ResponseInf<Object> mv20OGSgetDUKPTACK(@RequestBody AcqOGSMainKeyExchangeModel model,HttpServletRequest request) {
		logger.info("rest in vm20ogs dukpt ack ver1");
		ValidatorFactory vFactory = Validation.buildDefaultValidatorFactory();
		Validator validator = vFactory.getValidator();
		Set<ConstraintViolation<AcqOGSMainKeyExchangeModel>> inputErrorSet = validator.validate(model);
//		ControllerResponse<Object> response = new ControllerResponse<Object>();
		ControllerResponse2<Object> response = new ControllerResponse2<Object>();
		try{
			if(inputErrorSet.size() > 0){
				response.setStatusCode(EnumStatusConstant.InvalidCrediential.getId());
				Iterator<ConstraintViolation<AcqOGSMainKeyExchangeModel>> itr = inputErrorSet.iterator();
				while(itr.hasNext()){
					ConstraintViolation<AcqOGSMainKeyExchangeModel> validate = (ConstraintViolation<AcqOGSMainKeyExchangeModel>) itr
							.next();
					response.setStatusMessage(validate.getMessage());
				}
				return response;
			}else{
				ServiceDto<Object> daoResponse = ogsMainHandler.mv20OGSMainGetDUKPTACK(model);	
				response.setStatusCode(daoResponse.getStatus());
				response.setStatusMessage(daoResponse.getMessage());
				response.setBody(daoResponse.getResult());
				logger.info("response from vm20 dukpt ack controller");
				return response;
			}
		}catch(Exception e){
			response.setStatusCode(EnumStatusConstant.RollBackError.getId());
			response.setStatusMessage(EnumStatusConstant.RollBackError
					.getDescription());
			response.setBody(null);
			logger.info("Error in vm20 dukpt ack controller "+e);
		}
		return response;
	}
	
	@RequestMapping(value = "/getDUKPT/version1", method = RequestMethod.POST)
	public @ResponseBody
	ResponseInf<Object> mv20OGSgetDUKPT(@RequestBody AcqOGSMainKeyExchangeModel model,HttpServletRequest request) {
		logger.info("rest in vm20ogs keyex dukpt ver1");
		ValidatorFactory vFactory = Validation.buildDefaultValidatorFactory();
		Validator validator = vFactory.getValidator();
		Set<ConstraintViolation<AcqOGSMainKeyExchangeModel>> inputErrorSet = validator.validate(model);
//		ControllerResponse<Object> response = new ControllerResponse<Object>();
		ControllerResponse2<Object> response = new ControllerResponse2<Object>();

		try{
			if(inputErrorSet.size() > 0){
				response.setStatusCode(EnumStatusConstant.InvalidCrediential.getId());
				Iterator<ConstraintViolation<AcqOGSMainKeyExchangeModel>> itr = inputErrorSet.iterator();
				while(itr.hasNext()){
					ConstraintViolation<AcqOGSMainKeyExchangeModel> validate = (ConstraintViolation<AcqOGSMainKeyExchangeModel>) itr
							.next();
					response.setStatusMessage(validate.getMessage());
				}
				return response;
			}else{
				ServiceDto<Object> daoResponse = ogsMainHandler.getDUKPTOgsMain(model);	
				response.setStatusCode(daoResponse.getStatus());
				response.setStatusMessage(daoResponse.getMessage());
				response.setBody(daoResponse.getResult());
				logger.info("response from vm20 get dukpt controller");
				return response;
			}
		}catch(Exception e){
			response.setStatusCode(EnumStatusConstant.RollBackError.getId());
			response.setStatusMessage(EnumStatusConstant.RollBackError
					.getDescription());
			response.setBody(null);
			logger.info("Error in vm20 get dukpt controller "+e);
			System.err.println("Error in vm20 get dukpt controller "+e);
		}
		return response;
	}
	
	@RequestMapping(value = "/getTMKACK/version1", method = RequestMethod.POST)
	public @ResponseBody
	ResponseInf<Object> mv20OGSgetTMKACK(@RequestBody AcqOGSMainKeyExchangeModel model,HttpServletRequest request) {
		logger.info("rest in vm20ogs tmk ack ver1");
		ValidatorFactory vFactory = Validation.buildDefaultValidatorFactory();
		Validator validator = vFactory.getValidator();
		Set<ConstraintViolation<AcqOGSMainKeyExchangeModel>> inputErrorSet = validator.validate(model);
		//ControllerResponse<Object> response = new ControllerResponse<Object>();
		ControllerResponse2<Object> response = new ControllerResponse2<Object>();	
		try{
			if(inputErrorSet.size() > 0){
				response.setStatusCode(EnumStatusConstant.InvalidCrediential.getId());
				Iterator<ConstraintViolation<AcqOGSMainKeyExchangeModel>> itr = inputErrorSet.iterator();
				while(itr.hasNext()){
					ConstraintViolation<AcqOGSMainKeyExchangeModel> validate = (ConstraintViolation<AcqOGSMainKeyExchangeModel>) itr
							.next();
					response.setBody(validate.getMessage());
				}
				return response;
			}else{
				ServiceDto<Object> daoResponse = ogsMainHandler.mv20OGSMainGetTMKACK(model);	
				response.setStatusCode(daoResponse.getStatus());
				response.setStatusMessage(daoResponse.getMessage());
				response.setBody(daoResponse.getResult());
				logger.info("response from vm20 keyex ack controller");
				return response;
			}
		}catch(Exception e){
			response.setStatusCode(EnumStatusConstant.RollBackError.getId());
			response.setStatusMessage(EnumStatusConstant.RollBackError
					.getDescription());
			response.setBody(null);
			logger.info("Error in vm20 keyex controller "+e);
		}
		return response;
	}
	
	@RequestMapping(value = "/getTMK/version1", method = RequestMethod.POST)
	public @ResponseBody
	ResponseInf<Object> getTMK(@RequestBody AcqOGSMainKeyExchangeModel model,HttpServletRequest request) {
		logger.info("rest in vm20ogs keyex ver1");
		ValidatorFactory vFactory = Validation.buildDefaultValidatorFactory();
		Validator validator = vFactory.getValidator();
		Set<ConstraintViolation<AcqOGSMainKeyExchangeModel>> inputErrorSet = validator.validate(model);
//		ControllerResponse<Object> response = new ControllerResponse<Object>();
		ControllerResponse2<Object> response = new ControllerResponse2<Object>();
		try{
			if(inputErrorSet.size() > 0){
				response.setStatusCode(EnumStatusConstant.InvalidCrediential.getId());
				Iterator<ConstraintViolation<AcqOGSMainKeyExchangeModel>> itr = inputErrorSet.iterator();
				while(itr.hasNext()){
					ConstraintViolation<AcqOGSMainKeyExchangeModel> validate = (ConstraintViolation<AcqOGSMainKeyExchangeModel>) itr
							.next();
					response.setBody(validate.getMessage());
				}
				return response;
			}else{
				ServiceDto<Object> daoResponse = ogsMainHandler.getTMKOgsMain(model);	
				response.setStatusCode(daoResponse.getStatus());
				response.setStatusMessage(daoResponse.getMessage());
				response.setBody(daoResponse.getResult());
				
				logger.info("response from vm20 keyex controller");
				return response;
			}
		}catch(Exception e){
			response.setStatusCode(EnumStatusConstant.RollBackError.getId());
			response.setStatusMessage(EnumStatusConstant.RollBackError
					.getDescription());
			response.setBody(null);
			logger.info("Error in vm20 keyex controller "+e);
		}
		return response;
	}
	
	
	@RequestMapping(value = "/keyExchange/version1", method = RequestMethod.POST)
	public @ResponseBody
	ResponseInf<Object> vm20OgsKeyExchange(@ModelAttribute OGSMainKeyExModel model,
			HttpServletRequest request) {
		logger.info("rest in vm20ogs keyex ver1");
		ValidatorFactory vFactory = Validation.buildDefaultValidatorFactory();
		Validator validator = vFactory.getValidator();
		Set<ConstraintViolation<OGSMainKeyExModel>> inputErrorSet = validator.validate(model);
		ControllerResponse<Object> response = new ControllerResponse<Object>();
		try{
			if(inputErrorSet.size() > 0){
				response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
				Iterator<ConstraintViolation<OGSMainKeyExModel>> itr = inputErrorSet.iterator();
				while(itr.hasNext()){
					ConstraintViolation<OGSMainKeyExModel> validate = (ConstraintViolation<OGSMainKeyExModel>) itr.next();
					response.setMessage(validate.getMessage());
				}
				return response;
			}else{
				ServiceDto<Object> daoResponse = ogsMainHandler.vm20OgsKeyExchange(model);	
				response.setStatus(daoResponse.getStatus());
				response.setMessage(daoResponse.getMessage());
				response.setResult(daoResponse.getResult());
				logger.info("response from vm20 keyex controller");
				return response;
			}
		}catch(Exception e){
			response.setStatus(EnumStatusConstant.RollBackError.getId());
			response.setMessage(EnumStatusConstant.RollBackError
					.getDescription());
			response.setResult(null);
			logger.info("Error in vm20 keyex controller "+e);
		}
		return response;
	}	
	
	

	@RequestMapping(value = "ogsss/authenticate/version1", method = RequestMethod.POST)
	public @ResponseBody
	ResponseInf<Object> getLogin6(@RequestBody OGSMainLoginModel model,
				HttpServletRequest request) {		
		System.out.println("req in authenticate vm20login cntrlr");
		String clientIpAddress = "";
		ControllerResponse<Object> response = new ControllerResponse<Object>();
		try {
			String ipAddress = request.getHeader("X-FORWARDED-FOR");
			if (ipAddress == null) {
				ipAddress = request.getRemoteAddr();
				clientIpAddress = ipAddress;
			}
			ValidatorFactory vFactory = Validation
					.buildDefaultValidatorFactory();
			Validator validator = vFactory.getValidator();
			Set<ConstraintViolation<OGSMainLoginModel>> inputErrorSet = validator
					.validate(model);
			if (inputErrorSet.size() > 0) {
				response.setStatus(EnumStatusConstant.InvalidCrediential
						.getId());
				Iterator<ConstraintViolation<OGSMainLoginModel>> itr = inputErrorSet
						.iterator();
				while (itr.hasNext()) {
					ConstraintViolation<OGSMainLoginModel> validate = (ConstraintViolation<OGSMainLoginModel>) itr
							.next();
					response.setMessage(validate.getMessage());
				}
			} else {
				ServiceDto<Object> daoResponse = ogsMainHandler.vm20OgsLogin(model, clientIpAddress);	
				System.out.println("res from vm20auth handler");
				if (daoResponse.getStatus() == EnumStatusConstant.OK.getId()){
					HttpSession session = request.getSession();
					session.setAttribute("uname", model.getUsername());
					session.setAttribute("userid", daoResponse.getMessage());
					response.setStatus(daoResponse.getStatus());
					response.setMessage(EnumStatusConstant.OK.getDescription());
					response.setResult(daoResponse.getResult());					
				}else{
					response.setStatus(daoResponse.getStatus());
					response.setMessage(daoResponse.getMessage());
					response.setResult(daoResponse.getResult());
				}
			}
		} catch (Exception e) {
			response.setStatus(EnumStatusConstant.RollBackError.getId());
			response.setMessage(EnumStatusConstant.RollBackError
					.getDescription());
			response.setResult(null);
		}	
		return response;
	}
		
}