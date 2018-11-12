package com.acq.web.handler;

import com.acq.web.controller.model.AcqRechargeMerchantBalModel;
import com.acq.web.controller.model.AcqRechargeModel;
import com.acq.web.controller.model.AcqRechargeStatusModel;
import com.acq.web.dto.impl.ServiceDto2;

public interface RechargeHandlerInf {

	public ServiceDto2<Object> getOperators(AcqRechargeMerchantBalModel model);
	public ServiceDto2<Object> doRecharge(AcqRechargeModel model);
	public ServiceDto2<Object> getMerchantBalance(AcqRechargeMerchantBalModel model);
	public ServiceDto2<Object> getRechargeStatus(AcqRechargeStatusModel model);
	public ServiceDto2<Object> getRechargeList(AcqRechargeMerchantBalModel model);
	
	
	
}
