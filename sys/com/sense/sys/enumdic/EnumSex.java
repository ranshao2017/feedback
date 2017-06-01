package com.sense.sys.enumdic;

import java.util.EnumSet;

/**
 * 性别
 */
public enum EnumSex{
	
	male("1","男"), 
	famale("2","女");

	// 编码 及名字
	private final String code;
	private final String label;

	/**
	 * 构造函数
	 * 
	 * @param code
	 */
	EnumSex(String code ,String label) {
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
		for(EnumSex s : EnumSet.allOf(EnumSex.class)){
	         if(s.code.equals(code))
	        	 return s.label;
	    }
		return null;
	}
}
