package com.webserver.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 响应对象，该对象的每个实例表示服务端发送给客户端的一个HTTP响应
 * 一个响应由三部分构成：
 * 状态行，响应头，响应正文
 */
public class HttpResponse {
	// 状态行相关信息(状态码默认为:200,状态描述默认为:OK)
	private int statusCode = 200;
	private String statusReason = "OK";
	// 响应头相关信息
	private Map<String, String> headers = new HashMap<>();
	// 响应正文相关信息
	private File entity;
	// 跟连接相关的内容
	private Socket socket;
	private OutputStream out;
	
	/** 
	 * 构造方法(初始化)
	 * 实例化HttpResponse同时将Socket传入响应对象通过它将响应内容发送给客户端
	 * @param socket
	 */
	public HttpResponse(Socket socket) {
		try {
			this.socket = socket;
			this.out = socket.getOutputStream();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 将当前响应对象的内容以一个标准的HTTP响应格式发送给客户端
	 */
	public void flush() {
		try {
			// 发送状态行
			sendStatusLine();
			// 发送响应头
			sendHeaders();
			// 发送响应正文
			sendContent();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 发送状态行
	 */
	private void sendStatusLine() {
		try {
			// 1.发送状态行
			String line = "HTTP/1.1"+" "+statusCode+" "+statusReason;
			out.write(line.getBytes("ISO8859-1"));
			out.write(13); // written CR
			out.write(10); // written LF
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 发送响应头
	 */
	private void sendHeaders() {
		try {
			// 2.发送响应头
			Set<Entry<String,String>> entrySet = headers.entrySet();
			for(Entry<String,String> header:entrySet) {
				String line = header.getKey()+": "+header.getValue();
				System.out.println("响应头:"+line);
				out.write(line.getBytes("ISO8859-1"));
				out.write(13);
				out.write(10);
			}
			// 响应头结束标记，单独发送CRLF
			out.write(13);
			out.write(10);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 发送响应正文
	 */
	private void sendContent() {
		try (
			// 3.发送响应正文
			FileInputStream fis = new FileInputStream(entity);
		){
			int len = -1;
			byte[] data = new byte[1024*8];
			while((len = fis.read(data)) != -1) {
				out.write(data,0,len);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取响应正文文件
	 * @return entity 文件
	 */
	public File getEntity() {
		return entity;
	}

	/**
	 * 设置响应正文文件(给私有属性entity赋值)
	 * @param entity 要响应的文件
	 */
	public void setEntity(File entity) {
		this.entity = entity;
	}

	/**
	 * 获取状态码
	 * @return
	 */
	public int getStatusCode() {
		return statusCode;
	}

	/**
	 * 设置状态码
	 * @param statusCode
	 */
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	/**
	 * 获取状态描述
	 * @return
	 */
	public String getStatusReason() {
		return statusReason;
	}

	/**
	 * 设置状态描述
	 * @param statusReason
	 */
	public void setStatusReason(String statusReason) {
		this.statusReason = statusReason;
	}
	
	/**
	 * 添加一个响应头
	 * @param name 响应头的名字
	 * @param value 对应的值
	 */
	public void putHeader(String name,String value) {
		this.headers.put(name, value);
	}
	
}























