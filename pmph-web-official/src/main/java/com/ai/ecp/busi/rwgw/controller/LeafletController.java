package com.ai.ecp.busi.rwgw.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import com.ai.ecp.cms.dubbo.dto.CmsPlaceReqDTO;
import com.ai.ecp.cms.dubbo.dto.CmsPlaceRespDTO;
import com.ai.ecp.cms.dubbo.dto.CmsSiteRespDTO;
import com.ai.ecp.cms.dubbo.interfaces.ICmsAdvertiseRSV;
import com.ai.ecp.cms.dubbo.interfaces.ICmsPlaceRSV;
import com.ai.ecp.cms.dubbo.util.CmsCacheUtil;
import com.ai.ecp.cms.dubbo.util.CmsConstants;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.system.util.ParamsTool;
import com.ai.paas.utils.DateUtil;
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
@RequestMapping(value = "/leaflet")
public class LeafletController extends EcpBaseController {

    private static String MODULE = LeafletController.class.getName();
    
    @Resource(name = "cmsAdvertiseRSV")
    private ICmsAdvertiseRSV cmsAdvertiseRSV;
    @Resource(name = "cmsPlaceRSV")
    private ICmsPlaceRSV cmsPlaceRSV;
    /**
     * qryLeafletList:(查询广告列表). <br/>
     * 
     * @param model
     * @param placeId
     * @param linkType
     * @param status
     * @return
     * @throws Exception
     * @since JDK 1.6
     */
    @RequestMapping(value = "/qryLeafletList")
    @ResponseBody
    public Map<String,Object> qryLeafletList(Model model,ComponentReqVO reqVO){
        LogUtil.info(MODULE, "==========reqVO:" + reqVO.toString() + ";");
        return qryLeaflet(reqVO);
    }
    /**
     * 
     * qryLeafletVM:(获取广告，返回vm页面). <br/> 
     * 
     * @param model
     * @param reqVO
     * @return 
     * @since JDK 1.6
     */
    @RequestMapping(value = "/qryLeafletVM")
    public String qryLeafletVM(Model model,ComponentReqVO reqVO){
        LogUtil.info(MODULE, "==========reqVO:" + reqVO.toString() + ";");
        String url = reqVO.getReturnUrl();
        Map<String,Object> resultMap  = qryLeaflet(reqVO);
        if(null != resultMap){
            Iterator<Entry<String, Object>> iter = resultMap.entrySet().iterator();
            while(iter.hasNext()){
                Entry<String, Object> item = iter.next();
                model.addAttribute(item.getKey(), item.getValue());
            }
        }
        return url;
    }
    /**
     * 
     * qryLeaflet:(从数据库获取广告信息). <br/> 
     * 
     * @param reqVO
     * @return 
     * @since JDK 1.6
     */
    private Map<String,Object> qryLeaflet(ComponentReqVO reqVO){
        Map<String,Object> resultMap = new HashMap<String,Object>();
        List<CmsAdvertiseRespDTO> respList = null;
        CmsPlaceRespDTO placeRespDto = null;
        // 1. 入参加工
        if(StringUtil.isNotEmpty(reqVO.getPlaceId())){//内容位置
            //1.1 查询内容位置
            placeRespDto = this.qryPlaceByID(reqVO.getPlaceId(), reqVO.getStatus());
            if(placeRespDto != null && StringUtil.isNotEmpty(placeRespDto.getId())){
                if(StringUtil.isNotEmpty(reqVO.getPlaceHeight()) && StringUtil.isNotEmpty(placeRespDto.getPlaceHeight())){
                    reqVO.setPlaceHeight(placeRespDto.getPlaceHeight());
                }
                if(StringUtil.isNotEmpty(reqVO.getPlaceWidth()) && StringUtil.isNotEmpty(placeRespDto.getPlaceWidth())){
                    reqVO.setPlaceWidth(placeRespDto.getPlaceWidth());
                }
                if(StringUtil.isNotEmpty(reqVO.getPlaceSize()) && StringUtil.isNotEmpty(placeRespDto.getPlaceCount())){
                    reqVO.setPlaceSize(placeRespDto.getPlaceCount());
                }
                
                //1.2 初始化查询参数
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
                reqDTO.setThisTime(DateUtil.getSysDate());
                //reqDTO.setThisTime(DateUtil.getTheDayFirstSecond(DateUtil.getSysDate()));
               
                // 2. 调用广告服务，无分页
                respList = new ArrayList<CmsAdvertiseRespDTO>();
                try{
                    PageResponseDTO<CmsAdvertiseRespDTO> pageInfo = cmsAdvertiseRSV.queryCmsAdvertisePage(reqDTO);
                    if (pageInfo != null) {
                        respList = pageInfo.getResult();
                    }
                    // 3. 返回图片URL 及链接地址
                    CmsSiteRespDTO mallSiteDto = CmsCacheUtil.getCmsSiteCache(1l);
                    if (!CollectionUtils.isEmpty(respList)) {
                        for (CmsAdvertiseRespDTO dto : respList) {
                            // 3.1调文件服务器，返回图片
                            if (StringUtil.isNotBlank(dto.getVfsId())) {
                                dto.setVfsUrl(ParamsTool.getImageUrl(dto.getVfsId(),standard));
                            }
                            //缩略图
                            if(StringUtil.isNotBlank(dto.getNailVfsId())){
                                dto.setNailVfsUrl(ParamsTool.getImageUrl(dto.getNailVfsId(),""));
                            }
                            if (StringUtil.isNotBlank(dto.getLinkUrl())) {
                                if (StringUtil.isNotBlank(dto.getLinkUrl())) {
                                    if(CmsConstants.LinkType.CMS_LINKTYPE_01.equalsIgnoreCase(dto.getLinkType())){//商品
                                        if(this.isNumeric(dto.getLinkUrl())){
                                            dto.setLinkUrl("/gdsdetail/"+dto.getLinkUrl()+"-");
                                        }
                                        dto.setLinkUrl(mallSiteDto.getSiteUrl()+dto.getLinkUrl());
                                    }else if(CmsConstants.LinkType.CMS_LINKTYPE_02.equalsIgnoreCase(dto.getLinkType())){//公告
                                        if(this.isNumeric(dto.getLinkUrl())){
                                            dto.setLinkUrl("/info/infodetail?id="+dto.getLinkUrl());
                                        }
                                        dto.setLinkUrl(mallSiteDto.getSiteUrl()+dto.getLinkUrl());
                                    }else if(CmsConstants.LinkType.CMS_LINKTYPE_03.equalsIgnoreCase(dto.getLinkType())){
                                        dto.setLinkUrl("/prom/init");
                                        dto.setLinkUrl(mallSiteDto.getSiteUrl()+dto.getLinkUrl());
                                    }
                                }
                            }
                        }
                    }
                }catch(Exception err){
                    LogUtil.error(MODULE, "查询广告出现异常:",err);
                }
            }
        }
        // 4. 返回结果
        resultMap.put("respList", respList);
        resultMap.put("placeDto", placeRespDto);
        if(placeRespDto !=null){
            resultMap.put("siteId", placeRespDto.getSiteId()); 
        }
        return resultMap;
    }
    /**
     * 
     * qryPlaceByID:(查询内容位置). <br/> 
     * 
     * @param placeId
     * @param status
     * @return 
     * @since JDK 1.6
     */
    private CmsPlaceRespDTO  qryPlaceByID(Long placeId,String status){
        CmsPlaceRespDTO respDto = null;
        if(StringUtil.isNotEmpty(placeId)){
            if(StringUtil.isBlank(status)){//默认查询有效的广告位置
                status = CmsConstants.ParamStatus.CMS_PARAMSTATUS_1;
            }
            
            CmsPlaceReqDTO reqDto= new CmsPlaceReqDTO();
            reqDto.setId(placeId);
            reqDto.setStatus(status);
            try {
                respDto = cmsPlaceRSV.queryCmsPlace(reqDto);
            } catch (Exception e) {
                LogUtil.error(MODULE, "广告查询中查询内容位置出错！");
                respDto = null;
            }
            
        }
        
        return respDto;
    }
    /**
     * 
     * isNumeric:(判断字符串是否为纯数字). <br/> 
     * 
     * @param str
     * @return 
     * @since JDK 1.6
     */
    private boolean isNumeric(String str){
        if(StringUtil.isNotEmpty(str)){
            Pattern pattern = Pattern.compile("[0-9]*"); 
            Matcher isNum = pattern.matcher(str);
            if( isNum.matches() ){
                return true; 
            }
        }
         
        return false; 
     }
}
