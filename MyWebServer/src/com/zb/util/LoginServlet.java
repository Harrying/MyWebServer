package com.zb.util;

import java.io.IOException;
import java.util.Map;

/*
 * 具体登录动态资源类
 */
public class LoginServlet implements Servlet {
	@Override
	public void service(RequestImpl req, ResponceImpl res) throws Exception {
		System.out.println("in service...");
		
		Map<String, String> bodyMap = req.getBodyMap();
		String name = bodyMap.get("name");
		String passwd = bodyMap.get("passwd");
		
		if("zs".equals(name) && "12345".equals(passwd)) {
			res.sendResource("success.html");
		}else {
			res.sendResource("fail.html");
		}
	}
}
