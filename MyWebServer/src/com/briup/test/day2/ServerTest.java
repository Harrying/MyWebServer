package com.briup.test.day2;

import java.net.ServerSocket;
import java.net.Socket;

public class ServerTest {

	public static void main(String[] args) throws Exception {
		//1.搭建TCP服务器
		//从配置文件获取端口信息
		int  prop = Integer.parseInt(PropFinder.getProp("server_port"));
		System.out.println(prop);
		ServerSocket server = new ServerSocket(prop);
		System.out.println("服务器已经启动...");
                                                          
		while(true) {
			//2.接收客户端的连接【循环接收】 http://127.0.0.1:9999
			Socket socket = server.accept();
			if(socket != null)
				System.out.println("连接成功,socket: "+socket);
			
			//分离子线程，专门为该客户端提供服务
			Thread th = new Thread(new MyRunnable(socket));
			th.start();	
		}
		

	}
}
