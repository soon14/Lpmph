package com.ai.ecp.pmph.dubbo.impl.dataimport;

import java.util.Map;

import javax.annotation.Resource;

import com.ai.ecp.general.prom.interfaces.IPromMsg2SolrRSV;
import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsInfoRespDTO;
import com.ai.ecp.goods.dubbo.dto.gdslog.GdsLogReqDTO;
import com.ai.ecp.goods.dubbo.impl.AbstractRSVImpl;
import com.ai.ecp.goods.service.busi.interfaces.gdslog.IGdsLogSV;
import com.ai.ecp.goods.service.thread.GdsInfoMessageDTO;
import com.ai.ecp.goods.service.thread.GdsInfoMessageTask;
import com.ai.ecp.goods.service.thread.ThreadPoolExecutorUtil;
import com.ai.ecp.pmph.dubbo.interfaces.dataimport.IERPGdsInfoImportRSV;
import com.ai.ecp.pmph.service.busi.interfaces.dataimport.IERPGdsInfoImportSV;
import com.ai.paas.utils.DateUtil;
import com.ai.paas.utils.LogUtil;

/**
 * Project Name:ecp-services-goods <br>
 * Description: ERP图书信息导入<br>
 * Date:2015年10月24日上午10:32:55  <br>
 * 
 * @version  
 * @since JDK 1.6
 */
public class ERPGdsInfoImportRSVImpl extends AbstractRSVImpl implements IERPGdsInfoImportRSV {
    
    @Resource
    private IERPGdsInfoImportSV erpGdsInfoImportSV;
    
    @Resource
    private IPromMsg2SolrRSV promMsg2SolrRSV;

    @Resource
    private IGdsLogSV gdsLogSV;

    @Override
    public void receive(Map<String, Object> map) {

        //商品操作日志
        GdsLogReqDTO gdsLogReqDTO=new GdsLogReqDTO();
        gdsLogReqDTO.setStartTime(DateUtil.getSysDate());

        GdsInfoRespDTO gdsInfo=this.erpGdsInfoImportSV.saveGdsInfo(map);
        
        //移除缓存
        if(gdsInfo!=null){
            /*GdsCacheUtil.delGdsInfoCache(gdsInfo.getId());
            GdsCacheUtil.delGdsPicCache(gdsInfo.getId());
            if(CollectionUtils.isNotEmpty(gdsInfo.getSkuIds())){
                for (Long skuId : gdsInfo.getSkuIds()) {
                    GdsCacheUtil.delSkuInfoCache(skuId);
                    GdsCacheUtil.delSkuPicCache(skuId);
                    GdsUtils.sendPromMsg(gdsInfo.getShopId(), gdsInfo.getId(), skuId, gdsInfo.getGdsStatus(), promMsg2SolrRSV);
                }
            }

            GdsUtils.sendGdsIndexMsg(gdsInfo.getGdsStatus(), "T_GDS_INFO", MODULE, gdsInfo.getId(), gdsInfo.getCatlogId());*/
            
            //商品操作日志保存
            gdsLogReqDTO.setEndTime(DateUtil.getSysDate());
            gdsLogReqDTO.setModuleName(IGdsLogSV.GDS_MODEL);
            gdsLogReqDTO.setOperType(gdsInfo.getOperType());
            gdsLogReqDTO.setOperResult((short) 1);
            gdsLogReqDTO.setOperParam("{\"id\":"+gdsInfo.getSkuIds().get(0)+",\"shopId\":"+gdsInfo.getShopId()+"}");
            try {
                this.gdsLogSV.addGdsLog(gdsLogReqDTO);
            } catch (Exception e) {
                LogUtil.error(MODULE, "商品数据接口导入商品后保存日志失败!",e);
            }
            
            GdsInfoMessageDTO messageDTO = new GdsInfoMessageDTO();
            messageDTO.setGdsId(gdsInfo.getId());
            messageDTO.setShopId(gdsInfo.getShopId());
            messageDTO.setCatlogId(gdsInfo.getCatlogId());
            messageDTO.setSkuIds(gdsInfo.getSkuIds());
            messageDTO.setSendPromMsg(true);
            messageDTO.setGdsStatus(gdsInfo.getGdsStatus());
            ThreadPoolExecutorUtil.commitTask(new GdsInfoMessageTask(messageDTO));

        }
    }

}

