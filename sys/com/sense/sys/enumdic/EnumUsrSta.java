package com.sense.sys.enumdic;

import java.util.EnumSet;

/**
 * 用户状态
 */
public enum EnumUsrSta{
	normal("1","正常"), 
	illegal("2","注销");

	// 编码 及名字
	private final String code;
	private final String label;

	/**
	 * 构造函数
	 * 
	 * @param code
	 */
	EnumUsrSta(String code ,String label) {
		this.code = code;
		this.label = label ;
	}

	public String getLabel() {
		return label;
	}
	
	public String getCode() {
		return code;
	}
	
	public static String getLabelByCode(String code) {
		for(EnumUsrSta s : EnumSet.allOf(EnumUsrSta.class)){
	         if(s.code.equals(code))
	        	 return s.label;
	    }
		return null;
	}
}
