package com.acq.web.dao;

import com.acq.web.controller.model.AcqOGSMainKeyExchangeModel;
import com.acq.web.controller.model.OGSMainKeyExModel;
import com.acq.web.controller.model.OGSMainLoginModel;
import com.acq.web.controller.model.OGSMainReversalTxnModel;
import com.acq.web.controller.model.OGSMainTxnCompleteModel;
import com.acq.web.controller.model.OGSMainTxnInitiateModel;
import com.acq.web.controller.model.OGSMainVoidTnxModel;
import com.acq.web.controller.model.OGSMainVoidTxnModel;
import com.acq.web.dto.impl.DbDto;

public interface OGSMainDaoInf {

	DbDto<Object> vm20OgsLogin(OGSMainLoginModel model, String clientIpAddress);

	DbDto<Object> vm20OgsKeyExchange(OGSMainKeyExModel model);

	DbDto<Object> mv20OGSTxnInitiate(OGSMainTxnInitiateModel model);

	DbDto<Object> doCashAtPosInitiate(OGSMainTxnInitiateModel model);

	DbDto<Object> ogsVM20VoidTxnList(OGSMainVoidTnxModel model);

	DbDto<Object> getTMKOgsMain(AcqOGSMainKeyExchangeModel model);

	DbDto<Object> mv20OGSMainGetTMKACK(AcqOGSMainKeyExchangeModel model);

	DbDto<Object> getDUKPTOgsMain(AcqOGSMainKeyExchangeModel model);

	DbDto<Object> mv20OGSMainGetDUKPTACK(AcqOGSMainKeyExchangeModel model);

	DbDto<Object> mv20OGSTxnComplete(OGSMainTxnCompleteModel model);

	DbDto<Object> mv20OGSRvrslTxnInit(OGSMainReversalTxnModel model);

	DbDto<Object> mv20OGSVoidTxnInit(OGSMainVoidTxnModel model);

	DbDto<Object> vm20DoPrchsWthCshBck(OGSMainTxnInitiateModel model);

	DbDto<Object> getReversal(String txnId);


}
