package com.ai.ecp.pmph.service.busi.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import com.ai.ecp.general.order.dto.PayQuartzInfoRequest;
import com.ai.ecp.goods.dubbo.dto.GdsInterfaceGdsReqDTO;
import com.ai.ecp.goods.dubbo.dto.GdsInterfaceGdsRespDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsInfoReqDTO;
import com.ai.ecp.goods.dubbo.dto.gdsinfo.GdsInfoRespDTO;
import com.ai.ecp.goods.dubbo.interfaces.IGdsInfoQueryRSV;
import com.ai.ecp.goods.dubbo.interfaces.IGdsInterfaceGdsRSV;
import com.ai.ecp.pmph.dubbo.constants.PmphGdsDataImportConstants;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.ecp.staff.dubbo.dto.CustInfoReqDTO;
import com.ai.ecp.staff.dubbo.dto.ProductInfoReqDto;
import com.ai.ecp.staff.dubbo.dto.ScoreFuncDefReqDTO;
import com.ai.ecp.staff.dubbo.dto.ScoreInfoReqDTO;
import com.ai.ecp.staff.dubbo.dto.ScoreInfoResDTO;
import com.ai.ecp.staff.dubbo.dto.ScoreResultResDTO;
import com.ai.ecp.staff.dubbo.interfaces.IScoreCaclRSV;
import com.ai.ecp.staff.dubbo.interfaces.IScoreInfoRSV;
import com.ai.ecp.staff.dubbo.util.StaffConstants;
import com.ai.ecp.staff.service.busi.score.cacl.ICaclScore;
import com.ai.paas.utils.ObjectCopyUtil;
import com.ai.paas.utils.StringUtil;


/**
 * 
 * Project Name:ecp-services-staff-server <br>
 * Description: 一书一码赠送积分计算<br>
 * Date:2016-3-19下午5:03:57  <br>
 * 
 * @version  
 * @since JDK 1.6
 */
public class caclScoreByISBNActive implements ICaclScore {

    @Resource
    private IScoreInfoRSV scoreInfoRSV;
    
    @Resource
    private IScoreCaclRSV scoreCaclRSV;
    
    @Resource
	private IGdsInfoQueryRSV gdsInfoQueryRSV;
    
    @Resource
    private IGdsInterfaceGdsRSV gdsInterfaceGdsRSV;
    
    public ScoreResultResDTO caclScore(String pSourceType, Map<Integer, String> pParaMap,
            CustInfoReqDTO pCustInfo, ProductInfoReqDto pProductInfo) {
        long score = 0;
        /*参数说明：1、来源类型；2、函数名称；3、赠送单位(分)；4、赠送积分*/
        
        //计算积分：通过isbn号查出的参考价格（已经过折扣计算）/赠送单位*每单位赠送积分=获得积分总数
        score = (long) (Math.ceil(pProductInfo.getProductPrice()/Double.valueOf(pParaMap.get(2)))*Double.valueOf(pParaMap.get(3)));
        ScoreResultResDTO rs = new ScoreResultResDTO();
        
        rs.setScore(score);
        
        rs.setIsbnCode(pProductInfo.getIsbnCode());
        rs.setBookCode(pProductInfo.getBookCode());
        return rs;
    }

    @Override
    public ScoreResultResDTO saveDeal(String pSourceType, CustInfoReqDTO pCustInfo,
            PayQuartzInfoRequest pOrderInfo) {
    	/*查询用户积分账户，不存在则创建*/
        ScoreInfoResDTO scoreRes = scoreInfoRSV.findScoreInfoByCustAndCreate(pCustInfo);
        ScoreInfoReqDTO scoreInfo = new ScoreInfoReqDTO();
        ObjectCopyUtil.copyObjValue(scoreRes, scoreInfo, null, false);
        ScoreResultResDTO result = new ScoreResultResDTO();
        result.setScore(0L);
        result.setActionType(pSourceType);
        result.setOptType(StaffConstants.Score.SCORE_OPT_TYPE_1);
        if (StringUtil.isNotBlank(pOrderInfo.getOrderId())) {
            GdsInterfaceGdsReqDTO gdsReq = new GdsInterfaceGdsReqDTO();
            gdsReq.setOrigin(PmphGdsDataImportConstants.Commons.ORIGIN_ERP);
            gdsReq.setOriginGdsId(pOrderInfo.getOrderId());
            GdsInterfaceGdsRespDTO resDto = gdsInterfaceGdsRSV.queryGdsInterfaceGdsByOriginGdsId(gdsReq);
        	GdsInfoReqDTO gdsInfoReq = new GdsInfoReqDTO();
        	gdsInfoReq.setId(resDto.getGdsId());
        	GdsInfoRespDTO resp = gdsInfoQueryRSV.queryGdsInfoByOption(gdsInfoReq);
        	//这里注意：ISBN号用orderId字段来存储的
            result.setRemark("本版编号【"+pOrderInfo.getOrderId()+"】【"+resp.getGdsName()+"】【"+resp.getIsbn()+"】一书一码赠送积分");
        }
        
        ProductInfoReqDto product = new ProductInfoReqDto();
        product.setIsbnCode(pOrderInfo.getOrderId());//这里注意：ISBN号用orderId字段来存储的
        product.setBookCode(pOrderInfo.getDealFlag());//这里注意：书码用dealFlag字段来存储的
        product.setProductPrice(pOrderInfo.getPayment());//金额
        //清空这两个字段值
        pOrderInfo.setOrderId("");
        pOrderInfo.setDealFlag("");
        ScoreResultResDTO score = scoreCaclRSV.saveDoCaclScore(result, pCustInfo,scoreInfo, product);
        if(score!= null &&score.getScore() > 0){
            result.setScore(score.getScore());
            //更新积分账户信息
            scoreInfoRSV.updateScoreInfo(pCustInfo, scoreInfo, result);
        }   
        return score;
    }

    @Override
    public Map<Integer, String> judgeActionPara(ScoreFuncDefReqDTO fun,
            Map<Long, HashMap<Integer, String>> paraMap, CustInfoReqDTO pCustInfo,ProductInfoReqDto productInfo) {
        //遍历函数参数Map容器
        Iterator<Entry<Long, HashMap<Integer, String>>> entryKeyIterator =  paraMap.entrySet().iterator();
        Map<Integer, String> para = null;
        while(entryKeyIterator.hasNext()){
            Entry<Long, HashMap<Integer, String>> e = entryKeyIterator.next();
            para = e.getValue();
            if(para.get(0).equals(fun.getActionType()) && para.get(1).equals(fun.getFuncName())){
                return para;
            }
        }
        return null;
    }

}
