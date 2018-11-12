package com.acq.web.dao;

import com.acq.web.controller.model.AcqRechargeMerchantBalModel;
import com.acq.web.controller.model.AcqRechargeModel;
import com.acq.web.controller.model.AcqRechargeStatusModel;
import com.acq.web.dto.impl.DbDto2;

public interface RechargeDaoInf {

	
	
	public DbDto2<Object> getOperators(AcqRechargeMerchantBalModel model);

	public DbDto2<Object> doRecharge(AcqRechargeModel model);



	public DbDto2<Object> getMerchantBalance(AcqRechargeMerchantBalModel model);

	
	public DbDto2<Object> getRechargeList(AcqRechargeMerchantBalModel model);

	public DbDto2<Object> getRechargeStatus(AcqRechargeStatusModel model);

}
