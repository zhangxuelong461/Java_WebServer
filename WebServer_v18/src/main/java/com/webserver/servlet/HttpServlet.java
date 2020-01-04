package com.webserver.servlet;

import java.io.File;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;

/**
 * 所有Servlet的超类
 */
public abstract class HttpServlet {
	public abstract void service(HttpRequest request,HttpResponse response);
	
	/**
	 * 跳转到指定路径
	 * @param path
	 * @param request
	 * @param response
	 */
	public void forward(String path,HttpRequest request,HttpResponse response) {
		File file = new File("./webapps"+path);
		response.setEntity(file);
	}
}
