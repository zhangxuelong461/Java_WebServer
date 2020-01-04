package com.webserver.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

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
	
	private String requestURI; // uri中的请求部分(？左侧内容)
	private String queryString; // uri中的参数部分(？右侧内容)
	private Map<String,String> parameters = new HashMap<>(); // 每一个参数
	/* 消息头相关信息 */
	private Map<String, String> headers = new HashMap<>();
	/* 消息正文相关信息 */
	
	/* 和连接相关的属性(socket,in输入流) */
	private Socket socket;
	private InputStream in;
	
	/** 构造方法，实例化的过程就是解析请求的过程
	 * @param socket 客户端socket实例对象
	 * @throws EmptyRequestException 
	 */
	public HttpRequest(Socket socket) throws EmptyRequestException {
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
	
	/** 解析请求行 
	 * @throws EmptyRequestException */
	private void parseRequestLine() throws EmptyRequestException {
		System.out.println("HttpRequest：开始解析请求行...");
		try {
			// 1.通过输入流读取第一行字符串以CRLF结尾的(CR->13,LF->10)
			String line = readLine();
			System.out.println(line);
			// 如果读取到的字符串是空字符串，说明是个空请求,抛出异常
			if("".equals(line)) {
				throw new EmptyRequestException();
			}
			// 2.将字符串按照空格拆分为三部分
			String[] data = line.split(" "); //split(\\s)也可以\s表示空字符
			// 分别将三部分赋值给method,uri,protocal
			method = data[0];
			uri = data[1];
			protocol = data[2];
			
			// 进一步解析uri
			parseURI();
			
			System.out.println("method:"+method);
			System.out.println("uri:"+uri);
			System.out.println("protocol:"+protocol);
		} catch (EmptyRequestException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("HttpRequest：解析请求行完毕！\n");
	}
	
	/**
	 * 进一步解析uri
	 * 因为uri可能存在两种情况:
	 * 1.含有参数的:myweb/reg?username=zhangsan&password=123456
	 * 2.不含有参数的：myweb/index.html
	 * 因此我们要对uri解析，将参数部分获取
	 */
	private void parseURI() {
		/*
		 * 由于uri有两种情况，因此处理区分下不含有参数时：直接将uri的值赋给requestURI即可
		 * 含有参数时：
		 * 	1.按照?拆分uri,将？左侧内容赋值给requestURI,将？右侧内容赋值给queryString
		 * 	2.在将queryString按照&拆分出每一个参数，并且每个参数再按照=拆分参数名与参数值，
		 * 		保存到parameters这个Map属性上即可.
		 */
		System.out.println("进一步解析uri");
		String[] para = uri.split("\\?");
		if(para.length>1) {
			requestURI = para[0];
			queryString = para[1];
			String[] param = queryString.split("&");
			for(String str : param) {
				String[] parameter = str.split("=");
				String key = parameter[0];
				String value;
				if(parameter.length==1) {
					value=null;
				}else {
					value = parameter[1];
				}
				parameters.put(key, value);
			}
		}else {
			requestURI = uri;
		}
		
		
		System.out.println("requestURI:"+requestURI);
		System.out.println("queryString:"+queryString);
		System.out.println("paramters::"+parameters);
		System.out.println("进一步解析uri完毕!");
		
	}
	
	/** 解析消息头 */
	private void parseHeaders() {
		System.out.println("HttpRequest：开始解析消息头...");
		try {
			while(true) {
				String line = readLine();
				if("".equals(line)) {
					break;
				}
				System.out.println("消息头："+line);
				// 将消息头按照“：”(冒号空格)进行拆分存入Map中。
				// 注意：Host: localhost:8088,如果只按照冒号进行拆分会有三项
				String[] data = line.split(": ");
				headers.put(data[0], data[1]);
			}
			System.out.println(headers);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("HttpRequest：解析消息头完毕！\n");
	}
	
	/** 解析消息正文 */
	private void parseContent() {
		System.out.println("HttpRequest：开始解析消息正文...");
		System.out.println("HttpRequest：解析消息正文完毕！\n");
	}
	
	/**
	 * 读取一行字符串(以CR，LF结尾的一行的字符串,CR->13,LF->10)
	 * @return String 读取到的该行的字符串(不含有最后的回车与换行符)
	 * @throws IOException 
	 */
	private String readLine() throws IOException {
		int c1 = -1; // 上次读取到的字节
		int c2 = -1; // 本次读取到的字节
		StringBuilder builder = new StringBuilder();
		while((c2 = in.read()) != -1) {
			if(c1 == 13 && c2 == 10) {
				break;
			}
			builder.append((char)c2);
			c1 = c2;
		}
		return builder.toString().trim();
	}
	
	/** 获取method值 */
	public String getMethod() {
		return method;
	}
	
	/** 获取uri值 */
	public String getUri() {
		return uri;
	}
	
	/** 获取protocol值 */
	public String getProtocol() {
		return protocol;
	}
	
	/** 根据消息头key值获取对应的value
	 * @param name 消息头属性
	 * @return String 消息头属性对应的值
	 */
	public String getHeader(String name) {
		return headers.get(name);
	}

	public String getRequestURI() {
		return requestURI;
	}

	public String getQueryString() {
		return queryString;
	}

	public String getParameters(String name) {
		return parameters.get(name);
	}
	
	
}



















