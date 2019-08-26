/** 
 * File Name:IAipAbstractRequest.java 
 * Date:2015-10-28下午5:22:52 
 * 
 */ 
package com.ai.ecp.pmph.aip.dubbo.dto;

import com.ai.ecp.server.front.dto.BaseInfo;
import com.alibaba.fastjson.annotation.JSONType;

/**
 * Project Name:ecp-services-aip-server <br>
 * Description: Aip抽象请求。<br>
 * Date:2015-10-28下午5:22:52  <br>
 * 
 * @version  
 * @since JDK 1.6 
 */
@JSONType(ignores={"pageNo","pageSize","appName","threadId","locale","staff","currentSiteId","endRowIndex","startRowIndex"})
public class AipAbstractRequest extends BaseInfo{

	/** 
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么). 
	 * @since JDK 1.6 
	 */ 
	private static final long serialVersionUID = -7005885221321742637L;

}

