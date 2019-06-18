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
	//工具类 只提供私有构造器
	private PropFinder() {}
	
	/**
	 * 配置类单例对象
	 */
	private static Properties prop;
	
	//静态代码块 随着类的加载 加载配置文件
	static {
		prop = new Properties();
		//1.方式一：直接通过路劲获取对应的文件流
		//prop.load(new FileInputStream(new File("配置文件路径")));
		
		//2.方式二：把配置文件和源码放在一起
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
	 * 该方法为工具类的唯一对外接口，用来获取配置文件内容
	 * @param key 配置文件键
	 * @return String 返回值为配置文件具体储存值
	 */
	public static String getProp(String key) {
		String value = prop.getProperty(key);
		return value;
	}
	
	
}
