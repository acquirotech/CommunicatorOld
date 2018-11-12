package com.acq.web.controller;

import java.util.HashMap;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.acq.EnumStatusConstant;
import com.acq.AcqSession;
import com.acq.ISO8583;
import com.acq.NumberValidator;
import com.acq.web.controller.model.AcqChangePasswordModel;
import com.acq.web.controller.model.AcqD200ApiLogin;
import com.acq.web.controller.model.AcqOgsLoginModel;
import com.acq.web.controller.model.OGSMainLoginModel;
import com.acq.web.dto.ResponseInf;
import com.acq.web.dto.impl.ControllerResponse;
import com.acq.web.dto.impl.ControllerResponse2;
import com.acq.web.dto.impl.ServiceDto;
import com.acq.web.dto.impl.ServiceDto2;
import com.acq.web.handler.LoginHandlerInf;

@Controller
@RequestMapping(value = "/")
public class LoginController{

	@Autowired
	LoginHandlerInf loginHanler;

	@Value("#{acqConfig['loggers.location']}")
	private String loginLocation;

	public String getLoginLocation(){
		return loginLocation;
	}

	final static Logger logger = Logger.getLogger(LoginController.class);

	@RequestMapping(value = "logout/version1", method = RequestMethod.GET)
	public @ResponseBody
	ControllerResponse<Object> getLogout(HttpServletRequest request) {
		System.out.println("request landing in logout version1");
		logger.info("request landing in logout");
		ControllerResponse<Object> response = new ControllerResponse<Object>();
		HttpSession session = request.getSession();
		session.invalidate();
		logger.info("Session logout successfully");
		response.setStatus(EnumStatusConstant.OK.getId());
		response.setMessage(EnumStatusConstant.OK.getDescription());
		response.setResult(null);
		System.out.println("response from logout version1");
		return response;
	}
	
	
	@RequestMapping(value = "tpos/authenticate/version1", method = RequestMethod.POST)
	public @ResponseBody
	ControllerResponse2<Object> getLogintpos(HttpServletRequest request,AcqD200ApiLogin model) {
		System.out.println("request in authenticate tpos version1 controller");
		String status = "";
		StringBuffer logReport = new StringBuffer();
		logReport.append(",Info:Request intercepted in login tpos version1 controller");
		String clientIpAddress = "";
		ControllerResponse2<Object> response = new ControllerResponse2<Object>();
		try {
			String ipAddress = request.getHeader("X-FORWARDED-FOR");
			if (ipAddress == null) {
				ipAddress = request.getRemoteAddr();
				clientIpAddress = ipAddress;
				logReport.append(",Info: Client ip addres is found");
			}	
			ValidatorFactory vFactory = Validation
					.buildDefaultValidatorFactory();
			Validator validator = vFactory.getValidator();
			logReport.append(",Info: Validating Request params");
			Set<ConstraintViolation<AcqD200ApiLogin>> inputErrorSet = validator
					.validate(model);
			if (inputErrorSet.size() > 0) {
				response.setStatusCode(EnumStatusConstant.InvalidCrediential
						.getId());
				Iterator<ConstraintViolation<AcqD200ApiLogin>> itr = inputErrorSet
						.iterator();
				while (itr.hasNext()) {
					ConstraintViolation<AcqD200ApiLogin> validate = (ConstraintViolation<AcqD200ApiLogin>) itr.next();
					logReport.append(",Warning: " + validate.getMessage());
					response.setStatusMessage(validate.getMessage());
					status = "FAILED";
				}
			} else {
				logReport.append(",Info: Forwarding d200 request to handler/dao");
				ServiceDto2<Object> daoResponse = loginHanler.getLoginD200(model, clientIpAddress);	
				logReport.append(",Info: response returned from dao handler/dao");
				if (daoResponse.getStatusCode() == EnumStatusConstant.OK.getId()) {
					HttpSession session = request.getSession();
					session.setAttribute("uname", model.getLoginId());
					session.setAttribute("userid", daoResponse.getStatusMessage());
					response.setStatusCode(daoResponse.getStatusCode());
					response.setStatusMessage("Authenticated");
					if(daoResponse.getBody()!=null){
						response.setBody(daoResponse.getBody());
					}else
						response.setBody(null);					
					logReport.append(",Info: Response successfully return from controller");
					status = "SUCCESSFULL";
				} else {
					response.setStatusCode(daoResponse.getStatusCode());
					response.setStatusMessage(daoResponse.getStatusMessage());
					response.setBody(null);
					logReport.append(",Warning: User not found from controller");
					status = "FAILED";
				}
			}
		} catch (Exception e) {
			response.setStatusCode(EnumStatusConstant.RollBackError.getId());
			response.setStatusMessage(EnumStatusConstant.RollBackError.getDescription());
			response.setBody(null);
			logReport.append(",Error: in user authenticate d200 version1 " + e);
			status = "FAILED";
		}/* finally {
			FileHandler fh;
			try {
				fh = new FileHandler(loginLocation + File.separator
						+ "ezlogin.log", true);
				utilLogger.addHandler(fh);
				SimpleFormatter formatter = new SimpleFormatter();
				fh.setFormatter(formatter);
				logReport.append("," + status + ";");
				logReport
						.append("\n------------------------------------------------------------------------------------------------------------------------------------");
				utilLogger.info(clientIpAddress + logReport);
				fh.close();
			}catch(SecurityException e){
				e.printStackTrace();
			}catch(IOException e){
				e.printStackTrace();
			}
		}*/
		return response;
	}
	
