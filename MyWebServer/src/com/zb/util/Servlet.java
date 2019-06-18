package com.zb.util;

import java.io.IOException;

/*
 * 动态资源类 公共接口
 */
public interface Servlet {
	public void service(RequestImpl req, ResponceImpl res) throws Exception;
}
