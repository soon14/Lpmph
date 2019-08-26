package com.ai.ecp.pmph.service.busi.impl;

import com.ai.ecp.goods.dao.model.GdsSkuInfo;
import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsSkuInfoReqDTO;
import com.ai.ecp.goods.service.busi.interfaces.gdsinfo.IGdsSkuInfoQuerySV;
import com.ai.ecp.pmph.dao.mapper.common.ZEResourceActivationMapper;
import com.ai.ecp.pmph.dao.model.ZEResourceActivation;
import com.ai.ecp.pmph.dao.model.ZEResourceActivationCriteria;
import com.ai.ecp.pmph.dubbo.dto.GdsPmphYsymZhekouReqDTO;
import com.ai.ecp.pmph.service.busi.interfaces.IGdsPmphYsymZhekouSV;
import com.ai.paas.utils.StringUtil;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 */
public class GdsPmphYsymZhekouSVImpl implements IGdsPmphYsymZhekouSV{

    @Resource
    private ZEResourceActivationMapper zeResourceActivationMapper;

    @Resource
    protected IGdsSkuInfoQuerySV gdsSkuInfoQuerySV;

    @Override
    public ZEResourceActivation queryZEResourceActivation(GdsPmphYsymZhekouReqDTO reqDTO) {
        ZEResourceActivationCriteria criteria=new ZEResourceActivationCriteria();
        ZEResourceActivationCriteria.Criteria c=criteria.createCriteria();

        //ISBN
        if(StringUtils.isNotBlank(reqDTO.getMemo())){
           c.andMemoEqualTo(reqDTO.getMemo());
        }
        if(StringUtils.isNotBlank(reqDTO.getProp1())){
            c.andProp1EqualTo(reqDTO.getProp1());
        }
        if(StringUtils.isNotBlank(reqDTO.getProp2())){
            c.andProp2EqualTo(reqDTO.getProp2());
        }
        if(StringUtils.isNotBlank(reqDTO.getProp3())){
            c.andProp3EqualTo(reqDTO.getProp3());
        }
        if(StringUtils.isNotBlank(reqDTO.getProp4())){
            c.andProp4EqualTo(reqDTO.getProp4());
        }
        if(StringUtils.isNotBlank(reqDTO.getAdduser())){
            c.andAdduserEqualTo(reqDTO.getAdduser());
        }
        List<ZEResourceActivation> result= zeResourceActivationMapper.selectByExample(criteria);

        if(CollectionUtils.isNotEmpty(result)){
            return result.get(0);
        }

        return null;
    }

    @Override
    public boolean ifDigitalTeachOrDigitalBook(Long skuId) {

        GdsSkuInfoReqDTO gdsSkuInfoReqDTO=new GdsSkuInfoReqDTO();
        gdsSkuInfoReqDTO.setId(skuId);
        GdsSkuInfo gdsSkuInfo = gdsSkuInfoQuerySV.queryGdsSkuInfo(gdsSkuInfoReqDTO);

        //判断是否数字教材/电子书
        if(StringUtils.contains(gdsSkuInfo.getPlatCatgs(),"<1200>")||StringUtils.contains(gdsSkuInfo.getPlatCatgs(),"<1201>")){
            return true;
        }
        return false;
    }
}
