package com.ai.ecp.busi.rwzw.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.ecp.base.controller.EcpBaseController;
import com.ai.ecp.busi.rwgw.vo.ComponentReqVO;
import com.ai.ecp.cms.dubbo.dto.CmsAdvertiseReqDTO;
import com.ai.ecp.cms.dubbo.dto.CmsAdvertiseRespDTO;
import com.ai.ecp.cms.dubbo.dto.CmsSiteInfoRespDTO;
import com.ai.ecp.cms.dubbo.dto.CmsSiteRespDTO;
import com.ai.ecp.cms.dubbo.interfaces.ICmsAdvertiseRSV;
import com.ai.ecp.cms.dubbo.util.CmsCacheUtil;
import com.ai.ecp.cms.dubbo.util.CmsConstants;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.system.util.ParamsTool;
import com.ai.paas.utils.DateUtil;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.StringUtil;

/**
 * 
 * Project Name:ecp-web-mall <br>
 * Description: <br>
 * Date:2015年10月9日上午9:27:02 <br>
 * 
 * @version
 * @since JDK 1.6
 * 
 *        登陆控制
 */

@Controller
@RequestMapping(value = "/homepage")
public class HomePageController extends EcpBaseController {

    private static String MODULE = HomePageController.class.getName();
    
    @Resource(name = "cmsAdvertiseRSV")
    private ICmsAdvertiseRSV cmsAdvertiseRSV;
    
    @RequestMapping(value="")
    public String init(Model model){
        initSeo(model);
      
        
        return "/rwzw/main/index-content";
    }
    
    /**
     * 
     * queryAd:(查询广告). <br/>
     * 
     * @param vo
     * @return
     * @throws Exception 
     * @since JDK 1.7
     */
    @RequestMapping(value = "/queryAd")
    @ResponseBody
    public Map<String,Object> queryAd(ComponentReqVO reqVO)throws Exception{
        Map<String,Object> resultMap = new HashMap<String,Object>();
        List<CmsAdvertiseRespDTO> respList = null;
        
        try{
            // 1. 判断入参
            if(StringUtil.isNotEmpty(reqVO.getPlaceId())){//内容位置
                CmsAdvertiseReqDTO reqDTO = new CmsAdvertiseReqDTO();
                reqDTO.setPlaceId(reqVO.getPlaceId());
                if (StringUtil.isNotEmpty(reqVO.getStatus())) {//状态
                    reqDTO.setStatus(reqVO.getStatus());
                }else{
                    reqDTO.setStatus(CmsConstants.ParamStatus.CMS_PARAMSTATUS_1);
                }
                if (StringUtil.isNotEmpty(reqVO.getPlaceSize())) {//广告数量
                    reqDTO.setPageNo(1);
                    reqDTO.setPageSize(reqVO.getPlaceSize());
                }
                String standard = "";// 规格
                if (StringUtil.isNotEmpty(reqVO.getPlaceWidth()) && StringUtil.isNotEmpty(reqVO.getPlaceHeight())) {
                    standard = reqVO.getPlaceWidth() + "x" + reqVO.getPlaceHeight() + "!";
                }
                // 设置当前时间  查询当前时间有效的广告
                //reqDTO.setThisTime(DateUtil.getSysDate());
                reqDTO.setThisTime(DateUtil.getTheDayFirstSecond(DateUtil.getSysDate()));
           
                // 2. 调用广告服务，无分页
                respList = new ArrayList<CmsAdvertiseRespDTO>();
                PageResponseDTO<CmsAdvertiseRespDTO> pageInfo = cmsAdvertiseRSV.queryCmsAdvertisePage(reqDTO);
                if (pageInfo != null) {
                    respList = pageInfo.getResult();
                }
                // 3. 调文件服务器，返回图片
                if (!CollectionUtils.isEmpty(respList)) {
                    for (CmsAdvertiseRespDTO dto : respList) {
                        // 3.1调文件服务器，返回图片
                        if (StringUtil.isNotBlank(dto.getVfsId())) {
                            dto.setVfsUrl(ParamsTool.getImageUrl(dto.getVfsId(),standard));
                        }
                        if (StringUtil.isNotBlank(dto.getLinkUrl())) {
                            //当链接类型不为促销、其它时，拼绝对路径
                            if(!"03".equals(dto.getLinkType()) && !"09".equals(dto.getLinkType())){
                              //拼接商品的详情地址
                                CmsSiteRespDTO siteDto = CmsCacheUtil.getCmsSiteCache(1l);
                                dto.setLinkUrl(siteDto.getSiteUrl()+dto.getLinkUrl());
                            }
                        }
                    }
                }
            }
        }catch(Exception err){
            LogUtil.error(MODULE, "查询广告出现异常:",err);
        }
        // 4. 返回结果
        resultMap.put("respList", respList);
        return resultMap;
    }
    
    
    public void initSeo(Model model){
        model.addAttribute("title", "人卫智网-医学教育智慧平台-人民卫生出版社");
        model.addAttribute("keywords", "人卫智网,人卫智慧平台,医学教育,医学考试辅导");
        model.addAttribute("description", "人卫医学网全面升级为人卫智网，人卫智网是人民卫生出版社全力打造的国家权威医学教育智慧平台，提供医学教材、数字教材、电子书、数据库等产品及医学教育考试辅导培训");
    }
}
