package com.zb.util;

import java.io.InputStream;
import java.util.Properties;

/**
 * 功能描述: 该类是一个工具类，专门负责配置文件内容读取解析
 * @author 	briup
 * @date	2019.6.4 am
 * @version v1.0
 * @Company 杰普软件
 * @copyRight	本文件归属杰普软件所有
 */
public class PropFinder {
	//工具类 只提供私有构造器
	private PropFinder() {}
	
	/**
	 * 配置类单例对象
	 */
	private static Properties prop;
	
	//静态代码块，随着类加载 加载配置文件
	static {
		prop = new Properties();
		try {
			//方式1: 直接通过 路径 获取对应文件流
			//prop.load(new FileInputStream(new File("src/config.properties")));
			
			//方式2: 把配置文件和源码放一起
			InputStream is = 
					PropFinder.class.getResourceAsStream("config.properties");
			prop.load(is);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 该方法为该工具类唯一对外公共接口，用来获取配置文件内容
	 * @param key 配置文件键
	 * @return String 返回值为配置文件具体存储值
	 */
	public static String getProp(String key) {
		String value = prop.getProperty(key);
		return value;
	}
}