	@RequestMapping(value = "tpos/authenticate/version2", method = RequestMethod.POST)
	public @ResponseBody
	ControllerResponse2<Object> getLogintpos2(HttpServletRequest request,@RequestBody final AcqD200ApiLogin model) {
		System.out.println("request in authenticate tpos version1 controller");
		String status = "";
		StringBuffer logReport = new StringBuffer();
		logReport.append(",Info:Request intercepted in login tpos version1 controller");
		String clientIpAddress = "";
		ControllerResponse2<Object> response = new ControllerResponse2<Object>();
		try {
			String ipAddress = request.getHeader("X-FORWARDED-FOR");
			if (ipAddress == null) {
				ipAddress = request.getRemoteAddr();
				clientIpAddress = ipAddress;
				logReport.append(",Info: Client ip addres is found");
			}	
			ValidatorFactory vFactory = Validation
					.buildDefaultValidatorFactory();
			Validator validator = vFactory.getValidator();
			logReport.append(",Info: Validating Request params");
			Set<ConstraintViolation<AcqD200ApiLogin>> inputErrorSet = validator
					.validate(model);
			if (inputErrorSet.size() > 0) {
				response.setStatusCode(EnumStatusConstant.InvalidCrediential
						.getId());
				Iterator<ConstraintViolation<AcqD200ApiLogin>> itr = inputErrorSet
						.iterator();
				while (itr.hasNext()) {
					ConstraintViolation<AcqD200ApiLogin> validate = (ConstraintViolation<AcqD200ApiLogin>) itr
							.next();
					logReport.append(",Warning: " + validate.getMessage());
					response.setStatusMessage(validate.getMessage());
					status = "FAILED";
				}
			} else {
				logReport.append(",Info: Forwarding d200 request to handler/dao");
				ServiceDto2<Object> daoResponse = loginHanler.getLoginD200(model, clientIpAddress);	
				logReport.append(",Info: response returned from dao handler/dao");
				if (daoResponse.getStatusCode() == EnumStatusConstant.OK.getId()) {
					HttpSession session = request.getSession();
					//System.out.println("Jsession id is: "+session.getId());
					session.setAttribute("uname", model.getLoginId());
					session.setAttribute("userid", daoResponse.getStatusMessage());
					response.setStatusCode(daoResponse.getStatusCode());
					response.setStatusMessage("Authenticated");
					if(daoResponse.getBody()!=null){
						response.setBody(daoResponse.getBody());
					}else
						response.setBody(null);					
					logReport.append(",Info: Response successfully return from controller");
					status = "SUCCESSFULL";
				} else {
					response.setStatusCode(daoResponse.getStatusCode());
					response.setStatusMessage(daoResponse.getStatusMessage());
					response.setBody(null);
					logReport.append(",Warning: User not found from controller");
					status = "FAILED";
				}
			}
		} catch (Exception e) {
			response.setStatusCode(EnumStatusConstant.RollBackError.getId());
			response.setStatusMessage(EnumStatusConstant.RollBackError.getDescription());
			response.setBody(null);
			logReport.append(",Error: in user authenticate d200 version1 " + e);
			status = "FAILED";
		} /*finally {
			FileHandler fh;
			try {
				fh = new FileHandler(loginLocation + File.separator
						+ "ezlogin.log", true);
				utilLogger.addHandler(fh);
				SimpleFormatter formatter = new SimpleFormatter();
				fh.setFormatter(formatter);
				logReport.append("," + status + ";");
				logReport
						.append("\n------------------------------------------------------------------------------------------------------------------------------------");
				utilLogger.info(clientIpAddress + logReport);
				fh.close();  
			}catch(SecurityException e){
				e.printStackTrace();
			}catch(IOException e){
				e.printStackTrace();
			}
		}*/
		return response;
	}
	
