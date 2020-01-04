package com.webserver.core;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.webserver.servlet.HttpServlet;
import com.webserver.servlet.LoginServlet;
import com.webserver.servlet.RegServlet;

/**
 * 定义服务端相关配置信息
 */
public class ServerContext {
	/*
	 * 存放所有请求与对应业务处理类的Servlet
	 * key：请求路径
	 * value：处理该请求的业务类
	 */
	private static Map<String,HttpServlet> servletMapping = new HashMap<>();
	static {
		// 初始化静态资源
		initServletMapping();
	}
	
	/**
	 * 添加请求及对应业务处理类到servletMapping中
	 */
	private static void initServletMapping() {
		//servletMapping.put("/myweb/reg", new RegServlet());
		//servletMapping.put("/myweb/login", new LoginServlet());
		/*
		 * 通过解析conf/servlets.xml初始化将根标签下所有的<servlet>标签获取到
		 * 然后将属性path的值作为key,将属性classPath的值利用反射加载并实例化，
		 * 然后将实例化后的对应Servlet作为value保存到servletMapping中
		 */
		try {
			// 1.创建SAXReader
			SAXReader reader = new SAXReader();
			// 2.使用SAXReader读取要解析的XML文档,以一个Document对象形式返回
			Document doc = reader.read(new File("./conf/servlets.xml"));
			// 3.通过Document获取根元素
			Element root = doc.getRootElement();
			// 4.获取根元素下所有名为servlet的子标签
			List<Element> list = root.elements("servlet");
			for(Element ele : list) {
				String key = ele.attributeValue("path");
				String classPath = ele.attributeValue("className"); //com.webserver.servlet.RegServlet
				// 将classPath的值利用反射加载并实例化
				Class cls = Class.forName(classPath);
				Object o = cls.newInstance();
				HttpServlet value = (HttpServlet)o;
				servletMapping.put(key, value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(servletMapping.size());
	}
	
	/**
	 * 根据给定的请求路径获取处理该请求的Servlet
	 * @param path
	 * @return 如果给定的请求不是对应业务则返回null
	 */
	public static HttpServlet getServlet(String path) {
		return servletMapping.get(path);
	}
}
