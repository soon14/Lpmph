/**
 * 
 */
package com.ai.ecp.pmph.service.busi.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.ai.ecp.frame.vo.BaseCriteria;
import com.ai.ecp.goods.dubbo.constants.GdsOption;
import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsSkuInfoRespDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfoidx.GdsSku2PropPropIdxReqDTO;
import com.ai.ecp.goods.dubbo.interfaces.IGdsInterfaceGdsRSV;
import com.ai.ecp.goods.dubbo.interfaces.IGdsSkuInfoQueryRSV;
import com.ai.ecp.pmph.dao.mapper.busi.OrdSubTMMapper;
import com.ai.ecp.pmph.dao.model.OrdSubTM;
import com.ai.ecp.pmph.dao.model.OrdSubTMCriteria;
import com.ai.ecp.pmph.dubbo.dto.ROrdTmSubReq;
import com.ai.ecp.pmph.dubbo.dto.ROrdTmSubResp;
import com.ai.ecp.pmph.service.busi.interfaces.IOrdTmSubSV;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.ecp.server.service.impl.GeneralSQLSVImpl;
import com.ai.ecp.server.service.pagination.PaginationCallback;
import com.ai.paas.utils.StringUtil;
import com.alibaba.dubbo.common.utils.CollectionUtils;

/**
 * 
 * Project Name:ecp-services-order <br>
 * Description: <br>
 * Date:2015年8月10日下午2:44:54 <br>
 * 
 * @version
 * @since JDK 1.6
 */
@Service("ordTmSubSV")
public class OrdTmSubSVImpl extends GeneralSQLSVImpl implements IOrdTmSubSV {

    @Resource
    private OrdSubTMMapper ordSubTMMapper;
    
    @Resource
    private IGdsSkuInfoQueryRSV gdsSkuInfoQueryRSV;
    
    @Resource
    private IGdsInterfaceGdsRSV gdsInterfaceGdsRSV;
    

    private static final String MODULE = OrdTmSubSVImpl.class.getName();

    @Override
    public PageResponseDTO<ROrdTmSubResp> queryOrderTmSubByOrderId(ROrdTmSubReq rOrdTmSubReq) throws BusinessException {
        
        
        PageResponseDTO<ROrdTmSubResp> pageResponse = PageResponseDTO
                .buildByBaseInfo(rOrdTmSubReq, ROrdTmSubResp.class);
        pageResponse.setResult(new ArrayList<ROrdTmSubResp>());
        
        PageResponseDTO<ROrdTmSubResp> psdoi = queryTmOrderSub(rOrdTmSubReq);
        pageResponse.setCount(psdoi.getCount());
        pageResponse.setPageCount(psdoi.getPageCount());
        if (CollectionUtils.isEmpty(psdoi.getResult())) {
            pageResponse.setResult(null);
            return pageResponse;
        } 
        List<ROrdTmSubResp> subs = new ArrayList<ROrdTmSubResp>();
        for (ROrdTmSubResp rOrdTmSubResp : psdoi.getResult()) {
            ROrdTmSubResp sub = queryOrderByCode(rOrdTmSubResp);
            if(sub != null){
                subs.add(sub);
            }
        }
        pageResponse.getResult().addAll(subs);
        return pageResponse;
    }
    
    /**
     * 
     * queryTmOrderSub:(全表查询天猫订单明细表). <br/>
     * 
     * @param rOrdTmSubReq
     * @return 
     * @since JDK 1.6
     */
    private PageResponseDTO<ROrdTmSubResp> queryTmOrderSub(ROrdTmSubReq rOrdTmSubReq) {
        
        OrdSubTMCriteria omsic = createCriteria(rOrdTmSubReq);
        
        return super.queryByPagination(rOrdTmSubReq, omsic, true,
                new PaginationCallback<OrdSubTM, ROrdTmSubResp>() {

                    @Override
                    public List<OrdSubTM> queryDB(BaseCriteria bCriteria) {
                        return ordSubTMMapper.selectByExample((OrdSubTMCriteria) bCriteria);
                    }

                    @Override
                    public long queryTotal(BaseCriteria bCriteria) {
                        return ordSubTMMapper.countByExample((OrdSubTMCriteria) bCriteria);
                    }

                    @Override
                    public List<Comparator<OrdSubTM>> defineComparators() {
                        List<Comparator<OrdSubTM>> ls = new ArrayList<Comparator<OrdSubTM>>();
                        ls.add(new Comparator<OrdSubTM>(){

                            @Override
                            public int compare(OrdSubTM o1, OrdSubTM o2) {
                                return o2.getId().compareTo(o1.getId());
                            }
                            
                        });
                        return ls;
                    }

                    @Override
                    public ROrdTmSubResp warpReturnObject(OrdSubTM ordSubTM) {
                        ROrdTmSubResp sdoi = new ROrdTmSubResp();
                        BeanUtils.copyProperties(ordSubTM, sdoi);
                        return sdoi;
                    }
                });
    }
    
