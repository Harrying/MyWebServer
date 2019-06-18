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
 * 1.搭建一个TCP服务器
 * 2.接收客户端连接，连接建立成功后
 * 3.接收客户端发送过来的所有数据，输出到控制台
 */
public class ServerMain {
	public static void main(String[] args) {
		
		try {
			//1.搭建服务器
			ServerSocket server = new ServerSocket(9999);
			System.out.println("服务器已经启动...");
			
			//2.接收客户端连接  http://127.0.0.1:9999
			Socket socket = server.accept();
			if(socket != null)
				System.out.println("连接成功,socket: "+socket);
		
			//3.接收客户端发送过来的数据,输出
			InputStream in = socket.getInputStream();
			//字节流   数据以字节为单位进行传输  
			InputStreamReader isr = new InputStreamReader(in);
			BufferedReader br = new BufferedReader(isr);
		
			String line = null;
			//3.1.记录 第一行数据(请求行)
			String str = br.readLine();
			System.out.println(str);
			
			//3.2.读取 剩余的 请求信息 EOF  socket
			//"" == null
			while(!"".equals(line = br.readLine())) {
				System.out.println(line);
			}
			System.out.println("after readLine...");
			
			//读取body内部数据
			char[] barr = new char[1024];
			int bsize = 0;
			if(br.ready()) {
				bsize = br.read(barr);
				String s = new String(barr,0,bsize);
				System.out.println("读取到body: "+s);
			}
			System.out.println("请求信息全部读取完成!");
			
			//4.解析 请求行  获取 请求资源名
			// GET /a.txt HTTP/1.1
			//按照 空白字符 拆分字符串
			String[] array = str.split(" ");
			System.out.println(array);
			
			for (int i = 0;i < array.length;i++) {
				System.out.println("array["+i+"]: "+array[i]);
			}
			
			//String name = array[1].substring(1);
			
			// 判断申请的资源文件是否存在 E:\\myServer\
			File file = new File("D:\\MyServer",array[1]);
			boolean flag = file.exists();
			System.out.println("file: " + file + "flag: " + flag);
			
			//准备流(服务器 发送数据给 浏览器)
			OutputStream os = socket.getOutputStream();
			PrintStream ps = new PrintStream(os);
			
			//准备响应行
			String responseLine = null;
			
			//针对文件是否存在 ,准备响应行
			//如果127.0.0.1:9999  flag==true  file:welcome.html
			if(flag) { 
				responseLine = "HTTP/1.1 200 OK";
				//判断是否是 空白资源  /
				if("/".equals(array[1])) {
					file = new File("D://MyServer","welcome.html");
				}
			} else {
				responseLine = "HTTP/1.1 404 NotFound";
				//文件不存在，重写赋值error.html
				file = new File("D://MyServer","error.html");
			}
			
			//按照http协议发送
			//a. 发送响应行
			ps.println(responseLine);
			//b. 消息头(可以为空  不写)
			//c. 空行
			ps.println();
			
			//d. 消息体(文件具体内容)
			//额外准备文件流，提取file中的数据
			FileInputStream fis = new FileInputStream(file);
			BufferedInputStream bis = 
					new BufferedInputStream(fis);
			byte[] arr = new byte[1024];
			int size = 0;
			
			while((size = bis.read(arr)) != -1) {
				//将文件中的数据 发送给浏览器
				
				ps.write(arr,0,size);
				
				/*String s = new String(arr,0,size);
				//1111 1111 1111 1111
				// ?
				//浏览器: ? ==>  0111 1111 0000 0000
				ps.print(s);*/
			}
			
			//关闭相关资源
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
