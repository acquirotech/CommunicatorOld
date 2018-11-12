package com.acq.web.handler;

import java.util.HashMap;

import com.acq.web.controller.model.AcqApiLogin;
import com.acq.web.controller.model.AcqChangePasswordModel;
import com.acq.web.controller.model.AcqD200ApiLogin;
import com.acq.web.controller.model.AcqOgsLoginModel;
import com.acq.web.controller.model.OGSMainLoginModel;
import com.acq.web.dto.impl.ServiceDto;
import com.acq.web.dto.impl.ServiceDto2;

public interface LoginHandlerInf {

	
	
	public ServiceDto2<Object> getLoginD200(AcqD200ApiLogin model,
			String clientIpAddress);

	public ServiceDto<Object> getLoginVersion6(AcqOgsLoginModel model,
			String string);

	public ServiceDto<Object> getLoginD200(OGSMainLoginModel model,
			String clientIpAddress);

}
