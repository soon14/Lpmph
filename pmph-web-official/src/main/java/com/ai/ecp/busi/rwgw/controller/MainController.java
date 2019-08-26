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
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.ArrayUtils;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.ecp.base.controller.EcpBaseController;
import com.ai.ecp.base.vo.EcpBasePageRespVO;
import com.ai.ecp.busi.rwgw.vo.ComponentReqVO;
import com.ai.ecp.cms.dubbo.dto.CmsArticleReqDTO;
import com.ai.ecp.cms.dubbo.dto.CmsArticleRespDTO;
import com.ai.ecp.cms.dubbo.dto.CmsChannelReqDTO;
import com.ai.ecp.cms.dubbo.dto.CmsChannelResDTO;
import com.ai.ecp.cms.dubbo.dto.CmsFloorAdvertiseReqDTO;
import com.ai.ecp.cms.dubbo.dto.CmsFloorAdvertiseRespDTO;
import com.ai.ecp.cms.dubbo.dto.CmsFloorGdsReqDTO;
import com.ai.ecp.cms.dubbo.dto.CmsFloorGdsRespDTO;
import com.ai.ecp.cms.dubbo.dto.CmsFloorReqDTO;
import com.ai.ecp.cms.dubbo.dto.CmsFloorRespDTO;
import com.ai.ecp.cms.dubbo.dto.CmsFloorTabReqDTO;
import com.ai.ecp.cms.dubbo.dto.CmsFloorTabRespDTO;
import com.ai.ecp.cms.dubbo.dto.CmsPlaceChannelReqDTO;
import com.ai.ecp.cms.dubbo.dto.CmsPlaceChannelRespDTO;
import com.ai.ecp.cms.dubbo.dto.CmsPlaceReqDTO;
import com.ai.ecp.cms.dubbo.dto.CmsPlaceRespDTO;
import com.ai.ecp.cms.dubbo.dto.CmsSiteInfoReqDTO;
import com.ai.ecp.cms.dubbo.dto.CmsSiteInfoRespDTO;
import com.ai.ecp.cms.dubbo.dto.CmsSiteRespDTO;
import com.ai.ecp.cms.dubbo.interfaces.ICmsArticleRSV;
import com.ai.ecp.cms.dubbo.interfaces.ICmsChannelRSV;
import com.ai.ecp.cms.dubbo.interfaces.ICmsFloorAdvertiseRSV;
import com.ai.ecp.cms.dubbo.interfaces.ICmsFloorGdsRSV;
import com.ai.ecp.cms.dubbo.interfaces.ICmsFloorRSV;
import com.ai.ecp.cms.dubbo.interfaces.ICmsFloorTabRSV;
import com.ai.ecp.cms.dubbo.interfaces.ICmsPlaceChannelRSV;
import com.ai.ecp.cms.dubbo.interfaces.ICmsPlaceRSV;
import com.ai.ecp.cms.dubbo.interfaces.ICmsSiteInfoRSV;
import com.ai.ecp.cms.dubbo.util.CmsCacheUtil;
import com.ai.ecp.cms.dubbo.util.CmsConstants;
import com.ai.ecp.goods.dubbo.constants.GdsOption.GdsQueryOption;
import com.ai.ecp.goods.dubbo.constants.GdsOption.SkuQueryOption;
import com.ai.ecp.goods.dubbo.dto.GdsMediaRespDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsInfoDetailRespDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsInfoReqDTO;
import com.ai.ecp.goods.dubbo.interfaces.IGdsInfoQueryRSV;
import com.ai.ecp.goods.dubbo.interfaces.IGdsSkuInfoQueryRSV;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.system.util.ParamsTool;
import com.ai.paas.utils.DateUtil;
import com.ai.paas.utils.FileUtil;
import com.ai.paas.utils.ImageUtil;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.StringUtil;
import com.alibaba.dubbo.common.utils.CollectionUtils;

/**
 * Project Name:pmph-web-official <br>
 * Description: <br>
 * Date:2015年11月23日下午9:49:44  <br>
 * 人卫官网
 * @version  
 * @since JDK 1.6 
 */  
@Controller
@RequestMapping(value = "/main")
public class MainController extends EcpBaseController {

    private static String MODULE = MainController.class.getName();
    
    private final Long PMPHENSITEID =5L;
    
    private final int GDSDESCCONTENT_LENGTH = 256;
    @Resource
    private ICmsChannelRSV cmsChannelRSV;
    @Resource(name = "cmsSiteInfoRSV")
    private ICmsSiteInfoRSV cmsSiteInfoRSV;    
    @Resource(name = "cmsArticleRSV")
    private ICmsArticleRSV cmsArticleRSV;
    @Resource(name = "cmsPlaceChannelRSV")
    private ICmsPlaceChannelRSV cmsPlaceChannelRSV;
    @Resource(name = "cmsFloorRSV")
    private ICmsFloorRSV cmsFloorRSV;
    @Resource(name = "cmsFloorGdsRSV")
    private ICmsFloorGdsRSV cmsFloorGdsRSV;
    @Resource(name = "cmsFloorAdvertiseRSV")
    private ICmsFloorAdvertiseRSV cmsFloorAdvertiseRSV;
    @Resource(name = "gdsInfoQueryRSV")
    private IGdsInfoQueryRSV gdsInfoQueryRSV;
    @Resource(name = "cmsFloorTabRSV")
    private ICmsFloorTabRSV cmsFloorTabRSV;
    @Resource(name = "cmsPlaceRSV")
    private ICmsPlaceRSV cmsPlaceRSV;
    @Resource(name = "gdsSkuInfoQueryRSV")
    private IGdsSkuInfoQueryRSV gdsSkuInfoQueryRSV;
    
    /** 
     * init:(默认初始化方法). <br/> 
     * 
     * @return 
     * @since JDK 1.6 
     */ 
    @RequestMapping()
    public String init(Model model){
        model.addAttribute("ishome", "1");
        return "/rwgw/main/indexcontent/index-content";
    }
    