	@RequestMapping(value = "authenticate/version6", method = RequestMethod.POST)
	public @ResponseBody
	ControllerResponse<Object> getLogin6(HttpServletRequest request,AcqOgsLoginModel model) {
		System.out.println("request in authenticate version5 controller");
		ControllerResponse<Object> response = new ControllerResponse<Object>();
		try {
			//String ipAddress = request.getHeader("X-FORWARDED-FOR");				
			ValidatorFactory vFactory = Validation
					.buildDefaultValidatorFactory();
			Validator validator = vFactory.getValidator();
			Set<ConstraintViolation<AcqOgsLoginModel>> inputErrorSet = validator
					.validate(model);
			if (inputErrorSet.size() > 0) {
				response.setStatus(EnumStatusConstant.InvalidCrediential
						.getId());
				Iterator<ConstraintViolation<AcqOgsLoginModel>> itr = inputErrorSet
						.iterator();
				while (itr.hasNext()) {
					ConstraintViolation<AcqOgsLoginModel> validate = (ConstraintViolation<AcqOgsLoginModel>) itr
							.next();
					response.setMessage(validate.getMessage());
				}
			} else {
				ServiceDto<Object> daoResponse = loginHanler.getLoginVersion6(model, "127.0.0.1");	
				System.out.println("response from authenticate version5 handler");
				if (daoResponse.getStatus() == EnumStatusConstant.OK.getId()) {
					HttpSession session = request.getSession();
					//System.out.println("Jsession id is: "+session.getId());
					session.setAttribute("uname", model.getUsername());
					session.setAttribute("userid", daoResponse.getMessage());
					response.setStatus(daoResponse.getStatus());
					response.setMessage("Authenticated");
					response.setResult(daoResponse.getResult());
				} else {
					response.setStatus(daoResponse.getStatus());
					response.setMessage(daoResponse.getMessage());
					response.setResult(null);
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
	@RequestMapping(value = "/ogs/authenticate/version1", method = RequestMethod.POST)
	public @ResponseBody
	ControllerResponse<Object> getLoginD200(HttpServletRequest request,OGSMainLoginModel model) {
		System.out.println("request in authenticate version5 controller");
		ControllerResponse<Object> response = new ControllerResponse<Object>();
		try {
			//String ipAddress = request.getHeader("X-FORWARDED-FOR");				
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
				//ServiceDto<Object> daoResponse = loginHanler.getLoginVersion6(model, "127.0.0.1");	
				ServiceDto<Object> daoResponse = loginHanler.getLoginD200(model, "127.0.0.1");
				
				System.out.println("response from authenticate version5 handler");
				if (daoResponse.getStatus() == EnumStatusConstant.OK.getId()) {
					HttpSession session = request.getSession();
					//System.out.println("Jsession id is: "+session.getId());
					session.setAttribute("uname", model.getUsername());
					session.setAttribute("userid", daoResponse.getMessage());
					response.setStatus(daoResponse.getStatus());
					response.setMessage("Authenticated");
					response.setResult(daoResponse.getResult());
				} else {
					response.setStatus(daoResponse.getStatus());
					response.setMessage(daoResponse.getMessage());
					response.setResult(null);
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