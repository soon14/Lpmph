package com.ai.ecp.busi.rwzw.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.ArrayUtils;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.ecp.busi.rwzw.vo.ComponentReqVO;
import com.ai.ecp.busi.rwzw.vo.SiteInfoReqVO;
import com.ai.ecp.cms.dubbo.dto.CmsAdvertiseRespDTO;
import com.ai.ecp.cms.dubbo.dto.CmsArticleReqDTO;
import com.ai.ecp.cms.dubbo.dto.CmsArticleRespDTO;
import com.ai.ecp.cms.dubbo.dto.CmsChannelReqDTO;
import com.ai.ecp.cms.dubbo.dto.CmsChannelResDTO;
import com.ai.ecp.cms.dubbo.dto.CmsImageSwitcherReqDTO;
import com.ai.ecp.cms.dubbo.dto.CmsImageSwitcherRespDTO;
import com.ai.ecp.cms.dubbo.dto.CmsPlaceChannelReqDTO;
import com.ai.ecp.cms.dubbo.dto.CmsPlaceChannelRespDTO;
import com.ai.ecp.cms.dubbo.dto.CmsSiteInfoReqDTO;
import com.ai.ecp.cms.dubbo.dto.CmsSiteInfoRespDTO;
import com.ai.ecp.cms.dubbo.dto.CmsSiteRespDTO;
import com.ai.ecp.cms.dubbo.interfaces.ICmsArticleRSV;
import com.ai.ecp.cms.dubbo.interfaces.ICmsChannelRSV;
import com.ai.ecp.cms.dubbo.interfaces.ICmsImageSwitcherRSV;
import com.ai.ecp.cms.dubbo.interfaces.ICmsPlaceChannelRSV;
import com.ai.ecp.cms.dubbo.interfaces.ICmsSiteInfoRSV;
import com.ai.ecp.cms.dubbo.util.CmsCacheUtil;
import com.ai.ecp.cms.dubbo.util.CmsConstants;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.system.util.ParamsTool;
import com.ai.paas.utils.DateUtil;
import com.ai.paas.utils.FileUtil;
import com.ai.paas.utils.ImageUtil;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.StringUtil;

/**
 * 
 * Project Name:pmph-web-official <br>
 * Description: <br>
 * Date:2015-11-17上午9:42:15 <br>
 * 
 * @version
 * @since JDK 1.6
 * 
 *        人卫智网主页面控制
 */
@Controller
@RequestMapping(value = "/")
public class MainPageController {
    private static String MODULE = MainPageController.class.getName();

    @Resource(name = "cmsArticleRSV")
    private ICmsArticleRSV cmsArticleRSV;

    @Resource(name = "cmsPlaceChannelRSV")
    private ICmsPlaceChannelRSV cmsPlaceChannelRSV;

    @Resource
    private ICmsChannelRSV cmsChannelRSV;

    // 网站信息服务
    @Resource(name = "cmsSiteInfoRSV")
    private ICmsSiteInfoRSV cmsSiteInfoRSV;
    
    @Resource(name="cmsImageSwitcherRSV")
    private ICmsImageSwitcherRSV cmsImageSwitcherRSV;

    /**
     * 
     * education:(教育). <br/>
     * 
     * @return
     * @since JDK 1.6
     */
    @RequestMapping(value = "/edu")
    public String education(Model model) {
        model.addAttribute("title", "医学教育-人卫智网-人民卫生出版社");
        model.addAttribute("keywords", "医学教育网,医学教育,医学考试,医学素材库");
        model.addAttribute("description",
                "人民卫生出版社旗下人卫智网医学教育频道，拥有国内外权威专业医学教育资源，提供医学图书、医学教材、网络增值服务及医学教学素材库等医学教育资源");
        return "/rwzw/education/page-education";
    }

