package com.sense.feedback.enumdic;

import java.util.EnumSet;

/**
 * 问题状态
 */
public enum EnumProblemSta{
	
	init("0", "未回复"),
	replyed("1","已回复"), 
	manclose("2","人工关闭"),
	autoclose("3","自动关闭");

	// 编码 及名字
	private final String code;
	private final String label;

	/**
	 * 构造函数
	 * 
	 * @param code
	 */
	EnumProblemSta(String code ,String label) {
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
		for(EnumProblemSta s : EnumSet.allOf(EnumProblemSta.class)){
	         if(s.code.equals(code))
	        	 return s.label;
	    }
		return null;
	}
}
