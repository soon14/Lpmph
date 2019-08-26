package com.ai.ecp.pmph.busi.seller.staff.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.ecp.base.controller.EcpBaseController;
import com.ai.ecp.base.util.WebContextUtil;
import com.ai.ecp.base.vo.EcpBaseResponseVO;
import com.ai.ecp.busi.im.vo.PhrasebookGroupRespVO;
import com.ai.ecp.busi.im.vo.PhrasebookItemRespVO;
import com.ai.ecp.im.dubbo.dto.ImPhrasebookGroupReqDTO;
import com.ai.ecp.im.dubbo.dto.ImPhrasebookGroupResDTO;
import com.ai.ecp.im.dubbo.dto.ImPhrasebookItemReqDTO;
import com.ai.ecp.im.dubbo.dto.ImPhrasebookItemResDTO;
import com.ai.ecp.im.dubbo.interfaces.IPhrasebookGroupRSV;
import com.ai.ecp.im.dubbo.interfaces.IPhrasebookItemRSV;
import com.ai.ecp.im.dubbo.util.ImConstants;
import com.ai.ecp.pmph.busi.seller.staff.vo.PhrasebookTreeVO;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.ecp.server.front.security.AuthPrivilegeResDTO;
import com.alibaba.fastjson.JSON;

/**
 * Project Name:pmph-web-mall <br>
 * Description: 客服管理<br>
 * @author LBQ
 *
 */
@Controller
@RequestMapping(value = "/seller/customer/custmanage")
public class CustManageController extends EcpBaseController{
	@Resource
	private IPhrasebookGroupRSV phrasebookGroupRSV;
	@Resource
	private IPhrasebookItemRSV phrasebookItemRSV;
	private static String URL = "/seller/staff";
	/**
	 * index:(常用语管理). <br/>
	 * 公共常用语
	 */
	@RequestMapping()
	public String index(Model model) throws Exception{
		return URL+"/index";
	}
	
	/**
	 * 
	 * saveGroup:(保存分组). <br/>
	 * 新增或修改
	 * 
	 * @return
	 * @throws BusinessException
	 * @since JDK 1.7
	 */
	@RequestMapping("group/save")
	@ResponseBody
	public PhrasebookGroupRespVO saveGroup(ImPhrasebookGroupReqDTO reqDTO) throws BusinessException {
		PhrasebookGroupRespVO respVO = new PhrasebookGroupRespVO();
		respVO.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_SUCCESS);
		if (reqDTO.getId() == null) {
			Long id = phrasebookGroupRSV.savePhrasebookGroup(reqDTO);
			respVO.setId(id);
		} else {
			phrasebookGroupRSV.updatePhrasebookGroupById(reqDTO);
		}

		return respVO;
	}

	/**
	 * 
	 * saveItem:(保存常用语). <br/>
	 * 新增或修改
	 * 
	 * @param reqDTO
	 * @return
	 * @throws BusinessException
	 * @since JDK 1.7
	 */
	@RequestMapping("item/save")
	@ResponseBody
	public PhrasebookItemRespVO saveItem(ImPhrasebookItemReqDTO reqDTO) throws BusinessException {
		PhrasebookItemRespVO respVO = new PhrasebookItemRespVO();
		respVO.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_SUCCESS);
		if (reqDTO.getId() == null) {
			Long id = phrasebookItemRSV.savePhrasebookItem(reqDTO);
			respVO.setId(id);
		} else {
			phrasebookItemRSV.updatePhrasebookItemById(reqDTO);
		}

		return respVO;
	}

	/**
	 * 
	 * delGroup:(删除分组). <br/>
	 * 
	 * @param reqDTO
	 * @return
	 * @throws BusinessException
	 * @since JDK 1.7
	 */
	@RequestMapping("group/del")
	public EcpBaseResponseVO delGroup(ImPhrasebookGroupReqDTO reqDTO) throws BusinessException {
		EcpBaseResponseVO respVO = new EcpBaseResponseVO();
		respVO.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_SUCCESS);
		phrasebookGroupRSV.deletePhrasebookGroupById(reqDTO);

		return respVO;
	}

	/**
	 * 
	 * delItem:(删除常用语). <br/>
	 * 
	 * @param reqDTO
	 * @return
	 * @throws BusinessException
	 * @since JDK 1.7
	 */
	@RequestMapping("item/del")
	public EcpBaseResponseVO delItem(ImPhrasebookItemReqDTO reqDTO) throws BusinessException {
		EcpBaseResponseVO respVO = new EcpBaseResponseVO();
		respVO.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_SUCCESS);
		phrasebookItemRSV.deletePhrasebookItemById(reqDTO);

		return respVO;
	}

	/**
	 * 
	 * genarateTree:(查询常用语树). <br/>
	 * 二级结构
	 * 
	 * @return
	 * @throws BusinessException
	 * @since JDK 1.7
	 */
	@RequestMapping("tree")
	@ResponseBody
	public String genarateTree(ImPhrasebookGroupReqDTO reqDTO) throws BusinessException {
		ImPhrasebookGroupReqDTO reqFind = new ImPhrasebookGroupReqDTO();
		reqFind.setShopId(reqDTO.getShopId());//店铺控制
		reqFind.setGroupClass(ImConstants.PhraseBook.GROUP_CLASS_TEAM);//仅查询“公共”
		List<ImPhrasebookGroupResDTO> list = phrasebookGroupRSV.findPhrasebookGroupByShopId(reqFind);

		List<PhrasebookTreeVO> listTree = new ArrayList<>();
		for (ImPhrasebookGroupResDTO dto : list) {// 处理分组
			PhrasebookTreeVO groupVO = new PhrasebookTreeVO();
			groupVO.setId(dto.getId().toString());
			groupVO.setName(dto.getGroupName().toString());
			groupVO.setShopId(dto.getShopId().toString());
			groupVO.setSortNo(dto.getSortNo().toString());
			groupVO.setIsParent(true);
			
			List<ImPhrasebookItemResDTO> sub = dto.getItems();
			List<PhrasebookTreeVO> listSub = new ArrayList<>();
			for (ImPhrasebookItemResDTO sdto : sub) {// 处理常用语
				PhrasebookTreeVO itemVO = new PhrasebookTreeVO();
				itemVO.setId(sdto.getId().toString());
				itemVO.setName(sdto.getItemText().toString());
				itemVO.setSortNo(sdto.getSortNo().toString());
				
				listSub.add(itemVO);
			}
			
			groupVO.setChildren(listSub);
			listTree.add(groupVO);
		}

		String result = JSON.toJSONString(listTree);

		return result;
	}
}
