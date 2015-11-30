package com.bfd.job.testClient.t02;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * 
 * @author: BFD474
 *
 * @description:
 */
public class TestUpload {

	public static void main(String args[]) throws Exception {

		MultipartEntity multipartEntity = new MultipartEntity(
				HttpMultipartMode.BROWSER_COMPATIBLE,
				"----------ThIs_Is_tHe_bouNdaRY_$", Charset.defaultCharset());
		multipartEntity.addPart("key",
				new StringBody("136********", Charset.forName("UTF-8")));
		// multipartEntity.addPart("receiver", new StringBody("138***********",
		// Charset.forName("UTF-8")));
		// multipartEntity.addPart("image",
		// new FileBody(new File(System.getProperty("user.dir")
		// + "/src/test/resources/123.jpg"), "image/png"));
		multipartEntity.addPart("file", new FileBody(new File(
				"C://Users//BFD474//Desktop//demo.json"),
				"application/octet-stream"));

		HttpPost request = new HttpPost("http://192.168.14.251:9999/recv");
		request.setEntity(multipartEntity);
		request.addHeader("Content-Type",
				"multipart/form-data;");

		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpResponse response = httpClient.execute(request);

		InputStream is = response.getEntity().getContent();
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		StringBuffer buffer = new StringBuffer();
		String line = "";
		while ((line = in.readLine()) != null) {
			buffer.append(line);
		}

		System.out.println("发送消息收到的返回：" + buffer.toString());
	}
}
