package com.webserver.http;

import java.util.HashMap;
import java.util.Map;

/**
 * 该类保存所有有关HTTP协议规定的内容
 */
public class HttpContext {
	// 此集合存储常见的响应头信息
	private static Map<String,String> mimeMapping = new HashMap<>();
	
	static {
		// 初始化所有静态资源
		initMimeMapping();
	}
	
	/**
	 * 存储响应头信息
	 */
	private static void initMimeMapping() {
		mimeMapping.put("html", "text/html");
		mimeMapping.put("css", "text/css");
		mimeMapping.put("js", "application/javascript");
		mimeMapping.put("png", "image/png");
		mimeMapping.put("gif", "image/gif");
		mimeMapping.put("jpg", "image/jpeg");
	}
	
	/**
	 * 根据资源后缀名获取对应的Content-Type的值
	 * @param ext 文件后缀名
	 * @return 对应的响应头值
	 */
	public static String getMimeType(String ext) {
		return mimeMapping.get(ext);
	}
}
