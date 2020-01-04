package com.webserver.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import com.webserver.http.EmptyRequestException;
import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;

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
			/* 
			 * 1.解析请求(调用HttpRequest类进行解析处理)
			 */
			HttpRequest request = new HttpRequest(socket);
			System.out.println("ClientHandler:解析请求完毕！\n");
			
			/* 
			 * 2.处理请求(调用HttpResponse类flush方法将一个标准的HTTP响应格式发送给客户端)
			 */
			System.out.println("ClientHandler:2.处理请求...");
			HttpResponse response = new HttpResponse(socket);
			// 根据请求获取用户请求资源的抽象路径
			String path = request.getUri();
			System.out.println("抽象路径："+path);
			// 根据该抽象路径去webapps目录下寻找资源
			File file = new File("./webapps"+path);
			if(file.exists()) {
				System.out.println("资源已找到!");
				response.setEntity(file);
			}else {
				System.out.println("资源不存在!");
				File notFoundPage = new File("./webapps/root/404.html");
				response.setStatusCode(404);
				response.setStatusReason("Not Found");
				response.setEntity(notFoundPage);
			}
			
			System.out.println("ClientHandler:处理请求完毕！\n");
			/* 
			 * 3.响应客户端
			 */
			response.flush();
			System.out.println("ClientHandler:处理交互完毕！");
		}catch (EmptyRequestException e) {
			// 出现了空请求，不做任何处理
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			/*
			 * 先维持HTTP1.0协议的交互方式，一问一答后与客户端断开TCP连接
			 */
			try {
				socket.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
