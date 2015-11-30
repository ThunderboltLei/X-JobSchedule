package com.bfd.job.testClient.t01;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * 
 * @author: BFD474
 *
 * @description: FormFile 该JavaBean用来封装上传文件的数据<br>
 */
public class FormFile {
	/* 上传文件的数据 */
	private byte[] data;
	private InputStream inStream;
	private File file;
	/* 文件名称 */
	private String filname;
	/* 请求参数名称 */
	private String parameterName;
	/* 内容类型 */
	private String contentType = "application/octet-stream";

	/*
	 * 下面是两个构造函数，主要用于大小不同的文件的上传
	 */
	// 该构造器适用于文件小于1M，把数据全部转换为二进制，放到手机内存
	public FormFile(String filname, byte[] data, String parameterName,
			String contentType) {
		this.data = data;
		this.filname = filname;
		this.parameterName = parameterName;
		if (contentType != null)
			this.contentType = contentType;
	}

	// 大文件用输入、输出流进行边读取边上传
	public FormFile(String filname, File file, String parameterName,
			String contentType) {
		this.filname = filname;
		this.parameterName = parameterName;
		this.file = file;
		try {
			this.inStream = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if (contentType != null)
			this.contentType = contentType;
	}

	public File getFile() {
		return file;
	}

	public InputStream getInStream() {
		return inStream;
	}

	public byte[] getData() {
		return data;
	}

	public String getFilname() {
		return filname;
	}

	public void setFilname(String filname) {
		this.filname = filname;
	}

	public String getParameterName() {
		return parameterName;
	}

	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

}