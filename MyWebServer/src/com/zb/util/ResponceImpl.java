package com.zb.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.net.Socket;

/*
 * 响应类
 */
public class ResponceImpl {
	private Socket socket;
	private PrintStream ps; 
	private RequestImpl req;
	
	private String httpVer;
	
	public ResponceImpl(Socket socket, RequestImpl req) throws IOException {
		this.socket = socket;
		this.req = req;
		
		ps = new PrintStream(socket.getOutputStream());
		httpVer = req.getHttpVer();
	}
	
	public void sendResource(String resName) throws Exception {
		//根据资源名判断其到底是动态还是静态资源
		String className = isDynamicResource(resName);
		
		//如果是动态资源，特殊处理
		if(className != null) {
			
			/* 普通方式调用
			LoginServlet ls = new LoginServlet();
			ls.service(req, this);
			*/
			
			//反射实现
			Class<?> clazz = Class.forName(className);
			LoginServlet ls = (LoginServlet) clazz.newInstance();
			Method method = clazz.getMethod("service", RequestImpl.class, ResponceImpl.class);
			method.invoke(ls, req, this);
			
			return;
		}
		
		//如果是静态资源，常规处理
		//1.准备响应行，发送
		String resLine;
		String path = PropFinder.getProp("path");
		File file = new File(path,resName);
		if(file.exists()) {
			if("/".equals(resName)) {
				String welFile = PropFinder.getProp("welFile");
				System.out.println("welFile: " + welFile);
				file = new File(path,welFile);
			}
			resLine = httpVer + " 200 OK";
		}else {
			String errorFile = PropFinder.getProp("errorFile");
			file = new File(path,errorFile); 
			resLine = httpVer + " 404 NotFound";
		}
		
		ps.println(resLine);
		System.out.println("发送响应行成功");
		
		//2.发送响应头【空白】 
		//发送 空行
		ps.println();
		
		//3.发送响应体【文件内容】
		InputStream is = new FileInputStream(file);
		byte[] buff = new byte[1024];
		int len;
		while((len = is.read(buff)) != -1) {
			ps.write(buff, 0, len);
			ps.flush();
		}
		
		//关闭文件流
		is.close();
	}

	//判断该类是否为动态资源
	private String isDynamicResource(String resName) {
		//resName: /login
		String className = PropFinder.getProp(resName);
		System.out.println("className: " + className);
		
		return className;
	}
	
}
