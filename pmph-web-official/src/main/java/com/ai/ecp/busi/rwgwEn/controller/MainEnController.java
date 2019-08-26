package com.ai.ecp.busi.rwgwEn.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ai.ecp.base.controller.EcpBaseController;
import com.ai.ecp.busi.rwgw.vo.ComponentReqVO;
import com.ai.paas.utils.LogUtil;

/**
 * Project Name:pmph-web-official <br>
 * Description: <br>
 * Date:2015年11月23日下午9:49:44  <br>
 * 人卫官网
 * @version  
 * @since JDK 1.6 
 */  
@Controller
@RequestMapping(value = "/main-en")
public class MainEnController extends EcpBaseController {

    private static String MODULE = MainEnController.class.getName();
    
    private final Long PMPHENSITEID =5L;//官网英语版站点id
    
    /** 
     * init:(默认初始化方法). <br/> 
     * 
     * @return 
     * @since JDK 1.6 
     */ 
    @RequestMapping()
    public String init(Model model){
        LogUtil.info(MODULE, "进入官网英文版！！");
        model.addAttribute("ishome", "1");
        return "/rwgw-en/main/indexcontent/index-content";
    }
    
    /** 
     * about:(点击栏跳转至关于我们页面). <br/> 
     * 
     * @param model
     * @param reqVO：siteInfoId(参数)
     * @return 
     * @since JDK 1.6 
     */ 
    @RequestMapping(value="about")
    public String about(Model model){
       return "forward:/main/about?siteId="+this.PMPHENSITEID; 
    }
}
