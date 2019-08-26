package com.ai.ecp.pmph.busi.goods.gdsdetail.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.ecp.base.controller.EcpBaseController;
import com.ai.ecp.base.vo.EcpBaseResponseVO;
import com.ai.ecp.busi.goods.gdsdetail.vo.GdsCommonAuthorVO;
import com.ai.ecp.busi.goods.gdsdetail.vo.GdsNetWorkVO;
import com.ai.ecp.busi.goods.gdsdetail.vo.GdsPdfVO;
import com.ai.ecp.busi.goods.gdsdetail.vo.GdsReadOnlineVO;
import com.ai.ecp.goods.dubbo.constants.GdsConstants;
import com.ai.ecp.goods.dubbo.dto.GdsCategoryReqDTO;
import com.ai.ecp.goods.dubbo.dto.GdsCategoryRespDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsSkuInfoRespDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfoidx.GdsInterfaceGdsGidxReqDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfoidx.GdsInterfaceGdsGidxRespDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfoidx.GdsSku2PropPropIdxReqDTO;
import com.ai.ecp.goods.dubbo.interfaces.IGdsBrowseHisRSV;
import com.ai.ecp.goods.dubbo.interfaces.IGdsCategoryRSV;
import com.ai.ecp.goods.dubbo.interfaces.IGdsCatgCustDiscRSV;
import com.ai.ecp.goods.dubbo.interfaces.IGdsCollectRSV;
import com.ai.ecp.goods.dubbo.interfaces.IGdsEvalRSV;
import com.ai.ecp.goods.dubbo.interfaces.IGdsEvalReplyRSV;
import com.ai.ecp.goods.dubbo.interfaces.IGdsInfoQueryRSV;
import com.ai.ecp.goods.dubbo.interfaces.IGdsInterfaceGdsRSV;
import com.ai.ecp.goods.dubbo.interfaces.IGdsPlatRecomRSV;
import com.ai.ecp.goods.dubbo.interfaces.IGdsSkuInfoQueryRSV;
import com.ai.ecp.order.dubbo.interfaces.IReportGoodPayedRSV;
import com.ai.ecp.pmph.aip.dubbo.dto.AipLMNetValueAddedDetail;
import com.ai.ecp.pmph.aip.dubbo.dto.AipLMNetValueAddedRequest;
import com.ai.ecp.pmph.aip.dubbo.dto.AipLMNetValueAddedResponse;
import com.ai.ecp.pmph.aip.dubbo.dto.AipLMTONetValueAddedResponse;
import com.ai.ecp.pmph.aip.dubbo.dto.AipZYAuthRequest;
import com.ai.ecp.pmph.aip.dubbo.dto.AipZYAuthResponse;
import com.ai.ecp.pmph.aip.dubbo.interfaces.IAipLMNetValueAddedRSV;
import com.ai.ecp.pmph.aip.dubbo.interfaces.IAipZYAuthRSV;
import com.ai.ecp.pmph.dubbo.util.PmphRealOriginalGdsCodeProcessor;
import com.ai.ecp.prom.dubbo.interfaces.IPromQueryRSV;
import com.ai.ecp.prom.dubbo.interfaces.IPromRSV;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.ecp.server.front.util.SysCfgUtil;
import com.ai.ecp.staff.dubbo.dto.CustInfoReqDTO;
import com.ai.ecp.staff.dubbo.interfaces.ICustInfoRSV;
import com.ai.ecp.staff.dubbo.interfaces.IShopCollectRSV;
import com.ai.ecp.staff.dubbo.interfaces.IShopInfoRSV;
import com.ai.ecp.staff.dubbo.interfaces.IShopManageRSV;
import com.ai.paas.utils.FileUtil;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.StringUtil;

/**
 * 
 * Project Name:ecp-web-manage <br>
 * Description: 人卫商城商品详情个性服务(目前包含试读授权与网络增值服务)<br>
 * Date:2015年9月18日上午10:51:56 <br>
 * 
 * @version
 * @since JDK 1.6
 */
@RequestMapping(value = "/pmph/custsrvs")
@Controller
public class GdsDetailPmphController extends EcpBaseController {
    private static String URL = "/goods/gdsdetail";

    private static String MODULE = GdsDetailPmphController.class.getName();

    private static int PAGE_SIZE = 5;

