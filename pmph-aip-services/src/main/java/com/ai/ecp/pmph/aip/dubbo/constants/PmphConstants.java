/** 
 * File Name:PmphConstants.java
 * Date:2015-10-28下午5:12:31 
 * 
 */ 
package com.ai.ecp.pmph.aip.dubbo.constants;

/**
 * Project Name:ecp-services-aip-server <br>
 * Description: Aip能力平台常量类。<br>
 * Date:2015-10-28下午5:12:31  <br>
 * 
 * @version  
 * @since JDK 1.6 
 */
public class PmphConstants {
	/**
	 * 
	 * Project Name:ecp-services-aip-server <br>
	 * Description: 通用常量。<br>
	 * Date:2015-10-29下午3:18:31  <br>
	 * 
	 * @version PmphConstants
	 * @since JDK 1.6
	 */
	public static final class Commons{
		
	}
	/**
	 * 
	 * Project Name:ecp-services-aip-server <br>
	 * Description:远程服务地址。 <br>
	 * Date:2015-10-29下午3:18:40  <br>
	 * 
	 * @version PmphConstants
	 * @since JDK 1.6
	 */
	public static final class RemoteURL{
		/**
		 * 泽元远程授权URL地址。
		 */
		public static final String ZY_REMOTE_AUTH_URL = "";
		/**
		 * 雷鸣网络增值服务URL地址。
		 */
		public static final String LM_REMOTE_NET_VALUE_ADDED_URL = "http://192.168.42.92:8060/api/pmph/book!bookResource.do";
		
	}
	/**
	 * 
	 * Project Name:pmph-aip-services-server <br>
	 * Description: 远程服务地址在系统表的编码<br>
	 * Date:2017年8月24日下午2:15:45  <br>
	 * 
	 * @version PmphConstants 
	 * @since JDK 1.6
	 */
    public static final class RemoteURLSysCfgCode{
        /**
         * 人卫e教本版编号通知远程URL地址。
         */
        public static final String EEDU_REMOTE_VN_NOTICE_URL = "GDS_EEDU_NOTICE_VN_URL";
    }
	/**
	 * 
	 * Project Name:pmph-aip-services-server <br>
	 * Description: 人卫e教 通知类型<br>
	 * Date:2017年8月23日下午6:06:02  <br>
	 * 
	 * @version PmphConstants 
	 * @since JDK 1.6
	 */
	public static final class EEduNoteiceType{
	    /**
	     * 修改
	     */
	    public static final String UPDATE = "0";
	}
	/**
	 * 
	 * Project Name:pmph-aip-services-server <br>
	 * Description: 人卫e教 请求模块<br>
	 * Date:2017年8月24日上午9:22:23  <br>
	 * 
	 * @version PmphConstants 
	 * @since JDK 1.6
	 */
	public static final class EEduActType{
        public static final String SHOP = "shop";
    }
	
	/**
	 * 
	 * Project Name:ecp-services-aip-server <br>
	 * Description:异常码常量 <br>
	 * Date:2015-10-29下午3:35:36  <br>
	 * 
	 * @version PmphConstants
	 * @since JDK 1.6
	 */
	public static final class ErrorCode{
		/**
		 * aip.A20001 = 必传参数{0}缺失！
		 */
		public static final String AIP_A20001 = "aip.A20001";
		/**
		 * aip.A20002 = {0}方法执行异常!
		 */
		public static final String AIP_A20002 = "aip.A20002";
		/**
		 * zyauth.B20020 = 授权申请异常!
		 */
		public static final String ZYAUTH_B20020 = "zyauth.B20020";
		/**
		 * lmrec.b20021 = 网络增值信息获取异常!
		 */
		public static final String LMREC_B20021 = "lmrec.B20021";
		/**
		 * externalauth.c30300 = 外部系统授权申请异常!
		 */
		public static final String EXTERNALAUTH_C30300 = "externalauth.C30300";
	}
	


}

