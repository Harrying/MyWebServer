package com.briup.test.day3;

import java.net.Socket;
import java.util.Map;
import java.util.Map.Entry;

public class MyRunnable implements Runnable {
	//��ӿͻ���  �׽��� ��Ա
	private Socket socket;
    //��д�����������ͻ����׽��� ��ʼ��
	public MyRunnable(Socket socket) {
		this.socket = socket;
	}
		@Override
	public void run() {
		// TODO Auto-generated method stub
		String url;
		try {
			RequestImp r = new RequestImp(socket);
			r.receiveRequestMsg();
			//1.�����ӿͻ��� ���յ��� ����,������Դ��
			url = r.getResName();			
			Map<String, String> headMap =r.getHeadMap();
			Map<String, String> bodyMap = r.getBodyMap();
			//2.�����ȡ����������Ϣ
			System.out.println("��Դ��: "+url);
			System.out.println("����ʽ: "+r.getMethod());
			System.out.println("Э��汾: " +r.getHttpVer());
			//3.�����ȡ������ͷ��Ϣ
			System.out.println("����ͷ���ݣ�");
			for (Entry<String, String> entry : headMap.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				System.out.println(key + ":" + value);
			}
			//4.�����ȡ����������Ϣ
			System.out.println("���������ݣ�");
			for (Entry<String, String> entry : bodyMap.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				System.out.println(key + ":" + value);
			}
	
			//2.����Ӧ��Ϣ(��Դ�ļ� ��װ��HTTPЭ��)
			//���͸� �ͻ���
			ResponseImp res = new ResponseImp(socket,r);
			res.sendResponse(url);
			System.out.println("***********���ʽ����ָ���*************");
			socket.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}

}
