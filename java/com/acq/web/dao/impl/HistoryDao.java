package com.acq.web.dao.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.acq.EnumStatusConstant;
import com.acq.users.entity.AcqTransactionEntity;
import com.acq.web.controller.model.AcqTransactionHistryModel;
import com.acq.web.dao.AcqDao;
import com.acq.web.dao.HistoryDaoInf;
import com.acq.web.dto.impl.DbDto;

	@Repository
	public class HistoryDao extends AcqDao implements HistoryDaoInf {

		
		final static Logger logger = Logger.getLogger(HistoryDao.class);

		//@Autowired
		//MessageSource resource;

		@Override
		public DbDto<HashMap<String, HashMap<String, String>>> transactionHistry(
				AcqTransactionHistryModel model) {
			DbDto<HashMap<String, HashMap<String, String>>> response = new DbDto<HashMap<String, HashMap<String, String>>>();
			Session session = null;
			try {
				session = getNewSession();
				HashMap<String, HashMap<String, String>> responseMap = new HashMap<String, HashMap<String, String>>();
				List<AcqTransactionEntity> lstTxn = (List<AcqTransactionEntity>) session
						.createCriteria(AcqTransactionEntity.class)
						.addOrder(Order.desc("id"))
						.setMaxResults(Integer.parseInt(model.getTxnCount()))
						.add(Restrictions.eq("userId",
								Long.valueOf(model.getSessionId()))).list();
				Iterator<AcqTransactionEntity> lstTxnItr = lstTxn.iterator();
				System.out.println("lstTxnlstTxn"+lstTxn);
				HashMap<String, String> txnMap = null;
				if (lstTxn.isEmpty()) {
					response.setStatus(EnumStatusConstant.NotFound.getId());
					response.setMessage(EnumStatusConstant.NotFound
							.getDescription());
					response.setResult(null);
					logger.info("Successfully selected records in history service");
				} else {
					while (lstTxnItr.hasNext()) {
						AcqTransactionEntity sngleTxn = (AcqTransactionEntity) lstTxnItr
								.next();
						txnMap = new HashMap<String, String>();
						txnMap.put("amount", sngleTxn.getTotalAmount());
						txnMap.put("cardPanNo", sngleTxn.getCardPanNo());
						txnMap.put("dateTime", sngleTxn.getDateTime());
						txnMap.put("cardType", sngleTxn.getCardType());
						txnMap.put("txStatus", sngleTxn.getTxStatus());
						responseMap.put("" + sngleTxn.getId(), txnMap);
					}
					response.setStatus(EnumStatusConstant.OK.getId());
					response.setMessage(EnumStatusConstant.OK.getDescription());
					response.setResult(responseMap);
					logger.info("Successfully selected records in history service");
				}
			} catch (Exception e) {
				response.setStatus(EnumStatusConstant.RollBackError.getId());
				response.setMessage(EnumStatusConstant.RollBackError
						.getDescription());
				response.setResult(null);
				logger.info("Error to select history in dao");
			}finally{
				session.close();			
			}
				return response;
		}

	
	}