    private static int PAGE_SIZE_10 = 10;

    private static String KEY = "GDS_BROWSE_HIS";

    private static Long PROP_ID_1032 = 1032l;

    private static String WEB = "1";

    private static String GDS_E_BOOK_CAT_CODE = "1200";

    private static String GDS_PAPER_BOOK_CAT_CODE = "1115";

    private static String GDS_DIGITS_BOOK_CAT_CODE = "1201";
    
    private final static String ISLOGIN_VO_ATTR = "isLogin"; 

    @Resource
    private IGdsInfoQueryRSV iGdsInfoQueryRSV;

    @Resource
    private IShopInfoRSV iShopInfoRSV;

    @Resource
    private IPromRSV iPromRSV;

    @Resource
    private ICustInfoRSV iCustInfoRSV;

    @Resource
    private IShopManageRSV iShopManageRSV;

    @Resource
    private IGdsEvalRSV iGdsEvalRSV;

    @Resource
    private IGdsCategoryRSV igdsCategoryRSV;

    @Resource
    private IGdsSkuInfoQueryRSV iGdsSkuInfoQueryRSV;

    @Resource
    private IGdsCollectRSV iGdsCollectRSV;

    @Resource
    private IGdsBrowseHisRSV iGdsBrowseHisRSV;

    @Resource
    private IGdsPlatRecomRSV iGdsPlatRecomRSV;

    @Resource
    private IGdsEvalReplyRSV iGdsEvalReplyRSV;

    @Resource
    private IGdsCatgCustDiscRSV gdsCatgCustDiscRSV;

    @Resource
    private IAipLMNetValueAddedRSV iAipLMNetValueAddedRSV;

    @Resource
    private IAipZYAuthRSV iAipZYAuthRSV;

    @Resource
    private IGdsInterfaceGdsRSV iGdsInterfaceGdsRSV;

    @Resource
    private IPromQueryRSV promQueryRSV;
    
    @Resource
    private IShopCollectRSV iShopCollectRSV; 

    @Resource
    private IReportGoodPayedRSV iReportGoodPayedRSV;
	private final static String SUFFIX_IMAGE_SIZE = "_100x100!";

   /**
     * 
     * queryNetWordService:(获取网络增值服务). <br/>
     * TODO(这里描述这个方法的注意事项 – 可选).<br/>
     * 
     * @return
     * @since JDK 1.6
     */
    @RequestMapping(value = "/querynetwordservice")
    public String queryNetWordService(Model model, GdsNetWorkVO gdsNetWorkVO) {
        AipLMNetValueAddedRequest aipLMNetValueAddedRequest = new AipLMNetValueAddedRequest();
        if (StringUtil.isNotBlank(gdsNetWorkVO.getIsbn())) {
            aipLMNetValueAddedRequest.setIsbn(gdsNetWorkVO.getIsbn());
        }
        aipLMNetValueAddedRequest.setCnt(PAGE_SIZE_10);
        AipLMNetValueAddedResponse list = null;
        try {
            String GDS_NET_WORK_ADDRESS = SysCfgUtil.fetchSysCfg("GDS_NET_WORK_ADDRESS").getParaValue();
            aipLMNetValueAddedRequest.setReqUrl(GDS_NET_WORK_ADDRESS);
            list = iAipLMNetValueAddedRSV.requestResource(aipLMNetValueAddedRequest);
            if (list != null) {
                model.addAttribute("netWorkList", list.getResources());
            } else {
                model.addAttribute("netWorkList", new ArrayList<AipLMNetValueAddedDetail>());
            }
//            model.addAttribute("toMoreService", SysCfgUtil.fetchSysCfg("GDS_TO_NET_WORK_ADDRESS").getParaValue());
            // 获取增值服务详情页面的链接 点击更多
            String GDS_TO_NET_WORK_ADDRESS = SysCfgUtil.fetchSysCfg("GDS_TO_NET_WORK_ADDRESS").getParaValue();
            AipLMNetValueAddedRequest aipLMTONetValueAddedRequest = new AipLMNetValueAddedRequest();
            aipLMTONetValueAddedRequest.setIsbn(gdsNetWorkVO.getIsbn());
            aipLMTONetValueAddedRequest.setReqUrl(GDS_TO_NET_WORK_ADDRESS);
            AipLMTONetValueAddedResponse detailList = iAipLMNetValueAddedRSV.requestLink(aipLMTONetValueAddedRequest);
            if (detailList != null) {
                model.addAttribute("link", detailList.getLink());
            } else {
                model.addAttribute("link", null);
            }
            model.addAttribute("isbn", gdsNetWorkVO.getIsbn());
        } catch (BusinessException e) {
            LogUtil.error(MODULE, "获取网络增值服务错误！", e);
            model.addAttribute("netWorkList", new ArrayList<AipLMNetValueAddedDetail>());
        } catch (Exception e) {
            LogUtil.error(MODULE, "获取网络增值服务错误！", e);
            model.addAttribute("netWorkList", new ArrayList<AipLMNetValueAddedDetail>());
        }
        model.addAttribute("isbn", gdsNetWorkVO.getIsbn());
        return URL + "/list/gds-netWorkService-list";
    }

