package com.ai.ecp.pmph.service.busi.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import com.ai.ecp.general.order.dto.PayQuartzInfoRequest;
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


/**
 * 
 * Project Name:ecp-services-staff <br>
 * Description: <br>
 * Date:2015年8月26日下午4:34:26  <br>
 * 
 * @version  
 * @since JDK 1.6
 * 
 * 计算天猫订单积分
 */
public class caclScoreByTMallOrderMoney implements ICaclScore {

    @Resource
    private IScoreInfoRSV scoreInfoRSV;
    
    @Resource
    private IScoreCaclRSV scoreCaclRSV;
    
    public ScoreResultResDTO caclScore(String pSourceType, Map<Integer, String> pParaMap,
            CustInfoReqDTO pCustInfo, ProductInfoReqDto pProductInfo) {
        long score = 0;
        //计算积分：根据天猫订单的金额/赠送单位*每单位赠送积分=获得积分总数
        score = (long) (Math.ceil(pProductInfo.getProductPrice()/Double.valueOf(pParaMap.get(2)))*Double.valueOf(pParaMap.get(3)));
        ScoreResultResDTO rs = new ScoreResultResDTO();
        rs.setScore(score);
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
        result.setRemark("天猫订单【"+pOrderInfo.getOrderId()+"】送积分");
        
        ProductInfoReqDto product = new ProductInfoReqDto();
        product.setProductPrice(pOrderInfo.getPayment());
        product.setOrderId(pOrderInfo.getOrderId());
        ScoreResultResDTO score = scoreCaclRSV.saveDoCaclScore(result, pCustInfo,scoreInfo, product);
        if(score!= null &&score.getScore() > 0)
        {
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
