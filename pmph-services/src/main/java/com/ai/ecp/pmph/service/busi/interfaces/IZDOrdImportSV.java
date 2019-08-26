package com.ai.ecp.pmph.service.busi.interfaces;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.ai.ecp.order.dubbo.dto.RFileImportRequest;
import com.ai.ecp.order.dubbo.dto.RPreOrdMainsResponse;
import com.ai.ecp.server.front.exception.BusinessException;

public interface IZDOrdImportSV {
	
	/**
	  * 
	  * 导入征订单. <br/> 
	  * 入参：
	  * @param req
	  * @return
	  * @throws BusinessException 
	  * @since JDK 1.7
	  */
	public ConcurrentHashMap<String, RPreOrdMainsResponse> importZDPreOrdData(RFileImportRequest info, List<List<Object>> group, AtomicInteger countTool, ConcurrentHashMap<String, RPreOrdMainsResponse> resultMap);
}
