package com.ai.ecp.util;

import java.io.IOException;

import com.ai.ecp.server.front.dto.BaseSysCfgRespDTO;
import com.ai.ecp.server.front.util.SysCfgUtil;
import com.ai.paas.utils.StringUtil;
import com.ailk.butterfly.core.config.Application;

/**
 * 
 * Project Name:ecp-web-im <br>
 * Description: <br>
 * Date:2016年8月8日上午11:32:21 <br>
 * 
 * @version
 * @since JDK 1.7
 */
public class BizUtil {
	
	/**
	 * 提问类型 0 ：综合
	 */
	public static final String issueType_0 = "0"; 
	
	/**
	 * 提问类型 1：订单
	 */
	public static final String issueType_1 = "1"; 
	
	/**
	 * 提问类型 2：商品
	 */
	public static final String issueType_2 = "2"; 
	
	/**
	 * 状态0：失效
	 */
	public static final String status_0 ="0";
	
	/**
	 * 状态1：生效
	 */
	public static final String status_1 ="1"; 
	
	/**
	 * 消息状态：发起
	 */
	public static final String msg_status_10 = "10";
	
	/**
	 * 消息状态：到达
	 */
	public static final String msg_status_20 = "20";
	
	/**
	 * 消息类型:信息
	 */
    public static final String messageType_msg = "msg"; 
	/**
	 * 
	 * getBOSH:(获得BOSH service地址). <br/>
	 * 
	 * @return
	 * @throws Exception
	 * @since JDK 1.7
	 */
	public static String getBOSH() throws Exception {
		BaseSysCfgRespDTO baseSysCfgRespDTO = SysCfgUtil.fetchSysCfg("IM_CLIENT_HTTP_URL");
		if(StringUtil.isNotBlank(baseSysCfgRespDTO.getParaValue())){
			return baseSysCfgRespDTO.getParaValue();
		}
		return "";
	}

	
	/**
	 * 
	 * getBOSH:(获得openfire service地址). <br/>
	 * 
	 * @return
	 * @throws Exception
	 * @since JDK 1.7
	 */
	public static String getServer() throws Exception {
		BaseSysCfgRespDTO baseSysCfgRespDTO = SysCfgUtil.fetchSysCfg("IM_OPENFIRE_URL");
		if(StringUtil.isNotBlank(baseSysCfgRespDTO.getParaValue())){
			return baseSysCfgRespDTO.getParaValue();
		}
		return "";
	}
	
	
	/**
	 * 解码
	 * @param str
	 * @return
	 */
	public static byte[] Base64Decode(String str) {
		byte[] bt = null;
		try {
			sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
			bt = decoder.decodeBuffer(str);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return bt;
	}
	
	/**
	 * 获取imserver
	 * @return
	 * @throws Exception
	 */
	public static String getOfServer() throws Exception{
		BaseSysCfgRespDTO baseSysCfgRespDTO = SysCfgUtil.fetchSysCfg("IM_DOMAIN");
		if(StringUtil.isNotBlank(baseSysCfgRespDTO.getParaValue())){
			return baseSysCfgRespDTO.getParaValue();
		}
		return "";
	}
}
