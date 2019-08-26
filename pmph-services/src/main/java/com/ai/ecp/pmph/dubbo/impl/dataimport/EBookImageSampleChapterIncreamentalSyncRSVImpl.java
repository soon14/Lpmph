/** 
 * File Name:ImageSampleChapterRSVImpl.java 
 * Date:2015-10-30上午11:22:24 
 * 
 */
package com.ai.ecp.pmph.dubbo.impl.dataimport;

import com.ai.ecp.goods.dubbo.impl.AbstractRSVImpl;
import com.ai.ecp.pmph.dubbo.interfaces.dataimport.IEBookImageSampleChapterIncreamentalSyncRSV;
import com.ai.ecp.pmph.service.busi.interfaces.dataimport.IImageSampleChapterSV;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.StringUtil;
import org.apache.commons.collections.MapUtils;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Project Name:ecp-services-goods-server <br>
 * Description:纸质书图片样章更新服务 <br>
 * Date:2015-10-30上午11:22:24 <br>
 * 
 * @version
 * @since JDK 1.6
 */
public class EBookImageSampleChapterIncreamentalSyncRSVImpl extends AbstractRSVImpl implements
		IEBookImageSampleChapterIncreamentalSyncRSV {

	@Resource
	private IImageSampleChapterSV imageSampleChapterSV;
	/**
	 * 电子书分类编码。
	 */
	private static final String EBOOK_CATG_CODE = "1200";
	/**
	 * 数字教材分类编码
	 */
	private static final String DIGITAL_TECH_CATG_CODE = "1201";

	/**
	 * TODO 简单描述该方法的实现功能（可选）.
	 * 
	 * @see com.ai.ecp.server.front.kettle.IReceiveData#receive(Map)
	 */
	@Override
	public void receive(Map<String, Object> data) {
		if (MapUtils.isEmpty(data)) {
			LogUtil.info(MODULE, "=============执行图片样章更新操作,接收数据集为空，不执行直接返回!");
			return;
		}
		this.executeUpdate(data,EBOOK_CATG_CODE);
	}

	/**
	 * TODO 简单描述该方法的实现功能（可选）.
	 *
	 * @see com.ai.ecp.goods.dubbo.interfaces.dataimport.IImageSampleChapterRSV#executeUpdate(Map)
	 */
	@Override
	public void executeUpdate(Map<String, Object> map, String catgCode) throws BusinessException {
		LogUtil.info(MODULE, "=============执行电子书图片更新操作........");
		imageSampleChapterSV.executeUpdate(map,catgCode,true);
	}
	
	public boolean doLogic(Object data) throws Exception {
		String fileName = String.valueOf(data);
		if(!"null".equals(fileName) && !StringUtil.isBlank(fileName)){
		   return imageSampleChapterSV.executeUpdateCheck(fileName, EBOOK_CATG_CODE);
		}else{
			return false;
		}
		
	}

}
