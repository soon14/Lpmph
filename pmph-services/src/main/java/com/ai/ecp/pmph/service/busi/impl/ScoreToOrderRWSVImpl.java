package com.ai.ecp.pmph.service.busi.impl;

import java.math.BigDecimal;

import javax.annotation.Resource;

import com.ai.ecp.pmph.dubbo.dto.OrderBackMainRWReqDTO;
import com.ai.ecp.pmph.dubbo.dto.ROrdReturnRefundResp;
import com.ai.ecp.pmph.service.busi.interfaces.IScoreToOrderRWSV;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.ecp.server.front.util.BaseParamUtil;
import com.ai.ecp.staff.dao.model.ScoreExchange;
import com.ai.ecp.staff.dao.model.ScoreInfo;
import com.ai.ecp.staff.dao.model.ScoreOptLog;
import com.ai.ecp.staff.dubbo.dto.OrderBackMainReqDTO;
import com.ai.ecp.staff.dubbo.dto.OrderBackSubReqDTO;
import com.ai.ecp.staff.dubbo.dto.ScoreExchangeReqDTO;
import com.ai.ecp.staff.dubbo.dto.ScoreExchangeResDTO;
import com.ai.ecp.staff.dubbo.dto.ScoreSourceReqDTO;
import com.ai.ecp.staff.dubbo.dto.ScoreSourceResDTO;
import com.ai.ecp.staff.dubbo.util.StaffConstants;
import com.ai.ecp.staff.service.busi.acct.interfaces.IAcctToOrderSV;
import com.ai.ecp.staff.service.busi.score.interfaces.IScoreInfoSV;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.StringUtil;
import com.alibaba.dubbo.common.utils.CollectionUtils;

/**
 * 
 * Project Name:pmph-services-server <br>
 * Description: 针对人卫退货需求，积分服务改造<br>
 * Date:2016年9月1日下午3:37:04 <br>
 * 
 * @version
 * @since JDK 1.6
 */
public class ScoreToOrderRWSVImpl implements IScoreToOrderRWSV {

	private static final String MODULE = ScoreToOrderRWSVImpl.class.getName();

	@Resource
	private IScoreInfoSV scoreInfoSV;

	@Resource
	private IAcctToOrderSV acctToOrderSV;

	@Override
	public Long selTotalScoreByBackOrderRW(OrderBackMainReqDTO<OrderBackSubReqDTO> req) throws BusinessException {
		long orderScore = 0L;// 订单获得的总积分
		long backScore = 0l;// 回退的总积分
		ROrdReturnRefundResp o = new ROrdReturnRefundResp();
		/* 1、根据主订单id，用户staffId，查询出所有的积分来源记录 */
		PageResponseDTO<ScoreSourceResDTO> spage = null;
		if (StringUtil.isNotBlank(req.getOrderId())) {
			ScoreSourceReqDTO sourceReq = new ScoreSourceReqDTO();
			sourceReq.setStaffId(req.getStaffId());
			sourceReq.setOrderId(req.getOrderId());
			sourceReq.setPageNo(0);
			spage = scoreInfoSV.listScoreSource(sourceReq);
			if (spage == null || CollectionUtils.isEmpty(spage.getResult())) {
				LogUtil.info(MODULE, "根据用户以及订单信息：[" + req.toString() + "]没有查询到对应的积分来源记录，无需进行业务操作。");
				return 0L;
			}
		} else {
			return 0L;
		}
		//积分汇总
		for (ScoreSourceResDTO sourceRes : spage.getResult()) {
			if (StringUtil.isNotBlank(sourceRes.getSubOrderId())) {
				orderScore += sourceRes.getScore();
			}
		}
		//是否为最后一笔
		if (req.isLastFlag()) {
			ScoreSourceResDTO sourceRes = new ScoreSourceResDTO();
			sourceRes.setStaffId(req.getStaffId());
			sourceRes.setOrderId(req.getOrderId());
			backScore = orderScore - this.getLastScoreForAddBack(sourceRes);
			o.setReduCulateScore(backScore);
		} else {
			// 回退的积分
			backScore = orderScore * req.getScale() / 1000000;
			o.setReduCulateScore(backScore);
		}
		ScoreInfo scoreInfo = scoreInfoSV.findScoreInfoByStaffId(req.getStaffId());
		//查询出账户是否可以扣为负的开关
        String zeroFlag = BaseParamUtil.fetchParamValue("SUBTRACT_ZERO_FLAG", "BELOW_ZERO");
        //可用积分不足时
        if (scoreInfo != null && scoreInfo.getScoreBalance() - backScore < 0L) {
        	//如果配置了最多扣为0，则这里退的积分最后只能是可用积分
        	if (StaffConstants.Score.SUBTRACT_TO_ZERO.equals(zeroFlag)) {
        		backScore = scoreInfo.getScoreBalance();
        	}
        }

		return backScore;
	}

