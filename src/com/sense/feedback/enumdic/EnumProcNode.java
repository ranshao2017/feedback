package com.sense.feedback.enumdic;

import java.util.EnumSet;

/**
 * 整车调试状态
 */
public enum EnumProcNode{
	
	jc("0", "接车"),
	ts("1", "调试"), 
	gz("2", "故障排除"),
	sy("3", "送验"),
	rk("4", "入库");

	// 编码 及名字
	private final String code;
	private final String label;

	/**
	 * 构造函数
	 * 
	 * @param code
	 */
	EnumProcNode(String code ,String label) {
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
		for(EnumProcNode s : EnumSet.allOf(EnumProcNode.class)){
	         if(s.code.equals(code))
	        	 return s.label;
	    }
		return null;
	}
}
