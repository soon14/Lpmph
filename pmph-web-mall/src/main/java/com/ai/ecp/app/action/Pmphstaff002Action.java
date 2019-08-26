package com.ai.ecp.app.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.ai.ecp.app.req.Pmphstaff002Req;
import com.ai.ecp.app.resp.Pmphstaff002Resp;
import com.ai.ecp.app.util.SsoUtil;
import com.ai.ecp.base.util.AppUserCacheUtils;
import com.ai.ecp.server.front.dto.BaseStaff;
import com.ai.ecp.staff.dubbo.dto.CustInfoReqDTO;
import com.ai.ecp.staff.dubbo.dto.CustInfoResDTO;
import com.ai.ecp.staff.dubbo.interfaces.ICustManageRSV;
import com.ai.ecp.staff.dubbo.util.StaffConstants;
import com.ai.paas.utils.ObjectCopyUtil;
import com.ai.paas.utils.StringUtil;
import com.ailk.butterfly.app.annotation.Action;
import com.ailk.butterfly.core.exception.BusinessException;
import com.ailk.butterfly.core.exception.SystemException;

import net.sf.json.JSONObject;

/**
 * Project Name:ecp-web-mall <br>
 * Description: 用户注册<br>
 * Date:2016-2-22下午6:51:19  <br>
 * 
 * @version  
 * @since JDK 1.6 
 */

@Service("pmphstaff002")
@Action(bizcode="pmphstaff002", userCheck=false)
@Scope("prototype")
public class Pmphstaff002Action extends AppBaseAction<Pmphstaff002Req, Pmphstaff002Resp> {

    private static String MODULE = Pmphstaff002Action.class.getName();
    
    @Resource
    private ICustManageRSV custManageRSV;
    @Override
    protected void getResponse() throws BusinessException, SystemException, Exception {
    	
    	Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("username", SsoUtil.UserName);
		map.put("password", SsoUtil.PassWord);
		map.put("language", SsoUtil.language);
		
		map.put("method", "ZAS.MobileRegist");
		JSONObject value = new JSONObject();
		value.put("Mobile", this.request.getSerialNumber());
		value.put("SMSCode", this.request.getSmsCode());
		value.put("Password", this.request.getStaffPassword());
		value.put("PwdIsEncryption", "");
		map.put("params", value.toString());
		JSONObject jsonObject  = SsoUtil.postAuthSso("/api/json", map);
    	boolean flag = jsonObject.getBoolean("Success");
    	if(flag){
    		
    	   	   /*1、获取入参*/
            CustInfoReqDTO req = new CustInfoReqDTO();
            ObjectCopyUtil.copyObjValue(this.request, req, null, false);
            req.setCustType(StaffConstants.custInfo.CUST_TYPE_P);
            
            /*2、调用业务接口，保存注册用户信息*/
            long record = custManageRSV.saveCustInfo(req);
            
            /*注册成功，带出用户详细信息*/
            if (record > 0) {
                CustInfoReqDTO custReq = new CustInfoReqDTO();
                custReq.setStaffCode(this.request.getStaffCode());
                CustInfoResDTO custRes = custManageRSV.findCustInfo(custReq);
                BaseStaff baseStaff = new BaseStaff();
                baseStaff.setId(custRes.getId());
                baseStaff.setStaffCode(custRes.getStaffCode());
                baseStaff.setStaffLevelCode(custRes.getCustLevelCode());
                baseStaff.setStaffClass(custRes.getCustType());
                String tocken = AppUserCacheUtils.saveUser2Cache(baseStaff);
                
                /*3、返回结果,，往缓存里放入一个登录授权key*/
                this.response.setTocken(tocken);
                this.response.setSuccess("ok");
               // this.response.setMessage(jsonObject.getString("Message"));
            }
    	}else{
    		this.response.setSuccess("fail");
    		if(jsonObject.containsKey("_Message")){
    			this.response.setMessage(jsonObject.getString("_Message"));
    		}else if(jsonObject.containsKey("Message")){
    			this.response.setMessage(jsonObject.getString("Message"));
    		}else{
    			this.response.setMessage("注册失败，请检查网络");
    		}
    		
    	}
 
    }

}

