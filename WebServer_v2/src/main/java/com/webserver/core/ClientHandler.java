package com.webserver.core;

import java.io.InputStream;
import java.net.Socket;

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
			InputStream in = socket.getInputStream();
			int d = -1;
			int c1 = -1; // 表示上次读取到的内容
			int c2 = -1; // 表示本次读取到的内容
			StringBuilder builder = new StringBuilder();
			while((c2 = in.read()) != -1) {
				if(c1==13&&c2==10) {
					break;
				}
				builder.append((char)c2);
				c1 = c2;
			}
			String line = builder.toString().trim();
			System.out.println(line);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
