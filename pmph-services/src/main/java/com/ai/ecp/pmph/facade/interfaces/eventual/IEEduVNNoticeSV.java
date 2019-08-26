package com.ai.ecp.pmph.facade.interfaces.eventual;

import com.ai.ecp.pmph.aip.dubbo.dto.AipEEduVNNoticeRequest;
import com.distribute.tx.common.TransactionListener;

public interface IEEduVNNoticeSV extends TransactionListener {

    public void deal(AipEEduVNNoticeRequest request);
}

