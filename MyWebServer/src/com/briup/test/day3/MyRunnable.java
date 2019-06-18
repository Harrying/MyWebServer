package com.briup.test.day3;

import java.net.Socket;
import java.util.Map;
import java.util.Map.Entry;

public class MyRunnable implements Runnable {
	//添加客户端  套接字 成员
	private Socket socket;
    //重写构造器，将客户端套接字 初始化
	public MyRunnable(Socket socket) {
		this.socket = socket;
	}
		@Override
	public void run() {
		// TODO Auto-generated method stub
		String url;
		try {
			RequestImp r = new RequestImp(socket);
			r.receiveRequestMsg();
			//1.解析从客户端 接收到的 数据,返回资源名
			url = r.getResName();			
			Map<String, String> headMap =r.getHeadMap();
			Map<String, String> bodyMap = r.getBodyMap();
			//2.输出获取的请求行信息
			System.out.println("资源名: "+url);
			System.out.println("请求方式: "+r.getMethod());
			System.out.println("协议版本: " +r.getHttpVer());
			//3.输出获取的请求头信息
			System.out.println("请求头内容：");
			for (Entry<String, String> entry : headMap.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				System.out.println(key + ":" + value);
			}
			//4.输出获取的请求体信息
			System.out.println("请求体内容：");
			for (Entry<String, String> entry : bodyMap.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				System.out.println(key + ":" + value);
			}
	
			//2.将响应信息(资源文件 包装到HTTP协议)
			//发送给 客户端
			ResponseImp res = new ResponseImp(socket,r);
			res.sendResponse(url);
			System.out.println("***********访问结束分割线*************");
			socket.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}

}
