import com.ai.ecp.busi.im.onlinestatus.vo.OnlineReqVO;
import com.ai.paas.utils.DateUtil;
import com.ai.paas.utils.MongoUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class onlineTest {

	
	
	public static void main(String[] args) {
	    OnlineReqVO reqVO = new OnlineReqVO();
	    reqVO.setHotlineId(1L);
	    reqVO.setCreateTime(DateUtil.getSysDate());
	    reqVO.setCsaCode("csa_admin");
	    reqVO.setOnlineStatus("0");
	    reqVO.setResource("WEB");
	    reqVO.setUpdateTime(DateUtil.getSysDate());
		JSONObject doc = new JSONObject();
		doc = (JSONObject) JSON.toJSON(reqVO);
		MongoUtil.insert("T_IM_HOTLINE_ONLINE", doc);
	}
}
