package com.ai.ecp.busi.goods.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.drools.core.RuntimeDroolsException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.ecp.base.controller.EcpBaseController;
import com.ai.ecp.base.vo.EcpBasePageRespVO;
import com.ai.ecp.base.vo.EcpBaseResponseVO;
import com.ai.ecp.busi.goods.util.GdsParamsTool;
import com.ai.ecp.busi.goods.vo.GdsManageVO;
import com.ai.ecp.busi.goods.vo.GdsShopVO;
import com.ai.ecp.busi.goods.vo.GdsSkuVO;
import com.ai.ecp.busi.goods.vo.GdsVerifyVO;
import com.ai.ecp.busi.order.vo.RExportExcelResponse;
import com.ai.ecp.goods.dubbo.constants.GdsConstants;
import com.ai.ecp.goods.dubbo.constants.GdsOption.GdsQueryOption;
import com.ai.ecp.goods.dubbo.dto.GdsPropRespDTO;
import com.ai.ecp.goods.dubbo.dto.GdsTypeRespDTO;
import com.ai.ecp.goods.dubbo.dto.GdsVerifyReqDTO;
import com.ai.ecp.goods.dubbo.dto.GdsVerifyRespDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsInfoDetailRespDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsInfoReqDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsInfoRespDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsSkuInfoReqDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsSkuInfoRespDTO;
import com.ai.ecp.goods.dubbo.interfaces.IGdsInfoManageRSV;
import com.ai.ecp.goods.dubbo.interfaces.IGdsInfoQueryRSV;
import com.ai.ecp.goods.dubbo.interfaces.IGdsSkuInfoManageRSV;
import com.ai.ecp.goods.dubbo.interfaces.IGdsSkuInfoQueryRSV;
import com.ai.ecp.goods.dubbo.interfaces.IGdsTypeRSV;
import com.ai.ecp.goods.dubbo.util.GdsUtils;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.ecp.server.front.util.SysCfgUtil;
import com.ai.ecp.staff.dubbo.dto.ShopInfoResDTO;
import com.ai.ecp.staff.dubbo.interfaces.IShopInfoRSV;
import com.ai.paas.utils.FileUtil;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.ObjectCopyUtil;
import com.ai.paas.utils.StringUtil;

/**
 *
 * Project Name:ecp-web-manage <br>
 * Description: 商品管理<br>
 * Date:2015年8月18日上午9:21:20 <br>
 *
 * @version
 * @since JDK 1.6
 */
@Controller
@RequestMapping("/gdsmanage")
public class GdsManageController extends EcpBaseController {
	private static String MODULE = GdsManageController.class.getName();

	public final static String EXCEL_TYPE_XLS = "xls";
	public final static String EXCEL_TYPE_XLSX = "xlsx";

	private static String OPERATE_FLAG_UP = "1";

	private static String OPERATE_FLAG_DOWN = "0";

	@SuppressWarnings("unused")
	private static String OPERATE_FLAG_DELETE = "99";

	private static final String URL = "/goods/gdsManage";

	private static final String IF_GDS_SCORE = "0";// 不是积分商城的商品

	@Resource
	private IGdsInfoQueryRSV iGdsInfoQueryRSV;

	@Resource
	private IGdsSkuInfoQueryRSV iGdsSkuInfoQueryRSV;

	@Resource
	private IGdsInfoManageRSV iGdsInfoManageRSV;

	@Resource
	private IGdsSkuInfoManageRSV iGdsSkuInfoManageRSV;

	@Resource
	private IGdsTypeRSV iGdsTypeRSV;

	@Resource
	private IShopInfoRSV iShopInfoRSV;

	/**
	 *
	 * init:(初始化跳转到商品管理列表页面). <br/>
	 *
	 * @return
	 * @since JDK 1.6
	 */
	@RequestMapping()
	public String init(Model model, GdsShopVO gsShopVO) {
		List<GdsTypeRespDTO> gdsTypeList = iGdsTypeRSV
				.queryAllGdsTypesFromCache();
		try {
			model.addAttribute("GDS_VERIFY_SWITCH",
					SysCfgUtil.fetchSysCfg("GDS_VERIFY_SWITCH").getParaValue());
		} catch (Exception e) {
			model.addAttribute("GDS_VERIFY_SWITCH", "0");
		}

		model.addAttribute("gdsTypeList", gdsTypeList);
		model.addAttribute("shopId", gsShopVO.getShopId());

		model.addAttribute("ifGdsScore", IF_GDS_SCORE);
		return URL + "/gdsManage";
	}

	@RequestMapping("/shopStatus")
	@ResponseBody
	public GdsManageVO shopStatus(Long shopId) {
		// 判断店铺状态
		ShopInfoResDTO shopInfoResDTOStatus = iShopInfoRSV
				.findShopInfoByShopID(shopId);
		GdsManageVO manageVO = new GdsManageVO();
		manageVO.setStatus(shopInfoResDTOStatus.getShopStatus());
		return manageVO;
	}

