package com.ai.ecp.pmph.busi.seller.staff.controller;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import javax.annotation.Resource;
import javax.validation.Valid;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ai.ecp.base.controller.EcpBaseController;
import com.ai.ecp.base.vo.EcpBaseResponseVO;
import com.ai.ecp.pmph.busi.seller.staff.vo.ShopAcctApplicationMoneyReqVO;
import com.ai.ecp.pmph.busi.seller.staff.vo.ShopAcctBillMonthVO;
import com.ai.ecp.pmph.busi.seller.staff.vo.ShopAcctInfoVO;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.ecp.staff.dubbo.dto.ShopAcctAppMoneyDetailResDTO;
import com.ai.ecp.staff.dubbo.dto.ShopAcctBillMonthResDTO;
import com.ai.ecp.staff.dubbo.dto.ShopAcctInfoReqDTO;
import com.ai.ecp.staff.dubbo.dto.ShopAcctInfoResDTO;
import com.ai.ecp.staff.dubbo.dto.ShopAcctWithdrawApplyReqDTO;
import com.ai.ecp.staff.dubbo.interfaces.IShopAcctRSV;
import com.ai.paas.utils.DateUtil;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.ObjectCopyUtil;
/**
 * Project Name:pmph-web-mall <br>
 * Description: 提现申请<br>
 * @author mwz
 */
