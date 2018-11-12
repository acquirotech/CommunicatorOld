package com.acq.web.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.acq.EnumStatusConstant;
import com.acq.AcqSession;
import com.acq.web.controller.model.AcqTransactionHistryModel;
import com.acq.web.dto.ResponseInf;
import com.acq.web.dto.impl.ControllerResponse;
import com.acq.web.dto.impl.ServiceDto;
import com.acq.web.handler.HistoryHandlerInf;



@Controller
@RequestMapping(value = "/")
public class HistoryController {

	@Autowired
	HistoryHandlerInf historyHanler;

	@Autowired
	MessageSource resource;

	final static Logger logger = Logger.getLogger(HistoryController.class);
	
	@RequestMapping(value = "transactionhistory/version1", method = RequestMethod.POST)
	public @ResponseBody
	ResponseInf<Object> txhistry(@ModelAttribute AcqTransactionHistryModel model,
			HttpServletRequest request) {
		logger.info("request landing in transaction history");
		System.out.println("request landing in transaction history version1 controller");
		ValidatorFactory vFactory = Validation.buildDefaultValidatorFactory();
		Validator validator = vFactory.getValidator();
		Set<ConstraintViolation<AcqTransactionHistryModel>> inputErrorSet = validator
				.validate(model);
		ControllerResponse<Object> response = new ControllerResponse<Object>();
		try{
		if (inputErrorSet.size() > 0) {
			response.setStatus(EnumStatusConstant.InvalidCrediential.getId());
			Iterator<ConstraintViolation<AcqTransactionHistryModel>> itr = inputErrorSet
					.iterator();
			while (itr.hasNext()) {
				ConstraintViolation<AcqTransactionHistryModel> validate = (ConstraintViolation<AcqTransactionHistryModel>) itr
						.next();
				response.setMessage(validate.getMessage());
			}
			return response;
		} else {
			ServiceDto serviceDto = AcqSession.isSessionRunning(request,model.getSessionId());
			if (serviceDto.getStatus() == EnumStatusConstant.OK.getId()) {
				ServiceDto<HashMap<String, HashMap<String, String>>> daoResponse = historyHanler
						.transactionHistry(model);
				if (daoResponse.getStatus() == EnumStatusConstant.OK.getId()){
					response.setStatus(daoResponse.getStatus());
					response.setMessage(daoResponse.getMessage());
					//response.setResult(AcquiroSeq.MakeSmart(new JSONObject(daoResponse.getResult()).toString()));
					response.setResult(daoResponse.getResult());
					logger.info("response returning form transaction history controller");
					return response;
				}else{
					response.setStatus(daoResponse.getStatus());
					response.setMessage(daoResponse.getMessage());
					response.setResult(null);
					logger.warn("response returning form transaction history controller");
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
		logger.info("Error to select history"+e);
	}
		return response;
	}
	}

