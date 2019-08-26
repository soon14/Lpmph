package com.ailk.ecp.pmph.controller;

import com.ai.ecp.pmph.dubbo.interfaces.dataimport.IZYGdsInfoImportRSV;
import com.ailk.aip.security.DefaultServiceCheckChain;
import com.ailk.butterfly.core.annotation.Security;
import com.ailk.butterfly.core.exception.BusinessException;
import com.ailk.butterfly.core.web.BaseController;
import com.ailk.ecp.pmph.util.RespUtil;
import com.ailk.ecp.pmph.vo.ZYGdsInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Project Name:ecp-aip-web <br>
 * Description: <br>
 * Date:2015年11月2日上午10:40:46  <br>
 * 
 * @version  
 * @since JDK 1.6
 */
@Controller
public class ZYGdsInfoController extends BaseController{
    
    public final static String FORMAT="FORMAT";
    
    @Resource
    private IZYGdsInfoImportRSV zyGdsInfoImportRSV;
    
    @RequestMapping(value="/rest" ,params="method=com.ai.ecp.goods.zyOnShelves")
    @Security(mustLogin=true,authorCheckType=DefaultServiceCheckChain.class)
    @ResponseBody
    public Map<String,Object> zyOnShelves(ZYGdsInfo zyGdsInfo) throws BusinessException{

        Map<String, Object> dataMap=new HashMap<String,Object>();
        dataMap.put("tpGoodsId", zyGdsInfo.getTpGoodsId());
        dataMap.put("tpGoodsName", zyGdsInfo.getTpGoodsName());
        dataMap.put("tpPrice", zyGdsInfo.getTpPrice());
        dataMap.put("tpGoodTypeId", zyGdsInfo.getTpGoodTypeId());
        dataMap.put("tpISBN", zyGdsInfo.getTpISBN());
        dataMap.put("tpEISBN", zyGdsInfo.getTpEISBN());
        dataMap.put("tpAuthorName", zyGdsInfo.getTpAuthorName());
        dataMap.put("tpPublishDate", zyGdsInfo.getTpPublishDate());
        dataMap.put("tpEBookSize", zyGdsInfo.getTpEBookSize());
        dataMap.put("tpIsAcademic", zyGdsInfo.getTpIsAcademic());
        dataMap.put("tpEBookIntro", zyGdsInfo.getTpEBookIntro());
        dataMap.put("tpType", zyGdsInfo.getTpType());
        
        Map<String,Object> retMap = new HashMap<String,Object>();
        try{
            this.zyGdsInfoImportRSV.receive(dataMap);
            retMap.put(RespUtil.CODE,"1");
            retMap.put(RespUtil.MSG, "商品上架成功");
            
            Map<String,String> formatMap=new HashMap<String,String>();
            formatMap.put("tpGoodsId", zyGdsInfo.getTpGoodsId()+"");
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            formatMap.put("upTime", formatter.format(new Date()));
            retMap.put(FORMAT,formatMap);
        }catch(Exception e){
            retMap.put(RespUtil.CODE,"2");
            retMap.put(RespUtil.MSG, "商品上架失败，原因："+e.getMessage());
        }
        
        return RespUtil.renderRootResp(retMap);
    }
    
    @RequestMapping(value="/rest" ,params="method=com.ai.ecp.goods.zyOffShelves")
    @Security(mustLogin=true,authorCheckType=DefaultServiceCheckChain.class)
    @ResponseBody
    public Map<String,Object> zyOffShelves(String tpGoodsId) throws BusinessException{
        
        Map<String, Object> dataMap=new HashMap<String,Object>();
        dataMap.put("tpGoodsId",tpGoodsId);
        
        Map<String,Object> retMap = new HashMap<String,Object>();
        try{
            this.zyGdsInfoImportRSV.offShelves(dataMap);
            retMap.put(RespUtil.CODE,"1");
            retMap.put(RespUtil.MSG, "商品下架成功");
            
            Map<String,String> formatMap=new HashMap<String,String>();
            formatMap.put("tpGoodsId",tpGoodsId);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            formatMap.put("upTime", formatter.format(new Date()));
            retMap.put(FORMAT,formatMap);
        }catch(Exception e){
            retMap.put(RespUtil.CODE,"2");
            retMap.put(RespUtil.MSG, "商品下架失败，原因："+e.getMessage());
        }
        
        return RespUtil.renderRootResp(retMap);
        
    }

}

