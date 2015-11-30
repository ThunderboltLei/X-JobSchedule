package com.bfd.job.core.ResourceManage;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.apache.log4j.Logger;

import com.bfd.job.core.ResourceManage.slave.SlaveNode;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * @author: Thunderbold
 * @project: EverGNQuery
 * @package: com.eversoc.gateway.test02
 * @filename: LogHandler.java
 * @createdate: 2013 May 13, 2013 9:28:14 AM
 * @description:
 */

public class RecvHandler implements HttpHandler {
	
	private static final Logger logger = Logger.getLogger(RecvHandler.class);

	public RecvHandler() {
		logger.info("initialize a httphandler...");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sun.net.httpserver.HttpHandler#handle(com.sun.net.httpserver.HttpExchange
	 * )
	 */
	@Override
	public void handle(HttpExchange httpExchange) throws IOException {
		// TODO Auto-generated method stub

		// String responseMsg = "ok"; // 响应信息

		// String responseMsg = "hello world~~~"; // 响应信息

		// String responseMsg =
		// "<?xml version=\"1.0\" encoding=\"UTF-8\"?><a><a1>a1</a1><a2>a2</a2></a>";

		logger.info("key: " + httpExchange.getAttribute("key"));
		
//		Headers headers = httpExchange.getRequestHeaders();
//		for(Map.Entry<String,List<String>> e : headers.entrySet()){
//			logger.info(e.getKey());
//		}
			
		DataInputStream in = new DataInputStream(httpExchange.getRequestBody()); // 获得输入流
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String body = "";
		String temp = null;
		while ((temp = reader.readLine()) != null) {
			if (null != temp.trim() || temp.trim().equals("")) {
				body += temp.trim();
			}
		}
		logger.info("\nclient request: " + URLDecoder.decode(body));

		int xmlLength = body.length();
		logger.info("xmlLength: " + xmlLength);
		httpExchange.sendResponseHeaders(200, xmlLength * 2); // 设置响应头属性及响应信息的长度
		OutputStream out = httpExchange.getResponseBody(); // 获得输出流

		// encode to "utf-8"
		String str = URLEncoder.encode("200", "utf-8");
		out.write(str.getBytes());
		out.flush();

		httpExchange.close();
	}

}