	@Override
	public Long saveScoreFrozenForOrderBackRW(OrderBackMainReqDTO<OrderBackSubReqDTO> req) throws BusinessException {
		Long mainOrderScore = selTotalScoreByBackOrderRW(req);
		ScoreInfo scoreInfo = scoreInfoSV.findScoreInfoByStaffId(req.getStaffId());
		/* 1、记录积分操作日志 */
		ScoreOptLog log = new ScoreOptLog();
		log.setTotalScore(scoreInfo.getScoreTotal());// 操作前，总积分
		log.setBalanceScore(scoreInfo.getScoreBalance());// 操作前，可用积分
		log.setFreezeScore(scoreInfo.getScoreFrozen());// 操作前，冻结积分
		log.setUsedScore(scoreInfo.getScoreUsed());// 操作前，已使用积分
		log.setScore(mainOrderScore);// 本次操作的积分
		log.setStaffId(req.getStaffId());// 用户ID
		log.setRemark("订单退货，冻结已获得的积分。");// 备注
		log.setOrderId(req.getOrderId());// 订单编码
		log.setOptType(StaffConstants.Score.SCORE_OPT_TYPE_4);// 操作类型
		log.setCreateStaff(String.valueOf(req.getStaffId()));// 操作人
		log.setSiteId(req.getSiteId());// 站点id
		scoreInfoSV.saveScoreOptLog(log);

		/* 2、更新用户的积分账户，把积分冻结 */

		if (scoreInfo != null) {
			BigDecimal balance = new BigDecimal(scoreInfo.getScoreBalance());
			BigDecimal freeze = new BigDecimal(scoreInfo.getScoreFrozen());
			balance = balance.subtract(new BigDecimal(mainOrderScore));// 可用积分减少
			freeze = freeze.add(new BigDecimal(mainOrderScore));// 冻结积分增加
			scoreInfo.setScoreBalance(balance.longValue());
			scoreInfo.setScoreFrozen(freeze.longValue());
			scoreInfoSV.updateScoreInfo(scoreInfo);
		}

		return mainOrderScore;
	}

	@Override
	public boolean saveScoreAddForOrderBackRW(OrderBackMainRWReqDTO<OrderBackSubReqDTO> req) throws BusinessException {
		LogUtil.info(MODULE, "=========订单同意退货时，把冻结的积分解冻，并做扣除  START==========");
        ScoreInfo scoreInfo = scoreInfoSV.findScoreInfoByStaffId(req.getStaffId());
        
        /*1、记录一条订单的积分消费记录*/
        ScoreExchange mainExchange = new ScoreExchange();
        mainExchange.setStaffId(req.getStaffId());
        mainExchange.setCreateStaff(req.getStaffId());
        mainExchange.setOrderId(req.getOrderId());
        mainExchange.setScoreUsing(req.getModifyBackSocre());
        mainExchange.setSiteId(req.getSiteId());//站点id
        if ("0".equals(req.getRefundOrBack())) {
        	mainExchange.setRemark("订单退款，扣减该订单获得的积分。");
        } else {
        	mainExchange.setRemark("订单退货，扣减该订单获得的积分。");
        }
        
        mainExchange.setBackFlag(StaffConstants.Score.SCORE_BACK_FLAG_2);
        mainExchange.setExchangeMode(StaffConstants.Score.SCORE_EXCHANGE_MODE_2);//退货，退款
        scoreInfoSV.saveScoreExchange(mainExchange);
        
        /*2、记录一条订单积分操作日志*/
        ScoreOptLog log = new ScoreOptLog();
        log.setTotalScore(scoreInfo.getScoreTotal());//操作前，总积分
        log.setBalanceScore(scoreInfo.getScoreBalance());//操作前，可用积分
        log.setFreezeScore(scoreInfo.getScoreFrozen());//操作前，冻结积分
        log.setUsedScore(scoreInfo.getScoreUsed());//操作前，已使用积分
        log.setScore(req.getModifyBackSocre());//本次操作的积分
        log.setStaffId(req.getStaffId());//用户ID
        if ("0".equals(req.getRefundOrBack())) {
        	log.setRemark("订单退款，订单之前冻结的积分解冻，并扣除。");//备注
        } else {
        	log.setRemark("订单退货，订单之前冻结的积分解冻，并扣除。");//备注
        }
        log.setOrderId(req.getOrderId());//订单编码
        log.setOptType(StaffConstants.Score.SCORE_OPT_TYPE_3);//操作类型
        log.setCreateStaff(String.valueOf(req.getStaffId()));//操作人
        log.setSiteId(req.getSiteId());//站点id
        scoreInfoSV.saveScoreOptLog(log);
        
        /*3、更新用户的积分账户，把积分解冻，并作扣除，当成已使用
         * 冻结积分减少，已使用积分增加*/
        if (scoreInfo != null) {
            BigDecimal use = new BigDecimal(scoreInfo.getScoreUsed());
            BigDecimal freeze = new BigDecimal(scoreInfo.getScoreFrozen());
            freeze = freeze.subtract(new BigDecimal(req.getBackScore()));//冻结积分减少
            use = use.add(new BigDecimal(req.getModifyBackSocre()));//已使用积分增加
            scoreInfo.setScoreUsed(use.longValue());
            scoreInfo.setScoreFrozen(freeze.longValue());
            scoreInfoSV.updateScoreInfo(scoreInfo);
        }
        LogUtil.info(MODULE, "========订单同意退货时，把冻结的订单所获得的积分解冻，并做扣除  END==========");
        return true;
	}

