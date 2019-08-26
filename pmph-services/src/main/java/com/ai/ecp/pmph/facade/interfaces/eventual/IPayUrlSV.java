package com.ai.ecp.pmph.facade.interfaces.eventual;

import com.ai.ecp.order.dubbo.dto.pay.PaySuccInfo;
import com.distribute.tx.common.TransactionListener;

/**
 */
public interface IPayUrlSV extends TransactionListener {

    public void saveLog(PaySuccInfo paySuccInfo, String importType);

}
