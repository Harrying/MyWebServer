package com.briup.test.day2;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;


public class MyRunnable implements Runnable {
	
	//��ӿͻ��� �׽��� ��Ա
	private Socket socket;
	
	//�������ļ���ȡ��Դ·��
	private String path = PropFinder.getProp("path");
	private String errorFile = PropFinder.getProp("errorFile");
	private String welFile= PropFinder.getProp("welFile");
	
	//��д���췽�� ��ʼ���ͻ����׽���
	public  MyRunnable(Socket socket) {
		this.socket = socket;
	}
	
	
	@Override
	public void run() {
		//��ȡ���������������
		String url;
		try {
			//1.�����ӿͻ��� ���յ��� ����,������Դ��
			//����getUrl������ȡurl
			url = getUrl();
			System.out.println("URL��" + url);
			//2.����Ӧ��Ϣ(��Դ�ļ� ��װ��HTTPЭ��)
			//���͸� �ͻ���
			sendResponse(url);
			
			socket.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private String getUrl() throws Exception {
		//3.����io��  ���տͻ��˷��͵�����
		InputStream is = socket.getInputStream();
		//ת��Ϊ�����ַ����������ȡ
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		
		String line;
		//3.1.��¼��һ������ ������
		String str = br.readLine();
		System.out.println("��һ������Ϊ��" + str);
		if(str == null) {
			return null;
		}
		//3.2.���������
		String[] array = str.split(" ");
		if(array.length != 3) {
			System.out.println("�����и�ʽ����");
			return null;
		}
		return array[1];
	}
	
	private void sendResponse(String url) throws Exception {
		File file = new File(path,url);
		//�ж�Ŀ���ļ��Ƿ����
		boolean flag = file.exists();
		System.out.println("file:" + file + "  " + "flag:" + flag);
		
		//׼����(������ �������ݸ� �����)
		OutputStream os = socket.getOutputStream();
		PrintStream ps = new PrintStream(os);
		
		//׼����Ӧ��
		String responseLine = null;
		
		//����ļ��Ƿ���� ,׼����Ӧ��
		//���127.0.0.1:9999  flag==true  file:welcome.html
		if(flag) { 
			responseLine = "HTTP/1.1 200 OK";
			//�ж��Ƿ��� �հ���Դ  /
			if("/".equals(url)) {
				file = new File(path,welFile);
			}
		} else {
			responseLine = "HTTP/1.1 404 NotFound";
			//�ļ������ڣ���д��ֵerror.html
			file = new File(path,errorFile);
		}
		
		//����httpЭ�鷢��
		//a. ������Ӧ��
		ps.println(responseLine);
		//b. ��Ϣͷ(����Ϊ��  ��д)
		//c. ����
		ps.println();
		
		//d. ��Ϣ��(�ļ���������)
		//����׼���ļ�������ȡfile�е�����
		BufferedInputStream bis = 
				new BufferedInputStream(new FileInputStream(file));
		byte[] arr = new byte[1024];
		int size = 0;
		
		while((size = bis.read(arr)) != -1) {
			//���ļ��е����� ���͸������
			ps.write(arr,0,size);
		}
		
	}
	

}
