package com.briup.test.day2;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;


public class MyRunnable2 implements Runnable {
	
	//添加客户端 套接字 成员
	private Socket socket;
	
	//重写构造方法 初始化客户端套接字
	public  MyRunnable2(Socket socket) {
		this.socket = socket;
	}
	
	@Override
	public void run() {
		//获取浏览器的请求内容
		String url;
		try {
			//1.解析从客户端 接收到的 数据,返回资源名
			RequestHttp rh = new RequestHttp(socket);
			rh.splReques();
			//获得请求方式
			System.out.println("请求方式："+ rh.getMethod());
			//得到资源名
			url = rh.getResName();
			System.out.println("资源名："+ url);
			//获得http版本
			System.out.println("http版本："+ rh.getHttpVer());
			//获得请求头
			HashMap<String, String> headMap = rh.getHeadMap();
			Set<Entry<String, String>> entry = headMap.entrySet();
			System.out.println("请求头为：");
			for (Entry<String, String> entry2 : entry) {
				System.out.println(entry2.getKey()+ " " + entry2.getValue());
			}
			//获得请求体
			HashMap<String, String> bodyMap = rh.getBodyMap();
			if(bodyMap == null) {
				System.out.println("请求体为空！");
			}else {
				Set<Entry<String, String>> entry1 = bodyMap.entrySet();
				System.out.println("请求体为：");
				for (Entry<String, String> entry2 : entry1) {
					System.out.println(entry2.getKey()+ " " + entry2.getValue());
				}
			}
			
			//2.将响应信息发送给 客户端
			ResponseHttp rph = new ResponseHttp(url, socket);
			rph.sendRespon();
			socket.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
