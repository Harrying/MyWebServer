package com.zb.util;

import java.io.IOException;

/*
 * ��̬��Դ�� �����ӿ�
 */
public interface Servlet {
	public void service(RequestImpl req, ResponceImpl res) throws Exception;
}
