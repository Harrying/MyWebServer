package com.zb.util;

import java.io.IOException;
import java.net.Socket;

public class MyRunnable implements Runnable {
	private Socket socket;
	
	public MyRunnable(Socket socket) {
		this.socket = socket;
	}
	
	//�̴߳�����
	@Override
	public void run() {
		try {
			//ʵ����һ����Ӧ����
			RequestImpl req = new RequestImpl(socket);
			
			//ʵ����һ���������
			String resName = req.receiveRequestMsg();
			if(resName == null || "/favicon.ico".equals(resName))
				return;
			
//			System.out.println(req.getMethod());
			System.out.println("resName: " + req.getResName());
//			System.out.println(req.getHttpVer());
//			System.out.println(req.getHeadMap());
//			System.out.println(req.getBodyMap());
			System.out.println("*************************");
			
			//ʵ������Ӧ����
			ResponceImpl res = new ResponceImpl(socket, req);
			//������Ӧ
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
