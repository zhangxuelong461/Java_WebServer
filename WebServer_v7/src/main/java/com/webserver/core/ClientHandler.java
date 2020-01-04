package com.webserver.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
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
			/* 
			 * 1.解析请求(调用HttpRequest类进行解析处理)
			 */
			HttpRequest request = new HttpRequest(socket);
			System.out.println("ClientHandler:解析请求完毕！\n");
			
			/* 
			 * 2.处理请求
			 */
			System.out.println("ClientHandler:2.处理请求...");
			// 根据请求获取用户请求资源的抽象路径
			String path = request.getUri();
			System.out.println("抽象路径："+path);
			// 根据该抽象路径去webapps目录下寻找资源
			File file = new File("./webapps"+path);
			if(file.exists()) {
				System.out.println("资源已找到!");
				// 获取输出流
				OutputStream out = socket.getOutputStream();
				// 1.发送状态行
				String line = "HTTP/1.1 200 OK";
				out.write(line.getBytes("ISO8859-1"));
				out.write(13); // written CR
				out.write(10); // written LF
				// 2.发送响应头
				line = "Content-Type: text/html";
				out.write(line.getBytes("ISO8859-1"));
				out.write(13);
				out.write(10);
				line = "Content-Length:" + file.length();
				out.write(line.getBytes("ISO8859-1"));
				out.write(13);
				out.write(10);
				// 响应头结束标记，单独发送CRLF
				out.write(13);
				out.write(10);
				// 3.发送响应正文
				FileInputStream fis = new FileInputStream(file);
				int len = -1;
				byte[] data = new byte[1024*8];
				while((len = fis.read(data)) != -1) {
					out.write(data,0,len);
				}
				
			}else {
				file = new File("./webapps/root/404.html");
				System.out.println("资源不存在!");
				// 获取输出流
				OutputStream out = socket.getOutputStream();
				// 1.发送状态行
				String line = "HTTP/1.1 404 Not Found";
				out.write(line.getBytes("ISO8859-1"));
				out.write(13);
				out.write(10);
				// 2.发送响应头
				line = "Content-Type: text/html";
				out.write(line.getBytes("ISO8859-1"));
				out.write(13);
				out.write(10);
				line = "Content-Length: " + file.length();
				out.write(line.getBytes("ISO8859-1"));
				out.write(13);
				out.write(10);
				// 响应头结束，单独发送一个CRLF
				out.write(13);
				out.write(10);
				// 3.发送响应正文
				FileInputStream fis = new FileInputStream(file);
				int len = -1;
				byte[] data = new byte[1024*8];
				while((len = fis.read(data)) != -1) {
					out.write(data,0,len);
				}
			}
			
			System.out.println("ClientHandler:处理请求完毕！\n");
			/* 
			 * 3.响应客户端
			 */
			
			System.out.println("ClientHandler:处理交互完毕！");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
