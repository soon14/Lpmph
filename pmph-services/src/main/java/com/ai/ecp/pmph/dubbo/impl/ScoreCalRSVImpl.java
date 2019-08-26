package com.ai.ecp.pmph.dubbo.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.util.CollectionUtils;

import com.ai.ecp.general.order.dto.PayQuartzInfoRequest;
import com.ai.ecp.goods.dubbo.dto.GdsCategoryReqDTO;
import com.ai.ecp.goods.dubbo.dto.GdsCategoryRespDTO;
import com.ai.ecp.goods.dubbo.dto.GdsInterfaceGdsReqDTO;
import com.ai.ecp.goods.dubbo.dto.GdsInterfaceGdsRespDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsSkuInfoReqDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsSkuInfoRespDTO;
import com.ai.ecp.goods.dubbo.interfaces.IGdsCategoryRSV;
import com.ai.ecp.goods.dubbo.interfaces.IGdsInterfaceGdsRSV;
import com.ai.ecp.goods.dubbo.interfaces.IGdsSkuInfoQueryRSV;
import com.ai.ecp.pmph.dubbo.constants.PmphGdsDataImportConstants;
import com.ai.ecp.pmph.dubbo.interfaces.IScoreCalRSV;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.ecp.staff.dao.model.AuthStaffCIDX;
import com.ai.ecp.staff.dao.model.CustInfo;
import com.ai.ecp.staff.dao.model.ScoreCategoryRatio;
import com.ai.ecp.staff.dao.model.ScoreInfo;
import com.ai.ecp.staff.dao.model.ScoreOptLog;
import com.ai.ecp.staff.dubbo.dto.CustInfoReqDTO;
import com.ai.ecp.staff.dubbo.dto.ScoreCategoryRatioReqDTO;
import com.ai.ecp.staff.dubbo.dto.ScoreISBNActiveReqDTO;
import com.ai.ecp.staff.dubbo.dto.ScoreInfoReqDTO;
import com.ai.ecp.staff.dubbo.dto.ScoreInfoResDTO;
import com.ai.ecp.staff.dubbo.dto.ScoreResultResDTO;
import com.ai.ecp.staff.dubbo.dto.ScoreSourceReqDTO;
import com.ai.ecp.staff.dubbo.dto.ScoreSourceResDTO;
import com.ai.ecp.staff.dubbo.util.StaffConstants;
import com.ai.ecp.staff.service.busi.manage.interfaces.ICustManageSV;
import com.ai.ecp.staff.service.busi.score.interfaces.IScoreCaclSV;
import com.ai.ecp.staff.service.busi.score.interfaces.IScoreInfoSV;
import com.ai.ecp.staff.service.common.score.interfaces.IScoreCategoryRatioSV;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.ObjectCopyUtil;
import com.ai.paas.utils.StringUtil;

/**
 * 
 * Project Name:ecp-services-staff-server <br>
 * Description: 积分计算接口实现类<br>
 * Date:2016-2-17下午4:07:43  <br>
 * 
 * @version  
 * @since JDK 1.6
 */
public class ScoreCalRSVImpl implements IScoreCalRSV{
    
    private static final String MODULE = ScoreCalRSVImpl.class.getName();
    
    @Resource
    private IScoreCaclSV scoreCaclSV;
    
    @Resource
    private ICustManageSV custManageSV;
    
    @Resource
    private IScoreInfoSV scoreInfoSV;
    
    @Resource
    private IScoreCategoryRatioSV scoreCategoryRatioSV;
    
    @Resource
    private IGdsSkuInfoQueryRSV gdsSkuInfoQueryRSV;
    
    @Resource
    private IGdsCategoryRSV igdsCategoryRSV;
    
    @Resource
    private IGdsInterfaceGdsRSV gdsInterfaceGdsRSV;
    
    
    @Override
    public long saveScoreCal(String pSourceType, CustInfoReqDTO pCustInfo,
            PayQuartzInfoRequest dto) {
        long score = 0L;
        try {
            /*调用方法计算积分*/
            ScoreResultResDTO result = scoreCaclSV.updateScore(pSourceType, pCustInfo, dto);
            if (result != null && result.getScore() != null) {
                score = result.getScore();
            }
        } catch (Exception e) {
            throw new BusinessException(StaffConstants.STAFF_UPDATE_ERROR);
        }
        return score;
    }
    
