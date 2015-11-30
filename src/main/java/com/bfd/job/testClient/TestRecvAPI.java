package com.bfd.job.testClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author: Thunderbold
 * @project: EverGNQuery
 * @package: com.eversoc.gateway.test02
 * @filename: TestHttpServer02.java
 * @createdate: 2013 May 13, 2013 9:45:10 AM
 * @description:
 */
public class TestRecvAPI {
	
	public static void main(String[] args) {
		ExecutorService exec = Executors.newCachedThreadPool();
		// 测试并发对MyHttpServer的影响
		for (int i = 0; i < 1; i++) {
			Runnable run = new Runnable() {
				public void run() {
					try {
						startWork();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			};
			exec.execute(run);
		}
		exec.shutdown();// 关闭线程池
	}

	public static void startWork() throws IOException {
		URL url = new URL("http://192.168.14.251:9999/recv");
		HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
		urlConn.setDoOutput(true);
		urlConn.setDoInput(true);
		urlConn.setRequestMethod("POST");
		// 测试内容包
		String teststr = "key=hello&value=world";
		OutputStream out = urlConn.getOutputStream();
		out.write(teststr.getBytes());
		
		while (urlConn.getContentLength() != -1) {
			if (urlConn.getResponseCode() == 200) {
				InputStream in = urlConn.getInputStream();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(in));
				String receive = null;
				while ((receive = reader.readLine()) != null) {
					System.err.println("server response:" + receive);// 打印收到的信息
				}
				reader.close();
				in.close();
			}
		}
		out.flush();
		out.close();
		urlConn.disconnect();		
	}
}
