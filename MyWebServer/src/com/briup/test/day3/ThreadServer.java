package com.briup.test.day3;

import java.net.ServerSocket;
import java.net.Socket;

public class ThreadServer {
	public static void main(String[] args) throws Exception {
		String prot0 = PropFinder.getProp("server_port");
		int prot =Integer.parseInt(prot0);
		ServerSocket server = new ServerSocket(prot);
		System.out.println("�������Ѿ�������");
		while (true) {
			Socket socket = server.accept();
			Runnable r = new MyRunnable(socket);
			if (socket != null)
				System.out.println("���ӳɹ���socket:" + socket);
			// �������߳� ��ר��Ϊ�ÿͻ����ṩ����
			Thread th = new Thread(r);
			
			th.start();
		}
	}
}
