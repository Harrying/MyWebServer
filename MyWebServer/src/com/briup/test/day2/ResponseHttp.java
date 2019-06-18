package com.briup.test.day2;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;

public class ResponseHttp {
	private String url;
	private Socket socket;
	private String path = PropFinder.getProp("path");
	private String errorFile = PropFinder.getProp("errorFile");
	private String welFile= PropFinder.getProp("welFile");
	
	public ResponseHttp(String url,Socket socket) {
		this.url = url;
		this.socket = socket;
	}
	

	public void sendRespon() throws Exception {
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
