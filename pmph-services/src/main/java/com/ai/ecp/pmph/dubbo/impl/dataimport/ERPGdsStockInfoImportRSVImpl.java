package com.ai.ecp.pmph.dubbo.impl.dataimport;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.ai.ecp.goods.dubbo.constants.GdsOption.GdsQueryOption;
import com.ai.ecp.goods.dubbo.constants.GdsOption.SkuQueryOption;
import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsInfoReqDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsInfoRespDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsSkuInfoRespDTO;
import com.ai.ecp.goods.dubbo.dto.stock.StockInfoRespDTO;
import com.ai.ecp.goods.dubbo.interfaces.IGdsInfoQueryRSV;
import com.ai.ecp.goods.service.busi.interfaces.IGdsStockSV;
import com.ai.ecp.pmph.dubbo.interfaces.dataimport.IERPGdsStockInfoImportRSV;
import com.ai.ecp.pmph.service.busi.interfaces.dataimport.IERPGdsStockInfoImportSV;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.ecp.unpf.dubbo.dto.stock.GdsStockReqDTO;
import com.ai.ecp.unpf.dubbo.dto.stock.SkuStockReqDTO;
import com.ai.ecp.unpf.dubbo.interfaces.stock.IUnpfStockRSV;
import com.ai.paas.utils.LogUtil;
import com.alibaba.dubbo.common.utils.CollectionUtils;

public class ERPGdsStockInfoImportRSVImpl implements IERPGdsStockInfoImportRSV {
    
    private static final String MODULE = ERPGdsStockInfoImportRSVImpl.class.getName();
    @Resource
    IERPGdsStockInfoImportSV gdsStockInfoImportSV;

    @Resource
    private IGdsStockSV gdsStockSV;
    
    @Resource
    private IUnpfStockRSV unpfStockRSV;
    @Resource
    private IGdsInfoQueryRSV gdsInfoQueryRSV;

    @Override
    public void receive(Map<String, Object> data) {
      
        this.saveGdsStockInfo(data);
    }


    @Override
    public void saveGdsStockInfo(Map map) throws BusinessException {
        try {
            gdsStockInfoImportSV.saveGdsStockInfo(map);
            
            if(map.containsKey("$stock_id$")){
                Long stockId = (Long) map.get("$stock_id$");
                if(null != stockId){
                    // 库存变更发送库存变更消息。
                    StockInfoRespDTO realStock = gdsStockSV.queryStockInfoById(stockId);
                    notifyStockAlteration(realStock);
                }
            }
            
            
        } catch (BusinessException e) {
            // TODO: handle exception
            throw e;
        }catch(Exception ex){
            LogUtil.error(MODULE, "库存同步遇到异常！",ex);
            throw new BusinessException("库存同步遇到异常!");
        }
    }
    
    
    /** 
     * notifyStockAlteration:(这里用一句话描述这个方法的作用). <br/> 
     * 
     * @param realStock 
     * @since JDK 1.6 
     */ 
    private void notifyStockAlteration(StockInfoRespDTO realStock) {
        // 商品数据获得
        GdsInfoReqDTO gdsInfo = new GdsInfoReqDTO();
        gdsInfo.setId(realStock.getGdsId());
        gdsInfo.setShopId(realStock.getShopId());
        GdsQueryOption[] gdsQueryOptions = new GdsQueryOption[1];
        SkuQueryOption[] skuQuerys = new SkuQueryOption[1];
        gdsQueryOptions[0] = GdsQueryOption.ALL;
        skuQuerys[0] = SkuQueryOption.ALL;
        gdsInfo.setGdsQueryOptions(gdsQueryOptions);
        gdsInfo.setSkuQuerys(skuQuerys);
        GdsInfoRespDTO gdsInfoRespDTO = gdsInfoQueryRSV.queryGdsInfoByOption(gdsInfo);
        List<SkuStockReqDTO> stockReqDTOLst = null;
        long quantity = 0;
        if (gdsInfoRespDTO==null || gdsInfoRespDTO.getId()==null) {
            LogUtil.error(MODULE, "根据产品编码gdsId="+realStock.getGdsId()+"查找不到对应产品信息!");
            throw new BusinessException("根据产品编码gdsId="+realStock.getGdsId()+"查找不到对应产品信息!");
        }else{
            if (CollectionUtils.isNotEmpty(gdsInfoRespDTO.getSkus())) {
                stockReqDTOLst = new ArrayList<>();
                for(GdsSkuInfoRespDTO sku : gdsInfoRespDTO.getSkus()){
                    quantity += sku.getRealAmount();
                    SkuStockReqDTO skuStockReqDTO = new SkuStockReqDTO();
                    skuStockReqDTO.setSkuId(sku.getId());
                    skuStockReqDTO.setQuanties(sku.getRealAmount());
                    stockReqDTOLst.add(skuStockReqDTO);
                }
                
                GdsStockReqDTO stockReq = new GdsStockReqDTO();
                stockReq.setGdsId(realStock.getGdsId());
                stockReq.setShopId(realStock.getShopId());
                stockReq.setQuanties(quantity);
                stockReq.setSkuInfos(stockReqDTOLst);
                unpfStockRSV.updateStock(stockReq);
            }
        }
    }

}
