package com.zb.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/*
 * 请求类
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

	//接收请求信息并拆分处理
	//	返回请求到的资源名
	public String receiveRequestMsg() throws IOException {
		//1. 读取请求行 并处理
		String line = br.readLine();
		if(line == null)
			return null;
		doLine(line);
		
		//2. 读取请求头 并处理
		while(true) {
			line = br.readLine();
			//如果读取到空行，则表明头信息读取完成
			if("".equals(line))
				break;
			//拆分头信息，存入map集合
			String[] arr = line.split(": ");
			headMap.put(arr[0], arr[1]);
		}
		
		//3. 读取请求体内容
		doBody();
		
		return resName;
	}

	//读取请求体内容
	private void doBody() throws IOException {
		//如果是GET请求，请求体为空，不需要读取
		if("GET".equals(method)) {
			//查看 资源中是否存在提交的数据，如果有提取
			if(resName.contains("?")) {
				//拆分字符串进行处理
				String[] arr = resName.split("[?]");
				resName = arr[0];
				
				//解析请求体内容
				parseBodyStr(arr[1]);
			}
		}else if("POST".equals(method)) {
			//请求体如果有内容就读取
			if(br.ready() == false) 
				return;
			char[] buff = new char[1024];
			int len = br.read(buff);
			String bodyStr = new String(buff,0,len);
			parseBodyStr(bodyStr);
		}
		
	}

	//解析请求体 字符串
	//	user=zs&ps=12345
	//	user=&ps=
	private void parseBodyStr(String bodyStr) {
		String[] arr = bodyStr.split("&");
		//数组每个成员都是 key=value
		for (String s : arr) {
			String[] split = s.split("=");
			//System.out.println("size: " + split.length);
			if(split.length == 2)
				bodyMap.put(split[0], split[1]);
			else
				bodyMap.put(split[0], null);
		}
	}

	//拆分请求行
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
