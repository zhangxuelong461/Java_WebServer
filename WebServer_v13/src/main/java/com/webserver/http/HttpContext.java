package com.webserver.http;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

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
//		mimeMapping.put("html", "text/html");
//		mimeMapping.put("css", "text/css");
//		mimeMapping.put("js", "application/javascript");
//		mimeMapping.put("png", "image/png");
//		mimeMapping.put("gif", "image/gif");
//		mimeMapping.put("jpg", "image/jpeg");
		/*
		 * 1.使用dom4j解析conf/web.xml文档
		 * 2.将根标签下所有名为mime-mapping的子标签获取出来
		 * 3.遍历这些mime-mapping标签，并将其子标签：
		 * 		<extension>中间的文本作为key,<mime-type>中间的文本作为value put到mimeMapping这个Map中
		 * 初始化后mimeMapping这个Map应当有1000多个元素，这样一来我们就支持了所有的类型。
		 */
		try {
			// 1.创建SAXReader
			SAXReader reader = new SAXReader();
			// 2.使用SAXReader读取要解析的XML文档,以一个Document对象形式返回
			Document doc = reader.read(new File("./conf/web.xml"));
			// 3.通过Document获取根元素
			Element root = doc.getRootElement();
			// 4.获取根元素下所有名为mime-mapping的子标签
			List<Element> list = root.elements("mime-mapping");
			for(Element ele : list) {
				String key = ele.elementText("extension");
				String value = ele.elementText("mime-type");
				mimeMapping.put(key, value);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(mimeMapping.size());
	}
	
	/**
	 * 根据资源后缀名获取对应的Content-Type的值
	 * @param ext 文件后缀名
	 * @return 对应的响应头值
	 */
	public static String getMimeType(String ext) {
		return mimeMapping.get(ext);
	}
	
	public static void main(String[] args) {
		String type = getMimeType("js");
		System.out.println(type);
	}
}
