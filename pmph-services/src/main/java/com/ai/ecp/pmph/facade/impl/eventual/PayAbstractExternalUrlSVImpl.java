package com.ai.ecp.pmph.facade.impl.eventual;

import com.ai.ecp.order.dubbo.dto.pay.PayIntfReqLogDTO;
import com.ai.ecp.order.dubbo.dto.pay.PaySuccInfo;
import com.ai.ecp.order.service.busi.interfaces.pay.IPayIntfReqLogSV;
import com.ai.ecp.server.front.dto.BaseSysCfgRespDTO;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.ecp.server.front.util.SysCfgUtil;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.StringUtil;
import com.alibaba.fastjson.JSON;
import com.distribute.tx.common.TransactionStatus;
import net.sf.json.JSONObject;

import javax.annotation.Resource;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Date;

/**
 */
public abstract class PayAbstractExternalUrlSVImpl{
    public static final String METHOD_POST="POST";

    public static final String METHOD_GET_01="GET_01";

    public static final String METHOD_GET_02="GET_02";

    @Resource
    private IPayIntfReqLogSV payIntfReqLogSV;

    public void deal(PaySuccInfo paySuccInfo,String importType) {
        String result = "";
        try{
            result = getResult(paySuccInfo, setMethod());
        }catch (Exception e){
            result = "对外接口同步异常"+e.getMessage();
            saveResultLog(paySuccInfo,getModule()+" 对外接口同步异常: "+e.getMessage());
        }

        if(StringUtil.isBlank(result)) {
            LogUtil.info(getModule(),"实物商品无需对外接口同步数据");
            return;
        }

        try{
            handleResult(paySuccInfo, result);
        }catch (Exception e){
            result = "接口返回数据操作异常"+e.getMessage();
            saveResultLog(paySuccInfo,getModule()+" 接口返回数据操作异常: "+e.getMessage());
        }

        saveResultLog(paySuccInfo,result);
    }

    protected abstract String setMethod();

    public void joinTransaction(JSONObject message, TransactionStatus status, String s) {
        try {
            final PaySuccInfo paySuccInfo = JSON.parseObject(message.toString(), PaySuccInfo.class);
            LogUtil.info(getModule(), "开始=============" + paySuccInfo.toString());

            deal(paySuccInfo, getImportType());

            LogUtil.info(getModule(),"订单号："+paySuccInfo.getOrderId());
        } catch (BusinessException be) {
            LogUtil.error(getModule(), "接口处理异常", be);
            be.printStackTrace();

        } catch (Exception e) {
            LogUtil.error(getModule(), "接口处理异常", e);

        }
    }

    //get方式构造参数json串
    public abstract String getParamJson(PaySuccInfo paySuccInfo);

    //post方式构造参数第一个参数不带? get方式构造参数时第一个参数要带?
    public abstract String getParamNameValue(PaySuccInfo paySuccInfo);

    //在管理平台配置的url获取的参数即可
    public abstract String setUrlPrefix();

    //在管理平台配置的 导入类型，判断是什么日志
    public abstract String getImportType();

    protected String getResult(PaySuccInfo paySuccInfo, String method) throws Exception{

        BaseSysCfgRespDTO sysCfg = SysCfgUtil.fetchSysCfg(setUrlPrefix());
        String urlPrefix= sysCfg != null?sysCfg.getParaValue():"";


        if(METHOD_GET_01.equals(method.toUpperCase())){
            String jsonParams = getParamJson(paySuccInfo);
            String url = urlPrefix + jsonParams;

            if(!StringUtil.isBlank(jsonParams)&& !StringUtil.isBlank(urlPrefix)){
                LogUtil.info(getModule(),"处理向外域链接发送的参数"+jsonParams);

                LogUtil.info(getModule(),"外域链接="+url);

                saveParamsLog(paySuccInfo,urlPrefix+jsonParams);

                return getGetResult(url);
            }else{
                LogUtil.info(getModule(), "链接地址及参数"+url);
                LogUtil.info(getModule(), "order.api.geturl.error");
            }
        }else if(METHOD_POST.equals(method.toUpperCase())){

            String params = getParamNameValue(paySuccInfo);

            if(StringUtil.isBlank(params)) {
                LogUtil.info(getModule(),"处理向外域链接发送的参数为空不做处理");
                return "";
            }

            LogUtil.info(getModule(),"处理向外域链接发送的参数"+params);

            LogUtil.info(getModule(),"外域链接="+urlPrefix);

            saveParamsLog(paySuccInfo,urlPrefix+params);

            return getPostResult(params);

        }else if(METHOD_GET_02.equals(method.toUpperCase())){

            String getParams = getParamNameValue(paySuccInfo);
            String url = urlPrefix + getParams;

            if(!StringUtil.isBlank(getParams)&& !StringUtil.isBlank(urlPrefix)){
                LogUtil.info(getModule(), "处理向外域链接发送的参数" + getParams);

                saveParamsLog(paySuccInfo, urlPrefix+getParams);

                LogUtil.info(getModule(), "外域链接=" + url);
                return getGetResult(url);
            }else{
                LogUtil.error(getModule(), "order.api.geturl.error");
            }
        }

        return "";
    }

