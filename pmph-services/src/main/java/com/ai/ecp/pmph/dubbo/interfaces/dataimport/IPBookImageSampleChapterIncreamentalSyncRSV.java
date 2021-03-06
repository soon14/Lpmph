/** 
 * File Name:IImgSampleChapterRSV.java 
 * Date:2015-10-30上午11:08:24 
 * 
 */ 
package com.ai.ecp.pmph.dubbo.interfaces.dataimport;

import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.ecp.server.front.kettle.IReceiveData;
import com.ai.ecp.server.front.kettle.IRemoteLogic;

import java.util.Map;

/**
 * Project Name:ecp-services-goods-server <br>
 * Description:纸质书图片样章增量同步。 <br>
 * Date:2015-10-30上午11:08:24  <br>
 * 
 * @version  
 * @since JDK 1.6 
 */
public interface IPBookImageSampleChapterIncreamentalSyncRSV extends IReceiveData,IRemoteLogic{
	/**
	 * 
	 * executeUpdate:更新图片样章。
	 * 
	 * @param map
	 * @param catgCode
	 * @throws BusinessException 
	 * @since JDK 1.6
	 */
	public void executeUpdate(Map<String, Object> map, String catgCode)throws BusinessException;

}