    /**
     * 
     * qryChannel:(查询栏目). <br/> 
     * 2015.11.21
     * @return VM
     * @since JDK 1.6
     */
    @RequestMapping(value="qrychannel")
    public String qryChannel(Model model,ComponentReqVO reqVo){
        LogUtil.info(MODULE, "==========reqVO :" + reqVo.toString() + ";");
        //1.声明必须的变量
        String url = "/rwgw/main/componentVM/channel-nav";//VM地址
        if(StringUtil.isNotBlank(reqVo.getReturnUrl())){
            url = reqVo.getReturnUrl();
        }
        CmsChannelReqDTO reqDto = new CmsChannelReqDTO();//查询条件
        List<CmsChannelResDTO> channelsList = null;//栏目数据
        Long channelParentCode = 0l;//0代表根节点
        
        //2.用VO初始化查询条件
        //设置站点
        if(reqVo.getSiteId()!=null && StringUtil.isNotBlank(reqVo.getSiteId().toString())){
            reqDto.setSiteId(reqVo.getSiteId());
        }else{
            return url;
        }    
        //设置状态
        if(StringUtil.isNotBlank(reqVo.getStatus())){
            reqDto.setStatus(reqVo.getStatus());
        }else{//默认有效
            reqDto.setStatus(CmsConstants.ParamStatus.CMS_PARAMSTATUS_1);
        }
        //平台类型
        if(StringUtil.isNotBlank(reqVo.getPlatformType())){
            reqDto.setPlatformType(reqVo.getPlatformType());
        }else{//默认WEB
            reqDto.setPlatformType(CmsConstants.PlatformType.CMS_PLATFORMTYPE_01);
        }
        
        //导航栏目
        if(StringUtil.isNotBlank(reqVo.getChannelType())){
            reqDto.setChannelType(reqVo.getChannelType());
        }else{//默认导航栏目
            reqDto.setChannelType(CmsConstants.ChannelType.CMS_CHANNEL_TYPE_01);
        }
        
        //3.查询栏目
        try {
            //3.1 查询一级栏目
            reqDto.setChannelParent(channelParentCode);
            channelsList = cmsChannelRSV.listChannel(reqDto);
            //3.2 查询二级栏目
            if(!CollectionUtils.isEmpty(channelsList)){
                for(CmsChannelResDTO dto : channelsList){
                    if(dto!=null && dto.getId() != null && StringUtil.isNotBlank(dto.getId().toString()))
                    reqDto.setChannelParent(dto.getId());
                    dto.setChildList(cmsChannelRSV.listChannel(reqDto));
                }
            }
        } catch (Exception e) {
            LogUtil.error(MODULE, "查询栏目失败",e);
        }
        
        model.addAttribute("siteId", reqDto.getSiteId());
        model.addAttribute("channelsList", channelsList);
        return url;
    }

    /**
     * 
     * qryArticleListVM:(用于官网首页的组件查询文章列表). <br/> 
     * 
     * @param model
     * @param reqVo
     * @return VM
     * @since JDK 1.6
     */
    @RequestMapping(value = "qryArticleListVM")
    public String qryArticleListVM(Model model, ComponentReqVO reqVo) {
        
        // 1.声明必须的变量
        String url = "/rwgw/main/componentVM/article-list-com-";// VM基础地址
        
        PageResponseDTO<CmsArticleRespDTO> articlePage = null;// 文章列表容器
        CmsArticleReqDTO reqDTO = new CmsArticleReqDTO();// 查询条件对象
        CmsPlaceChannelReqDTO placeChannelDto = new CmsPlaceChannelReqDTO();// 位置栏目查询条件对象
        CmsChannelReqDTO channelReqDto = new CmsChannelReqDTO();// 栏目查询条件
        String standard = "";// 图片规则
        
        // 根据类型 确定vm地址
        if (StringUtil.isNotBlank(reqVo.getMenuType())) {
            url += reqVo.getMenuType();
        } else {
            url += "0";
        }
        if(StringUtil.isNotBlank(reqVo.getReturnUrl())){
            url = reqVo.getReturnUrl();
        }
        
        if (StringUtil.isEmpty(reqVo.getPlaceId())) {
            LogUtil.error(MODULE, "入参placeId为空！");
            return url;
        }
        
        //根据内容位置找站点
        CmsPlaceRespDTO placeInfo = this.getPlaceInfoByID(reqVo.getPlaceId());
        Long siteId = null;
        if(placeInfo ==null || StringUtil.isEmpty(placeInfo.getId())){
            return url;
        }else{
            siteId = placeInfo.getSiteId();
        }
        
        // 1.根据内容位置ID查询内容位置与栏目关系
        CmsPlaceChannelRespDTO placeChannel = null;
        try {
            placeChannelDto.setPlaceId(reqVo.getPlaceId());
            placeChannelDto.setStatus(CmsConstants.ParamStatus.CMS_PARAMSTATUS_1);
            List<CmsPlaceChannelRespDTO> placeChannelList = cmsPlaceChannelRSV.queryCmsPlaceChannelList(placeChannelDto);
            if (!CollectionUtils.isEmpty(placeChannelList)) {
                placeChannel = placeChannelList.get(0);
            }
        } catch (Exception e) {
            LogUtil.error(MODULE, "根据位置查询栏目出现异常:", e);
            return url;
        }
        
        // 2.根据栏目ID查询栏目信息
        if (placeChannel == null || StringUtil.isEmpty(placeChannel.getChannelId())) {
            if(siteId !=null && siteId ==this.PMPHENSITEID){
                model.addAttribute("errorMessage", "Sorry,No Channel.");
            }else{
                model.addAttribute("errorMessage", "亲，暂未关联栏目！"); 
            }
            
            return url;
        }
        CmsChannelResDTO channel = null;// 栏目信息
        channelReqDto.setId(placeChannel.getChannelId());
        channelReqDto.setStatus(CmsConstants.ParamStatus.CMS_PARAMSTATUS_1);
        
        List<CmsChannelResDTO> channelList = cmsChannelRSV.listChannel(channelReqDto);
        if (!CollectionUtils.isEmpty(channelList)) {
            channel = channelList.get(0);
        }

        // 3.根据栏目ID查询栏目下的文章
        if (channel == null || StringUtil.isEmpty(channel.getId())) {
            if(siteId !=null && siteId ==this.PMPHENSITEID){
                model.addAttribute("errorMessage", "Sorry,Useless Channel.");
            }else{
                model.addAttribute("errorMessage", "关联的栏目已失效,请重新关联！"); 
            }
            return url;
        }
        reqDTO.setChannelId(channel.getId());//栏目ID
        if (StringUtil.isNotBlank(reqVo.getHomepageIsShow())) {
            reqDTO.setHomepageIsShow(reqVo.getHomepageIsShow());// 首页是否显示
        } else {
            reqDTO.setHomepageIsShow(CmsConstants.IsNot.CMS_ISNOT_0);
        }
        reqDTO.setStatus(CmsConstants.ParamStatus.CMS_PARAMSTATUS_1);
        reqDTO.setThisTime(DateUtil.getSysDate());// 设置当前时间
        reqDTO.setPageNo(1);
        if (StringUtil.isNotEmpty(reqVo.getPlaceSize())) {// 设置查询条数
            reqDTO.setPageSize(reqVo.getPlaceSize());
        } else {
            reqDTO.setPageSize(3);
        }
        try {
            articlePage = cmsArticleRSV.queryCmsArticlePage(reqDTO);
            // 获取文章图片
            if (StringUtil.isNotEmpty(reqVo.getPlaceWidth())
                    && StringUtil.isNotEmpty(reqVo.getPlaceHeight())) {
                standard = reqVo.getPlaceWidth() + "x" + reqVo.getPlaceHeight() + "!";
            }
            if (articlePage != null && !CollectionUtils.isEmpty(articlePage.getResult())) {
                for (CmsArticleRespDTO respDto : articlePage.getResult()) {
                    respDto.setThumbnailUrl(ParamsTool.getImageUrl(respDto.getThumbnail(), standard));
                }
            }
        } catch (Exception e) {
            LogUtil.error(MODULE, "根据栏目查询文章列表出现异常:", e);
            return url;
        }

        if (articlePage != null) {
            model.addAttribute("articles", articlePage.getResult());
        } else {
            model.addAttribute("articles", null);
        }
        model.addAttribute("channel", channel);
        model.addAttribute("siteId", siteId); 
        return url;
    }
    
