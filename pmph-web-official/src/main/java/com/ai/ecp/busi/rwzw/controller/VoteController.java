package com.ai.ecp.busi.rwzw.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.ecp.busi.rwzw.vo.ArticleVO;
import com.ai.ecp.cms.dubbo.dto.CmsArticleReqDTO;
import com.ai.ecp.cms.dubbo.dto.CmsArticleRespDTO;
import com.ai.ecp.cms.dubbo.dto.CmsChannelReqDTO;
import com.ai.ecp.cms.dubbo.dto.CmsChannelResDTO;
import com.ai.ecp.cms.dubbo.interfaces.ICmsArticleRSV;
import com.ai.ecp.cms.dubbo.interfaces.ICmsChannelRSV;
import com.ai.ecp.cms.dubbo.util.CmsConstants;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.paas.utils.DateUtil;
import com.ai.paas.utils.ImageUtil;
import com.ai.paas.utils.ObjectCopyUtil;
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
 *        人卫智网投票界面
 */
@Controller
@RequestMapping(value = "/vote")
public class VoteController {
    private static String MODULE = VoteController.class.getName();
	private String URL = "/rwzw/vote";
    @Resource
	private ICmsArticleRSV iCmsArticleRSV;
    @Resource
    private ICmsChannelRSV iCmsChannelRSV;
    /**
     * 
     * init:(页面初始化路劲). <br/>
     * 
     * @return
     * @since JDK 1.6
     */
    @RequestMapping("/{channelId}")
  	public String init(Model model,@PathVariable("channelId")Long channelId){
    	model.addAttribute("channelId", channelId);
    	List<ArticleVO> data=getList(channelId,1);
    	CmsChannelReqDTO cmsChannelReqDTO=new CmsChannelReqDTO();
    	cmsChannelReqDTO.setId(channelId);
    	CmsChannelResDTO channelresp=iCmsChannelRSV.find(cmsChannelReqDTO);
    	model.addAttribute("channelresp", channelresp);
    	model.addAttribute("respData", data);
    	model.addAttribute("showEwm", "no");
    	return URL+"/vote";
    }
    /**
     * 
     * voteList:(获取投票分页列表). <br/>
     * 
     * @return
     * @since JDK 1.6
     */
    @RequestMapping("/voteList")
    @ResponseBody
	public List<ArticleVO> voteList(Model model,Long channelId,Integer page){
    	List<ArticleVO> datas=getList(channelId,page);
    	return datas;
    }
    /**
     * 
     * education:(教育). <br/>
     * 
     * @return
     * @since JDK 1.6
     */
    @RequestMapping("/article")
	public String article(Model model,Long channelId,Long id){
    	CmsArticleReqDTO cmsArticleReqDTO =new CmsArticleReqDTO();
    	cmsArticleReqDTO.setChannelId(channelId);
    	cmsArticleReqDTO.setId(id);
    	CmsArticleRespDTO article=iCmsArticleRSV.queryCmsArticle(cmsArticleReqDTO);
    	ArticleVO vo=new ArticleVO();
    	ObjectCopyUtil.copyObjValue(article, vo, null, false);
    	if(StringUtil.isNotEmpty(vo.getStaticId())){
	    	vo.setStaticId(ImageUtil.getStaticDocUrl(vo.getStaticId(), "html"));
        }
    	vo.setBigQrCode(ImageUtil.getImageUrl(article.getQrCode()+ "_125x125!"));
    	model.addAttribute("articleResp", vo);
    	model.addAttribute("showEwm", "no");
    	return URL+"/article";
    }
    /**
     * 
     * getList:(获取投票列表分页方法). <br/>
     * 
     * @return
     * @since JDK 1.6
     */
    private List<ArticleVO> getList(long channelId,int page){
    	CmsArticleReqDTO cmsArticleReqDTO =new CmsArticleReqDTO();
    	cmsArticleReqDTO.setChannelId(channelId);
    	cmsArticleReqDTO.setStatus(CmsConstants.ParamStatus.CMS_PARAMSTATUS_1);
    	cmsArticleReqDTO.setThisTime(DateUtil.getSysDate());// 设置当前时间
    	cmsArticleReqDTO.setPageSize(10);
    	cmsArticleReqDTO.setPageNo(page);
    	PageResponseDTO<CmsArticleRespDTO>  pages=iCmsArticleRSV.queryCmsArticlePage(cmsArticleReqDTO);
    	List<CmsArticleRespDTO> datas=pages.getResult();
    	List<ArticleVO> articles=new ArrayList<ArticleVO>();
    	if(CollectionUtils.isNotEmpty(datas)){
    		for(CmsArticleRespDTO dto:datas){
    			ArticleVO vo=new ArticleVO();
    		    ObjectCopyUtil.copyObjValue(dto, vo, null, false);
    		    if(StringUtil.isNotEmpty(dto.getStaticId())){
    		    	vo.setStaticId(ImageUtil.getStaticDocUrl(dto.getStaticId(), "html"));
                }
    		    vo.setQrCode(ImageUtil.getImageUrl(dto.getQrCode()+ "_65x65!"));
    		    vo.setBigQrCode(ImageUtil.getImageUrl(dto.getQrCode()+ "_116x116!"));
    		    articles.add(vo);
    		}
    	}
    	return articles;
    }
    
}
