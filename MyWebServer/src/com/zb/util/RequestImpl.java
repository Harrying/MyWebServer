package com.zb.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/*
 * ������
 */
public class RequestImpl {
	private String method;
	private String resName;
	private String httpVer;
	private Map<String, String> headMap;
	private Map<String, String> bodyMap;
	
	private Socket socket;
	private BufferedReader br; 
	
	public RequestImpl(Socket socket) throws IOException {
		this.socket = socket;
		
		InputStreamReader isr;
		isr = new InputStreamReader(socket.getInputStream());
		br = new BufferedReader(isr);
		
		headMap = new HashMap<>();
		bodyMap = new HashMap<>();
	}

	//����������Ϣ����ִ���
	//	�������󵽵���Դ��
	public String receiveRequestMsg() throws IOException {
		//1. ��ȡ������ ������
		String line = br.readLine();
		if(line == null)
			return null;
		doLine(line);
		
		//2. ��ȡ����ͷ ������
		while(true) {
			line = br.readLine();
			//�����ȡ�����У������ͷ��Ϣ��ȡ���
			if("".equals(line))
				break;
			//���ͷ��Ϣ������map����
			String[] arr = line.split(": ");
			headMap.put(arr[0], arr[1]);
		}
		
		//3. ��ȡ����������
		doBody();
		
		return resName;
	}

	//��ȡ����������
	private void doBody() throws IOException {
		//�����GET����������Ϊ�գ�����Ҫ��ȡ
		if("GET".equals(method)) {
			//�鿴 ��Դ���Ƿ�����ύ�����ݣ��������ȡ
			if(resName.contains("?")) {
				//����ַ������д���
				String[] arr = resName.split("[?]");
				resName = arr[0];
				
				//��������������
				parseBodyStr(arr[1]);
			}
		}else if("POST".equals(method)) {
			//��������������ݾͶ�ȡ
			if(br.ready() == false) 
				return;
			char[] buff = new char[1024];
			int len = br.read(buff);
			String bodyStr = new String(buff,0,len);
			parseBodyStr(bodyStr);
		}
		
	}

	//���������� �ַ���
	//	user=zs&ps=12345
	//	user=&ps=
	private void parseBodyStr(String bodyStr) {
		String[] arr = bodyStr.split("&");
		//����ÿ����Ա���� key=value
		for (String s : arr) {
			String[] split = s.split("=");
			//System.out.println("size: " + split.length);
			if(split.length == 2)
				bodyMap.put(split[0], split[1]);
			else
				bodyMap.put(split[0], null);
		}
	}

	//���������
	private void doLine(String line) {
		String arr[] = line.split(" ");
		method = arr[0];
		resName = arr[1];
		httpVer = arr[2];
	}

	public String getMethod() {
		return method;
	}

	public String getResName() {
		return resName;
	}

	public String getHttpVer() {
		return httpVer;
	}

	public Map<String, String> getHeadMap() {
		return headMap;
	}

	public Map<String, String> getBodyMap() {
		return bodyMap;
	}
}