    /** 
     * about:(点击栏跳转至关于我们页面  优先级：文章>栏目>网站信息 只能返回指定站点的信息). <br/> 
     * 
     * @param model
     * @param reqVO：siteInfoId  articleId channelId
     * @return 
     * @since JDK 1.6 
     */ 
    @RequestMapping(value="about")
    public String about(Model model,ComponentReqVO reqVO){
        boolean dataError = false;
        Long siteId = null;
        String url = "/rwgw/main/aboutcontent/about-content";
        if(StringUtil.isNotEmpty(reqVO.getSiteId())){
            siteId = reqVO.getSiteId();
        }
        if(null == siteId){
            siteId = 3L;//默认为官网站点id
        }
        if(this.PMPHENSITEID.equals(siteId)){//英文网站
            url = "/rwgw-en/main/aboutcontent/about-content";
        }
        //根据条件返回数据
        String articleId = reqVO.getArticleId();
        Long channelId = reqVO.getChannelId();
        Long siteInfoId = reqVO.getSiteInfoId();
        //获取文章
        CmsArticleRespDTO article = null;
        if(StringUtil.isNotBlank(articleId)){
            if(isNumeric(articleId)){
                article = getArticleById(Long.parseLong(articleId));
            }
            //文章优先级大  根据文章重置栏目信息   有id 却没有数据 则标记为数据错误
            if(null == article || !siteId.equals(article.getSiteId())){
                articleId = null;
                article = null;
                channelId = null;
                dataError = true;//标记为错误
            }else{
                channelId = article.getChannelId();
            }
        }
        //获取栏目
        CmsChannelResDTO channel = null;
        if(null != channelId){
            channel =  getChannelById(channelId);
            //有id 却没有数据 则标记为数据错误
            if(null == channel || !siteId.equals(channel.getSiteId())){
                channelId = null;
                channel = null;
                siteInfoId = null;
                dataError = true;//标记为错误
            }
        }
        //栏目优先级大于网站信息  根据栏目重置网站信息数据
        CmsSiteInfoRespDTO siteInfoRespDTO = null;
        if(null != channel){
            siteInfoRespDTO = this.getSiteInfoByChannelId(channelId, siteId);
            if(null != siteInfoRespDTO){
                siteInfoId = siteInfoRespDTO.getId();
                channel = null;
            }else{
                siteInfoId = null;
            }
        }
        //获取网站信息数据
        if(null != siteInfoId && null == siteInfoRespDTO){
            siteInfoRespDTO = this.getSiteInfoById(siteInfoId);
            //有id 却没有数据 则标记为数据错误
            if(null == siteInfoRespDTO || !siteId.equals(siteInfoRespDTO.getSiteId())){
                siteInfoId = null;
                siteInfoRespDTO = null;
                dataError = true;//标记为错误
            }
        }
        //获取顶级网站信息 3种清况
        CmsSiteInfoRespDTO topSiteInfo = null;
        if(null == siteInfoRespDTO){//取站点下顶级信息第一个  没关联到网站信息的栏目都默认归属于第一个顶级信息下
            List<CmsSiteInfoRespDTO> topSiteInfoList = this.getTopSiteInfo(siteId, false);
            if(CollectionUtils.isNotEmpty(topSiteInfoList)){
                topSiteInfo = topSiteInfoList.get(0);
            }
            topSiteInfoList = null;
        }else if(CmsConstants.SiteInfoRoot.CMS_SITE_INFO_ROOT_ID.equals(siteInfoRespDTO.getParent())){
            topSiteInfo = siteInfoRespDTO;
        }else{
            topSiteInfo = this.getSiteInfoById(siteInfoRespDTO.getParent());
        }
        //获取网站信息列表
        List<CmsSiteInfoRespDTO> siteInfoList = null;
        if(null != topSiteInfo){
            siteInfoList = this.getSubSiteInfo(topSiteInfo.getId(),topSiteInfo.getSiteId());
        }
        //没有指定栏目及 网站信息   或者本身是顶级信息   则取第一个网站信息
        if(!dataError && ((null == siteInfoRespDTO && null==channel) || (null != siteInfoRespDTO && CmsConstants.SiteInfoRoot.CMS_SITE_INFO_ROOT_ID.equals(siteInfoRespDTO.getParent())))){
            if(CollectionUtils.isNotEmpty(siteInfoList)){
                siteInfoRespDTO = siteInfoList.get(0);
            }else{
                siteInfoRespDTO = null;
            }
        }
        //回传到页面
        model.addAttribute("article", article);
        if(null == siteInfoRespDTO && null != channel){//当栏目无对应网站信息  返回栏目信息
            model.addAttribute("channel", channel);
        }
        model.addAttribute("topSiteInfo", topSiteInfo);
        model.addAttribute("siteInfoList", siteInfoList);
        model.addAttribute("siteInfo", siteInfoRespDTO);
        model.addAttribute("menuType", reqVO.getMenuType());
        
       return url; 
    }
    /**
     * 
     * getTopSiteInfo:(根据站点id从数据库获取顶级网站信息,可配置是否包含虚拟根节点). <br/> 
     * 
     * @param siteId
     * @param containRoot 
     * @since JDK 1.6
     */
    private List<CmsSiteInfoRespDTO> getTopSiteInfo(Long siteId,boolean containRoot){
        List<CmsSiteInfoRespDTO> respList = null;
        if(null != siteId){
            respList = cmsSiteInfoRSV.queryTopSiteInfoBySiteId(siteId);
        }
        if(CollectionUtils.isEmpty(respList)){
            respList = new ArrayList<CmsSiteInfoRespDTO>();
        }
        if(containRoot){
            CmsSiteInfoRespDTO root = new CmsSiteInfoRespDTO();
            root.setId(CmsConstants.SiteInfoRoot.CMS_SITE_INFO_ROOT_ID);
            root.setSiteInfoName(CmsConstants.SiteInfoRoot.CMS_SITE_INFO_ROOT_NAME);
            respList.add(0, root);
        }
        return respList;
    }
    /**
     * 
     * getSubSiteInfo:(获取子级信息). <br/> 
     * 
     * @param id
     * @return 
     * @since JDK 1.6
     */
    private List<CmsSiteInfoRespDTO> getSubSiteInfo(Long id,Long siteId){
        List<CmsSiteInfoRespDTO> respList = null;
        if(null != id){
            CmsSiteInfoReqDTO reqDto = new CmsSiteInfoReqDTO();
            reqDto.setParent(id);
            reqDto.setSiteId(siteId);
            reqDto.setStatus(CmsConstants.ParamStatus.CMS_PARAMSTATUS_1);
            respList = cmsSiteInfoRSV.queryCmsSiteInfoList(reqDto);
        }
        if(CollectionUtils.isEmpty(respList)){
            respList = new ArrayList<CmsSiteInfoRespDTO>();
        }
        return respList;
    }
    /**
     * 
     * getChannelById:(根据栏目id获取栏目信息 只返回发布的). <br/> 
     * 
     * @param id
     * @return 
     * @since JDK 1.6
     */
    private CmsChannelResDTO getChannelById(Long id){
        CmsChannelResDTO channel = null;
        if(null != id){
            CmsChannelReqDTO channelReqDto =  new CmsChannelReqDTO();
            channelReqDto.setId(id);
            channel = cmsChannelRSV.find(channelReqDto);
            if(null != channel && !CmsConstants.ParamStatus.CMS_PARAMSTATUS_1.equals(channel.getStatus())){
                channel = null;
            }
        }
        return channel;
    }
    /**
     * 
     * getArticleById:(根据id获取文章  只返回发布的). <br/> 
     * 
     * @param id
     * @return 
     * @since JDK 1.6
     */
    private CmsArticleRespDTO getArticleById(Long id){
        CmsArticleRespDTO cmsArticleRespDTO = null;
        if(null != id){
            CmsArticleReqDTO cmsArticleReqDTO = new CmsArticleReqDTO();
            cmsArticleReqDTO.setId(id);
            cmsArticleRespDTO = cmsArticleRSV.queryCmsArticle(cmsArticleReqDTO); 
        }
        if(null != cmsArticleRespDTO && !CmsConstants.ParamStatus.CMS_PARAMSTATUS_1.equals(cmsArticleRespDTO.getStatus())){
            cmsArticleRespDTO = null;
        }
        return cmsArticleRespDTO;
    }
    /**
     * 
     * getSiteInfoByExample:(根据条件获取网站信息 只返回发布的). <br/> 
     * 
     * @param siteInfoReqDTO
     * @return 
     * @since JDK 1.6
     */
    private List<CmsSiteInfoRespDTO> getSiteInfoByExample(CmsSiteInfoReqDTO siteInfoReqDTO){
        List<CmsSiteInfoRespDTO> result = null;
        if(null != siteInfoReqDTO){
            siteInfoReqDTO.setStatus(CmsConstants.ParamStatus.CMS_PARAMSTATUS_1);
            result = cmsSiteInfoRSV.queryCmsSiteInfoList(siteInfoReqDTO);
        }
        return result;
    }
    /**
     * 
     * getSiteInfoByChannelId:(根据栏目获取网站信息 ,对应多个返回第一个 只返回发布的). <br/> 
     * 
     * @param channelId
     * @param siteId
     * @return 
     * @since JDK 1.6
     */
    private CmsSiteInfoRespDTO getSiteInfoByChannelId(Long channelId,Long siteId){
        CmsSiteInfoRespDTO siteInfoRespDTO = null;
        if(null != channelId){
            CmsSiteInfoReqDTO siteInfoReqDto = new CmsSiteInfoReqDTO();
            siteInfoReqDto.setChannelId(channelId.toString());
            siteInfoReqDto.setSiteId(siteId);
            List<CmsSiteInfoRespDTO> siteInfoList = this.getSiteInfoByExample(siteInfoReqDto);
            if(CollectionUtils.isNotEmpty(siteInfoList)){
                siteInfoRespDTO = siteInfoList.get(0);
            }
        }
        return siteInfoRespDTO;
    }
    /**
     * 
     * getSiteInfoById:(根据id获取网站信息 只返回发布的). <br/> 
     * 
     * @param id
     * @return 
     * @since JDK 1.6
     */
    private CmsSiteInfoRespDTO getSiteInfoById(Long id){
        CmsSiteInfoRespDTO siteInfoRespDTO = null;
        if(null != id){
            CmsSiteInfoReqDTO siteInfoReqDTO = new CmsSiteInfoReqDTO();
            siteInfoReqDTO.setId(id);
            siteInfoReqDTO.setStatus(CmsConstants.ParamStatus.CMS_PARAMSTATUS_1);
            siteInfoRespDTO = cmsSiteInfoRSV.queryCmsSiteInfo(siteInfoReqDTO);
        }
        if(null != siteInfoRespDTO && !CmsConstants.ParamStatus.CMS_PARAMSTATUS_1.equals(siteInfoRespDTO.getStatus())){
            siteInfoRespDTO = null;
        }
        return siteInfoRespDTO;
    }
    /** 
     * toGetData:(获取静态文件或者栏目下的文章列表). <br/> 
     * 
     * @param model
     * @param reqVO : keyword siteInfoId channelId menuType pageSize
     * @return 栏目的优先级比网站信息高
     * @since JDK 1.6 
     */ 
    @RequestMapping(value="toGetData")
    public String toGetData(Model model,ComponentReqVO reqVO){
        String url = "/rwgw/main/aboutcontent/aboutchild/about-static";
        Long siteId = 3l;//默认为官网站点id
        Long siteinfoId = reqVO.getSiteInfoId();
        Long channelId = reqVO.getChannelId();
        
        //获取栏目   
        CmsChannelResDTO channel = null;
        if(null != channelId){
            channel = this.getChannelById(channelId);
        }
        //根据栏目 获取网站信息
        CmsSiteInfoRespDTO siteInfoRespDTO = null;
        if(null != channel){
            siteId = channel.getSiteId();
            siteInfoRespDTO = this.getSiteInfoByChannelId(channel.getId(), siteId);
        }
        //获取网站信息
        
        if(null != siteinfoId && null == siteInfoRespDTO){
            siteInfoRespDTO = getSiteInfoById(siteinfoId);
        }
        if(null != siteInfoRespDTO){
            siteId = siteInfoRespDTO.getSiteId();
            channelId = siteInfoRespDTO.getChannelId();
        }
        //获取返回数据
        if(null != siteInfoRespDTO && CmsConstants.SiteInfoType.CMS_SITE_INFO_TYPE_01.equals(siteInfoRespDTO.getSiteInfoType())){//静态文件
            model.addAttribute("staticHtml",ImageUtil.getStaticDocUrl(siteInfoRespDTO.getStaticId(), "html"));
            url = "/rwgw/main/aboutcontent/aboutchild/about-static";
        }else{//列表
            url = "/rwgw/main/aboutcontent/aboutchild/about-";
            //根据不同的显示方式返回不同的页面
            if(StringUtil.isNotBlank(reqVO.getMenuType())){
                url+=reqVO.getMenuType();
            }else{//默认为列表类型
                url+="list";
            }
            
            PageResponseDTO<CmsArticleRespDTO> respDTO = null;
            if(null != channelId){
                //获取栏目下的列表
                CmsArticleReqDTO cmsArticleReqDTO = reqVO.toBaseInfo(CmsArticleReqDTO.class);
                cmsArticleReqDTO.setStatus(CmsConstants.ParamStatus.CMS_PARAMSTATUS_1);
                cmsArticleReqDTO.setEndPubTime(DateUtil.getSysDate());
                cmsArticleReqDTO.setChannelId(channelId);
                if(StringUtil.isNotBlank(reqVO.getKeyword())){//关键字
                    cmsArticleReqDTO.setArticleTitle(reqVO.getKeyword());
                }
                try {
                    respDTO = cmsArticleRSV.queryCmsArticlePage(cmsArticleReqDTO);
                } catch (Exception e) {
                    LogUtil.error(MODULE, "获取栏目下文章列表异常", e);
                }
                //获取文章图片
                if (respDTO != null && !CollectionUtils.isEmpty(respDTO.getResult())) {
                    for (CmsArticleRespDTO respDto : respDTO.getResult()) {
                        respDto.setThumbnailUrl(ParamsTool.getImageUrl(respDto.getThumbnail(), "160x160!"));
                    }
                }
            }
            if(null == respDTO){
                respDTO = new PageResponseDTO<CmsArticleRespDTO>();
            }
            try {
                model.addAttribute("respVO", EcpBasePageRespVO.buildByPageResponseDTO(respDTO));
            } catch (Exception e) {
                LogUtil.error(MODULE, "栏目下文章列表转化异常", e);
            }
        }
        model.addAttribute("siteInfoId", siteinfoId);
        model.addAttribute("channelId", channelId);
        model.addAttribute("keyword", reqVO.getKeyword());
        model.addAttribute("siteId", siteId);
        return url;
    }
    
