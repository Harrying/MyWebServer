package com.briup.test.day3;

import java.io.InputStream;
import java.util.Properties;

/**
 * 
 * @author  HaiRui
 * @date    2019.6.0am
 * @version v1.0
 */
public class PropFinder {
	//������ ֻ�ṩ˽�й�����
	private PropFinder() {}
	
	/**
	 * �����൥������
	 */
	private static Properties prop;
	
	//��̬����� ������ļ��� ���������ļ�
	static {
		prop = new Properties();
		//1.��ʽһ��ֱ��ͨ��·����ȡ��Ӧ���ļ���
		//prop.load(new FileInputStream(new File("�����ļ�·��")));
		
		//2.��ʽ�����������ļ���Դ�����һ��
		InputStream is = PropFinder.class.getResourceAsStream("login.properties");
		try {
			prop.load(is);
			is.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * �÷���Ϊ�������Ψһ����ӿڣ�������ȡ�����ļ�����
	 * @param key �����ļ���
	 * @return String ����ֵΪ�����ļ����崢��ֵ
	 */
	public static String getProp(String key) {
		String value = prop.getProperty(key);
		return value;
	}
	
	
}