    /**
     * 
     * createCriteria:(查询条件). <br/> 
     * 
     * @param rQueryOrderRequest
     * @return 
     * @since JDK 1.6
     */
    private OrdSubTMCriteria createCriteria(ROrdTmSubReq rOrdTmSubReq){
        
        OrdSubTMCriteria omsic = new OrdSubTMCriteria(); 
        omsic.setLimitClauseCount(rOrdTmSubReq.getPageSize());
        omsic.setLimitClauseStart(rOrdTmSubReq.getStartRowIndex());
        omsic.setOrderByClause("id desc");
        OrdSubTMCriteria.Criteria ca = omsic.createCriteria();
        if(rOrdTmSubReq.getOrderId() != null){
            ca.andOrderIdEqualTo(rOrdTmSubReq.getOrderId());
        } 
        return omsic;
    } 
    
    private ROrdTmSubResp queryOrderByCode(ROrdTmSubResp rOrdTmSubResp) {
        String externalSysCode = rOrdTmSubResp.getExternalSysCode();
        if(StringUtil.isNotBlank(externalSysCode)){
//            externalSysCode = rOrdTmSubResp.getExternalSysCode().substring(1,6);
            externalSysCode = rOrdTmSubResp.getExternalSysCode().substring(1,6);
            GdsSkuInfoRespDTO dto = queryOrderByExternalSysCode(externalSysCode);
            if(dto != null ){
                rOrdTmSubResp.setExternalSysCode(externalSysCode);
                rOrdTmSubResp.setMainCatgs(dto.getMainCatgName());
                rOrdTmSubResp.setGuidePrice(dto.getGuidePrice());
            }else{
                rOrdTmSubResp.setExternalSysCode(externalSysCode);
                rOrdTmSubResp.setMainCatgs("未匹配上");
                rOrdTmSubResp.setGuidePrice(null);
            }
//            if (StringUtil.isNotBlank(dto.getMainCatgs()) && StringUtil.isNotBlank(dto.getGuidePrice()+"")) {
//                rOrdTmSubResp.setExternalSysCode(externalSysCode);
//                rOrdTmSubResp.setMainCatgs(dto.getMainCatgs());
//                rOrdTmSubResp.setGuidePrice(dto.getGuidePrice());
//            }else{
//                rOrdTmSubResp.setExternalSysCode("未匹配上");
//                rOrdTmSubResp.setMainCatgs("未匹配上");
//                rOrdTmSubResp.setGuidePrice(null);
//            }
        }
        return rOrdTmSubResp;
    }
    
    /**
     * 
     * queryOrderByExternalSysCode:(取商品域 主分类名称、商品指导价). <br/> 
     * 
     * @param externalSysCode
     * @return 
     * @since JDK 1.6
     */
    private GdsSkuInfoRespDTO queryOrderByExternalSysCode(String externalSysCode) {
//        GdsSkuInfoRespDTO gdsSkuInfoRespDTO = new GdsSkuInfoRespDTO();
//        GdsInterfaceGdsReqDTO gdsInterfaceGdsReqDTO = new GdsInterfaceGdsReqDTO();
//        gdsInterfaceGdsReqDTO.setOriginGdsId(externalSysCode);
//        gdsInterfaceGdsReqDTO.setOrigin(PmphGdsDataImportConstants.Commons.ORIGIN_ERP);
//        GdsInterfaceGdsRespDTO gdsInterfaceGdsRespDTO = gdsInterfaceGdsRSV.queryGdsInterfaceGdsByOriginGdsId(gdsInterfaceGdsReqDTO);
//        if (StringUtil.isNotEmpty(gdsInterfaceGdsRespDTO)) {
//            if (StringUtil.isNotEmpty(gdsInterfaceGdsRespDTO)) {
//                GdsSkuInfoReqDTO gdsSkuInfoReqDTO = new GdsSkuInfoReqDTO();
//                gdsSkuInfoReqDTO.setId(gdsInterfaceGdsRespDTO.getSkuId());
//                gdsSkuInfoReqDTO.setGdsId(gdsInterfaceGdsRespDTO.getGdsId());
//                gdsSkuInfoReqDTO.setShopId(gdsInterfaceGdsRespDTO.getShopId());
//                gdsSkuInfoReqDTO.setSkuQuery(new SkuQueryOption[]{SkuQueryOption.BASIC,SkuQueryOption.PRICE});
//                gdsSkuInfoRespDTO = gdsSkuInfoQueryRSV.querySkuInfoByOptions(gdsSkuInfoReqDTO);
//            }
//        }
        GdsSkuInfoRespDTO gdsSkuInfoRespDTO = null;
        GdsSku2PropPropIdxReqDTO gdsSku2PropPropIdxReqDTO=new GdsSku2PropPropIdxReqDTO();
        gdsSku2PropPropIdxReqDTO.setPropId(1004l);
        gdsSku2PropPropIdxReqDTO.setPropValue(externalSysCode);
        gdsSku2PropPropIdxReqDTO.setOptions(new GdsOption.SkuQueryOption[] {
            GdsOption.SkuQueryOption.BASIC });
        gdsSku2PropPropIdxReqDTO.setPageSize(100);
        gdsSku2PropPropIdxReqDTO.setPageNo(1);
        PageResponseDTO<GdsSkuInfoRespDTO> gdsSkuInfo=gdsSkuInfoQueryRSV.queryGdsSkuInfoPaging(gdsSku2PropPropIdxReqDTO);
        if(CollectionUtils.isNotEmpty(gdsSkuInfo.getResult())){
            gdsSkuInfoRespDTO = gdsSkuInfo.getResult().get(0);
        }
        return gdsSkuInfoRespDTO;
    }
}
