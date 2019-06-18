package com.briup.test.day2;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;


public class MyRunnable2 implements Runnable {
	
	//��ӿͻ��� �׽��� ��Ա
	private Socket socket;
	
	//��д���췽�� ��ʼ���ͻ����׽���
	public  MyRunnable2(Socket socket) {
		this.socket = socket;
	}
	
	@Override
	public void run() {
		//��ȡ���������������
		String url;
		try {
			//1.�����ӿͻ��� ���յ��� ����,������Դ��
			RequestHttp rh = new RequestHttp(socket);
			rh.splReques();
			//�������ʽ
			System.out.println("����ʽ��"+ rh.getMethod());
			//�õ���Դ��
			url = rh.getResName();
			System.out.println("��Դ����"+ url);
			//���http�汾
			System.out.println("http�汾��"+ rh.getHttpVer());
			//�������ͷ
			HashMap<String, String> headMap = rh.getHeadMap();
			Set<Entry<String, String>> entry = headMap.entrySet();
			System.out.println("����ͷΪ��");
			for (Entry<String, String> entry2 : entry) {
				System.out.println(entry2.getKey()+ " " + entry2.getValue());
			}
			//���������
			HashMap<String, String> bodyMap = rh.getBodyMap();
			if(bodyMap == null) {
				System.out.println("������Ϊ�գ�");
			}else {
				Set<Entry<String, String>> entry1 = bodyMap.entrySet();
				System.out.println("������Ϊ��");
				for (Entry<String, String> entry2 : entry1) {
					System.out.println(entry2.getKey()+ " " + entry2.getValue());
				}
			}
			
			//2.����Ӧ��Ϣ���͸� �ͻ���
			ResponseHttp rph = new ResponseHttp(url, socket);
			rph.sendRespon();
			socket.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
