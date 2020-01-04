package com.webserver.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * 请求对象
 * 该类的每一个实例表示客户端发送过来的一个Http请求内容
 * 每个请求由三部分构成：请求行，消息头，消息正文
 */
public class HttpRequest {
	/* 请求行相关信息 */
	private String method; // 请求方式
	private String uri; // 抽象路径
	private String protocol; // 协议版本
	/* 消息头相关信息 */
	
	/* 消息正文相关信息 */
	
	/* 和连接相关的属性(socket,in输入流) */
	private Socket socket;
	private InputStream in;
	
	/** 构造方法，实例化的过程就是解析请求的过程 */
	public HttpRequest(Socket socket) {
		this.socket = socket;
		try {
			this.in = socket.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// 1.解析请求行
		parseRequestLine();
		// 2.解析消息头
		parseHeaders();
		// 3.解析消息正文
		parseContent();
	}
	
	/** 解析请求行 */
	private void parseRequestLine() {
		System.out.println("HttpRequest：开始解析请求行...");
		try {
			// 1.通过输入流读取第一行字符串以CRLF结尾的(CR->13,LF->10)
			int c1 = -1;
			int c2 = -1;
			StringBuilder builder = new StringBuilder();
			while((c2 = in.read()) != -1) {
				if(c1 == 13 && c2 == 10) {
					break;
				}
				builder.append((char)c2);
				c1 = c2;
			}
			String line = builder.toString().trim();
			System.out.println(line);
			// 2.将字符串按照空格拆分为三部分
			String[] data = line.split(" "); //split(\\s)也可以\s表示空字符
			// 分别将三部分赋值给method,uri,protocal
			method = data[0];
			// 后期循环接收请求后可能出现下标越界，这是空请求导致的问题，会解决的
			uri = data[1];
			protocol = data[2];
			
			System.out.println("method:"+method);
			System.out.println("uri:"+uri);
			System.out.println("protocol:"+protocol);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("HttpRequest：解析请求行完毕！\n");
	}
	
	/** 解析消息头 */
	private void parseHeaders() {
		System.out.println("HttpRequest：开始解析消息头...");
		System.out.println("HttpRequest：解析消息头完毕！\n");
	}
	
	/** 解析消息正文 */
	private void parseContent() {
		System.out.println("HttpRequest：开始解析消息正文...");
		System.out.println("HttpRequest：解析消息头完毕！");
	}
}
