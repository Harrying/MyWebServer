package com.zb.util;

import java.io.IOException;
import java.net.Socket;

public class MyRunnable implements Runnable {
	private Socket socket;
	
	public MyRunnable(Socket socket) {
		this.socket = socket;
	}
	
	//线程处理函数
	@Override
	public void run() {
		try {
			//实例化一个响应对象
			RequestImpl req = new RequestImpl(socket);
			
			//实例化一个请求对象
			String resName = req.receiveRequestMsg();
			if(resName == null || "/favicon.ico".equals(resName))
				return;
			
//			System.out.println(req.getMethod());
			System.out.println("resName: " + req.getResName());
//			System.out.println(req.getHttpVer());
//			System.out.println(req.getHeadMap());
//			System.out.println(req.getBodyMap());
			System.out.println("*************************");
			
			//实例化响应对象
			ResponceImpl res = new ResponceImpl(socket, req);
			//做出响应
			res.sendResource(resName);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(socket != null)
					socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
	}
}
