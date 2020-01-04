package com.webserver.servlet;

import java.io.File;
import java.io.RandomAccessFile;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;

/**
 * 当前类用来处理登录业务
 */
public class LoginServlet extends HttpServlet{
	public void service(HttpRequest request,HttpResponse response) {
		System.out.println("LoginServlet：开始处理登录业务...");
		// 1.获取用户在登录页面上输入的信息(表单提交上来的数据)
		String username = request.getParameters("username");
		String password = request.getParameters("password");
		// 2.从user.dat文件中读取用户名跟密码是否与用户输入的一致(用flag进行标记)
		boolean flag = false;
		try(
			RandomAccessFile raf = new RandomAccessFile("user.dat", "r")
		){
			for(int i=0;i<raf.length();i+=100) {
				raf.seek(i);
				byte[] bytes = new byte[32];
				// 读取用户名(批量读取32位)
				raf.read(bytes);
				String name = new String(bytes,"UTF-8").trim(); // 去除后面的空白
				// 读取密码(批量读取32位)
				raf.read(bytes);
				String pwd = new String(bytes, "UTF-8").trim();
				if(name.equals(username) && pwd.equals(password)) {
					flag = true;
					break;
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		// 3.响应登录结果给客户端，登录成功或失败
		forward("/myweb/login_fail.html", request, response);
		if(flag) {
			forward("/myweb/login_success.html", request, response);
		}
		System.out.println("LoginServlet：处理登录业务完毕!");
	}
}
