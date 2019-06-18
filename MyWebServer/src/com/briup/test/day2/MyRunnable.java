package com.briup.test.day2;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;


public class MyRunnable implements Runnable {
	
	//添加客户端 套接字 成员
	private Socket socket;
	
	//从配置文件获取资源路径
	private String path = PropFinder.getProp("path");
	private String errorFile = PropFinder.getProp("errorFile");
	private String welFile= PropFinder.getProp("welFile");
	
	//重写构造方法 初始化客户端套接字
	public  MyRunnable(Socket socket) {
		this.socket = socket;
	}
	
	
	@Override
	public void run() {
		//获取浏览器的请求内容
		String url;
		try {
			//1.解析从客户端 接收到的 数据,返回资源名
			//调用getUrl方法获取url
			url = getUrl();
			System.out.println("URL：" + url);
			//2.将响应信息(资源文件 包装到HTTP协议)
			//发送给 客户端
			sendResponse(url);
			
			socket.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private String getUrl() throws Exception {
		//3.创建io流  接收客户端发送的数据
		InputStream is = socket.getInputStream();
		//转化为缓冲字符流，方便读取
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		
		String line;
		//3.1.记录第一行数据 请求行
		String str = br.readLine();
		System.out.println("第一行数据为：" + str);
		if(str == null) {
			return null;
		}
		//3.2.拆分数据项
		String[] array = str.split(" ");
		if(array.length != 3) {
			System.out.println("请求行格式出错！");
			return null;
		}
		return array[1];
	}
	
	private void sendResponse(String url) throws Exception {
		File file = new File(path,url);
		//判断目标文件是否存在
		boolean flag = file.exists();
		System.out.println("file:" + file + "  " + "flag:" + flag);
		
		//准备流(服务器 发送数据给 浏览器)
		OutputStream os = socket.getOutputStream();
		PrintStream ps = new PrintStream(os);
		
		//准备响应行
		String responseLine = null;
		
		//针对文件是否存在 ,准备响应行
		//如果127.0.0.1:9999  flag==true  file:welcome.html
		if(flag) { 
			responseLine = "HTTP/1.1 200 OK";
			//判断是否是 空白资源  /
			if("/".equals(url)) {
				file = new File(path,welFile);
			}
		} else {
			responseLine = "HTTP/1.1 404 NotFound";
			//文件不存在，重写赋值error.html
			file = new File(path,errorFile);
		}
		
		//按照http协议发送
		//a. 发送响应行
		ps.println(responseLine);
		//b. 消息头(可以为空  不写)
		//c. 空行
		ps.println();
		
		//d. 消息体(文件具体内容)
		//额外准备文件流，提取file中的数据
		BufferedInputStream bis = 
				new BufferedInputStream(new FileInputStream(file));
		byte[] arr = new byte[1024];
		int size = 0;
		
		while((size = bis.read(arr)) != -1) {
			//将文件中的数据 发送给浏览器
			ps.write(arr,0,size);
		}
		
	}
	

}