    /**
     * 
     * checkCustInfo:(验证用户). <br/> 
     * 一书一码激活赠送积分用户校验：
     * 如果会员名在系统中存在，则查询出staffId
     * 如果会员名在系统中不存在，则新建一个用户
     * @param req
     * @return
     * @throws BusinessException 
     * @since JDK 1.6
     */
    private long checkCustInfo(CustInfoReqDTO req) throws BusinessException{
        //校验参数:SSO会员名
        if (StringUtil.isBlank(req.getStaffCode())) {
            LogUtil.info(MODULE, "入参对象不能为空！");
            throw new BusinessException(StaffConstants.STAFF_NULL_ERROR,new String[]{"SSO会员名"});
        }
        
        AuthStaffCIDX auth = new AuthStaffCIDX();
        auth.setStaffCode(req.getStaffCode());
        
        /*1、根据sso传入的会员名，查询用户在系统中是否存在*/
        auth = custManageSV.findAuthStaffCIDXByCode(auth);
        
        /*2、如果存在，则返回staffId*/
        if (auth != null && auth.getStaffId() != null && auth.getStaffId() != 0L) {
            return 1L;
            
        /*3、如果不存在，则根据sso会员名，创建一个用户，默认密码123456*/
        } else {
            req.setStaffPassword(StaffConstants.authStaff.PWD_RESET_DEFAULT);
            return custManageSV.saveCustInfoForRSV(req);
        }
        
    }

    @Override
    public long saveScoreCalForIsbnActive(ScoreISBNActiveReqDTO req) throws BusinessException {
        
        /*1、判断本版编号在系统中是否能查询到对应商品*/
        GdsInterfaceGdsReqDTO gdsReq = new GdsInterfaceGdsReqDTO();
        gdsReq.setOrigin(PmphGdsDataImportConstants.Commons.ORIGIN_ERP);
        if (StringUtil.isBlank(req.getBbCode())) {
        	throw new BusinessException("本版编号不能为空。");
        }
        gdsReq.setOriginGdsId(req.getBbCode());
        GdsInterfaceGdsRespDTO resDto = gdsInterfaceGdsRSV.queryGdsInterfaceGdsByOriginGdsId(gdsReq);
        /*1-1、没查到商品，则抛出通过本版编号未查到商品的异常*/
        if (resDto == null || resDto.getGdsId() == null || resDto.getGdsId() == 0L) {
            throw new BusinessException(StaffConstants.Score.BBCODE_NOT_FOUNT,new String[]{req.getBbCode()});
        }
        
        /*1-2、根据商品id、单品id查具体的商品*/
        GdsSkuInfoReqDTO skuReq = new GdsSkuInfoReqDTO();
        skuReq.setGdsId(resDto.getGdsId());//商品id
        skuReq.setId(resDto.getSkuId());//单品id
        GdsSkuInfoRespDTO sku = gdsSkuInfoQueryRSV.queryGdsSkuInfoResp(skuReq);//查询到的商品
        if (sku == null || sku.getId() == null || sku.getId() == 0L) {
        	throw new BusinessException(StaffConstants.Score.BBCODE_NOT_FOUNT,new String[]{req.getBbCode()});
        }
        
        /*2、查询isbn号是否已赠送过积分*/
        ScoreSourceReqDTO sourceReq = new ScoreSourceReqDTO();
        sourceReq.setBookCode(req.getBookCode());
        sourceReq.setPageNo(1);
        sourceReq.setPageSize(1);
        PageResponseDTO<ScoreSourceResDTO> page = scoreInfoSV.listScoreSource(sourceReq);
        if (!CollectionUtils.isEmpty(page.getResult())) {
            throw new BusinessException(StaffConstants.Score.BOOK_CODE_EXIST_FOR_SCORE,new String[]{req.getBookCode()});
        }
        
        /*3、校验用户，查不到用户则新建*/
        CustInfoReqDTO custInfoReq = new CustInfoReqDTO();
        custInfoReq.setStaffCode(req.getStaffCode());
        this.checkCustInfo(custInfoReq);
        AuthStaffCIDX cidx = new AuthStaffCIDX();
        cidx.setStaffCode(req.getStaffCode());
        cidx = custManageSV.findAuthStaffCIDXByCode(cidx);
        custInfoReq.setId(cidx.getStaffId());
        
        /*4、根据查询到的商品，获取商品分类层级关系*/
        List<GdsCategoryRespDTO> cateList = new ArrayList<GdsCategoryRespDTO>();
        GdsCategoryReqDTO gdsCategoryReqDTO = new GdsCategoryReqDTO();
        gdsCategoryReqDTO.setCatgCode(sku.getMainCatgs());
        cateList = igdsCategoryRSV.queryCategoryTraceUpon(gdsCategoryReqDTO);
        
        /*5、根据分类关系，查询配置比例；分类由小到大迭代（优先级从小分类到大分类）*/
        long ratio = 100L;//商品分类配置的折扣比例,初始为100%
        for (int i = cateList.size() -1 ; i >= 0; i--) {
            ScoreCategoryRatioReqDTO scoreCategoryReq = new ScoreCategoryRatioReqDTO();
            scoreCategoryReq.setActionType(StaffConstants.ISBN_ACTIVE_TYPE);//积分行为
            scoreCategoryReq.setCatgCode(cateList.get(i).getCatgCode());//商品分类
            ScoreCategoryRatio categoryRatio = scoreCategoryRatioSV.findScoreCategoryRatio(scoreCategoryReq);
            //找到配置比例，则跳出循环
            if (categoryRatio != null && "1".equals(categoryRatio.getStatus())) {
                ratio = categoryRatio.getRatio();
                break;
            }
        }

        /*6、根据比例重新计算参考价，向上取整*/
        long guidePrice = (long)Math.ceil(sku.getCommonPrice().doubleValue() * ratio / 100);

        /*7、调用通用积分计算方法*/
        PayQuartzInfoRequest request = new PayQuartzInfoRequest();
        //这里的入参，要特别注意，字段套用，请见凉
        request.setOrderId(req.getBbCode());//这里用orderId代替isbnCode参数
        request.setDealFlag(req.getBookCode());//这里用dealFlag代替bookCode参数
        request.setPayment(guidePrice);//处理后的参考价格
        long score = this.saveScoreCal(StaffConstants.ISBN_ACTIVE_TYPE, custInfoReq, request);
        
        return score;
    }
    
