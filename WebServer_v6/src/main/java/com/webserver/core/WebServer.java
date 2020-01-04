package com.webserver.core;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServer {
	private ServerSocket server;
	
	/** 构造方法（初始化） */
	public WebServer() {
		try {
			System.out.println("正在启动服务端...");
			server = new ServerSocket(8088);
			System.out.println("服务端启动完毕!");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/** 服务端开始工作的方法 */
	public void start() {
		try {
			System.out.println("等待客户端的连接...");
			// 阻塞等待客户端的连接
			Socket socket = server.accept();
			System.out.println("一个客户端连接了!");
			// 启动线程，处理该客户端交互
			ClientHandler handler = new ClientHandler(socket);
			Thread t = new Thread(handler);
			t.start();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		WebServer server = new WebServer();
		server.start();
	}
}
