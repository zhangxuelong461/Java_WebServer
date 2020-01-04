package com.webserver.servlet;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.Arrays;

import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;

/**
 * 当前类用于处理注册业务
 */
public class RegServlet extends HttpServlet{
	public void service(HttpRequest request,HttpResponse response) {
		System.out.println("RegServlet：开始处理注册业务...");
		// 1.获取用户在注册页面上输入的信息(表单提交上来的数据)
		String username = request.getParameters("username");
		String password = request.getParameters("password");
		String nick = request.getParameters("nick");
		int age = Integer.parseInt(request.getParameters("age"));
		System.out.println(username+","+password+","+nick+","+age);
		// 2.将注册信息写入文件user.dat中(每条记录占100字节,32+32+32+4)
		boolean isexist = false;
		try (
			RandomAccessFile raf = new RandomAccessFile("user.dat", "rw")
		){
			/*
			 * 先读取文件现有数据，查看该用户名是否存在，如果存在则响应提示页面:have_user.html
			 */
			// 获取文件的大小
			long size = raf.length();
			for(int i=0;i<size;i+=100) {
				raf.seek(i);
				byte[] bytes = new byte[32];
				// 读取用户名(批量读取32位)
				raf.read(bytes);
				String name = new String(bytes,"UTF-8").trim(); // 去除后面的空白
				if(name.equals(username)) {
					System.out.println("名字重复:"+name);
					isexist = true;
					break;
				}
			}
			
			if(!isexist) {
				raf.seek(raf.length());
				// 写入用户名
				byte[] bytes = username.getBytes("UTF-8");
				bytes = Arrays.copyOf(bytes, 32);
				raf.write(bytes); // 文件指针移动到32
				// 写入密码
				bytes = password.getBytes("UTF-8");
				bytes = Arrays.copyOf(bytes, 32);
				raf.write(bytes);
				// 写入昵称
				bytes = nick.getBytes("UTF-8");
				bytes = Arrays.copyOf(bytes, 32);
				raf.write(bytes);
				// 写入年龄
				raf.writeInt(age); // 写4个字节一个int
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// 3.响应注册结果页面给客户端，注册成功或失败
		forward("/myweb/login.html", request, response);
		if(isexist) {
			forward("/myweb/have_user.html", request, response);
		}
		System.out.println("RegServlet：处理注册业务完毕!");
	}
}