    /** 
     * queryArticle:(根据文章ID获取文章). <br/> 
     * 
     * @param model
     * @param reqVO：articleId（参数）
     * @return 
     * @since JDK 1.6 
     */ 
    @RequestMapping(value="queryArticle")
    public String queryArticle(Model model,ComponentReqVO reqVO){
        //1 根据网站信息ID获取记录
        String url = "/rwgw/main/aboutcontent/aboutchild/about-article";
        CmsArticleRespDTO cmsArticleRespDTO = null;
        try {
            String articleId = reqVO.getArticleId();
            if(StringUtil.isNotEmpty(articleId)){
                CmsArticleReqDTO cmsArticleReqDTO = new CmsArticleReqDTO();
                cmsArticleReqDTO.setId(Long.parseLong(articleId));
                cmsArticleReqDTO.setStatus(CmsConstants.ParamStatus.CMS_PARAMSTATUS_1);
                cmsArticleRespDTO = cmsArticleRSV.queryCmsArticle(cmsArticleReqDTO);
                if(cmsArticleRespDTO != null){
                    if(StringUtil.isNotEmpty(cmsArticleRespDTO.getStaticId())){
                        //根据ID读取描述内容
                        cmsArticleRespDTO.setStaticId(ImageUtil.getStaticDocUrl(cmsArticleRespDTO.getStaticId(), "html"));
                    }
                    //缩略图URL
                    cmsArticleRespDTO.setThumbnailUrl(ParamsTool.getImageUrl(cmsArticleRespDTO.getThumbnail(),"504x377!"));
                }else{
                    cmsArticleRespDTO = null;
                }
            }
        } catch (Exception err) {
            LogUtil.error(MODULE, "查询文章出现异常:",err);
        }
        
        model.addAttribute("respVO", cmsArticleRespDTO);
        return url;
    }
    
