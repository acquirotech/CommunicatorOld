package com.acq.web.dao;

import java.util.HashMap;

import com.acq.web.controller.model.AcqApiLogin;
import com.acq.web.controller.model.AcqChangePasswordModel;
import com.acq.web.controller.model.AcqD200ApiLogin;
import com.acq.web.controller.model.AcqMobiWalletDebitModel;
import com.acq.web.controller.model.AcqOgsLoginModel;
import com.acq.web.controller.model.OGSMainLoginModel;
import com.acq.web.dto.impl.DbDto;
import com.acq.web.dto.impl.DbDto2;

public interface LoginDaoInf {

	public DbDto<Object> getTxnDetails(AcqMobiWalletDebitModel model);

	
	public DbDto<Object> getLoginD200(OGSMainLoginModel model,
			String clientIpAddress);

	public DbDto<Object> getLoginVersion6(AcqOgsLoginModel model,
			String clientIpAddress);

	DbDto2<Object> getLoginD200(AcqD200ApiLogin model, String clientIpAddress);

	

	
}
