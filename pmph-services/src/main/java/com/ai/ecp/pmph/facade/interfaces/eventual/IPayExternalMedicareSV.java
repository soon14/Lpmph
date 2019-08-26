package com.ai.ecp.pmph.facade.interfaces.eventual;

import com.ai.ecp.order.dubbo.dto.pay.PaySuccInfo;
import com.distribute.tx.common.TransactionListener;

public interface IPayExternalMedicareSV extends TransactionListener {
	
	public void deal(PaySuccInfo paySuccInfo);
	
}
