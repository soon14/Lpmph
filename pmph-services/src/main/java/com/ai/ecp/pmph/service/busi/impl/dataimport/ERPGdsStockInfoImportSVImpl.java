package com.ai.ecp.pmph.service.busi.impl.dataimport;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.ai.ecp.goods.dao.mapper.busi.StockInfoAdaptMapper;
import com.ai.ecp.goods.dao.mapper.busi.StockInfoMapper;
import com.ai.ecp.goods.dao.model.GdsInterfaceGds;
import com.ai.ecp.goods.dao.model.StockInfo;
import com.ai.ecp.goods.dao.model.StockInfoAdapt;
import com.ai.ecp.goods.dao.model.StockInfoAdaptCriteria;
import com.ai.ecp.goods.dubbo.constants.GdsConstants;
import com.ai.ecp.goods.dubbo.constants.GdsErrorConstants;
import com.ai.ecp.goods.dubbo.dto.GdsInterfaceGdsReqDTO;
import com.ai.ecp.goods.dubbo.dto.stock.StockInfoReqDTO;
import com.ai.ecp.goods.service.busi.impl.dataimport.BaseGdsInfoImportSV;
import com.ai.ecp.goods.service.busi.interfaces.IGdsInterfaceGdsSV;
import com.ai.ecp.goods.service.busi.interfaces.IGdsStockSV;
import com.ai.ecp.pmph.dubbo.constants.PmphGdsDataImportConstants;
import com.ai.ecp.pmph.service.busi.interfaces.dataimport.IERPGdsStockInfoImportSV;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.StringUtil;

public class ERPGdsStockInfoImportSVImpl implements IERPGdsStockInfoImportSV {

    public final static String KEY_BENBANBIANHAO = "benbanbianhao";

    public final static String KEY_KUCUNCESHU = "kucunceshu";
    /**
     * 工厂库存
     */
    public final static String KEY_GONGCHANGKUCUN = "gongchangkucun";
    
    public static final String MOUDLE = ERPGdsStockInfoImportSVImpl.class.getName();

    @Resource
    IGdsStockSV gdsStockSV;

    @Resource
    IGdsInterfaceGdsSV gdsInterfaceGdsSV;
    
    @Resource
    private StockInfoAdaptMapper stockInfoAdaptMapper;
    
    @Resource
    private StockInfoMapper stockInfoMapper;

    @SuppressWarnings({ "rawtypes" })
    @Override
    public void saveGdsStockInfo(Map map) throws BusinessException {

        if (!BaseGdsInfoImportSV.isNotNull(map, KEY_BENBANBIANHAO)) {

            // 本版编号值为空可以挪到历史表
            return;
            // throw new BusinessException("未包含本版编号：" + KEY_BENBANBIANHAO);
        }
        if (!BaseGdsInfoImportSV.isNotNull(map, KEY_KUCUNCESHU)) {

            // 库存值为空可以挪到历史表
            return;
            // throw new BusinessException("未包含库存数量：" + KEY_KUCUNCESHU);
        }

        GdsInterfaceGdsReqDTO gdsInterfaceGdsReqDTO = new GdsInterfaceGdsReqDTO();
        gdsInterfaceGdsReqDTO.setOriginGdsId(BaseGdsInfoImportSV.getValue(map, KEY_BENBANBIANHAO));
        gdsInterfaceGdsReqDTO.setOrigin(PmphGdsDataImportConstants.Commons.ORIGIN_ERP);
        GdsInterfaceGds gdsInterfaceGds = gdsInterfaceGdsSV
                .queryGdsInterfaceGdsByOriginGdsId(gdsInterfaceGdsReqDTO);

        if (gdsInterfaceGds == null) {

            // 增量场景中，若找不到库存对应的商品，则不删除原表库存记录。
            // throw new BusinessException("未找到商品编码映射记录：" + BaseGdsInfoImportSV.getValue(map,
            // KEY_BENBANBIANHAO));

            // 允许跳过，由于ERP过来的是全品种商品库存，有一半以上的库存找不到对应的商品，因此此处可以不输出错误日志。
            return;
        }

        StockInfoReqDTO stockInfoReqDTO = new StockInfoReqDTO();
        stockInfoReqDTO.setShopId(PmphGdsDataImportConstants.Commons.SHOP_ID);
        stockInfoReqDTO.setTurnCount(Long.parseLong(BaseGdsInfoImportSV.getValue(map,KEY_KUCUNCESHU)));
        if(StringUtil.isNotBlank(BaseGdsInfoImportSV.getValue(map, KEY_GONGCHANGKUCUN))){
                stockInfoReqDTO.setFacStock(
                        Long.valueOf(BaseGdsInfoImportSV.getValue(map, KEY_GONGCHANGKUCUN)));
        }else{
            stockInfoReqDTO.setFacStock(-1L);
        }
        //stockInfoReqDTO.setFacStock(facStock);
        stockInfoReqDTO.setGdsId(gdsInterfaceGds.getGdsId());
        stockInfoReqDTO.setSkuId(gdsInterfaceGds.getSkuId());
        try {
            gdsStockSV.updateStockInfoByFixedVal(stockInfoReqDTO);
            
            StockInfoAdaptCriteria stockInfoAdaptCriteria = new StockInfoAdaptCriteria();
            StockInfoAdaptCriteria.Criteria criteria = stockInfoAdaptCriteria.createCriteria();
            criteria.andSkuIdEqualTo(stockInfoReqDTO.getSkuId());
            criteria.andShopIdEqualTo(stockInfoReqDTO.getShopId());
            criteria.andRepTypeEqualTo(GdsConstants.GdsStock.STOCK_REP_TYPE_PUBLIC);
            criteria.andStockTypeEqualTo(GdsConstants.GdsStock.STOCK_INFO_TYPE_PUBLIC);
            List<StockInfoAdapt> stockInfoAdapts = stockInfoAdaptMapper.selectByExample(stockInfoAdaptCriteria);
            if (stockInfoAdapts != null && stockInfoAdapts.size() > 0) {
                Long stockId = stockInfoAdapts.get(0).getStockId();
                StockInfo stockInfo = stockInfoMapper.selectByPrimaryKey(stockId);
                if(null != stockInfo){
                    map.put("$stock_id$", stockInfo.getId());
                }
            }
            
        } catch (BusinessException e) {
            String errorInfo = "gdsId="+gdsInterfaceGds.getGdsId()+"skuId="+gdsInterfaceGds.getSkuId() + "errorDetail:";
            LogUtil.error(MOUDLE,errorInfo + map.toString(),e);
            throw e;
        } catch (Exception e) {
            String errorInfo = "gdsId="+gdsInterfaceGds.getGdsId()+"skuId="+gdsInterfaceGds.getSkuId() + "errorDetail:";
            LogUtil.error(MOUDLE,errorInfo + map.toString(),e);
            throw new BusinessException(GdsErrorConstants.GdsStock.ERROR_GOODS_STOCK_235031,new String []{map.toString()});
        }

    }

}
