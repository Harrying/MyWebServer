package com.briup.test.day3;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.net.Socket;

public class ResponseImp {
	private Socket socket;
	// ��������ԴĿ¼ ·��
	private String path = PropFinder.getProp("path");
	private String errorFile = PropFinder.getProp("errorFile");;
	private String welFile = PropFinder.getProp("welFile");;
	private RequestImp r;

	public ResponseImp(Socket socket, RequestImp r) {
		this.socket = socket;
		this.r = r;
		// this.res = res;
		// TODO Auto-generated constructor stub
	}

	public void sendResponse(String url) throws Exception {
		String url1 = url.substring(1);
		String str = PropFinder.getProp(url1);
		if (str != null) {
			System.out.println("����Դ�Ƕ�̬��Դ����������ֵ������֤...");
			Class<?> clazz1 = Class.forName(str);
			ServletLogin ser = (ServletLogin) clazz1.newInstance();
			// RequestImp req = new RequestImp(socket);
			// ResponseImp res = new ResponseImp(socket);
			Method method = clazz1.getMethod("service", RequestImp.class, ResponseImp.class);
			method.invoke(ser, r, this);
		} else {
			System.out.println("����Դ����ͨ��Դ��ֱ�ӷ���...");
			File file = new File(path, url);
			boolean flag = file.exists();
			System.out.println("file: " + file + " flag: " + flag);

			// ׼����(������ �������ݸ� �����)
			OutputStream os = socket.getOutputStream();
			PrintStream ps = new PrintStream(os);

			// ׼����Ӧ��
			String responseLine = null;

			// ����ļ��Ƿ���� ,׼����Ӧ��
			// ���127.0.0.1:9999 flag==true file:welcome.html
			if (flag) {
				responseLine = "HTTP/1.1 200 OK";
				// �ж��Ƿ��� �հ���Դ /
				if ("/".equals(url)) {
					file = new File(path, welFile);
				}
			} else {
				responseLine = "HTTP/1.1 404 NotFound";
				// �ļ������ڣ���д��ֵerror.html
				file = new File(path, errorFile);
			}

			// ����httpЭ�鷢��
			// a. ������Ӧ��
			ps.println(responseLine);
			// b. ��Ϣͷ(����Ϊ�� ��д)
			// c. ����
			ps.println();

			// d. ��Ϣ��(�ļ���������)
			// ����׼���ļ�������ȡfile�е�����
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			byte[] arr = new byte[1024];
			int size = 0;

			while ((size = bis.read(arr)) != -1) {
				// ���ļ��е����� ���͸������
				ps.write(arr, 0, size);
			}
		}
	}
}