package com.sense.feedback.enumdic;

import java.util.EnumSet;

/**
 * 是否
 */
public enum EnumYesNo{
	
	yes("1","是"), 
	no("0","否");

	// 编码 及名字
	private final String code;
	private final String label;

	/**
	 * 构造函数
	 * 
	 * @param code
	 */
	EnumYesNo(String code ,String label) {
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
		for(EnumYesNo s : EnumSet.allOf(EnumYesNo.class)){
	         if(s.code.equals(code))
	        	 return s.label;
	    }
		return null;
	}
	public static String getCodeByLabel(String label) {
		for(EnumYesNo s : EnumSet.allOf(EnumYesNo.class)){
	         if(s.label.equals(label))
	        	 return s.code;
	    }
		return null;
	}
}
