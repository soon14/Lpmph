package com.ai.ecp.pmph.facade.impl.eventual;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.ai.ecp.pmph.facade.interfaces.eventual.IEEduVNNoticeMainSV;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.StringUtil;
import com.alibaba.fastjson.JSONObject;
import com.distribute.tx.common.TransactionCallback;
import com.distribute.tx.common.TransactionContext;
import com.distribute.tx.common.TransactionStatus;
import com.distribute.tx.eventual.TransactionManager;
/**
 * 
 * Project Name:pmph-services-server <br>
 * Description: 通知人卫e教本版编号获取商品详情主事务<br>
 * Date:2017年8月25日上午9:36:56  <br>
 * 
 * @version  
 * @since JDK 1.6
 */
public class EEduVNNoticeMainSVImpl implements IEEduVNNoticeMainSV {
    
    @Resource(name="eEduVNNoticeTM")
    private TransactionManager transactionManager;
    
    private static final String MODULE = EEduVNNoticeMainSVImpl.class.getName();

    public static final String BUSINESS_TOPIC_GDS_EEDI_VN = "business-topic-gds-eedu-vn";
    
    @Override
    public void startAsyncEEduVNNoticMain(final String versionNumbers) {
      //设置分布式事务的消息内容；
        TransactionContext txMsg = new TransactionContext();
        txMsg.setContent(JSONObject.toJSONString(dealParams(versionNumbers)));
        txMsg.setName(BUSINESS_TOPIC_GDS_EEDI_VN);
        //启动异步下载资源主事务；
        transactionManager.startTransaction(txMsg, new TransactionCallback(){
            @Override
            public Object doInTransaction(TransactionStatus status) {
                LogUtil.info(MODULE,"本版编号 : "+ versionNumbers+"启动人卫e教通知任务");
                return true;
            }
        });
    }
    /**
     * 
     * dealParams:(将入参解析成键值对Map). <br/> 
     * 
     * @param versionNumbers
     * @return 
     * @since JDK 1.6
     */
    Map<String,List<Map<String,String>>> dealParams(String versionNumbers){
        Map<String,List<Map<String,String>>> result = new HashMap<String,List<Map<String,String>>>();
        List<Map<String,String>> vNMapList = new ArrayList<Map<String,String>>();
        if(null == versionNumbers){
            versionNumbers = "";
        }
        String[] vNStrs = versionNumbers.split(",");
        if(null != vNStrs && 0 < vNStrs.length){
            for(String vNStr : vNStrs){
                if(StringUtil.isNotBlank(vNStr)){
                    Map<String,String> vNMap = new HashMap<String, String>();
                    vNMap.put("versionNumber", vNStr);
                    vNMapList.add(vNMap);
                }
            }
        }
        result.put("keys", vNMapList);
        return result;
    }
}

