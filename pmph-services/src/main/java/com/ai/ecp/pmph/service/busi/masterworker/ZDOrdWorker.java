package com.ai.ecp.pmph.service.busi.masterworker;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.ai.ecp.frame.context.EcpFrameContextHolder;
import com.ai.ecp.order.dubbo.dto.RFileImportRequest;
import com.ai.ecp.order.dubbo.dto.RPreOrdMainsResponse;
import com.ai.ecp.pmph.service.busi.interfaces.IOrdImportSV;
import com.ai.ecp.server.front.exception.BusinessException;

public class ZDOrdWorker extends TaskWorker<RPreOrdMainsResponse> {
	
	@Override
	public RPreOrdMainsResponse handle(RFileImportRequest info, List<Object> row) throws BusinessException{
		IOrdImportSV ordImportSV = EcpFrameContextHolder.getBean(
                "ordImportSV",
                IOrdImportSV.class);
		return ordImportSV.saveOneOrdMainZD(info, row);
	}

	@Override
	public String generateResultMapID(AtomicInteger countTool) {
		return new StringBuffer().append(countTool.incrementAndGet()).toString();
	}

}
