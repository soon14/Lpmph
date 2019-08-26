package com.ai.ecp.util;

import org.apache.commons.lang.ArrayUtils;

import com.ai.ecp.server.front.util.BaseParamUtil;

public class EmoticonUtil {

	//表情集
	private static String[] emoticonArray = new String[]{"微笑","大笑","调皮","惊慌",
			"耍酷","发火","害羞","汗水","大哭","得志","鄙视","困乏","夸奖","晕倒",
			"疑问","媒婆","狂吐","青蛙","发愁","亲吻","斗鸡眼","爱心","心碎","玫瑰",
			"礼物","哭","奸笑","可爱","得意","呲牙","暴汗","楚楚可怜","困","流泪","生气",
			"惊讶","口水","彩虹","夜空","太阳","钱钱","灯泡","咖啡","蛋糕","音乐","爱",
			"胜利","赞","差","OK"};
	
	public static int getEmoticonPos(String emoticon) {
		return ArrayUtils.indexOf(emoticonArray, emoticon);
	}
	
	public static String getEmoticonName(String pos){
		Integer p = Integer.valueOf(pos);
		return "["+emoticonArray[--p]+"]";
	}
	
	public static String getHtmlTag(String emoticon){
		String imProjectUrl = BaseParamUtil.fetchParamValue("IM_PROJECT_URL", "IM_PROJECT_URL");
		String temp = emoticon.substring(1, emoticon.length()-1);
		int emoticonPos = getEmoticonPos(temp);
		if(ArrayUtils.INDEX_NOT_FOUND==emoticonPos){
			return emoticon;
		}
		emoticonPos++;
		String filePos = String.format("%02d", emoticonPos);
		String format = "<img class=\"emotionImg\" alt=\"%s\" src=\"%s/images/face/i_f%s.gif\">";
		return String.format(format, temp, imProjectUrl,filePos);
	}
	
}
