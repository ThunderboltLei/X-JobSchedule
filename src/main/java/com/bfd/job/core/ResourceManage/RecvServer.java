package com.bfd.job.core.ResourceManage;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Properties;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.bfd.job.utils.props.PropertiesUtil;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.spi.HttpServerProvider;

/**
 * 
 * @author: BFD474
 *
 * @description: 处理接收到文件的服务
 */
public class RecvServer {

	private static final Logger logger = Logger.getLogger(RecvServer.class);

	private static RecvServer httpServer;

	private RecvHandler recvHandler = new RecvHandler();

	// RECV服务配置信息
	private String RECV_IP = "127.0.0.1", //
			RECV_PATH = "/recv" //
			; //
	private int RECV_PORT = 0, //
			RECV_SERVER_COUNT = 1000 //
			; //

	public void httpserverService() throws IOException {
		// RECV服务配置信息
		Properties props = PropertiesUtil.getProps();
		RECV_IP = props.getProperty("RECV_IP");
		RECV_PORT = Integer.parseInt(props.getProperty("RECV_PORT"));
		RECV_SERVER_COUNT = Integer.parseInt(props
				.getProperty("RECV_SERVER_COUNT"));
		RECV_PATH = props.getProperty("RECV_PATH");
		logger.info(RECV_IP + ", " + RECV_PORT + ", " + RECV_PATH);

		// RECV服务
		HttpServerProvider provider = HttpServerProvider.provider();
		HttpServer httpserver = provider.createHttpServer(
				new InetSocketAddress(RECV_IP, RECV_PORT), RECV_SERVER_COUNT);
		httpserver.createContext(RECV_PATH, recvHandler);
		httpserver.setExecutor(null);
		httpserver.start();
		logger.info("server started");
	}

	// Singleton
	private RecvServer() {

	}

	public static RecvServer getInstance() {
		if (httpServer == null) {
			httpServer = new RecvServer();
		}
		return httpServer;
	}

	public static void main(String[] args) throws IOException {
		// if (args.length != 1) {
		// logger.info("请指定IP~~~");
		// System.exit(0);
		// } else {
		// logger.info("ip = " + args[0]);

		RecvServer httpserver = RecvServer.getInstance();
		httpserver.httpserverService();

		logger.info("输入Exit退出系统: ");
		Scanner sca = new Scanner(System.in);
		String s = sca.next();
		logger.info("您输入的内容是: " + s);
		if ("exit".equalsIgnoreCase(s)) {
			logger.info("退出系统~~~");
			System.exit(0);
		} else {
			logger.info("输入Exit退出系统: ");
		}
		// }
	}
}