	/**
	 *
	 * gridList:(获取商品管理页的商品列表). <br/>
	 *
	 * @param model
	 * @param vo
	 * @return
	 * @throws Exception
	 * @since JDK 1.6
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/gridlist")
	@ResponseBody
	public Model gridList(Model model, GdsManageVO vo) throws Exception {

		// /后场服务所需要的DTO；
		GdsInfoReqDTO dto = vo.toBaseInfo(GdsInfoReqDTO.class);
		// 组织参数
		ObjectCopyUtil.copyObjValue(vo, dto, "", false);
		dto.setGdsStatus(vo.getStatus());
		if (StringUtil.isNotEmpty(vo.getGdsId())) {
			dto.setId(vo.getGdsId());
		}
		if (StringUtil.isEmpty(vo.getStatus())) {
			dto.setGdsStatus(GdsConstants.GdsInfo.GDS_STATUS_WAITSHELVES);
		}

		if (GdsConstants.GdsInfo.GDS_STATUS_DELETE.equals(vo.getStatus())) {
			dto.setExt1Null(true);
		}
		if (StringUtil.isEmpty(vo.getShopId())) {
			throw new BusinessException("web.gds.200008");
		}
		if (StringUtil.isNotEmpty(vo.getCatgCode())) {
			dto.setPlatCatgs(vo.getCatgCode());
		}
		if (StringUtil.isNotEmpty(vo.getStartTime())) {
			dto.setBegCreateTime(new Timestamp(vo.getStartTime().getTime()));
		}
		if (StringUtil.isNotEmpty(vo.getEndTime())) {
			dto.setEndCreateTime(new Timestamp(vo.getEndTime().getTime()));
		}
		if (StringUtil.isNotBlank(vo.getPriceSort())) {

			dto.setPriceSort(vo.getPriceSort());
		}
		if (StringUtil.isNotEmpty(vo.getIsbn())) {
			dto.setIsbn(vo.getIsbn());
		}
		dto.setIfScoreGds(vo.getIfGdsScore());
		List<Long> propIds = new ArrayList<Long>();
		// 出版日期:1005作者:1001
		propIds.add(1005l);
		propIds.add(1001l);
		dto.setPropIds(propIds);
		dto.setGdsQueryOptions(new GdsQueryOption[] { GdsQueryOption.BASIC,
				GdsQueryOption.SHIPTEMPLATE, GdsQueryOption.PROP });
		PageResponseDTO<GdsInfoRespDTO> list = new PageResponseDTO<GdsInfoRespDTO>();
		EcpBasePageRespVO<Map> respVO = null;
		try {
			list = iGdsInfoQueryRSV.queryGdsInfoListPageWithAuth(dto);
			if (CollectionUtils.isNotEmpty(list.getResult())) {
				String verifySwitch = "";
				try {
					verifySwitch = SysCfgUtil.fetchSysCfg("GDS_VERIFY_SWITCH")
							.getParaValue();
				} catch (Exception e) {
					LogUtil.error(MODULE, "获取商品审核开关参数失败！", e);
				}
				ShopInfoResDTO shopInfoResDTO = iShopInfoRSV
						.findShopInfoByShopID(vo.getShopId());
				for (GdsInfoRespDTO gdsInfoRespDTO : list.getResult()) {
					if (gdsInfoRespDTO.getAllPropMaps() != null) {
						if (gdsInfoRespDTO.getAllPropMaps().get("1005") != null) {
							String gdsPublishTime = gdsInfoRespDTO
									.getAllPropMaps().get("1005").getValues()
									.get(0).getPropValue();
							gdsInfoRespDTO.setGdsPublishTime(gdsPublishTime);

						}
						if (gdsInfoRespDTO.getAllPropMaps().get("1001") != null) {
							String author = gdsInfoRespDTO.getAllPropMaps()
									.get("1001").getValues().get(0)
									.getPropValue();
							gdsInfoRespDTO.setGdsAuthor(author);
						}
					}
					if (shopInfoResDTO != null) {
						gdsInfoRespDTO.setShopStatus(shopInfoResDTO
								.getShopStatus());
					}
					gdsInfoRespDTO.setVerifySwitch(verifySwitch);

					//状态为已上架时判断是否有线下记录
					if(gdsInfoRespDTO.getGdsStatus().equals(GdsConstants.GdsInfo.GDS_STATUS_ONSHELVES)){

				        GdsInfoReqDTO gdsInfoReqDTO = new GdsInfoReqDTO();
				        gdsInfoReqDTO.setExt1(gdsInfoRespDTO.getId().toString());
				        gdsInfoReqDTO.setShopId(gdsInfoRespDTO.getShopId());
				        gdsInfoReqDTO.setGdsStatus(GdsConstants.GdsInfo.GDS_STATUS_OFFLINE);
				        PageResponseDTO<GdsInfoRespDTO> result = iGdsInfoQueryRSV.queryGdsInfoListPage(gdsInfoReqDTO);
				        List<GdsInfoRespDTO> listOffline= result.getResult();
				        //存在线下状态记录
				        if(CollectionUtils.isNotEmpty(listOffline)){
				        	for (GdsInfoRespDTO gdsInfoRespDTO2 : listOffline) {
				        		GdsVerifyVO gdsVerifyVO = new GdsVerifyVO();
				        		gdsVerifyVO.setGdsId(gdsInfoRespDTO2.getId());
				        		gdsVerifyVO.setShopId(gdsInfoRespDTO2.getShopId());
				        		if(whetherHaveWaiteVerify(gdsVerifyVO)){
				        			//提交更新审核中
				        			gdsInfoRespDTO.setGdsOfflineApprove("11");
				        		}else{
				        			//未提交更新审核
				        			gdsInfoRespDTO.setGdsOfflineApprove("0");
				        		}
							}
				        }
					}

				}
			}
			respVO = EcpBasePageRespVO.buildByPageResponseDTO(list);
		} catch (BusinessException e) {
			LogUtil.error(MODULE, "查询列表失败", e);
			respVO = EcpBasePageRespVO.buildByPageResponseDTO(list);
			return super.addPageToModel(model, respVO);
		}
		if (GdsUtils.isEqualsValid(vo.getIfGdsScore())) {
			return super.addPageToModel(model,
					GdsParamsTool.batchGdsDetailUrl(respVO, "url", 2l));
		} else {
			return super.addPageToModel(model,
					GdsParamsTool.batchGdsDetailUrl(respVO, "url"));
		}
	}

	/**
	 *
	 * gdsBatchUp:(单个商品上下架). <br/>
	 *
	 * @return
	 * @since JDK 1.6
	 */
	@RequestMapping(value = "/gdsupdown")
	@ResponseBody
	public EcpBaseResponseVO gdsBatchUp(GdsManageVO gdsVo) {
		EcpBaseResponseVO vo = new EcpBaseResponseVO();
		GdsInfoReqDTO dto = new GdsInfoReqDTO();
		dto.setSendIdxMsg(false);
		ObjectCopyUtil.copyObjValue(gdsVo, dto, "", false);
		if (OPERATE_FLAG_UP.equals(gdsVo.getOperateFlag())) {
			dto.setGdsStatus(GdsConstants.GdsInfo.GDS_STATUS_ONSHELVES);
		} else if (OPERATE_FLAG_DOWN.equals(gdsVo.getOperateFlag())) {
			dto.setGdsStatus(GdsConstants.GdsInfo.GDS_STATUS_OFFSHELVES);
		}
		if (!StringUtil.isBlank(gdsVo.getOperateId())) {
			dto.setId(Long.parseLong(gdsVo.getOperateId()));
		}
		try {
			iGdsInfoManageRSV.doGdsShelves(dto);
			vo.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_SUCCESS);
		} catch (BusinessException e) {
			LogUtil.error(MODULE, "报错了啦", e);
			vo.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_EXCEPTION);
			vo.setResultMsg(e.getErrorMessage());
		}
		return vo;
	}

	/**
	 *
	 * gdsBatchupdown:(批量上下架). <br/>
	 *
	 * @param gdsVo
	 * @return
	 * @since JDK 1.6
	 */
	@RequestMapping(value = "/gdsbatchupdown")
	@ResponseBody
	EcpBaseResponseVO gdsBatchupdown(GdsManageVO gdsVo) {
		EcpBaseResponseVO vo = new EcpBaseResponseVO();
		GdsInfoReqDTO dto = new GdsInfoReqDTO();
		dto.setSendIdxMsg(false);
		ObjectCopyUtil.copyObjValue(gdsVo, dto, "", false);
		String[] idList = gdsVo.getOperateId().split(",");
		Long[] ids = new Long[idList.length];
		if (ids.length > 0) {
			for (int i = 0; i < idList.length; i++) {
				if (StringUtil.isNotBlank(idList[i])) {
					ids[i] = Long.parseLong(idList[i]);
				}
			}
		}
		dto.setIds(ids);
		if (OPERATE_FLAG_UP.equals(gdsVo.getOperateFlag())) {
			dto.setGdsStatus(GdsConstants.GdsInfo.GDS_STATUS_ONSHELVES);
		} else if (OPERATE_FLAG_DOWN.equals(gdsVo.getOperateFlag())) {
			dto.setGdsStatus(GdsConstants.GdsInfo.GDS_STATUS_OFFSHELVES);
		}
		try {
			iGdsInfoManageRSV.batchDoGdsShelves(dto);
			vo.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_SUCCESS);
		} catch (BusinessException e) {
			LogUtil.error(MODULE, "报错了啦", e);
			vo.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_FAILURE);
			vo.setResultMsg(e.getErrorMessage());
		}
		return vo;
	}

	/**
	 *
	 * gdsBatchRemove:(批量删除商品). <br/>
	 *
	 * @return
	 * @since JDK 1.6
	 */
	@RequestMapping(value = "/gdsbatchremove")
	@ResponseBody
	public EcpBaseResponseVO gdsBatchRemove(Model model, GdsManageVO gdsVo) {
		EcpBaseResponseVO vo = new EcpBaseResponseVO();
		GdsInfoReqDTO dto = new GdsInfoReqDTO();
		ObjectCopyUtil.copyObjValue(gdsVo, dto, "", false);
		// 获取企业编码
		ShopInfoResDTO shopInfo = iShopInfoRSV.findShopInfoByShopID(gdsVo
				.getShopId());
		Long companyId = null;
		if (StringUtil.isNotEmpty(shopInfo)) {
			companyId = shopInfo.getCompanyId();
		} else {
			throw new BusinessException("web.gds.2000012");
		}
		dto.setCompanyId(companyId);
		String[] idList = gdsVo.getOperateId().split(",");
		Long[] ids = new Long[idList.length];
		if (ids.length > 0) {
			for (int i = 0; i < idList.length; i++) {
				if (StringUtil.isNotBlank(idList[i])) {
					ids[i] = Long.parseLong(idList[i]);
				}
			}
		}
		dto.setIds(ids);
		try {
			iGdsInfoManageRSV.batchDelGdsInfo(dto);
			vo.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_SUCCESS);
		} catch (BusinessException err) {
			LogUtil.error(MODULE, "批量删除失败", err);
			vo.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_FAILURE);
			vo.setResultMsg(err.getErrorMessage());
		}
		return vo;
	}

	/**
	 *
	 * gdsBatchRemove:(单个删除商品). <br/>
	 *
	 * @return
	 * @since JDK 1.6
	 */
	@RequestMapping(value = "/gdsremove")
	@ResponseBody
	public EcpBaseResponseVO gdsRemove(Model model, GdsManageVO gdsVo) {
		EcpBaseResponseVO vo = new EcpBaseResponseVO();
		GdsInfoReqDTO dto = new GdsInfoReqDTO();
		ObjectCopyUtil.copyObjValue(gdsVo, dto, "", false);
		if (!StringUtil.isBlank(gdsVo.getOperateId())) {
			dto.setId(Long.parseLong(gdsVo.getOperateId()));
		}
		// 获取企业编码
		ShopInfoResDTO shopInfo = iShopInfoRSV.findShopInfoByShopID(gdsVo
				.getShopId());
		Long companyId = null;
		if (StringUtil.isNotEmpty(shopInfo)) {
			companyId = shopInfo.getCompanyId();
		} else {
			throw new BusinessException("web.gds.2000012");
		}
		dto.setCompanyId(companyId);
		try {
			iGdsInfoManageRSV.delGdsInfo(dto);
			vo.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_SUCCESS);
		} catch (BusinessException err) {
			LogUtil.error(MODULE, "删除商品失败！", err);
			vo.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_FAILURE);
			vo.setResultMsg(err.getErrorMessage());
		}
		return vo;
	}

	/**
	 *
	 * copyGds:(商品设置运费模板). <br/>
	 *
	 * @param model
	 * @return
	 * @since JDK 1.6
	 */
	@RequestMapping(value = "/setshiptemp")
	@ResponseBody
	public EcpBaseResponseVO copyGds(Model model, GdsManageVO gdsManageVO) {
		EcpBaseResponseVO vo = new EcpBaseResponseVO();
		GdsInfoReqDTO dto = new GdsInfoReqDTO();
		if (StringUtil.isNotEmpty(gdsManageVO.getGdsId())) {
			dto.setId(gdsManageVO.getGdsId());
		}
		if (StringUtil.isNotEmpty(gdsManageVO.getShipTemplateId())) {
			dto.setShipTemplateId(gdsManageVO.getShipTemplateId());
		}
		dto.setShopId(gdsManageVO.getShopId());
		try {
			iGdsInfoManageRSV.editGdsShipTemplate(dto);
			vo.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_SUCCESS);
		} catch (BusinessException e) {
			LogUtil.error(MODULE, "报错了啦", e);
			vo.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_EXCEPTION);
		}
		return vo;
	}

	/**
	 *
	 * gridskulist:(获取商品单品管理列表). <br/>
	 *
	 * @param model
	 * @param vo
	 * @return
	 * @throws Exception
	 * @since JDK 1.6
	 */
	@RequestMapping("/gridskulist")
	public String gridskulist(Model model, @RequestParam("gdsId") String gdsId,
			@RequestParam("status") String status) throws Exception {
		List<GdsSkuInfoRespDTO> list = new ArrayList<GdsSkuInfoRespDTO>();
		// /后场服务所需要的DTO；
		GdsInfoReqDTO dto = new GdsInfoReqDTO();
		if (!StringUtil.isBlank(gdsId)) {
			dto.setId(Long.parseLong(gdsId));
		}
		if (GdsConstants.GdsInfo.GDS_STATUS_DELETE.equals(status)) {
			dto.setGdsStatusArr(GdsUtils.getDeleteStatusList());
		} else {
			dto.setGdsStatusArr(GdsUtils.getNoDeleteStatusList());
		}

		// 模拟一个后场返回的列表信息；
		list = iGdsInfoQueryRSV.querySkuInfosByGdsId(dto);
		model.addAttribute("skuList", list);
		return URL + "/list/sku-list";
	}

	/**
	 *
	 * skuupdown:(单品上下架). <br/>
	 *
	 * @param model
	 * @param skuId
	 * @param gdsId
	 * @return
	 * @since JDK 1.6
	 */
	@RequestMapping(value = "/skuupdown")
	@ResponseBody
	public EcpBaseResponseVO skuupdown(@Valid GdsSkuVO gdsSkuVO) {
		EcpBaseResponseVO vo = new EcpBaseResponseVO();
		GdsSkuInfoReqDTO dto = new GdsSkuInfoReqDTO();
		if (!StringUtil.isEmpty(gdsSkuVO.getSkuId())) {
			dto.setId(gdsSkuVO.getSkuId());
		}
		if (!StringUtil.isEmpty(gdsSkuVO.getGdsId())) {
			dto.setGdsId(gdsSkuVO.getGdsId());
		}
		if (!StringUtil.isEmpty(gdsSkuVO.getShopId())) {
			dto.setShopId(gdsSkuVO.getShopId());
		}
		if (OPERATE_FLAG_UP.equals(gdsSkuVO.getOperateFlag())) {
			dto.setGdsStatus(GdsConstants.GdsInfo.GDS_STATUS_ONSHELVES);
			// 判断审核开关是否开启
			String GDS_VERIFY_SWITCH = SysCfgUtil.fetchSysCfg(
					"GDS_VERIFY_SWITCH").getParaValue();
			if (GdsConstants.Commons.STATUS_VALID.equals(GDS_VERIFY_SWITCH)) {

			}
		} else if (OPERATE_FLAG_DOWN.equals(gdsSkuVO.getOperateFlag())) {
			dto.setGdsStatus(GdsConstants.GdsInfo.GDS_STATUS_OFFSHELVES);
		}
		try {
			iGdsSkuInfoManageRSV.doSkuShelves(dto);
			vo.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_SUCCESS);
		} catch (BusinessException e) {
			LogUtil.error(MODULE, "单品上架失败", e);
			vo.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_FAILURE);
			vo.setResultMsg(e.getErrorMessage());
		}
		return vo;
	}

	/**
	 *
	 * skuOpen:(上下架单条记录时候，弹出的单品列表框). <br/>
	 *
	 * @return
	 * @since JDK 1.6
	 */
	@RequestMapping("/sku-open")
	public String skuOpen() {
		return URL + "/list/sku-grid";
	}

	/**
	 *
	 * freeOpen:(设置运费模板的时候弹出的运费模板列表框). <br/>
	 *
	 * @param model
	 * @param id
	 * @return
	 * @since JDK 1.6
	 */
	@RequestMapping(value = "/free-open")
	public String freeOpen(Model model) {
		return URL + "/list/free-grid";
	}

	/**
	 *
	 * commitforverify:(提交商品上架、删除、更新审核). <br/>
	 * TODO(这里描述这个方法的注意事项 – 可选).<br/>
	 *
	 * @param model
	 * @return
	 * @since JDK 1.6
	 */
	@RequestMapping(value = "/commitforverify")
	@ResponseBody
	public EcpBaseResponseVO commitforverify(Model model,
			GdsVerifyVO gdsVerifyVO) {
		EcpBaseResponseVO ecpBaseResponseVO = new EcpBaseResponseVO();
		/**
		 * 在审核开关开启的情况下，任何的审核操作或者编辑操作，就将受到 尚未审核完成的限制。
		 */
		try {
			//获取对应线下记录
			GdsInfoReqDTO gdsInfoReqDTO = new GdsInfoReqDTO();
			gdsInfoReqDTO.setExt1(gdsVerifyVO.getGdsId().toString());
			gdsInfoReqDTO.setShopId(gdsVerifyVO.getShopId());
			gdsInfoReqDTO.setGdsStatus(GdsConstants.GdsInfo.GDS_STATUS_OFFLINE);
			PageResponseDTO<GdsInfoRespDTO> result = iGdsInfoQueryRSV.queryGdsInfoListPage(gdsInfoReqDTO);
			List<GdsInfoRespDTO> listOffline= result.getResult();

			//如果是已上架商品，对其对应线下记录的状态做判断
			if(GdsConstants.GdsInfo.GDS_STATUS_OFFLINE
					.equals(gdsVerifyVO.getOperateType())){
				if(null != listOffline && !listOffline.isEmpty()){
					gdsVerifyVO.setGdsId(listOffline.get(0).getId());
					gdsVerifyVO.setShopId(listOffline.get(0).getShopId());
				}
			}
			if (whetherHaveWaiteVerify(gdsVerifyVO)) {
				// 该商品尚处于审核状态，无法进行其他操作
				ecpBaseResponseVO
						.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_FAILURE);
				ecpBaseResponseVO.setResultMsg("该商品尚处于提交待审核状态，暂时无法进行其他操作！");
			} else {
				GdsVerifyReqDTO gdsVerifyReqDTO = new GdsVerifyReqDTO();
				ObjectCopyUtil.copyObjValue(gdsVerifyVO, gdsVerifyReqDTO, null,
						false);
				Long[] ids = new Long[] { gdsVerifyVO.getGdsId() };
				gdsVerifyReqDTO.setIds(ids);
				// 操作类型
				StringBuffer str = new StringBuffer();
				if (GdsConstants.GdsInfo.GDS_STATUS_ONSHELVES
						.equals(gdsVerifyVO.getOperateType())) {
					// 上架提交
					str.append("提交上架审核成功！");
				} else if (GdsConstants.GdsInfo.GDS_STATUS_DELETE
						.equals(gdsVerifyVO.getOperateType())) {
					// 删除提交
					str.append("提交删除审核成功！");
				}else if (GdsConstants.GdsInfo.GDS_STATUS_OFFLINE
						.equals(gdsVerifyVO.getOperateType())) {
					// （已上架商品）更新提交
					str.append("提交更新审核成功！");
					//存在线下状态记录
						ObjectCopyUtil.copyObjValue(listOffline.get(0), gdsVerifyReqDTO, null,
								false);
				}
				//设置开关的值
				gdsVerifyReqDTO.setVerifySwitch2(SysCfgUtil.fetchSysCfg("GDS_VERIFY_SWITCH").getParaValue());
				iGdsInfoManageRSV.doGdsVerify(gdsVerifyReqDTO);
				ecpBaseResponseVO
						.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_SUCCESS);
				ecpBaseResponseVO.setResultMsg(str.toString());
			}
		} catch (BusinessException e) {
			LogUtil.error(MODULE, "提交审核失败！", e);
			ecpBaseResponseVO
					.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_FAILURE);
			ecpBaseResponseVO.setResultMsg("提交审核失败,原因：" + e.getErrorMessage());
		} catch (Exception e) {
			LogUtil.error(MODULE, "提交审核失败！", e);
			ecpBaseResponseVO
					.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_FAILURE);
			ecpBaseResponseVO.setResultMsg("提交审核失败,原因：系统异常，请联系管理员！");
		}
		return ecpBaseResponseVO;
	}

	/**
	 *
	 * batchCommitForVerify:(批量提交上架、删除审核). <br/>
	 * TODO(这里描述这个方法的注意事项 – 可选).<br/>
	 *
	 * @param gdsVerifyVO
	 * @return
	 * @since JDK 1.6
	 */
	@RequestMapping(value = "/batchcommitforverify")
	@ResponseBody
	public EcpBaseResponseVO batchCommitForVerify(GdsVerifyVO gdsVerifyVO) {
		EcpBaseResponseVO ecpBaseResponseVO = new EcpBaseResponseVO();
		GdsVerifyReqDTO gdsVerifyReqDTO = new GdsVerifyReqDTO();
		ObjectCopyUtil.copyObjValue(gdsVerifyVO, gdsVerifyReqDTO, "", false);
		String[] idList = gdsVerifyVO.getOperateId().split(",");
		Long[] ids = new Long[idList.length];
		if (ids.length > 0) {
			for (int i = 0; i < idList.length; i++) {
				if (StringUtil.isNotBlank(idList[i])) {
					ids[i] = Long.parseLong(idList[i]);
				}
			}
		}
		/**
		 * 在审核开关开启的情况下，任何的审核操作或者编辑操作，就将受到 尚未审核完成的限制。
		 */
		try {
			if (whetherHaveWaiteVerify(gdsVerifyVO)) {
				// 该商品尚处于审核状态，无法进行其他操作
				ecpBaseResponseVO
						.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_FAILURE);
				ecpBaseResponseVO.setResultMsg("该商品尚处于提交待审核状态，暂时无法进行其他操作！");
			} else {
				gdsVerifyReqDTO.setIds(ids);
				// 操作类型
				StringBuffer str = new StringBuffer();
				if (GdsConstants.GdsInfo.GDS_STATUS_ONSHELVES
						.equals(gdsVerifyVO.getOperateType())) {
					// 上架提交
					str.append("批量提交上架审核成功！");
				} else if (GdsConstants.GdsInfo.GDS_STATUS_DELETE
						.equals(gdsVerifyVO.getOperateType())) {
					// 删除提交
					str.append("批量提交删除审核成功！");
				}
				//设置开关的值
				gdsVerifyReqDTO.setVerifySwitch2(SysCfgUtil.fetchSysCfg("GDS_VERIFY_SWITCH").getParaValue());
				iGdsInfoManageRSV.doGdsVerify(gdsVerifyReqDTO);
				ecpBaseResponseVO
						.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_SUCCESS);
				ecpBaseResponseVO.setResultMsg(str.toString());
			}
		} catch (BusinessException e) {
			LogUtil.error(MODULE, "批量提交审核失败！", e);
			ecpBaseResponseVO
					.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_FAILURE);
			ecpBaseResponseVO.setResultMsg("批量提交审核失败！");
		} catch (Exception e) {
			LogUtil.error(MODULE, "批量提交审核失败！", e);
			ecpBaseResponseVO
					.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_FAILURE);
			ecpBaseResponseVO.setResultMsg("批量提交审核失败！");
		}
		return ecpBaseResponseVO;
	}

	/**
	 *
	 * whetherHaveWaiteVerify:(判断是否存在提交待审核状态的商品操作审核记录). <br/>
	 *
	 * @return
	 * @since JDK 1.6
	 */
	public boolean whetherHaveWaiteVerify(GdsVerifyVO gdsVerifyVO) {
		if (gdsVerifyVO.getOperateId() != null) {
			String ids[] = gdsVerifyVO.getOperateId().split(",");
			if (ids.length > 0) {
				for (String id : ids) {
					GdsVerifyReqDTO dto = new GdsVerifyReqDTO();
					dto.setGdsId(Long.parseLong(id));
					dto.setShopId(gdsVerifyVO.getShopId());
					dto.setVerifyStatus(GdsConstants.GdsVerify.WAITE_VERIFY);// 提交待初审
					PageResponseDTO<GdsVerifyRespDTO> pageInfo = iGdsInfoQueryRSV
							.queryGdsVerifyInfoPage(dto);
					dto.setVerifyStatus(GdsConstants.GdsVerify.FIRST_VERIFY_APPROVED);// 提交待复审
					PageResponseDTO<GdsVerifyRespDTO> pageInfo2 = iGdsInfoQueryRSV
							.queryGdsVerifyInfoPage(dto);
					if ((pageInfo != null && pageInfo.getResult() != null
							&& pageInfo.getResult().size() > 0) || (pageInfo2 != null && pageInfo2.getResult() != null
							&& pageInfo2.getResult().size() > 0)) {
						return true;
					}

				}
				return false;
			} else {
				// 参数不为空却没传有效id，默认不可以处理
				return true;
			}
		} else {

			GdsVerifyReqDTO dto = new GdsVerifyReqDTO();
			dto.setGdsId(gdsVerifyVO.getGdsId());
			dto.setShopId(gdsVerifyVO.getShopId());
			dto.setSkuId(gdsVerifyVO.getSkuId());
			dto.setVerifyStatus(GdsConstants.GdsVerify.WAITE_VERIFY);// 提交待初审
			PageResponseDTO<GdsVerifyRespDTO> pageInfo = iGdsInfoQueryRSV
					.queryGdsVerifyInfoPage(dto);
			dto.setVerifyStatus(GdsConstants.GdsVerify.FIRST_VERIFY_APPROVED);// 提交待复审
			PageResponseDTO<GdsVerifyRespDTO> pageInfo2 = iGdsInfoQueryRSV
					.queryGdsVerifyInfoPage(dto);
			if ((pageInfo != null && pageInfo.getResult() != null
					&& pageInfo.getResult().size() > 0) || (pageInfo2 != null && pageInfo2.getResult() != null
					&& pageInfo2.getResult().size() > 0)) {
				return true;
			}
			return false;
		}
	}

	/**
	 *
	 * gdsVerifyRecord:(跳转到商品审核记录弹出框). <br/>
	 *
	 * @return
	 * @since JDK 1.6
	 */
	@RequestMapping(value = "/gdsverifyrecord")
	public String gdsVerifyRecord() {
		return URL + "/list/gds-verify-record";
	}

	/**
	 *
	 * whetherCanOperate:(判断该商品是否存在提交待审核的记录). <br/>
	 * TODO(这里描述这个方法的注意事项 – 可选).<br/>
	 *
	 * @param gdsVerifyVO
	 * @return
	 * @since JDK 1.6
	 */
	@RequestMapping(value = "/whethercanoperate")
	@ResponseBody
	public EcpBaseResponseVO whetherCanOperate(GdsVerifyVO gdsVerifyVO) {
		EcpBaseResponseVO ecpBaseResponseVO = new EcpBaseResponseVO();
		try {
			if (whetherHaveWaiteVerify(gdsVerifyVO)) {
				// 存在
				ecpBaseResponseVO
						.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_FAILURE);
				ecpBaseResponseVO.setResultMsg("该商品尚处于提交待审核状态，暂时无法进行其他操作！");
			} else {
				// 不存在
				ecpBaseResponseVO
						.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_SUCCESS);
			}
		} catch (BusinessException e) {
			LogUtil.error(MODULE, "", e);
			ecpBaseResponseVO
					.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_FAILURE);
			ecpBaseResponseVO.setResultMsg("该商品尚处于提交待审核状态，暂时无法进行其他操作！");
		} catch (Exception e) {
			LogUtil.error(MODULE, "", e);
			ecpBaseResponseVO
					.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_FAILURE);
			ecpBaseResponseVO.setResultMsg("该商品尚处于提交待审核状态，暂时无法进行其他操作！");
		}
		return ecpBaseResponseVO;
	}

	/**
	 * @return
	 *
	 */
	@RequestMapping(value = "/exportGds")
	public String exportGds(@RequestParam("exportIds") String exportIds,
			@RequestParam("exportShopId") Long exportShopId, Model model) {
		model.addAttribute("exportIds", exportIds);
		model.addAttribute("exportShopId", exportShopId);
		return "/goods/gdsManage/gds-export";
	}
	/**
	 * 过滤list
	 * @param list
	 * @return
	 */
	private List<GdsPropRespDTO> filterGdsPropResp(List<GdsPropRespDTO> list){
		 List<GdsPropRespDTO> filterList=new ArrayList<GdsPropRespDTO>();
		 for (GdsPropRespDTO propResp: list) {
			// 多选和富文本不进行处理
			if (GdsConstants.GdsProp.PROP_VALUE_TYPE_3
					.equals(propResp.getPropValueType())
					|| GdsConstants.GdsProp.GDS_PROP_VALUE_INPUT_TYPE_RICHTXT
							.equals(propResp.getPropInputType())) {
				continue;
			} else {
				// 如果是手工输入
				if (GdsConstants.GdsProp.PROP_VALUE_TYPE_1
						.equals(propResp.getPropValueType())) {
					// 如果不是字符数字的也不处理
					if (!GdsConstants.GdsProp.GDS_PROP_VALUE_INPUT_TYPE_ZIFU
							.equals(propResp.getPropInputType())) {
						continue;
					} else {
						filterList.add(propResp);
					}
				} else if (GdsConstants.GdsProp.PROP_VALUE_TYPE_2
						.equals(propResp.getPropValueType())) {
					// 如果是单选
					continue;
				} else {
					continue;
				}
			}
		 }
		 return filterList;
	}
	/**
	 * 商品导出
	 *
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "/getExportFileId")
    @ResponseBody
    public RExportExcelResponse getExportFileId(String ids,Long shopId){
    	GdsInfoReqDTO reqDTO=new GdsInfoReqDTO();
    	String[] idList = ids.split(",");
        Long[] idsLong = new Long[idList.length];
        List<GdsInfoDetailRespDTO> detailList=new ArrayList<GdsInfoDetailRespDTO>();
        if (idsLong.length > 0) {
            for (int i = 0; i < idList.length; i++) {
                if (StringUtil.isNotBlank(idList[i])) {
                	idsLong[i] = Long.parseLong(idList[i]);
                	GdsInfoReqDTO gdsInfoReqDTO = new GdsInfoReqDTO();
                    gdsInfoReqDTO.setId( Long.parseLong(idList[i]));
                    GdsQueryOption[] gdsOptions = new GdsQueryOption[] {
                            GdsQueryOption.PROP };
                    gdsInfoReqDTO.setGdsQueryOptions(gdsOptions);
                    GdsInfoDetailRespDTO gdsInfoDetailRespDTO = iGdsInfoQueryRSV.queryGdsInfoDetail(gdsInfoReqDTO);
                	detailList.add(gdsInfoDetailRespDTO);
                }
            }
        }
    	reqDTO.setIds(idsLong);
    	reqDTO.setShopId(shopId);
    	reqDTO.setFullInfo(true);
        RExportExcelResponse resp = new RExportExcelResponse();
        String fileType = "xlsx";
        String fileName = "gdsExport";
        List<String> columnNames=new ArrayList<>();
        columnNames.add("商品编码");
        columnNames.add("商品名称");
        columnNames.add("产品副标题");
        columnNames.add("属性编码");
        columnNames.add("属性名称");
        columnNames.add("属性值");
        try{
        	Workbook wb=null;
            ByteArrayOutputStream fos=null;
            String fileId=null;
            try {
                 if(EXCEL_TYPE_XLS.equalsIgnoreCase(fileType)){
                     wb=new HSSFWorkbook();
                 }else if(EXCEL_TYPE_XLSX.equalsIgnoreCase(fileType)){
                     wb=new XSSFWorkbook();
                 }else{
                     wb=new XSSFWorkbook();
                 }
                 Sheet sheet = wb.createSheet();
                 sheet.setDefaultColumnWidth((short) 15);
                 int ri = 0; //行
                 if(!CollectionUtils.isEmpty(columnNames)){
                     int cci=0;//列
                     ri=1;
                     Row row= sheet.createRow(0);
                     for(String cname : columnNames){
                         Cell cell=row.createCell(cci++, Cell.CELL_TYPE_STRING);
                         cell.setCellValue(cname);
                     }
                 }
                 for (GdsInfoDetailRespDTO detailRespDTO: detailList) {
                	 if(null!=detailRespDTO.getProps()&&detailRespDTO.getProps().size()>0){
                		 detailRespDTO.setProps(this.filterGdsPropResp(detailRespDTO.getProps()));
                	 }
                	 if(null!=detailRespDTO.getProps()&&detailRespDTO.getProps().size()>0){
                		 int l=detailRespDTO.getProps().size();
                		 for (int i = 0; i < l; i++) {
                			 GdsPropRespDTO propResp=detailRespDTO.getProps().get(i);
                			 Row row= sheet.createRow(ri+i);
                			 int cci_prop=3;
                			 if(i==0){
                				 sheet.addMergedRegion(new CellRangeAddress(ri, ri+l-1, 0, 0));
                				 int cci=0;//列

                				 XSSFCellStyle style = (XSSFCellStyle) wb.createCellStyle(); // 样式对象
                		         style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
                        		 Cell cell0=row.createCell(cci++, Cell.CELL_TYPE_STRING);
                                 cell0.setCellValue(detailRespDTO.getId().toString());
                                 cell0.setCellStyle(style);
                                 sheet.addMergedRegion(new CellRangeAddress(ri, ri+l-1, 1, 1));
                                 Cell cell1=row.createCell(cci++, Cell.CELL_TYPE_STRING);
                                 cell1.setCellValue(detailRespDTO.getGdsName().toString());
                                 cell1.setCellStyle(style);
                                 sheet.addMergedRegion(new CellRangeAddress(ri, ri+l-1, 2, 2));
                                 Cell cell2=row.createCell(cci++, Cell.CELL_TYPE_STRING);
                                 cell2.setCellValue(null!=detailRespDTO.getGdsSubHead()?detailRespDTO.getGdsSubHead():"");
                                 cell2.setCellStyle(style);
                			 }
                			 Cell cell3=row.createCell(cci_prop++, Cell.CELL_TYPE_STRING);
                			 cell3.setCellValue(propResp.getId().toString());
                			 Cell cell4=row.createCell(cci_prop++, Cell.CELL_TYPE_STRING);
                			 cell4.setCellValue(null!=propResp.getPropName()?propResp.getPropName():"");
                			 if(null!=propResp.getValues()&&propResp.getValues().size()>0&&null!=propResp.getValues().get(0)&&null!=propResp.getValues().get(0).getPropValue()){
                				 Cell cell5=row.createCell(cci_prop++, Cell.CELL_TYPE_STRING);
            					 cell5.setCellValue(null!=propResp.getValues().get(0).getPropValue()?propResp.getValues().get(0).getPropValue():"");
                			 }else{
                				 Cell cell5=row.createCell(cci_prop++, Cell.CELL_TYPE_STRING);
            					 cell5.setCellValue("");
                			 }
                		 }
                		 ri=ri+l;
                	 }else{
                		 Row row= sheet.createRow(ri++);
                		 int cci=0;//列
                		 Cell cell0=row.createCell(cci++, Cell.CELL_TYPE_STRING);
                         cell0.setCellValue(detailRespDTO.getId().toString());
                         Cell cell1=row.createCell(cci++, Cell.CELL_TYPE_STRING);
                         cell1.setCellValue(detailRespDTO.getGdsName().toString());
                         Cell cell2=row.createCell(cci++, Cell.CELL_TYPE_STRING);
                         cell2.setCellValue(null!=detailRespDTO.getGdsSubHead()?detailRespDTO.getGdsSubHead():"");
                         for (int i = 0; i < 3; i++) {
                        	 Cell cell3=row.createCell(cci++, Cell.CELL_TYPE_STRING);
                        	 cell3.setCellValue("");
                         }
                	 }
				 }
                fos=new ByteArrayOutputStream();
                wb.write(fos);
                fileId=FileUtil.saveFile(fos.toByteArray(), fileName, fileType);
            }catch(Exception e){
                e.printStackTrace();
                throw new RuntimeException(e.getMessage(),e);
            }finally {
                try {
                    if(null!=fos){
                        fos.close();
                    }
                    if(null!=wb){
                        wb.close();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeDroolsException(e);
                }

            }
            if(StringUtil.isBlank(fileId)){
                resp.setFileId(fileId);
                resp.setResultFlag(resp.RESULT_FLAG_FAILURE);
            }else{
                resp.setFileId(fileId);
                resp.setResultFlag(resp.RESULT_FLAG_SUCCESS);
            }
        }catch (BusinessException e){
            LogUtil.error(MODULE, "============商品导出异常==========");
            resp.setResultFlag(resp.RESULT_FLAG_EXCEPTION);
        }
        return resp;
    }

	/**
	 *
	 * commitforverifyShelved:(提交商品更新审核-已上架商品编辑页面内直接提交审核). <br/>
	 * TODO(这里描述这个方法的注意事项 – 可选).<br/>
	 *
	 * @param model
	 * @return
	 * @since JDK 1.6
	 */
	@RequestMapping(value = "/commitforverifyShelved")
	@ResponseBody
	public EcpBaseResponseVO commitforverifyShelved(Model model,
			GdsVerifyVO gdsVerifyVO) {
		EcpBaseResponseVO ecpBaseResponseVO = new EcpBaseResponseVO();
		/**
		 * 在审核开关开启的情况下，任何的审核操作或者编辑操作，就将受到 尚未审核完成的限制。
		 */
		try {
			if (whetherHaveWaiteVerify(gdsVerifyVO)) {
				// 该商品尚处于审核状态，无法进行其他操作
				ecpBaseResponseVO
						.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_FAILURE);
				ecpBaseResponseVO.setResultMsg("该商品尚处于提交待审核状态，暂时无法进行其他操作！");
			} else {
				GdsVerifyReqDTO gdsVerifyReqDTO = new GdsVerifyReqDTO();
				ObjectCopyUtil.copyObjValue(gdsVerifyVO, gdsVerifyReqDTO, null,
						false);
				Long[] ids = new Long[] { gdsVerifyVO.getGdsId() };
				gdsVerifyReqDTO.setIds(ids);
				// 操作类型
				StringBuffer str = new StringBuffer();
				if (GdsConstants.GdsInfo.GDS_STATUS_ONSHELVES
						.equals(gdsVerifyVO.getOperateType())) {
					// 上架提交
					str.append("提交上架审核成功！");
				} else if (GdsConstants.GdsInfo.GDS_STATUS_DELETE
						.equals(gdsVerifyVO.getOperateType())) {
					// 删除提交
					str.append("提交删除审核成功！");
				}else if (GdsConstants.GdsInfo.GDS_STATUS_OFFLINE
						.equals(gdsVerifyVO.getOperateType())) {
					// （已上架商品）更新提交
					str.append("提交更新审核成功！");
				}
				//设置开关的值
				gdsVerifyReqDTO.setVerifySwitch2(SysCfgUtil.fetchSysCfg("GDS_VERIFY_SWITCH").getParaValue());
				iGdsInfoManageRSV.doGdsVerify(gdsVerifyReqDTO);
				ecpBaseResponseVO
						.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_SUCCESS);
				ecpBaseResponseVO.setResultMsg(str.toString());
			}
		} catch (BusinessException e) {
			LogUtil.error(MODULE, "提交审核失败！", e);
			ecpBaseResponseVO
					.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_FAILURE);
			ecpBaseResponseVO.setResultMsg("提交审核失败,原因：" + e.getErrorMessage());
		} catch (Exception e) {
			LogUtil.error(MODULE, "提交审核失败！", e);
			ecpBaseResponseVO
					.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_FAILURE);
			ecpBaseResponseVO.setResultMsg("提交审核失败,原因：系统异常，请联系管理员！");
		}
		return ecpBaseResponseVO;
	}
} 
