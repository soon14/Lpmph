package com.ailk.ecp.pmph.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.ecp.pmph.dubbo.interfaces.IScoreCalRSV;
import com.ai.ecp.staff.dubbo.dto.CustInfoReqDTO;
import com.ai.ecp.staff.dubbo.dto.CustInfoResDTO;
import com.ai.ecp.staff.dubbo.dto.ScoreISBNActiveReqDTO;
import com.ai.ecp.staff.dubbo.dto.ScoreInfoResDTO;
import com.ai.ecp.staff.dubbo.interfaces.ICustManageRSV;
import com.ai.ecp.staff.dubbo.interfaces.IScoreInfoRSV;
import com.ailk.aip.security.DefaultServiceCheckChain;
import com.ailk.butterfly.core.annotation.Security;
import com.ailk.butterfly.core.exception.BusinessException;
import com.ailk.butterfly.core.web.BaseController;
import com.ailk.ecp.pmph.util.RespUtil;
import com.ailk.ecp.pmph.vo.IsbnScoreVO;

/**
 * 
 * Project Name:ecp-aip-web Maven Webapp <br>
 * Description: 一书一码激活赠送积分对外接口<br>
 * Date:2016-3-30下午6:51:12  <br>
 * 
 * @version  
 * @since JDK 1.6
 */
@Controller
public class IsbnScoreCalController extends BaseController{
    
    @Resource
    private IScoreCalRSV scoreCalRSV;
    
    @Resource
    private ICustManageRSV custManageRSV;
    
    @Resource
    private IScoreInfoRSV scoreInfoRSV;
    
    @RequestMapping(value="/rest" ,params="method=com.ai.ecp.staff.isbnScoreCal")
    @Security(mustLogin=false,authorCheckType=DefaultServiceCheckChain.class)
    @ResponseBody
    public Map<String,Object> isbnScoreCal(IsbnScoreVO isbnScoreVO) throws BusinessException{
        Map<String,Object> retMap = new HashMap<String,Object>();
        retMap.put("totalScore", 0);
        //组装参数
        ScoreISBNActiveReqDTO req = new ScoreISBNActiveReqDTO();
        req.setBbCode(isbnScoreVO.getBbCode());
        req.setStaffCode(isbnScoreVO.getStaffCode());
        req.setBookCode(isbnScoreVO.getBookCode());
        try{
            //调用业务方法，计算赠送的积分
            long score = this.scoreCalRSV.saveScoreCalForIsbnActive(req);
            retMap.put("score", score);
            retMap.put("resultFlag","1");
            retMap.put("resultMsg", "操作成功");
            //查询用户的总可用积分
            CustInfoReqDTO cust = new CustInfoReqDTO();
            cust.setStaffCode(isbnScoreVO.getStaffCode());
            CustInfoResDTO custRes = this.custManageRSV.findCustInfo(cust);
            if (custRes != null && custRes.getId() != null) {
            	ScoreInfoResDTO scoreRes = this.scoreInfoRSV.findScoreInfoByStaffId(custRes.getId());
            	retMap.put("totalScore", scoreRes.getScoreBalance());
            }
        }catch(Exception e){
            retMap.put("score", 0);
            retMap.put("totalScore", 0);
            retMap.put("resultFlag","0");
            retMap.put("resultMsg", e.getMessage());
        }
        return RespUtil.renderRootResp(retMap);
    }
    
}

