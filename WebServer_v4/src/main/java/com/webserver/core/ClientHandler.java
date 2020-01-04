package com.webserver.core;

import java.io.InputStream;
import java.net.Socket;
import com.webserver.http.HttpRequest;

/**
 * 负责与指定客户端交互
 * HTTP协议交互方式为一问一答，因此当前处理模式分三步：
 * 1.解析请求
 * 2.处理请求
 * 3.响应客户端
 */
public class ClientHandler implements Runnable{
	private Socket socket;
	
	public ClientHandler(Socket socket) {
		this.socket = socket;
	}
	
	public void run() {
		try {
			System.out.println("ClientHandler:开始处理交互...");
			System.out.println("ClientHandler:1.解析请求...");
			/* 1.解析请求 */
			HttpRequest request = new HttpRequest(socket);
			System.out.println("ClientHandler:解析请求完毕！");
			/* 2.处理请求 */
			
			/* 3.响应请求 */
			
			System.out.println("ClientHandler:处理交互完毕！");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