    /**
     * 
     * TODO 人卫E教兑换人卫商城积分（可选）. 
     * @see com.ai.ecp.staff.dubbo.interfaces.IScoreInfoRSV#updateScoreForE(com.ai.ecp.staff.dubbo.dto.CustInfoReqDTO, com.ai.ecp.staff.dubbo.dto.ScoreResultResDTO)
     */
	@Override
	public void updateScoreForE(CustInfoReqDTO cust, ScoreResultResDTO scoreResult) throws BusinessException {
		try {
			ScoreInfo scoreInfo = scoreInfoSV.findScoreInfoByStaffId(cust.getId());

			/*记录积分操作日志*/
			ScoreOptLog log = new ScoreOptLog();
			log.setTotalScore(scoreInfo.getScoreTotal());//操作前，总积分
			log.setBalanceScore(scoreInfo.getScoreBalance());//操作前，可用积分
			log.setFreezeScore(scoreInfo.getScoreFrozen());//操作前，冻结积分
			log.setUsedScore(scoreInfo.getScoreUsed());//操作前，已使用积分
			
			CustInfo custInfo = new CustInfo();
	        ObjectCopyUtil.copyObjValue(cust, custInfo, null, false);
	        
	        // 更新积分账户信息
			scoreInfoSV.updateScoreInfo(custInfo, scoreInfo, scoreResult);
			
			log.setScore(scoreResult.getScore());//本次操作的积分
			log.setStaffId(custInfo.getId());//用户ID
			log.setRemark("人卫E教兑换人卫商城积分");//备注
			log.setOptType(scoreResult.getOptType());//操作类型
			log.setCreateStaff(String.valueOf(cust.getStaff().getId()));//操作人
			log.setSiteId(cust.getCurrentSiteId());//站点id
			scoreInfoSV.saveScoreOptLog(log);
		} catch (Exception e) {
			LogUtil.info(MODULE, "======人卫E教兑换人卫商城积分异常========");
            throw new BusinessException(e.getMessage());
		}
	}

}

