package com.briup.test.day2;

import java.net.ServerSocket;
import java.net.Socket;

public class ServerTest {

	public static void main(String[] args) throws Exception {
		//1.�TCP������
		//�������ļ���ȡ�˿���Ϣ
		int  prop = Integer.parseInt(PropFinder.getProp("server_port"));
		System.out.println(prop);
		ServerSocket server = new ServerSocket(prop);
		System.out.println("�������Ѿ�����...");
                                                          
		while(true) {
			//2.���տͻ��˵����ӡ�ѭ�����ա� http://127.0.0.1:9999
			Socket socket = server.accept();
			if(socket != null)
				System.out.println("���ӳɹ�,socket: "+socket);
			
			//�������̣߳�ר��Ϊ�ÿͻ����ṩ����
			Thread th = new Thread(new MyRunnable(socket));
			th.start();	
		}
		

	}
}
