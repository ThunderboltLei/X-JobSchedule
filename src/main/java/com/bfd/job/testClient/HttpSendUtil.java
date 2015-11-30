package com.bfd.job.testClient;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

/**
 * 
 * @Author: lm8212<br>
 * @Date: 2014年8月30日 下午2:59:00<br>
 * @Project: TestJava<br>
 * @Package: com.test.java.http.httpclient<br>
 * @File: HttpSendUtil.java<br>
 * @Description: 使用httpclient-*.jar<br>
 */
public class HttpSendUtil {

	private final static Logger logger = Logger.getLogger(HttpSendUtil.class);

//	public static String HTTP_HOST = "http://yuntuapi.amap.com/datamanage/table/create";
	public static String HTTP_HOST = "http://127.0.0.1:9999/recv";

	public static void main(String[] args) {
		HttpSendUtil test = new HttpSendUtil();
		logger.info(test.doSend(HTTP_HOST));
	}

	/**
	 * 访问指定URL
	 * 
	 * @param url
	 * @return
	 */
	public String doSend(String url) {
		// 先将参数放入List，再对参数进行URL编码
		List<BasicNameValuePair> list_params = new LinkedList<BasicNameValuePair>();
		list_params.add(new BasicNameValuePair("key",
				"a61408720541cc9544cc4107e3e174bc"));
		list_params.add(new BasicNameValuePair("name", "t_loc_subway"));

		// 对参数编码
		String params = URLEncodedUtils.format(list_params, "UTF-8");
		logger.info(params);

		String result = null;
		try {
			// HTTP客户端
			HttpClient http = new DefaultHttpClient();
			url = url + "?" + params;
			logger.info(url);

			/**
			 * Get
			 */
			// HttpGet get = new HttpGet(url);
			// HttpResponse httpResponse = http.execute(get);

			/**
			 * Post
			 */
			HttpPost post = new HttpPost(url);
			post.setHeader("content-type", "application/x-www-form-urluncoded");

			HttpResponse httpResponse = http.execute(post);
			// 若状态码为200 ok
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				// 读返回数据
				result = EntityUtils
						.toString(httpResponse.getEntity(), "utf-8");
				// logger.info("resCode = "
				// + httpResponse.getStatusLine().getStatusCode()); // 获取响应码
				// logger.info("result = " + result);// 获取服务器响应内容
			} else { // 输出错误信息
				logger.info("Error Response: "
						+ httpResponse.getStatusLine().toString());
			}

			http.getConnectionManager().shutdown();// 彻底关闭当前HttpClient, method 1
		} catch (Exception e) {
			logger.error(e);
		}
		return result;
	}

	/**
	 * 
	 * @param baseUrl
	 * @param params
	 * @return
	 */
	public String getParamUrl(String baseUrl, Map<String, String[]> params) {
		if (baseUrl == null)
			return null;
		StringBuilder sb = new StringBuilder(baseUrl);
		if (params != null && params.size() > 0) {
			if (!baseUrl.endsWith("?"))
				sb.append("?");
			List<NameValuePair> paramList = new ArrayList<NameValuePair>();
			for (Entry<String, String[]> entry : params.entrySet()) {
				for (String value : entry.getValue()) {
					paramList
							.add(new BasicNameValuePair(entry.getKey(), value));
				}
			}
			sb.append(URLEncodedUtils.format(paramList, "utf-8"));
		}
		return sb.toString();
	}

}
