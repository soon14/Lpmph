package com.ai.ecp.pmph.busi.pub.controller;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.ai.ecp.base.controller.EcpBaseController;
import com.ai.ecp.base.mvc.annotation.NativeJson;
import com.ai.ecp.base.vo.EcpBaseResponseVO;
import com.ai.ecp.busi.seller.order.vo.RFileImportReqVO;
import com.ai.ecp.general.order.dto.ROrdCartsChkResponse;
import com.ai.ecp.order.dubbo.dto.PubOrderZdRequsetDTO;
import com.ai.ecp.order.dubbo.dto.RFileImportRequest;
import com.ai.ecp.order.dubbo.dto.ROrdZDResponse;
import com.ai.ecp.order.dubbo.dto.ROrderDetailsRequest;
import com.ai.ecp.order.dubbo.dto.RPreOrdMainResponse;
import com.ai.ecp.order.dubbo.dto.RPreOrdMainsResponse;
import com.ai.ecp.order.dubbo.dto.RPreOrdZDResponse;
import com.ai.ecp.order.dubbo.dto.RSumbitZDMainsRequest;
import com.ai.ecp.order.dubbo.interfaces.IOrdMainRSV;
import com.ai.ecp.order.dubbo.interfaces.IPubOrderZdRSV;
import com.ai.ecp.order.dubbo.interfaces.IZDOrdMainRSV;
import com.ai.ecp.order.dubbo.util.OrdConstants;
import com.ai.ecp.order.dubbo.util.PubOrdConstants;
import com.ai.ecp.pmph.dubbo.dto.ROrdZDImportLogResp;
import com.ai.ecp.pmph.dubbo.interfaces.IOrdImportZDMainRSV;
import com.ai.ecp.server.front.dto.BaseInfo;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.paas.utils.CacheUtil;
import com.ai.paas.utils.DateUtil;
import com.ai.paas.utils.FileUtil;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.ObjectCopyUtil;
import com.ai.paas.utils.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping(value="/puborder")
public class PubOrdController extends EcpBaseController {

	private static final String MODULE = PubOrdController.class.getName();

	@Resource
	private IPubOrderZdRSV pubOrderZdRSV;
	
	@Resource
    private IOrdMainRSV ordMainRSV;

	/**
	 * 
	 * cancelOrd:(取消订单). <br/> 
	 * TODO(这里描述这个方法适用条件 – 可选).<br/> 
	 * TODO(这里描述这个方法的执行流程 – 可选).<br/> 
	 * TODO(这里描述这个方法的使用方法 – 可选).<br/> 
	 * TODO(这里描述这个方法的注意事项 – 可选).<br/> 
	 * 
	 * @author myf 
	 * @param orderId
	 * @param oper
	 * @return 
	 * @since JDK 1.7
	 */
    @RequestMapping(value="/cancel")
    @ResponseBody
    public EcpBaseResponseVO cancelOrd(@RequestParam(value="pubOrderId")String pubOrderId,@RequestParam(value="oper")String oper){
    	EcpBaseResponseVO ecpResp = new EcpBaseResponseVO(); 
    	
    	PubOrderZdRequsetDTO reqDTO = new PubOrderZdRequsetDTO();
    	reqDTO.setStaffId(reqDTO.getStaff().getId());
    	reqDTO.setId(pubOrderId);
    	reqDTO.setStatus(PubOrdConstants.CustomerRequestStatus.REQUEST_STATUS_CANCLE);
    	try {
	    	// 取消征订单，将征订单状态修改为99，返回征订单下得主订单信息
	    	pubOrderZdRSV.cancelPubOrderById(reqDTO,oper);
	    	ecpResp.setResultFlag("true");
    	} catch (Exception e) {
            ecpResp.setResultFlag("false");
            ecpResp.setResultMsg(e.getMessage());
        }  
    	
    	return ecpResp;
    } 
}
