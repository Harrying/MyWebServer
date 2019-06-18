package com.briup.test.day1;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

/*
 * 1.�һ��TCP������
 * 2.���տͻ������ӣ����ӽ����ɹ���
 * 3.���տͻ��˷��͹������������ݣ����������̨
 */
public class ServerMain {
	public static void main(String[] args) {
		
		try {
			//1.�������
			ServerSocket server = new ServerSocket(9999);
			System.out.println("�������Ѿ�����...");
			
			//2.���տͻ�������  http://127.0.0.1:9999
			Socket socket = server.accept();
			if(socket != null)
				System.out.println("���ӳɹ�,socket: "+socket);
		
			//3.���տͻ��˷��͹���������,���
			InputStream in = socket.getInputStream();
			//�ֽ���   �������ֽ�Ϊ��λ���д���  
			InputStreamReader isr = new InputStreamReader(in);
			BufferedReader br = new BufferedReader(isr);
		
			String line = null;
			//3.1.��¼ ��һ������(������)
			String str = br.readLine();
			System.out.println(str);
			
			//3.2.��ȡ ʣ��� ������Ϣ EOF  socket
			//"" == null
			while(!"".equals(line = br.readLine())) {
				System.out.println(line);
			}
			System.out.println("after readLine...");
			
			//��ȡbody�ڲ�����
			char[] barr = new char[1024];
			int bsize = 0;
			if(br.ready()) {
				bsize = br.read(barr);
				String s = new String(barr,0,bsize);
				System.out.println("��ȡ��body: "+s);
			}
			System.out.println("������Ϣȫ����ȡ���!");
			
			//4.���� ������  ��ȡ ������Դ��
			// GET /a.txt HTTP/1.1
			//���� �հ��ַ� ����ַ���
			String[] array = str.split(" ");
			System.out.println(array);
			
			for (int i = 0;i < array.length;i++) {
				System.out.println("array["+i+"]: "+array[i]);
			}
			
			//String name = array[1].substring(1);
			
			// �ж��������Դ�ļ��Ƿ���� E:\\myServer\
			File file = new File("D:\\MyServer",array[1]);
			boolean flag = file.exists();
			System.out.println("file: " + file + "flag: " + flag);
			
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
				if("/".equals(array[1])) {
					file = new File("D://MyServer","welcome.html");
				}
			} else {
				responseLine = "HTTP/1.1 404 NotFound";
				//�ļ������ڣ���д��ֵerror.html
				file = new File("D://MyServer","error.html");
			}
			
			//����httpЭ�鷢��
			//a. ������Ӧ��
			ps.println(responseLine);
			//b. ��Ϣͷ(����Ϊ��  ��д)
			//c. ����
			ps.println();
			
			//d. ��Ϣ��(�ļ���������)
			//����׼���ļ�������ȡfile�е�����
			FileInputStream fis = new FileInputStream(file);
			BufferedInputStream bis = 
					new BufferedInputStream(fis);
			byte[] arr = new byte[1024];
			int size = 0;
			
			while((size = bis.read(arr)) != -1) {
				//���ļ��е����� ���͸������
				
				ps.write(arr,0,size);
				
				/*String s = new String(arr,0,size);
				//1111 1111 1111 1111
				// ?
				//�����: ? ==>  0111 1111 0000 0000
				ps.print(s);*/
			}
			
			//�ر������Դ
			bis.close();
			ps.close();
			br.close();
			socket.close();
//			server.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