    /**
     * 
     * qrySiteInfo:(查询网站信息列表). <br/> 
     * 2015.11.21
     * @return 
     * @since JDK 1.6
     */
    @RequestMapping(value = "/qrySiteInfo")
    @ResponseBody
    public Map<String,Object> qrySiteInfo(Model model,ComponentReqVO reqVO,HttpServletRequest request) throws Exception {
        LogUtil.info(MODULE, "==========reqVO:" + reqVO.toString() + ";");
        
        Map<String,Object> resultMap = new HashMap<String,Object>();
        if(StringUtil.isEmpty(reqVO.getSiteId())){
            return resultMap;
        }
        CmsSiteInfoReqDTO reqDTO = new CmsSiteInfoReqDTO();
        reqDTO.setStatus(CmsConstants.ParamStatus.CMS_PARAMSTATUS_1);
        reqDTO.setSiteId(reqVO.getSiteId());
        // 4. 返回结果
        resultMap.put("respList", this.getSiteInfoByExample(reqDTO));
        return resultMap;
    }
    
    /** 
     * qryFloorList:(根据内容位置获取楼层信息,返回json). <br/> 
     * 
     * @param model
     * @param reqVO
     * @return
     * @throws Exception 
     * @since JDK 1.6 
     */ 
    @RequestMapping(value = "/qryFloorList")
    @ResponseBody
    public Map<String,Object> qryFloorList(Model model,ComponentReqVO reqVO) throws Exception {
        LogUtil.info(MODULE, "==========reqVO:" + reqVO.toString() + ";" );
        return qryFloor(reqVO);
    }
    /**
     * 
     * qryFloorVM:(根据内容位置获取楼层信息,返回vm). <br/> 
     * 
     * @param model
     * @param reqVO
     * @return
     * @throws Exception 
     * @since JDK 1.6
     */
    @RequestMapping(value = "/qryFloorVM")
    public String qryFloorVM(Model model,ComponentReqVO reqVO) throws Exception {
        LogUtil.info(MODULE, "==========reqVO:" + reqVO.toString() + ";" );
        String url = reqVO.getReturnUrl();
        Map<String,Object> resultMap  = qryFloor(reqVO);
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
     * qryFloor:(获取楼层数据). <br/> 
     * 
     * @param reqVO
     * @return 
     * @since JDK 1.6
     */
    private Map<String,Object> qryFloor(ComponentReqVO reqVO){
        Map<String,Object> resultMap = new HashMap<String,Object>();
        
        //获取内容位置信息
        CmsPlaceRespDTO placeInfo = this.getPlaceInfoByID(reqVO.getPlaceId());
        
        // 图片规格
        String standard = "";
        if (StringUtil.isNotEmpty(reqVO.getPlaceWidth())&& StringUtil.isNotEmpty(reqVO.getPlaceHeight())) {
            standard = reqVO.getPlaceWidth() + "x" + reqVO.getPlaceHeight() + "!";
        } 
        CmsFloorRespDTO cmsFloorRespDTO = null;
        List<CmsFloorTabRespDTO> floorTabRespList = null;
        List<GdsInfoDetailRespDTO> gdsInfoDetailRespList = null;
        List<CmsFloorAdvertiseRespDTO> floorAdRespList = null;
        try{
            // 1 根据内容位置ID查询有效楼层
            cmsFloorRespDTO = this.qryFloorByPlaceId(reqVO.getPlaceId(),reqVO.getStatus());
            if(cmsFloorRespDTO != null && StringUtil.isNotEmpty(cmsFloorRespDTO.getId())){
                // 2 当有效楼层不为空时，根据楼层ID查询页签
                if (reqVO.getTabSize()!=null && reqVO.getTabSize() > 0) {//页签大于0 即启用页签
                    CmsFloorTabReqDTO cmsFloorTabReqDTO = new CmsFloorTabReqDTO();
                    cmsFloorTabReqDTO.setFloorId(cmsFloorRespDTO.getId());// 楼层ID
                    floorTabRespList = this.qryFloorTabByFloorId(reqVO, cmsFloorTabReqDTO);
                } 
                // 3 查楼层下广告
                if(null != reqVO.getFloorImgSize() && 0 < reqVO.getFloorImgSize()){
                    floorAdRespList = this.qryFloorAdList(cmsFloorRespDTO.getId(),reqVO);
                }
                // 4 当页签不为空时，获取第一个页签的商品，当页签为空时，获取楼层下的商品
                if(null != reqVO.getGdsSize() && 0 < reqVO.getGdsSize() && !CollectionUtils.isEmpty(floorTabRespList)){
                    CmsFloorGdsReqDTO cmsFloorGdsReqDTO = new CmsFloorGdsReqDTO();
                    cmsFloorGdsReqDTO.setFloorId(cmsFloorRespDTO.getId());// 楼层ID
                    CmsFloorTabRespDTO cmsFloorTabRespDTO = floorTabRespList.get(0);
                    if(cmsFloorTabRespDTO != null && StringUtil.isNotEmpty(cmsFloorTabRespDTO.getId())){
                        cmsFloorGdsReqDTO.setTabId(cmsFloorTabRespDTO.getId());
                    }
                    gdsInfoDetailRespList = this.qryFloorGdsList(reqVO,cmsFloorGdsReqDTO,standard);
                }
            }
        }catch(Exception err){
            LogUtil.error(MODULE, "查询商品分类出现异常:",err);
        }
        
        // 4.返回结果
        resultMap.put("floorRespDTO", cmsFloorRespDTO);
        resultMap.put("floorTabList", floorTabRespList);
        resultMap.put("floorAdRespList", floorAdRespList);
        resultMap.put("gdsList", gdsInfoDetailRespList);
        if(placeInfo !=null){
            resultMap.put("siteId", placeInfo.getSiteId());
        }
        return resultMap;
    }
    /**
     * 
     * qryFloorAdList:(通过楼层id获取楼层广告). <br/> 
     * 
     * @param floorId
     * @param reqVO 使用里面的 floorImgSize floorImgWidth floorImgHeight
     * @return 
     * @since JDK 1.6
     */
    private List<CmsFloorAdvertiseRespDTO> qryFloorAdList(Long floorId,ComponentReqVO reqVO){
        List<CmsFloorAdvertiseRespDTO> result = null;;
        if(null == floorId || null == reqVO || null == reqVO.getFloorImgSize() || 0 >= reqVO.getFloorImgSize()){
            return new ArrayList<CmsFloorAdvertiseRespDTO>();
        }
        CmsFloorAdvertiseReqDTO reqDto = new CmsFloorAdvertiseReqDTO();
        reqDto.setFloorId(floorId);
        reqDto.setPageNo(1);
        reqDto.setPageSize(reqVO.getFloorImgSize());
        reqDto.setStatus(CmsConstants.ParamStatus.CMS_PARAMSTATUS_1);
        PageResponseDTO<CmsFloorAdvertiseRespDTO> resultPage = cmsFloorAdvertiseRSV.queryCmsFloorAdvertisePage(reqDto);
        if(null != resultPage){
            result = resultPage.getResult();
        }
        if(null == result){
            result = new ArrayList<CmsFloorAdvertiseRespDTO>();
        }
        //扩展信息   图片地址   链接地址
        String standard = "";//图片尺寸
        if(null != reqVO.getFloorImgWidth() && 0 < reqVO.getFloorImgWidth()){
            standard += reqVO.getFloorImgWidth()+"x";
            if(null != reqVO.getFloorImgHeight() && 0 < reqVO.getFloorImgHeight()){
                standard += reqVO.getFloorImgHeight();
            }
            standard += "!";
        }
        for(CmsFloorAdvertiseRespDTO  advertise : result){
            advertise.setVfsUrl(ParamsTool.getImageUrl(advertise.getVfsId(), standard));
            advertise.setLinkUrl(fomatAdLinkUrl(advertise.getLinkUrl(),advertise.getLinkType()));
        }
        return result;
    }
    /**
     * 
     * fomatAdLinkUrl:(通过广告链接类型 扩展图片地址). <br/> 
     * 
     * @param linkUrl
     * @param type
     * @return 
     * @since JDK 1.6
     */
    private String fomatAdLinkUrl(String linkUrl,String type){
        if(StringUtil.isBlank(linkUrl)){
            return linkUrl;
        }
        CmsSiteRespDTO mallSiteDto = CmsCacheUtil.getCmsSiteCache(1l);//商城访问地址  管理平台配置的链接类型目前只能是商城的内容
        if(CmsConstants.LinkType.CMS_LINKTYPE_01.equalsIgnoreCase(type)){//商品
            if(this.isNumeric(linkUrl)){
                linkUrl = "/gdsdetail/"+linkUrl+"-";
            }
            linkUrl = mallSiteDto.getSiteUrl()+linkUrl;
        }else if(CmsConstants.LinkType.CMS_LINKTYPE_02.equalsIgnoreCase(type)){//公告
            if(this.isNumeric(linkUrl)){
                linkUrl = "/info/infodetail?id="+linkUrl;
            }
            linkUrl = mallSiteDto.getSiteUrl()+linkUrl;
        }else if(CmsConstants.LinkType.CMS_LINKTYPE_03.equalsIgnoreCase(type)){
            if(this.isNumeric(linkUrl)){
                linkUrl = "/prom/"+linkUrl;
            }else{
                linkUrl = "/prom/init";
            }
            linkUrl = mallSiteDto.getSiteUrl()+linkUrl;
        }
        return linkUrl;
    };
    /** 
     * qryFloorByPlaceId:(根据内容位置获取楼层id). <br/> 
     * 
     * @param reqVO
     * @return
     * @throws Exception 
     * @since JDK 1.6 
     */ 
    private CmsFloorRespDTO qryFloorByPlaceId(Long id , String status) throws Exception{
        CmsFloorReqDTO cmsFloorReqDTO = new CmsFloorReqDTO();
        /*1.验证前店入参*/
        if(StringUtil.isEmpty(id)){//内容位置
            LogUtil.error(MODULE, "入参placeId为空！");
            return null;
        }
        cmsFloorReqDTO.setPlaceId(id);
        if (StringUtil.isNotBlank(status)) {//状态
            cmsFloorReqDTO.setStatus(status);
        } else {//默认有效
            cmsFloorReqDTO.setStatus(CmsConstants.ParamStatus.CMS_PARAMSTATUS_1);
        }
        
        CmsFloorRespDTO cmsFloorRespDTO = new CmsFloorRespDTO();
        List<CmsFloorRespDTO> floorRespList = cmsFloorRSV.queryCmsFloorList(cmsFloorReqDTO);
        if(!CollectionUtils.isEmpty(floorRespList)){
            cmsFloorRespDTO = floorRespList.get(0);
        }
        return cmsFloorRespDTO;
    }
    
    /** 
     * qryFloorTabByFloorId:(根据楼层ID获取楼层页签). <br/> 
     * 
     * @param reqVO
     * @param cmsFloorTabReqDTO
     * @return 
     * @since JDK 1.6 
     */ 
    private List<CmsFloorTabRespDTO> qryFloorTabByFloorId(ComponentReqVO reqVO,CmsFloorTabReqDTO cmsFloorTabReqDTO) throws Exception{
        
        if (StringUtil.isNotEmpty(reqVO.getStatus())) {//状态
            cmsFloorTabReqDTO.setStatus(reqVO.getStatus());
        } else {//默认有效
            cmsFloorTabReqDTO.setStatus(CmsConstants.ParamStatus.CMS_PARAMSTATUS_1);
        }
        if (StringUtil.isNotEmpty(reqVO.getTabSize())) {// 楼层页签数量
            cmsFloorTabReqDTO.setPageNo(1);
            cmsFloorTabReqDTO.setPageSize(reqVO.getTabSize());
        }
        List<CmsFloorTabRespDTO> floorTabRespList = new ArrayList<CmsFloorTabRespDTO>();
        if(cmsFloorTabReqDTO.getPageSize() != null && cmsFloorTabReqDTO.getPageSize() != 0){
            PageResponseDTO<CmsFloorTabRespDTO> floorTabPageInfo = cmsFloorTabRSV.queryCmsFloorTabPage(cmsFloorTabReqDTO);
            if(floorTabPageInfo != null){
                floorTabRespList = floorTabPageInfo.getResult();
            }
        }
        return floorTabRespList;
    }
    
    /** 
     * qryFloorGdsList:(根据楼层ID或者页签ID获取商品). <br/> 
     * 
     * @param reqVO
     * @param cmsFloorGdsReqDTO
     * @param standard
     * @return 
     * @since JDK 1.6 
     */ 
    private List<GdsInfoDetailRespDTO> qryFloorGdsList(ComponentReqVO reqVO,CmsFloorGdsReqDTO cmsFloorGdsReqDTO,String standard) throws Exception{
        // 状态
        if (StringUtil.isNotEmpty(reqVO.getStatus())) {
            cmsFloorGdsReqDTO.setStatus(reqVO.getStatus());
        }else {// 默认有效
            cmsFloorGdsReqDTO.setStatus(CmsConstants.ParamStatus.CMS_PARAMSTATUS_1);
        }
        // 楼层商品分页
        if (StringUtil.isNotEmpty(reqVO.getGdsSize())) {
            cmsFloorGdsReqDTO.setPageNo(1);
            cmsFloorGdsReqDTO.setPageSize(reqVO.getGdsSize());
        }
        // 3. 根据楼层或者页签ID获取商品
        List<CmsFloorGdsRespDTO> respList = new ArrayList<CmsFloorGdsRespDTO>();
        if(cmsFloorGdsReqDTO.getPageSize() != null && cmsFloorGdsReqDTO.getPageSize() != 0){
            //商品下架或删除商城页面自动不再显示
            PageResponseDTO<CmsFloorGdsRespDTO> pageInfo = null;
            for(int size = cmsFloorGdsReqDTO.getPageSize(),respSize = respList.size();respSize < size ;respSize = respList.size()){
                pageInfo = cmsFloorGdsRSV.queryCmsFloorGdsPage(cmsFloorGdsReqDTO);
                if(pageInfo != null && !CollectionUtils.isEmpty(pageInfo.getResult())){
                    for(CmsFloorGdsRespDTO dto : pageInfo.getResult()){
                        GdsInfoReqDTO gdsInfoReqDTO = new GdsInfoReqDTO();
                        GdsQueryOption[] gdsOptions = new GdsQueryOption[] {GdsQueryOption.BASIC};
                        gdsInfoReqDTO.setGdsQueryOptions(gdsOptions);
                        gdsInfoReqDTO.setId(dto.getGdsId());
                        GdsInfoDetailRespDTO gdsInfoDetailRespDTO = gdsInfoQueryRSV.queryGdsInfoDetail(gdsInfoReqDTO);
                        if(gdsInfoDetailRespDTO !=null && "11".equalsIgnoreCase(gdsInfoDetailRespDTO.getGdsStatus())){
                            respList.add(dto);
                        }
                        if(respList.size() >= size){
                            break;
                        }
                    }
                }else{
                    break;
                }
                
              //取下一页
              cmsFloorGdsReqDTO.setPageNo(cmsFloorGdsReqDTO.getPageNo()+1);
            }
        }
        // 3.1 商品域  查询商品信息（单个）并根据页面要求处理商品
        List<GdsInfoDetailRespDTO> gdsInfoDetailRespList = this.qryGdsDetailList(respList,standard);
        
        return gdsInfoDetailRespList;
    }
    
    /** 
     * qryGdsDetailList:(将商品ID列表转成商品详情列表). <br/> 
     * 
     * @param respList  商品ID列表
     * @param standard  商品图片规格
     * @return
     * @throws Exception 
     * @since JDK 1.6 
     */ 
    private List<GdsInfoDetailRespDTO> qryGdsDetailList(List<CmsFloorGdsRespDTO> respList,String standard) throws Exception{
        List<GdsInfoDetailRespDTO> gdsInfoDetailRespList = null;
        if (!CollectionUtils.isEmpty(respList)) {
            gdsInfoDetailRespList = new ArrayList<GdsInfoDetailRespDTO>();
            for (CmsFloorGdsRespDTO dto : respList) {
                if (dto != null && StringUtil.isNotEmpty(dto.getGdsId())) {
                    GdsInfoReqDTO gdsInfoReqDTO = new GdsInfoReqDTO();
                    GdsQueryOption[] gdsOptions = new GdsQueryOption[] { 
                            GdsQueryOption.BASIC, GdsQueryOption.MAINPIC 
                            };
                    SkuQueryOption[] skuOptions = new SkuQueryOption[] { 
                            SkuQueryOption.BASIC, SkuQueryOption.PROP,SkuQueryOption.CAlDISCOUNT
                            };
                    //设置需要的单品属性Id
                    List<Long> propIds = new ArrayList<Long>();
                    propIds.add(1020l);//内容简介
                    gdsInfoReqDTO.setPropIds(propIds);
                    gdsInfoReqDTO.setGdsQueryOptions(gdsOptions);
                    gdsInfoReqDTO.setSkuQuerys(skuOptions);
                    gdsInfoReqDTO.setId(dto.getGdsId());
                    GdsInfoDetailRespDTO gdsInfoDetailRespDTO = gdsInfoQueryRSV.queryGdsInfoDetail(gdsInfoReqDTO);
                    if (gdsInfoDetailRespDTO != null) {
                        // 3.1.1 商品图片
                        GdsMediaRespDTO gdsMediaRespDTO = gdsInfoDetailRespDTO.getMainPic();
                        if(gdsMediaRespDTO == null){
                            gdsMediaRespDTO = new GdsMediaRespDTO();
                            gdsInfoDetailRespDTO.setMainPic(gdsMediaRespDTO);
                        }
                        gdsMediaRespDTO.setURL(ParamsTool.getImageUrl(gdsMediaRespDTO.getMediaUuid(), standard));

                        // 根据ID读取内容简介内容
                        if(gdsInfoDetailRespDTO != null 
                                && gdsInfoDetailRespDTO.getSkuInfo() != null
                                && gdsInfoDetailRespDTO.getSkuInfo().getAllPropMaps() != null
                                && gdsInfoDetailRespDTO.getSkuInfo().getAllPropMaps().get("1020") != null
                                && gdsInfoDetailRespDTO.getSkuInfo().getAllPropMaps().get("1020").getValues()!=null
                                && gdsInfoDetailRespDTO.getSkuInfo().getAllPropMaps().get("1020").getValues().get(0)!=null){
                            byte[] editRecm = FileUtil.readFile(gdsInfoDetailRespDTO.getSkuInfo().getAllPropMaps().get("1020").getValues().get(0).getPropValue());
                                if (ArrayUtils.isNotEmpty(editRecm)) {
                                    String gdsEditRecm = Jsoup.parse(new String(editRecm)).text();
                                    if (gdsEditRecm.length() > GDSDESCCONTENT_LENGTH) {
                                        gdsEditRecm = gdsEditRecm.substring(0, GDSDESCCONTENT_LENGTH) + "...";
                                    }
                                    gdsInfoDetailRespDTO.getSkuInfo().getAllPropMaps().get("1020").getValues().get(0).setPropValue(gdsEditRecm);
                                }
                        } 
                        //拼接商品的详情地址
                        CmsSiteRespDTO siteDto = CmsCacheUtil.getCmsSiteCache(1l);
                        gdsInfoDetailRespDTO.setUrl(siteDto.getSiteUrl()+gdsInfoDetailRespDTO.getUrl());
                        
                        gdsInfoDetailRespList.add(gdsInfoDetailRespDTO);
                    }
                }
            }
        }
        return gdsInfoDetailRespList;
    }
    
    /** 
     * queryGdsByTabId:(根据页签ID获取页签下的商品). <br/> 
     * 
     * @param model
     * @param reqVO
     * @return
     * @throws Exception 
     * @since JDK 1.6 
     */ 
    @RequestMapping(value = "/queryGdsByTabId")
    @ResponseBody
    public Map<String,Object> queryGdsByTabId(Model model,ComponentReqVO reqVO) throws Exception {
        LogUtil.info(MODULE, "==========reqVO:" + reqVO.toString() + ";");
        
        Map<String,Object> resultMap = new HashMap<String,Object>();
        List<GdsInfoDetailRespDTO> gdsInfoDetailRespList = null;
        
        //获取内容位置信息
        CmsPlaceRespDTO placeInfo = this.getPlaceInfoByID(reqVO.getPlaceId());
        
        // 1. 判断入参
        CmsFloorGdsReqDTO cmsFloorGdsReqDTO = new CmsFloorGdsReqDTO();
        if (StringUtil.isNotEmpty(reqVO.getTabId())) {//页签
            cmsFloorGdsReqDTO.setTabId(reqVO.getTabId());
        }
        if (StringUtil.isNotEmpty(reqVO.getStatus())) {//状态
            cmsFloorGdsReqDTO.setStatus(reqVO.getStatus());
        }
        if (StringUtil.isNotEmpty(reqVO.getGdsSize())) {//楼层商品数量
            cmsFloorGdsReqDTO.setPageNo(1);
            cmsFloorGdsReqDTO.setPageSize(reqVO.getGdsSize());
        }
        String standard = "";//规格
        if(StringUtil.isNotEmpty(reqVO.getPlaceWidth()) && StringUtil.isNotEmpty(reqVO.getPlaceHeight())){
            standard = reqVO.getPlaceWidth() + "x" + reqVO.getPlaceHeight() + "!";
        }
        try{
            gdsInfoDetailRespList = this.qryFloorGdsList(reqVO, cmsFloorGdsReqDTO, standard);
        }catch(Exception err){
            LogUtil.error(MODULE, "查询查询商品出现异常:",err);
        }
        
        resultMap.put("gdsList", gdsInfoDetailRespList);
        if(placeInfo !=null){
            resultMap.put("siteId", placeInfo.getSiteId());
        }
        return resultMap;
    }
    
    /**
     * 
     * qryChannelIds:(根据内容位置id找栏目id 用于对关于我们二级页进行展示类型初始化). <br/> 
     * 
     * @since JDK 1.6
     */
    @RequestMapping(value = "/qryChannelIds")
    @ResponseBody
    public List<Long> qryChannelIds (String placeIds){
        List<Long> channelIds = new ArrayList<Long>();
        if(StringUtil.isNotBlank(placeIds)){
            String[] placeList = placeIds.split(",");
            if(placeList!=null && placeList.length > 0){
                for(String s : placeList){
                    if(this.isNumeric(s)){
                        Long l = Long.parseLong(s);
                        // 1.根据内容位置ID查询内容位置与栏目关系
                        CmsPlaceChannelReqDTO placeChannelDto = new CmsPlaceChannelReqDTO();
                        List<CmsPlaceChannelRespDTO> placeChannelList =null;
                        try {
                            placeChannelDto.setPlaceId(l);
                            placeChannelDto.setStatus(CmsConstants.ParamStatus.CMS_PARAMSTATUS_1);
                            placeChannelList = cmsPlaceChannelRSV.queryCmsPlaceChannelList(placeChannelDto);
                        } catch (Exception e) {
                            placeChannelList = null;
                            LogUtil.error(MODULE, "根据位置查询栏目出现异常:", e);
                            
                        }
                        
                        //通过栏目Id找网站信息id
                        if(CollectionUtils.isNotEmpty(placeChannelList)){
                            for(CmsPlaceChannelRespDTO placeChannel : placeChannelList){
                                channelIds.add(placeChannel.getChannelId());
                            }
                        }
                    }
                }
            }
        }
        return channelIds;
    }
    
    /**
     * 
     * getPlaceInfoByID:(根据内容位置id获取内容位置). <br/> 
     * 
     * @param id
     * @return 
     * @since JDK 1.6
     */
    private CmsPlaceRespDTO getPlaceInfoByID (Long id){
        CmsPlaceRespDTO placeInfo = null;
        if(StringUtil.isNotEmpty(id)){
            CmsPlaceReqDTO reqDto = new CmsPlaceReqDTO();
            reqDto.setId(id);
            reqDto.setStatus(CmsConstants.ParamStatus.CMS_PARAMSTATUS_1);
            placeInfo = this.cmsPlaceRSV.queryCmsPlace(reqDto);
            if(placeInfo!=null && (StringUtil.isEmpty(placeInfo.getId()) || !CmsConstants.ParamStatus.CMS_PARAMSTATUS_1.equals(placeInfo.getStatus()))){
                placeInfo =null;
            }
        }
        
        return placeInfo;
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
