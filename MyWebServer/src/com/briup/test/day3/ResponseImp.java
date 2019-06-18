package com.briup.test.day3;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.net.Socket;

public class ResponseImp {
	private Socket socket;
	// 服务器资源目录 路径
	private String path = PropFinder.getProp("path");
	private String errorFile = PropFinder.getProp("errorFile");;
	private String welFile = PropFinder.getProp("welFile");;
	private RequestImp r;

	public ResponseImp(Socket socket, RequestImp r) {
		this.socket = socket;
		this.r = r;
		// this.res = res;
		// TODO Auto-generated constructor stub
	}

	public void sendResponse(String url) throws Exception {
		String url1 = url.substring(1);
		String str = PropFinder.getProp(url1);
		if (str != null) {
			System.out.println("该资源是动态资源，对请求体值进行验证...");
			Class<?> clazz1 = Class.forName(str);
			ServletLogin ser = (ServletLogin) clazz1.newInstance();
			// RequestImp req = new RequestImp(socket);
			// ResponseImp res = new ResponseImp(socket);
			Method method = clazz1.getMethod("service", RequestImp.class, ResponseImp.class);
			method.invoke(ser, r, this);
		} else {
			System.out.println("该资源是普通资源，直接访问...");
			File file = new File(path, url);
			boolean flag = file.exists();
			System.out.println("file: " + file + " flag: " + flag);

			// 准备流(服务器 发送数据给 浏览器)
			OutputStream os = socket.getOutputStream();
			PrintStream ps = new PrintStream(os);

			// 准备响应行
			String responseLine = null;

			// 针对文件是否存在 ,准备响应行
			// 如果127.0.0.1:9999 flag==true file:welcome.html
			if (flag) {
				responseLine = "HTTP/1.1 200 OK";
				// 判断是否是 空白资源 /
				if ("/".equals(url)) {
					file = new File(path, welFile);
				}
			} else {
				responseLine = "HTTP/1.1 404 NotFound";
				// 文件不存在，重写赋值error.html
				file = new File(path, errorFile);
			}

			// 按照http协议发送
			// a. 发送响应行
			ps.println(responseLine);
			// b. 消息头(可以为空 不写)
			// c. 空行
			ps.println();

			// d. 消息体(文件具体内容)
			// 额外准备文件流，提取file中的数据
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			byte[] arr = new byte[1024];
			int size = 0;

			while ((size = bis.read(arr)) != -1) {
				// 将文件中的数据 发送给浏览器
				ps.write(arr, 0, size);
			}
		}
	}
}