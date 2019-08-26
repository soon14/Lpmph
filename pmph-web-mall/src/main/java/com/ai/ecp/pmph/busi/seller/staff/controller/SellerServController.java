package com.ai.ecp.pmph.busi.seller.staff.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ai.ecp.base.controller.EcpBaseController;
import com.ai.ecp.im.dubbo.dto.ImStaffHotlineReqDTO;
import com.ai.ecp.im.dubbo.dto.ImStaffHotlineResDTO;
import com.ai.ecp.im.dubbo.interfaces.IStaffHotlineRSV;
import com.ai.ecp.pmph.busi.seller.util.ImConstant;
import com.ai.ecp.server.front.dto.BaseSysCfgRespDTO;
import com.ai.ecp.server.front.util.SysCfgUtil;
import com.ai.ecp.staff.dubbo.dto.CustInfoReqDTO;
import com.ai.ecp.staff.dubbo.dto.CustInfoResDTO;
import com.ai.ecp.staff.dubbo.interfaces.ICustInfoRSV;

/**
 * 卖家中心，客服系统免登录入口
 * @author admin
 *
 */

@Controller
@RequestMapping(value = "/seller/im/serv")
public class SellerServController extends EcpBaseController {

	private static String MODULE = SellerServController.class.getName();

	private static final Logger logger = LoggerFactory.getLogger(SellerServController.class);
	
	@Resource
	private IStaffHotlineRSV staffHotlineRSV;

	@Resource
	private ICustInfoRSV custInfoRSV;

	/**
	 * 实现免登录跳转
	 * 如果此时登录用户为客服，直接转入客服聊天界面
	 * 不是客服的话，给出提示信息，留在卖家中心首页
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/chat")
	public String chat(Model model, HttpServletRequest request) {
		String URL = request.getServerName() + ":" + request.getServerPort() + "/" + request.getContextPath();
		// 获取当前登录用户
		CustInfoReqDTO custReq = new CustInfoReqDTO();
		custReq.setId(custReq.getStaff().getId());
		// 获取用户更多信息
		CustInfoResDTO custRes = custInfoRSV.getCustInfoById(custReq);
		ImStaffHotlineReqDTO dto = new ImStaffHotlineReqDTO();
		dto.setStaffCode(custRes.getStaffCode());
		ImStaffHotlineResDTO hotlineResDTO = staffHotlineRSV.getStaffHotline(dto);
		if (hotlineResDTO == null) {
			model.addAttribute("servChatUrl", "0");
			model.addAttribute("staffCode", "0");
			return "/seller/im/servChat";
		}
		BaseSysCfgRespDTO res = SysCfgUtil.fetchSysCfg(ImConstant.IM_SERV_CHAT_URL);
		String servChatUrl = res.getParaValue();
		model.addAttribute("servChatUrl", servChatUrl);
		model.addAttribute("staffCode", hotlineResDTO.getStaffCode());
		return "/seller/im/servChat";
	}

}
