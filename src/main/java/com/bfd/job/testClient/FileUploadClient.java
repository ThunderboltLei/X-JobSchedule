package com.bfd.job.testClient;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;

/**
 * 
 * @author: BFD474
 *
 * @description: This is OK.
 */
public class FileUploadClient {
	
	public static void main(String[] args) {
		try {
			// URL url = new
			// URL("http://127.0.0.1:8888/fileSys/getFile.action?filePath=/c/v/b/&fileName=test.zip");

			// source file
			File file = new File("/home/lm8212/Desktop/my.zip");

			// URL
			URL url = new URL("http://127.0.0.1:9999/recv");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "text/html");
			conn.setRequestProperty("Cache-Control", "no-cache");
			conn.setRequestProperty("Charsert", "UTF-8");
			conn.setRequestProperty("file-name", file.getName());
			conn.connect();
			conn.setConnectTimeout(10000);
			OutputStream out = conn.getOutputStream();

			int bytes = 0;
			byte[] buffer = new byte[1024];

			// 写文件内容
			DataInputStream in = new DataInputStream(new FileInputStream(file));
			while ((bytes = in.read(buffer)) != -1) {
				out.write(buffer, 0, bytes);
			}

			in.close();
			out.flush();
			out.close();

			// 读取URLConnection的响应
			DataInputStream retDIS = new DataInputStream(conn.getInputStream());
			conn.disconnect();
		} catch (Exception e) {
			System.out.println("发送文件出现异常！" + e);
			e.printStackTrace();
		}
	}
}
