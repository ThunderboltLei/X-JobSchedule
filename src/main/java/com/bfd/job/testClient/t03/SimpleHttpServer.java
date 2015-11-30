package com.bfd.job.testClient.t03;

import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.*;
import java.util.concurrent.*;
import java.io.*;

/**
 * 
 * @author: BFD474
 *
 * @description:
 */
public class SimpleHttpServer {
	private int port = 9999;
	private ServerSocketChannel serverSocketChannel = null;
	private ExecutorService executorService;
	private static final int POOL_MULTIPLE = 4;

	public SimpleHttpServer() throws IOException {
		executorService = Executors.newFixedThreadPool(Runtime.getRuntime()
				.availableProcessors() * POOL_MULTIPLE);
		serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.socket().setReuseAddress(true);
		serverSocketChannel.socket().bind(new InetSocketAddress(port));
	}

	public void service() {
		while (true) {
			SocketChannel socketChannel = null;
			try {
				socketChannel = serverSocketChannel.accept();
				executorService.execute(new Handler(socketChannel));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws IOException {
		new SimpleHttpServer().service();
	}

}

class Handler implements Runnable {
	private SocketChannel socketChannel;

	public Handler(SocketChannel socketChannel) {
		this.socketChannel = socketChannel;
	}

	@Override
	public void run() {
		handle(socketChannel);
	}

	private void handle(SocketChannel socketChannel) {
		try {
			Socket socket = socketChannel.socket();
			System.out
					.println(socket.getInetAddress() + ":" + socket.getPort());
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			socketChannel.read(buffer);
			buffer.flip();
			String request = decode(buffer);
			System.out.println(request);
			StringBuffer sb = new StringBuffer("HTTP/1.1 200 OK\r\n");
			sb.append("Content-Type:application/octet-stream;chartset=UTF-8\r\n");
			socketChannel.write(encode(sb.toString()));

			FileInputStream in = null;
			String firstLineOfRequest = request.substring(0,
					request.indexOf("\r\n"));
			if (firstLineOfRequest.indexOf("recv") != -1)
				in = new FileInputStream("recv");
			else {
				File f = new File("C://Users//BFD474//Desktop//hello.rar");
				f.createNewFile();
				in = new FileInputStream(f);
			}
			FileChannel fileChannel = in.getChannel();
			fileChannel.transferTo(0, fileChannel.size(), socketChannel);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (socketChannel != null)
					socketChannel.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private Charset charset = Charset.forName("GBK");

	private ByteBuffer encode(String string) {
		return ByteBuffer.allocate(string.length() * 2).get(
				string.getBytes(charset));
	}

	private String decode(ByteBuffer buffer) {
		byte[] source = new byte[buffer.position() + 1];
		buffer.put(source);
		return new String(source, charset);
	}
}
