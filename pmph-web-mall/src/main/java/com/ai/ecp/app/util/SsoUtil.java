package com.ai.ecp.app.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.ai.paas.utils.LogUtil;
import com.ailk.butterfly.core.config.Application;

import net.sf.json.JSONObject;

public class SsoUtil {

	public static String MODULE = SsoUtil.class.getName();

	public static String URL;
	public static String UserName;
	public static String PassWord;
	public static String language = "zh-cn";

	static {
		SsoUtil.URL = Application.getValue("url.ssoUrl");
		SsoUtil.UserName = Application.getValue("url.ssoAdmin");
		SsoUtil.PassWord = Application.getValue("url.ssoPassword");
	}

	public static JSONObject postAuthSso(String url, Map<Object, Object> map) {
		CloseableHttpClient httpClient = getHttpClient();
		String httpUrl = URL+ url;
		try {
			HttpPost post = new HttpPost(httpUrl); // 这里用上本机的某个工程做测试
			// 创建参数列表
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			Iterator<Object> it = map.keySet().iterator();
			while (it.hasNext()) {
				String key = (String) it.next();
				String value = (String) map.get(key);
				list.add(new BasicNameValuePair(key, value));
			}
			// url格式编码
			UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(list, "UTF-8");
			post.setEntity(uefEntity);
			// 执行请求
			CloseableHttpResponse httpResponse = httpClient.execute(post);
			try {
				HttpEntity entity = httpResponse.getEntity();
				String result = EntityUtils.toString(entity, "utf-8");
				JSONObject msgAry = new JSONObject();
				if (null != entity) {
					msgAry = JSONObject.fromObject(result);
				}
				return msgAry;
			} finally {
				httpResponse.close();
			}

		} catch (UnsupportedEncodingException e) {
			LogUtil.error(MODULE, "获取返回参数异常", e);
		} catch (IOException e) {
			LogUtil.error(MODULE, "IO异常", e);
		} finally {
			try {
				closeHttpClient(httpClient);
			} catch (Exception e) {
				LogUtil.error(MODULE, "关闭httpClient异常", e);
			}
		}
		return null;
	}

	public static String getAuthSso() {
		String urlNameString = "http://books123456789.ipmph.com/newsso/doLogin?UserName=wangbh789&Password=wangbh123&ContentType=json";
		String result = "";
		try {
			// 根据地址获取请求
			HttpGet request = new HttpGet(urlNameString);// 这里发送get请求
			// 获取当前客户端对象
			HttpClient httpClient = getHttpClient();
			// 通过请求对象获取响应对象
			HttpResponse response = httpClient.execute(request);

			// 判断网络连接状态码是否正常(0--200都数正常)
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				result = EntityUtils.toString(response.getEntity(), "utf-8");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
		// ....result是用户信息,站内业务以及具体的json转换这里也不写了...
	}

	private static CloseableHttpClient getHttpClient() {
		return HttpClients.createDefault();
	}

	private static void closeHttpClient(CloseableHttpClient client) throws IOException {
		if (client != null) {
			client.close();
		}
	}

	public static void main(String[] args) {
		
/*		Map<Object, Object> map1 = new HashMap<Object, Object>();
		map1.put("username", "admin");
		map1.put("password", "admin");
		map1.put("language", SsoUtil.language);
		
		map1.put("method", "ZAS.SMSSend");
		JSONObject value1 = new JSONObject();
		value1.put("Mobile", "18906911225");
		map1.put("params", value1.toString());
		JSONObject jsonObject1  = SsoUtil.postAuthSso("/api/json", map1);
		boolean flag = jsonObject1.getBoolean("Success");*/
		
		
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("username", "admin");
		map.put("password", "admin");
		map.put("language", SsoUtil.language);
		
		map.put("method", "ZAS.MobileRegist");
		JSONObject value = new JSONObject();
		value.put("Mobile", "18906911225");
		value.put("SMSCode", "090300");
		value.put("Password", "111111");
		value.put("PwdIsEncryption", "");
		map.put("params", value.toString());
		JSONObject jsonObject  = SsoUtil.postAuthSso("/api/json", map);
		boolean flag = jsonObject.getBoolean("Success");
		System.out.println(jsonObject);
	}

}
