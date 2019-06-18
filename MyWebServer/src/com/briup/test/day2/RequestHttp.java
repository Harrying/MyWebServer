package com.briup.test.day2;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;

public class RequestHttp {
	/**
	 * 属性：请求方式、资源名、http版本、请求头、请求体
	 */
	private String method;
	private String resName;
	private String httpVer;
	private HashMap<String, String> headMap = new HashMap<String, String>();
	private HashMap<String, String> bodyMap = new HashMap<String, String>();
	private Socket socket;
	
	//封装Io
	
	public RequestHttp(Socket socket) {
		this.socket = socket;
	}
	
	
	public String getMethod() throws Exception {
		return method;
	}
	
	public String getResName() throws Exception {
		return resName;
	}
	
	public String getHttpVer() throws Exception {
		return httpVer;
	}

	
	public HashMap<String, String> getHeadMap() throws Exception {
		return headMap;
	}
	
	public HashMap<String, String> getBodyMap() throws Exception {
		return bodyMap;
	}
	public Socket getSocket() {
		return socket;
	}
	
	public void splReques() throws Exception {
		InputStream is = socket.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String line = br.readLine();	
		String[] arr = line.split(" ");
		
		//拆分获得请求方式
		this.method = arr[0];
		
		String res = arr[1].toString();
		//拆分获得资源名
		String[] arr1 = null;
		if(res.contains("?")) {
			arr1 = res.split("[?]");
			this.resName = arr1[0];
			
		}else {
			this.resName = arr[1];
		}
		
		//拆分获得http版本
		this.httpVer = arr[2];
		
		//拆分获得请求头
		String line2;
		while(!"".equals(line2 = br.readLine())) {
			String[] arr0 = line2.split(":", 2);
			this.headMap.put(arr0[0], arr0[1].trim());
			}
		
		
		//拆分获得请求体
		if("GET".equals(arr[0])) {
			String[] arr3 = arr[1].toString().split("[?]");
			if(arr3.length == 1) {
				this.bodyMap = null;
			}else {
				String[] arr4 = arr3[1].toString().split("&");
				for (int i = 0; i < arr4.length; i++) {
					String[] arr5 = arr4[i].toString().split("=");
					this.bodyMap.put(arr5[0], arr5[1]);
				}	
			}
			
		}else {
			char[] barr = new char[1024];
			int bsize = 0;
			if(br.ready()) {
				bsize = br.read(barr);
				String str = new String(barr,0,bsize);
				String[] arr3 = str.split("&");
				for (int i = 0; i < arr3.length; i++) {
					String[] arr4 = arr3[i].toString().split("=");
					this.bodyMap.put(arr4[0], arr4[1]);
				}
			}
		}
	}

}