    /**
     * 
     * academic:(学术). <br/>
     * 
     * @return
     * @since JDK 1.7
     */
    @RequestMapping(value = "/sci")
    public String academic(Model model) {
        model.addAttribute("title", "医学学术-人卫智网-人民卫生出版社");
        model.addAttribute("keywords", "医学学术,学术专著,医学参考书,人卫书城,临床知识库");
        model.addAttribute("description",
                "人民卫生出版社旗下人卫智网医学学术频道，提供学术专著、参考书增值服务、在线参考书、电子书城与临床知识库等医学学术资源");
        return "/rwzw/academic/academic";
    }

    /**
     * 
     * examination:(考试). <br/>
     * 
     * @return
     * @since JDK 1.7
     */
    @RequestMapping(value = "/exam")
    public String examination(Model model) {
        model.addAttribute("title", "人卫智网-人卫医学网-国家医学考试网-医学教育考试辅导");
        model.addAttribute("keywords", "人卫智网,人卫医学网,国家医学考试网,医学教育网,卫人网,医学考试辅导培训");
        model.addAttribute("description",
                "人卫智网在线考试培训是人民卫生出版社旗下医学考试辅导平台,提供：护士执业、主管护师、初级护师、临床医师、临床助理医师、执业药师等医学考试辅导");
        return "/rwzw/examination/examination";
    }

    /**
     * 
     * healthy:(健康). <br/>
     * 
     * @return
     * @since JDK 1.6
     */
    @RequestMapping(value = "/health")
    public String healthy(Model model) {
        model.addAttribute("title", "医学健康-人卫智网");
        return "/rwzw/healthy/healthy";
    }

    /**
     * 
     * wechat:(微信汇). <br/>
     * 
     * @return
     * @since JDK 1.6
     */
    @RequestMapping(value = "/wechat")
    public String wechat(Model model) {
        model.addAttribute("title", "人卫微信汇-人卫智网");
        return "/rwzw/wechat/wechat";
    }

    /**
     * 
     * periodical:(期刊). <br/>
     * 
     * @return
     * @since JDK 1.6
     */
    @RequestMapping(value = "/journal")
    public String periodical(Model model) {
        model.addAttribute("title", "医学期刊-人卫智网");
        return "/rwzw/journals/journals";
    }

    /**
     * 
     * periodical:(期刊). <br/>
     * 
     * @return
     * @since JDK 1.6
     */
    @RequestMapping(value = "/journals-electronic")
    public String electronic(Model model) {
        model.addAttribute("title", "医学期刊-人卫智网");
        return "/rwzw/journals/journals-electronic/journals-electronic";
    }

    /**
     * 
     * app:(app花园). <br/>
     * 
     * @return
     * @since JDK 1.7
     */
    @RequestMapping(value = "/app")
    public String appGarden(Model model) {
        model.addAttribute("title", "人卫APP应用-人卫客户端下载-人卫智网");
        return "/rwzw/app/app-garden";
    }

    /**
     * 
     * siteNav:(网站导航). <br/>
     * 
     * @return
     * @since JDK 1.7
     */
    @RequestMapping(value = "/site-nav")
    public String siteNav(Model model, @RequestParam(required = false)
    String item) {
        model.addAttribute("title", "网站导航-人卫智网");
        // 默认第一个选项卡
        model.addAttribute("item", "0");
        if (StringUtil.isNotBlank(item)) {
            model.addAttribute("item", item);
        }
        return "/rwzw/sitenav/page-sitenav";
    }

    /**
     * 
     * siteNav:(人卫大学). <br/>
     * 
     * @return
     * @since JDK 1.7
     */
    @RequestMapping(value = "/university")
    public String university(Model model) {
        return "/rwzw/university/university";
    }
    
/*    @RequestMapping(value = "/journalslist")
    public String journalslist(Model model){
        model.addAttribute("title", "医学期刊-人卫智网");
        
        return"rwzw/journals/journals-list";
        
    }*/
    
