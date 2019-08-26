package com.ai.ecp.pmph.busi.activity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ai.ecp.base.controller.EcpBaseController;

@Controller
@RequestMapping(value="/activity")
public class ActivityController  extends EcpBaseController{
    public static String MODULE = ActivityController.class.getName();
    
    @RequestMapping(value="/index")
    public String index(Model model)
    {

        return "activity/activity";
    }

}

