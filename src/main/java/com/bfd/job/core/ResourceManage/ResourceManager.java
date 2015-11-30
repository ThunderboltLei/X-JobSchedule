package com.bfd.job.core.ResourceManage;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

import org.apache.log4j.Logger;

import com.bfd.job.enums.TaskEnums;

/**
 * 
 * @author: BFD474
 *
 * @description: ResourceManager接收任务，并分发任务
 */
public class ResourceManager {
	
	private static final Logger logger = Logger.getLogger(ResourceManager.class);

	public static String WEB_ROOT = "C:/Users/BFD474/Desktop/"; // 服务器根目录，post.html,
																// upload.html都放在该位置

	private int port; // 端口

	private String requestPath, // 用户请求的文件的url
			boundary = null;// mltipart/form-data方式提交post的分隔符,

	private int contentLength = 0; // post提交请求的正文的长度

	public ResourceManager(String root, int port) {
		this.WEB_ROOT = root;
		this.port = port;
		this.requestPath = null;
	}
	
	// ResourceManager根据任务类型，选择处理逻辑
	public void switchTask(String taskName, String token, String timestamp){
		// 在线数据包，区别于在线单条，因为是个数据集，所以采用分布式处理
		if(TaskEnums.OnlinePackages.getTaskName().equals(taskName)){
			
		}
		// 离线数据包，如果包含多个文件，则采用分布式处理。master合并数据文件，再提交spark任务
		else if(TaskEnums.OfflinePackages.getTaskName().equals(taskName)){
			
		}
	}

	/**
	 * 处理GET请求
	 * 
	 * @param reader
	 * @param out
	 * @throws Exception
	 */
	private void doGet(DataInputStream reader, OutputStream out)
			throws Exception {
		if (new File(WEB_ROOT + this.requestPath).exists()) {
			// 从服务器根目录下找到用户请求的文件并发送回浏览器
			InputStream fileIn = new FileInputStream(WEB_ROOT
					+ this.requestPath);
			byte[] buf = new byte[fileIn.available()];
			fileIn.read(buf);
			out.write(buf);
			out.close();
			fileIn.close();
			reader.close();
			System.out.println("request complete.");
		}
	}

	private String fileName;

	/**
	 * 处理post请求
	 * 
	 * @param reader
	 * @param out
	 * @throws Exception
	 */
	private void doPost(DataInputStream reader, OutputStream out)
			throws Exception {
		String line = reader.readLine();
		while (null != line) {
			System.out.println(line);
			line = reader.readLine();
			if ("".equals(line)) {
				break;
			} else if (line.indexOf("Content-Length") != -1) {
				this.contentLength = Integer.parseInt(line.substring(line
						.indexOf("Content-Length") + 16));
				System.out.println("post - else if - " + this.boundary);
			}
			// 表明要上传附件， 跳转到doMultiPart方法。
			else if (line.indexOf("multipart/form-data") != -1) {
				// 得multiltipart的分隔符
				this.boundary = line.substring(line.indexOf("boundary") + 9);
				// this.doMultiPart(reader, out);
				System.out.println("post - else");
				return;
			} else if (line.indexOf("file-name") != -1) {
				this.fileName = line.split(":")[1].trim();
			}
		}
		// 继续读取普通post（没有附件）提交的数据
		System.out.println("begin reading posted data......");
		// 文本内容
		String dataLine = null;
		// 用户发送的post数据正文
		byte[] buf = {};
		int size = 0;
		if (this.contentLength != 0) {
			buf = new byte[this.contentLength];
			while (size < this.contentLength) {
				int c = reader.read();
				buf[size++] = (byte) c;
			}

			File f = new File("/home/lm8212/Desktop/demo/" + this.fileName);
			f.createNewFile();
			FileChannel fc = new FileOutputStream(f).getChannel();
			fc.write(ByteBuffer.wrap(buf));
			fc.close();
			System.out.println("The data user posted: \n"
					+ new String(buf, 0, size));
		}
		// 发送回浏览器的内容
		String response = "";
		response += "HTTP/1.1 200 OK/n";
		response += "Server: Sunpache 1.0/n";
		response += "Content-Type: text/html/n";
		response += "Last-Modified: Mon, 11 Jan 1998 13:23:42 GMT/n";
		response += "Accept-ranges: bytes";
		response += "/n";
		// String body =
		// "<html><head><title>test server</title></head><body><p>post ok:</p>"
		// + new String(buf, 0, size) + "</body></html>";
		String body = "<html><head><title>test server</title></head><body><p>post is ok!</p></body></html>";
		System.out.println(body);
		out.write(response.getBytes());
		out.write(body.getBytes());
		out.flush();
		reader.close();
		out.close();
		System.out.println("request complete.");
	}