    private String getPostResult(String params) throws IOException {
        BaseSysCfgRespDTO sysCfg = SysCfgUtil.fetchSysCfg(setUrlPrefix());
        String urlPrefix= sysCfg != null?sysCfg.getParaValue():"";

        LogUtil.info(getModule(),"处理向外域链接发送的参数");
        URL httpPost = new URL(urlPrefix);

        HttpURLConnection httpURLConnection = (HttpURLConnection)httpPost.openConnection();

        //设置参数
        httpURLConnection.setDoInput(true); //需要输入
        httpURLConnection.setDoOutput(true); //需要输出
        httpURLConnection.setUseCaches(false); //不允许缓存

        httpURLConnection.setRequestMethod("POST"); //请求方式

        //设置属性
        httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        httpURLConnection.setConnectTimeout(30000);
        httpURLConnection.setReadTimeout(30000);
        httpURLConnection.setRequestProperty("Charset", "UTF-8");

        //连接,也可以不用明文connect，使用下面的httpConn.getOutputStream()会自动connect
        httpURLConnection.connect();
        //建立输入流，向指向的URL传入参数
        DataOutputStream dos=new DataOutputStream(httpURLConnection.getOutputStream());
        dos.writeBytes(params);
        dos.flush();
        dos.close();


        //结果返回
        int responseCode = httpURLConnection.getResponseCode();
        if(HttpURLConnection.HTTP_OK == responseCode){
            return readResponseResult(httpURLConnection);
        }

        return "";
    }

    private String getGetResult(String url) throws IOException {
        URL httpGet = new URL(url);

        HttpURLConnection httpURLConnection = (HttpURLConnection)httpGet.openConnection();

        httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");

        if(httpURLConnection.getResponseCode() > 300){
            LogUtil.error(getModule(), "order.api.getdata.error");
            throw new BusinessException("order.api.getdata.error");
        }

        return readResponseResult(httpURLConnection);
    }

    private String readResponseResult(HttpURLConnection httpURLConnection) throws IOException {
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;

        StringBuilder readBuffer = new StringBuilder();
        String temp = null;
        try{
            inputStream = httpURLConnection.getInputStream();

            inputStreamReader = new InputStreamReader(inputStream);

            bufferedReader = new BufferedReader(inputStreamReader);

            while((temp = bufferedReader.readLine())!=null) {
                readBuffer.append(temp);
            }

        }catch (Exception e) {
            LogUtil.error(getModule(), "order.api.readdata.error");

            throw new BusinessException("order.api.readdata.error");
        }finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (inputStreamReader != null) {
                inputStreamReader.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        LogUtil.info(getModule(), "解析外域统计接口数据成功");

        return readBuffer.toString();
    }

    public abstract void handleResult(PaySuccInfo paySuccInfo, String resultStr);

    public abstract String getModule();

    private void saveParamsLog(PaySuccInfo paySuccInfo, String params){
        PayIntfReqLogDTO log = new PayIntfReqLogDTO();
        log.setOrderId(paySuccInfo.getOrderId());
        log.setStaffId(paySuccInfo.getStaffId());
        //               log.setRlRequestNo("");
        log.setTypeCode(getImportType());
        Long time = new Date().getTime();
        log.setRequestTime(new Timestamp(time));
        log.setResponseTime(new Timestamp(time));
        log.setRequestMsg(params);
        payIntfReqLogSV.addPayIntfReqLog(log);
    }

    private void saveResultLog(PaySuccInfo paySuccInfo,String result){
        PayIntfReqLogDTO log = new PayIntfReqLogDTO();
        log.setOrderId(paySuccInfo.getOrderId());
        log.setStaffId(paySuccInfo.getStaffId());
        //               log.setRlRequestNo("");
        Long time = new Date().getTime();
        log.setTypeCode(getImportType());
        log.setRequestTime(new Timestamp(time));
        log.setResponseTime(new Timestamp(time));
        log.setResponseMsg(result);
        payIntfReqLogSV.addPayIntfReqLog(log);
    }

}
