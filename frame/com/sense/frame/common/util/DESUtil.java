package com.sense.frame.common.util;

import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
/**
 * 
 * 类描述
 * DES加密解密工具类
 * @author rbn
 * @version 1.0
 * 创建时间 2013-10-27
 */
public class DESUtil{
	
	private static Key defaultKey;
	
	static {
		try {
			defaultKey = getKey("icfos");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/** 
	 * 方法简介.
	 * DES加密算法
	 * @throws 异常说明   发生条件
	 * @author 
	 * @date 创建时间 2013-10-27
	 * @since V1.0
	 */
	public static byte[] encoder(byte[] encoderByte,Key key) throws Exception{
		Cipher cipher = Cipher.getInstance("DES");
		cipher.init(Cipher.ENCRYPT_MODE, key);
		return cipher.doFinal(encoderByte);		
	}
	
	public static byte[] encoder(byte[] encoderByte) throws Exception{
		Cipher cipher = Cipher.getInstance("DES");
		cipher.init(Cipher.ENCRYPT_MODE, defaultKey);
		return cipher.doFinal(encoderByte);		
	}
	
	/** 
	 * 方法简介.
	 * DES解密算法
	 * @throws 异常说明   发生条件
	 * @author 
	 * @date 创建时间 2013-10-27
	 * @since V1.0
	 */
	public static byte[] decoder(byte[] encoderByte,Key key) throws Exception{
		Cipher cipher = Cipher.getInstance("DES");
		cipher.init(Cipher.DECRYPT_MODE, key);
		return cipher.doFinal(encoderByte);
	}
	
	public static byte[] decoder(byte[] encoderByte) throws Exception{
		Cipher cipher = Cipher.getInstance("DES");
		cipher.init(Cipher.DECRYPT_MODE, defaultKey);
		return cipher.doFinal(encoderByte);		
	}
	
	/** 
	 * 方法简介.
	 * 根据传进来的字符串，获取DES密钥对象
	 * @throws 异常说明   发生条件
	 * @author 
	 * @date 创建时间 2013-10-27
	 * @since V1.0
	 */
	public static Key getKey(String keyString)throws Exception{		
		SecureRandom random = new SecureRandom(keyString.getBytes());
		KeyGenerator keyGenerator = KeyGenerator.getInstance("DES");		
		keyGenerator.init(random);		
		Key key = keyGenerator.generateKey();
		return key;		
	}

	public static String byte2hex(byte[] b) { 
		String hs=""; 
		String stmp=""; 
		for (int n=0;n<b.length;n++) { 
			stmp=(java.lang.Integer.toHexString(b[n] & 0XFF)); 
			if (stmp.length()==1) hs=hs+"0"+stmp; 
			else hs=hs+stmp;
		} 
		return hs.toUpperCase(); 
	}
	
	public static String encode(String orignal) throws Exception {
		return byte2hex(encoder(orignal.getBytes()));
	}
	
	public static String decode(String clipher) throws Exception {
		return null;
	}
}