	/**
	 * 处理附件
	 * 
	 * @param reader
	 * @param out
	 * @throws Exception
	 */
	private void doMultiPart(DataInputStream reader, OutputStream out)
			throws Exception {
		System.out.println("doMultiPart ......");
		String line = reader.readLine();
		while (line != null) {
			System.out.println(line);
			line = reader.readLine();
			if ("".equals(line)) {
				break;
			} else if (line.indexOf("Content-Length") != -1) {
				this.contentLength = Integer.parseInt(line.substring(line
						.indexOf("Content-Length") + 16));
				System.out.println("contentLength: " + this.contentLength);
			} else if (line.indexOf("boundary") != -1) {
				// 获取multipart分隔符
				this.boundary = line.substring(line.indexOf("boundary") + 9);
			}
		}
		System.out.println("begin get data......");
		/*
		 * 下面的注释是一个浏览器发送带附件的请求的全文，所有中文都是说明性的文字***** <HTTP头部内容略> ............
		 * Cache-Control: no-cache <这里有一个空行，表明接下来的内容都是要提交的正文>
		 * -----------------------------7d925134501f6<这是multipart分隔符>
		 * Content-Disposition: form-data; name="myfile"; filename="mywork.doc"
		 * Content-Type: text/plain
		 * 
		 * <附件正文>........................................
		 * .................................................
		 * 
		 * -----------------------------7d925134501f6<这是multipart分隔符>
		 * Content-Disposition: form-data; name="myname"<其他字段或附件> <这里有一个空行>
		 * <其他字段或附件的内容>
		 * -----------------------------7d925134501f6--<这是multipart分隔符
		 * ，最后一个分隔符多两个->
		 * **************************************************************
		 */
		/**
		 * 上面的注释是一个带附件的multipart类型的POST的全文模型， 要把附件去出来，就是要找到附件正文的起始位置和结束位置
		 * **/
		if (this.contentLength != 0) {
			// 把所有的提交的正文，包括附件和其他字段都先读到buf.
			byte[] buf = new byte[this.contentLength];
			int totalRead = 0;
			int size = 0;
			while (totalRead < this.contentLength) {
				size = reader.read(buf, totalRead, this.contentLength
						- totalRead);
				totalRead += size;
			}
			// 用buf构造一个字符串，可以用字符串方便的计算出附件所在的位置
			String dataString = new String(buf, 0, totalRead);
			System.out.println("the data user posted:/n" + dataString);
			int pos = dataString.indexOf(boundary);
			// 以下略过4行就是第一个附件的位置
			pos = dataString.indexOf("/n", pos) + 1;
			pos = dataString.indexOf("/n", pos) + 1;
			pos = dataString.indexOf("/n", pos) + 1;
			pos = dataString.indexOf("/n", pos) + 1;
			// 附件开始位置
			int start = dataString.substring(0, pos).getBytes().length;
			pos = dataString.indexOf(boundary + "--", pos) - 4;
			// 附件结束位置
			int end = dataString.substring(0, pos).getBytes().length;
			// 以下找出filename
			int fileNameBegin = dataString.indexOf("filename") + 10;
			int fileNameEnd = dataString.indexOf("/n", fileNameBegin);
			String fileName = dataString.substring(fileNameBegin, fileNameEnd);
			System.out.println("fileName: " + fileName);
			/**
			 * 有时候上传的文件显示完整的文件名路径,比如c:/my file/somedir/project.doc
			 * 但有时候只显示文件的名字，比如myphoto.jpg. 所以需要做一个判断。
			 */
			if (fileName.lastIndexOf("//") != -1) {
				fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
			}
			fileName = fileName.substring(0, fileName.length() - 2);
			OutputStream fileOut = new FileOutputStream(
					"/home/lm8212/Desktop/demo/" + fileName);
			fileOut.write(buf, start, end - start);
			fileOut.close();
			fileOut.close();
		}
		String response = "";
		response += "HTTP/1.1 200 OK/n";
		response += "Server: Sunpache 1.0/n";
		response += "Content-Type: text/html/n";
		response += "Last-Modified: Mon, 11 Jan 1998 13:23:42 GMT/n";
		response += "Accept-ranges: bytes";
		response += "/n";
		out.write("<html><head><title>test server</title></head><body><p>Post is ok</p></body></html>"
				.getBytes());
		out.flush();
		reader.close();
		System.out.println("request complete.");
	}

	/**
	 * 服务方法
	 * 
	 * @throws Exception
	 */
	public void service() throws Exception {
		ServerSocket serverSocket = new ServerSocket(this.port);
		System.out.println("server is ok.");
		// 开启serverSocket等待用户请求到来，然后根据请求的类别作处理
		// 在这里我只针对GET和POST作了处理
		// 其中POST具有解析单个附件的能力
		while (true) {
			try {
				System.out.println("new request coming.");
				Socket socket = serverSocket.accept();
				DataInputStream reader = new DataInputStream(
						(socket.getInputStream()));
				String line = reader.readLine();
				if (line != null) {
					String method = line.substring(0, 4).trim();
					OutputStream out = socket.getOutputStream();
					this.requestPath = line.split(" ")[1];
					System.out.println(method);
					if ("GET".equalsIgnoreCase(method)) {
						System.out.println("do get......");
						this.doGet(reader, out);
					} else if ("POST".equalsIgnoreCase(method)) {
						System.out.println("do post......");
						this.doPost(reader, out);
					}
				} else {
					System.err.println("line is null!");
				}
				socket.close();
				System.out.println("socket closed.");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * String 转换 ByteBuffer
	 * 
	 * @param str
	 * @return
	 */
	@Deprecated
	public static ByteBuffer getByteBuffer(String str) {
		return ByteBuffer.wrap(str.getBytes());
	}

	/**
	 * ByteBuffer 转换 String
	 * 
	 * @param buffer
	 * @return
	 */
	@Deprecated
	public static String getString(ByteBuffer buffer) {
		Charset charset = null;
		CharsetDecoder decoder = null;
		CharBuffer charBuffer = null;
		try {
			charset = Charset.forName("UTF-8");
			decoder = charset.newDecoder();
			charBuffer = decoder.decode(buffer);// 用这个的话，只能输出来一次结果，第二次显示为空
			// charBuffer = decoder.decode(buffer.asReadOnlyBuffer());
			return charBuffer.toString();
		} catch (Exception ex) {
			ex.printStackTrace();
			return "";
		}
	}

	public static void main(String args[]) throws Exception {
		ResourceManager server = new ResourceManager("/home/lm8212/Desktop", 9999);
		server.service();
	}
}