    /**
     * 
     * about:(这里用一句话描述这个方法的作用). <br/> 
     * 
     * @param model
     * @return 
     * @since JDK 1.7
     */
    @RequestMapping(value = "/about")
    public String about(Model model,ComponentReqVO reqVo){
        model.addAttribute("staticHtml",ImageUtil.getStaticDocUrl(reqVo.getStaticId(), "html"));
        CmsArticleReqDTO cmsArticleReqDTO = new CmsArticleReqDTO();
        cmsArticleReqDTO.setId(Long.parseLong(reqVo.getArticleId()));
        cmsArticleReqDTO.setStatus(CmsConstants.ParamStatus.CMS_PARAMSTATUS_1);
        CmsArticleRespDTO cmsArticleRespDTO = cmsArticleRSV.queryCmsArticle(cmsArticleReqDTO);
        model.addAttribute("article", cmsArticleRespDTO);
        model.addAttribute("placeId", reqVo.getPlaceId());
        return "rwzw/journals/child/journals-text";
    };
    

    @RequestMapping(value = "qryartlist4com")
    public String qryArtList4Com(Model model, ComponentReqVO reqVo) {
        model.addAttribute("placeId", reqVo.getPlaceId());
        // 1.声明必须的变量
        String url = "/rwgw/main/componentVM/article-list-com-";// VM基础地址
        PageResponseDTO<CmsArticleRespDTO> articles = null;// 文章列表容器
        CmsArticleReqDTO reqDto = new CmsArticleReqDTO();// 查询条件对象
        List<CmsPlaceChannelRespDTO> placeChannelList = null;// 位置栏目关系对象
        CmsPlaceChannelReqDTO placeChannelDto = new CmsPlaceChannelReqDTO();// 位置栏目查询条件对象
        CmsChannelResDTO channel = null;// 栏目信息
        List<CmsChannelResDTO> channelList = null;// 栏目列表容器
        CmsChannelReqDTO channelReqDto = new CmsChannelReqDTO();// 栏目查询条件
        String standard = "";// 图片规则

        // 根据类型 确定vm地址
        if (StringUtil.isNotBlank(reqVo.getMenuType())) {
            url += reqVo.getMenuType();
        } else {
            url += "2";
        }

        // 2.初始化查询条件
        try {
            if (reqVo.getPlaceId() != null && StringUtil.isNotBlank(reqVo.getPlaceId().toString())) {
                // 根据位置查询栏目
                placeChannelDto.setPlaceId(reqVo.getPlaceId());
                placeChannelDto.setStatus(CmsConstants.ParamStatus.CMS_PARAMSTATUS_1);
                placeChannelList = cmsPlaceChannelRSV.queryCmsPlaceChannelList(placeChannelDto);
            } else {
                return url;
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            LogUtil.error(MODULE, "根据位置查询栏目出现异常:", e);
            return url;
        }
        CmsPlaceChannelRespDTO placeChannel = null;
        if (!CollectionUtils.isEmpty(placeChannelList)) {
            placeChannel = placeChannelList.get(0);
        }
        // 设置栏目
        // reqDto.setChannelId(32l);
        if (placeChannel != null && placeChannel.getChannelId() != null
                && StringUtil.isNotBlank(placeChannel.getChannelId().toString())) {
            reqDto.setChannelId(placeChannel.getChannelId());
            channelReqDto.setId(placeChannel.getChannelId());
            channelReqDto.setStatus(CmsConstants.ParamStatus.CMS_PARAMSTATUS_1);
            // 根据条件（栏目id对应 且为有效状态）
            channelList = cmsChannelRSV.listChannel(channelReqDto);
            // 获取对应栏目
            if (!CollectionUtils.isEmpty(channelList)) {
                channel = channelList.get(0);
            } else {
                channel = null;
            }
        } else {// 没有关联栏目
            model.addAttribute("errorMessage", "暂未关联栏目！");
            return url;
        }

        if (channel != null && channel.getId() != null) {// 关联了有效栏目
            // 设置状态
            if (StringUtil.isNotBlank(reqVo.getStatus())) {
                reqDto.setStatus(reqVo.getStatus());
            } else {// 默认有效
                reqDto.setStatus(CmsConstants.ParamStatus.CMS_PARAMSTATUS_1);
            }

            // 设置当前时间
            reqDto.setThisTime(DateUtil.getSysDate());
            // reqDto.setThisTime(DateUtil.getTheDayFirstSecond(DateUtil.getSysDate()));

            // 设置查询条数
            if (StringUtil.isNotEmpty(reqVo.getPlaceSize())) {
                reqDto.setPageNo(1);
                reqDto.setPageSize(reqVo.getPlaceSize());
            } else {// 默认3条
                reqDto.setPageNo(1);
                reqDto.setPageSize(3);
            }
            // 3.0 查询文章列表信息
            try {
                articles = cmsArticleRSV.queryCmsArticlePage(reqDto);
                // 获取文章图片
                // 设置图片规则
                if (StringUtil.isNotEmpty(reqVo.getPlaceWidth())
                        && StringUtil.isNotEmpty(reqVo.getPlaceHeight())) {
                    standard = reqVo.getPlaceWidth() + "x" + reqVo.getPlaceHeight() + "!";
                }
                if (articles != null && !CollectionUtils.isEmpty(articles.getResult())) {
                    for (CmsArticleRespDTO respDto : articles.getResult()) {
                        respDto.setThumbnailUrl(ParamsTool.getImageUrl(respDto.getThumbnail(),
                                standard));
                    }
                }
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                LogUtil.error(MODULE, "根据栏目查询文章列表异常:", e);
                return url;
            }
        } else {// 关联无效栏目
            model.addAttribute("errorMessage", "关联栏目已失效,请重新关联！");
            return url;
        }
        if (articles != null) {
            model.addAttribute("articles", articles.getResult());
        } else {
            model.addAttribute("articles", null);
        }
        model.addAttribute("channel", channel);
        return url;
    }

    /**
     * 
     * journalslist:(文章页). <br/>
     * 
     * @param model
     * @param reqVO
     * @return
     * @since JDK 1.7
     */
    @RequestMapping(value = "/journalsarticle")
    public String journalsarticle(Model model, ComponentReqVO reqVO) {
        // 1 根据网站信息ID获取记录
        String url = "/rwzw/journals/journals-list/journals-list";
        CmsArticleRespDTO cmsArticleRespDTO = null;
        String staticHtml = "";
        try {
            if (reqVO != null) {
                String articleId = reqVO.getArticleId();
                if (StringUtil.isNotEmpty(articleId)) {
                    CmsArticleReqDTO cmsArticleReqDTO = new CmsArticleReqDTO();
                    cmsArticleReqDTO.setId(Long.parseLong(articleId));
                    cmsArticleRespDTO = cmsArticleRSV.queryCmsArticle(cmsArticleReqDTO);
                    if (StringUtil.isNotEmpty(cmsArticleRespDTO.getStaticId())) {
                        byte[] content = FileUtil.readFile(reqVO.getStaticId());
                        if (ArrayUtils.isNotEmpty(content)) {
                            staticHtml = Jsoup.parse(new String(content)).text();
                        }
                        model.addAttribute("staticHtml", staticHtml);
                    }
                }
            }
        } catch (Exception err) {
            LogUtil.error(MODULE, "查询文章出现异常:", err);
        }

        model.addAttribute("respVO", cmsArticleRespDTO);
        return url;
    }

    @RequestMapping(value = "/journalslist")
    public String journalslist(Model model){
        return "/rwzw/journals/journals-list";
    }
    
    @RequestMapping(value = "/adlist")
    public String adlist(Model model){
        return "/rwzw/journals/advertiser/advertiser-list";
    }
    
    
     /**
     * 
     * journalslist:(文章列表页). <br/>
     * 
     * @param model
     * @param reqVO
     * @return
     * @since JDK 1.7
     */
    @RequestMapping(value = "/getjournalslist")
    public String getjournalslist(Model model, ComponentReqVO reqVO) throws Exception {
        // 根据位置查询栏目ID
        CmsPlaceChannelReqDTO placeChannelDto = new CmsPlaceChannelReqDTO();// 位置栏目查询条件对象
        placeChannelDto.setPlaceId(reqVO.getPlaceId());
        List<CmsPlaceChannelRespDTO> placeChannelList = cmsPlaceChannelRSV
                .queryCmsPlaceChannelList(placeChannelDto);
        String url = "/rwzw/journals/list/list";   
        CmsPlaceChannelRespDTO cmsPlaceChannelRespDTO = new CmsPlaceChannelRespDTO();
        if (!CollectionUtils.isEmpty(placeChannelList)) {
            cmsPlaceChannelRespDTO = placeChannelList.get(0);
        }else{
        	  PageResponseDTO<CmsArticleRespDTO> respDTO = new PageResponseDTO<CmsArticleRespDTO>();
              model.addAttribute("respVO", respDTO);
              model.addAttribute("placeId", reqVO.getPlaceId());
              return url;
        }
        CmsChannelReqDTO cmsChannelReqDTO = new CmsChannelReqDTO();
        cmsChannelReqDTO.setId(cmsPlaceChannelRespDTO.getChannelId());
        cmsChannelReqDTO.setStatus(CmsConstants.ParamStatus.CMS_PARAMSTATUS_1);
        cmsChannelReqDTO.setIsresiteinfo("1");
        CmsChannelResDTO cmsChannelResDTO = cmsChannelRSV.find(cmsChannelReqDTO);
     
        if(StringUtil.isEmpty(cmsChannelResDTO)){
            PageResponseDTO<CmsArticleRespDTO> respDTO = new PageResponseDTO<CmsArticleRespDTO>();
            model.addAttribute("respVO", respDTO);
            model.addAttribute("placeId", reqVO.getPlaceId());
            return url;
        }
        
        CmsSiteInfoReqDTO cmsSiteInfoReqDTO = new CmsSiteInfoReqDTO();
        cmsSiteInfoReqDTO.setId(cmsChannelResDTO.getSiteinfoId());
        cmsSiteInfoReqDTO.setStatus(CmsConstants.ParamStatus.CMS_PARAMSTATUS_1);
        cmsSiteInfoReqDTO.setSiteInfoType(CmsConstants.SiteInfoType.CMS_SITE_INFO_TYPE_02);
        List<CmsSiteInfoRespDTO> list = cmsSiteInfoRSV.queryCmsSiteInfoList(cmsSiteInfoReqDTO);
        
        if(CollectionUtils.isEmpty(list)){
            PageResponseDTO<CmsArticleRespDTO> respDTO = new PageResponseDTO<CmsArticleRespDTO>();
            model.addAttribute("respVO", respDTO);
            model.addAttribute("placeId", reqVO.getPlaceId());
            return url;
        }
        
        CmsArticleRespDTO cmsArticleRespDTO = null;
        CmsArticleReqDTO cmsArticleReqDTO = new CmsArticleReqDTO();
        cmsArticleReqDTO.setChannelId(cmsPlaceChannelRespDTO.getChannelId());
        cmsArticleReqDTO.setStatus("1");
        cmsArticleReqDTO.setPageNo(reqVO.getPageNumber());
        cmsArticleReqDTO.setPageSize(reqVO.getPageSize());
        PageResponseDTO<CmsArticleRespDTO> respDTO = cmsArticleRSV
                .queryCmsArticlePage(cmsArticleReqDTO);
        model.addAttribute("respVO", respDTO);
        model.addAttribute("placeId", reqVO.getPlaceId());
        return url;
    }

    /**
     * 
     * qryChannel:(查询栏目). <br/>
     * TODO(这里描述这个方法适用条件 – 可选).<br/>
     * TODO(这里描述这个方法的执行流程 – 可选).<br/>
     * TODO(这里描述这个方法的使用方法 – 可选).<br/>
     * TODO(这里描述这个方法的注意事项 – 可选).<br/>
     * 2015.11.21
     * 
     * @return VM
     * @since JDK 1.6
     */
    @RequestMapping(value = "qrychannel")
    public String qryChannel(Model model, ComponentReqVO reqVo) {
        LogUtil.info(MODULE, "==========reqVO :" + reqVo.toString() + ";");
        // 1.声明必须的变量
        String url = null;
        if (reqVo.getType() != null && reqVo.getType().equalsIgnoreCase("1"))
            url = "/rwzw/main/componentVM/channel-nav";// VM地址
        else if (reqVo.getType() != null && reqVo.getType().equalsIgnoreCase("2")) {
            url = "/rwzw/main/componentVM/channel-nav-phone";// VM地址
        }
        CmsChannelReqDTO reqDto = new CmsChannelReqDTO();// 查询条件
        List<CmsChannelResDTO> channelsList = null;// 栏目数据
        Long channelParentCode = 0l;// 0代表根节点

        // 2.用VO初始化查询条件
        // 设置站点
        if (reqVo.getSiteId() != null && StringUtil.isNotBlank(reqVo.getSiteId().toString())) {
            reqDto.setSiteId(reqVo.getSiteId());
        } else {
            return url;
        }
        // 设置状态
        if (StringUtil.isNotBlank(reqVo.getStatus())) {
            reqDto.setStatus(reqVo.getStatus());
        } else {// 默认有效
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

        // 3.查询栏目
        try {
            // 3.1 查询一级栏目
            reqDto.setChannelParent(channelParentCode);
            channelsList = cmsChannelRSV.listChannel(reqDto);
            // 3.2 查询二级栏目
            if (!CollectionUtils.isEmpty(channelsList)) {
                for (CmsChannelResDTO dto : channelsList) {
                    if (dto != null && dto.getId() != null
                            && StringUtil.isNotBlank(dto.getId().toString()))
                        reqDto.setChannelParent(dto.getId());
                    dto.setChildList(cmsChannelRSV.listChannel(reqDto));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.info(MODULE, "查询栏目失败");
        }
        model.addAttribute("channelsList", channelsList);
        return url;
    }

    /**
     * 
     * getSiteInfo:(获取网站信息). <br/>
     * TODO(这里描述这个方法适用条件 – 可选).<br/>
     * TODO(这里描述这个方法的执行流程 – 可选).<br/>
     * TODO(这里描述这个方法的使用方法 – 可选).<br/>
     * TODO(这里描述这个方法的注意事项 – 可选).<br/>
     * 
     * @param model
     * @param reqVO
     *            查询条件
     * @return
     * @since JDK 1.6
     */
    private List<CmsSiteInfoRespDTO> getSiteInfo(SiteInfoReqVO reqVO) {
        LogUtil.info(MODULE, "==========reqVO:" + reqVO.toString() + ";");

        List<CmsSiteInfoRespDTO> respList = null;
        CmsSiteInfoReqDTO reqDTO = new CmsSiteInfoReqDTO();// 网站信息查询条件
        // 1. 判断并设置入参
        if (StringUtil.isNotEmpty(reqVO.getSiteId())) {// 站点
            reqDTO.setSiteId(reqVO.getSiteId());
        } else {// 没有设置站点 则直接返回
            return respList;
        }

        if (StringUtil.isNotEmpty(reqVO.getStatus())) {// 状态
            reqDTO.setStatus(reqVO.getStatus());
        } else {
            reqDTO.setStatus(CmsConstants.ParamStatus.CMS_PARAMSTATUS_1);
        }

        if (StringUtil.isNotEmpty(reqVO.getSiteInfoType())) {// 网站信息类型
            reqDTO.setSiteInfoType(reqVO.getSiteInfoType());
        }

        // 2. 调用服务，无分页
        try {
            respList = cmsSiteInfoRSV.queryCmsSiteInfoList(reqDTO);
        } catch (Exception err) {
            LogUtil.error(MODULE, "getSiteInfo,查询网站信息列表出现异常:", err);
        }
        // 3. 返回结果
        return respList;
    }

    /**
     * 
     * qrySiteInfo:(查询网站信息列表). <br/>
     * TODO(这里描述这个方法适用条件 – 可选).<br/>
     * TODO(这里描述这个方法的执行流程 – 可选).<br/>
     * TODO(这里描述这个方法的使用方法 – 可选).<br/>
     * TODO(这里描述这个方法的注意事项 – 可选).<br/>
     * 2015.12.30
     * 
     * @since JDK 1.6
     */
    @RequestMapping(value = "/qrySiteInfo")
    public String qrySiteInfo(Model model, SiteInfoReqVO reqVO) {
        LogUtil.info(MODULE, "==========reqVO:" + reqVO.toString() + ";");
        // 1.检查参数并设置参数
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<CmsSiteInfoRespDTO> respList = null;
        String url = reqVO.getUrl();// 组件指定返回的vm 如果没有 则返回空

        // 2.查询数据
        if (StringUtil.isNotBlank(url) && StringUtil.isNotEmpty(reqVO.getSiteId())) {
            try {
                respList = this.getSiteInfo(reqVO);
                if (CollectionUtils.isEmpty(respList)) {
                    respList = new ArrayList<CmsSiteInfoRespDTO>();
                } else {
                    // 2.1 获取静态文件地址
                    for (CmsSiteInfoRespDTO siteinfo : respList) {
                        if (StringUtil.isNotEmpty(siteinfo.getStaticId())) {
                            siteinfo.setStaticUrl(ImageUtil.getStaticDocUrl(siteinfo.getStaticId(),
                                    "html"));
                        }
                    }
                }
            } catch (Exception err) {
                LogUtil.error(MODULE, "qrySiteInfo,查询网站信息列表出现异常:", err);
                model.addAttribute("siteInfoList", new ArrayList<CmsSiteInfoRespDTO>());
            }
        } else {
            return null;
        }
        // 3.返回数据
        model.addAttribute("siteInfoList", respList);
        return url;
    }
    
    
    /**
     * qryLeafletList:(查询广告列表). <br/>
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
    @RequestMapping(value = "/qryImageSwList")
    @ResponseBody
    public Map<String,Object> qryImageSwList(Model model,ComponentReqVO reqVO){
        LogUtil.info(MODULE, "==========reqVO:" + reqVO.toString() + ";");
        
        Map<String,Object> resultMap = new HashMap<String,Object>();
        List<CmsImageSwitcherRespDTO> respList = null;
        
        try{
            // 1. 判断入参
            if(StringUtil.isNotEmpty(reqVO.getPlaceId())){//内容位置
                CmsImageSwitcherReqDTO reqDTO = new CmsImageSwitcherReqDTO();
                reqDTO.setPlaceId(reqVO.getPlaceId());
                if (StringUtil.isNotEmpty(reqVO.getStatus())) {//状态
                    reqDTO.setStatus(reqVO.getStatus());
                }else{
                    reqDTO.setStatus(CmsConstants.ParamStatus.CMS_PARAMSTATUS_1);
                }
                if (StringUtil.isNotEmpty(reqVO.getPlaceSize())) {//广告数量
                    reqDTO.setPageNo(0);
                    reqDTO.setPageSize(reqVO.getPlaceSize());
                }
                String standard = "";// 规格
                if (StringUtil.isNotEmpty(reqVO.getPlaceWidth()) && StringUtil.isNotEmpty(reqVO.getPlaceHeight())) {
                    standard = reqVO.getPlaceWidth() + "x" + reqVO.getPlaceHeight() + "!";
                }
           
                // 2. 调用广告服务，无分页
                respList = new ArrayList<CmsImageSwitcherRespDTO>();
                PageResponseDTO<CmsImageSwitcherRespDTO> pageInfo = cmsImageSwitcherRSV.queryCmsImageSwitcherPage(reqDTO);
                if (pageInfo != null) {
                    respList = pageInfo.getResult();
                }
                // 3. 调文件服务器，返回图片
                if (!CollectionUtils.isEmpty(respList)) {
                    for (CmsImageSwitcherRespDTO dto : respList) {
                        // 3.1调文件服务器，返回图片
                        if (StringUtil.isNotBlank(dto.getOnePic())) {
                            dto.setOnePicUrl(ParamsTool.getImageUrl(dto.getOnePic(),standard));
                        }
                        if (StringUtil.isNotBlank(dto.getTwicePic())) {
                            dto.setTwicePicUrl(ParamsTool.getImageUrl(dto.getTwicePic(),standard));
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
    
    
}
