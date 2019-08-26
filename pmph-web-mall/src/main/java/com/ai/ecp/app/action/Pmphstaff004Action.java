package com.ai.ecp.app.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.ai.ecp.app.req.Pmphstaff004Req;
import com.ai.ecp.app.resp.Pmphstaff004Resp;
import com.ai.ecp.app.util.SsoUtil;
import com.ai.ecp.staff.dubbo.dto.AuthStaffReqDTO;
import com.ai.ecp.staff.dubbo.interfaces.IAuthStaffRSV;
import com.ailk.butterfly.app.annotation.Action;
import com.ailk.butterfly.core.exception.BusinessException;
import com.ailk.butterfly.core.exception.SystemException;

import net.sf.json.JSONObject;

/**
 * Project Name:ecp-web-mall <br>
 * Description: 用户修改密码<br>
 * Date:2016-2-22下午6:51:19  <br>
 * 
 * @version  
 * @since JDK 1.6 
 */

@Service("pmphstaff004")
@Action(bizcode="pmphstaff004", userCheck=true)
@Scope("prototype")
public class Pmphstaff004Action extends AppBaseAction<Pmphstaff004Req, Pmphstaff004Resp> {

    private static String MODULE = Pmphstaff004Action.class.getName();
    
    @Resource
    private IAuthStaffRSV authStaffRSV;
    
    @Override
    protected void getResponse() throws BusinessException, SystemException, Exception {
    	  /*1、获取入参*/
        AuthStaffReqDTO req = new AuthStaffReqDTO();
        req.setId(req.getStaff().getId());//用户id 
        req.setStaffPasswd(this.request.getNewPwd());//新密码
        req.setStaffPwdOld(this.request.getOldPwd());//旧密码
        /*2、调用sso接口*/
        Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("username", SsoUtil.UserName);
		map.put("password", SsoUtil.PassWord);
		
		map.put("method", "ZAS.ModifyUser");
		JSONObject value = new JSONObject();
		value.put("Password", this.request.getNewPwd());
		value.put("LoginID", req.getStaff().getStaffCode());
		map.put("params", value.toString());
		JSONObject jsonObject  = SsoUtil.postAuthSso("/api/json", map);
		String flag = jsonObject.getString("_Status");
		if(flag.equals("1")){
			  /*2、调用业务接口，修改用户密码*/
	       // authStaffRSV.updatePwdById(req); 
	        /*3、返回结果*/
	        this.response.setFlag(true);
		}
      
    }

}

