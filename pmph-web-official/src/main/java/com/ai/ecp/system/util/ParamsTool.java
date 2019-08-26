package com.ai.ecp.system.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.ai.ecp.server.front.dto.BaseInfo;
import com.ai.ecp.server.front.dto.BaseResponseDTO;
import com.ai.ecp.server.front.dto.PageResponseDTO;
import com.ai.paas.utils.ImageUtil;
import com.ai.paas.utils.StringUtil;

public class ParamsTool {
    
    
    /**
     * getImageUrl:(根据上传到mongoDB的图片ID 从mongoDB上获取图片路径). <br/>
     * TODO(这里描述这个方法适用条件 – 可选).<br/>
     * TODO(这里描述这个方法的执行流程 – 可选).<br/>
     * TODO(这里描述这个方法的使用方法 – 可选).<br/>
     * TODO(这里描述这个方法的注意事项 – 可选).<br/>
     * 
     * @param vfsId
     *            图片ID
     * @param param
     *            图片规格
     * @return
     * @since JDK 1.6
     */
    public static String getImageUrl(String vfsId, String param) {
        StringBuilder sb = new StringBuilder();
        sb.append(vfsId);
        if (StringUtil.isNotBlank(param)) {
            sb.append("_");
            sb.append(param);
        }
        return ImageUtil.getImageUrl(sb.toString());
    }
    

    //补全分页参数
    /**
     * 
     * checkPage:(补全分页参数). <br/> 
     * TODO(这里描述这个方法适用条件 – 可选).<br/> 
     * TODO(这里描述这个方法的执行流程 – 可选).<br/> 
     * TODO(这里描述这个方法的使用方法 – 可选).<br/> 
     * TODO(这里描述这个方法的注意事项 – 可选).<br/> 
     * 
     * @param rdors
     * @return 
     * @since JDK 1.6
     */
    public static PageResponseDTO<? extends BaseResponseDTO> checkPage(PageResponseDTO<? extends BaseResponseDTO> rdors){ 
        BaseInfo baseInfo = new BaseInfo();
        PageResponseDTO<? extends BaseResponseDTO> pageResponse = PageResponseDTO.buildByBaseInfo(
                baseInfo, BaseResponseDTO.class);
        pageResponse.setResult(null);
        return pageResponse;
    }
    

    // 进度条相关 每个编码属于哪个状态
    public static Map<String, Integer> getStatusMap() {
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("01", 1);
        map.put("02", 2);
        map.put("04", 2);
        map.put("05", 3);
        map.put("06", 4);
        map.put("80", 5);
        return map;
    }

    // 进度条相关底下的展示除了完成之外依次的状态
    public static List<String> getStatusList() {
        List<String> statuslist = new ArrayList<String>();
        statuslist.add(0, "01");
        statuslist.add(1, "02");
        statuslist.add(2, "05");
        statuslist.add(3, "06");
        return statuslist;
    }
    
    /**
     * 获取客户端地址
     * @param request
     * @return
     */
    public static String getClientAddr(HttpServletRequest request){
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("x-real-ip");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
    
    /**页面title
     * Project Name:ecp-web-mall <br>
     * Description: <br>
     * Date:2015年10月29日下午4:27:42  <br>
     * 
     * @version ParamsTool 
     * @since JDK 1.6 
     */  
    public static class PageTitle {
        public static final String HOMEPAGE = "人卫商城首页";
        public static final String TESTBOOK = "考试书";
        public static final String SCIENCEBOOK = "科普书";
        public static final String REFERENCEBOOK = "参考书";
        public static final String PAPERBOOK = "纸质教材";
        public static final String AUDIOBOOK = "音像制品";
        public static final String DIGITALPRODUCT = "数字产品";
        public static final String DIGITALBOOK = "数字教材";
        public static final String ONLINETEST = "在线考试培训";
        public static final String ELECTRONICBOOK = "电子书";
    }
    
    public static class Page {
        public static final String HOMEPAGE = "homepage";
        public static final String TESTBOOK = "testbook";
        public static final String SCIENCEBOOK = "sciencebook";
        public static final String REFERENCEBOOK = "referencebook";
        public static final String PAPERBOOK = "paperbook";
        public static final String AUDIOBOOK = "audiobook";
        public static final String DIGITALBOOK = "digitalbook";
        public static final String DIGITALPRODUCT = "digitalproduct";
        public static final String ONLINETEST = "onlinetest";
        public static final String ELECTRONICBOOK = "electronicbook";
        public static final String REGISTER_PAGE = "url.registerPage";
        public static final String LOSTPASSWORD_PAGE = "url.lostpasswordPage";
        public static final String UPDATEPASSWORD_PAGE ="url.updatepasswordPage";
        public static final String POINT_URL ="url.point";
        public static final String GW_URL ="url.gwloginPage";
    }
    
}

