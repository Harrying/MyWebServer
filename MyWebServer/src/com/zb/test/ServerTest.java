package com.zb.test;

import java.net.ServerSocket;
import java.net.Socket;

import com.zb.util.MyRunnable;
import com.zb.util.PropFinder;

/**
 * web服务器测试类，包含程序入口
 * @author 	briup
 * @date 	2019.6.5
 */
public class ServerTest {
	public static void main(String[] args) throws Exception {
		//获取服务器端口
		String propStr = PropFinder.getProp("server_port");
		int port = Integer.parseInt(propStr);
		
		//启动服务器
		ServerSocket server = new ServerSocket(port);
		System.out.println("服务器已经启动，端口为 " + port + " ...");
		
		while(true) {
			//接收客户端的连接
			Socket socket = server.accept();
			System.out.println("客户端连接成功,socket: " + socket);
			
			Thread th = new Thread(new MyRunnable(socket));
			th.start();
		}
	}
}
