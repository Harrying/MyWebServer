package com.zb.test;

import java.net.ServerSocket;
import java.net.Socket;

import com.zb.util.MyRunnable;
import com.zb.util.PropFinder;

/**
 * web�����������࣬�����������
 * @author 	briup
 * @date 	2019.6.5
 */
public class ServerTest {
	public static void main(String[] args) throws Exception {
		//��ȡ�������˿�
		String propStr = PropFinder.getProp("server_port");
		int port = Integer.parseInt(propStr);
		
		//����������
		ServerSocket server = new ServerSocket(port);
		System.out.println("�������Ѿ��������˿�Ϊ " + port + " ...");
		
		while(true) {
			//���տͻ��˵�����
			Socket socket = server.accept();
			System.out.println("�ͻ������ӳɹ�,socket: " + socket);
			
			Thread th = new Thread(new MyRunnable(socket));
			th.start();
		}
	}
}
