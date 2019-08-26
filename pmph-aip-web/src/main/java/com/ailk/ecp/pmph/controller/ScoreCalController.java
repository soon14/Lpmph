package com.ailk.ecp.pmph.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.ecp.order.dubbo.util.LongUtils;
import com.ai.ecp.pmph.dubbo.interfaces.IScoreCalRSV;
import com.ai.ecp.staff.dubbo.dto.AuthStaffResDTO;
import com.ai.ecp.staff.dubbo.dto.CustInfoReqDTO;
import com.ai.ecp.staff.dubbo.dto.CustInfoResDTO;
import com.ai.ecp.staff.dubbo.dto.ScoreResultResDTO;
import com.ai.ecp.staff.dubbo.dto.SsoUserInfoMsgResDTO;
import com.ai.ecp.staff.dubbo.dto.SsoUserInfoReqDTO;
import com.ai.ecp.staff.dubbo.interfaces.ICustManageRSV;
import com.ai.ecp.staff.dubbo.interfaces.IScoreInfoRSV;
import com.ai.ecp.staff.dubbo.interfaces.ISsoUserImportRSV;
import com.ai.ecp.staff.dubbo.util.StaffConstants;
import com.ai.paas.utils.ObjectCopyUtil;
import com.ai.paas.utils.StringUtil;
import com.ailk.aip.security.DefaultServiceCheckChain;
import com.ailk.butterfly.core.annotation.Security;
import com.ailk.butterfly.core.exception.BusinessException;
import com.ailk.butterfly.core.web.BaseController;
import com.ailk.ecp.pmph.util.RespUtil;
import com.ailk.ecp.pmph.vo.BizContentVO;
import com.alibaba.fastjson.JSONObject;

/**
 * 
 * Project Name:pmph-aip-web Maven Webapp <br>
 * Description: 人卫E教兑换人卫商城积分对外接口<br>
 * Date:2019-3-4下午6:51:12  <br>
 * 
 * @version  
 * @since JDK 1.7
 */
@Controller
public class ScoreCalController extends BaseController{
    
    @Resource
    private ICustManageRSV custManageRSV;
    
    @Resource
    private IScoreInfoRSV scoreInfoRSV;
    
    @Resource
    private IScoreCalRSV scoreCalRSV;
    
    @Resource
    private ISsoUserImportRSV ssoUserImportRSV;
    
    @RequestMapping(value="/rest" ,params="method=com.ai.ecp.pmph.staff.scoreCal")
    @Security(mustLogin=false,authorCheckType=DefaultServiceCheckChain.class)
    @ResponseBody
    public Map<String,Object> scoreCal(BizContentVO bizContentVO) throws BusinessException{
        Map<String,Object> retMap = new HashMap<String,Object>();
        
        String bizContent=bizContentVO.getBiz_content();
        
        //1.判断入参是否为空，为空返回错误信息
        if(StringUtil.isBlank(bizContent)){
            retMap.put(RespUtil.CODE, "2");
            retMap.put(RespUtil.MSG, "参数不允许为空");
            return RespUtil.renderRootResp(retMap);
        }
        JSONObject jsonObject=JSONObject.parseObject(bizContentVO.getBiz_content());
        String staffCode=jsonObject.getString("staff_code");
        if (StringUtil.isBlank(staffCode)) {
        	retMap.put(RespUtil.CODE, "2");
        	retMap.put(RespUtil.MSG, "[staff_code]参数不允许为空");
        	return RespUtil.renderRootResp(retMap); 
        }
        Long score=jsonObject.getLong("score");
        if (LongUtils.isEmpty(score)) {
            retMap.put(RespUtil.CODE, "2");
            retMap.put(RespUtil.MSG, "[score]参数不允许为空");
            return RespUtil.renderRootResp(retMap); 
        }

        try{
            //查询用户的总可用积分
            CustInfoReqDTO cust = new CustInfoReqDTO();
            cust.setStaffCode(staffCode);
            CustInfoResDTO custRes = this.custManageRSV.findCustInfo(cust);
            
            //处理员工编号,当获取不到会员信息，新增会员信息
            Long createStaff = custRes.getId();
            if (createStaff==null) {
                SsoUserInfoReqDTO infoReqDTO=new SsoUserInfoReqDTO();
                infoReqDTO.setUserName(staffCode);
                SsoUserInfoMsgResDTO msgResDTO= ssoUserImportRSV.saveStaffInfo(infoReqDTO);
                if (!msgResDTO.isFlag()) {
                    retMap.put(RespUtil.CODE, "2");
                    retMap.put(RespUtil.MSG, "[staff_code:"+staffCode+"]参数非法，请核实该参数值");
                    return RespUtil.renderRootResp(retMap); 
                }
            }
            
            custRes = this.custManageRSV.findCustInfo(cust);
            
            if (custRes != null && custRes.getId() != null) {
            	ObjectCopyUtil.copyObjValue(custRes, cust, null, false);
            	ScoreResultResDTO scoreResult = new ScoreResultResDTO();
            	// 人卫E教兑换人卫商城积分为  收入积分  增
            	scoreResult.setOptType(StaffConstants.Score.SCORE_OPT_TYPE_1);
            	scoreResult.setScore(score);
            	// 更新账户信息，新增操作日志
            	this.scoreCalRSV.updateScoreForE(cust,scoreResult);
            	retMap.put(RespUtil.CODE,"0");
                retMap.put(RespUtil.MSG, "操作成功");
            	
            } else {
            	retMap.put(RespUtil.CODE,"2");
                retMap.put(RespUtil.MSG, "通过本版编号找不到商品");
			}
            
        }catch(Exception e){
            retMap.put(RespUtil.CODE,"2");
            retMap.put(RespUtil.MSG, e.getMessage());
        }
        return RespUtil.renderRootResp(retMap);
    }
    
}

