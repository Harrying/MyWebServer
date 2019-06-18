package com.briup.test.day3;

import java.net.ServerSocket;
import java.net.Socket;

public class ThreadServer {
	public static void main(String[] args) throws Exception {
		String prot0 = PropFinder.getProp("server_port");
		int prot =Integer.parseInt(prot0);
		ServerSocket server = new ServerSocket(prot);
		System.out.println("服务器已经启动！");
		while (true) {
			Socket socket = server.accept();
			Runnable r = new MyRunnable(socket);
			if (socket != null)
				System.out.println("连接成功，socket:" + socket);
			// 分离子线程 ，专门为该客户端提供服务
			Thread th = new Thread(r);
			
			th.start();
		}
	}
}
