package com.zb.util;

import java.io.InputStream;
import java.util.Properties;

/**
 * ��������: ������һ�������࣬ר�Ÿ��������ļ����ݶ�ȡ����
 * @author 	briup
 * @date	2019.6.4 am
 * @version v1.0
 * @Company �������
 * @copyRight	���ļ����������������
 */
public class PropFinder {
	//������ ֻ�ṩ˽�й�����
	private PropFinder() {}
	
	/**
	 * �����൥������
	 */
	private static Properties prop;
	
	//��̬����飬��������� ���������ļ�
	static {
		prop = new Properties();
		try {
			//��ʽ1: ֱ��ͨ�� ·�� ��ȡ��Ӧ�ļ���
			//prop.load(new FileInputStream(new File("src/config.properties")));
			
			//��ʽ2: �������ļ���Դ���һ��
			InputStream is = 
					PropFinder.class.getResourceAsStream("config.properties");
			prop.load(is);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * �÷���Ϊ�ù�����Ψһ���⹫���ӿڣ�������ȡ�����ļ�����
	 * @param key �����ļ���
	 * @return String ����ֵΪ�����ļ�����洢ֵ
	 */
	public static String getProp(String key) {
		String value = prop.getProperty(key);
		return value;
	}
}