	/**
	 * 
	 * getLastScoreForAddBack:(因退货退款，订单获得的积分已退了多少). <br/>
	 * 
	 * @param sourceRes
	 * @return
	 * @since JDK 1.6
	 */
	private long getLastScoreForAddBack(ScoreSourceResDTO sourceRes) {
		ScoreExchangeReqDTO backReq = new ScoreExchangeReqDTO();
		backReq.setStaffId(sourceRes.getStaffId());
		backReq.setOrderId(sourceRes.getOrderId());
		backReq.setPageNo(0);
		backReq.setBackFlag(StaffConstants.Score.SCORE_BACK_FLAG_2);
		PageResponseDTO<ScoreExchangeResDTO> exchangePage = scoreInfoSV.listScoreExchange(backReq);
		long score = 0;
		if (exchangePage != null && CollectionUtils.isNotEmpty(exchangePage.getResult())) {
			for (ScoreExchangeResDTO ex : exchangePage.getResult()) {
				score += ex.getScoreUsing();
			}
		}
		return score;
	}

	@Override
	public Long saveScoreFrozenModifyForOrderBackRW(OrderBackMainRWReqDTO<OrderBackSubReqDTO> req)
			throws BusinessException {
		
		ScoreInfo scoreInfo = scoreInfoSV.findScoreInfoByStaffId(req.getStaffId());
		ScoreOptLog log = new ScoreOptLog();
		log.setTotalScore(scoreInfo.getScoreTotal());// 操作前，总积分
		log.setBalanceScore(scoreInfo.getScoreBalance());// 操作前，可用积分
		log.setFreezeScore(scoreInfo.getScoreFrozen());// 操作前，冻结积分
		log.setUsedScore(scoreInfo.getScoreUsed());// 操作前，已使用积分
		log.setStaffId(req.getStaffId());// 用户ID
		log.setOrderId(req.getOrderId());// 订单编码
		log.setCreateStaff(String.valueOf(req.getStaffId()));// 操作人
		log.setSiteId(req.getSiteId());// 站点id
		//如果金额（积分）有调整，则调用
		if (req.getBackScore() != null && req.getModifyBackSocre() != null && req.getBackScore() != req.getModifyBackSocre()) {
			/*1、调增，冻结积分要加上差额，可用积分减去差额*/
			if (req.getModifyBackSocre() > req.getBackScore()) {
				long scoreAdd = req.getModifyBackSocre() - req.getBackScore();
				//查询是否可以扣为负
				String zeroFlag = BaseParamUtil.fetchParamValue("SUBTRACT_ZERO_FLAG", "BELOW_ZERO");
				//如果为0，则表示最多扣为0，不扣为负，此处要比较可用积分是否够
		    	if (StaffConstants.Score.SUBTRACT_TO_ZERO.equals(zeroFlag)) {
		    		if (scoreInfo.getScoreBalance() < scoreAdd) {
		    			scoreAdd = scoreInfo.getScoreBalance();//如果积分不足，则最多可扣当前的所有可用积分
		    		}
		    		log.setScore(scoreAdd);// 本次操作的积分
		    		log.setRemark("订单退货，调增金额，冻结积分差额");// 备注
	    			log.setOptType(StaffConstants.Score.SCORE_OPT_TYPE_4);// 操作类型
	    			scoreInfoSV.saveScoreOptLog(log);
		    	}
		    	/*1.2 更新用户的积分账户，把差额积分冻结*/
		    	BigDecimal balance = new BigDecimal(scoreInfo.getScoreBalance());
				BigDecimal freeze = new BigDecimal(scoreInfo.getScoreFrozen());
				balance = balance.subtract(new BigDecimal(scoreAdd));// 可用积分减少
				freeze = freeze.add(new BigDecimal(scoreAdd));// 冻结积分增加
				scoreInfo.setScoreBalance(balance.longValue());
				scoreInfo.setScoreFrozen(freeze.longValue());
				scoreInfoSV.updateScoreInfo(scoreInfo);
				/*2、调减，冻结积分减去差额，可用积分加上差额*/
			} else if (req.getModifyBackSocre() < req.getBackScore()) {
				long scoreReduce = req.getBackScore() - req.getModifyBackSocre();
				log.setScore(scoreReduce);// 本次操作的积分
	    		log.setRemark("订单退货，调减金额，解冻积分差额");// 备注
    			log.setOptType(StaffConstants.Score.SCORE_OPT_TYPE_4);// 操作类型
    			scoreInfoSV.saveScoreOptLog(log);
    			/*2.2 更新用户的积分账户，把差额积分解冻*/
		    	BigDecimal balance = new BigDecimal(scoreInfo.getScoreBalance());
				BigDecimal freeze = new BigDecimal(scoreInfo.getScoreFrozen());
				balance = balance.add(new BigDecimal(scoreReduce));// 可用积分增加
				freeze = freeze.subtract(new BigDecimal(scoreReduce));// 冻结积分减少
				scoreInfo.setScoreBalance(balance.longValue());
				scoreInfo.setScoreFrozen(freeze.longValue());
				scoreInfoSV.updateScoreInfo(scoreInfo);
			}
		}
		//如果调减，则返回负
		if (req.getModifyBackSocre() < req.getBackScore()) {
			return -log.getScore();
		} else {
			return log.getScore();
		}
	}

