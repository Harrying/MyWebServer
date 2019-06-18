package com.briup.test.day3;

import java.util.Map;

public class ServletLogin {
	//private Socket socket;
	
	public void service(RequestImp req,ResponseImp res) throws Exception {
		Map<String, String> bodyMap = req.getBodyMap();

		String name = bodyMap.get("name");
		String password = bodyMap.get("pswd");
		String prop = PropFinder.getProp("name");
		String prop2 = PropFinder.getProp("password");
		if(prop.equals(name)&&prop2.equals(password)) {
			res.sendResponse(PropFinder.getProp("success"));
		}else
		{
			res.sendResponse(PropFinder.getProp("fail"));
		}
		}
	
}
