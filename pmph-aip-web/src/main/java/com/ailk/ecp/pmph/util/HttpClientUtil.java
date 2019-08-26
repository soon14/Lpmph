package com.ailk.ecp.pmph.util;

import com.ai.ecp.server.front.dto.BaseSysCfgRespDTO;
import com.ai.ecp.server.front.exception.BusinessException;
import com.ai.ecp.server.front.util.SysCfgUtil;
import com.ai.paas.utils.LogUtil;
import com.ai.paas.utils.StringUtil;
import java.io.IOException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpClientUtil {
	private static String MODULE = HttpClientUtil.class.getName();

	private static String getAnalysisUrl() {
		LogUtil.info(MODULE, "获取行为分析地址开始");
		BaseSysCfgRespDTO XwAnaUrlDto = null;
		try {
			XwAnaUrlDto = SysCfgUtil.fetchSysCfg("XW_ANAL_SYS_URL");
		} catch (Exception e) {
			LogUtil.error(MODULE, "获取用户行为分析地址结束：异常", e);
			return null;
		}

		if ((XwAnaUrlDto == null) || (StringUtil.isBlank(XwAnaUrlDto.getParaValue()))) {
			LogUtil.error(MODULE, "用户行为分析地址结束：未配置：常量库字段为XW_ANAL_SYS_URL");
			return null;
		}
		String url = XwAnaUrlDto.getParaValue();
		LogUtil.info(MODULE, "行为分析地址结束：成功 地址为" + url);

		return url;
	}

	public static String getRankAnalysUrl() {
		LogUtil.info(MODULE, "获取排行榜行为分析地址开始");

		String baseUrl = getAnalysisUrl();
		if (StringUtil.isBlank(baseUrl)) {
			LogUtil.info(MODULE, "获取排行榜行为分析地址结束：用户行为分析基础地址为空");
			return null;
		}

		String rankUrl = "/service/getGoodsRank";

		return baseUrl + rankUrl;
	}

	public static String doRequest(String url, List<BasicNameValuePair> formparams) {
		LogUtil.info(MODULE, "httpclient请求开始");

		if (StringUtil.isBlank(url)) {
			LogUtil.error(MODULE, "httpclient请求结束：地址为空");
			return null;
		}

		CloseableHttpClient httpclient = null;
		HttpPost httppost = null;
		CloseableHttpResponse response = null;
		String result = null;
		try {
			httpclient = HttpClients.createDefault();

			httppost = new HttpPost(url);

			httppost.setEntity(new UrlEncodedFormEntity(formparams, "UTF-8"));

			response = httpclient.execute(httppost);

			if ((response != null) && (response.getStatusLine() != null)) {
				LogUtil.info(MODULE, "httpclient请求返回状态码：" + response.getStatusLine().getStatusCode());

				HttpEntity entity = response.getEntity();
				result = EntityUtils.toString(entity);
			}
		} catch (ClientProtocolException e) {
			LogUtil.error(MODULE, "远程服务协议异常", e);
		} catch (IOException e) {
			LogUtil.error(MODULE, "远程服务连接异常", e);
		} catch (BusinessException e) {
			LogUtil.error(MODULE, e.getMessage(), e);
		} catch (Exception e) {
			LogUtil.error(MODULE, "调用远程服务未知异常", e);
		} finally {
			try {
				if (response != null)
					try {
						response.close();
					} catch (Exception e) {
						e.printStackTrace();
					}

				if (httppost != null)
					try {
						httppost.releaseConnection();
					} catch (Exception e) {
						e.printStackTrace();
					}

				if (httppost != null)
					try {
						httppost.abort();
					} catch (Exception e) {
						e.printStackTrace();
					}

				if (httpclient != null)
					try {
						httpclient.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
			} catch (Exception e) {
				LogUtil.error(MODULE, "关闭httpclient连接异常", e);
			}
		}
		LogUtil.info(MODULE, "httpclient请求结束");
		return result;
	}
	
}
