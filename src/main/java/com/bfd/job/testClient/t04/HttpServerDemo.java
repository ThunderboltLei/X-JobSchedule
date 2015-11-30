package com.bfd.job.testClient.t04;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
//在eclipse中会出现Access restriction: The type Headers is not accessible due to restriction on required library
//解决办法：把Windows-Preferences-Java-Complicer- Errors/Warnings里面的Deprecated and restricted API中的Forbidden references(access rules)选为Warning就可以编译通过。
/**
 * 使用jdk自带sun httpserver组件构建Http服务器，
 * JDK自带的HttpServer是一个非常轻量级的Http服务端框架，但是它非常灵活，易于扩展，
 * @author Administrator
 *
 */
public class HttpServerDemo {
    public static void main(String[] args) throws IOException {
        InetSocketAddress addr = new InetSocketAddress(9999);
        HttpServer server = HttpServer.create(addr, 0);

        server.createContext("/recv", new MyHandler());
        server.setExecutor(Executors.newCachedThreadPool());
        server.start();
        System.out.println("Server is listening on port 8080");
    }
}

class MyHandler implements HttpHandler {
	
    public void handle(HttpExchange exchange) throws IOException {
    	
        String requestMethod = exchange.getRequestMethod();
        System.out.println("处理新请求:"+requestMethod);
        if (requestMethod.equalsIgnoreCase("POST")) {
            Headers responseHeaders = exchange.getResponseHeaders();
            responseHeaders.set("Content-Type", "text/plain");
            exchange.sendResponseHeaders(200, 0);

            OutputStream responseBody = exchange.getResponseBody();
            Headers requestHeaders = exchange.getRequestHeaders();
            Set<String> keySet = requestHeaders.keySet();
            Iterator<String> iter = keySet.iterator();
            while (iter.hasNext()) {
                String key = iter.next();
                List values = requestHeaders.get(key);
                String s = key + " = " + values.toString() + "\n";
                responseBody.write(s.getBytes());
            }
            responseBody.close();
        }
    }
}