    /**
     * 
     * readOnline:(数字教材和电子书的授权阅读). <br/>
     * TODO(这里描述这个方法适用条件 – 可选).<br/>
     * 
     * @param gdsReadOnlineVO
     * @return
     * @since JDK 1.6
     */
    @RequestMapping(value = "/readonline")
    @ResponseBody
    public EcpBaseResponseVO readOnline(GdsReadOnlineVO gdsReadOnlineVO) {
        EcpBaseResponseVO vo = new EcpBaseResponseVO();
        CustInfoReqDTO custInfoReqDTO = new CustInfoReqDTO();
        AipZYAuthRequest aipZYAuthRequest = new AipZYAuthRequest();
        aipZYAuthRequest.setUserName(custInfoReqDTO.getStaff().getStaffCode());
        // 阅读方式 1:正式，2：试用
        aipZYAuthRequest.setReadType("2");
        AipZYAuthResponse result = null;
        try {
            GdsInterfaceGdsGidxReqDTO gdsInterfaceGdsGidxReqDTO = new GdsInterfaceGdsGidxReqDTO();
            gdsInterfaceGdsGidxReqDTO.setGdsId(gdsReadOnlineVO.getGdsId());
            // 08 是erp ,09 是泽云
            gdsInterfaceGdsGidxReqDTO.setOrigin("09");
            GdsInterfaceGdsGidxRespDTO gdsInterfaceGdsGidxRespDTO = iGdsInterfaceGdsRSV.queryGdsInterfaceGdsGidxByEcpGdsId(gdsInterfaceGdsGidxReqDTO, new PmphRealOriginalGdsCodeProcessor());
            if (gdsInterfaceGdsGidxRespDTO == null || gdsInterfaceGdsGidxRespDTO.getOriginGdsId() == null) {
                vo.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_FAILURE);
                vo.setResultMsg("亲，该商品暂时没有在线试读功能哦！");
                return vo;
            }
            aipZYAuthRequest.setGoodsId(gdsInterfaceGdsGidxRespDTO.getOriginGdsId());
            String GDS_BOOK_SHOU_QUAN_ADDRESS = SysCfgUtil.fetchSysCfg("GDS_BOOK_SHOU_QUAN_ADDRESS").getParaValue();
            aipZYAuthRequest.setAuthUrl(GDS_BOOK_SHOU_QUAN_ADDRESS);
            result = iAipZYAuthRSV.sendAuthRequest(aipZYAuthRequest);
            if (aipZYAuthRequest._ZVING_STATUS_OK.equals(result.getStatus())) {
                String GDS_DIGITS_BOOK_CAT_CODE = SysCfgUtil.fetchSysCfg("GDS_DIGITS_BOOK_CAT_CODE").getParaValue();
                String GDS_E_BOOK_CAT_CODE = SysCfgUtil.fetchSysCfg("GDS_E_BOOK_CAT_CODE").getParaValue();
                GdsCategoryReqDTO dto = new GdsCategoryReqDTO();
                dto.setCatgCode(gdsReadOnlineVO.getCatgCode());
                List<GdsCategoryRespDTO> list = igdsCategoryRSV.queryCategoryTraceUpon(dto);
                if (list != null && list.size() > 0) {
                    for (GdsCategoryRespDTO gdsCategoryRespDTO : list) {
                        if (GDS_DIGITS_BOOK_CAT_CODE.equals(gdsCategoryRespDTO.getCatgCode())) {
                            vo.setResultMsg("人卫书城试读授权成功，请下载人卫教材APP试读");
                            break;
                        } else if (GDS_E_BOOK_CAT_CODE.equals(gdsCategoryRespDTO.getCatgCode())) {
                            vo.setResultMsg("人卫书城试读授权成功，请下载人卫电子书 APP试读");
                            break;
                        }
                    }
                }
                vo.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_SUCCESS);
            } else {
                vo.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_FAILURE);
                vo.setResultMsg("在线试读授权失败！");
            }
        } catch (BusinessException e) {
            LogUtil.error(MODULE, "获取数字教材授权阅读失败！", e);
            vo.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_FAILURE);
            vo.setResultMsg("在线试读授权失败！原因是：" + e.getMessage());
        } catch (Exception e) {
            LogUtil.error(MODULE, "获取数字教材授权阅读失败！", e);
            vo.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_FAILURE);
            vo.setResultMsg("在线试读授权失败！原因是：" + e.getMessage());
        }
        return vo;
    }
    
    @RequestMapping(value = "/readpdf")
    public String readpdf() {
        return URL + "/pdf/gds-pdf";
    }

    /**
     * 
     * readPdfBuffer:(通过流方式读取pdf). <br/>
     * TODO(这里描述这个方法的注意事项 – 可选).<br/>
     * 
     * @since JDK 1.6
     */
    @RequestMapping(value = "/readpdfbuffer")
    public void readPdfBuffer(HttpServletResponse response, GdsPdfVO gdsPdfVO) {
        response.setCharacterEncoding("UTF-8");
        ServletOutputStream outputStream = null;
        try {
            byte[] b = FileUtil.readFile(gdsPdfVO.getPdfId());
            outputStream = response.getOutputStream();
            if (b != null && b.length > 0) {
                outputStream.write(b);
            }
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
	 * 
	 * querySomeValue:(获取相同作者的推荐). <br/>
	 * TODO(这里描述这个方法的注意事项 – 可选).<br/>
	 * 
	 * @return
	 * @since JDK 1.6
	 */
	@RequestMapping(value = "/querysomevalue")
	public String querySomeValue(Model model, GdsCommonAuthorVO gdsCommonAuthorVO) {
		GdsSku2PropPropIdxReqDTO reqDTO = gdsCommonAuthorVO.toBaseInfo(GdsSku2PropPropIdxReqDTO.class);
		PageResponseDTO<GdsSkuInfoRespDTO> pageInfo = null;
		if (StringUtil.isNotEmpty(gdsCommonAuthorVO.getPropId())) {
			reqDTO.setPropId(gdsCommonAuthorVO.getPropId());
		}
		if (StringUtil.isNotEmpty(gdsCommonAuthorVO.getGdsId())) {
			reqDTO.setGdsId(gdsCommonAuthorVO.getGdsId());
		}
		if (StringUtil.isNotEmpty(gdsCommonAuthorVO.getPropValue())) {

			reqDTO.setPropValueALike(gdsCommonAuthorVO.getPropValue());
		}else{
			
			reqDTO.setPropValue(gdsCommonAuthorVO.getPropValue());
		}

		reqDTO.setStatus(GdsConstants.Commons.STATUS_VALID);
		reqDTO.setPageSize(PAGE_SIZE);

		// 设置需要的单品属性Id
		List<Long> propIds = new ArrayList<Long>();
		propIds.add(1001l);// 作者
		reqDTO.setPropIds(propIds);

		try {
			pageInfo = iGdsSkuInfoQueryRSV.queryGdsSkuInfoPaging(reqDTO);
			if (pageInfo != null) {
				model.addAttribute("commonAuthorList", pageInfo.getResult());
			} else {
				model.addAttribute("commonAuthorList", new ArrayList<GdsSkuInfoRespDTO>());
			}
		} catch (BusinessException e) {
			LogUtil.error(MODULE, "获取相同作者推荐失败！", e);
			model.addAttribute("commonAuthorList", new ArrayList<GdsSkuInfoRespDTO>());
			return URL + "/list/gds-someAuthor-list";
		}
		return URL + "/list/gds-someAuthor-list";
	}

    
}
