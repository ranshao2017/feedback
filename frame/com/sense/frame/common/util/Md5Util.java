package com.sense.frame.common.util;

import java.security.MessageDigest;

public class Md5Util {

	public static String md5(String original) throws Exception {
		MessageDigest md5Hander = MessageDigest.getInstance("MD5");
		
		md5Hander.update(original.getBytes());
		return byte2hex(md5Hander.digest());
	}
	
	private static String byte2hex(byte[] b) { 
		String hs=""; 
		String stmp=""; 
		for (int n=0;n<b.length;n++) { 
			stmp=(java.lang.Integer.toHexString(b[n] & 0XFF)); 
			if (stmp.length()==1) hs=hs+"0"+stmp; 
			else hs=hs+stmp;
		} 
		return hs.toUpperCase(); 
	}

	public static String toHex(byte[] a) {
		StringBuilder sb = new StringBuilder(a.length * 2);
		for (int i = 0; i < a.length; i++) {
			sb.append(Character.forDigit((a[i] & 0xf0) >> 4, 16));
			sb.append(Character.forDigit(a[i] & 0x0f, 16));
		}
		return sb.toString();
	}
	
}
