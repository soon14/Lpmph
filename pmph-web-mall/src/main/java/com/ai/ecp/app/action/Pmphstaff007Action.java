package com.ai.ecp.app.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.ai.ecp.app.req.Pmphstaff007Req;
import com.ai.ecp.app.resp.Pmphstaff007Resp;
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
 * 
 * Project Name:pmph-web-mall <br>
 * Description: 第三方登录：通过第三方帐号查询是否已经有了帐号<br>
 * Date:2017年2月16日下午2:19:45  <br>
 * 
 * @version  
 * @since JDK 1.6
 */

@Service("pmphstaff007")
@Action(bizcode="pmphstaff007", userCheck=false)
@Scope("prototype")
public class Pmphstaff007Action extends AppBaseAction<Pmphstaff007Req, Pmphstaff007Resp> {

	 @Resource
	 private ICustManageRSV custManageRSV;
    
    @Override
    protected void getResponse() throws BusinessException, SystemException, Exception {
    	Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("username", SsoUtil.UserName);
		map.put("password", SsoUtil.PassWord);
		map.put("language", SsoUtil.language);
		map.put("method", "ZASSocial.SocialBind");
		JSONObject value = new JSONObject();
		value.put("PlatCode", this.request.getPlatCode());//平台编码类型
		value.put("PlatUID", this.request.getPlatUID());//平台唯一ID
		value.put("NoBindTactics", "Report");//当没有找到用户绑定信息时：只返回报告
		//有这个id时，需要传入
		if (StringUtil.isNotBlank(this.request.getPublicPlatUID())) {
			value.put("PublicPlatUID", this.request.getPublicPlatUID());//公用平台用户唯一ID
		}
		map.put("params", value.toString());
		
		/*调用sso接口*/
		JSONObject jsonObject  = SsoUtil.postAuthSso("/api/json", map);
		
		boolean flag = jsonObject.getBoolean("Success");
		if(flag){
		    this.response.setSuccess("ok");
		    /*调用成功，查询在我们系统中，是否存在该用户*/
		    JSONObject userInfo = jsonObject.getJSONObject("UserData");
		    String staffCode = userInfo.getString("UserName");//用户名，即staffCode
		    CustInfoReqDTO custInfoReq = new CustInfoReqDTO();
		    custInfoReq.setStaffCode(staffCode);
		    CustInfoResDTO res = custManageRSV.findCustInfo(custInfoReq);
		    //如果系统中已经存在该用户，则返回用户信息并做登录处理
		    if (res != null && res.getId() != null && res.getId() != 0L) {
		    	//返回用户信息
		    	ObjectCopyUtil.copyObjValue(res, this.response, null, false);
		    	//登录
		    	BaseStaff basestaff = new BaseStaff();
	            basestaff.setId(res.getId());
	            basestaff.setStaffCode(res.getStaffCode());
	            basestaff.setStaffLevelCode(res.getCustLevelCode());
	            basestaff.setStaffClass(res.getCustType());
	            String tocken = AppUserCacheUtils.saveUser2Cache(basestaff);
	            this.response.setTocken(tocken);
		    }
		}else{
			this.response.setSuccess("fail");
			this.response.setMessage(jsonObject.getString("Message"));
		}
    	
    }

}

