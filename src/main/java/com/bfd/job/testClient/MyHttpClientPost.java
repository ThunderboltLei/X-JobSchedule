package com.bfd.job.testClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;

/**
 * @author: Thunderbold
 * @project: TestJava
 * @package: com.test.java.httpclient
 * @filename: MyHttpClientPost.java
 * @createdate: 2013 Apr 25, 2013 2:36:08 PM
 * @description:
 */
public class MyHttpClientPost {

	private final static Logger logger = Logger
			.getLogger(MyHttpClientPost.class);

	public static void main(String[] args) throws Exception {
		try {
			// 定义client对象
			HttpClient client = new HttpClient();

			// 指定传送字符集
			HttpClientParams params = client.getParams();
			params.setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");

			// 设置连接超时时间为2秒（连接初始化时间）
			HttpConnectionManagerParams httpCMParams = client
					.getHttpConnectionManager().getParams();
			httpCMParams.setConnectionTimeout(20000);
			httpCMParams.setSoTimeout(20000);;

			// PostMethod method = new PostMethod(
			// "http://baike.baidu.com/taglist?tag=%CD%F8%C2%E7%D3%CE%CF%B7");
			PostMethod method = new PostMethod(
					"http://192.168.14.251:9999/recv");

			// 添加请求参数
			method.setRequestBody(new NameValuePair[] {
					new NameValuePair("key", "a61408720541cc9544cc4107e3e174bc"),
					new NameValuePair("name", "坐标表") //
			});

			File file = new File("C://Users//BFD474//Desktop//my.rar");
			// Part[] parts = { new FilePart(file.getName(), file) };
			// method.setRequestEntity(new MultipartRequestEntity(parts,
			// method.getParams()));

			FilePart fp = new FilePart("file", file);
			// fp.setContentType(MIME.getMIME(file.getName().substring(
			// file.getName().lastIndexOf(".") + 1)));

			// StringPart:普通的文本参数
			StringPart uname = new StringPart("key", "aa");
			StringPart pass = new StringPart("value", "123456");

			Part[] parts = { uname, pass, fp };

			// 对于MIME类型的请求，httpclient建议全用MulitPartRequestEntity进行包装
			MultipartRequestEntity mre = new MultipartRequestEntity(parts,
					method.getParams());

			method.setRequestEntity(mre);

			int statusCode = client.executeMethod(method);
			if (statusCode != HttpStatus.SC_OK) {
				logger.info("远程访问失败。");
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					method.getResponseBodyAsStream(), "utf-8"));
			StringBuilder response = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				response.append(line).append(
						System.getProperty("line.separator"));
			}
			reader.close();
			logger.info(response.toString());

			client.getHttpConnectionManager().closeIdleConnections(1); // method
																		// 1
			// ((SimpleHttpConnectionManager)client.getHttpConnectionManager()).shutdown();
			// // method 2
		} catch (Exception e) {
			logger.error(e);
		}
	}

	// get value of meta
	public void getMetaData() {

	}
}
