package com.zb.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.net.Socket;

/*
 * ��Ӧ��
 */
public class ResponceImpl {
	private Socket socket;
	private PrintStream ps; 
	private RequestImpl req;
	
	private String httpVer;
	
	public ResponceImpl(Socket socket, RequestImpl req) throws IOException {
		this.socket = socket;
		this.req = req;
		
		ps = new PrintStream(socket.getOutputStream());
		httpVer = req.getHttpVer();
	}
	
	public void sendResource(String resName) throws Exception {
		//������Դ���ж��䵽���Ƕ�̬���Ǿ�̬��Դ
		String className = isDynamicResource(resName);
		
		//����Ƕ�̬��Դ�����⴦��
		if(className != null) {
			
			/* ��ͨ��ʽ����
			LoginServlet ls = new LoginServlet();
			ls.service(req, this);
			*/
			
			//����ʵ��
			Class<?> clazz = Class.forName(className);
			LoginServlet ls = (LoginServlet) clazz.newInstance();
			Method method = clazz.getMethod("service", RequestImpl.class, ResponceImpl.class);
			method.invoke(ls, req, this);
			
			return;
		}
		
		//����Ǿ�̬��Դ�����洦��
		//1.׼����Ӧ�У�����
		String resLine;
		String path = PropFinder.getProp("path");
		File file = new File(path,resName);
		if(file.exists()) {
			if("/".equals(resName)) {
				String welFile = PropFinder.getProp("welFile");
				System.out.println("welFile: " + welFile);
				file = new File(path,welFile);
			}
			resLine = httpVer + " 200 OK";
		}else {
			String errorFile = PropFinder.getProp("errorFile");
			file = new File(path,errorFile); 
			resLine = httpVer + " 404 NotFound";
		}
		
		ps.println(resLine);
		System.out.println("������Ӧ�гɹ�");
		
		//2.������Ӧͷ���հס� 
		//���� ����
		ps.println();
		
		//3.������Ӧ�塾�ļ����ݡ�
		InputStream is = new FileInputStream(file);
		byte[] buff = new byte[1024];
		int len;
		while((len = is.read(buff)) != -1) {
			ps.write(buff, 0, len);
			ps.flush();
		}
		
		//�ر��ļ���
		is.close();
	}

	//�жϸ����Ƿ�Ϊ��̬��Դ
	private String isDynamicResource(String resName) {
		//resName: /login
		String className = PropFinder.getProp(resName);
		System.out.println("className: " + className);
		
		return className;
	}
	
}
