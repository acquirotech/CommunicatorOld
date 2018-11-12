package com.acq.web.handler;

import com.acq.web.controller.model.AcqOGSMainKeyExchangeModel;
import com.acq.web.controller.model.OGSMainKeyExModel;
import com.acq.web.controller.model.OGSMainLoginModel;
import com.acq.web.controller.model.OGSMainReversalTxnModel;
import com.acq.web.controller.model.OGSMainTxnCompleteModel;
import com.acq.web.controller.model.OGSMainTxnInitiateModel;
import com.acq.web.controller.model.OGSMainVoidTnxModel;
import com.acq.web.controller.model.OGSMainVoidTxnModel;
import com.acq.web.dto.impl.ServiceDto;

public interface OGSMainHandlerInf {

	ServiceDto<Object> mv20OGSTxnInitiate(OGSMainTxnInitiateModel model);

	ServiceDto<Object> vm20OgsKeyExchange(OGSMainKeyExModel model);

	ServiceDto<Object> vm20OgsLogin(OGSMainLoginModel model,
			String clientIpAddress);

	ServiceDto<Object> doCashAtPosInitiate(OGSMainTxnInitiateModel model);

	ServiceDto<Object> ogsVM20VoidTxnList(OGSMainVoidTnxModel model);

	ServiceDto<Object> getTMKOgsMain(AcqOGSMainKeyExchangeModel model);

	ServiceDto<Object> mv20OGSMainGetTMKACK(AcqOGSMainKeyExchangeModel model);

	ServiceDto<Object> mv20OGSMainGetDUKPTACK(AcqOGSMainKeyExchangeModel model);

	ServiceDto<Object> getDUKPTOgsMain(AcqOGSMainKeyExchangeModel model);

	ServiceDto<Object> mv20OGSTxnComplete(OGSMainTxnCompleteModel model);

	ServiceDto<Object> mv20OGSRvrslTxnInit(OGSMainReversalTxnModel model);

	ServiceDto<Object> mv20OGSVoidTxnInit(OGSMainVoidTxnModel model);

	ServiceDto<Object> vm20DoPrchsWthCshBck(OGSMainTxnInitiateModel model);

	ServiceDto<Object> getReversal(String txnId);


}