@Controller
@RequestMapping(value = "/seller/shopmgr/withdraw")
public class CustWithdrawController extends EcpBaseController{
    private static String MODULE = CustWithdrawController.class.getName();
    @Resource
    private IShopAcctRSV shopAcctRSV;
    /**
     * index:(初始化页面). <br/> 
     * TODO(这里描述这个方法适用条件 – 可选).<br/> 
     * TODO(这里描述这个方法的执行流程 – 可选).<br/> 
     * TODO(这里描述这个方法的使用方法 – 可选).<br/> 
     * TODO(这里描述这个方法的注意事项 – 可选).<br/> 
     * 
     * @author mwz 
     * @param model
     * @return
     * @throws Exception 
     * @since JDK 1.7
     */
    @RequestMapping()
    public String index(Model model,@RequestParam(value="shopId", required=false)Long shopId) throws Exception{
        Calendar date=Calendar.getInstance();
        String currentYear = String.valueOf(date.get(Calendar.YEAR));
        model.addAttribute("currentYear", currentYear);
        return "/seller/staff/shopmgr/shopmgr-withdraw";
    }
    @RequestMapping(value="/showWithdrawDetail/{shopId}/{billMonths}")
    public String showWithdrawDetail(Model model,@PathVariable(value="shopId") Long shopId,@PathVariable(value="billMonths") String billMonths){
        ShopAcctApplicationMoneyReqVO vo=new ShopAcctApplicationMoneyReqVO();
        //根据店铺ID查询店铺账户详情包括该店铺的名称
        ShopAcctInfoReqDTO reqDTO=new ShopAcctInfoReqDTO();
        reqDTO.setShopId(shopId);
        ShopAcctInfoResDTO resDTO = shopAcctRSV.findShopAcctInfoResDTOByShopId(reqDTO);
        //提现前账户总额
        Long acctTotal=resDTO.getAcctTotal();
        //将提现前账户总额存到vo中
        vo.setPrevAcctTotal(acctTotal);
        //将店铺ID存到VO中
        vo.setShopId(shopId);
        //将该店铺全称存到vo中
        vo.setShopFullName(resDTO.getShopName());
        //账户总计可提现金额(申请提现金额)
        Long money=0l;
        //根据billMonths查询这些个月的月结账单获取可提现金额
        String[] billMonthArr = billMonths.split(",");
        List<Integer> billMonthsList=new ArrayList<>();
        for (String billMonthStr : billMonthArr) {
            billMonthsList.add(Integer.parseInt(billMonthStr));
        }
        //结算月字符串
        model.addAttribute("monthsStr", billMonths);
        //根据结算月和店铺ID查询该月该店铺的账单
        //系统当前时间(用于判断该月的账单是否已到可提现时间)
        Long sysDate = DateUtil.getSysDate().getTime();
        for (Integer billMonth : billMonthsList) {
            ShopAcctBillMonthResDTO  shopAcctBillMonthResDTO= 
                    shopAcctRSV.listShopAcctBillMonthByShopIdAndBillMonth(shopId, billMonth);
            //判断状态是否为"未提现"并且已过可提现日期
            //状态
            String status = shopAcctBillMonthResDTO.getStatus();
            //可提现日期
            Long canWithdrawTime = shopAcctBillMonthResDTO.getCanWithdrawTime().getTime();
            if("1".equals(status)&&sysDate>=canWithdrawTime){
                money+=shopAcctBillMonthResDTO.getMoney();
            }
        }
        //申请提现金额
        vo.setApplicationMoney(money);
        //计算提现后账户总额=提现前账户总额-申请提现金额
        Long  alreadyAcctTotal=acctTotal-money;
        vo.setAlreadyAcctTotal(alreadyAcctTotal);
        //根据店铺id和结算月查询账户该月的订单详情（订单收入明细、订单退货退款支出明细、调账明细等）
        List<ShopAcctAppMoneyDetailResDTO> list=new ArrayList<>();
        for (Integer billMonth : billMonthsList) {
            ShopAcctAppMoneyDetailResDTO detailDTO = 
                    shopAcctRSV.findShopAcctAppMoneyDetail(shopId, billMonth);
            list.add(detailDTO);
        }
        //金额详情
        model.addAttribute("reqVO", vo);
        //订单详情
        model.addAttribute("list", list);
        return "/seller/staff/shopmgr/shopmgr-wdr-detail";
    }
    /**
     * selectShopAcctInfo:(根据店铺ID查询店铺详情). <br/> 
     * TODO(这里描述这个方法适用条件 – 可选).<br/> 
     * TODO(这里描述这个方法的执行流程 – 可选).<br/> 
     * TODO(这里描述这个方法的使用方法 – 可选).<br/> 
     * TODO(这里描述这个方法的注意事项 – 可选).<br/> 
     * 
     * @author mwz 
     * @param shopId
     * @return 
     * @since JDK 1.7
     */
    @RequestMapping(value="/shopAcctInfo")
    @ResponseBody
    public ShopAcctInfoVO selectShopAcctInfo(Long shopId){
        ShopAcctInfoVO vo=new ShopAcctInfoVO();
        ShopAcctInfoReqDTO reqDTO=new ShopAcctInfoReqDTO();
        reqDTO.setShopId(shopId);
        ShopAcctInfoResDTO resDTO = shopAcctRSV.findShopAcctInfoResDTOByShopId(reqDTO);
        if(resDTO!=null){
            ObjectCopyUtil.copyObjValue(resDTO, vo, null, false);
        }
        return vo;
    }
    @RequestMapping(value="/shopAcctBillMonth")
    @ResponseBody
    public List<ShopAcctBillMonthVO> selectShopAcctBillMonth(Long shopId,Integer currentYear){
        List<ShopAcctBillMonthVO> result=new ArrayList<>();
        List<ShopAcctBillMonthResDTO> list= 
                shopAcctRSV.listShopAcctBillMonthByShopIdAndYear(shopId, currentYear);
        if(!CollectionUtils.isEmpty(list)){
            for (ShopAcctBillMonthResDTO resDTO : list) {
                if(resDTO!=null){
                    //如果月结账单的状态为已提现("2"),将已提现金额转换成正数
                    if("2".equals(resDTO.getStatus())){
                        resDTO.setWithdrawMoney(resDTO.getWithdrawMoney()*-1);
                    }
                    ShopAcctBillMonthVO vo=new ShopAcctBillMonthVO();
                    ObjectCopyUtil.copyObjValue(resDTO, vo, null, false);
                    //将系统当前时间存到vo中，用于判断该笔月结订单是否已到可提现时间
                    vo.setSystemTime(DateUtil.getSysDate());
                    result.add(vo);
                }else{
                    result.add(null);
                }
            }
        }else{
            throw new BusinessException("查询店铺月账单信息异常");
        }
        return result;
    }
    @RequestMapping(value="/realSubmit")
    @ResponseBody
    public EcpBaseResponseVO realSubmit(@Valid ShopAcctWithdrawApplyReqDTO reqDTO){
        EcpBaseResponseVO resp=new EcpBaseResponseVO();
        try {
            //申请单状态:一级待审核
            reqDTO.setStatus("00");
            //提交提现申请
            shopAcctRSV.submitWithdrawMoney(reqDTO);
            //提交提现申请成功
            resp.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_SUCCESS);
        } catch (Exception e) {
            LogUtil.error(MODULE, e.getMessage());
            resp.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_EXCEPTION);
            resp.setResultMsg(e.getMessage()+",提现申请异常");
        }
        return resp;
    }
    /**
     * check:(检验该店铺是否有正在提现流程中的提现申请单). <br/> 
     * TODO(这里描述这个方法适用条件 – 可选).<br/> 
     * TODO(这里描述这个方法的执行流程 – 可选).<br/> 
     * TODO(这里描述这个方法的使用方法 – 可选).<br/> 
     * TODO(这里描述这个方法的注意事项 – 可选).<br/> 
     * 
     * @author mwz 
     * @param shopId
     * @return 
     * @since JDK 1.7
     */
    @RequestMapping(value="/check")
    @ResponseBody
    public EcpBaseResponseVO check(Long shopId,String billMonths,String year){
        EcpBaseResponseVO resp=new EcpBaseResponseVO();
        try {
            //1.判断前台传来的billMonths是否为空
            if(StringUtils.isBlank(billMonths)){
                //如果billMonths=""则表示用户没有选择任何一个可提现的月结账单就点击提现申请按钮,那么申请提现金额肯定为0
                resp.setResultFlag("0");
                resp.setResultMsg("您好,提现申请金额必须大于0!");
                return resp;
            }
            //用户选择的月结账单billMonth集合(例如201801,201802)
            String[] billMonthStrArr = billMonths.split(",");
            //将String类型的数组转换成int类型的集合
            List<Integer> billMonthIntList=new ArrayList<>();
            for (String string : billMonthStrArr) {
                billMonthIntList.add(Integer.parseInt(string));
            }
            //从小到大排序
            Collections.sort(billMonthIntList);
            //该店铺可提现的结算月集合(数据库数据)并且保证是已到可提现账期的
            List<Integer> result = new ArrayList<>();
            //2.根据店铺ID和年份从数据库查询该店铺这一整年所有可提现的月结账单并且这些月结账单已到可提现账期
            List<ShopAcctBillMonthResDTO> shopAcctBillMonths = shopAcctRSV.findAllCanWithdrawByShopId(shopId,year);
            if(shopAcctBillMonths.size()>0){
                for (ShopAcctBillMonthResDTO resDTO : shopAcctBillMonths) {
                    result.add(resDTO.getBillMonth());
                }
                //对result集合进行从小到大排序
                Collections.sort(result);
                //找出billMonth最小的月结账单,并查看该月是否被选中
                //数据库中查出的billMonth最小的月结账单
                int billMonthSmall = result.get(0);
                //前台传到后台的最小的月结账单
                int webBillMonthSmall = billMonthIntList.get(0);
                if(billMonthSmall!=webBillMonthSmall){
                  //表示用户没有从最小的可提现月份开始进行提现
                    resp.setResultFlag("1");
                    resp.setResultMsg("您好,请从最早可提现月份发起申请!");
                    return resp;
                }
            }else{
                //没有可提现的月结账单
                resp.setResultFlag("2");
                resp.setResultMsg("您好,您目前没有可提现的月结账单!");
                return resp;
            }
            //3.检查是否有正在提现流程中的月结账单
            Integer size = shopAcctRSV.findBillMonthInWithdrawingSizeByShopId(shopId);
            //size大于0表示有正在提现流程中的月结账单,不允许提现
            if(size>0){
                resp.setResultFlag("3");
                resp.setResultMsg("您好,您有正在提现流程中的月结账单,无法再次发起提现申请!");
                return resp;
            }
            //4.检验用户选的这些个月结账单是否都是可提现并且已到可提现账期的状态
            boolean flag=false;
            for (int billMonth : billMonthIntList) {
                for (int i : result) {
                    if (i==billMonth) {
                        flag=true;
                        break;
                    }
                }
                if(!flag){
                    //表示用户提交的提现月结账单中有不可提现的月结账单
                    resp.setResultFlag("4");
                    resp.setResultMsg("您好,您选择的提现申请月结账单中包含有不可提现或未到可提现账期的月结账单!");
                    return resp;
                }
            }
            //5.检查是否跳着选
            Integer length =billMonthIntList.size();
            //表示用户选的月结账单数大于1
            if(length>1){
                //如果前台传来的结算月集合的最大值-最小值+1等于该结算月集合的长度的话则表示没有跳着选
                Integer max=billMonthIntList.get(length-1);
                Integer min=billMonthIntList.get(0);
                int v=max-min+1;
                //表示该用户跳着选
                if(v!=length){
                    resp.setResultFlag("4");
                    resp.setResultMsg("您好,请连续选择可提现的月结账单!");
                    return resp;
                }
            }
            //4.根据用户选择的结算月检查这几个月的是否大于0
            Long money=0l;
            for (int billMonth : billMonthIntList) {
                for (ShopAcctBillMonthResDTO resDTO : shopAcctBillMonths) {
                    if(billMonth==resDTO.getBillMonth()){
                        money+=resDTO.getMoney();
                        continue;
                    }
                }
            }
            if(money==0l){
                resp.setResultFlag("0");
                resp.setResultMsg("您好,提现申请金额必须大于0!");
                return resp;
            }
            //5.查看该用户前一年是否有可提现并且可提现金额>0的月结账单
            Integer lastYearInt=Integer.parseInt(year)-1;
            String lastYear = lastYearInt.toString();
            //查询前一年所有可提现的月结账单
            List<ShopAcctBillMonthResDTO> lastYearBillMonths = shopAcctRSV.findAllCanWithdrawByShopId(shopId, lastYear);
            //前一年该店铺所有可提现的月结账单的"总"可提现金额
            long lastYearCanWithdrawMoney=0l;
            if(lastYearBillMonths.size()>0){
                for (ShopAcctBillMonthResDTO resDTO : lastYearBillMonths) {
                    lastYearCanWithdrawMoney+=resDTO.getMoney();
                }
                if(lastYearCanWithdrawMoney>0l){
                    resp.setResultFlag("5");
                    resp.setResultMsg("您好,"+lastYear+"年还有可提现的月结账单!");
                    return resp;
                }
            }
            //6.查看该店铺可用余额是否小于申请提现金额，如果小于则不允许申请
            //根据店铺Id查询店铺可用余额
            ShopAcctInfoReqDTO reqDTO=new ShopAcctInfoReqDTO();
            reqDTO.setShopId(shopId);
            ShopAcctInfoResDTO shopAcctInfo = shopAcctRSV.findShopAcctInfoResDTOByShopId(reqDTO);
            //店铺可用余额
            Long acctBalance = shopAcctInfo.getAcctBalance();
            if(acctBalance<money){
                resp.setResultFlag("6");
                resp.setResultMsg("您好,店铺可用余额不足!");
                return resp;
            }
            //如果以上条件全部满足则可以申请提现
            resp.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_SUCCESS);
            resp.setResultMsg("允许发起提现申请!");
        } catch (Exception e) {
            LogUtil.error(MODULE, e.getMessage());
            resp.setResultFlag(EcpBaseResponseVO.RESULT_FLAG_EXCEPTION);
            resp.setResultMsg("店铺提现申请异常,请联系管理员!");
        }
        return resp;
    }
}
