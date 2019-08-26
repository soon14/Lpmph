package com.ai.ecp.pmph.service.busi.impl;

import com.ai.ecp.goods.dubbo.constants.GdsConstants;
import com.ai.ecp.goods.dubbo.constants.GdsErrorConstants;
import com.ai.ecp.goods.dubbo.dto.GdsInfoShipmentReqDTO;
import com.ai.ecp.goods.dubbo.dto.GdsShipmentCalInfoReqDTO;
import com.ai.ecp.goods.service.busi.impl.GdsShipTempSVImpl;
import com.ai.ecp.prom.dubbo.dto.PromPostDTO;
import com.ai.ecp.prom.dubbo.dto.PromPostRespDTO;
import com.ai.ecp.prom.dubbo.dto.PromShipDTO;
import com.ai.ecp.prom.dubbo.dto.PromShipRespDTO;
import com.ai.ecp.prom.dubbo.interfaces.IPromShipRSV;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.ecp.server.front.util.SysCfgUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 */
public class GdsShipTempPmphSVImpl extends GdsShipTempSVImpl {

    @Resource
    private IPromShipRSV promShipRSV;

    @Override
    public Long calcShipExpense(GdsShipmentCalInfoReqDTO calInfoReqDTO) throws BusinessException {
        Long price = 0L;
        List<GdsInfoShipmentReqDTO> gdsInfoShipmentReqDTOs = calInfoReqDTO.getGdsInfos();
        if (gdsInfoShipmentReqDTOs == null || gdsInfoShipmentReqDTOs.size() <= 0) {

            throw new BusinessException(GdsErrorConstants.Commons.ERROR_GOODS_200100,
                    new String[] { "gdsInfos" });

        }
        // 判断是否金额达到促销免邮，如果达到，直接返回0；
        PromShipDTO promShipDTO = new PromShipDTO();
        promShipDTO.setShopId(calInfoReqDTO.getShopId());
        promShipDTO.setMoney(calInfoReqDTO.getAmount());
        promShipDTO.setCountryCode(calInfoReqDTO.getCountryCode());
        promShipDTO.setCityCode(calInfoReqDTO.getCityCode());
        promShipDTO.setProvinceCode(calInfoReqDTO.getProvinceCode());
        promShipDTO.setCurrentSiteId(calInfoReqDTO.getCurrentSiteId());
        PromShipRespDTO promShipRespDTO = promShipRSV.qryPromShip(promShipDTO);
        if(promShipRespDTO.getMsgCode().equals("1")){

            return 0L;
        }

        //促销配置免邮（订单级）
        PromPostDTO promPostDTO=new PromPostDTO();
        //订单级和单品明细级促销id都需要传
        promPostDTO.setPromIds(calInfoReqDTO.getPromIds());
        PromPostRespDTO promPostRespDTO=promShipRSV.checkIfFreePost(promPostDTO);
        if(StringUtils.equals(promPostRespDTO.getIfFreePost(), GdsConstants.Commons.STATUS_VALID)){

            //人卫需求：订单免邮。只有有一个符合免邮，则全部免邮。
            return 0L;
        }

        // 过滤虚拟商品
        this.delGdsInfosByTypeId(gdsInfoShipmentReqDTOs);
        // 过滤免邮商品
        this.delGdsInfosFree(gdsInfoShipmentReqDTOs);
        // 按规则1计算运费 ----商品-》分类—》店铺运费模板
        if (SysCfgUtil.checkSysCfgValue("GDS_SHIP_TEMPLATE_RULE",
                GdsConstants.GdsShiptemp.GDS_SHIPMENT_RULE_01)) {

            // 统计所有商品数量，以商品聚合数目;
            List<GdsInfoShipmentReqDTO> gdsInfoShipByGds = this
                    .delGdsShipInfoGroupByGdsId(gdsInfoShipmentReqDTOs);
            // 根据商品过滤

            this.delGdsInfoByFreeShipGds(calInfoReqDTO, gdsInfoShipmentReqDTOs, gdsInfoShipByGds);
            // 统计所有分类数量，以分类聚合数目；
            List<GdsInfoShipmentReqDTO> gdsInfoShipByCatgCode = this
                    .delGdsShipInfoGroupByCatgCode(gdsInfoShipmentReqDTOs);
            // 根据分类过滤
            this.delGdsInfoByFreeShipCatg(calInfoReqDTO, gdsInfoShipmentReqDTOs,
                    gdsInfoShipByCatgCode);
            // 根据店铺过滤
            this.delGdsInfoByFreeShipShop(calInfoReqDTO, gdsInfoShipmentReqDTOs);
            // **************过滤后，计算运费******************//
            // 统计所有商品数量，以商品聚合数目;
            List<GdsInfoShipmentReqDTO> gdsInfoShipByGdsForDeal = this
                    .delGdsShipInfoGroupByGdsId(gdsInfoShipmentReqDTOs);

            // 通过商品模板计算运费
            price = price
                    + this.dealGdsInfoByFreeShipGds(calInfoReqDTO, gdsInfoShipmentReqDTOs,
                    gdsInfoShipByGdsForDeal);
            // 统计所有分类数量，以分类聚合数目；
            List<GdsInfoShipmentReqDTO> gdsInfoShipByCatgCodeForDeal = this
                    .delGdsShipInfoGroupByCatgCode(gdsInfoShipmentReqDTOs);
            // 通过分类模板计算运费
            price = price
                    + this.dealGdsInfoByFreeShipCatg(calInfoReqDTO, gdsInfoShipmentReqDTOs,
                    gdsInfoShipByCatgCodeForDeal);
            // 通过店铺模板计算运费
            price = price + this.dealGdsInfoByFreeShipShop(calInfoReqDTO, gdsInfoShipmentReqDTOs);

            // 按规则1计算运费 ---- 店铺运费模板-》分类-》商品
        } else if (SysCfgUtil.checkSysCfgValue("GDS_SHIP_TEMPLATE_RULE",
                GdsConstants.GdsShiptemp.GDS_SHIPMENT_RULE_02)) {
            // 根据店铺过滤
            this.delGdsInfoByFreeShipShop(calInfoReqDTO, gdsInfoShipmentReqDTOs);

            // 统计所有分类数量，以分类聚合数目；
            List<GdsInfoShipmentReqDTO> gdsInfoShipByCatgCode = this
                    .delGdsShipInfoGroupByCatgCode(gdsInfoShipmentReqDTOs);
            // 根据分类过滤
            this.delGdsInfoByFreeShipCatg(calInfoReqDTO, gdsInfoShipmentReqDTOs,
                    gdsInfoShipByCatgCode);

            // 统计所有商品数量，以商品聚合数目;
            List<GdsInfoShipmentReqDTO> gdsInfoShipByGds = this
                    .delGdsShipInfoGroupByGdsId(gdsInfoShipmentReqDTOs);
            // 根据商品过滤

            this.delGdsInfoByFreeShipGds(calInfoReqDTO, gdsInfoShipmentReqDTOs, gdsInfoShipByGds);
            // 通过店铺模板计算运费
            price = price + this.dealGdsInfoByFreeShipShop(calInfoReqDTO, gdsInfoShipmentReqDTOs);

        }
        if (CollectionUtils.isNotEmpty(gdsInfoShipmentReqDTOs)) {
            throw new BusinessException(GdsErrorConstants.GdsShipMent.ERROR_GOODS_SHIP_MENT_240003);

        }
        return price;
    }

}
