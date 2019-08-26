package com.ai.ecp.busi.rwgw.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.ecp.base.controller.EcpBaseController;
import com.ai.ecp.cms.dubbo.dto.CmsLinkReqDTO;
import com.ai.ecp.cms.dubbo.dto.CmsLinkRespDTO;
import com.ai.ecp.cms.dubbo.interfaces.ICmsLinkRSV;
import com.ai.ecp.cms.dubbo.util.CmsConstants;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.StringUtil;

/**人卫官网-广告
 * Project Name:ecp-web-manage <br>
 * Description: <br>
 * Date:2015年8月17日下午6:54:49 <br>
 * 
 * @version
 * @since JDK 1.6
 */
@Controller
@RequestMapping(value = "/link")
public class LinkController extends EcpBaseController {

    private static String MODULE = LinkController.class.getName();
    
    @Resource(name = "cmsLinkRSV")
    private ICmsLinkRSV cmsLinkRSV;
    
    /**
     * qryLinkList:(查询广告列表). <br/>
     * TODO(这里描述这个方法适用条件 – 可选).<br/>
     * TODO(这里描述这个方法的执行流程 – 可选).<br/>
     * TODO(这里描述这个方法的使用方法 – 可选).<br/>
     * TODO(这里描述这个方法的注意事项 – 可选).<br/>
     * 
     * @param model
     * @param placeId
     * @param linkType
     * @param status
     * @return
     * @throws Exception
     * @since JDK 1.6
     */
    @RequestMapping(value = "/querylink")
    @ResponseBody
    public Map<String,Object> qryLinkList(Model model,@RequestParam(value="siteId",required=true) String siteId){
        LogUtil.info(MODULE, "==========model:" + ";");
        
        Map<String,Object> resultMap = new HashMap<String,Object>();
        List<CmsLinkRespDTO> respList = null;
        CmsLinkReqDTO reqDTO = new CmsLinkReqDTO();
       
        try{//查询状态为有效的链接。
        	if(StringUtil.isNotEmpty(siteId) && !siteId.equalsIgnoreCase("undefined"))
        	 reqDTO.setSiteId(Long.valueOf(siteId));
        	 reqDTO.setStatus(CmsConstants.ParamStatus.CMS_PARAMSTATUS_1);
             respList = cmsLinkRSV.queryCmsLinkList(reqDTO);
            // 1. 判断入参
        }catch(Exception err){
            LogUtil.error(MODULE, "查询友情链接出现异常:",err);
        }
        // 4. 返回结果
        resultMap.put("respList", respList);
        return resultMap;
    }
    
}