	@Override
	public long selTotalScoreByBackOrderAllRW(OrderBackMainReqDTO<OrderBackSubReqDTO> req) throws BusinessException {
		long orderScore = 0L;// 订单获得的总积分
		long backScore = 0l;// 回退的总积分
		/* 1、根据主订单id，用户staffId，查询出所有的积分来源记录 */
		PageResponseDTO<ScoreSourceResDTO> spage = null;
		if (StringUtil.isNotBlank(req.getOrderId())) {
			ScoreSourceReqDTO sourceReq = new ScoreSourceReqDTO();
			sourceReq.setStaffId(req.getStaffId());
			sourceReq.setOrderId(req.getOrderId());
			sourceReq.setPageNo(0);
			spage = scoreInfoSV.listScoreSource(sourceReq);
			if (spage == null || CollectionUtils.isEmpty(spage.getResult())) {
				LogUtil.info(MODULE, "根据用户以及订单信息：[" + req.toString() + "]没有查询到对应的积分来源记录，无需进行业务操作。");
				return 0L;
			}
		} else {
			return 0L;
		}
		//积分汇总
		for (ScoreSourceResDTO sourceRes : spage.getResult()) {
			if (StringUtil.isNotBlank(sourceRes.getSubOrderId())) {
				orderScore += sourceRes.getScore();
			}
		}
		//是否为最后一笔
		if (req.isLastFlag()) {
			ScoreSourceResDTO sourceRes = new ScoreSourceResDTO();
			sourceRes.setStaffId(req.getStaffId());
			sourceRes.setOrderId(req.getOrderId());
			backScore = orderScore - this.getLastScoreForAddBack(sourceRes);
		} else {
			// 回退的积分
			backScore = orderScore * req.getScale() / 1000000;
		}
        return backScore;
	}
}
