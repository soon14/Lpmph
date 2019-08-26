package com.ai.ecp.common.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.ecp.base.controller.EcpBaseController;
import com.ai.ecp.base.vo.EcpBasePageReqVO;
import com.ai.ecp.base.vo.EcpBaseResponseVO;
import com.ai.ecp.common.vo.ResetPwdVO;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.ecp.util.SsoUtil;
import com.ai.paas.utils.StringUtil;

import net.sf.json.JSONObject;

/**
 * 
 * Project Name:pmph-web-mobile <br>
 * Description: 忘记密码功能<br>
 * Date:2016年12月8日下午5:56:00 <br>
 * 
 * @version
 * @since JDK 1.6
 */

@Controller
@RequestMapping(value = "/forget")
public class ForgetPwdController extends EcpBaseController {

	@RequestMapping(value = "/index")
	public String index(Model model, EcpBasePageReqVO vo) throws Exception {
		return "/common/forget/forget-pwd";
	}

	/**
	 * 
	 * savePwd:(重置密码). <br/>
	 * 
	 * @param vo
	 * @return
	 * @throws BusinessException
	 * @since JDK 1.6
	 */
	@RequestMapping(value = "/resetpwd")
	@ResponseBody
	public EcpBaseResponseVO resetPwd(ResetPwdVO vo) throws BusinessException {
		EcpBaseResponseVO res = new EcpBaseResponseVO();
		if (StringUtil.isBlank(vo.getMobile()) || StringUtil.isBlank(vo.getCheckCode()) || StringUtil.isBlank(vo.getNewPwd())) {
			res.setResultMsg("请确认信息填写正确");
			res.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_FAILURE);
			return res;
		}

		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("username", SsoUtil.UserName);
		map.put("password", SsoUtil.PassWord);
		map.put("language", SsoUtil.language);
		
		map.put("method", "ZAS.ResetPwd");
		
		JSONObject value = new JSONObject();
		value.put("VerifyMode", "Code");//验证类型
		value.put("VerifyValue", vo.getCheckCode());//验证码
		value.put("AddressType", "Mobile");//找回密码的类型
		value.put("Address", vo.getMobile());//手机号码 
		value.put("NewPwd", vo.getNewPwd());//新密码
		map.put("params", value.toString());
		JSONObject jsonObject  = SsoUtil.postAuthSso("/api/json", map);
		boolean flag = jsonObject.getBoolean("Success");
		if (flag) {
			res.setResultMsg("密码重置成功！");
			res.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_SUCCESS);
		} else {
			res.setResultMsg(jsonObject.getString("Message"));
			res.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_FAILURE);
		}
		
		return res;
	}

}
