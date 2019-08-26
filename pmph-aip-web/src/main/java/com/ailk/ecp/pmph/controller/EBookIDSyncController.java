package com.ailk.ecp.pmph.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.ecp.pmph.dubbo.dto.EBookIDSyncReqDTO;
import com.ai.ecp.pmph.dubbo.interfaces.IEBookIDSyncRSV;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.ObjectCopyUtil;
import com.ailk.aip.security.DefaultServiceCheckChain;
import com.ailk.butterfly.core.annotation.Security;
import com.ailk.butterfly.core.exception.BusinessException;
import com.ailk.butterfly.core.web.BaseController;
import com.ailk.ecp.pmph.util.RespUtil;
import com.ailk.ecp.pmph.vo.EBookIDSyncVO;

/**
 * Project Name:ecp-aip-web <br>
 * Description: <br>
 * Date:2015年11月2日上午10:40:46  <br>
 * 
 * @version  
 * @since JDK 1.6
 */
@Controller
public class EBookIDSyncController extends BaseController{
    
    private static final String MODULE = EBookIDSyncController.class.getName();
    
    @Resource
    private IEBookIDSyncRSV eBookIDSyncRSV;
    
    @RequestMapping(value="/rest" ,params="method=com.ai.ecp.goods.EBookIDSync")
    @Security(mustLogin=true,authorCheckType=DefaultServiceCheckChain.class)
    @ResponseBody
    public Map<String,Object> ebookIDSync(EBookIDSyncVO eBookIDSyncVO) throws BusinessException{
       LogUtil.info(MODULE, "=================执行ebook_id同步;eBookIDSyncVO:"+ToStringBuilder.reflectionToString(eBookIDSyncVO));
       Map<String, Object> result = new HashMap<String, Object>();
       try{
           EBookIDSyncReqDTO reqDTO = new EBookIDSyncReqDTO();
           ObjectCopyUtil.copyObjValue(eBookIDSyncVO, reqDTO, null, false);
           eBookIDSyncRSV.executeEBookIDSync(reqDTO);
           result.put(RespUtil.CODE, 1);
           result.put(RespUtil.MSG, "ISBN号为"+eBookIDSyncVO.getIsbn()+"纸质书同步ebook_id成功!");
       }catch(Exception e){
           result.put(RespUtil.CODE, 2);
           if(e instanceof BusinessException){
               result.put(RespUtil.MSG, "ISBN号为"+eBookIDSyncVO.getIsbn()+"纸质书同步ebook_id失败!原因:"+((BusinessException)e).getErrorMsg());
           }else{
               result.put(RespUtil.MSG, "ISBN号为"+eBookIDSyncVO.getIsbn()+"纸质书同步ebook_id失败!原因:"+e.getMessage());
           }
       }
       return RespUtil.renderRootResp(result);       
    }
    
   

}

